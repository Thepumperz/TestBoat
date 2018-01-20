package com.example.a57050358.testboat.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.example.a57050358.testboat.R;
import com.example.a57050358.testboat.Util.GpsTracker;
import com.example.a57050358.testboat.Util.ProximityIntentReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by 57050358 on 25/12/2560.
 */

public class FragmentMap extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Button Btn1, Btn2, Btn3, Btn4, BtnF;
    private int TEST;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private final LatLng mDefaultLocation = new LatLng(13.7244416,100.3529084);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private static final String KEY_LOCATION = "location";

    protected GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates = false;
    GpsTracker gps;
    MarkerOptions markerOptions;
    LatLng latLng;
    LocationManager locationManager;
    int Rd = 175;
    ProximityIntentReceiver proximityIntentReceiver;

    public FragmentMap() {
        super();
    }

    public static FragmentMap newInstance() {
        FragmentMap fragment = new FragmentMap();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        gps = new GpsTracker(getContext());

        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.example.a57050358.testboat.R.layout.fragment_boat, container, false);
        initInstances(rootView, savedInstanceState);
        getLocationPermission();
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        proximityIntentReceiver = new ProximityIntentReceiver();
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState
        Btn1 = rootView.findViewById(R.id.Btn1);
        Btn2 = rootView.findViewById(R.id.Btn2);
        Btn3 = rootView.findViewById(R.id.Btn3);
        Btn4 = rootView.findViewById(R.id.Btn4);
        BtnF = rootView.findViewById(R.id.BtnF);
        Btn1.setOnClickListener(this);
        Btn2.setOnClickListener(this);
        Btn3.setOnClickListener(this);
        Btn4.setOnClickListener(this);
        BtnF.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String location = "KMITL";
        int v = view.getId();
        if (v == R.id.Btn1) {
            //Rd = 100;
            Rd = getRadius(1,location);
        }
        else if (v == R.id.Btn2) {
            // Rd = 200;
            Rd = getRadius(2,location);
        }
        else if (v == R.id.Btn3) {
            // Rd = 1000;
            Rd = getRadius(3,location);
        }
        else if (v == R.id.Btn4){
            // Rd = 2000;
            Rd = getRadius(4,location);
        }
        else if (v == R.id.BtnF) {
            if (!location.equals("")) {
                new GeocoderTask().execute(location);
            }
        }
        //latLng = new LatLng(13.727390, 100.778842);
    }

    private int getRadius(int b,String location) {
            switch (b) {

                case 1:
                    Rd = 2900;
                    break;
                case 2:
                    Rd = 3200;
                    break;
                case 3:
                    Rd = 3210;
                    break;
                case 4:
                    Rd = 3220;
                    break;
            }
        new GeocoderTask().execute(location);
        return Rd;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mRequestingLocationUpdates) {
            //startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

    @SuppressLint("MissingPermission")
    /**


    /**
     * Prompts the user for permission to use the device location.
     */

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(locationName[0], 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @SuppressLint("MissingPermission")
        @Override
        protected void onPostExecute(List<Address> addresses) {

            //Log.i("Location","address = "+addresses);

            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            if (addresses != null) {
                Toast.makeText(getContext(), "Location found", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < addresses.size(); i++) {

                    Address address = addresses.get(i);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    String addressText = String.format("%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getCountryName());

                    markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(addressText);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(latLng);
                    circleOptions.radius(Rd);


                    Intent intent = new Intent("com.example.a57050358.testboat.util.proximityintentreceiver");
                    PendingIntent proximityIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

                    locationManager.addProximityAlert(address.getLatitude(), address.getLongitude(), Rd, -1, proximityIntent);

                }
                IntentFilter filter = new IntentFilter ("com.example.a57050358.testboat.util.proximityintentreceiver");
                getContext().registerReceiver(new ProximityIntentReceiver(), filter);

            }
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

}
