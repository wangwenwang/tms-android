package com.kaidongyuan.app.kdydriver.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.kaidongyuan.app.basemodule.utils.nomalutils.SystemUtil;
import com.kaidongyuan.app.basemodule.widget.MLog;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;
import com.kaidongyuan.app.kdydriver.utils.baidumapUtils.DrivingRouteOverlay;
import com.kaidongyuan.app.kdydriver.utils.baidumapUtils.LocationPointUtil;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * 货物轨迹查询页面
 * Created by Administrator on 2015/9/9.
 */
public class OrderTrackCheckActivity extends BaseActivity implements View.OnClickListener {
    private MapView mMapView ;
    private BaiduMap mBaiduMap ;
    private EditText ed_serch_track;
    public LocationClient mLocationClient;// 百度定位客户端
    public MyLocationListener myListener;// 百度定位监听
    private String tempcoor = "bd09ll";// 百度地图的编码模式
    //高精度模式
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private boolean againBoolean=true,isSearchTrack=false,isEdittext=false,isNaviToThis=false;
    private BDLocation currentlocation;
    private BDLocation endlocation;
    private MarkerOptions stOption;
    private MarkerOptions endOption;
    private GeoCoder msearch;
    private OnGetGeoCoderResultListener geoCoderlistener;
    private DecimalFormat decimalFormat;
    private TextView tv_orderToAddress;
    private Intent naviIntent;
    private Snackbar choiceNaviSnackbar;
    private View.OnClickListener baiduNaviOnClickListener,autoNaviOnClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_trackcheck);
        initViews();
        initdata();
    }

    private void initdata() {
        naviIntent=getIntent();
        if (naviIntent.hasExtra("ORD_TO_ADDRESS")&&naviIntent.hasExtra("currentLocation_longitude")&&naviIntent.hasExtra("currentLocation_latitude")){
            isNaviToThis=true;
            tv_orderToAddress.setText(naviIntent.getStringExtra("ORD_TO_ADDRESS"));
            tv_orderToAddress.setVisibility(View.VISIBLE);
            double longtitude=naviIntent.getDoubleExtra("currentLocation_longitude",0.00);
            double latitude=naviIntent.getDoubleExtra("currentLocation_latitude",0.00);
            currentlocation=new BDLocation();
            currentlocation.setLongitude(longtitude);
            currentlocation.setLatitude(latitude);
            if (currentlocation!=null&&currentlocation.getLatitude()!=0.00&&currentlocation.getLongitude()!=0.00) {
                LatLng stLatLng = new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
                BitmapDescriptor stbitmap=BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
                stOption=new MarkerOptions().position(stLatLng).icon(stbitmap).zIndex(5);
                mBaiduMap.addOverlay(stOption);
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude())));
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
            }
            baiduNaviOnClickListener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentlocation!=null&&endlocation!=null&&currentlocation.getLongitude()!=0.00&&currentlocation.getLatitude()!=0.00){
                        //跳转到百度导航
                        try {
                            Intent  baiduintent = Intent.parseUri("intent://map/direction?" +
                                    "origin=latlng:" + currentlocation.getLatitude() + "," + currentlocation.getLongitude() +
                                    "|name:" +currentlocation.getAddrStr() +
                                    "&destination=latlng:" + endlocation.getLatitude() + "," + endlocation.getLongitude()+
                                    "|name:" + endlocation.getAddrStr() +
                                    "&mode=driving" +
                                    "&src=Name|AppName" +
                                    "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end", 0);
                            startActivity(baiduintent);
                        } catch (URISyntaxException e) {
                            MLog.d("URISyntaxException : " + e.getMessage());
                            e.printStackTrace();
                        }

                    }else {
                        showToastMsg("起始点或目的地坐标不完整，请补全后重新导航");
                    }
                }
            };
            autoNaviOnClickListener=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //跳转到高德导航
                    if (currentlocation!=null&&endlocation!=null&&currentlocation.getLongitude()!=0.00&&currentlocation.getLatitude()!=0.00){
                        HashMap<String,Double> stlatlonmap= LocationPointUtil.bd_decrypt(currentlocation.getLatitude(),currentlocation.getLongitude());
                        HashMap<String,Double>edlatlonmap=LocationPointUtil.bd_decrypt(endlocation.getLatitude(),endlocation.getLongitude());
                        Intent autoIntent=new Intent();
                        autoIntent.setData(Uri
                                .parse("androidamap://route?" +
                                        "sourceApplication="+getResources().getString(R.string.app_name)+
                                        "&slat=" + stlatlonmap.get("gcjlat") +
                                        "&slon=" + stlatlonmap.get("gcjlon") +
                                        "&dlat=" + edlatlonmap.get("gcjlat") +
                                        "&dlon=" + edlatlonmap.get("gcjlon") +
                                        "&dname=" +endlocation.getAddrStr()+
                                        "&dev=0" +
                                        "&m=0" +
                                        "&t=2"));
                        startActivity(autoIntent);
                    }else {
                        showToastMsg("起始点、目的地坐标不完整，请补全后重新导航");
                    }
                }
            };
        }else {
            isNaviToThis=false;
            tv_orderToAddress.setVisibility(View.GONE);
        }
    }

    @Override
    public void initWindow() {

    }

    private void initViews(){
        findViewById(R.id.btn_search).setOnClickListener(this);
        SlidingTitleView title = (SlidingTitleView) findViewById(R.id.title_order_track);
        title.setText("路线规划");
        title.setMode(SlidingTitleView.MODE_BACK);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(22.628646D,114.046639D)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(6).build()));
        ed_serch_track = (EditText) this.findViewById(R.id.tv_serch_track);
        ed_serch_track.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //判断输入edittext内容时标识值设为false
                isEdittext=true;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (endlocation==null){
                    endlocation = new BDLocation();
                }
                endlocation.setLatitude(latLng.latitude);
                endlocation.setLongitude(latLng.longitude);
                decimalFormat = new DecimalFormat("0.000");
                myaddoverly(endlocation);
                ed_serch_track.setText("坐标:("+ decimalFormat.format(latLng.latitude)+","+ decimalFormat.format(latLng.longitude)+")");
                isEdittext=false;
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                if (endlocation==null){
                    endlocation=new BDLocation();
                }
                endlocation.setLatitude(mapPoi.getPosition().latitude);
                endlocation.setLongitude(mapPoi.getPosition().longitude);
                ed_serch_track.setText(mapPoi.getName());
                myaddoverly(endlocation);
                isEdittext=false;
                return false;
            }
        });
        msearch=GeoCoder.newInstance();
        geoCoderlistener=new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (geoCodeResult==null||geoCodeResult.error!= SearchResult.ERRORNO.NO_ERROR){
                    //没有检索到结果
                    showToastMsg("检索坐标失败，请重输地址");
                }else {
                    //order_ToAdsGeoCodeResult=geoCodeResult;
                    if (endlocation==null){
                        endlocation=new BDLocation();
                    }
                    endlocation.setLatitude(geoCodeResult.getLocation().latitude);
                    endlocation.setLongitude(geoCodeResult.getLocation().longitude);
                    myaddoverly(endlocation);
                    decimalFormat=new DecimalFormat("0.000");
                    ed_serch_track.setText("坐标("+decimalFormat.format(endlocation.getLatitude())+","+decimalFormat.format(endlocation.getLongitude())+")");
                    isEdittext=false;
                    MLog.e("目的地：latitude"+geoCodeResult.getLocation().latitude+"\tlongitude"+geoCodeResult.getLocation().longitude);
                }
            }
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

            }
        };
        msearch.setOnGetGeoCodeResultListener(geoCoderlistener);
        mLocationClient=new LocationClient(this);
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener);
        initLocationClient();
        tv_orderToAddress= (TextView) findViewById(R.id.tv_order_toAddress);
    }

    private void myaddoverly(BDLocation location) {
        if (mBaiduMap==null)return;
        if (choiceNaviSnackbar!=null&&choiceNaviSnackbar.isShown()){
            choiceNaviSnackbar.dismiss();
        }
        mBaiduMap.clear();
        if (currentlocation!=null) {
            LatLng stLatLng = new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
            BitmapDescriptor stbitmap=BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            stOption=new MarkerOptions().position(stLatLng).icon(stbitmap).zIndex(5);
            mBaiduMap.addOverlay(stOption);
        }
        LatLng edLatLng=new LatLng(location.getLatitude(),location.getLongitude());
        BitmapDescriptor edbitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        endOption = new MarkerOptions().position(edLatLng).icon(edbitmap).zIndex(5);
        mBaiduMap.addOverlay(endOption);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(edLatLng));
        // mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
    }

    /**
     * 初始化定位客户端
     */
    public void initLocationClient() {
        LocationClientOption option = new LocationClientOption();
        option.setProdName(this.getPackageName());
        MLog.w("ProdName:" + this.getPackageName());
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        option.setLocationMode(tempMode);// 设置定位模式
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setIgnoreKillProcess(false);//设置是否忽略在.stop()后停止定位服务
        option.setTimeOut(10 * 1000); // 网络定位的超时时间
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView!=null&&mBaiduMap!=null){
            // mBaiduMap.clear();
            mMapView.onResume();
            startLocate();
        }
    }
    /**
     * 开始定位
     */
    public void startLocate() {
        if (isNaviToThis){
            return;
        }
//        if (mLocationClient != null && !mLocationClient.isStarted()) {
//            mLocationClient.start();
//        }else
        if (mLocationClient!=null){
            mLocationClient.start();
        }else {
            mLocationClient=new LocationClient(this);
            myListener=new MyLocationListener();
            mLocationClient.registerLocationListener(myListener);
            initLocationClient();
            mLocationClient.start();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mMapView!=null){
            mBaiduMap.clear();
            mMapView.onPause();
        }
        if (choiceNaviSnackbar!=null&&choiceNaviSnackbar.isShown()){
            choiceNaviSnackbar.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView!=null){
            mMapView.onDestroy();
        }
        if (msearch!=null){
            msearch.destroy();
        }
        if (mLocationClient!=null&&myListener!=null){
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
        }
    }
    //    private void addPoint (BDLocation location) {
    //        LatLng stLatLng = new LatLng(location.getLatitude(), location.getLongitude());
    //        BitmapDescriptor stbitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
    //        MarkerOptions stOption = new MarkerOptions().position(stLatLng).icon(stbitmap).zIndex(5);
    //        mBaiduMap.addOverlay(stOption);
    //    }

    private void getData(){
        if (endlocation!=null&&currentlocation==null){
            isSearchTrack=true;
            startLocate();
        }else if (endlocation!=null&&currentlocation!=null){
            searchTrack(currentlocation,endlocation);
        }else {
            showToast("请先点选目的地",Toast.LENGTH_SHORT);
        }
    }

    private void searchTrack(final BDLocation currentlocation, BDLocation endlocation) {
        if (mBaiduMap==null)return;
        mBaiduMap.clear();
        LatLng stLatLng=new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        LatLng enLatLng=new LatLng(endlocation.getLatitude(),endlocation.getLongitude());
//        BitmapDescriptor stbitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
//        stOption = new MarkerOptions().position(stLatLng).icon(stbitmap).zIndex(5);
        BitmapDescriptor endbitmap=BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        endOption = new MarkerOptions().position(enLatLng).icon(endbitmap).zIndex(5);
//        mBaiduMap.addOverlay(stOption);
        mBaiduMap.addOverlay(endOption);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(enLatLng));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
        final  RoutePlanSearch mSearch=RoutePlanSearch.newInstance();
        PlanNode stNode=PlanNode.withLocation(stLatLng);
        PlanNode enNode=PlanNode.withLocation(enLatLng);
        final DrivingRoutePlanOption drivingRoutePlanOption=new DrivingRoutePlanOption().from(stNode).to(enNode).policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST);
        OnGetRoutePlanResultListener listener=new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                //获取步行路径规划结果

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                //获取公交路径规划结果

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                //获取驾车路径规划结果
                MLog.e("驾车线路规划结果");
                if (drivingRouteResult==null||drivingRouteResult.error!= SearchResult.ERRORNO.NO_ERROR){
                    try {
                        MLog.w(drivingRouteResult.error.name());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    showToast("路线规划失败，请重试", Toast.LENGTH_SHORT);
                    return;
                }else {
                    MLog.d("onGetDrivingRouteResult no error");
                    try {
                        DrivingRouteOverlay overlay=new MyDrivingRouteOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        overlay.setData(drivingRouteResult.getRouteLines().get(0));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                        mSearch.destroy();
                        if (isNaviToThis){
                            if (SystemUtil.isInstalled(OrderTrackCheckActivity.this,"com.baidu.BaiduMap")&&SystemUtil.isInstalled(OrderTrackCheckActivity.this,"com.autonavi.minimap")){
                                String strbaidu="百度导航";
                                String strauto="高德导航";
                                choiceNaviSnackbar=Snackbar.make(mMapView,strbaidu,Snackbar.LENGTH_INDEFINITE);
                                View v=choiceNaviSnackbar.getView();
                                v.setBackgroundColor(getResources().getColor(R.color.details_text));
                                final TextView tv_snackbar= (TextView) v.findViewById(R.id.snackbar_text);
                                tv_snackbar.setGravity(Gravity.CENTER);
                                tv_snackbar.setTextColor(getResources().getColor(R.color.white));
                                tv_snackbar.setOnClickListener(baiduNaviOnClickListener);
                                choiceNaviSnackbar.setAction(strauto,autoNaviOnClickListener).show();
                                return;
                            }else if (SystemUtil.isInstalled(OrderTrackCheckActivity.this,"com.baidu.BaiduMap")){
                                choiceNaviSnackbar=Snackbar.make(mMapView,"是否启动地图导航？",Snackbar.LENGTH_INDEFINITE);
                                View v=choiceNaviSnackbar.getView();
                                v.setBackgroundColor(getResources().getColor(R.color.details_text));
                                final TextView tv_snackbar= (TextView) v.findViewById(R.id.snackbar_text);
                                tv_snackbar.setGravity(Gravity.CENTER);
                                tv_snackbar.setTextColor(getResources().getColor(R.color.white));
                                choiceNaviSnackbar.setAction("导航",baiduNaviOnClickListener).show();
                                return;
                            }else if (SystemUtil.isInstalled(OrderTrackCheckActivity.this,"com.autonavi.minimap")){
                                choiceNaviSnackbar=Snackbar.make(mMapView,"是否启动地图导航？",Snackbar.LENGTH_INDEFINITE);
                                View v=choiceNaviSnackbar.getView();
                                v.setBackgroundColor(getResources().getColor(R.color.details_text));
                                final TextView tv_snackbar= (TextView) v.findViewById(R.id.snackbar_text);
                                tv_snackbar.setGravity(Gravity.CENTER);
                                tv_snackbar.setTextColor(getResources().getColor(R.color.white));
                                choiceNaviSnackbar.setAction("导航",autoNaviOnClickListener).show();
                                return;
                            }else {
                                showToastMsg("未检索到本机已安装‘百度地图’或‘高德地图’App");
                                return;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
                //获取单车路径规划结果
            }
        };
        mSearch.setOnGetRoutePlanResultListener(listener);
        mSearch.drivingSearch(drivingRoutePlanOption);
    }

    @Override
    public void onBackPressed() {
        if (choiceNaviSnackbar!=null&&choiceNaviSnackbar.isShown()){
            choiceNaviSnackbar.dismiss();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                if (mBaiduMap!=null){
                    mBaiduMap.clear();
                }
                if (choiceNaviSnackbar!=null&&choiceNaviSnackbar.isShown()){
                    choiceNaviSnackbar.dismiss();
                }
                if (isEdittext){
                    msearch.geocode(new GeoCodeOption().city("深圳市").address(ed_serch_track.getText().toString()));
                }else {
                    getData();
                }
                break;
        }
    }
    /**
     * 判断是否会定位失败 ，errorCode是百度定位返回的错误代码
     *
     * @param errorCode
     * @return
     */
    public boolean isLocateAvailable(int errorCode) {
        //2016.08.30 陈翔 注销了65 ： 定位缓存的结果；68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
        //  return (errorCode == 161 ||errorCode == 61||errorCode==66||errorCode==65||errorCode==68);
        return (errorCode == 161 ||errorCode == 61||errorCode==66);
    }

    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }




    /* @Override
     public void onDestroy() {
         super.onDestroy();
         //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
         if (mMapView != null)mMapView.onDestroy();
     }*/
    /*@Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if (mMapView != null)
        mMapView.onResume();
    }*/
   /* @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if (mMapView != null)mMapView.onPause();
    }*/
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            MLog.i("OrderTrackCheckActivity MyLocationListener:\t"+location.getLocType());
            if (location == null) {
                //定位返回空值时，重新定位
                if (againBoolean){
                    try {
                        Thread.sleep(30*1000);
                        againBoolean=false;
                        int r=mLocationClient.requestLocation();
                        MLog.w("定位返回空值时，重新定位:" + r);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                return;
            }
            // 判断定位是否失败应该依据error code的值更加可靠, 上传坐标信息
            if (!isLocateAvailable(location.getLocType())) {
                //定位返回错误码时，重新定位
                MLog.w("定位返回错误码"+againBoolean);
                if (againBoolean){
                    try {
                        Thread.sleep(30*1000);
                        againBoolean=false;
                        int j=mLocationClient.requestLocation();
                        MLog.w("定位返回错误码时，重新定位:" + j);
                        return;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                showToast(location.getLocType()+"网络定位状态不良，请重试",Toast.LENGTH_SHORT);
                return;
            }
            currentlocation=location;
            if (endlocation!=null&&isSearchTrack) {
                searchTrack(currentlocation, endlocation);
            }else {
                LatLng stLatLng=new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
                BitmapDescriptor stbitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
                stOption = new MarkerOptions().position(stLatLng).icon(stbitmap).zIndex(5);
                mBaiduMap.addOverlay(stOption);
                //  mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(22.628646D,114.046639D)));
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude())));
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
            }
        }
    }
}
