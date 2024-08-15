package com.google.android.play.core.integrity;

import android.content.Context;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.play:integrity@@1.3.0 */
/* loaded from: classes.dex */
public final class s {
    private final s a = this;
    private final com.google.android.play.integrity.internal.an b;
    private final com.google.android.play.integrity.internal.an c;
    private final com.google.android.play.integrity.internal.an d;
    private final com.google.android.play.integrity.internal.an e;
    private final com.google.android.play.integrity.internal.an f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ s(Context context, r rVar) {
        ad adVar;
        m mVar;
        m mVar2;
        com.google.android.play.integrity.internal.ak b = com.google.android.play.integrity.internal.al.b(context);
        this.b = b;
        adVar = ac.a;
        com.google.android.play.integrity.internal.an b2 = com.google.android.play.integrity.internal.aj.b(adVar);
        this.c = b2;
        mVar = l.a;
        au auVar = new au(b, mVar);
        this.d = auVar;
        mVar2 = l.a;
        com.google.android.play.integrity.internal.an b3 = com.google.android.play.integrity.internal.aj.b(new al(b, b2, auVar, mVar2));
        this.e = b3;
        this.f = com.google.android.play.integrity.internal.aj.b(new ab(b3));
    }

    public final IntegrityManager a() {
        return (IntegrityManager) this.f.a();
    }
}
