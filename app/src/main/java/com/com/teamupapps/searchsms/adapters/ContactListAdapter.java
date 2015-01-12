package com.com.teamupapps.searchsms.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.com.teamupapps.searchsms.models.Contact;
import com.com.teamupapps.searchsms.utils.SearchUtils;
import com.teamupapps.searchsms.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;



/**
 * Created by clazell on 8/01/2015.
 */
public class ContactListAdapter extends ArrayAdapter<Contact> {
    private final List<Contact> list;
    private final Activity context;
    private LayoutInflater inflater;

    public ContactListAdapter(Activity context, List<Contact> list) {
        super(context, R.layout.contact_row, list);
        this.context = context;
        this.list = list;
    }


    static class ViewHolder {
        protected ImageView image;
        protected TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.contact_row, null);
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.image = (ImageView) convertView.findViewById(R.id.image_profile);
        viewHolder.name = (TextView) convertView.findViewById(R.id.txt_contact_name);

        final Contact cntct = list.get(position);

        viewHolder.name.setText(list.get(position).getName());


        if (list.get(position).getImageURI() != null) {
            try {
                Bitmap bitmap = SearchUtils.getRoundedShape(MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(list.get(position).getImageURI())));
                viewHolder.image.setImageBitmap(bitmap);
                System.out.println(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                viewHolder.image.setImageResource(R.drawable.contact);
            } catch (IOException e) {
                e.printStackTrace();
                viewHolder.image.setImageResource(R.drawable.contact);
            }


        } else {
            Log.w("tag", "os null");
            viewHolder.image.setImageResource(R.drawable.contact);
        }
        return convertView;

    }
}
