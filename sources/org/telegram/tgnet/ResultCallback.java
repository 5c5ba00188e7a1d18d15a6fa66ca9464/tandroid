package org.telegram.tgnet;

import org.telegram.tgnet.TLRPC;
/* loaded from: classes3.dex */
public interface ResultCallback<T> {

    /* loaded from: classes3.dex */
    public abstract /* synthetic */ class -CC {
        public static void $default$onError(ResultCallback resultCallback, Throwable th) {
        }

        public static void $default$onError(ResultCallback resultCallback, TLRPC.TL_error tL_error) {
        }
    }

    void onComplete(T t);

    void onError(Throwable th);

    void onError(TLRPC.TL_error tL_error);
}
