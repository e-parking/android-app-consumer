package com.nerdcastle.eparking.Adapters;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nerdcastle.eparking.Activities.PaymentActivity;
import com.nerdcastle.eparking.PoJoClasses.ParkingRequest;
import com.nerdcastle.eparking.PoJoClasses.Status;
import com.nerdcastle.eparking.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.Viewholder> {

    public static FirebaseDatabase mFirebaseInstance;
    public static FirebaseAuth auth;
    public static ProgressDialog progressDialog;
    public static List<ParkingRequest> requestList;
    public static Context context;

    public static String mConsumerID;


    public ActivityAdapter(List<ParkingRequest> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
        mFirebaseInstance = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mConsumerID = auth.getCurrentUser().getUid();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {


        public ImageView mParkImage;
        public TextView mParkAddress;
        public TextView mStatus;
        public TextView mDurationTV,phoneNumberTV;
        public Button mEndButton,callButton,mDirection;
        public RatingBar ratingBar;


        public Viewholder(final View itemView) {
            super(itemView);

            // 2. Define your Views here

            mParkImage = (ImageView)itemView.findViewById(R.id.parkPlaceImage);
            mParkAddress = (TextView)itemView.findViewById(R.id.addressTV);
            mStatus = (TextView)itemView.findViewById(R.id.status_id);
            mDurationTV = (TextView)itemView.findViewById(R.id.durationTV);
            mEndButton = (Button)itemView.findViewById(R.id.endButtonId);
            phoneNumberTV=(TextView)itemView.findViewById(R.id.phoneNumberTV);
            callButton=(Button)itemView.findViewById(R.id.callButton);
            mDirection=(Button)itemView.findViewById(R.id.mapButton);
            ratingBar=itemView.findViewById(R.id.ratingBarId);


        }
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_activities, parent, false);
        return new Viewholder(itemView);

    }

    @Override
    public void onBindViewHolder(final Viewholder holder, int position) {

        final ParkingRequest model = requestList.get(position);
        holder.mParkAddress.setText(model.getmParkPlaceAddress());
        Picasso.get().load(model.getmParkPlacePhotoUrl())
                .placeholder(context.getResources().getDrawable(R.drawable.header_cover))
                .error(context.getResources().getDrawable(R.drawable.header_cover))
                .into(holder.mParkImage);

        if (model.getmConsumerRatingValue()>0){
            holder.ratingBar.setRating(model.getmConsumerRatingValue());
        }
        final long startTime=model.getmStartTime();
        final String number=model.getmProviderPhone();
        holder.phoneNumberTV.setText(number);

        if (model.getmStatus().equals(Status.STARTED))
        {
            if (startTime!=0){
                holder.mDurationTV.setVisibility(View.VISIBLE);
                long currentTime=System.currentTimeMillis();
                long timeDistance=currentTime-startTime;
                int hour= (int) (timeDistance/3600000);
                timeDistance=timeDistance-(hour*3600000);
                int min= (int) (timeDistance/60000);
                timeDistance=timeDistance-(min*60000);
                int sec= (int) (timeDistance/1000);
                holder.mDurationTV.setText(hour+"h:"+min+"m"+" ago");
            }
        }
        else if (model.getmStatus().equals(Status.ENDED))
        {
            holder.mDurationTV.setVisibility(View.VISIBLE);
            long endTime=model.getmEndTime();
            long timeDistance=endTime-startTime;
            int hour= (int) (timeDistance/3600000);
            timeDistance=timeDistance-(hour*3600000);
            int min= (int) (timeDistance/60000);
            timeDistance=timeDistance-(min*60000);
            int sec= (int) (timeDistance/1000);
            holder.mDurationTV.setText(hour+"h:"+min+"m"+"\n"+model.getmEstimatedCost()+" TK");
        }


        String mStatus=model.getmStatus();
        if (mStatus.equals(Status.STARTED))
        {
            holder.mStatus.setText(Status.STARTED);
            holder.mEndButton.setVisibility(View.VISIBLE);
        }
        else if (mStatus.equals(Status.PENDING)){
            holder.mStatus.setText(Status.PENDING);
            holder.mEndButton.setVisibility(View.GONE);

        }
        else if (mStatus.equals(Status.REJECTED)){
            holder.mStatus.setText(Status.REJECTED);
            holder.mEndButton.setVisibility(View.GONE);
        }
        else if (mStatus.equals(Status.ENDED)){
            holder.mStatus.setText(Status.ENDED);
            holder.mEndButton.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.VISIBLE);
        }
        else if (mStatus.equals(Status.ACCEPTED)){
            holder.mStatus.setText(Status.ACCEPTED);
            holder.mEndButton.setVisibility(View.GONE);
        }



        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "You are calling "+number, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 456);
                    return;
                }
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        // 3. set the requestList to your Views here


        holder.mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference parkPlaceRequestDB=mFirebaseInstance.getReference("ProviderList/"+model.getmProviderID()+"/ParkPlaceList/" + model.getmParkPlaceID()+"/Request/"+model.getmRequestID());
                final DatabaseReference consumerRequestDB=mFirebaseInstance.getReference("ConsumerList/"+mConsumerID+"/Request/"+model.getmRequestID());
                final DatabaseReference parkPlaceDB=mFirebaseInstance.getReference("ProviderList/"+model.getmProviderID()+"/ParkPlaceList/" + model.getmParkPlaceID());


                DialogInterface.OnClickListener onClickListener=new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                parkPlaceRequestDB.child("mStatus").setValue(Status.ENDED, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        Toast.makeText(context, Status.ENDED+" service", Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(context, PaymentActivity.class);
                                        intent.putExtra("RequestId",model.getmRequestID());
                                        intent.putExtra("mProviderId",model.getmProviderID());
                                        intent.putExtra("mStartTime",model.getmStartTime());
                                        intent.putExtra("mProviderId",model.getmProviderID());
                                        intent.putExtra("mParkPlaceId",model.getmParkPlaceID());
                                        if (model.getmEndTime()>0){
                                            intent.putExtra("mEndTime",model.getmEndTime());
                                        }
                                        else {
                                            intent.putExtra("mEndTime",System.currentTimeMillis());
                                        }
                                        context.startActivity(intent);
                                    }
                                });
                                consumerRequestDB.child("mStatus").setValue(Status.ENDED);
                                consumerRequestDB.child("mEndTime").setValue(System.currentTimeMillis());
                                parkPlaceRequestDB.child("mEndTime").setValue(System.currentTimeMillis());
                                parkPlaceDB.child("mIsAvailable").setValue("true");

                                holder.mEndButton.setText(Status.ENDED);
                                holder.mEndButton.setEnabled(false);


                                FirebaseFirestore mFireStore=FirebaseFirestore.getInstance();
                                Map<String,Object> notificationMap=new HashMap<>();
                                notificationMap.put("message", model.getmParkPlaceTitle()+" is now free.");
                                notificationMap.put("consumer",mConsumerID);

                                mFireStore.collection("Users").document(model.getmProviderID()).collection("Notifications").add(notificationMap);






                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };


                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Alert");
                builder.setIcon(R.drawable.ic_add_alert_black_24dp);
                builder.setMessage("Are you Sure? ").setPositiveButton("End Now",onClickListener)
                        .setNegativeButton("Not Now",onClickListener).show();


            }
        });






    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }



}