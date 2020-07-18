package com.example.book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int IMAGE_PICK_CODE = 1000;

    Button  add_galeria;
    ImageView mImageView;
    Uri image_uri;

    EditText title_input, author_input, pages_input;
    int id;
    TextView id_projeto;

    Button btnSalvar;

    MyDatabaseHelper myDB;
    ArrayList<String> projeto_id;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        author_input = findViewById(R.id.author_input);
        pages_input = findViewById(R.id.pages_input);
        btnSalvar = findViewById(R.id.btnSalvar);
        id_projeto = findViewById(R.id.txt_id_projeto);



        mImageView = (ImageView) findViewById(R.id.image_view);

//        getAndSetIntentDataProjeto();



        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addBook(title_input.getText().toString().trim(),
                            author_input.getText().toString().trim(),
                            Integer.parseInt(pages_input.getText().toString().trim()

                                    )
                        );
                finish();

            }
        });






    }

//    void getAndSetIntentDataProjeto() {
//        if (getIntent().hasExtra("id") ) {
//            //Getting Data from Intent
//            id = getIntent().getIntExtra("id",-1);
////            id_projeto.setText(id);
//
//            //Setting Intent Data on Display
//            Toast.makeText(this, "No Data" +id, Toast.LENGTH_LONG).show();
//
//
//        } else {
//            Toast.makeText(this, "No Data.", Toast.LENGTH_LONG).show();
//        }
//    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_galeria, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_galeria){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED){

                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                    //Mostrar o popup para solicitar a permissão
                    final int PERMISSION_CODE = 1001;
                    requestPermissions(permissions, PERMISSION_CODE);
                }else{
                    pickImageFromGallery();
                }
            }else{
                pickImageFromGallery();
            }

        }

        return super.onOptionsItemSelected(item);
    }


    //Função de buscar uma imagem na galeria
    private void pickImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    //Controlando com o resultado da permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //openCamera();

                }else {
                    if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        pickImageFromGallery();
                    }else{
                        //  Permissão negada
                        Toast.makeText(this, "Permissão negada...", Toast.LENGTH_SHORT).show();
                    }


                }
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //É chamado  quando a imagem foi capturado pela camera

        if(requestCode == IMAGE_PICK_CODE){
            //Condição ser o resultado foi ok e o code da image é valido
            assert data != null;
            mImageView.setImageURI(data.getData());
        }else {

            if(resultCode == RESULT_OK ){
                //é 'Setado/set' a imagem na nossa variavel mImageView
                mImageView.setImageURI(image_uri);
            }
        }
    }


    public void onClickCamera(View view) {

        //Se o sistema OS é >= marshmallow
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //Permissão não ativada, Solicite a permissão
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                //Mostrar o popup para solicitar a permissão
                requestPermissions(permission, PERMISSION_CODE);


            } else {
                //permissão já liberada

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
                image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                // camera intent
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

            }
        }



    }
}
