package com.kaidongyuan.app.kdydriver.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.adapter.OrderCostListAdapter;
import com.kaidongyuan.app.kdydriver.adapter.OtherCostListAdapter;
import com.kaidongyuan.app.kdydriver.bean.order.ShipmentCost;
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
 * Created by Administrator on 2017/3/23.
 */
public class ShipmentCostActivity extends BaseActivity implements AsyncHttpCallback,View.OnClickListener {
    private OrderAsyncHttpClient mClient;
    private String SHIPMENTNO,ORD_NO;
    private final String TAG_GetPrice="GetPrice";
    private ShipmentCost shipmentCost;
    private SlidingTitleView titleView;
    private TextView tv_ORD_NO,tv_ORD_NO_CLIENT,tv_ORD_FROM_NAMES,tv_ORD_TO_NAME,tv_order_AMOUNT_PRICE,tv_order_CHARGE_AMOUNT,
            tv_ORD_QTY,tv_order_TRANSPORT_FEES,tv_ORD_WEIGHT,tv_order_DROPPOINT_FEES,tv_ORD_VOLUME,tv_order_RETURN_FEES,tv_order_DELIVER_FEES,
            tv_order_PRESS_NIGHT,tv_order_FUEL_SURCHARGE,tv_order_LOAD_FEES,tv_order_SITE_SURCHARGE,tv_order_OTHER_FEES,tv_order_FEESCOUNT,
            tv_Check_Shipmentcost,tv_SHIPMENT_NO,tv_DATE_ADD,tv_DATE_ISSUE,tv_TMS_FLEET_NAME,tv_DRIVER_NAME,tv_TOTAL_QTY,tv_DRIVER_TEL,
            tv_TOTAL_WEIGHT,tv_PLATE_NUMBER,tv_TOTAL_VOLUME,tv_AMOUNT_PRICE,tv_CHARGE_AMOUNT,tv_TRANSPORT_FEES,tv_DROPPOINT_FEES,
            tv_FUEL_SURCHARGE,tv_RETURN_FEES,tv_SITE_SURCHARGE,tv_PRESS_NIGHT,tv_DELIVER_FEES,tv_LOAD_FEES,tv_OTHER_FEES,tv_FEESCOUNT;
    private LinearLayout ll_shipmentcost_detail;
    private RecyclerView recyclerView_orderCostList,recyclerView_otherCostList;
    private OrderCostListAdapter morderCostListAdapter;
    private OtherCostListAdapter motherCostListAdapter;
    private List<ShipmentCost.OrderCost>orderCosts;
    private List<ShipmentCost.OtherCost>otherCosts;
    private boolean hasData;//是否有数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipmentcost);
        if (getIntent().hasExtra("shipment_no")&&getIntent().hasExtra("ORD_NO")) {
            SHIPMENTNO = getIntent().getStringExtra("shipment_no");
            ORD_NO=getIntent().getStringExtra("ORD_NO");
            mClient=new OrderAsyncHttpClient(this,this);
            initView();
            initdata();
        }else {
            finish();
        }
    }

    private void initdata() {
        Map<String,String> params=new HashMap<>();
        params.put("SHIPMENTNO",SHIPMENTNO);
        params.put("strLicense","");
        mClient.sendRequest(Constants.URL.GetPrice,params,TAG_GetPrice);
    }

    private void initView() {
        titleView= (SlidingTitleView) findViewById(R.id.slidingTitleView_shipmentcost);
        titleView.setText("计费详情");
        titleView.setMode(SlidingTitleView.MODE_BACK);
        tv_ORD_NO= (TextView) findViewById(R.id.tv_ORD_NO);
        tv_ORD_NO_CLIENT= (TextView) findViewById(R.id.tv_ORD_NO_CLIENT);
        tv_ORD_FROM_NAMES= (TextView) findViewById(R.id.tv_ORD_FROM_NAMES);
        tv_ORD_TO_NAME= (TextView) findViewById(R.id.tv_ORD_TO_NAME);
        tv_order_AMOUNT_PRICE= (TextView) findViewById(R.id.tv_order_AMOUNT_PRICE);
        tv_order_CHARGE_AMOUNT= (TextView) findViewById(R.id.tv_order_CHARGE_AMOUNT);
        tv_ORD_QTY= (TextView) findViewById(R.id.tv_ORD_QTY);
        tv_order_TRANSPORT_FEES= (TextView) findViewById(R.id.tv_order_TRANSPORT_FEES);
        tv_ORD_WEIGHT= (TextView) findViewById(R.id.tv_ORD_WEIGHT);
        tv_order_DROPPOINT_FEES= (TextView) findViewById(R.id.tv_DROPPOINT_FEES);
        tv_ORD_VOLUME= (TextView) findViewById(R.id.tv_ORD_VOLUME);
        tv_order_RETURN_FEES= (TextView) findViewById(R.id.tv_RETURN_FEES);
        tv_order_DELIVER_FEES= (TextView) findViewById(R.id.tv_order_DELIVER_FEES);
        tv_order_PRESS_NIGHT= (TextView) findViewById(R.id.tv_order_PRESS_NIGHT);
        tv_order_FUEL_SURCHARGE= (TextView) findViewById(R.id.tv_order_FUEL_SURCHARGE);
        tv_order_LOAD_FEES= (TextView) findViewById(R.id.tv_order_LOAD_FEES);
        tv_order_SITE_SURCHARGE= (TextView) findViewById(R.id.tv_order_SITE_SURCHARGE);
        tv_order_OTHER_FEES= (TextView) findViewById(R.id.tv_order_OTHER_FEES);
        tv_order_FEESCOUNT= (TextView) findViewById(R.id.tv_order_FEESCOUNT);
        tv_Check_Shipmentcost= (TextView) findViewById(R.id.tv_check_shipmentcost);
        tv_Check_Shipmentcost.setOnClickListener(this);
        ll_shipmentcost_detail= (LinearLayout) findViewById(R.id.ll_shipmentcost_detail);
        tv_SHIPMENT_NO= (TextView) findViewById(R.id.tv_SHIPMENT_NO);
        tv_DATE_ADD= (TextView) findViewById(R.id.tv_DATE_ADD);
        tv_DATE_ISSUE= (TextView) findViewById(R.id.tv_DATE_ISSUE);
        tv_TMS_FLEET_NAME= (TextView) findViewById(R.id.tv_TMS_FLEET_NAME);
        tv_DRIVER_NAME= (TextView) findViewById(R.id.tv_DRIVER_NAME);
        tv_TOTAL_QTY= (TextView) findViewById(R.id.tv_TOTAL_QTY);
        tv_DRIVER_TEL= (TextView) findViewById(R.id.tv_DRIVER_TEL);
        tv_TOTAL_WEIGHT= (TextView) findViewById(R.id.tv_TOTAL_WEIGHT);
        tv_PLATE_NUMBER= (TextView) findViewById(R.id.tv_PLATE_NUMBER);
        tv_TOTAL_VOLUME= (TextView) findViewById(R.id.tv_TOTAL_VOLUME);
        tv_AMOUNT_PRICE= (TextView) findViewById(R.id.tv_AMOUNT_PRICE);
        tv_CHARGE_AMOUNT= (TextView) findViewById(R.id.tv_CHARGE_AMOUNT);
        tv_TRANSPORT_FEES= (TextView) findViewById(R.id.tv_TRANSPORT_FEES);
        tv_DROPPOINT_FEES= (TextView) findViewById(R.id.tv_DROPPOINT_FEES);
        tv_FUEL_SURCHARGE= (TextView) findViewById(R.id.tv_FUEL_SURCHARGE);
        tv_RETURN_FEES= (TextView) findViewById(R.id.tv_RETURN_FEES);
        tv_SITE_SURCHARGE= (TextView) findViewById(R.id.tv_SITE_SURCHARGE);
        tv_PRESS_NIGHT= (TextView) findViewById(R.id.tv_PRESS_NIGHT);
        tv_DELIVER_FEES= (TextView) findViewById(R.id.tv_DELIVER_FEES);
        tv_LOAD_FEES= (TextView) findViewById(R.id.tv_LOAD_FEES);
        tv_OTHER_FEES= (TextView) findViewById(R.id.tv_OTHER_FEES) ;
        tv_FEESCOUNT= (TextView) findViewById(R.id.tv_FEESCOUNT);
        recyclerView_orderCostList= (RecyclerView) findViewById(R.id.recyclerView_orderCostList);
        recyclerView_otherCostList= (RecyclerView) findViewById(R.id.recyclerView_otherCostList);
        LinearLayoutManager ordermanager=new LinearLayoutManager(getMContext(),LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager othermanager=new LinearLayoutManager(getMContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView_orderCostList.setLayoutManager(ordermanager);
      //  recyclerView_orderCostList.addItemDecoration(new DividerListItemDecoration(getMContext(),DividerListItemDecoration.VERTICAL_LIST));
        recyclerView_otherCostList.setLayoutManager(othermanager);
      //  recyclerView_otherCostList.addItemDecoration(new DividerListItemDecoration(getMContext(),DividerListItemDecoration.VERTICAL_LIST));
        morderCostListAdapter=new OrderCostListAdapter(orderCosts,ShipmentCostActivity.this);
        motherCostListAdapter=new OtherCostListAdapter(otherCosts,ShipmentCostActivity.this);
        recyclerView_orderCostList.setAdapter(morderCostListAdapter);
        recyclerView_otherCostList.setAdapter(motherCostListAdapter);
    }
    private void setData(ShipmentCost shipmentCost) {
        orderCosts=shipmentCost.getOrder();
        morderCostListAdapter.setOrderCosts(orderCosts);
        for (int i=0;i<orderCosts.size();i++){
            if (ORD_NO.equals(orderCosts.get(i).getORD_NO())){
                tv_ORD_NO.setText(orderCosts.get(i).getORD_NO());
                tv_ORD_NO_CLIENT.setText(orderCosts.get(i).getORD_NO_CLIENT());
                tv_ORD_FROM_NAMES.setText(orderCosts.get(i).getORD_FROM_NAMES());
                tv_ORD_TO_NAME.setText(orderCosts.get(i).getORD_TO_NAME());
                tv_order_AMOUNT_PRICE.setText(orderCosts.get(i).getAMOUNT_PRICE());
                tv_order_CHARGE_AMOUNT.setText(orderCosts.get(i).getCHARGE_AMOUNT());
                tv_ORD_QTY.setText(orderCosts.get(i).getORD_QTY());
                tv_order_TRANSPORT_FEES.setText(orderCosts.get(i).getTRANSPORT_FEES());
                tv_ORD_WEIGHT.setText(orderCosts.get(i).getORD_WEIGHT());
                tv_order_DROPPOINT_FEES.setText(orderCosts.get(i).getDROPPOINT_FEES());
                tv_ORD_VOLUME.setText(orderCosts.get(i).getORD_VOLUME());
                tv_order_RETURN_FEES.setText(orderCosts.get(i).getRETURN_FEES());
                tv_order_DELIVER_FEES.setText(orderCosts.get(i).getDELIVER_FEES());
                tv_order_PRESS_NIGHT.setText(orderCosts.get(i).getPRESS_NIGHT());
                tv_order_FUEL_SURCHARGE.setText(orderCosts.get(i).getFUEL_SURCHARGE());
                tv_order_LOAD_FEES.setText(orderCosts.get(i).getLOAD_FEES());
                tv_order_SITE_SURCHARGE.setText(orderCosts.get(i).getSITE_SURCHARGE());
                tv_order_OTHER_FEES.setText(orderCosts.get(i).getOTHER_FEES());
                tv_order_FEESCOUNT.setText(orderCosts.get(i).getFEESCOUNT());
            }
        }
        otherCosts=shipmentCost.getOther();
        motherCostListAdapter.setOtherCosts(otherCosts);
        ShipmentCost.ShipmentCostDetail shipmentCostDetail=shipmentCost.getShipment();
        tv_SHIPMENT_NO.setText(shipmentCostDetail.getSHIPMENT_NO());
        tv_DATE_ADD.setText(shipmentCostDetail.getDATE_ADD());
        tv_DATE_ISSUE.setText(shipmentCostDetail.getDATE_ISSUE());
        tv_TMS_FLEET_NAME.setText(shipmentCostDetail.getTMS_FLEET_NAME());
        tv_DRIVER_NAME.setText(shipmentCostDetail.getDRIVER_NAME());
        tv_TOTAL_QTY.setText(shipmentCostDetail.getTOTAL_QTY());
        tv_DRIVER_TEL.setText(shipmentCostDetail.getDRIVER_TEL());
        tv_TOTAL_WEIGHT.setText(shipmentCostDetail.getTOTAL_WEIGHT());
        tv_PLATE_NUMBER.setText(shipmentCostDetail.getPLATE_NUMBER());
        tv_TOTAL_VOLUME.setText(shipmentCostDetail.getTOTAL_VOLUME());
        tv_AMOUNT_PRICE.setText(shipmentCostDetail.getAMOUNT_PRICE());
        tv_CHARGE_AMOUNT.setText(shipmentCostDetail.getCHARGE_AMOUNT());
        tv_TRANSPORT_FEES.setText(shipmentCostDetail.getTRANSPORT_FEES());
        tv_DROPPOINT_FEES.setText(shipmentCostDetail.getDROPPOINT_FEES());
        tv_FUEL_SURCHARGE.setText(shipmentCostDetail.getFUEL_SURCHARGE());
        tv_RETURN_FEES.setText(shipmentCostDetail.getRETURN_FEES());
        tv_SITE_SURCHARGE.setText(shipmentCostDetail.getSITE_SURCHARGE());
        tv_PRESS_NIGHT.setText(shipmentCostDetail.getPRESS_NIGHT());
        tv_DELIVER_FEES.setText(shipmentCostDetail.getDELIVER_FEES());
        tv_LOAD_FEES.setText(shipmentCostDetail.getLOAD_FEES());
        tv_OTHER_FEES.setText(shipmentCostDetail.getOTHER_FEES());
        tv_FEESCOUNT.setText(shipmentCostDetail.getFEESCOUNT());
        hasData=true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_check_shipmentcost:if (hasData){
                    if (ll_shipmentcost_detail.getVisibility()==View.VISIBLE){
                        ll_shipmentcost_detail.setVisibility(View.GONE);
                    }else {
                        ll_shipmentcost_detail.setVisibility(View.VISIBLE);
                    }
                }else {
                    initdata();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void postSuccessMsg(String msg, String request_tag) {
        if (msg.equals("error")){
            showToastMsg("网络加载失败，请返回重新加载");
        }else if (request_tag.equals(TAG_GetPrice)){
            try {
                JSONObject jo= JSON.parseObject(msg);
                shipmentCost=JSON.parseObject(jo.getString("result"),ShipmentCost.class);
                setData(shipmentCost);
            }catch (JSONException ex){
                ex.printStackTrace();
            }

        }
    }

}
