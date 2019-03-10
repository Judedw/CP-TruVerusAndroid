
package com.sl.clearpicture.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail {

    @SerializedName("uniqueProductCode")
    @Expose
    private String uniqueProductCode;
    @SerializedName("authenticationCode")
    @Expose
    private String authenticationCode;

    public String getUniqueProductCode() {
        return uniqueProductCode;
    }

    public void setUniqueProductCode(String uniqueProductCode) {
        this.uniqueProductCode = uniqueProductCode;
    }

    public String getAuthenticationCode() {
        return authenticationCode;
    }

    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }

}
