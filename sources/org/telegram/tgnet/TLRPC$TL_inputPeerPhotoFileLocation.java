package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputPeerPhotoFileLocation extends TLRPC$InputFileLocation {
    public boolean big;
    public TLRPC$InputPeer peer;
    public long photo_id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        int readInt32 = abstractSerializedData.readInt32(z);
        this.flags = readInt32;
        this.big = (readInt32 & 1) != 0;
        this.peer = TLRPC$InputPeer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.photo_id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(925204121);
        int i = this.big ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        abstractSerializedData.writeInt32(i);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt64(this.photo_id);
    }
}
