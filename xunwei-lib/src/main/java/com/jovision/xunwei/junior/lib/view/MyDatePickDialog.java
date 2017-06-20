package com.jovision.xunwei.junior.lib.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jovision.xunwei.junior.lib.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by aiwei on 2016/1/26.
 */
public class MyDatePickDialog extends AlertDialog {

    private List<String> yearArray;
    private String[] months = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"};
    private String[] days = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21",
            "22","23","24","25","26","27","28","29","30","31"};

    private static final String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
    private static final String[] months_little = { "4", "6", "9", "11" };
    private static final List<String> list_big = Arrays.asList(months_big);
    private static final List<String> list_little = Arrays.asList(months_little);

    private MyWheelView mYear;
    private MyWheelView mMonth;
    private MyWheelView mDay;
    private Calendar mStartDate;
    private Calendar mEndDate;

    private Activity activity;
    private String startTime;
    private String endTime;
    private SelectTimeCallBack mCallBack;
    private String initTime;

    private int selectYear, selectMonth, selectDay;

    public MyDatePickDialog(Activity activity, String startTime, String endTime,
                               String initTime, SelectTimeCallBack callBack) {
        super(activity);
        this.activity = activity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.initTime = initTime;
        this.mCallBack = callBack;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_date_picker_dialog);

        yearArray = new ArrayList<String>();

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        lp.width = dm.widthPixels;
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.pop_window_bottom_anim);

        findViewById(R.id.btn_datetime_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDate();
                dismiss();
            }
        });

        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();
        mEndDate.add(Calendar.DAY_OF_YEAR, 30);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if(startTime!=null&&!startTime.equals("0000-00-00")
                && !startTime.equals("")){
            Date daystart = null;
            try {
                daystart = df.parse(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(daystart!=null){
                mStartDate.setTime(daystart);
            }
        }
        if(endTime!=null&&!endTime.equals("0000-00-00")
                && !endTime.equals("")){
            Date dayend = null;
            try {
                dayend = df.parse(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(dayend!=null){

                mEndDate.setTime(dayend);
            }
        }

        mYear = (MyWheelView)findViewById(R.id.date_pick_year);
        mMonth = (MyWheelView)findViewById(R.id.date_pick_month);
        mDay = (MyWheelView)findViewById(R.id.date_pick_day);

        Calendar initdata = Calendar.getInstance();
        try{
            DateFormat initdf = new SimpleDateFormat("yyyy-MM-dd");
            initdata.setTime(initdf.parse(initTime));
        }catch (ParseException e){
        }

        final int startYear = mStartDate.get(Calendar.YEAR);
        int endYear = mEndDate.get(Calendar.YEAR);
        int currentPos = initdata.get(Calendar.YEAR) - mStartDate.get(Calendar.YEAR);
        Log.d("date picker dialog", "start: " + startYear + ", end: " + endYear);
        for(int i = startYear; i < endYear; i++){
            yearArray.add(i + "");
        }

        mYear.setViewWidth(lp.width);
        mYear.setOffset(1);
        mYear.setItems(yearArray);
        mYear.setSeletion(currentPos);

        mMonth.setViewWidth(lp.width);
        mMonth.setOffset(1);
        mMonth.setItems(Arrays.asList(months));
        mMonth.setSeletion(initdata.get(Calendar.MONTH));

        mDay.setViewWidth(lp.width);
        mDay.setOffset(1);
        if (list_big.contains(String.valueOf(initdata.get(Calendar.MONTH) + 1))) {
            //31
            mDay.setItems(Arrays.asList(days));
        } else if (list_little.contains(String.valueOf(initdata.get(Calendar.MONTH) + 1))) {
            //30
            mDay.setItems(Arrays.asList(Arrays.copyOfRange(days, 0, 30)));
        } else {
            if ((initdata.get(Calendar.YEAR) % 4 == 0 && initdata.get(Calendar.YEAR) % 100 != 0) || initdata.get(Calendar.YEAR) % 400 == 0) {
                //29
                mDay.setItems(Arrays.asList(Arrays.copyOfRange(days, 0, 29)));
            }
            else {
                //28
                mDay.setItems(Arrays.asList(Arrays.copyOfRange(days, 0, 28)));
            }
        }
        mDay.setSeletion(initdata.get(Calendar.DAY_OF_MONTH) - 1);

        selectYear = initdata.get(Calendar.YEAR);
        selectMonth = initdata.get(Calendar.MONTH) + 1;
        selectDay = initdata.get(Calendar.DAY_OF_MONTH);

        mYear.setOnWheelViewListener(new MyWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                int year_num = selectedIndex + startYear - 1;
                List<String> daysList;
                if (list_big.contains(String.valueOf(mMonth.getSeletedIndex() + 1))) {
                    //31
                    daysList = Arrays.asList(days);
                    if(mDay.getItemsSize() != daysList.size()){
                        mDay.setItems(daysList);
                        mDay.setSmoothSeletion(0);
                    }
                } else if (list_little.contains(String.valueOf(mMonth.getSeletedIndex() + 1))) {
                    //30
                    daysList = Arrays.asList(Arrays.copyOfRange(days, 0, 30));
                    if(mDay.getItemsSize() != daysList.size()){
                        mDay.setItems(daysList);
                        mDay.setSmoothSeletion(0);
                    }
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0){
                        //29
                        daysList = Arrays.asList(Arrays.copyOfRange(days, 0, 29));
                        if(mDay.getItemsSize() != daysList.size()){
                            mDay.setItems(daysList);
                            mDay.setSmoothSeletion(0);
                        }
                    }
                    else{
                        //28
                        daysList = Arrays.asList(Arrays.copyOfRange(days, 0, 28));
                        if(mDay.getItemsSize() != daysList.size()){
                            mDay.setItems(daysList);
                            mDay.setSmoothSeletion(0);
                        }
                    }
                }
            }
        });
        mMonth.setOnWheelViewListener(new MyWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                int month_num = selectedIndex;
                int year_num = mYear.getSeletedIndex() + startYear;

                if(selectMonth != month_num){
                    List<String> daysList;
                    if (list_big.contains(String.valueOf(month_num))) {
                        //31
                        daysList = Arrays.asList(days);
                        if(mDay.getItemsSize() != daysList.size()){
                            mDay.setItems(daysList);
                        }
                    } else if (list_little.contains(String.valueOf(month_num))) {
                        //30
                        daysList = Arrays.asList(Arrays.copyOfRange(days, 0, 30));
                        if(mDay.getItemsSize() != daysList.size()){
                            mDay.setItems(daysList);
                        }
                    } else {
                        if (year_num % 4 == 0 && year_num % 100 != 0 || year_num % 400 == 0){
                            //29
                            daysList = Arrays.asList(Arrays.copyOfRange(days, 0, 29));
                            if(mDay.getItemsSize() != daysList.size()){
                                mDay.setItems(daysList);
                            }
                        }else {
                            //28
                            daysList = Arrays.asList(Arrays.copyOfRange(days, 0, 28));
                            if(mDay.getItemsSize() != daysList.size()){
                                mDay.setItems(daysList);
                            }
                        }
                    }
                    mDay.setSmoothSeletion(0);

                    selectMonth = month_num;
                }
            }
        });
    }

    private void confirmDate() {
        String parten = "00";
        DecimalFormat decimal = new DecimalFormat(parten);
        int year = mYear.getSeletedIndex() + mStartDate.get(Calendar.YEAR);
        int month = mMonth.getSeletedIndex();
        int day = mDay.getSeletedIndex() + 1;
        Calendar temp = Calendar.getInstance();
        temp.set(Calendar.YEAR,year);
        temp.set(Calendar.MONTH, month);
        temp.set(Calendar.DAY_OF_MONTH, day);

        if(mCallBack != null){
            mCallBack.selectTimeCallBack(year + "-" + decimal.format(month+1) + "-" + decimal.format(day));
        }
    }

    public  interface SelectTimeCallBack{
        public void selectTimeCallBack(String time);
    }
}
