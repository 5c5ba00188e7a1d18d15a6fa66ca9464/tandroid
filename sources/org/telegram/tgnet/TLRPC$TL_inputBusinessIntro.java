package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputBusinessIntro extends TLObject {
    public String description;
    public int flags;
    public TLRPC$InputDocument sticker;
    public String title;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.flags = abstractSerializedData.readInt32(z);
        this.title = abstractSerializedData.readString(z);
        this.description = abstractSerializedData.readString(z);
        if ((this.flags & 1) != 0) {
            this.sticker = TLRPC$InputDocument.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        }
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(163867085);
        abstractSerializedData.writeInt32(this.flags);
        abstractSerializedData.writeString(this.title);
        abstractSerializedData.writeString(this.description);
        if ((this.flags & 1) != 0) {
            this.sticker.serializeToStream(abstractSerializedData);
        }
    }
}
