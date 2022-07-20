package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_sendMessageUploadVideoAction_old extends TLRPC$TL_sendMessageUploadVideoAction {
    public static int constructor = -1845219337;

    @Override // org.telegram.tgnet.TLRPC$TL_sendMessageUploadVideoAction, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
    }

    @Override // org.telegram.tgnet.TLRPC$TL_sendMessageUploadVideoAction, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
