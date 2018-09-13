package com.nerdcastle.eparking.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nerdcastle.eparking.R;

public class PaymentActivity extends AppCompatActivity {

    TextView mDuration,mAmmount,mSubmit,mProviderName;
    RatingBar mRatingBar;
    ImageView mProviderImage,mVehicleType;
    EditText mComment;
    String mRequestId;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUserId;
    private DatabaseReference consumerRequestDB;
    private long mStartTime,mEndTime;
    private String mParkPlaceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mDuration=findViewById(R.id.duration_id);
        mAmmount=findViewById(R.id.ammount_id);
        mSubmit=findViewById(R.id.submit_button_id);
        mProviderName=findViewById(R.id.provider_name_id);
        mProviderImage=findViewById(R.id.provider_image_id);
        mVehicleType=findViewById(R.id.vehicle_id);
        mRatingBar=findViewById(R.id.rating_bar_id);
        mComment=findViewById(R.id.comment_id);


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mCurrentUserId=mAuth.getCurrentUser();


        Intent intent=getIntent();
        mRequestId=intent.getStringExtra("RequestId");

        consumerRequestDB=mFirebaseInstance.getReference("ConsumerList/"+mCurrentUserId+"/Request/"+mRequestId);

        consumerRequestDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mStartTime= (long) dataSnapshot.child("mStartTime").getValue();
                mEndTime= (long) dataSnapshot.child("mEndTime").getValue();
                calculateDuration();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void calculateDuration() {
        long timeDistance=mEndTime-mStartTime;
        int hour= (int) (timeDistance/3600000);
        timeDistance=timeDistance-(hour*3600000);
        int min= (int) (timeDistance/60000);
        timeDistance=timeDistance-(min*60000);
        int sec= (int) (timeDistance/1000);
        mDuration.setText("Total Time- "+hour+"h:"+min+"m:"+sec+"s");
        calculateTk(hour,min);
    }

    private void calculateTk(int hour,int min) {

    }


}
