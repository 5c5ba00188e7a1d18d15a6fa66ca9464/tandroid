package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_updateStarsBalance extends TLRPC$Update {
    public long balance;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.balance = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(263737752);
        abstractSerializedData.writeInt64(this.balance);
    }
}
