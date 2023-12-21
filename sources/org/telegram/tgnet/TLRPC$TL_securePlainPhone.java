package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_securePlainPhone extends TLRPC$SecurePlainData {
    public String phone;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.phone = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(2103482845);
        abstractSerializedData.writeString(this.phone);
    }
}
