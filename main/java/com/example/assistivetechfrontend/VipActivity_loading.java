package com.example.assistivetechfrontend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class VipActivity_loading extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String name;

    private EditText etSessionId;
    private TextView tvName;
    private Location location;
    private TextView locationTv;
    private Button joinSession;

    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 second

    // lists for permission
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    // integer for permissions results request
    private static final int ALL_PERMISSION_RESULT = 1011;

    //public static TextView data;

    private int sessionId;
    private int userId;

    private CommunicationAPI communicationAPI;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_loading);

        //get from login
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        communicationAPI = new CommunicationAPI();

        //textview for the current vip location
        locationTv = findViewById(R.id.vipLocation);
        //textview for the name
        tvName = (TextView)findViewById(R.id.vipName);
        tvName.setText(name);

        //get the sessionId from the edittext
        etSessionId = (EditText)findViewById(R.id.inputSessionId);


        //set up the conclick listener for the button "join session"
        joinSession = (Button)findViewById(R.id.joinSession);
        joinSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etSessionId.getText().toString().isEmpty())
                    sessionId = Integer.parseInt(etSessionId.getText().toString());
                new joinSessionTask().execute();
            }
        });

        // we add permissions, we need to request location of users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (permissionsToRequest.size()>0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSION_RESULT);
            }
        }

        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();


        //textview for all locations in the session
        //data = (TextView)findViewById(R.id.allLocations);

    }

    // if we don't have the permission, add permissionsToRequest then requestPermissions
    private ArrayList<String> permissionsToRequest (ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();
        for(String perm: wantedPermissions) {
            if(!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    // check if already has the permission
    private boolean hasPermission(String permission) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(googleApiClient!=null){
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
// if you don't have the google play services in your phone
        if(!checkPlayServices()){
            locationTv.setText("You need to install Google Play Services to use the App properly");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // stop location updates and disconnect google play services
        if(googleApiClient!=null && googleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if(resultCode!= ConnectionResult.SUCCESS) {
            if(apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this,resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }
            return false;
        }

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }

        // Permission of, we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location!=null){
            locationTv.setText("Latitude : "+location.getLatitude() + "\nLongitude : "+location.getLongitude());
        }

        startLocationUpdates();
    }

    private void startLocationUpdates(){
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"You need to enable permission to display location !", Toast.LENGTH_SHORT).show();
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            locationTv.setText("Latitude : "+location.getLatitude() + "\nLongitude : "+location.getLongitude());
        }
    }

    // apply for user's permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSION_RESULT:
                for(String perm :permissionsToRequest) {
                    if(!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if(permissionsRejected.size()>0) {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(VipActivity_loading.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]),
                                                        ALL_PERMISSION_RESULT);
                                            }
                                        }
                                    }).
                                    setNegativeButton("Cancel", null).create().show();
                            return;
                        }
                    }
                }else {
                    if(googleApiClient!=null) {
                        googleApiClient.connect();
                    }
                }

                break;
        }
    }

    class joinSessionTask extends AsyncTask<String,Integer,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            Integer result = -1;
            try {
                result = communicationAPI.joinSession(name, location.getLatitude(),location.getLongitude(),sessionId,"http://10.0.2.2:5000/api/vip/joinSession");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            userId = result;
            Intent intent = new Intent(VipActivity_loading.this, GuideActivity_session.class);
            intent.putExtra("name", name);
            intent.putExtra("sessionId", sessionId);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }
    }

}
