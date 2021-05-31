package tr.edu.yildiz.virtualcloset;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout drawers, events, changingRoom, outfits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        defineVariables();
        defineListeners();
    }

    private void defineVariables() {
        drawers = findViewById(R.id.first);
        events = findViewById(R.id.second);
        changingRoom = findViewById(R.id.third);
        outfits = findViewById(R.id.fourth);
    }

    private void defineListeners() {
        drawers.setOnClickListener(this);
        events.setOnClickListener(this);
        changingRoom.setOnClickListener(this);
        outfits.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.first) {
            intent = new Intent(MenuActivity.this, DrawersActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.second) {
            intent = new Intent(MenuActivity.this, EventsActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.third) {
            intent = new Intent(MenuActivity.this, ChangingRoomActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.fourth) {
            intent = new Intent(MenuActivity.this, OutfitsActivity.class);
            startActivity(intent);
        }
    }
}