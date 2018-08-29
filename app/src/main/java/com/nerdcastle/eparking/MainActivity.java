package com.nerdcastle.eparking;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;
import com.nerdcastle.eparking.Activities.LoginActivity;
import com.nerdcastle.eparking.Activities.SignUpActivity;
import com.nerdcastle.eparking.CustomLayout.MapWrapperLayout;
import com.nerdcastle.eparking.Fragments.ActivityFragment;
import com.nerdcastle.eparking.Fragments.PaymentFragment;
import com.nerdcastle.eparking.OtherClasses.CustomClusterRenderer;
import com.nerdcastle.eparking.OtherClasses.LoginPreferences;
import com.nerdcastle.eparking.OtherClasses.OnInfoWindowElemTouchListener;
import com.nerdcastle.eparking.OtherClasses.TempData;
import com.nerdcastle.eparking.PoJoClasses.Consumer;
import com.nerdcastle.eparking.PoJoClasses.MyItems;
import com.nerdcastle.eparking.PoJoClasses.ParkPlace;
import com.nerdcastle.eparking.PoJoClasses.Provider;
import com.nerdcastle.eparking.PoJoClasses.Request;
import com.nerdcastle.eparking.PoJoClasses.StatusOfConsumer;
import com.nerdcastle.eparking.PoJoClasses.TempHolder;
import com.nerdcastle.eparking.WebApis.WebApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
        ,OnMapReadyCallback
        ,ActivityFragment.ActivityFragmentInterface
        ,PaymentFragment.PaymentFragmentInterface{



    //------------Custom Dialogs ---------
    private Dialog mGpsDialog;
    private Dialog mInternetDialog;

    //---------- Navigatin Header Info ----------
    private TextView mUserName;
    private TextView mUserEmailAddress;
    private CircleImageView mProfileImage;
    //-------------------------------------------


    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseUserInformation;
    private DatabaseReference mFirebaseRequestRef;
    private DatabaseReference mDatabaseConsumerStatus;
    private FirebaseUser mCurrentUser;

    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback callback;
    private Double latitude,longitude;
    private ClusterManager<MyItems>clusterManager;
    private GoogleMap map;
    private GoogleMapOptions options;
    private LatLng mCurrentLocation;
    private WebApi mWebApi;
    private CustomClusterRenderer renderer;
    private Boolean isMapInitialized = false;


    private MapWrapperLayout mapWrapperLayout;
    private ViewGroup infoWindow;
    private ViewGroup mCurrentLocationWindowBox;
    private TextView infoTitle;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;

    private FrameLayout fragmentContainer;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;

    private TextView mArivalTime,mLeavingTime;
    private LinearLayout mHeaderMenu;
    private ProgressDialog progressDialog;

    private List<Provider> providerList = new ArrayList<>();
    private List<ParkPlace> ParkPlaceList = new ArrayList<>();
    private String mRequestID;
    private String mConsumerID;
    private Request mRequest;
    private Boolean mInternetStatus;
    LoginPreferences mLoginPreference;

    //------------- Time Picking -----------------
    private Calendar calendar;
    private int year,day,month;

    //-------------- Notification  --------------------
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mInternetStatus = isNetworkAvailable();
        if (!mInternetStatus)
        {
            showInternetDialogBox();
        }
        //-------------- Custom Dialog -------------------
        mGpsDialog = new Dialog(this);
        mInternetDialog = new Dialog(this);
        statusCheck();
        //-------------------------------------------------
        mLoginPreference = new LoginPreferences(this);
        //---------------------------Navigation --------------------------------

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        mUserName = header.findViewById(R.id.mUserName);
        mUserEmailAddress = header.findViewById(R.id.mUserEmailAddress);
        mProfileImage = header.findViewById(R.id.circularImageView);

        //-------------------------- Firebase ----------------------------------------

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mConsumerID = mAuth.getUid();

        getUserInformation();

        /*
        if (mAuth.getCurrentUser() != null)
        {
            mCurrentUser = mAuth.getCurrentUser();
            mConsumerID = mCurrentUser.getUid();

            if (mCurrentUser.getDisplayName() == null )
            {
                mUserName.setText("");
            } else {
                mUserName.setText(mCurrentUser.getDisplayName());
            }

            if (mCurrentUser.getEmail() == null)
            {
                mUserEmailAddress.setText("");
            } else {

                if (mCurrentUser.getEmail().contains("@mail.com"))
                {
                    mUserEmailAddress.setText("");
                }
            }
        }
        */




        //final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.fragmentContainer);
        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        mArivalTime = findViewById(R.id.arivingTime);
        mLeavingTime = findViewById(R.id.leavingTime);
        mHeaderMenu = findViewById(R.id.headerMenu);
        //mHeaderMenu.setVisibility(View.VISIBLE);

        fragmentContainer = findViewById(R.id.fragmentContainer);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
        //----------------------------------- Picking Time   --------------------------------------

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        mArivalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String AM_PM ;
                        if(selectedHour < 12) {
                            AM_PM = " AM";
                        } else {
                            AM_PM = " PM";
                        }
                        mArivalTime.setText("Ariving \n at "+selectedHour + ":" + selectedMinute+AM_PM);
                        Toast.makeText(MainActivity.this, "dfsdf", Toast.LENGTH_SHORT).show();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        mLeavingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String AM_PM ;
                        if(selectedHour < 12) {
                            AM_PM = " AM";
                        } else {
                            AM_PM = " PM";
                        }
                        mLeavingTime.setText("Leaving \n at "+selectedHour + ":" + selectedMinute+AM_PM);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });




        //------------------------------------------------------------------------------------------




        //----------------------------------- Location ---------------------------------------------

        client = LocationServices.getFusedLocationProviderClient(this);
        request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(10000);
        request.setFastestInterval(5000);
        callback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null){
                    return;
                }
                for(Location location : locationResult.getLocations()){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    if(latitude!=0 && longitude!=0) {
                        if (isMapInitialized == false)
                        {
                            isMapInitialized = true;
                            innitializeMap ();
                        }
                        TempData.latitude = latitude;
                        TempData.longitude = longitude;
                        //Toast.makeText(MainActivity.this, latitude+", "+longitude, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };




        //------------------------------- End Of location Section ----------------------------------

        getDeviceCurrentLocation();
        getLocationUpdates();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserInformation();
    }


    //----------------------------------------------------------------------------------------------
    //--------------------------------------Map Section --------------------------------------------
    //----------------------------------------------------------------------------------------------



    public void innitializeMap ()
    {
        mGpsDialog.dismiss();
        options = new GoogleMapOptions();
        options.zoomControlsEnabled(true);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,mapFragment);
        ft.commit();
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        clusterManager = new ClusterManager<MyItems>(this,map);
        mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));
        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance

        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window, null);
        this.mCurrentLocationWindowBox = (ViewGroup)getLayoutInflater().inflate(R.layout.dialog_current_location, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.infoButton = (Button)infoWindow.findViewById(R.id.button);


        renderer = new CustomClusterRenderer(this,map,clusterManager);
        clusterManager.setRenderer(renderer);

        map.setOnMarkerClickListener(clusterManager);
        map.setOnCameraIdleListener(clusterManager);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);

        mCurrentLocation = new LatLng(TempData.getLatitude(),TempData.getLongitude());
        map.addMarker(new MarkerOptions().position(mCurrentLocation).title("My Current Location").snippet("Mirpur DOHS, Avenue 9").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation,15));





        /*clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItems>() {
            @Override
            public boolean onClusterItemClick(MyItems item) {
                Toast.makeText(MainActivity.this, "You Clicked "+item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/



        //------------------------------------------------------------------------------------------


        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
                getResources().getDrawable(R.drawable.round_but_green_sel), //btn_default_normal_holo_light
                getResources().getDrawable(R.drawable.round_but_red_sel)) //btn_default_pressed_holo_light
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                Toast.makeText(MainActivity.this, "Request Sent to > "+marker.getTitle(), Toast.LENGTH_SHORT).show();
                sendRequest(marker.getSnippet());
            }
        };
        this.infoButton.setOnTouchListener(infoButtonListener);


        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                Provider provider = new Provider();
                if (marker.getTitle()!= null)
                {

                if (marker.getTitle().equals("My Current Location"))
                {

                    infoTitle.setText("My Current Location");
                    infoSnippet.setText("");
                    return mCurrentLocationWindowBox;
                    //infoButtonListener.setMarker(marker);
                }
                else
                {
                    provider = getProviderByID(marker.getSnippet());
                    infoTitle.setText(provider.getmName());
                    infoSnippet.setText(provider.getmAddress());
                    infoButtonListener.setMarker(marker);
                }
                }else
                    {

                        Toast.makeText(MainActivity.this, "Zoom Out", Toast.LENGTH_SHORT).show();
                        return null;
                    }

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });


        //------------------------------------------------------------------------------------------

        if (mInternetStatus)
        {
            getAllParkOwnerFromFirebase();
        }

        //getNearbyPlaces();

    }

    public void sendRequest (String mProviderID)
    {
        mFirebaseRequestRef = mFirebaseInstance.getReference("ProviderList/"+mProviderID+"/Request/");
        mRequestID = mFirebaseRequestRef.push().getKey();
        String mSenderUID = mAuth.getCurrentUser().getUid();
        String mSenderName = mAuth.getCurrentUser().getDisplayName();

        if (TempHolder.mConsumer != null)
        {
            mRequest = new Request(mRequestID,mSenderUID,mSenderName,TempHolder.mConsumer.getmPhone(),TempHolder.mConsumer.getmPhoto(),"11:30 AM","2:30 PM","Car","DMC 5643 TA");
        }

        mFirebaseRequestRef.child(mRequestID).setValue(mRequest);
        addRequestChangeListener();
    }


    private void addRequestChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(mRequestID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Request request = dataSnapshot.getValue(Request.class);

                // Check for null
                if (request == null) {
                    Log.e(TAG, "New Request is null!");
                    return;
                }else
                    Toast.makeText(MainActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //--------------------------------------Get Demo LatLon ----------------------------------------
    //----------------------------------------------------------------------------------------------


    public void getAllParkOwnerFromFirebase()
    {
        mFirebaseDatabase = mFirebaseInstance.getReference("ProviderList");
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "We are collecting Parking Owners.", true);
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Event event = dataSnapshot.getValue(Event.class);
                LatLng myLocation = new LatLng(TempData.getLatitude(),TempData.getLongitude());
                providerList.clear();
                clusterManager.clearItems();
                System.out.println("HHHHHHHHHHHHHHH >>>"+dataSnapshot.getValue(Provider.class));
                for(DataSnapshot data:dataSnapshot.getChildren()) {


                    Provider provider = data.getValue(Provider.class);
                    if (provider != null && provider.getmName() != null && provider.getmLatitude() != null && provider.getmLongitude()!= null){


                        providerList.add(provider);
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + provider.getmName());
                        double lat = Double.parseDouble(provider.getmLatitude());
                        double lon = Double.parseDouble(provider.getmLongitude());

                        if (lat != 0 && lon != 0) {
                            LatLng restaurant = new LatLng(lat, lon);
                            MyItems item = new MyItems(restaurant, provider.getmName(), provider.getmProviderID());
                            clusterManager.addItem(item);
                            clusterManager.cluster();
                        }
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14));


                        if (provider.getParkPlaceList()!= null) {
                            for (Map.Entry<String, ParkPlace> me : provider.getParkPlaceList().entrySet()) {
                                ParkPlaceList.add(me.getValue());
                            }
                        }


                    }

                }

                for (ParkPlace x: ParkPlaceList)
                {
                    System.out.println(">>>>>>>>>>> HH: "+x);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The to read event data: " + databaseError.getCode());
            }
        });
    }



    public void getAllParkingPlaces()
    {
        mFirebaseDatabase = mFirebaseInstance.getReference("ProviderList");
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "We are collecting Parking Owners.", true);
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Event event = dataSnapshot.getValue(Event.class);
                LatLng myLocation = new LatLng(TempData.getLatitude(),TempData.getLongitude());
                providerList.clear();
                clusterManager.clearItems();
                System.out.println(dataSnapshot.getValue(Provider.class));
                for(DataSnapshot data:dataSnapshot.getChildren()) {

                    Provider provider = data.getValue(Provider.class);
                    if (provider != null && provider.getmName() != null && provider.getmLatitude() != null && provider.getmLongitude()!= null){

                        providerList.add(provider);
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + provider.getmName());
                        double lat = Double.parseDouble(provider.getmLatitude());
                        double lon = Double.parseDouble(provider.getmLongitude());

                        if (lat != 0 && lon != 0) {
                            LatLng restaurant = new LatLng(lat, lon);
                            MyItems item = new MyItems(restaurant, provider.getmName(), provider.getmProviderID());
                            clusterManager.addItem(item);
                            clusterManager.cluster();
                        }
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14));
                    }

                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The to read event data: " + databaseError.getCode());
            }
        });
    }




    public void getStatus ()
    {
        mDatabaseConsumerStatus =mFirebaseInstance.getReference("ConsumerList/"+mConsumerID+"/Status/");
        System.out.println(">>>>>>>>>>>>>> Get Status Called");
        mDatabaseConsumerStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TempHolder.mStatusOfConsuner = dataSnapshot.getValue(StatusOfConsumer.class);
                System.out.println(">>>>>>>>>>>>>> Get Status Called  from firebase");
                if (TempHolder.mStatusOfConsuner != null)
                {
                    setNotification(TempHolder.mStatusOfConsuner.getmProviderName(),"accepted your request.");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The to read event data: " + databaseError.getCode());
            }
        });
    }


    public void getUserInformation ()
    {
        mFirebaseUserInformation  = mFirebaseInstance.getReference("ConsumerList/"+mConsumerID);
        System.out.println(">>>>>>>>>>>>>> Get Status Called");
        mFirebaseUserInformation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                System.out.println(">>>>>>>>>>>>>> Get Status Called  from firebase");
                if (dataSnapshot.getValue(Consumer.class) != null)
                {
                    TempHolder.mConsumer = dataSnapshot.getValue(Consumer.class);
                    mUserName.setText(TempHolder.mConsumer.getmName());

                    if (TempHolder.mConsumer.getmEmail().contains("@mail.com"))
                    {
                        mUserEmailAddress.setText("");
                    }else
                    {
                        mUserEmailAddress.setText(TempHolder.mConsumer.getmEmail());
                    }

                    if (!TempHolder.mConsumer.getmPhoto().equals(""))
                    {
                        Picasso.get().load(TempHolder.mConsumer.getmPhoto()).into(mProfileImage);
                    }else
                    {
                        mProfileImage.setImageResource(R.drawable.profile);
                    }

                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> USER : "+TempHolder.mConsumer);
                    getStatus();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The to read event data: " + databaseError.getCode());
            }
        });
    }





   /* private void getNearbyPlaces() {

        //------------------------------------------------------------------------------------------
        mWebApi = GoogleMapRetrofitClient.getRetrofitClient().create(WebApi.class);
        //------------------------------------------------------------------------------------------
        String key = getString(R.string.API_KEY);
        String urlString = String.format("place/nearbysearch/json?location=%f,%f&radius=500&type=resturent&key=%s",TempData.getLatitude(),TempData.getLongitude(),key);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+urlString);
        Call<NearbyPlaceResponse>call = mWebApi.getNearbyPlaces(urlString);
        call.enqueue(new Callback<NearbyPlaceResponse>() {
            @Override
            public void onResponse(Call<NearbyPlaceResponse> call, Response<NearbyPlaceResponse> response) {
                if(response.code() == 200){
                    LatLng myLocation = new LatLng(TempData.getLatitude(),TempData.getLongitude());
                    NearbyPlaceResponse nearbyPlaceResponse = response.body();
                    List<Result> results = nearbyPlaceResponse.getResults();
                    for(int i = 0; i < results.size(); i++){
                        double lat = results.get(i).getGeometry().getLocation().getLat();
                        double lon = results.get(i).getGeometry().getLocation().getLng();
                        LatLng restaurant = new LatLng(lat,lon);
                        //map.addMarker(new MarkerOptions().position(restaurant));
                        MyItems item = new MyItems(restaurant,results.get(i).getName(),results.get(i).getVicinity());


                        *//*map.addMarker(new MarkerOptions()
                                .title(item.getTitle())
                                .snippet(item.getSnippet())
                                .position(item.getPosition()));*//*

                        clusterManager.addItem(item);
                        clusterManager.cluster();


                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,14));
                    }
                }
            }

            @Override
            public void onFailure(Call<NearbyPlaceResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }*/






    //----------------------------------------------------------------------------------------------
    //--------------------------------------Navigation Draware Initials-----------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //getNearbyPlaces();
            getAllParkOwnerFromFirebase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            goToActivity();

        } else if (id == R.id.nav_map) {
            innitializeMap ();

        } else if (id == R.id.nav_payments) {
            goToPayment();
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            mLoginPreference.setStatus(false);
            signOut();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void signOut ()
    {

        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    //----------------------------------------------------------------------------------------------
    //-------------------------------------Get Current Location-------------------------------------
    //----------------------------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length!=0)
        {
            if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getDeviceCurrentLocation();
            }
        }
    }

    private boolean checkLocationPermission(){
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},111);
            return false;
        }
        return true;
    }

    private void getDeviceCurrentLocation() {
        if(checkLocationPermission()){
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location == null){
                        return;
                    }
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    TempData.latitude = latitude;
                    TempData.longitude = longitude;
                    getLocationUpdates();
                }
            });
        }
    }

    private void getLocationUpdates() {
        if(checkLocationPermission()){
            client.requestLocationUpdates(request,callback,null);
        }
    }


    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }


    @Override
    public void goToActivity() {
        mHeaderMenu.setVisibility(View.GONE);
        ft = fm.beginTransaction();
        ActivityFragment activityFragment = new ActivityFragment();
        ft.replace(R.id.fragmentContainer, activityFragment);
        ft.addToBackStack("goToNearBy");
        ft.commit();
    }

    @Override
    public void goToPayment() {
        mHeaderMenu.setVisibility(View.GONE);
        ft = fm.beginTransaction();
        PaymentFragment paymentFragment = new PaymentFragment();
        ft.replace(R.id.fragmentContainer, paymentFragment);
        ft.addToBackStack("goToPayment");
        ft.commit();
    }



    public Provider getProviderByID (String ID)
    {
        for (Provider provider: providerList)
        {
            if (provider.getmProviderID().equals(ID))
            {
                return provider;
            }
        }

        return null;
    }



    public void setNotification (String title,String msg)
    {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        //Uri uri = Uri.parse("android.resource://"+this.getPackageName()+"/" + R.raw.notification);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            AudioAttributes.Builder attrs = new AudioAttributes.Builder();
            attrs.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            attrs.setUsage(AudioAttributes.USAGE_NOTIFICATION);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            //notificationChannel.setSound(uri,attrs.build());
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_icon);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                //.setSound(uri)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_small_notifiaction_icon)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentIntent(intent)
                .setContentText(msg);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onResume() {

        /*
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null)
        {
            mCurrentUser = mAuth.getCurrentUser();
            mConsumerID = mCurrentUser.getUid();

            if (mCurrentUser.getDisplayName() == null )
            {
                mUserName.setText("");
            } else {
                mUserName.setText(mCurrentUser.getDisplayName());
            }

            if (mCurrentUser.getEmail() == null)
            {
                mUserEmailAddress.setText("");
            } else {

                if (mCurrentUser.getEmail().contains("@mail.com"))
                {
                    mUserEmailAddress.setText("");
                }
            }
        }
        */
        super.onResume();
    }


    public void showGPSDialogBox ()
    {
        mGpsDialog = new Dialog(this);
        mGpsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mGpsDialog.setContentView(R.layout.dialog_gps);
        mGpsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mGpsDialog.setCancelable(false);

        TextView mSetting = mGpsDialog.findViewById(R.id.mTurnOnSetting);

        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        mGpsDialog.show();
    }


    public void showInternetDialogBox ()
    {
        mInternetDialog = new Dialog(this);
        mInternetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInternetDialog.setContentView(R.layout.dialog_internet);
        mInternetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mInternetDialog.setCancelable(false);

        TextView mRefresh = mInternetDialog.findViewById(R.id.mTurnOnInternet);

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        mInternetDialog.show();
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDialogBox();

        }
    }




}
