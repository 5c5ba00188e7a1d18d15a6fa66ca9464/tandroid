package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_statsURL extends TLObject {
    public String url;

    public static TLRPC$TL_statsURL TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        if (1202287072 != i) {
            if (z) {
                throw new RuntimeException(String.format("can't parse magic %x in TL_statsURL", Integer.valueOf(i)));
            }
            return null;
        }
        TLRPC$TL_statsURL tLRPC$TL_statsURL = new TLRPC$TL_statsURL();
        tLRPC$TL_statsURL.readParams(abstractSerializedData, z);
        return tLRPC$TL_statsURL;
    }

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.url = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1202287072);
        abstractSerializedData.writeString(this.url);
    }
}
