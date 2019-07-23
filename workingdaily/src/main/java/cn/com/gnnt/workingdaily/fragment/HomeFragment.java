package cn.com.gnnt.workingdaily.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.VO.DailyItem;
import cn.com.gnnt.workingdaily.activity.MainActivity;
import cn.com.gnnt.workingdaily.activity.WDAddDailyActivity;
import gnnt.mebs.base.component.BaseFragment;

import static android.app.Activity.RESULT_OK;
import static cn.com.gnnt.workingdaily.activity.WDAddDailyViewModel.PAGE_TYPE_ADD;
import static cn.com.gnnt.workingdaily.activity.WDAddDailyViewModel.PAGE_TYPE_MODIFY;

public class HomeFragment extends BaseFragment<HomeFragmentViewModel> {

    @BindView(R.id.today_daily_list)
    protected RecyclerView todayDailyList;

    private BaseQuickAdapter mAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @NonNull
    @Override
    protected Class<? extends HomeFragmentViewModel> getViewModelClass() {
        return HomeFragmentViewModel.class;
    }

    @OnClick(R.id.nav_button)
    protected void openDrawerLayout() {
        ((MainActivity) getActivity()).openDrawerLayout();
    }

    @Override
    protected void setupView(View rootView) {
        super.setupView(rootView);

        mAdapter = new DailyAdapter(DailyAdapter.ADAPTER_TYPE_NOW);
        todayDailyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.bindToRecyclerView(todayDailyList);
        mAdapter.setEmptyView(R.layout.no_data, todayDailyList);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.func_btn) {
                    List<DailyItem> items= adapter.getData();
                    openPopupView(view, position, items.get(position));
                }
            }
        });
    }

    @Override
    protected void setupViewModel(@Nullable HomeFragmentViewModel viewModel) {
        super.setupViewModel(viewModel);
        viewModel.getRespLiveData().observe(this, new Observer<List<DailyItem>>() {
            @Override
            public void onChanged(List<DailyItem> dailyItems) {
                mAdapter.setNewData(dailyItems);
            }
        });
    }

    private void openPopupView(View anchor, int position, DailyItem item) {
        final ListPopupWindow mPublishWindow = new ListPopupWindow(getContext());
        mPublishWindow.setWidth(com.beecampus.common.util.DisplayUtils.dip2px(getContext(), 100));
        // 发布适配器
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, getContext().getResources().getStringArray(R.array.item_func_menu));

        mPublishWindow.setAdapter(adapter);
        mPublishWindow.setAnchorView(anchor);
        mPublishWindow.setDropDownGravity(Gravity.RIGHT);
        mPublishWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //编辑
                    gotoModifyPage(item);
                }else if (position == 1) {
                    //删除
                    deleteDaily(item);
                }
                mPublishWindow.dismiss();
            }
        });
        mPublishWindow.show();
    }

    @OnClick(R.id.nav_add)
    protected void gotoAddDailyPAge() {
        List<DailyItem> items = mAdapter.getData();
        DailyItem item = null;
        if (items.size() > 0) {
            item = items.get(items.size()-1);
        }
        WDAddDailyActivity.actionStartForResult(this, item, PAGE_TYPE_ADD, 1);
    }

    private void gotoModifyPage(DailyItem item) {
        WDAddDailyActivity.actionStartForResult(this, item, PAGE_TYPE_MODIFY, 1);
    }

    private void deleteDaily(DailyItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(R.string.delete_tip)
                .setPositiveButton(R.string.ok_btn_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mViewModel.delteDaily(item);
                    }
                })
                .setNegativeButton(R.string.cancel_btn_text, null);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            mViewModel.refreshDataIfNeed();
        }
    }
}
