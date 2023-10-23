package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$TopPeerCategory extends TLObject {
    public static TLRPC$TopPeerCategory TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$TopPeerCategory tLRPC$TopPeerCategory;
        switch (i) {
            case -1472172887:
                tLRPC$TopPeerCategory = new TLRPC$TopPeerCategory() { // from class: org.telegram.tgnet.TLRPC$TL_topPeerCategoryForwardUsers
                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1472172887);
                    }
                };
                break;
            case -1419371685:
                tLRPC$TopPeerCategory = new TLRPC$TopPeerCategory() { // from class: org.telegram.tgnet.TLRPC$TL_topPeerCategoryBotsPM
                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1419371685);
                    }
                };
                break;
            case -1122524854:
                tLRPC$TopPeerCategory = new TLRPC$TopPeerCategory() { // from class: org.telegram.tgnet.TLRPC$TL_topPeerCategoryGroups
                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-1122524854);
                    }
                };
                break;
            case -68239120:
                tLRPC$TopPeerCategory = new TLRPC$TopPeerCategory() { // from class: org.telegram.tgnet.TLRPC$TL_topPeerCategoryForwardChats
                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(-68239120);
                    }
                };
                break;
            case 104314861:
                tLRPC$TopPeerCategory = new TLRPC$TL_topPeerCategoryCorrespondents();
                break;
            case 344356834:
                tLRPC$TopPeerCategory = new TLRPC$TL_topPeerCategoryBotsInline();
                break;
            case 371037736:
                tLRPC$TopPeerCategory = new TLRPC$TopPeerCategory() { // from class: org.telegram.tgnet.TLRPC$TL_topPeerCategoryChannels
                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(371037736);
                    }
                };
                break;
            case 511092620:
                tLRPC$TopPeerCategory = new TLRPC$TopPeerCategory() { // from class: org.telegram.tgnet.TLRPC$TL_topPeerCategoryPhoneCalls
                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                        abstractSerializedData2.writeInt32(511092620);
                    }
                };
                break;
            default:
                tLRPC$TopPeerCategory = null;
                break;
        }
        if (tLRPC$TopPeerCategory == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in TopPeerCategory", Integer.valueOf(i)));
        }
        if (tLRPC$TopPeerCategory != null) {
            tLRPC$TopPeerCategory.readParams(abstractSerializedData, z);
        }
        return tLRPC$TopPeerCategory;
    }
}
