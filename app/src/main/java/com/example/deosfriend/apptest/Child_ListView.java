package com.example.deosfriend.apptest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Controller.Child;

/**
 * Created by Deo's Friend on 3/14/2015.
 */
public class Child_ListView extends Activity{

    private List<Child> childList = new ArrayList<Child>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_list);

        populateChildList();
        populateListView();
        registerClickCallBack();
    }

    private void registerClickCallBack() {
        ListView list = (ListView) findViewById(R.id.ChildListView);

        list.setOnItemClickListener(new
                                            AdapterView.OnItemClickListener(){

                                                public void onItemClick(AdapterView<?> parent, View viewClicked,
                                                                        int position, long id){
                                                    Child clickedChild = childList.get(position);

                                                    String message = "Click position: " + position + "which is :" + clickedChild.getChildName();

                                                    Toast.makeText(Child_ListView.this, message, Toast.LENGTH_LONG).show();
                                                }
                                            });
    }

    private void populateChildList(){

    }

    private void populateListView() {
        ArrayAdapter<Child> adapter = new MyListAdapter();
        ListView list = (ListView)findViewById(R.id.ChildListView);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter() {
            super(Child_ListView.this, R.layout.listview_row, childList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if ( itemView == null )
                itemView = getLayoutInflater().inflate(R.layout.listview_row, parent, false);

            Child currentChild = childList.get(position);

            ImageView imageView = (ImageView)itemView.findViewById(R.id.lv_Image);
            imageView.setImageResource(currentChild.getIcon());

            TextView nameText =  (TextView)itemView.findViewById(R.id.tvName_Db);
            nameText.setText(currentChild.getChildName());

            TextView ageText =  (TextView)itemView.findViewById(R.id.tvAge);
            ageText.setText(currentChild.getAge());
            return itemView;
        }

    }

}
