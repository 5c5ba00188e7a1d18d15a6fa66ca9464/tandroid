package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_userDeleted_old extends TLRPC$TL_userDeleted_old2 {
    public static int constructor = -1298475060;

    @Override // org.telegram.tgnet.TLRPC$TL_userDeleted_old2, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = abstractSerializedData.readInt32(z);
        this.first_name = abstractSerializedData.readString(z);
        this.last_name = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_userDeleted_old2, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.id);
        abstractSerializedData.writeString(this.first_name);
        abstractSerializedData.writeString(this.last_name);
    }
}
