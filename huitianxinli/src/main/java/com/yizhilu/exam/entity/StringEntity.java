package com.yizhilu.exam.entity;

import java.io.Serializable;

/**
 * Created by yzl on 2016/5/24.
 */
public class StringEntity implements Serializable{
    private String message;
    private boolean success;
    private String entity;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
