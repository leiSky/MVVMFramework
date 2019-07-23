package cn.com.gnnt.workingdaily.fragment;

import android.app.Application;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.OnClick;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.activity.MainActivity;
import gnnt.mebs.base.component.BaseFragment;
import gnnt.mebs.base.component.BaseViewModel;

public class AboutFragment extends BaseFragment<AboutFragment.AboutFragmentViewModel> {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_about;
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.inflateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    protected Class<? extends AboutFragmentViewModel> getViewModelClass() {
        return AboutFragmentViewModel.class;
    }

    @OnClick(R.id.nav_button)
    protected void openDrawerLayout() {
        ((MainActivity) getActivity()).openDrawerLayout();
    }

    public static class AboutFragmentViewModel extends BaseViewModel {

        public AboutFragmentViewModel(@NonNull Application application) {
            super(application);
        }
    }
}
