package com.example.drcbse.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.drcbse.customview.R;

/**
 * Created by Lin.Hou on 2016-11-28.
 */

public class CustomImage extends View {
    private  Context mContext;

    public CustomImage(Context context) {
        this(context,null);
    }

    public CustomImage(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomImage(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public CustomImage(final Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext=context;
        final ImageView imageView=new ImageView(context);
        imageView.setBackgroundResource(R.mipmap.ic_launcher);
        imageView.setOnTouchListener(new OnTouchListener() {
            float currentDistance;
            float lastDistance=-1;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(mContext,"按下操作",Toast.LENGTH_SHORT).show();
                        break;
                    case  MotionEvent.ACTION_MOVE:
                        if (event.getPointerCount()>=2) {
                            float offsetX = event.getX(0) - event.getX(1);
                            float offsetY = event.getX(0) - event.getY(1);
                            currentDistance = (float) Math.sqrt(offsetX * offsetX + offsetY * offsetY);
                        }
                        if (lastDistance<=0){
                            lastDistance=currentDistance;
                        }else {
                            if (currentDistance-lastDistance>=5){
                                Toast.makeText(mContext,"放大操作",Toast.LENGTH_SHORT).show();

                                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                                lp.width=(int) (imageView.getWidth()*1.1f);
                                lp.height=(int) (imageView.getHeight()*1.1);
                                imageView.setLayoutParams(lp);

                                lastDistance=currentDistance;

                            }else if (lastDistance-currentDistance>5){
                                Toast.makeText(mContext,"缩小操作",Toast.LENGTH_SHORT).show();

                                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                                lp.width=(int) (imageView.getWidth()*0.9f);
                                lp.height=(int) (imageView.getHeight()*0.9f);
                                imageView.setLayoutParams(lp);

                                lastDistance=currentDistance;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }
}
