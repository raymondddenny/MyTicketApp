package com.ticketapp.myticketapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessBuyTicketActivity extends AppCompatActivity {

    @OnClick(R.id.btn_view_ticket)
    public void goToMyticeketView (View view){
        Intent gotomyticketview = new Intent(this,MyProfileActivity.class);
        startActivity(gotomyticketview);
    }

    @OnClick(R.id.btn_my_dashboard)
    public void gotodashboard (View view){
        Intent gotomydashboard = new Intent( this,HomeActivity.class);
        startActivity(gotomydashboard);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);
        ButterKnife.bind(this);

    }
}
