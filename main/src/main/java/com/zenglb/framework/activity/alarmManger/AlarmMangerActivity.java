package com.zenglb.framework.activity.alarmManger;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.zenglb.baselib.base.BaseActivity;
import com.zenglb.framework.R;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.Calendar;
import java.util.Date;

/**
 * AlarmManger 的练习使用,执行系统的定时任务
 */
public class AlarmMangerActivity extends BaseActivity {
    public static final String ALARM_ACTION = "com.zenglb.framework.alarm.clock";

    private TextView displayEnvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("后台定时任务还能活？");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_alarm_manger;
    }

    @Override
    protected void initViews() {
        displayEnvents = (TextView) findViewById(R.id.displayevents);
        displayEnvents.setMovementMethod(ScrollingMovementMethod.getInstance());

        Button btn = (Button) findViewById(R.id.btn_io_canlender);
        btn.setOnClickListener(this);
        Button btn2 = (Button) findViewById(R.id.btn_find_canlender);
        btn2.setOnClickListener(this);
        Button btn3 = (Button) findViewById(R.id.btn_del_canlender);
        btn3.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_io_canlender:
                long startMillis = 0;
                long endMillis = 0;

                Tools.addEventMonth(this, "titlexxxx", "xx", "addr", startMillis, endMillis, 10);

                startMillis = System.currentTimeMillis() + 1000 * 60 * 100;
                endMillis = startMillis + 1000 * 60 * 30;

                //https://github.com/florent37/SingleDateAndTimePicker

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.TITLE, "你要去催缴了，GGDSADF");

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);//起始时间-必须设置的属性
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis);//结束时间-必须设置的属性
                startActivity(intent);

                break;
            case R.id.btn_find_canlender:
//                Tools.showAllEvents(this);

//                new SingleDateAndTimePickerDialog.Builder(this)
////                        .bottomSheet()
//                        //.curved()
//                        //.minutesStep(15)
//
//                        //.displayHours(false)
//                        //.displayMinutes(false)
//
//                        .backgroundColor(Color.WHITE)
//                        .mainColor(Color.parseColor("#123454"))
//                        .titleTextColor(Color.GREEN)
//                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
//                            @Override
//                            public void onDisplayed(SingleDateAndTimePicker picker) {
//                                //retrieve the SingleDateAndTimePicker
//                            }
//                        })
//
//                        .title("Simple")
//                        .listener(new SingleDateAndTimePickerDialog.Listener() {
//                            @Override
//                            public void onDateSelected(Date date) {
//
//                            }
//                        }).display();

//                TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
//                    @Override
//                    public void handle(String time) {
//                        Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
//                    }
//                }, "2015-11-22 17:34", "2015-12-1 15:20");
//
//                timeSelector.show();


//                //时间选择器
//                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
//                    @Override
//                    public void onTimeSelect(Date date, View v) {//选中事件回调
//                        Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_LONG).show();
//
//                    }
//                })
//                        .build();
//                pvTime.setDate(Calendar.getInstance());
//                pvTime.show();


                Calendar selectedDate = Calendar.getInstance();
                Calendar startDate = Calendar.getInstance();
                //startDate.set(2013,1,1);
                Calendar endDate = Calendar.getInstance();
                //endDate.set(2020,1,1);

                //正确设置方式 原因：注意事项有说明
                startDate.set(2013, 0, 1);
                endDate.set(2020, 11, 31);

                TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        Toast.makeText(getApplicationContext(), date.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                        .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                        .setContentSize(18)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        .setTitleText("Title")//标题文字
                        .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(false)//是否循环滚动
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                        .setCancelColor(Color.BLUE)//取消按钮文字颜色
                        .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                        .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                        .setRangDate(startDate, endDate)//起始终止年月日设定
                        .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                        .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .isDialog(false)//是否显示为对话框样式
                        .build();

                pvTime.setDate(Calendar.getInstance());
                pvTime.show();

                break;
            case R.id.btn_del_canlender:
//                Tools.delAllEvents(this);
                break;
            default:
                break;
        }
    }


}
