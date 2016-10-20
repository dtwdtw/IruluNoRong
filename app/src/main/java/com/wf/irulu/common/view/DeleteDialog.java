package com.wf.irulu.common.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.wf.irulu.R;
import com.wf.irulu.logic.listener.DeleteDialogListener;

/**
 * 
 * @描述: 删除操作对话框
 *
 * @项目名: irulu
 * @包名:com.wf.irulu.common.view
 * @类名:UIHelper
 * @作者: 左西杰
 * @创建时间:2015-7-29 下午3:29:54
 *
 */
public class DeleteDialog {
	
	/** 加载数据对话框 */
	private static Dialog mLoadingDialog;
	
	
	/**
	 * 显示加载对话框
	 * @param context 上下文
	 * @param msg 对话框显示内容
	 * @param cancelable 对话框是否可以取消
	 */
	public static void showDeleteDialog(Context context, String msg,final DeleteDialogListener listener) {
		final AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
		alterDialog.setMessage(msg);
        alterDialog.setCancelable(true);

        alterDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	listener.deleteDialog();
            }
        });
        alterDialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alterDialog.show();
	}
	
	public static void showDeleteDialog(Context context,String title,String msg,final DeleteDialogListener listener) {
		final AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
		alterDialog.setMessage(msg);
		alterDialog.setCancelable(true);
		
		alterDialog.setPositiveButton(title, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.deleteDialog();
			}
		});
		alterDialog.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alterDialog.show();
	}
	
}
