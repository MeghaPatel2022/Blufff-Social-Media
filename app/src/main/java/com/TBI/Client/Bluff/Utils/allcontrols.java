package com.TBI.Client.Bluff.Utils;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class allcontrols extends FragmentActivity {


    public static void setdatepicker_validation1(final EditText e1, final Context context, final MonthAdapter.CalendarDay minday, final MonthAdapter.CalendarDay maxday) {

        e1.setFocusable(false);
        Calendar c = Calendar.getInstance();
        int currentday = c.get(Calendar.DAY_OF_MONTH);
        final MonthAdapter.CalendarDay mindaytoday = new MonthAdapter.CalendarDay(c.get(Calendar.YEAR), c.get(Calendar.MONTH), currentday);

        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                int num = monthOfYear + 1;
                                String text = (num < 10 ? "0" : "") + num;
                                int num1 = dayOfMonth;
                                String text1 = (num1 < 10 ? "0" : "") + num1;

                                e1.setText(datetimeformat.getdate(year + "-" + (text) + "-" + text1));

                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDateRange(null, mindaytoday)
                        .setDoneText("Yes")
                        .setCancelText("No")
                        .setThemeDark();

                if (e1.getText().toString().equals("")) {
                    cdp.setPreselectedDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                } else {
                    String datestring = e1.getText().toString();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  ");
                    try {
                        Date date = format.parse(datestring);

                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(date);
                        cdp.setPreselectedDate(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                FragmentActivity activity = (FragmentActivity) context;
                cdp.show(activity.getSupportFragmentManager(), "date");

            }
        });
    }
}
