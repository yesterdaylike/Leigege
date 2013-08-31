package com.huige.mines;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MySimpleCursorAdapter extends SimpleCursorAdapter {

	String second;

	public MySimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		// TODO Auto-generated constructor stub
		second = context.getResources().getString(R.string.second);

		while (c.moveToNext()) {
			Log.e("MySimpleCursorAdapter", ""+c.getString(0)+" "+c.getString(1)+" "+c.getString(2)+" "+c.getString(3));
		}
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		super.bindView(view, context, cursor);

		TextView rankingTextView = (TextView)view.findViewById(R.id.ranking);
		TextView spendTimeTextView = (TextView)view.findViewById(R.id.spend_time);
		//TextView timeTextView = (TextView)view.findViewById(R.id.time);
		spendTimeTextView.append(second);

		CharSequence ranking = rankingTextView.getText();
		int id = -1;
		if(ranking.equals("0")){
			id = R.string.primary;
		}
		else if(ranking.equals("1")){
			id = R.string.middle;
		}
		else if(ranking.equals("2")){
			id = R.string.high;
		}

		if( id >= 0 ){
			rankingTextView.setText(id);
		}
	}
}
