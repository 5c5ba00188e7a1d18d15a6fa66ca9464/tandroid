package org.telegram.tgnet;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public abstract class TLRPC$messages_SavedReactionTags extends TLObject {
    public long hash;
    public ArrayList tags = new ArrayList();

    public static TLRPC$messages_SavedReactionTags TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$messages_SavedReactionTags tLRPC$TL_messages_savedReactionsTags = i != -2003084817 ? i != 844731658 ? null : new TLRPC$TL_messages_savedReactionsTags() : new TLRPC$messages_SavedReactionTags() { // from class: org.telegram.tgnet.TLRPC$TL_messages_savedReactionsTagsNotModified
            @Override // org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(-2003084817);
            }
        };
        if (tLRPC$TL_messages_savedReactionsTags == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in messages_SavedReactionTags", Integer.valueOf(i)));
        }
        if (tLRPC$TL_messages_savedReactionsTags != null) {
            tLRPC$TL_messages_savedReactionsTags.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_messages_savedReactionsTags;
    }
}
