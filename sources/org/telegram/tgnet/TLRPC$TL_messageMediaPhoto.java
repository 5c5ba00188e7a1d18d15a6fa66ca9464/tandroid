package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageMediaPhoto extends TLRPC$MessageMedia {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        int readInt32 = abstractSerializedData.readInt32(z);
        this.flags = readInt32;
        this.spoiler = (readInt32 & 8) != 0;
        if ((readInt32 & 1) != 0) {
            this.photo = TLRPC$Photo.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        } else {
            this.photo = new TLRPC$TL_photoEmpty();
        }
        if ((this.flags & 4) != 0) {
            this.ttl_seconds = abstractSerializedData.readInt32(z);
        }
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1766936791);
        int i = this.spoiler ? this.flags | 8 : this.flags & (-9);
        this.flags = i;
        abstractSerializedData.writeInt32(i);
        if ((this.flags & 1) != 0) {
            this.photo.serializeToStream(abstractSerializedData);
        }
        if ((this.flags & 4) != 0) {
            abstractSerializedData.writeInt32(this.ttl_seconds);
        }
    }
}
