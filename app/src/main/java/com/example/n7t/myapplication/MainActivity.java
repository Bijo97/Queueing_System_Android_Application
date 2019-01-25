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
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String URl="https://opensource.petra.ac.id/~m26415171/Website/";
    SharedPreferences sp ;
    private String id_customer,dataQueue,user_customer;ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("queue", Context.MODE_PRIVATE);
        EditText e_pass = (EditText) findViewById(R.id.e_pass);
        e_pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public class Login extends AsyncTask<Void ,Void ,Void> {
        EditText e_user = (EditText) findViewById(R.id.e_user);
        EditText e_pass = (EditText) findViewById(R.id.e_pass);
        String user = e_user.getText().toString();
        String pass = e_pass.getText().toString();
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = URl+"android_login_customer.php?user="+user+"&pass="+pass;
            id_customer = sh.makeServiceCall(url);
            id_customer = id_customer.substring(0, id_customer.length() - 1);//ECHO MENGANDUNG SPASI DI AKHIR KATA
            if (!id_customer.equals("false")) {
                String[] get = id_customer.split("_");
                id_customer = get[0];
                user_customer = get[1];
                url = URl + "android_request_queue.php?id_customer=" + id_customer;
                dataQueue = sh.makeServiceCall(url);
                dataQueue = dataQueue.substring(0, dataQueue.length() - 1);
                String token = new RegisterFirebase().getToken();
                url = URl + "android_update_token.php?id_customer=" + id_customer+"&token="+token;
                String tes =sh.makeServiceCall(url);
                System.out.println(tes);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            EditText e_pass = (EditText) findViewById(R.id.e_pass);
            e_pass.setText("");
            pDialog.dismiss();
            if (!id_customer.equals("false")) {
                SharedPreferences.Editor e = sp.edit();
                e.putString("id_customer",id_customer);
                e.putString("user_customer",user_customer);
                if(dataQueue.equals("Empty")){
                    e.remove("id_perusahaan");
                }
                else{
                    String[] data = dataQueue.split(",");
                    String id_layanan=data[0],id_perusahaan=data[1],id_cabang=data[2],no_antrian=data[4],nama_perusahaan=data[5],alamat_cabang=data[6];
                    e.putString("id_cabang",id_cabang);
                    e.putString("id_perusahaan",id_perusahaan);
                    e.putString("id_layanan",id_layanan);
                    e.putString("no_antrian",no_antrian);
                    e.putString("nama_perusahaan",nama_perusahaan);
                    e.putString("alamat_cabang",alamat_cabang);
                }
                e.commit();

                Intent intentMain = new Intent(MainActivity.this , GuestActivity.class);
                MainActivity.this.startActivity(intentMain);
            }
            else
            {
                Toast.makeText(MainActivity.this,"Data akun salah",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void FungsiClick(View view) {
        if(view.getId()==R.id.b_login){
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(false);
            pDialog.show();
            new Login().execute();
        }
        else if(view.getId()==R.id.t_register){
            Intent intentMain = new Intent(MainActivity.this , RegisterCustomer.class);
            MainActivity.this.startActivity(intentMain);
        }
        else if(view.getId()==R.id.t_staff){
            Intent intentMain = new Intent(MainActivity.this , LoginStaff.class);
            MainActivity.this.startActivity(intentMain);
        }
    }
}
