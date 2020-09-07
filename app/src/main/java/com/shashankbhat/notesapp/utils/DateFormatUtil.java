package com.shashankbhat.notesapp.utils;

import android.text.Spanned;

import androidx.core.text.HtmlCompat;

import java.util.Date;

/**
 * Created by SHASHANK BHAT on 07-Sep-20.
 */
public class DateFormatUtil {

    static String []months = new String[]{"Jan", "Feb", "Mar", "Apr","May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public static Spanned getStandardDate(Date date){
        int day = date.getDate();
        int month = date.getMonth();
        int year = date.getYear();
        return HtmlCompat.fromHtml("<b>Date </b> <i>"+day+"/"+months[month]+"/"+year+"</i>", HtmlCompat.FROM_HTML_MODE_COMPACT);
    }
}
