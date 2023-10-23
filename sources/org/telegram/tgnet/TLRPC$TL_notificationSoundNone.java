package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_notificationSoundNone extends TLRPC$NotificationSound {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1863070943);
    }
}
