package tr.edu.yildiz.virtualcloset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView middleText1, middleText2, bottomText;
    Animation text1Anim, text2Anim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineVariables();
        setAnimations();
        goToMenu();
    }

    private void defineVariables() {
        middleText1 = findViewById(R.id.middleName1);
        middleText2 = findViewById(R.id.middleName2);
        bottomText = findViewById(R.id.bottomText);

        text1Anim = AnimationUtils.loadAnimation(this, R.anim.text1_anim);
        text2Anim = AnimationUtils.loadAnimation(this, R.anim.text2_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
    }

    private void setAnimations() {
        middleText1.setAnimation(text1Anim);
        middleText2.setAnimation(text2Anim);
        bottomText.setAnimation(bottomAnim);
    }

    private void goToMenu() {
        int SPLASH_SCREEN_TIME_OUT = 1400;
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        },SPLASH_SCREEN_TIME_OUT);
    }
}