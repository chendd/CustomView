package com.example.customview;

import com.example.customedittext.R;
import com.example.customview.listener.OnFocusClearHintListener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class TestActivity extends Activity {

	EditText editText;
	ClearEditText clearEditText;
	AutoEmailComplete autoEmailComplete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		// findview by id
		editText = (EditText) findViewById(R.id.editText);
		clearEditText = (ClearEditText) findViewById(R.id.clearEditText);
		autoEmailComplete = (AutoEmailComplete) findViewById(R.id.autoEmailComplete);
		
		// add listener
		editText.setOnFocusChangeListener(new OnFocusClearHintListener());
	}

}
