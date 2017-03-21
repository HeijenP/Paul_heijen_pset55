package com.example.paul.paul_heijen_pset41;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.paul.paul_heijen_pset41.R.id.todolistItems;

public class Main2Activity extends AppCompatActivity {
    private TodoManager dbManager;
    EditText addName;
    ListView listviewItems;
    ContextMenu itemMenu;
    Main2Activity.TodoCursorAdapter todoAdapter;
    Cursor cursor;
    long listid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.deleteDatabase("TODOLISTDATABASE.db");
        addName = (EditText) findViewById(R.id.addName);
        itemMenu = (ContextMenu) findViewById(R.id.itemMenu);
        saveSharedPref();




        Bundle extras = getIntent().getExtras();
        listid = extras.getLong("listid");
        String nameoflist = extras.getString("listname");
        TextView namelist = (TextView) findViewById(R.id.listTitle);
        namelist.setText("Expanded list: " + nameoflist);

        // initialize db
        dbManager = new TodoManager(this);
        dbManager.open();

        makeItemsAdapter();
        registerForContextMenu(listviewItems);
        loadSharedPref();

    }

    public void saveSharedPref(){
        SharedPreferences pref = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("listidee",listid);
        editor.commit();

    }

    public void loadSharedPref(){
        SharedPreferences pref = this.getSharedPreferences("settings", this.MODE_PRIVATE);

        long listideerestored = pref.getLong("listidee",0);
        if(listideerestored != 0){
            listid = listideerestored;
        }
    }


    public void makeItemsAdapter() {
        cursor = dbManager.fetchItemsFromList(listid);
        listviewItems = (ListView) findViewById(todolistItems);
        todoAdapter = new Main2Activity.TodoCursorAdapter(this,cursor);
        assert listviewItems != null;
        listviewItems.setAdapter(todoAdapter);
        todoAdapter.notifyDataSetChanged();
    }

    public void getBack(View view){
        Intent back = new Intent(this, MainActivity.class);
        this.startActivity(back);
    }


    public void fetchCursor() {
        cursor = dbManager.fetchItemsFromList(listid);
        todoAdapter.changeCursor(cursor);
    }

    public void addItem(View view) {
        String addedItem = addName.getText().toString();

        Toast noInputError = Toast.makeText(getApplicationContext(), "You have to enter a name for your todolist item, try again!",
                Toast.LENGTH_SHORT);

        if (addName.length() > 0) {
            addName.getText().clear();
            dbManager.insert(addedItem, listid);
            fetchCursor();
            todoAdapter.notifyDataSetChanged();
        } else{
            noInputError.setGravity(Gravity.CENTER_HORIZONTAL, 0, 200);
            noInputError.show();
        }
    }

    public void deleteItem() {
        long itemid = cursor.getInt(cursor.getColumnIndex("_id"));
        dbManager.delete(itemid);
        fetchCursor();
        todoAdapter.notifyDataSetChanged();
    }


    private void checkDone(String checkItem){
        String mark = "(FINITO!)";
        String toCheck =  checkItem.substring(0, Math.min(checkItem.length(), mark.length()));
        String updateEntry = "";
        if(toCheck.equals(mark)){
            updateEntry = checkItem;

        }
        else {
            updateEntry = mark + checkItem;
        }

        long id = cursor.getInt(cursor.getColumnIndex("_id"));
        dbManager.update(id,updateEntry);
        fetchCursor();
        todoAdapter.notifyDataSetChanged();
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu2, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        String involved2 = cursor.getString(cursor.getColumnIndex("todoitemname"));

        if (menuItem == R.id.check){
            checkDone(involved2);
            return true;
        }else if(menuItem == R.id.delete){
            deleteItem();
            return true;
        }
        else{
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putLong("ListID", listid);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        addName = (EditText) findViewById(R.id.addName);
//        long listID = outState.getLong("ListID");

        registerForContextMenu(listviewItems);
        itemMenu = (ContextMenu) findViewById(R.id.itemMenu);
        makeItemsAdapter();
        fetchCursor();
        todoAdapter.notifyDataSetChanged();
    }


//    public void onDestroy() {
//        super.onDestroy();
//        //if(dbManager != null) {
//        //   dbManager.close();
//        //}
//    }

    public class TodoCursorAdapter extends CursorAdapter {
        public TodoCursorAdapter(Context context, Cursor cursor) {
            super(context,cursor,0);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            dbManager.open();
            TextView textViewTitle = (TextView) view.findViewById(R.id.listitem);
            String itemname = cursor.getString( cursor.getColumnIndex("todoitemname"));
            textViewTitle.setText(itemname);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.commit();
    }

}
