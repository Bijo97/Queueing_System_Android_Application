package com.example.n7t.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Bijo97 on 06/12/2017.
 */

public class NotifActivity extends AppCompatActivity {
    String URl="https://opensource.petra.ac.id/~m26415171/Website/";
    private String alamat_cabang, nama_perusahaan, id_layanan, id_customer;
    ProgressDialog pDialog;
    String note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        SharedPreferences sp = getSharedPreferences("queue", Context.MODE_PRIVATE);
        alamat_cabang=sp.getString("alamat_cabang",null);
        nama_perusahaan=sp.getString("nama_perusahaan",null);
        id_layanan=sp.getString("id_layanan",null);
        id_customer=sp.getString("id_customer",null);

        TextView tv_perusahaan = (TextView) findViewById(R.id.text_perusahaan);
        tv_perusahaan.setText(nama_perusahaan + "\n" + alamat_cabang + "\n" + id_layanan);
        new GetNotif().execute();
    }

    public class GetNotif extends AsyncTask<Void ,Void ,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            String url= "https://opensource.petra.ac.id/~m26415171/Website/ambilpesan.php?id_customer="+id_customer;

            note = sh.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView tv_note = (TextView) findViewById(R.id.note);
            tv_note.setText(note);
        }
    }

    public void FungsiClick(View view) {
        this.finish();
    }
}
