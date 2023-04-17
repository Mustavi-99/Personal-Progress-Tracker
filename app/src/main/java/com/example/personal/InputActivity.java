package com.example.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Random;

public class InputActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Button timeButton;
    Button DateButton;
    Button Save;
    CheckBox checkBox;
    EditText subject,fr1,fr2,fr3;
    EditText description;
    Spinner sp;
    int N2flaggg = 0;
    int N3flaggg = 0;
    int N4flaggg = 0;
    int H,M;
    String Date,category;
    String friendUID1 = "",friendUID2 = "",friendUID3 = "";
    FirebaseUser user;
    DatabaseReference databaseReference,dbr;
    String date;
    String uid;
    String progTime;
    String NumberChecker="0";
    String NumberChecker2="0";
    String NumberChecker3="0";
    String NumberChecker4="0";
    Calendar c = Calendar.getInstance();
    ReadableTime readableTime =new ReadableTime();
    int i=0;
    int id;
    long alarmTime;
    DatabaseHelper myDb;
    Random rand= new Random();
    ManageAlarm manageAlarm=new ManageAlarm();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));

        timeButton = (Button) findViewById(R.id.timeButton);
        DateButton = (Button) findViewById(R.id.dateButton);
        checkBox = (CheckBox) findViewById(R.id.checkbox_button);
        Save = (Button) findViewById(R.id.save);
        Save.setEnabled(false);
        subject = (EditText) findViewById(R.id.subjectText);
        description = (EditText) findViewById(R.id.descriptionText);
        sp = (Spinner) findViewById(R.id.aSpinner);
        fr1 = (EditText) findViewById(R.id.f1);
        fr2 = (EditText) findViewById(R.id.f2);
        fr3 = (EditText) findViewById(R.id.f3);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        dbr = FirebaseDatabase.getInstance().getReference("Users");

        myDb = new DatabaseHelper(InputActivity.this);

        sp.setOnItemSelectedListener(this);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFriendsUID(dbr);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"Time Picker");
            }
        });
        DateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date Picker");
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friendUID1.length()>1){
                    String sub = subject.getText().toString();
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID1).child("PendingWorks").child("SourceUID").child(NumberChecker2).setValue(uid);
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID1).child("PendingWorks").child("SourceCategory").child(NumberChecker2).setValue(category);
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID1).child("PendingWorks").child("NumberChecker").child(NumberChecker2).setValue(NumberChecker);
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID1).child("PendingWorks").child("PendingSubject").child(NumberChecker2).setValue(sub);
                }
                if(friendUID2.length()>1){
                    String sub = subject.getText().toString();
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID2).child("PendingWorks").child("SourceUID").child(NumberChecker3).setValue(uid);
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID2).child("PendingWorks").child("SourceCategory").child(NumberChecker3).setValue(category);
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID2).child("PendingWorks").child("NumberChecker").child(NumberChecker3).setValue(NumberChecker);
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID2).child("PendingWorks").child("PendingSubject").child(NumberChecker3).setValue(sub);
                }
                if(friendUID3.length()>1){
                    String sub = subject.getText().toString();
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID3).child("PendingWorks").child("SourceUID").child(NumberChecker4).setValue(uid);
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID3).child("PendingWorks").child("SourceCategory").child(NumberChecker4).setValue(category);
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID3).child("PendingWorks").child("NumberChecker").child(NumberChecker4).setValue(NumberChecker);
                    FirebaseDatabase.getInstance().getReference("Social")
                            .child(friendUID3).child("PendingWorks").child("PendingSubject").child(NumberChecker4).setValue(sub);
                }
                if(subject.length()<1){
                    Toast.makeText(InputActivity.this,"No Input detected",Toast.LENGTH_SHORT).show() ;
                    Save.setEnabled(false);
                }
                else{
                    Intent dashboard = new Intent(InputActivity.this, DashboardActivity.class);
                    String sub = subject.getText().toString();
                    String des = description.getText().toString();
                    FirebaseDatabase.getInstance().getReference("Category").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(category).child(NumberChecker).child("Date")
                            .setValue(Date);
                    FirebaseDatabase.getInstance().getReference("Category").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(category).child(NumberChecker).child("Description")
                            .setValue(des);
                    FirebaseDatabase.getInstance().getReference("Category").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(category).child(NumberChecker).child("Subject")
                            .setValue(sub);
                    FirebaseDatabase.getInstance().getReference("Category").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(category).child(NumberChecker).child("Time")
                            .setValue(readableTime.convertTime(H,M));
                    id = rand.nextInt(100);
                    progTime=H+":"+M;
                    c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY,H);
                    c.set(Calendar.MINUTE,M);
                    c.set(Calendar.SECOND,0);
                    alarmTime=c.getTimeInMillis();
                    Log.d("AddDataaa","Date: "+category);
                    AddData(String.valueOf(id),category,progTime,readableTime.convertTime(H,M),sub);
                    manageAlarm.pptTime(id,alarmTime,InputActivity.this);
                    startActivity(dashboard);
                }

            }
        });
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView textview = (TextView)findViewById(R.id.textDate);
        textview.setText("Date: "+currentDateString);
        Date = currentDateString;

    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView textview = (TextView)findViewById(R.id.view1);
        H = hourOfDay;
        M = minute;
        textview.setText(readableTime.convertTime(hourOfDay,minute));
    }

    private void onClick(View view) {
        Intent dashboardIntent = new Intent(InputActivity.this, DashboardActivity.class);
        startActivity(dashboardIntent);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        category = adapterView.getSelectedItem().toString();
        Log.d("dateeeeeeeeeeeeeeee","categoryyyyy: "+category);
        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        getData(databaseReference,uid);
    }
    private void AddData(String id, String category,String prTime,String reTime,String subject){
        Log.d("DataAdded","Date: "+category+"p "+prTime+"r "+reTime+"s "+subject+"i "+id);
        boolean insertData = myDb.insertData(id,category,prTime,reTime,subject);
        if(insertData)
            Toast.makeText(this, "Data Inserted",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Data Not Inserted",Toast.LENGTH_SHORT).show();
    }
    public void getData(DatabaseReference dbf, String id){
        dbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(i=0,NumberChecker="0";i<=5;i++){
                    date = snapshot.child("Category").child(id).child(category).child(String.valueOf(i)).child("Date").getValue(String.class);
                    if(i==5){
                        Toast.makeText(InputActivity.this,"No more entries possible...pls delete entry",Toast.LENGTH_SHORT).show() ;
                        break;
                    }
                    if(date.equals("")){
                        NumberChecker = String.valueOf(i);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void getFriendsUID(DatabaseReference dbr){
        String friend1 = fr1.getText().toString();
        String friend2 = fr2.getText().toString();
        String friend3 = fr3.getText().toString();
        if(friend1.length()>1) {
            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        String DBusername = dsp.child("username").getValue(String.class);
                        friendUID1 = dsp.child("uid").getValue(String.class);
                        if (DBusername.equals(friend1)) {
                            Log.d("friendUID", "SourceUID: " + uid + " destinationUID :" + friendUID1 + " NumberChecker: " + NumberChecker2 + " Category: " + category);
                            break;
                        }
                    }
                    setNumberChecker2(databaseReference, friendUID1);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        if(friend2.length()>1){
            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        String DBusername = dsp.child("username").getValue(String.class);
                        if (DBusername.equals(friend2)) {
                            friendUID2 = dsp.child("uid").getValue(String.class);
                            Log.d("friendUID","UID :"+friendUID2);
                            break;
                        }
                    }
                    setNumberChecker3(databaseReference,friendUID2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(friend3.length()>1){
            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        String DBusername = dsp.child("username").getValue(String.class);
                        if (DBusername.equals(friend3)) {
                            friendUID3 = dsp.child("uid").getValue(String.class);
                            Log.d("friendUID","UID :"+friendUID3);
                            break;
                        }
                    }
                    setNumberChecker4(databaseReference,friendUID3);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        Save.setEnabled(true);
    }

    private void setNumberChecker4(DatabaseReference databaseReference, String id) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(N4flaggg==0){
                    for(i=0,NumberChecker4="0";i<=10;i++){
                        String ggg = snapshot.child("Social").child(id).child("PendingWorks").child("SourceCategory").child(String.valueOf(i)).getValue(String.class);
                        Log.d("friendUIDDD","UID :"+id+" GGG: "+ggg);
                        if(i==10){
                            Toast.makeText(InputActivity.this,"No more entries possible...pls delete entry",Toast.LENGTH_SHORT).show() ;
                            break;
                        }
                        if(ggg.equals("")){
                            NumberChecker4 = String.valueOf(i);
                            break;
                        }
                    }
                }
                N4flaggg = 1;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setNumberChecker3(DatabaseReference databaseReference, String id) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(N3flaggg==0){
                    for(i=0,NumberChecker3="0";i<=10;i++){
                        String ggg = snapshot.child("Social").child(id).child("PendingWorks").child("SourceCategory").child(String.valueOf(i)).getValue(String.class);
                        Log.d("friendUIDDD","UID :"+id+" GGG: "+ggg);
                        if(i==10){
                            Toast.makeText(InputActivity.this,"No more entries possible...pls delete entry",Toast.LENGTH_SHORT).show() ;
                            break;
                        }
                        if(ggg.equals("")){
                            NumberChecker3 = String.valueOf(i);
                            break;
                        }
                    }
                }
                N3flaggg = 1;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setNumberChecker2(DatabaseReference databaseReference, String id) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(N2flaggg==0){
                    for(i=0,NumberChecker2="0";i<=10;i++){
                        String ggg = snapshot.child("Social").child(id).child("PendingWorks").child("SourceCategory").child(String.valueOf(i)).getValue(String.class);
                        Log.d("friendUIDDD","UID :"+id+" GGG: "+ggg);
                        if(i==10){
                            Toast.makeText(InputActivity.this,"No more entries possible...pls delete entry",Toast.LENGTH_SHORT).show() ;
                            break;
                        }
                        if(ggg.equals("")){
                            NumberChecker2 = String.valueOf(i);
                            break;
                        }
                    }
                }
                N2flaggg = 1;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}