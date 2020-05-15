package com.sarkar.catdog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class Result extends AppCompatActivity {

    DatabaseReference reference;
    StorageReference storageRef;
    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        final String stringid;
        final Uri img_uri;
        Intent intent=getIntent();
        storageRef= FirebaseStorage.getInstance().getReference();
        Bundle extras = intent.getExtras();
        stringid= extras.getString("id");
        final Bitmap bmp;
        byte[] byteArray = getIntent().getByteArrayExtra("img");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);




        textView=findViewById(R.id.result);
        imageView=findViewById(R.id.img2);
        reference= FirebaseDatabase.getInstance().getReference("requests");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("id").exists()&& snapshot.child("result").exists()){
                        if(snapshot.child("id").getValue(String.class).equals(stringid)){
                            String value=snapshot.child("result").getValue(String.class);
                            if(!value.equals("null")) {
                                textView.setText(value);
                                if(snapshot.child("permit").getValue(String.class).equals("1"))
                                    getfile(bmp);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Result.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getfile(Bitmap bmp) {
        imageView.setImageBitmap(bmp);
    }
}
