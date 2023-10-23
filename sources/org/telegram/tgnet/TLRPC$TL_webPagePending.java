package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_webPagePending extends TLRPC$WebPage {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.flags = abstractSerializedData.readInt32(z);
        this.id = abstractSerializedData.readInt64(z);
        if ((this.flags & 1) != 0) {
            this.url = abstractSerializedData.readString(z);
        }
        this.date = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1328464313);
        abstractSerializedData.writeInt32(this.flags);
        abstractSerializedData.writeInt64(this.id);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeString(this.url);
        }
        abstractSerializedData.writeInt32(this.date);
    }
}
