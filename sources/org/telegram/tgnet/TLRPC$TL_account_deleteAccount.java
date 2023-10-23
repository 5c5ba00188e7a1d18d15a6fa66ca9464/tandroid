package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_account_deleteAccount extends TLObject {
    public String reason;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1099779595);
        abstractSerializedData.writeString(this.reason);
    }
}
