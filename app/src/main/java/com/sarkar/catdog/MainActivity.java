package com.sarkar.catdog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

import androidx.annotation.NonNull;

public class MainActivity extends Activity {
    ImageView imageView;
    String stringid;
    StorageReference storageRef;
    Bitmap bitmap = null;
    ByteArrayOutputStream bStream;
    DatabaseReference reference;
    Button button;
    byte[] byteArray;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storageRef= FirebaseStorage.getInstance().getReference();
        reference=FirebaseDatabase.getInstance().getReference("requests");
        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button)findViewById(R.id.buttonLoadPicture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byteArray = bStream.toByteArray();
                uploadimage(byteArray);
            }
        });
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    private void uploadimage(final byte[] byteArray) {
        if(imageUri!=null)
        {
            stringid=idgenerator();
            //stringid=stringid+".jpg";
            StorageReference childRef = storageRef.child(stringid+".jpg");

            UploadTask uploadTask =childRef.putBytes(byteArray);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            update();
                            Intent intent=new Intent(MainActivity.this,Result.class);
                            intent.putExtra("id",stringid);
                            intent.putExtra("img",byteArray);
                            //intent.putExtra("img",imageUri.toString());
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(MainActivity.this, "Upload Failed -> "+exception, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void update() {
        reference.child(stringid).child("result").setValue("null");
        reference.child(stringid).child("id").setValue(stringid);
        reference.child(stringid).child("permit").setValue("0");
    }

    public String idgenerator() {
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int count=10;
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}