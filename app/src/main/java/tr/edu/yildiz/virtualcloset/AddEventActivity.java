package tr.edu.yildiz.virtualcloset;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tr.edu.yildiz.virtualcloset.Adapters.PickOutfitAdapter;
import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Event;
import tr.edu.yildiz.virtualcloset.Model.Outfit;

public class AddEventActivity extends AppCompatActivity {
    private final static int LOCATION_REQUEST_CODE = 45;
    public static Dialog dialog;
    public static int outfitNo;

    EditText eventName, eventType;
    TextView eventDate, eventLocation, eventOutfit;
    Button addEvent;

    Calendar calendar, mCalendar;
    SimpleDateFormat sdf;
    int year, month, dayOfMonth;
    double latitude, longitude;

    DatabaseHelper databaseHelper;
    ArrayList<Outfit> outfits;
    int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        defineVariables();

        Intent intent = getIntent();
        if (intent.hasExtra("update")) {
            eventId = intent.getIntExtra("eventId", -1);
            setFields();
            defineUpdateListener();
        } else {
            defineAddListener();
        }

        defineListeners();
    }

    @SuppressLint("SetTextI18n")
    private void setFields() {
        Event event = databaseHelper.getEvent(eventId);

        outfitNo = event.getOutfitNo();
        eventOutfit.setText("Kombin " + outfitNo);
        eventName.setText(event.getName());
        eventType.setText(event.getType());
        eventDate.setText(event.getDate());
        eventLocation.setText(event.getLocation());
        latitude = event.getLatitude();
        longitude = event.getLongitude();
    }

    private void defineUpdateListener() {
        addEvent.setText(R.string.update);
        addEvent.setOnClickListener(v -> {
            if (validateFields()) {
                String name, type, date, location;
                name = eventName.getText().toString();
                type = eventType.getText().toString();
                date = eventDate.getText().toString();
                location = eventLocation.getText().toString();

                databaseHelper.updateEvent(new Event(outfitNo, name, type, date, location, latitude, longitude), eventId);
                Toast.makeText(AddEventActivity.this, "Etkinlik güncellendi", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddEventActivity.this, "Alanlar boş bırakılamaz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void defineAddListener() {
        addEvent.setOnClickListener(v -> {
            if (validateFields()) {
                String name, type, date, location;
                name = eventName.getText().toString();
                type = eventType.getText().toString();
                date = eventDate.getText().toString();
                location = eventLocation.getText().toString();

                databaseHelper.addEvent(new Event(outfitNo, name, type, date, location, latitude, longitude));
                Toast.makeText(AddEventActivity.this, "Etkinlik eklendi", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddEventActivity.this, "Alanlar boş bırakılamaz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 567 && data != null) {
            eventLocation.setText(data.getStringExtra("address"));
            latitude = data.getDoubleExtra("lat", 0);
            longitude = data.getDoubleExtra("lon", 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createLocationPicker();
        } else {
            Toast.makeText(this, "İzin verilmedi", Toast.LENGTH_SHORT).show();
        }
    }

    private void createDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            mCalendar.set(year, month, dayOfMonth);

            String strDate = sdf.format(mCalendar.getTime());
            eventDate.setText(strDate);

            this.year = year;
            this.month = month;
            this.dayOfMonth = dayOfMonth;
        }, year, month, dayOfMonth);
        dpd.setCancelable(false);
        dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dpd.show();
    }

    @SuppressWarnings("deprecation")
    private void createLocationPicker() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult(intent, 567);
    }

    private void createOutfitPicker() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.rv_dialog);

        RecyclerView mRv = dialog.findViewById(R.id.dialog_rv);
        mRv.setHasFixedSize(true);
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        PickOutfitAdapter outfitAdapter = new PickOutfitAdapter(this, outfits);
        mRv.setAdapter(outfitAdapter);
        dialog.setOnDismissListener(dialog -> {
            if (outfitNo != 0) {
                String s = "Kombin " + outfitNo;
                eventOutfit.setText(s);
            }
        });
        dialog.show();
    }

    private void defineListeners() {
        eventDate.setOnClickListener(v -> createDatePicker());
        eventOutfit.setOnClickListener(v -> createOutfitPicker());
        eventLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddEventActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                createLocationPicker();
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
        addEvent = findViewById(R.id.addEvent);

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