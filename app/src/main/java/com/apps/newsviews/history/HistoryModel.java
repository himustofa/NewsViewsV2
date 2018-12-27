package com.apps.newsviews.history;

public class HistoryModel {

    private String historyId;
    private String historyItem;
    private String createdAt;

    public HistoryModel(String historyId, String historyItem, String createdAt) {
        this.historyId = historyId;
        this.historyItem = historyItem;
        this.createdAt = createdAt;
    }

    public HistoryModel(String historyItem) {
        this.historyItem = historyItem;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getHistoryItem() {
        return historyItem;
    }

    public void setHistoryItem(String historyItem) {
        this.historyItem = historyItem;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
