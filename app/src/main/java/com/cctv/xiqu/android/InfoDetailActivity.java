package com.cctv.xiqu.android;

import java.text.SimpleDateFormat;

import com.cctv.xiqu.android.adapter.InfoListAdapter.InfoItem;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class InfoDetailActivity extends BaseActivity implements OnClickListener{

	public static void open(Context context,InfoItem item){
		
		Intent intent = new Intent(context, InfoDetailActivity.class);
		
		intent.putExtra("item", item);
		
		context.startActivity(intent);
		
	}
	
	private TextView titleView;
	
	private TextView descView;
	
	private TextView timeView;

	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		InfoItem item = (InfoItem) getIntent().getSerializableExtra("item");
		setContentView(R.layout.activity_info_detail);
		findViewById(R.id.back).setOnClickListener(this);
		titleView = (TextView) findViewById(R.id.title);
		descView = (TextView) findViewById(R.id.desc);
		timeView = (TextView) findViewById(R.id.time);
		titleView.setText(item.getTitle());
		descView.setText(item.getDesc());
		timeView.setText(new SimpleDateFormat("yyyy/MM/dd").format(item.getDate()));
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}
	
}
