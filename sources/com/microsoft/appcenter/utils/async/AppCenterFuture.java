package com.microsoft.appcenter.utils.async;

/* loaded from: classes.dex */
public interface AppCenterFuture {
    Object get();

    void thenAccept(AppCenterConsumer appCenterConsumer);
}
