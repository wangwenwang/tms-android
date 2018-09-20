package com.kaidongyuan.app.kdydriver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.bean.order.Order.OrderDetails;
import com.kaidongyuan.app.kdydriver.utils.ListViewUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 物流信息中的Adapter，内容比较简单
 */
public class OrderDetailsSimpleAdapter extends BaseAdapter {
    private List<OrderDetails> orderDetails;
    private Context mContext;

    public OrderDetailsSimpleAdapter(Context context) {
        this.orderDetails = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return orderDetails.size();
    }

    public void resetData(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
        notifyDataSetChanged();

    }

    @Override
    public Object getItem(int position) {
        return orderDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderDetails orderDetail = this.orderDetails.get(position);
        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_detail_simple, null);
            holder = new Holder();
            holder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
            holder.tv_order_qty = (TextView) convertView.findViewById(R.id.tv_order_qty);
            holder.tv_order_weight = (TextView) convertView.findViewById(R.id.tv_order_weight);
            holder.tv_order_volume = (TextView) convertView.findViewById(R.id.tv_order_volume);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_productName.setText(orderDetail.getPRODUCT_NAME() + " (" + orderDetail.getPRODUCT_NO() + ")");
        holder.tv_order_qty.setText(orderDetail.getISSUE_QTY() + orderDetail.getORDER_UOM());
        holder.tv_order_weight.setText(orderDetail.getISSUE_WEIGHT() + "吨");
        holder.tv_order_volume.setText(orderDetail.getISSUE_VOLUME() + "吨");
        return convertView;
    }

    class Holder {
        TextView tv_productName, tv_order_qty, tv_order_weight, tv_order_volume;
    }


}
