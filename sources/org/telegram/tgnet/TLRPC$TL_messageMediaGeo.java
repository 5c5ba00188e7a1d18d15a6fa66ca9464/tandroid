package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messageMediaGeo extends TLRPC$MessageMedia {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.geo = TLRPC$GeoPoint.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1457575028);
        this.geo.serializeToStream(abstractSerializedData);
    }
}
