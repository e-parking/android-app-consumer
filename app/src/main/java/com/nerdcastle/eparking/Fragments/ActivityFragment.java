package com.nerdcastle.eparking.Fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nerdcastle.eparking.PoJoClasses.ParkPlace;
import com.nerdcastle.eparking.PoJoClasses.ParkingRequest;
import com.nerdcastle.eparking.PoJoClasses.Process;
import com.nerdcastle.eparking.PoJoClasses.Status;
import com.nerdcastle.eparking.PoJoClasses.StatusOfConsumer;
import com.nerdcastle.eparking.PoJoClasses.TempHolder;
import com.nerdcastle.eparking.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment {

    long MillisecondTime, StartTime;
    Handler handler;

    private TextView mInfoText;
    private android.support.v7.widget.CardView mCardView;

    private Button mCallButton;
    private Button mMapDirectionButton;

    private TextView mAddressTV;
    private TextView mPhoneNumberTV;
    private TextView mDurationTV;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabaseProcess;
    private DatabaseReference mUserCurrentActivityDB;
    private String mConsumerID;
    private Process mProcess;
    private TextView mStatusTV;
    private ImageView mParkPlaceImage;

    boolean isStarted = false;
    long mStartedTime;

    public interface ActivityFragmentInterface {
        public void goToActivity ();

    }
    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
        {
            mConsumerID = mAuth.getCurrentUser().getUid();
        }
        mFirebaseInstance = FirebaseDatabase.getInstance();


       getUserCurrentActivity();

        mInfoText = view.findViewById(R.id.mInfoText);

        mCardView = view.findViewById(R.id.activityCardView);
        mCallButton = view.findViewById(R.id.callButton);
        mMapDirectionButton = view.findViewById(R.id.mapButton);
        mAddressTV = view.findViewById(R.id.addressTV);
        mPhoneNumberTV = view.findViewById(R.id.phoneNumberTV);
        mDurationTV = view.findViewById(R.id.durationTV);
        mStatusTV=view.findViewById(R.id.status_id);
        mParkPlaceImage=view.findViewById(R.id.placeImage);

      /*  if (TempHolder.mStatusOfConsuner != null)
        {
            mPhoneNumberTV.setText(TempHolder.mStatusOfConsuner.getmProviderPhone());
            mAddressTV.setText(TempHolder.mStatusOfConsuner.getmProviderAddress());
        }*/


        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mPhoneNumberTV.getText().toString();
                Toast.makeText(getActivity(), "You are calling "+phoneNumber, Toast.LENGTH_SHORT).show();
                call(phoneNumber);
            }
        });

        mMapDirectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Direction feature is not available yet.", Toast.LENGTH_SHORT).show();
            }
        });


/*


        if (TempHolder.mStatusOfConsuner != null)
        {
            getProcess ();
            mCardView.setVisibility(View.VISIBLE);
            mInfoText.setVisibility(View.GONE);
        }else
        {
            mCardView.setVisibility(View.GONE);
            mInfoText.setVisibility(View.VISIBLE);
        }
*/


        return view;
    }


    private void getUserCurrentActivity() {
        mUserCurrentActivityDB=mFirebaseInstance.getReference("ConsumerList/" + mConsumerID +"/Request/" );

        mUserCurrentActivityDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    ParkingRequest parkingRequest = data.getValue(ParkingRequest.class);
                    if (parkingRequest.getmStatus().equals(Status.PENDING) || parkingRequest.getmStatus().equals(Status.ACCEPTED) ||  parkingRequest.getmStatus().equals(Status.STARTED) )
                    {
                        mCardView.setVisibility(View.VISIBLE);
                        mAddressTV.setText(parkingRequest.getmParkPlaceAddress());
                        mPhoneNumberTV.setText(parkingRequest.getmProviderPhone());
                        mStatusTV.setText(parkingRequest.getmStatus());
                        if (parkingRequest.getmParkPlacePhotoUrl()!=null)
                        Picasso.get().load(parkingRequest.getmParkPlacePhotoUrl()).placeholder(R.drawable.garage_picture).into(mParkPlaceImage);
                    }
                    else {
                        mCardView.setVisibility(View.GONE);
                        mInfoText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*   mUserCurrentActivityDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               *//* if (dataSnapshot.child("mStatus").getValue().toString().equals(Status.PENDING)
                        || dataSnapshot.child("mStatus").getValue().toString().equals(Status.ACCEPTED)
                        || dataSnapshot.child("mStatus").getValue().toString().equals(Status.STARTED)){*//*

                    String address=dataSnapshot.child("mParkPlaceAddress").getValue().toString();
                    mAddressTV.setText(address);
                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

    }


    private void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 456);
            return;
        }
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


/*
    //----------------- Thread ------------------------
    final Runnable runnable = new Runnable() {
        public void run() {
            if (isStarted)
            {
                if (mStartedTime == 0)
                {
                    MillisecondTime = SystemClock.uptimeMillis() - StartTime;
                    mStartedTime = StartTime;
                }else
                {
                    MillisecondTime = SystemClock.uptimeMillis() - mStartedTime;
                }

                long second = (MillisecondTime / 1000) % 60;
                long minute = (MillisecondTime / (1000 * 60)) % 60;
                long hour = (MillisecondTime / (1000 * 60 * 60)) % 24;
                mDurationTV.setText("" + String.format("%02d", hour) + ":"
                        + String.format("%02d", minute) + ":"
                        + String.format("%02d"+"s", second));
                handler.postDelayed(this, 0);

            }

        }

    };*/



   /* public void getProcess ()
    {

        String mProcessID = mConsumerID+"<<>>"+TempHolder.mStatusOfConsuner.getmProviderID();

        mDatabaseProcess = mFirebaseInstance.getReference("OnGoingProcesses/"+mProcessID);
        System.out.println(">>>>>>>>>>>>>> Get Status Called With >>>"+mProcessID);

        mDatabaseProcess.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null)
                {
                    if (dataSnapshot.getValue(Process.class) != null) {
                        mProcess = dataSnapshot.getValue(Process.class);
                        System.out.println(">>>>>>>>>>>>>> Get Process Called  from firebase");
                        if (mProcess != null) {
                            System.out.println("The process is on going.");

                            //------------------------------------------------

                            if (mProcess.getIsStarted().equals("true")) {
                                isStarted = true;
                                mCardView.setVisibility(View.VISIBLE);
                            } else {
                                isStarted = false;
                                mCardView.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "You have no current activity.", Toast.LENGTH_SHORT).show();
                                mDurationTV.setText("00:00:00");
                            }

                            mStartedTime = Long.parseLong(mProcess.getmStartedTime());

                            StartTime = SystemClock.uptimeMillis();
                            handler = new Handler();
                            handler.postDelayed(runnable, 0);
                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mCardView.setVisibility(View.GONE);
                System.out.println("The to read event data: " + databaseError.getCode());
            }
        });
    }*/



}
