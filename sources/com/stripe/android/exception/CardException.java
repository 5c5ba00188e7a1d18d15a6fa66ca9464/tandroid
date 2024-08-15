package com.stripe.android.exception;
/* loaded from: classes.dex */
public class CardException extends StripeException {
    private String charge;
    private String code;
    private String declineCode;
    private String param;

    public CardException(String str, String str2, String str3, String str4, String str5, String str6, Integer num, Throwable th) {
        super(str, str2, num, th);
        this.code = str3;
        this.param = str4;
        this.declineCode = str5;
        this.charge = str6;
    }
}
