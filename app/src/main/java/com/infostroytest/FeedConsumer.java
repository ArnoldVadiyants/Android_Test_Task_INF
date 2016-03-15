package com.infostroytest;

public interface FeedConsumer {
    void showProgressBar();
    void hideProgressBar();
    void initializeFeed();
    void handleError(String message);
}
