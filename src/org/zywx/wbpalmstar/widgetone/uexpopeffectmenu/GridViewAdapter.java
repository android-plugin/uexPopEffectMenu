package org.zywx.wbpalmstar.widgetone.uexpopeffectmenu;

import java.util.List;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<Bean> res;

	public GridViewAdapter(Context context, List<Bean> res) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.res = res;
	}
	
	@Override
	public int getCount() {
		return res.size();
	}

	@Override
	public Object getItem(int position) {
		return res.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(EUExUtil.getResLayoutID("plugin_popeffectmenu_gridview_item"), null);
			holder.iv = (ImageView) convertView.findViewById(EUExUtil.getResIdID("plugin_gridview_item_iv"));
			holder.tv = (TextView) convertView.findViewById(EUExUtil.getResIdID("plugin_gridview_item_tv"));
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.iv.setImageBitmap(EPopEffectMenuUtils.getImage(context, res.get(position).getUrl()));
		holder.tv.setText(res.get(position).getText());
		holder.tv.setTextColor(Color.parseColor(res.get(position).getTextColor()));
		return convertView;
	}
	
	public class ViewHolder {
		public ImageView iv;
		public TextView tv;
	}

}
