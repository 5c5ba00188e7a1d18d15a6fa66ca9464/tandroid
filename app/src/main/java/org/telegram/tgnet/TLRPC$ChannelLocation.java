package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$ChannelLocation extends TLObject {
    public static TLRPC$ChannelLocation TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$ChannelLocation tLRPC$ChannelLocation;
        if (i == -1078612597) {
            tLRPC$ChannelLocation = new TLRPC$TL_channelLocationEmpty();
        } else {
            tLRPC$ChannelLocation = i != 547062491 ? null : new TLRPC$TL_channelLocation();
        }
        if (tLRPC$ChannelLocation != null || !z) {
            if (tLRPC$ChannelLocation != null) {
                tLRPC$ChannelLocation.readParams(abstractSerializedData, z);
            }
            return tLRPC$ChannelLocation;
        }
        throw new RuntimeException(String.format("can't parse magic %x in ChannelLocation", Integer.valueOf(i)));
    }
}
