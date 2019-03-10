
package com.sl.clearpicture.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SurveyCallResponse implements Parcelable{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("content")
    @Expose
    private ContentSurvey contentSurvey;

    protected SurveyCallResponse(Parcel in) {
        status = in.readString();
        if (in.readByte() == 0) {
            statusCode = null;
        } else {
            statusCode = in.readInt();
        }
        this.contentSurvey = in.readParcelable(ContentSurvey.class.getClassLoader());
    }

    public static final Creator<SurveyCallResponse> CREATOR = new Creator<SurveyCallResponse>() {
        @Override
        public SurveyCallResponse createFromParcel(Parcel in) {
            return new SurveyCallResponse(in);
        }

        @Override
        public SurveyCallResponse[] newArray(int size) {
            return new SurveyCallResponse[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public ContentSurvey getContentSurvey() {
        return contentSurvey;
    }

    public void setContentSurvey(ContentSurvey contentSurvey) {
        this.contentSurvey = contentSurvey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        if (statusCode == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(statusCode);
        }
        parcel.writeParcelable(contentSurvey,i);
    }
}
