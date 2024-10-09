package com.google.android.gms.common.api;

/* loaded from: classes.dex */
public class ApiException extends Exception {
    protected final Status mStatus;

    public ApiException(Status status) {
        super(status.getStatusCode() + ": " + (status.getStatusMessage() != null ? status.getStatusMessage() : ""));
        this.mStatus = status;
    }

    public Status getStatus() {
        return this.mStatus;
    }

    public int getStatusCode() {
        return this.mStatus.getStatusCode();
    }
}
