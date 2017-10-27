package com.example.drcbse.customview.view.zoomimage;

import android.annotation.SuppressLint;
import android.content.Context;

import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Lin.Hou on 2017/10/24.
 */

@SuppressLint("AppCompatCustomView")
public class ZoomImageView extends ImageView  implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener,View.OnTouchListener{

    //初始化工作只需要进行一次，所以进行判断
    private  boolean mOnce;

    //初始化时缩放的值
    private float mInitScale;

    //双击放大时达到的值
    private float mMidScale;

    //放大的极限
    private float mMaxScale;

    //矩阵
    private  Matrix mScaleMatrix;
    //进行缩放时监听事件，捕获用户多点触控时缩放的比例
    private ScaleGestureDetector mScaleGestureDetector;

    //----------------自由移动的成员变量--------------------------
    // 记录上一次多点触摸的数量
    private int mLatsPointerCount;

    private float mLastX,mLastY;
    //自由移动的比较值
    private int mTouchSlop;

    private boolean isCanDrag;

    //进行边界检测的变量
    private boolean isCheckLeftAndRight;
    private boolean isCheckTopAndBottom;

    //----------双击放大与缩小------
    private GestureDetector mGestureDetector;
    //已经在双击过程中就不在处理用户的双击事件了
    private boolean isAutoScale;

    //自动放大缩小
    private class  AutoScaleRunnable implements Runnable{
        //缩放的目标值
        private float mTargetScale;
        //缩放的中心点
        private float x;
        private float y;
        //放大的一个梯度
        private final float BIGGER=1.07f;
        //缩小的一个梯度
        private final float SMALL=0.93f;

        private  float tmpScale;



        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;
            if (getScale()<mTargetScale){
                tmpScale=BIGGER;
            }
            if(getScale()>mTargetScale){
                tmpScale=SMALL;
            }

        }

        @Override
        public void run() {
            //进行缩放
            mScaleMatrix.postScale(tmpScale,tmpScale,x,y);
            chekcBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
            float currentScale=getScale();
            if((tmpScale>1.0f&&currentScale<mTargetScale)||(tmpScale<1.0f&&currentScale>mTargetScale)){
                //UI调用自己的run方法，十几以内的的数字，用户是感觉不出来的
                postDelayed(this,16);
            }else{
                //设置为目标值
                float scale=mTargetScale/currentScale;
                mScaleMatrix.postScale(scale,scale,x,y);
                chekcBorderAndCenterWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale=false;
            }
        }
    }

    public ZoomImageView(Context context) {
        this(context,null);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mScaleMatrix=new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector=new ScaleGestureDetector(context,this);
        setOnTouchListener(this);
        //初始化比较值
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAutoScale) return true;
               float x= e.getX();
                float y=e.getY();

                if (getScale()<mMidScale){
//                    mScaleMatrix.postScale(mMidScale/getScale(),mMidScale/getScale(),x,y);
//                    setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mMidScale,x,y),16);
                    isAutoScale=true;
                }else {
//                    mScaleMatrix.postScale(mInitScale/getScale(),mInitScale/getScale(),x,y);
//                    setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(mInitScale,x,y),16);
                    isAutoScale=true;
                }

                return true;
            }
        });
    }

    //view加载的window的时候调用的方法
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    //view消失在window的时候调用的方法
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    //本函数作用是全局布局完成后调用的方法
    //本次作用： 获取ImageView加载完成的图片，当图片加载的时候图片很大就要缩小，图片很小就要放大并且居中
    @Override
    public void onGlobalLayout() {
        //获取图片的大小，然后在获取屏幕大小，进行对比
        if (!mOnce){
            //得到控件的宽和高
            int width=getWidth();
            int height=getHeight();

            //得到图片的图片以及宽和高
            Drawable drawable = getDrawable();
            if (drawable==null){
                return;
            }
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();

            //设置一个缩放值
            float scale=1.0f;
            //如果图片的宽度大于控件的宽度，但是高度小于控件的高度，我们将其缩小
            if (intrinsicWidth>width&&intrinsicHeight<height){
                scale=width*0.1f/intrinsicWidth;
            }
            //如果图片的宽度小于控件的宽度，但是高度大于控件的高度，我们将其放大
            if (intrinsicWidth<width&&intrinsicHeight>height){
                scale=height*1.0f/intrinsicHeight;
            }
            //如果控件的宽高都小于图片的宽高或者都大于图片的宽高，我们去缩放的最小值进行缩放
            if ((intrinsicWidth>width&&intrinsicHeight>height)||(intrinsicWidth<width&&intrinsicHeight<height)){
                scale=Math.min(width*1.0f/intrinsicWidth,height*1.0f/intrinsicHeight);
            }

            //初始化是图片缩放的比例
            mInitScale=scale;
            mMaxScale=mInitScale*4;
            mMidScale=mInitScale*2;

            //将图片移动到屏幕的中心位置
            int dx=getWidth()/2-intrinsicWidth/2;
            int dy=getHeight()/2-intrinsicHeight/2;

            //进行平移操作
            mScaleMatrix.postTranslate(dx,dy);
            //进行缩放操作,后面两个参数是缩放的中心点
            mScaleMatrix.postScale(mInitScale,mInitScale,width/2,height/2);
            setImageMatrix(mScaleMatrix);

            mOnce=true;
        }
    }
    //获取当前图片的缩放值
    public float getScale(){
        float [] values=new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    //缩放进行时
    //缩放的缺件：initScale， maxScaale
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale=getScale();
        float scaleFactor = detector.getScaleFactor();//获取多点触摸时的值
        if(getDrawable()==null){
            return  true;
        }
        //缩放范围的控制
        if ((scale<mMaxScale&&scaleFactor>1.0f)||(scale>mInitScale&&scaleFactor<1.0f)){
           if (scale*scaleFactor<mInitScale){
               scaleFactor=mInitScale/scale;
           }
           if (scale*scaleFactor>mMaxScale){
               scaleFactor=mMaxScale/scale;
           }
           //缩放的中心是图片的中心
//           mScaleMatrix.postScale(scaleFactor,scaleFactor,getWidth()/2,getHeight()/2);
            //detector.getFocusX(),detector.getFocusY()触摸的中线点
           mScaleMatrix.postScale(scaleFactor,scaleFactor,detector.getFocusX(),detector.getFocusY());
            //因为缩放的中心位置变了，所以放大会后然后缩小，图片的位置也会变
            chekcBorderAndCenterWhenScale();
         setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    //放大后图片的宽和高以及上下左右
    private RectF getMatrixRectF(){
        Matrix matrix=mScaleMatrix;
        RectF rectF=new RectF();
        Drawable drawable=getDrawable();
        if (drawable!=null){
            rectF.set(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    //在缩放的是进行边界控制，以及位置的控制
    private void chekcBorderAndCenterWhenScale() {
        RectF matrixRectF = getMatrixRectF();

        float delatX=0;
        float delatY=0;
        //获取控件的宽和高
        int width=getWidth();
        int height=getHeight();


        //缩放时进行边界检测，方式出现白边
        if (matrixRectF.width()>=width){
            if (matrixRectF.left>0){
                delatX=-matrixRectF.left;
            }
            if (matrixRectF.right<width){
                delatX=width-matrixRectF.right;
            }
        }

        if (matrixRectF.height()>=height){
            if (matrixRectF.top>0){
                delatY=-matrixRectF.top;
            }
            if (matrixRectF.bottom<height){
                delatY=height-matrixRectF.bottom;
            }
        }
        //如果宽度和高度小于控件的宽或者高，让其居中
        if (matrixRectF.width()<width){
            delatX=width/2-matrixRectF.right+matrixRectF.width()/2;
        }
        if (matrixRectF.height()<height){
            delatY=height/2-matrixRectF.bottom+matrixRectF.height()/2;
        }
        mScaleMatrix.postTranslate(delatX,delatY);

    }

    //缩放前
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    //缩放后
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
//        mScaleMatrix.postTranslate(getWidth()/2,getHeight()/2);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //事件传递，当双击的时候
        if (mGestureDetector.onTouchEvent(event)){
            return true;
        }

        //多点触摸交由onTouch进行处理，这样可以获取多点触控的多个坐标，在onScale中可以获取多个坐标值
        mScaleGestureDetector.onTouchEvent(event);
        
        float x=0;
        float y=0;
        //拿到多点触摸的数量
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
           x+= event.getX(i);
            y+=event.getY(i);
        }
        x/=pointerCount;
        y/=pointerCount;
        if (mLatsPointerCount!=pointerCount){
            isCanDrag=false;
            mLastX=x;
            mLastY=y;
        }
        mLatsPointerCount=pointerCount;
        RectF rectF=getMatrixRectF();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //+0.01是避免细小的误差
                if (rectF.width()>getWidth()+0.01||rectF.height()>getHeight()+0.01){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (rectF.width()>getWidth()+0.01||rectF.height()>getHeight()+0.01){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                float dx=x-mLastX;
                float dy=y-mLastY;

                if (!isCanDrag){
                    isCanDrag=isMoveAction(dx,dy);
                }

                if(isCanDrag){
                    if (getDrawable()!=null){
                        isCheckLeftAndRight=isCheckTopAndBottom=true;
                        //如果宽度不小于控件的宽度，不允许横向移动
                        if (rectF.width()<getWidth()){
                            isCheckLeftAndRight=false;
                            dx=0;
                        }
                        //如果高度不小于控件高度，不允许竖向移动
                        if (rectF.height()<getHeight()){
                            isCheckTopAndBottom=false;
                            dy=0;
                        }
                        mScaleMatrix.postTranslate(dx,dy);
                        chekcBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX=x;
                mLastY=y;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                mLatsPointerCount=0;
                break;
        }

        
        return true;
    }
    //当移动时进行边界检查
    private void chekcBorderWhenTranslate() {

        RectF rectF=getMatrixRectF();
        float deltaX=0;
        float deltaY=0;
        int width=getWidth();
        int height=getHeight();

        if (rectF.top>0&&isCheckTopAndBottom){
            deltaY=-rectF.top;
        }
        if (rectF.bottom<height&&isCheckTopAndBottom){
            deltaY=height-rectF.bottom;
        }
        if (rectF.left>0&&isCheckLeftAndRight){
            deltaX=-rectF.left;
        }
        if (rectF.right<width&&isCheckLeftAndRight){
            deltaX=width-rectF.right;
        }
        mScaleMatrix.postTranslate(deltaX,deltaY);

    }

    //判断数据是否足以出发move
    private boolean isMoveAction(float dx, float dy) {

        return Math.sqrt(dx*dx+dy*dy)>mTouchSlop;
    }
}
