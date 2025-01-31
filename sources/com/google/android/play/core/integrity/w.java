package com.google.android.play.core.integrity;

import android.content.Context;

/* loaded from: classes.dex */
final class w implements aw {
    private final w a = this;
    private final com.google.android.play.integrity.internal.an b;
    private final com.google.android.play.integrity.internal.an c;
    private final com.google.android.play.integrity.internal.an d;
    private final com.google.android.play.integrity.internal.an e;
    private final com.google.android.play.integrity.internal.an f;
    private final com.google.android.play.integrity.internal.an g;

    /* synthetic */ w(Context context, v vVar) {
        bc bcVar;
        o oVar;
        o oVar2;
        com.google.android.play.integrity.internal.ak b = com.google.android.play.integrity.internal.al.b(context);
        this.b = b;
        bcVar = bb.a;
        com.google.android.play.integrity.internal.an b2 = com.google.android.play.integrity.internal.aj.b(bcVar);
        this.c = b2;
        oVar = n.a;
        au auVar = new au(b, oVar);
        this.d = auVar;
        oVar2 = n.a;
        com.google.android.play.integrity.internal.an b3 = com.google.android.play.integrity.internal.aj.b(new bp(b, b2, auVar, oVar2));
        this.e = b3;
        com.google.android.play.integrity.internal.an b4 = com.google.android.play.integrity.internal.aj.b(new bu(b3));
        this.f = b4;
        this.g = com.google.android.play.integrity.internal.aj.b(new ba(b3, b4));
    }

    @Override // com.google.android.play.core.integrity.aw
    public final StandardIntegrityManager a() {
        return (StandardIntegrityManager) this.g.a();
    }
}
