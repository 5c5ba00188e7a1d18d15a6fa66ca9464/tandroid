package com.google.firebase.messaging;

import com.google.android.datatransport.Transformer;
import com.google.firebase.messaging.reporting.MessagingClientEventExtension;

/* loaded from: classes.dex */
final /* synthetic */ class MessagingAnalytics$$Lambda$0 implements Transformer {
    static final Transformer $instance = new MessagingAnalytics$$Lambda$0();

    private MessagingAnalytics$$Lambda$0() {
    }

    @Override // com.google.android.datatransport.Transformer
    public Object apply(Object obj) {
        return ((MessagingClientEventExtension) obj).toByteArray();
    }
}
