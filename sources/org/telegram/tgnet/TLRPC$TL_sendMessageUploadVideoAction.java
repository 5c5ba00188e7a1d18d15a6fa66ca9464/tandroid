package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_sendMessageUploadVideoAction extends TLRPC$SendMessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.progress = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-378127636);
        abstractSerializedData.writeInt32(this.progress);
    }
}
