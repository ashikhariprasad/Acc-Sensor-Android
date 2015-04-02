package com.sjsu.proj.accsensor;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sjsu.proj.accsensor.UserManager.AsyncRegisterUser;


public class RegisterActivity extends ActionBarActivity implements AsyncResponse {

    EditText mFirstName,mLastName, mRegisterPhone,mRegisterPassword;
    Button mRegister;
    TextView mLogin;

    String sFirstName,sLastName,sPhoneNumber,sRegisterPassword,sResult;

    Context c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirstName = (EditText)findViewById(R.id.firstname);
        mLastName = (EditText)findViewById(R.id.lastname);
        mRegisterPhone = (EditText)findViewById(R.id.reg_phone);
        mRegisterPassword = (EditText)findViewById(R.id.reg_password);
        mRegister = (Button)findViewById(R.id.btnRegister);
        mLogin = (TextView)findViewById(R.id.link_to_login);

        c = RegisterActivity.this;

        final RegisterActivity ref = this;

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sFirstName = mFirstName.getText().toString();
                sLastName = mLastName.getText().toString();
                sPhoneNumber = mRegisterPhone.getText().toString();
                sRegisterPassword = mRegisterPassword.getText().toString();
                if(sFirstName != null && sFirstName.length() > 0){
                    if(sLastName != null && sLastName.length() >  0){
                        if(sPhoneNumber != null && sPhoneNumber.length() > 0){
                            if(sRegisterPassword != null && sRegisterPassword.length() > 0){
                                AsyncRegisterUser regUser = new AsyncRegisterUser(c,ref);
                                regUser.execute(new String[] {sFirstName,sLastName,sPhoneNumber,sRegisterPassword});
                            }
                            else{
                                Toast.makeText(RegisterActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT);
                            }
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Email Id cannot be empty",Toast.LENGTH_SHORT);
                        }
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Last name cannot be empty",Toast.LENGTH_SHORT);
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this,"First name cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void postRegistrationHandler(String s){
        if(s.equalsIgnoreCase("success")){
            finish();
        }
        else{
            Toast.makeText(RegisterActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
            super.recreate();
        }
    }
}
