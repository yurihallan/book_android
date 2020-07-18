package com.example.book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    int id;

    MyDatabaseHelper myDB;
    ArrayList<String> book_id, book_title, book_author, book_pages;
    ArrayList<String> txt_projeto, txt_cliente, txt_endereco, id_projeto;
    CustomAdapter customAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);





        myDB = new MyDatabaseHelper(MainActivity.this);
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_pages = new ArrayList<>();
        txt_projeto = new ArrayList<>();
        txt_cliente = new ArrayList<>();



        storeDataInArray();
        storeDataInArrayProjeto();


        customAdapter = new CustomAdapter(MainActivity.this,this, book_id, book_title,book_author,book_pages, txt_projeto, txt_cliente);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this ));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArray(){


        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));

            }
        }
    }

    // Buscando dados do banco Projeto
    void storeDataInArrayProjeto(){


        Cursor cursor = myDB.readAllDataProjeto();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){

                txt_projeto.add(cursor.getString(1));
                txt_cliente.add(cursor.getString(2));


            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inserir, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_button){
            id = getIntent().getIntExtra("id",-1);

            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            intent.putExtra("id", id);
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

}
