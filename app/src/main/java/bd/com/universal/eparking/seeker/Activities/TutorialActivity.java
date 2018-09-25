package bd.com.universal.eparking.seeker.Activities;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import bd.com.universal.eparking.seeker.Adapters.SlideShowAdapter;

import java.util.Timer;
import java.util.TimerTask;

import bd.com.universal.eparking.seeker.R;
import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    ViewPager viewPager;
    SlideShowAdapter adapter;
    CircleIndicator indicator;
    Handler handler;
    Runnable runnable;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);


        viewPager = findViewById(R.id.viewPager_id);
        indicator = findViewById(R.id.circleIndicator_id);

        adapter = new SlideShowAdapter(this);

        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {

                int i = viewPager.getCurrentItem();

                if (i==adapter.images.length-1){

                    i=0;
                    viewPager.setCurrentItem(i,true);

                }else {

                    i++;
                    viewPager.setCurrentItem(i,true);

                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(runnable);
            }
        },4000,4000);


    }


}
