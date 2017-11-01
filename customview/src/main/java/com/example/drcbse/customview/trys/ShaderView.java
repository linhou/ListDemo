package com.example.drcbse.customview.trys;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ComposeShader;
import android.graphics.DashPathEffect;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.drcbse.customview.R;

/**
 * Created by Lin.Hou on 2017/10/31.
 */

public class ShaderView extends View {

    public ShaderView(Context context) {
        this(context,null);
    }

    public ShaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public ShaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
//        Shader shader=new LinearGradient(0,0,500,500, Color.GREEN,Color.RED, Shader.TileMode.CLAMP);
//        paint.setShader(shader);
//        paint.setTextSize(200);
//        canvas.drawText("Shader",0,500,paint);
//        String text="Shader";
//        Rect rect=new Rect();
//       paint.getTextBounds(text,0,text.length(),rect);
//        Shader shader1=new RadialGradient(rect.width()/2,rect.height()/2,rect.height()/2,Color.GREEN,Color.RED, Shader.TileMode.CLAMP);
//        paint.setShader(shader1);
//        canvas.drawText(text,0,500,paint);
//        Shader shader = new SweepGradient(300, 300, Color.parseColor("#E91E63"),
//                Color.parseColor("#2196F3"));
//        paint.setShader(shader);
//        canvas.drawCircle(300, 300, 200, paint);
//        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.t2);
//        Shader shader=new BitmapShader(bitmap,Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        paint.setShader(shader);
//        canvas.drawCircle(600, 600, 200, paint);
//        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.batmap1);
//        Shader shader1 = new BitmapShader(bitmap1, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        // 第二个 Shader：从上到下的线性渐变（由透明到黑色）
//        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.batmatlogo);
//        Shader shader2 = new BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//        // ComposeShader：结合两个 Shader
//        Shader shader = new ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OVER);
//        paint.setShader(shader);
//        canvas.drawCircle(300, 300, 300, paint);
//        Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
//        canvas.drawBitmap(rectBitmap, 0, 0, paint); // 画方
//        paint.setXfermode(xfermode); // 设置 Xfermode
//        canvas.drawBitmap(circleBitmap, 0, 0, paint); // 画圆
//        paint.setXfermode(null); // 用完及时清除 Xfermode
        PathEffect pathEffect = new DashPathEffect(new float[]{10, 5}, 10);
        paint.setPathEffect(pathEffect);
        canvas.drawCircle(300, 300, 200, paint);
    }
}
