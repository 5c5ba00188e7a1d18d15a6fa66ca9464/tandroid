package com.stripe.android.net;

import com.stripe.android.model.Token;
import com.stripe.android.util.StripeJsonUtils;
import com.stripe.android.util.StripeTextUtils;
import java.util.Date;
import org.json.JSONObject;
/* loaded from: classes.dex */
public abstract class TokenParser {
    public static Token parseToken(String str) {
        JSONObject jSONObject = new JSONObject(str);
        String string = StripeJsonUtils.getString(jSONObject, "id");
        long j = jSONObject.getLong("created");
        boolean z = jSONObject.getBoolean("livemode");
        String asTokenType = StripeTextUtils.asTokenType(StripeJsonUtils.getString(jSONObject, "type"));
        return new Token(string, z, new Date(j * 1000), Boolean.valueOf(jSONObject.getBoolean("used")), CardParser.parseCard(jSONObject.getJSONObject("card")), asTokenType);
    }
}
