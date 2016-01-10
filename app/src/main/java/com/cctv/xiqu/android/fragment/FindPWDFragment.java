package com.cctv.xiqu.android.fragment;

import java.util.Timer;
import java.util.TimerTask;

import com.cctv.xiqu.android.fragment.network.ForgetpasswordRequest;
import com.cctv.xiqu.android.fragment.network.GetVerifyCodeRequest;
import com.cctv.xiqu.android.fragment.network.UpdateSingerInfoRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.RequestHandler;
import com.cctv.xiqu.android.fragment.network.BaseClient.SimpleRequestHandler;
import com.cctv.xiqu.android.utils.RegexUtils;
import com.mengle.lib.utils.Utils;

import com.cctv.xiqu.android.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FindPWDFragment extends BaseFragment implements OnClickListener {

	public static FindPWDFragment newInstance() {
		return new FindPWDFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.find_pwd_layout, null);
	}

	private EditText phoneEditText;

	private TextView sendBtn;

	private EditText verifyEditText;

	private EditText passwordEditText;

	private EditText repasswordEditText;

	private View nextBtn;

	private String verifyCode;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.back).setOnClickListener(this);
		phoneEditText = (EditText) view.findViewById(R.id.phone);
		sendBtn = (TextView) view.findViewById(R.id.send);
		sendBtn.setOnClickListener(this);
		verifyEditText = (EditText) view.findViewById(R.id.verify);
		passwordEditText = (EditText) view.findViewById(R.id.password);
		repasswordEditText = (EditText) view.findViewById(R.id.repassword);
		nextBtn = view.findViewById(R.id.next);
		nextBtn.setOnClickListener(this);
	}

	private void sendVerify() {
		String phone = phoneEditText.getText().toString();

		
		if (TextUtils.isEmpty(phone)) {
			Utils.tip(getActivity(), "手机号不能为空");
			return;
		}
		if (!RegexUtils.checkPhone(phone)) {
			Utils.tip(getActivity(), "手机号格式错误");
			return;
		}
		
		

		GetVerifyCodeRequest.Params params = new GetVerifyCodeRequest.Params(
				phone, 3);
		GetVerifyCodeRequest request = new GetVerifyCodeRequest(getActivity(),
				params);
		request.request(new RequestHandler() {

			@Override
			public void onSuccess(Object object) {
				GetVerifyCodeRequest.Result result = (GetVerifyCodeRequest.Result) object;
				startTimer();
				verifyCode = result.getCode();
				
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(int error, String msg) {
				switch (error) {
				case 1015:
					Utils.tip(getActivity(), "手机已经注册过了");
					break;
				default:
					Utils.tip(getActivity(), "发送验证码失败");
					break;
				}
				
			}
		});
		
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			((MemberFragment) getParentFragment()).backFragment();
			break;
		case R.id.send:
			sendVerify();
			break;
		case R.id.next:
			next();
			break;
		default:
			break;
		}

	}

	private void next() {
		String phone = phoneEditText.getText().toString();
		if(TextUtils.isEmpty(phone)){
			Utils.tip(getActivity(), "请输入手机号");
			return;
		}
		if (!RegexUtils.checkPhone(phone)) {
			Utils.tip(getActivity(), "手机号格式错误");
			return;
		}
		
		String verify = verifyEditText.getText().toString();
		if(TextUtils.isEmpty(verify)){
			Utils.tip(getActivity(), "请输入验证码");
			return;
		}
		String password = passwordEditText.getText().toString();
		if(TextUtils.isEmpty(password)){
			Utils.tip(getActivity(), "密码不能为空");
			return;
		}
		String repassword = repasswordEditText.getText().toString();
		if(TextUtils.isEmpty(repassword)){
			Utils.tip(getActivity(), "请确认密码");
			return;
		}
		if(!TextUtils.equals(password, repassword)){
			Utils.tip(getActivity(), "两次密码输入不一致");
			return;
		}
		if(!TextUtils.equals(verify, verifyCode)){
			Utils.tip(getActivity(), "验证码输入有误");
			return;
		}
		ForgetpasswordRequest request = new ForgetpasswordRequest(getActivity(), new ForgetpasswordRequest.Params(phone, password));
		request.request(new SimpleRequestHandler(){
			@Override
			public void onError(int error, String msg) {
				Utils.tip(getActivity(), "提交失败");
			}
			
			@Override
			public void onSuccess(Object object) {
				Utils.tip(getActivity(), "找回密码成功");
				((MemberFragment) getParentFragment()).backFragment();
			}
			
		});
		
		
	}

	private void startTimer() {
		Timer t = new Timer();
		t.schedule(new SendTimer(60), 0, 1000);
	}

	private class SendTimer extends TimerTask {

		private int second;

		public SendTimer(int second) {
			super();
			this.second = second;
			sendBtn.setEnabled(false);
		}

		@Override
		public void run() {
			Activity activity = getActivity();
			if (activity != null) {
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						second--;
						sendBtn.setText("发送验证码(" + second + ")");
						if (second == 0) {
							sendBtn.setText("发送验证码");
							sendBtn.setEnabled(true);
							cancel();
						}

					}
				});
			}

		}

	}

}
