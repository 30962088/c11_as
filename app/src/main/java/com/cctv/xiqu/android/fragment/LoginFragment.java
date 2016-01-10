package com.cctv.xiqu.android.fragment;

import org.apache.http.Header;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.InfoListActivity;
import com.cctv.xiqu.android.SettingActivity;
import com.cctv.xiqu.android.fragment.FillInfoFragment.Model;
import com.cctv.xiqu.android.fragment.network.GetPushInfoRequest;
import com.cctv.xiqu.android.fragment.network.IsHaveSingerRequest;
import com.cctv.xiqu.android.fragment.network.UserloginVerifyRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.RequestHandler;
import com.cctv.xiqu.android.fragment.network.BaseClient.SimpleRequestHandler;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.cctv.xiqu.android.utils.OauthUtils;
import com.cctv.xiqu.android.utils.RegexUtils;
import com.cctv.xiqu.android.utils.OauthUtils.OauthCallback;
import com.cctv.xiqu.android.utils.OauthUtils.Result;
import com.mengle.lib.utils.Utils;

import com.cctv.xiqu.android.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class LoginFragment extends BaseFragment implements OnClickListener,
		OauthCallback {

	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	private OauthUtils oauthUtils;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		oauthUtils = new OauthUtils(getActivity());
		oauthUtils.setOauthCallback(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.login_layout, null);
	}

	private EditText phoneText;

	private EditText passwordText;

	private View nofityGoneView;

	private View nofityVisibleView;

	private TextView notifyCountView;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		phoneText = (EditText) view.findViewById(R.id.phone);
		passwordText = (EditText) view.findViewById(R.id.password);
		view.findViewById(R.id.signin).setOnClickListener(this);
		view.findViewById(R.id.weibo_btn).setOnClickListener(this);
		view.findViewById(R.id.qq_btn).setOnClickListener(this);
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		view.findViewById(R.id.setting_btn).setOnClickListener(this);
		view.findViewById(R.id.info_btn).setOnClickListener(this);
		view.findViewById(R.id.findpwd).setOnClickListener(this);
		nofityGoneView = view.findViewById(R.id.nofity_gone);
		nofityVisibleView = view.findViewById(R.id.notify_visible);
		notifyCountView = (TextView) view.findViewById(R.id.notify_count);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.findpwd:
			getParentFragment()
					.getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container,
							FindPWDFragment.newInstance())
					.addToBackStack("password").commit();
			break;
		case R.id.info_btn:
			InfoListActivity.open(getActivity());
			break;
		case R.id.setting_btn:
			SettingActivity.open(getActivity());
			break;
		case R.id.signin:
			getParentFragment()
					.getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container,
							SigninFragment.newInstance())
					.addToBackStack("sigin").commit();
			break;
		case R.id.weibo_btn:
			oauthUtils.sinaOauth();
			break;
		case R.id.qq_btn:
			oauthUtils.qqOauth();
			break;
		case R.id.login_btn:
			onlogin();
			break;
		default:
			break;
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPushCount();
	}

	private void getPushCount() {
		GetPushInfoRequest request = new GetPushInfoRequest(getActivity());
		request.request(new SimpleRequestHandler() {
			@Override
			public void onSuccess(Object object) {
				GetPushInfoRequest.Result result = (GetPushInfoRequest.Result) object;
				if (result.getResult() == 1000) {
					int count = result.getPushinfolist().size();
					if (count == 0) {
						nofityVisibleView.setVisibility(View.GONE);
						nofityGoneView.setVisibility(View.VISIBLE);
					} else {
						nofityVisibleView.setVisibility(View.VISIBLE);
						nofityGoneView.setVisibility(View.GONE);
						notifyCountView.setText("" + count);
					}
				}
			}
		});
	}

	private void onlogin() {
		String phone = phoneText.getText().toString();
		String password = passwordText.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			Utils.tip(getActivity(), "请输入手机号码");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Utils.tip(getActivity(), "请输入密码");
			return;
		}
		if (!RegexUtils.checkPhone(phone)) {
			Utils.tip(getActivity(), "手机号码格式错误");
			return;
		}

		UserloginVerifyRequest request = new UserloginVerifyRequest(
				getActivity(), new UserloginVerifyRequest.Params(phone,
						password));

		LoadingPopup.show(getActivity());
		request.request(new SimpleRequestHandler() {
			@Override
			public void onComplete() {
				LoadingPopup.hide(getActivity());
			}

			@Override
			public void onSuccess(Object object) {

				((MemberFragment) getParentFragment())
						.initFragment(UserSettingFragment.newInstance(APP
								.getSession().getSid()));

			}

			@Override
			public void onError(int error, String msg) {
				Utils.tip(getActivity(), "用户名或密码错误");
			}

		});

	}

	@Override
	public void onSuccess(Result result) {
		final Model model = result.toModel();
		IsHaveSingerRequest request = new IsHaveSingerRequest(getActivity(),
				result.toParams());
		LoadingPopup.show(getActivity());
		request.request(new RequestHandler() {

			@Override
			public void onSuccess(Object object) {
				IsHaveSingerRequest.Result result = (IsHaveSingerRequest.Result) object;
				Fragment fragment;
				if (result.getPkey() == null) {
					fragment = FillInfoFragment.newInstance(model);
				} else {
					fragment = UserSettingFragment.newInstance(""
							+ result.getSid());
				}
				getParentFragment().getChildFragmentManager()
						.beginTransaction()
						.replace(R.id.fragment_container, fragment)
						.addToBackStack("fillinfo").commit();
			}

			@Override
			public void onComplete() {
				LoadingPopup.hide(getActivity());

			}

			@Override
			public void onError(int error, String msg) {
				if(error == 1004){
					getParentFragment().getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.fragment_container, FillInfoFragment.newInstance(model))
					.addToBackStack("fillinfo").commit();
				}
				
				

			}
		});

	}

}
