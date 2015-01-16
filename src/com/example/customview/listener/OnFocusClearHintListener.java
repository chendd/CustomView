package com.example.customview.listener;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * 当输入框 获取焦点,清除hint
 * @author chendd
 *
 */
public class OnFocusClearHintListener implements OnFocusChangeListener{

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		EditText _v=(EditText)v;

        if (!hasFocus) {// 失去焦点
            _v.setHint(_v.getTag().toString());
        } else {
            String hint=_v.getHint().toString();
            _v.setTag(hint);
            _v.setHint("");
        }
		
	}

}