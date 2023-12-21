package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_payments_getPaymentForm extends TLObject {
    public int flags;
    public TLRPC$InputInvoice invoice;
    public TLRPC$TL_dataJSON theme_params;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_payments_paymentForm.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(924093883);
        abstractSerializedData.writeInt32(this.flags);
        this.invoice.serializeToStream(abstractSerializedData);
        if ((this.flags & 1) != 0) {
            this.theme_params.serializeToStream(abstractSerializedData);
        }
    }
}
