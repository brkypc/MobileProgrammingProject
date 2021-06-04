package tr.edu.yildiz.virtualcloset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView middleText, middleNumber, bottomText;
    Animation middleTextAnimation, middleNumberAnimation, bottomAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineVariables();
        setAnimations();
        goToMenu();
    }

    private void defineVariables() {
        middleText = findViewById(R.id.middleName);
        middleNumber = findViewById(R.id.middleNumber);
        bottomText = findViewById(R.id.bottomText);

        middleTextAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_text_animation);
        middleNumberAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_number_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
    }

    private void setAnimations() {
        middleText.setAnimation(middleTextAnimation);
        middleNumber.setAnimation(middleNumberAnimation);
        bottomText.setAnimation(bottomAnimation);
    }

    private void goToMenu() {
        int SPLASH_SCREEN_TIME_OUT = 1200;
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        },SPLASH_SCREEN_TIME_OUT);
    }
}