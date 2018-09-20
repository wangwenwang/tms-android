package com.kaidongyuan.app.kdydriver.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.utils.nomalutils.DensityUtil;
import com.kaidongyuan.app.basemodule.utils.nomalutils.StringUtils;
import com.kaidongyuan.app.basemodule.utils.nomalutils.SystemUtil;
import com.kaidongyuan.app.basemodule.widget.MLog;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.adapter.OrderDetailsSimpleAdapter;
import com.kaidongyuan.app.kdydriver.bean.order.CustomerAutographAndPicture;
import com.kaidongyuan.app.kdydriver.bean.order.Order;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.serviceAndReceiver.TrackingService;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;
import com.kaidongyuan.app.kdydriver.utils.ListViewUtils;
import com.kaidongyuan.app.kdydriver.utils.baidumapUtils.LocationPointUtil;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2016/10/13.
 */
public class OrderDetailActivity extends BaseActivity implements AsyncHttpCallback{

    private OrderAsyncHttpClient mClient;
    private final String Get_Tms_Info = "Get_Tms_Info";
    private final String Tag_GetAutograph="GetAutograph";
    private Order mOrder;
    private boolean isrefresh=false;
    private SlidingTitleView slidingTitleView;
    private ListView lv_details;
    private OrderDetailsSimpleAdapter madapter;

    private TextView tv_tms_order_no,tv_client_order_no, tv_tms_shipment_no,tv_tms_shipment_cost,tv_cost_error_desc, tv_tms_date_load, tv_tms_date_issue, tv_tms_fleet_name, tv_tms_plate_number,
            tv_tms_driver_name, tv_tms_driver_tel, tv_ord_issue_qty, tv_ord_issue_weight, tv_ord_issue_volume,
            tv_ord_state, tv_ord_workflow, tv_driver_pay, tv_order_to_name, tv_order_from_name, tv_order_to_addres;
    private FloatingActionButton fab_payorder,fab_ordertrack,fab_ordercost;
    private LinearLayout ll_order_pictures,ll_cost_error_desc;//查看已交付订单图片的LinearLayout和查看装运计费错误提示的LinearLayout
    private ImageView iv_orderpicture,iv_orderpicture2,iv_orderpicture3;
    /**
     * 订单电子回单图片集合
     */
    private List<CustomerAutographAndPicture> customerAutographAndPictures;
    private Intent intent;
    private GeoCoder msearch;
    private OnGetGeoCoderResultListener geoCoderlistener;
    private GeoCodeResult order_ToAdsGeoCodeResult;
    private BDLocation currentlocation;
    public LocationClient mLocationClient;// 百度定位客户端
    public MyLocationListener myListener;// 百度定位监听
    private String tempcoor = "bd09ll";// 百度地图的编码模式
    //高精度模式
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private boolean againBoolean=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
        mClient=new OrderAsyncHttpClient(this,this);
        intent = getIntent();
        mClient.setShowToast(false);
        initView();
        getData();
    }

    private void initView() {
        slidingTitleView = (SlidingTitleView) findViewById(R.id.unpayed_orderdetail_titel_view);
        slidingTitleView.setText("物流详情");
        slidingTitleView.setMode(SlidingTitleView.MODE_BACK);
        tv_tms_order_no = (TextView) findViewById(R.id.tv_tms_order_no);
        tv_client_order_no= (TextView) findViewById(R.id.tv_client_order_no);
        tv_tms_shipment_no = (TextView) findViewById(R.id.tv_tms_shipment_no);
        tv_tms_shipment_cost= (TextView) findViewById(R.id.tv_tms_shipment_cost);
        tv_cost_error_desc= (TextView) findViewById(R.id.tv_cost_error_desc);
        ll_cost_error_desc= (LinearLayout) findViewById(R.id.ll_cost_error_desc);
        tv_tms_date_load = (TextView) findViewById(R.id.tv_tms_date_load);
        tv_tms_date_issue = (TextView) findViewById(R.id.tv_tms_date_issue);
        tv_tms_fleet_name = (TextView) findViewById(R.id.tv_tms_fleet_name);
        tv_tms_plate_number = (TextView) findViewById(R.id.tv_tms_plate_number);
        tv_tms_driver_name = (TextView) findViewById(R.id.tv_tms_driver_name);
        tv_tms_driver_tel = (TextView) findViewById(R.id.tv_tms_driver_tel);
        tv_ord_issue_qty = (TextView) findViewById(R.id.tv_ord_issue_qty);
        tv_ord_issue_weight = (TextView) findViewById(R.id.tv_ord_issue_weight);
        tv_ord_issue_volume = (TextView) findViewById(R.id.tv_ord_issue_volume);
        tv_ord_state = (TextView) findViewById(R.id.tv_ord_state);
        tv_ord_workflow = (TextView) findViewById(R.id.tv_ord_workflow);
        tv_driver_pay = (TextView) findViewById(R.id.tv_driver_pay);
        tv_order_to_name = (TextView) findViewById(R.id.tv_order_to_name);
        tv_order_from_name = (TextView) findViewById(R.id.tv_order_from_name);
        tv_order_to_addres = (TextView) findViewById(R.id.tv_order_to_address);
        lv_details= (ListView) findViewById(R.id.lv_details);
        madapter=new OrderDetailsSimpleAdapter(OrderDetailActivity.this);
        lv_details.setAdapter(madapter);
        ListViewUtils.setListViewHeightBasedOnChildren(lv_details);
        fab_payorder= (FloatingActionButton) findViewById(R.id.fab_payorder);
        fab_ordertrack= (FloatingActionButton) findViewById(R.id.fab_orderTrack);
        fab_ordercost= (FloatingActionButton) findViewById(R.id.fab_ordercost);
        ll_order_pictures= (LinearLayout) findViewById(R.id.ll_order_pictures);
        iv_orderpicture= (ImageView) findViewById(R.id.iv_order_picture);
        iv_orderpicture2= (ImageView) findViewById(R.id.iv_order_picture2);
        iv_orderpicture3= (ImageView) findViewById(R.id.iv_order_picture3);
        FloatingActionsMenu floatingActionsMenu= (FloatingActionsMenu) findViewById(R.id.fab_memu);
        //floatingActionsMenu.expand();//初始时展开FloatingBar
//        mLocationClient=new LocationClient(this);
//
//        myListener=new MyLocationListener();
//        mLocationClient.registerLocationListener(myListener);
//        initLocationClient();
    }


    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
        final String strOrderId;
        if (intent.hasExtra("order_id")) {
            strOrderId = intent.getStringExtra("order_id");
          //  strOrderId="24415";
            params.put("strTmsOrderId", strOrderId);
        } else {
            showToastMsg("货单ID不能为空");
            finish();
            return;
        }
        params.put("strLicense", "");
        mClient.sendRequest(Constants.URL.GetOrderTmsInfo, params, Get_Tms_Info);
    }

    private void setData(final Order order) {
        tv_tms_order_no.setText(order.getORD_NO());
        tv_client_order_no.setText(order.getORD_NO_CLIENT());
        tv_tms_shipment_no.setText(order.getTMS_SHIPMENT_NO());
        tv_tms_date_load.setText(order.getTMS_DATE_LOAD());
        tv_tms_date_issue.setText(order.getTMS_DATE_ISSUE());
        tv_tms_fleet_name.setText(order.getTMS_FLEET_NAME());
        tv_tms_plate_number.setText(order.getTMS_PLATE_NUMBER());
        tv_tms_driver_name.setText(order.getTMS_DRIVER_NAME());
        tv_tms_driver_tel.setText(order.getTMS_DRIVER_TEL());
        if (StringUtils.isDouble(order.getORD_ISSUE_QTY())){
            DecimalFormat decimalFormat=new DecimalFormat("0.00");//将double类型保留两位小数，不四舍五入
            tv_ord_issue_qty.setText(decimalFormat.format(Double.parseDouble(order.getORD_ISSUE_QTY()))+ "件");
        }else {
            tv_ord_issue_qty.setText(order.getORD_ISSUE_QTY()+"件");
        }
        if (StringUtils.isDouble(order.getORD_ISSUE_WEIGHT())){
            DecimalFormat decimalFormat=new DecimalFormat("0.00");//将double类型保留两位小数，不四舍五入
            tv_ord_issue_weight.setText(decimalFormat.format(Double.parseDouble(order.getORD_ISSUE_WEIGHT()))+ "吨");
        }else {
            tv_ord_issue_qty.setText(order.getORD_ISSUE_WEIGHT()+"吨");
        }
        if (StringUtils.isDouble(order.getORD_ISSUE_VOLUME())){
            DecimalFormat decimalFormat=new DecimalFormat("0.00");//将double类型保留两位小数，不四舍五入
            tv_ord_issue_volume.setText(decimalFormat.format(Double.parseDouble(order.getORD_ISSUE_VOLUME()))+ "m³");
        }else {
            tv_ord_issue_volume.setText(order.getORD_ISSUE_VOLUME()+"m³");
        }

        tv_ord_state.setText(StringUtils.getOrderStatus(order.getORD_STATE()));//yb 发运方式
        tv_ord_workflow.setText(StringUtils.getOrderState(order.getORD_WORKFLOW()));
        tv_order_to_name.setText(order.getORD_TO_NAME());
        tv_order_from_name.setText(order.getORD_FROM_NAME());
        //剔除约定的以*代替无法查询行政级别名称的地址字段
        tv_order_to_addres.setText(StringUtils.subAllTargetCharSequence(mOrder.getORD_TO_ADDRESS(),"*"));
        madapter.resetData(order.getOrderDetails());
        ListViewUtils.setListViewHeightBasedOnChildren(lv_details);
        if (mOrder.getDRIVER_PAY().equals("未交付")) {
            tv_driver_pay.setText("未交付");
            tv_tms_shipment_cost.setText("未结费");
            ll_cost_error_desc.setVisibility(View.GONE);
            fab_payorder.setColorNormalResId(R.color.really_yellow);
            fab_payorder.setIcon(R.drawable.fab_payorder);
            fab_payorder.setVisibility(View.VISIBLE);
            fab_ordertrack.setIcon(R.drawable.fab_ordernavi);
            fab_ordercost.setIcon(R.drawable.fab_choiceorders);
            fab_ordercost.setVisibility(View.VISIBLE);
            startLocate();
            msearch = GeoCoder.newInstance();
            geoCoderlistener = new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                    if (currentlocation==null){
                        startLocate();
                        showToastMsg("网络定位失败，请稍后重试~");
                        return;
                    }
                    if (geoCodeResult==null||geoCodeResult.error!= SearchResult.ERRORNO.NO_ERROR){
                        //没有检索到结果
                        showToastMsg("检索目的地坐标失败，请手动点选目的地");
                        Intent intent = new Intent(OrderDetailActivity.this, OrderTrackCheckActivity.class);
                        intent.putExtra("ORD_TO_ADDRESS", StringUtils.subAllTargetCharSequence(mOrder.getORD_TO_ADDRESS(),"*"));
                        intent.putExtra("currentLocation_longitude", currentlocation.getLongitude());
                        intent.putExtra("currentLocation_latitude", currentlocation.getLatitude());
                        startActivity(intent);
                    }else {
                        order_ToAdsGeoCodeResult=geoCodeResult;
                        MLog.e("目的地：latitude"+geoCodeResult.getLocation().latitude+"\tlongitude"+geoCodeResult.getLocation().longitude);
                        if (SystemUtil.isInstalled(OrderDetailActivity.this,"com.autonavi.minimap")){
                            //跳转到高德导航
                            if (currentlocation!=null&&order_ToAdsGeoCodeResult!=null){
                                HashMap<String,Double>stlatlonmap=LocationPointUtil.bd_decrypt(currentlocation.getLatitude(),currentlocation.getLongitude());
                                HashMap<String,Double>edlatlonmap=LocationPointUtil.bd_decrypt(order_ToAdsGeoCodeResult.getLocation().latitude,order_ToAdsGeoCodeResult.getLocation().longitude);
                                Intent autoIntent=new Intent();
                                autoIntent.setData(Uri
                                        .parse("androidamap://route?" +
                                                "sourceApplication="+getResources().getString(R.string.app_name)+
                                                "&slat=" + stlatlonmap.get("gcjlat") +
                                                "&slon=" +stlatlonmap.get("gcjlon") +
                                                "&dlat=" + edlatlonmap.get("gcjlat") +
                                                "&dlon=" + edlatlonmap.get("gcjlon") +
                                                "&dname=" + order_ToAdsGeoCodeResult.getAddress()+
                                                "&dev=0" +
                                                "&m=2" +
                                                "&t=0"
                                        ));
                                startActivity(autoIntent);
                            }else {
                                showToastMsg("起始点、目的地坐标不完整，请补全后重新导航");
                            }
                            return;
                        }else  if (SystemUtil.isInstalled(OrderDetailActivity.this,"com.baidu.BaiduMap")){
                            if (currentlocation!=null&&order_ToAdsGeoCodeResult!=null){
                                //跳转到百度导航
                                try {
                                    Intent  baiduintent = Intent.parseUri("intent://map/direction?" +
                                            "origin=latlng:" + currentlocation.getLatitude() + "," + currentlocation.getLongitude() +
                                            "|name:" +currentlocation.getAddrStr() +
                                            "&destination=latlng:" + order_ToAdsGeoCodeResult.getLocation().latitude + "," + order_ToAdsGeoCodeResult.getLocation().longitude+
                                            "|name:" +order_ToAdsGeoCodeResult.getAddress() +
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
                            return;
                        } else {
                            showToastMsg("未检索到本机已安装‘百度地图’或‘高德地图’App");
                            return;
                        }
                    }
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                }
            };
            msearch.setOnGetGeoCodeResultListener(geoCoderlistener);
            //   fab_payorder.setImageResource(R.drawable.fab_payorder);
            fab_payorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (mOrder != null && mOrder.getIDX() != null) {
                            Intent intent = new Intent(OrderDetailActivity.this, OrderPayActivity.class);
                            intent.putExtra("order_id", mOrder.getIDX());
                            boolean onlyphotograph=isPhotographType(mOrder.getORD_NO().trim());
                            intent.putExtra("onlyphotograph",onlyphotograph);
                            intent.putExtra("order_driver_pay",mOrder.getDRIVER_PAY());
                            intent.putExtra("order_currentLocation_address",currentlocation.getAddrStr());
                            intent.putExtra("order_currentLocation_lng",currentlocation.getLongitude());
                            intent.putExtra("order_currentLocation_lat",currentlocation.getLatitude());
                            isrefresh=true;
                            startActivity(intent);
                        } else {
                            showToastMsg("订单信息重载");
                            getData();
                        }
                    }catch (Exception e){
                        showToastMsg("订单信息重载:"+e.getMessage());
                        getData();
                    }

                }
            });
            fab_ordercost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOrder!=null&&mOrder.getTMS_SHIPMENT_NO()!=null){
                        Intent intent=new Intent(OrderDetailActivity.this,OrderListChoiceActivity.class);
                        intent.putExtra("tms_shipment_no",mOrder.getTMS_SHIPMENT_NO().trim());
                        intent.putExtra("order_currentLocation_address",currentlocation.getAddrStr());
                        intent.putExtra("order_currentLocation_lng",currentlocation.getLongitude());
                        intent.putExtra("order_currentLocation_lat",currentlocation.getLatitude());
                        isrefresh=true;
                        startActivity(intent);
                    }else {
                        showToastMsg("订单信息重载");
                        getData();
                    }
                }
            });
            fab_ordertrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        msearch.geocode(new GeoCodeOption().city("深圳市").address(StringUtils.subAllTargetCharSequence(mOrder.getORD_TO_ADDRESS(),"*")));
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
        }
        else if (mOrder.getDRIVER_PAY().equals("已交付")){
            if (mOrder.getShipment().getAUDIT_FLAG()!=null&&mOrder.getShipment().getAUDIT_FLAG().trim().equals("Y")){
                    tv_tms_shipment_cost.setText("已结费");
                    ll_cost_error_desc.setVisibility(View.GONE);
            }else if (mOrder.getShipment().getERROR_FLAG()!=null&&mOrder.getShipment().getERROR_FLAG().equals("Y")){
                tv_tms_shipment_cost.setText("计费错误");
                ll_cost_error_desc.setVisibility(View.VISIBLE);
                tv_cost_error_desc.setText(mOrder.getShipment().getERROR_DESC()+"");
            }else if (mOrder.getShipment().getAUDIT_FLAG()!=null&&mOrder.getShipment().getAUDIT_FLAG().trim().equals("N")){
                tv_tms_shipment_cost.setText("未计费");
            }else {
                tv_tms_shipment_cost.setText("计费异常，"+mOrder.getTMS_SHIPMENT_NO());
            }
            fab_payorder.setVisibility(View.VISIBLE);
            fab_payorder.setIcon(R.drawable.fab_checkpicture);
            fab_ordercost.setVisibility(View.VISIBLE);
            fab_payorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOrder!=null&&mOrder.getIDX()!=null){
                        Map<String, String> params = new HashMap<>();
                        params.put("strOrderIdx", mOrder.getIDX());
                        params.put("strLicense", "");
                        mClient.sendRequest(Constants.URL.GETAUTOGRAPH,params,Tag_GetAutograph);
                    }else {
                        showToastMsg("订单信息重载");
                        getData();
                    }
                }
            });
            fab_ordercost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOrder.getShipment().getAUDIT_FLAG()!=null&&mOrder.getShipment().getAUDIT_FLAG().trim().equals("Y")){
                        Intent intent=new Intent(OrderDetailActivity.this,ShipmentCostActivity.class);
                        intent.putExtra("shipment_no",mOrder.getTMS_SHIPMENT_NO());
                        intent.putExtra("ORD_NO",mOrder.getORD_NO());
                        startActivity(intent);
                    }else if (mOrder.getShipment().getERROR_FLAG()!=null&&mOrder.getShipment().getERROR_FLAG().equals("Y")){
                        showToast(mOrder.getShipment().getERROR_DESC(),Toast.LENGTH_LONG);
                    }
                }
            });
            fab_ordertrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2=new Intent(OrderDetailActivity.this,OrderTrackActivity.class);
                    intent2.putExtra("order_IDX",mOrder.getIDX());
                    startActivity(intent2);
                }
            });
            tv_driver_pay.setText("已交付");
        }else {
            tv_driver_pay.setText((order.getDRIVER_PAY()));
        }
        MLog.i(order.toString());
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
        option.setIgnoreKillProcess(false);
        option.setTimeOut(10 * 1000); // 网络定位的超时时间
        mLocationClient.setLocOption(option);
    }
    /**
     * 根据订单号首三位判断是否可以交付是拍照+相册
     * @param ord_no
     * @return true 只能拍照，false 可以拍照或相册选取
     */
    private boolean isPhotographType(String ord_no) {
        if (ord_no==null||ord_no.isEmpty()){
            return false;
        }else if (ord_no.startsWith("YIB")||ord_no.startsWith("XHA")){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isrefresh){
            getData();
            isrefresh=false;
        }
    }
    public void startLocate() {

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
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        mClient.cancleRequest(Tag_GetAutograph,Get_Tms_Info);
        if (msearch!=null) {
            msearch.destroy();
        }
        if (mLocationClient!=null){
            mLocationClient.stop();
            mLocationClient=null;
        }
        if (myListener!=null){
            myListener=null;
        }
        super.onDestroy();
    }

    @Override
    public void postSuccessMsg(String msg, String request_tag) {
        if (msg.equals("error")){
            if (request_tag.equals(Tag_GetAutograph)){
                ll_order_pictures.setVisibility(View.VISIBLE);
                showToastMsg("没有查询到图片");
            }
            return;
        }
        if (request_tag.equals(Get_Tms_Info)) {
            try {
                JSONObject jos = JSON.parseObject(msg);
                JSONArray ja = JSON.parseArray(jos.getString("result"));
                JSONObject jo = JSON.parseObject(ja.get(0).toString());
                mOrder = JSON.parseObject(jo.getString("order"), Order.class);
//                org.json.JSONObject jsonObject0=new org.json.JSONObject(jo.getString("order"));
//                ArrayList<OrderDetails>orderDetailses=new ArrayList<>();
//                org.json.JSONArray jsonArray=jsonObject0.getJSONArray("OrderDetails");
//                for (int i=0;i<jsonArray.length();i++){
//                    org.json.JSONObject jsonObject=jsonArray.getJSONObject(i);
//                    OrderDetails orderDetails=new OrderDetails();
//                    orderDetails.setPRODUCT_NAME(jsonObject.getString("PRODUCT_NAME"));
//                    orderDetails.setPRODUCT_NO(jsonObject.getString("PRODUCT_NO"));
//                    orderDetails.setORDER_UOM(jsonObject.getString("ORDER_UOM"));
//                    orderDetails.setISSUE_QTY(jsonObject.getString("ISSUE_QTY"));
//                    orderDetails.setISSUE_WEIGHT(jsonObject.getString("ISSUE_WEIGHT"));
//                    orderDetails.setISSUE_VOLUME(jsonObject.getString("ISSUE_VOLUME"));
//                    orderDetails.setPRODUCT_TYPE(jsonObject.getString("PRODUCT_TYPE"));
//                    orderDetails.setPRODUCT_URL(jsonObject.getString("PRODUCT_URL"));
//                    orderDetailses.add(orderDetails);
//                }
//                mOrder.setOrderDetails(orderDetailses);
                if (mOrder != null) {
                    setData(mOrder);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if (request_tag.equals(Tag_GetAutograph)){
            JSONObject jo=JSON.parseObject(msg);
            int type=jo.getInteger("type");
            if (type==1){
                customerAutographAndPictures=JSON.parseArray(jo.getString("result"),CustomerAutographAndPicture.class);
                setll_order_pictures();
                return;
            }
        }
    }

    private void setll_order_pictures() {
        Glide.with(getMContext()).load(getPictureUrl(0)).error(R.drawable.ic_no_record1).override(DensityUtil.dip2px(400),DensityUtil.dip2px(400))
                .diskCacheStrategy(DiskCacheStrategy.NONE).crossFade().fitCenter().into(iv_orderpicture);
        Glide.with(getMContext()).load(getPictureUrl(1)).error(R.drawable.ic_no_record1).override(DensityUtil.dip2px(400),DensityUtil.dip2px(400))
                .diskCacheStrategy(DiskCacheStrategy.NONE).crossFade().fitCenter().into(iv_orderpicture2);
        Glide.with(getMContext()).load(getPictureUrl(2)).error(R.drawable.ic_no_record1).override(DensityUtil.dip2px(400),DensityUtil.dip2px(400))
                .diskCacheStrategy(DiskCacheStrategy.NONE).crossFade().fitCenter().into(iv_orderpicture3);
        ll_order_pictures.setVisibility(View.VISIBLE);
        iv_orderpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetailActivity.this,PhotoActivity.class);
                intent.putExtra("strUrl",getPictureUrl(0));
                startActivity(intent);
            }
        });
        iv_orderpicture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetailActivity.this,PhotoActivity.class);
                intent.putExtra("strUrl",getPictureUrl(1));
                startActivity(intent);
            }
        });
        iv_orderpicture3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderDetailActivity.this,PhotoActivity.class);
                intent.putExtra("strUrl",getPictureUrl(2));
                startActivity(intent);
            }
        });


    }
    /**
     * 获取图片的 url
     * @param remarkInt 标记
     *                  0 客户签名
     *                  1 现场图片1
     *                  2 现场图片2
     * @return 图片的url 路径
     */
    private String getPictureUrl(int remarkInt) {
        try {
            if (customerAutographAndPictures == null) {
                return "";
            }
            int i = 1;
            int j=1;
            for (CustomerAutographAndPicture customerAutographAndPicture : customerAutographAndPictures) {
                try {
                    if (customerAutographAndPicture != null) {
                        String remark = customerAutographAndPicture.getREMARK();
                        if ("Autograph".equals(remark) && remarkInt == 0) {//为客户签名图片
                            return Constants.URL.Load_Url + "Uploadfile/" + customerAutographAndPicture.getPRODUCT_URL();
                        } else if ("pricture".equals(remark)) {
                            String pictureUrl = Constants.URL.Load_Url + "Uploadfile/" + customerAutographAndPicture.getPRODUCT_URL();
                            if (i == 1 && remarkInt == 1) {
                                return pictureUrl;
                            }
                            if (i == 2 && remarkInt == 2) {
                                return pictureUrl;
                            }
                            i++;
                        }else if ("prictureS".equals(remark)){
                            String picturesUrl=Constants.URL.Load_Url+"Uploadfile/"+customerAutographAndPicture.getPRODUCT_URL();
                            if (j==1&&remarkInt==3){
                                return picturesUrl;
                            }
                            if (j==2&&remarkInt==4){
                                return picturesUrl;
                            }
                            j++;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 判断是否会定位失败 ，errorCode是百度定位返回的错误代码
     *
     * @param errorCode
     * @return
     */
    public boolean isLocateAvailable(int errorCode) {
        return (errorCode == 161 ||errorCode == 61||errorCode==66||errorCode==65||errorCode==68);
        //2016.08.30 陈翔 注销了65 ： 定位缓存的结果；68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
        //return (errorCode == 161 ||errorCode == 61||errorCode==66);
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            MLog.i("MyLocationListener:\t"+location.getLocType());
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
                showToast(location.getLocType()+"网络状态不良，请重试", Toast.LENGTH_SHORT);
                return;
            }
            currentlocation=location;
            mLocationClient.stop();
            return;
        }
    }
}
