package com.deadswine.brickator.sample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.v4.content.ContextCompat;

import com.deadswine.brickator.library.Brickator;
import com.deadswine.brickator.sample.resolvers.Resolver;
import com.deadswine.brickator.sample.states.BackgroundState;
import com.deadswine.brickator.sample.states.GalleryState;
import com.deadswine.brickator.sample.states.MainMenuState;
import com.deadswine.brickator.sample.states.MainState;
import com.deadswine.brickator.sample.states.PermissionState;
import com.deadswine.brickator.sample.states.UninstalledState;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 * <br><br>
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleBrickatorTestBasic {
    private final String TAG = this.getClass().getSimpleName();

    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            Brickator.print("Starting test: " + description.getMethodName() + " for class: " + TAG);
            Brickator.getInstance().setCurrentTestName(TAG, description.getMethodName());
        }
    };

    @Before
    public void beforeTest() {
        // initialize automator here
        // #1 initialize
        Brickator.print("STAGE 1111111111");
        Brickator.getInstance().initialize("com.deadswine.brickator.sample", new Resolver());

        // #2 register states clasess
        Brickator.print("STAGE 2222222222");
        Brickator.getInstance().registerState(UninstalledState.class);
        Brickator.getInstance().registerState(BackgroundState.class);
        Brickator.getInstance().registerState(PermissionState.class);
        Brickator.getInstance().registerState(MainState.class);
        Brickator.getInstance().registerState(MainMenuState.class);
        Brickator.getInstance().registerState(GalleryState.class);

        SystemClock.sleep(500);
        Brickator.print("STAGE 3333333333");
        // #3 grant permission for screenshots
        Context appContext = InstrumentationRegistry.getTargetContext();
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
        SystemClock.sleep(100);
        permission = ContextCompat.checkSelfPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        assertEquals(PackageManager.PERMISSION_GRANTED, permission);

        Brickator.getInstance().takeScreenShot("BeforeTest");
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.

        Brickator.print("TEST useAppContext()");

        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.deadswine.brickator.sample", appContext.getPackageName());

        out.println("hello world");

    }

    @Test
    public void goToStatesWithoutPathTracingExample() throws Exception {
        // this test assumes you are already in background state // remember that test execution order is random, so if one state depends on another one test will fail.
        checkMainState();
        checkMainMenuState();
        checkGalleryState();
    }

    public void checkMainState() throws Exception {
        Brickator.print("TEST checkMainState()");

        Brickator.getInstance().goToStateSimple(MainState.class);

        Brickator.getInstance().takeScreenShot("checkMainState");

        assertEquals(MainState.class.getName(), Brickator.getInstance().resolveCurrentState().getName());

    }

    public void checkMainMenuState() throws Exception {
        Brickator.print("TEST checkMainMenuState()");

        Brickator.getInstance().goToStateSimple(MainMenuState.class);

        Brickator.getInstance().takeScreenShot("checkMainMenuState");

        assertEquals(MainMenuState.class.getName(), Brickator.getInstance().resolveCurrentState().getName());

    }

    public void checkGalleryState() throws Exception {
        Brickator.print("TEST checkGalleryState()");

        Brickator.getInstance().goToStateSimple(GalleryState.class);

        Brickator.getInstance().takeScreenShot("checkGalleryState");

        assertEquals(GalleryState.class.getName(), Brickator.getInstance().resolveCurrentState().getName());

    }

    @After
    public void afterTest() {
        Brickator.getInstance().takeScreenShot("AfterTest");
    }
}
