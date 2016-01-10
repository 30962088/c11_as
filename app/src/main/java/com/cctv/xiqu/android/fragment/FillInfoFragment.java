package com.cctv.xiqu.android.fragment;

import java.io.File;
import java.io.Serializable;

import org.apache.http.Header;

import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.BaseActivity;
import com.cctv.xiqu.android.APP.DisplayOptions;
import com.cctv.xiqu.android.BaseActivity.OnCitySelectionListener;
import com.cctv.xiqu.android.BaseActivity.OnGallerySelectionListener;
import com.cctv.xiqu.android.fragment.network.BaseClient;
import com.cctv.xiqu.android.fragment.network.BaseClient.RequestHandler;
import com.cctv.xiqu.android.fragment.network.IsHaveSingerName;
import com.cctv.xiqu.android.fragment.network.SetSingerRequest;
import com.cctv.xiqu.android.fragment.network.SetSingerUserRequest;
import com.cctv.xiqu.android.utils.AliyunUtils;
import com.cctv.xiqu.android.utils.CropImageUtils;
import com.cctv.xiqu.android.utils.Dirctionary;
import com.cctv.xiqu.android.utils.LoadingPopup;
import com.cctv.xiqu.android.utils.AliyunUtils.UploadListener;
import com.cctv.xiqu.android.utils.AliyunUtils.UploadResult;
import com.cctv.xiqu.android.widget.PhotoSelectPopupWindow;
import com.cctv.xiqu.android.widget.PhotoSelectPopupWindow.OnItemClickListener;
import com.mengle.lib.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.cctv.xiqu.android.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FillInfoFragment extends BaseFragment implements OnClickListener,OnCitySelectionListener,
		OnGallerySelectionListener,UploadListener {

	public enum Sex {
		Female("female"), Male("male"), UnKouwn("unkouwn");
		private String code;
		private Sex(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
	}
	
	public static class PhoneAccount implements Serializable{
		private String phone;
		private String password;
		public PhoneAccount(String phone, String password) {
			super();
			this.phone = phone;
			this.password = password;
		}

		
		
	}
	
	public static class Account implements Serializable{
		private String wbqqid;
		private int type;
		public Account(String wbqqid, int type) {
			super();
			this.wbqqid = wbqqid;
			this.type = type;
		}
		
	}

	public static class Model implements Serializable {
		private Account account;
		private PhoneAccount phoneAccount;
		private Sex sex=Sex.Male;
		private String nickname="";
		private String avatar="";

		public Model(Account account, Sex sex, String nickname, String avatar) {
			super();
			this.account = account;
			this.sex = sex;
			this.nickname = nickname;
			this.avatar = avatar;
		}

		public Model(PhoneAccount phoneAccount) {
			super();
			this.phoneAccount = phoneAccount;
		}
		
		

	}

	public static FillInfoFragment newInstance(Model model) {
		FillInfoFragment fragment = new FillInfoFragment();
		Bundle args = new Bundle();
		args.putSerializable("model", model);
		fragment.setArguments(args);
		return fragment;
	}

	private Model model;

	private EditText nicknameEditText;

	private RadioButton maleRadioButton;

	private RadioButton femaleRadioButton;

	private RadioGroup sexRadioGroup;

	private TextView cityTextView;

	private ImageView avatarImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		model = (Model) getArguments().getSerializable("model");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fill_info_layout, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		avatarImageView = (ImageView) view.findViewById(R.id.avatar);
		maleRadioButton = (RadioButton) view.findViewById(R.id.male);
		maleRadioButton.setTag(Sex.Male);
		femaleRadioButton = (RadioButton) view.findViewById(R.id.female);
		femaleRadioButton.setTag(Sex.Female);
		sexRadioGroup = (RadioGroup) view.findViewById(R.id.sexGroup);
		nicknameEditText = (EditText) view.findViewById(R.id.nickname);
		cityTextView = (TextView) view.findViewById(R.id.city);
		view.findViewById(R.id.avatar_setting).setOnClickListener(this);
		view.findViewById(R.id.cityBtn).setOnClickListener(this);
		view.findViewById(R.id.cancel).setOnClickListener(this);
		view.findViewById(R.id.submit).setOnClickListener(this);
		init();

	}

	private void init() {
		/*ImageLoader.getInstance().displayImage(model.avatar, avatarImageView,
				DisplayOptions.IMG.getOptions());*/
		nicknameEditText.setText(model.nickname);
		switch (model.sex) {
		case Female:
			femaleRadioButton.setChecked(true);
			break;
		case Male:
			maleRadioButton.setChecked(true);
			break;
		default:
			break;
		}
	}

	// private static final int ACTION_REQUEST_SELECTION = 3;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.avatar_setting:
			((BaseActivity) getActivity()).getPhoto(this);
			break;
		case R.id.cityBtn:
			((BaseActivity) getActivity()).getCity(this);
			break;
		case R.id.cancel:
			((MemberFragment)getParentFragment()).backFragment();
			break;
		case R.id.submit:
			onsubmit();
			break;
		default:
			break;
		}

	}
	
	private void onsubmit() {
		if(result == null){
			Utils.tip(getActivity(), "请选择头像");
			return;
		}
		final String nickname = nicknameEditText.getText().toString();
		if(TextUtils.isEmpty(nickname)){
			Utils.tip(getActivity(), "请填写昵称");
			return;
		}
		LoadingPopup.show(getActivity());
		IsHaveSingerName haveSingerName = new IsHaveSingerName(getActivity(), nickname);
		haveSingerName.request(new RequestHandler() {
			
			@Override
			public void onSuccess(Object object) {
				String city = "";
				if(cityTextView.getTag() != null){
					city = cityTextView.getTag().toString();
				}
				Sex sex = (Sex)sexRadioGroup.findViewById(sexRadioGroup.getCheckedRadioButtonId()).getTag();
				
				BaseClient request = null;
				
				if(model.account != null){
					request = new SetSingerRequest(getActivity(), 
							new SetSingerRequest.Params(model.account.wbqqid, model.account.type, nickname, sex, result.getGuid(), result.getExt(), city));
				}else{
					request = new SetSingerUserRequest(getActivity(), 
							new SetSingerUserRequest.Params(nickname, sex.getCode(), result.getGuid(), result.getExt(), city, model.phoneAccount.phone,model.phoneAccount.password));
				}

				request.request(new BaseClient.SimpleRequestHandler(){
					@Override
					public void onSuccess(Object object) {
						
						((MemberFragment) getParentFragment()).initFragment(UserSettingFragment.newInstance(APP.getSession().getSid()));
					}
					@Override
					public void onComplete() {
						LoadingPopup.hide(getActivity());
					}
				});
				
			}
			
			@Override
			public void onError(int error, String msg) {
				if(error == 1006){
					Utils.tip(getActivity(),"昵称重复");
				}
				
			}
			
			@Override
			public void onComplete() {
				LoadingPopup.hide(getActivity());
				
			}
		});
		
		
		
	}

	@Override
	public void onGallerySelection(File file) {
		LoadingPopup.show(getActivity());
		AliyunUtils.getInstance().upload(CropImageUtils.cropImage(file, 300, 300),"cctv11cdn",this);
	}

	@Override
	public void onCitySelection(String city) {
		cityTextView.setText(city);
		cityTextView.setTag(city);
		
	}
	
	private UploadResult result;

	@Override
	public void onsuccess(UploadResult result) {
		this.result = result;
		LoadingPopup.hide(getActivity());
		ImageLoader.getInstance().displayImage(result.getUrl(), avatarImageView,
				DisplayOptions.IMG.getOptions());
	}

}
