package bd.com.universal.eparking.seeker.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import bd.com.universal.eparking.seeker.PoJoClasses.ReportItem;
import bd.com.universal.eparking.seeker.R;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {


    //-------------Firebase ------------------
    public static FirebaseDatabase mFirebaseInstance;
    public FirebaseAuth mAuth;
    public static DatabaseReference mDatabaseReportIssue;

    //-----------------------------------------
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mReportItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setTitle("Report The Issue");

        initializeItems ();




        mFirebaseInstance = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();



        mListView = (ListView) findViewById(R.id.mReportListView);

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mReportItems);

        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ReportItem mReportItem = new ReportItem("",mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getDisplayName(),mReportItems.get(position));
                mReportTheIssue (mReportItem);

            }
        });


    }


    public void initializeItems ()
    {
        mReportItems.add("The Place was too narrow.");
        mReportItems.add("There was no CC Camera.");
        mReportItems.add("There was no security gaurd.");
        mReportItems.add("There was already someone else car.");
    }


    public void mReportTheIssue(ReportItem mReportItem)
    {


        mDatabaseReportIssue = mFirebaseInstance.getReference("ReportedIssue");
        String key = mDatabaseReportIssue.push().getKey();
        mReportItem.mReportID = key;
                mDatabaseReportIssue.child(key).setValue(mReportItem, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                if (firebaseError != null) {
                    System.out.println("Report Not Sent to the server.");
                    Toast.makeText(ReportActivity.this, "Reported", Toast.LENGTH_SHORT).show();

                } else {
                    System.out.println("Successfully Reported to the server.");
                    finish();

                }
            }
        });
    }



}
