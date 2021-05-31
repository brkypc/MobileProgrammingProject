package tr.edu.yildiz.virtualcloset;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import tr.edu.yildiz.virtualcloset.Database.DatabaseHelper;
import tr.edu.yildiz.virtualcloset.Model.Clothes;

public class AddClothesActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 123;
    private static final int PERMISSION_CODE = 124;

    DatabaseHelper databaseHelper;

    ImageView photo;
    Spinner type, pattern;
    TextView color, date;
    EditText price;
    Button addClothes;

    Uri imgUri = null;
    String hexColor = null, finalColor = null;
    int drawerNo;

    Calendar calendar, mCalendar;
    SimpleDateFormat sdf;
    int year, month, dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        Intent intent = getIntent();
        drawerNo = intent.getIntExtra("drawerNo", -1);

        defineVariables();
        defineListeners();
    }

    private void defineListeners() {
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddClothesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddClothesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                }
                else { pickImageFromGallery(); }
            }
        });

        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()) {
                    byte[] image = getByteArray();;
                    if(image != null) {
                        String sType, sPattern, sDate, sPrice;
                        sType = type.getItemAtPosition(type.getSelectedItemPosition()).toString();
                        sPattern = pattern.getItemAtPosition(pattern.getSelectedItemPosition()).toString();
                        sDate = date.getText().toString();
                        sPrice = price.getText().toString();

                        databaseHelper.addClothes(new Clothes(drawerNo, sType, finalColor, sPattern, sDate, sPrice, image));
                        Toast.makeText(AddClothesActivity.this, "Kıyafet eklendi", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AddClothesActivity.this, "Alanlar boş bırakılamaz", Toast.LENGTH_SHORT).show();
                }
            }
        });

        color.setOnClickListener(v -> createColorPicker());
        date.setOnClickListener(v -> createDatePicker());
    }

    private byte[] getByteArray() {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imgUri);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int bufferSize = 1024;

            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            outputStream.close();

            return outputStream.toByteArray();

        }
        catch (Exception e) { Log.d("virtualCloset", e.getMessage()); return null; }
    }

    @SuppressWarnings("deprecation")
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            imgUri = data.getData();
            photo.setImageURI(imgUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery();
        } else {
            Toast.makeText(this, "İzin verilmedi", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        if (imgUri == null || finalColor == null) return false;
        else if(type.getSelectedItemPosition()==0 || pattern.getSelectedItemPosition()==0) return false;
        else if (date.getText().toString().startsWith("A")) return false;
        else return !price.getText().toString().isEmpty();
    }

    private void createDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            mCalendar.set(year, month, dayOfMonth);

            String strDate = sdf.format(mCalendar.getTime());
            date.setText(strDate);

            this.year = year; this.month = month; this.dayOfMonth = dayOfMonth;
        }, year, month, dayOfMonth);
        dpd.setCancelable(false);
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpd.show();
    }

    private void createColorPicker() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Renk Seçiniz")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        hexColor =  "#" + Integer.toHexString(selectedColor).substring(2);
                    }
                })
                .setPositiveButton("Seç", new ColorPickerClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                       if(hexColor!=null) {
                           finalColor = hexColor;
                           color.setText("Seçilen Renk");
                           color.setBackgroundColor(Color.parseColor(finalColor));
                           Toast.makeText(AddClothesActivity.this, "Renk seçildi", Toast.LENGTH_SHORT).show();
                       }
                       else {
                           Toast.makeText(AddClothesActivity.this, "Renk seçmediniz", Toast.LENGTH_SHORT).show();
                       }
                    }
                })
                .setNegativeButton("İptal", null)
                .build()
                .show();
    }

    @SuppressLint("SimpleDateFormat")
    private void defineVariables() {
        databaseHelper = new DatabaseHelper(this);

        photo = findViewById(R.id.photo);
        type = findViewById(R.id.type);
        pattern = findViewById(R.id.pattern);
        color = findViewById(R.id.color);
        price = findViewById(R.id.price);
        addClothes = findViewById(R.id.addClothesButton);

        date = findViewById(R.id.date);
        calendar = Calendar.getInstance();
        mCalendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        sdf = new SimpleDateFormat("dd/MM/yyyy");;

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_list_item_1);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);

        ArrayAdapter<CharSequence> patternAdapter = ArrayAdapter.createFromResource(this, R.array.pattern, android.R.layout.simple_list_item_1);
        patternAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pattern.setAdapter(patternAdapter);
    }
}