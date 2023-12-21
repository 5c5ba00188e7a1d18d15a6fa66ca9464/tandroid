package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_notificationSoundRingtone extends TLRPC$NotificationSound {
    public long id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-9666487);
        abstractSerializedData.writeInt64(this.id);
    }
}
