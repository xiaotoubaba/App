package com.water.roll;

import android.app.Application;

import com.jovision.xunwei.junior.lib.util.StorageUtil;

import java.io.File;

public class AppCacheManager {

	private AppCacheManager() {
	}

	public static File cache_base_dir;

	public static File cache_splash_dir;
	public static File cache_splash_file;
	public static String splash_file_name = "splash.jpg";

	public static File cache_update_apk_dir;
	public static File cache_update_apk_file;
	public static String update_apk_file_name = "baby365.apk";

	public static File cache_image_dir;

	public static File cache_request_dir;

	public static File image_capture_dir;

	public static File video_record_dir;

	public static File cache_file_dir;

	public static File cloud_video_dir;

	public static void init(Application context) {

		// 缓存的根目录
		cache_base_dir = StorageUtil.getCacheDirectory(context, true);

		// 启动页面
		cache_splash_dir = new File(cache_base_dir, "splash");
		if (!cache_splash_dir.exists()) {
			cache_splash_dir.mkdirs();
		}
		cache_splash_file = new File(cache_splash_dir, splash_file_name);

		// 下载更新
		cache_update_apk_dir = new File(cache_base_dir, "update_apk");
		if (!cache_update_apk_dir.exists()) {
			cache_update_apk_dir.mkdirs();
		}
		cache_update_apk_file = new File(cache_update_apk_dir, update_apk_file_name);

		// 图片缓存,universal-image-loader使用
		cache_image_dir = new File(cache_base_dir, "image");
		if (!cache_image_dir.exists()) {
			cache_image_dir.mkdirs();
		}

		// 视频截图保存路径
		image_capture_dir = new File(cache_base_dir, "capture");
		if (!image_capture_dir.exists()) {
			image_capture_dir.mkdirs();
		}

		// 视频保存路径
		video_record_dir = new File(cache_base_dir, "video");
		if (!video_record_dir.exists()) {
			video_record_dir.mkdirs();
		}

		//文件
		cache_file_dir = new File(cache_base_dir, "file");
		if (!cache_file_dir.exists()) {
			cache_file_dir.mkdirs();
		}

		//请求
		cache_request_dir = new File(cache_base_dir, "request");
		if (!cache_request_dir.exists()) {
			cache_request_dir.mkdirs();
		}

	}

}
