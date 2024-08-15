package com.google.android.exoplayer2.source.rtsp;

import android.net.Uri;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.common.collect.ImmutableList;
/* loaded from: classes.dex */
final class RtspTrackTiming {
    public final long rtpTimestamp;
    public final int sequenceNumber;
    public final Uri uri;

    /* JADX WARN: Removed duplicated region for block: B:25:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0082 A[Catch: Exception -> 0x008e, TRY_LEAVE, TryCatch #0 {Exception -> 0x008e, blocks: (B:7:0x0027, B:28:0x0070, B:29:0x0075, B:30:0x007a, B:31:0x007b, B:33:0x0082, B:14:0x0049, B:17:0x0053, B:20:0x005e), top: B:51:0x0027 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ImmutableList<RtspTrackTiming> parseTrackTiming(String str, Uri uri) throws ParserException {
        char c;
        ImmutableList.Builder builder = new ImmutableList.Builder();
        String[] split = Util.split(str, ",");
        int length = split.length;
        char c2 = 0;
        int i = 0;
        while (i < length) {
            String str2 = split[i];
            String[] split2 = Util.split(str2, ";");
            int length2 = split2.length;
            int i2 = 0;
            Uri uri2 = null;
            int i3 = -1;
            long j = -9223372036854775807L;
            while (i2 < length2) {
                String str3 = split2[i2];
                try {
                    String[] splitAtFirst = Util.splitAtFirst(str3, "=");
                    String str4 = splitAtFirst[c2];
                    String str5 = splitAtFirst[1];
                    int hashCode = str4.hashCode();
                    String[] strArr = split;
                    if (hashCode == 113759) {
                        if (str4.equals("seq")) {
                            c = 1;
                            if (c != 0) {
                            }
                            i2++;
                            split = strArr;
                            c2 = 0;
                        }
                        c = 65535;
                        if (c != 0) {
                        }
                        i2++;
                        split = strArr;
                        c2 = 0;
                    } else if (hashCode != 116079) {
                        if (hashCode == 1524180539 && str4.equals("rtptime")) {
                            c = 2;
                            if (c != 0) {
                                uri2 = resolveUri(str5, uri);
                            } else if (c == 1) {
                                i3 = Integer.parseInt(str5);
                            } else if (c == 2) {
                                j = Long.parseLong(str5);
                            } else {
                                throw ParserException.createForMalformedManifest(str4, null);
                            }
                            i2++;
                            split = strArr;
                            c2 = 0;
                        }
                        c = 65535;
                        if (c != 0) {
                        }
                        i2++;
                        split = strArr;
                        c2 = 0;
                    } else {
                        if (str4.equals("url")) {
                            c = 0;
                            if (c != 0) {
                            }
                            i2++;
                            split = strArr;
                            c2 = 0;
                        }
                        c = 65535;
                        if (c != 0) {
                        }
                        i2++;
                        split = strArr;
                        c2 = 0;
                    }
                } catch (Exception e) {
                    throw ParserException.createForMalformedManifest(str3, e);
                }
                throw ParserException.createForMalformedManifest(str3, e);
            }
            String[] strArr2 = split;
            if (uri2 != null && uri2.getScheme() != null) {
                long j2 = j;
                if (i3 != -1 || j2 != -9223372036854775807L) {
                    builder.add((ImmutableList.Builder) new RtspTrackTiming(j2, i3, uri2));
                    i++;
                    split = strArr2;
                    c2 = 0;
                }
            }
            throw ParserException.createForMalformedManifest(str2, null);
        }
        return builder.build();
    }

    static Uri resolveUri(String str, Uri uri) {
        Assertions.checkArgument(((String) Assertions.checkNotNull(uri.getScheme())).equals("rtsp"));
        Uri parse = Uri.parse(str);
        if (parse.isAbsolute()) {
            return parse;
        }
        Uri parse2 = Uri.parse("rtsp://" + str);
        String uri2 = uri.toString();
        if (((String) Assertions.checkNotNull(parse2.getHost())).equals(uri.getHost())) {
            return parse2;
        }
        if (uri2.endsWith("/")) {
            return UriUtil.resolveToUri(uri2, str);
        }
        return UriUtil.resolveToUri(uri2 + "/", str);
    }

    private RtspTrackTiming(long j, int i, Uri uri) {
        this.rtpTimestamp = j;
        this.sequenceNumber = i;
        this.uri = uri;
    }
}
