package com.stripe.android.exception;
/* loaded from: classes.dex */
public abstract class StripeException extends Exception {
    private String requestId;
    private Integer statusCode;

    public StripeException(String str, String str2, Integer num) {
        super(str, null);
        this.requestId = str2;
        this.statusCode = num;
    }

    public StripeException(String str, String str2, Integer num, Throwable th) {
        super(str, th);
        this.statusCode = num;
        this.requestId = str2;
    }

    @Override // java.lang.Throwable
    public String toString() {
        String str;
        if (this.requestId == null) {
            str = "";
        } else {
            str = "; request-id: " + this.requestId;
        }
        return super.toString() + str;
    }
}
