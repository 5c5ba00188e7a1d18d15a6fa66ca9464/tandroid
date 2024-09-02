package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_channels_getChannelRecommendations extends TLObject {
    public TLRPC$InputChannel channel;
    public int flags;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_Chats.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(631707458);
        abstractSerializedData.writeInt32(this.flags);
        if ((this.flags & 1) != 0) {
            this.channel.serializeToStream(abstractSerializedData);
        }
    }
}
