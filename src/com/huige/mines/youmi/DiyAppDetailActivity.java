package com.huige.mines.youmi;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.diy.AdObject;
import net.youmi.android.diy.DiyManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huige.mines.R;

public class DiyAppDetailActivity extends Activity {

    Context context;
    ImageView appIcon;
    TextView appName;
    TextView appSize;
    TextView version;
    Button downloadBtn;
    TextView appDesc;
    ImageView backBtn;
    AppScreenShotScrollView appSSView;
    TextView loading;

    List<AdObject> list;
    AdObject adObject;
    ArrayList<String> appScreenShots;

    Bitmap[] bitmaps;
    int adId;
    MyHandler myHandler = new MyHandler();

    @SuppressWarnings("unchecked")
	public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        /**
         * 所需要资源
         * drawable/btn_back.png
         * drawable/btn_download_details.png
         *
         * */


        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        adId = getIntent().getExtras().getInt("adId");
        if(adId == -1){
            finish();
        }


        //list = DiyManager.getAdList(this);
        adObject = list.get(adId);
        appScreenShots = adObject.getScreenShortcuts();

        setContentView(R.layout.activity_diy_detail);


        backBtn = (ImageView) findViewById(R.id.backBtn);
        appIcon = (ImageView) findViewById(R.id.appIcon);
        appName = (TextView) findViewById(R.id.appName);
        appSize = (TextView) findViewById(R.id.appSize);
        version = (TextView) findViewById(R.id.version);
        downloadBtn = (Button) findViewById(R.id.downloadBtn);
        appDesc = (TextView) findViewById(R.id.appDesc);
        appSSView = (AppScreenShotScrollView) findViewById(R.id.appSSView);

        appIcon.setImageBitmap(adObject.getIcon());
        appName.setText(adObject.getAppName());
        appSize.setText(adObject.getSize());
        version.setText("版本："+adObject.getVersionName());
        appDesc.setText(adObject.getDescription());

        loading = (TextView) findViewById(R.id.loading);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });


        downloadBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //DiyManager.downloadAd(context, adObject.getAdId());
            }
        });


        Thread thread = new Thread(new Runnable() {
            public void run() {
                try{
                    if(appScreenShots!=null && appScreenShots.size()>0){
                        bitmaps = new Bitmap[appScreenShots.size()];

                        for(int i=0; i<appScreenShots.size(); i++){
                            bitmaps[i] = ImageLoader.loadBitmapFromNetWork(appScreenShots.get(i));
                        }

                        myHandler.sendEmptyMessageAtTime(-1, 0);

                    }
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }


    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            appSSView.initView();
            appSSView.setImages(bitmaps);
            loading.setVisibility(View.INVISIBLE);
        }
    }


}
