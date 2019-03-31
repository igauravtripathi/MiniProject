package com.example.darkiee.miniproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Users extends AppCompatActivity implements View.OnClickListener {
private Button add;
private TextView name,address,email;
private EditText names,addresses;
    private  static  String URL="https://miniprojectlr.000webhostapp.com/add.php";
    private  static  String URLS="https://miniprojectlr.000webhostapp.com/view.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        name=findViewById(R.id.name);
        names=findViewById(R.id.names);
        address=findViewById(R.id.address);
        email=findViewById(R.id.email);
        add=findViewById(R.id.add);
        addresses=findViewById(R.id.addresses);
        add.setOnClickListener(this);
viewdata();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPrefManager.getInstance(this).logout();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        add();

    }
private  void viewdata(){



        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //creating a json array from the json string
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            //looping through all the elements in json array
                            for (int i = 0; i < jsonArray.length(); i++) {

                                //getting json object from the json array
                                JSONObject obj = jsonArray.getJSONObject(i);

                                email.setText(obj.getString("Email"));
                                name.setText(obj.getString("Name"));
                                address.setText(obj.getString("Address"));
                                //storing the user in shared preferences
                            }
                        //}
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("email",SharedPrefManager.getKeyUserEmail());
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }





    private void add() {

        final ProgressDialog progressBar= new ProgressDialog(this);
        final String name=names.getText().toString();
        final String address=addresses.getText().toString();

        if(name.length()!=0&&address.length()!=0){
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
                    params.put("name", name);
                    params.put("address", address);
                    params.put("email",SharedPrefManager.getKeyUserEmail());
                    return params;
                }
            };

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        }



        else{

            Toast.makeText(getApplicationContext(),"Please Enter name and address",Toast.LENGTH_SHORT).show();
        }
    }
    }

