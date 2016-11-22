package com.deadswine.brickator.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.Until;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 * <p>
 * Most methods are currently using api 21
 */
public class BrickatorUtilities {

    /**
     * Execute adb shell commands <br>
     * Warning!!! Adding grep to the cmd don't seems to be working!!!
     * <br> You can pass parameter and java contains will be used for parsing response, it seems to be working ok
     *
     * @param cmd
     * @return
     * @throws IOException
     */
    @TargetApi(21)
    public static String shell(String cmd, String grep) {

        Brickator.print("Executing shell cmd: " + cmd);
        ParcelFileDescriptor descriptor = InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(cmd);

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(descriptor.getFileDescriptor())));

        StringBuffer stringBuffer = new StringBuffer();
        String readString = null;
        do {

            try {
                readString = br.readLine();
            } catch (IOException e) {
                throw new UnknownError(" IO EXCEPTION Failed to read shell response: " + e.getMessage());
            }

            if (readString != null) {

                if (grep != null) {

                    if (readString.contains(grep)) {
                        stringBuffer.append(readString);
                    }

                } else {
                    stringBuffer.append(readString);
                }
            }

        } while (readString != null);
        try {
            descriptor.close();
        } catch (IOException e) {
            throw new UnknownError(" IO EXCEPTION Failed to close descriptor: " + e.getMessage());

        }

        return stringBuffer.toString();

    }

    /**
     * Executes adb shell pm lis packages grep appPackage <br>
     * If the returned list is empty returns false
     *
     * @param appPackage
     * @return
     */
    @TargetApi(21)
    public static boolean isAppInstalled(String appPackage) {

        String shellAppInstalled;

        shellAppInstalled = shell("pm list packages", appPackage);

        shellAppInstalled = shellAppInstalled.trim();
        Brickator.print("checking if app: " + appPackage + " is installed");

        Brickator.print("shellAppInstalled: " + shellAppInstalled);
        if (shellAppInstalled == null || shellAppInstalled.isEmpty()) {
            return false;
        } else {

            return true;
        }

    }

    /**
     * @param appPackage with activity com.package.name/com.package.name.ActivityName
     * @return
     */
    public static boolean launchApp(String appPackage, String appActivity) {

        if (!isAppInstalled(appPackage)) {
            Brickator.print("Failed to launch app " + appPackage + "  app is not installed");
            return false;
        }

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackage);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        Brickator.getInstance().getDevice().wait(Until.hasObject(By.pkg(appPackage).depth(0)), 10000);

        String[] currentWindow = getCurrentFocusedWindow();
        if (currentWindow[1].equals(appActivity)) {

            return true;
        } else {
            return false;
        }

    }

    /**
     * Returns window  that is currently focused as an string array
     *
     * @return String[2] 0 - currentPackage 1- currentActivity
     */
    @TargetApi(21)
    public static String[] getCurrentFocusedWindow() {
        String adbShellFocusedWindow = null;
        int retryToGetFocus = 0;
        do {

            adbShellFocusedWindow = shell("dumpsys window windows", "mCurrentFocus");

            retryToGetFocus++;

            if (adbShellFocusedWindow.contains("mCurrentFocus=null")) {
                Brickator.print("adbShellFocusedWindow mCurrentFocus=null - sleeping");
                SystemClock.sleep(200);
            } else {
                Brickator.print("adbShellFocusedWindow is ! null or empty " + adbShellFocusedWindow);
            }

            if (adbShellFocusedWindow.contains("mCurrentFocus=null") && retryToGetFocus >= 10) {
                throw new Error("Failed to get proper response from: adb shell dumpsys window windows in this many retry: " + retryToGetFocus);
            }

        } while (adbShellFocusedWindow.contains("mCurrentFocus=null"));

        adbShellFocusedWindow = adbShellFocusedWindow.trim();
        adbShellFocusedWindow = adbShellFocusedWindow.replace("{", "");
        adbShellFocusedWindow = adbShellFocusedWindow.replace("}", "");
        adbShellFocusedWindow = adbShellFocusedWindow.trim();

        String[] splitBySpace = adbShellFocusedWindow.split(" ");

        String currentPackage = splitBySpace[splitBySpace.length - 1].split("/")[0];
        String currentActivity = splitBySpace[splitBySpace.length - 1].split("/")[1];

        return new String[]{currentPackage, currentActivity};
    }
}
