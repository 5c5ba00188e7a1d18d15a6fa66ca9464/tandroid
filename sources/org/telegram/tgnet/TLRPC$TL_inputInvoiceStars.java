package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputInvoiceStars extends TLRPC$InputInvoice {
    public static int constructor = 497236696;
    public TLRPC$TL_starsTopupOption option;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.option = TLRPC$TL_starsTopupOption.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.option.serializeToStream(abstractSerializedData);
    }
}
