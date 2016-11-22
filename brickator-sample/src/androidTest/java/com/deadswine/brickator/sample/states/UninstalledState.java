package com.deadswine.brickator.sample.states;

import com.deadswine.brickator.library.BrickatorState;
import com.deadswine.brickator.library.annotations.LeadsTo;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class UninstalledState extends BrickatorState {
    @Override
    public void stateReached() {

    }

    @LeadsTo(BackgroundState.class)
    public void goToBackgroundState() {

    }
}
