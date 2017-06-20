package com.water.roll.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jovision.xunwei.junior.lib.util.MD5Util;
import com.jovision.xunwei.junior.lib.util.ToastUtils;
import com.jovision.xunwei.junior.lib.view.ClearEditText;
import com.water.roll.BaseActivity;
import com.water.roll.R;
import com.water.roll.request.API;
import com.water.roll.request.res.LoginResult;
import com.water.roll.util.Contants;
import com.water.roll.util.LoginUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

	private static final String TAG = LoginActivity.class.getSimpleName();

	private ClearEditText mUsername;
	private ClearEditText mPassword;
	private Button mConfirmBtn;
	private TextView mRegister;
	private TextView mForgetPasswd;
	private View mRootView;
	private String mMobile;
	private Button mDelPwdBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		initView();
	}

	private void initView(){
		getTitleBar().setTitle(R.string.btn_login);
		mUsername = $(R.id.login_username);
		mPassword = $(R.id.login_password);
		mConfirmBtn = $(R.id.login_confirm);
		mConfirmBtn.setOnClickListener(this);
		mRegister = $(R.id.login_register);
		mRegister.setOnClickListener(this);
		mRegister.setVisibility(View.GONE);
		mForgetPasswd = $(R.id.login_forget_password);
		mForgetPasswd.setOnClickListener(this);
		mRootView = $(R.id.root_layout_login);
		mRootView.setOnClickListener(this);
		mDelPwdBtn= $(R.id.login_del_pwd);
		mDelPwdBtn.setOnClickListener(this);

		Intent intent = getIntent();
		if(intent != null){
			String mobile = intent.getStringExtra(Contants.BundleKey.MOBILE);
			if(!TextUtils.isEmpty(mobile)){
				mUsername.setText(mobile);
				mPassword.requestFocus();
			}
		}

	}


	@Override
	public void onClick(View v) {
		if(v == mConfirmBtn){
			checkInput();
//			jump(TabMainActivity.class, false , new Bundle());
		}else if(v == mRootView){
			hideKeyboard();
		}else if(v.getId() == R.id.title_bar_left){
			this.finish();
		}else if(v == mDelPwdBtn){
			mPassword.setText("");
		}
	}



	protected void onDestroy() {

		super.onDestroy();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after){

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count){

	}

	@Override
	public void afterTextChanged(Editable s){
		mPassword.setText("");
	}

	private void checkInput(){
		String username = mUsername.getText().toString();
		String password = mPassword.getText().toString();
		if(TextUtils.isEmpty(username)){
			ToastUtils.show(this, R.string.toast_mobile_empty);
			return;
		}
//		if(!ValidateUtil.isValidMobile(username)){
//			ToastUtils.show(this, R.string.toast_mobile_format_error);
//			return;
//		}
		if(TextUtils.isEmpty(password)){
			ToastUtils.show(this, R.string.input_hint_pwd_empty);
			return;
		}
		if(password.length() <6 && password.length() > 20){
			ToastUtils.show(this, R.string.toast_pwd_length_error);
			return;
		}
		doLogin(username, createPwd(password));
	}

	private void doLogin(final String username, final String passwordEncoded){
		openLoadingDialog(null, true);
		LoginUtil.doLogin(this, API.LOGIN_URL, username, passwordEncoded, new LoginUtil.OnLoginListener() {
			@Override
			public void onLoginSuccess(String username, String passwordEncoded, LoginResult result) {
				dismissLoadingDialog();
				jump(TabMainActivity.class, true, new Bundle());
			}

			@Override
			public void onLoginFailure(int errcode, String ermsg) {
				dismissLoadingDialog();
				ToastUtils.show(LoginActivity.this, ermsg);
			}
		});
	}
	/**
	 * 要对用户输入的密码在客户端做一次MD5再传给服务端校验
	 * */
	private String createPwd(String password) {
		return MD5Util.string2MD5(Contants.PASSWORD_SALT + password);
	}

}
