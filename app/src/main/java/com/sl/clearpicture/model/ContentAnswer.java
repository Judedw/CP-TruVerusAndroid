
package com.sl.clearpicture.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentAnswer {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("answerTemplateType")
    @Expose
    private String answerTemplateType;
    @SerializedName("reverseDisplay")
    @Expose
    private String reverseDisplay;
    @SerializedName("answers")
    @Expose
    private List<Answer> answers = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswerTemplateType() {
        return answerTemplateType;
    }

    public void setAnswerTemplateType(String answerTemplateType) {
        this.answerTemplateType = answerTemplateType;
    }

    public String getReverseDisplay() {
        return reverseDisplay;
    }

    public void setReverseDisplay(String reverseDisplay) {
        this.reverseDisplay = reverseDisplay;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}
