package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_updateStory extends TLRPC$Update {
    public static int constructor = 1974712216;
    public TLRPC$Peer peer;
    public TLRPC$StoryItem story;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.peer = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.story = TLRPC$StoryItem.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.peer.serializeToStream(abstractSerializedData);
        this.story.serializeToStream(abstractSerializedData);
    }
}
