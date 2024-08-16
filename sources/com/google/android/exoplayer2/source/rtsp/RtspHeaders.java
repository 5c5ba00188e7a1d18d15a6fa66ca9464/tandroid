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
    public static final RtspHeaders EMPTY = new Builder().build();
    private final ImmutableListMultimap<String, String> namesAndValues;

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
        if (Ascii.equalsIgnoreCase(str, "Accept")) {
            return "Accept";
        }
        if (Ascii.equalsIgnoreCase(str, "Allow")) {
            return "Allow";
        }
        if (Ascii.equalsIgnoreCase(str, "Authorization")) {
            return "Authorization";
        }
        if (Ascii.equalsIgnoreCase(str, "Bandwidth")) {
            return "Bandwidth";
        }
        if (Ascii.equalsIgnoreCase(str, "Blocksize")) {
            return "Blocksize";
        }
        if (Ascii.equalsIgnoreCase(str, "Cache-Control")) {
            return "Cache-Control";
        }
        if (Ascii.equalsIgnoreCase(str, "Connection")) {
            return "Connection";
        }
        if (Ascii.equalsIgnoreCase(str, "Content-Base")) {
            return "Content-Base";
        }
        if (Ascii.equalsIgnoreCase(str, "Content-Encoding")) {
            return "Content-Encoding";
        }
        if (Ascii.equalsIgnoreCase(str, "Content-Language")) {
            return "Content-Language";
        }
        if (Ascii.equalsIgnoreCase(str, "Content-Length")) {
            return "Content-Length";
        }
        if (Ascii.equalsIgnoreCase(str, "Content-Location")) {
            return "Content-Location";
        }
        if (Ascii.equalsIgnoreCase(str, "Content-Type")) {
            return "Content-Type";
        }
        if (Ascii.equalsIgnoreCase(str, "CSeq")) {
            return "CSeq";
        }
        if (Ascii.equalsIgnoreCase(str, "Date")) {
            return "Date";
        }
        if (Ascii.equalsIgnoreCase(str, "Expires")) {
            return "Expires";
        }
        if (Ascii.equalsIgnoreCase(str, "Location")) {
            return "Location";
        }
        if (Ascii.equalsIgnoreCase(str, "Proxy-Authenticate")) {
            return "Proxy-Authenticate";
        }
        if (Ascii.equalsIgnoreCase(str, "Proxy-Require")) {
            return "Proxy-Require";
        }
        if (Ascii.equalsIgnoreCase(str, "Public")) {
            return "Public";
        }
        if (Ascii.equalsIgnoreCase(str, "Range")) {
            return "Range";
        }
        if (Ascii.equalsIgnoreCase(str, "RTP-Info")) {
            return "RTP-Info";
        }
        if (Ascii.equalsIgnoreCase(str, "RTCP-Interval")) {
            return "RTCP-Interval";
        }
        if (Ascii.equalsIgnoreCase(str, "Scale")) {
            return "Scale";
        }
        if (Ascii.equalsIgnoreCase(str, "Session")) {
            return "Session";
        }
        if (Ascii.equalsIgnoreCase(str, "Speed")) {
            return "Speed";
        }
        if (Ascii.equalsIgnoreCase(str, "Supported")) {
            return "Supported";
        }
        if (Ascii.equalsIgnoreCase(str, "Timestamp")) {
            return "Timestamp";
        }
        if (Ascii.equalsIgnoreCase(str, "Transport")) {
            return "Transport";
        }
        if (Ascii.equalsIgnoreCase(str, "User-Agent")) {
            return "User-Agent";
        }
        if (Ascii.equalsIgnoreCase(str, "Via")) {
            return "Via";
        }
        return Ascii.equalsIgnoreCase(str, "WWW-Authenticate") ? "WWW-Authenticate" : str;
    }
}
