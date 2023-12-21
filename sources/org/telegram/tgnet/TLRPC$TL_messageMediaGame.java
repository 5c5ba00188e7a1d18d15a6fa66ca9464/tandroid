package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messageMediaGame extends TLRPC$MessageMedia {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.game = TLRPC$TL_game.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-38694904);
        this.game.serializeToStream(abstractSerializedData);
    }
}
