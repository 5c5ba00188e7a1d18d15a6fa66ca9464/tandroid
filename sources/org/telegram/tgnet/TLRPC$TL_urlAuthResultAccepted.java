package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_urlAuthResultAccepted extends TLRPC$UrlAuthResult {
    public String url;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.url = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1886646706);
        abstractSerializedData.writeString(this.url);
    }
}
