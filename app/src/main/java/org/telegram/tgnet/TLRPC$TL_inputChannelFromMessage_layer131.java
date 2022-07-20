package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputChannelFromMessage_layer131 extends TLRPC$TL_inputChannelFromMessage {
    public static int constructor = 707290417;

    @Override // org.telegram.tgnet.TLRPC$TL_inputChannelFromMessage, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.peer = TLRPC$InputPeer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.msg_id = abstractSerializedData.readInt32(z);
        this.channel_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_inputChannelFromMessage, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(this.msg_id);
        abstractSerializedData.writeInt32((int) this.channel_id);
    }
}
