package com.example.cameraguide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_REQUEST = 101;
    ImageView ivDisplay;
    String currentPhotoPath;
    Button btnAdd;
    String imageFileName;
    File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenCamera = findViewById(R.id.open_camera);
        btnOpenCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
                if(photoFile != null){
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.android.fileproviders",
                            photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, CAPTURE_REQUEST);
                }

            }
        });

        ivDisplay = findViewById(R.id.to_display);
        Button btnAdd = findViewById(R.id.add_to_gallery);
        btnAdd.setOnClickListener(view -> {
//            galleryAddPic();
            //Printed after just taking a picture.
            ///storage/emulated/0/Android/data/com.example.cameraguide/files/Pictures/JPEG_20200619_191932_3008856244093275927.jpg
            System.out.println(currentPhotoPath);
            System.out.println(image.getName());
            File appSpecificExternalDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "LrtE7ZXNxpE5c8Kj.jpg");
            System.out.println(appSpecificExternalDir.exists());
            Bitmap bitmap = BitmapFactory.decodeFile(appSpecificExternalDir.getAbsolutePath());
            ivDisplay.setImageBitmap(bitmap);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap)extras.get("data");
                ivDisplay.setImageBitmap(imageBitmap);

            }
        }
    }

    private File createImageFile() throws IOException{
        //Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = generateRandomChars();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName, /*prefix*/
//                ".jpg",  /*suffix*/
//                storageDir     /*directory*/
//        );
        image = new File(storageDir, imageFileName + ".jpg");

        //Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    // Checks if a volume containing external storage is available
    // for read and write.
    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Checks if a volume containing external storage is available to at least read.
    private boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    public static String generateRandomChars() {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        }
        return sb.toString();
    }




}
