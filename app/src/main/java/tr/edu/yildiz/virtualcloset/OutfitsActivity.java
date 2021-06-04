package tr.edu.yildiz.virtualcloset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import tr.edu.yildiz.virtualcloset.Adapters.OutfitAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;
import tr.edu.yildiz.virtualcloset.Model.Outfit;

public class OutfitsActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ArrayList<Outfit> outfits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfits);

        databaseHelper = new DatabaseHelper(this);
        outfits = databaseHelper.getOutfits();
        if(outfits != null) {
            defineRv();
        }
        else {
            TextView noOutfit = findViewById(R.id.noOutfit);
            noOutfit.setVisibility(View.VISIBLE);
        }
    }

    private void defineRv() {
        RecyclerView rvOutfits = findViewById(R.id.rvOutfits);
        rvOutfits.setItemViewCacheSize(outfits.size());
        rvOutfits.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        rvOutfits.addItemDecoration(decoration);

        OutfitAdapter outfitAdapter = new OutfitAdapter(this, outfits);
        rvOutfits.setAdapter(outfitAdapter);
    }
}