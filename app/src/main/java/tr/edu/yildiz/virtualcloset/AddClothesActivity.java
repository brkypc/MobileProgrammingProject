package tr.edu.yildiz.virtualcloset;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    ArrayAdapter<CharSequence> typeAdapter, patternAdapter;

    ImageView photo;
    Spinner type, pattern;
    TextView color, date;
    EditText price;
    Button addClothes;

    Animation scaleUp, scaleDown;

    Uri imgUri = null;
    String hexColor = null, finalColor = null;
    int drawerNo, count, clothesId;
    byte[] image;

    Calendar calendar, mCalendar;
    SimpleDateFormat sdf;
    int year, month, dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        defineVariables();

        Intent intent = getIntent();
        drawerNo = intent.getIntExtra("drawerNo", -1);
        if(intent.hasExtra("update")) {
            clothesId = intent.getIntExtra("clothesId", -1);
            setFields();
            defineUpdateListener();
        }
        else {
            count = intent.getIntExtra("count", 0);
            defineAddListener();
        }

        defineListeners();
    }

    private void setFields() {
        Clothes clothes = databaseHelper.getClothes(clothesId);

        image = clothes.getPhoto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        photo.setImageBitmap(bitmap);

        finalColor = clothes.getColor();
        color.setText(R.string.clothes_color);
        color.setBackgroundColor(Color.parseColor(finalColor));

        date.setText(clothes.getDate());
        price.setText(clothes.getPrice());
        type.setSelection(typeAdapter.getPosition(clothes.getType()));
        pattern.setSelection(patternAdapter.getPosition(clothes.getPattern()));
    }

    private void defineAddListener() {
        addClothes.setOnClickListener(v -> {
            addClothes.startAnimation(scaleUp);
            addClothes.startAnimation(scaleDown);
            if(validateFields()) {
                byte[] image = getByteArray();
                if(image != null) {
                    String sType, sPattern, sDate, sPrice;
                    sType = type.getItemAtPosition(type.getSelectedItemPosition()).toString();
                    sPattern = pattern.getItemAtPosition(pattern.getSelectedItemPosition()).toString();
                    sDate = date.getText().toString();
                    sPrice = price.getText().toString();
                    databaseHelper.addClothes(new Clothes(drawerNo, sType, finalColor, sPattern, sDate, sPrice, image));
                    databaseHelper.updateDrawer(drawerNo, count+1);
                    Toast.makeText(AddClothesActivity.this, "Kıyafet eklendi", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(AddClothesActivity.this, "Alanlar boş bırakılamaz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void defineUpdateListener() {
        addClothes.setText(R.string.update);
        addClothes.setOnClickListener(v -> {
            addClothes.startAnimation(scaleUp);
            addClothes.startAnimation(scaleDown);
            if(validateUpdateFields()) {
                if(imgUri != null) {
                    image = getByteArray();
                }

                if(image != null) {
                    String sType, sPattern, sDate, sPrice;
                    sType = type.getItemAtPosition(type.getSelectedItemPosition()).toString();
                    sPattern = pattern.getItemAtPosition(pattern.getSelectedItemPosition()).toString();
                    sDate = date.getText().toString();
                    sPrice = price.getText().toString();
                    databaseHelper.updateClothes(new Clothes(drawerNo, sType, finalColor, sPattern, sDate, sPrice, image), clothesId);
                    Toast.makeText(AddClothesActivity.this, "Kıyafet güncellendi", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(AddClothesActivity.this, "Alanlar boş bırakılamaz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void defineListeners() {
        photo.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(AddClothesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AddClothesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
            }
            else { pickImageFromGallery(); }
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

            int len;
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

    private boolean validateUpdateFields() {
        if(type.getSelectedItemPosition()==0 || pattern.getSelectedItemPosition()==0) return false;
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

    @SuppressLint("SetTextI18n")
    private void createColorPicker() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Renk Seçiniz")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> hexColor =  "#" + Integer.toHexString(selectedColor).substring(2))
                .setPositiveButton("Seç", (dialog, selectedColor, allColors) -> {
                   if(hexColor!=null) {
                       finalColor = hexColor;
                       color.setText("Kıyafet Rengi");
                       color.setBackgroundColor(Color.parseColor(finalColor));
                       Toast.makeText(AddClothesActivity.this, "Renk seçildi", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       Toast.makeText(AddClothesActivity.this, "Renk seçmediniz", Toast.LENGTH_SHORT).show();
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
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scaleDown.setStartOffset(100);

        typeAdapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_list_item_1);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);

        patternAdapter = ArrayAdapter.createFromResource(this, R.array.pattern, android.R.layout.simple_list_item_1);
        patternAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pattern.setAdapter(patternAdapter);
    }
}