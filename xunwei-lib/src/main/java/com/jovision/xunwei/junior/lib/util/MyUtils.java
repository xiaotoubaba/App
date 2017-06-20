package com.jovision.xunwei.junior.lib.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyUtils {

	/**
	 * This method is used to get real path of file from from uri<br/>
	 * http://stackoverflow.com/questions/11591825/how-to-get-image-path-just-
	 * captured-from-camera
	 * 
	 * @param contentUri
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	public static String getRealPathFromURI(Activity activity, Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			// Do not call Cursor.close() on a cursor obtained using this
			// method,
			// because the activity will do that for you at the appropriate time
			Cursor cursor = activity.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	@SuppressLint("NewApi")
	public static int getGridViewColumnWidth(GridView gridview) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			return gridview.getColumnWidth();
		else {
			try {
				Field field = GridView.class.getDeclaredField("mColumnWidth");
				field.setAccessible(true);
				Integer value = (Integer) field.get(gridview);
				field.setAccessible(false);
				return value.intValue();
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static Rect getViewAbsoluteLocation(View view){  
        if(view == null){  
            return new Rect();  
        }  
        // 获取View相对于屏幕的坐标  
        int[] location = new int[2] ;  
        view.getLocationInWindow(location);//这是获取相对于屏幕的绝对坐标，而view.getLocationInWindow(location); 是获取window上的相对坐标，本例中只有一个window，二者等价  
        // 获取View的宽高  
        int width = view.getMeasuredWidth();  
        int height = view.getMeasuredHeight();  
        // 获取View的Rect  
        Rect rect = new Rect();  
        rect.left = location[0];  
        rect.top = location[1];  
        rect.right = rect.left + width;  
        rect.bottom = rect.top + height;  
        return rect;  
    }

	public static int getStatusBarHeight(Context context){
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	public static int getActivityStatusBarHeight(Activity context){
		Rect frame = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	public static int ip2int(String ip) {
		int result = 0;

		try {
			byte[] bytes = InetAddress.getByName(ip).getAddress();

			int size = bytes.length;
			result = bytes[0] & 0xFF;
			for (int i = 1; i < size; i++) {
				result <<= 8;
				result += bytes[i] & 0xFF;
			}
		} catch (UnknownHostException e) {
			// [Neo] Empty
		}

		return result;
	}

}
