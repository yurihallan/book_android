package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjetoActivity extends AppCompatActivity {

    EditText edtProjeto, edtCliente, edtEndereco;
    int id;
    Button btnSalvar;

    MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto);

//        myDB = new MyDatabaseHelper(ProjetoActivity.this);

        edtProjeto = findViewById(R.id.edtProjeto);
        edtCliente = findViewById(R.id.edtCliente);
        edtEndereco = findViewById(R.id.edtEndereco);
        btnSalvar = findViewById(R.id.btnSalvar);

        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        final String dataFormatada = formatoData.format(data);



        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(ProjetoActivity.this);
              id = (int) myDB.addProjeto(edtProjeto.getText().toString().trim(),
                            edtCliente.getText().toString().trim(),
                            edtEndereco.getText().toString().trim(),
                            dataFormatada  );


            }
        });

        findViewById(R.id.btnProximo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProjetoActivity.this, MainActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }
}
