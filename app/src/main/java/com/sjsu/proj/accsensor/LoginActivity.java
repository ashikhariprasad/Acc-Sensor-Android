package com.sjsu.proj.accsensor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sjsu.proj.accsensor.UserManager.AsyncCheckLogin;


public class LoginActivity extends ActionBarActivity {

    EditText mPhone, mPassword;
    String sPhone, sPassword;
    Button mSignin;
    TextView mRegister;
    Context context;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPhone = (EditText)findViewById(R.id.phone_user);
        mPassword = (EditText)findViewById(R.id.password_user);
        mSignin = (Button)findViewById(R.id.btnLogin);
        mRegister = (TextView)findViewById(R.id.link_to_register);
        context = this.getApplicationContext();

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPhone = mPhone.getText().toString();
                sPassword = mPassword.getText().toString();
                if(sPhone != null && sPhone.length()>0){
                    Log.d("PHONE", sPhone);
                    if(sPassword != null && sPassword.length()>0){
                        Log.d("PASSWORD", sPassword);
                        AsyncCheckLogin checkLogin = new AsyncCheckLogin(context);
                        checkLogin.execute(new String[]{sPhone,sPassword});
                    }else{
                        Toast.makeText(LoginActivity.this, R.string.empty_password,Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, R.string.empty_phone,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void onRegisterClick(View v){
        Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
