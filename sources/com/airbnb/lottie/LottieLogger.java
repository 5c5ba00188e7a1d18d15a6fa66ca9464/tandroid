package com.airbnb.lottie;
/* loaded from: classes.dex */
public interface LottieLogger {
    void error(String str, Throwable th);

    void warning(String str);

    void warning(String str, Throwable th);
}
