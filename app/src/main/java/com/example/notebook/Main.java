package com.example.notebook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Main extends AppCompatActivity {
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        update();
    }

    public void newRecord(View view) {
        record("",0);
    }


    public void save(View view) {
        save(identificator);
    }

    int identificator = 0;

    public void save(int id){
        EditText theme, text;
        theme = findViewById(R.id.theme);
        text = findViewById(R.id.text);
        if(!(theme.getText().toString().trim().equals(""))) {
            dbHelper = new DBHelper(this);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_THEME, theme.getText().toString().trim());
            contentValues.put(DBHelper.KEY_TEXT, text.getText().toString().trim());
            if(id == 0){
                database.insert(DBHelper.TABLE, null, contentValues);
            }
            else{
                database.update(DBHelper.TABLE, contentValues, DBHelper.KEY_ID + "=" + id, null);
            }
            setContentView(R.layout.main);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            identificator = 0;
            setContentView(R.layout.main);
            update();
        }
        else{
            Toast.makeText(this, "Тема не может быть пустой", Toast.LENGTH_SHORT).show();
        }
    }

    int x = 0;
    int y = 0;

    public void update(){
        final ConstraintLayout Layout = findViewById(R.id.linear);
        Display display = getWindowManager().getDefaultDisplay();
        final int width_screen = display.getWidth() - 40;
        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getColumnIndex(DBHelper.KEY_ID);
            int theme = cursor.getColumnIndex(DBHelper.KEY_THEME);
            do {
                final Button b = new Button(getApplicationContext());
                b.setText(cursor.getString(theme));
                b.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                );
                b.setId(cursor.getInt(id));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        identificator = button.getId();
                        record(button.getText().toString(), button.getId());
                    }
                });
                b.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        b.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        if(x + b.getWidth()>= width_screen){
                            x = 0;
                            y += b.getHeight() + 5;
                            Layout.setMinHeight(y + b.getHeight());
                        }
                        b.setX(x);
                        b.setY(y);
                        x += b.getWidth() + 5;
                    }
                });
                Layout.addView(b);
            } while (cursor.moveToNext());
            x = 0;
            y = 0;
        }
        cursor.close();
    }

    public void record(String theme, final int id){
        setContentView(R.layout.record);
        EditText them = findViewById(R.id.theme);
        them.setText(theme);
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE, null, null, null, null, null, null);
        String result = null;
        if(cursor.moveToFirst()){
            do{
                if(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)) == id){
                    result = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TEXT));
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        EditText text = findViewById(R.id.text);
        text.setText(result);

        Button b = findViewById(R.id.delete);
        ConstraintLayout constraintLayout = findViewById(R.id.record);

        Log.i("Identificator", String.valueOf(identificator));
        if(identificator == 0){
            constraintLayout.removeView(b);
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.execSQL("Delete from " + DBHelper.TABLE + " where " + DBHelper.KEY_ID + "=" + id);
                identificator = 0;
                setContentView(R.layout.main);
                update();
            }
        });
    }

    public void onBackPressed(){
        setContentView(R.layout.main);
        update();
    }

}