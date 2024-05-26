package com.google.android.play.core.integrity;
/* compiled from: com.google.android.play:integrity@@1.3.0 */
/* loaded from: classes.dex */
final class aq extends IntegrityTokenResponse {
    private final String a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public aq(String str, y yVar) {
        this.a = str;
    }

    @Override // com.google.android.play.core.integrity.IntegrityTokenResponse
    public final String token() {
        return this.a;
    }
}
