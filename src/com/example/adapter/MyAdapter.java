package com.example.adapter;
import static com.example.bean.Cheeses.NAMES;

import java.util.ArrayList;

import com.example.bean.Cheeses;
import com.example.swipdelete.R;
import com.example.ui.SwipLayout;
import com.example.ui.SwipLayout.OnSwipLayoutListener;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter {
	protected static final String TAG = "TAG";
	private Context context;
	private ArrayList<SwipLayout> openItem;
	public MyAdapter(Context context) {
		super();
		this.context = context;
		//打开的条目
		openItem = new ArrayList<SwipLayout>();
	}

	

	@Override
	public int getCount() {
		
		return NAMES.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return NAMES[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(convertView == null){
			view = View.inflate(context,R.layout.item_list,null);
		}
		ViewHolder mHolder = ViewHolder.getHolder(view);
		final SwipLayout sl = (SwipLayout)view;
		mHolder.getHolder(view).lv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
        sl.setSwipLayoutListener(new OnSwipLayoutListener() {

			@Override
			public void onOpen(SwipLayout mSwipLayout) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onOpen");
				openItem.add(mSwipLayout);
			}

			@Override
			public void onClose(SwipLayout mSwipLayout) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onClose");
				openItem.remove(mSwipLayout);
			}

			@Override
			public void onDraging(SwipLayout mSwipLayout) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStartOpen(SwipLayout mSwipLayout) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onStartOpen");

				for (SwipLayout layout  : openItem) {
					layout.close(true);
				}
				openItem.clear();
			}

			@Override
			public void onStartClose(SwipLayout mSwipLayout) {
				// TODO Auto-generated method stub
				Log.d(TAG,"onStartClose");
			}

			
		});
		return view;
	}
	public static class ViewHolder{
		public TextView lv_call;
		public TextView lv_delete;
		public static ViewHolder getHolder(View view) {
			Object tag = view.getTag();
			if(tag == null){
			
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.lv_call = (TextView) view.findViewById(R.id.call);
				viewHolder.lv_delete = (TextView) view.findViewById(R.id.delete);
				tag = viewHolder;
				view.setTag(tag);
				
			}
			return (ViewHolder) tag;
		}
	}
	

}
