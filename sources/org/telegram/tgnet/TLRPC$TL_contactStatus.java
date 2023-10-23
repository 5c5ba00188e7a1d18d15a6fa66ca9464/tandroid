package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_contactStatus extends TLObject {
    public TLRPC$UserStatus status;
    public long user_id;

    public static TLRPC$TL_contactStatus TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        if (383348795 != i) {
            if (z) {
                throw new RuntimeException(String.format("can't parse magic %x in TL_contactStatus", Integer.valueOf(i)));
            }
            return null;
        }
        TLRPC$TL_contactStatus tLRPC$TL_contactStatus = new TLRPC$TL_contactStatus();
        tLRPC$TL_contactStatus.readParams(abstractSerializedData, z);
        return tLRPC$TL_contactStatus;
    }

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt64(z);
        this.status = TLRPC$UserStatus.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(383348795);
        abstractSerializedData.writeInt64(this.user_id);
        this.status.serializeToStream(abstractSerializedData);
    }
}
