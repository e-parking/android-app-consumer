package bd.com.universal.eparking.seeker.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import bd.com.universal.eparking.seeker.MainActivity;
import bd.com.universal.eparking.seeker.R;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Boolean mInternetStatus;
    private Dialog mGpsDialog;
    private Dialog mInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAuth.getCurrentUser() != null) {

                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
                else {
                    Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }

        }, 2000);
    }


}
