package com.kaidongyuan.app.kdydriver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.bean.order.ShipmentCost;

import java.util.List;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/3/28.
 */
public class OtherCostListAdapter extends RecyclerView.Adapter<OtherCostListAdapter.MyViewHolder> {
    private List<ShipmentCost.OtherCost> otherCosts;
    private Context mcontext;

    public OtherCostListAdapter(List<ShipmentCost.OtherCost> otherCosts, Context mcontext) {
        this.otherCosts = otherCosts;
        this.mcontext = mcontext;
    }

    public void setOtherCosts(List<ShipmentCost.OtherCost> otherCosts) {
        this.otherCosts = otherCosts;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(mcontext).inflate(R.layout.item_othercost_recyclerview,parent,false);
        MyViewHolder holder=new MyViewHolder(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ShipmentCost.OtherCost otherCost=otherCosts.get(position);
        holder.tv_ORD_NO.setText(otherCost.getORD_NO());
        holder.tv_FEE_TYPE.setText(otherCost.getFEE_TYPE());
        holder.tv_FEE_DESC.setText(otherCost.getFEE_DESC());
        holder.tv_OTHER_FEES.setText(otherCost.getOTHER_FEES());
    }

    @Override
    public int getItemCount() {
        return otherCosts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_ORD_NO,tv_FEE_TYPE,tv_OTHER_FEES,tv_FEE_DESC;//
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_ORD_NO= (TextView) itemView.findViewById(R.id.tv_ORD_NO);
            tv_FEE_TYPE= (TextView) itemView.findViewById(R.id.tv_FEE_TYPE);
            tv_OTHER_FEES= (TextView) itemView.findViewById(R.id.tv_OTHER_FEES);
            tv_FEE_DESC= (TextView) itemView.findViewById(R.id.tv_FEE_DESC);
        }
    }

}
