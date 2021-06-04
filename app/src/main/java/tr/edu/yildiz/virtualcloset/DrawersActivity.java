package tr.edu.yildiz.virtualcloset;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import tr.edu.yildiz.virtualcloset.Adapters.DrawerAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Drawer;

public class DrawersActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ArrayList<Drawer> drawers;

    RecyclerView rvDrawers;
    DrawerAdapter drawerAdapter;
    TextView noDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawers);

        databaseHelper = new DatabaseHelper(this);
        drawers = databaseHelper.getDrawers();

        defineAddDrawer();
        defineRv();

        if(drawers != null) {
            drawerAdapter = new DrawerAdapter(this, drawers);
            rvDrawers.setAdapter(drawerAdapter);
        }
        else {
            noDrawer = findViewById(R.id.noDrawer);
            noDrawer.setVisibility(View.VISIBLE);
        }
    }

    private void defineRv() {
        rvDrawers = findViewById(R.id.rvDrawers);
        rvDrawers.setHasFixedSize(true);

        rvDrawers.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        rvDrawers.addItemDecoration(decoration);

        /*Animation recycler_animation = AnimationUtils.loadAnimation(this, R.anim.recycler_animation);
        rvDrawers.startAnimation(recycler_animation);*/
    }

    private void defineAddDrawer() {
        ImageButton addDrawer = findViewById(R.id.addDrawer);
        addDrawer.setOnClickListener(v -> {
            if(drawers != null) {
                createDialog(drawers.size());
            }
            else {
                createDialog(0);
            }
        });
    }

    private void createDialog(int size) {
        String name = "Çekmece " + (size + 1);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.drawer_dialog);

        EditText dialogName = dialog.findViewById(R.id.dialogName);
        dialogName.setText(name);

        Button dialogDismiss = dialog.findViewById(R.id.dialogDismiss);
        dialogDismiss.setOnClickListener(v1 -> dialog.dismiss());

        Button dialogAdd = dialog.findViewById(R.id.dialogAdd);
        dialogAdd.setOnClickListener(v12 -> {
            if(!dialogName.getText().toString().isEmpty()) {
                long result = databaseHelper.addDrawer(dialogName.getText().toString());
                if(result != -1) {
                    dialog.dismiss();
                    noDrawer.setVisibility(View.GONE);
                    Toast.makeText(this, "Çekmece eklendi", Toast.LENGTH_SHORT).show();
                    drawers = databaseHelper.getDrawers();
                    drawerAdapter = new DrawerAdapter(this, drawers);
                    rvDrawers.setAdapter(drawerAdapter);
                }
                else {
                    Toast.makeText(this, "Çekmece eklenemedi", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Çekmece ismi alanı boş bırakılamaz", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}