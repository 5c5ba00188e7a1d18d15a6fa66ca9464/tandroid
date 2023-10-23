package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channels_clickSponsoredMessage extends TLObject {
    public TLRPC$InputChannel channel;
    public byte[] random_id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(414170259);
        this.channel.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeByteArray(this.random_id);
    }
}
