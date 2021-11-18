package com.example.luke_imagevideo_send.chifen.magnetic.util;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.luke_imagevideo_send.R;
import com.example.luke_imagevideo_send.chifen.magnetic.activity.MainActivity;
import com.example.luke_imagevideo_send.http.base.DialogCallBack;
import com.example.luke_imagevideo_send.http.base.SSHCallBack;

import java.math.BigDecimal;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class getSSHGPSData {
    /**
     * 获取定位信息
     * @param mainActivity
     * @param tvGPS
     */
    public void getGPS(MainActivity mainActivity, TextView tvGPS) {
        try {
            String address = new getIp().getConnectIp();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //发送设置数据
                    SSHExcuteCommandHelper.writeBefor(address, "cat /dev/ttyUSB1", new SSHCallBack() {
                        @Override
                        public void confirm(String data) {
                            try {
                                String [] GpsData = data.split(mainActivity.getResources().getString(R.string.special_data));
                                if (GpsData.length!=0){
                                    String lastData = GpsData[GpsData.length-1];
                                    String [] NEData = lastData.split(",");
                                    String firstN = NEData[1];
                                    String firstE = NEData[3];
                                    BigDecimal DataN = new BigDecimal(Double.valueOf(firstN)/100).setScale(6,BigDecimal.ROUND_HALF_UP);
                                    BigDecimal DataE = new BigDecimal(Double.valueOf(firstE)/100).setScale(6,BigDecimal.ROUND_HALF_UP);
                                    String s = DataE+","+DataN;
                                    tvGPS.setText(s);
                                }else {
//                                    getGPSData();
                                    Location location = getLastKnownLocation(mainActivity,tvGPS);
                                    BigDecimal DataN = new BigDecimal(location.getLongitude()).setScale(6,BigDecimal.ROUND_HALF_UP);
                                    BigDecimal DataE = new BigDecimal(location.getLongitude()).setScale(6,BigDecimal.ROUND_HALF_UP);
                                    tvGPS.setText(DataE+","+DataN);
                                    Log.e("XXXXXXX", "有位置权限-纬度:" + location.getLatitude() + " 经度:" + location.getLongitude());
                                }
                            }catch (Exception ex) {
//                                getGPSData();

                                // 使用
                                Location location = getLastKnownLocation(mainActivity,tvGPS);
                                BigDecimal DataN = new BigDecimal(location.getLongitude()).setScale(6,BigDecimal.ROUND_HALF_UP);
                                BigDecimal DataE = new BigDecimal(location.getLongitude()).setScale(6,BigDecimal.ROUND_HALF_UP);
                                tvGPS.setText(DataE+","+DataN);
                                Log.e("XXXXXXX", "有位置权限-纬度:" + location.getLatitude() + " 经度:" + location.getLongitude());
                            }
                        }

                        @Override
                        public void error(String s) {
                            (mainActivity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mainActivity, s, Toast.LENGTH_LONG).show();
                                    getGPSData(mainActivity,tvGPS);
                                }
                            });
                        }
                    });
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getGPSData(MainActivity mainActivity, TextView tvGPS){
        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(LOCATION_SERVICE);
        new getGPS().initGPS(mainActivity,locationManager,tvGPS, new DialogCallBack() {
            @Override
            public void confirm(String name, Dialog dialog) {

            }

            @Override
            public void cancel() {
                Toast.makeText(mainActivity, "请开启GPS模块", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Location getLastKnownLocation(MainActivity mainActivity, TextView tvGPS) {
        //获取地理位置管理器
        LocationManager mLocationManager = (LocationManager) mainActivity.getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO:去请求权限后再获取
            return null;
        }
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
// 在一些手机5.0(api21)获取为空后，采用下面去兼容获取。
        if (bestLocation==null){
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = mLocationManager.getBestProvider(criteria, true);
            if (!TextUtils.isEmpty(provider)){
                bestLocation = mLocationManager.getLastKnownLocation(provider);
            }
        }
        return bestLocation;
    }
}
