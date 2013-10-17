package com.zhengwenhui.mines;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class HistoryActivity extends Activity{
	
	private ListView mListview;
	private DataBase database;
	private Cursor mCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_layout);
		
		database = new DataBase(this);
		mCursor = database.query();
		
		mListview = (ListView)findViewById(R.id.HistoryListView);
		MySimpleCursorAdapter adapter = new MySimpleCursorAdapter(this,  
				R.layout.item_layout, 
				mCursor,  
				new String[] { "type", "spend_time", "time" },
				new int[] { R.id.ranking,  R.id.spend_time, R.id.time });
		mListview.setAdapter(adapter);
	}
	
	public void onReturnButtonClick(View view){
		this.finish();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		mCursor.close();
		super.finish();
	}
}
