package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channelParticipantModerator_layer67 extends TLRPC$TL_channelParticipantAdmin {
    public static int constructor = -1861910545;

    @Override // org.telegram.tgnet.TLRPC$TL_channelParticipantAdmin, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
        this.peer = tLRPC$TL_peerUser;
        tLRPC$TL_peerUser.user_id = abstractSerializedData.readInt32(z);
        this.inviter_id = abstractSerializedData.readInt32(z);
        this.date = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_channelParticipantAdmin, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.peer.user_id);
        abstractSerializedData.writeInt32((int) this.inviter_id);
        abstractSerializedData.writeInt32(this.date);
    }
}
