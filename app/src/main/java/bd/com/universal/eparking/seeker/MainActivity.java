package bd.com.universal.eparking.seeker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.android.clustering.ClusterManager;
import bd.com.universal.eparking.seeker.Activities.LoginActivity;
import bd.com.universal.eparking.seeker.Activities.SignUpActivity;
import bd.com.universal.eparking.seeker.Activities.TutorialActivity;
import bd.com.universal.eparking.seeker.CustomLayout.MapWrapperLayout;
import bd.com.universal.eparking.seeker.Fragments.ActivityFragment;
import bd.com.universal.eparking.seeker.Fragments.AddVehicleFragment;
import bd.com.universal.eparking.seeker.Fragments.PaymentFragment;
import bd.com.universal.eparking.seeker.OtherClasses.CustomClusterRenderer;
import bd.com.universal.eparking.seeker.OtherClasses.LoginPreferences;
import bd.com.universal.eparking.seeker.OtherClasses.OnInfoWindowElemTouchListener;
import bd.com.universal.eparking.seeker.OtherClasses.TempData;
import bd.com.universal.eparking.seeker.OtherClasses.VehicleType;
import bd.com.universal.eparking.seeker.PoJoClasses.Consumer;
import bd.com.universal.eparking.seeker.PoJoClasses.MyItems;
import bd.com.universal.eparking.seeker.PoJoClasses.ParkPlace;
import bd.com.universal.eparking.seeker.PoJoClasses.Provider;
import bd.com.universal.eparking.seeker.PoJoClasses.ParkingRequest;
import bd.com.universal.eparking.seeker.PoJoClasses.ProviderRating;
import bd.com.universal.eparking.seeker.PoJoClasses.Request;
import bd.com.universal.eparking.seeker.PoJoClasses.Schedule;
import bd.com.universal.eparking.seeker.PoJoClasses.Status;
import bd.com.universal.eparking.seeker.PoJoClasses.StatusOfConsumer;
import bd.com.universal.eparking.seeker.PoJoClasses.TempHolder;
import bd.com.universal.eparking.seeker.PoJoClasses.Vehicle;


import bd.com.universal.eparking.seeker.WebApis.DirectionService;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
        , OnMapReadyCallback
        , ActivityFragment.ActivityFragmentInterface
        , PaymentFragment.PaymentFragmentInterface
        ,AddVehicleFragment.AddVehicleFragmentInterface {
    private static final int LONG_DELAY = 2000;

    private static final String DISTANCE_BASE_URL = "https://maps.googleapis.com/maps/api/directions/";
    private DirectionService directionService;
    private String origin = "23.830971,90.42442489999999";
    private String destination = " 23.9998649,90.424532";
    private Polyline polyline = null;
    private List<Polyline> polylineList = new ArrayList<>();

    private static final String TAG = "MainActivity";
    //------------Custom Dialogs ---------
    private Dialog mGpsDialog;
    private Dialog mInternetDialog;

    //---------- Navigatin Header Info ----------
    private TextView mUserName;
    private TextView mUserEmailAddress;
    private CircleImageView mProfileImage;
    //-------------------------------------------


    Calendar cal = Calendar.getInstance();
    SimpleDateFormat parser = new SimpleDateFormat("hh:mm aa");
    Date userDate;
    Date timeFrom, timeTo;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseUserInformation;
    private DatabaseReference mFirebaseRequestRef;
    private DatabaseReference mDatabaseConsumerStatus;
    private DatabaseReference consumerRequestDb;
    private CardView vehicleSelection;
    private DatabaseReference providerRequestDb;
    private DatabaseReference providerRequestDb2;
    private FirebaseUser mCurrentUser;

    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback callback;
    private Double latitude, longitude;
    private ClusterManager<MyItems> clusterManager;
    private GoogleMap map;
    private GoogleMapOptions options;
    private LatLng mCurrentLocation;
    private CustomClusterRenderer renderer;
    private Boolean isMapInitialized = false;


    private MapWrapperLayout mapWrapperLayout;
    private ViewGroup infoWindow;
    private ViewGroup mCurrentLocationWindowBox;
    private TextView infoProviderName, hourlyRate;
    private TextView infoParkPlaceAddress;
    private TextView infoParkPlaceTitle;
    private TextView infoParkingType;
    private RatingBar ratingBar;
    private ImageView infoParkPlaceImage, bikeImage, carImage;
    // private String currentProviderId;
    private Provider provider;
    private List<Vehicle> vehicleList = new ArrayList<>();
    private Button infoButton;
    String formattedCurrentDay;
    private OnInfoWindowElemTouchListener infoButtonListener;

    private FrameLayout fragmentContainer;
    private android.support.v4.app.FragmentManager fm;
    private android.support.v4.app.FragmentTransaction ft;

    private TextView mArivalTime, mLeavingTime;
    private LinearLayout mHeaderMenu;
    private ProgressDialog progressDialog;

    private List<Provider> providerList = new ArrayList<>();
    private List<ParkPlace> parkPlaceList = new ArrayList<>();
    private List<String> ratingValue = new ArrayList<>();
    private float totalProviderRatingValue = 0;
    private float averageProviderParkingValue = 0;
    private String mRequestID;
    private String mConsumerID;
    private Request mRequest;
    private Boolean mInternetStatus;
    LoginPreferences mLoginPreference;
    private String requestVehicleType = VehicleType.Car;
    private boolean isCar = true;

    //------------- Time Picking -----------------
    private Calendar calendar;
    private int year, day, month;

    //-------------- Notification  --------------------
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    private boolean isReuestPending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("We are Preparing your map.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.isIndeterminate();
        progressDialog.show();


        //-------------------------- Firebase ----------------------------------------

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mConsumerID = mAuth.getCurrentUser().getUid();


        Retrofit retrofitDistance = new Retrofit.Builder()
                .baseUrl(DISTANCE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        directionService = retrofitDistance.create(DirectionService.class);


        //-------------- Custom Dialog -------------------
        mGpsDialog = new Dialog(this);
        mInternetDialog = new Dialog(this);


        mInternetStatus = isNetworkAvailable();
        if (!mInternetStatus) {
            progressDialog.dismiss();
            showInternetDialogBox();
        } else {
            statusCheck();
        }

        GetPendingRequest();
        getUserInformation();

        //-------------------------------------------------
        mLoginPreference = new LoginPreferences(this);
        //---------------------------Navigation --------------------------------

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        mUserName = header.findViewById(R.id.mUserName);
        mUserEmailAddress = header.findViewById(R.id.mUserEmailAddress);
        mProfileImage = header.findViewById(R.id.circularImageView);
        vehicleSelection = findViewById(R.id.vehicleSelection);
        bikeImage = findViewById(R.id.bikeImage);
        carImage = findViewById(R.id.carImage);


        //get current date
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        formattedCurrentDay = simpleDateFormat.format(date);

        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        mArivalTime = findViewById(R.id.arivingTime);
        mLeavingTime = findViewById(R.id.leavingTime);
        mHeaderMenu = findViewById(R.id.headerMenu);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();


        cal.get(Calendar.HOUR_OF_DAY);
        cal.get(Calendar.MINUTE);
        try {
            userDate = parser.parse(parser.format(cal.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }


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

                        String AM_PM;
                        if (selectedHour < 12) {
                            AM_PM = " AM";
                        } else {
                            AM_PM = " PM";
                        }
                        mArivalTime.setText("Ariving \n at " + selectedHour + ":" + selectedMinute + AM_PM);

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

                        String AM_PM;
                        if (selectedHour < 12) {
                            AM_PM = " AM";
                        } else {
                            AM_PM = " PM";
                        }
                        mLeavingTime.setText("Leaving \n at " + selectedHour + ":" + selectedMinute + AM_PM);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        bikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikeSelect();
            }
        });


        carImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarSelect();
            }
        });

        //------------------------------------------------------------------------------------------


        //----------------------------------- Location ---------------------------------------------

        client = LocationServices.getFusedLocationProviderClient(this);
        request = new LocationRequest();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(10000);
        request.setFastestInterval(5000);
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    if (latitude != 0 && longitude != 0) {
                        if (isMapInitialized == false) {
                            isMapInitialized = true;
                            innitializeMap();
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
        GetVehicle();


    }  //End of onCreat Method



    private void ShowToast(String text){

        LayoutInflater layoutInflater=getLayoutInflater();
        View layout=layoutInflater.inflate(R.layout.custom_toast_layout,(ViewGroup)findViewById(R.id.custom_toast_layout));
        TextView textView=layout.findViewById(R.id.toast_text_id);
        textView.setText(text);

        Toast toast=new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.setView(layout);
        toast.show();
    }



    private void GetPendingRequest() {

        DatabaseReference requestDB = FirebaseDatabase.getInstance().getReference("ConsumerList/" + mConsumerID + "/Request");
        requestDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        if (data.child("mStatus").getValue().toString().equals(Status.PENDING)
                                || data.child("mStatus").getValue().toString().equals(Status.ACCEPTED)
                                || data.child("mStatus").getValue().toString().equals(Status.STARTED)) {
                            isReuestPending = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void CarSelect() {
        bikeImage.setImageResource(R.drawable.bike_white);
        carImage.setImageResource(R.drawable.car_red);
        requestVehicleType = VehicleType.Car;
        clusterManager.clearItems();

        for (ParkPlace parkPlace : parkPlaceList) {
            if (parkPlace.getmParkingType().equals(VehicleType.Car)) {

                double lat = Double.parseDouble(parkPlace.getmLatitude());
                double lon = Double.parseDouble(parkPlace.getmLongitude());
                if (lat != 0 && lon != 0) {
                    LatLng latLng = new LatLng(lat, lon);
                    MyItems item = new MyItems(latLng, parkPlace.getmParkPlaceTitle(), parkPlace.getmParkPlaceID());
                    clusterManager.addItem(item);
                    clusterManager.cluster();
                }
            }
        }
    }

    private void BikeSelect() {

        bikeImage.setImageResource(R.drawable.bike_red);
        carImage.setImageResource(R.drawable.car_white);
        requestVehicleType = VehicleType.MotorCycle;
        clusterManager.clearItems();

        for (ParkPlace parkPlace : parkPlaceList) {
            if (parkPlace.getmParkingType().equals(VehicleType.MotorCycle)) {

                double lat = Double.parseDouble(parkPlace.getmLatitude());
                double lon = Double.parseDouble(parkPlace.getmLongitude());
                if (lat != 0 && lon != 0) {
                    LatLng latLng = new LatLng(lat, lon);
                    MyItems item = new MyItems(latLng, parkPlace.getmParkPlaceTitle(), parkPlace.getmParkPlaceID());
                    clusterManager.addItem(item);
                    clusterManager.cluster();
                }
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        getUserInformation();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onResume() {

        super.onResume();
    }


    //----------------------------------------------------------------------------------------------
    //--------------------------------------Map Section --------------------------------------------
    //----------------------------------------------------------------------------------------------


    public void innitializeMap() {
        //  vehicleSelection.setVisibility(View.VISIBLE);
        mGpsDialog.dismiss();
        options = new GoogleMapOptions();
        options.zoomControlsEnabled(true);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mapFragment);
        ft.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        if (mInternetStatus) {
            getAllParkOwnerFromFirebase();
        }

        map = googleMap;

        clusterManager = new ClusterManager<MyItems>(this, map);
        mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));
        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance


        this.infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.info_window, null);
        this.mCurrentLocationWindowBox = (ViewGroup) getLayoutInflater().inflate(R.layout.dialog_current_location, null);
        this.infoProviderName = infoWindow.findViewById(R.id.providerName);
        this.hourlyRate = infoWindow.findViewById(R.id.parkPlaceCost);
        this.infoParkPlaceAddress = infoWindow.findViewById(R.id.parkAddress);
        this.infoParkPlaceTitle = infoWindow.findViewById(R.id.parkPlaceTitle);
        this.infoParkingType = infoWindow.findViewById(R.id.parkingType);
        this.ratingBar = infoWindow.findViewById(R.id.ratingBarId);
        this.infoParkPlaceImage = infoWindow.findViewById(R.id.parkPlaceImage);
        this.infoButton = infoWindow.findViewById(R.id.button);


        renderer = new CustomClusterRenderer(this, map, clusterManager);
        clusterManager.setRenderer(renderer);

        map.setOnMarkerClickListener(clusterManager);
        map.setOnCameraIdleListener(clusterManager);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);

        mCurrentLocation = new LatLng(TempData.getLatitude(), TempData.getLongitude());

        //map.addMarker(new MarkerOptions().position(mCurrentLocation).title("My Current Location").snippet("Mirpur DOHS, Avenue 9").icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentLocation, 15));


        //------------------------------------------------------------------------------------------


        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
                getResources().getDrawable(R.drawable.round_but_green_sel), //btn_default_normal_holo_light
                getResources().getDrawable(R.drawable.round_but_red_sel)) //btn_default_pressed_holo_light
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                ParkPlace parkPlace = getParkPlaceByID(marker.getSnippet());
                sendRequest(parkPlace);
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
                // Provider provider = new Provider();
                ParkPlace parkPlace = new ParkPlace();
                if (marker.getTitle() != null) {

                    if (marker.getTitle().equals("My Current Location")) {

                        infoProviderName.setText("My Current Location");
                        infoParkPlaceAddress.setText("");
                        return mCurrentLocationWindowBox;
                        //infoButtonListener.setMarker(marker);
                    } else {


                        //provider = getProviderByID(marker.getSnippet());
                        parkPlace = getParkPlaceByID(marker.getSnippet());
                        provider = getProviderByID(parkPlace.getmProviderID());
                        //currentProviderId = provider.getmProviderID();


                        Location loc1 = new Location("");
                        loc1.setLatitude(latitude);
                        loc1.setLongitude(longitude);
                        Location loc2 = new Location("");
                        loc2.setLatitude(Double.parseDouble(parkPlace.getmLatitude()));
                        loc2.setLongitude(Double.parseDouble(parkPlace.getmLongitude()));
                        float distance = loc1.distanceTo(loc2);
                        float distanceInKm = distance / 1000;
                        String mDistance = String.format("%.2f", distanceInKm);


                        infoProviderName.setText(provider.getmName());
                        infoParkPlaceAddress.setText(parkPlace.getmAddress());
                        infoParkPlaceTitle.setText(parkPlace.getmParkPlaceTitle() + ", 1 " + parkPlace.getmParkingType());
                        infoParkingType.setText(mDistance + " km");
                        ratingBar.setRating(parkPlace.getmProviderAvarageRating());
                        hourlyRate.setText(parkPlace.getmParkingChargePerHour() + " TK/Hr");

                        if (parkPlace.getmParkPlacePhotoUrl().contains("https://")) {
                            Picasso.get().load(parkPlace.getmParkPlacePhotoUrl()).into(infoParkPlaceImage);
                        } else {
                            Bitmap bitmap = decodeBase64(parkPlace.getmParkPlacePhotoUrl());
                            infoParkPlaceImage.setImageBitmap(bitmap);
                        }

                        infoButtonListener.setMarker(marker);
                    }
                } else {

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


        //getNearbyPlaces();

    }


  /*  private void getDirection() {
       String api_key = getString(R.string.DIRECTION_API_KEY);
       String url = String.format("json?origin=%s,&destination=%s,&key=%s",origin,destination,api_key);

        Call<DirectionResponse> directionResponseCall = directionService.getAllDistances(url);
        directionResponseCall.enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {

                if(response.code()==200){

                    DirectionResponse directionResponse = response.body();
                    LatLng focusArea = new LatLng(directionResponse.getRoutes().get(0).getLegs().get(0).getSteps().get(0).getStartLocation().getLat(),
                            directionResponse.getRoutes().get(0).getLegs().get(0).getSteps().get(0).getStartLocation().getLat());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(focusArea,12));
                    List<DirectionResponse.Step>steps = directionResponse.getRoutes().get(0).getLegs().get(0).getSteps();
                    for(int i = 0; i < steps.size(); i++){

                        double startLat = steps.get(i).getStartLocation().getLat();
                        double startLng = steps.get(i).getStartLocation().getLng();
                        double endLat = steps.get(i).getEndLocation().getLat();
                        double endLng =steps.get(i).getEndLocation().getLng();

                        LatLng start= new LatLng(startLat,startLng);
                        LatLng end = new LatLng(endLat,endLng);

                        polyline = map.addPolyline(new PolylineOptions()
                                .add(start)
                                .add(end)
                                .width(10)
                                .color(Color.BLUE)
                                .clickable(true));
                       // polylineList.add(polyline);
                    }
                }

            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {

            }
        });

    }*/


    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    goToAddVehicle();
                    dialog.dismiss();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;

            }
        }
    };

    public void GetVehicle() {
        DatabaseReference vehicleDB = FirebaseDatabase.getInstance().getReference("ConsumerList/" + mConsumerID + "/Vehicle");
        vehicleDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Vehicle vehicle = data.getValue(Vehicle.class);
                        vehicleList.add(vehicle);

                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please add vehicle details first", Toast.LENGTH_LONG).show();
                    //goToAddVehicle();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void sendRequest(ParkPlace parkPlace) {

        if (isReuestPending==true) {
            ShowToast("You already have a pending request");
            goToActivity();
        } else {

        boolean success = false;
        if (vehicleList.size() > 0) {

            if (requestVehicleType.equals(VehicleType.Car)) {
                for (Vehicle vehicle : vehicleList) {
                    if (vehicle.getmVehicleType().equals(VehicleType.Car)) {

                        providerRequestDb = mFirebaseInstance.getReference
                                ("ProviderList/" + provider.getmProviderID() + "/ParkPlaceList/" + parkPlace.getmParkPlaceID() + "/Request/");
                        mRequestID = providerRequestDb.push().getKey();
                        String mSenderUID = mAuth.getCurrentUser().getUid();
                        String mSenderName = mAuth.getCurrentUser().getDisplayName();

                        if (TempHolder.mConsumer != null) {
                            mRequest = new Request(mRequestID, mSenderUID, TempHolder.mConsumer.getmName(),
                                    TempHolder.mConsumer.getmPhone(),
                                    TempHolder.mConsumer.getmPhoto(),
                                    vehicle.getmVehicleNumber());
                            Consumer consumer = TempHolder.mConsumer;

                            ParkingRequest providerRequest = new ParkingRequest(consumer.getmComsumerID(),
                                    provider.getmProviderID(),
                                    parkPlace.getmParkPlaceID(),
                                    parkPlace.getmParkPlaceTitle(),
                                    mRequestID,
                                    consumer.getmName(),
                                    consumer.getmPhone(),
                                    vehicle.getmVehicleNumber(),
                                    provider.getmName(),
                                    provider.getmPhone(),
                                    parkPlace.getmAddress(),
                                    parkPlace.getmLatitude(),
                                    parkPlace.getmLongitude(),
                                    Status.PENDING,
                                    parkPlace.getmParkPlacePhotoUrl()
                                    , consumer.getmPhoto(),
                                    0,
                                    0,
                                    0,
                                    0,
                                    System.currentTimeMillis());


                            //for update parking current status available or not
                            providerRequestDb2 = mFirebaseInstance.getReference
                                    ("ProviderList/" + provider.getmProviderID() + "/ParkPlaceList/" + parkPlace.getmParkPlaceID());
                            providerRequestDb2.child("mIsAvailable").setValue("false");
                            //end

                            consumerRequestDb = mFirebaseInstance.getReference
                                    ("ConsumerList/" + consumer.getmComsumerID() + "/Request/");

                            providerRequestDb.child(mRequestID).setValue(providerRequest);
                            consumerRequestDb.child(mRequestID).setValue(providerRequest);


                            //for notification
                            FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
                            Map<String, Object> notificationMap = new HashMap<>();
                            notificationMap.put("message", consumer.getmName() + " wants to park " + vehicle.getmVehicleType() + " (" + vehicle.getmVehicleNumber() + ")");
                            notificationMap.put("consumer", mConsumerID);

                            mFireStore.collection("Users").document(provider.getmProviderID()).collection("Notifications").add(notificationMap);


                        }


                        addRequestChangeListener();
                        success = true;
                    }

                }
                if (success == false) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("You have no Car Information ").setPositiveButton("OK", onClickListener)
                            .setNegativeButton("No Thanks", onClickListener).show();


                }
            } else if (requestVehicleType.equals(VehicleType.MotorCycle)) {
                for (Vehicle vehicle : vehicleList) {
                    if (vehicle.getmVehicleType().equals(VehicleType.MotorCycle)) {


                        providerRequestDb = mFirebaseInstance.getReference
                                ("ProviderList/" + provider.getmProviderID() + "/ParkPlaceList/" + parkPlace.getmParkPlaceID() + "/Request/");
                        mRequestID = providerRequestDb.push().getKey();
                        String mSenderUID = mAuth.getCurrentUser().getUid();
                        String mSenderName = mAuth.getCurrentUser().getDisplayName();

                        if (TempHolder.mConsumer != null) {
                            mRequest = new Request(mRequestID, mSenderUID, TempHolder.mConsumer.getmName(),
                                    TempHolder.mConsumer.getmPhone(),
                                    TempHolder.mConsumer.getmPhoto(),
                                    "DMC 5643 TA");
                            Consumer consumer = TempHolder.mConsumer;

                            ParkingRequest providerRequest = new ParkingRequest(consumer.getmComsumerID(),
                                    provider.getmProviderID(),
                                    parkPlace.getmParkPlaceID(),
                                    parkPlace.getmParkPlaceTitle(),
                                    mRequestID,
                                    consumer.getmName(),
                                    consumer.getmPhone(),
                                    vehicle.getmVehicleNumber(),
                                    provider.getmName(),
                                    provider.getmPhone(),
                                    parkPlace.getmAddress(),
                                    parkPlace.getmLatitude(),
                                    parkPlace.getmLongitude(),
                                    Status.PENDING,
                                    parkPlace.getmParkPlacePhotoUrl()
                                    , consumer.getmPhoto(),
                                    0,
                                    0,
                                    0,
                                    0,
                                    System.currentTimeMillis());

                            //for update parking current status available or not
                            providerRequestDb2 = mFirebaseInstance.getReference
                                    ("ProviderList/" + provider.getmProviderID() + "/ParkPlaceList/" + parkPlace.getmParkPlaceID());
                            providerRequestDb2.child("mIsAvailable").setValue("false");
                            //end

                            consumerRequestDb = mFirebaseInstance.getReference
                                    ("ConsumerList/" + consumer.getmComsumerID() + "/Request/");

                            providerRequestDb.child(mRequestID).setValue(providerRequest);
                            consumerRequestDb.child(mRequestID).setValue(providerRequest);


                            //for notification
                            FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
                            Map<String, Object> notificationMap = new HashMap<>();
                            notificationMap.put("message", consumer.getmName() + " wants to park " + vehicle.getmVehicleType() + " (" + vehicle.getmVehicleNumber() + ")");
                            notificationMap.put("consumer", mConsumerID);

                            mFireStore.collection("Users").document(provider.getmProviderID()).collection("Notifications").add(notificationMap);


                        }


                        addRequestChangeListener();

                        success = true;
                    }

                }
                if (success == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("You have no Bike Information ").setPositiveButton("OK", onClickListener)
                            .setNegativeButton("No Thanks", onClickListener).show();
                }
            }


        } else {
            Toast.makeText(this, "Please add vehicle details for sending request", Toast.LENGTH_SHORT).show();
            goToAddVehicle();
        }

    }

}


    private void addRequestChangeListener() {
        // User data change listener
        providerRequestDb.child(mRequestID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Request request = dataSnapshot.getValue(Request.class);
                ParkingRequest providerRequest = dataSnapshot.getValue(ParkingRequest.class);

                goToActivity();
                // Check for null
                if (providerRequest == null) {
                    Log.e(TAG, "New request is null!");
                    return;
                } else{
                    vehicleSelection.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Request sent to parking owner ", Toast.LENGTH_SHORT).show();
                }

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


    private Runnable changeMessage=new Runnable() {
        @Override
        public void run() {
            progressDialog.setMessage("We are collecting Parking Owners.");
        }
    };



    public void getAllParkOwnerFromFirebase() {

        mFirebaseDatabase = mFirebaseInstance.getReference("ProviderList");/*
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "We are collecting Parking Owners.", true);*/
        runOnUiThread(changeMessage);

        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Event event = dataSnapshot.getValue(Event.class);
                LatLng myLocation = new LatLng(TempData.getLatitude(), TempData.getLongitude());
                providerList.clear();
                parkPlaceList.clear();

                clusterManager.clearItems();
                System.out.println("HHHHHHHHHHHHHHH >>>" + dataSnapshot.getValue(Provider.class));
                for (DataSnapshot data : dataSnapshot.getChildren()) {


                    Provider provider = data.getValue(Provider.class);
                    if (provider != null && provider.getmName() != null
                            && provider.getmLatitude() != null
                            && provider.getmLongitude() != null) {

                        providerList.add(provider);

                    }

                }
                for (final Provider provider : providerList) {
                    final DatabaseReference dbReference = FirebaseDatabase.getInstance().
                            getReference("ProviderList/" + provider.getmProviderID() + "/ParkPlaceList");
                    dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                final ParkPlace parkPlace = data.getValue(ParkPlace.class);


                                if (parkPlace != null && parkPlace.getmAddress() != null
                                        && parkPlace.getmLatitude() != null
                                        && parkPlace.getmLongitude() != null
                                        && parkPlace.getmIsAvailable().equals("true")) {


                                    //provider rating section
                                    DatabaseReference ratingDB=FirebaseDatabase.getInstance().
                                            getReference("ProviderList/" + parkPlace.getmProviderID() + "/ParkPlaceList/" + parkPlace.getmParkPlaceID()+ "/Request");

                                    ratingDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                totalProviderRatingValue=0;
                                                ProviderRating providerRating;
                                                int counter=0;
                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                    providerRating=data.getValue(ProviderRating.class);
                                                    counter++;
                                                    totalProviderRatingValue=totalProviderRatingValue+providerRating.getmConsumerRatingValue();
                                                }
                                                if (counter>0){
                                                    averageProviderParkingValue=totalProviderRatingValue/counter;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    //End of rating section



                                    DatabaseReference dbReferenceS = FirebaseDatabase.getInstance().
                                            getReference("ProviderList/" + provider.getmProviderID() + "/ParkPlaceList/" + parkPlace.getmParkPlaceID()+ "/Schedule");

                                    dbReferenceS.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists())
                                            {
                                                for (DataSnapshot data : dataSnapshot.getChildren()) {

                                                    Schedule schedule = data.getValue(Schedule.class);


                                                    if (schedule.getmDay().equals(formattedCurrentDay) && schedule.getmIsForRent().equals("true")){


                                                        try {
                                                            timeFrom=parser.parse(schedule.getmFromTime());
                                                            timeTo=parser.parse(schedule.getmToTime());
                                                        } catch (ParseException e) {
                                                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                                            //e.printStackTrace();
                                                        }


                                                        if (userDate.after(timeFrom) && userDate.before(timeTo)) {

                                                            if (averageProviderParkingValue>0){
                                                                parkPlace.setmProviderAvarageRating(averageProviderParkingValue);
                                                            }

                                                            parkPlaceList.add(parkPlace);

                                                            if (parkPlace.getmParkingType().equals(VehicleType.Car)){

                                                                double lat = Double.parseDouble(parkPlace.getmLatitude());
                                                                double lon = Double.parseDouble(parkPlace.getmLongitude());
                                                                if (lat != 0 && lon != 0) {
                                                                    LatLng latLng = new LatLng(lat, lon);
                                                                    MyItems item = new MyItems(latLng, parkPlace.getmParkPlaceTitle(), parkPlace.getmParkPlaceID());
                                                                    clusterManager.addItem(item);
                                                                    clusterManager.cluster();
                                                                }
                                                            }


                                                            // map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14));
                                                        } else {

                                                        }
                                                    }

                                                }
                                        }
                                        else {
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The to read event data: " + databaseError.getCode());
            }
        });
    }




    public void getStatus() {
        mDatabaseConsumerStatus = mFirebaseInstance.getReference("ConsumerList/" + mConsumerID + "/Status/");
        System.out.println(">>>>>>>>>>>>>> Get Status Called");
        mDatabaseConsumerStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TempHolder.mStatusOfConsuner = dataSnapshot.getValue(StatusOfConsumer.class);
                System.out.println(">>>>>>>>>>>>>> Get Status Called  from firebase");
                if (TempHolder.mStatusOfConsuner != null) {
                    setNotification(TempHolder.mStatusOfConsuner.getmProviderName(), "accepted your request.");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The to read event data: " + databaseError.getCode());
            }
        });
    }


    public void getUserInformation() {
        mFirebaseUserInformation = mFirebaseInstance.getReference("ConsumerList/" + mConsumerID);
        System.out.println(">>>>>>>>>>>>>> Get Status Called");
        mFirebaseUserInformation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                System.out.println(">>>>>>>>>>>>>> Get Status Called  from firebase");
                if (dataSnapshot.getValue(Consumer.class) != null) {
                    TempHolder.mConsumer = dataSnapshot.getValue(Consumer.class);
                    mUserName.setText(TempHolder.mConsumer.getmName());

                    if (TempHolder.mConsumer.getmEmail().contains("@mail.com")) {
                        mUserEmailAddress.setText("");
                    } else {
                        mUserEmailAddress.setText(TempHolder.mConsumer.getmEmail());
                    }

                    if (!TempHolder.mConsumer.getmPhoto().equals("")) {
                        Picasso.get().load(TempHolder.mConsumer.getmPhoto()).into(mProfileImage);
                    } else {
                        mProfileImage.setImageResource(R.drawable.profile);
                    }

                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> USER : " + TempHolder.mConsumer);
                    getStatus();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The to read event data: " + databaseError.getCode());
            }
        });
    }



    @Override
    public void onBackPressed() {
        int backStackCount=getSupportFragmentManager().getBackStackEntryCount();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (backStackCount==1){
            super.onBackPressed();
            CarSelect();
            vehicleSelection.setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }

        //vehicleSelection.setVisibility(View.VISIBLE);
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //getNearbyPlaces();
            getAllParkOwnerFromFirebase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            goToActivity();
            vehicleSelection.setVisibility(View.GONE);


        } else if (id == R.id.nav_map) {
            mInternetStatus = isNetworkAvailable();
            if (mInternetStatus ==  true){
                innitializeMap();
                bikeImage.setImageResource(R.drawable.bike_white);
                carImage.setImageResource(R.drawable.car_red);
                vehicleSelection.setVisibility(View.VISIBLE);
            }
            else {
                showInternetDialogBox();
            }



        }else if (id == R.id.nav_add_vehicle) {
            goToAddVehicle();
            vehicleSelection.setVisibility(View.GONE);


        } else if (id == R.id.nav_tutorials) {
            Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            mLoginPreference.setStatus(false);

                signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void signOut() {

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
        if (grantResults.length != 0) {
            if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceCurrentLocation();
            }
        }
    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
            return false;
        }
        return true;
    }

    private void getDeviceCurrentLocation() {
        if (checkLocationPermission()) {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null) {
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
        if (checkLocationPermission()) {
            client.requestLocationUpdates(request, callback, null);
        }
    }


    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    public void goToActivity() {
        mHeaderMenu.setVisibility(View.GONE);
        ft = fm.beginTransaction();
        vehicleSelection.setVisibility(View.GONE);
        ActivityFragment activityFragment = new ActivityFragment();
        ft.replace(R.id.fragmentContainer, activityFragment);
        ft.addToBackStack("goToNearBy");
        ft.commit();
    }

    @Override
    public void goToPayment() {
        mHeaderMenu.setVisibility(View.GONE);
        ft = fm.beginTransaction();
        vehicleSelection.setVisibility(View.GONE);
        PaymentFragment paymentFragment = new PaymentFragment();
        ft.replace(R.id.fragmentContainer, paymentFragment);
        ft.addToBackStack("goToPayment");

        ft.commit();
    }
    @Override
    public void goToAddVehicle() {
        mHeaderMenu.setVisibility(View.GONE);
        ft = fm.beginTransaction();
        vehicleSelection.setVisibility(View.GONE);
        AddVehicleFragment addVehicleFragment = new AddVehicleFragment();
        ft.replace(R.id.fragmentContainer, addVehicleFragment);
        ft.addToBackStack("goToAddVehicle");
        ft.commit();

    }


    public Provider getProviderByID(String ID) {
        for (Provider provider : providerList) {
            if (provider.getmProviderID().equals(ID)) {
                return provider;
            }
        }

        return null;
    }

    public ParkPlace getParkPlaceByID(String ID) {
        for (ParkPlace parkPlace : parkPlaceList) {

            if (parkPlace.getmParkPlaceID().equals(ID)){
                return parkPlace;
            }

        }
        return null;
    }

    public void setNotification(String title, String msg) {
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




    public void showGPSDialogBox() {
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


    public void showInternetDialogBox() {



        mInternetDialog = new Dialog(this);
        mInternetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mInternetDialog.setContentView(R.layout.dialog_internet);
        mInternetDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mInternetDialog.setCancelable(false);

        TextView mRefresh = mInternetDialog.findViewById(R.id.mTurnOnInternet);

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
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
            progressDialog.dismiss();
            showGPSDialogBox();

        }
    }



}
