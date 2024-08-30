package com.google.android.play.core.integrity;
/* loaded from: classes.dex */
final class a extends ap {
    private String a;
    private y b;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.play.core.integrity.ap
    public final ap a(y yVar) {
        this.b = yVar;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.play.core.integrity.ap
    public final ap b(String str) {
        this.a = str;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.play.core.integrity.ap
    public final aq c() {
        y yVar;
        String str = this.a;
        if (str == null || (yVar = this.b) == null) {
            StringBuilder sb = new StringBuilder();
            if (this.a == null) {
                sb.append(" token");
            }
            if (this.b == null) {
                sb.append(" integrityDialogWrapper");
            }
            throw new IllegalStateException("Missing required properties:".concat(sb.toString()));
        }
        return new aq(str, yVar);
    }
}
