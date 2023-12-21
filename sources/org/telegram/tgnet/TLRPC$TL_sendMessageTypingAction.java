package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_sendMessageTypingAction extends TLRPC$SendMessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(381645902);
    }
}
