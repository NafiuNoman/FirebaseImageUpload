package com.example.uploadimagetofire;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    ImageView imageView;
    Button upload,browse;

    Uri filePath;
    Bitmap bitmap;

    Integer i =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = findViewById(R.id.IdimageView);
        upload = findViewById(R.id.IdBtnUpload);
        browse = findViewById(R.id.IdBtnBrowse);


        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(MainActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");

                                startActivityForResult(Intent.createChooser(intent,"select the image"),1);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                            }
                        }).check();

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageToFireBase();
            }
        });



    }

    private void UploadImageToFireBase() {



        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference uploader = storage.getReference().child("Image"+i++);

        uploader.putFile(filePath)
               .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                       Toast.makeText(MainActivity.this, "Upload is successful", Toast.LENGTH_SHORT).show();

                   }
               });






    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==1 && resultCode== RESULT_OK)
        {
            filePath = data.getData();

            try {

                InputStream inputStream = getContentResolver().openInputStream(filePath);

                bitmap = BitmapFactory.decodeStream(inputStream);

                imageView.setImageBitmap(bitmap);

            }catch (Exception ex)

            {


            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}