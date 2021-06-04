package tr.edu.yildiz.virtualcloset;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            Log.d("mytag", "fragment null deel");
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d("mytag", "map ready");
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMapClickListener(latLng -> {
            Log.d("mytag", "onMapClick");
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            markerOptions.title("");
            mMap.clear();
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    latLng, 15);
            mMap.animateCamera(location);
            mMap.addMarker(markerOptions);
            getAddress(latLng);
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.d("mytag", "onMyLocationButtonClick");
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Log.d("mytag", "onMyLocationClick");
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }


    private void getAddress(LatLng latLng){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
           /* String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();*/

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
                Log.d("mytag", "accepted");
            });

            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("mytag", "onLocationChanged");
        LatLng ltlng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                ltlng, 16f);
        mMap.animateCamera(cameraUpdate);
    }
}