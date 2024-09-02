package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputInvoicePremiumGiftCode extends TLRPC$InputInvoice {
    public TLRPC$TL_premiumGiftCodeOption option;
    public TLRPC$InputStorePaymentPurpose purpose;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.purpose = TLRPC$InputStorePaymentPurpose.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.option = TLRPC$TL_premiumGiftCodeOption.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1734841331);
        this.purpose.serializeToStream(abstractSerializedData);
        this.option.serializeToStream(abstractSerializedData);
    }
}
