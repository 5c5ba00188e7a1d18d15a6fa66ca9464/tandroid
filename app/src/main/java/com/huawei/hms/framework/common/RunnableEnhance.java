package com.huawei.hms.framework.common;
/* loaded from: classes.dex */
public class RunnableEnhance implements Runnable {
    static final String TRANCELOGO = " -->";
    private String parentName = Thread.currentThread().getName();
    private Runnable proxy;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RunnableEnhance(Runnable runnable) {
        this.proxy = runnable;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.proxy.run();
    }

    public String getParentName() {
        return this.parentName;
    }
}
