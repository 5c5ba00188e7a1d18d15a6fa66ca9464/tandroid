package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_updateStory extends TLRPC$Update {
    public static int constructor = 542785843;
    public TLRPC$StoryItem story;
    public long user_id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt64(z);
        this.story = TLRPC$StoryItem.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt64(this.user_id);
        this.story.serializeToStream(abstractSerializedData);
    }
}
