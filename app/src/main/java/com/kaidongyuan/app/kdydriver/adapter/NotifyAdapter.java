package com.kaidongyuan.app.kdydriver.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.kaidongyuan.app.basemodule.utils.nomalutils.DateUtil;
import com.kaidongyuan.app.basemodule.utils.nomalutils.DensityUtil;
import com.kaidongyuan.app.basemodule.utils.nomalutils.StringUtils;
import com.kaidongyuan.app.kdydriver.R;
import com.kaidongyuan.app.kdydriver.bean.order.Notify;
import com.kaidongyuan.app.kdydriver.utils.baidumapUtils.DataUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通知列表adapter
 */
public class NotifyAdapter extends BaseAdapter {

	private Context mContext;
	List<Notify> mNotifyList;
	private static final int TYPE_ITEM =0;  //普通Item View
	private static final int TYPE_FOOTER = 1;  //底部FootView
	public static final int LOADING_MORE=10; //上拉加载更多
	public static final int NO_MORE=11; //无更多数据
	public int loadState=10;//上拉加载状态值

	public NotifyAdapter(Context mContext) {
		this.mContext = mContext;
		this.mNotifyList = new ArrayList<>();
	}

	public void setData(List<Notify> notifyList){
		this.mNotifyList = notifyList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mNotifyList.size()+1;//预加上底部FootView
	}

	@Override
	public Object getItem(int position) {
		return mNotifyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		//Type种类：TYPE_FOOTER，TYPE_ITEM　
		return 2;
	}
	@Override
	public int getItemViewType(int position) {
		if (position+1==getCount()){
			return TYPE_FOOTER;
		}else {
			return TYPE_ITEM;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position)==TYPE_ITEM) {
			ViewHolder holder = null;
			final Notify notify = mNotifyList.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_notify, null);
				holder = new ViewHolder();
				holder.iv_notify = (ImageView) convertView.findViewById(R.id.iv_notify);
				holder.tv_notify_type = (TextView) convertView.findViewById(R.id.tv_notify_type);
				holder.tv_notify_title = (TextView) convertView.findViewById(R.id.tv_notify_title);
				holder.tv_notify_date = (TextView) convertView.findViewById(R.id.tv_notify_date);
				holder.iv_isRead= (ImageView) convertView.findViewById(R.id.iv_notify_isRead);
//			holder.ll_notify_context0= (LinearLayout) convertView.findViewById(R.id.ll_notify_context0);
//			holder.tv_notify_context0= (TextView) convertView.findViewById(R.id.tv_notify_context0);
//			holder.tv_notify_context00= (TextView) convertView.findViewById(R.id.tv_notify_context00);
//			holder.ll_notify_context1= (LinearLayout) convertView.findViewById(R.id.ll_notify_context1);
//			holder.tv_notify_context1= (TextView) convertView.findViewById(R.id.tv_notify_context1);
//			holder.tv_notify_context11= (TextView) convertView.findViewById(R.id.tv_notify_context11);
//			holder.ll_notify_context2= (LinearLayout) convertView.findViewById(R.id.ll_notify_context2);
//			holder.tv_notify_context2= (TextView) convertView.findViewById(R.id.tv_notify_context2);
//			holder.tv_notify_context22= (TextView) convertView.findViewById(R.id.tv_notify_context22);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (Integer.parseInt(notify.getTYPE().trim())==0){
				holder.iv_notify.setImageResource(R.drawable.icon_notify);
				holder.tv_notify_type.setText("装运消息：");
				holder.tv_notify_type.setTextColor(mContext.getResources().getColor(R.color.blue));
				holder.tv_notify_title.setText("" + notify.getMESSAGE());
			}else {
				holder.tv_notify_type.setText("公告：");
				holder.tv_notify_type.setTextColor(mContext.getResources().getColor(R.color.yb_yellow));
				holder.iv_notify.setImageResource(R.drawable.icon_notify1);
				holder.tv_notify_title.setText(""+notify.getMESSAGE());
			}
			if (Integer.parseInt(notify.getISREAD())>0){
				convertView.setAlpha(0.5f);
				holder.iv_isRead.setVisibility(View.GONE);
			}else {
				convertView.setAlpha(1f);
				holder.iv_isRead.setVisibility(View.VISIBLE);
			}
			Date date=DateUtil.getDateTime(StringUtils.StringToTime(notify.getADD_DATE()));
			holder.tv_notify_date.setText(StringUtils.formatDate(date,"yyyy/MM/dd HH:mm"));
//				.error(R.drawable.ic_hot_normal)
//				.override(DensityUtil.dip2px(40), DensityUtil.dip2px(40))
//				.diskCacheStrategy(DiskCacheStrategy.NONE)
//				.crossFade().centerCrop().into(holder.iv_notify);
		}else {
			MyFootViewHolder footholder=null;
			if (convertView==null){
				convertView=LayoutInflater.from(mContext).inflate(R.layout.more_item_orderlist,null);
				footholder=new MyFootViewHolder();
				footholder.moreProgressbar= (ProgressBar) convertView.findViewById(R.id.progressBar_more_item);
				footholder.moreTextview= (TextView) convertView.findViewById(R.id.tv_more_item);
				convertView.setTag(footholder);
			}else {
				footholder= (MyFootViewHolder) convertView.getTag();
			}
			switch (loadState){
				case LOADING_MORE:
					((MyFootViewHolder) footholder).moreProgressbar.setVisibility(View.VISIBLE);
					((MyFootViewHolder) footholder).moreTextview.setText("加载更多消息...");
					break;
				case NO_MORE:
					((MyFootViewHolder) footholder).moreProgressbar.setVisibility(View.GONE);
					((MyFootViewHolder) footholder).moreTextview.setText("无更多数据");
					break;
			}
		}
		return convertView;
	}
	class ViewHolder {
		TextView tv_notify_type,tv_notify_title,tv_notify_date,tv_notify_context0,tv_notify_context00,
				tv_notify_context1,tv_notify_context11,tv_notify_context2,tv_notify_context22;
		LinearLayout ll_notify_context0,ll_notify_context1,ll_notify_context2;
		ImageView iv_notify,iv_isRead;
	}
	class MyFootViewHolder   {
		ProgressBar moreProgressbar;
		TextView moreTextview;
//		public MyFootViewHolder(View itemView) {
//			super(itemView);
//			moreProgressbar= (ProgressBar) itemView.findViewById(R.id.progressBar_more_item);
//			moreTextview= (TextView) itemView.findViewById(R.id.tv_more_item);
//		}
	}
}
