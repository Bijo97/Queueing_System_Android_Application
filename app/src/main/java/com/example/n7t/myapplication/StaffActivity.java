package com.example.n7t.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StaffActivity extends AppCompatActivity {
    String URl="https://opensource.petra.ac.id/~m26415171/Website/";
        public static String id_loket;
        private String nomor_sekarang;
        public static int detik;
        public static int menit;
        public static int jam;
        public static int inc;ProgressDialog pDialog;
        Button b_next,b_st;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pegawai);
        inc=0;
        b_next = (Button) findViewById(R.id.b_next);
        b_st = (Button) findViewById(R.id.b_st);
        b_next.setEnabled(false);
        b_next.setText("START");
        b_st.setText("OPEN");
        new Getnomor_sekarang().execute();
        /*final Handler h = new Handler();
        final Runnable r = new Runnable() {
        @Override
        public void run() {
            detik = detik + inc;
            if(detik >= 60){detik = 0;menit = menit + 1;}
            if(menit >= 60){menit = 0;jam = jam + 1;}
            h.postDelayed(this, 1000); //ms
        }
        };
        h.postDelayed(r, 1000);*/
        }

        public class Getnomor_sekarang extends AsyncTask<Void ,Void ,Void> {
                @Override
                protected Void doInBackground(Void... params) {
                        HttpHandler sh = new HttpHandler();
                        String url = URl+"android_get_nomorsekarang.php?"+"id_loket="+id_loket;
                        nomor_sekarang = sh.makeServiceCall(url);
                        return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                        TextView tv_noantrian = (TextView) findViewById(R.id.text_noantrian);
                        tv_noantrian.setText(nomor_sekarang);
                        Toast.makeText(StaffActivity.this,"Berikutnya!",Toast.LENGTH_LONG).show();
                }
        }

        public class Start extends AsyncTask<Void ,Void ,Void> {
                @Override
                protected Void doInBackground(Void... params) {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
                        String waktuselesai = df2.format(c.getTime());

                        HttpHandler sh = new HttpHandler();
                        String url = URl+"android_start_loket.php?"+"id_loket="+id_loket+"&waktu_mulai="+waktuselesai+"&nomor="+nomor_sekarang;
                        nomor_sekarang = sh.makeServiceCall(url);
                        Log.v("JUDUL",nomor_sekarang);
                        return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    pDialog.dismiss();
                    if(!TextUtils.isEmpty(nomor_sekarang)) {
                        TextView tv_noantrian = (TextView) findViewById(R.id.text_noantrian);
                        tv_noantrian.setText(nomor_sekarang);
                        Toast.makeText(StaffActivity.this, "Berikutnya!", Toast.LENGTH_LONG).show();
                    }
                }
        }
    public class Done extends AsyncTask<Void ,Void ,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
            String waktuselesai = df2.format(c.getTime());

            HttpHandler sh = new HttpHandler();
            String url = URl+"android_done_loket.php?"+"id_loket="+id_loket+"&waktu_selesai="+waktuselesai+"&nomor="+nomor_sekarang;
            nomor_sekarang = sh.makeServiceCall(url);Log.v("JUDUL",nomor_sekarang);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            if(!TextUtils.isEmpty(nomor_sekarang)) {
                TextView tv_noantrian = (TextView) findViewById(R.id.text_noantrian);
                tv_noantrian.setText(nomor_sekarang);
                Toast.makeText(StaffActivity.this, "Berikutnya!", Toast.LENGTH_LONG).show();
            }
        }
    }
        public class Open extends AsyncTask<Void ,Void ,Void> {
                @Override
                protected Void doInBackground(Void... params) {
                        HttpHandler sh = new HttpHandler();
                        String url = URl+"android_open_loket.php?"+"id_loket="+id_loket;
                        nomor_sekarang = sh.makeServiceCall(url);Log.v("JUDUL",nomor_sekarang);
                        return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    pDialog.dismiss();
                    if(!TextUtils.isEmpty(nomor_sekarang)) {
                        TextView tv_noantrian = (TextView) findViewById(R.id.text_noantrian);
                        tv_noantrian.setText(nomor_sekarang);
                        Toast.makeText(StaffActivity.this, "Berikutnya!", Toast.LENGTH_LONG).show();
                    }
                }
        }
        public class Close extends AsyncTask<Void ,Void ,Void> {
                @Override
                protected Void doInBackground(Void... params) {
                        HttpHandler sh = new HttpHandler();
                        String url = URl+"android_close_loket.php?"+"id_loket="+id_loket;
                        sh.makeServiceCall(url);
                        return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    pDialog.dismiss();
                }
        }
        public void FungsiClick(View view) {
            pDialog = new ProgressDialog(StaffActivity.this);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(false);
            pDialog.show();
                if(view.getId()==R.id.b_next){
                    if(b_next.getText().toString().equals("START")) {
                        new Start().execute();
                        b_next.setText("DONE");
                    }
                    else{
                        new Done().execute();
                        b_next.setText("START");
                        inc=0;
                    }
                }
                else if(view.getId()==R.id.b_st){
                    if(b_st.getText().toString().equals("OPEN")) {
                        new Open().execute();
                        if(!TextUtils.isEmpty(nomor_sekarang)) {
                            b_next.setEnabled(true);
                            b_st.setText("CLOSE");
                            inc=1;
                        }
                    }
                    else{
                        new Close().execute();
                            b_st.setText("OPEN");
                            b_next.setEnabled(false);
                            inc=0;
                    }
                }
        }
}