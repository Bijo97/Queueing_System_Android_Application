package com.example.n7t.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by N7T on 11/14/2017.
 */

public class LoginStaff extends AppCompatActivity {
    String URl="https://opensource.petra.ac.id/~m26415171/Website/";
    public static String id_loket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginstaff);
        EditText e_pass = (EditText) findViewById(R.id.e_pass);
        e_pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public class Login extends AsyncTask<Void ,Void ,Void> {
        EditText e_user = (EditText) findViewById(R.id.e_user);
        EditText e_pass = (EditText) findViewById(R.id.e_pass);
        String user = e_user.getText().toString();
        String pass = e_pass.getText().toString();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LoginStaff.this);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = URl+"android_login_staff.php?"+"user="+user+"&pass="+pass;

            //String[] arr = jsonStr.split(" ");
            id_loket = sh.makeServiceCall(url);
            id_loket = id_loket.substring(0, id_loket.length() - 1); //ECHO MENGANDUNG SPASI DI AKHIR KATA
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            if (!id_loket.equals("false")) {
                StaffActivity.id_loket = id_loket;
                Intent intentMain = new Intent(LoginStaff.this , StaffActivity.class);
                LoginStaff.this.startActivity(intentMain);
            }
            else
            {
                Toast.makeText(LoginStaff.this,"Data akun salah",Toast.LENGTH_LONG).show();
            }
        }
    }



    public void FungsiClick(View view) {
        if(view.getId()==R.id.b_login){
            new Login().execute();
        }
    }

}
