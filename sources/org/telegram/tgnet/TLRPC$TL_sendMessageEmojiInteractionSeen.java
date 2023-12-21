package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_sendMessageEmojiInteractionSeen extends TLRPC$SendMessageAction {
    public String emoticon;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.emoticon = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1234857938);
        abstractSerializedData.writeString(this.emoticon);
    }
}
