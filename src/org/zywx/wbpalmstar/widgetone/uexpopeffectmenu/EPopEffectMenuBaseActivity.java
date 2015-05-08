package org.zywx.wbpalmstar.widgetone.uexpopeffectmenu;

import java.util.ArrayList;
import java.util.List;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EPopEffectMenuBaseActivity extends Activity implements OnItemClickListener, OnClickListener {

	private View v;
	private GridView gv;
	private GridViewAdapter gva;
	private RelativeLayout rl;
	private List<View> lls;
	private ArrayList<Bean> res;
	private List<Point> dxy;
	private Interpolator decelerate, accelerate;
	private int type;
	private String bgColor;
	private boolean isInitFlag = false;
	private EUExPopEffectMenu euexPopEffectMenu;
	private String activityId;
	private boolean bl = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		type = intent.getIntExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_TYPE, EPopEffectMenuUtils.MENU_PARAMS_TYPE_NOMAL);
		if(intent.hasExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_BGCOLOR)) {
			bgColor = intent.getStringExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_BGCOLOR);
		}
		euexPopEffectMenu = (EUExPopEffectMenu) intent.getSerializableExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_OBJ);
		res = intent.getParcelableArrayListExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_RES);
		activityId = intent.getStringExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_ACTIVITYID);
		LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
		v = layoutInflater.inflate(EUExUtil.getResLayoutID("plugin_popeffectmenu_main"), null);
		setContentView(v);
		initView(type, res.size());
		show();
	}
	
	private void show() {
		if(type == EPopEffectMenuUtils.MENU_PARAMS_TYPE_NOMAL) {
			if (!isInitFlag) {
				isInitFlag = true;
				gva = new GridViewAdapter(getApplicationContext(), res);
				gv.setAdapter(gva);
			}
			gv.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(gv.pointToPosition((int)event.getX(), (int)event.getY()) == GridView.INVALID_POSITION) {
						gv.setOnTouchListener(null);
						gv.setOnItemClickListener(null);
						dismiss(getWindow().getDecorView(), null);
						euexPopEffectMenu.destoryActivity(activityId);
						bl = true;
						return true;
					}
					return false;
				}
			});
			gv.setOnItemClickListener(this);
			EPopEffectMenuUtils.measureGridView(gv, decelerate);
		}else {
			rl = (RelativeLayout) v.findViewById(EUExUtil.getResIdID("plugin_popeffectmenu_rl"));
			if(bgColor != null) {
				rl.setBackgroundColor(Color.parseColor(bgColor));
			}
			rl.setVisibility(View.VISIBLE);
			rl.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					rl.setOnClickListener(null);
					for (int i = 0; i < lls.size(); i++) {
						lls.get(i).setOnClickListener(null);
					}
					dismiss(getWindow().getDecorView(), null);
					euexPopEffectMenu.destoryActivity(activityId);
				}
			});
			EPopEffectMenuUtils.calculateXY(rl, lls, dxy, accelerate);
		}
	}

	private void initView(int type, int count) {
		if(type == EPopEffectMenuUtils.MENU_PARAMS_TYPE_TUMBLR) {
			if(lls != null) {
				lls.clear();
				dxy.clear();
			}else {
				lls = new ArrayList<View>();
				dxy = new ArrayList<Point>();
			}
			for (int i = 0; i < count; i++) {
				View tabView = View.inflate(getApplicationContext(), EUExUtil.getResLayoutID("plugin_popeffectmenu_ll_item"), null);
				ImageView iv = (ImageView) tabView.findViewById(EUExUtil.getResIdID("plugin_ll_item_iv"));
				TextView tv = (TextView) tabView.findViewById(EUExUtil.getResIdID("plugin_ll_item_tv"));
				iv.setImageBitmap(EPopEffectMenuUtils.getImage(getApplicationContext(), res.get(i).getUrl()));
				tv.setText(res.get(i).getText());
				tv.setTextColor(BUtility.parseColor(res.get(i).getTextColor()));
				tabView.setId(i);
				tabView.setOnClickListener(this);
				tabView.measure(0, 0);
				dxy.add(new Point(tabView.getMeasuredWidth(), tabView.getMeasuredHeight()));
				lls.add(tabView);
			}
		}else {
			gv = (GridView) v.findViewById(EUExUtil.getResIdID("plugin_popeffectmenu_gv"));
			if(bgColor != null) {
				gv.setBackgroundColor(Color.parseColor(bgColor));
			}
			gv.setVisibility(View.VISIBLE);
			decelerate = new DecelerateInterpolator();
		}
		accelerate = new AccelerateInterpolator();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void dismiss(View view, String js) {
		if(type == EPopEffectMenuUtils.MENU_PARAMS_TYPE_TUMBLR) {
			EPopEffectMenuUtils.getTumblrAnimation(lls, 0f, -1.0f, accelerate, euexPopEffectMenu, view, js);
		}else {
			EPopEffectMenuUtils.getGridAnimation(gv, 0f, -1f, 1.0f, 0f, accelerate, euexPopEffectMenu, view, js);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(bl) {
			return;
		}
		gv.setOnItemClickListener(null);
		gv.setOnTouchListener(null);
		String js = EPopEffectMenuUtils.SCRIPT_HEADER + "if(" + EPopEffectMenuUtils.F_CALLBACK_NAME_ONITEMCLICK + "){" +EPopEffectMenuUtils.F_CALLBACK_NAME_ONITEMCLICK + "(" + position + ")}";
		dismiss(getWindow().getDecorView(), js);
		euexPopEffectMenu.destoryActivity(activityId);
	}

	@Override
	public void onClick(View view) {
		for (int i = 0; i < lls.size(); i++) {
			lls.get(i).setOnClickListener(null);
		}
		rl.setOnClickListener(null);
		String js = EPopEffectMenuUtils.SCRIPT_HEADER + "if(" + EPopEffectMenuUtils.F_CALLBACK_NAME_ONITEMCLICK + "){" +EPopEffectMenuUtils.F_CALLBACK_NAME_ONITEMCLICK + "(" + view.getId() + ")}";
		dismiss(getWindow().getDecorView(), js);
		euexPopEffectMenu.destoryActivity(activityId);
	}
}
