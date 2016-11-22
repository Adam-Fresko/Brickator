package com.deadswine.brickator.library;

import android.os.Bundle;
import android.os.Environment;
import android.support.test.uiautomator.UiDevice;
import android.text.TextUtils;
import android.util.Log;

import com.deadswine.brickator.library.states.UnknownState;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class Brickator {
    public static String TAG = "Brickator";
    private static Brickator ourInstance = new Brickator();
    private UiDevice mDevice;
    private PathFinder mPathFinder;

    private boolean isInitialized;

    /**
     * Your app package name, required to launch app
     */
    private String mAppPackage;

    private String mCurrentTestClassName = "unknown_test_class";
    private String mCurrentTestName = "unknown_test";
    private int mCurrentScreenShot = 0;

    private StateResolver mStateResolver;
    private ArrayList<Class<? extends BrickatorState>> mStatesList;

    private Brickator() {
        mDevice = UiDevice.getInstance(getInstrumentation());
        mPathFinder = new PathFinder();

        mStatesList = new ArrayList<>();
        mStatesList.add(UnknownState.class);
    }

    public static Brickator getInstance() {
        return ourInstance;
    }

    public static void print(String text) {
        Bundle bundle = new Bundle();
        bundle.putString(TAG, text);
        getInstrumentation().sendStatus(0, bundle);

        Log.d(TAG, text);
    }

    public void initialize(String appPackage, StateResolver stateResolver) {

        if (TextUtils.isEmpty(appPackage)) {
            throw new IllegalArgumentException("appPackage cannot be null");
        }

        if (stateResolver == null) {
            throw new IllegalArgumentException("stateResolver cannot be null");
        }

        mAppPackage = appPackage;
        mStateResolver = stateResolver;
        isInitialized = true;

    }

    public void registerState(Class<? extends BrickatorState> newState) {
        print("Adding new state: " + newState);
        mStatesList.add(newState);
    }

    /**
     * Remember that test execution is random!
     * <Br>
     * Therefore if one state depends on another one test might fail using this method.
     * <BR>
     * The solution for this is to use single test in class and separate methods for each state
     *
     * @param desiredState
     * @param <T>
     * @return
     */
    public <T extends BrickatorState> BrickatorState goToStateSimple(Class<T> desiredState) {

        print("goToStateSimple: " + desiredState.getSimpleName());

        Class<? extends BrickatorState> currentState = resolveCurrentState();

        Method desiredMethod = mPathFinder.findMethodLeadingToDesiredState(currentState, desiredState);
        if (desiredMethod == null) {
            throw new IllegalStateException("Didn't found method in: " + currentState + " that can lead to: " + desiredState);
        }
        BrickatorState foo = mPathFinder.invokeLeadsToMethod(currentState, desiredMethod);

        return foo;
    }

    /**
     * Works by finding path that is leading from current state to the desired state
     *
     * @param desiredState
     * @param <T>
     * @return
     */
    public <T extends BrickatorState> BrickatorState goToStateAdvanced(Class<T> desiredState) {
        print("goToStateAdvanced: " + desiredState.getSimpleName());

        Class<? extends BrickatorState> currentState = resolveCurrentState();

        Method desiredMethod = null;
        BrickatorState foo = null;

        // if the desired state == currentState we return current state class
        if (currentState.getSimpleName().equals(desiredState.getSimpleName())) {
            return mPathFinder.invokeLeadsToMethod(currentState, null);
        }

        ArrayList<Class<? extends BrickatorState>> returnedPath = mPathFinder.findPathv2(getStatesList(), currentState, desiredState);

        if (returnedPath == null || returnedPath.size() == 0) {
            throw new IllegalStateException("The returned path was null or with size of 0");
        }

        for (int i = 0; i < returnedPath.size(); i++) {
            currentState = resolveCurrentState();
            desiredMethod = mPathFinder.findMethodLeadingToDesiredState(currentState, returnedPath.get(i));
            if (desiredMethod == null) {
                throw new IllegalStateException("Didn't found method in: " + currentState + " that can lead to: " + desiredState);
            }
            foo = mPathFinder.invokeLeadsToMethod(currentState, desiredMethod);
        }

        return foo;
    }

    /**
     * Finds whats current state is by calling method "resolveCurrentState" in your implementation of StateResolver
     *
     * @return BrickatorState
     * @see com.deadswine.brickator.library.StateResolver#resolveCurrentState(String, String, UiDevice)
     */
    public Class<? extends BrickatorState> resolveCurrentState() {

        checkIfInitialized();

        String[] focusedWindow = BrickatorUtilities.getCurrentFocusedWindow();

        String currentPackage = focusedWindow[0]; //TODO

        String currentActivity = focusedWindow[1]; //TODO

        Class<? extends BrickatorState> resolvedState = mStateResolver.resolveCurrentState(currentPackage, currentActivity, getDevice());

        if (resolvedState == null) {
            throw new IllegalStateException("The resolved state is null: " + resolvedState);
        }

        boolean isStateRegistered = false;

        print("Starting checking if class " + resolvedState.getSimpleName() + " is registered");

        for (Class<? extends BrickatorState> state : mStatesList) {

            if (resolvedState.getSimpleName().equals(state.getSimpleName())) {
                isStateRegistered = true;
                break;
            }
        }

        if (!isStateRegistered) {
            throw new IllegalStateException("Class " + resolvedState.getClass().getSimpleName() + " has not been registered");
        }

        if (resolvedState.getSimpleName().equals(UnknownState.class.getSimpleName())) {
            throw new IllegalStateException("Resolved current state is unknown! Did you properly resolved state in your implementation of State Resolver? " + resolvedState);
        }

        print("Resolved current state: " + resolvedState.getSimpleName());

        return resolvedState;
    }

    public String getScreenShotDirectory() {
        return String.format("%s/%s", Environment.getExternalStorageDirectory(), "test-screenshots");
    }

    /**
     * On android >= 6 This method will work only if we successfully granted permission to write in to external storage
     *
     * @param screenShotName
     * @return
     */
    public String takeScreenShot(String screenShotName) {
        print("takeScreenShot: " + screenShotName);
        String screenShotPath = null;
        mCurrentScreenShot += 1;

        // check if storage is writable // this method kind of lies to us on android 6 and above
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            print("External storage is writable");
        } else {
            print("External storage is NOT writable");
        }

        File theDir = new File(getScreenShotDirectory());
        if (!theDir.exists()) {

            if (theDir.mkdir()) {
                print("Created screen shot directory at: " + theDir.getAbsolutePath());
            } else {
                throw new Error("Failed to created screen shot directory at: " + theDir.getAbsolutePath());
            }
        }

        if (mDevice.takeScreenshot(new File(String.format("%s/%s", getScreenShotDirectory(), getCurrentTestClassName() + "@" + getCurrentTestName() + "@" + mCurrentScreenShot + "@" + screenShotName + ".jpg")))) {
            print("Taken Screenshot: " + getScreenShotDirectory());
        } else {
            print("Failed to Take ScreenShot at: " + getScreenShotDirectory());
        }

        return screenShotPath;
    }

    private void checkIfInitialized() {
        if (isInitialized == false) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " has not been initialized");
        }
    }

    public UiDevice getDevice() {
        return mDevice;
    }

    public ArrayList<Class<? extends BrickatorState>> getStatesList() {
        return mStatesList;
    }

    public String getAppPackage() {
        return mAppPackage;
    }

    public String getCurrentTestName() {
        return mCurrentTestName;
    }

    public String getCurrentTestClassName() {
        return mCurrentTestClassName;
    }

    public void setCurrentTestName(String currentTestClassName, String mCurrentTestName) {
        this.mCurrentTestName = mCurrentTestName;
        mCurrentTestClassName = currentTestClassName;
        mCurrentScreenShot = 0;
    }
}
