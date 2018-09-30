package bd.com.universal.eparking.seeker.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import bd.com.universal.eparking.seeker.Activities.LoginWithPhone;
import bd.com.universal.eparking.seeker.OtherClasses.VehicleType;
import bd.com.universal.eparking.seeker.PoJoClasses.Vehicle;
import bd.com.universal.eparking.seeker.R;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class AddVehicleFragment extends Fragment {

    private TextView carNumber,motorCycleNumber,blueBookImageUpload,microbusTV;
    private RadioButton carRadioButton, motorCycleRadioButton;
    private EditText vehicleNumberInpur;
    private Button saveButton,addnew;
    private CardView carCardView,motorCycleCardView;
    private RadioGroup radioGroup;
    private ImageView carBlueBookImageView, motorCycleBlueBookImageView,blueBookImageShow;
    private final static int galary_pick = 1;
    private FirebaseAuth mAuth;
    private String mUserId;
    private StorageReference mStorage;
    private String downloadUrl;
    private final int IMG4_REQUEST = 4;
    private Dialog mImagePickerDialog;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private String mParkPlacePhotoUrl = "";
    private Uri profileUri;
    private List<Vehicle> car=new ArrayList<>();
    private List<Vehicle> motorByke=new ArrayList<>();
    private Bitmap mBitmapImage4;
    private ProgressDialog progressDialog;
    private String mImageName4 = "null";
    private StorageReference mStorageForProfile;
    private String vehicleType;
    private String vehicleNumber,vehicleNumberPrefix;
    private int count=0;
    private boolean edit=false;
    private String carVehicleId,motorBikeVehicleId,currentVehicleId;
    private LinearLayout linearLayout;
    private String[] rNumberFormat={"Dhaka Metro","Chotta Metro"};
    private AutoCompleteTextView autoCompleteNumber;
    private Dialog mUserAlertDialog;
    Boolean internetstatus;
    public Vehicle vehicle;

    public interface  AddVehicleFragmentInterface {
        public void goToAddVehicle ();
    }

    public AddVehicleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        carNumber=view.findViewById(R.id.carNumberId);
        motorCycleNumber=view.findViewById(R.id.motorCycleNumberId);
        blueBookImageUpload=view.findViewById(R.id.uploadBlueBookId);
        carRadioButton=view.findViewById(R.id.carRadioButtonId);
        motorCycleRadioButton=view.findViewById(R.id.motorcycleRadioButtonId);
        vehicleNumberInpur=view.findViewById(R.id.registrationNumberId);
        carCardView=view.findViewById(R.id.carCardViewId);
        motorCycleCardView=view.findViewById(R.id.bikeCardViewId);
        carBlueBookImageView=view.findViewById(R.id.carBlueBookId);
        saveButton=view.findViewById(R.id.saveButtonId);
        addnew = view.findViewById(R.id.addnew);
        radioGroup=view.findViewById(R.id.radioGroupId);
        motorCycleBlueBookImageView=view.findViewById(R.id.motorcycleBlueBookId);
        blueBookImageShow=view.findViewById(R.id.blueBookImageShowId);
        linearLayout=view.findViewById(R.id.layoutId);
        autoCompleteNumber=view.findViewById(R.id.autoCompleteNumber);
        microbusTV=view.findViewById(R.id.microbusTV);
        progressDialog = new ProgressDialog(getActivity());
        autoCompleteNumber.setDropDownBackgroundDrawable(new ColorDrawable(getActivity().getApplicationContext().getResources().getColor(R.color.colorPrimary)));

        final ArrayAdapter adapter = new
                ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,rNumberFormat);
        autoCompleteNumber.setAdapter(adapter);
        autoCompleteNumber.setThreshold(1);

        getCameraPermission();

        mAuth=FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mUserId=mAuth.getCurrentUser().getUid();
        DatabaseReference vehicleListDB=FirebaseDatabase.getInstance().getReference("ConsumerList/"+mUserId+"/Vehicle");

        count=0;
        vehicleListDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Vehicle vehicle = data.getValue(Vehicle.class);
                        count++;


                        if (vehicle.getmVehicleType().equals(VehicleType.Car)){
                            carCardView.setVisibility(View.VISIBLE);
                            carRadioButton.setVisibility(View.GONE);
                            microbusTV.setVisibility(View.GONE);
                            carVehicleId=data.getKey();
                            car.add(vehicle);
                            if (vehicle.getmVehicleImage()==null || vehicle.getmVehicleImage().equals(null)| vehicle.getmVehicleImage().isEmpty()){
                            }
                            else {
                                Picasso.get().load(vehicle.getmVehicleImage()).placeholder(R.drawable.car_red).error(R.drawable.car_red).into(carBlueBookImageView);
                            }

                            carNumber.setText(vehicle.getmVehicleNumberPrefix()+" "+vehicle.getmVehicleNumber()+"\n"+vehicle.getmVehicleType());
                        }
                        else if (vehicle.getmVehicleType().equals(VehicleType.MotorCycle))
                        {
                            motorCycleCardView.setVisibility(View.VISIBLE);
                            motorCycleRadioButton.setVisibility(View.GONE);
                            motorBikeVehicleId=data.getKey();
                            motorByke.add(vehicle);
                            if (vehicle.getmVehicleImage()==null || vehicle.getmVehicleImage().equals(null)|| vehicle.getmVehicleImage().isEmpty()){
                            }
                            else {
                                Picasso.get().load(vehicle.getmVehicleImage()).placeholder(R.drawable.bike_red).error(R.drawable.bike_red).into(motorCycleBlueBookImageView);
                            }
                            motorCycleNumber.setText(vehicle.getmVehicleNumberPrefix()+" "+vehicle.getmVehicleNumber()+"\n"+vehicle.getmVehicleType());
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        if (car.size()==0 && motorByke.size()==0 || car.size()==1 && motorByke.size()==1){
            addnew.setVisibility(View.GONE);
        }


        blueBookImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internetstatus = isNetworkAvailable();
                if (internetstatus==true){
                    if (count<2){
                        vehicleNumberPrefix = autoCompleteNumber.getText().toString();
                        vehicleNumber=vehicleNumberInpur.getText().toString();
                        if (carRadioButton.isChecked())
                            vehicleType= VehicleType.Car;
                        if (motorCycleRadioButton.isChecked())
                            vehicleType=VehicleType.MotorCycle;
                        if (vehicleNumber==null||vehicleNumber.equals(null) || vehicleNumber.isEmpty() || vehicleNumberPrefix == null||vehicleNumberPrefix.isEmpty()){
                            Toast.makeText(getContext().getApplicationContext(), "Add vehicle number", Toast.LENGTH_SHORT).show();
                        }
                        /*
                    else if (mParkPlacePhotoUrl.equals(null)||mParkPlacePhotoUrl==null || mParkPlacePhotoUrl.isEmpty()){
                        Toast.makeText(getActivity().getApplicationContext(), "Add a Blue Book Image", Toast.LENGTH_SHORT).show();
                    }*/
                        else if (vehicleType==null||vehicleType.equals(null)||vehicleType.isEmpty()){
                            Toast.makeText(getActivity().getApplicationContext(), "Select vehicle type", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            UploadVehicleToFirebase();
                        }
                    }
                    else {
                        ShowToast( "You cann't add more then two vehicle");
                       // Toast.makeText(getActivity().getApplicationContext(), "You cann't add more then two vehicle", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    showInternetDialogBox();
                }



            }
        });

            addnew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit=false;
                  autoCompleteNumber.getText().clear();
                  autoCompleteNumber.requestFocus();
                    vehicleNumberInpur.getText().clear();

                    if(car.size()>0){
                        carRadioButton.setVisibility(View.GONE);
                        microbusTV.setVisibility(View.GONE);
                        carRadioButton.setChecked(false);

                    }else {
                        carRadioButton.setVisibility(View.VISIBLE);
                        microbusTV.setVisibility(View.VISIBLE);
                        blueBookImageShow.setImageResource(R.drawable.car_red);
                    }
                    if(motorByke.size()>0){
                        motorCycleRadioButton.setVisibility(View.GONE);
                        motorCycleRadioButton.setChecked(false);

                    }else {
                        motorCycleRadioButton.setVisibility(View.VISIBLE);
                        blueBookImageShow.setImageResource(R.drawable.bike_red);
                    }
                }
            });

        carCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit=true;
                count=0;
                if (motorByke.size()==0){
                    addnew.setVisibility(View.VISIBLE);
                }
                radioGroup.clearCheck();
                currentVehicleId=carVehicleId;
                carRadioButton.setVisibility(View.VISIBLE);
                microbusTV.setVisibility(View.VISIBLE);
                motorCycleRadioButton.setVisibility(View.GONE);
                carRadioButton.setChecked(true);
                mParkPlacePhotoUrl=car.get(0).getmVehicleImage();
                vehicleNumberInpur.setText(car.get(0).getmVehicleNumber());
                autoCompleteNumber.setText(car.get(0).getmVehicleNumberPrefix());
                vehicleNumberInpur.requestFocus();
                vehicleNumberInpur.setSelection(vehicleNumberInpur.getText().length());

                if (car.get(0).getmVehicleImage()!=null && !car.get(0).getmVehicleImage().equals(null) && !car.get(0).getmVehicleImage().isEmpty()){
                    Picasso.get().load(car.get(0).getmVehicleImage()).placeholder(R.drawable.car_red).error(R.drawable.car_red).into(blueBookImageShow);
                }
                else {
                    blueBookImageShow.setImageResource(R.drawable.car_red);
                }

            }
        });


        motorCycleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit=true;
                count=0;
                radioGroup.clearCheck();

                if (car.size()==0){
                    addnew.setVisibility(View.VISIBLE);
                }

                currentVehicleId=motorBikeVehicleId;
                vehicleNumberInpur.setText(motorByke.get(0).getmVehicleNumber());
                autoCompleteNumber.setText(motorByke.get(0).getmVehicleNumberPrefix());
                motorCycleRadioButton.setVisibility(View.VISIBLE);
                carRadioButton.setVisibility(View.GONE);
                microbusTV.setVisibility(View.GONE);
                motorCycleRadioButton.setChecked(true);
                vehicleNumberInpur.requestFocus();
                vehicleNumberInpur.setSelection(vehicleNumberInpur.getText().length());
                mParkPlacePhotoUrl=motorByke.get(0).getmVehicleImage();
                if (motorByke.get(0).getmVehicleImage()!=null && !motorByke.get(0).getmVehicleImage().equals(null) && !motorByke.get(0).getmVehicleImage().isEmpty()){
                    Picasso.get().load(motorByke.get(0).getmVehicleImage()).placeholder(R.drawable.bike_red).error(R.drawable.bike_red).into(blueBookImageShow);
                }
                else {
                    blueBookImageShow.setImageResource(R.drawable.bike_red);
                }


            }
        });




        return view;
    }

    private void UploadVehicleToFirebase() {
        DatabaseReference addVehicleDB;
        if (edit){
            addVehicleDB=FirebaseDatabase.getInstance().getReference("ConsumerList/"+mUserId+"/Vehicle/"+currentVehicleId);
            HashMap<String, String> newVehicle = new HashMap<>();

            newVehicle.put("mVehicleNumberPrefix",vehicleNumberPrefix);
            newVehicle.put("mVehicleNumber", vehicleNumber);
            newVehicle.put("mVehicleType", vehicleType);
            newVehicle.put("mBlueBookImage", "");
            newVehicle.put("mVehicleImage", mParkPlacePhotoUrl);
            addVehicleDB.setValue(newVehicle).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        SuccessToast("Vehicle added successfully");
                      //  Toast.makeText(getActivity().getApplicationContext(), "Vehicle added successfully", Toast.LENGTH_SHORT).show();
                        if (vehicleType.equals(VehicleType.Car)){
                            carCardView.setVisibility(View.VISIBLE);
                            showCarDetails();
                        }
                        if (vehicleType.equals(VehicleType.MotorCycle)){
                            motorCycleCardView.setVisibility(View.VISIBLE);
                            showMotorCycleDetails();
                        }
                    }
                }
            });
        }
        else {
            addVehicleDB=FirebaseDatabase.getInstance().getReference("ConsumerList/"+mUserId+"/Vehicle");
            HashMap<String, String> newVehicle = new HashMap<>();

            newVehicle.put("mVehicleNumberPrefix",vehicleNumberPrefix);
            newVehicle.put("mVehicleNumber", vehicleNumber);
            newVehicle.put("mVehicleType", vehicleType);
            newVehicle.put("mBlueBookImage", "");
            newVehicle.put("mVehicleImage", mParkPlacePhotoUrl);
            addVehicleDB.push().setValue(newVehicle).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        SuccessToast("Vehicle added successfully");
                       // Toast.makeText(getActivity().getApplicationContext(), "Vehicle added successfully", Toast.LENGTH_SHORT).show();
                        if (vehicleType.equals(VehicleType.Car)){
                            carCardView.setVisibility(View.VISIBLE);
                            showCarDetails();
                        }
                        if (vehicleType.equals(VehicleType.MotorCycle)){
                            motorCycleCardView.setVisibility(View.VISIBLE);
                            showMotorCycleDetails();
                        }
                    }
                }
            });
        }


    }

    private void showMotorCycleDetails() {

        motorCycleBlueBookImageView.setImageBitmap(mBitmapImage4);
        motorCycleNumber.setText(vehicleNumberPrefix+"\n"+vehicleNumber+"\n"+vehicleType);
    }

    private void showCarDetails() {

        carBlueBookImageView.setImageBitmap(mBitmapImage4);
        carNumber.setText(vehicleNumberPrefix+"\n"+vehicleNumber+"\n"+vehicleType);
    }



    public void showImagePickerDialog() {
        mImagePickerDialog = new Dialog(getActivity());
        mImagePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mImagePickerDialog.setContentView(R.layout.dialog_image_picker);
        mImagePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mImagePickerDialog.setCancelable(true);


        ImageView mCamera = mImagePickerDialog.findViewById(R.id.mCameraImageView);
        ImageView mGallery = mImagePickerDialog.findViewById(R.id.mGalleryImageView);


        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                mImagePickerDialog.dismiss();
            }
        });

        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDocument4();
                mImagePickerDialog.dismiss();
            }
        });
        mImagePickerDialog.show();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mParkPlacePhotoUrl = encodeToBase64(imageBitmap, Bitmap.CompressFormat.JPEG, 50);

        } /*else if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);
        } */else if (requestCode == IMG4_REQUEST && resultCode == RESULT_OK && data != null) {
            profileUri = data.getData();
            try {
                mBitmapImage4 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), profileUri);
                blueBookImageShow.setImageBitmap(mBitmapImage4);
                mImageName4 = profileUri.getLastPathSegment();
                addProfilePictureToFirebase(profileUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void addProfilePictureToFirebase(Uri uri) {
        progressDialog.setMessage("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        StorageReference filepath = mStorage.child("BlueBookPhotos").child(mProfileUrlMaker());

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mParkPlacePhotoUrl = taskSnapshot.getDownloadUrl().toString();
                blueBookImageShow.setImageBitmap(mBitmapImage4);
                //Picasso.get().load(mParkPlacePhotoUrl).into(blueBookImageShow);
                SuccessToast("picture added successfully.");

                //Toast.makeText(getActivity(), "picture added successfully.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ShowToast(e.getMessage());

                //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public String mProfileUrlMaker() {
        long time = System.currentTimeMillis();
        String millis = Long.toString(time);
        //String url = mProviderID+"<<>>"+millis;
        String url = mUserId + "<<>>" + millis;
        return url;
    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    private void pickDocument4() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG4_REQUEST);
    }


    private void dispatchTakePictureIntent() {

        //openCameraForResult (REQUEST_IMAGE_CAPTURE);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    public void getCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showInternetDialogBox ()
    {
        mUserAlertDialog = new Dialog(getActivity());
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
        View layout=layoutInflater.inflate(R.layout.error_custom_toast,(ViewGroup)getView().findViewById(R.id.error_toast_layout));
        TextView textView=layout.findViewById(R.id.toast_text_id);
        textView.setText(text);

        Toast toast=new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM,0,30);
        toast.setView(layout);
        toast.show();
    }

    private void SuccessToast(String text){

        LayoutInflater layoutInflater=getLayoutInflater();
        View layout=layoutInflater.inflate(R.layout.custom_toast_layout,(ViewGroup)getView().findViewById(R.id.custom_toast_layout));
        TextView textView=layout.findViewById(R.id.toast_text_id);
        textView.setText(text);

        Toast toast=new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM,0,30);
        toast.setView(layout);
        toast.show();
    }

}