package com.example.notebook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newRecord(View view) {
        setContentView(R.layout.record);
    }

    public void save(View view) {
        EditText theme, text;
        theme = findViewById(R.id.theme);
        if(theme.getText().equals("")) {
            text = findViewById(R.id.text);
            dbHelper = new DBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_THEME, theme.getText().toString());
            contentValues.put(DBHelper.KEY_TEXT, text.getText().toString());
            database.insert(DBHelper.TABLE, null, contentValues);
            setContentView(R.layout.activity_main);
            update();
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Тема не может быть пустой", Toast.LENGTH_SHORT).show();
        }
    }

    public void update(){
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_THEME);
            int emailIndex = cursor.getColumnIndex(DBHelper.KEY_TEXT);
            do {
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", name = " + cursor.getString(nameIndex) +
                        ", email = " + cursor.getString(emailIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }
}