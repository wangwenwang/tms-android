package com.kaidongyuan.app.kdydriver.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.ui.fragment.BaseLifecyclePrintFragment;
import com.kaidongyuan.app.basemodule.utils.nomalutils.DateUtil;
import com.kaidongyuan.app.basemodule.utils.nomalutils.StringUtils;
import com.kaidongyuan.app.basemodule.widget.DateTimePicker.SlideDateTimeListener;
import com.kaidongyuan.app.basemodule.widget.DateTimePicker.SlideDateTimePicker;
import com.kaidongyuan.app.basemodule.widget.MLog;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.adapter.OrderListAdapter;
import com.kaidongyuan.app.kdydriver.bean.order.Order;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.ui.widget.DividerListItemDecoration;
import com.kaidongyuan.app.kdydriver.utils.SharedPreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/14.
 */
public class OrderListFragment extends BaseLifecyclePrintFragment implements AsyncHttpCallback,View.OnClickListener {
    private RecyclerView orderListReclerView;
    private OrderListAdapter myorderlistAdapter;
    private  ArrayList<Order>orderlist;
    private Context mContext;
    private int page = 1;// 用于存储当前数据的页数
    private final int pageSize = 20;// 每页的数据条数
    private OrderAsyncHttpClient mHttpClient;
    private Map<String,String> params;
    public String TAG_TRANSIT ;
    private LinearLayout ll_no_record;
    public static boolean isrefresh=false;
    private View parent;
    private SwipeRefreshLayout mswipeRefreshLayout;
    private LinearLayoutManager manager;
    private LinearLayout ll_times_line;
    private TextView tv_startTime,tv_endTime;
    private Date startDate,endDate;
    private SimpleDateFormat df;


//    public OrderListFragment(String TAG_TRANSIT) {
//        this.TAG_TRANSIT = TAG_TRANSIT;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        parent=inflater.inflate(R.layout.fragment_orderlist, null);
        Bundle bundle=getArguments();
        if (bundle!=null){
            TAG_TRANSIT=bundle.getString("TAG_TRANSIT");
        }else {
            showToastMsg("订单分类失败，默认加载全部订单");
            TAG_TRANSIT="";
        }
        mContext=getActivity();
        page=1;
        return parent;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        loadData(page);
    }

    private void loadData(int page) {
        if (startDate.after(endDate)){
            showToastMsg("请设置正确时间轴区间！");
            return;
        }
        if (page==1&&orderlist!=null&&orderlist.size()>0){
                orderlist=new ArrayList<>();
            myorderlistAdapter.loadState= OrderListAdapter.LOADING_MORE;
        }
        params = new HashMap<String, String>();
        //	AppContext appContext=AppContext.getInstance();
        //	User user=appContext.getUser();
        params.put("strUserIdx", SharedPreferencesUtils.getUserId());
        params.put("strIsPay", TAG_TRANSIT);
        params.put("strPage", page + "");
        params.put("strPageCount", pageSize +"");
        params.put("strStartDate",df.format(startDate));
        params.put("strEndDate",df.format(endDate));
        params.put("strLicense", "");
        mHttpClient.setShowToast(false);
      //  mHttpClient.sendRequest(Constants.URL.GetDriverDateOrderList, params, TAG_TRANSIT);
        mHttpClient.sendRequest(Constants.URL.GetDriverDateOrderClientList, params, TAG_TRANSIT);
    }

    private void initView() {
        mContext=getActivity();
        isrefresh=false;
        mswipeRefreshLayout= (SwipeRefreshLayout) parent.findViewById(R.id.swipeRefreshLayout_orderlist_unpayed);
        mswipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_orange_light, android.R.color.holo_red_light, android.R.color.holo_green_light);
        orderListReclerView= (RecyclerView) parent.findViewById(R.id.recyclerView_orderlist_unpayed);
        orderlist=new ArrayList<>();
        myorderlistAdapter=new OrderListAdapter(orderlist,OrderListFragment.this);
        manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        orderListReclerView.setLayoutManager(manager);
        orderListReclerView.addItemDecoration(new DividerListItemDecoration(getMContext(), DividerListItemDecoration.VERTICAL_LIST));
        orderListReclerView.setAdapter(myorderlistAdapter);
        mHttpClient=new OrderAsyncHttpClient(this,this);
        mswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                loadData(page);
            }
        });
        orderListReclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
             // ll_times_line.setVisibility(View.GONE);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (manager.findLastVisibleItemPosition() + 1) == myorderlistAdapter.getItemCount()) {
                   if (myorderlistAdapter.loadState!=OrderListAdapter.NO_MORE) {
                       myorderlistAdapter.loadState = OrderListAdapter.LOADING_MORE;
                       loadData(++page);
                   }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(Math.abs(dx)+Math.abs(dy)>200) {//根据滑动订单列表情况来判定是否显示时间轴信息。
                    ll_times_line.setVisibility(View.VISIBLE);
                }
            }
        });
        ll_no_record= (LinearLayout) parent.findViewById(R.id.ll_no_record);
        ll_no_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page=1;
                loadData(page);
            }
        });
        ll_times_line= (LinearLayout) parent.findViewById(R.id.ll_times_line);
        ll_times_line.setVisibility(View.GONE);
        ll_times_line.getParent().requestDisallowInterceptTouchEvent(true);
        tv_startTime= (TextView) parent.findViewById(R.id.tv_start_point);
        tv_startTime.setOnClickListener(this);
        tv_endTime= (TextView) parent.findViewById(R.id.tv_end_point);
        tv_endTime.setOnClickListener(this);
        endDate=DateUtil.getDateTime(System.currentTimeMillis()+1*24*60*60*1000L);
        startDate=DateUtil.getDateTime(System.currentTimeMillis()-365*24*60*60*1000L);
        df = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void onResume() {
        super.onResume();
        //如果有订单提交则通过改变isrefresh值为true来刷新orderlist数据
        if (isrefresh==true){
            page=1;
            loadData(page);
            isrefresh=false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        mHttpClient.cancleRequest(TAG_TRANSIT);
        super.onDestroyView();
    }


    @Override
    public void postSuccessMsg(String msg, String request_tag) {
        mswipeRefreshLayout.setRefreshing(false);
        if (msg.equals("error")){
            MLog.w("postSuccessMsg error");
            mHttpClient.cancleRequest(TAG_TRANSIT);
            if (page == 1){
                if (orderlist == null || orderlist.size() == 0){
                    mswipeRefreshLayout.setVisibility(View.GONE);
                    ll_no_record.setVisibility(View.VISIBLE);
                    ll_times_line.setVisibility(View.VISIBLE);
                }else {
                    mswipeRefreshLayout.setVisibility(View.VISIBLE);
                    ll_no_record.setVisibility(View.GONE);
                }
            }else {
                myorderlistAdapter.loadState= OrderListAdapter.NO_MORE;
                myorderlistAdapter.notifyItemChanged(manager.findLastVisibleItemPosition());
            }
            return;
        }
        if (request_tag.equals(TAG_TRANSIT)){
            com.alibaba.fastjson.JSONObject object= JSON.parseObject(msg);
            List<Order>tmsOrderList= JSON.parseArray(object.getString("result"),Order.class);
            MLog.i("tmsOrderlist.size:" + tmsOrderList.size());
            if (orderlist!=null){
                orderlist.addAll(tmsOrderList);
              //ll_times_line.setVisibility(View.GONE);
                if (orderlist.size()<pageSize){
                    myorderlistAdapter.loadState=OrderListAdapter.NO_MORE;
                    ll_times_line.setVisibility(View.VISIBLE);
                }
                myorderlistAdapter.setMorderlist(orderlist);
            }
            if (mswipeRefreshLayout.getVisibility()==View.GONE) {
                mswipeRefreshLayout.setVisibility(View.VISIBLE);
                ll_no_record.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_start_point:
                new SlideDateTimePicker.Builder(this.getFragmentManager())
                        .setListener(new DateHandler(R.id.tv_start_point))
                        .setInitialDate(startDate)
                        // .setMinDate(new Date()) 这样就导致取时间最小为当前时间
                        .setMinDate(DateUtil.getDateTime(System.currentTimeMillis()-365*24*60*60*1000L))
                        .setMaxDate(DateUtil.getDateTime(System.currentTimeMillis()))
                        .build()
                        .show();
                break;
            case R.id.tv_end_point:
                new SlideDateTimePicker.Builder(this.getFragmentManager())
                        .setListener(new DateHandler(R.id.tv_end_point))
                        .setInitialDate(endDate)
                        .setMaxDate(DateUtil.getDateTime(System.currentTimeMillis()+1*24*60*60*1000L))
                        .build()
                        .show();
                break;
            default:
                break;
        }
    }
    class DateHandler extends SlideDateTimeListener {
        TextView tv;
        DateHandler(int which) {
            tv= (TextView) parent.findViewById(which);
        }
        @Override
        public void onDateTimeCancel() {
            super.onDateTimeCancel();
            page=1;
            loadData(page);
            tv.setText(null);
            if (tv.getId()==R.id.tv_start_point) {
                tv.setBackground(getResources().getDrawable(R.drawable.start_point));
            }else if (tv.getId()==R.id.tv_end_point){
                tv.setBackground(getResources().getDrawable(R.drawable.end_point));
            }
        }
        @Override
        public void onDateTimeSet(Date date) {
            if (date != null) {
                // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar=Calendar.getInstance(Locale.CHINA);
                calendar.setTime(date);
                int month=calendar.get(Calendar.MONTH)+1;
                tv.setText(month + "/ \n  " + calendar.get(Calendar.DATE));
                tv.setBackgroundColor(getResources().getColor(R.color.transparent));
                   if (tv.getId()==R.id.tv_start_point) {
                       // tv.setTextColor(getResources().getColor(R.color.white));
                       startDate = date;//设置为XXXX-XX-XX 00:00:00
                       if (!endDate.before(startDate)) {
                           page = 1;
                           loadData(page);
                       }
                       return;
                   }else if (tv.getId()==R.id.tv_end_point){
                       endDate=date;
                       if (!endDate.before(startDate)) {
                           page = 1;
                           loadData(page);
                       }
                       return;
                   }
            }
        }
    }
}
