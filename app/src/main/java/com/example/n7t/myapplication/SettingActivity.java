package com.example.n7t.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    String URl="https://opensource.petra.ac.id/~m26415171/Website/";
    SharedPreferences sp ;
    private String id_customer;ProgressDialog pDialog;
    private int estimasi;Spinner spin;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("queue", Context.MODE_PRIVATE);
        id_customer=sp.getString("id_customer",null);
        spin = (Spinner) findViewById(R.id.spinner);
        new GetTime().execute();
    }

    public class GetTime extends AsyncTask<Void ,Void ,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            String url= "https://opensource.petra.ac.id/~m26415171/Website/android_get_time.php?id_customer="+id_customer;

            time = sh.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView tv_time = (TextView) findViewById(R.id.text_time);
            tv_time.setText(time);
        }
    }

    public class saveSetting extends AsyncTask<Void ,Void ,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = URl+"android_set_notif.php?"+"estimasi="+estimasi+"&user="+id_customer;
            String tes = sh.makeServiceCall(url);
            System.out.println(tes);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            new GetTime().execute();
            Toast.makeText(SettingActivity.this,"Saved!",Toast.LENGTH_LONG).show();
        }
    }

    public void FungsiClick(View view) {
        if(view.getId()==R.id.b_save){
            estimasi = Integer.valueOf(spin.getSelectedItem().toString());
            pDialog = new ProgressDialog(SettingActivity.this);
            pDialog.setMessage("Loading data ke server");
            pDialog.setCancelable(false);
            pDialog.show();
            new saveSetting().execute();
        }
        else if(view.getId()==R.id.b_exit){
            SettingActivity.this.finish();
        }
    }
}
