package com.utcc.ruk.thingspeak;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

//import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url = "https://api.thingspeak.com/channels/122038/feeds/last.json?api_key=";
        String apikey = "9A46C54NQCKHWNRF";
        final UriApi uriapi01 = new UriApi();
        uriapi01.setUri(url,apikey);
        Timer timer = new Timer();
        TimerTask tasknew = new TimerTask(){
            public void run() {
                LoadJSON task = new LoadJSON();
                task.execute(uriapi01.getUri());
            }
        };
        timer.scheduleAtFixedRate(tasknew,15*1000,15*1000);

    }

    private class UriApi {

        private String uri,url,apikey;

        protected void setUri(String url, String apikey){
            this.url = url;
            this.apikey = apikey;
            this.uri = url + apikey;
        };

        protected  String getUri(){
            return uri;
        }

    }

    private class LoadJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return getText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            /*TextView textview = (TextView) findViewById(R.id.textJSON);
            textview.setText(result);*/
            String msg01 = "";
            String msg02 = "";
            try {
                JSONObject json = new JSONObject(result);

                msg01 += String.format("%s", json.getString("entry_id"));
                msg02 += String.format("%s", json.getString("field1"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView TextID01 = (TextView) findViewById(R.id.textID01);
            TextID01.setText(msg01);
            TextView text = (TextView) findViewById(R.id.text);
            text.setText(msg02);
        }
    }

    private String getText(String strUrl) {
        String strResult = "";
        try {
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            strResult = readStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
