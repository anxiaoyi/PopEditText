package com.zk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends BaseActivity {

	private PopEditText mEditText;
	private ViewGroup mContainer;
	
	private Button mButtonA;
	private Button mButtonB;
	private Button mButtonC;
	
	private View viewA;
	private View viewB;
	private View viewC;
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mEditText = (PopEditText) findViewById(R.id.popEditText1);
		mContainer = (ViewGroup) findViewById(R.id.view_container);
		mButtonA = (Button) findViewById(R.id.button1);
		mButtonB = (Button) findViewById(R.id.button2);
		mButtonC = (Button) findViewById(R.id.button3);
		
		viewA = LayoutInflater.from(this).inflate(R.layout.view_a, null);
		viewB = LayoutInflater.from(this).inflate(R.layout.view_b, null);
		viewC = LayoutInflater.from(this).inflate(R.layout.view_c, null);
		
		mEditText.setContainer(mContainer);
		mButtonA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditText.dispatchOnClickEvent(v, viewA);
			}
		});
		
		mButtonB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditText.dispatchOnClickEvent(v, viewB);
			}
		});
		
		mButtonC.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditText.dispatchOnClickEvent(v, viewC);
			}
		});
		
		findViewById(R.id.next_activity_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, NextActivity.class));
			}
		});
	}
}
