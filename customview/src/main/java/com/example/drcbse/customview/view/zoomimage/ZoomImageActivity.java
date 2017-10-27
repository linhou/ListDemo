package com.example.drcbse.customview.view.zoomimage;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.drcbse.customview.R;

public class ZoomImageActivity extends AppCompatActivity {

    private ViewPager mViewpage ;
    private int[] mImage=new int[]{R.mipmap.t2,R.mipmap.fg_guaguaka,R.mipmap.ic_launcher};
    private ImageView[] imageViews=new ImageView[mImage.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_zoom_image);
        mViewpage= (ViewPager) findViewById(R.id.viewpage);

        mViewpage.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView zoomImageView=new ZoomImageView(getApplicationContext());
                zoomImageView.setImageResource(mImage[position]);
                container.addView(zoomImageView);
                imageViews[position]=zoomImageView;
                return zoomImageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews[position]);
            }

            @Override
            public int getCount() {
                return imageViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });

    }

}
