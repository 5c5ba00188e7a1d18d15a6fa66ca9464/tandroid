package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_attachMenuBotIconColor extends TLObject {
    public int color;
    public String name;

    public static TLRPC$TL_attachMenuBotIconColor TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        if (1165423600 != i) {
            if (z) {
                throw new RuntimeException(String.format("can't parse magic %x in TL_attachMenuBotIconColor", Integer.valueOf(i)));
            }
            return null;
        }
        TLRPC$TL_attachMenuBotIconColor tLRPC$TL_attachMenuBotIconColor = new TLRPC$TL_attachMenuBotIconColor();
        tLRPC$TL_attachMenuBotIconColor.readParams(abstractSerializedData, z);
        return tLRPC$TL_attachMenuBotIconColor;
    }

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.name = abstractSerializedData.readString(z);
        this.color = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1165423600);
        abstractSerializedData.writeString(this.name);
        abstractSerializedData.writeInt32(this.color);
    }
}
