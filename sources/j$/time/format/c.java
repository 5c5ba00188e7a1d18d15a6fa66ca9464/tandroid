package j$.time.format;

import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class c extends v {
    final /* synthetic */ u d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(u uVar) {
        this.d = uVar;
    }

    @Override // j$.time.format.v
    public final String c(j$.time.temporal.l lVar, long j, TextStyle textStyle, Locale locale) {
        return this.d.a(j, textStyle);
    }
}
