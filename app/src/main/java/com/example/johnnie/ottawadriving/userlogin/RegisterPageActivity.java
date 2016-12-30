package com.example.johnnie.ottawadriving.userlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.view.FloatLabeledEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jiangqi on 2016-12-28.
 */

public class RegisterPageActivity extends Activity implements View.OnClickListener {

    FloatLabeledEditText username;
    FloatLabeledEditText password;
    FloatLabeledEditText firstName;
    FloatLabeledEditText lastName;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        username = (FloatLabeledEditText) findViewById(R.id.register_username);
        password = (FloatLabeledEditText) findViewById(R.id.register_password);
        firstName = (FloatLabeledEditText) findViewById(R.id.register_firstName);
        lastName = (FloatLabeledEditText) findViewById(R.id.register_lastName);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
    }

    //volly to post register with username, password, firstName, lastName
    private void requestRegister(String username, String password, String firstName, String lastName) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        String uri = "http://192.168.2.12:5000/api/auth/register";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, uri, new JSONObject(params),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            //if user exists
                            if (!success) {
                                Toast.makeText(RegisterPageActivity.this, message, Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(RegisterPageActivity.this, LogInPageActivity.class);
                                intent.putExtra("registerMessage", message);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }


    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String usernameString = username.getTextString().trim();
            String passString = password.getTextString().trim();
            String firstNameString = firstName.getTextString().trim();
            String lastNameString = lastName.getTextString().trim();
            // if user or password is invalid
            if (usernameString.isEmpty() || passString.isEmpty()) {
                Toast.makeText(this, "Username or Password invalid", Toast.LENGTH_SHORT).show();
            } else {
//                Log.d("REGISTER_Username",usernameString);
//                Log.d("REGISTER_pass",passString);
//                Log.d("REGISTER_first",firstNameString);
//                Log.d("REGISTER_last",lastNameString);
                requestRegister(usernameString, passString, firstNameString, lastNameString);
            }

        }
    }
}
