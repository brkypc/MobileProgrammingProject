package tr.edu.yildiz.virtualcloset;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.Adapters.ClothesAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;

public class ClothesActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ArrayList<Clothes> clothes;

    RecyclerView rvClothes;
    ClothesAdapter clothesAdapter;
    TextView noClothes;
    SwipeRefreshLayout refreshLayout;

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

        swipeToRefresh();
    }

    private void swipeToRefresh() {
        refreshLayout = findViewById(R.id.refreshClothes);
        refreshLayout.setOnRefreshListener(() -> {
            if (clothesAdapter != null) {
                clothesAdapter.dataChanged(drawerNo);
            }
            refreshLayout.setRefreshing(false);
        });
    }

    private void defineRv() {
        rvClothes = findViewById(R.id.rvClothes);
        rvClothes.setHasFixedSize(true);
        rvClothes.setLayoutManager(new LinearLayoutManager(this));

        clothesAdapter = new ClothesAdapter(this, clothes);
        rvClothes.setAdapter(clothesAdapter);
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