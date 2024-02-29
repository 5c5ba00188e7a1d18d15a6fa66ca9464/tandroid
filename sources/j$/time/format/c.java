package j$.time.format;

import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class c extends v {
    final /* synthetic */ u d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(r rVar, u uVar) {
        this.d = uVar;
    }

    @Override // j$.time.format.v
    public String c(j$.time.temporal.l lVar, long j, TextStyle textStyle, Locale locale) {
        return this.d.a(j, textStyle);
    }
}
