package cn.com.gnnt.workingdaily.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.gnnt.workingdaily.R;
import cn.com.gnnt.workingdaily.Util.DateUtils;
import cn.com.gnnt.workingdaily.VO.DailyItem;
import cn.com.gnnt.workingdaily.VO.DailyItem2;

public class DailyAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private static List<View> VH = new ArrayList<>();
    public static final String ADAPTER_TYPE_NOW = "now";
    public static final String ADAPTER_TYPE_HISTORY = "history";

    private String type;

    public DailyAdapter(String type) {
        super(null);
        this.type = type;
        addItemType(0, R.layout.daily_list_item);
        addItemType(1, R.layout.daily_list_item2);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        if (item.getItemType() == 0) {
            DailyItem dailyItem = (DailyItem) item;
            helper.setText(R.id.tv_title, dailyItem.project);
            helper.setText(R.id.tv_content, dailyItem.content);
            helper.setText(R.id.tv_type, dailyItem.workType);
            if (type.equals(ADAPTER_TYPE_NOW)) {
                Date sd = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, dailyItem.startTime);
                Date ed = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, dailyItem.endTime);
                helper.setText(R.id.start_time, DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, sd));
                helper.setText(R.id.end_time, DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, ed));
                helper.addOnClickListener(R.id.func_btn);
            } else {
                helper.setText(R.id.start_time, dailyItem.startTime);
                helper.setText(R.id.end_time, dailyItem.endTime);
                helper.setGone(R.id.func_btn, false);
            }
        }else if (item.getItemType() == 1) {
            DailyItem2 dailyItem = (DailyItem2) item;
            helper.setText(R.id.tv_title, dailyItem.title);
            helper.setText(R.id.tv_time, dailyItem.time);

            LinearLayout layout = helper.getView(R.id.conetnt_layout);
            for (int i=0; i<layout.getChildCount(); i++) {
                VH.add(layout.getChildAt(i));
            }
            layout.removeAllViews();

            for (DailyItem2.Work work : dailyItem.works) {
                View v = null;
                if (VH.size() > 0) {
                    v = VH.get(0);
                    VH.remove(v);
                }else {
                    v = mLayoutInflater.inflate(R.layout.daily_list_item2_sub_item, layout, false);
                    SubItemViewHolder holder = new SubItemViewHolder(v);
                    v.setTag(holder);
                }
                layout.addView(v);
                SubItemViewHolder holder = (SubItemViewHolder)v.getTag();
                holder.setData(work);
            }
        }
    }

    class SubItemViewHolder {

        private View root;

        @BindView(R.id.tv_project)
        protected TextView projectName;
        @BindView(R.id.tv_type)
        protected TextView workTypeName;
        @BindView(R.id.start_time)
        protected TextView startTime;
        @BindView(R.id.end_time)
        protected TextView endTime;
        @BindView(R.id.tv_content)
        protected TextView content;

        public SubItemViewHolder(View root) {
            this.root = root;
            ButterKnife.bind(this, root);
        }

        public void setData(DailyItem2.Work work) {
            projectName.setText(work.projectName);
            workTypeName.setText(work.workTypeName);
            Date sd = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, work.startTime);
            startTime.setText(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, sd));
            Date ed = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, work.endTime);
            endTime.setText(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, ed));
            content.setText(work.content);
        }
    }
}
