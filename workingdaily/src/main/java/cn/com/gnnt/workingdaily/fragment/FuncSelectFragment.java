package cn.com.gnnt.workingdaily.fragment;

import androidx.annotation.NonNull;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.activity.WDLoginViewModel;
import gnnt.mebs.base.component.BaseFragment;

public class FuncSelectFragment extends BaseFragment<FuncSelectViewModel> {

    @BindView(R.id.nav_title)
    protected TextView nav_title;

    @BindView(R.id.status_view)
    protected View statusView;

    @BindView(R.id.func_menu)
    protected RecyclerView listView;

    private List<FuncSelectViewModel.FuncMenuItem> dataList;
    private FuncListAdapter mAdapter;

    private MutableLiveData<FuncSelectViewModel.FuncMenuItem> funcClick = new MutableLiveData<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_func_select;
    }

    @NonNull
    @Override
    protected Class<? extends FuncSelectViewModel> getViewModelClass() {
        return FuncSelectViewModel.class;
    }

    @Override
    protected void setupView(View rootView) {
        super.setupView(rootView);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        nav_title.setText(prefs.getString(WDLoginViewModel.USERNAME_PREFS_KEY, "--"));

        ViewGroup.LayoutParams layoutParams = statusView.getLayoutParams();
        layoutParams.height = getStatusBarHeight();

        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new FuncListAdapter();
        listView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FuncSelectViewModel.FuncMenuItem item = dataList.get(position);
                funcClick.postValue(item);
            }
        });
    }

    @Override
    protected void setupViewModel(@NonNull FuncSelectViewModel viewModel) {
        super.setupViewModel(viewModel);
        viewModel.getFuncItems().observeForever(new Observer<List<FuncSelectViewModel.FuncMenuItem>>() {
            @Override
            public void onChanged(List<FuncSelectViewModel.FuncMenuItem> funcMenuItems) {
                mAdapter.setNewData(funcMenuItems);
                dataList = funcMenuItems;
                funcClick.setValue(funcMenuItems.get(0));
            }
        });
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public MutableLiveData<FuncSelectViewModel.FuncMenuItem> getFuncClick() {
        return funcClick;
    }

    class FuncListAdapter extends BaseQuickAdapter<FuncSelectViewModel.FuncMenuItem, BaseViewHolder> {

        public FuncListAdapter() {
            super(R.layout.func_list_item);
        }

        @Override
        protected void convert(BaseViewHolder helper, FuncSelectViewModel.FuncMenuItem item) {
            helper.setImageResource(R.id.iv_imageV, item.imageID);
            helper.setText(R.id.tv_func, item.funcName);
        }
    }
}
