package org.telegram.tgnet;

import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class TLRPC$Config extends TLObject {
    public String autoupdate_url_prefix;
    public int base_lang_pack_version;
    public boolean blocked_mode;
    public int call_connect_timeout_ms;
    public int call_packet_timeout_ms;
    public int call_receive_timeout_ms;
    public int call_ring_timeout_ms;
    public int caption_length_max;
    public int channels_read_media_period;
    public int chat_size_max;
    public int date;
    public ArrayList<TLRPC$TL_dcOption> dc_options = new ArrayList<>();
    public String dc_txt_domain_name;
    public boolean default_p2p_contacts;
    public int edit_time_limit;
    public int expires;
    public int flags;
    public boolean force_try_ipv6;
    public int forwarded_count_max;
    public String gif_search_username;
    public boolean ignore_phone_entities;
    public String img_search_username;
    public int lang_pack_version;
    public String me_url_prefix;
    public int megagroup_size_max;
    public int message_length_max;
    public int notify_cloud_delay_ms;
    public int notify_default_delay_ms;
    public int offline_blur_timeout_ms;
    public int offline_idle_timeout_ms;
    public int online_cloud_timeout_ms;
    public int online_update_period_ms;
    public boolean pfs_enabled;
    public boolean phonecalls_enabled;
    public int pinned_dialogs_count_max;
    public int pinned_infolder_count_max;
    public boolean preload_featured_stickers;
    public int push_chat_limit;
    public int push_chat_period_ms;
    public int rating_e_decay;
    public TLRPC$Reaction reactions_default;
    public boolean revoke_pm_inbox;
    public int revoke_pm_time_limit;
    public int revoke_time_limit;
    public int saved_gifs_limit;
    public String static_maps_provider;
    public int stickers_faved_limit;
    public int stickers_recent_limit;
    public String suggested_lang_code;
    public boolean test_mode;
    public int this_dc;
    public int tmp_sessions;
    public String venue_search_username;
    public int webfile_dc_id;

    public static TLRPC$Config TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$Config tLRPC$Config;
        if (i == 589653676) {
            tLRPC$Config = new TLRPC$Config() { // from class: org.telegram.tgnet.TLRPC$TL_config
                public static int constructor = 589653676;

                @Override // org.telegram.tgnet.TLObject
                public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                    int readInt32 = abstractSerializedData2.readInt32(z2);
                    this.flags = readInt32;
                    this.phonecalls_enabled = (readInt32 & 2) != 0;
                    this.default_p2p_contacts = (readInt32 & 8) != 0;
                    this.preload_featured_stickers = (readInt32 & 16) != 0;
                    this.ignore_phone_entities = (readInt32 & 32) != 0;
                    this.revoke_pm_inbox = (readInt32 & 64) != 0;
                    this.blocked_mode = (readInt32 & 256) != 0;
                    this.pfs_enabled = (readInt32 & 8192) != 0;
                    this.force_try_ipv6 = (readInt32 & 16384) != 0;
                    this.date = abstractSerializedData2.readInt32(z2);
                    this.expires = abstractSerializedData2.readInt32(z2);
                    this.test_mode = abstractSerializedData2.readBool(z2);
                    this.this_dc = abstractSerializedData2.readInt32(z2);
                    int readInt322 = abstractSerializedData2.readInt32(z2);
                    if (readInt322 != 481674261) {
                        if (z2) {
                            throw new RuntimeException(String.format("wrong Vector magic, got %x", Integer.valueOf(readInt322)));
                        }
                        return;
                    }
                    int readInt323 = abstractSerializedData2.readInt32(z2);
                    for (int i2 = 0; i2 < readInt323; i2++) {
                        TLRPC$TL_dcOption TLdeserialize = TLRPC$TL_dcOption.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                        if (TLdeserialize == null) {
                            return;
                        }
                        this.dc_options.add(TLdeserialize);
                    }
                    this.dc_txt_domain_name = abstractSerializedData2.readString(z2);
                    this.chat_size_max = abstractSerializedData2.readInt32(z2);
                    this.megagroup_size_max = abstractSerializedData2.readInt32(z2);
                    this.forwarded_count_max = abstractSerializedData2.readInt32(z2);
                    this.online_update_period_ms = abstractSerializedData2.readInt32(z2);
                    this.offline_blur_timeout_ms = abstractSerializedData2.readInt32(z2);
                    this.offline_idle_timeout_ms = abstractSerializedData2.readInt32(z2);
                    this.online_cloud_timeout_ms = abstractSerializedData2.readInt32(z2);
                    this.notify_cloud_delay_ms = abstractSerializedData2.readInt32(z2);
                    this.notify_default_delay_ms = abstractSerializedData2.readInt32(z2);
                    this.push_chat_period_ms = abstractSerializedData2.readInt32(z2);
                    this.push_chat_limit = abstractSerializedData2.readInt32(z2);
                    this.saved_gifs_limit = abstractSerializedData2.readInt32(z2);
                    this.edit_time_limit = abstractSerializedData2.readInt32(z2);
                    this.revoke_time_limit = abstractSerializedData2.readInt32(z2);
                    this.revoke_pm_time_limit = abstractSerializedData2.readInt32(z2);
                    this.rating_e_decay = abstractSerializedData2.readInt32(z2);
                    this.stickers_recent_limit = abstractSerializedData2.readInt32(z2);
                    this.stickers_faved_limit = abstractSerializedData2.readInt32(z2);
                    this.channels_read_media_period = abstractSerializedData2.readInt32(z2);
                    if ((this.flags & 1) != 0) {
                        this.tmp_sessions = abstractSerializedData2.readInt32(z2);
                    }
                    this.pinned_dialogs_count_max = abstractSerializedData2.readInt32(z2);
                    this.pinned_infolder_count_max = abstractSerializedData2.readInt32(z2);
                    this.call_receive_timeout_ms = abstractSerializedData2.readInt32(z2);
                    this.call_ring_timeout_ms = abstractSerializedData2.readInt32(z2);
                    this.call_connect_timeout_ms = abstractSerializedData2.readInt32(z2);
                    this.call_packet_timeout_ms = abstractSerializedData2.readInt32(z2);
                    this.me_url_prefix = abstractSerializedData2.readString(z2);
                    if ((this.flags & ConnectionsManager.RequestFlagNeedQuickAck) != 0) {
                        this.autoupdate_url_prefix = abstractSerializedData2.readString(z2);
                    }
                    if ((this.flags & 512) != 0) {
                        this.gif_search_username = abstractSerializedData2.readString(z2);
                    }
                    if ((this.flags & 1024) != 0) {
                        this.venue_search_username = abstractSerializedData2.readString(z2);
                    }
                    if ((this.flags & 2048) != 0) {
                        this.img_search_username = abstractSerializedData2.readString(z2);
                    }
                    if ((this.flags & 4096) != 0) {
                        this.static_maps_provider = abstractSerializedData2.readString(z2);
                    }
                    this.caption_length_max = abstractSerializedData2.readInt32(z2);
                    this.message_length_max = abstractSerializedData2.readInt32(z2);
                    this.webfile_dc_id = abstractSerializedData2.readInt32(z2);
                    if ((this.flags & 4) != 0) {
                        this.suggested_lang_code = abstractSerializedData2.readString(z2);
                    }
                    if ((this.flags & 4) != 0) {
                        this.lang_pack_version = abstractSerializedData2.readInt32(z2);
                    }
                    if ((this.flags & 4) != 0) {
                        this.base_lang_pack_version = abstractSerializedData2.readInt32(z2);
                    }
                    if ((this.flags & 32768) == 0) {
                        return;
                    }
                    this.reactions_default = TLRPC$Reaction.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                }

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                    abstractSerializedData2.writeInt32(constructor);
                    int i2 = this.phonecalls_enabled ? this.flags | 2 : this.flags & (-3);
                    this.flags = i2;
                    int i3 = this.default_p2p_contacts ? i2 | 8 : i2 & (-9);
                    this.flags = i3;
                    int i4 = this.preload_featured_stickers ? i3 | 16 : i3 & (-17);
                    this.flags = i4;
                    int i5 = this.ignore_phone_entities ? i4 | 32 : i4 & (-33);
                    this.flags = i5;
                    int i6 = this.revoke_pm_inbox ? i5 | 64 : i5 & (-65);
                    this.flags = i6;
                    int i7 = this.blocked_mode ? i6 | 256 : i6 & (-257);
                    this.flags = i7;
                    int i8 = this.pfs_enabled ? i7 | 8192 : i7 & (-8193);
                    this.flags = i8;
                    int i9 = this.force_try_ipv6 ? i8 | 16384 : i8 & (-16385);
                    this.flags = i9;
                    abstractSerializedData2.writeInt32(i9);
                    abstractSerializedData2.writeInt32(this.date);
                    abstractSerializedData2.writeInt32(this.expires);
                    abstractSerializedData2.writeBool(this.test_mode);
                    abstractSerializedData2.writeInt32(this.this_dc);
                    abstractSerializedData2.writeInt32(481674261);
                    int size = this.dc_options.size();
                    abstractSerializedData2.writeInt32(size);
                    for (int i10 = 0; i10 < size; i10++) {
                        this.dc_options.get(i10).serializeToStream(abstractSerializedData2);
                    }
                    abstractSerializedData2.writeString(this.dc_txt_domain_name);
                    abstractSerializedData2.writeInt32(this.chat_size_max);
                    abstractSerializedData2.writeInt32(this.megagroup_size_max);
                    abstractSerializedData2.writeInt32(this.forwarded_count_max);
                    abstractSerializedData2.writeInt32(this.online_update_period_ms);
                    abstractSerializedData2.writeInt32(this.offline_blur_timeout_ms);
                    abstractSerializedData2.writeInt32(this.offline_idle_timeout_ms);
                    abstractSerializedData2.writeInt32(this.online_cloud_timeout_ms);
                    abstractSerializedData2.writeInt32(this.notify_cloud_delay_ms);
                    abstractSerializedData2.writeInt32(this.notify_default_delay_ms);
                    abstractSerializedData2.writeInt32(this.push_chat_period_ms);
                    abstractSerializedData2.writeInt32(this.push_chat_limit);
                    abstractSerializedData2.writeInt32(this.saved_gifs_limit);
                    abstractSerializedData2.writeInt32(this.edit_time_limit);
                    abstractSerializedData2.writeInt32(this.revoke_time_limit);
                    abstractSerializedData2.writeInt32(this.revoke_pm_time_limit);
                    abstractSerializedData2.writeInt32(this.rating_e_decay);
                    abstractSerializedData2.writeInt32(this.stickers_recent_limit);
                    abstractSerializedData2.writeInt32(this.stickers_faved_limit);
                    abstractSerializedData2.writeInt32(this.channels_read_media_period);
                    if ((this.flags & 1) != 0) {
                        abstractSerializedData2.writeInt32(this.tmp_sessions);
                    }
                    abstractSerializedData2.writeInt32(this.pinned_dialogs_count_max);
                    abstractSerializedData2.writeInt32(this.pinned_infolder_count_max);
                    abstractSerializedData2.writeInt32(this.call_receive_timeout_ms);
                    abstractSerializedData2.writeInt32(this.call_ring_timeout_ms);
                    abstractSerializedData2.writeInt32(this.call_connect_timeout_ms);
                    abstractSerializedData2.writeInt32(this.call_packet_timeout_ms);
                    abstractSerializedData2.writeString(this.me_url_prefix);
                    if ((this.flags & ConnectionsManager.RequestFlagNeedQuickAck) != 0) {
                        abstractSerializedData2.writeString(this.autoupdate_url_prefix);
                    }
                    if ((this.flags & 512) != 0) {
                        abstractSerializedData2.writeString(this.gif_search_username);
                    }
                    if ((this.flags & 1024) != 0) {
                        abstractSerializedData2.writeString(this.venue_search_username);
                    }
                    if ((this.flags & 2048) != 0) {
                        abstractSerializedData2.writeString(this.img_search_username);
                    }
                    if ((this.flags & 4096) != 0) {
                        abstractSerializedData2.writeString(this.static_maps_provider);
                    }
                    abstractSerializedData2.writeInt32(this.caption_length_max);
                    abstractSerializedData2.writeInt32(this.message_length_max);
                    abstractSerializedData2.writeInt32(this.webfile_dc_id);
                    if ((this.flags & 4) != 0) {
                        abstractSerializedData2.writeString(this.suggested_lang_code);
                    }
                    if ((this.flags & 4) != 0) {
                        abstractSerializedData2.writeInt32(this.lang_pack_version);
                    }
                    if ((this.flags & 4) != 0) {
                        abstractSerializedData2.writeInt32(this.base_lang_pack_version);
                    }
                    if ((this.flags & 32768) != 0) {
                        this.reactions_default.serializeToStream(abstractSerializedData2);
                    }
                }
            };
        } else {
            tLRPC$Config = i != 856375399 ? null : new TLRPC$TL_config_layer144();
        }
        if (tLRPC$Config != null || !z) {
            if (tLRPC$Config != null) {
                tLRPC$Config.readParams(abstractSerializedData, z);
            }
            return tLRPC$Config;
        }
        throw new RuntimeException(String.format("can't parse magic %x in Config", Integer.valueOf(i)));
    }
}
