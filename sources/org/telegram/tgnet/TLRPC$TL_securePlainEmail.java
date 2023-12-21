package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_securePlainEmail extends TLRPC$SecurePlainData {
    public String email;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.email = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(569137759);
        abstractSerializedData.writeString(this.email);
    }
}
