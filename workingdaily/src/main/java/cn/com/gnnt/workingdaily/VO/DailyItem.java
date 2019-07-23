package cn.com.gnnt.workingdaily.VO;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class DailyItem implements Parcelable,MultiItemEntity {

    public String id;

    //-----------在当前日报的返回包中有----------
    public String workTypeID;

    public String projectID;
    //---------------------------------------

    public String workType;

    public String project;

    public String content;

    public String startTime;

    public String endTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.workTypeID);
        dest.writeString(this.projectID);
        dest.writeString(this.workType);
        dest.writeString(this.project);
        dest.writeString(this.content);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
    }

    public DailyItem() {
    }

    protected DailyItem(Parcel in) {
        this.id = in.readString();
        this.workTypeID = in.readString();
        this.projectID = in.readString();
        this.workType = in.readString();
        this.project = in.readString();
        this.content = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
    }

    public static final Parcelable.Creator<DailyItem> CREATOR = new Parcelable.Creator<DailyItem>() {
        @Override
        public DailyItem createFromParcel(Parcel source) {
            return new DailyItem(source);
        }

        @Override
        public DailyItem[] newArray(int size) {
            return new DailyItem[size];
        }
    };

    @Override
    public int getItemType() {
        return 0;
    }
}
