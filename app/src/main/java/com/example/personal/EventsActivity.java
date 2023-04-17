package com.example.personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class EventsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar tool;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String uid;
    int flag =0;
    int flagNCS = 0;
    String levelup = "";
    String xp = "";
    String workdone = "";
    int lvl;
    Double x ;
    int wd;
    int i ;
    TextView usertxt;
    String USER;
    String NumberChekersub = "";
    FloatingActionButton FloatButton;
    public RecyclerView recyclerevents ;
    public MyThirdAdapter Adapter;
    public RecyclerView.LayoutManager Manager;
    ArrayList<EnlistedItems> items = new ArrayList<EnlistedItems>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        usertxt=(TextView) findViewById(R.id.usertxt);
        getData(databaseReference, uid);
        getStat(databaseReference,uid);

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
        recyclerevents = findViewById(R.id.RecyclerView_events);
        recyclerevents.setHasFixedSize(true);
        Manager = new LinearLayoutManager(this);
        Adapter = new MyThirdAdapter(items);
        recyclerevents.setLayoutManager(Manager);
        recyclerevents.setAdapter(Adapter);
        Adapter.setOnItemClickListener(new MyThirdAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(int position) {
                Toast.makeText(EventsActivity.this,""+position,Toast.LENGTH_SHORT).show();
                String pose;
                Intent General = new Intent(EventsActivity.this,GeneralActivity.class) ;
                String name = "Events";
                General.putExtra("NAME",name);
                pose = String.valueOf(position);
                General.putExtra("Position",pose);
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
                    Toast.makeText(EventsActivity.this, "Level Up", Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(EventsActivity.this,"Done Clicked..and subject = ",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCheckclick(int position) {
                EnlistedItems e = new EnlistedItems();
                e = items.get(position);
                String sub = e.TEXT;
                //Log.d("numberrrr","onCheck sub: "+sub);
                flagNCS = 0;
                findSubject(databaseReference,sub);
                items.remove(position);
                Adapter.notifyDataSetChanged();
                //Toast.makeText(EventsActivity.this,"Check Clicked..",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_general:
                Intent generalIntent = new Intent(EventsActivity.this, GeneralActivityM.class);
                startActivity(generalIntent);
                break;
            case R.id.nav_health:
                Intent healthIntent = new Intent(EventsActivity.this, HealthActivity.class);
                startActivity(healthIntent);
                break;
            case R.id.nav_work:
                Intent workIntent = new Intent(EventsActivity.this, WorkActivity.class);
                startActivity(workIntent);
                break;
            case R.id.nav_education:
                Intent educationIntent = new Intent(EventsActivity.this, EducationActivity.class);
                startActivity(educationIntent);
                break;
            case R.id.nav_food:
                Intent foodIntent = new Intent(EventsActivity.this, FoodActivity.class);
                startActivity(foodIntent);
                break;
            case R.id.nav_friends:
                Intent Social = new Intent(EventsActivity.this, SocialActivity.class);
                startActivity(Social);
                break;
            case R.id.nav_events:
                Intent eventIntent = new Intent(EventsActivity.this, EventsActivity.class);
                startActivity(eventIntent);
                break;
            case R.id.nav_profile:
                Intent profileIntent = new Intent(EventsActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(EventsActivity.this, LoginActivity.class);
                finish();
                startActivity(logoutIntent);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getData(DatabaseReference dbf, String id){
        dbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(flag == 0){
                    for(int i=0;i<10;i++){
                        String m = snapshot.child("Category").child(id).child("Events").child(String.valueOf(i)).child("Subject").getValue(String.class);
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
                        String nn = snapshot.child("Category").child(uid).child("Events").child(String.valueOf(i)).child("Subject").getValue(String.class);
                        Log.d("numberrr","nn: "+nn+" sub: "+sub);
                        if(nn.equals(sub)){
                            NumberChekersub = String.valueOf(i);
                            //Log.d("numberrr","numberchekcer: "+NumberChekersub);
                            if(flagNCS == 0){
                                Log.d("dateeeeeeeeeeeeeeeee","categoryyyyy: "+NumberChekersub);
                                FirebaseDatabase.getInstance().getReference().child("Category").child(uid).child("Events").child(NumberChekersub).child("Date")
                                        .setValue("");
                                FirebaseDatabase.getInstance().getReference().child("Category").child(uid).child("Events").child(NumberChekersub).child("Description")
                                        .setValue("");
                                FirebaseDatabase.getInstance().getReference().child("Category").child(uid).child("Events").child(NumberChekersub).child("Subject")
                                        .setValue("");
                                FirebaseDatabase.getInstance().getReference().child("Category").child(uid).child("Events").child(NumberChekersub).child("Time")
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
    public void openActivityInput(View v) {
        Intent input = new Intent(EventsActivity.this, InputActivity.class);
        startActivity(input);
    }
}
