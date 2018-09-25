package bd.com.universal.eparking.seeker.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import bd.com.universal.eparking.seeker.MainActivity;
import bd.com.universal.eparking.seeker.R;

import com.squareup.picasso.Picasso;

public class PaymentActivity extends AppCompatActivity {

    TextView mDuration,mAmmount,mSubmit,mProviderName,mAddress;
    RatingBar mRatingBar;
    ImageView mProviderImage,mVehicleType;
    EditText mComment;
    String mRequestId;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    private DatabaseReference consumerRequestDB,providerRequestDB,providerDB;
    private long mStartTime,mEndTime;
    private String mParkPlaceId,mProviderId,mParkingChargePerHour,mParkPlacePhotoUrl;
    private String mParkPlaceTitle,mParkPlaceAddress;
    private int totalCharge;
    private int hour,min;
    private float mRatingValue;
    private String mCommentText;
    private boolean reviewStatus=false;


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
        mAddress=findViewById(R.id.address);


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mCurrentUserId=mAuth.getCurrentUser().getUid();


        Intent intent=getIntent();
        mRequestId=intent.getStringExtra("RequestId");
        mStartTime=intent.getLongExtra("mStartTime",0);
        mEndTime=intent.getLongExtra("mEndTime",0);
        mProviderId=intent.getStringExtra("mProviderId");
        mParkPlaceId=intent.getStringExtra("mParkPlaceId");



        calculateDuration();

        providerDB=FirebaseDatabase.getInstance().getReference("ProviderList/"+mProviderId);
        providerRequestDB=FirebaseDatabase.getInstance().getReference("ProviderList/"+mProviderId+"/ParkPlaceList/" + mParkPlaceId);

        providerRequestDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mParkingChargePerHour=dataSnapshot.child("mParkingChargePerHour").getValue().toString();
                mParkPlacePhotoUrl=dataSnapshot.child("mParkPlacePhotoUrl").getValue().toString();
                mParkPlaceTitle=dataSnapshot.child("mParkPlaceTitle").getValue().toString();
                mParkPlaceAddress=dataSnapshot.child("mAddress").getValue().toString();

                totalCharge=0;
                calculateTk(Integer.valueOf(mParkingChargePerHour));
                Picasso.get().load(mParkPlacePhotoUrl).placeholder(R.drawable.header_cover).error(R.drawable.header_cover).into(mProviderImage);
                mAddress.setText(mParkPlaceTitle+", "+mParkPlaceAddress);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        providerDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mProviderN=dataSnapshot.child("mName").getValue().toString();
                mProviderName.setText(mProviderN);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mRatingValue=rating;
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText=mComment.getText().toString();
                if (mRatingValue>0){
                    SendReviewToDatabase(mRatingValue,commentText);
                }
                else {
                    Toast.makeText(PaymentActivity.this, "Please provide your rating for this parking place", Toast.LENGTH_LONG).show();
                }
                UpdateParkingCharge();

            }
        });

    }

    private void UpdateParkingCharge() {
        DatabaseReference providerDB=FirebaseDatabase.getInstance().getReference("ProviderList/"+mProviderId+"/ParkPlaceList/" + mParkPlaceId+"/Request/"+mRequestId);

        DatabaseReference consumerDB=FirebaseDatabase.getInstance().getReference("ConsumerList/"+mCurrentUserId+"/Request/"+mRequestId);
        providerDB.child("mEstimatedCost").setValue(totalCharge);
        consumerDB.child("mEstimatedCost").setValue(totalCharge);

    }



    private void SendReviewToDatabase(float mRatingValue, String commentText) {

        DatabaseReference mRatingProviderDB=FirebaseDatabase.getInstance().getReference("ProviderList/"+mProviderId+"/ParkPlaceList/" + mParkPlaceId+"/Request/"+mRequestId);
        DatabaseReference mRatingConsumerDB=FirebaseDatabase.getInstance().getReference("ConsumerList/"+mCurrentUserId+"/Request/"+mRequestId);

        mRatingConsumerDB.child("mConsumerRatingValue").setValue(mRatingValue);
        mRatingConsumerDB.child("mConsumerComment").setValue(commentText);
        mRatingConsumerDB.child("mConsumerRatingTime").setValue(System.currentTimeMillis());

        mRatingProviderDB.child("mConsumerRatingValue").setValue(mRatingValue);
        mRatingProviderDB.child("mConsumerComment").setValue(commentText);
        mRatingProviderDB.child("mConsumerRatingTime").setValue(System.currentTimeMillis(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                reviewStatus=true;
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        UpdateParkingCharge();
        if (reviewStatus==true){
            super.onBackPressed();
        }
        else {
            Toast.makeText(this, "Please provide your rating for this parking place", Toast.LENGTH_LONG).show();
        }
    }

    private void calculateDuration() {
        long timeDistance=mEndTime-mStartTime;
        hour= (int) (timeDistance/3600000);
        timeDistance=timeDistance-(hour*3600000);
        min= (int) (timeDistance/60000);
        timeDistance=timeDistance-(min*60000);
        int sec= (int) (timeDistance/1000);
        mDuration.setText("Total Time- "+hour+"h:"+min+"m");
    }

    private void calculateTk(int costPerHour) {

        if (hour>0){
            totalCharge=hour*costPerHour;
        }
        if (min>0){
            totalCharge=totalCharge+costPerHour;
        }

        mAmmount.setText("Total Charge- "+totalCharge+" TK");


    }


}
