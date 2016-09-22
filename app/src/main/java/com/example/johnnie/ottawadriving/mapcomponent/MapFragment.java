package com.example.johnnie.ottawadriving.mapcomponent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.model.PersonModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnnie on 2016-09-22.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , LocationListener {

    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_FINE_LOCATION = 0;
    //private LocationRequest mLocationRequest;
    private static final double
            SEATTLE_LAT = 47.60621,
            SEATTLE_LNG = -122.33207;

    private GoogleApiClient mLocationClient;
    private Location currentLocation;
    private View mapView;
    private Marker marker;
    private ArrayList<Marker> markers = new ArrayList<>();

    private FragmentActivity mActivity;
    private static final String PERSONMODELS= "PersonModels";
    private ArrayList<PersonModel> mModels;


    public ArrayList<Marker> getMarkers() {
        return this.markers;
    }


    public interface OnMapFragmentClicked {
        public void GoToMapActivity();
    }


    private static OnMapFragmentClicked sDummyCallbacks = new OnMapFragmentClicked() {
        @Override
        public void GoToMapActivity() {
            Log.d("GOTOMAPACTIVITY", "call in map fragment");
        }
    };


    private OnMapFragmentClicked mCallbacks = sDummyCallbacks;


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public static MapFragment newInstance(ArrayList<PersonModel> models) {
        MapFragment fragment = new MapFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(PERSONMODELS, models);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static MapFragment newInstance(Bundle savedState) {
        MapFragment fragment = new MapFragment();
        fragment.setArguments(savedState);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mModels = (ArrayList<PersonModel>) args.getSerializable(PERSONMODELS);
        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        // loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
        setHasOptionsMenu(true);
        getMapAsync(this);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mapView = super.onCreateView(inflater, container, savedInstanceState);

        return mapView;

    }


    // implement GoogleApiClient.ConnectionCallbacks interface
    @Override
    public void onConnected(Bundle bundle) {

    }

    // implement GoogleApiClient.ConnectionCallbacks interface
    @Override
    public void onConnectionSuspended(int i) {

    }


    // implement LocationListener interface
    @Override
    public void onLocationChanged(Location location) {
        if (currentLocation == null) {

            currentLocation = location;
            LatLng latLng = new LatLng(
                    currentLocation.getLatitude(), currentLocation.getLongitude()
            );
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(update);
        }
    }

    // implement LocationListener interface
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    // implement LocationListener interface
    @Override
    public void onProviderEnabled(String provider) {

    }

    // implement LocationListener interface
    @Override
    public void onProviderDisabled(String provider) {

    }

    // implement GoogleApiClient.OnConnectionFailedListener interface
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("connection result", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    // implement OnMapReadyCallback interface
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //set my current location
        mLocationClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mLocationClient.connect();
       // end of set my current location


        //initialize map location based on model data
        if (mModels != null) {
            try {
                geoLocate(mModels);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // set map info windows
        this.setInfoWindows(mMap);

    }



    // ======================= Helper methods ===============================

    private void setInfoWindows(GoogleMap map){
        if (map != null) {
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater(null).inflate(R.layout.map_infowindow, null);
                    TextView tvLocality = (TextView) v.findViewById(R.id.tvLocality);
                    tvLocality.setText(marker.getTitle());
                    return v;
                }
            });

            // set info window onClicked event
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(getActivity(), null);
                    Bundle bundle = new Bundle();
                    bundle.putString("message", marker.getTitle());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }



    private void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), perm)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{perm}, requestCode);
            }
        }
    }


    public void geoLocate(List<PersonModel> models) throws IOException {
        Log.d("mActivityTest", "geo called");
        Geocoder gc = new Geocoder(mActivity);
        double lat = 0;
        double lng = 0;
        //mModels = (ArrayList<PersonModel>) models;
        for (PersonModel model : models) {
            String searchString = model.getAddress();
            List<Address> list = gc.getFromLocationName(searchString, 1);

            if (list.size() > 0) {
                Address address = list.get(0);
                String locality = address.getLocality();
                //Toast.makeText(getActivity(), "Found" + locality, Toast.LENGTH_SHORT).show();
                lat = address.getLatitude();
                lng = address.getLongitude();
                addMarker(model, lat, lng);
            }
        }
        gotoLocationWhenMarkers();

    }

    private void gotoLocationWhenMarkers() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //initialize myLocation red mark
        if(currentLocation != null){
            MarkerOptions options = new MarkerOptions()
                    .title("My Location")
                    .position(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markers.add(mMap.addMarker(options));
        }
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (width * 0.30);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(update);
    }


//    private void gotoLocation(double lat, double lng, float zoom) {
//        LatLng latLng = new LatLng(lat, lng);
//        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
//        mMap.moveCamera(update);
//
//    }


    public void markersFresh() {
        if (markers.size() > 0) {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
    }

    private void addMarker(PersonModel model, double lat, double lng) {
        //Title:address,Id,category,name,info,faq
        String title = model.getAddress();
        MarkerOptions options = new MarkerOptions()
                .title(title)
                .position(new LatLng(lat, lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        String address = model.getAddress();
        if (address.length() > 0) {
            options.snippet(address);
        }
        marker = mMap.addMarker(options);
        markers.add(marker);
    }


    public void searchMarkerByTitle(String Address){
        for(Marker marker: markers){
            if(marker.getTitle().endsWith(Address)){
                marker.showInfoWindow();

                LatLng markerLatLng = new LatLng(marker.getPosition().latitude,
                        marker.getPosition().longitude);
                CameraUpdate center = CameraUpdateFactory.newLatLng(markerLatLng);
                mMap.moveCamera(center);
                marker.showInfoWindow();
                return;
            }
        }
    }




// ======================= Helper methods end ===============================


    @Override
    public void onActivityCreated(Bundle savedinstanceState) {
        super.onActivityCreated(savedinstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("mActivityTest", "attach called");
        this.mActivity = (FragmentActivity) activity;
        if (mActivity == null) {
            Log.d("mActivityTest", "mActivity is null in Attach method");
        }
        if (!(activity instanceof OnMapFragmentClicked)) {
            throw new ClassCastException(
                    "Activity must implement fragment's callbacks.");
        }

        mCallbacks = (OnMapFragmentClicked) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("mActivityTest", "on Detach  called");
        mCallbacks = sDummyCallbacks;

    }


    @Override
    public void onStop() {
        super.onStop();
        mLocationClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}