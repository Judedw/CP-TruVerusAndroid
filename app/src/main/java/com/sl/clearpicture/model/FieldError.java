package com.sl.clearpicture.model;


public class FieldError {
    private boolean validation;
    private String reason;


    private  FieldError(final Builder builder) {
        this.validation = builder.validation;
        this.reason = builder.reason;
    }

    public boolean isValidation() {
        return validation;
    }



    public String getReason() {
        return reason;
    }


    public static class Builder{
        private boolean validation;
        private String reason;

        public Builder setValidation(final boolean validation){
            this.validation = validation;
            return  this;
        }

        public Builder setReason(final String reason){
            this.reason = reason;
            return this;
        }

        public FieldError create(){
            return new FieldError(this);
        }
    }
}
