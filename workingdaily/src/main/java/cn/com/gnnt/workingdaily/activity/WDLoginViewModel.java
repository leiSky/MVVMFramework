package cn.com.gnnt.workingdaily.activity;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import cn.com.gnnt.workingdaily.Config;
import cn.com.gnnt.workingdaily.Util.ResponseTransformer;
import cn.com.gnnt.workingdaily.Util.WDUtils;
import cn.com.gnnt.workingdaily.VO.WDLoginReqVO;
import cn.com.gnnt.workingdaily.VO.WDLoginRespVO;
import cn.com.gnnt.workingdaily.VO.WDVerifyImageReqVO;
import cn.com.gnnt.workingdaily.VO.WDVerifyImageRespVO;
import cn.com.gnnt.workingdaily.WDApi;
import gnnt.mebs.base.component.BaseViewModel;
import gnnt.mebs.common.CommonApp;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class WDLoginViewModel extends BaseViewModel {

    public static final int LOGIN_REQUEST_CODE = 1;

    public static final String USERNAME_PREFS_KEY = "user_name_key";
    public static final String USERPWD_SAVE_PREFS_KEY = "user_pwd_save_key";
    public static final String USERPWD_PREFS_KEY = "user_pwd_key";

    public MutableLiveData<String> mUserName = new MutableLiveData<>();
    public MutableLiveData<String> mUserPwd = new MutableLiveData<>();
    public MutableLiveData<String> mUserCode = new MutableLiveData<>();
    public MutableLiveData<Boolean> mCheckBox = new MutableLiveData<>();

    private PublishSubject<String> userName = PublishSubject.create();
    private PublishSubject<String> userPwd = PublishSubject.create();
    private PublishSubject<String> userCode = PublishSubject.create();

    private MutableLiveData<Bitmap> codeImage = new MutableLiveData<Bitmap>();
    private MutableLiveData<Boolean> loginBtnEnable = new MutableLiveData<Boolean>();

    private String session = null;

    public WDLoginViewModel(@NonNull Application application) {
        super(application);

        mUserName.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userName.onNext(s);
            }
        });
        mUserPwd.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userPwd.onNext(s);
            }
        });
        mUserCode.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userCode.onNext(s);
            }
        });


        String sessionID = ((CommonApp)getApplication()).getSessionID();
        session = !TextUtils.isEmpty(sessionID) ? sessionID : "";

        refreshCode(null);
        checkInputRuntime();

        initDefault();
    }

    private void initDefault() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication().getApplicationContext());

        String userName = prefs.getString(USERNAME_PREFS_KEY, null);
        if (!TextUtils.isEmpty(userName)) {
            mUserName.setValue(userName);
        }

        boolean checkBoxFlag = prefs.getBoolean(USERPWD_SAVE_PREFS_KEY, false);
        mCheckBox.setValue(checkBoxFlag);

        if (checkBoxFlag) {
            String userPwd = prefs.getString(USERPWD_PREFS_KEY, null);
            if (!TextUtils.isEmpty(userPwd)) {
                mUserPwd.setValue(userPwd);
            }
        }

    }

    public MutableLiveData<Bitmap> getCodeImage() {
        return codeImage;
    }

    public MutableLiveData<Boolean> getLoginBtnEnable() {
        return loginBtnEnable;
    }

    /**
     * TODO:具体的校验规则
     * 输入时的校验
     */
    private void checkInputRuntime() {
        Observable.combineLatest(userName, userPwd, userCode, new Function3<String, String, String, Boolean>() {
            @Override
            public Boolean apply(String s, String s2, String s3) throws Exception {
                return !TextUtils.isEmpty(s) && !TextUtils.isEmpty(s2) && !TextUtils.isEmpty(s3);
            }
        }).subscribe(new ViewModelObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                loginBtnEnable.postValue(aBoolean);
            }

            @Override
            public boolean dispatchStatusToView() {
                return false;
            }
        });
    }

    public void refreshCode(View view) {
        final WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        WDVerifyImageReqVO reqVO = new WDVerifyImageReqVO();
        reqVO.sessionID = session;
        api.getVerifyCodeImage(reqVO)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ResponseTransformer<>())
                .subscribe(new ViewModelSingleObserver<WDVerifyImageRespVO>(){
                    @Override
                    public void onSuccess(WDVerifyImageRespVO wdVerifyImageRespVO) {
                        super.onSuccess(wdVerifyImageRespVO);
                        session = wdVerifyImageRespVO.sessionID;
                        String imgString = wdVerifyImageRespVO.verifyImgString;
                        Bitmap bitmap = WDUtils.stringToBitmap(imgString);
                        codeImage.setValue(bitmap);
                    }
                });
    }

    public void loginBtnClick(View view) {
        final WDApi api = getApplication().getRetrofitManager().getApi(Config.HOST, WDApi.class);
        WDLoginReqVO reqVO = new WDLoginReqVO();
        reqVO.username = mUserName.getValue();
        reqVO.password = mUserPwd.getValue();
        reqVO.verifyCode = mUserCode.getValue();
        reqVO.sessionID = session;
        api.login(reqVO)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ResponseTransformer<>())
                .subscribe(new ViewModelSingleObserver<WDLoginRespVO>(){
                    @Override
                    public void onSuccess(WDLoginRespVO wdLoginRespVO) {
                        super.onSuccess(wdLoginRespVO);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication().getApplicationContext());
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putString(USERNAME_PREFS_KEY, mUserName.getValue());
                        editor.putBoolean(USERPWD_SAVE_PREFS_KEY, mCheckBox.getValue());

                        if (mCheckBox.getValue()) {
                            editor.putString(USERPWD_PREFS_KEY, mUserPwd.getValue());
                        }else {
                            editor.putString(USERPWD_PREFS_KEY, "");
                        }
                        editor.apply();

                        ((CommonApp)getApplication()).setSessionID(session);
                    }

                    @Override
                    public int getRequestCode() {
                        return LOGIN_REQUEST_CODE;
                    }
                });
    }

    public void resetBtnClick(View view) {
        mUserName.setValue("");
        mUserPwd.setValue("");
        mUserCode.setValue("");
    }
}
