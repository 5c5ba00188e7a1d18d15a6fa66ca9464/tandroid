package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messageActionChatJoinedByLink extends TLRPC$MessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.inviter_id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(51520707);
        abstractSerializedData.writeInt64(this.inviter_id);
    }
}
