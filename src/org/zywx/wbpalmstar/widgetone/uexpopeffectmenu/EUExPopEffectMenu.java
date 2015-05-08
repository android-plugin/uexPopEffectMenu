package org.zywx.wbpalmstar.widgetone.uexpopeffectmenu;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

@SuppressWarnings({ "serial", "deprecation" })
public class EUExPopEffectMenu extends EUExBase implements Serializable {
	
	private ArrayList<Bean> res;
	private int type = EPopEffectMenuUtils.MENU_PARAMS_TYPE_NOMAL;
	private String bgColor;
	private LocalActivityManager mgr;

	public EUExPopEffectMenu(Context context, EBrowserView eBrowserView) {
		super(context, eBrowserView);
		mgr = ((ActivityGroup)mContext).getLocalActivityManager();
		res = new ArrayList<Bean>();
	}
	
	public void open(String[] params) {
		sendMessageInMenu(EPopEffectMenuUtils.MENU_MSG_CODE_OPEN, params);
	}
	
	private void sendMessageInMenu(int msgType, String[] params) {
		if(mHandler == null) {
			return;
		}
		Message msg = Message.obtain();
		msg.what = msgType;
		msg.obj = this;
		Bundle b = new Bundle();
		b.putStringArray(EPopEffectMenuUtils.MENU_PARAMS_KEY_FUNCTION, params);
		msg.setData(b);
		mHandler.sendMessage(msg);
	}
	
	
	@Override
	public void onHandleMessage(Message msg) {
		if(msg.what == EPopEffectMenuUtils.MENU_MSG_CODE_OPEN) {
			handleOpen(msg);
		}else {
			handleInMenu(msg);
		}
	}

	private void handleInMenu(Message msg) {
		String[] params = msg.getData().getStringArray(EPopEffectMenuUtils.MENU_PARAMS_KEY_FUNCTION);
		switch (msg.what) {
		case EPopEffectMenuUtils.MENU_MSG_CODE_CLOSE:
			handleClose(params);
			break;

		case EPopEffectMenuUtils.MENU_MSG_CODE_SETDATA:
			handleSetData(params);
			break;
		}
	}

	private void handleSetData(String[] params) {
		if(params != null && params.length == 1) {
			try {
				if(res != null) {
					res.clear();
				}
				JSONObject json = new JSONObject(params[0]);
				type = Integer.parseInt(json.getString(EPopEffectMenuUtils.MENU_PARAMS_KEY_TYPE));
				if(json.has(EPopEffectMenuUtils.MENU_PARAMS_KEY_BGCOLOR)) {
					bgColor = json.getString(EPopEffectMenuUtils.MENU_PARAMS_KEY_BGCOLOR);
				}
				JSONArray array = json.getJSONArray(EPopEffectMenuUtils.MENU_PARAMS_KEY_POPMENUITEMS);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String text = obj.getString(EPopEffectMenuUtils.MENU_PARAMS_KEY_POPMENUITEMS_TEXT);
					String img = obj.getString(EPopEffectMenuUtils.MENU_PARAMS_KEY_POPMENUITEMS_IMG);
					String textColor = obj.getString(EPopEffectMenuUtils.MENU_PARAMS_KEY_POPMENUITEMS_TEXTCOLOR);
					res.add(new Bean(text, img, textColor));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleClose(String[] params) {
		
	}

	private void handleOpen(Message msg) {
		String[] params = msg.getData().getStringArray(EPopEffectMenuUtils.MENU_PARAMS_KEY_FUNCTION);
		if(params == null || params.length != 1 || res.size() < 1) {
			return;
		}
		try {
			JSONObject json = new JSONObject(params[0]);
			float x = Float.parseFloat(json.getString(EPopEffectMenuUtils.MENU_PARAMS_KEY_X));
			float y = Float.parseFloat(json.getString(EPopEffectMenuUtils.MENU_PARAMS_KEY_Y));
			float w = Float.parseFloat(json.getString(EPopEffectMenuUtils.MENU_PARAMS_KEY_W));
			float h = Float.parseFloat(json.getString(EPopEffectMenuUtils.MENU_PARAMS_KEY_H));
			
			Intent intent = new Intent(mContext, EPopEffectMenuBaseActivity.class);
			intent.putExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_TYPE, type);
			if(bgColor != null) {
				intent.putExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_BGCOLOR, bgColor);
			}
			intent.putExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_OBJ, this);
			intent.putParcelableArrayListExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_RES, res);
			
			String activityId = EPopEffectMenuUtils.MENU_PARAMS_KEY_ACTIVITYID + EUExPopEffectMenu.this.hashCode();
			EPopEffectMenuBaseActivity activity = (EPopEffectMenuBaseActivity) mgr.getActivity(activityId);
			if(activity != null) {
				activity.dismiss(activity.getWindow().getDecorView(), null);
				destoryActivity(activityId);
			}else {
				intent.putExtra(EPopEffectMenuUtils.MENU_PARAMS_KEY_ACTIVITYID, activityId);
				Window window = mgr.startActivity(activityId, intent);
				View decorView = window.getDecorView();
				LayoutParams param = new LayoutParams((int)w, (int)h);
				param.topMargin = (int)x;
				param.leftMargin = (int)y;
				addView2CurrentWindow(decorView, param);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	
	}
	
	private void addView2CurrentWindow(View child, RelativeLayout.LayoutParams parms) {
		int l = (int) (parms.leftMargin);
		int t = (int) (parms.topMargin);
		int w = parms.width;
		int h = parms.height;
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
		lp.gravity = Gravity.NO_GRAVITY;
		lp.leftMargin = l;
		lp.topMargin = t;
		adptLayoutParams(parms, lp);
		mBrwView.addViewToCurrentWindow(child, lp);
	}
	
	public void callBack(String js) {
		onCallback(js);
	}
	
	public void destoryActivity(String activityId) {
		mgr.destroyActivity(activityId, true);
	}

	public void close(String[] params) {
		sendMessageInMenu(EPopEffectMenuUtils.MENU_MSG_CODE_CLOSE, params);
	}

	public void setItems(String[] params) {
		sendMessageInMenu(EPopEffectMenuUtils.MENU_MSG_CODE_SETDATA, params);
	}
	
	@Override
	protected boolean clean() {
		return false;
	}

}
