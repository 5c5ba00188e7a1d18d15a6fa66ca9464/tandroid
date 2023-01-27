package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_sendBotRequestedPeer extends TLObject {
    public static int constructor = -29831141;
    public int button_id;
    public int msg_id;
    public TLRPC$InputPeer peer;
    public TLRPC$InputPeer requested_peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(this.msg_id);
        abstractSerializedData.writeInt32(this.button_id);
        this.requested_peer.serializeToStream(abstractSerializedData);
    }
}
