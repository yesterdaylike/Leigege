package com.huige.mines;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;

public class Set{
	//	static final String SETTING_INFOS = "SETTING_INFOS";
	static final String OPEN_SOUND = "OPEN_SOUND";
	static final String DIFFICULTY = "DIFFICULTY";
	static final String PRIMARY = "PRIMARY";
	static final String MIDDLE = "MIDDLE";
	static final String HIGH = "HIGH";
	
	static final String SKIN = "SKIN";
	static final String CLASSIC_SKIN = "CLASSIC_SKIN";
	static final String TIME_SKIN = "TIME_SKIN";

	/*	private CheckBox openSound;
	private RadioGroup difficulty;*/

	private SoundPool mSoundPool;
	public boolean mOpenSound = true;  
	private int mClickSound;
	private int mLongclickSound; 

	public int mDifficulty;
	public int mSkin;
	
	private Context mContext;

	/*@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_layout);
		initSoundPool();
		openSound = (CheckBox)findViewById(R.id.openSoundCheckBox);
		difficulty = (RadioGroup)findViewById(R.id.myRadioGroup);
		RadioButton radioButton = (RadioButton)(difficulty.getChildAt(getDifficulty(this)));
		radioButton.setChecked(true);
		openSound.setChecked(getOpenSound(this));
	}*/
	public Set(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		getOpenSound();
		getDifficulty();
		getSkin();
		//指定声音池的最大音频流数目为10，声音品质为5  
		mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);  
		//载入音频流，返回在池中的id  
		mLongclickSound = mSoundPool.load(mContext, R.raw.command, 0);  
		mClickSound = mSoundPool.load(mContext, R.raw.start, 0);
	}

	/*	public static int getPrimary(Context context){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		//		SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
		return settings.getInt(PRIMARY, -1);
	}
	public static int getMiddle(Context context){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		//		SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
		return settings.getInt(MIDDLE, -1);
	}
	public static int getHigh(Context context){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		//		SharedPreferences settings = context.getSharedPreferences(SETTING_INFOS, 0);
		return settings.getInt(HIGH, -1);
	}

	public static void setPrimary(Context context,int value){
		Editor editor  = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putInt(PRIMARY, value);
		editor.commit();
	}
	public static void setMiddle(Context context,int value){
		Editor editor  = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putInt(MIDDLE, value);
		editor.commit();
	}
	public static void setHigh(Context context,int value){
		Editor editor  = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putInt(HIGH, value);
		editor.commit();
	}*/

	public boolean getOpenSound(){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		mOpenSound = settings.getBoolean(OPEN_SOUND, true);
		return mOpenSound;
	}

	public void setOpenSound(boolean isChecked ){
		Editor editor  = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		editor.putBoolean(OPEN_SOUND, isChecked);
		editor.commit();
		mOpenSound = isChecked;
	}

	public int getDifficulty(){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		mDifficulty = settings.getInt(DIFFICULTY, 0);
		return mDifficulty;
	}
	
	public void setDifficulty(int difficulty){
		Editor editor  = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		editor.putInt(DIFFICULTY, difficulty);
		editor.commit();
		mDifficulty = difficulty;
	}

	public int getSkin(){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
		mSkin = settings.getInt(SKIN, 0);
		return mSkin;
	}
	
	public void setSkin(int skin){
		Editor editor  = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
		editor.putInt(SKIN, skin);
		editor.commit();
		mDifficulty = skin;
	}

	public void playSoundPool(int type){
		if(mOpenSound){
			switch (type) {
			case 0:
				mSoundPool.play(mLongclickSound, 0.5f, 0.5f, 0, 0, 1);
				break;
			case 1:
				mSoundPool.play(mClickSound, 0.5f, 0.5f, 0, 0, 1);
				break;

			default:
				break;
			}
		}
	}

	/*	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Editor editor  = PreferenceManager.getDefaultSharedPreferences(this).edit();
			editor.putBoolean(OPEN_SOUND, openSound.isChecked());
			RadioButton rButton;
			for(int i = 0; i<difficulty.getChildCount(); i++){
				rButton = (RadioButton) difficulty.getChildAt(i);
				if(rButton.isChecked()){
					editor.putInt(DIFFICULTY, i);
					break;
				}
			}
			editor.commit();
		}
		return super.onKeyDown(keyCode, event);
	}*/

	/*	public void onClick(View view){
		if(view.getId()==R.id.openSoundCheckBox){
			OpenSound = ((CheckBox)view).isChecked();
		}
		playSoundPool();
	}*/
	/*
	private boolean initSoundPool(){

		OpenSound = Set.getOpenSound(this);
		//ָ�������ص������Ƶ����ĿΪ10������Ʒ��Ϊ5  
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);  
		//������Ƶ���������ڳ��е�id  
		clickSound = soundPool.load(this, R.raw.command, 0);
		return OpenSound;
	}*/

	/*	private void playSoundPool(){
		if(OpenSound){
			soundPool.play(clickSound, 0.5f, 0.5f, 0, 0, 1);
		}
	}*/
}
