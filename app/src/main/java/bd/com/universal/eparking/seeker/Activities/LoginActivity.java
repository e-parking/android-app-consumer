package bd.com.universal.eparking.seeker.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bd.com.universal.eparking.seeker.MainActivity;
import bd.com.universal.eparking.seeker.OtherClasses.LoginPreferences;


import bd.com.universal.eparking.seeker.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.llphone)
    LinearLayout llphone;

    @BindView(R.id.ivUberLogo)
    ImageView uber;

    @BindView(R.id.tvMoving)
    TextView tvMoving;

    @BindView(R.id.tvPhoneNo)
    TextView tvPhoneNo;

    @BindView(R.id.llInfo)
    LinearLayout llInfo;

    @BindView(R.id.ivFlag)
    ImageView ivFlag;

    @BindView(R.id.tvCode)
    TextView tvCode;

    @BindView(R.id.ivback)
    ImageView ivBack;




    //Firebase Section
    private FirebaseAuth mAuth;
    private LoginPreferences mLoginPreferences;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            LoginActivity.this.finish();
        }


        setupWindowAnimations();
        ButterKnife.bind(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        uber.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (0.65 * height)));
        ivBack.setImageAlpha(0);

    }

    private void setupWindowAnimations() {

        ChangeBounds exitTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            exitTransition = new ChangeBounds();
            exitTransition.setDuration(1000);
            exitTransition.addListener(exitListener);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementExitTransition(exitTransition);
        }

        ChangeBounds reenterTransition = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            reenterTransition = new ChangeBounds();
            reenterTransition.setDuration(1000);
            reenterTransition.addListener(reenterListener);
            reenterTransition.setInterpolator(new DecelerateInterpolator(4));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementReenterTransition(reenterTransition);
        }

    }


    Transition.TransitionListener exitListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {


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


    Transition.TransitionListener reenterListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(tvMoving, "alpha", 0f, 1f));
            animatorSet.setDuration(800);
            animatorSet.start();
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

            tvMoving.setAlpha(1);
        }
    };

    @OnClick({R.id.llphone, R.id.ivFlag, R.id.tvPhoneNo})
    void startTransition() {

        Intent intent = new Intent(LoginActivity.this, LoginWithPhone.class);

        Pair<View, String> p1 = Pair.create((View) ivBack, getString(R.string.transition_arrow));
        Pair<View, String> p2 = Pair.create((View) ivFlag, getString(R.string.transition_ivFlag));
        Pair<View, String> p3 = Pair.create((View) tvCode, getString(R.string.transition_tvCode));
        Pair<View, String> p4 = Pair.create((View) tvPhoneNo, getString(R.string.transition_tvPhoneNo));
        Pair<View, String> p5 = Pair.create((View) llphone, getString(R.string.transition_llPhone));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1, p2, p3, p4, p5);
        startActivity(intent, options.toBundle());
    }







}
