package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_payments_validateRequestedInfo extends TLObject {
    public int flags;
    public TLRPC$TL_paymentRequestedInfo info;
    public TLRPC$InputInvoice invoice;
    public boolean save;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_payments_validatedRequestedInfo.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1228345045);
        int i = this.save ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        abstractSerializedData.writeInt32(i);
        this.invoice.serializeToStream(abstractSerializedData);
        this.info.serializeToStream(abstractSerializedData);
    }
}
