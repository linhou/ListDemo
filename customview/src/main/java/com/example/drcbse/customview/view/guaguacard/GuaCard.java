package com.example.drcbse.customview.view.guaguacard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Lin.Hou on 2017/10/10.
 * 初步刮刮卡效果，这是类似一个画板的内容
 */

public class GuaCard extends View {

    private Paint mOutPaint;
    private Path mPath; //绘制路径
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private int mLastX;
    private int mLastY;

    public GuaCard(Context context) {
        this(context,null);
    }

    public GuaCard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GuaCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);

    }

    public GuaCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }


    //调用本方法就可以去获取控件的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=getMeasuredWidth();
        int height=getMeasuredHeight();
        //初始化bitmap
        mBitmap =Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        //将画布绘制到Bitmap上面
        mCanvas=new Canvas(mBitmap);


        //设置绘制Path的画笔属性,这是橡皮擦的画笔属性
        mOutPaint.setColor(Color.RED);
        mOutPaint.setAntiAlias(true);
        mOutPaint.setDither(true);
        mOutPaint.setStrokeJoin(Paint.Join.ROUND);//设置是圆角画笔
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);//设置是圆角画笔
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setStrokeWidth(20);



    }

    //进行一些初始化操作
    private void init() {
        mOutPaint=new Paint();
        mPath=new Path();
    }

    //点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x= (int) event.getX();
        int y= (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX=x;
                mLastY=y;
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx=Math.abs(mLastX-x);
                int dy=Math.abs(mLastY-y);
                if (dx>3||dy>3){
                    mPath.lineTo(x,y);
                }

                mLastX=x;
                mLastY=y;

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
      drawPath();
        //super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,null);

    }

    private void drawPath() {
        mCanvas.drawPath(mPath,mOutPaint);

    }
}
