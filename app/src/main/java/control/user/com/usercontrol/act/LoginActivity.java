package control.user.com.usercontrol.act;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import control.user.com.usercontrol.R;
import control.user.com.usercontrol.database.DbEntryService;

import static control.user.com.usercontrol.act.MainActivity.client;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static final String SERVICE_URI = "http://www.example.com";
    // UI references.
    private LinearLayout signInLayout;
    private LinearLayout signUpLayout;
    private LinearLayout rememberPassLayout;

    private AutoCompleteTextView suNameView;
    private AutoCompleteTextView suEmailView;
    private AutoCompleteTextView siEmailView;
    private AutoCompleteTextView rememberEmailView;

    private EditText suPasswordView;
    private EditText suPasswordViewAgain;
    private EditText siPasswordView;

    private Button suButton;
    private Button siButton;
    private Button siWarningButton;
    private Button suWarningButton;
    private Button rememberCancelButton;
    private Button rememberWarningButton;
    private Button rememberSendButton;

    private ProgressBar progressBar;
    private TextView tvMessage;


    @Override
    public void setTitle(CharSequence title){
        String str = "<font color=\"" + "#FFFFFF" + "\">" + title.toString() +
                "</font>";

        super.setTitle(Html.fromHtml(str));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        setUpSignInLayout();
        setUpSignUpLayout();
        setUpRememberLayout();

        showLayout(1);

        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
    }

    private void showLayout(int code){
        switch (code) {
            case 1:
                signUpLayout.setVisibility(View.VISIBLE);
                signInLayout.setVisibility(View.GONE);
                rememberPassLayout.setVisibility(View.GONE);
                setTitle(getResources().getString(R.string.title_sign_up));
                break;
            case 2:
                signUpLayout.setVisibility(View.GONE);
                signInLayout.setVisibility(View.VISIBLE);
                rememberPassLayout.setVisibility(View.GONE);
                setTitle(getResources().getString(R.string.title_sign_in));
                break;
            case 3:
                signUpLayout.setVisibility(View.GONE);
                signInLayout.setVisibility(View.GONE);
                rememberPassLayout.setVisibility(View.VISIBLE);
                setTitle(getResources().getString(R.string.title_remember));
                break;
        }
    }

    private void showMessage(boolean state, String msg, int color){
        if (!state) {
            tvMessage.setText(msg);
            tvMessage.setTextColor(color);
            tvMessage.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setVisibility(View.GONE);
        }
    }

    private void showProgress(boolean state){
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setUpSignInLayout(){
        signInLayout = (LinearLayout) findViewById(R.id.signInLayout);
        siEmailView = (AutoCompleteTextView) findViewById(R.id.signin_email);
        siPasswordView = (EditText) findViewById(R.id.signin_password);
        suWarningButton = (Button) findViewById(R.id.btnSignUpWarning);
        suWarningButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showLayout(1);
            }
        });

        rememberWarningButton = (Button) findViewById(R.id.btnRemember);
        rememberWarningButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showLayout(3);
            }
        });

        siButton = (Button) findViewById(R.id.btnSignIn);
        siButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                attemptSignIn();
            }
        });
    }

    private void setUpSignUpLayout(){
        signUpLayout = (LinearLayout) findViewById(R.id.signUpLayout);
        suEmailView = (AutoCompleteTextView) findViewById(R.id.signup_email);
        suNameView = (AutoCompleteTextView) findViewById(R.id.signup_name);
        suPasswordView = (EditText) findViewById(R.id.signup_password);
        suPasswordViewAgain = (EditText) findViewById(R.id.signup_password_again);
        siWarningButton = (Button) findViewById(R.id.btnSignInWarning);
        siWarningButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showLayout(2);
            }
        });
        suButton = (Button) findViewById(R.id.btnSignUp);
        suButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                attemptSignUp();
            }
        });
    }

    private void setUpRememberLayout(){
        rememberPassLayout = (LinearLayout) findViewById(R.id.remember_password_layout);
        rememberEmailView = (AutoCompleteTextView) findViewById(R.id.remember_password);
        rememberCancelButton = (Button) findViewById(R.id.btnRememberCancel);
        rememberCancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showLayout(2);
            }
        });
        rememberSendButton = (Button) findViewById(R.id.btnRememberSend);
        rememberSendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                attemptRemember();
            }
        });
    }

    private void attemptSignUp(){

        suButton.setEnabled(false);
        // Reset errors.
        suNameView.setError(null);
        suEmailView.setError(null);
        suPasswordView.setError(null);
        suPasswordViewAgain.setError(null);

        // Store values at the time of the login attempt.
        String name = suNameView.getText().toString();
        String email = suEmailView.getText().toString();
        String password = suPasswordView.getText().toString();
        String passwordAgain = suPasswordViewAgain.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            suPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = suPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(passwordAgain) && !isPasswordValid(passwordAgain)) {
            suPasswordViewAgain.setError(getString(R.string.error_invalid_password));
            focusView = suPasswordViewAgain;
            cancel = true;
        }


        if (!TextUtils.isEmpty(passwordAgain) && !TextUtils.isEmpty(password) && !password.equals
                (passwordAgain)) {
            suPasswordViewAgain.setError(getString(R.string.passwords_unmatched));
            focusView = suPasswordViewAgain;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            suNameView.setError(getString(R.string.error_field_required));
            focusView = suNameView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            suEmailView.setError(getString(R.string.error_field_required));
            focusView = suEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            suEmailView.setError(getString(R.string.error_invalid_email));
            focusView = suEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            suButton.setEnabled(true);
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            getSignUpTask(name, email, password).execute();
        }
    }

    private void attemptSignIn(){
        siButton.setEnabled(false);
        // Reset errors.
        siEmailView.setError(null);
        siPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = siEmailView.getText().toString();
        String password = siPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            siPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = siPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            siEmailView.setError(getString(R.string.error_field_required));
            focusView = siEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            siEmailView.setError(getString(R.string.error_invalid_email));
            focusView = siEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            siButton.setEnabled(true);
        } else {
            showProgress(true);
            getSignInTask(email, password).execute();
        }
    }

    private void attemptRemember(){

        rememberSendButton.setEnabled(false);
        // Reset errors.
        rememberEmailView.setError(null);

        // Store values at the time of the login attempt.
        String email = rememberEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            rememberEmailView.setError(getString(R.string.error_field_required));
            focusView = rememberEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            rememberEmailView.setError(getString(R.string.error_invalid_email));
            focusView = rememberEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            rememberSendButton.setEnabled(true);
        } else {
            showProgress(true);
            getRememberTask(email).execute();
        }
    }

    private boolean isEmailValid(String email){
        return email.contains("@");
    }

    private boolean isPasswordValid(String password){
        return password.length() > 4;
    }

    private AsyncTask<Void, Void, Long> getSignUpTask(final String name, final String email, final String
            password){
        return new AsyncTask<Void, Void, Long>(){

            @Override
            protected void onPreExecute(){

            }

            @Override
            protected Long doInBackground(Void... params){

                try {
                    Long id = DbEntryService.saveAccount(name, email, password);
                } catch (Exception e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
                return 0l;
            }

            @Override
            protected void onPostExecute(Long s){
                suButton.setEnabled(true);
                try {
                    showMessage(true, "Account created", Color.GREEN);
                    Intent i = new Intent(LoginActivity.this, UserListActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        };
    }

    private AsyncTask<Void, Void, Boolean> getSignInTask(final String email, final String pass){
        return new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... params){
                //boolean login = DbEntryService.login(email, pass);
                //return login;
                try {
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody body = RequestBody.create(JSON, email + "___" + pass);
                    //Email ve pass body ye eklendi
                    Request request = new Request.Builder()
                            .url(SERVICE_URI + "/login")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute(); //Çağrı yapıldı response alındı
                    ResponseBody responseBody = response.body();
                    byte[] bytes = responseBody.bytes();//Dosya byte array olarak alındı
                    File f = new File("zipfile.zip");//File oluşturuldu
                    FileOutputStream fos = new FileOutputStream(f);//File açıldı
                    fos.write(bytes);//Byte array yazıldı
                    fos.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean s){
                siButton.setEnabled(true);

            }
        };
    }

    private AsyncTask<Void, Void, String> getRememberTask(String email){
        return new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params){
                return null;
            }

            @Override
            protected void onPostExecute(String s){
                rememberSendButton.setEnabled(true);
            }
        };
    }
}