# Brickator
State based UiAutomator library for Ui tests in Android

|   |  Build |  Emulator Test |
|---|---|---|
|Master   | [![Build Status](https://travis-ci.org/Adam-Fresko/brickator.svg?branch=master)](https://travis-ci.org/Adam-Fresko/brickator)  | [![Build Status](https://travis-ci.org/Adam-Fresko/brickator.svg?branch=master-tests-emulator)](https://travis-ci.org/Adam-Fresko/brickator)  |
|Develop   | [![Build Status](https://travis-ci.org/Adam-Fresko/brickator.svg?branch=develop)](https://travis-ci.org/Adam-Fresko/brickator)  |   |

#What can this do?
Here is example of brickator at work <br>


| Video  |    coverage report      | screenshots in coverage report |
|----------|:-------------:|------:|
| <a href="http://www.youtube.com/watch?feature=player_embedded&v=iPbzTZrDuDE&feature=youtu.be" target="_blank"><img src="http://img.youtube.com/vi/iPbzTZrDuDE/0.jpg" alt="IMAGE ALT TEXT HERE" width="240" height="180" border="10" /></a> | <img src="https://github.com/Adam-Fresko/brickator/blob/develop/brickator-sample/src/main/res/drawable/ss_1.png?raw=true" alt="IMAGE ALT TEXT HERE" width="240" height="180" border="10" /> | <img src="https://github.com/Adam-Fresko/brickator/blob/develop/brickator-sample/src/main/res/drawable/ss_2.png?raw=true" alt="IMAGE ALT TEXT HERE" width="240" height="180" border="10" /> |



Apart from clicking brickator does:
- detect current state, based on states that you have registered
- travel between registered states
- you can tell its to travel one by one registered state
- can takes screenshots during travel between state (this can be used for screenshots in google play)
- taken screenshots are added to debug coverage report
- utilities for UiAutomator for example shell wrappers


#Can i help improve this library?
Yes! Pull requests are welcome as well as adding issues

#How do i start?

### #0 Check sample application
[Sample](../master/brickator-sample)

### #1 Add library as module

### #2 Gradle
```Groovy
androidTestCompile 'com.android.support.test.uiautomator:uiautomator-v18:2.1.2'
androidTestCompile project(':brickator-library')

compile project(':brickator-library') // required for asking for storager permissions
```

### #3 Create States
Under src/androidTest/java create class called BackgroundState that is extending BrickatorState <br>
More states can be found here [here](../master/brickator-sample/src/androidTest/java/com/deadswine/brickator/sample/states/)

```Java
public class BackgroundState extends BrickatorState {
    @Override
    public void stateReached() {
        // called when we reach this state
    }
}
```

Create second state called MainState that is extending BrickatorState
```Java
public class MainState extends BrickatorState {
    @Override
    public void stateReached() {
        // called when we reach this state
    }
}
```

In BackgroundState class add method annotated with 
 ```Java  @LeadsTo(MainState.class)``` 
With this annotation library is able to find out to witch your state class leads.
So library can now know that BackgroundState can lead to MainState

The full method supposed to look like this 
```Java
@LeadsTo(MainState.class)
public void goToMainState() {
    Brickator.print(TAG + " goToMainState()"); // prints to logcat aswell as console
    BrickatorUtilities.launchApp("com.deadswine.brickator.sample", "com.deadswine.brickator.sample.Activities.MainActivity"); // its launching app

}
```

### #4 Create your implementation of state resolver
This class will be used to resolve what state we are in.
Under src/androidTest/java create class called Resolver that is extending [StateResolver](../master/brickator-library/src/androidTest/java/com/deadswine/brickator/library/StateResolver.java). 
```Java
@Override
public class Resolver extends StateResolver {
    public Class<? extends BrickatorState> resolveCurrentState(String currentFocusedPackage, String currentFocusedActivity, UiDevice uiDevice) {
        if (BrickatorUtilities.isAppInstalled(Brickator.getInstance().getAppPackage())) { // i'm using utility from brickator that tells me if sample app is installed
        
            if (!currentFocusedPackage.equals(Brickator.getInstance().getAppPackage())) { 
                // if the current focused package dont equals to ouer app this means that we didnt start ouer app or there is some system dialog present
                // in this case i'm returning background state
                
                return BackgroundState.class;
            }
        
        } else {  // if sample is not installed i'm returning uninstalled state // note that executing tests is installing the application
            return UninstalledState.class;
        }
    } 
}
```

### #5 Create test
Under src/androidTest/java create test class that is extending [BrickatorBaseTest](../master/brickator-library/src/androidTest/java/com/deadswine/brickator/library/BrickatorBaseTest.java). 
Note! You don't need to use BrickatorBaseTest but it makes your life easier with screenshots. See [BrickatorBaseTest](../master/brickator-library/src/androidTest/java/com/deadswine/brickator/library/BrickatorBaseTest.java) to learn how to handle that yourself 

```Java
@RunWith(AndroidJUnit4.class)
public class ExampleBrickatorTestWithPathFinding extends BrickatorBaseTest {
    { // this is called when class is created
        setTag(this.getClass().getSimpleName()); // set tag for screenshots // otherwise screenshots wont be created
    }
}
```

### #6 Add  ```Java @Before``` method to test class
This method will be best place to initialize Brickator library

```Java
@Before
public void beforeTest() {
     // initialize Brickator singleton
     Brickator.getInstance().initialize("com.deadswine.brickator.sample", new Resolver()); 
     
     // register states // you obviously need to write more states than the ones created at step 3 
     Brickator.getInstance().registerState(UninstalledState.class);
     Brickator.getInstance().registerState(BackgroundState.class);
     Brickator.getInstance().registerState(PermissionState.class);
     Brickator.getInstance().registerState(MainState.class);
     
     
     // #3 grant permission for screenshots
     PermissionState.checkScreenShotsPermission();
     
     super.beforeTest(); // call super method so we have screenshot in before automatically or you can handle it yourself
}
```

### #7 add test method
```Java
@Test
public void goToStatesWithPathTracingExample() throws Exception {
    
    Brickator.getInstance().goToState(MainState.class);
    Brickator.getInstance().takeScreenShot("goToState-MainState");
        
}    
```
