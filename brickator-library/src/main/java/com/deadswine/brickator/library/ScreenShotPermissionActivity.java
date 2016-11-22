package com.deadswine.brickator.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION_CODES.M;
import static android.widget.Toast.LENGTH_LONG;

@TargetApi(M)
public class ScreenShotPermissionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            if (hasStoragePermission()) {
                Toast.makeText(getApplication(), "Permission already granted", LENGTH_LONG).show();
                finish();
                return;
            }
            String[] permissions = {WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, 42);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (!hasStoragePermission()) {
            Toast.makeText(getApplication(), "Permission not granted", LENGTH_LONG).show();
        }
        finish();
    }

    private boolean hasStoragePermission() {
        return checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
    }
}
