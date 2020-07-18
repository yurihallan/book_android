package com.example.book;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.sql.Date;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BookLibrary.db";
    private static final int DATABASE_VERSION = 2;
    //Variaveis da tabela do Livro(my_library)
    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "book_title";
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PAGES = "book_pages";
    private static final String COLUMN_ID_PROJETO = "ID_PROJETO";

    //Variaveis da tabela do Projeto
    private static final String TABLE_NAME_PORJETO = "projeto";
    private static final String ID = "_id";
    private static final String COLUMN_NAME_PROJETO = "projeto_nome";
    private static final String COLUMN_NAME_CLIENTE = "projeto_cliente";
    private static final String COLUMN_ENDERECO = "projeto_endereco";
    private static final String DATA = "DATE";


    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryBook = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "
                                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                        COLUMN_TITLE + " TEXT , " +
                                        COLUMN_AUTHOR + " TEXT, "+
                                        COLUMN_PAGES + " INTEGER, "+
                                        COLUMN_ID_PROJETO + " INTEGER NOT NULL," +
                                        "FOREIGN KEY (ID_PROJETO)  REFERENCES projeto (_id) )";

        db.execSQL(queryBook);


        String queryProjeto = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_PORJETO + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_NAME_PROJETO + " TEXT, " +
                COLUMN_NAME_CLIENTE + " TEXT, "
                + COLUMN_ENDERECO + " TEXT, "+
                DATA + " TEXT) ";

        db.execSQL(queryProjeto);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        if (!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME_PORJETO);
        onCreate(db);
    }



    void addBook(String title, String author, int pages){



        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();



        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_ID_PROJETO, 1);// inserindo valor fixo

        long result = db.insert(TABLE_NAME,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();
        }

    }

    long addProjeto(String projeto, String cliente, String endereco, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(COLUMN_NAME_PROJETO, projeto);
        cv.put(COLUMN_NAME_CLIENTE, cliente);
        cv.put(COLUMN_ENDERECO, endereco);
        cv.put(DATA, data);

        long result = db.insert(TABLE_NAME_PORJETO,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
            return -1;
        }else{
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_LONG).show();

            return result;
        }

    }



    Cursor readAllData(){
        String query = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
          cursor = db.rawQuery(query,null);
        }
        return cursor;
    }


    Cursor readAllDataProjeto(){
        String query = "SELECT * FROM "+ TABLE_NAME_PORJETO;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    void updateData(String row_id, String title, String author, String pages){
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues cv = new ContentValues();
         cv.put(COLUMN_TITLE,title);
         cv.put(COLUMN_AUTHOR,author);
         cv.put(COLUMN_PAGES,pages);


         long result = db.update(TABLE_NAME, cv,"_id=?",new String[]{row_id});
         if(result == -1){
             Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();
         }else{
             Toast.makeText(context, "Successfully updated!", Toast.LENGTH_LONG).show();
         }
    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?",new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "Successfully Deleted!", Toast.LENGTH_LONG).show();
        }
    }
}
