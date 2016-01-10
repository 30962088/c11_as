package com.cctv.xiqu.android.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;


import com.cctv.xiqu.android.APP;
import com.cctv.xiqu.android.fragment.FillInfoFragment.Account;
import com.cctv.xiqu.android.fragment.FillInfoFragment.Model;
import com.cctv.xiqu.android.fragment.FillInfoFragment.Sex;
import com.cctv.xiqu.android.fragment.network.IsHaveSingerRequest.Params;
import com.umeng.socialize.bean.Gender;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SnsAccount;
import com.umeng.socialize.bean.SocializeUser;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchUserListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

public class OauthUtils implements UMAuthListener, FetchUserListener {

	public static class Result {

		private String sid;

		private Sex sex;

		private String nickname;
		
		private String avatar;

		private SHARE_MEDIA media;

		

		public Result(String sid, Sex sex, String nickname, String avatar,
				SHARE_MEDIA media) {
			super();
			this.sid = sid;
			this.sex = sex;
			this.nickname = nickname;
			this.avatar = avatar;
			this.media = media;
		}

		public SHARE_MEDIA getMedia() {
			return media;
		}

		public String getSid() {
			return sid;
		}

		public Params toParams() {
			int type = 2;
			if (media == SHARE_MEDIA.SINA) {
				type = 1;
			}
			return new Params(sid, type);
		}

		public Model toModel() {
			int type = 2;
			if(media == SHARE_MEDIA.SINA){
				type = 1;
			}
			return new Model(new Account(sid, type) ,sex, nickname,avatar);
		}

	}

	private UMSocialService mController;

	private Context context;

	private SHARE_MEDIA media;

	public static interface OauthCallback {
		public void onSuccess(Result params);
	}

	private OauthCallback callback;

	public OauthUtils(Context context) {
		this.context = context;
		mController = UMServiceFactory.getUMSocialService("com.umeng.login",
				RequestType.SOCIAL);
	}

	public void setOauthCallback(OauthCallback callback) {
		this.callback = callback;
	}

	public void tencentOauth() {
		media = SHARE_MEDIA.TENCENT;
		oauth();
	}

	public void sinaOauth() {
//		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		media = SHARE_MEDIA.SINA;
		oauth();
	}

	public void renrenOauth() {
		media = SHARE_MEDIA.RENREN;
		oauth();

	}

	public void qqOauth() {
		media = SHARE_MEDIA.QQ;
		oauth();
	}

	private void oauth() {
		mController.doOauthVerify(context, media, this);
	}

	@Override
	public void onCancel(SHARE_MEDIA arg0) {
		// TODO Auto-generated method stub

	}

	private Result result;

	@Override
	public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
		if(arg1 == SHARE_MEDIA.SINA){
			APP.getSession().setWeiboUid(arg0.getString("uid"));
			APP.getSession().setWeiboAccessToken(arg0.getString("access_key"));
		}
		
		if(callback != null){
			mController.getUserInfo(context, this);
		}
	}

	@Override
	public void onError(SocializeException arg0, SHARE_MEDIA arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart(SHARE_MEDIA arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(int arg0, SocializeUser user) {
		SnsAccount snsAccount = null;
		if (user != null && user.mAccounts != null) {
			for (SnsAccount account : user.mAccounts) {
				if (media == SHARE_MEDIA.SINA
						&& "sina".equals(account.getPlatform())) {
					
					snsAccount = account;
					break;
				} else if (media == SHARE_MEDIA.TENCENT
						&& "tencent".equals(account.getPlatform())) {
					snsAccount = account;

					break;
				} else if (media == SHARE_MEDIA.QQ
						&& "qq".equals(account.getPlatform())) {
					snsAccount = account;
					break;
				} else if (media == SHARE_MEDIA.RENREN
						&& "renren".equals(account.getPlatform())) {
					snsAccount = account;
					break;
				}

			}
			Sex sex = null;
			Gender gender = snsAccount.getGender();
			switch (gender) {
			case FEMALE:
				sex = Sex.Female;
				break;

			case MALE:
				sex = Sex.Male;
				break;
			default:
				sex = Sex.UnKouwn;
				break;
			}
			result = new Result(snsAccount.getUsid(), sex, snsAccount.getUserName(),snsAccount.getAccountIconUrl(), media);
		}
		
		
		if (callback != null) {
			callback.onSuccess(result);
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

}
