package com.deadswine.brickator.sample.states;

import android.support.test.uiautomator.Until;

import com.deadswine.brickator.library.Brickator;
import com.deadswine.brickator.library.BrickatorState;
import com.deadswine.brickator.library.annotations.LeadsTo;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class GalleryState extends BrickatorState {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void stateReached() {
        Brickator.print(TAG + "stateReached()");
    }

    @LeadsTo(MainMenuState.class)
    public void goToMainMenuState() {

        Brickator.print(TAG + " goToMainMenuState()");
        MainState.getHamburger().clickAndWait(Until.newWindow(), 1000);
    }

}
