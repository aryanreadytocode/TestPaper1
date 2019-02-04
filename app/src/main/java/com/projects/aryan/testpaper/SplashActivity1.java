package  com.topperstuition.testpaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startTimerForSplash();
    }

    private void startTimerForSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              Intent i = new Intent(SplashActivity1.this, HomeActivity1.class);
              startActivity(i);
              SplashActivity1.this.finish();
            }
        }, 3000);
    }

}
