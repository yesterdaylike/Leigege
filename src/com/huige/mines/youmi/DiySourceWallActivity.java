package com.huige.mines.youmi;

import java.util.List;

import net.youmi.android.diy.AdObject;
import net.youmi.android.diy.DiyManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huige.mines.R;

public class DiySourceWallActivity extends Activity{

	ListView listView;
	List<AdObject> list;
	ImageView backBtn;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * 所需要资源
		 * drawable/btn_back.png
		 * drawable/btn_download_details.xml
		 * drawable/list_background.xml
		 * drawable/btn_download_list.xml
		 * drawable/btn_download_list_click.png
		 * drawable/btn_download_list_normal.png
		 * values/colors.xml
		 * */

		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

		setContentView(R.layout.activity_diysourcewall);
		listView = (ListView) findViewById(R.id.recommendList);
		backBtn = (ImageView) findViewById(R.id.backBtn);

		list = DiyManager.getAdList(this);

		RecommendAdapter recommendAdapter = new RecommendAdapter(this, list);
		listView.setAdapter(recommendAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				try {
					DiyManager.downloadAd(DiySourceWallActivity.this, i);
				} catch (Exception e) {
					// TODO: handle exception
				}
				/*Intent intent = new Intent();
                intent.putExtra("adId", i);
                intent.setClass(DiySourceWallActivity.this, DiyAppDetailActivity.class);
                startActivity(intent);*/
				DiyManager.downloadAd(DiySourceWallActivity.this, list.get(i).getAdId());
			}
		});

		backBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});

	}

	protected class RecommendAdapter extends BaseAdapter{

		Context context;
		List<AdObject> list;
		LayoutInflater mInflater;

		public RecommendAdapter(Context context , List<AdObject> arrayList) {
			this.context = context;
			this.list = arrayList;
			this.mInflater = getLayoutInflater();
		}

		public int getCount() {
			int count = 0;
			if(null != list){
				count = list.size();
			}
			return count;
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			final AdObject adObject = list.get(position);
			ViewHolder viewHolder = null;
			if(convertView==null){
				convertView =  mInflater.inflate(R.layout.list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.iconView = (ImageView) convertView.findViewById(R.id.icon);
				viewHolder.appNameView = (TextView) convertView.findViewById(R.id.appName);
				viewHolder.appDescriptionView = (TextView) convertView.findViewById(R.id.apptxt);
				//viewHolder.downloadBtn = (ImageView) convertView.findViewById(R.id.downloadBtn);
				viewHolder.tagView = (View) convertView.findViewById(R.id.tag);
				viewHolder.flagView = (View) convertView.findViewById(R.id.flag);
				convertView.setTag(viewHolder);

			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.iconView.setImageBitmap(adObject.getIcon());
			viewHolder.appNameView.setText(adObject.getAppName());
			viewHolder.appDescriptionView.setText(adObject.getAdText());

			if( position < 3 ){
				viewHolder.tagView.setBackgroundResource(R.drawable.content_icon_hot);
			}
			else if( position < 8 ){
				viewHolder.tagView.setBackgroundResource(R.drawable.content_icon_rec);
			}
			else{
				viewHolder.tagView.setBackgroundResource(android.R.color.transparent);
			}

			//viewHolder.flagView.setBackgroundResource(R.drawable.market_sequence_first);
			/*viewHolder.downloadBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    DiyManager.downloadAd(context, adObject.getAdId());
                }
            });*/
			return convertView;
		}

		class ViewHolder{
			ImageView iconView;
			TextView appNameView;
			TextView appDescriptionView;
			View tagView;
			View flagView;
			//ImageView downloadBtn;
		}
	}
}
