package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_simpleWebViewResultUrl extends TLObject {
    public String url;

    public static TLRPC$TL_simpleWebViewResultUrl TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        if (-2010155333 != i) {
            if (z) {
                throw new RuntimeException(String.format("can't parse magic %x in TL_simpleWebViewResultUrl", Integer.valueOf(i)));
            }
            return null;
        }
        TLRPC$TL_simpleWebViewResultUrl tLRPC$TL_simpleWebViewResultUrl = new TLRPC$TL_simpleWebViewResultUrl();
        tLRPC$TL_simpleWebViewResultUrl.readParams(abstractSerializedData, z);
        return tLRPC$TL_simpleWebViewResultUrl;
    }

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.url = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-2010155333);
        abstractSerializedData.writeString(this.url);
    }
}
