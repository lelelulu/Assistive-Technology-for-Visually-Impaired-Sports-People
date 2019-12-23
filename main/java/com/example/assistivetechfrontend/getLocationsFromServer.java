package com.example.assistivetechfrontend;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.text.TextUtils.split;

public class getLocationsFromServer extends AsyncTask<Void,Void,Void> {
    String data;

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.myjson.com/bins/m31mz");// this URL for test. should integrate later

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line !=null) {
                line=bufferedReader.readLine();
                data = data+line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    //-35.422    149.084
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivity.data.setText(this.data);
        String q = "null{\"list of locaton\":[[\"1345\",\"Ruichi Wu\",\"-35.422\",\"149.084\"],[\"2\",\"Wu Ruichi\",\"-35.424\",\"149.082\"],[\"7\",\"Le fang\",\"-35.421\",\"149.080\"]]}null";

    }


}
