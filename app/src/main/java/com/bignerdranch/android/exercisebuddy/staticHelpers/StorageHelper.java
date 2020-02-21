package com.bignerdranch.android.exercisebuddy.staticHelpers;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bignerdranch.android.exercisebuddy.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class StorageHelper {
    public static boolean loadProfileImageIntoStorage(String imageUrl, String userId, ContentResolver resolver){
        if (imageUrl.isEmpty()) {
            return false;
        }
        else {
            Uri imageUri = Uri.parse(imageUrl);
            Bitmap bitmap = null;
            final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source = ImageDecoder.createSource(resolver, imageUri);
                try {
                    bitmap = ImageDecoder.decodeBitmap(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(resolver, imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
            byte[] data = outputStream.toByteArray();
            UploadTask uploadTask = filePath.putBytes(data);
            return uploadTask.isSuccessful();
        }
    }

    public static void loadProfileImageFromStorageIntoImageView(final Context context, String userId, final ImageView imageView){
        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);

        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // There is no profile image in storage bc the user never set a profile image. We must set the image view or else the
                // last loaded image can be displayed: http://bumptech.github.io/glide/doc/getting-started.html#listview-and-recyclerview
                Glide.with(context).load(R.mipmap.ic_launcher).into(imageView);
            }
        });
    }
}
