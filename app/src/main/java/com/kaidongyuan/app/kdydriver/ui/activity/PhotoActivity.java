package com.kaidongyuan.app.kdydriver.ui.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kaidongyuan.app.basemodule.utils.nomalutils.DensityUtil;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2016/10/19.
 */
public class PhotoActivity extends BaseActivity {
    private SlidingTitleView title;
    private PhotoView photoView;
    private String  mpicturePath;
    private String  mstrUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initView();
        initData();

    }

    private void initView() {
        title= (SlidingTitleView) findViewById(R.id.slidingTitelView_photoActivity);
        title.setMode(SlidingTitleView.MODE_BACK);
        title.setText("查看大图");
        photoView= (PhotoView) findViewById(R.id.photoview_photoActivity);
    }
    private void initData() {
       Intent intent=getIntent();
        mpicturePath=intent.getStringExtra("picturePath");
        mstrUrl=intent.getStringExtra("strUrl");
        if (mpicturePath!=null&&mpicturePath.length()>0){
            Uri uri=Uri.fromFile(new File(mpicturePath));
            photoView.setImageURI(uri);
            return;
        }else if (mstrUrl!=null&&mstrUrl.length()>0){
            Glide.with(getMContext()).load(mstrUrl).error(R.drawable.ic_no_record1)
                 .override(DensityUtil.dip2px(720f),DensityUtil.dip2px(720f)).diskCacheStrategy(DiskCacheStrategy.NONE)
                 .fitCenter().into(photoView);
            return;
        }else {
            showToastMsg("图片数据丢失，请重新上传");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }

    }


}
