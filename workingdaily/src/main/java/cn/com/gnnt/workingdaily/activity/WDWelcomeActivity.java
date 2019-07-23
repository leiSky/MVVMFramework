package cn.com.gnnt.workingdaily.activity;

import cn.com.gnnt.workingdaily.R;
import gnnt.mebs.common.CommonApp;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

public class WDWelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wdwelcome);
        setupView();
    }

    protected void setupView() {
        Single.timer(1,TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        String session = ((CommonApp)getApplication()).getSessionID();
                        if (TextUtils.isEmpty(session)) {
                            gotoLoginPage();
                        }else {
                            gotoMainPage();
                        }
                        finish();
                    }
                });
    }

    private void gotoLoginPage() {
        Intent intent = new Intent(this, WDLoginActivity.class);
        startActivity(intent);
    }

    private void gotoMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
