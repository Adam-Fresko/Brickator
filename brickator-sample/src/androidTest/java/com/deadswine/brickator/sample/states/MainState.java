package com.deadswine.brickator.sample.states;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.deadswine.brickator.library.Brickator;
import com.deadswine.brickator.library.BrickatorState;
import com.deadswine.brickator.library.annotations.LeadsTo;
import com.deadswine.brickator.sample.R;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class MainState extends BrickatorState {

    public String TAG = getClass().getSimpleName();

    public static UiObject2 getHamburger() {

        UiObject2 hamburger = Brickator.getInstance().getDevice().findObject(By.desc("Open navigation drawer"));

        return hamburger;

    }

    public static UiObject2 getOverflow() {

        UiObject2 hamburger = Brickator.getInstance().getDevice().findObject(By.desc("More options"));

        return hamburger;

    }

    public static UiObject2 getSettingsFromOverflow() {

        Context context = InstrumentationRegistry.getTargetContext();

        UiObject2 hamburger = Brickator.getInstance().getDevice().findObject(By.text(context.getResources().getString(R.string.action_settings)));

        return hamburger;

    }

    public static String getActivityName() {
        return "com.deadswine.brickator.sample.Activities.MainActivity";
    }

    @Override
    public void stateReached() {

    }

    @LeadsTo(MainMenuState.class)
    public void goToMainMenu() {
        Brickator.print(TAG + " goToMainMenu()");
        getHamburger().clickAndWait(Until.newWindow(), 1000);
    }

    @LeadsTo(SettingsState.class)
    public void goToSettings() {
        // ct

        Brickator.print(TAG + " goToSettings()");
        getOverflow().clickAndWait(Until.newWindow(), 1000);
        getSettingsFromOverflow().clickAndWait(Until.newWindow(), 1000);
    }

}
