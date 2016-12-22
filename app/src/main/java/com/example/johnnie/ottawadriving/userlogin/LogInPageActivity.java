package com.example.johnnie.ottawadriving.userlogin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnnie.ottawadriving.R;
import com.example.johnnie.ottawadriving.explore.ExploreActivity;

/**
 * Created by Jiangqi on 2016-12-21.
 */

public class LogInPageActivity extends Activity implements View.OnClickListener {
    TextView login, register, skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE); // Removing
        // ActionBar

        EditText loginText;
        EditText passText;
        setContentView(R.layout.activity_login_page);
        loginText = (EditText) findViewById(R.id.login_page_social_login_text);
        passText = (EditText) findViewById(R.id.login_page_social_login_password);
        Typeface sRobotoThin = Typeface.createFromAsset(getAssets(),
                "fonts/Roboto-Thin.ttf");
        ;
        loginText.setTypeface(sRobotoThin);
        passText.setTypeface(sRobotoThin);


        login = (TextView) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        skip = (TextView) findViewById(R.id.skip);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == login) {
            Toast.makeText(this, login.getText(), Toast.LENGTH_SHORT).show();
        }
        if (view == register) {
            Toast.makeText(this, register.getText(), Toast.LENGTH_SHORT).show();
        }
        if (view == skip) {
            Intent intent = new Intent(this, ExploreActivity.class);
            startActivity(intent);
        }
    }
}
