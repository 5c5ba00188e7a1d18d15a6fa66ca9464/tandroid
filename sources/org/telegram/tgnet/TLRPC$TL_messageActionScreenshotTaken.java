package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionScreenshotTaken extends TLRPC$MessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1200788123);
    }
}
