package com.example.shubhamk.sms_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main5Activity extends AppCompatActivity {
    ListActivity g;

    String Json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ArrayList<String> selections=new ArrayList<String>();
    JSONObject listitem =new JSONObject();
    Button btnLookup;
    List<Item> items;
    ListView listView;
    ItemsListAdapter myItemsListAdapter;
    CheckBox c;
    boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        listView = (ListView) findViewById(R.id.listview);
        btnLookup = (Button) findViewById(R.id.lookup);
        c=(CheckBox)findViewById(R.id.selectall);
        initItems();
        myItemsListAdapter = new ItemsListAdapter(this, items, b);

        listView.setAdapter(myItemsListAdapter);

        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                myItemsListAdapter.update(items,b);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Main5Activity.this,
                        ((Item) (parent.getItemAtPosition(position))).ItemString,
                        Toast.LENGTH_LONG).show();
            }
        });


        btnLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int key = 1;
                try {

                    String str = "Check items:\n";
                    JSONObject jsonObject = new JSONObject();
                    for (int i = 0; i < items.size(); i++) {

                        if (items.get(i).isChecked()) {

                            listitem.put((key++)+"",items.get(i).ItemString);
                            // String[] k;
                            //selections+=items.get(i).ItemString;
                            // String hk = items.get(i).ItemString;


                        }
                    }
                    jsonObject.put("rollno", listitem);
                    // listitem.put("count",(int)key-97);
                    String message = listitem.toString();
                    Log.d("0", "0");
                    BackgroundTsk bg = new BackgroundTsk();
                    Log.d("2", "1");
                    Log.d("data",message);
                    bg.execute(message);
                    Log.d("1", "2");
                    Toast.makeText(Main5Activity.this,
                            "dfgh",
                            Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initItems () {
        Json_string = getIntent().getExtras().getString("json_data");
        try {
            jsonObject = new JSONObject(Json_string);
            jsonArray = jsonObject.getJSONArray("response_server");
            items = new ArrayList<Item>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                String s = jo.getString("Rollno");
                String batch = jo.getString("Batch");
                boolean b = true;
                Item item = new Item(s, b, batch);
                items.add(item);
            }

        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    public ListView getListView() {
        return listView;
    }


    public class Item {
        boolean checked;
        String ItemString;
        String fb;

        Item(String t, boolean b, String batch) {
            ItemString = t;
            checked = b;
            fb = batch;
        }

        public boolean isChecked() {
            return checked;
        }
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView text;
        TextView bat;
    }


    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> list;
        boolean b;
        private boolean[] mChecked;
        private boolean all;
        private CompoundButton.OnCheckedChangeListener mListener;

        ItemsListAdapter(Context c, List<Item> l,boolean b) {
            context = c;
            list = l;
            this.all =b;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).checked;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_row, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.rowCheckBox);
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                viewHolder.bat = (TextView) rowView.findViewById(R.id.Batch);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.checkBox.setChecked(list.get(position).checked);// last postion value

            final String itemStr = list.get(position).ItemString;
            final String batch = list.get(position).fb;
            viewHolder.text.setText(itemStr);
            viewHolder.bat.setText(batch);
            viewHolder.checkBox.setTag(position);
            if (all) {
                viewHolder.checkBox.setChecked(true);
            }

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).isChecked();
                    list.get(position).checked = newState;



                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }

        public void update(List<Item> items, boolean b) {
            notifyDataSetChanged();
            this.list=items;
            this.all=b;
        }
    }


    @SuppressLint("StaticFieldLeak")
    class BackgroundTsk extends AsyncTask<String, Void, Void> {
        String add_info;

        @Override
        protected void onPreExecute() {
            //add_info = "https://unribbed-headers.000webhostapp.com/pre.php";
            add_info="https://unribbed-headers.000webhostapp.com/newinsert.php";
        }

        @Override
        protected Void doInBackground(String... json) {
            Log.d("3", "3");
            String mess=json[0];
            Log.d("4", "4");
            try{
                URL url= new URL(add_info);
                Log.d("5", "5");

                HttpURLConnection urlConnect=(HttpURLConnection) url.openConnection();
                Log.d("6", "6");
                urlConnect.setReadTimeout( 10000 /*milliseconds*/ );
                Log.d("7", "7");
                urlConnect.setConnectTimeout( 15000 /* milliseconds */ );
                Log.d("8", "8");
                urlConnect.setRequestMethod("POST");
                Log.d("9", "9");
                urlConnect.setDoOutput(true);
                Log.d("13", "13");
                urlConnect.setDoInput(true);
                Log.d("9", "10");urlConnect.setFixedLengthStreamingMode(mess.getBytes().length);
                Log.d("11", "11");urlConnect.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                Log.d("12", "12");urlConnect.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                Log.d("", "");
                //open
                urlConnect.connect();
                Log.d("14", "4");
                //setup send
                OutputStream os = new BufferedOutputStream(urlConnect.getOutputStream());
                Log.d("15", "15");
                os.write(mess.getBytes());
                //clean up
                Log.d("16", "16");
                os.flush();
                Log.d("17", "17");


                // var inString= ConvertStreamToString(urlConnect.inputStream)
            }catch (Exception e){
                Log.d("kj", "doInBackground: idhr");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }


    }

}
