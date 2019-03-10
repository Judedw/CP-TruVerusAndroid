
package com.sl.clearpicture.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentSurvey implements Parcelable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("questions")
    @Expose
    private List<Question> questions = null;

    protected ContentSurvey(Parcel in) {
        id = in.readString();
        topic = in.readString();
        type = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        questions = in.createTypedArrayList(Question.CREATOR);
    }

    public static final Creator<ContentSurvey> CREATOR = new Creator<ContentSurvey>() {
        @Override
        public ContentSurvey createFromParcel(Parcel in) {
            return new ContentSurvey(in);
        }

        @Override
        public ContentSurvey[] newArray(int size) {
            return new ContentSurvey[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(topic);
        parcel.writeString(type);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeTypedList(questions);
    }
}
