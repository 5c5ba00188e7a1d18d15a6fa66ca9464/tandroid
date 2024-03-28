package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_channels_restrictSponsoredMessages extends TLObject {
    public TLRPC$InputChannel channel;
    public boolean restricted;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1696000743);
        this.channel.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeBool(this.restricted);
    }
}
