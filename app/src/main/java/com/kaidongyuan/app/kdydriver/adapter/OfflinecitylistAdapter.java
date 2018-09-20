package com.kaidongyuan.app.kdydriver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.bean.baidumap.OfflineCitymap;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/20.
 */
public class OfflinecitylistAdapter extends BaseAdapter {
    ArrayList<OfflineCitymap> citymaps;
    Context mcontext;
    android.os.Handler handler;
    public OfflinecitylistAdapter(ArrayList<OfflineCitymap> citymaps, Context mcontext, android.os.Handler handler) {
        this.citymaps = citymaps;
        this.handler=handler;
        this.mcontext = mcontext;
    }

    public ArrayList<OfflineCitymap> getCitymaps() {
        return citymaps;
    }

    public void setCitymaps(ArrayList<OfflineCitymap> citymaps) {
        this.citymaps = citymaps;
    }

    @Override
    public int getCount() {
        return citymaps.size();
    }

    @Override
    public Object getItem(int position) {
        return citymaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        final OfflineCitymap citymap=citymaps.get(position);
        if (convertView==null){
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.item_offlinecitymap,parent,false);
            holder=new ViewHolder();
            holder.city_name= (TextView) convertView.findViewById(R.id.city_name);
            holder.city_currentprogress= (TextView) convertView.findViewById(R.id.tv_city_currentprogress);
            holder.progressBar_city= (ProgressBar) convertView.findViewById(R.id.progressBar_city);
            holder.progressBar_city.setBackgroundColor(Color.WHITE);
            holder.city_state= (ImageView) convertView.findViewById(R.id.iv_city_state);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.city_name.setText(citymap.getCityName());
        final ViewHolder finalHolder = holder;
        if (holder.city_state.getTag()==null) {
            holder.city_state.setTag(citymap.getCityName());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Message msg = new Message();
                switch (citymap.getFlag()) {
                    case PAUSE:
                        finalHolder.city_state.setImageResource(R.drawable.download_pause_bt);
                        bundle.putString("strkey", "downloading");
                        bundle.putParcelable("citymap", citymap);
                      //  bundle.putInt("intkey", citymap.getCityCode());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        break;
                    case DOWNLOADING:
                        finalHolder.city_state.setImageResource(R.drawable.download_start_bt);
                        bundle.putString("strkey", "pause");
                        bundle.putParcelable("citymap",citymap);
                     //   bundle.putInt("intkey", citymap.getCityCode());
                     //   bundle.putInt("positionkey",position);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        break;
                    case NO_STATUS:
                        finalHolder.city_state.setImageResource(R.drawable.download_pause_bt);
                        bundle.putString("strkey", "start");
                        bundle.putParcelable("citymap",citymap);
                       // bundle.putInt("intkey", citymap.getCityCode());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                        break;
                    default:
                        break;
                }
            }
        });
        holder.city_state.setImageResource(R.drawable.download_start_bt);
        holder.city_state.setClickable(true);
        String strprogress="";
        switch (citymap.getFlag()){
//            case PAUSE:
//               // strprogress="等待下载";
//               holder.city_state.setImageResource(R.drawable.download_start_bt);
//                break;
            case DOWNLOADING:
                //  strprogress="正在下载中";
                holder.city_state.setImageResource(R.drawable.download_pause_bt);
                break;
            default:
                break;
        }
        if (citymap.getProgress()==0){
            strprogress="未下载";
        }else if (citymap.getProgress()==100&&holder.city_name.getText().equals(citymap.getCityName())){
            strprogress="已下载";
            holder.city_state.setImageResource(R.drawable.download_finish_bt);
           holder.city_state.setClickable(false);
            citymap.setFlag(OfflineCitymap.CitymapFlag.FINISH);
        }else {
            strprogress="已下载"+citymap.getProgress()+"%";
        }
        holder.city_currentprogress.setText(strprogress);
        holder.progressBar_city.setProgress(citymap.getProgress());
        return convertView;
    }
    private class ViewHolder{
        TextView city_name;
        TextView city_currentprogress;
        ProgressBar progressBar_city;
        ImageView city_state;
    }
}
