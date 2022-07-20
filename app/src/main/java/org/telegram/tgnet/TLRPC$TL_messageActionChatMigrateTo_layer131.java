package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionChatMigrateTo_layer131 extends TLRPC$TL_messageActionChatMigrateTo {
    public static int constructor = 1371385889;

    @Override // org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.channel_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_messageActionChatMigrateTo, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.channel_id);
    }
}
