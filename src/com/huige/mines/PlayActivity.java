package com.huige.mines;

import java.util.Calendar;
import java.util.Random;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.diy.DiyManager;
import net.youmi.android.offers.OffersManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PlayActivity extends Activity implements OnClickListener,OnLongClickListener{
	private RelativeLayout mPlayLayout;
	private TableLayout mTableLayout;
	private ImageButton[][] mViews;
	private byte [][] mField;		     	  //格子周围的地雷的数目（-1 表示本身为地雷）
	private Status[][] mStatus;		    	  //每个格子的状态
	private int mCountX=0; 					  //X 方向上的格子数目
	private int mCountY=0; 					  //Y 方向上的格子数目
	private int w;
	private int h;

	private TextView mTimerTextView;
	private TextView mMinesTextView;
	private int mSeconds = 0; 				  //游戏计时
	private int mMines = 0;					  //地雷的个数

	private boolean mCountTime = false; 	  //是否开始计时
	private boolean mGameing = false;

	private ToggleButton mSoundToggleButton;  //音效开关
	private RadioGroup mLevelRadioGroup;	  //设置难度级别 
	private RadioGroup mSkinGroup;	          //设置皮肤
	private Panel mTopPanel;

	private Set set;
	private DataBase database;

	//private List<AdObject> adList;
	private String[] mPhotos;
	private Random mRandom;

	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {
			if(mCountTime){
				mSeconds++;
				mTimerTextView.setText(String.valueOf(mSeconds));
			} 
			handler.postDelayed(this, 1000);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_layout);

		getContacts();
		mRandom = new Random();
		// 初始化应用的发布ID和密钥，以及设置测试模式
		AdManager.getInstance(this).init("d72e9f61b58f5ee5","fb9dfc2456866caf", false);
		// 预加载自定义数据列表
		//DiyManager.initAdObjects(this);
		set = new Set(this);
		mSoundToggleButton = (ToggleButton)findViewById(R.id.SoundToggleButton);
		mSoundToggleButton.setChecked(set.mOpenSound);
		mSoundToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				//控制音效的开关
				set.setOpenSound(isChecked);
			}
		});
		mPlayLayout = (RelativeLayout)findViewById(R.id.play_layout);
		initLevel();
		initSkin();

		mTopPanel = (Panel)findViewById(R.id.topPanel);

		mTableLayout = (TableLayout)findViewById(R.id.mainfiled);
		mTimerTextView = (TextView)findViewById(R.id.timer);
		mMinesTextView = (TextView)findViewById(R.id.mines); 
		openButtonClick(null);
		handler.postDelayed(runnable, 1000);

		database = new DataBase(this);
		
		/*//实例化广告条
	    AdView adView = new AdView(this, AdSize.FIT_SCREEN);
	    //获取要嵌入广告条的布局
	    LinearLayout adLayout=(LinearLayout)findViewById(R.id.ad_layout);
	    //将广告条加入到布局中
	    adLayout.addView(adView);*/
		
	}

	private void initLevel(){
		mLevelRadioGroup = (RadioGroup)findViewById(R.id.LevelRadioGroup);

		int id = R.id.primaryRadioButton;
		switch (set.mDifficulty) {
		case 0:
			id = R.id.primaryRadioButton;
			break;
		case 1:
			id = R.id.middleRadioButton;
			break;
		case 2:
			id = R.id.highRadioButton;
			break;
		default:
			break;
		}
		mLevelRadioGroup.check(id);
		mLevelRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int difficulty = 0;
				switch (checkedId) {
				case R.id.primaryRadioButton:
					difficulty = 0;
					break;
				case R.id.middleRadioButton:
					difficulty = 1;
					break;
				case R.id.highRadioButton:
					difficulty = 2;
					break;
				default:
					break;
				}
				set.setDifficulty(difficulty);
				openButtonClick(null);
			}
		});
	}

	private void initSkin(){
		mSkinGroup = (RadioGroup)findViewById(R.id.SkinGroup);

		int id = R.id.classicSkinButton;
		switch (set.mSkin) {
		case 0:
			id = R.id.classicSkinButton;
			mPlayLayout.setBackgroundResource(R.drawable.wall);
			break;
		case 1:
			id = R.id.timeSkinButton;
			mPlayLayout.setBackgroundResource(R.drawable.wall_paper);
			break;
		default:
			break;
		}
		mSkinGroup.check(id);
		mSkinGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int skin = 0;
				switch (checkedId) {
				case R.id.classicSkinButton:
					skin = 0;
					mPlayLayout.setBackgroundResource(R.drawable.wall);
					break;
				case R.id.timeSkinButton:
					skin = 1;
					mPlayLayout.setBackgroundResource(R.drawable.wall_paper);
					break;
				default:
					break;
				}
				set.setSkin(skin);
				openButtonClick(null);
			}
		});

	}

	/**
	 * 根据游戏的难度，初始化矩阵的大小（长和高分别有多少个格子）
	 */
	private void initCount(int width,int height){

		switch (set.mDifficulty) {
		case 0:
			mCountX = 5;
			mCountY = 7;
			mMines = 4;
			break;
		case 1:
			mCountX = 6;
			mCountY = 9;
			mMines = 6;
			break;
		case 2:
			mCountX = 7;
			mCountY = 11;
			mMines = 9;
			break;

		default:
			mCountX = 5;
			mCountY = 7;
			mMines = 4;
			set.mDifficulty = 0;
			break;
		}

		w = ( width - 10 ) / mCountX;
		h = ( height - 170 ) / mCountY;

		if( w > h ){
			w = h;
		}
		else {
			h = w;
		}
	}

	/**
	 * 点击开始按钮，开始游戏
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public void openButtonClick(View view){
		set.playSoundPool(1);
		mGameing = true;
		mCountTime = true;
		mSeconds=0;
		mTimerTextView.setText(String.valueOf(mSeconds));
		Display display = getWindowManager().getDefaultDisplay();
		mTableLayout.removeAllViews();
		initCount(display.getWidth(),display.getHeight());
		addViews(mCountX,mCountY,display.getWidth(),display.getHeight());
		InitMineField(mCountX,mCountY);
	}

	private void addViews(int x,int y,int width,int height){
		mViews = new ImageButton[x][y];
		TableRow tableRow;
		ImageButton button;

		mTableLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		for(int row=0,line; row<y; row++){
			tableRow = new TableRow(this);
			tableRow.setGravity(Gravity.CENTER);
			for(line=0; line<x; line++){
				button = new ImageButton(this); 

				if(set.mSkin==1){
					button.setBackgroundResource(R.drawable.mine_selector2);
				}
				else{
					button.setBackgroundResource(R.drawable.mine_selector);
				}

				button.setImageResource(R.drawable.space);
				button.setOnClickListener(this);
				button.setOnLongClickListener(this);
				mViews[line][row] = button;
				tableRow.addView(button,new TableRow.LayoutParams(w, h));
			}
			mTableLayout.addView(tableRow);
		}
		mTableLayout.requestLayout();
	}

	public void onClick(View v) { //onLongClick
		// TODO Auto-generated method stub
		if(!mGameing){
			return;
		}
		set.playSoundPool(0);
		mCountTime = true;

		int line = 0,row = 0;
		for(line =0; line<mCountX; line++){
			for(row =0; row<mCountY; row++){
				if(mViews[line][row]==v){
					click(v,line,row);
					break;
				}
			}
		}
	}

	public void click(View view,int line,int row){
		Status dot = mStatus[line][row];
		switch (dot) {
		case UNKNOW:
		case FLAG:
			Open(line,row);
			break;
		case OPEN:
			OpenSurround(line,row);
			break;
		default:
			break;
		}
	}

	public boolean onLongClick(View v) {  
		// TODO Auto-generated method stub
		if(!mGameing){
			return true;
		}
		set.playSoundPool(1);
		int line,row = 0;

		mCountTime = true;

		for(line =0; line<mCountX; line++){
			for(row =0; row<mCountY; row++){
				if(mViews[line][row]==v){
					longClick(v,line,row);
					break;
				}
			}
		}
		return true;
	}

	private void longClick(View view,int line,int row){
		ImageButton btn = (ImageButton)view;

		switch (mStatus[line][row]) {
		case UNKNOW:
			mStatus[line][row] = Status.FLAG;
			minusMines();
			btn.setImageResource(R.drawable.flag);
			break;
		case FLAG:
			mStatus[line][row] = Status.UNKNOW;
			btn.setImageResource(R.drawable.space);
			plusMines();
			break;
			/*case OPEN:
			break;*/
		default:
			break;
		}
	}

	private void OpenSurround(int line, int row){
		byte dot = mField[line][row];
		if(dot>0){
			int flagCount = flagCountSurround(line, row);
			int unknowCount = unknowCountSurround(line, row);

			if(flagCount == dot){
				//open all unknow
				openAllUnknowSurround(line, row);
			}
			else if((flagCount+unknowCount) == dot){
				//flag all unknow
				flagAllUnknowSurround(line, row);
			}
		}
	}

	private void setOpenImage(int line, int row){
		int resid = 0;
		switch (mField[line][row]) {
		case 1:
			resid = R.drawable.no_1;
			break;
		case 2:
			resid = R.drawable.no_2;
			break;
		case 3:
			resid = R.drawable.no_3;
			break;
		case 4:
			resid = R.drawable.no_4;
			break;
		case 5:
			resid = R.drawable.no_5;
			break;
		case 6:
			resid = R.drawable.no_6;
			break;
		case 7:
			resid = R.drawable.no_7;
			break;
		case 8:
			resid = R.drawable.no_8;
			break;	
		default:
			break;
		}
		if(resid>0){
			((ImageButton)mViews[line][row]).setImageResource(resid);
		}
		else{
			Bitmap bitmap = getIconByPhotoId(mPhotos[mRandom.nextInt(mPhotos.length)]);
			if( null != bitmap ){
				mViews[line][row].setImageBitmap(bitmap);
			}
		}
	}

	private void openMulti(int line, int row){
		try {
			if(mStatus[line][row]!=Status.OPEN){

				if(mStatus[line][row]==Status.FLAG){
					plusMines();
				}

				//open this
				mViews[line][row].setBackgroundResource(R.drawable.open);
				mStatus[line][row] = Status.OPEN;
				setOpenImage(line, row);
				if(0 == mField[line][row]){
					openMulti(line-1,row-1);
					openMulti(line-1,row);
					openMulti(line-1,row+1);
					openMulti(line,row-1);
					openMulti(line,row+1);
					openMulti(line+1,row-1);
					openMulti(line+1,row);
					openMulti(line+1,row+1);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 *  状态
	 **/
	private enum Status{
		UNKNOW,      //未打开
		FLAG,		 //标记
		OPEN,        //已打开
	}

	private void countSurrendMines(int line,int row){
		try {
			if(mField[line][row] != -1){
				mField[line][row]++;
			}
		} catch (Exception e) {
			// TODO: handle exception 1
		}
	}

	/**
	 *  初始化地雷阵
	 **/
	private void InitMineField(int width,int height){
		mField = new byte[width][height];
		mStatus = new Status[width][height];
		Random random = new Random();
		int line,row;
		//mMines = 5;

		for(line =0; line<width; line++){
			for(row =0; row<height; row++){
				mStatus[line][row] = Status.UNKNOW;
			}
		}

		for(int i = mMines; i > 0 ; i-- ){
			line = random.nextInt(width);
			row = random.nextInt(height);

			if( -1 == mField[line][row]){
				i++;
			}
			else{
				mField[line][row] = -1;
				//周围位置的数字加1（如果不是地雷）
				countSurrendMines(line-1,row-1);
				countSurrendMines(line-1,row);
				countSurrendMines(line-1,row+1);
				countSurrendMines(line,row-1);
				countSurrendMines(line,row+1);
				countSurrendMines(line+1,row-1);
				countSurrendMines(line+1,row);
				countSurrendMines(line+1,row+1);
			}
		}
		mMinesTextView.setText(String.valueOf(mMines));
	}

	/**
	 * 当前位置的状态是否为UNKNOW，若是UNKNOW返回1，该位置不存在或者不为UNKNOW返回0
	 **/
	private int isUnknow(int line, int row){
		try {
			if(Status.UNKNOW == mStatus[line][row]){
				return 1;
			}
		} catch (Exception e) {
			// TODO: handle exception 1
			return 0;
		}
		return 0;
	}

	/**
	 * 返回当前位置周围的状态为Flag的个数
	 **/
	private int unknowCountSurround(int line, int row){
		int count = 0;
		count += isUnknow(line-1, row-1);
		count += isUnknow(line-1, row);
		count += isUnknow(line-1, row+1);

		count += isUnknow(line, row-1);
		count += isUnknow(line, row+1);

		count += isUnknow(line+1, row-1);
		count += isUnknow(line+1, row);
		count += isUnknow(line+1, row+1);	

		return count;
	}

	/**
	 * 当前位置的状态是否为FLAG，若是FLAG返回1，该位置不存在或者不为FLAG返回0
	 **/
	private int isFlag(int line, int row){
		try {
			if(Status.FLAG == mStatus[line][row]){
				return 1;
			}
		} catch (Exception e) {
			// TODO: handle exception 1
			return 0;
		}
		return 0;
	}

	/**
	 * 返回当前位置周围的状态为Flag的个数
	 **/
	private int flagCountSurround(int line, int row){
		int count = 0;
		count += isFlag(line-1, row-1);
		count += isFlag(line-1, row);
		count += isFlag(line-1, row+1);

		count += isFlag(line, row-1);
		count += isFlag(line, row+1);

		count += isFlag(line+1, row-1);
		count += isFlag(line+1, row);
		count += isFlag(line+1, row+1);	

		return count;
	}

	/**
	 * 遍历该位置周围，将周围状态为UNKNOW的位置标记为OPEN
	 **/
	private void openAllUnknowSurround(int line,int row){
		openIfUnknow(line-1,row-1);
		openIfUnknow(line-1,row);
		openIfUnknow(line-1,row+1);

		openIfUnknow(line,row-1);
		openIfUnknow(line,row+1);

		openIfUnknow(line+1,row-1);
		openIfUnknow(line+1,row);
		openIfUnknow(line+1,row+1);
	}

	private void openIfUnknow(int line,int row){
		try {
			if(Status.UNKNOW == mStatus[line][row]){
				Open(line, row);
			}
		} catch (Exception e) {
			// TODO: handle exception 8
		}
	}

	private void Open(int line, int row) {
		// TODO Auto-generated method stub
		if(-1 == mField[line][row]){
			//game over
			mViews[line][row].setBackgroundResource(R.drawable.ismine);
			mViews[line][row].setImageResource(R.drawable.mine);
			mMines = 0;
			mCountTime = false;
			mGameing = false;
			lostDialog();
		}
		else{
			openMulti(line, row);
		}
	}

	/**
	 * 遍历该位置周围，将周围状态为UNKNOW的位置标记为FLAG
	 **/
	private void flagAllUnknowSurround(int line,int row){
		flag(line-1,row-1);
		flag(line-1,row);
		flag(line-1,row+1);

		flag(line,row-1);
		flag(line,row+1);

		flag(line+1,row-1);
		flag(line+1,row);
		flag(line+1,row+1);
	}

	/**
	 * 如果该处状态为UNKNOW，标记该处，FLAG
	 **/
	private void flag(int line,int row){
		try {
			if(Status.UNKNOW == mStatus[line][row]){
				mStatus[line][row] = Status.FLAG;
				mViews[line][row].setImageResource(R.drawable.flag);
				minusMines();     //地雷数目减去1
			}
		} catch (Exception e) {
			// TODO: handle exception 8
		}
	}

	/**
	 * 地雷的数目减去1，如果数目已经等于0或者小于0，游戏结束
	 **/
	private void minusMines(){
		if((--mMines)==0 && isWin()){
			mCountTime = false;
			mGameing = false;
			//Toast.makeText(this, "You Win!", Toast.LENGTH_LONG).show();
			win();
		}
		mMinesTextView.setText(String.valueOf(mMines));
	}

	private boolean isWin(){
		int line,row;
		for(line =0; line<mCountX; line++){
			for(row =0; row<mCountY; row++){
				if( -1==mField[line][row] ^ Status.FLAG==mStatus[line][row] ){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 地雷的数目减去1，如果数目已经等于0或者小于0，游戏结束
	 **/
	private void plusMines(){
		mMines++;
		mMinesTextView.setText(String.valueOf(mMines));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Do something.
			handler.removeCallbacks(runnable);
		}
		return super.onKeyDown(keyCode, event);
	}

	//成功
	@SuppressWarnings("deprecation")
	private void win(){
		//int value;
		String message = "成功了！";

		int line,row;
		for(line =0; line<mCountX; line++){
			for(row =0; row<mCountY; row++){
				if(mStatus[line][row] == Status.UNKNOW){
					Open(line,row);
				}
			}
		}
		message +="\n用时"+mSeconds+"秒";
		winDialog(message);
		database.insert(Calendar.getInstance().getTime().toLocaleString(), mSeconds, set.getDifficulty());
	}

	/**
	 * 游戏失败的对话框
	 * @param message
	 */
	private void lostDialog(){

		int line,row;
		for(line =0; line<mCountX; line++){
			for(row =0; row<mCountY; row++){
				if(-1 == mField[line][row] && mStatus[line][row] != Status.FLAG){
					//game over
					mViews[line][row].setBackgroundResource(R.drawable.ismine);
					mViews[line][row].setImageResource(R.drawable.mine);
				}
			}
		}

		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.dialog_layout);
		
		//实例化广告条
	    AdView adView = new AdView(this, AdSize.FIT_SCREEN);
	    //获取要嵌入广告条的布局
	    LinearLayout adLayout=(LinearLayout)dialog.findViewById(R.id.ad_layout);
	    //将广告条加入到布局中
	    adLayout.addView(adView);
		
		Button okButton = (Button) dialog.findViewById(R.id.ok);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				set.playSoundPool(1);
				dialog.cancel();
			}
		});
		Button moreButton = (Button) dialog.findViewById(R.id.more);
		moreButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				set.playSoundPool(1);
				onMoreButtonClick(null);
				dialog.cancel();
			}
		});
	}

	/**
	 * 游戏成功的对话框
	 * @param message
	 */
	private void winDialog(String message){
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		dialog.getWindow().setContentView(R.layout.dialog_layout);


		Drawable d = getResources().getDrawable(R.drawable.win);
		d.setBounds(new Rect(0, 0, 50, 50));
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setCompoundDrawables(d, null, null, null);

		TextView msgTextView = (TextView) dialog.findViewById(R.id.message);

		msgTextView.setText(message);

		//实例化广告条
	    AdView adView = new AdView(this, AdSize.SIZE_320x50);
	    //获取要嵌入广告条的布局
	    LinearLayout adLayout=(LinearLayout)dialog.findViewById(R.id.ad_layout);
	    //将广告条加入到布局中
	    adLayout.addView(adView);
		
		Button okButton = (Button) dialog.findViewById(R.id.ok);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				set.playSoundPool(1);
				dialog.cancel();
			}
		});

		Button moreButton = (Button) dialog.findViewById(R.id.more);
		moreButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				set.playSoundPool(1);
				onMoreButtonClick(null);
				dialog.cancel();
			}
		});

	}

	public void onExitButtonClick(View view){
		this.finish();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		database.close();
		OffersManager.getInstance(this).onAppExit();
		super.finish();
	}

	@SuppressWarnings("unchecked")
	public void onMoreButtonClick(View view){
		// 获取自定义广告列表
		DiyManager.showRecommendWall(this);

		/*adList = DiyManager.getAdList(this);
		if(adList!=null){
			Intent i = new Intent();
			i.setClass(this, DiySourceWallActivity.class);
			startActivity(i);
		}else{
			Toast.makeText(this, "加载数据失败，请尝试再次加载。", Toast.LENGTH_LONG).show();
			DiyManager.initAdObjects(this);
		}*/
	}

	public void onHistoryButtonClick(View view){
		Intent intent = new Intent(this, HistoryActivity.class);
		startActivity(intent);
	}

	public void onHelpButtonClick(View view){
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}

	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if(mTopPanel.isOpen()){
			mTopPanel.onClick();
		}
		else{
			super.onBackPressed();
			OffersManager.getInstance(this).onAppExit(); 
		}
	}
	
	/**获得所有的联系人*/
	private void getContacts(){
		Cursor contactsCur = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,  
				new String[] {ContactsContract.Contacts.PHOTO_ID},  
						null,
						null,
						ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		if( null != contactsCur ){
			mPhotos = new String[contactsCur.getCount()];
			while (contactsCur.moveToNext()) {
				Log.i("zhengwenhui", "Position:"+contactsCur.getPosition());
				mPhotos[contactsCur.getPosition()] = contactsCur.getString(contactsCur.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
			}
			contactsCur.close();
		}
	}

	/**根据photo id 获得联系人头像*/
	@SuppressLint("NewApi")
	private Bitmap getIconByPhotoId(String photoId){
		byte[] contactIcon = null;
		Bitmap map = null; 

		String selection = ContactsContract.Data._ID + " = " + photoId;
		Cursor cur = getContentResolver().query(
				ContactsContract.Data.CONTENT_URI, 
				new String[]{ContactsContract.Data.DATA15}, 
				selection, 
				null,
				null);

		if( null != cur && cur.moveToFirst() ){
			contactIcon = cur.getBlob(0);
			map = BitmapFactory.decodeByteArray(contactIcon, 0, contactIcon.length);
		}
		return map;
	}
}
