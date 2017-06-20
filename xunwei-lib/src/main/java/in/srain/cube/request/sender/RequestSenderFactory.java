package in.srain.cube.request.sender;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import in.srain.cube.Cube;
import in.srain.cube.request.FailData;
import in.srain.cube.request.IRequest;
import in.srain.cube.request.RequestData;

public class RequestSenderFactory {

    public static <T> BaseRequestSender create(IRequest<T> request) {
        BaseRequestSender sender = null;
        try {
            RequestData requestData = request.getRequestData();
            URL url = new URL(requestData.getRequestUrl());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            int timeout = 0;
            if (!requestData.shouldPost()) {
                sender = new GetRequestSender(request, urlConnection);
                timeout = Cube.getInstance().getTimeout();
            } else {
                if (requestData.isMultiPart()) {
                    sender = new MultiPartRequestSender(request, urlConnection);
                    timeout = Cube.getInstance().getUploadTimeout();
                } else {
                    sender = new PostRequestSender(request, urlConnection);
                    timeout =  Cube.getInstance().getTimeout();
                }
            }
            //设置超时间，尽快释放连接
            urlConnection.setReadTimeout(timeout);
            urlConnection.setConnectTimeout(timeout);
            
            sender.setup();

        } catch (MalformedURLException e) {
            request.setFailData(FailData.inputError(request));
        } catch (IOException e) {
            request.setFailData(FailData.networkError(request));
        }
        return sender;
    }
}
