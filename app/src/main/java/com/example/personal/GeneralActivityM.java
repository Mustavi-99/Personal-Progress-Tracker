package com.example.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GeneralActivityM extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar tool;
    String uid;
    int flag = 0;
    int flagNCS = 0;
    String levelup = "";
    String xp = "";
    String workdone = "";
    int lvl;
    Double x;
    int wd;
    TextView usertxt;
    String USER;
    String NumberChekersub = "";
    FloatingActionButton FloatButton;
    public static ListView listView ;
    public static TextView task;
    FirebaseUser user;
    DatabaseReference databaseReference;
    public RecyclerView recyclereducation ;
    public MyThirdAdapter Adapter;
    public RecyclerView.LayoutManager Manager;
    ArrayList<EnlistedItems> items = new ArrayList<EnlistedItems>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_m);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        usertxt=(TextView) findViewById(R.id.usertxt);
        getData(databaseReference,uid);
        getStat(databaseReference,uid);


        drawerLayout = findViewById(R.id.drawer_Layout);
        navigationView = findViewById(R.id.na_view);
        tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        navigationView.bringToFront();
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, tool, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        FloatButton = (FloatingActionButton) findViewById(R.id.floatButton);
        FloatButton.setOnClickListener(this::openActivityInput);
        recyclereducation = findViewById(R.id.RecyclerView_education);
        recyclereducation.setHasFixedSize(true);
        Manager = new LinearLayoutManager(this);
        Adapter = new MyThirdAdapter(items);
        recyclereducation.setLayoutManager(Manager);
        recyclereducation.setAdapter(Adapter);
        Adapter.setOnItemClickListener(new MyThirdAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {
                Toast.makeText(GeneralActivityM.this,""+position,Toast.LENGTH_SHORT).show();
                String pose;
                Intent General = new Intent(GeneralActivityM.this,GeneralActivity.class) ;
                String name = "General";
                General.putExtra("NAME",name);
                pose = String.valueOf(position);
                General.putExtra("Position",pose);
                Log.d("position",pose+"     "+position+" ");
                startActivity(General);
            }
            @Override
            public void onDoneclick(int position) {
                EnlistedItems e = new EnlistedItems();
                e = items.get(position);
                String sub = e.TEXT;
                Log.d("numberrrr","onCheck sub: "+sub);
                flagNCS = 0;
                x+=wd * (Math.log(53 * Math.pow(10,wd))/Math.log(39))-4 * Math.log(lvl+wd)/Math.log(40);
                x=Math.floor(x * 100) / 100;
                if(x>(lvl +1)* 10){
                    lvl++;
                    Toast.makeText(GeneralActivityM.this, "Level Up", Toast.LENGTH_SHORT).show();
                    x= x - (lvl +1) * 10;
                    wd=1;
                    if(x<0){
                        x = x*(-1);
                    }
                }
                wd++;
                Log.d("xppp", "called xp:"+"Xp: "+x+" Wd: "+wd+" lvl: "+lvl);
                findSubject(databaseReference,sub);
                items.remove(position);
                Adapter.notifyDataSetChanged();
                //Toast.makeText(GeneralActivityM.this,"Done Clicked..and subject = ",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCheckclick(int position) {
                //Toast.makeText(GeneralActivityM.this,"Check Clicked..",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_general:
                Intent generalIntent = new Intent(GeneralActivityM.this, GeneralActivityM.class);
                startActivity(generalIntent);
                break;
            case R.id.nav_health:
                Intent healthIntent = new Intent(GeneralActivityM.this, HealthActivity.class);
                startActivity(healthIntent);
                break;
            case R.id.nav_work:
                Intent workIntent = new Intent(GeneralActivityM.this, WorkActivity.class);
                startActivity(workIntent);
                break;
            case R.id.nav_education:
                Intent educationIntent = new Intent(GeneralActivityM.this, EducationActivity.class);
                startActivity(educationIntent);
                break;
            case R.id.nav_food:
                Intent foodIntent = new Intent(GeneralActivityM.this, FoodActivity.class);
                startActivity(foodIntent);
                break;
            case R.id.nav_friends:
                Intent Social = new Intent(GeneralActivityM.this, SocialActivity.class);
                startActivity(Social);
                break;
            case R.id.nav_events:
                Intent eventIntent = new Intent(GeneralActivityM.this, EventsActivity.class);
                startActivity(eventIntent);
                break;
            case R.id.nav_profile:
                Intent profileIntent = new Intent(GeneralActivityM.this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(GeneralActivityM.this, LoginActivity.class);
                finish();
                startActivity(logoutIntent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openActivityInput(View v) {
        Intent input = new Intent(GeneralActivityM.this, InputActivity.class);
        startActivity(input);
    }

    int i = 0 ;
    private void getData(DatabaseReference dbf, String id){
        dbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(flag == 0){
                    for(int i=0;i<10;i++){
                        String m = snapshot.child("Category").child(id).child("General").child(String.valueOf(i)).child("Subject").getValue(String.class);
                        if(m.equals(""))
                            i++ ;
                        else
                        {
                            AddElements(m);
                            Adapter.notifyDataSetChanged();
                        }
                    }
                }
                flag = 1;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void findSubject(DatabaseReference dbf, String sub){
        dbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(flagNCS == 0){
                    //Log.d("numberrr","called");
                    for(i=0,NumberChekersub = "0";i<10;i++){
                        String nn = snapshot.child("Category").child(uid).child("General").child(String.valueOf(i)).child("Subject").getValue(String.class);
                        Log.d("numberrr","nn: "+nn+" sub: "+sub);
                        if(nn.equals(sub)){
                            NumberChekersub = String.valueOf(i);
                            //Log.d("numberrr","numberchekcer: "+NumberChekersub);
                            if(flagNCS == 0){
                                Log.d("dateeeeeeeeeeeeeeeee","categoryyyyy: "+NumberChekersub);
                                FirebaseDatabase.getInstance().getReference().child("Category").child(uid).child("General").child(NumberChekersub).child("Date")
                                        .setValue("");
                                FirebaseDatabase.getInstance().getReference().child("Category").child(uid).child("General").child(NumberChekersub).child("Description")
                                        .setValue("");
                                FirebaseDatabase.getInstance().getReference().child("Category").child(uid).child("General").child(NumberChekersub).child("Subject")
                                        .setValue("");
                                FirebaseDatabase.getInstance().getReference().child("Category").child(uid).child("General").child(NumberChekersub).child("Time")
                                        .setValue("");
                                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("levelup")
                                        .setValue(String.valueOf(lvl));
                                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("xp")
                                        .setValue(String.valueOf(x));
                                FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("workdone")
                                        .setValue(String.valueOf(wd));
                                flagNCS = 1;
                            }
                        }
                    }
                }
                flagNCS = 1;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getStat(DatabaseReference dbrr, String uid){
        dbrr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                levelup = snapshot.child("Users").child(uid).child("levelup").getValue(String.class);
                xp = snapshot.child("Users").child(uid).child("xp").getValue(String.class);
                workdone = snapshot.child("Users").child(uid).child("workdone").getValue(String.class);
                USER= snapshot.child("Users").child(uid).child("username").getValue(String.class);
                usertxt.setText(USER);

                if(levelup.length()>0){
                    lvl = Integer.parseInt(levelup);
                    x = Double.parseDouble(xp);
                    wd = Integer.parseInt(workdone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void AddElements(String m){
        items.add(new EnlistedItems() {
                      {
                          TEXT = m;
                      }
                  }
        );
    }
}
