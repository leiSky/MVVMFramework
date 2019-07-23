package cn.com.gnnt.workingdaily.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import butterknife.BindView;
import butterknife.OnClick;
import cn.com.gnnt.workingdaily.Util.WDUtils;
import cn.com.gnnt.workingdaily.activity.Base.WDBaseActivity;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.databinding.ActivityLoginBinding;
import gnnt.mebs.base.component.BaseViewModel;

public class WDLoginActivity extends WDBaseActivity<WDLoginViewModel> {

    @BindView(R.id.user_name)
    protected EditText et_userName;
    @BindView(R.id.user_pwd)
    protected EditText et_userPwd;
    @BindView(R.id.user_code)
    protected EditText et_code;
    @BindView(R.id.code_image)
    protected ImageView iv_codeImage;
    @BindView(R.id.login_btn)
    protected Button loginBtn;
    @BindView(R.id.reset_btn)
    protected Button resetBtn;

    ActivityLoginBinding mBinding;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupLayout(int layoutRes) {
        mBinding = DataBindingUtil.setContentView(this, layoutRes);
        mBinding.setLifecycleOwner(this);
    }

    @NonNull
    @Override
    protected Class<? extends WDLoginViewModel> getViewModelClass() {
        return WDLoginViewModel.class;
    }

    @Override
    protected void setupViewModel(@Nullable WDLoginViewModel viewModel) {
        super.setupViewModel(viewModel);
        mBinding.setViewModel(viewModel);

        viewModel.getLoginBtnEnable().observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean == true) {
                    loginBtn.setEnabled(true);
                }else {
                    loginBtn.setEnabled(false);
                }
            }
        });
        viewModel.getCodeImage().observeForever(new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                iv_codeImage.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    protected void onLoadStatusChanged(int status, int requestCode) {
        super.onLoadStatusChanged(status, requestCode);
        if (requestCode == WDLoginViewModel.LOGIN_REQUEST_CODE && status == BaseViewModel.LOAD_COMPLETE) {
            //登录成功,跳转
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
            gotoMainActivity();
        }
    }

    @OnClick(R.id.login_btn)
    protected void loginBtnClick() {
        WDUtils.hideKeyboard(this);
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
