package com.example.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    Button createAccount;
    EditText userID;
    EditText password;
    TextView reset ;
    private CheckBox remember ;
    private long backPressedTime;
    private SharedPreferences mPrefs ;
    private static final String PREFS_NAME = "prefsFile" ;
    ProgressBar progressBar ;
    private FirebaseAuth mAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));
        loginButton = (Button) findViewById(R.id.buttonLogin);
        createAccount = (Button) findViewById(R.id.buttonCreate);
        password = (EditText) findViewById(R.id.PASSWORD);
        userID = (EditText) findViewById(R.id.EMAILID);
        createAccount.setOnClickListener(this::onClickCreate);
        loginButton.setOnClickListener(this::onClickLogin);
        remember = (CheckBox)findViewById(R.id.remember) ;
        reset = (TextView)findViewById(R.id.Forgotten);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite Wave = new Wave();
        progressBar.setIndeterminateDrawable(Wave);
        progressBar.setVisibility(View.INVISIBLE);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            }
        });
        mPrefs = getSharedPreferences(PREFS_NAME,MODE_PRIVATE) ;
        getPreferencesData();
        mAuth = FirebaseAuth.getInstance() ;
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(this,DashboardActivity.class));
        }
    }
    private void getPreferencesData(){
        SharedPreferences sp = getSharedPreferences(PREFS_NAME,MODE_PRIVATE) ;
        if(sp.contains("pref_name")){
            String u = sp.getString("pref_name","not_found") ;
            userID.setText(u.toString());
        }
        if(sp.contains("pref_pass")){
            String p = sp.getString("pref_pass","not_found") ;
            password.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean b = sp.getBoolean("pref_check",false) ;
            remember.setChecked(b);
        }
    }
    public  void onBackPressed(){
        if(backPressedTime+2000>System.currentTimeMillis()){
            super.onBackPressed();
            exitFromApp() ;
        }else{
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backPressedTime=System.currentTimeMillis();
    }
    private void exitFromApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        finishAffinity();
        startActivity(intent);
    }
    private void userLogin(){
        String name = userID.getText().toString() ;
        String pass = password.getText().toString() ;
        if(name.isEmpty()) {
            userID.setError("Useremail is required.");
            userID.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(name).matches()) //this function will return true if it's not valid..
        {
            userID.setError("Enter a valid one. ");
            userID.requestFocus();
            return ;
        }
        if(pass.isEmpty()) {
            password.setError("Password is required.");
            password.requestFocus();
            return;
        }
        else if(pass.length()<6) {
            password.setError("Minimum length of password should be 6.");
            password.requestFocus();
            return;
        }
        if(remember.isChecked()){
            Boolean boolIsChecked = remember.isChecked() ;
            SharedPreferences.Editor editor = mPrefs.edit() ;
            editor.putString("pref_email",userID.getText().toString()) ;
            editor.putString("pref_pass",password.getText().toString()) ;
            editor.putBoolean("pref_check",boolIsChecked);
            editor.apply();
            Toast.makeText(getApplicationContext(),"User credentials saved..",Toast.LENGTH_SHORT).show();
        }else{
            mPrefs.edit().clear().apply();
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(name,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                         Toast.makeText(LoginActivity.this,"User Verified...",Toast.LENGTH_SHORT).show();
                         progressBar.setVisibility(View.GONE);
                         finish();
                         startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Login Failed!!Try again ",Toast.LENGTH_LONG).show() ;
                    progressBar.setVisibility(View.GONE);
                }
            }
        }) ;
    }
    public void onClickLogin(View v) {
        userLogin();
    }
    public void onClickCreate(View v) {
        Intent createIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(createIntent);
    }
}