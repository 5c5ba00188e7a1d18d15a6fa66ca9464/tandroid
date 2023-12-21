package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_emojiStatusEmpty extends TLRPC$EmojiStatus {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(769727150);
    }
}
