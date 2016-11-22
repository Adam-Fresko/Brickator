package com.deadswine.brickator.library;

import android.support.test.uiautomator.UiDevice;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public abstract class StateResolver {

    /**
     * Do not use this method directly instead use method from Brickator singleton
     *
     * @return
     */
    public abstract Class<? extends BrickatorState> resolveCurrentState(String Package, String Activity, UiDevice uiDevice);

}
