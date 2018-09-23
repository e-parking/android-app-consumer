package com.nerdcastle.eparking.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.nerdcastle.eparking.OtherClasses.VehicleType;
import com.nerdcastle.eparking.PoJoClasses.Vehicle;
import com.nerdcastle.eparking.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddVehicleFragment extends Fragment {

    private TextView carNumber,motorCycleNumber,blueBookImageUpload;
    private RadioButton carRadioButton, motorCycleRadioButton;
    private EditText vehicleNumberInpur;
    private Button saveButton;
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
    private String vehicleNumber;
    private int count=0;
    private boolean edit=false;
    private String carVehicleId,motorBikeVehicleId,currentVehicleId;
    private LinearLayout linearLayout;


    public interface  AddVehicleFragmentInterface {
        public void goToAddVehicle ();
    }

    public AddVehicleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

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
        radioGroup=view.findViewById(R.id.radioGroupId);
        motorCycleBlueBookImageView=view.findViewById(R.id.motorcycleBlueBookId);
        blueBookImageShow=view.findViewById(R.id.blueBookImageShowId);
        linearLayout=view.findViewById(R.id.layoutId);
        progressDialog = new ProgressDialog(getActivity());

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
                            carVehicleId=data.getKey();
                            car.add(vehicle);
                            if (vehicle.getmBlueBookImage()==null || vehicle.getmBlueBookImage().equals(null)| vehicle.getmBlueBookImage().isEmpty()){
                            }
                            else {
                                Picasso.get().load(vehicle.getmBlueBookImage()).placeholder(R.drawable.book).error(R.drawable.book).into(carBlueBookImageView);
                            }

                            carNumber.setText(vehicle.getmVehicleNumber()+"\n"+vehicle.getmVehicleType());
                        }
                        else if (vehicle.getmVehicleType().equals(VehicleType.MotorCycle))
                        {
                            motorCycleCardView.setVisibility(View.VISIBLE);
                            motorCycleRadioButton.setVisibility(View.GONE);
                            motorBikeVehicleId=data.getKey();
                            motorByke.add(vehicle);
                            if (vehicle.getmBlueBookImage()==null || vehicle.getmBlueBookImage().equals(null)| vehicle.getmBlueBookImage().isEmpty()){
                            }
                            else {
                                Picasso.get().load(vehicle.getmBlueBookImage()).placeholder(R.drawable.book).error(R.drawable.book).into(motorCycleBlueBookImageView);
                            }
                            motorCycleNumber.setText(vehicle.getmVehicleNumber()+"\n"+vehicle.getmVehicleType());
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        blueBookImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count<2){
                    vehicleNumber=vehicleNumberInpur.getText().toString();
                    if (carRadioButton.isChecked())
                        vehicleType= VehicleType.Car;
                    if (motorCycleRadioButton.isChecked())
                        vehicleType=VehicleType.MotorCycle;
                    if (vehicleNumber==null||vehicleNumber.equals(null) || vehicleNumber.isEmpty()){
                        Toast.makeText(getContext().getApplicationContext(), "Add vehicle number", Toast.LENGTH_SHORT).show();
                    }/*
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
                    Toast.makeText(getActivity().getApplicationContext(), "You cann't add more then two vehicle", Toast.LENGTH_SHORT).show();
                }


            }
        });



        carCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit=true;
                count=0;
                currentVehicleId=carVehicleId;
                carRadioButton.setVisibility(View.VISIBLE);
                motorCycleRadioButton.setVisibility(View.GONE);
                carRadioButton.setChecked(true);
                mParkPlacePhotoUrl=car.get(0).getmBlueBookImage();
                vehicleNumberInpur.setText(car.get(0).getmVehicleNumber());

                if (car.get(0).getmBlueBookImage()==null || car.get(0).getmBlueBookImage().equals(null)| car.get(0).getmBlueBookImage().isEmpty()){
                    blueBookImageShow.setImageResource(R.drawable.book);
                }
                else {
                    Picasso.get().load(car.get(0).getmBlueBookImage()).placeholder(R.drawable.book).error(R.drawable.book).into(blueBookImageShow);
                }

            }
        });


        motorCycleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit=true;
                count=0;
                currentVehicleId=motorBikeVehicleId;
                vehicleNumberInpur.setText(motorByke.get(0).getmVehicleNumber());
                motorCycleRadioButton.setVisibility(View.VISIBLE);
                carRadioButton.setVisibility(View.GONE);
                mParkPlacePhotoUrl=motorByke.get(0).getmBlueBookImage();
                motorCycleRadioButton.setChecked(true);
                if (car.get(0).getmBlueBookImage()==null || car.get(0).getmBlueBookImage().equals(null)| car.get(0).getmBlueBookImage().isEmpty()){
                    blueBookImageShow.setImageResource(R.drawable.book);
                }
                else {
                    Picasso.get().load(motorByke.get(0).getmBlueBookImage()).placeholder(R.drawable.book).error(R.drawable.book).into(blueBookImageShow);
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

            newVehicle.put("mVehicleNumber", vehicleNumber);
            newVehicle.put("mVehicleType", vehicleType);
            newVehicle.put("mBlueBookImage", mParkPlacePhotoUrl);
            addVehicleDB.setValue(newVehicle).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(getActivity().getApplicationContext(), "Vehicle added successfully", Toast.LENGTH_SHORT).show();
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

            newVehicle.put("mVehicleNumber", vehicleNumber);
            newVehicle.put("mVehicleType", vehicleType);
            newVehicle.put("mBlueBookImage", mParkPlacePhotoUrl);
            addVehicleDB.push().setValue(newVehicle).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(getActivity().getApplicationContext(), "Vehicle added successfully", Toast.LENGTH_SHORT).show();
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
        motorCycleNumber.setText(vehicleNumber+"\n"+vehicleType);
    }

    private void showCarDetails() {

        carBlueBookImageView.setImageBitmap(mBitmapImage4);
        carNumber.setText(vehicleNumber+"\n"+vehicleType);
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
                Toast.makeText(getActivity(), "Blue Book picture added successfully.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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

}