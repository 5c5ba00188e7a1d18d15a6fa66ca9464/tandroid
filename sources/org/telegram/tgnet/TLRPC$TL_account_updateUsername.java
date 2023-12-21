package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_updateUsername extends TLObject {
    public String username;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$User.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1040964988);
        abstractSerializedData.writeString(this.username);
    }
}
