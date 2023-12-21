package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputStorePaymentPremiumSubscription extends TLRPC$InputStorePaymentPurpose {
    public int flags;
    public boolean restore;
    public boolean upgrade;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        int readInt32 = abstractSerializedData.readInt32(z);
        this.flags = readInt32;
        this.restore = (readInt32 & 1) != 0;
        this.upgrade = (readInt32 & 2) != 0;
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1502273946);
        int i = this.restore ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        int i2 = this.upgrade ? i | 2 : i & (-3);
        this.flags = i2;
        abstractSerializedData.writeInt32(i2);
    }
}
