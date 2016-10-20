package com.wf.irulu.module.aas.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.digits.sdk.android.DigitsOAuthSigning;
import com.digits.sdk.android.DigitsUser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.OAuthSigning;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.umeng.analytics.MobclickAgent;
import com.wf.irulu.R;
import com.wf.irulu.common.base.CommonTitleBaseActivity;
import com.wf.irulu.common.bean.LoginUser;
import com.wf.irulu.common.bean.ProductCart;
import com.wf.irulu.common.bean.ShoppingCart;
import com.wf.irulu.common.utils.CacheUtils;
import com.wf.irulu.common.utils.ConstantsUtils;
import com.wf.irulu.common.utils.CustomerEvent;
import com.wf.irulu.common.utils.ErrorCodeUtils;
import com.wf.irulu.common.utils.ILog;
import com.wf.irulu.common.utils.MD5Util;
import com.wf.irulu.common.utils.StringUtils;
import com.wf.irulu.common.utils.TwitterUtils;
import com.wf.irulu.common.view.PageLoadDialog;
import com.wf.irulu.component.rongcloud.Listener.ReceiveMessageListener;
import com.wf.irulu.framework.HomeActivity;
import com.wf.irulu.logic.listener.ServiceListener;
import com.wf.irulu.module.message.activity.MessageActivity;
import com.wf.irulu.module.mycoupon.activity.MyCouponActivity;
import com.wf.irulu.module.order.activity.OrdersActivity;
import com.wf.irulu.module.product.activity.WishActivity;
import com.wf.irulu.module.setting.activity.ChangePasswordActivity;
import com.wf.irulu.module.user.activity.ProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import io.fabric.sdk.android.Fabric;
import io.rong.imlib.RongIMClient;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by daniel on 2015/10/28.
 * 登陆页面
 */
public class LoginActivity extends CommonTitleBaseActivity implements ServiceListener {
    private final String TAG = getClass().getCanonicalName();
    private TwitterAuthClient mTwitterClient;
    private EditText mlogin_email_et;
    private EditText mlogin_password_et;
    private ImageView mshow_hidden_iv;
    private Button mlogin_btn;
    private TextView maccount_tv;
    private TextView mforgot_password_tv;
    private FrameLayout mfacebook_iv;
    private FrameLayout mtwitter_iv;
    private boolean isOpened = false;
    private CallbackManager callbackManager;
    public RongIMClient mRongIMClient;
    //    private BindUserPlatform mLastLoginWith = BindUserPlatform.UNKNOWN;

    private int flags = 0;
    private AlertDialog alertDialog;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected String setWindowTitle() {
        return getString(R.string.sign);
    }

    @Override
    public void initView() {
        commonTitleBack.setVisibility(View.GONE);
        commonTitleLeftText.setVisibility(View.VISIBLE);
        mlogin_email_et = (EditText) findViewById(R.id.login_email_et);
        mlogin_password_et = (EditText) findViewById(R.id.login_password_et);
        mshow_hidden_iv = (ImageView) findViewById(R.id.login_password_show_iv);
        mlogin_btn = (Button) findViewById(R.id.login_btn);
        maccount_tv = (TextView) findViewById(R.id.login_account_tv);
        mforgot_password_tv = (TextView) findViewById(R.id.login_forgot_password_tv);
        mfacebook_iv = (FrameLayout) findViewById(R.id.facebook);
        mtwitter_iv = (FrameLayout) findViewById(R.id.twitter);
        mshow_hidden_iv.setOnClickListener(this);
        mlogin_btn.setOnClickListener(this);
        maccount_tv.setOnClickListener(this);
        mforgot_password_tv.setOnClickListener(this);
        mfacebook_iv.setOnClickListener(this);
        mtwitter_iv.setOnClickListener(this);
        commonTitleLeftText.setOnClickListener(this);
        MobclickAgent.onEvent(this, String.valueOf(CustomerEvent.filter_enter_login_view), ConstantsUtils.mVersionAnalystics);
    }

    @Override
    public void initData() {
        mlogin_email_et.setText(CacheUtils.getString(this, "returnemail", ""));
        initTwitter();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final String[] email = new String[1];
                        final String[] gender = new String[1]; // 01/31/1980 format
                        final String[] link = new String[1];
                        final String[] locale = new String[1];
                        final String[] name = new String[1];
                        final String[] weblink = new String[1];
                        // App code
                        AccessToken token = loginResult.getAccessToken();

                        final String openId = token.getUserId();

                        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    email[0] = object.getString("email");
                                    gender[0] = object.getString("gender");
                                    locale[0] = object.getString("locale");
                                    name[0] = object.getString("name");
                                    weblink[0] = object.getString("link");
                                    link[0] = "https://graph.facebook.com/"+object.getString("id")+"/picture?width=150&height=150";
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //联网请求登录
                                controller.getServiceManager().getAasService().login(email[0], gender[0], link[0], locale[0], name[0],weblink[0], openId, 2, LoginActivity.this);
                            }
                        });

                        PageLoadDialog.showDialogForLoading(LoginActivity.this, false, false);
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday,link,website,locale,about,hometown,location");
                        request.setParameters(parameters);
                        request.executeAsync();

                       }

                    @Override
                    public void onCancel() {
                        // App code
                        PageLoadDialog.hideDialogForLoading();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        PageLoadDialog.hideDialogForLoading();
                        showToast(exception.getMessage());
                    }
                });
    }

    private void initTwitter() {

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TwitterUtils.TWITTER_API_KEY, TwitterUtils.TWITTER_API_SECRET);
        Fabric.with(this, new TwitterCore(authConfig));
        mTwitterClient = new TwitterAuthClient();
    }

    @Override
    public void onClick(View view) {
        /**
         * 显示与隐藏密码
         */
        switch (view.getId()) {
            case R.id.login_password_show_iv:

                if (isOpened) {
                    // 显示密码
                    mlogin_password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mshow_hidden_iv.setImageResource(R.mipmap.login_extend);
                    isOpened = false;
                } else {
                    // 隐藏密码
                    mlogin_password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mshow_hidden_iv.setImageResource(R.mipmap.login_close);
                    isOpened = true;
                }

                break;
            /**
             * 提交登录信息
             */
            case R.id.login_btn:
                String email = mlogin_email_et.getText().toString().trim();
                String password = mlogin_password_et.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    showToast(getString(R.string.empty_email_prompt));
                    return;
                } else if (!StringUtils.isEmail(email)) {
                    showToast(getString(R.string.email_style_wrong));
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    showToast(getString(R.string.empty_password));
                    return;
                } else if (password.length() < 6) {
                    showToast(getString(R.string.password_least));
                    return;
                } else {
                    PageLoadDialog.showDialogForLoading(this, false, false);
                    String _encode_password = MD5Util.md5(password + ":alllandnet");
                    //联网请求登录
                    controller.getServiceManager().getAasService().login(email, _encode_password, null, 1, this);
                }
                break;
            case R.id.login_account_tv:
                Intent account = new Intent(this, AccountActivity.class);
                startActivity(account);
                overridePendingTransition(R.anim.acitvity_open, 0);
                break;
            case R.id.login_forgot_password_tv:
                Intent forgotPassword = new Intent(this, ForgotPasswordActivity.class);
                startActivity(forgotPassword);
                break;
            case R.id.facebook:
                flags = 0;
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "user_website", "user_about_me", "email", "user_hometown", "user_location", "user_birthday"));
                break;
            case R.id.twitter:
                flags = 1;
                loginTwitter();
                break;
            case R.id.commontitle_tv_left_text:
                if ("RefrenceActivity".equals(getIntent().getStringExtra("flag"))) {
                    Intent i = new Intent(this, HomeActivity.class);
                    startActivity(i);
                }
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 登录twitter
     */
    private void loginTwitter() {

        mTwitterClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                TwitterSession session = twitterSessionResult.data;
                final String openid = "" + session.getUserId();
                String token = session.getAuthToken().token;
                String name = session.getUserName();

                PageLoadDialog.showDialogForLoading(LoginActivity.this, false, false);
                //联网请求登录


//                twitterAuthClient.requestEmail(session, new Callback<String>() {
//                    @Override
//                    public void success(Result<String> result) {
//                        result.data
//                    }
//
//                    @Override
//                    public void failure(TwitterException e) {
//
//                    }
//                });


// Can also use Twitter directly: Twitter.getApiClient()
                class MyTwitterApiClient extends TwitterApiClient {
                    public MyTwitterApiClient(TwitterSession session) {
                        super(session);
                    }

                    /**
                     * Provide CustomService with defined endpoints
                     */
                    public CustomService getCustomService() {
                        return getService(CustomService.class);
                    }
                }

                MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(session);
                myTwitterApiClient.getCustomService().show(session.getId(), new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        String screenname = result.data.screenName;
                        String name = result.data.name;
                        String description = result.data.description;
                        String profileImageUrl = result.data.profileImageUrl;
                        String url = result.data.url;
                        String location = result.data.location;
                        String email = result.data.email;
                        controller.getServiceManager().getAasService().login(email, profileImageUrl, location, name, screenname, description, url, openid, 3, LoginActivity.this);
                    }

                    @Override
                    public void failure(TwitterException e) {

                    }
                });

// example users/show service endpoint


                controller.getServiceManager().getAasService().login(null, null, openid, 3, LoginActivity.this);
            }

            @Override
            public void failure(com.twitter.sdk.android.core.TwitterException e) {
                e.printStackTrace();
//                msg("twitter登录挂了");
                PageLoadDialog.hideDialogForLoading();
                showToast(e.getMessage());
            }
        });

    }

    interface CustomService {
        @GET("/1.1/users/show.json")
        void show(@Query("user_id") long id, Callback<User> cb);
    }

    /**
     * 成功登陆
     *
     * @param action    当前操作
     * @param bandObj
     * @param returnObj 返回对象
     */
    @Override
    public void serviceSuccess(ActionTypes action, Object bandObj, Object returnObj) {
        switch (action) {
            case LOGIN:
                LoginUser user = (LoginUser) returnObj;
                String rongCloudToken = user.getRongCloudToken();//融云token
                rongConnect(rongCloudToken);//融云连接
                CacheUtils.setLong(this, "uid", user.getUid());
                // email
                CacheUtils.setString(this, "email", user.getEmail());
                // lastname
                CacheUtils.setString(this, "lastname", user.getLastname());
                // firstname
                CacheUtils.setString(this, "firstname", user.getFirstname());
                // nickname

                CacheUtils.setString(this, "returnemail", user.getEmail());

                CacheUtils.setString(this, "nickname", user.getNickname());
                // registerDate
                CacheUtils.setLong(this, "registerDate", user.getRegisterDate());
                // froms
                CacheUtils.setLong(this, "froms", user.getFroms());
                // headjpg
                CacheUtils.setString(this, "headjpg", user.getHeadjpg());
                // sex
                CacheUtils.setString(this, "sex", user.getSex());
                CacheUtils.setString(this, "token", user.getToken());
                // 保存登陆用户信息
                CacheUtils.setLong(this, "user_type", (int) bandObj);
                // rongCloudToke
                CacheUtils.setString(this, "rong_cloud_token", rongCloudToken);
                // birthday
                CacheUtils.setString(this, "birthday", user.getBirthday());
                //联网请求获取购物车信息
                controller.getServiceManager().getShoppingService().getCartInfo(this);
                //联网请求获取订单信息
                controller.getServiceManager().getShoppingService().getOrders(1, 1, 2, this);
                Log.v("hellolove",user.getHeadjpg());
                break;
            case GET_CART_INFO:
                PageLoadDialog.hideDialogForLoading();
                ShoppingCart shoppingCart = (ShoppingCart) returnObj;
                ArrayList<ProductCart> productList = shoppingCart.shoppingList.getProductList();

                int quantity;// 购买数量
                int quantityTem = 0;
                int shoppingSize = productList.size();
                for (int i = 0; i < shoppingSize; i++) {
                    quantity = productList.get(i).getQuantity();
                    quantity = quantityTem + quantity;
                    quantityTem = quantity;
                }

                CacheUtils.setLong(this, "cartNum", quantityTem);// 购物车数量设置sp中就是为了在其他页面初始化的时候调用
                controller.postShoppongCartCountCallback(quantityTem);

                String shoppingCartActivityTag = getIntent().getStringExtra("shoppingCartActivityTag");
                if (shoppingCartActivityTag != null) {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("productList", productList);
                    setResult(RESULT_OK, intent);
                }

                String flag = getIntent().getStringExtra("flag");

                if (!TextUtils.isEmpty(flag)) {
                    switch (flag) {
                        case "profile":
                            Intent profile = new Intent(this, ProfileActivity.class);
                            startActivity(profile);
                            break;
                        case "wishlist":
                            WishActivity.startWishActivity(this);
                            break;
                        case "order":
                            Intent intentOrder = new Intent(this, OrdersActivity.class);
                            startActivity(intentOrder);
                            break;
                        case "coupon":
                            Intent intentCoupon = new Intent(this, MyCouponActivity.class);
                            startActivity(intentCoupon);
                            break;
                        case "massage":
                            Intent intentMessage = new Intent(this, MessageActivity.class);
                            startActivity(intentMessage);
                            break;
                        case "changepassword":
                            Intent change = new Intent(this, ChangePasswordActivity.class);
                            startActivity(change);
                            break;
                        case "RefrenceActivity":
                            Intent ra = new Intent(this, HomeActivity.class);
                            startActivity(ra);
                            break;
                        default:
                            break;
                    }
                }
                finish();


                break;
            case ORDER_LIST://在getOrders方法里做更新未支付的订单数量了
//                OrderInfo info = (OrderInfo) returnObj;
//                int count = info.getNotpay().getCount();
//                controller.(count);
                break;
            case RESET_RONG_TOKEN:
                String resetrongtoken = returnObj.toString();
                rongConnect(resetrongtoken);
                break;
            default:
                break;
        }
    }

    /**
     * 登录失败
     *
     * @param action    当前操作
     * @param returnObj 返回对象
     * @param errorCode
     */
    @Override
    public void serviceFailure(ActionTypes action, Object returnObj, int errorCode) {
        PageLoadDialog.hideDialogForLoading();
        if (errorCode == ErrorCodeUtils.NO_NET_RESPONSE) {
            //网络连接失败
            showToast(R.string.no_network);
            return;
        }
        if (action == ActionTypes.LOGIN) {
            if (errorCode == 30301) {
                showUnRegisterDialog();
            } else {
                showToast((String) returnObj);
            }
        }
    }

    @Override
    public void serviceCallback(ActionTypes action, int type, Object returnObj) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (flags == 1) {
            mTwitterClient.onActivityResult(requestCode, resultCode, data);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void enterLoginActivity(Activity activity, String flag) {
        Intent i = new Intent(activity, LoginActivity.class);
        i.putExtra("flag", flag);
        activity.startActivity(i);
    }


    @Override
    public void onBackPressed() {
        if ("RefrenceActivity".equals(getIntent().getStringExtra("flag"))) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
        finish();

    }

    /**
     * 融云连接
     *
     * @param rongCloudToken 融云Token
     */
    private void rongConnect(String rongCloudToken) {
        try {
            mRongIMClient = RongIMClient.connect(rongCloudToken, new RongIMClient.ConnectCallback() {
                @Override
                public void onSuccess(String userId) {
                    ILog.d(TAG, "--connect--onSuccess----userId---" + userId);// 自己的用户ID(登录后，登录用户的ID)
//                    初始化未读总数
                    int totalUnreadCount = mRongIMClient.getTotalUnreadCount();// 未读消息总数
                    controller.postTotalUnreadCountCallback(totalUnreadCount);
                    ReceiveMessageListener.getInstance().userId = userId;// 给谁发消息的用户ID
                    ReceiveMessageListener.getInstance().setRongIMClient(mRongIMClient);
                    ReceiveMessageListener.getInstance().registerReceiveMessageListener();// 注册接受消息监听事件
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    ILog.d(TAG, "--connect--errorCode-------" + errorCode);
                }

                @Override
                public void onTokenIncorrect() {
                    ILog.d(TAG, "融云Token错啦，正在从新获取Token");
                    controller.getServiceManager().getAasService().resetRongToken(LoginActivity.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showUnRegisterDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        View v = View.inflate(this, R.layout.dialog_unregister, null);
        Button goback_btn = (Button) v.findViewById(R.id.dialog_unregister_goback);
        Button goregister_btn = (Button) v.findViewById(R.id.dialog_unregister_register);
        goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        goregister_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent account = new Intent(LoginActivity.this, AccountActivity.class);
                startActivity(account);
                overridePendingTransition(R.anim.acitvity_open, 0);
            }
        });
        adb.setView(v);
        alertDialog = adb.create();
        alertDialog.show();
    }
}
