package com.ticketapp.myticketapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {

    TextView btn_new_account;
    EditText xusername,xpassword;
    Button xbtn_sign_in;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String usernamekey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_new_account = findViewById(R.id.btn_new_account);
        xbtn_sign_in = findViewById(R.id.xbtn_sign_in);
        xusername =  findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);


        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterOneActivity.class));
            }
        });

        xbtn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //change state to loading when btn clicked!
                xbtn_sign_in.setEnabled(false);
                xbtn_sign_in.setText("Logging in...");

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if(username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Username cannot be empty",Toast.LENGTH_SHORT).show();
                    //change state to loading when btn clicked!
                    xbtn_sign_in.setEnabled(true);
                    xbtn_sign_in.setText("SIGN IN");
                } else {
                    if (password.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Password cannot be empty",Toast.LENGTH_SHORT).show();
                        //change state to loading when btn clicked!
                        xbtn_sign_in.setEnabled(true);
                        xbtn_sign_in.setText("SIGN IN");
                    } else {
                        reference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    //take password from database
                                    String passwordFromDatabase = dataSnapshot.child("password").getValue().toString();

                                    //validasi pass dengan pass database
                                    if (password.equals(passwordFromDatabase)){
                                        //save username and pass to local
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(usernamekey,xusername.getText().toString());
                                        editor.apply();

                                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(),"Wrong Password!",Toast.LENGTH_SHORT).show();
                                        //change state to loading when btn clicked!
                                        xbtn_sign_in.setEnabled(true);
                                        xbtn_sign_in.setText("SIGN IN");
                                    }

//                            TODO: kondisi jika form kosong belum k handle
//                            Toast.makeText(getApplicationContext(),"Your Account has been registered!",Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(),"Your Account has not been registered!",Toast.LENGTH_SHORT).show();
                                    //change state to loading when btn clicked!
                                    xbtn_sign_in.setEnabled(true);
                                    xbtn_sign_in.setText("SIGN IN");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),"Database Error",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
