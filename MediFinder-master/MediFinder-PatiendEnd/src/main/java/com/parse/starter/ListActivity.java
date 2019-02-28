package com.parse.starter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListActivity extends Activity {

    private ListView listView;
    private ProgressDialog progressDialog;
    private List<SampleData> sampleData;
    private ArrayList<SampleData> sampleList;
    private SampleAdapter sampleAdapter;
    private EditText etSearch;

    ArrayList<SampleData> arraylist = new ArrayList<SampleData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        listView=(ListView) findViewById(R.id.list_view);

        BackGroundTask bt = new BackGroundTask();
        bt.execute();
        etSearch=(EditText) findViewById(R.id.et_search);
        etSearch.setSingleLine(true);


        etSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                Log.i("hii12",text);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent=new Intent(ListActivity.this, addSymptoms.class);
                        Log.i("Chosen",sampleList.get(i).getId().toString());
                        Log.i("Name Chosen",sampleList.get(i).getName().toString());
                        intent.putExtra("ID",sampleList.get(i).getId().toString());
                        intent.putExtra("Symptom",sampleList.get(i).getName().toString());
                        startActivity(intent);
                    }
                });

                sampleAdapter.filter(text);


            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }
        });
    }

    private class BackGroundTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(ListActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
                JSONArray jsonArray = jsonObject.getJSONArray("symptoms");
                sampleData=new ArrayList<SampleData>();

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObjectInside = jsonArray.getJSONObject(i);

                    String id = jsonObjectInside.getString("ID");

                    String name = jsonObjectInside.getString("Name");
                    Log.i("symptoms1",name);
                    sampleData.add(new SampleData(id, name));
                    Log.e("id",id+"");
                    Log.e("name",name+"");

                }

                handlePostsList(sampleData);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Log.i("jk789",e.toString());
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
    }// End of Background AsyncTask


    private void handlePostsList(final List<SampleData> sampleData) {
        this.sampleData = sampleData;
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    // logic for retrieve data first time in list view.
                    sampleList = new ArrayList<SampleData>();

                    for (int i = 0; i < sampleData.size(); i++) {

                        sampleList.add(new SampleData(sampleData.get(i).getId().toString(),sampleData.get(i).getName().toString()));
                    }

                    sampleAdapter= new SampleAdapter(ListActivity.this, sampleList);
                    listView.setAdapter(sampleAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent=new Intent(ListActivity.this, addSymptoms.class);
                            Log.i("Chosen",sampleList.get(i).getId().toString());
                            Log.i("Name Chosen",sampleList.get(i).getName().toString());
                            intent.putExtra("ID",sampleList.get(i).getId().toString());
                            intent.putExtra("Symptom",sampleList.get(i).getName().toString());
                            startActivity(intent);
                        }
                    });

                } catch (Exception e) {


                    e.printStackTrace();
                }
            }
        });
    }


    public class SampleAdapter extends BaseAdapter
    {
        Context context;
        List<SampleData> sampleData=null;
        ArrayList<SampleData> arraylist;
        LayoutInflater inflater;


        public SampleAdapter(Context context, List<SampleData> sampleData) {
            this.context = context;
            this.sampleData = sampleData;
            this.arraylist = new ArrayList<SampleData>();
            this.arraylist.addAll(sampleData);
        }


        public class ViewHolder {
            TextView txtID,txtName;

        }

        public int getCount() {

            return sampleData.size();
        }

        public Object getItem(int position) {
            return sampleData.get(position);
        }

        public long getItemId(int position) {
            return sampleData.indexOf(getItem(position));
        }

        public View getView(final int position, View convertView, ViewGroup parent)
        {


            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                assert mInflater != null;
                convertView = mInflater.inflate(R.layout.custom_row, null);
                holder = new ViewHolder();
                holder.txtID = (TextView) convertView.findViewById(R.id.txt_id);
                holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.txtID.setText(sampleData.get(position).getId());
            holder.txtName.setText(sampleData.get(position).getName());
            return convertView;
        }

        // Filter Class
        public void filter(String charText) {
            Log.i("I am coming ing",charText);
            charText = charText.toLowerCase(Locale.getDefault());
            sampleData.clear();
            if (charText.length() == 0) {
                sampleData.addAll(arraylist);

            } else {
                for (SampleData st : arraylist) {
                    if (st.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        sampleData.add(st);
                    }else if (st.getId().toLowerCase(Locale.getDefault()).contains(charText)) {
                        sampleData.add(st);
                    }
                    else if (st.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        sampleData.add(st);
                    }
                }
            }
            notifyDataSetChanged();
        }

    }


    public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = getAssets().open("symtoms.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


}