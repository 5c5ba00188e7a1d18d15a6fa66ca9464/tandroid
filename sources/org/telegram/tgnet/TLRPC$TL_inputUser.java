package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputUser extends TLRPC$InputUser {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt64(z);
        this.access_hash = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-233744186);
        abstractSerializedData.writeInt64(this.user_id);
        abstractSerializedData.writeInt64(this.access_hash);
    }
}
