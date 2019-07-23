package cn.com.gnnt.workingdaily.activity;

import android.app.Application;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import butterknife.BindView;
import butterknife.OnClick;
import cn.com.gnnt.workingdaily.activity.Base.WDBaseActivity;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.fragment.AboutFragment;
import cn.com.gnnt.workingdaily.fragment.FuncSelectFragment;
import cn.com.gnnt.workingdaily.fragment.FuncSelectViewModel;
import cn.com.gnnt.workingdaily.fragment.HistoryFragment;
import cn.com.gnnt.workingdaily.fragment.HomeFragment;
import cn.com.gnnt.workingdaily.fragment.SettingFragment;
import cn.com.gnnt.workingdaily.fragment.WeekDailyFragment;
import gnnt.mebs.base.component.BaseViewModel;
import gnnt.mebs.base.model.FileRepository;
import gnnt.mebs.common.CommonApp;
import io.reactivex.Single;

public class MainActivity extends WDBaseActivity<MainActivity.MainViewModel> {

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.content_layout)
    protected FrameLayout contentLayout;

    protected FuncSelectFragment mFuncSelectFragment;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @NonNull
    @Override
    protected Class<? extends MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected void setupView() {
        super.setupView();
        settingStatueBar();
        mFuncSelectFragment = (FuncSelectFragment)getSupportFragmentManager().findFragmentById(R.id.select_func_list);
    }

    @Override
    protected void setupViewModel(@Nullable MainViewModel viewModel) {
        super.setupViewModel(viewModel);

        mFuncSelectFragment.getFuncClick().observeForever(new Observer<FuncSelectViewModel.FuncMenuItem>() {
            @Override
            public void onChanged(FuncSelectViewModel.FuncMenuItem item) {
                drawerLayout.closeDrawers();
                changeFragment(item);
            }
        });
    }

    private void settingStatueBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void changeFragment(FuncSelectViewModel.FuncMenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        Fragment temp = null;
        for (Fragment f : fragments) {
            if (f.getId() != R.id.select_func_list) {
                if (f.getTag().equals(item.tag)) {
                    temp = f;
                }else {
                    transaction.hide(f);
                }
            }
        }
        if (temp != null) {
            transaction.show(temp);
        }else {
            switch (item.tag) {
                case FuncSelectViewModel.WD_TODAY:
                    temp = new HomeFragment();
                    break;
                case FuncSelectViewModel.WD_HISTORY:
                    temp = new HistoryFragment();
                    break;
                case FuncSelectViewModel.WD_WEEK:
                    temp = new WeekDailyFragment();
                    break;
                case FuncSelectViewModel.WD_SETTING:
                    temp = new SettingFragment();
                    break;
                case FuncSelectViewModel.WD_ABOUT:
                    temp = new AboutFragment();
                    break;
                default:
                    break;
            }
            transaction.add(R.id.content_layout, temp, item.tag);
        }
        transaction.commit();
    }

    public static class MainViewModel extends BaseViewModel {

        public MainViewModel(@NonNull Application application) {
            super(application);
        }
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        if((System.currentTimeMillis()-exitTime) > 2000) {
            Toast.makeText(this, R.string.press_once_to_exit, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else {
            super.onBackPressed();
        }
    }
}
