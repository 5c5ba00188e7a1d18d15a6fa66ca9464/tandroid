package org.telegram.messenger;
/* loaded from: classes3.dex */
public final /* synthetic */ class MessagesController$PeerColors$$ExternalSyntheticBackport0 {
    public static /* synthetic */ int m(String str, int i) {
        if (str.length() > 1 && str.charAt(0) == '+') {
            str = str.substring(1);
        }
        long parseLong = Long.parseLong(str, i);
        if ((4294967295L & parseLong) == parseLong) {
            return (int) parseLong;
        }
        throw new NumberFormatException("Input " + str + " in base " + i + " is not in the range of an unsigned integer");
    }
}
