package com.example.drcbse.calendarselectdemo;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.drcbse.calendarselectdemo.calendar.cons.DPMode;
import com.example.drcbse.calendarselectdemo.calendar.views.MonthView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lin.Hou on 2016-11-28.
 */

public class CalendarPopupWindow implements MonthView.OnDateChangeListener, MonthView.OnDatePickedListener,View.OnClickListener{


    private  View mView;
    private MonthView monthView;

    private Calendar now;
    private Context mContext;
    private PopupWindow mPopupWindow;
    private TextView mDataView;
    private TextView mCancel;
    private TextView mConfirm;


    public  CalendarPopupWindow(Context context,View view){
        this.mContext=context;
        this.mView=view;
        initPoPupWindow();
    }


    public void initPoPupWindow(){
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        View inflate = LayoutInflater.from(mContext).inflate(
                R.layout.popupwindow_calendar, null);
        mPopupWindow = new PopupWindow(inflate, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // 解决点击外部，popupview消失
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);


        now = Calendar.getInstance();

        mCancel= (TextView) inflate.findViewById(R.id.cancel);
        mConfirm= (TextView) inflate.findViewById(R.id.confirm);
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mDataView = (TextView) inflate.findViewById(R.id.dataText);
        mDataView.setText(now.get(Calendar.YEAR) + "." + (now.get(Calendar.MONTH) + 1));
        monthView = (MonthView) inflate.findViewById(R.id.month_calendar);
        monthView.setDPMode(DPMode.MULTIPLE);
        monthView.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        monthView.setFestivalDisplay(true);
        monthView.setTodayDisplay(true);
        monthView.setOnDateChangeListener(this);
        monthView.setOnDatePickedListener(this);



    }


    @Override
    public void onDatePicked(String date) {

        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat format2 = new SimpleDateFormat("EEEE");
            Date choosedate = format1.parse(date);

            Log.d("info","--------format1----"+format1);
            Log.d("info","--------format2----"+format2);
            Log.d("info","--------choosedate----"+choosedate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDateChange(int year, int month) {
        mDataView.setText(year + "." +month);
    }

    //不显示的情况下 PopupWindow才会出现
    public void showPopupwindow() {

        if (!mPopupWindow.isShowing()) {
             int[] loc = new int[2];
            mView.getLocationOnScreen(loc);
            mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        }
    }

    //消失的事件
    public void dismissPopupWindow(){
        if (mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cancel:
                dismissPopupWindow();
                break;
            case R.id.confirm:

                break;
        }
    }
}
