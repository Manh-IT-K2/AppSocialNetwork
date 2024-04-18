package com.example.frontend.response.Search;

import java.time.LocalDateTime;
import java.util.Date;

public class SearchHistoryResponse {

    private String text;
    private String avatar;
    private Boolean account;
    private String id;
    private String name;
    private Date dateTime;

    public SearchHistoryResponse(String text, String avatar, Boolean account, String id, String name, Date dateTime) {
        this.text = text;
        this.avatar = avatar;
        this.account = account;
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
    }

    public SearchHistoryResponse() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getAccount() {
        return account;
    }

    public void setAccount(Boolean account) {
        this.account = account;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
