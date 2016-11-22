package com.deadswine.brickator.library;

import com.deadswine.brickator.library.annotations.LeadsTo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Copyright 2016 Adam Fresko. All Rights Reserved.
 */
public class PathFinder {

    public static String TAG = "PathFinder";

    public <T extends BrickatorState> Method findMethodLeadingToDesiredState(Class<? extends BrickatorState> currentState, Class<T> desiredState) {
        Method desiredMethod = null;
        for (Method method : currentState.getMethods()) {
            LeadsTo goToAnnotation = (LeadsTo) method.getAnnotation(LeadsTo.class);
            if (goToAnnotation != null) {
                if (goToAnnotation.value().equals(desiredState)) {
                    desiredMethod = method;
                }

            } else {
                // print("Method: "+method.getName() +" dont have leads to annotation");
            }
        }

        return desiredMethod;
    }

    public <T extends BrickatorState> BrickatorState invokeLeadsToMethod(Class<? extends BrickatorState> currentState, Method desiredMethod) {

        BrickatorState foo = null;

        try {
            foo = currentState.newInstance();
            if (desiredMethod != null) {
                desiredMethod.invoke(foo);
            } else {
                Brickator.print(TAG + " Instantiated " + foo.getClass().getSimpleName() + " but NOT INVOKING METHOD as provided one was null");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        if (foo == null) {
            throw new IllegalStateException("Failed to instantiate current state class: " + currentState);
        }

        return foo;

    }

    public <T extends BrickatorState> ArrayList<Class<? extends BrickatorState>> findStatesThatLeadsTo(ArrayList<Class<? extends BrickatorState>> registeredStates, Class<? extends BrickatorState> desiredState) {
        ArrayList<Class<? extends BrickatorState>> statesThatLeadsTo = new ArrayList<>();

        Method desiredMethod;
        for (int i = 0; i < registeredStates.size(); i++) {
            desiredMethod = findMethodLeadingToDesiredState(registeredStates.get(i), desiredState);

            if (desiredMethod != null) {
                statesThatLeadsTo.add(registeredStates.get(i));
            }

        }

        return statesThatLeadsTo;
    }

    public <T extends BrickatorState> ArrayList<Class<? extends BrickatorState>> findPathv2(ArrayList<Class<? extends BrickatorState>> registeredStates, Class<? extends BrickatorState> currentState, Class<? extends BrickatorState> desiredState) {
        Brickator.print(TAG + " ----------------- ");
        Brickator.print(TAG + " PATH FINDER START ");
        Brickator.print(TAG + " ----------------- ");

        Class<? extends BrickatorState> tmpDesiredState = desiredState;

        Brickator.print(TAG + " findPath from: " + currentState.getSimpleName() + " to " + desiredState.getSimpleName());
        ArrayList<Class<? extends BrickatorState>> pathThatWeFound;
        ArrayList<Class<? extends BrickatorState>> returnedPath = new ArrayList<>();

        // #1 check if desired state != current state;
        if (currentState.getSimpleName().equals(desiredState.getSimpleName())) {
            Brickator.print(TAG + " desired state == current state");
            return null;
        }

        // #2 iterate over registered states find states methods that leads to desired state
        pathThatWeFound = findStatesThatLeadsTo(registeredStates, desiredState);
        for (int i = 0; i < pathThatWeFound.size(); i++) {
            Brickator.print(TAG + " found following nodes " + pathThatWeFound.get(i).getSimpleName() + " leading to " + desiredState.getSimpleName());
        }

        // #3 check if we found any node leading to desired state if not throw error
        if (pathThatWeFound.size() == 0) {
            throw new IllegalStateException("No registered state leads to: " + desiredState + " found in " + registeredStates);
        }

        do {
            desiredState = pathThatWeFound.get(0);
            Brickator.print("Looping for state: " + desiredState.getSimpleName());

            returnedPath.add(pathThatWeFound.get(0));
            pathThatWeFound = findStatesThatLeadsTo(registeredStates, desiredState);

        } while (pathThatWeFound.contains(currentState));

        Brickator.print("Path that we found: " + pathThatWeFound);

        // #4 loop over last returned path for current desired state that leads to current state
        for (int i = 0; i < pathThatWeFound.size(); i++) {

            if (findMethodLeadingToDesiredState(currentState, pathThatWeFound.get(i)) != null) {
                returnedPath.add(pathThatWeFound.get(i));
                break;
            } else {
                Brickator.print("pathThatWeFound.get(i) " + pathThatWeFound.get(i).getSimpleName() + " not equals " + currentState.getSimpleName());
            }
        }

        returnedPath.add(0, tmpDesiredState);

        Collections.reverse(returnedPath);
        Brickator.print(TAG + " returnedPath: " + returnedPath);

        Brickator.print(TAG + " ----------------- ");
        Brickator.print(TAG + " PATH FINDER END   ");
        Brickator.print(TAG + " ----------------- ");

        return returnedPath;
    }

}
