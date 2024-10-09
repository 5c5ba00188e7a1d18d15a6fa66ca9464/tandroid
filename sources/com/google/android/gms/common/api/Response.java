package com.google.android.gms.common.api;

/* loaded from: classes.dex */
public abstract class Response {
    private Result zza;

    public Response() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Response(Result result) {
        this.zza = result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Result getResult() {
        return this.zza;
    }

    public void setResult(Result result) {
        this.zza = result;
    }
}
