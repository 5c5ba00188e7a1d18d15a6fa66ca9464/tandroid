package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_phoneCallDiscardReasonBusy extends TLRPC$PhoneCallDiscardReason {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-84416311);
    }
}
