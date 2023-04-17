package com.example.personal;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
public class SocialActivity extends AppCompatActivity implements View.OnClickListener {
    Button checkbutton;
    Button sendbutton;
    EditText Sentusername;
    String myUID;
    FirebaseUser user;
    DatabaseReference databaseReference;
    DatabaseReference dbr,dbref;
    String originalUser;
    String friendUID;
    String UID;
    String friendUsername = "";
    String NumberChecker = null;
    int flagSendreq = 0;
    int flagaddreq = 0;

    String Date[] = {"","","","","","","","","",""};
    String Time[] = {"","","","","","","","","",""};
    String description[] = {"","","","","","","","","",""};
    String sub[] = {"","","","","","","","","",""};
    String categoryString[] = {"","","","","","","","","",""};
    String Number[] = {"","","","","","","","","",""};
    String SourceString []= {"","","","","","","","","",""};
    String ttt[] = {"","","","","","","","","",""};

    String NumberChecker2 = "0";
    String NumberChecker3 = "";
    String NumberChecker4 = "";
    String NCUID = "";
    String NCfriendUID = "";
    String SourceCategory = "";
    String SourceUID = "";
    String pendingSub = "";
    int Position;
    int flag2 = 0;
    int NCUIDflag = 0;
    int NCfriendUIDflag = 0;


    String check = "";
    String temp = "",temp2 = "",temp3 = "";

    Dialog dialouge ;
    int i, flagg = 0;
    public RecyclerView r1;
    public RecyclerView r2;
    public MyAdapter Adapter;
    public RecyclerView.LayoutManager Manager;
    ArrayList<EnlistedItems> items = new ArrayList<EnlistedItems>();
    public MyAdapter Adapter2;
    public RecyclerView.LayoutManager Manager2;
    ArrayList<EnlistedItems> items2 = new ArrayList<EnlistedItems>();
    Button Viewfriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        user = FirebaseAuth.getInstance().getCurrentUser();
        myUID = user.getUid();///

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        dbref = FirebaseDatabase.getInstance().getReference("Social");
        dbr =  FirebaseDatabase.getInstance().getReference();

        checkbutton = (Button) findViewById(R.id.CheckButton);
        sendbutton = (Button) findViewById(R.id.SendButton);
        sendbutton.setEnabled(false);
        checkbutton.setEnabled(true);

        Sentusername = (EditText) findViewById(R.id.inputUsername);
        dialouge = new Dialog(SocialActivity.this) ;
        dialouge.setContentView(R.layout.customdialog);
        dialouge.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView des = dialouge.findViewById(R.id.descriptionText);
        TextView time = dialouge.findViewById(R.id.texttime);
        TextView date = dialouge.findViewById(R.id.textDate);
        getValuesOfPendingWorks(dbr);

        Viewfriend = (Button) findViewById(R.id.ViewFriends);
        Viewfriend.setOnClickListener(this::onClickview) ;
        r1 = findViewById(R.id.RecyclerView_1);
        r1.setHasFixedSize(true);
        Manager = new LinearLayoutManager(this);
        Adapter = new MyAdapter(items);
        r1.setLayoutManager(Manager);
        r1.setAdapter(Adapter);
        Adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EnlistedItems e = new EnlistedItems();
                e = items.get(position);
                String G = e.NAME;
                Log.d("GG","Deleting: "+G);
                getUID(databaseReference, myUID, G);        ///UID e G er uid store kora



            }
            public void onDeclineclick(int position) {
                EnlistedItems e = new EnlistedItems();
                e = items.get(position);
                String G = e.NAME;
                Log.d("GG","Deleting: "+G);
                getUID(databaseReference, myUID, G);        ///UID e G er uid store kora
                //getUID(databaseReference, myUID, G);        ///UID e G er uid store kora
                if(UID!=null){
                    Log.d("UID","UID: "+UID+" My UID: "+myUID);
                    removeFromDB(dbr);
                    items.remove(position);
                    Adapter.notifyItemRemoved(position);
                    Toast.makeText(SocialActivity.this, UID, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onAcceptclick(int position) {
                EnlistedItems e = new EnlistedItems();
                e = items.get(position);
                String G = e.NAME;
                Log.d("GG","Deleting: "+G);
                getUID(databaseReference, myUID, G);        ///UID e G er uid store kora
                if(UID!=null) {
                    searchBlankfriendUID(dbr, UID);
                    flagaddreq = 0;
                    AddtoFriendlist(dbr);
                    items.remove(position);
                    Adapter.notifyItemRemoved(position);
                    Toast.makeText(SocialActivity.this, "Request Accepted...", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // Second Recycle View of the pending social tasks......
        //AddElements2("NELOY BARMAN") ;
        //AddElements2("Sanjay Mandal") ;
        r2 = findViewById(R.id.RecyclerView_2);
        r2.setHasFixedSize(true);
        Manager2 = new LinearLayoutManager(this);
        Adapter2 = new MyAdapter(items2);
        r2.setLayoutManager(Manager2);
        r2.setAdapter(Adapter2);
        Adapter2.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //pending social task theke shob read koira eidike set kore de...
                des.setText(description[position]);
                time.setText(Time[position]);
                date.setText(Date[position]);
                searchBlank(dbr,categoryString[position]);
                getNUMBER(dbr, myUID, sub[position]);
                Position = position;
                dialouge.show();
            }
            public void onDeclineclick(int position) {              ///Recycle view er button

                Log.d("PendingWorks","Upore Numberchecker2 = "+Number[Position]+" SourceCategory = "+categoryString[0]+" SourceUID: "+SourceUID+" Sub:"+sub[0]);
                /*for(i = Position;i<9;i++){
                    SourceString[9] = "";   // 1  2 3 ""
                    SourceString[i] = SourceString[i+1];
                    categoryString[i] = categoryString[i+1];
                    Date[i] = Date[i+1];
                    Time[i] = Time[i+1];
                    sub[9] = "";
                    sub[i] = sub[i+1];
                    description[i] = description[i+1];
                    Number[i] = Number[i+1];

                }*/
                for (i =0;i<9;i++){
                    if (ttt[i].equals(sub[position])){
                        resetPendingWorks(i);
                        items2.remove(Position);
                        Adapter2.notifyItemRemoved(Position);
                        break;
                        //items2.remove(Position);
                        //Adapter2.notifyItemRemoved(Position);
                    }
                }
                //Toast.makeText(SocialActivity.this,"Decline works",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAcceptclick(int position) {               ///Recycle view er button
                for (i =0;i<9;i++){
                    if (ttt[i].equals(sub[position])){
                        if(NumberChecker4.length()>0){
                            setPendingWorks(i,NumberChecker4);
                        }
                        items2.remove(Position);
                        Adapter2.notifyItemRemoved(Position);
                        break;
                        //items2.remove(Position);
                        //Adapter2.notifyItemRemoved(Position);
                    }
                }
                //Toast.makeText(SocialActivity.this,"ACCEPT works",Toast.LENGTH_SHORT).show();

            }
        });

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i=0;i<5;i++){
                    temp = snapshot.child("Social").child(myUID).child("PendingStatus").child(String.valueOf(i)).getValue(String.class);
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
        checkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendUsername = Sentusername.getText().toString();
                flagSendreq = 0;
                getData(databaseReference, myUID);

                //if(!NumberChecker.equals(null))
                //FirebaseDatabase.getInstance().getReference("Social").child(friendUID).child("PendingStatus").child(NumberChecker).setValue("1");

                //Log.d("checker","Returned to value with NumberChecker = "+NumberChecker);
            }
        });

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Social").child(friendUID)
                        .child("PendingStatus").child(NumberChecker).setValue(myUID);
                sendbutton.setEnabled(false);
                checkbutton.setEnabled(true);
                Sentusername.setText("");
                Toast.makeText(SocialActivity.this,"Friend Request Sent!",Toast.LENGTH_SHORT).show() ;
            }
        });


    } ///end of onCreate

    private void searchBlankmyUID(DatabaseReference dbr, String uid) {
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(NCUIDflag ==0){
                    for(i=0,NCUID="0";i<=10;i++){
                        String catefaka = snapshot.child("Social").child(uid).child("Friendlist").child(String.valueOf(i)).getValue(String.class);
                        if(i==10){
                            Toast.makeText(SocialActivity.this,"Can't accept any more",Toast.LENGTH_SHORT).show() ;
                            break;
                        }

                        if(catefaka.equals("")){
                            NCUID = String.valueOf(i);
                            //Log.d("checker","Checking checker = "+check+" value of i = "+i);
                            //FirebaseDatabase.getInstance().getReference("Social").child(friendUID).child("PendingStatus").child(NumberChecker).setValue("1");
                            //Toast.makeText(SocialActivity.this,"i = "+i,Toast.LENGTH_SHORT).show() ;
                            break;
                            //return;
                        }
                        //
                    }
                }
                NCUIDflag =1;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void searchBlankfriendUID(DatabaseReference dbr, String uid) {
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(NCfriendUIDflag ==0){
                    for(i=0,NCfriendUID="0";i<=10;i++){
                        String catefaka = snapshot.child("Social").child(uid).child("Friendlist").child(String.valueOf(i)).getValue(String.class);
                        if(i==10){
                            Toast.makeText(SocialActivity.this,"Can't accept any more",Toast.LENGTH_SHORT).show() ;
                            break;
                        }

                        if(catefaka.equals("")){
                            NCfriendUID = String.valueOf(i);
                            //Log.d("checker","Checking checker = "+check+" value of i = "+i);
                            //FirebaseDatabase.getInstance().getReference("Social").child(friendUID).child("PendingStatus").child(NumberChecker).setValue("1");
                            //Toast.makeText(SocialActivity.this,"i = "+i,Toast.LENGTH_SHORT).show() ;
                            break;
                            //return;
                        }
                        //
                    }
                }
                NCfriendUIDflag =1;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showDialogueBoxData(DatabaseReference dbr, String id){
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Date = snapshot.child("Category").child(id).child()
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getValuesOfPendingWorks(DatabaseReference dbr){
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int j;
                for (i=0,j=0;i<9;i++){
                    NumberChecker2 = snapshot.child("Social").child(myUID).child("PendingWorks").child("NumberChecker").child(String.valueOf(i)).getValue(String.class);
                    SourceCategory = snapshot.child("Social").child(myUID).child("PendingWorks").child("SourceCategory").child(String.valueOf(i)).getValue(String.class);
                    SourceUID = snapshot.child("Social").child(myUID).child("PendingWorks").child("SourceUID").child(String.valueOf(i)).getValue(String.class);
                    pendingSub = snapshot.child("Social").child(myUID).child("PendingWorks").child("PendingSubject").child(String.valueOf(i)).getValue(String.class);

                    if(SourceUID.equals(""))
                        continue;

                    if(SourceUID.length()>2){
                        SourceString[j] = SourceUID;
                        Number[j] = NumberChecker2;
                        categoryString[j] = SourceCategory;
                        sub[j] =  pendingSub;
                        description[j] = snapshot.child("Category").child(SourceUID).child(SourceCategory).child(NumberChecker2).child("Description").getValue(String.class);
                        Time[j] = snapshot.child("Category").child(SourceUID).child(SourceCategory).child(NumberChecker2).child("Time").getValue(String.class);
                        Date[j] = snapshot.child("Category").child(SourceUID).child(SourceCategory).child(NumberChecker2).child("Date").getValue(String.class);

                        if(flagg == 0){
                            if(sub[j].length()>1){
                                Log.d("balda","Call hoy balda");
                                AddElements2(sub[j]);
                                Adapter2.notifyDataSetChanged();
                            }
                        }
                        j++;
                        //Log.d("PendingWorks","Numberchecker2 = "+Number[1]+" SourceCategory = "+categoryString[1]+" SourceUID: "+SourceUID+" Sub:"+sub[1]);
                    }
                    Log.d("PendingWorks","sub: "+sub[i]+" UID = "+SourceUID);

                }
                flagg = 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    public void AddElements2(String m){
        items2.add(new EnlistedItems() {
                       {
                           NAME = m;
                       }
                   }
        );
    }
    public void viewPendingRequest(DatabaseReference dbr,String myuid){
    }
    @Override
    public void onClick(View v) {
    }
    private void onClickview(View v) {
        Intent showfriends = new Intent(SocialActivity.this,ShowFriends.class);
        startActivity(showfriends);
    }

    public void AddtoFriendlist(DatabaseReference dbbb){
        dbbb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(flagaddreq == 0){
                    for(i=0;i<5;i++){
                        String s = snapshot.child("Social").child(myUID).child("PendingStatus").child(String.valueOf(i)).getValue(String.class);
                        if(s.equals(UID)){
                            FirebaseDatabase.getInstance().getReference("Social").child(myUID)
                                    .child("PendingStatus").child(String.valueOf(i)).setValue("");
                            FirebaseDatabase.getInstance().getReference("Social").child(UID)
                                    .child("Friendlist").child(String.valueOf(i)).setValue(myUID);

                            FirebaseDatabase.getInstance().getReference("Social").child(myUID)
                                    .child("Friendlist").child(NCfriendUID).setValue(UID);

                            FirebaseDatabase.getInstance().getReference("Social").child(myUID)
                                    .child("Friendlist").child(String.valueOf(i)).setValue(UID);
                            Log.d("UIDDDDDD","uid: "+UID+" Myuid: "+myUID);
                            break;
                        }
                    }
                }
                flagaddreq = 1;

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void removeFromDB(DatabaseReference dbbbb){
        dbbbb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d("UIDDDDDD","Ssssssssss: ");
                for(i=0;i<5;i++){
                    String s = snapshot.child("Social").child(myUID).child("PendingStatus").child(String.valueOf(i)).getValue(String.class);
                    if(s.equals(UID)){
                        FirebaseDatabase.getInstance().getReference("Social").child(myUID)
                                .child("PendingStatus").child(String.valueOf(i)).setValue("");
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
    public void getNUMBER(DatabaseReference dbf, String id, String subjectt) {           ///getting UID using username
        dbf.addValueEventListener(new ValueEventListener() {        /////
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(i=0,NumberChecker3="0";i<10;i++){
                    ttt[i] = snapshot.child("Social").child(id).child("PendingWorks").child("PendingSubject").child(String.valueOf(i)).getValue(String.class);
                    //Log.d("ttt","ttt "+i+" = "+ttt[i]);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void resetPendingWorks(int i){
        Log.d("Number3","Number = "+NumberChecker3);

        FirebaseDatabase.getInstance().getReference().child("Social").child(myUID).child("PendingWorks")
                .child("NumberChecker").child(String.valueOf(i)).setValue("");
        FirebaseDatabase.getInstance().getReference().child("Social").child(myUID).child("PendingWorks")
                .child("SourceCategory").child(String.valueOf(i)).setValue("");
        FirebaseDatabase.getInstance().getReference().child("Social").child(myUID).child("PendingWorks")
                .child("SourceUID").child(String.valueOf(i)).setValue("");
        FirebaseDatabase.getInstance().getReference().child("Social").child(myUID).child("PendingWorks")
                .child("PendingSubject").child(String.valueOf(i)).setValue("");
        //Log.d("possss","position: "+position+" Number: "+NumberChecker3);
    }

    public void setPendingWorks(int i,String nc4){

        if(NumberChecker4.length()>0){
            FirebaseDatabase.getInstance().getReference().child("Category").child(myUID).child(categoryString[i])
                    .child(nc4).child("Date").setValue(Date[i]);
            FirebaseDatabase.getInstance().getReference().child("Category").child(myUID).child(categoryString[i])
                    .child(nc4).child("Subject").setValue(sub[i]);
            FirebaseDatabase.getInstance().getReference().child("Category").child(myUID).child(categoryString[i])
                    .child(nc4).child("Description").setValue(description[i]);
            FirebaseDatabase.getInstance().getReference().child("Category").child(myUID).child(categoryString[i])
                    .child(nc4).child("Time").setValue(Time[i]);
        }



        FirebaseDatabase.getInstance().getReference().child("Social").child(myUID).child("PendingWorks")
                .child("NumberChecker").child(String.valueOf(i)).setValue("");
        FirebaseDatabase.getInstance().getReference().child("Social").child(myUID).child("PendingWorks")
                .child("SourceCategory").child(String.valueOf(i)).setValue("");
        FirebaseDatabase.getInstance().getReference().child("Social").child(myUID).child("PendingWorks")
                .child("SourceUID").child(String.valueOf(i)).setValue("");
        FirebaseDatabase.getInstance().getReference().child("Social").child(myUID).child("PendingWorks")
                .child("PendingSubject").child(String.valueOf(i)).setValue("");
        //Log.d("possss","position: "+position+" Number: "+NumberChecker3);
    }

    public void getData(DatabaseReference dbf, String id) {


        dbf.addValueEventListener(new ValueEventListener() {        /////
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //originalUser = snapshot.child("Users").child(uid).child("username").getValue(String.class);

                for (DataSnapshot dsp : snapshot.getChildren()) {
                    String DBusername = dsp.child("username").getValue(String.class);
                    if (DBusername.equals(friendUsername)) {
                        friendUID = dsp.child("uid").getValue(String.class);     ///jekhane jete chaitesi oi ID paitesi...bortoman id paitese na
                        Log.d("upper","Log er upor myUID"+myUID+" friendUID: " + friendUID);

                        //check = snapshot.child("Social").child(friendUID).child("PendingStatus").child("0").getValue(String.class);

                        break;
                    }
                    //String num = dsp.child("num").getValue(String.class);
                }
                sendFriendRequest(dbr);

                //Toast.makeText(SocialActivity.this,myUID+" "+friendUID ,Toast.LENGTH_SHORT).show() ;
                return;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void searchBlank(DatabaseReference databbb, String categoryy){
        databbb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(flag2 ==0){
                    for(i=0,NumberChecker4="0";i<=10;i++){
                        String catefaka = snapshot.child("Category").child(myUID).child(categoryy).child(String.valueOf(i)).child("Date").getValue(String.class);
                        if(i==10){
                            Toast.makeText(SocialActivity.this,"Can't accept any more",Toast.LENGTH_SHORT).show() ;
                            break;
                        }

                        if(catefaka.equals("")){
                            NumberChecker4 = String.valueOf(i);
                            //Log.d("checker","Checking checker = "+check+" value of i = "+i);
                            //FirebaseDatabase.getInstance().getReference("Social").child(friendUID).child("PendingStatus").child(NumberChecker).setValue("1");
                            //Toast.makeText(SocialActivity.this,"i = "+i,Toast.LENGTH_SHORT).show() ;
                            break;
                            //return;
                        }
                        //
                    }
                }
                flag2 =1;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendFriendRequest(DatabaseReference db) {

        db.addValueEventListener(new ValueEventListener() {        /////
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d("checker","send friend req called");
                if(flagSendreq==0){
                    for(i=0,NumberChecker="0";i<=10;i++){
                        check = snapshot.child("Social").child(friendUID).child("PendingStatus").child(String.valueOf(i)).getValue(String.class);
                        if(i==10){
                            Toast.makeText(SocialActivity.this,"Can't accept any more friend requests",Toast.LENGTH_SHORT).show() ;
                            break;
                        }

                        if(check.equals("")){
                            sendbutton.setEnabled(true);
                            checkbutton.setEnabled(false);
                            NumberChecker = String.valueOf(i);
                            Toast.makeText(SocialActivity.this,"This username exists in database",Toast.LENGTH_SHORT).show() ;
                            break;
                        }
                    }
                    flagSendreq = 1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}