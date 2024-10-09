package com.microsoft.appcenter.ingestion.models.one;

import com.microsoft.appcenter.ingestion.models.Model;
import com.microsoft.appcenter.ingestion.models.json.JSONUtils;
import org.json.JSONObject;
import org.json.JSONStringer;

/* loaded from: classes.dex */
public class UserExtension implements Model {
    private String localId;
    private String locale;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserExtension userExtension = (UserExtension) obj;
        String str = this.localId;
        if (str == null ? userExtension.localId != null : !str.equals(userExtension.localId)) {
            return false;
        }
        String str2 = this.locale;
        String str3 = userExtension.locale;
        return str2 != null ? str2.equals(str3) : str3 == null;
    }

    public String getLocalId() {
        return this.localId;
    }

    public String getLocale() {
        return this.locale;
    }

    public int hashCode() {
        String str = this.localId;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.locale;
        return hashCode + (str2 != null ? str2.hashCode() : 0);
    }

    @Override // com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) {
        setLocalId(jSONObject.optString("localId", null));
        setLocale(jSONObject.optString("locale", null));
    }

    public void setLocalId(String str) {
        this.localId = str;
    }

    public void setLocale(String str) {
        this.locale = str;
    }

    @Override // com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        JSONUtils.write(jSONStringer, "localId", getLocalId());
        JSONUtils.write(jSONStringer, "locale", getLocale());
    }
}
