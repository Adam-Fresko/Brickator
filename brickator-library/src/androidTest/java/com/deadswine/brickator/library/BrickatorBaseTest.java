package com.deadswine.brickator.library;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public abstract class BrickatorBaseTest {
    public String TAG = "AutomatorBase-ChangeMe";
    @Rule
    public TestRule watcher = new TestWatcher() {
        protected void starting(Description description) {
            Brickator.print("Starting test: " + description.getMethodName() + " for class: " + TAG);
            Brickator.getInstance().setCurrentTestName(TAG, description.getMethodName());
        }
    };

    @Before
    public void beforeTest() {
        Brickator.print("beforeTest");
        Brickator.getInstance().takeScreenShot("BeforeTest");
    }

    @After
    public void afterTest() {
        Brickator.print("afterTest");
        Brickator.getInstance().takeScreenShot("AfterTest");
    }

    public void setTag(String newTag) {
        TAG = newTag;
    }
}
