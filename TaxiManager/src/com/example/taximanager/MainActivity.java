package com.example.taximanager;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText et1;
	private CheckBox cb1;
	private RadioGroup rg1;
	private RadioButton rb1, rb2;	
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		et1 = (EditText) findViewById(R.id.editText1);
		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		rg1 = (RadioGroup) findViewById(R.id.radioGroup1);
		rb1 = (RadioButton) findViewById(R.id.radio0);
		rb2 = (RadioButton) findViewById(R.id.radio1);
		list = (ListView)findViewById(R.id.listView1);
		// save the status of user
        SharedPreferences settings = getSharedPreferences("SETTING_Infos", 0);
        String strJudge = settings.getString("judgeText", "no");
        String strClass = settings.getString("userClass", "passenger");
		String strUserNumber = settings.getString("userNumberText", "");
			
		// setting the phone number and checkbox
		if(strJudge.equals("yes")){
			cb1.setChecked(true);
			et1.setText(strUserNumber);
		}else{
			cb1.setChecked(false);
			et1.setText("");
		}
		
		//setting the radiobutton
		if(strClass.equals("driver")){
			rb1.setChecked(true);
		}else{
			rb2.setChecked(true);
		}
		
		// checkbox and radiogroup event
		cb1.setOnCheckedChangeListener(CheckedChangeListenerCB);
		rg1.setOnCheckedChangeListener(CheckedChangeListenerRG);
		
		//button event
		findViewById(R.id.button1).setOnClickListener(ClickListener);
		
	}
	
	private CheckBox.OnCheckedChangeListener CheckedChangeListenerCB = new  CheckBox.OnCheckedChangeListener(){
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			SharedPreferences settings = getSharedPreferences("SETTING_Infos", 0);
			if(isChecked == true){
				settings.edit().putString("judgeText", "yes")
				.putString("userNumberText", et1.getText().toString())
				.commit();
				Toast.makeText(MainActivity.this, R.string.SaveAccount, Toast.LENGTH_SHORT)
				.show();
			}else{
				settings.edit().putString("judgeText", "no")
				.putString("userNumberText", "")
				.commit();
				Toast.makeText(MainActivity.this, R.string.UnSaveAccount, Toast.LENGTH_SHORT)
				.show();
			}
			
		}
		
	};
	
	private RadioGroup.OnCheckedChangeListener CheckedChangeListenerRG = new  RadioGroup.OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			SharedPreferences settings = getSharedPreferences("SETTING_Infos", 0);
			if(checkedId == R.id.radio0){
				settings.edit().putString("userClass", "driver")				
				.commit();
				Toast.makeText(MainActivity.this, R.string.Driver, Toast.LENGTH_SHORT)
				.show();
			}else{
				settings.edit().putString("userClass", "passenger")
				.commit();
				Toast.makeText(MainActivity.this, R.string.Passenger, Toast.LENGTH_SHORT)
				.show();
			}	
			
		}
		
	};
	
	private Button.OnClickListener ClickListener = new  Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(et1.getText().toString().length() != 10)
				Toast.makeText(MainActivity.this, R.string.NotNumber, Toast.LENGTH_SHORT)
				.show();
			else{
				if(rb1.isChecked()){
					//php identify the number					
					addData();
					
					 /*
					  * //這些附加在 Intent 上的訊息都儲存在 Bundle 物件中
			        Intent intent = new Intent(MainActivity.this, DriverActivity.class);
			        Bundle bundle = new Bundle();
			        bundle.putString("Number", et1.getText().toString());	
			        //透過「intent.putExtras(bundle)」敘述，將「bundle」 物件附加在 Intent 上，隨著 Intent 送出而送出
			        intent.putExtras(bundle);
			        startActivityForResult(intent, 0);
			        */
				}else{
					//setting data to next activity
					Intent intent = new Intent(MainActivity.this, PassengerActivity.class);
			        Bundle bundle = new Bundle();
			        bundle.putString("Number", et1.getText().toString());	
			        //透過「intent.putExtras(bundle)」敘述，將「bundle」 物件附加在 Intent 上，隨著 Intent 送出而送出
			        intent.putExtras(bundle);
			        startActivityForResult(intent, 0);
					
				}
				relistData();
			}
			
		}};
		
		
		private void addData(){
			String nuser = et1.getText().toString();
			try{
				HttpClient client = new DefaultHttpClient();
				HttpGet get = 
						new HttpGet("http://192.168.56.1/mysql_adddata.php?newuser=" +
															nuser);
				client.execute(get);
			}catch (Exception ee){}
			et1.setText("");
			relistData();
			
		}
		
		private void relistData(){
			String[] from = {"item"};
			int[] to = {R.id.item};
			ArrayList<HashMap<String,String>> data = new ArrayList();
			
			try{
				HttpClient client = new DefaultHttpClient();
				HttpGet get = 
						new HttpGet("http://192.168.56.1/mysql_query.php");
				HttpResponse response = client.execute(get);
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent()));
				String raw;
				while ((raw = reader.readLine()) != null){
					HashMap<String,String> it = new HashMap();
					it.put(from[0], raw);
					data.add(it);
				}
				reader.close();
				SimpleAdapter adapter = 
						new SimpleAdapter(
								this, data, R.layout.listitem, from, to);
				list.setAdapter(adapter);
			}catch (Exception ee){}
		}
		
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
