package cn.com.gnnt.workingdaily.activity.Base;

import cn.com.gnnt.workingdaily.R;
import gnnt.mebs.base.component.BaseActivity;
import gnnt.mebs.base.component.BaseViewModel;

public abstract class WDBaseActivity<T extends BaseViewModel> extends BaseActivity<T> {

    @Override
    protected int getStatusBarColor() {
        return getResources().getColor(R.color.colorPrimary);
    }

    @Override
    protected boolean isDarkStatusBarText() {
        return false;
    }
}
