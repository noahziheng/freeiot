package net.noahgao.freeiot.api;

/**
 * Created by Noah Gao on 17-2-8.
 * By Android Studio
 */

public class ApiClient {
    private static final int CONNECT_TIMEOUT_MILLIS = 20000; // 20 seconds
    private static final int READ_TIMEOUT_MILLIS    = 20000; // 20 seconds
    private static final int WRITE_TIMEOUT_MILLIS   = 20000; // 20 seconds

    public static Api API;
}
