package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_auth_checkPassword extends TLObject {
    public TLRPC$InputCheckPasswordSRP password;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$auth_Authorization.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-779399914);
        this.password.serializeToStream(abstractSerializedData);
    }
}
