package com.water.roll.util;

public class Contants {
	
	public static final String RESPONSE_ENCODING = "GBK";//TODO BaseRequestSender
	public static final String FROM_ACTIVATE = "activate";
	public static final String PHONE_ID_PREFIX = "android:";
	public static final String PASSWORD_SALT = "r4QD4vG9";
	
	public static class ThirdPartKey{
		
		//mob发短信验证码
		public static class Mob{
			public static final String APP_KEY = "1a5d95df5fe60";
			public static final String APP_SECRET = "4f7b65ccacc3bef01fc926fd4578cc1e";
		}

		//极光推送
		public static class JPush{
			// 此处无效，在配置文件中
			public static final String APP_KEY = "9ea32a4fa15fa3a824f8491f";
			public static final String MASTER_SECRET = "70074d6ed190bea10eae28e7";
		}
	}
	
	public static class BundleKey{
		public static String MOBILE = "mobile";
		public static String PASSWD = "passwd";
		public static String FROM = "from";
		public static String PARAM = "__param__";
		public static String NOTICE = "notice";
		public static String CATEGORY_ID = "categary_id";
		public static String DEVICE_INFO = "device_info";
		public static String LAND_INFO = "land_info";
		public static String SENSOR_ID = "sensor_id";
		public static String LAND_LIST = "land_list";
		public static String PLAN_LIST = "plan_list";
		public static String LIVE_INFO = "live_info";
		public static String RTMP_URL = "rtmp_url";
		public static String LAND_PLAN_INFO = "land_plan_info";
		public static String PLANT_PLAN_INFO = "plant_plan_info";
		public static String PLANT_LIST_INFO = "plant_list_info";
		public static String FARMWORK_INFO = "farmwork_info";
		public static String POSITION = "position";
		public static String FARM_PICS = "farm_pics";
	}

	public static class RecordPlayAction{//1:暂停，2:继续播放，4:停止，5:seek
		public static final int PAUSE = 1;
		public static final int RESTART = 2;
		public static final int STOP = 4;
		public static final int SEEK = 5;
	}

//	1	temp	温度
//	2	humidity	湿度
//	3	soilTemp	土壤温度
//	4	soilHumidity	土壤湿度
//	5	carbonDioxide	二氧化碳
//	6	klux	光照强度
//	7	ph	ph
//	8	precipitation	降雨量

	public static class SenSorID{
		public static final int temp = 1;
		public static final int humidity = 2;
		public static final int soilTemp = 3;
		public static final int soilHumidity = 4;
		public static final int carbonDioxide = 4;
		public static final int klux = 4;
		public static final int ph = 4;
		public static final int precipitation = 4;
	}

	public static class ParamKey{
		public static final String SESSION = "SESSION";
	}
	
	public static class OssPath{
		public static final String AVATAR_PATH = "avatar";
	}

	public static class VideoType{
		public static final String LIVE = "live";
		public static final String RECORD = "record";
	}


	/**
	 * 用于startActivityForResult()
	 * */
	public static class RequestCode{
		/** 登录 */
		public static final int REQUEST_LOGIN = 1001;
		/** 获取头像 */
	    public static final int PHOTO_REQUEST_AVATAR = 1002;
		public static final int 返回值拍照照片保存照片 = 1008;
		public static final int 录音权限 = 1012;
		public static final int 扫描返回 = 1011;
		public static final int 返回值位置 = 1009;
		public static final int 存储权限 = 1010;
		public static final int 电话权限 = 1011;
		public static final int 文件列表 = 1013;
		public static final int 农事类型返回值 = 1111;
		/**
		 * 选择图片
		 */
		public static final int REQUEST_PHOTO_PICKER = 1003;
		public static final int REQUEST_PHOTO_PICKBACK = 0x011;
		public static final int REQUEST_PHOTO_PICKERREQ = 0x012;
	}


	public static class AreaType {
		/**
		 * 区域
		 */
		public static final int 区域 = 0;

		/**
		 * 地块
		 */
		public static final int 地块 = 1;

	}

		/**
	 * 角色权限
	 */
	public static class RoleType{
		/** 市级管理人员 */
		public static final int 市级管理人员 = 2;

		/** 县级管理人员*/
		public static final int 县级管理人员 = 3;

		/**乡镇管理人员*/
		public static final int 乡镇管理人员 = 4;

		/**村管理人员*/
		public static final int 村管理人员 = 5;

		/**片区管理人员*/
		public static final int 片区管理人员 = 6;
	}

	/**
	 * 工作状态
	 */
	public static class WorkStatus{
		/** 没有上传 */
		public static final String 未提交 = "0";

		/** 待审核*/
		public static final String 待审核 = "1";

		/**已审核*/
		public static final String 已审核 = "3";

	}


	public static final String PARTICIPANTS_KEY = "participant_selected";
	public static final String PARTICIPANTS_ID_KEY = "participant_selected_id";



	public static final double clear阈值 = 0.8;
	public static final double in_bound阈值 = 0.8;
	public static final double is_idcard阈值 = 0.5;

	public static final String megvii_url =  "https://api.megvii.com/cardpp/v1/ocridcard";

	public static final int WORK_REPORT_LEVEL_1 = 0;

	public static final String[][] MIME_MapTable={
			//{后缀名，MIME类型}
			{".3gp", "video/3gpp"},
			{".apk", "application/vnd.android.package-archive"},
			{".asf", "video/x-ms-asf"},
			{".avi", "video/x-msvideo"},
			{".bin", "application/octet-stream"},
			{".bmp", "image/bmp"},
			{".c", "text/plain"},
			{".class", "application/octet-stream"},
			{".conf", "text/plain"},
			{".cpp", "text/plain"},
			{".doc", "application/msword"},
			{".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
			{".xls", "application/vnd.ms-excel"},
			{".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
			{".exe", "application/octet-stream"},
			{".gif", "image/gif"},
			{".gtar", "application/x-gtar"},
			{".gz", "application/x-gzip"},
			{".h", "text/plain"},
			{".htm", "text/html"},
			{".html", "text/html"},
			{".jar", "application/java-archive"},
			{".java", "text/plain"},
			{".jpeg", "image/jpeg"},
			{".jpg", "image/jpeg"},
			{".js", "application/x-javascript"},
			{".log", "text/plain"},
			{".m3u", "audio/x-mpegurl"},
			{".m4a", "audio/mp4a-latm"},
			{".m4b", "audio/mp4a-latm"},
			{".m4p", "audio/mp4a-latm"},
			{".m4u", "video/vnd.mpegurl"},
			{".m4v", "video/x-m4v"},
			{".mov", "video/quicktime"},
			{".mp2", "audio/x-mpeg"},
			{".mp3", "audio/x-mpeg"},
			{".mp4", "video/mp4"},
			{".mpc", "application/vnd.mpohun.certificate"},
			{".mpe", "video/mpeg"},
			{".mpeg", "video/mpeg"},
			{".mpg", "video/mpeg"},
			{".mpg4", "video/mp4"},
			{".mpga", "audio/mpeg"},
			{".msg", "application/vnd.ms-outlook"},
			{".ogg", "audio/ogg"},
			{".pdf", "application/pdf"},
			{".png", "image/png"},
			{".pps", "application/vnd.ms-powerpoint"},
			{".ppt", "application/vnd.ms-powerpoint"},
			{".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
			{".prop", "text/plain"},
			{".rc", "text/plain"},
			{".rmvb", "audio/x-pn-realaudio"},
			{".rtf", "application/rtf"},
			{".sh", "text/plain"},
			{".tar", "application/x-tar"},
			{".tgz", "application/x-compressed"},
			{".txt", "text/plain"},
			{".wav", "audio/x-wav"},
			{".wma", "audio/x-ms-wma"},
			{".wmv", "audio/x-ms-wmv"},
			{".wps", "application/vnd.ms-works"},
			{".xml", "text/plain"},
			{".z", "application/x-compress"},
			{".zip", "application/x-zip-compressed"},
			{"", "*/*"}
	};
}
