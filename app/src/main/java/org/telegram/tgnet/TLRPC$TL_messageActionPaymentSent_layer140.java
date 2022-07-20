package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionPaymentSent_layer140 extends TLRPC$TL_messageActionPaymentSent {
    public static int constructor = 1080663248;

    @Override // org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.currency = abstractSerializedData.readString(z);
        this.total_amount = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.currency);
        abstractSerializedData.writeInt64(this.total_amount);
    }
}
