package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channelParticipant_layer131 extends TLRPC$TL_channelParticipant {
    public static int constructor = 367766557;

    @Override // org.telegram.tgnet.TLRPC$TL_channelParticipant, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
        this.peer = tLRPC$TL_peerUser;
        tLRPC$TL_peerUser.user_id = abstractSerializedData.readInt32(z);
        this.date = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_channelParticipant, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.peer.user_id);
        abstractSerializedData.writeInt32(this.date);
    }
}
