package com.deadswine.brickator.sample.states;

import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.deadswine.brickator.library.Brickator;
import com.deadswine.brickator.library.BrickatorState;
import com.deadswine.brickator.library.annotations.LeadsTo;

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

}
