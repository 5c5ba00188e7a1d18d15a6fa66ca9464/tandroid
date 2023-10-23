package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channels_leaveChannel extends TLObject {
    public TLRPC$InputChannel channel;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-130635115);
        this.channel.serializeToStream(abstractSerializedData);
    }
}
