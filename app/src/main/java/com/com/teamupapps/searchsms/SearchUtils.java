package com.com.teamupapps.searchsms;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.teamupapps.searchsms.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by nrobatmeily on 27/10/2014.
 */
public class SearchUtils {


    public static List<SMS> readMessages(Context context, String wordToSearch) {
        Map<String, String> contactMap = getContactList(context);

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
                    SMS foundSMS = new SMS(id[i], getContactName(number[i], contactMap), body[i], date[i]);
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

    public static Map getContactList(Context context){
        String zip = "+"+GetCountryZipCode(context);
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        Map<String, String> countMap = new HashMap<String, String>();
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if(phoneNumber.startsWith("0")){
                String codeNumber = zip+phoneNumber.substring(1);
                phoneNumber = codeNumber.replaceAll("\\s","");
            }

            countMap.put(phoneNumber, name);
        }
        phones.close();

        return countMap;
    }

    public static String getContactName(String number ,Map<String, String> cMap) {

        Map<String, String> contactMap = cMap;
        String contactName;
        contactName = contactMap.get(number);
        if(contactName != null) {
            return contactName;
        }else{
            return number;
        }
    }

    public static String GetCountryZipCode(Context context){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }

}
