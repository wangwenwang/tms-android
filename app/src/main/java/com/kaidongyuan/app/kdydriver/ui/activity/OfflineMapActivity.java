package com.kaidongyuan.app.kdydriver.ui.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;

import com.kaidongyuan.app.kdydriver.adapter.OfflinecitylistAdapter;
import com.kaidongyuan.app.kdydriver.bean.baidumap.OfflineCitymap;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by Administrator on 2016/4/20.
 */
public class OfflineMapActivity extends BaseActivity implements MKOfflineMapListener {
    SlidingTitleView title;

      EditText search_ed;
      TextView search_tv;
//    ArrayList<MKOLUpdateElement>localMapList;
    ArrayList<OfflineCitymap> citymaps;
    MKOfflineMap moffline=null;
    Handler handler;
    OfflinecitylistAdapter offlinecitylistAdapter;
    ListView offlinecitylistlistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offlinemap);
        moffline=new MKOfflineMap();
        moffline.init(this);
        initdata();
        initview();
    }

    @Override
    public void initWindow() {
        //重写为空，针对满屏页面取消沉浸式状态栏
    }

    private void initdata() {
        citymaps=new ArrayList<>();
        ArrayList<MKOLSearchRecord> offlinecitylist=moffline.getOfflineCityList();
        ArrayList<MKOLUpdateElement> allUpdatecitys=moffline.getAllUpdateInfo();
        for (MKOLSearchRecord record:offlinecitylist){

            if (record.cityID==2912||record.cityID==2911||record.cityID==9000)continue;//排除港澳台地区离线地图
           if (record.cityType==1){
               for (MKOLSearchRecord record1:record.childCities){
             OfflineCitymap citymap=creatCitymap(record1,allUpdatecitys);
                   citymaps.add(citymap);
               }
               continue;
           }
           OfflineCitymap citymap=creatCitymap(record,allUpdatecitys);
            citymaps.add(citymap);
        }
        handler = new android.os.Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Bundle bundle=msg.getData();
                switch (bundle.getString("strkey")){
                    case "downloading":
                        moffline.start(((OfflineCitymap)bundle.getParcelable("citymap")).getCityCode());
                        break;
                    case "pause":
                        moffline.pause(((OfflineCitymap) bundle.getParcelable("citymap")).getCityCode());
                        try {
                            Thread.sleep(500);
                            ((OfflineCitymap) bundle.getParcelable("citymap")).setFlag(OfflineCitymap.CitymapFlag.PAUSE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "start":
                        boolean b=moffline.start(((OfflineCitymap)bundle.getParcelable("citymap")).getCityCode());

                        b=false;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        offlinecitylistAdapter=new OfflinecitylistAdapter(citymaps,OfflineMapActivity.this,handler);
    }
    private OfflineCitymap creatCitymap(MKOLSearchRecord record, ArrayList<MKOLUpdateElement> allUpdatecitys){
        OfflineCitymap citymap=new OfflineCitymap();
        citymap.setCityCode(record.cityID);
        DecimalFormat decimalFormat=new DecimalFormat("0.0");//将double类型保留一位小数，不四舍五入
        citymap.setCityName(record.cityName+":"+decimalFormat.format((double)record.size/(1024*1024))+"M");
        if (allUpdatecitys!=null){
            for (MKOLUpdateElement element:allUpdatecitys){
                if (citymap.getCityCode()==element.cityID){
                    citymap.setProgress(element.ratio);
                }
            }
        }
        return citymap;
    }
    private void initview() {
        title= (SlidingTitleView) findViewById(R.id.title_offlineMapActivity);
        title.setText("离线地图管理");
        title.findViewById(R.id.tv_headview_content).setVisibility(View.GONE);
        title.setMode(SlidingTitleView.MODE_BACK);
        search_tv=title.getManagment();
       // search_tv.setVisibility(View.VISIBLE);
        search_ed= title.getEd_center();
        search_ed.setHint("\t按城市名检索");
       // search_ed.setVisibility(View.VISIBLE);
        offlinecitylistlistview= (ListView) findViewById(R.id.lv_citymaps);
        offlinecitylistlistview.setAdapter(offlinecitylistAdapter);
        search_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != s.toString()) {
                   try {
                       searchCityMaps(citymaps, s.toString().trim());
                   }catch (Exception ex){
                       ex.printStackTrace();
                   }

                }
            }
        });
    }

    private void searchCityMaps(ArrayList<OfflineCitymap> citymaps, String s) {
        ArrayList<OfflineCitymap> searchcitymaps=new ArrayList<>();
        for (OfflineCitymap citymap:citymaps){
            if(citymap.getCityName().contains(s)){
                searchcitymaps.add(citymap);
            }
        }
        offlinecitylistAdapter.setCitymaps(searchcitymaps);
        offlinecitylistAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void finish() {
        super.finish();
    }
    @Override
    public void onGetOfflineMapState(int type, int state) {

        switch (type){
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:{
                MKOLUpdateElement update=moffline.getUpdateInfo(state);
                if (update!=null){
                    for (OfflineCitymap citymap:citymaps){
                        if (update.cityID==citymap.getCityCode()){
                            citymap.setProgress(update.ratio);
                            citymap.setFlag(OfflineCitymap.CitymapFlag.DOWNLOADING);
                            break;
                        }
                    }
                    offlinecitylistAdapter.notifyDataSetChanged();
                }

            }
        }
    }
}
