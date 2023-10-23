package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_phoneCallDiscardReasonHangup extends TLRPC$PhoneCallDiscardReason {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1471006352);
    }
}
