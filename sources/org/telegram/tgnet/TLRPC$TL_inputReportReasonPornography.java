package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputReportReasonPornography extends TLRPC$ReportReason {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(777640226);
    }
}
