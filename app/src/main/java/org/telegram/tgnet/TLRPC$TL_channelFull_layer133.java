package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channelFull_layer133 extends TLRPC$ChatFull {
    public static int constructor = -374179305;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        int readInt32 = abstractSerializedData.readInt32(z);
        this.flags = readInt32;
        this.can_view_participants = (readInt32 & 8) != 0;
        this.can_set_username = (readInt32 & 64) != 0;
        this.can_set_stickers = (readInt32 & 128) != 0;
        this.hidden_prehistory = (readInt32 & 1024) != 0;
        this.can_set_location = (65536 & readInt32) != 0;
        this.has_scheduled = (524288 & readInt32) != 0;
        this.can_view_stats = (1048576 & readInt32) != 0;
        this.blocked = (readInt32 & 4194304) != 0;
        this.id = abstractSerializedData.readInt64(z);
        this.about = abstractSerializedData.readString(z);
        if ((this.flags & 1) != 0) {
            this.participants_count = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 2) != 0) {
            this.admins_count = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 4) != 0) {
            this.kicked_count = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 4) != 0) {
            this.banned_count = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 8192) != 0) {
            this.online_count = abstractSerializedData.readInt32(z);
        }
        this.read_inbox_max_id = abstractSerializedData.readInt32(z);
        this.read_outbox_max_id = abstractSerializedData.readInt32(z);
        this.unread_count = abstractSerializedData.readInt32(z);
        this.chat_photo = TLRPC$Photo.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.notify_settings = TLRPC$PeerNotifySettings.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        if ((this.flags & 8388608) != 0) {
            this.exported_invite = TLRPC$ExportedChatInvite.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        }
        int readInt322 = abstractSerializedData.readInt32(z);
        if (readInt322 != 481674261) {
            if (z) {
                throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
            }
            return;
        }
        int readInt323 = abstractSerializedData.readInt32(z);
        for (int i = 0; i < readInt323; i++) {
            TLRPC$BotInfo TLdeserialize = TLRPC$BotInfo.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            if (TLdeserialize == null) {
                return;
            }
            this.bot_info.add(TLdeserialize);
        }
        if ((this.flags & 16) != 0) {
            this.migrated_from_chat_id = abstractSerializedData.readInt64(z);
        }
        if ((this.flags & 16) != 0) {
            this.migrated_from_max_id = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 32) != 0) {
            this.pinned_msg_id = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 256) != 0) {
            this.stickerset = TLRPC$StickerSet.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        }
        if ((this.flags & 512) != 0) {
            this.available_min_id = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 2048) != 0) {
            this.folder_id = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 16384) != 0) {
            this.linked_chat_id = abstractSerializedData.readInt64(z);
        }
        if ((this.flags & 32768) != 0) {
            this.location = TLRPC$ChannelLocation.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        }
        if ((this.flags & 131072) != 0) {
            this.slowmode_seconds = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 262144) != 0) {
            this.slowmode_next_send_date = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 4096) != 0) {
            this.stats_dc = abstractSerializedData.readInt32(z);
        }
        this.pts = abstractSerializedData.readInt32(z);
        if ((this.flags & 2097152) != 0) {
            this.call = TLRPC$TL_inputGroupCall.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        }
        if ((this.flags & 16777216) != 0) {
            this.ttl_period = abstractSerializedData.readInt32(z);
        }
        if ((this.flags & 33554432) != 0) {
            int readInt324 = abstractSerializedData.readInt32(z);
            if (readInt324 != 481674261) {
                if (z) {
                    throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt324)));
                }
                return;
            }
            int readInt325 = abstractSerializedData.readInt32(z);
            for (int i2 = 0; i2 < readInt325; i2++) {
                this.pending_suggestions.add(abstractSerializedData.readString(z));
            }
        }
        if ((this.flags & 67108864) != 0) {
            this.groupcall_default_join_as = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        }
        if ((this.flags & 134217728) == 0) {
            return;
        }
        this.theme_emoticon = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        int i = this.can_view_participants ? this.flags | 8 : this.flags & (-9);
        this.flags = i;
        int i2 = this.can_set_username ? i | 64 : i & (-65);
        this.flags = i2;
        int i3 = this.can_set_stickers ? i2 | 128 : i2 & (-129);
        this.flags = i3;
        int i4 = this.hidden_prehistory ? i3 | 1024 : i3 & (-1025);
        this.flags = i4;
        int i5 = this.can_set_location ? i4 | 65536 : i4 & (-65537);
        this.flags = i5;
        int i6 = this.has_scheduled ? i5 | 524288 : i5 & (-524289);
        this.flags = i6;
        int i7 = this.can_view_stats ? i6 | 1048576 : i6 & (-1048577);
        this.flags = i7;
        int i8 = this.blocked ? i7 | 4194304 : i7 & (-4194305);
        this.flags = i8;
        abstractSerializedData.writeInt32(i8);
        abstractSerializedData.writeInt64(this.id);
        abstractSerializedData.writeString(this.about);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeInt32(this.participants_count);
        }
        if ((this.flags & 2) != 0) {
            abstractSerializedData.writeInt32(this.admins_count);
        }
        if ((this.flags & 4) != 0) {
            abstractSerializedData.writeInt32(this.kicked_count);
        }
        if ((this.flags & 4) != 0) {
            abstractSerializedData.writeInt32(this.banned_count);
        }
        if ((this.flags & 8192) != 0) {
            abstractSerializedData.writeInt32(this.online_count);
        }
        abstractSerializedData.writeInt32(this.read_inbox_max_id);
        abstractSerializedData.writeInt32(this.read_outbox_max_id);
        abstractSerializedData.writeInt32(this.unread_count);
        this.chat_photo.serializeToStream(abstractSerializedData);
        this.notify_settings.serializeToStream(abstractSerializedData);
        if ((this.flags & 8388608) != 0) {
            this.exported_invite.serializeToStream(abstractSerializedData);
        }
        abstractSerializedData.writeInt32(481674261);
        int size = this.bot_info.size();
        abstractSerializedData.writeInt32(size);
        for (int i9 = 0; i9 < size; i9++) {
            this.bot_info.get(i9).serializeToStream(abstractSerializedData);
        }
        if ((this.flags & 16) != 0) {
            abstractSerializedData.writeInt64(this.migrated_from_chat_id);
        }
        if ((this.flags & 16) != 0) {
            abstractSerializedData.writeInt32(this.migrated_from_max_id);
        }
        if ((this.flags & 32) != 0) {
            abstractSerializedData.writeInt32(this.pinned_msg_id);
        }
        if ((this.flags & 256) != 0) {
            this.stickerset.serializeToStream(abstractSerializedData);
        }
        if ((this.flags & 512) != 0) {
            abstractSerializedData.writeInt32(this.available_min_id);
        }
        if ((this.flags & 2048) != 0) {
            abstractSerializedData.writeInt32(this.folder_id);
        }
        if ((this.flags & 16384) != 0) {
            abstractSerializedData.writeInt64(this.linked_chat_id);
        }
        if ((this.flags & 32768) != 0) {
            this.location.serializeToStream(abstractSerializedData);
        }
        if ((this.flags & 131072) != 0) {
            abstractSerializedData.writeInt32(this.slowmode_seconds);
        }
        if ((this.flags & 262144) != 0) {
            abstractSerializedData.writeInt32(this.slowmode_next_send_date);
        }
        if ((this.flags & 4096) != 0) {
            abstractSerializedData.writeInt32(this.stats_dc);
        }
        abstractSerializedData.writeInt32(this.pts);
        if ((this.flags & 2097152) != 0) {
            this.call.serializeToStream(abstractSerializedData);
        }
        if ((this.flags & 16777216) != 0) {
            abstractSerializedData.writeInt32(this.ttl_period);
        }
        if ((this.flags & 33554432) != 0) {
            abstractSerializedData.writeInt32(481674261);
            int size2 = this.pending_suggestions.size();
            abstractSerializedData.writeInt32(size2);
            for (int i10 = 0; i10 < size2; i10++) {
                abstractSerializedData.writeString(this.pending_suggestions.get(i10));
            }
        }
        if ((this.flags & 67108864) != 0) {
            this.groupcall_default_join_as.serializeToStream(abstractSerializedData);
        }
        if ((this.flags & 134217728) != 0) {
            abstractSerializedData.writeString(this.theme_emoticon);
        }
    }
}
