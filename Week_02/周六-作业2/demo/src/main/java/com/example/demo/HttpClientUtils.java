package com.example.demo;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HttpClientUtils {
    private static volatile HttpClientUtils httpClientUtils = null;

    private HttpClientUtils(){
    }
    public static synchronized HttpClientUtils getInstance(){
        if(httpClientUtils == null){
            synchronized (HttpClientUtils.class){
                if(httpClientUtils == null){
                    httpClientUtils = new HttpClientUtils();
                }
            }
        }
        return httpClientUtils;
    }

    /**
     * get请求
     * @param url
     * @param params
     * @return
     */
    public String doGet(String url, Map<String,String> params){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        try {
            URIBuilder builder = new URIBuilder(url);
            if(null!=params){
                for (String key:params.keySet()){
                    builder.setParameter(key,params.get(key));
                }
            }
            HttpGet httpGet = new HttpGet(builder.build());
             response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity,"utf-8");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * post请求
     * @param url
     * @param params
     * @return
     */
    public String doPost(String url,Map<String,String> params){
        CloseableHttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        String result = "";
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramaters = new ArrayList<>();
        if(null!=params){
            for (String key:params.keySet()){
                paramaters.add(new BasicNameValuePair(key,params.get(key)));
            }
        }
        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramaters);
            httpPost.setEntity(formEntity);
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200){
                result =EntityUtils.toString(response.getEntity(),"utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
