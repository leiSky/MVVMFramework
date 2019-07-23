package cn.com.gnnt.workingdaily.fragment;


import android.content.Intent;

import androidx.annotation.NonNull;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.activity.MainActivity;
import cn.com.gnnt.workingdaily.activity.WDLoginActivity;
import gnnt.mebs.base.component.BaseFragment;
import gnnt.mebs.base.component.BaseViewModel;
import gnnt.mebs.common.CommonApp;

public class SettingFragment extends BaseFragment<SettingViewModel> {

    @BindView(R.id.setting_menu)
    protected RecyclerView setting_menu;

    private SettingItemAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_setting;
    }

    @NonNull
    @Override
    protected Class<? extends SettingViewModel> getViewModelClass() {
        return SettingViewModel.class;
    }

    @Override
    protected void setupView(View rootView) {
        super.setupView(rootView);
        setting_menu.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SettingItemAdapter();
        setting_menu.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<SettingViewModel.SettingMenuItem> list = adapter.getData();
                funcClick(list.get(position));
            }
        });
    }

    @Override
    protected void setupViewModel(@NonNull SettingViewModel viewModel) {
        super.setupViewModel(viewModel);

        viewModel.getSettingItems().observeForever(new Observer<List<SettingViewModel.SettingMenuItem>>() {
            @Override
            public void onChanged(List<SettingViewModel.SettingMenuItem> settingMenuItems) {
                mAdapter.setNewData(settingMenuItems);
            }
        });
    }

    @OnClick(R.id.nav_button)
    protected void openDrawerLayout() {
        ((MainActivity) getActivity()).openDrawerLayout();
    }

    private void funcClick(SettingViewModel.SettingMenuItem item) {
        switch (item.tag) {
            case SettingViewModel.SETTING_CHANGE_PWD:
                gotoChangePwdPage(item);
                break;
            case SettingViewModel.SETTING_LOGOUT:
                logout();
                break;
            default:
                break;
        }
    }

    private void gotoChangePwdPage(SettingViewModel.SettingMenuItem item) {
        Intent intent = new Intent(getActivity(), item.turnCls);
        startActivity(intent);
    }

    private void logout() {
        mViewModel.logout();
        _logout();
    }

    @Override
    protected void onLoadStatusChanged(int status, int requestCode) {
        if (status == BaseViewModel.LOAD_COMPLETE) {
            return;
        }
        super.onLoadStatusChanged(status, requestCode);

    }

    private void _logout() {
        ((CommonApp)getActivity().getApplication()).setSessionID(null);
        Intent intent = new Intent(getActivity(), WDLoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    class SettingItemAdapter extends BaseQuickAdapter<SettingViewModel.SettingMenuItem, BaseViewHolder> {

        public SettingItemAdapter() {
            super(R.layout.setting_list_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, SettingViewModel.SettingMenuItem item) {
            helper.setImageResource(R.id.iv_imageV, item.imageID);
            helper.setText(R.id.tv_func, item.settingName);
            helper.setGone(R.id.iv_detail,item.turnCls!=null);
        }
    }
}
