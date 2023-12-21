package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_payments_paymentResult extends TLRPC$payments_PaymentResult {
    public TLRPC$Updates updates;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.updates = TLRPC$Updates.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1314881805);
        this.updates.serializeToStream(abstractSerializedData);
    }
}
