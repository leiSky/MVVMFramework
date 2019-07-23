package cn.com.gnnt.workingdaily.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import butterknife.BindView;
import butterknife.OnClick;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.Util.DateUtils;
import cn.com.gnnt.workingdaily.Util.WDUtils;
import cn.com.gnnt.workingdaily.VO.DailyItem;
import cn.com.gnnt.workingdaily.activity.Base.WDBaseActivity;
import cn.com.gnnt.workingdaily.databinding.ActivityAddDailyBinding;
import gnnt.mebs.base.component.BaseViewModel;

public class WDAddDailyActivity extends WDBaseActivity<WDAddDailyViewModel> {

    public static final String DAILY_INFO_KEY = "daily_info_key";
    public static final String PAGE_TYPE_KEY = "page_type_key";

    private String pageType;
    private DailyItem daily;

    @BindView(R.id.nav_title)
    protected TextView navTitle;
    @BindView(R.id.nav_save)
    protected Button saveBtn;
    @BindView(R.id.content)
    protected EditText content;

    private ActivityAddDailyBinding mBinding;

    public static void actionStartForResult(Fragment fragment, DailyItem daily, String pageType, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), WDAddDailyActivity.class);
        if (daily != null) {
            intent.putExtra(DAILY_INFO_KEY, daily);
        }
        intent.putExtra(PAGE_TYPE_KEY, pageType);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_daily;
    }

    @Override
    protected void setupLayout(int layoutRes) {
        super.setupLayout(layoutRes);
        mBinding = DataBindingUtil.setContentView(this, layoutRes);
        mBinding.setLifecycleOwner(this);
    }

    @NonNull
    @Override
    protected Class<? extends WDAddDailyViewModel> getViewModelClass() {
        return WDAddDailyViewModel.class;
    }

    @Override
    protected void setupView() {
        super.setupView();

        pageType = getIntent().getStringExtra(PAGE_TYPE_KEY);
        daily = getIntent().getParcelableExtra(DAILY_INFO_KEY);

        if (pageType.equals(WDAddDailyViewModel.PAGE_TYPE_ADD)) {
            navTitle.setText(R.string.add_page_title);
        }else if (pageType.equals(WDAddDailyViewModel.PAGE_TYPE_MODIFY)) {
            navTitle.setText(R.string.modify_page_title);
        }
    }

    @Override
    protected void setupViewModel(@Nullable WDAddDailyViewModel viewModel) {
        super.setupViewModel(viewModel);
        mBinding.setViewModel(viewModel);
        viewModel.setPageType(pageType);
        viewModel.initDefaultWithDailyItem(daily);
    }

    @OnClick(R.id.nav_save)
    protected void saveBtnClick() {
        WDUtils.hideKeyboard(this);
    }

    @OnClick(R.id.project)
    protected void projectClick() {
        WDUtils.hideKeyboard(this);
        openOptionsDialog(mViewModel.projectLiveData, mViewModel.getProjectNameList());
    }

    @OnClick(R.id.work_type)
    protected void workTypeClick() {
        WDUtils.hideKeyboard(this);
        openOptionsDialog(mViewModel.workTypeLiveData, mViewModel.getWorkTypeList());
    }

    @OnClick(R.id.start_time)
    protected void startTimeClick() {
        WDUtils.hideKeyboard(this);
        openChooseTimeDialog(mViewModel.startTimeLiveData);
    }

    @OnClick(R.id.end_time)
    protected void endTimeCLick() {
        WDUtils.hideKeyboard(this);
        openChooseTimeDialog(mViewModel.endTimeLiveData);
    }

    @OnClick(R.id.nav_colse)
    protected void closeBtnClick() {
        WDUtils.hideKeyboard(this);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.exit_tip)
                .setPositiveButton(R.string.ok_btn_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel_btn_text, null);
        dialog.show();
    }

    private void openChooseTimeDialog(MutableLiveData<String> liveData) {
        String time = liveData.getValue();
        int hour = 0;
        int min = 0;
        if (time != null) {
            String[] times = time.split(":");
            hour = Integer.valueOf(times[0]);
            min = Integer.valueOf(times[1]);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, min);

        TimePickerView pvTime = new TimePickerBuilder(this, null)
                .isCyclic(true)
                .setDate(calendar)
                .setType(new boolean[]{false, false, false, true, true, false})
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        liveData.setValue(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, date));
                    }
                })
                .build();
        pvTime.show();
    }

    private void openOptionsDialog(MutableLiveData<String> liveData, List<String> options) {
        int selected = 0;
        if (!TextUtils.isEmpty(liveData.getValue())) {
            for (int i=0; i<options.size(); i++) {
                if (options.get(i).equals(liveData.getValue())) {
                    selected = i;
                }
            }
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                liveData.setValue(options.get(options1));
            }
        })
                .setSelectOptions(selected)
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        liveData.setValue(options.get(options1));
                    }
                })
                .build();
        pvOptions.setPicker(options);
        pvOptions.show();
    }

    @Override
    protected void onLoadStatusChanged(int status, int requestCode) {
        super.onLoadStatusChanged(status, requestCode);
        if (status == BaseViewModel.LOAD_COMPLETE && requestCode == WDAddDailyViewModel.SAVE_DAILY_CODE) {
            if (pageType.equals(WDAddDailyViewModel.PAGE_TYPE_ADD)) {
                Toast.makeText(this, R.string.save_success, Toast.LENGTH_SHORT).show();
            }else if (pageType.equals(WDAddDailyViewModel.PAGE_TYPE_MODIFY)) {
                Toast.makeText(this, R.string.modify_success, Toast.LENGTH_SHORT).show();
            }
            setResult(RESULT_OK);
            finish();
        }
    }
}
