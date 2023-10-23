package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channels_toggleAntiSpam extends TLObject {
    public TLRPC$InputChannel channel;
    public boolean enabled;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1760814315);
        this.channel.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeBool(this.enabled);
    }
}
