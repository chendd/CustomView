package com.example.customview;

import java.util.ArrayList;
import java.util.List;

import com.example.customedittext.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

 
/**
 * 自定义 邮箱输入 补全控件
 * @author chendd
 *
 */
@SuppressLint("NewApi")
public class AutoEmailComplete extends AutoCompleteTextView  implements OnFocusChangeListener{  
    private static String[] emailSuffix;//{ "qq.com","gmail.com", "163.com", "126.com", "sina.com", "hotmail.com",  
       // "yahoo.cn", "sohu.com", "foxmail.com", "139.com"};  
    
    /**
	 * 删除按钮的引用
	 */
	private Drawable mClearDrawable;
    
    public AutoEmailComplete(Context context){  
        super(context);  
        init(context);  
    }  
    public AutoEmailComplete(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init(context);  
    }  
      
    private void init(Context context){  
    	emailSuffix = getResources().getStringArray(R.array.email_suffix);
    	// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
    	mClearDrawable = getCompoundDrawables()[2];
    	if (mClearDrawable == null) {
    		mClearDrawable = getResources().getDrawable(R.drawable.sd_select_popupmenu_delete);
    	}
    	mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth()/2, mClearDrawable.getIntrinsicHeight()/2);
    	setClearIconVisible(false);
    	setOnFocusChangeListener(this);
    			
        final MyAdatper adapter = new MyAdatper(context);  
        setAdapter(adapter);  
        addTextChangedListener(new TextWatcher() {  
            @Override  
            public void afterTextChanged(Editable s) {  
                String input = s.toString();  
                adapter.mList.clear();  
                // 输入第二个字符是@符后提示邮箱后缀
                if (input.length()>1&&"@".equals(input.substring(input.length()-1))) {  
                    for (int i = 0; i < emailSuffix.length; ++i) {  
                        adapter.mList.add(input + emailSuffix[i]);  
                    }  
                }  
                adapter.notifyDataSetChanged();  
                //showDropDown();  
            }  
  
            @Override  
            public void beforeTextChanged(CharSequence s, int start, int count,  
                    int after) {  
                  
            }  
            @Override  
            public void onTextChanged(CharSequence s, int start, int before,  
                    int count) {  
            	setClearIconVisible(s.length() > 0);
            }  
        });  
        // default=2 当输入一个字符的时候就开始检测  
        setThreshold(1);  
    }  
    
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
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth() - getPaddingRight() - mClearDrawable.getIntrinsicWidth()) && (event.getX() < ((getWidth() - getPaddingRight())));
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
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		Drawable right;
		if(this.getError() == null || this.getError().equals("")){
			right = visible&hasFocus() ? mClearDrawable : null;
		}else {
			right = getCompoundDrawables()[2];
		}
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}
      
    class MyAdatper extends BaseAdapter implements Filterable {  
        List<String> mList;  
        private Context mContext;  
        private MyFilter mFilter;  
          
        public MyAdatper(Context context) {  
            mContext = context;  
            mList = new ArrayList<String>();  
        }  
          
        @Override  
        public int getCount() {  
            return mList == null ? 0 : mList.size();  
        }  
  
        @Override  
        public Object getItem(int position) {  
            return mList == null ? null : mList.get(position);  
        }  
  
        @Override  
        public long getItemId(int position) {  
            return position;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
        	
        	ViewHolder holder = null;  
            if(convertView==null){  
                holder=new ViewHolder();  
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
                convertView=inflater.inflate(R.layout.sd_list_item_autocomplete, null);  
                holder.tv=(TextView)convertView.findViewById(R.id.simple_item_0);    
                convertView.setTag(holder);  
            }else{  
                holder = (ViewHolder) convertView.getTag();  
            }  
              
            holder.tv.setText(mList.get(position));              
            return convertView; 
        	
        	
        }  
  
        @Override  
        public Filter getFilter() {  
            if (mFilter == null) {  
                mFilter = new MyFilter();  
            }  
            return mFilter;  
        }  
          
        private class MyFilter extends Filter {  
  
            @Override  
            protected FilterResults performFiltering(CharSequence constraint) {  
                FilterResults results = new FilterResults();  
                if (mList == null) {  
                    mList = new ArrayList<String>();  
                }  
                results.values = mList;  
                results.count = mList.size();  
                return results;  
            }  
  
            @Override  
            protected void publishResults(CharSequence constraint, FilterResults results) {  
                if (results.count > 0) {  
                    notifyDataSetChanged();  
                } else {  
                    notifyDataSetInvalidated();  
                }  
            }  
        }  
    }  
    
    class ViewHolder {  
        TextView tv;  
    }

}