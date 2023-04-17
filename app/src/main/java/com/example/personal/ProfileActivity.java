package com.example.personal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar tool;
    FloatingActionButton FloatButton;
    String userStr;
    String emailIDStr;
    String xpStr;
    String lvlStr;
    String avgxpStr;
    String wdStr;
    TextView name;
    TextView email;
    TextView xp;
    TextView avgxp;
    TextView lvl;
    ImageView view ;
    RadarChart radar ;
    Button SELECT ;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String UID;
    int x;
    int v1;
    int v2;
    int v3;
    int v4;
    int v5;
    int v6;
    int v7;
    double x1;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1 ;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));
        radar = (RadarChart)findViewById(R.id.Radarchart) ;
        view = (ImageView)findViewById(R.id.img) ;
        SELECT = (Button)findViewById(R.id.SELECT) ;
        SELECT.setOnClickListener(this::onClicSELECT);

        user = FirebaseAuth.getInstance().getCurrentUser();
        UID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getData(databaseReference,UID);

        drawerLayout = findViewById(R.id.drawer_Layout);
        navigationView = findViewById(R.id.na_view);
        tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, tool, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        FloatButton = (FloatingActionButton) findViewById(R.id.floatButton);
        FloatButton.setOnClickListener(this::openActivityInput);
        name = (TextView) findViewById(R.id.nameID);
        email = (TextView) findViewById(R.id.emailID);
        xp=(TextView)findViewById(R.id.xpID);
        avgxp=(TextView)findViewById(R.id.avgxpID);
        lvl=(TextView)findViewById(R.id.lvlID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userStr= snapshot.child("Users").child(UID).child("username").getValue(String.class);
                Log.d("userssssss","user: "+userStr);
                emailIDStr= snapshot.child("Users").child(UID).child("emailid").getValue(String.class);
                xpStr=snapshot.child("Users").child(UID).child("xp").getValue(String.class);
                lvlStr=snapshot.child("Users").child(UID).child("levelup").getValue(String.class);
                avgxpStr=snapshot.child("Users").child(UID).child("xp").getValue(String.class);
                wdStr=snapshot.child("Users").child(UID).child("workdone").getValue(String.class);
                name.setText(" "+userStr);
                email.setText(" "+emailIDStr);
                xp.setText(" "+xpStr);
                lvl.setText(" "+lvlStr);
                avgxp.setText(" "+avgxpStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void getData(DatabaseReference dbr,String uid){
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wdStr=snapshot.child("Users").child(UID).child("workdone").getValue(String.class);
                x=Integer.parseInt(wdStr);
                Log.d("Radava","value "+x+" i "+wdStr);
                v1=x*2;
                v2=x;
                v3=x*3;
                v4=x*2;
                v5=x*5;
                v6=x*4;
                v7=x*6;
                RADAR(v1,v2,v3,v4,v5,v6,v7);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void RADAR(int v1,int v2,int v3,int v4,int v5,int v6,int v7){
        //Log.d("Radava","value "+v1+" i "+wdStr);
        ArrayList<RadarEntry> daybydayprogress = new ArrayList<>();
        daybydayprogress.add(new RadarEntry(v1));
        daybydayprogress.add(new RadarEntry(v2));
        daybydayprogress.add(new RadarEntry(v3));
        daybydayprogress.add(new RadarEntry(v4));
        daybydayprogress.add(new RadarEntry(v5));
        daybydayprogress.add(new RadarEntry(v6));
        daybydayprogress.add(new RadarEntry(v7));

        RadarDataSet daycount = new RadarDataSet(daybydayprogress,"Day by day progress: ");
        daycount.setColor(Color.BLUE);
        daycount.setLineWidth(3f);
        daycount.setValueTextColor(Color.RED);
        daycount.setValueTextSize(16f);

        RadarData data = new RadarData() ;
        data.addDataSet(daycount);

        String[] labels = {"1st", "2nd", "3rd", "4th", "5th", "6th" , "7th"} ;

        XAxis xAxis = radar.getXAxis() ;
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        radar.setData(data);
    }

    private void onClicSELECT(View view) {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    ProfileActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE_PERMISSION
            );
        }
        else {
            selectIMAGE();

        }
    }

    public void selectIMAGE(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) ;
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectIMAGE();
            }
            else {
                Toast.makeText(ProfileActivity.this,"Permission denied...",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if(data!=null){
                Uri selectedimageuri = data.getData() ;
                if(selectedimageuri != null){
                    try{
                        InputStream input = getContentResolver().openInputStream(selectedimageuri) ;
                        Bitmap bit = BitmapFactory.decodeStream(input);
                        view.setImageBitmap(bit);
                        File selectedimagefile  = new File(getPathfromUri(selectedimageuri));
                    }catch (Exception e){
                        Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show() ;
                    }
                }
            }
        }
    }
    private String getPathfromUri(Uri contentUri){
        String filePath ;
        Cursor cursor = getContentResolver()
                .query(contentUri,null,null,null,null);
        if(cursor == null){
            filePath = contentUri.getPath() ;
        }
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("data") ;
            filePath = cursor.getString(index) ;
            cursor.close();
        }
        return filePath ;

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_general:
                Intent generalIntent = new Intent(ProfileActivity.this, GeneralActivityM.class);
                startActivity(generalIntent);
                break;
            case R.id.nav_health:
                Intent healthIntent = new Intent(ProfileActivity.this, HealthActivity.class);
                startActivity(healthIntent);
                break;
            case R.id.nav_work:
                Intent workIntent = new Intent(ProfileActivity.this, WorkActivity.class);
                startActivity(workIntent);
                break;
            case R.id.nav_education:
                Intent educationIntent = new Intent(ProfileActivity.this, EducationActivity.class);
                startActivity(educationIntent);
                break;
            case R.id.nav_events:
                Intent eventIntent = new Intent(ProfileActivity.this, EventsActivity.class);
                startActivity(eventIntent);
                break;
            case R.id.nav_food:
                Intent foodIntent = new Intent(ProfileActivity.this, FoodActivity.class);
                startActivity(foodIntent);
                break;
            case R.id.nav_friends:
                Intent friendsIntent = new Intent(ProfileActivity.this, SocialActivity.class);
                startActivity(friendsIntent);
                break;
            case R.id.nav_profile:
                Intent profileIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                finish();
                startActivity(logoutIntent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openActivityInput(View v) {
        Intent input = new Intent(ProfileActivity.this, InputActivity.class);
        startActivity(input);
    }

}