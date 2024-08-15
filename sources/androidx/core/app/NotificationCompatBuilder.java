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
import org.telegram.messenger.LiteMode;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class NotificationCompatBuilder implements NotificationBuilderWithBuilderAccessor {
    private RemoteViews mBigContentView;
    private final Notification.Builder mBuilder;
    private final NotificationCompat.Builder mBuilderCompat;
    private RemoteViews mContentView;
    private final Context mContext;
    private int mGroupAlertBehavior;
    private RemoteViews mHeadsUpContentView;
    private final List<Bundle> mActionExtrasList = new ArrayList();
    private final Bundle mExtras = new Bundle();

    /* JADX INFO: Access modifiers changed from: package-private */
    public NotificationCompatBuilder(NotificationCompat.Builder builder) {
        int i;
        Object obj;
        AudioAttributes audioAttributes;
        List<String> list;
        List<String> combineLists;
        this.mBuilderCompat = builder;
        Context context = builder.mContext;
        this.mContext = context;
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 26) {
            this.mBuilder = Api26Impl.createBuilder(context, builder.mChannelId);
        } else {
            this.mBuilder = new Notification.Builder(builder.mContext);
        }
        Notification notification = builder.mNotification;
        this.mBuilder.setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, builder.mTickerView).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 2) != 0).setOnlyAlertOnce((notification.flags & 8) != 0).setAutoCancel((notification.flags & 16) != 0).setDefaults(notification.defaults).setContentTitle(builder.mContentTitle).setContentText(builder.mContentText).setContentInfo(builder.mContentInfo).setContentIntent(builder.mContentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(builder.mFullScreenIntent, (notification.flags & 128) != 0).setLargeIcon(builder.mLargeIcon).setNumber(builder.mNumber).setProgress(builder.mProgressMax, builder.mProgress, builder.mProgressIndeterminate);
        if (i2 < 21) {
            this.mBuilder.setSound(notification.sound, notification.audioStreamType);
        }
        Api16Impl.setPriority(Api16Impl.setUsesChronometer(Api16Impl.setSubText(this.mBuilder, builder.mSubText), builder.mUseChronometer), builder.mPriority);
        Iterator<NotificationCompat.Action> it = builder.mActions.iterator();
        while (it.hasNext()) {
            addAction(it.next());
        }
        Bundle bundle = builder.mExtras;
        if (bundle != null) {
            this.mExtras.putAll(bundle);
        }
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 20) {
            if (builder.mLocalOnly) {
                this.mExtras.putBoolean("android.support.localOnly", true);
            }
            String str = builder.mGroupKey;
            if (str != null) {
                this.mExtras.putString("android.support.groupKey", str);
                if (builder.mGroupSummary) {
                    this.mExtras.putBoolean("android.support.isGroupSummary", true);
                } else {
                    this.mExtras.putBoolean("android.support.useSideChannel", true);
                }
            }
            String str2 = builder.mSortKey;
            if (str2 != null) {
                this.mExtras.putString("android.support.sortKey", str2);
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
            if (i3 < 28) {
                list = combineLists(getPeople(builder.mPersonList), builder.mPeople);
            } else {
                list = builder.mPeople;
            }
            if (list != null && !list.isEmpty()) {
                for (String str3 : list) {
                    Api21Impl.addPerson(this.mBuilder, str3);
                }
            }
            this.mHeadsUpContentView = builder.mHeadsUpContentView;
            if (builder.mInvisibleActions.size() > 0) {
                Bundle bundle2 = builder.getExtras().getBundle("android.car.EXTENSIONS");
                bundle2 = bundle2 == null ? new Bundle() : bundle2;
                Bundle bundle3 = new Bundle(bundle2);
                Bundle bundle4 = new Bundle();
                for (int i4 = 0; i4 < builder.mInvisibleActions.size(); i4++) {
                    bundle4.putBundle(Integer.toString(i4), NotificationCompatJellybean.getBundleForAction(builder.mInvisibleActions.get(i4)));
                }
                bundle2.putBundle("invisible_actions", bundle4);
                bundle3.putBundle("invisible_actions", bundle4);
                builder.getExtras().putBundle("android.car.EXTENSIONS", bundle2);
                this.mExtras.putBundle("android.car.EXTENSIONS", bundle3);
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
            Iterator<Person> it2 = builder.mPersonList.iterator();
            while (it2.hasNext()) {
                Api28Impl.addPerson(this.mBuilder, it2.next().toAndroidPerson());
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
            int i7 = notification.defaults & (-2) & (-3);
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

    private static List<String> combineLists(List<String> list, List<String> list2) {
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

    private static List<String> getPeople(List<Person> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (Person person : list) {
            arrayList.add(person.resolveToLegacyUri());
        }
        return arrayList;
    }

    @Override // androidx.core.app.NotificationBuilderWithBuilderAccessor
    public Notification.Builder getBuilder() {
        return this.mBuilder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context getContext() {
        return this.mContext;
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
        if (makeContentView != null) {
            buildInternal.contentView = makeContentView;
        } else {
            RemoteViews remoteViews = this.mBuilderCompat.mContentView;
            if (remoteViews != null) {
                buildInternal.contentView = remoteViews;
            }
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

    private void addAction(NotificationCompat.Action action) {
        Notification.Action.Builder createBuilder;
        Bundle bundle;
        int i = Build.VERSION.SDK_INT;
        if (i >= 20) {
            IconCompat iconCompat = action.getIconCompat();
            if (i >= 23) {
                createBuilder = Api23Impl.createBuilder(iconCompat != null ? iconCompat.toIcon() : null, action.getTitle(), action.getActionIntent());
            } else {
                createBuilder = Api20Impl.createBuilder(iconCompat != null ? iconCompat.getResId() : 0, action.getTitle(), action.getActionIntent());
            }
            if (action.getRemoteInputs() != null) {
                for (android.app.RemoteInput remoteInput : RemoteInput.fromCompat(action.getRemoteInputs())) {
                    Api20Impl.addRemoteInput(createBuilder, remoteInput);
                }
            }
            if (action.getExtras() != null) {
                bundle = new Bundle(action.getExtras());
            } else {
                bundle = new Bundle();
            }
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
            return;
        }
        this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.mBuilder, action));
    }

    protected Notification buildInternal() {
        int i = Build.VERSION.SDK_INT;
        if (i >= 26) {
            return Api16Impl.build(this.mBuilder);
        }
        if (i >= 24) {
            Notification build = Api16Impl.build(this.mBuilder);
            if (this.mGroupAlertBehavior != 0) {
                if (Api20Impl.getGroup(build) != null && (build.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0 && this.mGroupAlertBehavior == 2) {
                    removeSoundAndVibration(build);
                }
                if (Api20Impl.getGroup(build) != null && (build.flags & LiteMode.FLAG_CALLS_ANIMATIONS) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(build);
                }
            }
            return build;
        } else if (i >= 21) {
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
                if (Api20Impl.getGroup(build2) != null && (build2.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0 && this.mGroupAlertBehavior == 2) {
                    removeSoundAndVibration(build2);
                }
                if (Api20Impl.getGroup(build2) != null && (build2.flags & LiteMode.FLAG_CALLS_ANIMATIONS) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(build2);
                }
            }
            return build2;
        } else if (i >= 20) {
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
            if (this.mGroupAlertBehavior != 0) {
                if (Api20Impl.getGroup(build3) != null && (build3.flags & LiteMode.FLAG_CALLS_ANIMATIONS) != 0 && this.mGroupAlertBehavior == 2) {
                    removeSoundAndVibration(build3);
                }
                if (Api20Impl.getGroup(build3) != null && (build3.flags & LiteMode.FLAG_CALLS_ANIMATIONS) == 0 && this.mGroupAlertBehavior == 1) {
                    removeSoundAndVibration(build3);
                }
            }
            return build3;
        } else {
            SparseArray<Bundle> buildActionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (buildActionExtrasMap != null) {
                this.mExtras.putSparseParcelableArray("android.support.actionExtras", buildActionExtrasMap);
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
            return build4;
        }
    }

    private void removeSoundAndVibration(Notification notification) {
        notification.sound = null;
        notification.vibrate = null;
        notification.defaults = notification.defaults & (-2) & (-3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api16Impl {
        static Notification.Builder setSubText(Notification.Builder builder, CharSequence charSequence) {
            return builder.setSubText(charSequence);
        }

        static Notification.Builder setUsesChronometer(Notification.Builder builder, boolean z) {
            return builder.setUsesChronometer(z);
        }

        static Notification.Builder setPriority(Notification.Builder builder, int i) {
            return builder.setPriority(i);
        }

        static Notification build(Notification.Builder builder) {
            return builder.build();
        }
    }

    /* loaded from: classes.dex */
    static class Api17Impl {
        static Notification.Builder setShowWhen(Notification.Builder builder, boolean z) {
            return builder.setShowWhen(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api19Impl {
        static Notification.Builder setExtras(Notification.Builder builder, Bundle bundle) {
            return builder.setExtras(bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api20Impl {
        static Notification.Action.Builder createBuilder(int i, CharSequence charSequence, PendingIntent pendingIntent) {
            return new Notification.Action.Builder(i, charSequence, pendingIntent);
        }

        static Notification.Action.Builder addRemoteInput(Notification.Action.Builder builder, android.app.RemoteInput remoteInput) {
            Notification.Action.Builder addRemoteInput;
            addRemoteInput = builder.addRemoteInput(remoteInput);
            return addRemoteInput;
        }

        static Notification.Action.Builder addExtras(Notification.Action.Builder builder, Bundle bundle) {
            Notification.Action.Builder addExtras;
            addExtras = builder.addExtras(bundle);
            return addExtras;
        }

        static Notification.Builder addAction(Notification.Builder builder, Notification.Action action) {
            Notification.Builder addAction;
            addAction = builder.addAction(action);
            return addAction;
        }

        static Notification.Action build(Notification.Action.Builder builder) {
            Notification.Action build;
            build = builder.build();
            return build;
        }

        static String getGroup(Notification notification) {
            String group;
            group = notification.getGroup();
            return group;
        }

        static Notification.Builder setGroup(Notification.Builder builder, String str) {
            Notification.Builder group;
            group = builder.setGroup(str);
            return group;
        }

        static Notification.Builder setGroupSummary(Notification.Builder builder, boolean z) {
            Notification.Builder groupSummary;
            groupSummary = builder.setGroupSummary(z);
            return groupSummary;
        }

        static Notification.Builder setLocalOnly(Notification.Builder builder, boolean z) {
            Notification.Builder localOnly;
            localOnly = builder.setLocalOnly(z);
            return localOnly;
        }

        static Notification.Builder setSortKey(Notification.Builder builder, String str) {
            Notification.Builder sortKey;
            sortKey = builder.setSortKey(str);
            return sortKey;
        }
    }

    /* loaded from: classes.dex */
    static class Api21Impl {
        static Notification.Builder addPerson(Notification.Builder builder, String str) {
            Notification.Builder addPerson;
            addPerson = builder.addPerson(str);
            return addPerson;
        }

        static Notification.Builder setCategory(Notification.Builder builder, String str) {
            Notification.Builder category;
            category = builder.setCategory(str);
            return category;
        }

        static Notification.Builder setColor(Notification.Builder builder, int i) {
            Notification.Builder color;
            color = builder.setColor(i);
            return color;
        }

        static Notification.Builder setVisibility(Notification.Builder builder, int i) {
            Notification.Builder visibility;
            visibility = builder.setVisibility(i);
            return visibility;
        }

        static Notification.Builder setPublicVersion(Notification.Builder builder, Notification notification) {
            Notification.Builder publicVersion;
            publicVersion = builder.setPublicVersion(notification);
            return publicVersion;
        }

        static Notification.Builder setSound(Notification.Builder builder, Uri uri, Object obj) {
            Notification.Builder sound;
            sound = builder.setSound(uri, (AudioAttributes) obj);
            return sound;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api23Impl {
        static Notification.Action.Builder createBuilder(Icon icon, CharSequence charSequence, PendingIntent pendingIntent) {
            return new Notification.Action.Builder(icon, charSequence, pendingIntent);
        }

        static Notification.Builder setSmallIcon(Notification.Builder builder, Object obj) {
            Notification.Builder smallIcon;
            smallIcon = builder.setSmallIcon((Icon) obj);
            return smallIcon;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api24Impl {
        static Notification.Action.Builder setAllowGeneratedReplies(Notification.Action.Builder builder, boolean z) {
            Notification.Action.Builder allowGeneratedReplies;
            allowGeneratedReplies = builder.setAllowGeneratedReplies(z);
            return allowGeneratedReplies;
        }

        static Notification.Builder setRemoteInputHistory(Notification.Builder builder, CharSequence[] charSequenceArr) {
            Notification.Builder remoteInputHistory;
            remoteInputHistory = builder.setRemoteInputHistory(charSequenceArr);
            return remoteInputHistory;
        }

        static Notification.Builder setCustomContentView(Notification.Builder builder, RemoteViews remoteViews) {
            Notification.Builder customContentView;
            customContentView = builder.setCustomContentView(remoteViews);
            return customContentView;
        }

        static Notification.Builder setCustomBigContentView(Notification.Builder builder, RemoteViews remoteViews) {
            Notification.Builder customBigContentView;
            customBigContentView = builder.setCustomBigContentView(remoteViews);
            return customBigContentView;
        }

        static Notification.Builder setCustomHeadsUpContentView(Notification.Builder builder, RemoteViews remoteViews) {
            Notification.Builder customHeadsUpContentView;
            customHeadsUpContentView = builder.setCustomHeadsUpContentView(remoteViews);
            return customHeadsUpContentView;
        }
    }

    /* loaded from: classes.dex */
    static class Api26Impl {
        static Notification.Builder createBuilder(Context context, String str) {
            return new Notification.Builder(context, str);
        }

        static Notification.Builder setGroupAlertBehavior(Notification.Builder builder, int i) {
            Notification.Builder groupAlertBehavior;
            groupAlertBehavior = builder.setGroupAlertBehavior(i);
            return groupAlertBehavior;
        }

        static Notification.Builder setColorized(Notification.Builder builder, boolean z) {
            Notification.Builder colorized;
            colorized = builder.setColorized(z);
            return colorized;
        }

        static Notification.Builder setBadgeIconType(Notification.Builder builder, int i) {
            Notification.Builder badgeIconType;
            badgeIconType = builder.setBadgeIconType(i);
            return badgeIconType;
        }

        static Notification.Builder setSettingsText(Notification.Builder builder, CharSequence charSequence) {
            Notification.Builder settingsText;
            settingsText = builder.setSettingsText(charSequence);
            return settingsText;
        }

        static Notification.Builder setShortcutId(Notification.Builder builder, String str) {
            Notification.Builder shortcutId;
            shortcutId = builder.setShortcutId(str);
            return shortcutId;
        }

        static Notification.Builder setTimeoutAfter(Notification.Builder builder, long j) {
            Notification.Builder timeoutAfter;
            timeoutAfter = builder.setTimeoutAfter(j);
            return timeoutAfter;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api28Impl {
        static Notification.Action.Builder setSemanticAction(Notification.Action.Builder builder, int i) {
            Notification.Action.Builder semanticAction;
            semanticAction = builder.setSemanticAction(i);
            return semanticAction;
        }

        static Notification.Builder addPerson(Notification.Builder builder, android.app.Person person) {
            Notification.Builder addPerson;
            addPerson = builder.addPerson(person);
            return addPerson;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api29Impl {
        static Notification.Action.Builder setContextual(Notification.Action.Builder builder, boolean z) {
            Notification.Action.Builder contextual;
            contextual = builder.setContextual(z);
            return contextual;
        }

        static Notification.Builder setLocusId(Notification.Builder builder, Object obj) {
            Notification.Builder locusId;
            locusId = builder.setLocusId((LocusId) obj);
            return locusId;
        }

        static Notification.Builder setBubbleMetadata(Notification.Builder builder, Notification.BubbleMetadata bubbleMetadata) {
            Notification.Builder bubbleMetadata2;
            bubbleMetadata2 = builder.setBubbleMetadata(bubbleMetadata);
            return bubbleMetadata2;
        }

        static Notification.Builder setAllowSystemGeneratedContextualActions(Notification.Builder builder, boolean z) {
            Notification.Builder allowSystemGeneratedContextualActions;
            allowSystemGeneratedContextualActions = builder.setAllowSystemGeneratedContextualActions(z);
            return allowSystemGeneratedContextualActions;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Api31Impl {
        static Notification.Action.Builder setAuthenticationRequired(Notification.Action.Builder builder, boolean z) {
            Notification.Action.Builder authenticationRequired;
            authenticationRequired = builder.setAuthenticationRequired(z);
            return authenticationRequired;
        }

        static Notification.Builder setForegroundServiceBehavior(Notification.Builder builder, int i) {
            Notification.Builder foregroundServiceBehavior;
            foregroundServiceBehavior = builder.setForegroundServiceBehavior(i);
            return foregroundServiceBehavior;
        }
    }
}
