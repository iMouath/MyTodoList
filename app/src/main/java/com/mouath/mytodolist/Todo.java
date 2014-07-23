package com.mouath.mytodolist;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Todo extends Activity {
    // Creating Array list, adapter ... getting a handle to list view and attaching it
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);


    /* ContextMenu stuff - to be replaced with better code
    public void onCreateContextMenu(final ContextMenu menu, final View v,
                                    final ContextMenu.ContextMenuInfo menuInfo){
        if (v.getId()==R.id.lvItems){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Options");
        }
    }
    */

        //defining lvItems
        lvItems = (ListView) findViewById(R.id.lvItems);
        //defining items as array list
        items = new ArrayList<String>();
        //loading read and write functions OR methods
        readItems();
        // I need to google 'Adapter'
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //setting up a new listener for removing items
        //setupListViewListener();
        registerForContextMenu(lvItems);
    }
    //ContextMenu stuff
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        /*
        Menu Title & Options - YOU NEED TO SET THE VALUES IN Strings.xml !!
        Also Reorganize them properly
         */
        menu.setHeaderTitle(getString(R.string.select_option));
        menu.add(0, view.getId(), 0, R.string.remove_option);
        menu.add(0, view.getId(), 1, R.string.edit_option);
        menu.add(0, view.getId(), 2, R.string.complete_option);
        menu.add(0, view.getId(), 3, R.string.color_option);
        menu.add(0, view.getId(), 4, R.string.share_option);

    }

    // When Item is selected call
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        /*
        if statements for each options in context menu
        Maybe implement a better code here ? nested.
         */

        if(item.getOrder()==0){ //0 item = remove
            // assign temp variable to the position of item selected
            String selectedItem = items.get(position);
            // remove the item
            items.remove(selectedItem);
            // notify user using a toast
            Toast.makeText(this, R.string.removed_toast, Toast.LENGTH_SHORT).show();
            // update items view
            itemsAdapter.notifyDataSetChanged();
            // update items on file when one is removed
            saveItems();
            }
        else if(item.getOrder()==1){ // 1 item = edit
            Toast.makeText(this, "Edit Option Chosen", Toast.LENGTH_SHORT).show();

            // Edit Code goes HERE
        }
        else if(item.getOrder()==2){ //2 item = Complete
            /* TO BE IMPLEMENTED LATER
            // Completed Code goes here
            //Toast.makeText(this, "Completed Option Chosen", Toast.LENGTH_SHORT).show();
            //STOPSHIP
            //TextView selectedItem2 = (TextView)(lvItems.getItemAtPosition(position));
            String selectedItem = lvItems.getItemAtPosition(position).toString();
            SpannableString content = new SpannableString(selectedItem);
            //selectedItem2.setPaintFlags(selectedItem2.setPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            content.setSpan(new StrikethroughSpan(), 0, selectedItem.length(), 0);
            itemsAdapter.add(content.toString());

            itemsAdapter.notifyDataSetChanged();
            saveItems();
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
            //TextView selectedItem2 = (TextView) (lvItems.getItemAtPosition(info.position));
            //selectedItem.setPaintFlags(selectedItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //itemsAdapter.notifyDataSetChanged();
            //saveItems();
            */
        }
        else if(item.getOrder()==3){ //3 item = color
            // Color Code Goes here
            Toast.makeText(this, "Color Option Chosen", Toast.LENGTH_SHORT).show();
            //String s = (String) lvItems.getItemAtPosition(position);
            TextView selectedItem = (TextView) lvItems.getChildAt(position);
            selectedItem.setTextColor(Color.RED);
            itemsAdapter.notifyDataSetChanged();
            //String selectedItem = items.get(position);
            //selectedItem.setTextColor(Color.Red);


        }
        else if(item.getOrder()==4) { //4 item = share
            // Share Code Goes here
            Toast.makeText(this, "Share Option Chosen", Toast.LENGTH_SHORT).show();
        }


    return true;
    }
    /*
    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long rowID) {

                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                // update items on file when one is removed
                saveItems();
                return true;
            }
        });
    }
    */
    /*
    Below here we already defined 'addTodoItem' as onClick action in the xml
    here its actions are coded and it basically adds items from the text field into the list
    then it clears the text field for new input
     */
    public void addTodoItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String enteredText = etNewItem.getText().toString();
        if (enteredText.matches("")){
            Toast.makeText(this, "Please input a task first", Toast.LENGTH_SHORT).show();
            return;
        }
        itemsAdapter.add(etNewItem.getText().toString());
        etNewItem.setText("");
        // save items to txt file
        saveItems();
    }

    //Reading Items from txt file
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e){
            items = new ArrayList<String>();
            e.printStackTrace();
        }
    }
    //Saving Items into txt file
    private void saveItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
