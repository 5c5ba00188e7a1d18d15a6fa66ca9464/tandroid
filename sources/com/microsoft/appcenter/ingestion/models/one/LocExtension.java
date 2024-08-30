package com.microsoft.appcenter.ingestion.models.one;

import com.microsoft.appcenter.ingestion.models.Model;
import com.microsoft.appcenter.ingestion.models.json.JSONUtils;
import org.json.JSONObject;
import org.json.JSONStringer;
/* loaded from: classes.dex */
public class LocExtension implements Model {
    private String tz;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        String str = this.tz;
        String str2 = ((LocExtension) obj).tz;
        return str != null ? str.equals(str2) : str2 == null;
    }

    public String getTz() {
        return this.tz;
    }

    public int hashCode() {
        String str = this.tz;
        if (str != null) {
            return str.hashCode();
        }
        return 0;
    }

    @Override // com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) {
        setTz(jSONObject.optString("tz", null));
    }

    public void setTz(String str) {
        this.tz = str;
    }

    @Override // com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        JSONUtils.write(jSONStringer, "tz", getTz());
    }
}
