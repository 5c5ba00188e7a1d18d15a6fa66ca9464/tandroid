package org.telegram.tgnet;
/* loaded from: classes3.dex */
public interface ResultCallback<T> {

    /* loaded from: classes3.dex */
    public final /* synthetic */ class -CC<T> {
        public static void $default$onError(ResultCallback resultCallback, Throwable th) {
        }

        public static void $default$onError(ResultCallback resultCallback, TLRPC$TL_error tLRPC$TL_error) {
        }
    }

    void onComplete(T t);

    void onError(TLRPC$TL_error tLRPC$TL_error);
}
