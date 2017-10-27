package com.example.drcbse.customview.view.guaguacard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.drcbse.customview.R;

/**
 * Created by Lin.Hou on 2017/10/10.
 *没有完善的刮刮卡
 */

public class GuaCard1 extends View {

    private Paint mOutPaint;
    private Path mPath; //绘制路径
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private int mLastX;
    private int mLastY;

    //设置底层图片
    private Bitmap bitmap;

    public GuaCard1(Context context) {
        this(context,null);
    }

    public GuaCard1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GuaCard1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);

    }

    public GuaCard1(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        //绘制遮盖图层
        mCanvas.drawColor(Color.parseColor("#c0c0c0"));
        //mCanvas.drawColor(0x1111);



    }

    //进行一些初始化操作
    private void init() {
        mOutPaint=new Paint();
        mPath=new Path();
        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.t2);
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
        //在底层绘制图片
        canvas.drawBitmap(bitmap,0,0,null);

        drawPath();
        //super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,null);

    }

    private void drawPath() {
        //绘制擦除画笔
        mOutPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mCanvas.drawPath(mPath,mOutPaint);

    }
}
