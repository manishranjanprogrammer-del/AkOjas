package com.libojassoft.android.misc;

public interface SendDataBackToComponent {
    /**
     * This method is used to get result in calling component
     *
     * @param response
     * @param method   - is identify calls
     */
    public void doActionAfterGetResult(String response, int method);

    public void doActionOnError(String response);
}
