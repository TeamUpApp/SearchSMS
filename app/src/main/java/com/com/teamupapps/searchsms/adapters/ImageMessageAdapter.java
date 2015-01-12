package com.com.teamupapps.searchsms.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.com.teamupapps.searchsms.models.MMS;
import com.com.teamupapps.searchsms.models.SMS;
import com.com.teamupapps.searchsms.utils.SearchUtils;
import com.teamupapps.searchsms.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by clazell on 12/01/2015.
 */
public class ImageMessageAdapter extends
        ArrayAdapter<MMS> {

    private final List<MMS> list;
    private final Activity context;
    private LayoutInflater inflater;

    public ImageMessageAdapter(Activity context, List<MMS> list) {
        super(context, R.layout.list_row, list);
        this.context = context;
        this.list = list;
    }


    static class ViewHolder {
        protected ImageView image;
        protected ImageView imageMessage;
        protected TextView name;
        protected TextView message;
        protected ImageButton buttonMore;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.image_list_row, null);
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.image = (ImageView) convertView.findViewById(R.id.image_profile);
        viewHolder.imageMessage = (ImageView) convertView.findViewById(R.id.image_message);
        viewHolder.name = (TextView) convertView.findViewById(R.id.txt_contact_name);
        viewHolder.message = (TextView) convertView.findViewById(R.id.txt_message);

        final MMS mms = list.get(position);

        viewHolder.name.setText(list.get(position).getName());
        viewHolder.message.setText(list.get(position).getContent());
        viewHolder.imageMessage.setImageBitmap(list.get(position).getBitmap());
        viewHolder.buttonMore = (ImageButton) convertView.findViewById(R.id.btn_settings);
        viewHolder.buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, viewHolder.buttonMore);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.drop_down_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.share) {
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, mms.getContent());
                            sendIntent.setType("text/plain");
                            context.startActivity(sendIntent);
                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });


        /*if (list.get(position).getImageURI() != null) {
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
        }*/
        return convertView;

    }

}

