package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputGeoPointEmpty extends TLRPC$InputGeoPoint {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-457104426);
    }
}
