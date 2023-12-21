package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_updateTheme extends TLRPC$Update {
    public TLRPC$Theme theme;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.theme = TLRPC$Theme.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-2112423005);
        this.theme.serializeToStream(abstractSerializedData);
    }
}
