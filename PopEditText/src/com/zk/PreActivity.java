package com.zk;

import android.content.Intent;
import android.os.Bundle;

public class PreActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pre);
		
		startActivity(new Intent(this, MainActivity.class));
		
	}
	
}
