package com.example.swipdelete;

import com.example.adapter.MyAdapter;
import com.example.adapter.MyAdapter.ViewHolder;
import com.example.ui.SwipLayout;
import com.example.ui.SwipLayout.OnSwipLayoutListener;



import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mList = (ListView) findViewById(R.id.lv);
        MyAdapter adapter = new MyAdapter(this);
        mList.setAdapter(adapter);
 
       
    }

}
