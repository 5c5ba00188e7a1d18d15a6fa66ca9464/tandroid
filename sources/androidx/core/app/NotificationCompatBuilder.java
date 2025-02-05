package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.LocusId;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RemoteViews;
import androidx.collection.ArraySet;
import androidx.core.app.NotificationCompat;
import androidx.core.content.LocusIdCompat;
import androidx.core.graphics.drawable.IconCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
    private RemoteViews mBigContentView;
    private final Notification.Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;
    private RemoteViews mContentView;
    private final Context mContext;
    private int mGroupAlertBehavior;
    private RemoteViews mHeadsUpContentView;
    private final List mActionExtrasList = new ArrayList();
    private final Bundle mExtras = new Bundle();

    static class Api16Impl {
        static Notification build(Notification.Builder builder) {
            return builder.build();
        }

        static Notification.Builder setPriority(Notification.Builder builder, int i) {
            return builder.setPriority(i);
        }

        static Notification.Builder setSubText(Notification.Builder builder, CharSequence charSequence) {
            return builder.setSubText(charSequence);
        }

        static Notification.Builder setUsesChronometer(Notification.Builder builder, boolean z) {
            return builder.setUsesChronometer(z);
        }
    }

    static class Api17Impl {
        static Notification.Builder setShowWhen(Notification.Builder builder, boolean z) {
            return builder.setShowWhen(z);
        }
    }

    static class Api19Impl {
        static Notification.Builder setExtras(Notification.Builder builder, Bundle bundle) {
            return builder.setExtras(bundle);
        }
    }

    static class Api20Impl {
        static Notification.Builder addAction(Notification.Builder builder, Notification.Action action) {
            return builder.addAction(action);
        }

        static Notification.Action.Builder addExtras(Notification.Action.Builder builder, Bundle bundle) {
            return builder.addExtras(bundle);
        }

        static Notification.Action.Builder addRemoteInput(Notification.Action.Builder builder, android.app.RemoteInput remoteInput) {
            return builder.addRemoteInput(remoteInput);
        }

        static Notification.Action build(Notification.Action.Builder builder) {
            return builder.build();
        }

        static Notification.Action.Builder createBuilder(int i, CharSequence charSequence, PendingIntent pendingIntent) {
            return new Notification.Action.Builder(i, charSequence, pendingIntent);
        }

        static String getGroup(Notification notification) {
            return notification.getGroup();
        }

        static Notification.Builder setGroup(Notification.Builder builder, String str) {
            return builder.setGroup(str);
        }

        static Notification.Builder setGroupSummary(Notification.Builder builder, boolean z) {
            return builder.setGroupSummary(z);
        }

        static Notification.Builder setLocalOnly(Notification.Builder builder, boolean z) {
            return builder.setLocalOnly(z);
        }

        static Notification.Builder setSortKey(Notification.Builder builder, String str) {
            return builder.setSortKey(str);
        }
    }

    static class Api21Impl {
        static Notification.Builder addPerson(Notification.Builder builder, String str) {
            return builder.addPerson(str);
        }

        static Notification.Builder setCategory(Notification.Builder builder, String str) {
            return builder.setCategory(str);
        }

        static Notification.Builder setColor(Notification.Builder builder, int i) {
            return builder.setColor(i);
        }

        static Notification.Builder setPublicVersion(Notification.Builder builder, Notification notification) {
            return builder.setPublicVersion(notification);
        }

        static Notification.Builder setSound(Notification.Builder builder, Uri uri, Object obj) {
            return builder.setSound(uri, (AudioAttributes) obj);
        }

        static Notification.Builder setVisibility(Notification.Builder builder, int i) {
            return builder.setVisibility(i);
        }
    }

    static class Api23Impl {
        static Notification.Action.Builder createBuilder(Icon icon, CharSequence charSequence, PendingIntent pendingIntent) {
            return new Notification.Action.Builder(icon, charSequence, pendingIntent);
        }

        static Notification.Builder setSmallIcon(Notification.Builder builder, Object obj) {
            return builder.setSmallIcon((Icon) obj);
        }
    }

    static class Api24Impl {
        static Notification.Action.Builder setAllowGeneratedReplies(Notification.Action.Builder builder, boolean z) {
            return builder.setAllowGeneratedReplies(z);
        }

        static Notification.Builder setCustomBigContentView(Notification.Builder builder, RemoteViews remoteViews) {
            return builder.setCustomBigContentView(remoteViews);
        }

        static Notification.Builder setCustomContentView(Notification.Builder builder, RemoteViews remoteViews) {
            return builder.setCustomContentView(remoteViews);
        }

        static Notification.Builder setCustomHeadsUpContentView(Notification.Builder builder, RemoteViews remoteViews) {
            return builder.setCustomHeadsUpContentView(remoteViews);
        }

        static Notification.Builder setRemoteInputHistory(Notification.Builder builder, CharSequence[] charSequenceArr) {
            return builder.setRemoteInputHistory(charSequenceArr);
        }
    }

    static class Api26Impl {
        static Notification.Builder createBuilder(Context context, String str) {
            return new Notification.Builder(context, str);
        }

        static Notification.Builder setBadgeIconType(Notification.Builder builder, int i) {
            return builder.setBadgeIconType(i);
        }

        static Notification.Builder setColorized(Notification.Builder builder, boolean z) {
            return builder.setColorized(z);
        }

        static Notification.Builder setGroupAlertBehavior(Notification.Builder builder, int i) {
            return builder.setGroupAlertBehavior(i);
        }

        static Notification.Builder setSettingsText(Notification.Builder builder, CharSequence charSequence) {
            return builder.setSettingsText(charSequence);
        }

        static Notification.Builder setShortcutId(Notification.Builder builder, String str) {
            return builder.setShortcutId(str);
        }

        static Notification.Builder setTimeoutAfter(Notification.Builder builder, long j) {
            return builder.setTimeoutAfter(j);
        }
    }

    static class Api28Impl {
        static Notification.Builder addPerson(Notification.Builder builder, android.app.Person person) {
            return builder.addPerson(person);
        }

        static Notification.Action.Builder setSemanticAction(Notification.Action.Builder builder, int i) {
            return builder.setSemanticAction(i);
        }
    }

    static class Api29Impl {
        static Notification.Builder setAllowSystemGeneratedContextualActions(Notification.Builder builder, boolean z) {
            return builder.setAllowSystemGeneratedContextualActions(z);
        }

        static Notification.Builder setBubbleMetadata(Notification.Builder builder, Notification.BubbleMetadata bubbleMetadata) {
            return builder.setBubbleMetadata(bubbleMetadata);
        }

        static Notification.Action.Builder setContextual(Notification.Action.Builder builder, boolean z) {
            return builder.setContextual(z);
        }

        static Notification.Builder setLocusId(Notification.Builder builder, Object obj) {
            return builder.setLocusId((LocusId) obj);
        }
    }

    static class Api31Impl {
        static Notification.Action.Builder setAuthenticationRequired(Notification.Action.Builder builder, boolean z) {
            return builder.setAuthenticationRequired(z);
        }

        static Notification.Builder setForegroundServiceBehavior(Notification.Builder builder, int i) {
            return builder.setForegroundServiceBehavior(i);
        }
    }

    NotificationCompatBuilder(NotificationCompat.Builder builder) {
        int i;
        Object obj;
        AudioAttributes audioAttributes;
        List combineLists;
        Bundle bundle;
        String str;
        this.mBuilderCompat = builder;
        Context context = builder.mContext;
        this.mContext = context;
        int i2 = Build.VERSION.SDK_INT;
        this.mBuilder = i2 >= 26 ? Api26Impl.createBuilder(context, builder.mChannelId) : new Notification.Builder(builder.mContext);
        Notification notification = builder.mNotification;
        this.mBuilder.setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, builder.mTickerView).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 2) != 0).setOnlyAlertOnce((notification.flags & 8) != 0).setAutoCancel((notification.flags & 16) != 0).setDefaults(notification.defaults).setContentTitle(builder.mContentTitle).setContentText(builder.mContentText).setContentInfo(builder.mContentInfo).setContentIntent(builder.mContentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(builder.mFullScreenIntent, (notification.flags & 128) != 0).setLargeIcon(builder.mLargeIcon).setNumber(builder.mNumber).setProgress(builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate);
        if (i2 < 21) {
            this.mBuilder.setSound(notification.sound, notification.audioStreamType);
        }
        Api16Impl.setPriority(Api16Impl.setUsesChronometer(Api16Impl.setSubText(this.mBuilder, builder.mSubText), builder.mUseChronometer), builder.mPriority);
        Iterator it = builder.mActions.iterator();
        while (it.hasNext()) {
            addAction((NotificationCompat.Action) it.next());
        }
        Bundle bundle2 = builder.mExtras;
        if (bundle2 != null) {
            this.mExtras.putAll(bundle2);
        }
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 20) {
            if (builder.mLocalOnly) {
                this.mExtras.putBoolean("android.support.localOnly", true);
            }
            String str2 = builder.mGroupKey;
            if (str2 != null) {
                this.mExtras.putString("android.support.groupKey", str2);
                if (builder.mGroupSummary) {
                    bundle = this.mExtras;
                    str = "android.support.isGroupSummary";
                } else {
                    bundle = this.mExtras;
                    str = "android.support.useSideChannel";
                }
                bundle.putBoolean(str, true);
            }
            String str3 = builder.mSortKey;
            if (str3 != null) {
                this.mExtras.putString("android.support.sortKey", str3);
            }
        }
        this.mContentView = builder.mContentView;
        this.mBigContentView = builder.mBigContentView;
        Api17Impl.setShowWhen(this.mBuilder, builder.mShowWhen);
        if (i3 < 21 && (combineLists = combineLists(getPeople(builder.mPersonList), builder.mPeople)) != null && !combineLists.isEmpty()) {
            this.mExtras.putStringArray("android.people", (String[]) combineLists.toArray(new String[combineLists.size()]));
        }
        if (i3 >= 20) {
            Api20Impl.setLocalOnly(this.mBuilder, builder.mLocalOnly);
            Api20Impl.setGroup(this.mBuilder, builder.mGroupKey);
            Api20Impl.setSortKey(this.mBuilder, builder.mSortKey);
            Api20Impl.setGroupSummary(this.mBuilder, builder.mGroupSummary);
            this.mGroupAlertBehavior = builder.mGroupAlertBehavior;
        }
        if (i3 >= 21) {
            Api21Impl.setCategory(this.mBuilder, builder.mCategory);
            Api21Impl.setColor(this.mBuilder, builder.mColor);
            Api21Impl.setVisibility(this.mBuilder, builder.mVisibility);
            Api21Impl.setPublicVersion(this.mBuilder, builder.mPublicVersion);
            Notification.Builder builder2 = this.mBuilder;
            Uri uri = notification.sound;
            audioAttributes = notification.audioAttributes;
            Api21Impl.setSound(builder2, uri, audioAttributes);
            List combineLists2 = i3 < 28 ? combineLists(getPeople(builder.mPersonList), builder.mPeople) : builder.mPeople;
            if (combineLists2 != null && !combineLists2.isEmpty()) {
                Iterator it2 = combineLists2.iterator();
                while (it2.hasNext()) {
                    Api21Impl.addPerson(this.mBuilder, (String) it2.next());
                }
            }
            this.mHeadsUpContentView = builder.mHeadsUpContentView;
            if (builder.mInvisibleActions.size() > 0) {
                Bundle bundle3 = builder.getExtras().getBundle("android.car.EXTENSIONS");
                bundle3 = bundle3 == null ? new Bundle() : bundle3;
                Bundle bundle4 = new Bundle(bundle3);
                Bundle bundle5 = new Bundle();
                for (int i4 = 0; i4 < builder.mInvisibleActions.size(); i4++) {
                    bundle5.putBundle(Integer.toString(i4), NotificationCompatJellybean.getBundleForAction((NotificationCompat.Action) builder.mInvisibleActions.get(i4)));
                }
                bundle3.putBundle("invisible_actions", bundle5);
                bundle4.putBundle("invisible_actions", bundle5);
                builder.getExtras().putBundle("android.car.EXTENSIONS", bundle3);
                this.mExtras.putBundle("android.car.EXTENSIONS", bundle4);
            }
        }
        int i5 = Build.VERSION.SDK_INT;
        if (i5 >= 23 && (obj = builder.mSmallIcon) != null) {
            Api23Impl.setSmallIcon(this.mBuilder, obj);
        }
        if (i5 >= 24) {
            Api19Impl.setExtras(this.mBuilder, builder.mExtras);
            Api24Impl.setRemoteInputHistory(this.mBuilder, builder.mRemoteInputHistory);
            RemoteViews remoteViews = builder.mContentView;
            if (remoteViews != null) {
                Api24Impl.setCustomContentView(this.mBuilder, remoteViews);
            }
            RemoteViews remoteViews2 = builder.mBigContentView;
            if (remoteViews2 != null) {
                Api24Impl.setCustomBigContentView(this.mBuilder, remoteViews2);
            }
            RemoteViews remoteViews3 = builder.mHeadsUpContentView;
            if (remoteViews3 != null) {
                Api24Impl.setCustomHeadsUpContentView(this.mBuilder, remoteViews3);
            }
        }
        if (i5 >= 26) {
            Api26Impl.setBadgeIconType(this.mBuilder, builder.mBadgeIcon);
            Api26Impl.setSettingsText(this.mBuilder, builder.mSettingsText);
            Api26Impl.setShortcutId(this.mBuilder, builder.mShortcutId);
            Api26Impl.setTimeoutAfter(this.mBuilder, builder.mTimeout);
            Api26Impl.setGroupAlertBehavior(this.mBuilder, builder.mGroupAlertBehavior);
            if (builder.mColorizedSet) {
                Api26Impl.setColorized(this.mBuilder, builder.mColorized);
            }
            if (!TextUtils.isEmpty(builder.mChannelId)) {
                this.mBuilder.setSound(null).setDefaults(0).setLights(0, 0, 0).setVibrate(null);
            }
        }
        if (i5 >= 28) {
            Iterator it3 = builder.mPersonList.iterator();
            while (it3.hasNext()) {
                Api28Impl.addPerson(this.mBuilder, ((Person) it3.next()).toAndroidPerson());
            }
        }
        int i6 = Build.VERSION.SDK_INT;
        if (i6 >= 29) {
            Api29Impl.setAllowSystemGeneratedContextualActions(this.mBuilder, builder.mAllowSystemGeneratedContextualActions);
            Api29Impl.setBubbleMetadata(this.mBuilder, NotificationCompat.BubbleMetadata.toPlatform(builder.mBubbleMetadata));
            LocusIdCompat locusIdCompat = builder.mLocusId;
            if (locusIdCompat != null) {
                Api29Impl.setLocusId(this.mBuilder, locusIdCompat.toLocusId());
            }
        }
        if (i6 >= 31 && (i = builder.mFgsDeferBehavior) != 0) {
            Api31Impl.setForegroundServiceBehavior(this.mBuilder, i);
        }
        if (builder.mSilent) {
            if (this.mBuilderCompat.mGroupSummary) {
                this.mGroupAlertBehavior = 2;
            } else {
                this.mGroupAlertBehavior = 1;
            }
            this.mBuilder.setVibrate(null);
            this.mBuilder.setSound(null);
            int i7 = notification.defaults & (-4);
            notification.defaults = i7;
            this.mBuilder.setDefaults(i7);
            if (i6 >= 26) {
                if (TextUtils.isEmpty(this.mBuilderCompat.mGroupKey)) {
                    Api20Impl.setGroup(this.mBuilder, "silent");
                }
                Api26Impl.setGroupAlertBehavior(this.mBuilder, this.mGroupAlertBehavior);
            }
        }
    }

    private void addAction(NotificationCompat.Action action) {
        int i = Build.VERSION.SDK_INT;
        if (i < 20) {
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, action));
            return;
        }
        IconCompat iconCompat = action.getIconCompat();
        Notification.Action.Builder createBuilder = i >= 23 ? Api23Impl.createBuilder(iconCompat != null ? iconCompat.toIcon() : null, action.getTitle(), action.getActionIntent()) : Api20Impl.createBuilder(iconCompat != null ? iconCompat.getResId() : 0, action.getTitle(), action.getActionIntent());
        if (action.getRemoteInputs() != null) {
            for (android.app.RemoteInput remoteInput : RemoteInput.fromCompat(action.getRemoteInputs())) {
                Api20Impl.addRemoteInput(createBuilder, remoteInput);
            }
        }
        Bundle bundle = action.getExtras() != null ? new Bundle(action.getExtras()) : new Bundle();
        bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 24) {
            Api24Impl.setAllowGeneratedReplies(createBuilder, action.getAllowGeneratedReplies());
        }
        bundle.putInt("android.support.action.semanticAction", action.getSemanticAction());
        if (i2 >= 28) {
            Api28Impl.setSemanticAction(createBuilder, action.getSemanticAction());
        }
        if (i2 >= 29) {
            Api29Impl.setContextual(createBuilder, action.isContextual());
        }
        if (i2 >= 31) {
            Api31Impl.setAuthenticationRequired(createBuilder, action.isAuthenticationRequired());
        }
        bundle.putBoolean("android.support.action.showsUserInterface", action.getShowsUserInterface());
        Api20Impl.addExtras(createBuilder, bundle);
        Api20Impl.addAction(this.mBuilder, Api20Impl.build(createBuilder));
    }

    private static List combineLists(List list, List list2) {
        if (list == null) {
            return list2;
        }
        if (list2 == null) {
            return list;
        }
        ArraySet arraySet = new ArraySet(list.size() + list2.size());
        arraySet.addAll(list);
        arraySet.addAll(list2);
        return new ArrayList(arraySet);
    }

    private static List getPeople(List list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(((Person) it.next()).resolveToLegacyUri());
        }
        return arrayList;
    }

    private void removeSoundAndVibration(Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        notification.defaults &= -4;
    }

    public Notification build() {
        Bundle extras;
        RemoteViews makeHeadsUpContentView;
        RemoteViews makeBigContentView;
        NotificationCompat.Style style = this.mBuilderCompat.mStyle;
        if (style != null) {
            style.apply(this);
        }
        RemoteViews makeContentView = style != null ? style.makeContentView(this) : null;
        Notification buildInternal = buildInternal();
        if (makeContentView != null || (makeContentView = this.mBuilderCompat.mContentView) != null) {
            buildInternal.contentView = makeContentView;
        }
        int i = Build.VERSION.SDK_INT;
        if (style != null && (makeBigContentView = style.makeBigContentView(this)) != null) {
            buildInternal.bigContentView = makeBigContentView;
        }
        if (i >= 21 && style != null && (makeHeadsUpContentView = this.mBuilderCompat.mStyle.makeHeadsUpContentView(this)) != null) {
            buildInternal.headsUpContentView = makeHeadsUpContentView;
        }
        if (style != null && (extras = NotificationCompat.getExtras(buildInternal)) != null) {
            style.addCompatExtras(extras);
        }
        return buildInternal;
    }

    protected Notification buildInternal() {
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            return Api16Impl.build(this.mBuilder);
        }
        if (i >= 24) {
            Notification build = Api16Impl.build(this.mBuilder);
            if (this.mGroupAlertBehavior != 0) {
                if (Api20Impl.getGroup(build) != null && (build.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                    removeSoundAndVibration(build);
                }
                if (Api20Impl.getGroup(build) != null && (build.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(build);
                }
            }
            return build;
        }
        if (i >= 21) {
            Api19Impl.setExtras(this.mBuilder, this.mExtras);
            Notification build2 = Api16Impl.build(this.mBuilder);
            RemoteViews remoteViews = this.mContentView;
            if (remoteViews != null) {
                build2.contentView = remoteViews;
            }
            RemoteViews remoteViews2 = this.mBigContentView;
            if (remoteViews2 != null) {
                build2.bigContentView = remoteViews2;
            }
            RemoteViews remoteViews3 = this.mHeadsUpContentView;
            if (remoteViews3 != null) {
                build2.headsUpContentView = remoteViews3;
            }
            if (this.mGroupAlertBehavior != 0) {
                if (Api20Impl.getGroup(build2) != null && (build2.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                    removeSoundAndVibration(build2);
                }
                if (Api20Impl.getGroup(build2) != null && (build2.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(build2);
                }
            }
            return build2;
        }
        if (i < 20) {
            SparseArray<? extends Parcelable> buildActionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (buildActionExtrasMap != null) {
                this.mExtras.putSparseParcelableArray("android.support.actionExtras", buildActionExtrasMap);
            }
            Api19Impl.setExtras(this.mBuilder, this.mExtras);
            Notification build3 = Api16Impl.build(this.mBuilder);
            RemoteViews remoteViews4 = this.mContentView;
            if (remoteViews4 != null) {
                build3.contentView = remoteViews4;
            }
            RemoteViews remoteViews5 = this.mBigContentView;
            if (remoteViews5 != null) {
                build3.bigContentView = remoteViews5;
            }
            return build3;
        }
        Api19Impl.setExtras(this.mBuilder, this.mExtras);
        Notification build4 = Api16Impl.build(this.mBuilder);
        RemoteViews remoteViews6 = this.mContentView;
        if (remoteViews6 != null) {
            build4.contentView = remoteViews6;
        }
        RemoteViews remoteViews7 = this.mBigContentView;
        if (remoteViews7 != null) {
            build4.bigContentView = remoteViews7;
        }
        if (this.mGroupAlertBehavior != 0) {
            if (Api20Impl.getGroup(build4) != null && (build4.flags & 512) != 0 && this.mGroupAlertBehavior == 2) {
                removeSoundAndVibration(build4);
            }
            if (Api20Impl.getGroup(build4) != null && (build4.flags & 512) == 0 && this.mGroupAlertBehavior == 1) {
                removeSoundAndVibration(build4);
            }
        }
        return build4;
    }

    @Override // androidx.core.app.NotificationBuilderWithBuilderAccessor
    public Notification.Builder getBuilder() {
        return this.mBuilder;
    }

    Context getContext() {
        return this.mContext;
    }
}
