package cn.com.gnnt.workingdaily.fragment;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.VO.WDWeekDailyRespVO;
import gnnt.mebs.base.component.BaseFragment;

public class WDWeekDailyContentFragment extends BaseFragment<WeekDailyViewModel> {

    private BaseQuickAdapter mAdapter;
    @BindView(R.id.week_daily_list)
    protected RecyclerView weekDailyList;

    private int item_style = WeekDailyViewModel.ITEM_STYLE_TIME;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_week_daily_content;
    }

    @NonNull
    @Override
    protected Class<? extends WeekDailyViewModel> getViewModelClass() {
        return WeekDailyViewModel.class;
    }

    @Override
    protected WeekDailyViewModel createViewModel() {
        return ViewModelProviders.of(getParentFragment()).get(getViewModelClass());
    }

    @Override
    protected void setupView(View rootView) {
        super.setupView(rootView);

        mAdapter = new WeekDailyAdapter();
        weekDailyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.bindToRecyclerView(weekDailyList);
        mAdapter.setEmptyView(R.layout.no_data, weekDailyList);

        WeekDailyFragment parent = (WeekDailyFragment)getParentFragment();
        parent.getItemStyleLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                item_style = integer;
                refreshData();
            }
        });
    }

    @Override
    protected void setupViewModel(@NonNull WeekDailyViewModel viewModel) {
        super.setupViewModel(viewModel);
        viewModel.getRespLiveData().observe(this, new Observer<WDWeekDailyRespVO>() {
            @Override
            public void onChanged(WDWeekDailyRespVO wdWeekDailyRespVO) {
                refreshData();
            }
        });
    }

    private void refreshData() {
        int page_tag = getArguments().getInt("page_tag");
        if (page_tag == 0) {//上周
            mAdapter.setNewData(mViewModel.getDataItems(item_style, mViewModel.getRespLiveData().getValue().lastWeek));
        }else if (page_tag == 1) {//本周
            mAdapter.setNewData(mViewModel.getDataItems(item_style, mViewModel.getRespLiveData().getValue().thisWeek));
        }
    }
}
