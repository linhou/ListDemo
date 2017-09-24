package com.example.sensordemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private LocationManager locationManager;
    private String locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        textView.setText("获取到locationMangaer");
        //检验版本在6.0或SDK在23以上的权限申请
        if (Build.VERSION.SDK_INT >= 23&&(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
              startActivityForResult(intent, 1);
                Toast.makeText(this,"未授权",Toast.LENGTH_LONG).show();
            return;
        }else {
                    //获取所有可用的位置提供器
            List<String> providers = locationManager.getProviders(true);
            //下面是获取所有的手机权限。
//           List<String> providers = locationManager.getAllProviders();
            for (String s:providers) {
                System.out.println(s);
            }

            if (providers.contains(LocationManager.GPS_PROVIDER)) {
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER;
            } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                //如果是Network
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationProvider = LocationManager.NETWORK_PROVIDER;
                }
            } else {
                Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //获取Location,现在可以获取到GPS
       Location location = locationManager.getLastKnownLocation(locationProvider);
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!=null){
            //不为空,显示地理位置经纬度
            showLocation(location);
        }
//        //监视地理位置变化
    locationManager.requestLocationUpdates(locationProvider, 3000, 1, new LocationListener() {
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
            public void onLocationChanged(Location location) {
            showLocation(location);
            }
            // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }
         // Provider被enable时触发此函数，比如GPS被打开
            @Override
            public void onProviderEnabled(String s) {

            }
        // Provider被disable时触发此函数，比如GPS被关闭
            @Override
            public void onProviderDisabled(String s) {

            }
        });

        //Location还有解绑事件，应该是locationManager.removeUpdates(locationListener);


}

    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                 super.onActivityResult(requestCode, resultCode, data);
                 if (requestCode == 1) {
                        //版本23以上也就是M以上。判断权限是否生效
                         if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&& checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                     }else {
                             Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();

                         }
                   }
           }
    private void showLocation(Location location) {
        String so="维度：" + location.getLatitude() +"\n"
                + "经度：" + location.getLongitude();
        textView.setText(so);
    }
}
