package com.nerdcastle.eparking.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nerdcastle.eparking.PoJoClasses.Consumer;
import com.nerdcastle.eparking.PoJoClasses.Provider;
import com.nerdcastle.eparking.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.Gravity.LEFT;

public class LoginWithPhone extends AppCompatActivity {

    @BindView(R.id.ivback)
    ImageView ivBack;

    @BindView(R.id.etPhoneNo)
    EditText etPhoneNo;

    @BindView(R.id.tvMoving)
    TextView tvMoving;

    @BindView(R.id.ivFlag)
    ImageView ivFlag;

    @BindView(R.id.tvCode)
    TextView tvCode;

    @BindView(R.id.fabProgressCircle)
    FABProgressCircle fabProgressCircle;

    @BindView(R.id.rootFrame)
    FrameLayout rootFrame;

    @BindView(R.id.llphone)
    LinearLayout llPhone;


    private Dialog mUserAlertDialog;
    //Firebase Section
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseRefConsumer;
    private DatabaseReference mFirebaseRefProvider;
    private FirebaseDatabase mFirebaseInstance;
    List<Consumer> consumerList = new ArrayList<>();
    List<Provider> providerList = new ArrayList<>();
    Boolean status = false;
    Boolean isProvider = false;
    private String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_with_phone);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseRefConsumer = mFirebaseInstance.getReference("ConsumerList");
        mFirebaseRefProvider = mFirebaseInstance.getReference("ProviderList");

        mAuth = FirebaseAuth.getInstance();


        ButterKnife.bind(this);
        //setupWindowAnimations();

    }

  /*  private void setupWindowAnimations() {

        ChangeBounds enterTransition = new ChangeBounds();
        enterTransition.setDuration(1000);
        enterTransition.setInterpolator(new DecelerateInterpolator(4));
        enterTransition.addListener(enterTransitionListener);
        getWindow().setSharedElementEnterTransition(enterTransition);

        ChangeBounds returnTransition = new ChangeBounds();
        returnTransition.setDuration(1000);
        returnTransition.addListener(returnTransitionListener);
        getWindow().setSharedElementReturnTransition(returnTransition);

        Slide exitSlide = new Slide(LEFT);
        exitSlide.setDuration(700);
        exitSlide.addListener(exitTransitionListener);
        exitSlide.addTarget(R.id.llphone);
        exitSlide.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(exitSlide);

        Slide reenterSlide = new Slide(LEFT);
        reenterSlide.setDuration(700);
        reenterSlide.addListener(reenterTransitionListener);
        reenterSlide.setInterpolator(new DecelerateInterpolator(2));
        reenterSlide.addTarget(R.id.llphone);
        getWindow().setReenterTransition(reenterSlide);


    }

    Transition.TransitionListener enterTransitionListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {
            ivBack.setImageAlpha(0);
        }

        @Override
        public void onTransitionEnd(Transition transition) {

            ivBack.setImageAlpha(255);
            Animation animation = AnimationUtils.loadAnimation(LoginWithPhone.this, R.anim.slide_right);
            ivBack.startAnimation(animation);

        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    };


    Transition.TransitionListener returnTransitionListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etPhoneNo.getWindowToken(), 0);
            tvMoving.setText(null);
            tvMoving.setHint(getString(R.string.enter_no));
            ivFlag.setImageAlpha(0);
            tvCode.setAlpha(0);
            etPhoneNo.setVisibility(View.GONE);
            etPhoneNo.setCursorVisible(false);
            etPhoneNo.setBackground(null);
            Animation animation = AnimationUtils.loadAnimation(LoginWithPhone.this, R.anim.slide_left);
            ivBack.startAnimation(animation);
        }

        @Override
        public void onTransitionEnd(Transition transition) {


        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    };

    Transition.TransitionListener exitTransitionListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {

            rootFrame.setAlpha(1f);
            fabProgressCircle.hide();
            llPhone.setBackgroundColor(Color.parseColor("#EFEFEF"));
        }

        @Override
        public void onTransitionEnd(Transition transition) {


        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    };


    Transition.TransitionListener reenterTransitionListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {


        }

        @Override
        public void onTransitionEnd(Transition transition) {

            llPhone.setBackgroundColor(Color.parseColor("#FFFFFF"));
            etPhoneNo.setCursorVisible(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    };*/


    @OnClick(R.id.fabProgressCircle)
    void nextPager() {

        fabProgressCircle.show();
        String mobile = etPhoneNo.getText().toString().trim();


        if(mobile.isEmpty() || mobile.length() < 11 || mobile.length()>11){
            etPhoneNo.setError("Enter a valid mobile");
            etPhoneNo.requestFocus();
            return;
        }

        phoneNumber = etPhoneNo.getText().toString();
        isProvider = false;
        getAllProvider();




        etPhoneNo.setCursorVisible(false);
        rootFrame.setAlpha(0.4f);
        fabProgressCircle.show();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /*Intent intent = new Intent(LoginWithPhone.this, PasswordActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginWithPhone.this);
                startActivity(intent, options.toBundle());*/

            }
        }, 1000);
    }

    @OnClick(R.id.ivback)
    void startReturnTransition() {
        super.onBackPressed();
    }





    public void getAllProvider()
    {

        status = false;
        mFirebaseRefProvider.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Provider provider = data.getValue(Provider.class);
                    System.out.println(provider);
                    providerList.add(provider);

                    if (provider!=null && provider.getmPhone()!=null)
                    if (provider.getmPhone().contains(phoneNumber) || provider.getmPhone().equals("+88"+phoneNumber) )
                    {

                        isProvider = true;
                        etPhoneNo.setText("");
                        showInternetDialogBox ();

                        etPhoneNo.setCursorVisible(true);
                        rootFrame.setAlpha(1.0f);
                        fabProgressCircle.hide();

                        break;
                    }
                }



                if (isProvider == false)
                {
                    getAllConsumer();
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginWithPhone.this, "We cant read.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void getAllConsumer()
    {

        status = false;
        mFirebaseRefConsumer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Consumer consumer = data.getValue(Consumer.class);
                    System.out.println(consumer);
                    consumerList.add(consumer);

                    if (consumer!=null && consumer.getmPhone()!=null)
                    if (consumer.getmPhone().contains(phoneNumber) || consumer.getmPhone().equals("+88"+phoneNumber) )
                    {
                        //Toast.makeText(LoginWithPhone.this, "Match Found", Toast.LENGTH_SHORT).show();
                        status = true;
                        break;
                    }
                }

                if (status)
                {
                    fabProgressCircle.hide();
                    Intent intent = new Intent(LoginWithPhone.this, PasswordActivity.class);
                    intent.putExtra("user","old_user");
                    intent.putExtra("mobile",phoneNumber);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginWithPhone.this);
                    startActivity(intent, options.toBundle());
                }else
                {
                    fabProgressCircle.hide();
                    Intent intent = new Intent(LoginWithPhone.this, VerifyPhoneActivity.class);
                    intent.putExtra("mobile", phoneNumber);
                    startActivity(intent);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginWithPhone.this, "We cant read.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showInternetDialogBox ()
    {
        mUserAlertDialog = new Dialog(this);
        mUserAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mUserAlertDialog.setContentView(R.layout.dialog_alert);
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

}
