package com.scheeles.valentin.scheelestats;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/***************************************************************
 *
 *
 *  My first Android 5 Project:
 *  My wife asks for a simple app that shows her the total duration
 *  of calls in the current month and the last month.
 *  She also wants the data transfered listed similarly.
 *  Most UIs/Stock Android show this data already, but not with a
 *  all-in-one-click-view.
 *
 ***************************************************************/

public class MainActivity extends AppCompatActivity {

    public int bytesthismonth;
    public int byteslastmonth;
    public int secondsthismonth;
    public int secondslastmonth;

    boolean permissionsgranted=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lets do this in the GUI main thread first, later we add AsyncTask
        this.setToLoading();
        startPermissionsCheck();
        //setToFinished();
        //this.requestPermissions();

    }
    void startPermissionsCheck(){
        // Check for permissions
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED )

        {
            // Should we show an explanation?
            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)||false) {
                System.out.println("ok");

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } */
            //else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_CALL_LOG},
                        999); //999 will be returned in the callback
            //}
        } else {
            this.permissionsgranted=true;
            this.getDataFromAndroid();
        }
    }

    void getDataFromAndroid(){
        TextView t1 = (TextView) findViewById (R.id.AnrufText1);
        StatsCounter sc=new StatsCounter();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,null, null, null);
        String result= sc.readCallEntrys(managedCursor);
        t1.setText(result);
        this.setToFinished();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 999: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    this.permissionsgranted=true;
                    this.getDataFromAndroid();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //Hides the textview wich contain the results, shows loading views
    void setToLoading()   {
        TextView tl = (TextView) findViewById (R.id.AnrufText1);
        tl.setVisibility(TextView.INVISIBLE);
        TextView t2 = (TextView) findViewById (R.id.AnrufText2);
        t2.setVisibility(TextView.INVISIBLE);
        TextView t3 = (TextView) findViewById (R.id.DataText1);
        t3.setVisibility(TextView.INVISIBLE);
        TextView t4 = (TextView) findViewById (R.id.DataText2);
        t4.setVisibility(TextView.INVISIBLE);
        ProgressBar p = (ProgressBar) findViewById (R.id.AnrufProgressbar1);
        p.setVisibility(TextView.VISIBLE);
        ProgressBar p2 = (ProgressBar) findViewById (R.id.DataProgressbar);
        p2.setVisibility(TextView.VISIBLE);
        TextView t5 = (TextView) findViewById (R.id.AnrufLadeText1);
        t5.setVisibility(TextView.VISIBLE);
        TextView t6 = (TextView) findViewById (R.id.DataLadeText1);
        t6.setVisibility(TextView.VISIBLE);
        LinearLayout l = (LinearLayout)findViewById (R.id.Linear1);
        l.setVisibility(View.VISIBLE);
        LinearLayout l2 = (LinearLayout)findViewById (R.id.Linear2);
        l2.setVisibility(View.VISIBLE);
    }
    //Shows the textview wich contain the results, hides loading views
    void setToFinished()   {
        TextView tl = (TextView) findViewById (R.id.AnrufText1);
        tl.setVisibility(TextView.VISIBLE);
        TextView t2 = (TextView) findViewById (R.id.AnrufText2);
        t2.setVisibility(TextView.VISIBLE);
        TextView t3 = (TextView) findViewById (R.id.DataText1);
        t3.setVisibility(TextView.VISIBLE);
        TextView t4 = (TextView) findViewById (R.id.DataText2);
        t4.setVisibility(TextView.VISIBLE);
        ProgressBar p = (ProgressBar) findViewById (R.id.AnrufProgressbar1);
        p.setVisibility(TextView.INVISIBLE);
        ProgressBar p2 = (ProgressBar) findViewById (R.id.DataProgressbar);
        p2.setVisibility(TextView.INVISIBLE);
        TextView t5 = (TextView) findViewById (R.id.AnrufLadeText1);
        t5.setVisibility(TextView.INVISIBLE);
        TextView t6 = (TextView) findViewById (R.id.DataLadeText1);
        t6.setVisibility(TextView.INVISIBLE);
        LinearLayout l = (LinearLayout) findViewById(R.id.Linear1);
        l.setVisibility(View.GONE);
        LinearLayout l2 = (LinearLayout)findViewById (R.id.Linear2);
        l2.setVisibility(View.GONE);
    }


}
