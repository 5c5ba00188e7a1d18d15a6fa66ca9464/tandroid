package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputInvoiceChatInviteSubscription extends TLRPC$InputInvoice {
    public String hash;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.hash = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(887591921);
        abstractSerializedData.writeString(this.hash);
    }
}
