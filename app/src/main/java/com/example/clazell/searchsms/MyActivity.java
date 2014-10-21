package com.example.clazell.searchsms;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private TextView heading;
        private Button btnSearch;
        private EditText searchWord;
        private Spinner spinner;
        private ListView listview;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            searchWord = (EditText) rootView.findViewById(R.id.editText);
            spinner = (Spinner) rootView.findViewById(R.id.spinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.spinner_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            btnSearch = (Button) rootView.findViewById(R.id.btn_search);
            btnSearch.setVisibility(View.VISIBLE);
            btnSearch.setEnabled(true);
            btnSearch.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

                    String wordToSearch = searchWord.getText().toString();
                    Boolean found;

                    String[] body = new String[cursor.getCount()];
                    String[] number = new String[cursor.getCount()];

                    String[] foundSMS = new String[cursor.getCount()];
                    int amountofFS = 0;

                    if(cursor.moveToFirst()){
                        for(int i=0;i<cursor.getCount();i++) {
                            body[i] = cursor.getString(cursor.getColumnIndexOrThrow("body")).toString();
                            found = body[i].contains(wordToSearch);

                            if(found){
                                foundSMS[amountofFS] = body[i];
                                amountofFS++;
                                found = false;
                            }
                            cursor.moveToNext();
                        }
                    }
                    cursor.close();

                    listview = (ListView) rootView.findViewById(R.id.listView);

                    ArrayList<String> list = new ArrayList<String>();
                    for (int i = 0; i < foundSMS.length; i++) {
                        list.add(foundSMS[i]);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                            android.R.layout.simple_list_item_1, list);

                    listview.setAdapter(adapter);
                }
            });



            return rootView;
        }
    }
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}
