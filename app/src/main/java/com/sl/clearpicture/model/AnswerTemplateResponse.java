
package com.sl.clearpicture.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerTemplateResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("content")
    @Expose
    private ContentAnswer contentAnswer;

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

    public ContentAnswer getContentAnswer() {
        return contentAnswer;
    }

    public void setContentAnswer(ContentAnswer contentAnswer) {
        this.contentAnswer = contentAnswer;
    }

}
