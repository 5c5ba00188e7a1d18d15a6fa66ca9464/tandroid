package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messagePeerReaction_layer137 extends TLRPC$TL_messagePeerReaction {
    public static int constructor = -1826077446;
    public long user_id;

    @Override // org.telegram.tgnet.TLRPC$TL_messagePeerReaction, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt64(z);
        this.reaction = abstractSerializedData.readString(z);
        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
        this.peer_id = tLRPC$TL_peerUser;
        tLRPC$TL_peerUser.user_id = this.user_id;
    }

    @Override // org.telegram.tgnet.TLRPC$TL_messagePeerReaction, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt64(this.user_id);
        abstractSerializedData.writeString(this.reaction);
    }
}
