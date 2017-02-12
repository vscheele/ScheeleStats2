package com.scheeles.valentin.scheelestats;

import android.database.Cursor;
import android.provider.CallLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.*;
import java.util.HashMap;

/**
 * Created by Valentin Scheele on 15.01.2017.
 *
 * Statscounter is the singleton which stores the stats
 * It has public members which can easily be accessed and
 * also provides some utility functions.
 *
 *************/

public class StatsCounter {

    public String outputtext="";
    public HashMap<Integer,Integer> outgoingseconds = new HashMap(0);  // 0 is current month, 1 is last month,...
    public HashMap<Integer,Integer> incomingseconds = new HashMap(0);
    public HashMap<Integer,Integer> outgoingbytes = new HashMap(0);
    public HashMap<Integer,Integer> incomingbytes = new HashMap(0);
    /*********************************************************************************
    *  Reads all call Entrys from a given Cursor and stores them in the local singleton object.
     **********************************************************************************/
    public String readCallEntrys(Cursor managedCursor){
        String result="";
        //get Column Definitions
        int colnumber = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int coltype = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int coldate = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int colduration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int secondsThismonth=0;
        int secondsLastMonth=0;
        int bytesThisMonth=0;
        int bytesLastMonth=0;
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();   // used for date arithmetic
        cal.setTime(date);
        int todaymonth = cal.get(Calendar.MONTH);   //today

        //walk through all calls and check what to do with them
        while (managedCursor.moveToNext()) {

            String phNum = managedCursor.getString(colnumber);      //Phone Number
            Integer callDuration = managedCursor.getInt(colduration);
            int callType = managedCursor.getInt(coltype);           //Type of Call
            String callDate = managedCursor.getString(coldate);     //Time of Call as String
            Date callDayTime = new Date(Long.valueOf(callDate));    //converted to Java data
            cal.setTime(callDayTime);
            Integer callmonth = cal.get(Calendar.MONTH);

            //count outgoing calls
            if (callType==CallLog.Calls.OUTGOING_TYPE){
                if (outgoingseconds.containsKey(callmonth)){
                    outgoingseconds.put(callmonth,outgoingseconds.get(callmonth)+callDuration);
                } else {
                    outgoingseconds.put(callmonth,callDuration);
                }
            }
            //count incoming calls
            if (callType==CallLog.Calls.INCOMING_TYPE){
                if (incomingseconds.containsKey(callmonth)){
                    incomingseconds.put(callmonth,incomingseconds.get(callmonth)+callDuration);
                } else {
                    incomingseconds.put(callmonth,callDuration);
                }
            }
            result+=(phNum + "\r\n" + callDayTime + "\r\n" + callDuration + "\r\n");
        }

        for (Integer key : outgoingseconds.keySet()) {
            System.out.println(new SimpleDateFormat("MMM").format(cal.getTime()));
        }


        //format the output for all months
        Calendar mCalendar = Calendar.getInstance();
        String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());




        return result;
    }

}
