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
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.paul.paul_heijen_pset41.R.id.todolistItems;



public class MainActivity extends AppCompatActivity {
    private DBManager dbManager;
    EditText addName;
    ListView listviewLists;
    ContextMenu listMenu;
    TodoCursorAdapter todoAdapter;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.deleteDatabase("TODOLISTDATABASE.db");
        addName = (EditText) findViewById(R.id.addName);
        listMenu = (ContextMenu) findViewById(R.id.listMenu);

        dbManager = new DBManager(this);
        dbManager.open();
        makeItemsAdapter();
        registerForContextMenu(listviewLists);
    }



    public void addItem(View view) {
        String addedItem = addName.getText().toString();

        Toast noInputError = Toast.makeText(getApplicationContext(), "You have to enter a title for your todolist, try again!",
                Toast.LENGTH_SHORT);

        if (addName.length() > 0) {
            addName.getText().clear();
            dbManager.insert(addedItem);
            fetchCursor();
            todoAdapter.notifyDataSetChanged();
        }
        else{
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


    public void checkDone(String checkItem){
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


    public void expandList() {
        long id = cursor.getInt(cursor.getColumnIndex("_id"));
        String name = cursor.getString(cursor.getColumnIndex("todolistname"));
        Intent newIntent = new Intent(this, Main2Activity.class);
        newIntent.putExtra("listid", id);
        newIntent.putExtra("listname",name);
        this.startActivity(newIntent);
    }

    public void makeItemsAdapter() {
        cursor = dbManager.fetch();
        listviewLists = (ListView) findViewById(todolistItems);
        todoAdapter = new TodoCursorAdapter(this,cursor);
        assert listviewLists != null;
        listviewLists.setAdapter(todoAdapter);
        todoAdapter.notifyDataSetChanged();
    }

    public void fetchCursor() {
        cursor = dbManager.fetch();
        todoAdapter.changeCursor(cursor);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        String involved = cursor.getString(cursor.getColumnIndex("todolistname"));

        if (menuItem == R.id.check){
            checkDone(involved);
            return true;
        }else if(menuItem == R.id.delete){
            deleteItem();
            return true;
        }else if(menuItem == R.id.expandList){
            expandList();
            return true;
        }

        else{
            return super.onContextItemSelected(item);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        addName = (EditText) findViewById(R.id.addName);

        dbManager.open();
        registerForContextMenu(listviewLists);
        listMenu = (ContextMenu) findViewById(R.id.listMenu);
        makeItemsAdapter();
        fetchCursor();
        todoAdapter.notifyDataSetChanged();
    }


//    public void onDestroy() {
//        super.onDestroy();
//    }

    public class TodoCursorAdapter extends CursorAdapter {
        public TodoCursorAdapter(Context context, Cursor cursor) {
            super(context,cursor,0);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            dbManager.open();
            TextView newlistTitle = (TextView) view.findViewById(R.id.listitem);
            String itemname = cursor.getString( cursor.getColumnIndex("todolistname"));
            newlistTitle.setText(itemname);
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



