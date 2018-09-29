package bd.com.universal.eparking.seeker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import bd.com.universal.eparking.seeker.Adapters.ActivityAdapter;
import bd.com.universal.eparking.seeker.PoJoClasses.ParkingRequest;
import bd.com.universal.eparking.seeker.PoJoClasses.Status;
import bd.com.universal.eparking.seeker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityFragment extends Fragment {


    private ActivityAdapter activityAdapter;
    private List<ParkingRequest> requestList = new ArrayList<>();
    private RecyclerView mActivityRecyclerView;
    private LinearLayoutManager llm;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseInstance;
    private String mConsumerID;
    private DatabaseReference mUserCurrentActivityDB;
    private TextView mInfoText;
    private ProgressBar progressBar;
    public interface  ActivityFragmentInterface {
        public void goToActivity ();

    }
    public ActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        requestList.clear();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
        {
            mConsumerID = mAuth.getCurrentUser().getUid();
        }
        mFirebaseInstance = FirebaseDatabase.getInstance();


        mInfoText = view.findViewById(R.id.mInfoText);
        progressBar=view.findViewById(R.id.progressBarId);

        mActivityRecyclerView = (RecyclerView) view.findViewById(R.id.consumerActivityRecyclerView);
        llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mActivityRecyclerView.setLayoutManager(llm);

        getUserCurrentActivity();

        return view;
    }





    private void getUserCurrentActivity() {
        mUserCurrentActivityDB = mFirebaseInstance.getReference("ConsumerList/" + mConsumerID + "/Request/");

        Query query=mUserCurrentActivityDB.orderByChild("mRequestTime").limitToLast(7);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        ParkingRequest parkingRequest = data.getValue(ParkingRequest.class);

                        if (parkingRequest.getmStatus().equals(Status.PENDING)
                                || parkingRequest.getmStatus().equals(Status.ACCEPTED)
                                || parkingRequest.getmStatus().equals(Status.STARTED)
                                || parkingRequest.getmStatus().equals(Status.REJECTED)
                                || parkingRequest.getmStatus().equals(Status.ENDED)
                                || parkingRequest.getmStatus().equals(Status.CANCELLED)) {
                            requestList.add(parkingRequest);
                            setNotifactionRecyclerView();

                        }
                    }
                }
                else {
                    setNotifactionRecyclerView();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setNotifactionRecyclerView()
    {
        progressBar.setVisibility(View.GONE);
        if (requestList.size()<1)
        {
            mInfoText.setVisibility(View.VISIBLE);

        }
        //Collections.reverse(requestList);
        activityAdapter = new ActivityAdapter(requestList,getActivity());
        mActivityRecyclerView.setAdapter(activityAdapter);
    }


}
