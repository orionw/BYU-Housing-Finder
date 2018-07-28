package com.orionweller.collegehousing;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static com.orionweller.collegehousing.ApartmentTabView.apartmentList;
import static com.orionweller.collegehousing.ApartmentTabView.markers;
import static com.orionweller.collegehousing.ApartmentTabView.pinsDoneLoading;
import static java.lang.Thread.sleep;

public class Tab_2_MapsActivity extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Location mLastLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tab_2__maps, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // 1
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return rootView;

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true); // 1

        LocationAvailability locationAvailability =
                LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient); // 2
        if (null != locationAvailability && locationAvailability.isLocationAvailable()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient); // 3

            LatLng BYU = new LatLng(40.251782, -111.649356);

            if (mLastLocation != null) {    // 4
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(BYU, 12));
//                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation
//                        .getLongitude());
//                placeMarkerOnMap(BYU);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 2
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 3
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

//    private class GetMapInfo extends AsyncTask<Void, Void, Void>
//    {
//        ProgressDialog pdLoading = new ProgressDialog(getContext());
//        ArrayList<MarkerOptions> markers;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading...");
//            pdLoading.show();
//        }
//        @Override
//        protected Void doInBackground(Void... params) {
//            markers = setAllLocations();
//            //this method will be running on background thread so don't update UI frome here
//            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            for (MarkerOptions pin : markers) {
//                mMap.addMarker(pin);
//            }
//
//            //this method will be running on UI thread
//
//            pdLoading.dismiss();
//        }
//
//    }

    // only load this data on click
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ProgressDialog pdLoading = new ProgressDialog(getContext());
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
            int i = 0;
            while (!pinsDoneLoading) {
                i += 1;
            }
            Log.d("Been waiting for: ",  Integer.toString(i));
            // load data here
            for (MarkerOptions pin : markers) {
                mMap.addMarker(pin);
            }
            pdLoading.dismiss();
            setUpMap();
        }else{
            // fragment is no longer visible
            Log.d(TAG, "setUserVisibleHint: not in view ");
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}

// Extra functions not used that I might use later

    // from LatLong place an item on map
//    protected void placeMarkerOnMap(LatLng location) {
//        MarkerOptions markerOptions = new MarkerOptions().position(location); //1
//        String titleStr = "BYU";
//        markerOptions.title(titleStr);
//
//        mMap.addMarker(markerOptions); //2
//    }

//      From lat/long get address
//    private String getAddress( LatLng latLng ) {
//        Geocoder geocoder = new Geocoder( getActivity() ); // 1
//        String addressText = "";
//        List<Address> addresses;
//        Address address;
//        try {
//            addresses = geocoder.getFromLocation( latLng.latitude, latLng.longitude, 1 ); // 2
//            if (null != addresses && !addresses.isEmpty()) { // 3
//                address = addresses.get(0);
//                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                    addressText += (i == 0)?address.getAddressLine(i):("\n" + address.getAddressLine(i));
//                }
//            }
//        } catch (IOException e ) {
//        }
//        return addressText;
//    }

