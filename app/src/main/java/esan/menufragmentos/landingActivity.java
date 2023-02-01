package esan.menufragmentos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;

public class landingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page_activity);

         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {

                 if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                     Intent intent = new Intent(landingActivity.this, MainActivity.class);
                     startActivity(intent);
                 }else{
                     Intent intent = new Intent(landingActivity.this, LoginActivity.class);
                     startActivity(intent);
                 }

             }
         },1000);


    }
}