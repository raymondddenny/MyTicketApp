package com.ticketapp.myticketapp;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditProfileActivity extends AppCompatActivity {


    @BindView(R.id.pic_profile_register_user)
    ImageView pic_profile_register_user;
    @BindView(R.id.txt_fullname)
    EditText txt_fullname;
    @BindView(R.id.txt_bio)
    EditText txt_bio;
    @BindView(R.id.txt_new_password)
    EditText txt_new_password;
    @BindView(R.id.txt_username)
    EditText txt_username;
    @BindView(R.id.txt_new_email)
    EditText txt_new_email;

    Uri photo_location;
    //only can upload 1 photo
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storageReference;

    String USERNAME_KEY  = "usernamekey";
    String username_key  = "";
    String username_key_new  = "";

    @OnClick(R.id.btn_back)
    public void gotomyprofile(){
        onBackPressed();
    }

    @OnClick(R.id.btn_save_edit_profile)
    public void gotoprofile (){
        //save the update to database
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("fullname").setValue(txt_fullname.getText().toString());
                dataSnapshot.getRef().child("bio").setValue(txt_bio.getText().toString());
                dataSnapshot.getRef().child("username").setValue(txt_username.getText().toString());
                dataSnapshot.getRef().child("password").setValue(txt_new_password.getText().toString());
                dataSnapshot.getRef().child("email_database").setValue(txt_new_password.getText().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //validasi file in database
        if (photo_location != null){
            final StorageReference storageReference1 = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));

            //jika sukses
            storageReference1.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //upload url
                            String uri_photo = uri.toString();
                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            //pindah activity
                            Intent gotomyprofile = new Intent(EditProfileActivity.this,MyProfileActivity.class);
                            startActivity(gotomyprofile);
                        }
                    });
                }
            });
        }
    }

    @OnClick(R.id.btn_add_new_photo_profile)
    public void addnewphoto(){
        findPhoto();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        getUserNameLocal();

        //set storage reference from firebase
        storageReference = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);
        //get data from database
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                txt_fullname.setText(dataSnapshot.child("fullname").getValue().toString());
                txt_bio.setText(dataSnapshot.child("bio").getValue().toString());
                txt_username.setText(dataSnapshot.child("username").getValue().toString());
                txt_new_password.setText(dataSnapshot.child("password").getValue().toString());
                txt_new_email.setText(dataSnapshot.child("email_database").getValue().toString());

                //get existed photo profile then change with new one
                Picasso.with(EditProfileActivity.this)
                        .load(dataSnapshot.child("url_photo_profile")
                                .getValue().toString()).centerCrop().fit()
                        .into(pic_profile_register_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //picasso get file tyoe
    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        //tipe file
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //picasso find photo from device
    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic,photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //after findphoto ()
        //validasi if the photo already upload
        if (requestCode==photo_max && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_profile_register_user);
        }
    }

    public void getUserNameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
