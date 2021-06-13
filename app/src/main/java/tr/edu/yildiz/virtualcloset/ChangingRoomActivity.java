package tr.edu.yildiz.virtualcloset;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tr.edu.yildiz.virtualcloset.Adapters.ChangingRoomAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;
import tr.edu.yildiz.virtualcloset.Model.Outfit;

public class ChangingRoomActivity extends AppCompatActivity {
    public static Dialog dialog;
    public static Bitmap bitmap;
    public static boolean selected = false;
    public static Clothes cOverHead, cUpper, cLower, cFoot;

    Button addOutfit;
    ImageView imgOverHead, imgFace, imgUpper, imgLower, imgFoot;

    DatabaseHelper databaseHelper;
    ArrayList<Clothes> clothes, overhead, upper, lower, foot;

    Animation scaleUp, scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changing_room);
        defineVariables();

        databaseHelper = new DatabaseHelper(this);
        clothes = databaseHelper.getAllClothes();
        if (clothes != null) {
            separateClothes();

            if (overhead.size() == 0 || upper.size() == 0 || lower.size() == 0 || foot.size() == 0) {
                defineNoOutfit();
            } else {
                defineListeners();
            }
        } else {
            defineNoOutfit();
        }
    }

    private void defineNoOutfit() {
        TextView typeCounts = findViewById(R.id.typeCounts);

        String counts = "Başüstü: " + overhead.size() + " adet\n" +
                "Üst Beden: " + upper.size() + " adet\n" +
                "Alt Beden: " + lower.size() + " adet\n" +
                "Ayak: " + foot.size() + " adet";
        typeCounts.setText(counts);
        typeCounts.setVisibility(View.VISIBLE);

        TextView noType = findViewById(R.id.noType);
        noType.setVisibility(View.VISIBLE);
        addOutfit.setVisibility(View.GONE);
        imgOverHead.setVisibility(View.GONE);
        imgFace.setVisibility(View.GONE);
        imgUpper.setVisibility(View.GONE);
        imgLower.setVisibility(View.GONE);
        imgFoot.setVisibility(View.GONE);
    }

    private void defineListeners() {
        addOutfit.setOnClickListener(v -> {
            addOutfit.startAnimation(scaleUp);
            addOutfit.startAnimation(scaleDown);

            if (cUpper == null || cLower == null || cFoot == null) {
                Toast.makeText(ChangingRoomActivity.this, "Lütfen seçim yapınız", Toast.LENGTH_SHORT).show();
            } else {
                databaseHelper.addOutfit(new Outfit(cOverHead == null ? -1 : cOverHead.getId(), cUpper.getId(), cLower.getId(), cFoot.getId()));
                Toast.makeText(ChangingRoomActivity.this, "Kombin eklendi", Toast.LENGTH_SHORT).show();
            }
        });

        imgOverHead.setOnClickListener(v -> createDialog(overhead, 1));
        imgUpper.setOnClickListener(v -> createDialog(upper, 2));
        imgLower.setOnClickListener(v -> createDialog(lower, 3));
        imgFoot.setOnClickListener(v -> createDialog(foot, 4));
    }

    private void defineVariables() {
        addOutfit = findViewById(R.id.addOutfit);
        imgOverHead = findViewById(R.id.imgOverHead);
        imgFace = findViewById(R.id.imgFace);
        imgUpper = findViewById(R.id.imgUpper);
        imgLower = findViewById(R.id.imgLower);
        imgFoot = findViewById(R.id.imgFoot);

        overhead = new ArrayList<>();
        upper = new ArrayList<>();
        lower = new ArrayList<>();
        foot = new ArrayList<>();

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleDown.setStartOffset(100);
    }

    private void createDialog(ArrayList<Clothes> clothes, int type) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.rv_dialog);

        ChangingRoomAdapter roomAdapter = new ChangingRoomAdapter(this, clothes, type);
        RecyclerView mRv = dialog.findViewById(R.id.dialog_rv);
        mRv.setHasFixedSize(true);
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
                        imgUpper.setImageBitmap(bitmap);
                        break;
                    case 3:
                        imgLower.setImageBitmap(bitmap);
                        break;
                    case 4:
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
                case "T-Shirt":
                case "Gömlek":
                case "Kazak":
                case "Sweatshirt":
                case "Mont":
                    upper.add(aClothes);
                    break;
                case "Pantolon":
                case "Şort":
                case "Eşofman":
                    lower.add(aClothes);
                    break;
                case "Ayakkabı":
                    foot.add(aClothes);
                    break;
            }
        }
    }
}