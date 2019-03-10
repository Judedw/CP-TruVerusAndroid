package com.sl.clearpicture.utils;


public class MessageEvent {
    public static final int QUESTION_CH = 202;
    public static final int REFRESH_SCAN = 203;

    private int resultCode;
    private String counter;
    private int requestCode;
    private String buttonText;
    private String message;


    public MessageEvent(int resultCode, String counter,String buttonText) {
        this.resultCode = resultCode;
        this.counter = counter;
        this.buttonText = buttonText;
    }

    public MessageEvent(int resultCode, String msg) {
        this.resultCode = resultCode;
        this.message = msg;

    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
