package com.ticketapp.myticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfileActivity extends AppCompatActivity {

    Button btn_edit_profile,btn_signout;
    ImageView btn_back_arrow;

    @BindView(R.id.photo_profile) ImageView photo_profile;
    @BindView(R.id.fullname) TextView fullname;
    @BindView(R.id.bio) TextView bio;
    LinearLayout item_my_ticket;


    DatabaseReference reference,reference2;

    //ambil data user yg sedang login
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    RecyclerView myticket_place;
    ArrayList<MyTicket> list;
    TicketAdapter ticketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        ButterKnife.bind(this);

        getUserNameLocal();

        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        item_my_ticket = findViewById(R.id.item_my_ticket);
        btn_back_arrow = findViewById(R.id.btn_back_arrow);
        btn_signout = findViewById(R.id.btn_signout);


        myticket_place = findViewById(R.id.myticket_place);


        myticket_place = findViewById(R.id.myticket_place);
        myticket_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<MyTicket>();
        
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, EditProfileActivity.class));
            }
        });

        //ambil data dari table Users
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullname.setText(dataSnapshot.child("fullname").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());

                //use Picasso library to get the url image
                Picasso.with(MyProfileActivity.this).load(dataSnapshot.child("url_photo_profile").
                        getValue().toString()).centerCrop().fit().into(photo_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this,EditProfileActivity.class));
            }
        });

        //ambil data dari MyTicket Table
        reference2 = FirebaseDatabase.getInstance().getReference().child("MyOrderTickets").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                list = new ArrayList<MyTicket>();

//                Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();
//
//                for (Object obj : objectMap.values()) {
//                    if (obj instanceof Map) {
//                        Map<String, Object> mapObj = (Map<String, Object>) obj;
//                        MyTicket ticket = new MyTicket();
//                        ticket.setNama_wisata(mapObj.get("nama_wisata").toString());
//                        ticket.setJumlah_tiket(mapObj.get("jumlah_tiket").toString());
//                        ticket.setLokasi(mapObj.get("lokasi").toString());
////                            ticket.setLokasi(mapObj.get("lokasi").toString());
//
//                        list.add(ticket);
//                    }

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    MyTicket mt = dataSnapshot1.getValue(MyTicket.class);
                    list.add(mt);
                }

                ticketAdapter = new TicketAdapter(MyProfileActivity.this, list);
                myticket_place.setAdapter(ticketAdapter);
                ticketAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyProfileActivity.this, HomeActivity.class));
            }
        });

        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete isi/nilai/value dari username local
                //same with function save session local from register one activity
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key,null);
                editor.apply();

                //berpindah activity
                Intent gotosignin = new Intent(MyProfileActivity.this,LoginActivity.class);
                startActivity(gotosignin);
                finish();
            }
        });
    }

    //Control the user Session
    public void getUserNameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }

}
