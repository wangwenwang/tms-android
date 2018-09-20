package com.kaidongyuan.app.kdydriver.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kaidongyuan.app.basemodule.interfaces.AsyncHttpCallback;
import com.kaidongyuan.app.basemodule.ui.fragment.BaseLifecyclePrintFragment;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.adapter.OrderFMViewpageAdapter;
import com.kaidongyuan.app.kdydriver.ui.widget.IsScrollableViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/7.
 */
public class OrderFragment extends BaseLifecyclePrintFragment  {
    private View parent;
    private TabLayout tabLayout;
    private IsScrollableViewPager viewPager;
    private List<String>tablist;
    private List<Fragment>fragments;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        parent=inflater.inflate(R.layout.fragment_order,container,false);
        initView();
        return parent;
    }

    private void initView() {
        tabLayout= (TabLayout) parent.findViewById(R.id.tablayout_orderFragment);
        viewPager= (IsScrollableViewPager) parent.findViewById(R.id.viewPager_orderFragment);
        viewPager.setisScroll(true);
        tablist=new ArrayList<>();
        tablist.add("在途");
        tablist.add("已交付");
        tablist.add("全部");
        fragments=new ArrayList<>();
        OrderListFragment orderListFragmentN=new OrderListFragment();
        Bundle bundleN=new Bundle();
        bundleN.putString("TAG_TRANSIT","N");
        orderListFragmentN.setArguments(bundleN);
        fragments.add(orderListFragmentN );
        OrderListFragment orderListFragmentY=new OrderListFragment();
        Bundle bundleY=new Bundle();
        bundleY.putString("TAG_TRANSIT","Y");
        orderListFragmentY.setArguments(bundleY);
        fragments.add(orderListFragmentY );
        OrderListFragment orderListFragment=new OrderListFragment();
        Bundle bundle=new Bundle();
        bundle.putString("TAG_TRANSIT","");
        orderListFragment.setArguments(bundle);
        fragments.add(orderListFragment );
        for (int i=0;i<tablist.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(tablist.get(i)));
        }
        OrderFMViewpageAdapter orderFMViewpageAdapter=new OrderFMViewpageAdapter(getFragmentManager(),fragments,tablist,getActivity());
        viewPager.setAdapter(orderFMViewpageAdapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
        for (int j=0;j<tabLayout.getTabCount();j++){
            TabLayout.Tab tab=tabLayout.getTabAt(j);
            tab.setCustomView(orderFMViewpageAdapter.getTabView(tablist.get(j)));
        }
       viewPager.setCurrentItem(0);
    }
}
