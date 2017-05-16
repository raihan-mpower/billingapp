package billingapp.psionicinteractivelimited.com.billingapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
//        implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        if(preferences.getBoolean("isLoggedIn",false)){

            String u=preferences.getString("user","");
            String p=preferences.getString("pass","");
            String t=preferences.getString("token","");
            String lastUpdatedAt=preferences.getString("last_updated_at","");
            Log.v("unishsho70",lastUpdatedAt);
            executeAsynctask(u,p,t);

        }

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEmailView.getText().toString().equals("") || mPasswordView.getText().toString().equals("")){
                }else{
                    executeAsynctask();
                }


            }
        });
    }
    public void executeAsynctask(){
        (new AsyncTask() {
            String username;
            String password;
            String token;
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                username = mEmailView.getText().toString();
                password = mPasswordView.getText().toString();
                dialog = ProgressDialog.show(LoginActivity.this,"please wait","processing");
            }

            @Override
            protected Object doInBackground(Object[] params) {
                token = postData(username, password);

                Log.v("token",token);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if((!token.equalsIgnoreCase("error"))||token.equalsIgnoreCase("")){
//                    if((!token.equalsIgnoreCase("error"))|| token.equalsIgnoreCase("")){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token",token);
                    editor.putString("user",username);
                    editor.putString("pass",password);
                    editor.putString("last_updated_at","1970");

                    editor.putBoolean("isLoggedIn",true);

                    editor.apply();

                    dialog.dismiss();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Wrong Creds", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();

            }
        }).execute();
    }

    //////////////////////////////////////////////
    public void executeAsynctask(final String u,final String p,final String t){
        (new AsyncTask() {
            String username=u;
            String password=p;
            String token=t;
            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                mEmailView.setText(u);
                mPasswordView.setText(p);
                super.onPreExecute();
                dialog = ProgressDialog.show(LoginActivity.this,"please wait","processing");
            }

            @Override
            protected Object doInBackground(Object[] params) {
                token = postData(username, password);

                Log.v("token",token);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                dialog.dismiss();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }
        }).execute();
    }

    //////////////////////////////////////////////
    public String postData(String username, String password) {
        String token = "";
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://cable.psionichub.com/authmob");
        HttpPost httppost = new HttpPost("http://cable.hmannan.com/authmob");

//        HttpPost httppost = new HttpPost("http://192.168.0.108:8000/authmob");




        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String respond = response.getStatusLine().getReasonPhrase();
            int statusCode = response.getStatusLine().getStatusCode();

// Getting the response body.
            String responseBody = EntityUtils.toString(response.getEntity());
;
            if(statusCode == 200){
                Log.v("testcall",responseBody);
                try {
                    JSONObject tokenholder = new JSONObject(responseBody);
                    token = (String) tokenholder.get("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                token = "error";
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return token;
    }
}

