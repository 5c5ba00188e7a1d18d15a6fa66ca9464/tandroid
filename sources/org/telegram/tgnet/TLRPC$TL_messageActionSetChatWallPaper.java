package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionSetChatWallPaper extends TLRPC$MessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.wallpaper = TLRPC$WallPaper.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1136350937);
        this.wallpaper.serializeToStream(abstractSerializedData);
    }
}
