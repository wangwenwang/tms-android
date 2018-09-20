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
 * Created by Administrator on 2017/3/27.
 */
public class OrderCostListAdapter extends RecyclerView.Adapter<OrderCostListAdapter.MyViewHolder>  {
    private List<ShipmentCost.OrderCost> orderCosts;
   private Context mcontext;

    public OrderCostListAdapter(List<ShipmentCost.OrderCost> orderCosts, Context context) {
        this.orderCosts = orderCosts;
       this.mcontext=context;
    }

    public void setOrderCosts(List<ShipmentCost.OrderCost> orderCosts) {
        this.orderCosts = orderCosts;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(mcontext).inflate(R.layout.item_ordercost_recyclerview,parent,false);
        MyViewHolder holder=new MyViewHolder(itemview);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ShipmentCost.OrderCost orderCost=orderCosts.get(position);
        holder.tv_ORD_NO.setText(orderCost.getORD_NO());
        holder.tv_ORD_NO_CLIENT.setText(orderCost.getORD_NO_CLIENT());
//        holder.tv_ORD_STATE.setText(orderCost.getORD_STATE());
//        holder.tv_ORD_WORKFLOW.setText(orderCost.getORD_WORKFLOW());
//        holder.tv_UPODATE03.setText(orderCost.getUPODATE03());
        holder.tv_ORD_FROM_NAMES.setText(orderCost.getORD_FROM_NAMES());
        holder.tv_ORD_TO_NAME.setText(orderCost.getORD_TO_NAME());
        holder.tv_CHARGE_AMOUNT.setText(orderCost.getCHARGE_AMOUNT());
        holder.tv_TRANSPORT_FEES.setText(orderCost.getTRANSPORT_FEES());
        holder.tv_DELIVER_FEES.setText(orderCost.getDELIVER_FEES());
        holder.tv_DROPPOINT_FEES.setText(orderCost.getDROPPOINT_FEES());
        holder.tv_LOAD_FEES.setText(orderCost.getLOAD_FEES());
        holder.tv_SITE_SURCHARGE.setText(orderCost.getSITE_SURCHARGE());
        holder.tv_FUEL_SURCHARGE.setText(orderCost.getFUEL_SURCHARGE());
        holder.tv_RETURN_FEES.setText(orderCost.getRETURN_FEES());
        holder.tv_PRESS_NIGHT.setText(orderCost.getPRESS_NIGHT());
        holder.tv_OTHER_FEES.setText(orderCost.getOTHER_FEES());
        holder.tv_AMOUNT_PRICE.setText(orderCost.getAMOUNT_PRICE());
        holder.tv_FEESCOUNT.setText(orderCost.getFEESCOUNT());
        holder.tv_ORD_QTY.setText(orderCost.getORD_QTY());
        holder.tv_ORD_WEIGHT.setText(orderCost.getORD_WEIGHT());
        holder.tv_ORD_VOLUME.setText(orderCost.getORD_VOLUME());

    }

    @Override
    public int getItemCount() {
        return orderCosts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView   tv_ORD_NO,tv_ORD_NO_CLIENT,tv_ORD_FROM_NAMES,tv_ORD_TO_NAME,tv_CHARGE_AMOUNT,tv_TRANSPORT_FEES,
                tv_DROPPOINT_FEES,tv_LOAD_FEES,tv_SITE_SURCHARGE,tv_FUEL_SURCHARGE,tv_RETURN_FEES, tv_DELIVER_FEES,
                tv_PRESS_NIGHT,tv_OTHER_FEES,tv_AMOUNT_PRICE,tv_FEESCOUNT,tv_ORD_QTY,tv_ORD_WEIGHT,tv_ORD_VOLUME;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_ORD_NO= (TextView) itemView.findViewById(R.id.tv_ORD_NO);
            tv_ORD_NO_CLIENT= (TextView) itemView.findViewById(R.id.tv_ORD_NO_CLIENT);
            tv_ORD_FROM_NAMES= (TextView) itemView.findViewById(R.id.tv_ORD_FROM_NAMES);
            tv_ORD_TO_NAME= (TextView) itemView.findViewById(R.id.tv_ORD_TO_NAME);
            tv_AMOUNT_PRICE= (TextView) itemView.findViewById(R.id.tv_AMOUNT_PRICE);
            tv_CHARGE_AMOUNT= (TextView) itemView.findViewById(R.id.tv_CHARGE_AMOUNT);
            tv_ORD_QTY= (TextView) itemView.findViewById(R.id.tv_ORD_QTY);
            tv_TRANSPORT_FEES= (TextView) itemView.findViewById(R.id.tv_TRANSPORT_FEES);
            tv_ORD_WEIGHT= (TextView) itemView.findViewById(R.id.tv_ORD_WEIGHT);
            tv_DROPPOINT_FEES= (TextView) itemView.findViewById(R.id.tv_DROPPOINT_FEES);
            tv_ORD_VOLUME= (TextView) itemView.findViewById(R.id.tv_ORD_VOLUME);
            tv_RETURN_FEES= (TextView) itemView.findViewById(R.id.tv_RETURN_FEES);
            tv_DELIVER_FEES= (TextView) itemView.findViewById(R.id.tv_DELIVER_FEES);
            tv_PRESS_NIGHT= (TextView) itemView.findViewById(R.id.tv_PRESS_NIGHT);
            tv_FUEL_SURCHARGE= (TextView) itemView.findViewById(R.id.tv_FUEL_SURCHARGE);
            tv_LOAD_FEES= (TextView) itemView.findViewById(R.id.tv_LOAD_FEES);
            tv_SITE_SURCHARGE= (TextView) itemView.findViewById(R.id.tv_SITE_SURCHARGE);
            tv_OTHER_FEES= (TextView) itemView.findViewById(R.id.tv_OTHER_FEES);
            tv_FEESCOUNT= (TextView) itemView.findViewById(R.id.tv_FEESCOUNT);

        }
    }
}
