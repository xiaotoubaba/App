package in.srain.cube.request;

import in.srain.cube.util.CLog;


public class CubeError{
	
	private static final String TAG = CubeError.class.getSimpleName();
	
	/**请求超时*/
	public static final int ERROR_CODE_TIMEOUT = 1000;
	/**请求URL格式错误*/
	public static final int ERROR_CODE_URL_ERROR = 1001;
	/**无网络*/
	public static final int ERROR_CODE_NO_NETWORK = 1002;
	/**session失效*/
	public static final int	ERROR_CODE_SESSION_INVALID = 1003;
	/**服务端异常*/
	public static final int ERROR_CODE_SERVER_ERROR = 1004;
	/**服务端返回数据格式错误*/
	public static final int ERROR_CODE_RESPONSE_FORMAT_ERROR = 1005;
	/**未知错误*/
	public static final int ERROR_CODE_UNKNOWN_ERROR = 1006;
	
	public int errcode;
	public String errmsg;

	public CubeError(int code){
		this(code, getMsgFromCode(code));
	}
	
	public CubeError(int code, String msg){
		this.errcode = code;
		this.errmsg = msg;
	}

	public static String getMsgFromCode(String errCode) {
		int code = -1;
		try{
			code = Integer.parseInt(errCode);
		}catch(Exception e){
			CLog.e(TAG, e.getMessage(), e);
		}
		return getMsgFromCode(code);
	}

	public static String getMsgFromCode(int errCode){
		String msg = "";
		switch(errCode){
		case CubeError.ERROR_CODE_NO_NETWORK:
			msg = "客户端没有连网";
			break;
		case CubeError.ERROR_CODE_RESPONSE_FORMAT_ERROR:
			msg = "服务端返回数据格式不正确";
			break;
		case CubeError.ERROR_CODE_SERVER_ERROR:
			msg = "网络或者服务端处理异常";
			break;
		case CubeError.ERROR_CODE_TIMEOUT:
			msg = "请求超时";
			break;
		case CubeError.ERROR_CODE_URL_ERROR:
			msg = "请求URL有误";
			break;
		case CubeError.ERROR_CODE_UNKNOWN_ERROR:
			msg = "未知错误";
		}
		return msg;
	}
	
	public int getErrcode() {
		return errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	@Override
	public String toString() {
		return "Error [errcode=" + errcode + ", errmsg=" + errmsg + "]";
	}
	
}