package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$InlineQueryPeerType extends TLObject {
    public static TLRPC$InlineQueryPeerType TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$InlineQueryPeerType tLRPC$InlineQueryPeerType;
        switch (i) {
            case -2093215828:
                tLRPC$InlineQueryPeerType = new TLRPC$InlineQueryPeerType() { // from class: org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypePM
                    public static int constructor = -2093215828;

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(constructor);
                    }
                };
                break;
            case -681130742:
                tLRPC$InlineQueryPeerType = new TLRPC$InlineQueryPeerType() { // from class: org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypeChat
                    public static int constructor = -681130742;

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(constructor);
                    }
                };
                break;
            case 238759180:
                tLRPC$InlineQueryPeerType = new TLRPC$InlineQueryPeerType() { // from class: org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypeBotPM
                    public static int constructor = 238759180;

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(constructor);
                    }
                };
                break;
            case 813821341:
                tLRPC$InlineQueryPeerType = new TLRPC$InlineQueryPeerType() { // from class: org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypeSameBotPM
                    public static int constructor = 813821341;

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(constructor);
                    }
                };
                break;
            case 1589952067:
                tLRPC$InlineQueryPeerType = new TLRPC$InlineQueryPeerType() { // from class: org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypeMegagroup
                    public static int constructor = 1589952067;

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(constructor);
                    }
                };
                break;
            case 1664413338:
                tLRPC$InlineQueryPeerType = new TLRPC$InlineQueryPeerType() { // from class: org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypeBroadcast
                    public static int constructor = 1664413338;

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(constructor);
                    }
                };
                break;
            default:
                tLRPC$InlineQueryPeerType = null;
                break;
        }
        if (tLRPC$InlineQueryPeerType == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in InlineQueryPeerType", Integer.valueOf(i)));
        }
        if (tLRPC$InlineQueryPeerType != null) {
            tLRPC$InlineQueryPeerType.readParams(abstractSerializedData, z);
        }
        return tLRPC$InlineQueryPeerType;
    }
}
