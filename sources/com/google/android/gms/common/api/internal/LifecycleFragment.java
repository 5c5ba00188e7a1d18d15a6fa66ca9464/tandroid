package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.content.Intent;
/* compiled from: com.google.android.gms:play-services-basement@@18.1.0 */
/* loaded from: classes.dex */
public interface LifecycleFragment {
    void addCallback(String str, LifecycleCallback lifecycleCallback);

    <T extends LifecycleCallback> T getCallbackOrNull(String str, Class<T> cls);

    Activity getLifecycleActivity();

    void startActivityForResult(Intent intent, int i);
}
