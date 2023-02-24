package com.google.android.exoplayer2.source.rtsp;

import com.google.android.exoplayer2.util.Util;
import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class RtspHeaders {
    private final ImmutableListMultimap<String, String> namesAndValues;

    static {
        new Builder().build();
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private final ImmutableListMultimap.Builder<String, String> namesAndValuesBuilder;

        public Builder() {
            this.namesAndValuesBuilder = new ImmutableListMultimap.Builder<>();
        }

        public Builder(String str, String str2, int i) {
            this();
            add("User-Agent", str);
            add("CSeq", String.valueOf(i));
            if (str2 != null) {
                add("Session", str2);
            }
        }

        public Builder add(String str, String str2) {
            this.namesAndValuesBuilder.put((ImmutableListMultimap.Builder<String, String>) RtspHeaders.convertToStandardHeaderName(str.trim()), str2.trim());
            return this;
        }

        public Builder addAll(List<String> list) {
            for (int i = 0; i < list.size(); i++) {
                String[] splitAtFirst = Util.splitAtFirst(list.get(i), ":\\s?");
                if (splitAtFirst.length == 2) {
                    add(splitAtFirst[0], splitAtFirst[1]);
                }
            }
            return this;
        }

        public Builder addAll(Map<String, String> map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                add(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public RtspHeaders build() {
            return new RtspHeaders(this);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RtspHeaders) {
            return this.namesAndValues.equals(((RtspHeaders) obj).namesAndValues);
        }
        return false;
    }

    public int hashCode() {
        return this.namesAndValues.hashCode();
    }

    public ImmutableListMultimap<String, String> asMultiMap() {
        return this.namesAndValues;
    }

    public String get(String str) {
        ImmutableList<String> values = values(str);
        if (values.isEmpty()) {
            return null;
        }
        return (String) Iterables.getLast(values);
    }

    public ImmutableList<String> values(String str) {
        return this.namesAndValues.get((ImmutableListMultimap<String, String>) convertToStandardHeaderName(str));
    }

    private RtspHeaders(Builder builder) {
        this.namesAndValues = builder.namesAndValuesBuilder.build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String convertToStandardHeaderName(String str) {
        return Ascii.equalsIgnoreCase(str, "Accept") ? "Accept" : Ascii.equalsIgnoreCase(str, "Allow") ? "Allow" : Ascii.equalsIgnoreCase(str, "Authorization") ? "Authorization" : Ascii.equalsIgnoreCase(str, "Bandwidth") ? "Bandwidth" : Ascii.equalsIgnoreCase(str, "Blocksize") ? "Blocksize" : Ascii.equalsIgnoreCase(str, "Cache-Control") ? "Cache-Control" : Ascii.equalsIgnoreCase(str, "Connection") ? "Connection" : Ascii.equalsIgnoreCase(str, "Content-Base") ? "Content-Base" : Ascii.equalsIgnoreCase(str, "Content-Encoding") ? "Content-Encoding" : Ascii.equalsIgnoreCase(str, "Content-Language") ? "Content-Language" : Ascii.equalsIgnoreCase(str, "Content-Length") ? "Content-Length" : Ascii.equalsIgnoreCase(str, "Content-Location") ? "Content-Location" : Ascii.equalsIgnoreCase(str, "Content-Type") ? "Content-Type" : Ascii.equalsIgnoreCase(str, "CSeq") ? "CSeq" : Ascii.equalsIgnoreCase(str, "Date") ? "Date" : Ascii.equalsIgnoreCase(str, "Expires") ? "Expires" : Ascii.equalsIgnoreCase(str, "Location") ? "Location" : Ascii.equalsIgnoreCase(str, "Proxy-Authenticate") ? "Proxy-Authenticate" : Ascii.equalsIgnoreCase(str, "Proxy-Require") ? "Proxy-Require" : Ascii.equalsIgnoreCase(str, "Public") ? "Public" : Ascii.equalsIgnoreCase(str, "Range") ? "Range" : Ascii.equalsIgnoreCase(str, "RTP-Info") ? "RTP-Info" : Ascii.equalsIgnoreCase(str, "RTCP-Interval") ? "RTCP-Interval" : Ascii.equalsIgnoreCase(str, "Scale") ? "Scale" : Ascii.equalsIgnoreCase(str, "Session") ? "Session" : Ascii.equalsIgnoreCase(str, "Speed") ? "Speed" : Ascii.equalsIgnoreCase(str, "Supported") ? "Supported" : Ascii.equalsIgnoreCase(str, "Timestamp") ? "Timestamp" : Ascii.equalsIgnoreCase(str, "Transport") ? "Transport" : Ascii.equalsIgnoreCase(str, "User-Agent") ? "User-Agent" : Ascii.equalsIgnoreCase(str, "Via") ? "Via" : Ascii.equalsIgnoreCase(str, "WWW-Authenticate") ? "WWW-Authenticate" : str;
    }
}
