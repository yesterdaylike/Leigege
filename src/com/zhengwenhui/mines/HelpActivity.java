package com.zhengwenhui.mines;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
	}
	
	public void onReturnButtonClick(View view){
		this.finish();
	}
}
