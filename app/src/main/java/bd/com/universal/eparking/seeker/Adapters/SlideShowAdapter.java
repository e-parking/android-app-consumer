package bd.com.universal.eparking.seeker.Adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import bd.com.universal.eparking.seeker.R;

public class SlideShowAdapter extends PagerAdapter {
    private Context context;
    LayoutInflater inflater;

    public int[] images = {
          R.drawable.welcome_1,
            R.drawable.welcome_2,
            R.drawable.welcome_3
    };


    public SlideShowAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {

        return images.length;

    }


    @Override
    public boolean isViewFromObject(View view, Object object) {

        return (view==(LinearLayout)object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.slideshow_layout,container,false);

        ImageView img = view.findViewById(R.id.imageView_id);

        //img.setImageResource(images[position]);

        Picasso.get().load(images[position]).into(img);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make( view ,"Tutorial " + ( position + 1 ) , Snackbar.LENGTH_LONG ).show();
            }
        });

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout)object);

    }

}
