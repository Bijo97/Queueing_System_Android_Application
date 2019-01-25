package com.example.n7t.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GuestActivity extends AppCompatActivity {

    String URl="https://opensource.petra.ac.id/~m26415171/Website/";
    public Intent intent;
    private String id_customer;
    private String user_customer;

    private String QR_result;
    private String id_layanan;
    private String id_cabang, alamat_cabang;
    private String id_perusahaan, nama_perusahaan, no_antrian,data_antrian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuguest);
        SharedPreferences sp = getSharedPreferences("queue", Context.MODE_PRIVATE);
        id_customer = sp.getString("id_customer",null);
        user_customer = sp.getString("user_customer",null);
        id_perusahaan = sp.getString("id_perusahaan",null);
        TextView tv_customer = (TextView) findViewById(R.id.text_customer);
        String customer = "ID:"+id_customer+"\n"+"User:"+user_customer;
        tv_customer.setText(customer);
    }

    public void FungsiClick(View view) {
        SharedPreferences sp = getSharedPreferences("queue", Context.MODE_PRIVATE);
        id_perusahaan = sp.getString("id_perusahaan",null);
        if(view.getId()==R.id.b_queue){
            if (id_perusahaan != null) {
                Intent intentMain = new Intent(GuestActivity.this, QueueActivity.class);
                GuestActivity.this.startActivity(intentMain);
            }
            else Toast.makeText(GuestActivity.this,"Belum masuk antrian!",Toast.LENGTH_LONG).show();
        }
        else if(view.getId()==R.id.b_scan){
            Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            startActivityForResult(intent, 1);
        }
        else if(view.getId()==R.id.b_sett){
            Intent intentMain = new Intent(GuestActivity.this, SettingActivity.class);
            GuestActivity.this.startActivity(intentMain);
        }
        else if(view.getId()==R.id.b_notif){
            Intent intentMain = new Intent(GuestActivity.this, NotifActivity.class);
            GuestActivity.this.startActivity(intentMain);
        }
        else if(view.getId()==R.id.b_exit){
            this.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK){
            if(requestCode==1) {
                String contents = intent.getStringExtra(Intents.Scan.RESULT);
                String formatName = intent.getStringExtra(Intents.Scan.RESULT_FORMAT);
                QR_result = contents;
                String[] arr = QR_result.split("_");

                //[0] - NAMA PERUSAHAAN
                //[1] - ALAMAT CABANG
                //[2] - ID LAYANAN
                //[3] - NO ANTRI
                nama_perusahaan = arr[0];
                alamat_cabang = arr[1];
                id_layanan = arr[2];
                no_antrian = arr[3];
                new Antri().execute();
            }
        }

    }


    public class Antri extends AsyncTask<Void ,Void ,Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Calendar c = Calendar.getInstance();


            SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
            String tgl = df1.format(c.getTime());

            SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
            String waktumulai = df2.format(c.getTime());

            HttpHandler sh = new HttpHandler();
            String url = URl+"android_insert_antrian.php?layanan="+id_layanan+"&tgl="+tgl+"&customer="+id_customer+"&no_antrian="+no_antrian;

            data_antrian = sh.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            String[] arr = data_antrian.split("_");
            //[0] - ID PERUSAHAAN
            //[1] - ID CABANG
            id_perusahaan = arr[0];
            id_cabang = arr[1];
            id_cabang = id_cabang.substring(0, id_cabang.length() - 1);

            SharedPreferences sp = getSharedPreferences("queue", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.putString("no_antrian",no_antrian);
            e.putString("id_perusahaan",id_perusahaan);
            e.putString("nama_perusahaan",nama_perusahaan);
            e.putString("id_cabang",id_cabang);
            e.putString("alamat_cabang",alamat_cabang);
            e.putString("id_layanan",id_layanan);
            e.commit();
            Toast.makeText(GuestActivity.this,"Terantrikan",Toast.LENGTH_LONG).show();
            Intent intentMain = new Intent(GuestActivity.this , QueueActivity.class);
            GuestActivity.this.startActivity(intentMain);

        }
    }


}
