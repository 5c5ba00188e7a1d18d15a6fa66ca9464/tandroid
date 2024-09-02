package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_channels_updateEmojiStatus extends TLObject {
    public TLRPC$InputChannel channel;
    public TLRPC$EmojiStatus emoji_status;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-254548312);
        this.channel.serializeToStream(abstractSerializedData);
        this.emoji_status.serializeToStream(abstractSerializedData);
    }
}
