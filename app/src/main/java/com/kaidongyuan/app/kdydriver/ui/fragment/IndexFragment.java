package com.kaidongyuan.app.kdydriver.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.ui.fragment.BaseLifecyclePrintFragment;
import com.kaidongyuan.app.basemodule.utils.nomalutils.DensityUtil;
import com.kaidongyuan.app.basemodule.widget.SlidingTitleView;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.adapter.NotifyAdapter;
import com.kaidongyuan.app.kdydriver.adapter.NotifyOrderListAdapter;
import com.kaidongyuan.app.kdydriver.bean.order.Notify;
import com.kaidongyuan.app.kdydriver.constants.Constants;
import com.kaidongyuan.app.kdydriver.httpclient.OrderAsyncHttpClient;
import com.kaidongyuan.app.kdydriver.ui.activity.NotifyActivity;
import com.kaidongyuan.app.kdydriver.ui.activity.OrderDetailActivity;
import com.kaidongyuan.app.kdydriver.ui.activity.OrderTrackCheckActivity;
import com.kaidongyuan.app.kdydriver.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/7.
 */
public class IndexFragment extends BaseLifecyclePrintFragment implements AsyncHttpCallback,View.OnClickListener {
    private View parent;
    private LinearLayout ll_points;
    private ViewPager vp_ad;
    private List<ImageView> images = new ArrayList<ImageView>();
    private List<ImageView> points = new ArrayList<ImageView>();
    private SlidingTitleView titleView;
    private LinearLayout ll_no_record;
    private int current_position;
    private boolean isContinue;
    private SwipeRefreshLayout srLayout_notify;
    private int page = 1;// 用于存储当前数据的页数
    private final int pageSize = 10;// 每页的数据条数
    private ListView lv_notify;
    private final String Tag_Notify = "tag_notify";
    private final String Tag_ReadMessage="tag_readMessage";
    private List<Notify> notifies;
    private NotifyAdapter notifyAdapter;
    private TextView btn_search_track;
    private Thread cycleThread;//图片轮播线程
    private Boolean iscycleThread=true;//图片轮播线程开关
    private OrderAsyncHttpClient client;
    private Dialog mOrdersDialog;// notify订单选择 Dialog
    private ListView mListViewOrders;//用户业务类型的 ListView
    private Button mButtonCancel;//取消通过notify查看订单对话框
    private NotifyOrderListAdapter mNotifyOrderListAdapter;//用户业务类型 Adapter
    private Notify currentNotify;//所选notify
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        parent=inflater.inflate(R.layout.fragment_index,container,false);
        iscycleThread=true;
        client = new OrderAsyncHttpClient(this, this);
        client.setShowToast(false);
        initView();
        initVpAd();
        page=1;
        getInformation(page);
        return parent;
    }

    private void initVpAd() {
        images.clear();
        for (int i=0;i<4;i++){
            ImageView image=new ImageView(getActivity());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            images.add(image);
        }
        images.get(0).setImageResource(R.drawable.ad_pic_0);
        images.get(1).setImageResource(R.drawable.ad_pic_1);
        images.get(2).setImageResource(R.drawable.ad_pic_2);
        images.get(3).setImageResource(R.drawable.ad_pic_3);
        initpager();
    }
    /**
     * 在获取图片信息后，初始化viewpager
     * 这样会有一个问题，在页面初始化时不能第一时间显示出图片信息，优化方案：
     * 本地保存图片地址信息，这样imageloader能获取到缓存
     */
    private void initpager() {
        vp_ad.setAdapter(pageradapter);
        ImageView imageView;
        for (int i=0;i<images.size();i++){
            // 创建一个ImageView, 并设置宽高. 将该对象放入到数组中
            imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(10), DensityUtil.dip2px(10));
            params.setMargins(DensityUtil.dip2px(5), 0, DensityUtil.dip2px(5),
                    0);
            imageView.setLayoutParams(params);

            points.add(imageView);
            // 初始值, 默认第0个选中
            if (i == 0) {
                points.get(i).setBackgroundResource(R.drawable.point_unfocused);
            } else {
                points.get(i).setBackgroundResource(R.drawable.point_focused);
            }
            // 将小圆点放入到布局中
            ll_points.addView(points.get(i));
        }
        vp_ad.setOnPageChangeListener(pageChangeListener);
        vp_ad.setCurrentItem(points.size()*100);
        cycleThread=new Thread(runnable);
        cycleThread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        isContinue=true;
        notifyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        isContinue=false;
    }

    @Override
    public void onDestroyView() {
        if (client!=null) {
            client.cancleRequest(Tag_Notify);
        }
        super.onDestroyView();
        iscycleThread=false;
        ll_points.removeAllViews();
        ll_points=null;
        points.clear();
    }

    private void initView() {
        vp_ad= (ViewPager) parent.findViewById(R.id.viewpager_ad);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(DensityUtil.getWidth(),DensityUtil.getWidth()/2);
        vp_ad.setLayoutParams(params);
        ll_points= (LinearLayout) parent.findViewById(R.id.ll_points);
        titleView= (SlidingTitleView) parent.findViewById(R.id.info_title_view);
        titleView.setText(getString(R.string.index));
        titleView.setMode(SlidingTitleView.MODE_NULL);
        ll_no_record= (LinearLayout) parent.findViewById(R.id.ll_no_record);
        ll_no_record.setOnClickListener(this);
        srLayout_notify= (SwipeRefreshLayout) parent.findViewById(R.id.swipeRefreshLayout_indexFM_information);
        srLayout_notify.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_orange_light, android.R.color.holo_red_light, android.R.color.holo_green_light);
        srLayout_notify.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    page=1;
                getInformation(page);
            }
        });
        lv_notify= (ListView) parent.findViewById(R.id.lv_notify);
        lv_notify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=notifies.size())return;
                currentNotify=notifies.get(i);
                int type=Integer.parseInt(currentNotify.getTYPE().trim());
                if (type==0){
                    choiceOrderMapDialog();
                }else if (type==1){
                    currentNotify.setISREAD("1");
                    Intent intent = new Intent(getActivity(), NotifyActivity.class);
                    intent.putExtra("title", currentNotify.getTITLE());
                    intent.putExtra("id", currentNotify.getIDX());
                    mStartActivity(intent);
                }
            }
        });
        lv_notify.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i==SCROLL_STATE_IDLE&&notifyAdapter!=null&&notifyAdapter.loadState==NotifyAdapter.LOADING_MORE&&absListView.getLastVisiblePosition()+1>=notifies.size()){
                    getInformation(page);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        notifyAdapter=new NotifyAdapter(getMContext());
        notifies = new ArrayList<>();
        notifyAdapter.setData(notifies);
        lv_notify.setAdapter(notifyAdapter);
        btn_search_track= (TextView) parent.findViewById(R.id.btn_search_track);
        btn_search_track.setOnClickListener(this);
    }
    /**
     * 创建选择订单的列表 Dialog
     */
    private void choiceOrderMapDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getMContext());
            mOrdersDialog = builder.create();
            mOrdersDialog.setCanceledOnTouchOutside(false);
            mOrdersDialog.show();
            Window window = mOrdersDialog.getWindow();
            window.setContentView(R.layout.dialog_ordernotify_choice);
            mListViewOrders = (ListView) window.findViewById(R.id.listView_orderMaps);
            mListViewOrders.setOnItemClickListener(new InnerOnItemClickListener());
            mButtonCancel = (Button) window.findViewById(R.id.button_cancel);
            mButtonCancel.setOnClickListener(this);
            mNotifyOrderListAdapter = new NotifyOrderListAdapter(currentNotify,getMContext());
            mListViewOrders.setAdapter(mNotifyOrderListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void readMessage(String notifyIDX) {
        Map<String, String> params = new HashMap<>();
        params.put("strLicense", "");
        params.put("strIDX",notifyIDX);
        client.sendRequest(Constants.URL.GetMessageDetils, params, Tag_ReadMessage);
    }

    @Override
    public void postSuccessMsg(String msg, String request_tag) {
        srLayout_notify.setRefreshing(false);
        if (msg.equals("error")&&request_tag.equals(Tag_Notify)){
            if (page==1) {
                if (notifies==null||notifies.size()<=0) {
                    ll_no_record.setVisibility(View.VISIBLE);
                    srLayout_notify.setVisibility(View.GONE);
                    return;
                }else {
                    ll_no_record.setVisibility(View.GONE);
                    srLayout_notify.setVisibility(View.VISIBLE);
                    return;
                }
            }else {
                notifyAdapter.loadState=NotifyAdapter.NO_MORE;
                synchronized (notifyAdapter){
                    notifyAdapter.notify();
                }
                return;
            }
        } else if (request_tag.equals(Tag_Notify)) {
            if (notifies==null){
                notifies=new ArrayList<>();
            }
            try {
                JSONObject jo = JSON.parseObject(msg);
                List<Notify> jo2notifies = JSON.parseArray(jo.getString("result"), Notify.class);
                notifies.addAll(jo2notifies);
                page++;
                if (notifies.size()<pageSize){
                    notifyAdapter.loadState=NotifyAdapter.NO_MORE;
                }
                notifyAdapter.setData(notifies);
                if (srLayout_notify.getVisibility()==View.GONE){
                    srLayout_notify.setVisibility(View.VISIBLE);
                    ll_no_record.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_search_track:
                mStartActivity(OrderTrackCheckActivity.class);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.ll_no_record:
                page=1;
                getInformation(page);
                break;
            case R.id.button_cancel:
                mOrdersDialog.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     *@auther: Tom
     *created at 2016/6/12 17:24
     *通过司机的IDX 查找对应的订单消息资讯列表
     */
    private void getInformation(int current_page) {
       if (page==1&&notifies!=null&&notifies.size()>0){
           notifies=new ArrayList<>();
           notifyAdapter.loadState=NotifyAdapter.LOADING_MORE;
       }
        Map<String, String> params = new HashMap<>();
        params.put("strLicense", "");
        params.put("strUserId", SharedPreferencesUtils.getUserId());
        params.put("strPage", current_page + "");
        params.put("strPageCount", pageSize +"");
        client.sendRequest(Constants.URL.GetMessage, params, Tag_Notify);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (iscycleThread) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                if (isContinue) {
                    handler.sendEmptyMessage(1);
                }

            }
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                vp_ad.setCurrentItem(current_position + 1);
            }
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            current_position = position;
            for (int i = 0; i < points.size(); i++) {
                points.get(i).setBackgroundResource(i == position % points.size() ? R.drawable.point_unfocused : R.drawable.point_focused);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private PagerAdapter pageradapter=new PagerAdapter() {
        @Override
        public int getCount() {
            try {
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            } catch (NullPointerException e) { // (sh)it happens (Issue #660)
                showToastMsg("手机SD卡存储功能异常");
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                container.removeView(images.get(position%images.size()));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            try {
                container.addView(images.get(position % images.size()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return images.get(position % images.size());
        }
    };
    /**
     * 用户业务列表 Item 点击监听实现类
     */
    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Integer.parseInt(currentNotify.getISREAD())==0){
                    readMessage(currentNotify.getIDX());
                    currentNotify.setISREAD("1");
                }
                mOrdersDialog.dismiss();
                Intent intent = new Intent(getMContext(), OrderDetailActivity.class);
                intent.putExtra("order_id", currentNotify.getSHIPMENT_List().get(position).getORD_IDX());
                mStartActivity(intent);
        }
    }

}
