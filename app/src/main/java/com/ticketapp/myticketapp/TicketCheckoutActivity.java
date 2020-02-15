package com.ticketapp.myticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Random;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketCheckoutActivity extends AppCompatActivity {

    Button btn_pay_now,btnmines,btnpls;
    LinearLayout btn_back;
    TextView textjumlahticket,txt_mybalance,txt_totaltiketprice;
    @BindView(R.id.notice_uang) ImageView notice_tiket;
    @BindView(R.id.nama_wisata) TextView nama_wisata;
    @BindView(R.id.lokasi) TextView lokasi;
    @BindView(R.id.ketentuan) TextView ketentuan;

    String USERNAME_KEY = "usernamekey";
    String username_key ="";
    String username_key_new="";

    String date_wisata = "";
    String time_wisata = "";


    //initial value for the ticket
    Integer valueTotalTicket = 1;
    Integer valuehargaTiket = 0;
    Integer myBalance = 0;
    Integer valuetotalHarga = 0;
    Integer no_transaksi = new Random().nextInt();
    Integer left_balance = 0;

    DatabaseReference reference,reference2,reference3,reference4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);
        getUsernameLocal();
        ButterKnife.bind(this);

//        //take data from Intent TicketDetailActivity
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");


        //take data from firebase
        //reference2 from Users
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myBalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                txt_mybalance.setText("US$ " + myBalance.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //reference from Wisata
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //bind new data
                nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                date_wisata= dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata= dataSnapshot.child("time_wisata").getValue().toString();
                valuehargaTiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                valuetotalHarga = valuehargaTiket*valueTotalTicket;
                txt_totaltiketprice.setText("US$ " + valuetotalHarga.toString());
                txt_mybalance.setText("US$ " + myBalance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_pay_now = findViewById(R.id.btn_pay_now);
        btn_back = findViewById(R.id.btn_back);
        btnmines = findViewById(R.id.btnmines);
        btnpls = findViewById(R.id.btnpls);
        textjumlahticket = findViewById(R.id.textjumlahticket);
        txt_mybalance = findViewById(R.id.txt_mybalance);
        txt_totaltiketprice = findViewById(R.id.txt_totaltiketprice);

        //set new value
        textjumlahticket.setText(valueTotalTicket.toString());


        //default condition, total tiket start from 1, btn mines hide
        btnmines.animate().alpha(0).setDuration(200).start();
        btnmines.setEnabled(false);
        notice_tiket.setVisibility(View.GONE);

        btnmines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when button mines tapped!
                valueTotalTicket--;
                textjumlahticket.setText(valueTotalTicket.toString());

                //condition when valuetotalticket > 1, show the button mines
                if (valueTotalTicket<2){
                    btnmines.animate().alpha(0).setDuration(200).start();
                    btnmines.setEnabled(false);
                }

                //update the total tiket price when tap btn mines
                valuetotalHarga = valuehargaTiket*valueTotalTicket;
                txt_totaltiketprice.setText("US$ " + valuetotalHarga.toString());

                if (valuetotalHarga<myBalance){
                    btn_pay_now.animate().translationY(0).setDuration(200).alpha(1).start();
                    btn_pay_now.setEnabled(false);
                    txt_mybalance.setTextColor(Color.parseColor("#203DD1"));
                    notice_tiket.setVisibility(View.GONE);
                }
            }
        });

        btnpls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when button mines tapped!
                valueTotalTicket++;
                textjumlahticket.setText(valueTotalTicket.toString());

                //when valueTotalTicket < 2, hide the button mines
                if (valueTotalTicket>1){
                    btnmines.animate().alpha(1).setDuration(200).start();
                    btnmines.setEnabled(true);
                }

                //update the total tiket price when tap btn plus
                valuetotalHarga = valuehargaTiket*valueTotalTicket;
                txt_totaltiketprice.setText("US$ " + valuetotalHarga.toString());

                if (valuetotalHarga>myBalance){
                    btn_pay_now.animate().translationY(200).setDuration(200).alpha(0).start();
                    btn_pay_now.setEnabled(false);
                    txt_mybalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_tiket.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save data to firebase then create new table MyTickets
                reference3 = FirebaseDatabase.getInstance().getReference().child("MyOrderTickets").child(username_key_new).child(nama_wisata.getText().toString() + no_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //save new data
                        reference3.getRef().child("id_ticket").setValue(nama_wisata.getText().toString() + no_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_tiket").setValue(valueTotalTicket.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //update user balance
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        left_balance = myBalance - valuetotalHarga;
                        reference4.getRef().child("user_balance").setValue(left_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                startActivity(new Intent(TicketCheckoutActivity.this,SuccessBuyTicketActivity.class));
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBacktoTicketDetail = new Intent(TicketCheckoutActivity.this,TicketDetailActivity.class);
                startActivity(goBacktoTicketDetail);
            }
        });

    }

    //store the current login user to local storage
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
