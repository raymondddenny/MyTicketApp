package com.ticketapp.myticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTicketDetailActivity extends AppCompatActivity {

    @BindView(R.id.xnama_wisata) TextView xnama_wisata;
    @BindView(R.id.xdate_wisata) TextView xdate_wisata;
    @BindView(R.id.xlokasi) TextView xlokasi;
    @BindView(R.id.xketentuan) TextView xketentuan;
    @BindView(R.id.xtime_wisata) TextView xtime_wisata;

    @OnClick(R.id.btn_refund)
    public void  gotorefundmenu()
    {
        Intent gotorefundmenu = new Intent(this,HomeActivity.class);
        startActivity(gotorefundmenu);
    }

    @OnClick(R.id.btn_back)
    public void gotomyprofile(){
        onBackPressed();
    }

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_wisata");

        //take data from firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //bind new data
//                TODO : MASIH BUG!
                xnama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                xlokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                xketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                xdate_wisata.setText(dataSnapshot.child("date_wisata").getValue().toString());
                xtime_wisata.setText(dataSnapshot.child("time_wisata").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
