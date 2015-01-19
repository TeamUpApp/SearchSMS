package com.com.teamupapps.searchsms.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.com.teamupapps.searchsms.models.MMS;
import com.com.teamupapps.searchsms.models.SMS;
import com.teamupapps.searchsms.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nrobatmeily on 27/10/2014.
 */
public class SearchUtils {

    public static List<MMS> readMMS(Context context) {

        List<MMS> mmsList = new LinkedList<MMS>();

        Map<String, String> contactMap = getContactList(context);
        Map<String, String> contactImageMap = getContactPhotoList(context);

        Cursor cursor = context.getContentResolver().query(Uri.parse("content://mms/inbox"), null, null, null, null);

        String[] mmsBody = new String[cursor.getCount()];
        String[] number = new String[cursor.getCount()];
        String[] id = new String[cursor.getCount()];
        String[] date = new String[cursor.getCount()];
        String[] bitmap = new String[cursor.getCount()];
        String body = "";

        if (cursor.moveToFirst()) {

            String string = cursor.getString(cursor.getColumnIndex("ct_t"));
            if ("application/vnd.wap.multipart.related".equals(string)) {

                for (int i = 0; i < cursor.getCount(); i++) {
                    String mmsId = cursor.getString(cursor.getColumnIndex("_id"));

                    String selectionPart = "mid =" + mmsId;
                    Uri uri = Uri.parse("content://mms/part");
                    Cursor cursor2 = context.getContentResolver().query(uri, null,
                            selectionPart, null, null);

                    // String addselectionPart = "mid =" + mmsId;
                    // "content://mms/{MmsId}/addr"
                    Uri adduri = Uri.parse("content://mms/" + mmsId + "/addr");
                    Cursor cursoradd = context.getContentResolver().query(adduri, null,
                            null, null, null);
                    if (cursoradd.moveToFirst()) {


                        number[i] = cursoradd.getString(cursoradd.getColumnIndex("address"));
                    }
                    Log.i("ID ad i", mmsId + " " + i);

                    if (cursor2.moveToFirst()) {
                        do {
                            String partId = cursor2.getString(cursor2.getColumnIndex("_id"));
                            String type = cursor2.getString(cursor2.getColumnIndex("ct"));

                            if ("text/plain".equals(type)) {
                                String data = cursor2.getString(cursor2.getColumnIndex("_data"));
                                Log.i("data", "" + i + " " + data);

                                if (data != null) {
                                    // implementation of this method below
                                    body = getMmsText(context, partId);
                                } else {
                                    body = cursor2.getString(cursor2.getColumnIndex("text"));
                                }

                            } else {
                                body = "";
                            }

                            if ("image/jpeg".equals(type) || "image/bmp".equals(type) ||
                                    "image/gif".equals(type) || "image/jpg".equals(type) ||
                                    "image/png".equals(type)) {
                                bitmap[i] = partId;
                                Log.i("IMAGE", "" + i + " " + bitmap.toString());
                            }
                            //}

                            id[i] = partId;
                            mmsBody[i] = body;

                            date[i] = "";

                            // MMS foundMMS = new MMS(id[i], getContactName(number[i], contactMap), mmsBody[i], date[i], getContactName(number[i], contactImageMap), bitmap[i]);
                            // MMS foundMMS = new MMS(partId, getContactName(number[i], contactMap), body, "", getContactName(number[i], contactImageMap), getMmsImage(context, partId));
                            //mmsList.add(foundMMS);
                        } while (cursor2.moveToNext());

                        Log.i("YO", getContactName(number[i], contactMap));
                        MMS foundMMS = new MMS(id[i], getContactName(number[i], contactMap), mmsBody[i], date[i], getContactName(number[i], contactImageMap), bitmap[i]);
                        mmsList.add(foundMMS);
                        mmsBody[i] = "";
                    }
                    cursor.moveToNext();
                }

            }

        }

        return mmsList;

    }

    public static List<SMS> getHyperlinks(Context context) {
        Map<String, String> contactMap = getContactList(context);
        Map<String, String> contactImageMap = getContactPhotoList(context);

        List<SMS> smsLinkList = new LinkedList<SMS>();
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        Boolean found = false;

        String[] body = new String[cursor.getCount()];
        String[] number = new String[cursor.getCount()];
        String[] id = new String[cursor.getCount()];
        String[] date = new String[cursor.getCount()];

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                id[i] = cursor.getString(cursor.getColumnIndexOrThrow("_id")).toString();
                body[i] = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                number[i] = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
                date[i] = cursor.getString(cursor.getColumnIndexOrThrow("date")).toString();

                String regex = "\\(?\\b(http://|www[.]|Www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(body[i]);
                String urlStr ="";
                while (m.find()) {
                    urlStr = m.group();
                    if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                        urlStr = urlStr.substring(1, urlStr.length() - 1);
                    }
                    found = true;
                }

                if (found) {
                    SMS foundSMS = new SMS(id[i], getContactName(number[i], contactMap), body[i], date[i], getContactName(number[i], contactImageMap));
                    foundSMS.setUrl(urlStr);
                    smsLinkList.add(foundSMS);
                    found = false;
                }
                cursor.moveToNext();
            }
        }
        cursor.close();

        return smsLinkList;

    }

    public static String getMmsText(Context context, String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = context.getContentResolver().openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    public static List<SMS> readMessages(Context context, String wordToSearch) {
        Map<String, String> contactMap = getContactList(context);
        Map<String, String> contactImageMap = getContactPhotoList(context);

        List<SMS> smsList = new LinkedList<SMS>();
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        Boolean found = false;

        String[] body = new String[cursor.getCount()];
        String[] number = new String[cursor.getCount()];
        String[] id = new String[cursor.getCount()];
        String[] date = new String[cursor.getCount()];


        int amountofFS = 0;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean caseSensitive = prefs.getBoolean(context.getApplicationContext().getString(R.string.prefs_case_key), false);
        boolean exact = prefs.getBoolean(context.getApplicationContext().getString(R.string.prefs_exact_key), false);
        Log.w("TAG", "" + caseSensitive);
        if (!caseSensitive) {
            wordToSearch = wordToSearch.toLowerCase();
        }
        Log.w("TAG", "" + wordToSearch);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                id[i] = cursor.getString(cursor.getColumnIndexOrThrow("_id")).toString();
                body[i] = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                number[i] = cursor.getString(cursor.getColumnIndexOrThrow("address")).toString();
                date[i] = cursor.getString(cursor.getColumnIndexOrThrow("date")).toString();

                if (exact) {
                    if (!caseSensitive) {
                        found = body[i].toLowerCase().matches(".*\\b" + wordToSearch + "\\b.*");
                    } else {
                        found = body[i].matches(".*\\b" + wordToSearch + "\\b.*");
                    }

                } else {
                    if (!caseSensitive) {
                        found = body[i].toLowerCase().contains(wordToSearch);
                    } else {
                        found = body[i].contains(wordToSearch);
                    }

                }


                if (found) {
                    SMS foundSMS = new SMS(id[i], getContactName(number[i], contactMap), body[i], date[i], getContactName(number[i], contactImageMap));
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

    public static Map getContactList(Context context) {
        String zip = "+" + GetCountryZipCode(context);
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        Map<String, String> countMap = new HashMap<String, String>();
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if (phoneNumber.startsWith("0")) {
                String codeNumber = zip + phoneNumber.substring(1);
                phoneNumber = codeNumber.replaceAll("\\s", "");
            }

            countMap.put(phoneNumber, name);
        }
        phones.close();

        return countMap;
    }

    /*public static List<Contact> getContacts(Context context){
        Map<String, String> contactMap = getContactList(context);
        Map<String, String> contactImageMap = getContactPhotoList(context);
        String name;
        String image;
        List<Contact> cLict;

        for (Map.Entry<String, String> entry : contactMap.entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            name = entry.getValue();

        }
        for (Map.Entry<String, String> entry : contactImageMap.entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            name = entry.getValue();
        }

        return cLict;

    }*/

    public static Map getContactPhotoList(Context context) {
        String zip = "+" + GetCountryZipCode(context);
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        Map<String, String> countMap = new HashMap<String, String>();
        while (phones.moveToNext()) {

            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String image = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            if (phoneNumber.startsWith("0")) {
                String codeNumber = zip + phoneNumber.substring(1);
                phoneNumber = codeNumber.replaceAll("\\s", "");
            }

            countMap.put(phoneNumber, image);
        }
        phones.close();

        return countMap;
    }

    public static String getContactName(String number, Map<String, String> cMap) {

        Map<String, String> contactMap = cMap;
        String contactName;
        contactName = contactMap.get(number);
        if (contactName != null) {
            return contactName;
        } else {
            return number;
        }
    }

    public static String GetCountryZipCode(Context context) {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        // TODO Auto-generated method stub
        int targetWidth = 50;
        int targetHeight = 50;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }


}
