package ca.uvic.ecg.webModel;

import com.mkingzhu.dataprotocol.Entity;
import com.mkingzhu.dataprotocol.Feed;

public class RestModel<T> {

    private Entity<T> entity;
    private Feed<T> feed;

    private String errorMessage;
    private Integer errorCode;

    public RestModel(ErrorInfo errorInfo) {
        this.errorMessage = errorInfo.getErrorMessage();
        this.errorCode = errorInfo.getErrorCode();
    }

    public RestModel(Entity<T> entity, ErrorInfo errorInfo) {
        this(errorInfo);

        this.entity = entity;
    }

    public RestModel(Feed<T> feed, ErrorInfo errorInfo) {
        this(errorInfo);

        this.feed = feed;
    }


    public Entity<T> getEntity() {
        return entity;
    }

    public Feed<T> getFeed() {
        return feed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
