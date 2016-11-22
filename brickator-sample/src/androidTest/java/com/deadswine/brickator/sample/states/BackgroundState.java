package com.deadswine.brickator.sample.states;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Until;

import com.deadswine.brickator.library.Brickator;
import com.deadswine.brickator.library.BrickatorState;
import com.deadswine.brickator.library.BrickatorUtilities;
import com.deadswine.brickator.library.ScreenShotPermissionActivity;
import com.deadswine.brickator.library.annotations.LeadsTo;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class BackgroundState extends BrickatorState {

    public String TAG = getClass().getSimpleName();

    @Override
    public void stateReached() {

    }

    @LeadsTo(MainState.class)
    public void goToMainState() {
        Brickator.print(TAG + " goToMainState()");
        BrickatorUtilities.launchApp("com.deadswine.brickator.sample", "com.deadswine.brickator.sample.Activities.MainActivity");

    }

    @LeadsTo(PermissionState.class)
    public void goToPermissionsActivityState() {
        Brickator.print(TAG + " goToPermissionsActivityState()");
        Context appContext = InstrumentationRegistry.getTargetContext();

        Intent intent = new Intent(appContext, ScreenShotPermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);

        // Wait for the app to appear
        Brickator.getInstance().getDevice().wait(Until.hasObject(By.pkg(PermissionState.getActivityName()).depth(0)), 10000);

    }
}
