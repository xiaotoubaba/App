package in.srain.cube.request;

import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import in.srain.cube.concurrent.SimpleExecutor;
import in.srain.cube.concurrent.SimpleTask;
import in.srain.cube.request.sender.BaseRequestSender;
import in.srain.cube.request.sender.RequestSenderFactory;
import in.srain.cube.util.CLog;
import in.srain.cube.util.CubeDebug;


/**
 * @author http://www.liaohuqiu.net
 */
public class DefaultRequestProxy implements IRequestProxy {

    private static final boolean DEBUG = CubeDebug.DEBUG_REQUEST;
    private static final String LOG_TAG = CubeDebug.DEBUG_REQUEST_LOG_TAG;

    private static IRequestProxy sInstance;
    private SimpleExecutor mSimpleExecutor;

    protected DefaultRequestProxy() {
        mSimpleExecutor = SimpleExecutor.create("cube-request-", 2, 4);
    }

    public static IRequestProxy getInstance() {
        if (sInstance == null) {
            sInstance = new DefaultRequestProxy();
        }
        return sInstance;
    }

    public static <T> T doSyncRequest(IRequest<T> request) {
        T data = null;
        try {
            StringBuilder sb = new StringBuilder();
            RequestData requestData = request.getRequestData();
            if (DEBUG) {
                CLog.d(LOG_TAG, "%s", requestData);
            }
            BaseRequestSender requestSender = RequestSenderFactory.create(request);
            if (requestSender != null) {
                requestSender.send();
                requestSender.getResponse(sb);
                if (DEBUG) {
                    CLog.d(LOG_TAG, "%s: %s", requestData, sb.toString());
                }
                Log.d("junior-parent","origin resposne:" + sb.toString());
                data = request.onDataFromServer(sb.toString());
            }
        } catch (ConnectException e) {
            String message = e.getMessage();
            // 网络不通
            if (message != null && message.indexOf("Network is unreachable") >= 0) {
                FailData faildata = FailData.networkError(request);
                faildata.mData = CubeError.ERROR_CODE_NO_NETWORK;
                request.setFailData(faildata);
            } else {
                request.setFailData(FailData.networkError(request));
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            FailData faildata = FailData.networkError(request);
            faildata.mData = CubeError.ERROR_CODE_TIMEOUT;
            request.setFailData(faildata);
        } catch (Exception e) {
            e.printStackTrace();
            request.setFailData(FailData.networkError(request));
        }
        return data;
    }

    @Override
    public <T> T requestSync(final IRequest<T> request) {
        final T data = doSyncRequest(request);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (data == null) {
                    request.onRequestFail(request.getFailData());
                } else {
                    request.onRequestSuccess(data);
                }
            }
        };

        SimpleTask.post(runnable);

        return data;
    }

    @Override
    public <T> void sendRequest(final IRequest<T> request) {
        DoRequestTask<T> doRequestTask = new DoRequestTask<T>(request);
        mSimpleExecutor.execute(doRequestTask);
    }

    @Override
    public void prepareRequest(RequestBase request) {

    }

    @Override
    public void onRequestFail(RequestBase request, FailData failData) {

    }

    @Override
    public JsonData processOriginDataFromServer(RequestBase request, JsonData data) {
        return data;
    }

    private static class DoRequestTask<T> extends SimpleTask {

        private T mData;
        private IRequest<T> mRequest;

        private DoRequestTask(IRequest<T> request) {
            mRequest = request;
        }

        @Override
        public void doInBackground() {
            mData = doSyncRequest(mRequest);
        }

        @Override
        public void onFinish(boolean canceled) {
            if (mData == null) {
                mRequest.onRequestFail(mRequest.getFailData());
            } else {
                mRequest.onRequestSuccess(mData);
            }
        }
    }
}
