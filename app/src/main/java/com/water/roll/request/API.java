
package com.water.roll.request;

/**
 * Web请求接口
 */
public final class API {
    //test
    public static final String API_HOST = "http://192.168.15.101:8080";
    //online
//    public static final String API_HOST ="http://syp.xunvision.com/hami-sy/rs";
//    public static final String API_HOST ="http://agri.xunvision.com/hami-sy/rs";
//    账号
    public static final String LOGIN_URL = API_HOST + "/account/login";
    public static final String MODIFY_PASSWORD_URL = API_HOST + "/account/changepwd.do";
    public static final String LOGOUT_URL = API_HOST + "/account/logout.do";
    public static final String GET_CHANGE_HEAD_IMAGE_URL = API_HOST + "/account/changephoto.do";
    public static final String GET_QINIU_TOKEN_URL = API_HOST + "/account/upload_token.do";


    public static final String GET_AREA_LIST = API_HOST + "/area/getAreaList.do";
    public static final String GET_AREA_INFO = API_HOST + "/area/getAreaInfo.do";

    public static final String GET_DEL_AREA_INFO = API_HOST + "/area/deleteArea.do";

    public static final String GET_OWNER_LIST = API_HOST + "/account/getAccountList.do";


    //    种植计划
    public static final String GET_PLAN_LIST_URL = API_HOST + "/plan/getplanlist.do";

    public static final String GET_ADD_PLAN_URL = API_HOST + "/plan/addplan.do";

    public static final String GET_ALL_PLAN_LIST_URL = API_HOST + "/plan/getAllPlanList.do";

    public static final String GET_PRODUCT_LIST = API_HOST + "/product/getProductType.do";

    public static final String GET_CYCLE_LIST_URL = API_HOST + "/plan/getplancycle.do";

    //    农事
    public static final String GET_PLANT_LIST_URL = API_HOST + "/farmwork/getFarmworkList.do";

    public static final String GET_FARMTYPE_LIST_URL = API_HOST + "/farmwork/getFarmworkTypeList.do";

    public static final String GET_ADD_FARM_URL = API_HOST + "/farmwork/addFarmwork.do";

    public static final String GET_UPDATE_FARM_URL = API_HOST + "/farmwork/changeFarmwork.do";

    public static final String GET_DEL_FARM_URL = API_HOST + "/farmwork/deleteFarmwork.do";

    public static final String GET_ADD_LAND_URL = API_HOST + "/area/addArea.do";

    public static final String GET_UPDATE_LAND_URL = API_HOST + "/area/modifyArea.do";

    public static final String GET_LIVE_LIST_URL = API_HOST + "/device/getDeviceList.do";
    public static final String GET_LIVE_URL = API_HOST + "/device/playVideo.do";
    public static final String GET_SENSOR_NEW_URL = API_HOST + "/environment/sensorNewData.do";
    public static final String GET_SENSOR_URL = API_HOST + "/environment/sensorData.do";

    public static final String GET_NOTICE_URL = API_HOST + "/notice/list.do";

    public static final String GET_NOTICE_URL2 = "http://120.27.111.162:8088/mlxc/rs" + "/notice/list.do";

    public static final String GET_NOTICE_DETAIL_URL2 = "http://120.27.111.162:8088/mlxc/rs" + "/notice/detail.do";

}
