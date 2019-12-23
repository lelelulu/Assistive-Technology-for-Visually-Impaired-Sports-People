package com.example.assistivetechfrontend;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//This class contains the method to send data to the server using HTTPURLConnection through POST request
public class HttpUtil {
    //send POST request to the server, return the response
    public static String sendPostRequest(JSONObject data, String encode, String url_in) throws MalformedURLException {

        try {
            URL url = new URL(url_in);
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");

            String datastr=data.toString();
            byte[] bytes=datastr.getBytes("UTF-8");

            urlConnection.connect();
            OutputStream out=urlConnection.getOutputStream();
            BufferedOutputStream outputStream=new BufferedOutputStream(out);

            outputStream.write(bytes);
            outputStream.flush();
            out.close();
            outputStream.close();

            int responseCode=urlConnection.getResponseCode();
            if(responseCode==HttpURLConnection.HTTP_OK){
                Log.i("send", "connect");
                InputStream inputStream=urlConnection.getInputStream();
                return response(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String response(InputStream inputStream){
        BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
        byte[] data=new byte[1024];
        int len=-1;
        StringBuffer stringBuffer=new StringBuffer();
        try{
            while((len=bufferedInputStream.read(data))!=-1){
                stringBuffer.append(new String(data,0,len));
            }
            inputStream.close();
            bufferedInputStream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        String result=stringBuffer.toString();
        return result;
    }
}

