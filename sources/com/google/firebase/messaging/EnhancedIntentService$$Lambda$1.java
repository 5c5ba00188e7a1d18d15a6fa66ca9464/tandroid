package com.google.firebase.messaging;

import java.util.concurrent.Executor;

/* loaded from: classes.dex */
final /* synthetic */ class EnhancedIntentService$$Lambda$1 implements Executor {
    static final Executor $instance = new EnhancedIntentService$$Lambda$1();

    private EnhancedIntentService$$Lambda$1() {
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        runnable.run();
    }
}
