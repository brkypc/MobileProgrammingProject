package tr.edu.yildiz.virtualcloset;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import tr.edu.yildiz.virtualcloset.Adapters.ClothesAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;

public class ClothesActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ArrayList<Clothes> clothes;

    RecyclerView rvClothes;
    ClothesAdapter clothesAdapter;
    TextView noClothes;

    int drawerNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes);

        Intent intent = getIntent();
        drawerNo = intent.getIntExtra("drawerNo", -1);

        databaseHelper = new DatabaseHelper(this);
        clothes = databaseHelper.getDrawerClothes(drawerNo);

        if (clothes != null) {
            defineAddClothes(clothes.size());
            defineRv();
        } else {
            defineAddClothes(0);
            noClothes = findViewById(R.id.noClothes);
            noClothes.setVisibility(View.VISIBLE);
        }
    }

    private void defineRv() {
        rvClothes = findViewById(R.id.rvClothes);
        rvClothes.setHasFixedSize(true);

        rvClothes.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        rvClothes.addItemDecoration(decoration);

        clothesAdapter = new ClothesAdapter(this, clothes);
        rvClothes.setAdapter(clothesAdapter);

        /*Animation recycler_animation = AnimationUtils.loadAnimation(this, R.anim.recycler_animation);
        rvClothes.startAnimation(recycler_animation);*/
    }

    private void defineAddClothes(int size) {
        ImageButton addClothes = findViewById(R.id.addClothes);
        addClothes.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddClothesActivity.class);
            intent.putExtra("drawerNo", drawerNo);
            intent.putExtra("count", size);
            startActivity(intent);
        });
    }
}