package com.wf.irulu.module.user.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.UploadPicKeyTokenBean;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.DateFormatUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.ImageCompress;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.common.view.RoundImageView;
import com.wf.irulu.logic.listener.ServiceListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.rong.imlib.RongIMClient;

/**
 * Created by daniel on 2015/11/2.
 * 用户信息页面
 */
public class ProfileActivity extends CommonTitleBaseActivity implements ServiceListener {

    private final int CameraPermissionRequest=11;

    private RoundImageView head_riv;
    private TextView nickname_tv;
    private TextView birthday_tv;
    private TextView email_tv;
    private AlertDialog sign_out_dialog;
    private AlertDialog selector_dialog;
    private AlertDialog user_name_dialog;
    private String username = null;
    private static final int REQUEST_PIC = 0x00001;
    private final static int SELECT_PIC = 0x123;
    private File file;
    private boolean flag = true;

    @Override
    protected String setWindowTitle() {
        return getString(R.string.profile);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_profile);
    }

    @Override
    public void initView() {
        RelativeLayout head_pic_rl = (RelativeLayout) findViewById(R.id.profile_head_portrait_rl);
        RelativeLayout nickname_rl = (RelativeLayout) findViewById(R.id.profile_user_name_rl);
        RelativeLayout birthday_rl = (RelativeLayout) findViewById(R.id.profile_birthday_rl);
        RelativeLayout shipping_rl = (RelativeLayout) findViewById(R.id.profile_shipping_rl);
        Button sign_out_btn = (Button) findViewById(R.id.profile_sign_out_btn);

        head_riv = (RoundImageView) findViewById(R.id.profile_head_pic);
        nickname_tv = (TextView) findViewById(R.id.profile_user_name_tv);
        birthday_tv = (TextView) findViewById(R.id.profile_birthday_tv);
        email_tv = (TextView) findViewById(R.id.profile_email_tv);

        head_pic_rl.setOnClickListener(this);
        nickname_rl.setOnClickListener(this);
        birthday_rl.setOnClickListener(this);
        shipping_rl.setOnClickListener(this);
        sign_out_btn.setOnClickListener(this);
    }

    /**
     * 初始化数据 邮箱   昵称 头像  生日
     */
    @Override
    public void initData() {
//邮箱 昵称
        String email = CacheUtils.getString(this, "email");
        email_tv.setText(email);//设置邮箱
        nickname_tv.setText(CacheUtils.getString(this, "nickname", email.split("@")[0]));
//头像
        String headurl = CacheUtils.getString(this, "head_url");
        String nheadurl = CacheUtils.getString(this, "headjpg");


        if (CacheUtils.getLong(this, "froms") == 5) {
            IruluApplication.getInstance().getPicasso().load(nheadurl).error(R.mipmap.headpic).placeholder(R.mipmap.headpic).into(head_riv);
        } else {
            if (!TextUtils.isEmpty(headurl)) {
                IruluApplication.getInstance().getPicasso().load(new File(headurl)).error(R.mipmap.headpic).placeholder(R.mipmap.headpic).into(head_riv);

            } else {
                if (!TextUtils.isEmpty(nheadurl)) {
                    String netHeadUrl = nheadurl + "?imageView2/4/w/" + ConstantsUtils.DISPLAYW + "/h/" + ConstantsUtils.DISPLAYW + "/format/jpg/interlace/1";
                    IruluApplication.getInstance().getPicasso().load(netHeadUrl).error(R.mipmap.headpic).placeholder(R.mipmap.headpic).into(head_riv);
                } else {
                    head_riv.setImageResource(R.mipmap.headpic);
                }
            }
        }
//生日
        String birthday = CacheUtils.getString(this, "birthday", "");
        setBirthday(birthday);
    }

    /**
     * 设置生日的工具类
     */
    private void setBirthday(String birthday) {
        String[] birthdays = birthday.split("-");
        if (birthdays.length < 3) {
            return;
        }
        birthday_tv.setText(DateFormatUtils.getEngMonth(Integer.parseInt(birthdays[1]) - 1) + " " + birthdays[2] + "," + birthdays[0]);
    }

    /**
     * profile主页面的所有点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_head_portrait_rl:
                setHeadPicDialog();
                break;
            case R.id.profile_user_name_rl:
                setUserName();
                break;
            case R.id.profile_birthday_rl:
                String birthday = CacheUtils.getString(this, "birthday");
                String[] birthdays = birthday.split("-");
                if (birthdays != null && birthdays.length == 3) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, DateSet, Integer.parseInt(birthdays[0]), Integer.parseInt(birthdays[1]) - 1, Integer.parseInt(birthdays[2]));
                    datePickerDialog.show();
                } else {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, DateSet, 1980, 0, 1);
                    datePickerDialog.show();
                }
                break;
            case R.id.profile_shipping_rl:
                Intent shipping = new Intent(this, ShippingAddressActivity.class);
                startActivity(shipping);
                break;
            case R.id.profile_sign_out_btn:
                setSignOutDialog();
                break;
        }
    }

    /**
     * 此页面所有访问网络成功的 操作
     *
     * @param action    当前操作
     * @param bandObj
     * @param returnObj 返回对象
     */
    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case LOGOUT:
                PageLoadDialog.hideDialogForLoading();
                clearInfor();
                break;
            case UPDATE_NICKNAME:
                PageLoadDialog.hideDialogForLoading();
                nickname_tv.setText((String) bandObj);
                CacheUtils.setString(this, "nickname", (String) bandObj);
                break;
            case UPDATE_BIRTHDAY:
                flag = true;
                setBirthday((String) bandObj);
                CacheUtils.setString(this, "birthday", (String) bandObj);//保存的格式为1990-3-4 年-月-日
                break;
            case UPLOAD_TOKEN:
                UploadPicKeyTokenBean ub = (UploadPicKeyTokenBean) returnObj;
                String key = ub.getKey();
                String token = ub.getToken();
                controller.getServiceManager().getAasService().qiNiuUpload(this, key, token, (String) bandObj, this);
                break;
            case QINIU_UPLOAD:
                break;
        }
    }

    /**
     * 此页面访问网络失败的操作
     *
     * @param action    当前操作
     * @param returnObj 返回对象
     * @param errorCode
     */
    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        if (action == ActionTypes.UPDATE_BIRTHDAY) {
            flag = true;
        }
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            if (action == ActionTypes.UPDATE_NICKNAME || action == ActionTypes.LOGOUT) {
                PageLoadDialog.hideDialogForLoading();
                sign_out_dialog.dismiss();
            }
            showToast(R.string.no_network);
            return;
        }
        switch (action) {
            case LOGOUT:
                PageLoadDialog.hideDialogForLoading();
                clearInfor();
                break;
            case UPDATE_NICKNAME:
                PageLoadDialog.hideDialogForLoading();
                showToast((String) returnObj);
                break;
            case UPLOAD_TOKEN:
            case QINIU_UPLOAD:
                showToast((String) returnObj);
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CameraPermissionRequest){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri fileUri = getUri();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, REQUEST_PIC);
                selector_dialog.dismiss();
            }
        }
    }

    /**
     * 设置头像的对话框
     */
    private void setHeadPicDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View pView = View.inflate(this, R.layout.dialog_profile_choose_photo, null);
        adb.setView(pView);
        selector_dialog = adb.create();
        TextView take_pic_tv = (TextView) pView.findViewById(R.id.profile_choose_photo_take_tv);
        TextView choose_pic_tv = (TextView) pView.findViewById(R.id.profile_choose_photo_choose_tv);
        ImageView close_dialog_iv = (ImageView) pView.findViewById(R.id.profile_choose_photo_iv);
        close_dialog_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selector_dialog.dismiss();
            }
        });
        //拍照
        take_pic_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionChecker.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,Manifest.permission.CAMERA)){
                        new AlertDialog.Builder(ProfileActivity.this)
                                .setMessage("We need to use the camera to take a picture!")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{Manifest.permission.CAMERA}, CameraPermissionRequest);
                                    }
                                })
                                .setNegativeButton("cancle",null)
                                .create()
                                .show();
                    }
                    else{
                        ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{Manifest.permission.CAMERA}, CameraPermissionRequest);
                    }
                }
                else{

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri fileUri = getUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_PIC);
                    selector_dialog.dismiss();
                }
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri fileUri = getUri();
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                startActivityForResult(intent, REQUEST_PIC);
//                selector_dialog.dismiss();
            }
        });
        //选择照片
        choose_pic_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri fileUri = getUri();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);// Pick an item from the
                // data
                intent.setType("image/*");// 从所有图片中进行选择
                intent.putExtra("crop", "true");// 设置为裁切
                intent.putExtra("aspectX", 1);// 裁切的宽比例
                intent.putExtra("aspectY", 1);// 裁切的高比例
                intent.putExtra("outputX", 600);// 裁切的宽度
                intent.putExtra("outputY", 600);// 裁切的高度
                intent.putExtra("scale", true);// 支持缩放
                intent.putExtra("return-data", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);// 将裁切的结果输出到指定的Uri
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 裁切成的图片的格式
                intent.putExtra("noFaceDetection", true); // no face detection
                startActivityForResult(intent, SELECT_PIC);
                selector_dialog.dismiss();
            }
        });

        selector_dialog.show();
    }

    /**
     * 设置昵称的dialog
     */
    private void setUserName() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View sView = View.inflate(this, R.layout.dialog_profile_user_name, null);
        adb.setView(sView);
        user_name_dialog = adb.create();
        final EditText user_name_et = (EditText) sView.findViewById(R.id.change_name_et);
        ImageView user_name_iv = (ImageView) sView.findViewById(R.id.change_name_iv);
        Button cancle = (Button) sView.findViewById(R.id.change_name_cancel_btn);
        Button done = (Button) sView.findViewById(R.id.change_name_done_btn);
        user_name_et.setText(nickname_tv.getText());

        CharSequence text = user_name_et.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }

        user_name_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name_et.setText("");
            }
        });
        //取消修改昵称
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name_dialog.dismiss();
            }
        });
        //确认修改后的昵称
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = user_name_et.getText().toString().trim() + "";
                if (TextUtils.isEmpty(username) || username.equals(nickname_tv.getText())) {
                    return;
                }
                PageLoadDialog.showDialogForLoading(ProfileActivity.this, true, false);
                controller.getServiceManager().getAasService().updateNickName(username, ProfileActivity.this);
                user_name_dialog.dismiss();
            }
        });

        user_name_dialog.show();
    }

    /**
     * 设置登出时的对话框
     */
    private void setSignOutDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View sView = View.inflate(this, R.layout.dialog_sign_out, null);
        adb.setView(sView);
        sign_out_dialog = adb.create();
        TextView infors = (TextView) sView.findViewById(R.id.dialog_sign_out_tv);
        Button cancle = (Button) sView.findViewById(R.id.dialog_sign_out_cancle_btn);
        final Button sure = (Button) sView.findViewById(R.id.dialog_sign_out_sure_btn);
        infors.setText("You are signed in " + CacheUtils.getString(this, "nickname", CacheUtils.getString(this, "email").split("@")[0]) + ".");
        //取消登出
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_out_dialog.dismiss();
            }
        });
        //确认登出
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sure.setClickable(false);
                PageLoadDialog.showDialogForLoading(ProfileActivity.this, true, false);
                controller.getServiceManager().getAasService().logout(ProfileActivity.this);
            }
        });

        sign_out_dialog.show();
    }

    /**
     * 设置生日
     */
    DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            /**
             * 返回的值是int 类型 显示格式(Dec 01,1990)
             * 将日 和  月 做对应的转换
             */
            String dm;
            if (dayOfMonth < 10) {
                dm = "0" + dayOfMonth;
            } else {
                dm = String.valueOf(dayOfMonth);
            }
            String birthday_t = DateFormatUtils.getEngMonth(monthOfYear) + " " + dm + "," + year;
            if (birthday_tv.getText().equals(birthday_t)) {
                return;
            }
            /**
             * 生日改变请求网络
             */
            String birthday = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            if (flag) {
                flag = false;
                controller.getServiceManager().getAasService().updateBirthday(birthday, ProfileActivity.this);
            }
        }
    };

    /**
     * 把当前时间作为图片的名字 并将图片存储到sd卡中
     *
     * @return 图片存储位置的 uri
     */
    private Uri getUri() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        String format = dateFormat.format(date) + ".jpg";

        file = new File(Environment.getExternalStorageDirectory(), format);

        Uri fileUri = Uri.fromFile(file);
        return fileUri;
    }

    /**
     * 照相  选择图片后 的操作
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PIC || requestCode == SELECT_PIC) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (file != null && file.exists() && file.length() > 0) {
                        String compressAvatar = null;
                        try {
                            compressAvatar = ImageCompress.compressAvatar(file.getAbsolutePath(), file.getAbsolutePath());
                        } catch (ImageCompress.CompressException e) {
                            e.printStackTrace();
                        }
                        CacheUtils.setString(this, "head_url", compressAvatar);
                        IruluApplication.getInstance().getPicasso().load(new File(compressAvatar)).error(R.mipmap.headpic).placeholder(R.mipmap.headpic).into(head_riv);
                        // 网络获取上传头像的token
                        long uid = CacheUtils.getLong(this, "uid");
                        controller.getServiceManager().getAasService().getUpLoadImageToken(uid + "", compressAvatar, "headerface", "headpickey", ProfileActivity.this);

                    }
                    break;
                case Activity.RESULT_CANCELED:
                    // 用户取消
                    break;
                default:
                    break;
            }
        }
    }

    private void clearInfor() {
        controller.getCacheManager().setLoginUser(null);
        CacheUtils.setString(this, "token", null);
        CacheUtils.setLong(this, "uid", 0);
        CacheUtils.setString(this, "password", null);
        CacheUtils.setString(this, "lastname", null);
        CacheUtils.setString(this, "firstname", null);
        CacheUtils.setString(this, "nickname", null);
        CacheUtils.setLong(this, "registerDate", 0);
        CacheUtils.setLong(this, "froms", 0);
        CacheUtils.setLong(this, "signout", 0);
        CacheUtils.setString(this, "headjpg", null);
        CacheUtils.setString(this, "email", null);
        CacheUtils.setString(this, "sex", null);
        CacheUtils.setLong(this, "user_type", 0);
        CacheUtils.setLong(this, "outtime", 0);
        CacheUtils.setString(this, "headjpg", null);
        CacheUtils.setString(this, "head_url", null);
        CacheUtils.setString(this, "rong_cloud_token", null);
        CacheUtils.setString(this, "birthday", null);
        // 登出时清除购物车数量
        CacheUtils.setLong(this, "cartNum", 0);
        controller.postShoppongCartCountCallback(0);
        // 登出时清除WishList数量
        CacheUtils.setLong(this, "wishListNum", 0);
        controller.postWishListCountCallback(0);
        //登出时清除消息数量
        controller.postTotalUnreadCountCallback(0);
        //登出时清除订单数量
        CacheUtils.setLong(this, "unpaidOrderNum", 0);
        controller.postUnpaidOrderCountCallback(0);

        if (RongIMClient.getInstance() != null) {
            RongIMClient.getInstance().disconnect();
        }
        finish();
    }
}
