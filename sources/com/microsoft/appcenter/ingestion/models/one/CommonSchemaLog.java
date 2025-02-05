package com.microsoft.appcenter.ingestion.models.one;

import com.microsoft.appcenter.ingestion.models.AbstractLog;
import com.microsoft.appcenter.ingestion.models.json.JSONDateUtils;
import com.microsoft.appcenter.ingestion.models.json.JSONUtils;
import org.json.JSONObject;
import org.json.JSONStringer;

/* loaded from: classes.dex */
public abstract class CommonSchemaLog extends AbstractLog {
    private String cV;
    private Data data;
    private Extensions ext;
    private Long flags;
    private String iKey;
    private String name;
    private Double popSample;
    private String ver;

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        CommonSchemaLog commonSchemaLog = (CommonSchemaLog) obj;
        String str = this.ver;
        if (str == null ? commonSchemaLog.ver != null : !str.equals(commonSchemaLog.ver)) {
            return false;
        }
        String str2 = this.name;
        if (str2 == null ? commonSchemaLog.name != null : !str2.equals(commonSchemaLog.name)) {
            return false;
        }
        Double d = this.popSample;
        if (d == null ? commonSchemaLog.popSample != null : !d.equals(commonSchemaLog.popSample)) {
            return false;
        }
        String str3 = this.iKey;
        if (str3 == null ? commonSchemaLog.iKey != null : !str3.equals(commonSchemaLog.iKey)) {
            return false;
        }
        Long l = this.flags;
        if (l == null ? commonSchemaLog.flags != null : !l.equals(commonSchemaLog.flags)) {
            return false;
        }
        String str4 = this.cV;
        if (str4 == null ? commonSchemaLog.cV != null : !str4.equals(commonSchemaLog.cV)) {
            return false;
        }
        Extensions extensions = this.ext;
        if (extensions == null ? commonSchemaLog.ext != null : !extensions.equals(commonSchemaLog.ext)) {
            return false;
        }
        Data data = this.data;
        Data data2 = commonSchemaLog.data;
        return data != null ? data.equals(data2) : data2 == null;
    }

    public String getCV() {
        return this.cV;
    }

    public Data getData() {
        return this.data;
    }

    public Extensions getExt() {
        return this.ext;
    }

    public Long getFlags() {
        return this.flags;
    }

    public String getIKey() {
        return this.iKey;
    }

    public String getName() {
        return this.name;
    }

    public Double getPopSample() {
        return this.popSample;
    }

    public String getVer() {
        return this.ver;
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog
    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        String str = this.ver;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.name;
        int hashCode3 = (hashCode2 + (str2 != null ? str2.hashCode() : 0)) * 31;
        Double d = this.popSample;
        int hashCode4 = (hashCode3 + (d != null ? d.hashCode() : 0)) * 31;
        String str3 = this.iKey;
        int hashCode5 = (hashCode4 + (str3 != null ? str3.hashCode() : 0)) * 31;
        Long l = this.flags;
        int hashCode6 = (hashCode5 + (l != null ? l.hashCode() : 0)) * 31;
        String str4 = this.cV;
        int hashCode7 = (hashCode6 + (str4 != null ? str4.hashCode() : 0)) * 31;
        Extensions extensions = this.ext;
        int hashCode8 = (hashCode7 + (extensions != null ? extensions.hashCode() : 0)) * 31;
        Data data = this.data;
        return hashCode8 + (data != null ? data.hashCode() : 0);
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog, com.microsoft.appcenter.ingestion.models.Model
    public void read(JSONObject jSONObject) {
        setVer(jSONObject.getString("ver"));
        setName(jSONObject.getString("name"));
        setTimestamp(JSONDateUtils.toDate(jSONObject.getString("time")));
        if (jSONObject.has("popSample")) {
            setPopSample(Double.valueOf(jSONObject.getDouble("popSample")));
        }
        setIKey(jSONObject.optString("iKey", null));
        setFlags(JSONUtils.readLong(jSONObject, "flags"));
        setCV(jSONObject.optString("cV", null));
        if (jSONObject.has("ext")) {
            Extensions extensions = new Extensions();
            extensions.read(jSONObject.getJSONObject("ext"));
            setExt(extensions);
        }
        if (jSONObject.has("data")) {
            Data data = new Data();
            data.read(jSONObject.getJSONObject("data"));
            setData(data);
        }
    }

    public void setCV(String str) {
        this.cV = str;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setExt(Extensions extensions) {
        this.ext = extensions;
    }

    public void setFlags(Long l) {
        this.flags = l;
    }

    public void setIKey(String str) {
        this.iKey = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPopSample(Double d) {
        this.popSample = d;
    }

    public void setVer(String str) {
        this.ver = str;
    }

    @Override // com.microsoft.appcenter.ingestion.models.AbstractLog, com.microsoft.appcenter.ingestion.models.Model
    public void write(JSONStringer jSONStringer) {
        jSONStringer.key("ver").value(getVer());
        jSONStringer.key("name").value(getName());
        jSONStringer.key("time").value(JSONDateUtils.toString(getTimestamp()));
        JSONUtils.write(jSONStringer, "popSample", getPopSample());
        JSONUtils.write(jSONStringer, "iKey", getIKey());
        JSONUtils.write(jSONStringer, "flags", getFlags());
        JSONUtils.write(jSONStringer, "cV", getCV());
        if (getExt() != null) {
            jSONStringer.key("ext").object();
            getExt().write(jSONStringer);
            jSONStringer.endObject();
        }
        if (getData() != null) {
            jSONStringer.key("data").object();
            getData().write(jSONStringer);
            jSONStringer.endObject();
        }
    }
}
