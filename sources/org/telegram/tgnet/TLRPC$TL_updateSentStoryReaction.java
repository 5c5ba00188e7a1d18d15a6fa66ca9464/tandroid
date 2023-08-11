package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_updateSentStoryReaction extends TLRPC$Update {
    public static int constructor = -475579104;
    public TLRPC$Reaction reaction;
    public int story_id;
    public long user_id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt64(z);
        this.story_id = abstractSerializedData.readInt32(z);
        this.reaction = TLRPC$Reaction.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt64(this.user_id);
        abstractSerializedData.writeInt32(this.story_id);
        this.reaction.serializeToStream(abstractSerializedData);
    }
}
