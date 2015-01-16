package com.example.customview;

import com.example.customedittext.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 * 带删除功能输入框
 * @author chendd
 *
 */
public class ClearEditText extends EditText implements OnFocusChangeListener, TextWatcher {
	/**
	 * 删除按钮的引用
	 */
	private Drawable mClearDrawable;
	
	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(R.drawable.sd_select_popupmenu_delete);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth()/2, mClearDrawable.getIntrinsicHeight()/2);
		setOnFocusChangeListener(this);
		addTextChangedListener(this);
		setClearIconVisible(false);
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth() - mClearDrawable.getIntrinsicWidth()) && (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable) {
					if(this.getError() == null || this.getError().equals("")) {
						this.setText("");
					}					
				}
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		EditText _v=(EditText)v;
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
			// 也可以获取焦点时清除hint
//			String hint=_v.getHint().toString();
//            _v.setTag(hint);
//            _v.setHint("");
		} else {
			setClearIconVisible(false);
			// 也可以失去焦点时还原hint
//			_v.setHint(_v.getTag().toString());
		}
		
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {		
		Drawable right;
	
		if(visible&&hasFocus()&&this.getError() == null) {
			right = mClearDrawable;
		}else if(hasFocus()&&this.getError() != null){
			right = getCompoundDrawables()[2];
		}else {
			right = null; 
		}
		

		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		setClearIconVisible(s.length() > 0);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	/**
	 * 晃动动画
	 * 
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
