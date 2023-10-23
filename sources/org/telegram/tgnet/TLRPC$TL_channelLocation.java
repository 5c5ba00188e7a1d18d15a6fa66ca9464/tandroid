package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channelLocation extends TLRPC$ChannelLocation {
    public String address;
    public TLRPC$GeoPoint geo_point;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.geo_point = TLRPC$GeoPoint.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.address = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(547062491);
        this.geo_point.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.address);
    }
}
