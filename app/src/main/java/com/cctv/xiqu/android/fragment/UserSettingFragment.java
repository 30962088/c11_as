package com.cctv.xiqu.android.fragment;


import java.io.File;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.BaseActivity;
import com.cctv.xiqu.android.InfoListActivity;
import com.cctv.xiqu.android.SettingActivity;
import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.BaseActivity.OnCitySelectionListener;
import com.cctv.xiqu.android.BaseActivity.OnGallerySelectionListener;
import com.cctv.xiqu.android.BaseActivity.OnModifyPasswordListener;
import com.cctv.xiqu.android.BaseActivity.OnModifyPhoneListener;
import com.cctv.xiqu.android.BaseActivity.OnNicknameFillListener;
import com.cctv.xiqu.android.BaseActivity.OnWeiboBindingListener;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.GetPushInfoRequest;
import com.cctv.xiqu.android.fragment.network.GetSingerInfoRequest;
import com.cctv.xiqu.android.fragment.network.UpdateSingerInfoRequest;
import com.cctv.xiqu.android.fragment.network.BaseClient.SimpleRequestHandler;
import com.cctv.xiqu.android.utils.AliyunUtils;
import com.cctv.xiqu.android.utils.CropImageUtils;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.cctv.xiqu.android.utils.AliyunUtils.UploadListener;
import com.cctv.xiqu.android.utils.AliyunUtils.UploadResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UserSettingFragment extends BaseFragment implements
		OnClickListener,OnGallerySelectionListener,UploadListener,OnCitySelectionListener,OnNicknameFillListener
		,OnModifyPhoneListener,OnModifyPasswordListener,OnWeiboBindingListener{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static UserSettingFragment newInstance(String sid) {
		Bundle args = new Bundle();
		args.putString("sid", sid);
		UserSettingFragment fragment = new UserSettingFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sid = getArguments().getString("sid");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.user_setting_layout, null);
	}
	
	private View containerView;
	
	private String sid;

	private ImageView avatar;

	private TextView city;

	private View weiboBinding;

	private View weiboUnBinding;

	private TextView nickname;
	
	private View phoneContainer;

	private View nofityGoneView;
	
	private View nofityVisibleView;
	
	private TextView notifyCountView;
	
	private View loadingView;
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		loadingView = view.findViewById(R.id.loading);
		loadingView.setVisibility(View.VISIBLE);
		containerView = view.findViewById(R.id.container);
		containerView.setVisibility(View.GONE);
		avatar = (ImageView) view.findViewById(R.id.avatar);
		
		city = (TextView) view.findViewById(R.id.city);
		
		weiboBinding = view.findViewById(R.id.weibo_binding);
		weiboUnBinding = view.findViewById(R.id.weibo_unbinding);
		nickname = (TextView) view.findViewById(R.id.nickname);
		phoneContainer = view.findViewById(R.id.phone_container);
		view.findViewById(R.id.account_btn).setOnClickListener(this);
		view.findViewById(R.id.avatar_btn).setOnClickListener(this);
		view.findViewById(R.id.city_btn).setOnClickListener(this);
		view.findViewById(R.id.phone_btn).setOnClickListener(this);
		view.findViewById(R.id.pwd_btn).setOnClickListener(this);
		view.findViewById(R.id.setting_btn).setOnClickListener(this);
		view.findViewById(R.id.logout).setOnClickListener(this);
		view.findViewById(R.id.weibo_btn).setOnClickListener(this);
		view.findViewById(R.id.info_btn).setOnClickListener(this);
		nofityGoneView = view.findViewById(R.id.nofity_gone);
		nofityVisibleView = view.findViewById(R.id.notify_visible);
		notifyCountView = (TextView) view.findViewById(R.id.notify_count);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPushCount();
		String accessToken = APP.getSession().getWeiboAccessToken();
		if(accessToken == null){
			weiboBinding.setVisibility(View.GONE);
			weiboUnBinding.setVisibility(View.VISIBLE);
		}else{
			weiboBinding.setVisibility(View.VISIBLE);
			weiboUnBinding.setVisibility(View.GONE);
		}
		GetSingerInfoRequest request = new GetSingerInfoRequest(getActivity(), 
				new GetSingerInfoRequest.Params(sid));
		request.request(new SimpleRequestHandler(){
			@Override
			public void onSuccess(Object object) {
				GetSingerInfoRequest.Result result = (GetSingerInfoRequest.Result)object;
				city.setText(result.getModels().getAddress());
				ImageLoader.getInstance().displayImage(result.getModels().getSingerimgurl(), avatar,DisplayOptions.IMG.getOptions());
				nickname.setText(result.getModels().getSingername());
				
				if(result.isPhoneLogin()){
					phoneContainer.setVisibility(View.VISIBLE);
				}else{
					phoneContainer.setVisibility(View.GONE);
				}
				containerView.setVisibility(View.VISIBLE);
			}
			@Override
			public void onComplete() {
				loadingView.setVisibility(View.GONE);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.info_btn:
			InfoListActivity.open(getActivity());
			break;
		case R.id.account_btn:
			((BaseActivity) getActivity()).getNickname(nickname.getText().toString(), this);
			break;
		case R.id.avatar_btn:
			((BaseActivity) getActivity()).getPhoto(this);
			break;
		case R.id.city_btn:
			((BaseActivity) getActivity()).getCity(this);
			break;
		case R.id.phone_btn:
			((BaseActivity) getActivity()).modifyPhone(this);
			break;
		case R.id.pwd_btn:
			((BaseActivity) getActivity()).modifyPassowrd(this);
			break;
		case R.id.weibo_btn:
			((BaseActivity) getActivity()).bindingWeibo(this);
			break;
		case R.id.logout:
			onlogout();
			break;
		case R.id.setting_btn:
			SettingActivity.open(getActivity());
			break;
		default:
			break;
		}

	}

	private void onlogout() {
		APP.getSession().logout();
		((MemberFragment) getParentFragment()).initFragment(LoginFragment.newInstance());
		
	}

	@Override
	public void onGallerySelection(File file) {
		
		AliyunUtils.getInstance().upload(CropImageUtils.cropImage(file, 300, 300),"cctv11cdn",this);
		
	}

	@Override
	public void onsuccess(final UploadResult result) {
		UpdateSingerInfoRequest request = new UpdateSingerInfoRequest(getActivity(), 
				new UpdateSingerInfoRequest.Params(sid, result.getGuid(), result.getExt()));
		LoadingPopup.show(getActivity());
		request.request(new SimpleRequestHandler(){
			@Override
			public void onComplete() {
				LoadingPopup.hide(getActivity());
			}
			@Override
			public void onSuccess(Object object) {
				onResume();
			}
		});
		
	}
	
	private void getPushCount(){
		GetPushInfoRequest request = new GetPushInfoRequest(getActivity());
		request.request(new SimpleRequestHandler(){
			@Override
			public void onSuccess(Object object) {
				GetPushInfoRequest.Result result = (GetPushInfoRequest.Result) object;
				if(result.getResult() == 1000){
					int count = result.getPushinfolist().size();
					if(count == 0){
						nofityVisibleView.setVisibility(View.GONE);
						nofityGoneView.setVisibility(View.VISIBLE);
					}else{
						nofityVisibleView.setVisibility(View.VISIBLE);
						nofityGoneView.setVisibility(View.GONE);
						notifyCountView.setText(""+count);
					}
				}
			}
		});
	}

	@Override
	public void onCitySelection(String city) {
		UpdateSingerInfoRequest request = new UpdateSingerInfoRequest(getActivity(), 
				new UpdateSingerInfoRequest.Params(sid, city));
		LoadingPopup.show(getActivity());
		request.request(new SimpleRequestHandler(){
			@Override
			public void onComplete() {
				LoadingPopup.hide(getActivity());
			}
			@Override
			public void onSuccess(Object object) {
				onResume();
			}
		});
		
	}

	@Override
	public void onNicknameFill(String nickname) {
		
		UpdateSingerInfoRequest request = new UpdateSingerInfoRequest(getActivity(), 
				new UpdateSingerInfoRequest.Params(new UpdateSingerInfoRequest.Singername(sid,nickname)));
		LoadingPopup.show(getActivity());
		request.request(new SimpleRequestHandler(){
			@Override
			public void onComplete() {
				LoadingPopup.hide(getActivity());
			}
			@Override
			public void onSuccess(Object object) {
				onResume();
			}
		});
	}

	@Override
	public void onModifyPhone(String phone) {
		
		
	}

	@Override
	public void onModifyPassword(String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWeiboBinding() {
		onResume();
		
	}

}
