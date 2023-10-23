package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_webPageUrlPending extends TLRPC$WebPage {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.url = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-736472729);
        abstractSerializedData.writeString(this.url);
    }
}
