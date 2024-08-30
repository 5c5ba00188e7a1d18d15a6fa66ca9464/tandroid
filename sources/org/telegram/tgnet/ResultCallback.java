package org.telegram.tgnet;
/* loaded from: classes3.dex */
public interface ResultCallback {

    /* loaded from: classes3.dex */
    public abstract /* synthetic */ class -CC {
        public static void $default$onError(ResultCallback resultCallback, Throwable th) {
        }

        public static void $default$onError(ResultCallback resultCallback, TLRPC$TL_error tLRPC$TL_error) {
        }
    }

    void onComplete(Object obj);

    void onError(Throwable th);

    void onError(TLRPC$TL_error tLRPC$TL_error);
}
