package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channels_setStickers extends TLObject {
    public TLRPC$InputChannel channel;
    public TLRPC$InputStickerSet stickerset;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-359881479);
        this.channel.serializeToStream(abstractSerializedData);
        this.stickerset.serializeToStream(abstractSerializedData);
    }
}
