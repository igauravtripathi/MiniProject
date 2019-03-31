package com.example.darkiee.miniproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText email,password;
    private Button register;
    private  static  String URL="https://miniprojectlr.000webhostapp.com/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.login);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        register();

    }

    private void register() {

        final ProgressDialog progressBar= new ProgressDialog(this);
        final String emails=email.getText().toString();
        final String pswd=password.getText().toString();

        if(emails.length()!=0&&pswd.length()!=0){
            //if everything is fine
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.dismiss();
                            if(response.equals("error")){
                                Toast.makeText(getApplicationContext(), "Already Exists", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                //storing the user in shared preferences
                                User user=new User(emails,pswd);
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                //starting the profile activity
                                startActivity(new Intent(getApplicationContext(), Users.class));
                                finish();
                            }
                        }



                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", emails);
                    params.put("password", pswd);
                    return params;
                }
            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }



        else{

            Toast.makeText(getApplicationContext(),"Please Enter email and password",Toast.LENGTH_SHORT).show();
        }
    }
    }


