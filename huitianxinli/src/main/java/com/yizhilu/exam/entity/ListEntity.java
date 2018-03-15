package com.yizhilu.exam.entity;

import java.io.Serializable;
import java.util.List;

/**
 * list实体
 * Created by yzl on 2016/5/24.
 */
public class ListEntity implements Serializable{
    private String message;
    private boolean success;
    private List<PublicEntity> entity;

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

    public List<PublicEntity> getEntity() {
        return entity;
    }

    public void setEntity(List<PublicEntity> entity) {
        this.entity = entity;
    }
}
