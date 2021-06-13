package tr.edu.yildiz.virtualcloset;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng mLatLng;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        LatLng lng = new LatLng(40.99055195081905, 29.02918425499755);
        CameraUpdate starting = CameraUpdateFactory.newLatLngZoom(
                lng, 10);
        mMap.animateCamera(starting);

        mMap.setOnMapClickListener(latLng -> {
            mLatLng = latLng;
            mMap.clear();

            address = getAddress(latLng);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(address);

            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(latLng, 15);

            mMap.animateCamera(location);
            mMap.addMarker(markerOptions);
        });

        mMap.setOnMarkerClickListener(marker -> {
            confirmAddress(mLatLng);
            return false;
        });
    }

    private String getAddress(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            return addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return true;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    private void confirmAddress(LatLng latLng) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirm_dialog);

        TextView dialogAddress = dialog.findViewById(R.id.address);
        dialogAddress.setText(address);

        Button dialogDismiss = dialog.findViewById(R.id.pickNew);
        dialogDismiss.setOnClickListener(v -> {
            dialog.dismiss();
            Intent data = new Intent();
            String text = "no pick";
            data.setData(Uri.parse(text));
            setResult(RESULT_CANCELED, data);
        });

        Button dialogAccept = dialog.findViewById(R.id.acceptLocation);
        dialogAccept.setOnClickListener(v -> {
            dialog.dismiss();
            Intent data = new Intent();
            data.putExtra("lat", latLng.latitude);
            data.putExtra("lon", latLng.longitude);
            data.putExtra("address", address);
            setResult(RESULT_OK, data);
            finish();
        });

        dialog.show();
    }
}