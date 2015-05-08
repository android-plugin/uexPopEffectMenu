package org.zywx.wbpalmstar.widgetone.uexpopeffectmenu;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.zywx.wbpalmstar.base.BUtility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class EPopEffectMenuUtils {
	public static final String SCRIPT_HEADER = "javascript:";
	public static final String F_CALLBACK_NAME_ONITEMCLICK = "uexPopEffectMenu.onItemClick";
	
	public static final int MENU_MSG_CODE_SETDATA = 0;
	public static final int MENU_MSG_CODE_OPEN = 1;
	public static final int MENU_MSG_CODE_CLOSE = 2;
	
	public static final String MENU_PARAMS_KEY_FUNCTION = "function";
	public static final String MENU_PARAMS_KEY_ACTIVITYID = "activityId";
	public static final String MENU_PARAMS_KEY_MGR = "mgr";
	
	public static final Integer MENU_PARAMS_TYPE_TUMBLR = 2;
	public static final Integer MENU_PARAMS_TYPE_NOMAL = 1;
	
	public static final String MENU_PARAMS_KEY_X = "x";
	public static final String MENU_PARAMS_KEY_Y = "y";
	public static final String MENU_PARAMS_KEY_W = "w";
	public static final String MENU_PARAMS_KEY_H = "h";
	
	public static final String MENU_PARAMS_KEY_OBJ = "obj";
	public static final String MENU_PARAMS_KEY_RES = "res";
	public static final String MENU_PARAMS_KEY_TYPE = "type";
	public static final String MENU_PARAMS_KEY_DENSITY = "density";
	public static final String MENU_PARAMS_KEY_BGCOLOR = "bgColor";	
	
	public static final String MENU_PARAMS_KEY_POPMENUITEMS = "popMenuItems";
	public static final String MENU_PARAMS_KEY_POPMENUITEMS_TEXT = "text";
	public static final String MENU_PARAMS_KEY_POPMENUITEMS_IMG = "img";
	public static final String MENU_PARAMS_KEY_POPMENUITEMS_TEXTCOLOR = "textColor";
	
	public static Bitmap getImage(Context ctx, String imgUrl) {
		if (imgUrl == null || imgUrl.length() == 0) {
			return null;
		}
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			if (imgUrl.startsWith(BUtility.F_Widget_RES_SCHEMA)) {
				is = BUtility.getInputStreamByResPath(ctx, imgUrl);
				bitmap = BitmapFactory.decodeStream(is);
			} else if (imgUrl.startsWith(BUtility.F_FILE_SCHEMA)) {
				imgUrl = imgUrl.replace(BUtility.F_FILE_SCHEMA, "");
				bitmap = BitmapFactory.decodeFile(imgUrl);
			} else if (imgUrl.startsWith(BUtility.F_Widget_RES_path)) {
				try {
					is = ctx.getAssets().open(imgUrl);
					if (is != null) {
						bitmap = BitmapFactory.decodeStream(is);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				bitmap = BitmapFactory.decodeFile(imgUrl);
			}

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}
	
	public static void getGridAnimation(GridView gv, float from, float to, float f, float t, Interpolator interpolator, final EUExPopEffectMenu euexPopEffectMenu, final View view, final String js) {
		for (int i = 0; i < gv.getCount(); i++) {
			AnimationSet set = new AnimationSet(true);
			TranslateAnimation animation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f,
					Animation.RELATIVE_TO_PARENT, from, Animation.RELATIVE_TO_PARENT, to);
			AlphaAnimation alphaAnimation = new AlphaAnimation(f, t);
			if(from < 0) {
				alphaAnimation.setDuration((gv.getCount() / 3 + 1) * 100 - 100 * (i / 3));
				animation.setDuration((gv.getCount() / 3 + 1) * 100 - 100 * (i / 3));
			}else {
				alphaAnimation.setDuration((gv.getCount() / 3 + 1) * 200 - 200 * (i / 3));
				animation.setDuration((gv.getCount() / 3 + 1) * 200 - 200 * (i / 3));
			}
			animation.setInterpolator(interpolator);
			alphaAnimation.setFillAfter(true);
			animation.setFillAfter(true);
			set.addAnimation(alphaAnimation);
			set.addAnimation(animation);
			set.setFillAfter(true);
			
			gv.getChildAt(i).startAnimation(set);
			
			if(i == (gv.getCount() - 1)) {
				set.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						if(euexPopEffectMenu != null && view != null) {
							euexPopEffectMenu.removeViewFromCurrentWindow(view);
							euexPopEffectMenu.callBack(js);
						}
					}
				});
			}
		}
	}

	public static void getTumblrAnimation(List<View> lls, float f, float t, Interpolator interpolator, final EUExPopEffectMenu euexPopEffectMenu, final View view, final String js) {
		for (int i = 0; i < lls.size(); i++) {
			TranslateAnimation animation = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f,
					Animation.RELATIVE_TO_PARENT, f, Animation.RELATIVE_TO_PARENT, t); 
			animation.setDuration(500);
			animation.setFillAfter(true);
			animation.setInterpolator(interpolator);
			if(view != null) {
				if(i == 0) {
					animation.setStartOffset(100);
				}else {
					animation.setStartOffset(i * 50);
				}
			}else {
				if(i == 0) {
					animation.setStartOffset((lls.size() - 1) * 50);
				}else {
					animation.setStartOffset((i - 1) * 50);
				}
			}
			lls.get(i).startAnimation(animation);
			if(i == (lls.size() - 1)) {
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						if(euexPopEffectMenu != null && view != null) {
							euexPopEffectMenu.removeViewFromCurrentWindow(view);
							euexPopEffectMenu.callBack(js);
						}
					}
				});
			}
		}
	}

	public static void calculateXY(final RelativeLayout v, final List<View> lls, final List<Point> dxy, final Interpolator accelerate) {
		
		ViewTreeObserver observer = v.getViewTreeObserver();
		observer.addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				v.getViewTreeObserver().removeOnPreDrawListener(this);
				int width = v.getMeasuredWidth();
				int height = v.getMeasuredHeight();
				List<Point> xy = calculate(width, height, lls.size());
				for (int i = 0; i < lls.size(); i++) {
					LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
					lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
					lp.leftMargin = xy.get(i).getX() - dxy.get(i).getX() / 2;
					lp.topMargin = xy.get(i).getY() - dxy.get(i).getY() / 2;
					v.addView(lls.get(i), lp);
				}
				getTumblrAnimation(lls, 1.0f, 0f, accelerate, null ,null, null);
				return true;
			}
		});
	}

	private static List<Point> calculate(int width, int height, int size) {
		List<Point> xy = new ArrayList<Point>();
		int radius = Math.round(width / 3);
		int standWidth = Math.round(width / 2);
		int standHeight = Math.round(height / 2);
		int wid = 0, heig = 0;
		int length = size - 1;
		float degree = 360 / length;
		float deg = 0;
		for (int i = 0; i < size; i++) {
			if(i == 0) {
				wid = standWidth;
				heig = standHeight;
			}
			else if(i == 1) {
				wid = standWidth;
				heig = standHeight - radius;
			}
			else {
				deg = degree * (i - 1);
				if(deg < 360) {
					wid = (int)(standWidth - Math.abs(radius * Math.sin(deg2radio(360 - deg))));
					heig = (int)(standHeight - Math.abs(radius * Math.cos(deg2radio(360 - deg))));
				}
				if(deg < 270) {
					wid = (int)(standWidth - Math.abs(radius * Math.cos(deg2radio(270 - deg))));
					heig = (int)(standHeight + Math.abs(radius * Math.sin(deg2radio(270 - deg))));
				}
				if(deg < 180) {
					wid = (int)(standWidth + Math.abs(radius * Math.sin(deg2radio(180 - deg))));
					heig = (int)(standHeight + Math.abs(radius * Math.cos(deg2radio(180 - deg))));
				}
				if(deg < 90) {
					wid = (int)(standWidth + Math.abs(radius * Math.cos(deg2radio(90 - deg))));
					heig = (int)(standHeight - Math.abs(radius * Math.sin(deg2radio(90 - deg))));
				}
			}
			xy.add(new Point(wid, heig));
		}
		return xy;
	}
	
	private static double deg2radio(double deg) {
		return deg * Math.PI / 180;
	}

	public static void measureGridView(final GridView gv, final Interpolator decelerate) {
		ViewTreeObserver observer = gv.getViewTreeObserver();
		observer.addOnPreDrawListener(new OnPreDrawListener() {
			
			@Override
			public boolean onPreDraw() {
				gv.getViewTreeObserver().removeOnPreDrawListener(this);
				getGridAnimation(gv, -1f, 0f, 0f, 1.0f, decelerate, null, null, null);
				return true;
			}
		});
	}
}
