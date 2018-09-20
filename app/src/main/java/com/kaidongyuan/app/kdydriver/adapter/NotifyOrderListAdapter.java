package com.kaidongyuan.app.kdydriver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.bean.order.Notify;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2016/12/15.
 */
public class NotifyOrderListAdapter extends BaseAdapter {
    private Notify notify;
    private Context mContext;

    public NotifyOrderListAdapter(Notify notify, Context mContext) {
        this.notify = notify;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (notify!=null&&notify.getSHIPMENT_List().size()>0){
            return notify.getSHIPMENT_List().size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return notify.getSHIPMENT_List().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.item_ordermap_listview,null);
            holder.tv_orderNum= (TextView) convertView.findViewById(R.id.tv_orderNum);
            holder.tv_clientNum= (TextView) convertView.findViewById(R.id.tv_clientNum);
            holder.tv_clientName= (TextView) convertView.findViewById(R.id.tv_clientName);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_orderNum.setText("订单编号："+notify.getSHIPMENT_List().get(i).getORD_NO());
        holder.tv_clientNum.setText("客户单号："+notify.getSHIPMENT_List().get(i).getORD_NO_CLIENT());
        holder.tv_clientName.setText("客户名称："+notify.getSHIPMENT_List().get(i).getORD_TO_NAME());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_orderNum,tv_clientNum,tv_clientName;
    }
}
