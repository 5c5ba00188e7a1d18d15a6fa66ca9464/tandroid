package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_channels_toggleViewForumAsMessages extends TLObject {
    public TLRPC$InputChannel channel_id;
    public boolean enabled;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1757889771);
        this.channel_id.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeBool(this.enabled);
    }
}
