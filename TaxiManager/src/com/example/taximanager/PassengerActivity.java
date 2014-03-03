package com.example.taximanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class PassengerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_passenger);
		Intent intent = this.getIntent();
	    Bundle bundle = intent.getExtras();
	    EditText et = (EditText)findViewById(R.id.editText1);
	    et.setText(bundle.getString("Number"));
	
	}

}
