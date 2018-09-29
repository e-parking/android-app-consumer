package bd.com.universal.eparking.seeker.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import bd.com.universal.eparking.seeker.MainActivity;
import bd.com.universal.eparking.seeker.PoJoClasses.Consumer;
import bd.com.universal.eparking.seeker.PoJoClasses.TempHolder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import bd.com.universal.eparking.seeker.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class SignUpActivity extends AppCompatActivity {




    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseUserInformation;
    private StorageReference mStorage;
    private StorageReference mStorageForProfile;
    private FirebaseUser mFirebaseUser;

    private final int IMG1_REQUEST = 1;
    private final int IMG2_REQUEST = 2;
    private final int IMG3_REQUEST = 3;
    private final int IMG4_REQUEST = 4;

    private String mProfileURL = "";

    private Bitmap mBitmapImage4;

    private String mImageName1 = "null";
    private String mImageName2 = "null";
    private String mImageName3 = "null";
    private String mImageName4 = "null";

    private static final LatLngBounds BOUNDS_BD = new LatLngBounds(new LatLng(23.777176, 90.399452), new LatLng(23.777176, 90.399452));

    private CircleImageView mProviderProfilePicture;
    private EditText mProviderName,mProviderEmail,mProviderPhone,mProviderAddress, mProviderNID, mProviderPassword1,mProviderPassword2;
    private Button mProfileUpdateButton,mProviderProfilePicker;
    private ImageView mMapPicker;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private String mConsumerID, mSelectedLatitude = "0", mSelectedLongitude = "0";
    private final int REQUEST_CODE_PLACEPICKER = 199;
    private FirebaseAuth mAuth;


    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private String thisDate;
    private int year,day,month;

    private Uri profileUri,documentUri1,documentUri2,documentUri3;

    private Dialog mUserAlertDialog;
    Boolean internetstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        progressDialog = new ProgressDialog(SignUpActivity.this);
        //-----------------firebase initializations------------------------
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mConsumerID = mAuth.getUid();
        //Toast.makeText(this, mAuth.getUid(), Toast.LENGTH_SHORT).show();


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("ConsumerList");
        mStorage = FirebaseStorage.getInstance().getReference();
        mStorageForProfile = FirebaseStorage.getInstance().getReference();

        mProviderName = findViewById(R.id.mProviderNameET);
        mProviderEmail = findViewById(R.id.mProviderEmailET);
        mProviderPhone = findViewById(R.id.mProviderPhoneET);
        mProviderAddress = findViewById(R.id.mProviderAddressET);
        mProviderNID = findViewById(R.id.mProviderNidET);
        mProviderPassword1 = findViewById(R.id.mProviderPassword1ET);
        mProviderPassword2 = findViewById(R.id.mProviderPassword2ET);


        mProfileUpdateButton = findViewById(R.id.mProfileUpdateButton);
        mProviderProfilePicker = findViewById(R.id.mProviderProfilePicker);

        mMapPicker = findViewById(R.id.mMapPickerButton);
        mProviderProfilePicture = findViewById(R.id.mProviderProfile);




        mProviderPhone.setCursorVisible(false);
        mProviderPhone.setLongClickable(false);
        mProviderPhone.setClickable(false);
        mProviderPhone.setFocusable(false);
        mProviderPhone.setSelected(false);
        mProviderPhone.setKeyListener(null);
        //--------------------------------------------

        mProviderProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDocument4();
            }
        });


        getUserInformation ();




        /*
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null)
        {
            mProviderName.setText(mUser.getDisplayName());

            if (mUser.getEmail().contains("@mail.com"))
            {
                mProviderEmail.setText("");
            }else
            {
                mProviderEmail.setText(mUser.getEmail());
            }
            mProviderPhone.setText(mUser.getPhoneNumber());
        }
        */



        mProviderProfilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDocument4();
            }
        });


        mMapPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlacePickerActivity();
            }
        });



        mProfileUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetstatus = isNetworkAvailable();
                if (internetstatus == true){
                    updateProfile();
                }
                else {
                    showInternetDialogBox();
                }

            }
        });

        mProviderPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Sorry,Phone number can not be changed.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void updateProfile()
    {
        mFirebaseUserInformation  = mFirebaseInstance.getReference("ConsumerList/"+mConsumerID);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("We are updateing your profile information...");
        progressDialog.show();

        Date todayDate = new Date();
        long currentDayMillis = todayDate.getTime();
        thisDate = Long.toString(currentDayMillis);

        final String name = mProviderName.getText().toString();
        final String email = mProviderEmail.getText().toString();
        final String password1 = mProviderPassword1.getText().toString();
        final String password2 = mProviderPassword2.getText().toString();
        final String phone = mProviderPhone.getText().toString();
        final String address = mProviderAddress.getText().toString();
        final String nationalid = mProviderNID.getText().toString();
        TempHolder.mUserName = name;

        //create user

        FirebaseUser user = mAuth.getCurrentUser();


        if (!password1.isEmpty())
        {
            user.updatePassword(password1);
        }

        if (!name.isEmpty())
        {
            UserProfileChangeRequest mProfileName = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
            user.updateProfile(mProfileName);
        }
        if (!mProfileURL.isEmpty())
        {
            UserProfileChangeRequest mProfilePhotoUrl = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(mProfileURL)).build();
            user.updateProfile(mProfilePhotoUrl);
        }

        mFirebaseUserInformation.child("mName").setValue(name);
        mFirebaseUserInformation.child("mAddress").setValue(address);
        mFirebaseUserInformation.child("mEmail").setValue(email);
        mFirebaseUserInformation.child("mNationalID").setValue(nationalid);
        mFirebaseUserInformation.child("mNationalID").setValue(nationalid);
        mFirebaseUserInformation.child("mPassword").setValue(password1);

        /*Consumer consumer = new Consumer(mConsumerID,name,mImageName4,address,email,phone,password1,nationalid,thisDate, "0", "0");
            createNewProvider (consumer);*/



        if (mSelectedLatitude.equals("0") && mSelectedLongitude.equals("0"))
        {
            mFirebaseUserInformation.child("mLatitude").setValue(TempHolder.mConsumer.getmLatitude());
            mFirebaseUserInformation.child("mLongitude").setValue(TempHolder.mConsumer.getmLongitude());
        }else
        {
            mFirebaseUserInformation.child("mLatitude").setValue(mSelectedLatitude);
            mFirebaseUserInformation.child("mLongitude").setValue(mSelectedLatitude);
        }


        // clear edit text
        mProviderName.setText("");
        mProviderEmail.setText("");
        mProviderPassword1.setText("");
        mProviderPassword1.setText("");
        mProviderPhone.setText("");
        mProviderAddress.setText("");
        mProviderNID.setText("");
        progressDialog.dismiss();
        ShowToast("Profile updated successfully");
       // Toast.makeText(SignUpActivity.this, "Profile successfully updated",    Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();



/*        mAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {


                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mAuth !=null)
                                    {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                        user.updateProfile(profileUpdates);
                                        mConsumerID = mAuth.getUid();

                                        Consumer consumer = new Consumer(mConsumerID,name,mImageName4,address,email,phone,password1,nationalid,thisDate, mSelectedLatitude, mSelectedLongitude);
                                        createNewProvider (consumer);
                                    }
                                }
                            }, 2000);
                        }
                    }
                });*/
    }

    private void createNewProvider(Consumer consumer ) {

        mFirebaseDatabase.child(mConsumerID).setValue(consumer);
        addUserChangeListener();
    }


    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(mConsumerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Consumer consumer = dataSnapshot.getValue(Consumer.class);

                // Check for null
                if (consumer == null) {
                    Log.e(TAG, "Provider is null!");
                    return;
                }else
                    {
                    // clear edit text
                    mProviderName.setText("");
                    mProviderEmail.setText("");
                    mProviderPassword1.setText("");
                    mProviderPassword1.setText("");
                    mProviderPhone.setText("");
                    mProviderAddress.setText("");
                    mProviderNID.setText("");
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Profile successfully updated",    Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                    //reload();

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }




    public void addPhotoToFirebase (Uri uri)
    {
        StorageReference filepath  =  mStorage.child("ConsumerProfilePictures").child(mProfileUrlMaker ());
        filepath.putFile(uri).addOnSuccessListener(new    OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, "Picture uploaded",    Toast.LENGTH_SHORT).show();
                /*startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();*/

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    public void addProfilePictureToFirebase(Uri uri)
    {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        mStorageForProfile = FirebaseStorage.getInstance().getReference();
        StorageReference filepath  =  mStorageForProfile.child("ConsumerProfilePictures").child(mProfileUrlMaker());

        filepath.putFile(uri).addOnSuccessListener(new    OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProfileURL = taskSnapshot.getDownloadUrl().toString();
                mFirebaseUserInformation.child("mPhoto").setValue(mProfileURL);
                Toast.makeText(SignUpActivity.this, "Profile picture added successfully",    Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG4_REQUEST && resultCode == RESULT_OK && data !=null)
        {
            profileUri = data.getData();
            try {
                mBitmapImage4 = MediaStore.Images.Media.getBitmap(getContentResolver(),profileUri);
                mProviderProfilePicture.setImageBitmap(mBitmapImage4);
                mImageName4 = profileUri.getLastPathSegment();
                addProfilePictureToFirebase (profileUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
        }
    }




    private void pickDocument1()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG1_REQUEST);
    }

    private void pickDocument2()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG2_REQUEST);
    }

    private void pickDocument3()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG3_REQUEST);
    }

    private void pickDocument4()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG4_REQUEST);
    }




    private void startPlacePickerActivity() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(BOUNDS_BD);
        try {
            startActivityForResult(builder.build(this), REQUEST_CODE_PLACEPICKER);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }




    private void displaySelectedPlaceFromPlacePicker(Intent data) {

        /*
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        */


        Place placeSelected = PlacePicker.getPlace(data, this);
        String address = placeSelected.getAddress().toString();
        mSelectedLatitude = Double.toString(placeSelected.getLatLng().latitude);
        mSelectedLongitude = Double.toString(placeSelected.getLatLng().longitude);
        mProviderAddress.setText(address);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Location>>>>>>>>>>>>>>  "+ mSelectedLatitude +"  "+ mSelectedLongitude);
    }



    public String mProfileUrlMaker ()
    {
        long time= System.currentTimeMillis();
        String millis =  Long.toString(time);
        //String url = mProviderID+"<<>>"+millis;
        String url = mConsumerID+"<<>>"+millis;
        return url;
    }

    private String mProfilePictureURL ()
    {

        final long time= System.currentTimeMillis();
        String url = mAuth.getUid()+"<<>>"+ Long.toString(time);
        return url;
    }


    public static void restartActivity(Activity act){

        Intent intent=new Intent();
        intent.setClass(act, act.getClass());
        act.startActivity(intent);
        act.finish();

    }

    public void reload() {
        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }



    public void getUserInformation ()
    {
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("We are Updating your profile...");
        progressDialog.show();


        mFirebaseUserInformation  = mFirebaseInstance.getReference("ConsumerList/"+mConsumerID);
        System.out.println(">>>>>>>>>>>>>> Get Status Called");
        mFirebaseUserInformation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                System.out.println(">>>>>>>>>>>>>> Get Status Called  from firebase");
                if (dataSnapshot.getValue(Consumer.class) != null)
                {
                    TempHolder.mConsumer = dataSnapshot.getValue(Consumer.class);
                    mUpdateAllInputFields ();
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> USER : "+TempHolder.mConsumer);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The to read event data: " + databaseError.getCode());
            }
        });
    }



    public void mUpdateAllInputFields ()
    {

        if (TempHolder.mConsumer != null)
        {

            if (!TempHolder.mConsumer.getmPhoto().equals(""))
            {
                Picasso.get().load(TempHolder.mConsumer.getmPhoto()).placeholder(R.drawable.profile).error(R.drawable.profile).into(mProviderProfilePicture);
            }else
            {
                mProviderProfilePicture.setImageResource(R.drawable.profile);
            }

            if (TempHolder.mConsumer.getmEmail().contains("@mail.com"))
            {
                mProviderEmail.setText("");
            }else
            {
                mProviderEmail.setText(TempHolder.mConsumer.getmEmail());
            }




            mProviderName.setText(TempHolder.mConsumer.getmName());
            mProviderPassword1.setText(TempHolder.mConsumer.getmPassword());
            mProviderPhone.setText(TempHolder.mConsumer.getmPhone());
            mProviderAddress.setText(TempHolder.mConsumer.getmAddress());
            mProviderNID.setText(TempHolder.mConsumer.getmNationalID());
            progressDialog.dismiss();
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showInternetDialogBox ()
    {
        mUserAlertDialog = new Dialog(this);
        mUserAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mUserAlertDialog.setContentView(R.layout.dialog_internet);
        mUserAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mUserAlertDialog.setCancelable(false);

        TextView mRefresh = mUserAlertDialog.findViewById(R.id.mTurnOnInternet);

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUserAlertDialog.dismiss();

            }
        });
        mUserAlertDialog.show();
    }
    private void ShowToast(String text){

        LayoutInflater layoutInflater=getLayoutInflater();
        View layout=layoutInflater.inflate(R.layout.custom_toast_layout,(ViewGroup)findViewById(R.id.custom_toast_layout));
        TextView textView=layout.findViewById(R.id.toast_text_id);
        textView.setText(text);

        Toast toast=new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM,0,30);
        toast.setView(layout);
        toast.show();
    }
}
