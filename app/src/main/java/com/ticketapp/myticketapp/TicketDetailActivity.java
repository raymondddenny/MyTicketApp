package com.ticketapp.myticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketDetailActivity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_buy_ticket;
    ImageView header_ticket_detail;
    TextView title_ticket,title_ticket_location,photo_spot_ticket,wifi_ticket,festival_ticket,short_desc;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);
        btn_back = findViewById(R.id.btn_back);

        header_ticket_detail = findViewById(R.id.header_ticket_detail);

        title_ticket = findViewById(R.id.title_ticket);
        title_ticket_location = findViewById(R.id.title_ticket_location);
        photo_spot_ticket = findViewById(R.id.photo_spot_ticket);
        wifi_ticket = findViewById(R.id.wifi_ticket);
        festival_ticket = findViewById(R.id.festival_ticket);
        short_desc = findViewById(R.id.short_desc);

        //take data from intent
        Bundle bundle = getIntent().getExtras();
        String jenis_tiket_baru = bundle.getString("jenis_tiket");


        //import data from database reference from the intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //overwrite existed data with new data
                title_ticket.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                title_ticket_location.setText(dataSnapshot.child("lokasi").getValue().toString());
                photo_spot_ticket.setText(dataSnapshot.child("is_photo_spot").getValue().toString());
                wifi_ticket.setText(dataSnapshot.child("is_wifi").getValue().toString());
                festival_ticket.setText(dataSnapshot.child("is_festival").getValue().toString());
                short_desc.setText(dataSnapshot.child("short_desc").getValue().toString());

                //use Picasso library to get the url image
                Picasso.with(TicketDetailActivity.this).load(dataSnapshot.child("url_thumbnail").
                        getValue().toString()).centerCrop().fit().into(header_ticket_detail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCheckout = new Intent(TicketDetailActivity.this,TicketCheckoutActivity.class);
                goToCheckout.putExtra("jenis_tiket",jenis_tiket_baru);
                startActivity(goToCheckout);
            }
        });
    }
}
