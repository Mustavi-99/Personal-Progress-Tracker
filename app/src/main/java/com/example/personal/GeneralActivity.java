package com.example.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class GeneralActivity extends AppCompatActivity {

    FirebaseUser user;
    DatabaseReference databaseReference;
    String uid;
    public String subject[] = new String[]{"","","","",""};
    public String date[] = new String[]{"","","","",""};
    public String description[] = new String[]{"","","","",""};
    public String time[] = new String[]{"","","","",""};
    TextView textV1,textV2;
    TextView realdate ;
    TextView realtime ;
    Intent edu ;
    Intent wor ;
    Intent foo ;
    Intent heal ;
    ReadableTime readableTime=new ReadableTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        edu = new Intent(GeneralActivity.this,EducationActivity.class);
        wor = new Intent(GeneralActivity.this,WorkActivity.class);
        foo = new Intent(GeneralActivity.this,FoodActivity.class);
        heal = new Intent(GeneralActivity.this,HealthActivity.class);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        textV1 = (TextView) findViewById(R.id.tv1);
        textV2 = (TextView) findViewById(R.id.tv2);
        realdate = (TextView)findViewById(R.id.showdate) ;
        realtime = (TextView)findViewById(R.id.showtime) ;
        getData(databaseReference,uid);
    }



    private void getData(DatabaseReference dbf, String id){
        dbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bundle bundle = getIntent().getExtras();
                String ed = bundle.getString("Position");
                Log.d("positionnn",ed+" pos");
                int i = Integer.parseInt(ed);
                Log.d("positionnn",i+" i");
                showall(i,bundle.getString("NAME"),snapshot,id);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void showall(int i, String m,DataSnapshot snapshot,String id)
        {
                date[i] = snapshot.child("Category").child(id).child(m).child(String.valueOf(i)).child("Date").getValue(String.class);
                realdate.setText(date[i]);
            description[i] = snapshot.child("Category").child(id).child(m).child(String.valueOf(i)).child("Description").getValue(String.class);
                textV2.setText(description[i]);
            subject[i] = snapshot.child("Category").child(id).child(m).child(String.valueOf(i)).child("Subject").getValue(String.class);
                textV1.setText(subject[i]);
            time[i] = snapshot.child("Category").child(id).child(m).child(String.valueOf(i)).child("Time").getValue(String.class);
            Log.d("dateeeeeeeeeeeeeeee","Time "+time[i]);
                realtime.setText(time[i]);
        }
}