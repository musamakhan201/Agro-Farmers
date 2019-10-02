package example.com.agrofarmers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1240;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        validateAndPopulate();
        getLocationPermission();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
    private boolean getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }else {
            onRequestPermissionsResult(REQUEST_CODE, null, new int[]{PackageManager.PERMISSION_GRANTED});
        }
        return true;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }
    private void validateAndPopulate() {
        if (!isNetworkAvailable()) {
            final AlertDialog.Builder networkErrorDialog = new AlertDialog.Builder(this).setTitle("No Network")
                    .setMessage("Internet is not available.");
            networkErrorDialog.setNegativeButton("Exit App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }).setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    validateAndPopulate();
                }
            }).show();

        }
    }
}
