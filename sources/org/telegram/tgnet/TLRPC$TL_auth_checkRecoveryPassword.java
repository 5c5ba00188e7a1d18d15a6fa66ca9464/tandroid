package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_auth_checkRecoveryPassword extends TLObject {
    public String code;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(221691769);
        abstractSerializedData.writeString(this.code);
    }
}
