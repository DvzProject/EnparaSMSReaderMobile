package net.stdowl.dovizexenpara.dovizexenpara.enpara;

import android.Manifest;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private static final String TAG = "SmsBroadcastReceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);


        smsBroadcastReceiver = new SmsBroadcastReceiver("123", "Lutfen bu sifreyi");
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

        smsBroadcastReceiver.setListener(new SmsBroadcastReceiver.Listener() {
            @Override
            public void onTextReceived(String text)  {
                Log.d(TAG,"SMS Received:" + text);


                /*
                * Lutfen bu sifreyi, banka personeli de dahil, hic kimseyle paylasmayin. Enpara.com Sirketim internet Subesi'ne giris icin SMS sifreniz: 847176  B001
                * */

                Pattern p = Pattern.compile("-?\\d+");
                Matcher m = p.matcher(text);

                while (m.find()) {
                    String code = m.group();
                    if(code.length() == 6)
                    {
                        URL url = null;
                        try {
                            url = new URL("https://bankintegration.dovizex.com/enpara/addSMS/" + code);
                        } catch (MalformedURLException e) {
                            Log.d(TAG,"error url");

                            Log.d(TAG, e.toString());
                        }
                        HttpURLConnection urlConnection = null;
                        try {
                            urlConnection = (HttpURLConnection) url.openConnection();
                        } catch (IOException e) {
                            Log.d(TAG,"error urlconnect");

                            Log.d(TAG, e.toString());
                        }
                        try {
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                        } catch (IOException e) {
                            Log.d(TAG,"error io");

                            Log.d(TAG, e.toString());

                        } finally {
                            urlConnection.disconnect();
                        }

                        break;
                    }


                }


            }
        });



    }




    @Override
    public void onDestroy() {
        unregisterReceiver(smsBroadcastReceiver);
        super.onDestroy();

    }



}
