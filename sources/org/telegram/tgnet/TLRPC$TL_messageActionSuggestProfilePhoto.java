package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionSuggestProfilePhoto extends TLRPC$MessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.photo = TLRPC$Photo.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1474192222);
        this.photo.serializeToStream(abstractSerializedData);
    }
}
