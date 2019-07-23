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
import cn.com.gnnt.workingdaily.VO.WeekDailyProjectItem;
import cn.com.gnnt.workingdaily.VO.WeekDailyTimeItem;

public class WeekDailyAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private List<View> timeVH = new ArrayList<>();
    private List<View> projectVH = new ArrayList<>();

    public WeekDailyAdapter() {
        super(null);
        addItemType(0, R.layout.daily_list_item2);
        addItemType(1, R.layout.daily_list_item2);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        if (item.getItemType() == 0) {
            ///时间分组
            WeekDailyTimeItem dailyItem = (WeekDailyTimeItem) item;
            helper.setText(R.id.tv_title, dailyItem.title);
            helper.setText(R.id.tv_time, dailyItem.time);
            LinearLayout layout = helper.getView(R.id.conetnt_layout);
            for (int i=0; i<layout.getChildCount(); i++) {
                timeVH.add(layout.getChildAt(i));
            }
            layout.removeAllViews();

            for (WeekDailyTimeItem.Work work : dailyItem.works) {
                View v = null;
                if (timeVH.size() > 0) {
                    v = timeVH.get(0);
                    timeVH.remove(v);
                }else {
                    v = mLayoutInflater.inflate(R.layout.daily_list_item2_sub_item, layout, false);
                    SubTimeItemViewHolder holder = new SubTimeItemViewHolder(v);
                    v.setTag(holder);
                }
                layout.addView(v);
                SubTimeItemViewHolder holder = (SubTimeItemViewHolder)v.getTag();
                holder.setData(work);
            }
        }else if (item.getItemType() == 1) {
            ///项目分组
            WeekDailyProjectItem dailyItem = (WeekDailyProjectItem) item;
            helper.setText(R.id.tv_title, dailyItem.title);
            helper.setGone(R.id.tv_time, false);
            LinearLayout layout = helper.getView(R.id.conetnt_layout);
            for (int i=0; i<layout.getChildCount(); i++) {
                projectVH.add(layout.getChildAt(i));
            }
            layout.removeAllViews();

            for (WeekDailyTimeItem.Work work : dailyItem.works) {
                View v = null;
                if (projectVH.size() > 0) {
                    v = projectVH.get(0);
                    projectVH.remove(v);
                }else {
                    v = mLayoutInflater.inflate(R.layout.week_daily_project_sub_item, layout, false);
                    SubProjectItemViewHolder holder = new SubProjectItemViewHolder(v);
                    v.setTag(holder);
                }
                layout.addView(v);
                SubProjectItemViewHolder holder = (SubProjectItemViewHolder)v.getTag();
                holder.setData(work);
            }
        }
    }

    class SubTimeItemViewHolder {

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

        public SubTimeItemViewHolder(View root) {
            this.root = root;
            ButterKnife.bind(this, root);
        }

        public void setData(WeekDailyTimeItem.Work work) {
            projectName.setText(work.projectName);
            workTypeName.setText(work.workTypeName);
            Date sd = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, work.startTime);
            startTime.setText(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, sd));
            Date ed = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, work.endTime);
            endTime.setText(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, ed));
            content.setText(work.content);
        }
    }

    class SubProjectItemViewHolder {

        private View root;

        @BindView(R.id.tv_type)
        protected TextView workType;
        @BindView(R.id.tv_sub_week_day)
        protected TextView weekday;
        @BindView(R.id.start_time)
        protected TextView startTime;
        @BindView(R.id.end_time)
        protected TextView endTime;
        @BindView(R.id.tv_content)
        protected TextView content;

        public SubProjectItemViewHolder(View root) {
            this.root = root;
            ButterKnife.bind(this, root);
        }

        public void setData(WeekDailyProjectItem.Work work) {
            workType.setText(work.workTypeName);
            Date sd = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, work.startTime);
            weekday.setText(DateUtils.getWeekOfDate(sd));
            startTime.setText(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, sd));
            Date ed = DateUtils.dateFromDateString(DateUtils.FORMAT_YYYY_MM_DD_HH_MM, work.endTime);
            endTime.setText(DateUtils.stringFromDate(DateUtils.FORMAT_HH_MM, ed));
            content.setText(work.content);
        }
    }
}
