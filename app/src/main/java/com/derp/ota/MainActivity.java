package com.derp.ota;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class MainActivity extends Activity {
TextView tt,date,device,down;
String zipdate,download,packagedate;
Button ck;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit App")
                    .setMessage("Are you sure you want to close the App?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tt=(TextView)findViewById(R.id.ver);
        down=(TextView)findViewById(R.id.download);
        date=(TextView)findViewById(R.id.date);
        device=(TextView)findViewById(R.id.device);
        ck=(Button)findViewById(R.id.check);
        try {
          Process p = new ProcessBuilder("/system/bin/getprop", "ro.build.date").start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            Process p2 = new ProcessBuilder("/system/bin/getprop", "derp.ota.version").start();
            BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            String da = br.readLine().toString();
            date.append(da+"\n");
            String version=br2.readLine().toString();
            String[] separated = version.split("-");
            final String zipdate=separated[6];
            this.zipdate=zipdate;
            tt.append(version+"\n");
            device.setText(Build.DEVICE);
            ck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ota().execute();
                    ck.setEnabled(false);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public InputStream getInputStream(URL url)
    {
        try
        {
            return url.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            return null;
        }
    }
public class ota extends AsyncTask<Void,Void,Void>
{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url=new URL("https://raw.githubusercontent.com/derp-caf/OtaConfig/master/"+Build.DEVICE+".xml");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(getInputStream(url), "UTF_8");
            boolean insidedevice = false;
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("device")) {
                        insidedevice = true;
                    }
                   else if (xpp.getName().equalsIgnoreCase("codename")) {
                        String devicename = xpp.nextText();

                    }
                    else if (xpp.getName().equalsIgnoreCase("download")) {
                        String downloa = xpp.nextText();
                        download=downloa;
                    }
                    else if (xpp.getName().equalsIgnoreCase("package")) {
                        String packagename = xpp.nextText();
                        String[] updatepk=packagename.split("-");
                        String pacagedate=updatepk[6];
                        packagedate=pacagedate;
                    }
                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("device")) {
                    insidedevice = false;
                }
                eventType = xpp.next();
            }
        }
        catch (Exception e)
        {
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ck.setEnabled(true);
        if(zipdate.equals(packagedate))
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Sorry")
                    .setMessage("No Updates Available")
                    .setNegativeButton("ok",null).show();
        }
        else {
            down.setText(download);
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Update Available")
                    .setMessage("Do you want to update?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri=Uri.parse(download);
                            Intent i = new Intent(Intent.ACTION_VIEW,uri);
                            startActivity(i);
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }


}

}
