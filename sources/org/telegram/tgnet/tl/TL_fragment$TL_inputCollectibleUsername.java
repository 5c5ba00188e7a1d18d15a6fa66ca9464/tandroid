package org.telegram.tgnet.tl;

import org.telegram.tgnet.AbstractSerializedData;
/* loaded from: classes3.dex */
public class TL_fragment$TL_inputCollectibleUsername extends TL_fragment$InputCollectible {
    public String username;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-476815191);
        abstractSerializedData.writeString(this.username);
    }

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.username = abstractSerializedData.readString(z);
    }
}
