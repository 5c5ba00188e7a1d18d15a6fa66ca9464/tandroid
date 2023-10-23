package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$UrlAuthResult extends TLObject {
    public static TLRPC$UrlAuthResult TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$UrlAuthResult tLRPC$TL_urlAuthResultAccepted;
        if (i == -1886646706) {
            tLRPC$TL_urlAuthResultAccepted = new TLRPC$TL_urlAuthResultAccepted();
        } else if (i != -1831650802) {
            tLRPC$TL_urlAuthResultAccepted = i != -1445536993 ? null : new TLRPC$UrlAuthResult() { // from class: org.telegram.tgnet.TLRPC$TL_urlAuthResultDefault
                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                    abstractSerializedData2.writeInt32(-1445536993);
                }
            };
        } else {
            tLRPC$TL_urlAuthResultAccepted = new TLRPC$TL_urlAuthResultRequest();
        }
        if (tLRPC$TL_urlAuthResultAccepted == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in UrlAuthResult", Integer.valueOf(i)));
        }
        if (tLRPC$TL_urlAuthResultAccepted != null) {
            tLRPC$TL_urlAuthResultAccepted.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_urlAuthResultAccepted;
    }
}
