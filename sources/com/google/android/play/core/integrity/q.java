package com.google.android.play.core.integrity;

import android.content.Context;
/* compiled from: com.google.android.play:integrity@@1.3.0 */
/* loaded from: classes.dex */
final class q implements x {
    private Context a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ q(p pVar) {
    }

    public final q a(Context context) {
        context.getClass();
        this.a = context;
        return this;
    }

    @Override // com.google.android.play.core.integrity.x
    public final s b() {
        com.google.android.play.integrity.internal.am.a(this.a, Context.class);
        return new s(this.a, null);
    }
}
