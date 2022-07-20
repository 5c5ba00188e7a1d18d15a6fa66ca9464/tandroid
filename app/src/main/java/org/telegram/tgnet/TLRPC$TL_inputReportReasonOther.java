package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputReportReasonOther extends TLRPC$ReportReason {
    public static int constructor = -1041980751;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
