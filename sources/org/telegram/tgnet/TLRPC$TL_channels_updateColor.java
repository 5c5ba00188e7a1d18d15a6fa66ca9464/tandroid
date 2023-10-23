package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channels_updateColor extends TLObject {
    public long background_emoji_id;
    public TLRPC$InputChannel channel;
    public int color;
    public int flags;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1645879327);
        abstractSerializedData.writeInt32(this.flags);
        this.channel.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(this.color);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeInt64(this.background_emoji_id);
        }
    }
}
