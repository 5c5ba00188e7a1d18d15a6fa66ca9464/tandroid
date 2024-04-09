package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_channels_getChannelRecommendations extends TLObject {
    public static int constructor = 631707458;
    public TLRPC$InputChannel channel;
    public int flags;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_Chats.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.flags);
        if ((this.flags & 1) != 0) {
            this.channel.serializeToStream(abstractSerializedData);
        }
    }
}
