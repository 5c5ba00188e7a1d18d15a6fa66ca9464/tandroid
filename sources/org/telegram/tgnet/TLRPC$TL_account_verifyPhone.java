package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_account_verifyPhone extends TLObject {
    public String phone_code;
    public String phone_code_hash;
    public String phone_number;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1305716726);
        abstractSerializedData.writeString(this.phone_number);
        abstractSerializedData.writeString(this.phone_code_hash);
        abstractSerializedData.writeString(this.phone_code);
    }
}
