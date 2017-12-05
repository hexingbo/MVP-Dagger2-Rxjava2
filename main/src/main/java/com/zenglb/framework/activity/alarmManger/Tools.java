package com.zenglb.framework.activity.alarmManger;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zlb on 2017/11/16.
 */
public class Tools {

    /**
     * 向日历中添加事件
     *
     * @param context          上下文
     * @param eventTitle       事件标题
     * @param eventDescription 事件描述
     * @param startMillis      事件开始时间
     * @param endMillis        事件结束时间
     * @param reminderMinutus  提前时间提醒(分钟)
     */
    public static String addEvent(Context context, String eventTitle,
                                  String eventDescription, String noticeAddr, long startMillis, long endMillis, int reminderMinutus) {
        try {
            String calanderURL = "";
            String calanderEventURL = "";
            String calanderRemiderURL = "";

            if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                calanderURL = "content://com.android.calendar/calendars";
                calanderEventURL = "content://com.android.calendar/events";
                calanderRemiderURL = "content://com.android.calendar/reminders";
            } else {
                calanderURL = "content://calendar/calendars";
                calanderEventURL = "content://calendar/events";
                calanderRemiderURL = "content://calendar/reminders";
            }

            double calId = 0;

            // 这是数据库中视图 sql: xxxx--- FROM Events JOIN Calendars ON (Events.calendar_id=Calendars._id)
            Cursor c = context.getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);
            if (c.moveToFirst()) {
                calId = c.getDouble(c.getColumnIndex("_id"));
                System.out.println("movie to first  and id = " + calId);
            } else {
                ContentValues cValues = new ContentValues();
                cValues.put("_id", calId);
                cValues.put("name", "magus");
                context.getContentResolver().insert(Uri.parse(calanderURL), cValues);
                System.out.println("no first and add id to calander");
            }

            ContentValues eValues = new ContentValues();
            eValues.put("dtstart", startMillis);
            eValues.put("dtend", endMillis);
            eValues.put("title", eventTitle);
            eValues.put("description", eventDescription);
            eValues.put("calendar_id", calId);
            eValues.put("hasAlarm", 1);
            eValues.put("eventTimezone", "GMT+8");
            eValues.put("eventLocation", noticeAddr);


            Uri uri = context.getContentResolver().insert(
                    Uri.parse(calanderEventURL), eValues);

            String myEventsId = uri.getLastPathSegment();  //得到当前表的_id

            ContentValues rValues = new ContentValues();
            rValues.put("event_id", myEventsId);
            rValues.put("minutes", reminderMinutus);
            rValues.put("method", 1);
            context.getContentResolver().insert(Uri.parse(calanderRemiderURL),
                    rValues);
            System.out.println("success");

            return myEventsId;

        } catch (Exception e) {
            return "-1";
        }


    }

    /**
     * 向日历中添加事件  提醒重复规则为每月
     *
     * @param context          上下文
     * @param eventTitle       事件标题
     * @param eventDescription 事件描述
     * @param startMillis      事件开始时间
     * @param endMillis        事件结束时间
     * @param reminderMinutus  提前时间提醒(分钟)
     */
    public static String addEventMonth(Context context, String eventTitle,
                                       String eventDescription, String noticeAddr, long startMillis, long endMillis, int reminderMinutus) {
        try {
            String calanderURL = "";
            String calanderEventURL = "";
            String calanderRemiderURL = "";

            if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                calanderURL = "content://com.android.calendar/calendars";
                calanderEventURL = "content://com.android.calendar/events";
                calanderRemiderURL = "content://com.android.calendar/reminders";
            } else {
                calanderURL = "content://calendar/calendars";
                calanderEventURL = "content://calendar/events";
                calanderRemiderURL = "content://calendar/reminders";
            }

            double calId = 0;

            // 这是数据库中视图 sql: xxxx--- FROM Events JOIN Calendars ON (Events.calendar_id=Calendars._id)
            Cursor c = context.getContentResolver().query(Uri.parse(calanderURL), null, null, null, null);
            if (c.moveToFirst()) {
                calId = c.getDouble(c.getColumnIndex("_id"));
                System.out.println("movie to first  and id = " + calId);
            } else {
                ContentValues cValues = new ContentValues();
                cValues.put("_id", calId);
                cValues.put("name", "magus");
                context.getContentResolver().insert(Uri.parse(calanderURL), cValues);
                System.out.println("no first and add id to calander");
            }

            ContentValues eValues = new ContentValues();
            eValues.put("dtstart", startMillis);
            eValues.put("dtend", endMillis);
            eValues.put("title", eventTitle);
            eValues.put("description", eventDescription);
            eValues.put("calendar_id", calId);
            eValues.put("hasAlarm", 1);
            eValues.put("eventTimezone", "GMT+8");
            eValues.put("eventLocation", noticeAddr);

            //FREQ=MONTHLY;UNTIL=20220315T040000Z;WKST=SU;BYMONTHDAY=15
            Date date = new Date(startMillis);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String sMonth = month < 10 ? "0" + month : month + "";
            String sDay = day < 10 ? "0" + day : day + "";

            String rrule = "FREQ=MONTHLY;UNTIL=" + year + sMonth + sDay + "T000000Z;WKST=SU;BYMONTHDAY=" + day;
            System.out.println(rrule);
            eValues.put("rrule", rrule);

            Uri uri = context.getContentResolver().insert(
                    Uri.parse(calanderEventURL), eValues);

            String myEventsId = uri.getLastPathSegment();  //得到当前表的_id

            ContentValues rValues = new ContentValues();
            rValues.put("event_id", myEventsId);
            rValues.put("minutes", reminderMinutus);
            rValues.put("method", 1);
            context.getContentResolver().insert(Uri.parse(calanderRemiderURL),
                    rValues);
            System.out.println("success");

            return myEventsId;

        } catch (Exception e) {
            return "-1";
        }

    }

    /**
     * 删除所有的事件
     *
     * @param context
     */
    public static void delAllEvents(Context context) {
        String calanderEventURL = "";
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            calanderEventURL = "content://com.android.calendar/events";
        } else {
            calanderEventURL = "content://calendar/events";
        }

        ContentResolver cre = context.getContentResolver();
        int cure = cre.delete(Uri.parse(calanderEventURL), null, null);
        System.out.println("del events lines ==>" + cure);

    }

    /**
     * 删除指定id的事件
     *
     * @param context
     * @param id
     */
    public static void deleteEventById(Context context, String id) {
        String calanderEventURL = "";
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            calanderEventURL = "content://com.android.calendar/events";
        } else {
            calanderEventURL = "content://calendar/events";
        }

        ContentResolver cre = context.getContentResolver();
        int cure = cre.delete(Uri.parse(calanderEventURL), "_id=?", new String[]{id});
        System.out.println("del events lines ==>" + cure);
    }


    /**
     * 显示所有日历中事件的详细
     *
     * @param context
     */
    public static void showAllEvents(Context context) {
        String calanderURL = "content://calendar/calendars";
        String calanderEventURL = "content://calendar/events";


        ContentResolver cru = context.getContentResolver();
        Cursor curu = cru.query(Uri.parse(calanderURL), null,null, null, null);
        while (curu.moveToNext()) {
            String id = curu.getString(curu.getColumnIndex("_id"));
            String name = curu.getString(curu.getColumnIndex("name"));
            System.out.println("calanderID-->" + id + ",name=" + name);
        }
        System.out.println("--------show-----------");

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(Uri.parse(calanderEventURL), null,null, null, null);
        while (cur.moveToNext()) {
            String id = cur.getString(cur.getColumnIndex("_id"));
            String calendarId = cur.getString(cur.getColumnIndex("calendar_id"));
            String title = cur.getString(cur.getColumnIndex("title"));
            String dtstart = cur.getString(cur.getColumnIndex("dtstart"));
            String dtend = cur.getString(cur.getColumnIndex("dtend"));
            String eventLocation = cur.getString(cur.getColumnIndex("eventLocation"));
            String description = cur.getString(cur.getColumnIndex("description"));

            System.out.println("event =>" + id + ","
                    + calendarId + "," + title + ","
                    + dtstart + "," + dtend + "," + eventLocation + "," + description);
        }
    }
}