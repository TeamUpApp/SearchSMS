package com.com.teamupapps.searchsms.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.com.teamupapps.searchsms.models.MMS;
import com.com.teamupapps.searchsms.utils.BitmapUtils;
import com.teamupapps.searchsms.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.com.teamupapps.searchsms.utils.BitmapUtils.BitmapWorkerTask;

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


        @InjectView(R.id.image_message)
        ImageView imageMessage;
        @InjectView(R.id.txt_contact_name)
        TextView name;
        @InjectView(R.id.txt_message)
        TextView message;
        @InjectView(R.id.btn_settings)
        ImageButton buttonMore;
        /*@InjectView(R.id.btn_view)
        ImageButton buttonView;*/

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            if (inflater == null)
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.image_list_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        final MMS mms = list.get(position);

        holder.name.setText(list.get(position).getName());
        holder.message.setText(list.get(position).getContent());

        BitmapWorkerTask task = new BitmapWorkerTask(holder.imageMessage, context);
        task.execute(list.get(position).getBitmap());

        holder.buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Bitmap icon = list.get(position).getBitmap();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Log.i("uri", list.get(position).getBitmap());
                Uri partURI = Uri.parse("content://mms/part/" + list.get(position).getBitmap());
                share.putExtra(Intent.EXTRA_STREAM, partURI);
                context.startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

/*        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setType("image/jpeg");
                Uri partURI = Uri.parse("content://mms/part/" + list.get(position).getBitmap());
                intent.putExtra(Intent.EXTRA_STREAM, partURI);
                context.startActivity(Intent.createChooser(intent, "View Image"));
            }
        });*/

/*        if (list.get(position).getImageURI() != null) {
            try {
                Bitmap bitmap = SearchUtils.getRoundedShape(MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(list.get(position).getImageURI())));
                holder.image.setImageBitmap(bitmap);
                System.out.println(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                holder.image.setImageResource(R.drawable.contact);
            } catch (IOException e) {
                e.printStackTrace();
                holder.image.setImageResource(R.drawable.contact);
            }


        } else {
            Log.w("tag", "os null");
            holder.image.setImageResource(R.drawable.contact);
        }*/
        return convertView;

    }


}

