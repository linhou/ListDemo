package com.example.drcbse.listdemo;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.RotateAnimation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutInflater from = LayoutInflater.from(getApplicationContext());
        LayoutInflater from1 = LayoutInflater.from(getApplicationContext());
        Log.e("tag","------------------>"+from.hashCode()+"------------------------->"+from1.hashCode());
    }
}
