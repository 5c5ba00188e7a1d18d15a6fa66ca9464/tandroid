package com.google.android.gms.common.api;

/* loaded from: classes.dex */
public abstract class Response {
    private Result zza;

    public Response() {
    }

    protected Response(Result result) {
        this.zza = result;
    }

    protected Result getResult() {
        return this.zza;
    }

    public void setResult(Result result) {
        this.zza = result;
    }
}
