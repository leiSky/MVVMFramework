package gnnt.mebs.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
//import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;

import gnnt.mebs.base.BaseApp;
import gnnt.mebs.base.model.BaseRepository;
import gnnt.mebs.common.event.LoginUserEvent;

/*******************************************************************
 * CommonApp.java  2019/3/5
 * <P>
 * <br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class CommonApp extends BaseApp {

    private static final String SESSION_PREFS_KEY = "session_prefs_key";

    /**
     * 登录用户事件类
     */
    private LoginUserEvent mLoginUserEvent;

    private String sessionID;

    private HashMap<String, BaseRepository> mRepositoryMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

//        // 内存泄露检测
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//
//        // 内存泄露检测
//        LeakCanary.install(this);

        // ARouter 初始化
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug(); // 没用这句不能在debug模式运行
        }
        ARouter.init(this);

        // 初始化事件类
        mLoginUserEvent = new LoginUserEvent(this);
    }

    public BaseRepository getRepository(String key) {
        return mRepositoryMap.get(key);
    }

    public void setRepository(String key, BaseRepository repository) {
        mRepositoryMap.put(key, repository);
    }

    public String getSessionID() {
        if (TextUtils.isEmpty(sessionID)) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            sessionID = prefs.getString(SESSION_PREFS_KEY, null);
        }
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(SESSION_PREFS_KEY, sessionID);
        editor.apply();
    }

    /**
     * 获取登录用户事件监听
     *
     * @return 登录用户事件
     */
    public LoginUserEvent getLoginUserEvent() {
        return mLoginUserEvent;
    }


}
