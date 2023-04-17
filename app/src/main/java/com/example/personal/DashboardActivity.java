package com.example.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button generalButton;
    Button healthButton;
    Button workButton;
    Button educationButton;
    Button eventsButton;
    Button foodButton;
    Button Friends ;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar tool;
    private long backPressedTime;
    TextView usertxt;
    String USER;
    FloatingActionButton FloatButton;
    String userStr;
    String emailIDStr;
    String passwordStr;
    private TextView logout ;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        usertxt=(TextView) findViewById(R.id.usertxt);
        getData(databaseReference, uid);

        generalButton = (Button) findViewById(R.id.buttonGeneral);
        healthButton = (Button) findViewById(R.id.buttonHealth);
        workButton = (Button) findViewById(R.id.buttonWork);
        educationButton = (Button) findViewById(R.id.buttonEducation);
        eventsButton = (Button) findViewById(R.id.buttonEvents);
        foodButton = (Button) findViewById(R.id.buttonFood);
        Friends = (Button) findViewById(R.id.buttonFriends);
        generalButton.setOnClickListener(this::onClickGeneral);
        healthButton.setOnClickListener(this::onClickHealth);
        workButton.setOnClickListener(this::onClickWork);
        educationButton.setOnClickListener(this::onClickEducation);
        foodButton.setOnClickListener(this::onClickFood);
        Friends.setOnClickListener(this::onClickFriends);
        eventsButton.setOnClickListener(this::onClickEvents);

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

        Bundle continuebundle = getIntent().getExtras();
        if (continuebundle != null) {
            userStr = continuebundle.getString("user");
            emailIDStr = continuebundle.getString("emailID");
            passwordStr = continuebundle.getString("password");
        }
    }

    private void getData(DatabaseReference databaseReference, String uid) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                USER= snapshot.child("Users").child(uid).child("username").getValue(String.class);
                usertxt.setText(USER);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onClickWork(View view) {
        Intent workIntent = new Intent(DashboardActivity.this, WorkActivity.class);
        startActivity(workIntent);
    }
    private void onClickHealth(View view) {
        Intent healthIntent = new Intent(DashboardActivity.this, HealthActivity.class);
        startActivity(healthIntent);
    }

    private void onClickEducation(View view) {
        Intent educationIntent = new Intent(DashboardActivity.this, EducationActivity.class);
        startActivity(educationIntent);
    }
    private void onClickFood(View view) {
        Intent foodIntent = new Intent(DashboardActivity.this,FoodActivity.class);
        Toast.makeText(DashboardActivity.this,"Food",Toast.LENGTH_SHORT);
        startActivity(foodIntent);
    }
    private void onClickEvents(View view) {
        Intent foodIntent = new Intent(DashboardActivity.this,EventsActivity.class);
        Toast.makeText(DashboardActivity.this,"Food",Toast.LENGTH_SHORT);
        startActivity(foodIntent);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            exitFromApp() ;
        }

    }
    private void exitFromApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    public void onClickGeneral(View v) {
        Intent generalIntent = new Intent(DashboardActivity.this, GeneralActivityM.class);

        startActivity(generalIntent);
    }
    public void onClickFriends(View v) {
        Intent generalIntent = new Intent(DashboardActivity.this, SocialActivity.class);
        startActivity(generalIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_general:
                Intent generalIntent = new Intent(DashboardActivity.this, GeneralActivityM.class);
                startActivity(generalIntent);
                break;
            case R.id.nav_health:
                Intent healthIntent = new Intent(DashboardActivity.this, HealthActivity.class);
                startActivity(healthIntent);
                break;
            case R.id.nav_work:
                Intent workIntent = new Intent(DashboardActivity.this, WorkActivity.class);
                startActivity(workIntent);
                break;
            case R.id.nav_education:
                Intent educationIntent = new Intent(DashboardActivity.this, EducationActivity.class);
                startActivity(educationIntent);
                break;
            case R.id.nav_food:
                Intent foodIntent = new Intent(DashboardActivity.this, FoodActivity.class);
                startActivity(foodIntent);
                break;
            case R.id.nav_friends:
                Intent Social = new Intent(DashboardActivity.this, SocialActivity.class);
                startActivity(Social);
                break;
            case R.id.nav_events:
                Intent eventIntent = new Intent(DashboardActivity.this, EventsActivity.class);
                startActivity(eventIntent);
                break;

            case R.id.nav_profile:
                Intent profileIntent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                finish();
                startActivity(logoutIntent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void openActivityInput(View v) {
        Intent input = new Intent(DashboardActivity.this, InputActivity.class);
        startActivity(input);
    }

}