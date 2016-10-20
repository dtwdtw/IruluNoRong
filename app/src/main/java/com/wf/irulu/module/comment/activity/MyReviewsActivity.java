package com.wf.irulu.module.comment.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wf.irulu.IruluApplication;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.Product;
import com.wf.irulu.common.bean.QiNiuReturnBean;
import com.wf.irulu.common.bean.UploadPicKeyTokenBean;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.FileUtils;
import com.wf.irulu.common.utils.ImageCompress;
import com.wf.irulu.common.utils.ImageType;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.UIUtils;
import com.wf.irulu.common.view.HorizontialListView;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.logic.listener.NotifyPhotoSetChangedListener;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.comment.adapter.MyReviewsImageAdapter;
import com.wf.irulu.module.comment.listener.PhotoSelectListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by daniel on 2015/11/23.
 */
public class MyReviewsActivity extends CommonTitleBaseActivity implements ServiceListener, PhotoSelectListener, NotifyPhotoSetChangedListener {

    private RatingBar star_rb;
    private TextView star_num_tv;
    private EditText write_review_et;
    private AlertDialog dialog;
    private HorizontialListView hlv;
    private int max = 5;
    private MyReviewsImageAdapter adapter;
    private final int SELECT_IMAGE_REQUEST_CODE = 1;
    /*********
     * *图片地址
     *********/
    private ArrayList<String> list = null;
    private TextView photo_num_tv;
    private AlertDialog dialog_photo;
    private String cameraPath;                                        // 拍照后保存的地址
    private File cameraFile;                                        // 拍照后保存的File
    private StringBuilder sb = null;
    private String content;
    private float star;
    private Product product;
    private ImageView my_reviews_iv;
    private TextView my_reviews_sku;
    private TextView my_reviews_price;
    private TextView phone_name_tv;
    private String flags;

    @Override
    protected String setWindowTitle() {
        flags = getIntent().getStringExtra("flag");
        if ("order".equalsIgnoreCase(flags)) {
            return "Write a Review";
        }
        return "My Reviews";
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my_reviews);
    }

    @Override
    public void initView() {
        my_reviews_iv = (ImageView) findViewById(R.id.my_reviews_iv);
        my_reviews_sku = (TextView) findViewById(R.id.my_reviews_sku_tv);
        phone_name_tv = (TextView) findViewById(R.id.my_reviews_phone_name_tv);
        my_reviews_price = (TextView) findViewById(R.id.my_reviews_price_tv);
        star_rb = (RatingBar) findViewById(R.id.my_reviews_star_rb);
        star_num_tv = (TextView) findViewById(R.id.my_reviews_star_num_tv);
        write_review_et = (EditText) findViewById(R.id.my_reviews_et);
        photo_num_tv = (TextView) findViewById(R.id.my_reviews_photo_num_tv);
        hlv = (HorizontialListView) findViewById(R.id.my_reviews_hlv);
        Button btn = (Button) findViewById(R.id.my_reviews_btn);
        btn.setOnClickListener(this);
        commonTitleBack.setOnClickListener(this);
        controller.registNotifyPhotoSetChangedListener(this);
    }

    @Override
    public void initData() {
        product = getIntent().getParcelableExtra("product");
        IruluApplication.getInstance().getPicasso().load(product.getImage() + "?imageView2/3/w/" + UIUtils.dip2px(100) + "/h/" + UIUtils.dip2px(100)).error(R.mipmap.bg_picutre_show).into(my_reviews_iv);
        phone_name_tv.setText(product.getName());
        if ("order".equalsIgnoreCase(flags)) {
            my_reviews_sku.setText(product.getSku().toString().replace("[", "").replace("]", ""));
            my_reviews_price.setText(StringUtils.getPriceByFormat(product.getPrice()) + "×" + product.getQuantity());
        } else {
            my_reviews_price.setText(StringUtils.getPriceByFormat(product.getPrice()));
            my_reviews_sku.setVisibility(View.GONE);
        }

        star_rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v > 0.0) {
                    star_num_tv.setText(String.valueOf(v));
                } else {
                    star_num_tv.setText("");
                }
            }
        });

        setPhotoSelectDialog();
        list = new ArrayList<String>();
        list.add("default");
        adapter = new MyReviewsImageAdapter(this, list, this, max);
        hlv.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_reviews_btn:
                star = star_rb.getRating();
                content = write_review_et.getText().toString().trim();
                if (star < 1) {
                    showToast("Please choose customer rating.");
                } else if (TextUtils.isEmpty(content)) {
                    showToast("Please write your review.");
                } else {
                    /*** 如果list集合里有默认的(default),就将其删除 */
                    PageLoadDialog.showDialogForLoading(this, false, false);
                    if (list != null && list.size() > 0) {
                        if ("default".equals(list.get(list.size() - 1))) {
                            list.remove(list.size() - 1);
                        }
                        if (list.size() > 0) {
                            long uid = CacheUtils.getLong(this, "uid");
                            sb = new StringBuilder("");
                            for (int i = 0; i < list.size(); i++) {
                                controller.getServiceManager().getAasService().getUpLoadImageToken(String.valueOf(uid), list.get(i), "productcomment", "comment" + i, this);
                            }
                        } else {
                            controller.getServiceManager().getProductService().publishProductComment(product.getId(), content, null, (int) star, this);
                        }
                    } else {

                        controller.getServiceManager().getProductService().publishProductComment(product.getId(), content, null, (int) star, this);
                    }
                }
                break;
            case R.id.commontitle_iv_back:
                if (TextUtils.isEmpty(write_review_et.getText().toString().trim())) {
                    Intent intent = new Intent();
                    intent.putExtra("isNeedRefresh", false);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    setEditDialog();
                }
                break;
        }
    }

    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case PUBLISH_COMMENT:/**提交评论信息 后的 逻辑 */
                PageLoadDialog.hideDialogForLoading();
                Intent intent = new Intent();
                intent.putExtra("isNeedRefresh", true);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case UPLOAD_TOKEN:/**成功获取上传图片的 token 和 key */
                String url = (String) bandObj;
                UploadPicKeyTokenBean ub = (UploadPicKeyTokenBean) returnObj;
                if (ub != null) {
                    ub.getKey();
                    ub.getToken();
                    controller.getServiceManager().getAasService().qiNiuUpload(this, ub.getKey(), ub.getToken(), url, this);
                }
                break;
            case QINIU_UPLOAD: /**图片成功上传到七牛*/
                QiNiuReturnBean qb = (QiNiuReturnBean) returnObj;
                if (qb == null) {
                    return;
                }
                String pic = (String) bandObj;
                list.remove(pic);
                qb.getSaveKey();
                if (sb != null && sb.length() < 1) {
                    sb.append(qb.getSaveKey());
                } else {
                    sb.append("," + qb.getSaveKey());
                }
                if (list == null || list.size() == 0) {
                    controller.getServiceManager().getProductService().publishProductComment(product.getId(), content, sb.toString(), (int) star, this);
                }

                break;
        }
    }

    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        PageLoadDialog.hideDialogForLoading();
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            showToast(R.string.no_network);
            return;
        }
        switch (action) {
            case PUBLISH_COMMENT:
                    showToast((String) returnObj);
                break;
            case UPLOAD_TOKEN:
            case QINIU_UPLOAD:
                showToast("Pictures are failed to upload, please try again!");
                break;
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {
        PageLoadDialog.hideDialogForLoading();
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(write_review_et.getText().toString().trim())) {
            Intent intent = new Intent();
            intent.putExtra("isNeedRefresh", false);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            setEditDialog();
        }
    }

    /**
     * 内容不为空是显示的dialog
     */
    private void setEditDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_feedback, null);

        Button no = (Button) view.findViewById(R.id.feed_dialog_no_btn);
        Button yes = (Button) view.findViewById(R.id.feed_dialog_yes_btn);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("isNeedRefresh", false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ab.setView(view);
        dialog = ab.create();
        dialog.show();
    }

    @Override
    public void notifyPhotoSetChanged(ArrayList<String> photos) {
        photo_num_tv.setText(photos.size() + "/" + max);
        list.clear();
        list.addAll(photos);
        list.add("default");
        adapter.setNotifyDataSetChanged(list);
    }

    @Override
    public void selectPics() {
        dialog_photo.show();
    }

    @Override
    public void fullScreenShow(int location) {
        WriteCommentShowPhotosActivity.startShowPhotosActivity(this, list, "local", location);
    }

    @Override
    public void selectDefaultComment(int position) {

    }

    /**
     * 照片选择方式提示框初始化
     */
    private void setPhotoSelectDialog() {
        LayoutInflater factory = LayoutInflater.from(MyReviewsActivity.this);
        View myView = factory.inflate(R.layout.dialog_choose_pic_layout, null);// 将布局文件转换为View
        ImageView cancel_choose = (ImageView) myView.findViewById(R.id.cancel_choose);
        LinearLayout pic_layout = (LinearLayout) myView.findViewById(R.id.pic_layout);
        LinearLayout camera_layout = (LinearLayout) myView.findViewById(R.id.camera_layout);
        dialog_photo = new AlertDialog.Builder(this).setView(myView).create();
        // 对话框右上角的差的点击事件
        cancel_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_photo.dismiss();
            }
        });

        // 对话框里的相册的点击事件
        pic_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_photo.dismiss();
                try {
                    Intent intent = new Intent(MyReviewsActivity.this, LocalImageListActivity.class);// 照片
                    intent.putExtra("list", list);
                    intent.putExtra("max", max);
                    startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 对话框里的相机的点击事件
        camera_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_photo.dismiss();
                try {
                    // 调用系统的拍照功能
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraPath = FileUtils.createFilePathByType(ImageType.TYPE_CAMERA);
                    cameraFile = new File(cameraPath);
                    Uri cameraUri = Uri.fromFile(cameraFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
                    startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap image = null;
        switch (resultCode) {
            // 返回相册选择
            case ConstantsUtils.RESULT_CODE_PHOTOS:// RESULT_OK
                if (null != data) {
                    ArrayList<String> images = data.getStringArrayListExtra("photos");
                    if (images == null || images.size() == 0) {
                        return;
                    }
                    list.clear();
                    // list.addAll(list.size()-1, images);
                    list.addAll(images);
                    list.add("default");
                    adapter.setNotifyDataSetChanged(list);
                    photo_num_tv.setText(list.size() - 1 + "/" + max);
                }

                break;
            // 返回照相选择
            case RESULT_OK:// RESULT_OK
                if (cameraFile != null && cameraFile.exists() && cameraFile.length() > 0) {
                    String compressAvatar = null;
                    try {
                        compressAvatar = ImageCompress.compressPicture(cameraFile.getAbsolutePath(), cameraFile.getAbsolutePath());
                    } catch (ImageCompress.CompressException e) {
                        e.printStackTrace();
                    }
                    refreshListData(compressAvatar);
                }
                break;
            case 12:
                ArrayList<String> images = data.getStringArrayListExtra("image");
                this.notifyPhotoSetChanged(images);
                break;
            default:
                break;
        }
    }

    /**
     * @param image 图片地址
     */
    private void refreshListData(String image) {
        if (image == null) {
            return;
        }
        if (list.size() <= max) {
            list.add(list.size() - 1, image);
            adapter.setNotifyDataSetChanged(list);
        }
        photo_num_tv.setText(list.size() - 1 + "/" + max);
    }

    public static void startMyReviewsActivity(Activity activity, Product product, String flag) {
        Intent intent = new Intent(activity, MyReviewsActivity.class);
        intent.putExtra("product", product);
        intent.putExtra("flag", flag);
        activity.startActivity(intent);
    }

    public static void startMyReviewsActivityOnResult(Activity activity, Product product, String flag, int requestCode) {
        Intent intent = new Intent(activity, MyReviewsActivity.class);
        intent.putExtra("product", product);
        intent.putExtra("flag", flag);
        activity.startActivityForResult(intent, requestCode);
    }
}