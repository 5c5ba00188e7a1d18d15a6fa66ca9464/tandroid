package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionChannelMigrateFrom_layer131 extends TLRPC$TL_messageActionChannelMigrateFrom {
    public static int constructor = -1336546578;

    @Override // org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.title = abstractSerializedData.readString(z);
        this.chat_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.title);
        abstractSerializedData.writeInt32((int) this.chat_id);
    }
}
