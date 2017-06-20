package com.water.roll.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.EditText;

import com.jovision.xunwei.junior.lib.util.ToastUtils;
import com.water.roll.BaseActivity;
import com.water.roll.R;
import com.water.roll.request.CubeRequest;
import com.water.roll.request.Request;

import in.srain.cube.request.RequestAble;


public class DialogManager {
	
	private BaseActivity mContext;
	private Dialog mConfirmDialog;
	private Dialog netErrorDialog;
	private Dialog mRetryDialog;
    private Dialog mETDialog;
	protected ProgressDialog mLoadingDialog;
	
	public DialogManager(BaseActivity context){
		this.mContext = context;
	}
	
	public void destroy(){
		dismissConfirmDialog();
		dismissLoadingDialog();
		dismissNetErrorDialog();
	}
    

	
	/**
	 * 加载中提示
	 * */
	public void openLoadingDialog(String msg, boolean cancel) {
		if(mLoadingDialog != null && mLoadingDialog.isShowing()){
			return;
		}
        try {
            if (mLoadingDialog == null) {
            	mLoadingDialog = new ProgressDialog(mContext);
            }
            if (TextUtils.isEmpty(msg)) {
                msg = mContext.getResources().getString(R.string.str_dialog_tips_waiting);
            }
            mLoadingDialog.setMessage(msg);
            mLoadingDialog.setCancelable(cancel);
            mLoadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    public void dismissLoadingDialog() {
        if (null != mLoadingDialog && !mContext.isFinishing()) {
        	mLoadingDialog.dismiss();
        	mLoadingDialog = null;
        }
    }
    

    public interface OnConfirmDialogClickListener {
        public void onConfirmClicked(DialogInterface dialog, int which);
        public void onCancelClicked(DialogInterface dialog, int which);
    }

    public interface OnETConfirmDialogClickListener {
        public void onConfirmClicked(DialogInterface dialog, int which, String tv);
        public void onCancelClicked(DialogInterface dialog, int which, String tv);
    }
    
    /**
     * 没有网络提示 打开设置网络界面
     */
    public void openNetErrorDialog() {
        if (null != netErrorDialog && netErrorDialog.isShowing()) {
            return;
        }
        try {
            // 提示对话框
            Resources resource = mContext.getResources();
            netErrorDialog = buildDialog(resource.getString(R.string.str_tips),
                    resource.getString(R.string.str_error_network),
                    resource.getString(R.string.str_setting),
                    resource.getString(R.string.str_cancel), true,
                    new OnConfirmDialogClickListener() {
                        @Override
                        public void onConfirmClicked(DialogInterface dialog, int which) {
                            if (android.os.Build.VERSION.SDK_INT > 10) {
                            	mContext.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                            } else {
                            	mContext.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }
                        @Override
                        public void onCancelClicked(DialogInterface dialog, int which) {
                        }
                    });
            netErrorDialog.show();
        } catch (ActivityNotFoundException e) {
            ToastUtils.show(mContext, R.string.str_error_network);
            e.printStackTrace();
        }
    }
    
    public void dismissNetErrorDialog(){
    	if (null != netErrorDialog && !mContext.isFinishing()) {
    		netErrorDialog.dismiss();
    		netErrorDialog = null;
        }
    }
    
	/**
     * 通用的确认对话框
     */
    public void openConfirmDialog(String tiltle, String msg, String positive, String negative,
                                  boolean cancel, final OnConfirmDialogClickListener listener) {
        if (null != mConfirmDialog && mConfirmDialog.isShowing()) {
            return;
        }
        mConfirmDialog = buildDialog(tiltle, msg, positive, negative, cancel, listener);
//        mConfirmDialog.setOnDismissListener(new OnDismissListener(){
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				if(listener != null){
//					listener.onCancelClicked(dialog, 0);
//				}
//			}
//        });
        mConfirmDialog.show();
    }

    public void dismissConfirmDialog() {
        if (null != mConfirmDialog && !mContext.isFinishing()) {
            mConfirmDialog.dismiss();
            mConfirmDialog = null;
        }
    }
    
    /**
     * 服务端异常，打开重试Dialog
     * 
     * @param msg
     * @param cancel Dialog能否取消
     */
    public void openRetryDialog(final RequestAble requestAble, final CubeRequest request,
                                String msg, final boolean cancel) {
        if (null != mRetryDialog && mRetryDialog.isShowing()) {
            return;
        }
        mRetryDialog = buildDialog(null, msg, "重试", null, cancel,
                new OnConfirmDialogClickListener() {
                    @Override
                    public void onConfirmClicked(DialogInterface dialog, int which) {
                        Request.redo(requestAble, request);
                    }

                    @Override
                    public void onCancelClicked(DialogInterface dialog, int which) {
                        dismissRetryDialog();
                    }
                });
        mRetryDialog.show();
    }

    public void dismissRetryDialog() {
        if (null != mRetryDialog) {
            mRetryDialog.dismiss();
            mRetryDialog = null;
        }
    }


    /**
     * 通用的确认对话框
     */
    public void openETDialog(String tiltle, String msg, String positive, String negative,
                             boolean cancel, final OnETConfirmDialogClickListener listener) {
        if (null != mETDialog && mETDialog.isShowing()) {
            return;
        }
        mETDialog = buildETDialog(tiltle, msg, positive, negative, cancel, listener);
//        mConfirmDialog.setOnDismissListener(new OnDismissListener(){
//			@Override
//			public void onDismiss(DialogInterface dialog) {
//				if(listener != null){
//					listener.onCancelClicked(dialog, 0);
//				}
//			}
//        });
        mETDialog.show();
    }

    public void dismissETDialog() {
        if (null != mETDialog && !mContext.isFinishing()) {
            mETDialog.dismiss();
            mETDialog = null;
        }
    }
    
    private Dialog buildDialog(String title, String msg, String positive, String negative,
                               boolean cancel, final OnConfirmDialogClickListener listener) {
        Builder builder = new Builder(mContext);
        // title
        if (title != null && title.length() > 0) {
            builder.setTitle(title);
        }
        // message
        builder.setMessage(msg);
        // positive
        if (positive == null || positive.length() < 0) {
            positive = "确定";
        }
        builder.setPositiveButton(positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onConfirmClicked(dialog, which);
                        }
                    }
                });
        // negative
        if (negative == null || negative.length() <= 0) {
            negative = "取消";
        }
        builder.setNegativeButton(negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onCancelClicked(dialog, which);
                        }
                    }
                });
        Dialog dialog = builder.create();
        // cancelable
        dialog.setCancelable(cancel);
        return dialog;
    }

    /**
     * 带输入框的dialog
     * @param title
     * @param msg
     * @param positive
     * @param negative
     * @param cancel
     * @param listener
     * @return
     */
    private Dialog buildETDialog(String title, String msg, String positive, String negative,
                                 boolean cancel, final OnETConfirmDialogClickListener listener) {

        final EditText et = new EditText(mContext);

        Builder builder = new Builder(mContext);
        // title
        if (title != null && title.length() > 0) {
            builder.setTitle(title);
        }
        // message
        builder.setMessage(msg);
        // positive
        if (positive == null || positive.length() < 0) {
            positive = "确定";
        }
        builder.setView(et);

        builder.setPositiveButton(positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onConfirmClicked(dialog, which, et.getText().toString());
                        }
                    }
                });
        // negative
        if (negative == null || negative.length() <= 0) {
            negative = "取消";
        }
        builder.setNegativeButton(negative,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null) {
                            listener.onCancelClicked(dialog, which, et.getText().toString());
                        }
                    }
                });
        Dialog dialog = builder.create();
        // cancelable
        dialog.setCancelable(cancel);
        return dialog;
    }
}
