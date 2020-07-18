package com.example.book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {



    EditText title_input, author_input, pages_input;
    Button update_button, delete_button;

    String id, title, author, pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_input = findViewById(R.id.title_input2);
        author_input = findViewById(R.id.author_input2);
        pages_input = findViewById(R.id.pages_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete);


        //First we call this
        getAndSetIntentData();






        //set actionBar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Editar projeto");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("author") && getIntent().hasExtra("pages")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            author = getIntent().getStringExtra("author");
            pages = getIntent().getStringExtra("pages");


            //Setting Intent Data on Display
            title_input.setText(title);
            author_input.setText(author);
            pages_input.setText(pages);

        } else {
            Toast.makeText(this, "No Data.", Toast.LENGTH_LONG).show();
        }
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete "+title);
            builder.setMessage("Are you sure you want to delete "+ title + " ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                    myDB.deleteOneRow(id);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();

        }

        if (item.getItemId() == R.id.update_button){
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
            title = title_input.getText().toString().trim();
            author = author_input.getText().toString().trim();
            pages = pages_input.getText().toString().trim();
            myDB.updateData(id, title, author, pages);
            finish();
        }

        if(item.getItemId() == R.id.ItDownload){
            Intent intent = new Intent(UpdateActivity.this, DownloadActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



}
