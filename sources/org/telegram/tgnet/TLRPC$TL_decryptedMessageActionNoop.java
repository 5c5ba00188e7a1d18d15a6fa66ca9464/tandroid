package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_decryptedMessageActionNoop extends TLRPC$DecryptedMessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1473258141);
    }
}
