package com.example.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.RotatingCircle;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText username;
    EditText email;
    EditText pass;

    Button continueButton;
    ProgressBar progressBar ;
    private FirebaseAuth mAuth;
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));
        username = (EditText) findViewById(R.id.usernameUser);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        continueButton = (Button) findViewById(R.id.Continue);
        continueButton.setOnClickListener(this::onClick);
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite Wave = new Wave();
        progressBar.setIndeterminateDrawable(Wave);
        progressBar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        myDb = new DatabaseHelper(RegisterActivity.this);
    }
    public void registeruser()
    {
        String name = username.getText().toString() ;
        String emailid = email.getText().toString() ;
        String password = pass.getText().toString() ;
        String General = "";
        String Health = "";
        String Work = "";
        String Education = "";
        String Events = "";
        String Food = "";
        String Alarms = "";
        String categ = "";
        String subject = "";
        String description = "";
        String date = "";
        String time = "";
        String levelup = "0";
        String xp = "0";
        String workdone = "0";
        if(name.isEmpty())
        {
            username.setError("Username is required.");
            username.requestFocus();
            return ;
        }
        if(emailid.isEmpty()) {
            email.setError("Email is required.");
            email.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()) //this function will return true if it's not valid..
        {
            email.setError("Enter a valid one. ");
            email.requestFocus();
            return ;
        }
        if(password.isEmpty())
        {
            pass.setError("Password is required.");
            pass.requestFocus();
            return ;
        }
        else if(password.length()<6) {
            pass.setError("Minimum length of password should be 6.");
            pass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user;
                    user = new User(name,
                            emailid,
                            password,levelup,xp,workdone);
                    Categories categories;
                    categories = new Categories(General,Health,Work,Education,Events,Food);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("uid").
                                        setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                String s;
                                SubCategories subCategories = new SubCategories(subject,description,date,time);
                                for(int i=0; i < 10; i++){
                                    s = String.valueOf(i);
                                    FirebaseDatabase.getInstance().getReference("Category")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("General").child(s).setValue(subCategories);

                                    FirebaseDatabase.getInstance().getReference("Category")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Health").child(s).setValue(subCategories);

                                    FirebaseDatabase.getInstance().getReference("Category")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Food").child(s).setValue(subCategories);

                                    FirebaseDatabase.getInstance().getReference("Category")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Events").child(s).setValue(subCategories);

                                    FirebaseDatabase.getInstance().getReference("Category")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Work").child(s).setValue(subCategories);

                                    FirebaseDatabase.getInstance().getReference("Category")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Education").child(s).setValue(subCategories);

                                }

                                for(int i=0; i < 10; i++){
                                    s = String.valueOf(i);
                                    FirebaseDatabase.getInstance().getReference("Social")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("PendingStatus").child(s).setValue("");
                                    FirebaseDatabase.getInstance().getReference("Social")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Friendlist").child(s).setValue("");
                                    FirebaseDatabase.getInstance().getReference("Social")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("PendingWorks").child("SourceUID").child(s).setValue("");
                                    FirebaseDatabase.getInstance().getReference("Social")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("PendingWorks").child("NumberChecker").child(s).setValue("");
                                    FirebaseDatabase.getInstance().getReference("Social")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("PendingWorks").child("SourceCategory").child(s).setValue("");
                                    FirebaseDatabase.getInstance().getReference("Social")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("PendingWorks").child("PendingSubject").child(s).setValue("");
                                }


                                Toast.makeText(RegisterActivity.this,"You are registered !!",Toast.LENGTH_SHORT).show() ;
                                Intent continueIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(continueIntent);
                                progressBar.setVisibility(View.GONE);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Failed!! try again...:(",Toast.LENGTH_SHORT).show() ;
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }) ;
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"Try again...:(",Toast.LENGTH_SHORT).show() ;
                    progressBar.setVisibility(View.GONE);
                }
            }
        }) ;
    }
    public void onClick(View v) {
        registeruser();
    }

}