package com.kaidongyuan.app.kdydriver.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.bean.order.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/8/8.
 */
public class OrderListChoiceAdapter extends RecyclerView.Adapter<OrderListChoiceAdapter.MyViewHolder>  {
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map ;
    //数据源
    private List<Order> list;

    public OrderListChoiceAdapter(List<Order> list) {
        this.list = list;
        map=new HashMap<>();
        initMap(false);
    }
    //初始化map集合
    public void initMap(boolean allcheck) {
        for (int i = 0; i < list.size(); i++) {
            map.put(i, allcheck);
        }
        notifyDataSetChanged();
    }
    public void setData(List<Order> list){
        this.list=list;
        initMap(false);
    }
    /**
     * @return 所选订单id的数组
     */
    public ArrayList<Order> getSelectedlist(){
        ArrayList<Order> selectedlist=new ArrayList<>();
        try {
            Iterator iterator=map.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry entry= (Map.Entry) iterator.next();
                if ((Boolean) entry.getValue()){
                    selectedlist.add(list.get((Integer) entry.getKey()));//获取订单列表时订单的idx在ORD_IDX中，获取订单详情时idx在IDX中
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return selectedlist;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_choice, parent, false);
        MyViewHolder vh = new MyViewHolder(root);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.order_no.setText(list.get(position).getORD_NO());
        holder.tv_party_order_no.setText(list.get(position).getORD_NO_CLIENT());
        holder.tv_order_to_name.setText(list.get(position).getORD_TO_NAME());
        holder.order_to_address.setText(list.get(position).getORD_TO_ADDRESS());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //用map集合保存
                map.put(position, isChecked);
            }
        });
        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        holder.checkBox.setChecked(map.get(position));
    }

    @Override
    public int getItemCount() {
        if (list!=null){
          return list.size();
        }
        return 0;
    }

    //视图管理
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView order_no,tv_party_order_no,tv_order_to_name,order_to_address;
        private CheckBox checkBox;


        public MyViewHolder(View root) {
            super(root);
            order_no = (TextView) root.findViewById(R.id.tv_order_no);
            tv_party_order_no= (TextView) root.findViewById(R.id.tv_party_order_no);
            tv_order_to_name= (TextView) root.findViewById(R.id.tv_order_to_name);
            order_to_address= (TextView) root.findViewById(R.id.tv_order_to_address);
            checkBox = (CheckBox) root.findViewById(R.id.cb_order);
        }
    }
}
