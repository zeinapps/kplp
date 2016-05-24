package zein.apps.kplp;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;
 
public class ServiceHandler {
 
    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    String message_error="";
    public ServiceHandler() {
 
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }
    
    public String getMessageError(){
    	return this.message_error;
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method,
            List<NameValuePair> params) {
        try {
        	//set connection timeout
        	HttpParams httpParameters = new BasicHttpParams();
        	  int timeoutConnection = 5000;
        	  HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        	  int timeoutSocket = 20000;
        	  HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        	
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
             
            // Checking http request method type
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpPost.setHeader("Content-type", "application/json");
    			httpPost.setHeader("Accept", "application/json");
                httpResponse = httpClient.execute(httpPost);
 
            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                Log.d("URL", url);
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Accept", "application/json");
                httpResponse = httpClient.execute(httpGet);
 
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("Exception zein request", e.toString());
            message_error =  e.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.d("Exception zein request", e.toString());
            message_error =  e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Exception zein request", e.toString());
            message_error =  e.toString();
        }
         
        return response;
 
    }
}