package org.telegram.tgnet;
/* loaded from: classes3.dex */
public abstract class TLRPC$ChannelLocation extends TLObject {
    public static TLRPC$ChannelLocation TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$ChannelLocation tLRPC$TL_channelLocation = i != -1078612597 ? i != 547062491 ? null : new TLRPC$TL_channelLocation() : new TLRPC$ChannelLocation() { // from class: org.telegram.tgnet.TLRPC$TL_channelLocationEmpty
            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(-1078612597);
            }
        };
        if (tLRPC$TL_channelLocation == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in ChannelLocation", Integer.valueOf(i)));
        }
        if (tLRPC$TL_channelLocation != null) {
            tLRPC$TL_channelLocation.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_channelLocation;
    }
}
