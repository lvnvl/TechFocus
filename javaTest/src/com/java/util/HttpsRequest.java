package com.java.util;

import com.google.gson.Gson;
import com.java.util.IServiceRequest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;


public class HttpsRequest implements IServiceRequest {

    public interface ResultListener {


        public void onConnectionPoolTimeoutError();

    }

    private static Logger log = Logger.getLogger(HttpsRequest.class);

    //表示请求器是否已经做了初始化工作
    private boolean hasInit = false;

    //连接超时时间，默认10秒
    private int socketTimeout = 10000;

    //传输超时时间，默认30秒
    private int connectTimeout = 30000;

    //请求器的配置
    private RequestConfig requestConfig;

    //HTTP请求器
    private CloseableHttpClient httpClient;

    public HttpsRequest() throws UnrecoverableKeyException,
            KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, IOException {
        init();
    }

    private void init() throws IOException, KeyStoreException,
            UnrecoverableKeyException,
            NoSuchAlgorithmException,
            KeyManagementException {

        //加载证书
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("E:\\keys\\httpstest.p12"));//加载本地的证书进行https加密传输
        try {
            keyStore.load(instream, "httpstest".toCharArray());//设置证书密码
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "httpstest".toCharArray())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();

        hasInit = true;
    }
    
    /**
     * 通过Https往API post 数据
     *
     * @param url    API地址
     * @param Obj 要提交的数据对象
     * @param dataType 要提交数据的封装类型
     * @return API回包的实际数据
     * @throws IOException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */

    public String sendPost(String url, Object Obj, String dataType) throws IOException,
            KeyStoreException,
            UnrecoverableKeyException,
            NoSuchAlgorithmException,
            KeyManagementException {

        if (!hasInit) {
            init();
        }

        String result = null;

        HttpPost httpPost = new HttpPost(url);
        String postData = "";
        
        if(dataType.equals("XML")){
        	//解决XStream对出现双下划线的bug
            XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

            //将要提交给API的数据对象转换成XML格式数据Post给API
            postData = xStreamForRequestPostData.toXML(Obj);

//            httpPost.addHeader("Content-Type", "text/xml");
            
            log.info("API，POST过去的数据是：");
            System.out.println("API，POST过去的数据是：");
            log.info(postData);
            System.out.println(postData);
        }else if(dataType.equals("GSON")){
        	Gson gson = new Gson();
        	postData = gson.toJson(Obj);
//            httpPost.addHeader("Content-Type", "application/json");
            
            log.info("API，POST过去的数据是：");
            System.out.println("API，POST过去的数据是：");
            log.info(postData);
            System.out.println(postData);
            
        }
        

        //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
//        StringEntity postEntity = new StringEntity(postData, "UTF-8");
        // 创建参数队列    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("userinfo", postData)); 
        formparams.add(new BasicNameValuePair("datatype", dataType)); 
        UrlEncodedFormEntity uefEntity;
        uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httpPost.setEntity(uefEntity);

        //设置请求器的配置
        httpPost.setConfig(requestConfig);

        log.info("executing request" + httpPost.getRequestLine());
        System.out.println("executing request" + httpPost.getRequestLine());

        try {
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            result = EntityUtils.toString(entity, "UTF-8");
            
            /**
             * 检查http 响应状态
             */
            System.out.println("返回状态码为："+ response.getStatusLine());

        } catch (ConnectionPoolTimeoutException e) {
            log.error("http get throw ConnectionPoolTimeoutException(wait time out)");
            System.out.println("http get throw ConnectionPoolTimeoutException(wait time out)");
        } catch (ConnectTimeoutException e) {
            log.error("http get throw ConnectTimeoutException");
            System.out.println("2");
        } catch (SocketTimeoutException e) {
            log.error("http get throw SocketTimeoutException");
            System.out.println("3");
        } catch (Exception e) {
            log.error("http get throw Exception");
            System.out.println("4");
            System.out.println(e.getMessage());
        } finally {
            httpPost.abort();
        }

        return result;
    }

    /**
     * 设置连接超时时间
     *
     * @param socketTimeout 连接时长，默认10秒
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        resetRequestConfig();
    }

    /**
     * 设置传输超时时间
     *
     * @param connectTimeout 传输时长，默认30秒
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        resetRequestConfig();
    }

    private void resetRequestConfig(){
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
    }

    /**
     * 允许商户自己做更高级更复杂的请求器配置
     *
     * @param requestConfig 设置HttpsRequest的请求器配置
     */
    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }
}