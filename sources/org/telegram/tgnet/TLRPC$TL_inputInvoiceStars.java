package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputInvoiceStars extends TLRPC$InputInvoice {
    public TLRPC$InputStorePaymentPurpose purpose;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.purpose = TLRPC$InputStorePaymentPurpose.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1710230755);
        this.purpose.serializeToStream(abstractSerializedData);
    }
}
