package com.example.drcbse.customview.view.guaguacard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.drcbse.customview.R;

/**
 * Created by Lin.Hou on 2017/10/10.
 *完善刮刮卡
 */

public class GuaCard2 extends View {

    private Paint mOutPaint;
    private Path mPath; //绘制路径
    private Canvas mCanvas;
    private Bitmap mBitmap;

    private int mLastX;
    private int mLastY;

    private Bitmap  mOutterBitmap;

    //设置底层图片
    private Bitmap bitmap;
    //设置底部文字
    private String  mText;
    //设置底部文字的画笔
    private  Paint mTextPaint;
    //设置一个矩形,测绘TextView的宽和高
    private Rect  mTextRect=new Rect();

    private  int textsize;


    //启动一个县城来计算手指抬起时，阴影面积
    private  Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            int w=getWidth();
            int h=getHeight();
            //擦除区域的大小
            float wipeArea=0;
            //总共像素值
            float totalArea=w*h;

            Bitmap bitmap=mBitmap;
            int [] mPixels=new int[w*h];
            //获得bitmap上所有的像素信息
            bitmap.getPixels(mPixels,0,w,0,0,w,h);

            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    int index=i+j*w;
                    if (mPixels[index]==0){
                        wipeArea++;
                    }
                }
            }
            if (wipeArea>0&&totalArea>0){
                int percent= (int) (wipeArea*100/totalArea);
                Log.i("TAG", "run: "+percent);

                if (percent>60){
                    //清除图层区域
                    mComplete=true;
                    //重新绘制区域。
                    postInvalidate();

                }
            }
        }
    };
    //volatile 异步问题，所以可能存在问题，潜在存在的并发问题。
    //判断遮盖层区域是否达到阈值
    private volatile boolean mComplete =false;



    //创建一个接口告诉用户已经刮得差不多了
    //刮刮卡回调
    public  interface  OnGuaguaKaCompleteLister{
        void complete();
    }
    private OnGuaguaKaCompleteLister mLister;

    public void setOnGuaguaKaComplete(OnGuaguaKaCompleteLister onGuaguaKaComplete) {
        this.mLister = onGuaguaKaComplete;
    }

    public GuaCard2(Context context) {
        this(context,null);
    }

    public GuaCard2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GuaCard2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);

    }

    public void setTextsize(int textsize) {
        this.textsize = textsize;
        //获得当前护臂绘制文本的宽和高，以一个矩形的形式，加入setTextSize方法后，需要重新测量字体大小进行修改，所以移动位置，否则字体会有问题
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextRect);
    }

    public GuaCard2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        TypedArray  a=context.getTheme().obtainStyledAttributes(attrs,R.styleable.GuaCard2,defStyleAttr,defStyleRes);
        int index=a.getIndexCount();
        for (int i = 0; i < index; i++) {
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.GuaCard2_text:
                    mText=a.getString(attr);
                    break;
                case R.styleable.GuaCard2_textColor:
//                    mColor=a.getColor(attr,0X0000);
                    break;
                case R.styleable.GuaCard2_textSize:
                    textsize=(int) a.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,22,getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();


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
        mOutPaint.setColor(Color.parseColor("#c0c0c0"));
        mOutPaint.setAntiAlias(true);
        mOutPaint.setDither(true);
        mOutPaint.setStrokeJoin(Paint.Join.ROUND);//设置是圆角画笔
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);//设置是圆角画笔
        mOutPaint.setStyle(Paint.Style.FILL);//设置画笔属性
        mOutPaint.setStrokeWidth(20);


        //设置绘制文字的画笔属性
        mTextPaint.setColor(Color.DKGRAY);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(textsize);



        //绘制遮盖图层
    //    mCanvas.drawColor(Color.parseColor("#c0c0c0"));
        //mCanvas.drawColor(0x1111);

        mCanvas.drawRoundRect(new RectF(0,0,width,height),30,30,mOutPaint);

        mCanvas.drawBitmap(mOutterBitmap,null,new Rect(0,0,width,height),null);



    }

    //进行一些初始化操作
    private void init() {
        mOutPaint=new Paint();
        mPath=new Path();
//        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.t2);
        mOutterBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.fg_guaguaka);
        mText="谢谢惠顾";
        mTextPaint=new Paint();
        textsize=30;
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
                new Thread(mRunnable).start();
                break;
        }
        invalidate();
        return true;
    }




    @Override
    protected void onDraw(Canvas canvas) {
        //在底层绘制图片
//        canvas.drawBitmap(bitmap,0,0,null);   现在在底部不在绘制图片了改为绘制文字

        //在底层图片区域绘制绘制文字
        canvas.drawText(mText,getWidth()/2-mTextRect.width()/2,getHeight()/2+mTextRect.height()/2,mTextPaint);
    if (!mComplete){
        drawPath();
        //super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,null);}
    else {
        if (mLister!=null){
            mLister.complete();
        }
    }

    }

    private void drawPath() {
        //绘制擦除画笔
        mOutPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mCanvas.drawPath(mPath,mOutPaint);

    }
}
