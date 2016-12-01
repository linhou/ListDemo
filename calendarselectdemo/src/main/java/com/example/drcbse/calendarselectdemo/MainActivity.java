package com.example.drcbse.calendarselectdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MainActivity extends AppCompatActivity  {

    private Button buttons;
    private RelativeLayout mLinearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttons=(Button)findViewById(R.id.button);
        mLinearlayout=(RelativeLayout)findViewById(R.id.li);
        buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarPopupWindow calendarPopupWindow=new CalendarPopupWindow(getApplicationContext(),mLinearlayout);
                calendarPopupWindow.showPopupwindow();
//               PopupWindow popupwindow = calendarPopupWindow.get();
//                TextView textView = new TextView(MainActivity.this);
//                textView.setText("sdfdsfsfddsf");
//                PopupWindow popupwindow = new PopupWindow(textView, 500, 500);
//                popupwindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
//                popupwindow.setOutsideTouchable(true);
//                popupwindow.showAtLocation(mLinearlayout, Gravity.BOTTOM,0,0);

            }
        });
    }

}

