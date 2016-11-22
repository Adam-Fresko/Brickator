package com.deadswine.brickator.sample.resolvers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.deadswine.brickator.library.Brickator;
import com.deadswine.brickator.library.BrickatorState;
import com.deadswine.brickator.library.BrickatorUtilities;
import com.deadswine.brickator.library.StateResolver;
import com.deadswine.brickator.library.states.UnknownState;
import com.deadswine.brickator.sample.R;
import com.deadswine.brickator.sample.states.BackgroundState;
import com.deadswine.brickator.sample.states.GalleryState;
import com.deadswine.brickator.sample.states.MainMenuState;
import com.deadswine.brickator.sample.states.MainState;
import com.deadswine.brickator.sample.states.PermissionState;
import com.deadswine.brickator.sample.states.UninstalledState;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class Resolver extends StateResolver {

    @Override
    public Class<? extends BrickatorState> resolveCurrentState(String currentFocusedPackage, String currentFocusedActivity, UiDevice uiDevice) {

        Brickator.print("current package is: " + currentFocusedPackage);
        Brickator.print("current activity is " + currentFocusedActivity);

        if (BrickatorUtilities.isAppInstalled(Brickator.getInstance().getAppPackage())) {

            if (!currentFocusedPackage.equals(Brickator.getInstance().getAppPackage())) {
                return BackgroundState.class;
            }

        } else {

            return UninstalledState.class;

        }

        if (currentFocusedActivity.equals(PermissionState.getActivityName())) {
            return PermissionState.class;
        }

        if (currentFocusedActivity.equals(MainState.getActivityName())) {

            Context context = InstrumentationRegistry.getTargetContext();

            UiObject mainMenu = uiDevice.findObject(new UiSelector().text(context.getResources().getString(R.string.gallery)));

            if (mainMenu != null && mainMenu.exists()) {
                return MainMenuState.class;

            }

            UiObject mainState = uiDevice.findObject(new UiSelector().description(context.getResources().getString(R.string.fragment_main)));

            if (mainState != null && mainState.exists()) {
                return MainState.class;

            }

            UiObject galleryState = uiDevice.findObject(new UiSelector().description(context.getResources().getString(R.string.fragment_gallery)));

            if (galleryState != null && galleryState.exists()) {
                return GalleryState.class;

            }

        }

        // Remember to return UnknownState.class; at the very end! //TODO this should probably require calling super class at the end would make things for convenient
        return UnknownState.class;
    }

}
