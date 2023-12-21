package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_photoSizeEmpty extends TLRPC$PhotoSize {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.type = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(236446268);
        abstractSerializedData.writeString(this.type);
    }
}
