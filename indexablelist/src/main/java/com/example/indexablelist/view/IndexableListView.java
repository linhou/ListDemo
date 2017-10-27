package com.example.indexablelist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Lin.Hou on 2017/10/16.
 * 封装右侧的索引栏和相关的动作
 */

public class IndexableListView extends ListView {
    private  boolean mIsFastScrollenabled=false;
    private IndexScroller mScroller=null;
    private GestureDetector mGestureDetector=null;
    public IndexableListView(Context context) {
        super(context);
    }

    public IndexableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IndexableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public  boolean isFastScrollEnabled(){
        return mIsFastScrollenabled;
    }

    @Override
    public  void setFastScrollEnabled(boolean enabled){
        mIsFastScrollenabled=enabled;
        if (mIsFastScrollenabled){
            if (mScroller==null){

                mScroller=new IndexScroller(getContext(),this);
            }

        }else{
            if (mScroller!=null){
                mScroller.hide();
                mScroller=null;
            }
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        if (mScroller!=null){
            //用于绘制右侧的索引栏
            mScroller.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //如果mScroller自己处理触事件，该方法就返回true；
        if (mScroller!=null&&mScroller.onTouchuEvent(event)){
            return true;
        }
        if (mGestureDetector==null){
            //使用收拾处理触摸事件
            mGestureDetector=new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                   //直接显示右侧的索引条
                    if(mScroller!=null)
                    mScroller.show();

                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
        }
        mGestureDetector.onTouchEvent(event);
        return  super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        if (mScroller.contains(event.getX(),event.getY()))
            return true;
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public void setAdapter(ListAdapter adapter){
        super.setAdapter(adapter);
        if (mScroller!=null){
            mScroller.setAdapter(adapter);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mScroller!=null){
            mScroller.onSizechanged(w,h,oldw,oldh);
        }
    }
}
