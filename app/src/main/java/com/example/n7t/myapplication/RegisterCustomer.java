package com.example.n7t.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by N7T on 11/12/2017.
 */

public class RegisterCustomer extends AppCompatActivity {
    String URl="https://opensource.petra.ac.id/~m26415171/Website/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registercustomer);
        EditText e_pass = (EditText) findViewById(R.id.e_pass);
        e_pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public class Register extends AsyncTask<Void ,Void ,Void> {
        EditText e_user = (EditText) findViewById(R.id.e_user);
        EditText e_pass = (EditText) findViewById(R.id.e_pass);
        String user = e_user.getText().toString();
        String pass = e_pass.getText().toString();
        String token = new RegisterFirebase().getToken();
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(RegisterCustomer.this);
            pDialog.setMessage("Loading data dari server");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            String url = null;
            url = URl+"android_register_customer.php?"+"user="+user+"&pass="+pass+"&token="+token;
            sh.makeServiceCall(url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(RegisterCustomer.this,"Register sukses! silahkan login",Toast.LENGTH_LONG).show();
            //Intent intentMain = new Intent(RegisterCustomer.this , MainActivity.class);
            //RegisterCustomer.this.startActivity(intentMain);
            RegisterCustomer.this.finish();
        }
    }



    public void FungsiClick(View view) {
        new Register().execute();

    }


}