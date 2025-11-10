package com.ronak.bytebreaker;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity {

    public static final int RES_IMG = 1;

    ImageView imgOrignal, imgCompress;
    TextView txtOrignal, txtCompress, txtQuality;
    EditText txtHeight, txtWidth;
    SeekBar seekBar;
    Button btnPick, btnCompress;
    File orignalImage, compressImage;
    File path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ByteBreaker");

        if (!path.exists()) {
            path.mkdirs();
        }

        takePermission();
        initialize();

        btnPick.setOnClickListener(v -> openGallery());

        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtQuality.setText("Quality: " + progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnCompress.setOnClickListener(v -> compressImage());
    }

    private void compressImage() {
        if (orignalImage == null) {
            Toast.makeText(this, "Please select an image first!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (txtWidth.getText().toString().isEmpty() || txtHeight.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter width and height", Toast.LENGTH_SHORT).show();
            return;
        }

        int quality = seekBar.getProgress();
        int width = Integer.parseInt(txtWidth.getText().toString());
        int height = Integer.parseInt(txtHeight.getText().toString());

        try {
            Toast.makeText(this, "Compressing...", Toast.LENGTH_SHORT).show();

            compressImage = new Compressor(this)
                    .setMaxWidth(width)
                    .setMaxHeight(height)
                    .setQuality(quality)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(path.getAbsolutePath())
                    .compressToFile(orignalImage);

            Bitmap finalBitmap = BitmapFactory.decodeFile(compressImage.getAbsolutePath());
            imgCompress.setImageBitmap(finalBitmap);

            txtCompress.setText("Size: " + Formatter.formatShortFileSize(this, compressImage.length()));


            MediaScannerConnection.scanFile(this,new String[]{compressImage.getAbsolutePath()}, null, null);

            Toast.makeText(this, "Saved to: " + compressImage.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Toast.makeText(this, "Compression failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RES_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            btnCompress.setVisibility(View.VISIBLE);
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                imgOrignal.setImageBitmap(selectedImage);

                orignalImage = getFileFromUri(imageUri);
                txtOrignal.setText("Size: " + Formatter.formatShortFileSize(this, orignalImage.length()));

            } catch (FileNotFoundException e) {
                Toast.makeText(this, "File not found: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Error loading image: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    private String genrateName(){
        String s="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String name="Byte_Breaker_";
        for(int i=0;i<8;i++){
            int x= (int) Math.random();
            x=Math.abs(x)%s.length();
            name+=s.charAt(x);
        }
        return name;
    }
    @NonNull
    private File getFileFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = new File(getCacheDir(), genrateName());
        OutputStream outputStream = new java.io.FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return tempFile;
    }

    private void initialize() {
        imgOrignal = findViewById(R.id.img_orignal);
        imgCompress = findViewById(R.id.img_compress);
        txtOrignal = findViewById(R.id.txt_orignal);
        txtCompress = findViewById(R.id.txt_compress);
        txtQuality = findViewById(R.id.txt_quality);
        txtHeight = findViewById(R.id.txt_hight);
        txtWidth = findViewById(R.id.txt_width);
        seekBar = findViewById(R.id.seek_quality);
        btnPick = findViewById(R.id.btnPick);
        btnCompress = findViewById(R.id.btn_compress);
        btnCompress.setVisibility(View.GONE);
    }

    private void takePermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


}
