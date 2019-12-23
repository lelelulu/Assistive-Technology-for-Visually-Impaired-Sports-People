package com.example.assistivetechfrontend;

import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.IOException;
import java.util.ArrayList;

//this class implements Google map and displays multiple locations of users in it
public class Mapfragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener
{

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;


    private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE };
    private int curMapTypeIndex = 0;

    //locations for test
    private LatLng locate1 = new LatLng(-35.422, 149.084);
    ArrayList<String> finals = new ArrayList<>();
    ArrayList<LatLng> locs = new ArrayList<LatLng>(){};




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mGoogleApiClient = new GoogleApiClient.Builder( getActivity() )
                .addConnectionCallbacks( this )
                //.addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
                .build();

        initListeners();
        dealwithlocs();
        drawPath(locs);
    }
    /**
     * This method processes the data sent from backend
     */
    public void dealwithlocs()
    {
        String q = "null{\"list of locaton\":[[\"1345\",\"Ruichi Wu\",\"-35.422\",\"149.084\"],[\"2\",\"Wu Ruichi\",\"-35.424\",\"149.082\"],[\"7\",\"Le fang\",\"-35.421\",\"149.080\"]]}null";
        processdata(q);
    }

    /**
     * This method can get address from LatLng
     */
    private String getAddressFromLatLng( LatLng latLng ) {
        Geocoder geocoder = new Geocoder( getActivity() );
        String address = "";
        try {
            address = geocoder.getFromLocation( latLng.latitude, latLng.longitude, 1 ).get( 0 ).getAddressLine( 0 );
        } catch (IOException e ) {
        }

        return address;
    }
    /**
     * This method formulates the data
     */
    public void processdata(String data) {
        System.out.println("initial data is: "+data);
        data = data.trim();
        String[] vs = data.split(":");
        String dataFlow = vs[1];
        String[] info = dataFlow.split("\"");

        for (int i = 0; i < info.length; i++) {
            String current = info[i];
            if(!(current.contains("[") || current.contains("]")||current.contains(","))) {
                finals.add(current);
            }
        }

        for(int i = 0; i < finals.size()/4; i++) {
            String ID = finals.get(4*i);
            String Name = finals.get(4*i+1);
            String Lat = finals.get(4*i+2);
            String Lon = finals.get(4*i+3);
            LatLng location = new LatLng(Double.parseDouble(Lat),Double.parseDouble(Lon));
            locs.add(location);
            placeMarkerOnMap(location,ID,Name);
        }
    }

    /**
     * This method places the Marker on the map with title(id,name)
     */
    protected void placeMarkerOnMap(LatLng location,String id,String name) {
        MarkerOptions markerOptions = new MarkerOptions().position(location); //1
        String titleStr = getAddressFromLatLng(location);
        markerOptions.title(titleStr);
        String info = id+","+name;
        getMap().addMarker(markerOptions.position(location).title(info));
    }

    /**
     * This method draws the path by using given locations data
     */
    private void drawPath(ArrayList<LatLng> pathnodes)
    {
        PolylineOptions plO = new PolylineOptions( );
        plO.color(getResources().getColor(R.color.Black));
        plO.geodesic( true );
        for ( LatLng latLng : pathnodes )
        {
            plO.add( latLng );

        }
        getMap().addPolyline(plO);

    }


    private void initListeners() {
        getMap().getUiSettings().setZoomControlsEnabled(true);
        getMap().setOnMarkerClickListener(this);
        getMap().setOnInfoWindowClickListener( this );
    }

    private void removeListeners() {
        if( getMap() != null ) {
            getMap().setOnMarkerClickListener( null );
            getMap().setOnMapLongClickListener(null);
            getMap().setOnInfoWindowClickListener(null);
            getMap().setOnMapClickListener(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        removeListeners();
    }
    /**
     * This method initializes the camera of google map
     */
    private void initCamera() {
        CameraPosition position = CameraPosition.builder().target( new LatLng( locate1.latitude,locate1.longitude ) )
                .zoom( 16f ).bearing( 0.0f ).tilt( 0.0f ).build();

        getMap().animateCamera( CameraUpdateFactory.newCameraPosition( position ), null );

        getMap().setMapType( MAP_TYPES[curMapTypeIndex] );
        getMap().setTrafficEnabled( true );
        getMap().setMyLocationEnabled( true );
        getMap().getUiSettings().setZoomControlsEnabled( true );
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        initCamera();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //handle play services disconnecting if location is being constantly used
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText( getActivity(), "Clicked on marker", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (null != mCurrentLocation) {
            placeMarkerOnMap(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),"","");
        }

    }


}
