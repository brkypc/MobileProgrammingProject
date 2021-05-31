package tr.edu.yildiz.virtualcloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import tr.edu.yildiz.virtualcloset.Adapters.ChangingRoomAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;
import tr.edu.yildiz.virtualcloset.Model.Outfit;

public class ChangingRoomActivity extends AppCompatActivity {
    public static Dialog dialog;
    public static Bitmap bitmap;
    public static boolean selected = false;
    public static Clothes cOverHead, cFace, cUpper, cLower, cFoot;

    Button addOutfit;
    ImageView imgOverHead, imgFace, imgUpper, imgLower, imgFoot;

    DatabaseHelper databaseHelper;
    ArrayList<Clothes> clothes, overhead, face, upper, lower, foot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changing_room);
        defineVariables();

        databaseHelper = new DatabaseHelper(this);
        clothes = databaseHelper.getAllClothes();
        if (clothes != null) {
            separateClothes();
            if (overhead.size() == 0 || face.size() == 0 || upper.size() == 0 || lower.size() == 0 || foot.size() == 0) {
                TextView noType = findViewById(R.id.noType);
                noType.setVisibility(View.VISIBLE);
            }
            else {
                defineListeners();
            }
        }
        else {
            TextView noType = findViewById(R.id.noType);
            noType.setVisibility(View.VISIBLE);
        }
    }

    private void defineListeners() {
        addOutfit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cOverHead == null || cFace == null || cUpper == null || cLower == null || cFoot == null) {
                    Toast.makeText(ChangingRoomActivity.this, "Bütün bölümler seçili olmalıdır", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseHelper.addOutfit(new Outfit(cOverHead.getId(), cFace.getId(), cUpper.getId(), cLower.getId(), cFoot.getId()));
                    Toast.makeText(ChangingRoomActivity.this, "Kombin eklendi", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imgOverHead.setOnClickListener(v -> createDialog(overhead, 1));
        imgFace.setOnClickListener(v -> createDialog(face, 2));
        imgUpper.setOnClickListener(v -> createDialog(upper, 3));
        imgLower.setOnClickListener(v -> createDialog(lower, 4));
        imgFoot.setOnClickListener(v -> createDialog(foot, 5));
    }

    private void defineVariables() {
        addOutfit = findViewById(R.id.addOutfit);
        imgOverHead = findViewById(R.id.imgOverHead);
        imgFace = findViewById(R.id.imgFace);
        imgUpper = findViewById(R.id.imgUpper);
        imgLower = findViewById(R.id.imgLower);
        imgFoot = findViewById(R.id.imgFoot);

        overhead = new ArrayList<>();
        face = new ArrayList<>();
        upper = new ArrayList<>();
        lower = new ArrayList<>();
        foot = new ArrayList<>();
    }

    private void createDialog(ArrayList<Clothes> clothes, int type) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.rv_dialog);
        ChangingRoomAdapter roomAdapter = new ChangingRoomAdapter(this, clothes, type);
        RecyclerView mRv = dialog.findViewById(R.id.dialog_rv);
        mRv.setHasFixedSize(true); // DENEMEDİM
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(roomAdapter);
        dialog.setOnDismissListener(dialog -> {
            if (selected) {
                selected = false;
                switch (type) {
                    case 1:
                        imgOverHead.setImageBitmap(bitmap);
                        break;
                    case 2:
                        imgFace.setImageBitmap(bitmap);
                        break;
                    case 3:
                        imgUpper.setImageBitmap(bitmap);
                        break;
                    case 4:
                        imgLower.setImageBitmap(bitmap);
                        break;
                    case 5:
                        imgFoot.setImageBitmap(bitmap);
                        break;
                }
            }
        });
        dialog.show();
    }

    private void separateClothes() {
        for (Clothes aClothes : clothes) {
            switch (aClothes.getType()) {
                case "Şapka":
                    overhead.add(aClothes);
                    break;
                case "Gözlük":
                    face.add(aClothes);
                    break;
                case "T-Shirt":
                case "Mont":
                    upper.add(aClothes);
                    break;
                case "Pantolon":
                    lower.add(aClothes);
                    break;
                case "Ayakkabı":
                    foot.add(aClothes);
                    break;
            }
        }
    }

    /*private void defineRvs() {
        rvOverHead = findViewById(R.id.rvOverhead);
        rvFace = findViewById(R.id.rvFace);
        rvUpper = findViewById(R.id.rvUpper);
        rvLower = findViewById(R.id.rvLower);
        rvFoot = findViewById(R.id.rvFoot);

        rvOverHead.setHasFixedSize(true);
        rvFace.setHasFixedSize(true);
        rvUpper.setHasFixedSize(true);
        rvLower.setHasFixedSize(true);
        rvFoot.setHasFixedSize(true);

        rvOverHead.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFace.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvUpper.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvLower.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFoot.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        rvOverHead.addItemDecoration(decoration);
        rvFace.addItemDecoration(decoration);
        rvUpper.addItemDecoration(decoration);
        rvLower.addItemDecoration(decoration);
        rvFoot.addItemDecoration(decoration);
    }*/
}