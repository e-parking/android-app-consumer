package com.nerdcastle.eparking.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nerdcastle.eparking.MainActivity;
import com.nerdcastle.eparking.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.Gravity.RIGHT;


public class PasswordActivity extends AppCompatActivity {


    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    @BindView(R.id.rootFrame)
    FrameLayout rootFrame;

    @BindView(R.id.etPass)
    TextInputEditText etPass;


    //Firebase Section
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String mPhoneNumber = "";
    private String mEmailAddress= "";
    private String mPassword = "";
    private String mUserType = "";
    TextView mTitleTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_password);

        mTitleTV = findViewById(R.id.titleTV);

        Intent intent = getIntent();


        if (intent != null)
        {
             mUserType = intent.getStringExtra("user");

            if (mUserType.equals("old_user"))
            {
                mPhoneNumber = intent.getStringExtra("mobile");
                mTitleTV.setText("Welcome back, sign in to continue");
            }else if (mUserType.equals("new_user"))
            {
                mTitleTV.setText("Please enter password to sign in");
            }
            //Toast.makeText(this, mPhoneNumber, Toast.LENGTH_SHORT).show();
        }

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("ConsumerList");
        mAuth = FirebaseAuth.getInstance();


        ButterKnife.bind(this);

     /*   Slide enterSlide = new Slide(RIGHT);
        enterSlide.setDuration(700);
        enterSlide.addTarget(R.id.llphone);
        enterSlide.setInterpolator(new DecelerateInterpolator(2));
        getWindow().setEnterTransition(enterSlide);

        Slide returnSlide = new Slide(RIGHT);
        returnSlide.setDuration(700);
        returnSlide.addTarget(R.id.llphone);
        returnSlide.setInterpolator(new DecelerateInterpolator());
        getWindow().setReturnTransition(returnSlide);*/


    }


    @OnClick(R.id.fabProgressCircle)
    void nextActivity() {

        int length=etPass.getText().toString().length();


        if (length>0)
        {
            fabProgressCircle.show();
            if (mUserType.equals("new_user"))
            {
                mAuth.getCurrentUser().updatePassword(etPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(PasswordActivity.this, "Password added successfully.", Toast.LENGTH_SHORT).show();

                        etPass.setCursorVisible(false);
                        rootFrame.setAlpha(0.4f);

                        if (etPass.getText().toString().length()<6)
                        {
                            Toast.makeText(PasswordActivity.this, "Password should be minimum 6 Characters", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mFirebaseDatabase.child(mAuth.getUid()).child("mPassword").setValue(etPass.getText().toString());
                            InputMethodManager imm = (InputMethodManager) getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etPass.getWindowToken(), 0);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    fabProgressCircle.hide();
                                    Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);
                        }


                    }
                });
            }else if (mUserType.equals("old_user"))
            {
                mEmailAddress = mPhoneNumber+"@mail.com";
                mPassword = etPass.getText().toString();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+mEmailAddress+"   "+mPassword);

                //Toast.makeText(this, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+mEmailAddress+"   "+mPassword, Toast.LENGTH_SHORT).show();

                mAuth.signInWithEmailAndPassword(mEmailAddress, mPassword).addOnCompleteListener(PasswordActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {


                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Password Activity", "createUserWithEmail:success");
                            Log.d("Password Activity", "Email :"+mEmailAddress);
                            Log.d("Password Activity", "Password :"+etPass.getText().toString());
                            //FirebaseUser user = mAuth.getCurrentUser();

                            fabProgressCircle.hide();
                            Intent intent = new Intent(PasswordActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Password Activity", "createUserWithEmail:failure", task.getException());
                            fabProgressCircle.hide();
                            Toast.makeText(PasswordActivity.this, "Authentication failed ! "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else {
            Toast.makeText(this, "Enter Your password", Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.ivback)
    void back() {
        onBackPressed();
    }



}
