package com.example.n7t.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class QueueActivity extends AppCompatActivity {
    String URl="https://opensource.petra.ac.id/~m26415171/Website/";
    private String id_cabang, alamat_cabang,id_perusahaan, nama_perusahaan,no_antrian,id_layanan,id_customer;
    ProgressDialog pDialog;
    public String times;

    private int detik,menit,jam;
    TextView tv_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue);
        SharedPreferences sp = getSharedPreferences("queue", Context.MODE_PRIVATE);
        id_cabang=sp.getString("id_cabang",null);
        alamat_cabang=sp.getString("alamat_cabang",null);
        id_perusahaan=sp.getString("id_perusahaan",null);
        nama_perusahaan=sp.getString("nama_perusahaan",null);
        no_antrian=sp.getString("no_antrian",null);
        id_layanan=sp.getString("id_layanan",null);
        id_customer=sp.getString("id_customer",null);
        TextView tv_noantrian = (TextView) findViewById(R.id.text_noantrian);
        TextView tv_perusahaan = (TextView) findViewById(R.id.text_perusahaan);
        tv_noantrian.setText(no_antrian);
        tv_perusahaan.setText(nama_perusahaan + "\n" + alamat_cabang + "\n" + id_layanan);
        new Getestimasi().execute();
        /*tv_timer = (TextView) findViewById(R.id.text_timer);

        final Handler h = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                detik = detik + 1;
                if(detik >= 60){detik = 0;menit = menit + 1;}
                if(menit >= 60){menit = 0;jam = jam + 1;}

                tv_timer.setText("Timer: " + String.valueOf(jam) + ":" + String.valueOf(menit) + ":" + String.valueOf(detik));
                h.postDelayed(this, 1000); //ms
            }
        };
        h.postDelayed(r, 1000); // one second in ms
        final Handler h2 = new Handler();
        final Runnable r2 = new Runnable() {
            @Override
            public void run() {

                new Getestimasi().execute();
                h.postDelayed(this, 10000); //ms
            }
        };
        h2.postDelayed(r, 10000); // one second in ms*/
    }


    public class Getestimasi extends AsyncTask<Void ,Void ,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            String url= "https://opensource.petra.ac.id/~m26415171/Website/android_estimasi_queue.php?id_perusahaan="+id_perusahaan+"&id_cabang="+id_cabang+"&id_layanan="+id_layanan+"&no_antrian="+no_antrian;

            times = sh.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView tv_estimasi = (TextView) findViewById(R.id.text_estimasi);
            tv_estimasi.setText("Estimasi: " + times);
        }
    }

    public class CancelQueue extends AsyncTask<Void ,Void ,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            String url= "https://opensource.petra.ac.id/~m26415171/Website/android_cancel_queue.php?id_perusahaan="+id_perusahaan+"&id_cabang="+id_cabang+"&id_layanan="+id_layanan+"&id_customer="+id_customer;

            times = sh.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            SharedPreferences sp = getSharedPreferences("queue", Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp.edit();
            e.remove("id_perusahaan");
            e.commit();
            pDialog.dismiss();
            Done();
        }
    }
    public void Done(){
        this.finish();
    }
    public void FungsiClick(View view) {
        if(view.getId()==R.id.b_back){
            this.finish();
        }
        else if(view.getId()==R.id.b_cancel){
            pDialog = new ProgressDialog(QueueActivity.this);
            pDialog.setMessage("Cancelling...");
            pDialog.setCancelable(false);
            pDialog.show();
            new CancelQueue().execute();
        }
    }
}
