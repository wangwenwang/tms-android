package com.kaidongyuan.app.kdydriver.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.adapter.OrderListChoiceAdapter;
import com.kaidongyuan.app.kdydriver.bean.order.Order;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.ui.base.BaseActivity;
import com.kaidongyuan.app.kdydriver.ui.widget.DividerListItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/8/8.
 */
public class OrderListChoiceActivity extends BaseActivity implements AsyncHttpCallback,View.OnClickListener {
    private OrderAsyncHttpClient mClient;
    private final String TAG_GetShipmentUnPayOrderList = "TAG_GetShipmentUnPayOrderList";
    private SlidingTitleView titleView;
    private RecyclerView mrecyclerview;
    private LinearLayout ll_no_record;
    private OrderListChoiceAdapter madapter;
    private Button bt_allchoice,bt_choicecomfirm;
    private boolean allchoice=true;
    private List<Order> orderlist;
    private String currentLocationAds;
    private double currentLocationLng,currentLocationLat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlistchoice);
        mClient=new OrderAsyncHttpClient(this,this);
        initView();
        if (getIntent().hasExtra("tms_shipment_no")&&getIntent().hasExtra("order_currentLocation_address")){
           initdata(getIntent().getStringExtra("tms_shipment_no").trim());
            currentLocationAds = getIntent().getStringExtra("order_currentLocation_address").trim();
            currentLocationLng=getIntent().getDoubleExtra("order_currentLocation_lng",0);
            currentLocationLat=getIntent().getDoubleExtra("order_currentLocation_lat",0);
        }else {
            showToastMsg("装运单号数据丢失，请重新进入");
            finish();
        }
    }

    private void initdata(String tms_shipment_no) {
        Map<String,String> params=new HashMap<>();
        params.put("TMS_SHIPMENT_NO",tms_shipment_no);
        params.put("strIsPay","N");
        params.put("strLicense","");
        mClient.sendRequest(Constants.URL.GetShipmentUnPayOrderList,params,TAG_GetShipmentUnPayOrderList);
    }

    private void initView() {
        titleView= (SlidingTitleView) this.findViewById(R.id.orderlist_choose_titelview);
        titleView.setText("选择订单");
        titleView.setMode(SlidingTitleView.MODE_BACK);
        mrecyclerview= (RecyclerView) findViewById(R.id.recyclerview_orderlist_choice);
        ll_no_record= (LinearLayout) this.findViewById(R.id.ll_no_record);
        orderlist=new ArrayList<>();
        madapter=new OrderListChoiceAdapter(orderlist);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mrecyclerview.setLayoutManager(layoutManager);
        mrecyclerview.addItemDecoration(new DividerListItemDecoration(getMContext(), DividerListItemDecoration.VERTICAL_LIST));
        mrecyclerview.setAdapter(madapter);
        bt_allchoice= (Button) this.findViewById(R.id.btn_allchoice);
        bt_allchoice.setOnClickListener(this);
        bt_choicecomfirm= (Button) this.findViewById(R.id.btn_comfirmchoice);
        bt_choicecomfirm.setOnClickListener(this);
        ll_no_record.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_allchoice:
                madapter.initMap(allchoice);
                allchoice=!allchoice;
                break;
            case R.id.btn_comfirmchoice:
                if (madapter!=null&&madapter.getSelectedlist().size()>0){
                    String orderids=madapter.getSelectedlist().get(0).getORD_IDX();
                    boolean onlyphotograph=isPhotographType(madapter.getSelectedlist().get(0).getORD_NO().trim());
                    if (madapter.getSelectedlist().size()>1){
                        for (int i=1;i<madapter.getSelectedlist().size();i++){
                            orderids=orderids+","+madapter.getSelectedlist().get(i).getORD_IDX();
                            onlyphotograph=isPhotographType(madapter.getSelectedlist().get(i).getORD_NO().trim());
                        }
                    }
                    Intent intent=new Intent(OrderListChoiceActivity.this,OrderPayActivity.class);
                    intent.putExtra("order_currentLocation_address",currentLocationAds);
                    intent.putExtra("order_currentLocation_lng",currentLocationLng);
                    intent.putExtra("order_currentLocation_lat",currentLocationLat);
                    intent.putExtra("onlyphotograph",onlyphotograph);
                    intent.putExtra("orderids",orderids);
                    startActivity(intent);
                }else {
                     showToastMsg("请正确选择需要批量提交的订单");
                }
                break;
            case R.id.ll_no_record:
                if (getIntent().hasExtra("tms_shipment_no")){
                    initdata(getIntent().getStringExtra("tms_shipment_no").trim());
                }else {
                    showToastMsg("加载失败，请返回上一界面！");
                }
                   break;
            default:
                break;
        }
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
    public void postSuccessMsg(String msg, String request_tag) {
        if (msg.equals("error")){
            switch (request_tag){
                case TAG_GetShipmentUnPayOrderList:
                    showToastMsg("关联订单查找失败，请稍后重试");
                    if (orderlist!=null&&orderlist.size()<=0){
                        mrecyclerview.setVisibility(View.GONE);
                        ll_no_record.setVisibility(View.VISIBLE);
                    }else {
                        mrecyclerview.setVisibility(View.VISIBLE);
                        ll_no_record.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
            return;
        }else if (request_tag.equals(TAG_GetShipmentUnPayOrderList)){
            try {
                JSONObject jo= JSON.parseObject(msg);
                orderlist=JSON.parseArray(jo.getString("result"),Order.class);
                madapter.setData(orderlist);
            }catch (Exception ex){
                showToastMsg("订单列表数据解析失败");
                ex.printStackTrace();
            }
            mrecyclerview.setVisibility(View.VISIBLE);
            ll_no_record.setVisibility(View.GONE);
        }
    }

}
