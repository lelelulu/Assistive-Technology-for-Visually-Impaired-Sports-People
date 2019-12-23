package com.example.assistivetechfrontend;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class CommunicationAPI implements Communicator {
    /**
     * This method update the location of VIPs
     *
     * @param sessionId  the session id that VIP joined
     * @param personalId the personal id of the VIP
     * @param latitude   the latitude of VIP
     * @param longitude  the longitude of VIP
     * @param url_in     the URL
     * @return the message from the server("Success True/False")
     */
    @Override
    public Boolean updateLocation(int sessionId, int personalId, double latitude, double longitude, String url_in) throws MalformedURLException {
       // String jsonData = "{\"sessionId\":\"" + sessionId + "\"," + "\"persionalId\":\"" + personalId + "\"," + "{\"latitude\":\"" + latitude + "\"," + "{\"longitude\":\"" + longitude + "\"}";
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("sessionId", sessionId);
            jsonData.put("personalId", personalId);
            jsonData.put("latitude", latitude);
            jsonData.put("longitude", longitude);
            String data = jsonData.toString();
            String response = sendPutRequest(data, url_in);
            JSONObject responseJson = new JSONObject(response);
            Boolean result = responseJson.getBoolean("Success");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method refers to the leave of the session by VIPs using PUT
     *
     * @param sessionId  the session id that VIP joined
     * @param personalId the personal id of the VIP
     * @param url_in     the URL
     * @return the message from the server("Success True/False")
     */
    @Override
    public Boolean leaveSession(int sessionId, int personalId, String url_in) throws MalformedURLException {
        //String jsonData = "{\"sessionId\":\"" + sessionId + "\"," + "{\"personalId\":\"" + personalId + "\"}";
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("sessionId", sessionId);
            jsonData.put("personalId", personalId);
            String data = jsonData.toString();
            String response = sendPutRequest(data, url_in);
            JSONObject responseJson = new JSONObject(response);
            Boolean result = responseJson.getBoolean("Success");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method creates the user and receive integer id from the server using GET request.
     *
     * @param name   the name of the VIP
     * @param encode the encode
     * @param url_in the URL
     * @return the integer id from the server
     */
    @Override
    public int createUser(String name, String encode, String url_in) throws MalformedURLException {
        int id = -1;
        try {
            String url = url_in + "?name=" + URLEncoder.encode(name, encode);
            String response = sendGetRequest(url);
            JSONObject responseJson = new JSONObject(response);
            id = responseJson.getInt("id");
            return id;
        } catch (UnsupportedEncodingException|JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int[] createSession(String name, double latitude, double longitude, String url_in) throws MalformedURLException {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("name", name);
            jsonData.put("latitude", latitude);
            jsonData.put("longitude", longitude);
            String data = jsonData.toString();

            String response = sendPostRequest(data, url_in);
            int[] result = new int[2];
            JSONObject jsonObject = new JSONObject(response);
            result[0] = jsonObject.getInt("sessionId");
            result[1] = jsonObject.getInt("guideId");
            return result;
        } catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double[] getLocation(int personalId, double latitude, double longitude, String url_in) throws MalformedURLException {
        JSONObject jsonData = new JSONObject();
        double[] result = new double[2];
        try {
            jsonData.put("personalId", personalId);
            jsonData.put("latitude", latitude);
            jsonData.put("longitude", longitude);
            String data = jsonData.toString();
            String url = url_in+"/"+personalId+"/location";
            String response = sendPutRequest(data, url);
            JSONObject responseJson = new JSONObject(response);
            result[0]= Double.parseDouble(responseJson.getString("latitude"));
            result[1]= Double.parseDouble(responseJson.getString("longitude"));
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int joinSession(String name, double latitude, double longitude, int sessionId, String url_in) throws MalformedURLException {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("name", name);
            jsonData.put("latitude", latitude);
            jsonData.put("longitude", longitude);
            jsonData.put("sessionId", sessionId);
            String data = jsonData.toString();

            String response = sendPostRequest(data, url_in);
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("Success")){
                int result = jsonObject.getInt("vin_id");
                return result;
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * This method is used to send PUT request.
     *
     * @param data   the data of the request in JSON file
     * @param url_in the URL
     * @return the JSON String from the server
     */
    public String sendPutRequest(String data, String url_in) throws MalformedURLException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(url_in);
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);//default is false
            urlConnection.setRequestMethod("PUT");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept-Charset", "utf-8");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Transfer-Encoding", "chunked");
            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
                String result = getDataFromInputStream(in);
                return result;
            } else {
                Log.i("sendPutRequest", "sendPutRequest: failed to connect " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
        }
        return "";
    }

    /**
     * This method is used to send GET request
     *
     * @param url_in the URL
     * @return the assigned id of the user
     */
    public String sendGetRequest(String url_in) throws MalformedURLException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(url_in);
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);

            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
                String result = getDataFromInputStream(in);
                return result;
            } else {
                Log.i("sendGetRequest", "sendGetRequest: failed to connect " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
        }
        return "";
    }

    public static String sendPostRequest(String datastr, String url_in) throws MalformedURLException {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(url_in);
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            urlConnection.setRequestProperty("content-length", datastr.length()+"");

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
                return getDataFromInputStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
        }
        return "";
    }
    public static String getDataFromInputStream(InputStream inputStream){
        BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
        byte[] data=new byte[1024];
        int len = -1;
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

