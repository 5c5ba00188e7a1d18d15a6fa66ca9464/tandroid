package org.telegram.tgnet.tl;

import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputChannel;
import org.telegram.tgnet.TLRPC$InputCheckPasswordSRP;
/* loaded from: classes3.dex */
public class TL_stats$TL_getBroadcastRevenueWithdrawalUrl extends TLObject {
    public TLRPC$InputChannel channel;
    public TLRPC$InputCheckPasswordSRP password;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TL_stats$TL_broadcastRevenueWithdrawalUrl.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(711323507);
        this.channel.serializeToStream(abstractSerializedData);
        this.password.serializeToStream(abstractSerializedData);
    }
}
