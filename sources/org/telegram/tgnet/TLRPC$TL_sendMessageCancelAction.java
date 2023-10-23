package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_sendMessageCancelAction extends TLRPC$SendMessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-44119819);
    }
}
