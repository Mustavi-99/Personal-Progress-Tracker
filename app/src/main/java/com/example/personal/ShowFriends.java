package com.example.personal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowFriends extends AppCompatActivity {

    String myUID,UID;
    FirebaseUser user;
    DatabaseReference databaseReference;
    DatabaseReference dbr;
    String temp,temp2,temp3 = "";
    int i;

    public RecyclerView r1;
    public SecondAdapter Adapter;
    public RecyclerView.LayoutManager Manager;
    ArrayList<EnlistedItems> items = new ArrayList<EnlistedItems>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfriends);

        user = FirebaseAuth.getInstance().getCurrentUser();
        myUID = user.getUid();///
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        dbr =  FirebaseDatabase.getInstance().getReference();

        r1 = findViewById(R.id.RecyclerView_2);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (int i=0;i<5;i++){
                    temp = snapshot.child("Social").child(myUID).child("Friendlist").child(String.valueOf(i)).getValue(String.class);
                    temp2 = snapshot.child("Users").child(temp).child("username").getValue(String.class);

                    if(temp2!=temp3 && temp2!=null){
                        AddElements(temp2);
                        Adapter.notifyDataSetChanged();

                        Log.d("temppp","Temp :"+temp2+" i = "+i+" temp3= "+temp3);
                    }
                    temp3 = temp2;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //AddElements("Lawda Behenchodhh");
        //AddElements("khoniker chele");
        r1.setHasFixedSize(true);
        Manager = new LinearLayoutManager(this);
        Adapter = new SecondAdapter(items);
        r1.setLayoutManager(Manager);
        r1.setAdapter(Adapter);
        Adapter.setOnItemClickListener(new SecondAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EnlistedItems e = new EnlistedItems();
                e = items.get(position);
                String G = e.NAME;
                Log.d("GG","Deleting: "+G);
                getUID(databaseReference, myUID, G);        ///UID e G er uid store kora

                if(UID!=null){
                    Log.d("UID","UID: "+UID+" My UID: "+myUID);
                    removeFromDB(dbr);
                    items.remove(position);
                    Adapter.notifyItemRemoved(position);
                    Toast.makeText(ShowFriends.this, UID, Toast.LENGTH_SHORT).show();
                }                       //removing from database after declining

            }
        });
    }

    public void AddElements(String m){
        items.add(new EnlistedItems() {
                      {
                          NAME = m;
                      }
                  }
        );
    }

    public void removeFromDB(DatabaseReference dbbbb){
        dbbbb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d("UIDDDDDD","Ssssssssss: ");
                for(i=0;i<5;i++){
                    String s = snapshot.child("Social").child(myUID).child("Friendlist").child(String.valueOf(i)).getValue(String.class);
                    if(s.equals(UID)){
                        FirebaseDatabase.getInstance().getReference("Social").child(myUID)
                                .child("Friendlist").child(String.valueOf(i)).setValue("");
                        Log.d("UIDDDDDD","S: "+s);
                        break;
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUID(DatabaseReference dbf, String id, String friendname) {           ///getting UID using username
        dbf.addValueEventListener(new ValueEventListener() {        /////
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    String DBusername = dsp.child("username").getValue(String.class);
                    if (DBusername.equals(friendname)) {
                        UID = dsp.child("uid").getValue(String.class);
                        Log.d("UIDDD","UID :"+UID);
                        break;
                    }
                }
                return;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}