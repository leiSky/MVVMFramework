package cn.com.gnnt.workingdaily.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.Util.DateUtils;
import cn.com.gnnt.workingdaily.VO.DailyItem;
import cn.com.gnnt.workingdaily.activity.MainActivity;
import cn.com.gnnt.workingdaily.databinding.FragmentHistoryBinding;
import gnnt.mebs.base.component.page.BasePageFragment;

public class HistoryFragment extends BasePageFragment<HistoryViewModel> {

    @BindView(R.id.start_time)
    protected TextView startTime;
    @BindView(R.id.end_time)
    protected TextView endTime;
    @BindView(R.id.history_content_view)
    protected RecyclerView historyList;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_history;
    }

    private FragmentHistoryBinding mBinding;

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,getLayoutResource(),container,false);
        mBinding.setLifecycleOwner(this);
        return mBinding.getRoot();
    }

    @NonNull
    @Override
    protected Class<? extends HistoryViewModel> getViewModelClass() {
        return HistoryViewModel.class;
    }

    @Override
    protected void setupView(View rootView) {
        super.setupView(rootView);
        historyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DailyAdapter(DailyAdapter.ADAPTER_TYPE_HISTORY);
        mAdapter.bindToRecyclerView(historyList);
        mAdapter.setEmptyView(R.layout.no_data, historyList);
        setAdapter(mAdapter);
    }

    @Override
    protected void setupViewModel(@Nullable HistoryViewModel viewModel) {
        super.setupViewModel(viewModel);
        mBinding.setViewModel(viewModel);
        viewModel.getData().removeObservers(this);
        viewModel.getItemData().observe(this, new Observer<List<MultiItemEntity>>() {
            @Override
            public void onChanged(List<MultiItemEntity> multiItemEntities) {
                mAdapter.setNewData(multiItemEntities);
            }
        });
    }

    @OnClick(R.id.nav_style_select)
    protected void itemStyleChangeClick(View anchor) {
        final ListPopupWindow mPublishWindow = new ListPopupWindow(getContext());
        mPublishWindow.setWidth(com.beecampus.common.util.DisplayUtils.dip2px(getContext(), 120));
        // 发布适配器
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, getContext().getResources().getStringArray(R.array.history_item_sort_menu));

        mPublishWindow.setAdapter(adapter);
        mPublishWindow.setAnchorView(anchor);
        mPublishWindow.setDropDownGravity(Gravity.RIGHT);
        mPublishWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //按时间
                    mViewModel.setItem_style(HistoryViewModel.ITEM_STYLE_TIME);
                }else if (position == 1) {
                    //按顺序
                    mViewModel.setItem_style(HistoryViewModel.ITEM_STYLE_NORMAL);
                }
                mPublishWindow.dismiss();
            }
        });
        mPublishWindow.show();
    }

    @OnClick(R.id.nav_button)
    protected void openDrawerLayout() {
        ((MainActivity) getActivity()).openDrawerLayout();
    }

    @OnClick(R.id.start_time)
    protected void startTimeClick() {
        openChooseDateDialog(DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD, startTime.getText().toString()), mViewModel.startDate);
    }

    @OnClick(R.id.end_time)
    protected void endTimeClick() {
        openChooseDateDialog(DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD, endTime.getText().toString()), mViewModel.endDate);
    }

    private void openChooseDateDialog(Date startDate, MutableLiveData<String> view) {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(startDate);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(new Date(0));

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(new Date());
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);

        TimePickerView pvTime = new TimePickerBuilder(getActivity(), null)
                .setDate(dateCalendar)
                .setRangDate(startCalendar, endCalendar)
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        view.setValue(DateUtils.stringFromDate(DateUtils.FORMAT_YYYY_MM_DD, date));
                    }
                })
                .build();
        pvTime.show();
    }
}
