package com.yhkj.yymall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by Administrator on 2017/7/26.
 */

public class BannerItemBean implements Parcelable {
    public int type;
    public String url; //视频地址
    public String img; //图片、视频首帧
    public File file;

    public BannerItemBean(int type,File file){
        this.type = type;
        this.file = file;
    }

    public BannerItemBean(int type,String url,String img){
        this.type = type;
        this.img = img;
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.url);
        dest.writeString(this.img);
        dest.writeSerializable(this.file);
    }

    protected BannerItemBean(Parcel in) {
        this.type = in.readInt();
        this.url = in.readString();
        this.img = in.readString();
        this.file = (File) in.readSerializable();
    }

    public static final Parcelable.Creator<BannerItemBean> CREATOR = new Parcelable.Creator<BannerItemBean>() {
        @Override
        public BannerItemBean createFromParcel(Parcel source) {
            return new BannerItemBean(source);
        }

        @Override
        public BannerItemBean[] newArray(int size) {
            return new BannerItemBean[size];
        }
    };
}
