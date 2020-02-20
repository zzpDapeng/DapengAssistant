package com.example.dapeng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.dapeng.HttpGetDataListener;
import com.example.dapeng.ListData;
import com.example.dapeng.R.string;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity implements HttpGetDataListener,
OnClickListener {

	private HttpData httpData;
	private List<ListData> lists;
	private ListView lv;
	private EditText sendtext;
	private Button send_btn;
	private String content_str;
	private TextAdapter adapter;
	private String [] welcome_array;
	private double currentTime,oldTime=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	
	private void initView() {
		lv = (ListView)findViewById(R.id.lv);
		sendtext = (EditText)findViewById(R.id.sendText);
		send_btn = (Button)findViewById(R.id.send_btn);
		lists = new ArrayList<ListData>();
		send_btn.setOnClickListener(this);
		adapter = new TextAdapter(lists, this) ;
		lv.setAdapter(adapter);
		ListData listData;
		listData = new ListData(getRandomWelcomeTips(), ListData.RECEVER,getTime());
		lists.add(listData);
	}
	
	private String getRandomWelcomeTips(){
		String welcome_tip = null;
		welcome_array = this.getResources().getStringArray(R.array.welcome_tips);
		int index = (int)(Math.random()*(welcome_array.length-1));
		welcome_tip = welcome_array[index];
		return welcome_tip;
	}
	
	@Override
	public void getDataUrl(String data) {
		System.out.println(data);
		parseText(data);
	}
	
	public void parseText(String str){

		try {
			JSONObject jb = new JSONObject(str);
			ListData listData;
			listData = new  ListData(jb.getString("text"),ListData.RECEVER,getTime());
			lists.add(listData);
			adapter.notifyDataSetChanged();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		getTime();
		content_str = sendtext.getText().toString();
		sendtext.setText("");  //清空输入框
		String dropk = content_str.replace(" ", "");
		String droph = content_str.replace("\n", "");
		ListData listData;
		listData = new ListData(content_str,ListData.SEND,getTime());
		lists.add(listData);

		adapter.notifyDataSetChanged();
		httpData = (HttpData) new HttpData(
		//替换为你的API_KEY
				"http://www.tuling123.com/openapi/api?key=YOUR_APIKEY&info="+droph, this).execute();
	}
	
	private String getTime(){
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日    hh:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		if(currentTime-oldTime>=5*60*1000){
			oldTime = currentTime;
			return str;
		}else{
			return "";
		}
	}
}

