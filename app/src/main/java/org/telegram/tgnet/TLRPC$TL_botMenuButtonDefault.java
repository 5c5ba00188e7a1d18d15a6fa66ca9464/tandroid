package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_botMenuButtonDefault extends TLRPC$BotMenuButton {
    public static int constructor = 1966318984;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
