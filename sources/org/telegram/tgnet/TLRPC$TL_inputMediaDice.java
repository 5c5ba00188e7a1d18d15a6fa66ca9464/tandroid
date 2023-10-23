package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputMediaDice extends TLRPC$InputMedia {
    public String emoticon;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.emoticon = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-428884101);
        abstractSerializedData.writeString(this.emoticon);
    }
}
