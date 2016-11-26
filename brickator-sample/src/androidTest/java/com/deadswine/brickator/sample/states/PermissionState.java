package com.deadswine.brickator.sample.states;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.content.ContextCompat;

import com.deadswine.brickator.library.Brickator;
import com.deadswine.brickator.library.BrickatorState;
import com.deadswine.brickator.library.annotations.LeadsTo;

import static junit.framework.Assert.assertEquals;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class PermissionState extends BrickatorState {
    public String TAG = getClass().getSimpleName();

    public static String getActivityName() {
        return "com.deadswine.brickator.sample.Activities.ScreenShotPermissionActivity"; //TODO avoid hardcoded string, for example use get package
    }

    public static UiObject getAllowButton() {

        UiObject hamburger = Brickator.getInstance().getDevice().findObject(new UiSelector().text("Allow"));

        return hamburger;

    }

    @Override
    public void stateReached() {
        Brickator.print(TAG + " stateReached()");
    }

    @LeadsTo(BackgroundState.class)
    public void goToBackgroundState() {
        Brickator.getInstance().getDevice().pressHome();
    }

    public static void checkScreenShotsPermission() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        Brickator.print("app package is: " + appContext.getApplicationContext().getPackageName());

        int permission = ContextCompat.checkSelfPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_DENIED) {
            Brickator.getInstance().goToStateSimple(PermissionState.class);

            UiObject allowButton = PermissionState.getAllowButton();

            try {
                allowButton.click();
            } catch (UiObjectNotFoundException e) {
                throw new UnknownError("Allow button for storage permission don't exists");
            }

        }

        permission = ContextCompat.checkSelfPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        assertEquals(PackageManager.PERMISSION_GRANTED, permission);

        SystemClock.sleep(100);
    }
}
