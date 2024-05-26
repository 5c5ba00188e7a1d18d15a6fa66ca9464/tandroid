package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_channels_searchPosts extends TLObject {
    public String hashtag;
    public int limit;
    public int offset_id;
    public TLRPC$InputPeer offset_peer;
    public int offset_rate;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_Messages.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-778069893);
        abstractSerializedData.writeString(this.hashtag);
        abstractSerializedData.writeInt32(this.offset_rate);
        this.offset_peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(this.offset_id);
        abstractSerializedData.writeInt32(this.limit);
    }
}
