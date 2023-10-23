package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_account_sendChangePhoneCode extends TLObject {
    public String phone_number;
    public TLRPC$TL_codeSettings settings;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$auth_SentCode.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-2108208411);
        abstractSerializedData.writeString(this.phone_number);
        this.settings.serializeToStream(abstractSerializedData);
    }
}
