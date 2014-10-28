package com.com.teamupapps.searchsms;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;

import com.teamupapps.searchsms.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nrobatmeily on 27/10/2014.
 */
public class SearchUtils {


    public static List<SMS> readMessages(Context context, String wordToSearch) {
        List<SMS> smsList = new LinkedList<SMS>();
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        Boolean found= false;

        String[] body = new String[cursor.getCount()];
        String[] number = new String[cursor.getCount()];
        String[] id = new String[cursor.getCount()];
        String[] date = new String[cursor.getCount()];

        int amountofFS = 0;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean caseSensitive = prefs.getBoolean(context.getApplicationContext().getString(R.string.prefs_case_key), false);
        boolean exact = prefs.getBoolean(context.getApplicationContext().getString(R.string.prefs_exact_key), false);
        Log.w("TAG", "" + caseSensitive);
        if (!caseSensitive){
            wordToSearch = wordToSearch.toLowerCase();
        }
        Log.w("TAG", "" + wordToSearch);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                id[i] = cursor.getString(cursor.getColumnIndexOrThrow("_id")).toString();
                body[i] = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                number[i] = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
                date[i] = cursor.getString(cursor.getColumnIndexOrThrow("date")).toString();

                if (exact){
                    if (!caseSensitive){
                        found = body[i].toLowerCase().matches(".*\\b" + wordToSearch + "\\b.*");
                    }else{
                        found = body[i].matches(".*\\b" + wordToSearch + "\\b.*");
                    }

                }else{
                    if (!caseSensitive){
                        found = body[i].toLowerCase().contains(wordToSearch);
                    }else{
                        found = body[i].contains(wordToSearch);
                    }

                }


                if (found) {
                    SMS foundSMS = new SMS(id[i], number[i], body[i], date[i]);
                    smsList.add(foundSMS);
                    amountofFS++;
                    found = false;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();

        return smsList;


    }
    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

}
