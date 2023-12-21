package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messageActionChatMigrateTo extends TLRPC$MessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.channel_id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-519864430);
        abstractSerializedData.writeInt64(this.channel_id);
    }
}
