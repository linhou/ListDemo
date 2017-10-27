package com.example.indexablelist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfRenderer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.sax.RootElement;
import android.view.MotionEvent;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

/**
 * Created by Lin.Hou on 2017/10/17.
 */

public class IndexScroller {
    private float mIndexbarWidth;//索引条的宽度
    private float mIndexbarMargin;// 索引条距离右侧的距离
    private float mPreviewPadding;  //文本距离四周的距离，在中心显示的文本
   //作用：自动调整索引条
    private float mDensity;             //当前的屏幕密度除以160
    private float mScaledDensity; //当前屏幕密度除以160，设置字体的尺寸
    private float mAlphaRate;    //设置透明度，用于显示和隐藏索引条，0到1之间的数值
    private  int mState=STATE_HIDDEN; //索引条当前的状态
    private int  mListViewWidth; //listView的宽度
    private int mListViewHeight;//listView的高度
    private int mCurrentSection=-1;//当前所点中索引条中的索引
    private boolean mIsIndexing=false;
    private ListView mListView=null;
    private SectionIndexer mIndexer=null;
    private  String[] mSections=null;
    private RectF mIndexbarRect;

    private static final int STATE_HIDDEN=0;
    private static final int STATE_SHOWING=1;
    private static final int STATE_SHOWN=2;
    private static final int STATE_HIDING=3;

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (mState){
                case STATE_SHOWING:
                    mAlphaRate+=(1-mAlphaRate)*0.2;
                    if (mAlphaRate>0.9){
                        mAlphaRate=1;
                        setState(STATE_SHOWN);
                    }
                    mListView.invalidate();//调用本方法draw方法就会不断刷新
                    fade(10);
                    break;
                case STATE_SHOWN:
                    setState(STATE_HIDING);
                    break;
                case STATE_HIDING:
                    mAlphaRate-=mAlphaRate*0.2;
                    if (mAlphaRate<0.1){
                        mAlphaRate=0;
                        setState(STATE_HIDDEN);
                    }
                    mListView.invalidate();
                    fade(10);
                    break;
            }
        }
    };

    private void fade(long i) {
        //首先清除消息
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageAtTime(0, SystemClock.uptimeMillis()+i);
    }

    //索引条的初始化与尺寸本地化（根据屏幕的密度绘制索引条）
    public IndexScroller(Context context,ListView listView){
        //通过上下文来获取当前屏幕密度
        mDensity=context.getResources().getDisplayMetrics().density;
        //获取字体的密度
        mScaledDensity=context.getResources().getDisplayMetrics().scaledDensity;
        mListView=listView;
        setAdapter(mListView.getAdapter());
        //根据屏幕密度计算索引条的宽度，单位是像素
        mIndexbarWidth=20*mDensity;
        mIndexbarMargin=10*mDensity;
        mPreviewPadding=5*mDensity;
    }
    //绘制预览索引条
    public void draw(Canvas canvas){
        //绘制步骤
        //1.绘制索引条。包括索引条的背景和文本
        //2.绘制预览文本和背景

        //如果索引条隐藏，就不行绘制
        if (mState==STATE_HIDDEN){
            return;
        }
        //设置索引条背景的绘制属性
        Paint indexbarPaint=new Paint();
        indexbarPaint.setColor(Color.BLACK);
        indexbarPaint.setAlpha((int)(64*mAlphaRate));
        indexbarPaint.setAntiAlias(true);

        //绘制索引条，第一个参数是绘制的矩形，第二第三个是圆角的圆弧
        canvas.drawRoundRect(mIndexbarRect,5*mDensity,5*mDensity,indexbarPaint);

        //绘制Section
        if (mSections!=null&&mSections.length>0){
            //绘制预览文本和背景，在中央显示的文本
            if (mCurrentSection>=0){
                Paint perviwPaint=new Paint();
                perviwPaint.setColor(Color.BLACK);
                perviwPaint.setAlpha(96);
                perviwPaint.setAntiAlias(true);
                perviwPaint.setShadowLayer(3,0,0,Color.argb(64,0,0,0));

                Paint previewTextPaint=new Paint();
                previewTextPaint.setColor(Color.WHITE);
                previewTextPaint.setAntiAlias(true);
                previewTextPaint.setTextSize(50*mDensity);

                float previewTextWidth=previewTextPaint.measureText(mSections[mCurrentSection]);
                float previewSize=2*mPreviewPadding+previewTextPaint.descent()-previewTextPaint.ascent();
                //预览文本的区域定义
                RectF previewRect=new RectF((mListViewWidth-previewSize)/2,(mListViewHeight-previewSize)/2,(mListViewWidth-previewSize)/2+previewSize,(mListViewHeight-previewSize)/2+previewSize);
               //绘制背景，也是一个圆角
                canvas.drawRoundRect(previewRect,5*mDensity,5*mDensity,perviwPaint);
                //绘制预览文本
                canvas.drawText(mSections[mCurrentSection],previewRect.left+(previewSize-previewTextWidth)/2-1,previewRect.top+mPreviewPadding-previewTextPaint.ascent()+1,previewTextPaint);
            }
            //设置索引的绘制属性
            Paint indexPaint =new Paint();
            indexPaint.setColor(Color.WHITE);
            indexPaint.setAlpha((int)(255*mAlphaRate));
            indexPaint.setAntiAlias(true);
            indexPaint.setTextSize(12*mScaledDensity);

            float sectionHeight=(mIndexbarRect.height()-2*mIndexbarMargin)/mSections.length;
            float paddingTop =(sectionHeight-(indexPaint.descent())-indexPaint.ascent())/2;
            for (int i = 0; i < mSections.length; i++) {
                float paddingLeft=(mIndexbarWidth-indexPaint.measureText(mSections[i]))/2;
                canvas.drawText(mSections[i],mIndexbarRect.left+paddingLeft,mIndexbarRect.top+mIndexbarMargin+sectionHeight*i+paddingTop-indexPaint.ascent(),indexPaint);
            }
        }
    }

    public boolean onTouchuEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mState != STATE_HIDDEN && contains(ev.getX(), ev.getY())) {
                    setState(STATE_SHOWN);
                    mIsIndexing = true;
                    //通过触摸点获取当前Section的索引
                    mCurrentSection = getSectionByPoint(ev.getY());
                    //将listView定位到指定的item
                    mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsIndexing){
                    if (contains(ev.getX(),ev.getY())){
                        mCurrentSection=getSectionByPoint(ev.getY());
                        mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsIndexing){
                    mIsIndexing=false;
                    mCurrentSection=-1;
                }
                if (mState==STATE_SHOWN)
                    setState(STATE_HIDING);
                break;
        }

        return false;

    }

    public void show(){
        if (mState==STATE_HIDDEN)
            setState(STATE_SHOWING);
        else if (mState==STATE_HIDING)
            setState(STATE_HIDING);
    }
    public void hide(){
        if (mState==STATE_SHOWN)
            setState(STATE_HIDING);
    }

    private int getSectionByPoint(float y) {
        if (mSections==null||mSections.length==0){
            return 0;
        }
        if (y<mIndexbarRect.top+mIndexbarMargin){
            return 0;
        }
        if (y>=mIndexbarRect.top+mIndexbarRect.height()-mIndexbarMargin){
        return mSections.length-1;
        }
    return (int)((y-mIndexbarRect.top-mIndexbarMargin)/((mIndexbarRect.height()-2*mIndexbarMargin)/mSections.length));
    }

    public void onSizechanged(int w,int h,int oldw,int oldh){
        mListViewWidth=w;
        mListViewHeight=h;
        mIndexbarRect=new RectF(w-mIndexbarMargin-mIndexbarWidth,mIndexbarMargin,w-mIndexbarMargin,h-mIndexbarMargin);
    }

    public boolean contains(float x,float y){
        return (x>mIndexbarRect.left&&y>mIndexbarRect.top&&y<=mIndexbarRect.top+mIndexbarRect.height());
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof SectionIndexer){
            mIndexer=(SectionIndexer)adapter;
            //字符串数组
            mSections=(String[])mIndexer.getSections();
        }

    }

    public void setState(int state) {
        if (state<STATE_SHOWN||state>STATE_SHOWING){
            return;
        }
        mState=state;
        switch (mState){
            case STATE_HIDDEN:
                mHandler.removeMessages(0);
                break;
            case STATE_SHOWING://正在显示
                mAlphaRate=0;
                fade(0);
                break;
            case STATE_SHOWN:
                mHandler.removeMessages(0);
                break;
            case STATE_HIDING:
                mAlphaRate=1;
                fade(3000);
                break;
        }

    }
}
