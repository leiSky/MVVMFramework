package cn.com.gnnt.workingdaily.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.activity.MainActivity;
import gnnt.mebs.base.component.BaseFragment;

public class WeekDailyFragment extends BaseFragment<WeekDailyViewModel> {

    @BindView(R.id.tab_layout)
    protected TabLayout tabs;
    @BindView(R.id.view_pager)
    protected ViewPager pages;

    private MutableLiveData<Integer> itemStyleLiveData = new MutableLiveData<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_week_daily;
    }

    @NonNull
    @Override
    protected Class<? extends WeekDailyViewModel> getViewModelClass() {

        return WeekDailyViewModel.class;
    }

    @Override
    protected void setupView(View rootView) {
        super.setupView(rootView);

        List<String> mTitles = new ArrayList<>();
        mTitles.add(getString(R.string.last_week_daily_title));
        mTitles.add(getString(R.string.this_week_daily_title));

        pages.setAdapter(new WeekDailyViewModel.WeekDailyPagerAdpater(getChildFragmentManager(), getActivity(), mTitles));
        tabs.setupWithViewPager(pages);
    }

    @OnClick(R.id.nav_sort_select)
    protected void itemSortChangeClick(View anchor) {
        final ListPopupWindow mPublishWindow = new ListPopupWindow(getContext());
        mPublishWindow.setWidth(com.beecampus.common.util.DisplayUtils.dip2px(getContext(), 120));
        // 发布适配器
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, getContext().getResources().getStringArray(R.array.week_daily_sort_menu));

        mPublishWindow.setAdapter(adapter);
        mPublishWindow.setAnchorView(anchor);
        mPublishWindow.setDropDownGravity(Gravity.RIGHT);
        mPublishWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    //按时间分组
                    itemStyleLiveData.setValue(WeekDailyViewModel.ITEM_STYLE_TIME);
                }else if (position == 1) {
                    //按项目分组
                    itemStyleLiveData.setValue(WeekDailyViewModel.ITEM_STYLE_PROJECT);
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

    public LiveData<Integer> getItemStyleLiveData() {
        return itemStyleLiveData;
    }
}
