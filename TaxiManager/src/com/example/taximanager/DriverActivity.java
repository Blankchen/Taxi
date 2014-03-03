package com.example.taximanager;

import com.example.taximanager.R.string;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class DriverActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		
		setContentView(R.layout.activity_driver);
		Intent intent = this.getIntent();
	    Bundle bundle = intent.getExtras();
	    EditText et = (EditText)findViewById(R.id.editText1);
	    et.setText(bundle.getString("Number"));
		
	    Spinner status = (Spinner) findViewById(R.id.spinner1);
	    ArrayAdapter<CharSequence> lunchList = ArrayAdapter.createFromResource(
	    		this, R.array.DriverStatus, 
	    		android.R.layout.simple_spinner_item);
	    lunchList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    status.setAdapter(lunchList);
	 
	}
	
}
