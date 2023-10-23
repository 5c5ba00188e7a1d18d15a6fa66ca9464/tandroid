package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_sendMessageUploadDocumentAction extends TLRPC$SendMessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.progress = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1441998364);
        abstractSerializedData.writeInt32(this.progress);
    }
}
