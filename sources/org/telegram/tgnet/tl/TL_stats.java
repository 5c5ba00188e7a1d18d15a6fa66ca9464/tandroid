package org.telegram.tgnet.tl;

import java.util.ArrayList;
import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.OutputSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1;
import org.telegram.tgnet.TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.tl.TL_stats;
import org.telegram.tgnet.tl.TL_stories;

/* loaded from: classes3.dex */
public class TL_stats {

    public static class BroadcastRevenueTransaction extends TLObject {
        public static BroadcastRevenueTransaction TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            BroadcastRevenueTransaction tL_broadcastRevenueTransactionWithdrawal = i != 1121127726 ? i != 1434332356 ? i != 1515784568 ? null : new TL_broadcastRevenueTransactionWithdrawal() : new TL_broadcastRevenueTransactionProceeds() : new TL_broadcastRevenueTransactionRefund();
            if (tL_broadcastRevenueTransactionWithdrawal == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in BroadcastRevenueTransaction", Integer.valueOf(i)));
            }
            if (tL_broadcastRevenueTransactionWithdrawal != null) {
                tL_broadcastRevenueTransactionWithdrawal.readParams(inputSerializedData, z);
            }
            return tL_broadcastRevenueTransactionWithdrawal;
        }
    }

    public static abstract class PostInteractionCounters extends TLObject {
        public static PostInteractionCounters TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            PostInteractionCounters tL_postInteractionCountersMessage = i != -1974989273 ? i != -419066241 ? null : new TL_postInteractionCountersMessage() : new TL_postInteractionCountersStory();
            if (tL_postInteractionCountersMessage == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in PostInteractionCounters", Integer.valueOf(i)));
            }
            if (tL_postInteractionCountersMessage != null) {
                tL_postInteractionCountersMessage.readParams(inputSerializedData, z);
            }
            return tL_postInteractionCountersMessage;
        }
    }

    public static abstract class PublicForward extends TLObject {
        public static PublicForward TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            PublicForward tL_publicForwardMessage = i != -302797360 ? i != 32685898 ? null : new TL_publicForwardMessage() : new TL_stories.TL_publicForwardStory();
            if (tL_publicForwardMessage == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in PublicForward", Integer.valueOf(i)));
            }
            if (tL_publicForwardMessage != null) {
                tL_publicForwardMessage.readParams(inputSerializedData, z);
            }
            return tL_publicForwardMessage;
        }
    }

    public static abstract class StatsGraph extends TLObject {
        public float rate;

        public static StatsGraph TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            StatsGraph tL_statsGraphAsync = i != -1901828938 ? i != -1092839390 ? i != 1244130093 ? null : new TL_statsGraphAsync() : new TL_statsGraphError() : new TL_statsGraph();
            if (tL_statsGraphAsync == null && z) {
                throw new RuntimeException(String.format("can't parse magic %x in StatsGraph", Integer.valueOf(i)));
            }
            if (tL_statsGraphAsync != null) {
                tL_statsGraphAsync.readParams(inputSerializedData, z);
            }
            return tL_statsGraphAsync;
        }
    }

    public static class TL_broadcastRevenueStats extends TLObject {
        public static final int constructor = 1409802903;
        public TLRPC.BroadcastRevenueBalances balances;
        public StatsGraph revenue_graph;
        public StatsGraph top_hours_graph;
        public double usd_rate;

        public static TL_broadcastRevenueStats TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (1409802903 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stats_broadcastRevenueStats", Integer.valueOf(i)));
                }
                return null;
            }
            TL_broadcastRevenueStats tL_broadcastRevenueStats = new TL_broadcastRevenueStats();
            tL_broadcastRevenueStats.readParams(inputSerializedData, z);
            return tL_broadcastRevenueStats;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.top_hours_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.revenue_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.balances = TLRPC.BroadcastRevenueBalances.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.usd_rate = inputSerializedData.readDouble(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.top_hours_graph.serializeToStream(outputSerializedData);
            this.revenue_graph.serializeToStream(outputSerializedData);
            this.balances.serializeToStream(outputSerializedData);
            outputSerializedData.writeDouble(this.usd_rate);
        }
    }

    public static class TL_broadcastRevenueTransactionProceeds extends BroadcastRevenueTransaction {
        public static final int constructor = 1434332356;
        public long amount;
        public int from_date;
        public int to_date;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.amount = inputSerializedData.readInt64(z);
            this.from_date = inputSerializedData.readInt32(z);
            this.to_date = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt64(this.amount);
            outputSerializedData.writeInt32(this.from_date);
            outputSerializedData.writeInt32(this.to_date);
        }
    }

    public static class TL_broadcastRevenueTransactionRefund extends BroadcastRevenueTransaction {
        public static final int constructor = 1121127726;
        public long amount;
        public int from_date;
        public String provider;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.amount = inputSerializedData.readInt64(z);
            this.from_date = inputSerializedData.readInt32(z);
            this.provider = inputSerializedData.readString(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt64(this.amount);
            outputSerializedData.writeInt32(this.from_date);
            outputSerializedData.writeString(this.provider);
        }
    }

    public static class TL_broadcastRevenueTransactionWithdrawal extends BroadcastRevenueTransaction {
        public static final int constructor = 1515784568;
        public long amount;
        public int date;
        public boolean failed;
        public int flags;
        public boolean pending;
        public String provider;
        public int transaction_date;
        public String transaction_url;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            int readInt32 = inputSerializedData.readInt32(z);
            this.flags = readInt32;
            this.pending = (readInt32 & 1) != 0;
            this.failed = (readInt32 & 4) != 0;
            this.amount = inputSerializedData.readInt64(z);
            this.date = inputSerializedData.readInt32(z);
            this.provider = inputSerializedData.readString(z);
            if ((this.flags & 2) != 0) {
                this.transaction_date = inputSerializedData.readInt32(z);
                this.transaction_url = inputSerializedData.readString(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.pending ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            this.flags = this.failed ? i | 1 : i & (-2);
            outputSerializedData.writeInt64(this.amount);
            outputSerializedData.writeInt32(this.date);
            outputSerializedData.writeString(this.provider);
            if ((this.flags & 2) != 0) {
                outputSerializedData.writeInt32(this.transaction_date);
                outputSerializedData.writeString(this.transaction_url);
            }
        }
    }

    public static class TL_broadcastRevenueTransactions extends TLObject {
        public static final int constructor = -2028632986;
        public int count;
        public ArrayList<BroadcastRevenueTransaction> transactions = new ArrayList<>();

        public static TL_broadcastRevenueTransactions TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-2028632986 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stats_broadcastRevenueTransactions", Integer.valueOf(i)));
                }
                return null;
            }
            TL_broadcastRevenueTransactions tL_broadcastRevenueTransactions = new TL_broadcastRevenueTransactions();
            tL_broadcastRevenueTransactions.readParams(inputSerializedData, z);
            return tL_broadcastRevenueTransactions;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.count = inputSerializedData.readInt32(z);
            this.transactions = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stats$TL_broadcastRevenueTransactions$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stats.BroadcastRevenueTransaction.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.count);
            Vector.serialize(outputSerializedData, this.transactions);
        }
    }

    public static class TL_broadcastRevenueWithdrawalUrl extends TLObject {
        public static final int constructor = -328886473;
        public String url;

        public static TL_broadcastRevenueWithdrawalUrl TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-328886473 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stats_broadcastRevenueWithdrawalUrl", Integer.valueOf(i)));
                }
                return null;
            }
            TL_broadcastRevenueWithdrawalUrl tL_broadcastRevenueWithdrawalUrl = new TL_broadcastRevenueWithdrawalUrl();
            tL_broadcastRevenueWithdrawalUrl.readParams(inputSerializedData, z);
            return tL_broadcastRevenueWithdrawalUrl;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.url = inputSerializedData.readString(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeString(this.url);
        }
    }

    public static class TL_broadcastStats extends TLObject {
        public static int constructor = 963421692;
        public TL_statsPercentValue enabled_notifications;
        public TL_statsAbsValueAndPrev followers;
        public StatsGraph followers_graph;
        public StatsGraph growth_graph;
        public StatsGraph interactions_graph;
        public StatsGraph iv_interactions_graph;
        public StatsGraph languages_graph;
        public StatsGraph mute_graph;
        public StatsGraph new_followers_by_source_graph;
        public TL_statsDateRangeDays period;
        public StatsGraph reactions_by_emotion_graph;
        public TL_statsAbsValueAndPrev reactions_per_post;
        public TL_statsAbsValueAndPrev reactions_per_story;
        public ArrayList<PostInteractionCounters> recent_posts_interactions = new ArrayList<>();
        public TL_statsAbsValueAndPrev shares_per_post;
        public TL_statsAbsValueAndPrev shares_per_story;
        public StatsGraph story_interactions_graph;
        public StatsGraph story_reactions_by_emotion_graph;
        public StatsGraph top_hours_graph;
        public StatsGraph views_by_source_graph;
        public TL_statsAbsValueAndPrev views_per_post;
        public TL_statsAbsValueAndPrev views_per_story;

        public static TL_broadcastStats TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (constructor != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stats_broadcastStats", Integer.valueOf(i)));
                }
                return null;
            }
            TL_broadcastStats tL_broadcastStats = new TL_broadcastStats();
            tL_broadcastStats.readParams(inputSerializedData, z);
            return tL_broadcastStats;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.period = TL_statsDateRangeDays.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.followers = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.views_per_post = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.shares_per_post = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.reactions_per_post = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.views_per_story = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.shares_per_story = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.reactions_per_story = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.enabled_notifications = TL_statsPercentValue.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.growth_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.followers_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.mute_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.top_hours_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.interactions_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.iv_interactions_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.views_by_source_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.new_followers_by_source_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.languages_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.reactions_by_emotion_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.story_interactions_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.story_reactions_by_emotion_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.recent_posts_interactions = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stats$TL_broadcastStats$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stats.PostInteractionCounters.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.period.serializeToStream(outputSerializedData);
            this.followers.serializeToStream(outputSerializedData);
            this.views_per_post.serializeToStream(outputSerializedData);
            this.shares_per_post.serializeToStream(outputSerializedData);
            this.reactions_per_post.serializeToStream(outputSerializedData);
            this.views_per_story.serializeToStream(outputSerializedData);
            this.shares_per_story.serializeToStream(outputSerializedData);
            this.reactions_per_story.serializeToStream(outputSerializedData);
            this.enabled_notifications.serializeToStream(outputSerializedData);
            this.growth_graph.serializeToStream(outputSerializedData);
            this.followers_graph.serializeToStream(outputSerializedData);
            this.mute_graph.serializeToStream(outputSerializedData);
            this.top_hours_graph.serializeToStream(outputSerializedData);
            this.interactions_graph.serializeToStream(outputSerializedData);
            this.iv_interactions_graph.serializeToStream(outputSerializedData);
            this.views_by_source_graph.serializeToStream(outputSerializedData);
            this.new_followers_by_source_graph.serializeToStream(outputSerializedData);
            this.languages_graph.serializeToStream(outputSerializedData);
            this.reactions_by_emotion_graph.serializeToStream(outputSerializedData);
            this.story_interactions_graph.serializeToStream(outputSerializedData);
            this.story_reactions_by_emotion_graph.serializeToStream(outputSerializedData);
            Vector.serialize(outputSerializedData, this.recent_posts_interactions);
        }
    }

    public static class TL_getBroadcastRevenueStats extends TLObject {
        public static final int constructor = -142021095;
        public boolean dark;
        public int flags;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_broadcastRevenueStats.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.dark ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.peer.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_getBroadcastRevenueTransactions extends TLObject {
        public static final int constructor = 1889078125;
        public int limit;
        public int offset;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_broadcastRevenueTransactions.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.offset);
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class TL_getBroadcastRevenueWithdrawalUrl extends TLObject {
        public static final int constructor = -1644889427;
        public TLRPC.InputCheckPasswordSRP password;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_broadcastRevenueWithdrawalUrl.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            this.password.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_getBroadcastStats extends TLObject {
        public static final int constructor = -1421720550;
        public TLRPC.InputChannel channel;
        public boolean dark;
        public int flags;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_broadcastStats.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.dark ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.channel.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_getMegagroupStats extends TLObject {
        public static final int constructor = -589330937;
        public TLRPC.InputChannel channel;
        public boolean dark;
        public int flags;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_megagroupStats.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.dark ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.channel.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_getMessagePublicForwards extends TLObject {
        public static final int constructor = 1595212100;
        public TLRPC.InputChannel channel;
        public int limit;
        public int msg_id;
        public String offset;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_publicForwards.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.channel.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.msg_id);
            outputSerializedData.writeString(this.offset);
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class TL_getMessageStats extends TLObject {
        public static final int constructor = -1226791947;
        public TLRPC.InputChannel channel;
        public boolean dark;
        public int flags;
        public int msg_id;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_messageStats.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            int i = this.dark ? this.flags | 1 : this.flags & (-2);
            this.flags = i;
            outputSerializedData.writeInt32(i);
            this.channel.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.msg_id);
        }
    }

    public static class TL_getStoryPublicForwards extends TLObject {
        public static final int constructor = -1505526026;
        public int id;
        public int limit;
        public String offset;
        public TLRPC.InputPeer peer;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return TL_publicForwards.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.peer.serializeToStream(outputSerializedData);
            outputSerializedData.writeInt32(this.id);
            outputSerializedData.writeString(this.offset);
            outputSerializedData.writeInt32(this.limit);
        }
    }

    public static class TL_loadAsyncGraph extends TLObject {
        public static final int constructor = 1646092192;
        public int flags;
        public String token;
        public long x;

        @Override // org.telegram.tgnet.TLObject
        public TLObject deserializeResponse(InputSerializedData inputSerializedData, int i, boolean z) {
            return StatsGraph.TLdeserialize(inputSerializedData, i, z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeString(this.token);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeInt64(this.x);
            }
        }
    }

    public static class TL_megagroupStats extends TLObject {
        public static final int constructor = -276825834;
        public StatsGraph actions_graph;
        public StatsGraph growth_graph;
        public StatsGraph languages_graph;
        public TL_statsAbsValueAndPrev members;
        public StatsGraph members_graph;
        public TL_statsAbsValueAndPrev messages;
        public StatsGraph messages_graph;
        public StatsGraph new_members_by_source_graph;
        public TL_statsDateRangeDays period;
        public TL_statsAbsValueAndPrev posters;
        public StatsGraph top_hours_graph;
        public TL_statsAbsValueAndPrev viewers;
        public StatsGraph weekdays_graph;
        public ArrayList<TL_statsGroupTopPoster> top_posters = new ArrayList<>();
        public ArrayList<TL_statsGroupTopAdmin> top_admins = new ArrayList<>();
        public ArrayList<TL_statsGroupTopInviter> top_inviters = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static TL_megagroupStats TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-276825834 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stats_megagroupStats", Integer.valueOf(i)));
                }
                return null;
            }
            TL_megagroupStats tL_megagroupStats = new TL_megagroupStats();
            tL_megagroupStats.readParams(inputSerializedData, z);
            return tL_megagroupStats;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.period = TL_statsDateRangeDays.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.members = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.messages = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.viewers = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.posters = TL_statsAbsValueAndPrev.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.growth_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.members_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.new_members_by_source_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.languages_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.messages_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.actions_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.top_hours_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.weekdays_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.top_posters = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stats$TL_megagroupStats$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stats.TL_statsGroupTopPoster.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            this.top_admins = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stats$TL_megagroupStats$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stats.TL_statsGroupTopAdmin.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            this.top_inviters = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stats$TL_megagroupStats$$ExternalSyntheticLambda2
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stats.TL_statsGroupTopInviter.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.period.serializeToStream(outputSerializedData);
            this.members.serializeToStream(outputSerializedData);
            this.messages.serializeToStream(outputSerializedData);
            this.viewers.serializeToStream(outputSerializedData);
            this.posters.serializeToStream(outputSerializedData);
            this.growth_graph.serializeToStream(outputSerializedData);
            this.members_graph.serializeToStream(outputSerializedData);
            this.new_members_by_source_graph.serializeToStream(outputSerializedData);
            this.languages_graph.serializeToStream(outputSerializedData);
            this.messages_graph.serializeToStream(outputSerializedData);
            this.actions_graph.serializeToStream(outputSerializedData);
            this.top_hours_graph.serializeToStream(outputSerializedData);
            this.weekdays_graph.serializeToStream(outputSerializedData);
            Vector.serialize(outputSerializedData, this.top_posters);
            Vector.serialize(outputSerializedData, this.top_admins);
            Vector.serialize(outputSerializedData, this.top_inviters);
            Vector.serialize(outputSerializedData, this.users);
        }
    }

    public static class TL_messageStats extends TLObject {
        public static final int constructor = 2145983508;
        public StatsGraph reactions_by_emotion_graph;
        public StatsGraph views_graph;

        public static TL_messageStats TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (2145983508 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stats_messageStats", Integer.valueOf(i)));
                }
                return null;
            }
            TL_messageStats tL_messageStats = new TL_messageStats();
            tL_messageStats.readParams(inputSerializedData, z);
            return tL_messageStats;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.views_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            this.reactions_by_emotion_graph = StatsGraph.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.views_graph.serializeToStream(outputSerializedData);
            this.reactions_by_emotion_graph.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_postInteractionCountersMessage extends PostInteractionCounters {
        public static final int constructor = -419066241;
        public int forwards;
        public int msg_id;
        public int reactions;
        public int views;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.msg_id = inputSerializedData.readInt32(z);
            this.views = inputSerializedData.readInt32(z);
            this.forwards = inputSerializedData.readInt32(z);
            this.reactions = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.msg_id);
            outputSerializedData.writeInt32(this.views);
            outputSerializedData.writeInt32(this.forwards);
            outputSerializedData.writeInt32(this.reactions);
        }
    }

    public static class TL_postInteractionCountersStory extends PostInteractionCounters {
        public static final int constructor = -1974989273;
        public int forwards;
        public int reactions;
        public int story_id;
        public int views;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.story_id = inputSerializedData.readInt32(z);
            this.views = inputSerializedData.readInt32(z);
            this.forwards = inputSerializedData.readInt32(z);
            this.reactions = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.story_id);
            outputSerializedData.writeInt32(this.views);
            outputSerializedData.writeInt32(this.forwards);
            outputSerializedData.writeInt32(this.reactions);
        }
    }

    public static class TL_publicForwardMessage extends PublicForward {
        public static final int constructor = 32685898;
        public TLRPC.Message message;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.message = TLRPC.Message.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            this.message.serializeToStream(outputSerializedData);
        }
    }

    public static class TL_publicForwards extends TLObject {
        public static final int constructor = -1828487648;
        public int count;
        public int flags;
        public String next_offset;
        public ArrayList<PublicForward> forwards = new ArrayList<>();
        public ArrayList<TLRPC.Chat> chats = new ArrayList<>();
        public ArrayList<TLRPC.User> users = new ArrayList<>();

        public static TL_publicForwards TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-1828487648 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_stats_publicForwards", Integer.valueOf(i)));
                }
                return null;
            }
            TL_publicForwards tL_publicForwards = new TL_publicForwards();
            tL_publicForwards.readParams(inputSerializedData, z);
            return tL_publicForwards;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.count = inputSerializedData.readInt32(z);
            this.forwards = Vector.deserialize(inputSerializedData, new Vector.TLDeserializer() { // from class: org.telegram.tgnet.tl.TL_stats$TL_publicForwards$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.Vector.TLDeserializer
                public final TLObject deserialize(InputSerializedData inputSerializedData2, int i, boolean z2) {
                    return TL_stats.PublicForward.TLdeserialize(inputSerializedData2, i, z2);
                }
            }, z);
            if ((this.flags & 1) != 0) {
                this.next_offset = inputSerializedData.readString(z);
            }
            this.chats = Vector.deserialize(inputSerializedData, new TLRPC$TL_channels_adminLogResults$$ExternalSyntheticLambda1(), z);
            this.users = Vector.deserialize(inputSerializedData, new TLRPC$TL_attachMenuBots$$ExternalSyntheticLambda1(), z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            outputSerializedData.writeInt32(this.count);
            Vector.serialize(outputSerializedData, this.forwards);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.next_offset);
            }
            Vector.serialize(outputSerializedData, this.chats);
            Vector.serialize(outputSerializedData, this.users);
        }
    }

    public static class TL_statsAbsValueAndPrev extends TLObject {
        public static final int constructor = -884757282;
        public double current;
        public double previous;

        public static TL_statsAbsValueAndPrev TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-884757282 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_statsAbsValueAndPrev", Integer.valueOf(i)));
                }
                return null;
            }
            TL_statsAbsValueAndPrev tL_statsAbsValueAndPrev = new TL_statsAbsValueAndPrev();
            tL_statsAbsValueAndPrev.readParams(inputSerializedData, z);
            return tL_statsAbsValueAndPrev;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.current = inputSerializedData.readDouble(z);
            this.previous = inputSerializedData.readDouble(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeDouble(this.current);
            outputSerializedData.writeDouble(this.previous);
        }
    }

    public static class TL_statsDateRangeDays extends TLObject {
        public static final int constructor = -1237848657;
        public int max_date;
        public int min_date;

        public static TL_statsDateRangeDays TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-1237848657 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_statsDateRangeDays", Integer.valueOf(i)));
                }
                return null;
            }
            TL_statsDateRangeDays tL_statsDateRangeDays = new TL_statsDateRangeDays();
            tL_statsDateRangeDays.readParams(inputSerializedData, z);
            return tL_statsDateRangeDays;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.min_date = inputSerializedData.readInt32(z);
            this.max_date = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.min_date);
            outputSerializedData.writeInt32(this.max_date);
        }
    }

    public static class TL_statsGraph extends StatsGraph {
        public static final int constructor = -1901828938;
        public int flags;
        public TLRPC.TL_dataJSON json;
        public String zoom_token;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.flags = inputSerializedData.readInt32(z);
            this.json = TLRPC.TL_dataJSON.TLdeserialize(inputSerializedData, inputSerializedData.readInt32(z), z);
            if ((this.flags & 1) != 0) {
                this.zoom_token = inputSerializedData.readString(z);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt32(this.flags);
            this.json.serializeToStream(outputSerializedData);
            if ((this.flags & 1) != 0) {
                outputSerializedData.writeString(this.zoom_token);
            }
        }
    }

    public static class TL_statsGraphAsync extends StatsGraph {
        public static final int constructor = 1244130093;
        public String token;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.token = inputSerializedData.readString(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeString(this.token);
        }
    }

    public static class TL_statsGraphError extends StatsGraph {
        public static final int constructor = -1092839390;
        public String error;

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.error = inputSerializedData.readString(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeString(this.error);
        }
    }

    public static class TL_statsGroupTopAdmin extends TLObject {
        public static final int constructor = -682079097;
        public int banned;
        public int deleted;
        public int kicked;
        public long user_id;

        public static TL_statsGroupTopAdmin TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-682079097 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_statsGroupTopAdmin", Integer.valueOf(i)));
                }
                return null;
            }
            TL_statsGroupTopAdmin tL_statsGroupTopAdmin = new TL_statsGroupTopAdmin();
            tL_statsGroupTopAdmin.readParams(inputSerializedData, z);
            return tL_statsGroupTopAdmin;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.user_id = inputSerializedData.readInt64(z);
            this.deleted = inputSerializedData.readInt32(z);
            this.kicked = inputSerializedData.readInt32(z);
            this.banned = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt64(this.user_id);
            outputSerializedData.writeInt32(this.deleted);
            outputSerializedData.writeInt32(this.kicked);
            outputSerializedData.writeInt32(this.banned);
        }
    }

    public static class TL_statsGroupTopInviter extends TLObject {
        public static final int constructor = 1398765469;
        public int invitations;
        public long user_id;

        public static TL_statsGroupTopInviter TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (1398765469 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_statsGroupTopInviter", Integer.valueOf(i)));
                }
                return null;
            }
            TL_statsGroupTopInviter tL_statsGroupTopInviter = new TL_statsGroupTopInviter();
            tL_statsGroupTopInviter.readParams(inputSerializedData, z);
            return tL_statsGroupTopInviter;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.user_id = inputSerializedData.readInt64(z);
            this.invitations = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt64(this.user_id);
            outputSerializedData.writeInt32(this.invitations);
        }
    }

    public static class TL_statsGroupTopPoster extends TLObject {
        public static final int constructor = -1660637285;
        public int avg_chars;
        public int messages;
        public long user_id;

        public static TL_statsGroupTopPoster TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-1660637285 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_statsGroupTopPoster", Integer.valueOf(i)));
                }
                return null;
            }
            TL_statsGroupTopPoster tL_statsGroupTopPoster = new TL_statsGroupTopPoster();
            tL_statsGroupTopPoster.readParams(inputSerializedData, z);
            return tL_statsGroupTopPoster;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.user_id = inputSerializedData.readInt64(z);
            this.messages = inputSerializedData.readInt32(z);
            this.avg_chars = inputSerializedData.readInt32(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeInt64(this.user_id);
            outputSerializedData.writeInt32(this.messages);
            outputSerializedData.writeInt32(this.avg_chars);
        }
    }

    public static class TL_statsPercentValue extends TLObject {
        public static final int constructor = -875679776;
        public double part;
        public double total;

        public static TL_statsPercentValue TLdeserialize(InputSerializedData inputSerializedData, int i, boolean z) {
            if (-875679776 != i) {
                if (z) {
                    throw new RuntimeException(String.format("can't parse magic %x in TL_statsPercentValue", Integer.valueOf(i)));
                }
                return null;
            }
            TL_statsPercentValue tL_statsPercentValue = new TL_statsPercentValue();
            tL_statsPercentValue.readParams(inputSerializedData, z);
            return tL_statsPercentValue;
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(InputSerializedData inputSerializedData, boolean z) {
            this.part = inputSerializedData.readDouble(z);
            this.total = inputSerializedData.readDouble(z);
        }

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(OutputSerializedData outputSerializedData) {
            outputSerializedData.writeInt32(constructor);
            outputSerializedData.writeDouble(this.part);
            outputSerializedData.writeDouble(this.total);
        }
    }
}
