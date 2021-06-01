package tr.edu.yildiz.virtualcloset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import tr.edu.yildiz.virtualcloset.Adapters.ChangingRoomAdapter;
import tr.edu.yildiz.virtualcloset.Adapters.OutfitAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Event;
import tr.edu.yildiz.virtualcloset.Model.Outfit;

public class AddEventActivity extends AppCompatActivity {
    public static Dialog dialog;
    public static int outfitNo;

    EditText eventName, eventType;
    TextView eventDate, eventLocation, eventOutfit;
    Button addOutfit;

    Calendar calendar, mCalendar;
    SimpleDateFormat sdf;
    int year, month, dayOfMonth;

    DatabaseHelper databaseHelper;
    ArrayList<Outfit> outfits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);
        defineVariables();
        defineListeners();
    }

    private void createDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            mCalendar.set(year, month, dayOfMonth);

            String strDate = sdf.format(mCalendar.getTime());
            eventDate.setText(strDate);

            this.year = year; this.month = month; this.dayOfMonth = dayOfMonth;
        }, year, month, dayOfMonth);
        dpd.setCancelable(false);
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpd.show();
    }

    private void createLocationPicker() {

    }

    private void createOutfitPicker() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.rv_dialog);

        RecyclerView mRv = dialog.findViewById(R.id.dialog_rv);
        mRv.setHasFixedSize(true); // DENEMEDİM
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        decoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        mRv.addItemDecoration(decoration);

        OutfitAdapter outfitAdapter = new OutfitAdapter(this, outfits);
        mRv.setAdapter(outfitAdapter);
        dialog.setOnDismissListener(dialog -> {
            if(outfitNo != 0) {
                eventOutfit.setText("Kombin " + outfitNo);
            }
        });
        dialog.show();
    }

    private void defineListeners() {
        eventDate.setOnClickListener(v -> createDatePicker());
        eventLocation.setOnClickListener(v -> createLocationPicker());
        eventOutfit.setOnClickListener(v -> createOutfitPicker());
        addOutfit.setOnClickListener(v -> {
            if(validateFields()) {
                //ekle
                int outfitNo = Integer.parseInt(eventOutfit.getText().toString());
                String name, type, date, location;
                name = eventName.getText().toString();
                type = eventType.getText().toString();
                date = eventDate.getText().toString();
                location = eventLocation.getText().toString();

                //databaseHelper.addEvent(new Event(outfitNo, name, type, date, location));
                Toast.makeText(AddEventActivity.this, "Etkinlik eklendi", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(AddEventActivity.this, "Alanlar boş bırakılamaz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFields() {
        return !eventName.getText().toString().isEmpty() && !eventType.getText().toString().isEmpty() && !eventDate.getText().toString().isEmpty() &&
                !eventLocation.getText().toString().isEmpty() && !eventOutfit.getText().toString().isEmpty();
    }

    @SuppressLint("SimpleDateFormat")
    private void defineVariables() {
        outfitNo = 0;

        eventName = findViewById(R.id.eventName);
        eventType = findViewById(R.id.eventType);
        eventDate = findViewById(R.id.eventDate);
        eventLocation = findViewById(R.id.eventLocation);
        eventOutfit = findViewById(R.id.eventOutfit);
        addOutfit = findViewById(R.id.addOutfit);

        calendar = Calendar.getInstance();
        mCalendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        databaseHelper = new DatabaseHelper(this);
        outfits = databaseHelper.getOutfits();
    }
}