package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_getEmojiURL extends TLObject {
    public String lang_code;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_emojiURL.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-709817306);
        abstractSerializedData.writeString(this.lang_code);
    }
}
