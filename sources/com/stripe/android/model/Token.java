package com.stripe.android.model;

import java.util.Date;

/* loaded from: classes.dex */
public class Token {
    private final Card mCard;
    private final Date mCreated;
    private final String mId;
    private final boolean mLivemode;
    private final String mType;
    private final boolean mUsed;

    public Token(String str, boolean z, Date date, Boolean bool, Card card, String str2) {
        this.mId = str;
        this.mType = str2;
        this.mCreated = date;
        this.mLivemode = z;
        this.mCard = card;
        this.mUsed = bool.booleanValue();
    }

    public Card getCard() {
        return this.mCard;
    }

    public String getId() {
        return this.mId;
    }

    public String getType() {
        return this.mType;
    }
}
