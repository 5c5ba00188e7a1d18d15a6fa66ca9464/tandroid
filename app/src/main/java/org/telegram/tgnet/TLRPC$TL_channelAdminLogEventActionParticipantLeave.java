package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channelAdminLogEventActionParticipantLeave extends TLRPC$ChannelAdminLogEventAction {
    public static int constructor = -124291086;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
