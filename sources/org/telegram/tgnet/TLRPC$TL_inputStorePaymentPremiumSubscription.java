package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputStorePaymentPremiumSubscription extends TLRPC$InputStorePaymentPurpose {
    public static int constructor = -1502273946;
    public int flags;
    public boolean restore;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        int readInt32 = abstractSerializedData.readInt32(z);
        this.flags = readInt32;
        this.restore = (readInt32 & 1) != 0;
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        int i = this.restore ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        abstractSerializedData.writeInt32(i);
    }
}
