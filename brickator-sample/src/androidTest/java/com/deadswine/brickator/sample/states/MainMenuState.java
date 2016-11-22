package com.deadswine.brickator.sample.states;

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.deadswine.brickator.library.Brickator;
import com.deadswine.brickator.library.BrickatorState;
import com.deadswine.brickator.library.annotations.LeadsTo;
import com.deadswine.brickator.sample.R;

import static org.junit.Assert.assertNotNull;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class MainMenuState extends BrickatorState {

    @Override
    public void stateReached() {
        Brickator.print("MainMenuState - stateReached()");
    }

    @LeadsTo(GalleryState.class)
    public void goToGalleryState() {

        UiObject uiObject = getGallery();

        try {
            uiObject.clickAndWaitForNewWindow();
        } catch (UiObjectNotFoundException e) {
            //e.printStackTrace();
            assertNotNull(uiObject);
        }

        SystemClock.sleep(1000);

    }

    @LeadsTo(MainState.class)
    public void goToMainState() {

        UiObject uiObject = getMainState();

        try {
            uiObject.clickAndWaitForNewWindow();
        } catch (UiObjectNotFoundException e) {
            //e.printStackTrace();
            assertNotNull(uiObject);
        }

        SystemClock.sleep(1000);

    }

    public static UiObject getGallery() {

        Context context = InstrumentationRegistry.getTargetContext();

        return Brickator.getInstance().getDevice().findObject(new UiSelector().text(context.getResources().getString(R.string.gallery)));

    }

    public static UiObject getMainState() {

        Context context = InstrumentationRegistry.getTargetContext();

        return Brickator.getInstance().getDevice().findObject(new UiSelector().text(context.getResources().getString(R.string.state_main)));

    }

}
