package com.example.book;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class DownloadActivity extends AppCompatActivity {

    String imag_url = "https://uc3f3600688485c18e48e020699b.previews.dropboxusercontent.com/p/thumb/AA0oTS3GXjQAPWClZ2PTtiZJGiTfWPIk-S-fgMkrq_Vo6WbHs9nkUwBIBTkjZdv8g7-QOiMndc3IPXNoemywhnnrR_Udq8IWopNTTmIEHc6IDhLdr-LMYUSeHoM_bSpn6LhhJAZhx9NR81z8uKiUXYO-O1xIVaKNf8-IUIeWI2dXDWLpcsBV2RovFNxgzZWDQsSGiBeF4jJ2NUr7F3RYjps00bvEHsx2QrArzccWKo7A3NPKbNcyyfPIgcd0slmefaU4u38CEELr5JExWnB_iNeUTtpcrs7CCk-l8LqYikqR_LJyotJmJKMqTNFTQNKBNWfKyCxYbsnyQ8aKUvDpwLe1WCPbV6Jp91rgmaxeEJChiDdKOaTX072RThgJk4imsIfFks21_tQxLsovhRclsGgp40Ih2_oUko_qlHuErl5jc5zjDVCG9g6xjFzaedvyD41hbB6_9qW88PV2xTMNM7_H/p.png?fv_content=true&size_mode=5";

    Button btnDownload;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        btnDownload = (Button) findViewById(R.id.btnDownload);
        imageView = (ImageView) findViewById(R.id.image_view);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(imag_url);
            }
        });
    }



    class DownloadTask extends AsyncTask<String,Integer,String>
    {

        ProgressDialog progressDialog;




        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(DownloadActivity.this);
            progressDialog.setTitle("Download in progress...");
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }).start();
        }

        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();
                File new_folder = new File("sdcard/Download");
                if(new_folder.exists()){
                    new_folder.mkdir();
                }

                File input_file = new File(new_folder, "download_image.png");
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(input_file);
                while ((count = inputStream.read(data)) != -1)
                {
                    total+= count;
                    outputStream.write(data, 0, count);
                    int progress = (int) total * 100/file_length;
                    publishProgress(progress);
                }

                inputStream.close();
                outputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Donwload Complete!";
        }



        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            String path = "sdcard/Download/download_image.png";
            imageView.setImageDrawable(Drawable.createFromPath(path));

        }
    }

}
