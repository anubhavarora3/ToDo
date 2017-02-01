package com.example.anubharora.todo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.anubharora.todo.db.TaskDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TaskDBHelper helper;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.todo);
        helper = new TaskDBHelper(this);

        updateUI();

    }

    private void updateUI() {
        ArrayList<String> tasks = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(helper.TABLE,new String[]{helper.DB_ID,helper.COL_TASK_TITLE},
                null,null,null,null,null);

        while(cursor.moveToNext()){
            int idx = cursor.getColumnIndex(helper.COL_TASK_TITLE);
            //Log.d(TAG,"Task : "+cursor.getString(idx));
            tasks.add(cursor.getString(idx));
        }

        if(adapter == null){
            adapter = new ArrayAdapter<>(this,R.layout.list_row,
                    R.id.task,tasks);
            listView.setAdapter(adapter);
        }else{
            adapter.clear();
            adapter.addAll(tasks);
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_task:
                final EditText editText = new EditText(this);
                AlertDialog alertDialog = new AlertDialog.Builder(this).
                        setTitle("Add a New Task").setMessage("What do you wanna do next?").
                        setView(editText).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = editText.getText().toString();
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(helper.COL_TASK_TITLE,task);
                        db.insertWithOnConflict(helper.TABLE,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        updateUI();
                    }
                }).setNegativeButton("Cancel",null).create();
                alertDialog.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view){
        View parent = (View) view.getParent();
        TextView text = (TextView) parent.findViewById(R.id.task);
        String task = text.getText().toString();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(helper.TABLE, helper.COL_TASK_TITLE + "= ?", new String[]{task});
        db.close();
        updateUI();
    }
}
