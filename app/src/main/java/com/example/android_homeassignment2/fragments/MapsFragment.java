package com.example.android_homeassignment2.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.android_homeassignment2.FeedbackHandler;
import com.example.android_homeassignment2.R;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {
    private final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PermissionGrantedCallback permissionGrantedCallback;
    private final MapLoadedCallback mapLoadedCallback;

    public MapsFragment(@Nullable MapLoadedCallback mapLoadedCallback) {
        this.mapLoadedCallback = mapLoadedCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        initializeGoogleMaps();
        return view;
    }

    public void addLocationMarker(String locationTitle, double latitude, double longitude) {
        LatLng location = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(location).title(locationTitle));
    }

    public void zoomToLocation(double latitude, double longitude) {
        LatLng userLocation = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, 15);
        map.moveCamera(cameraUpdate);
    }

    public void getCurrentUserLocation(LocationRetrievedCallback locationRetrievedCallback) {
        // Check for location permission
        permissionGrantedCallback = new PermissionGrantedCallback() {
            @Override
            public void onLocationAvailable() {
                getCurrentUserLocation(locationRetrievedCallback);
            }

            @Override
            public void onLocationNotAvailable() {
                locationRetrievedCallback.onLocationNotAvailable();
            }
        };

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(permissions);
            return;
        }

        // Check the GPS setting is turned on.
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGpsEnabled) {
            askToActivateGPS();
            return;
        }
        permissionGrantedCallback = null;
        CurrentLocationRequest locationRequest = new CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();
        fusedLocationProviderClient.getCurrentLocation(locationRequest, null).addOnSuccessListener(location -> {

            if (location == null) {
                FeedbackHandler.getInstance().toast(getString(R.string.unable_to_locate) + "...");
                locationRetrievedCallback.onLocationNotAvailable();
                return;
            }
            locationRetrievedCallback.onLocationRetrieved(location.getLatitude(), location.getLongitude());
        });
    }

    private void askToActivateGPS() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setMessage(getString(R.string.enable_gps_prompt) + ".");
        dialogBuilder.setPositiveButton(getString(R.string.enable_gps_button), (dialog, which) -> {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            locationSettingsLauncher.launch(settingsIntent);
        });
        dialogBuilder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            FeedbackHandler.getInstance().toast(getString(R.string.gps_warning) + "!");
            if (permissionGrantedCallback != null) {
                permissionGrantedCallback.onLocationNotAvailable();
            }
            permissionGrantedCallback = null;
        });
        dialogBuilder.create().show();
    }

    private void initializeGoogleMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_MAP_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                map = googleMap;
                if (mapLoadedCallback != null)
                    mapLoadedCallback.onMapLoaded();
            });
        }
    }

    // Initialize the permission launcher
    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                // Check if all permissions are granted
                boolean allPermissionsGranted = true;
                for (Boolean isGranted : result.values()) {
                    if (!isGranted) {
                        allPermissionsGranted = false;
                        break;
                    }
                }

                // Handle the result
                if (allPermissionsGranted) {
                    // Permissions granted, do something here
                    if (permissionGrantedCallback != null) {
                        permissionGrantedCallback.onLocationAvailable();
                    }
                } else {
                    // Permissions denied, show an error message
                    FeedbackHandler.getInstance().toast(getString(R.string.permissions_denied));
                    if (permissionGrantedCallback != null) {
                        permissionGrantedCallback.onLocationNotAvailable();
                    }
                }
//                permissionGrantedCallback = null;
            });

    // Initialize the permission launcher
    private final ActivityResultLauncher<Intent> locationSettingsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {

                LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                // Handle the result
                if (isGpsEnabled) {
                    if (permissionGrantedCallback != null) {
                        permissionGrantedCallback.onLocationAvailable();
                    }
                } else {
                    // GPS setting is still disabled, show an error message
                    FeedbackHandler.getInstance().toast(getString(R.string.gps_warning));
                    if (permissionGrantedCallback != null) {
                        permissionGrantedCallback.onLocationNotAvailable();
                    }
                }
//                permissionGrantedCallback = null;
            });

    public interface LocationRetrievedCallback {
        void onLocationRetrieved(double latitude, double longitude);

        void onLocationNotAvailable();
    }

    public interface MapLoadedCallback {
        void onMapLoaded();
    }

    private interface PermissionGrantedCallback {
        void onLocationAvailable();

        void onLocationNotAvailable();
    }
}
