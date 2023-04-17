package com.example.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText email ;
    private Button rest ;
    FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email = (EditText)findViewById(R.id.Email) ;
        rest = (Button)findViewById(R.id.res);
        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpass() ;
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }
    public void resetpass(){
        String mail= email.getText().toString() ;
        if(mail.isEmpty()) {
            email.setError("Useremail is required.");
            email.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) //this function will return true if it's not valid..
        {
            email.setError("Enter a valid one. ");
            email.requestFocus();
            return ;
        }
        mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Check Input email",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Try Again....;(",Toast.LENGTH_LONG).show(); ;
                }
            }
        });
    }
}