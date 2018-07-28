package com.example.seiya.wifi_map;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.util.Log;

// wifi
import android.net.wifi.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.net.wifi.WifiManager;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            unregisterReceiver(mScanResultsReceiver);
        } catch (IllegalArgumentException e) {
            // 既に登録解除されている場合
            // 事前に知るための API は用意されていない
        }
    }

    private final BroadcastReceiver mScanResultsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // intent に情報が乗っているわけではないので、
            // WifiManager の getScanResults で結果を取得
            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
            List<ScanResult> scanResults = wm.getScanResults();

            for (ScanResult scanResult : scanResults) {
                Log.d("", scanResult.toString());
            }

            // onReceive() は何度も呼ばれるので、
            // 1度で終了させたい場合はここで unregister する
            try {
                unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                // 既に登録解除されている場合
                // 事前に知るための API は用意されていない
            }
        }
    };

}
