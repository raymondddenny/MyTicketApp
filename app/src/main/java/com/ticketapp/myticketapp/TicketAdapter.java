package com.ticketapp.myticketapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

// untuk listing content dari firebase
public class TicketAdapter extends RecyclerView.Adapter <TicketAdapter.MyViewHolder> {

    //data sebagai content

    Context context;
    ArrayList<MyTicket> myTickets;

    public TicketAdapter(Context c, ArrayList<MyTicket> mt){
        context = c;
        myTickets = mt;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //replace existed data using inflater
        //item_myticket akan di inflate di recycler View
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myticket, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //binding data
        //get data from database
        holder.xnama_wisata.setText(myTickets.get(position).getNama_wisata());
        holder.xjumlah_tiket.setText(myTickets.get(position).getJumlah_tiket() + " Tickets");
        holder.xlokasi.setText(myTickets.get(position).getLokasi());

        String getNamaWisata = myTickets.get(position).getNama_wisata();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyticketdetails = new Intent(context,MyTicketDetailActivity.class);
                gotomyticketdetails.putExtra("nama_wisata",getNamaWisata);
                context.startActivity(gotomyticketdetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myTickets.size();
    }

    //class untuk define component
    class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.xnama_wisata) TextView xnama_wisata;
        @BindView(R.id.xlokasi) TextView xlokasi;
        @BindView(R.id.xjumlah_tiket) TextView xjumlah_tiket;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
