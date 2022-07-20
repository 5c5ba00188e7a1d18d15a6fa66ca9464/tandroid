package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionChatJoinedByLink_layer131 extends TLRPC$TL_messageActionChatJoinedByLink {
    public static int constructor = -123931160;

    @Override // org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.inviter_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_messageActionChatJoinedByLink, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.inviter_id);
    }
}
