package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_updateUser extends TLRPC$Update {
    public long user_id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(542282808);
        abstractSerializedData.writeInt64(this.user_id);
    }
}
