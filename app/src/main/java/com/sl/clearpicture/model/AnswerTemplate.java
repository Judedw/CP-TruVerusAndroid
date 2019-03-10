
package com.sl.clearpicture.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerTemplate implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;

    protected AnswerTemplate(Parcel in) {
        name = in.readString();
        id = in.readString();
    }

    public static final Creator<AnswerTemplate> CREATOR = new Creator<AnswerTemplate>() {
        @Override
        public AnswerTemplate createFromParcel(Parcel in) {
            return new AnswerTemplate(in);
        }

        @Override
        public AnswerTemplate[] newArray(int size) {
            return new AnswerTemplate[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
    }
}
