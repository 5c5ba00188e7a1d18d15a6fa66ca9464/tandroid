package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputReportReasonGeoIrrelevant extends TLRPC$ReportReason {
    public static int constructor = -606798099;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
