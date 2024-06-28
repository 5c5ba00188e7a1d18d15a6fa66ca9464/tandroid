package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_simpleWebViewResultUrl extends TLObject {
    public String url;

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
