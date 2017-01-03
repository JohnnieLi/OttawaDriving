package com.example.johnnie.ottawadriving.userlogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.explore.ExploreActivity;
import com.example.johnnie.ottawadriving.model.UserModel;
import com.example.johnnie.ottawadriving.utils.JsonArrayParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Jiangqi on 2016-12-21.
 */

public class LogInPageActivity extends Activity implements View.OnClickListener {
    TextView login, register, skip;
    EditText usernameText;
    EditText passwordText;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String message = extras.getString("registerMessage");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        getWindow().requestFeature(Window.FEATURE_NO_TITLE); // Removing
        // ActionBar
        setContentView(R.layout.activity_login_page);
        sharedPref = this.getSharedPreferences("FEDSa", Context.MODE_PRIVATE);
        usernameText = (EditText) findViewById(R.id.login_page_social_login_text);
        passwordText = (EditText) findViewById(R.id.login_page_social_login_password);
        Typeface sRobotoThin = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Thin.ttf");

        usernameText.setTypeface(sRobotoThin);
        passwordText.setTypeface(sRobotoThin);


        String username = sharedPref.getString("username", "default");
        usernameText.setText(username);

        login = (TextView) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        skip = (TextView) findViewById(R.id.skip);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        skip.setOnClickListener(this);

        readToken();

    }


    private void postUserLogin(String username, String password) {
        // POST params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        String IPAddress = getString(R.string.httpIPAddress);
        String uri = "http://" + IPAddress + "/api/auth/login";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, uri, new JSONObject(params),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            // if user login successfully
                            if (success) {
                                //store user basic info in shared preference
                                JSONObject userObject = response.getJSONObject("user");
                                UserModel userModel = JsonArrayParser.parseUserModel(userObject);
                                saveUserToSharedPreference(userModel);
                                //store user token in shared preference
                                String token = response.getString("token");
                                Log.d("LOGINTOKEN", token);
                                saveTokenToSharedPreference(token);

                                //change to explore activity
                                Intent intent = new Intent(LogInPageActivity.this, ExploreActivity.class);
                                startActivity(intent);
                                // if user login failed, show message
                            } else {
                                Toast.makeText(LogInPageActivity.this, message, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    public void saveTokenToSharedPreference(String token) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public void saveUserToSharedPreference(UserModel user) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", user.getUsername());
        editor.putString("firstName", user.getFirstName());
        editor.commit();
    }


    public void readToken() {

        String token = sharedPref.getString("token", "no token exist");
        Log.d("LOGINSPTOKEN", token);
    }


    @Override
    public void onClick(View view) {
        if (view == login) {
            this.postUserLogin(usernameText.getText().toString(), passwordText.getText().toString());

        }
        if (view == register) {
            Intent intent = new Intent(this, RegisterPageActivity.class);
            startActivity(intent);
        }
        if (view == skip) {
            Intent intent = new Intent(this, ExploreActivity.class);
            startActivity(intent);
        }
    }
}
