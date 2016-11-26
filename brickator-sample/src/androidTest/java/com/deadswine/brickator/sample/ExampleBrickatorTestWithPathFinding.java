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
import com.deadswine.brickator.library.BrickatorBaseTest;
import com.deadswine.brickator.sample.resolvers.Resolver;
import com.deadswine.brickator.sample.states.BackgroundState;
import com.deadswine.brickator.sample.states.GalleryState;
import com.deadswine.brickator.sample.states.MainMenuState;
import com.deadswine.brickator.sample.states.MainState;
import com.deadswine.brickator.sample.states.PermissionState;
import com.deadswine.brickator.sample.states.SettingsState;
import com.deadswine.brickator.sample.states.UninstalledState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 * <br><br>
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleBrickatorTestWithPathFinding extends BrickatorBaseTest {

    {
        setTag(this.getClass().getSimpleName()); // set tag for screenshots // otherwise screenshots wont be created
    }

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



        SystemClock.sleep(5000);
        Brickator.print("STAGE 3333333333");

        // #3 grant permission for screenshots
        PermissionState.checkScreenShotsPermission();


        super.beforeTest(); // call super method so we have screenshot in before automatically or handle it yourself
    }

    @Test
    public void goToStatesWithPathTracingExample() throws Exception {

        Brickator.getInstance().goToStateAdvanced(GalleryState.class);
        Brickator.getInstance().takeScreenShot("goToStateAdvanced-GalleryState");

//        Context appContext = InstrumentationRegistry.getTargetContext();
//        int permission = ContextCompat.checkSelfPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        assertEquals(PackageManager.PERMISSION_DENIED, permission);

        Brickator.getInstance().goToStateAdvanced(SettingsState.class);
        Brickator.getInstance().takeScreenShot("goToStateAdvanced-SettingsState");

    }

}
