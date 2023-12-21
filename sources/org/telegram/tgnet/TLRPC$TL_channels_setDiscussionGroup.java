package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_channels_setDiscussionGroup extends TLObject {
    public TLRPC$InputChannel broadcast;
    public TLRPC$InputChannel group;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1079520178);
        this.broadcast.serializeToStream(abstractSerializedData);
        this.group.serializeToStream(abstractSerializedData);
    }
}
