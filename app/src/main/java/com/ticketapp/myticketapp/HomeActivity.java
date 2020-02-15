package com.ticketapp.myticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    LinearLayout btn_ticket_one,btn_ticket_two,btn_ticket_three,btn_ticket_four,btn_ticket_five,btn_ticket_six;
    CircleView btn_my_profile;
    ImageView photo_home_user;
    TextView fullname,bio,user_balance;

    //untuk ambil data dari firebase berdasar username
    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String usernamekey = "";
    String usernamekey_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUserNameLocal();


        btn_ticket_one = findViewById(R.id.btn_ticket_one);
        btn_ticket_two = findViewById(R.id.btn_ticket_two);
        btn_ticket_three = findViewById(R.id.btn_ticket_three);
        btn_ticket_four = findViewById(R.id.btn_ticket_four);
        btn_ticket_five = findViewById(R.id.btn_ticket_five);
        btn_ticket_six = findViewById(R.id.btn_ticket_six);

        btn_my_profile = findViewById(R.id.btn_my_profile);

        photo_home_user = findViewById(R.id.photo_home_user);
        fullname = findViewById(R.id.fullname);
        bio = findViewById(R.id.bio);
        user_balance = findViewById(R.id.user_balance);

        //ambil data dari user yg sedang login
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(usernamekey_new);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullname.setText(dataSnapshot.child("fullname").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                user_balance.setText("$ " + dataSnapshot.child("user_balance").getValue().toString());

                Picasso.with(HomeActivity.this).load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_home_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_ticket_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,TicketDetailActivity.class);
                //put data to intent
                //jenis_tiket + nama_wisata dari table Wisata
                intent.putExtra("jenis_tiket","Pisa");
                startActivity(intent);
            }
        });

        btn_ticket_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,TicketDetailActivity.class);
                intent.putExtra("jenis_tiket","Monas");
                startActivity(intent);
            }
        });

        btn_ticket_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,TicketDetailActivity.class);
                intent.putExtra("jenis_tiket","Candi");
                startActivity(intent);
            }
        });

        btn_ticket_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,TicketDetailActivity.class);
                intent.putExtra("jenis_tiket","Pagoda");
                startActivity(intent);
            }
        });

        btn_ticket_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,TicketDetailActivity.class);
                intent.putExtra("jenis_tiket","Sphinx");
                startActivity(intent);
            }
        });

        btn_ticket_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,TicketDetailActivity.class);
                intent.putExtra("jenis_tiket","Torri");
                startActivity(intent);
            }
        });


        btn_my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MyProfileActivity.class));
            }
        });
    }

    public void getUserNameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        usernamekey_new = sharedPreferences.getString(usernamekey,"");
    }
}
