package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messageActionPaymentRefunded extends TLRPC$MessageAction {
    public TLRPC$TL_paymentCharge charge;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.flags = abstractSerializedData.readInt32(z);
        this.peer = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.currency = abstractSerializedData.readString(z);
        this.total_amount = abstractSerializedData.readInt64(z);
        if ((this.flags & 1) != 0) {
            this.payload = abstractSerializedData.readByteArray(z);
        }
        this.charge = TLRPC$TL_paymentCharge.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1102307842);
        abstractSerializedData.writeInt32(this.flags);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.currency);
        abstractSerializedData.writeInt64(this.total_amount);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeByteArray(this.payload);
        }
        this.charge.serializeToStream(abstractSerializedData);
    }
}
