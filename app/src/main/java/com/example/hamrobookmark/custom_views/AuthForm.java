package com.example.hamrobookmark.custom_views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hamrobookmark.misc.Constants;
import com.example.hamrobookmark.R;
import com.example.hamrobookmark.activities.MainActivity;
import com.example.hamrobookmark.misc.AuthTokenHelper;
import com.example.hamrobookmark.misc.NotificationBackgroundJob;
import com.example.hamrobookmark.misc.RequestFactory;
import com.example.hamrobookmark.misc.ProgressDialog;
import com.example.hamrobookmark.models.User;

import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AuthForm extends LinearLayout implements Response.Listener<JSONObject>, Response.ErrorListener {
    private EditText usernameInput,passwordInput;
    private String apiEndpoint;
    private ProgressDialog progressDialog;
    private RequestQueue requestQueue;

    public AuthForm(Context context) {
        super(context);
    }

    public AuthForm(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView(context,attrs);
    }

    public AuthForm(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView(context,attrs);
    }
    public void setRequestQueue(RequestQueue queue){
        requestQueue = queue;
    }
    private void initializeView(Context context,AttributeSet set){
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_auth_form,this,true);
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        Button actionBtn = findViewById(R.id.authActionButton);
        TypedArray typedArray = context.obtainStyledAttributes(set,R.styleable.AuthForm);
        actionBtn.setText(typedArray.getString(R.styleable.AuthForm_actionBtnText));
        apiEndpoint = typedArray.getString(R.styleable.AuthForm_apiEndpoint);
        typedArray.recycle();
        actionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                Map<String,String> errors = getErrors(username,password);
                if(errors.size() == 0){
                    User user = new User(username,password);
                    sendRequest(user);
                    return;
                }
                setUsernameError(errors.get("username"));
                setPasswordError(errors.get("password"));
            }
        });
    }
    public void sendRequest(User user){
        Context context = getContext();
        if(progressDialog == null){
            progressDialog = new ProgressDialog(context,"Authenticating...");
        }
        progressDialog.show();
        requestQueue.add(RequestFactory.makeJsonObjectRequest(
                context,
                com.android.volley.Request.Method.POST,
                Constants.API_URL + apiEndpoint,
                user.toJSON(),
                this,
                this
        ));
    }

    public Map<String,String> getErrors(String username, String password){
        Map<String,String> errors = new HashMap<>();
        if(username.equals("") || username.length()< 3){
            errors.put("username","Username should be at least 3 characters long");
        }
        if(password.equals("") || password.length()< 4){
            errors.put("password","Password should be at least 4 characters long");
        }
        return errors;
    }
    public void setUsernameError(String errorMsg){
        if(errorMsg != null) {
            usernameInput.setError(errorMsg);
        }
    }
    public void setPasswordError(String errorMsg){
        if(errorMsg != null) {
            passwordInput.setError(errorMsg);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            progressDialog.hide();
            String errorMsg = new String(error.networkResponse.data, StandardCharsets.UTF_8);
            JSONObject errorObj = new JSONObject(errorMsg);
            if(errorObj.has("username")){
                setUsernameError(errorObj.getString("username"));
            }
            if(errorObj.has("password")){
                setPasswordError(errorObj.getString("password"));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            String token = response.getString("token");
            Context context = getContext();
            AuthTokenHelper.saveToken(context,token);
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            NotificationBackgroundJob.start(context);
            progressDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
