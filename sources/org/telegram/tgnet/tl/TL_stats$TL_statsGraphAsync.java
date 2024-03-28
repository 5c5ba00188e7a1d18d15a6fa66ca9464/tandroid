package org.telegram.tgnet.tl;

import org.telegram.tgnet.AbstractSerializedData;
/* loaded from: classes3.dex */
public class TL_stats$TL_statsGraphAsync extends TL_stats$StatsGraph {
    public String token;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.token = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1244130093);
        abstractSerializedData.writeString(this.token);
    }
}
