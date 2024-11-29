package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.Premium.PremiumButtonView;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PremiumPreviewFragment;
import org.telegram.ui.ProfileActivity;

/* loaded from: classes3.dex */
public class EmojiPacksAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate {
    private static Pattern urlPattern;
    private Adapter adapter;
    private int adaptiveEmojiColor;
    private ColorFilter adaptiveEmojiColorFilter;
    private TextView addButtonView;
    private LongSparseArray animatedEmojiDrawables;
    private FrameLayout buttonsView;
    private ContentView contentView;
    private EmojiPacksLoader customEmojiPacks;
    private BaseFragment fragment;
    private Float fromY;
    private GridLayoutManager gridLayoutManager;
    private boolean hasDescription;
    private AnimatedFloat highlightAlpha;
    int highlightEndPosition;
    private int highlightIndex;
    int highlightStartPosition;
    private float lastY;
    private boolean limitCount;
    private RecyclerListView listView;
    private ValueAnimator loadAnimator;
    private float loadT;
    boolean loaded;
    private View paddingView;
    private ActionBarPopupWindow popupWindow;
    private long premiumButtonClicked;
    private PremiumButtonView premiumButtonView;
    private ContentPreviewViewer.ContentPreviewViewerDelegate previewDelegate;
    private CircularProgressDrawable progressDrawable;
    private TextView removeButtonView;
    private RecyclerAnimationScrollHelper scrollHelper;
    private View shadowView;
    private boolean shown;

    class 1 implements ContentPreviewViewer.ContentPreviewViewerDelegate {
        1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setAsEmojiStatus$0(TLRPC.EmojiStatus emojiStatus) {
            MessagesController.getInstance(((BottomSheet) EmojiPacksAlert.this).currentAccount).updateEmojiStatus(emojiStatus);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void addToFavoriteSelected(String str) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$addToFavoriteSelected(this, str);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public boolean can() {
            return true;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean canDeleteSticker(TLRPC.Document document) {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$canDeleteSticker(this, document);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean canEditSticker() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$canEditSticker(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public boolean canSchedule() {
            return false;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public Boolean canSetAsStatus(TLRPC.Document document) {
            TLRPC.User currentUser;
            if (!UserConfig.getInstance(UserConfig.selectedAccount).isPremium() || !MessageObject.isAnimatedEmoji(document) || (currentUser = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser()) == null) {
                return null;
            }
            Long emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(currentUser);
            return Boolean.valueOf(document != null && (emojiStatusDocumentId == null || emojiStatusDocumentId.longValue() != document.id));
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public void copyEmoji(TLRPC.Document document) {
            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(MessageObject.findAnimatedEmojiEmoticon(document));
            valueOf.setSpan(new AnimatedEmojiSpan(document, (Paint.FontMetricsInt) null), 0, valueOf.length(), 33);
            if (AndroidUtilities.addToClipboard(valueOf)) {
                BulletinFactory.of((FrameLayout) ((BottomSheet) EmojiPacksAlert.this).containerView, ((BottomSheet) EmojiPacksAlert.this).resourcesProvider).createCopyBulletin(LocaleController.getString(R.string.EmojiCopied)).show();
            }
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void deleteSticker(TLRPC.Document document) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$deleteSticker(this, document);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void editSticker(TLRPC.Document document) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$editSticker(this, document);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public long getDialogId() {
            return 0L;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ String getQuery(boolean z) {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$getQuery(this, z);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void gifAddedOrDeleted() {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$gifAddedOrDeleted(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public boolean isInScheduleMode() {
            if (EmojiPacksAlert.this.fragment instanceof ChatActivity) {
                return ((ChatActivity) EmojiPacksAlert.this.fragment).isInScheduleMode();
            }
            return false;
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean isPhotoEditor() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$isPhotoEditor(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean isReplacedSticker() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$isReplacedSticker(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean isSettingIntroSticker() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$isSettingIntroSticker(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean isStickerEditor() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$isStickerEditor(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public boolean needCopy(TLRPC.Document document) {
            return UserConfig.getInstance(UserConfig.selectedAccount).isPremium() && MessageObject.isAnimatedEmoji(document);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean needMenu() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$needMenu(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean needOpen() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$needOpen(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean needRemove() {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$needRemove(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ boolean needRemoveFromRecent(TLRPC.Document document) {
            return ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$needRemoveFromRecent(this, document);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public boolean needSend(int i) {
            return (EmojiPacksAlert.this.fragment instanceof ChatActivity) && ((ChatActivity) EmojiPacksAlert.this.fragment).canSendMessage() && (UserConfig.getInstance(UserConfig.selectedAccount).isPremium() || (((ChatActivity) EmojiPacksAlert.this.fragment).getCurrentUser() != null && UserObject.isUserSelf(((ChatActivity) EmojiPacksAlert.this.fragment).getCurrentUser())));
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void newStickerPackSelected(CharSequence charSequence, String str, Utilities.Callback callback) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$newStickerPackSelected(this, charSequence, str, callback);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public void openSet(TLRPC.InputStickerSet inputStickerSet, boolean z) {
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void remove(SendMessagesHelper.ImportingSticker importingSticker) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$remove(this, importingSticker);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void removeFromRecent(TLRPC.Document document) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$removeFromRecent(this, document);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void resetTouch() {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$resetTouch(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public void sendEmoji(TLRPC.Document document) {
            if (EmojiPacksAlert.this.fragment instanceof ChatActivity) {
                ((ChatActivity) EmojiPacksAlert.this.fragment).sendAnimatedEmoji(document, true, 0);
            }
            EmojiPacksAlert.this.onCloseByLink();
            EmojiPacksAlert.this.dismiss();
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void sendGif(Object obj, Object obj2, boolean z, int i) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$sendGif(this, obj, obj2, z, i);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void sendSticker() {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$sendSticker(this);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void sendSticker(TLRPC.Document document, String str, Object obj, boolean z, int i) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$sendSticker(this, document, str, obj, z, i);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public void setAsEmojiStatus(TLRPC.Document document, Integer num) {
            TLRPC.EmojiStatus emojiStatus;
            Bulletin createEmojiBulletin;
            if (document == null) {
                emojiStatus = new TLRPC.TL_emojiStatusEmpty();
            } else if (num != null) {
                TLRPC.TL_emojiStatusUntil tL_emojiStatusUntil = new TLRPC.TL_emojiStatusUntil();
                tL_emojiStatusUntil.document_id = document.id;
                tL_emojiStatusUntil.until = num.intValue();
                emojiStatus = tL_emojiStatusUntil;
            } else {
                TLRPC.TL_emojiStatus tL_emojiStatus = new TLRPC.TL_emojiStatus();
                tL_emojiStatus.document_id = document.id;
                emojiStatus = tL_emojiStatus;
            }
            TLRPC.User currentUser = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
            final TLRPC.EmojiStatus tL_emojiStatusEmpty = currentUser == null ? new TLRPC.TL_emojiStatusEmpty() : currentUser.emoji_status;
            MessagesController.getInstance(((BottomSheet) EmojiPacksAlert.this).currentAccount).updateEmojiStatus(emojiStatus);
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.EmojiPacksAlert$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    EmojiPacksAlert.1.this.lambda$setAsEmojiStatus$0(tL_emojiStatusEmpty);
                }
            };
            if (document == null) {
                Bulletin.SimpleLayout simpleLayout = new Bulletin.SimpleLayout(EmojiPacksAlert.this.getContext(), ((BottomSheet) EmojiPacksAlert.this).resourcesProvider);
                simpleLayout.textView.setText(LocaleController.getString(R.string.RemoveStatusInfo));
                simpleLayout.imageView.setImageResource(R.drawable.msg_settings_premium);
                Bulletin.UndoButton undoButton = new Bulletin.UndoButton(EmojiPacksAlert.this.getContext(), true, ((BottomSheet) EmojiPacksAlert.this).resourcesProvider);
                undoButton.setUndoAction(runnable);
                simpleLayout.setButton(undoButton);
                createEmojiBulletin = Bulletin.make((FrameLayout) ((BottomSheet) EmojiPacksAlert.this).containerView, simpleLayout, 1500);
            } else {
                createEmojiBulletin = BulletinFactory.of((FrameLayout) ((BottomSheet) EmojiPacksAlert.this).containerView, ((BottomSheet) EmojiPacksAlert.this).resourcesProvider).createEmojiBulletin(document, LocaleController.getString(R.string.SetAsEmojiStatusInfo), LocaleController.getString(R.string.Undo), runnable);
            }
            createEmojiBulletin.show();
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void setIntroSticker(String str) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$setIntroSticker(this, str);
        }

        @Override // org.telegram.ui.ContentPreviewViewer.ContentPreviewViewerDelegate
        public /* synthetic */ void stickerSetSelected(TLRPC.StickerSet stickerSet, String str) {
            ContentPreviewViewer.ContentPreviewViewerDelegate.-CC.$default$stickerSetSelected(this, stickerSet, str);
        }
    }

    class 9 extends ShareAlert {
        9(Context context, ArrayList arrayList, String str, boolean z, String str2, boolean z2, Theme.ResourcesProvider resourcesProvider) {
            super(context, arrayList, str, z, str2, z2, resourcesProvider);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSend$0(androidx.collection.LongSparseArray longSparseArray, int i) {
            UndoView undoView = EmojiPacksAlert.this.fragment instanceof ChatActivity ? ((ChatActivity) EmojiPacksAlert.this.fragment).getUndoView() : EmojiPacksAlert.this.fragment instanceof ProfileActivity ? ((ProfileActivity) EmojiPacksAlert.this.fragment).getUndoView() : null;
            if (undoView != null) {
                if (longSparseArray.size() == 1) {
                    undoView.showWithAction(((TLRPC.Dialog) longSparseArray.valueAt(0)).id, 53, Integer.valueOf(i));
                } else {
                    undoView.showWithAction(0L, 53, Integer.valueOf(i), Integer.valueOf(longSparseArray.size()), (Runnable) null, (Runnable) null);
                }
            }
        }

        @Override // org.telegram.ui.Components.ShareAlert
        protected void onSend(final androidx.collection.LongSparseArray longSparseArray, final int i, TLRPC.TL_forumTopic tL_forumTopic) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.EmojiPacksAlert$9$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    EmojiPacksAlert.9.this.lambda$onSend$0(longSparseArray, i);
                }
            }, 100L);
        }
    }

    private class Adapter extends RecyclerListView.SelectionAdapter {
        private final int VIEW_TYPE_EMOJI;
        private final int VIEW_TYPE_HEADER;
        private final int VIEW_TYPE_PADDING;
        private final int VIEW_TYPE_SEPARATOR;
        private final int VIEW_TYPE_TEXT;

        private Adapter() {
            this.VIEW_TYPE_PADDING = 0;
            this.VIEW_TYPE_EMOJI = 1;
            this.VIEW_TYPE_HEADER = 2;
            this.VIEW_TYPE_TEXT = 3;
            this.VIEW_TYPE_SEPARATOR = 4;
        }

        /* synthetic */ Adapter(EmojiPacksAlert emojiPacksAlert, 1 r2) {
            this();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            EmojiPacksAlert emojiPacksAlert = EmojiPacksAlert.this;
            emojiPacksAlert.hasDescription = !UserConfig.getInstance(((BottomSheet) emojiPacksAlert).currentAccount).isPremium() && EmojiPacksAlert.this.customEmojiPacks.stickerSets != null && EmojiPacksAlert.this.customEmojiPacks.stickerSets.size() == 1 && MessageObject.isPremiumEmojiPack((TLRPC.TL_messages_stickerSet) EmojiPacksAlert.this.customEmojiPacks.stickerSets.get(0));
            return (EmojiPacksAlert.this.hasDescription ? 1 : 0) + 1 + EmojiPacksAlert.this.customEmojiPacks.getItemsCount() + Math.max(0, EmojiPacksAlert.this.customEmojiPacks.data.length - 1);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == 0) {
                return 0;
            }
            int i2 = i - 1;
            if (EmojiPacksAlert.this.hasDescription) {
                if (i2 == 1) {
                    return 3;
                }
                if (i2 > 0) {
                    i2 = i - 2;
                }
            }
            int i3 = 0;
            for (int i4 = 0; i4 < EmojiPacksAlert.this.customEmojiPacks.data.length; i4++) {
                if (i2 == i3) {
                    return 2;
                }
                int size = EmojiPacksAlert.this.customEmojiPacks.data[i4].size();
                if (EmojiPacksAlert.this.customEmojiPacks.data.length > 1) {
                    size = Math.min(EmojiPacksAlert.this.gridLayoutManager.getSpanCount() * 2, size);
                }
                int i5 = i3 + size + 1;
                if (i2 == i5) {
                    return 4;
                }
                i3 = i5 + 1;
            }
            return 1;
        }

        public int getSetEndPosition(int i) {
            int i2 = EmojiPacksAlert.this.hasDescription ? 2 : 1;
            for (int i3 = 0; i3 < EmojiPacksAlert.this.customEmojiPacks.data.length; i3++) {
                int size = EmojiPacksAlert.this.customEmojiPacks.data[i3].size();
                if (EmojiPacksAlert.this.customEmojiPacks.data.length > 1) {
                    size = Math.min(EmojiPacksAlert.this.gridLayoutManager.getSpanCount() * 2, size);
                }
                if (i3 == i) {
                    return i2 + size + 1;
                }
                i2 += size + 2;
            }
            return i2;
        }

        public int getSetHeaderPosition(int i) {
            int i2 = EmojiPacksAlert.this.hasDescription ? 2 : 1;
            for (int i3 = 0; i3 < EmojiPacksAlert.this.customEmojiPacks.data.length && i3 != i; i3++) {
                int size = EmojiPacksAlert.this.customEmojiPacks.data[i3].size();
                if (EmojiPacksAlert.this.customEmojiPacks.data.length > 1) {
                    size = Math.min(EmojiPacksAlert.this.gridLayoutManager.getSpanCount() * 2, size);
                }
                i2 += size + 2;
            }
            return i2;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            EmojiView.CustomEmoji customEmoji;
            ArrayList<TLRPC.Document> arrayList;
            int i2 = i - 1;
            int itemViewType = viewHolder.getItemViewType();
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = null;
            int i3 = 0;
            boolean z = true;
            if (itemViewType == 1) {
                if (EmojiPacksAlert.this.hasDescription) {
                    i2 = i - 2;
                }
                EmojiImageView emojiImageView = (EmojiImageView) viewHolder.itemView;
                int i4 = 0;
                while (true) {
                    if (i3 >= EmojiPacksAlert.this.customEmojiPacks.data.length) {
                        customEmoji = null;
                        break;
                    }
                    int size = EmojiPacksAlert.this.customEmojiPacks.data[i3].size();
                    if (EmojiPacksAlert.this.customEmojiPacks.data.length > 1) {
                        size = Math.min(EmojiPacksAlert.this.gridLayoutManager.getSpanCount() * 2, size);
                    }
                    if (i2 > i4 && i2 <= i4 + size) {
                        customEmoji = (EmojiView.CustomEmoji) EmojiPacksAlert.this.customEmojiPacks.data[i3].get((i2 - i4) - 1);
                        break;
                    } else {
                        i4 += size + 2;
                        i3++;
                    }
                }
                AnimatedEmojiSpan animatedEmojiSpan = emojiImageView.span;
                if ((animatedEmojiSpan != null || customEmoji == null) && ((customEmoji != null || animatedEmojiSpan == null) && (customEmoji == null || animatedEmojiSpan.documentId == customEmoji.documentId))) {
                    return;
                }
                if (customEmoji == null) {
                    emojiImageView.span = null;
                    return;
                }
                TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
                TLRPC.StickerSet stickerSet = customEmoji.stickerSet.set;
                tL_inputStickerSetID.id = stickerSet.id;
                tL_inputStickerSetID.short_name = stickerSet.short_name;
                tL_inputStickerSetID.access_hash = stickerSet.access_hash;
                TLRPC.Document document = customEmoji.getDocument();
                if (document != null) {
                    emojiImageView.span = new AnimatedEmojiSpan(document, (Paint.FontMetricsInt) null);
                    return;
                } else {
                    emojiImageView.span = new AnimatedEmojiSpan(customEmoji.documentId, (Paint.FontMetricsInt) null);
                    return;
                }
            }
            if (itemViewType != 2) {
                if (itemViewType != 3) {
                    return;
                }
                TextView textView = (TextView) viewHolder.itemView;
                textView.setTextSize(1, 13.0f);
                textView.setTextColor(EmojiPacksAlert.this.getThemedColor(Theme.key_chat_emojiPanelTrendingDescription));
                textView.setText(AndroidUtilities.replaceTags(LocaleController.getString(R.string.PremiumPreviewEmojiPack)));
                textView.setPadding(AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(30.0f), AndroidUtilities.dp(14.0f));
                return;
            }
            if (EmojiPacksAlert.this.hasDescription && i2 > 0) {
                i2 = i - 2;
            }
            int i5 = 0;
            int i6 = 0;
            while (i5 < EmojiPacksAlert.this.customEmojiPacks.data.length) {
                int size2 = EmojiPacksAlert.this.customEmojiPacks.data[i5].size();
                if (EmojiPacksAlert.this.customEmojiPacks.data.length > 1) {
                    size2 = Math.min(EmojiPacksAlert.this.gridLayoutManager.getSpanCount() * 2, size2);
                }
                if (i2 == i6) {
                    break;
                }
                i6 += size2 + 2;
                i5++;
            }
            if (EmojiPacksAlert.this.customEmojiPacks.stickerSets != null && i5 < EmojiPacksAlert.this.customEmojiPacks.stickerSets.size()) {
                tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) EmojiPacksAlert.this.customEmojiPacks.stickerSets.get(i5);
            }
            if (tL_messages_stickerSet != null && tL_messages_stickerSet.documents != null) {
                for (int i7 = 0; i7 < tL_messages_stickerSet.documents.size(); i7++) {
                    if (!MessageObject.isFreeEmoji(tL_messages_stickerSet.documents.get(i7))) {
                        break;
                    }
                }
            }
            z = false;
            if (i5 < EmojiPacksAlert.this.customEmojiPacks.data.length) {
                EmojiPackHeader emojiPackHeader = (EmojiPackHeader) viewHolder.itemView;
                if (tL_messages_stickerSet != null && (arrayList = tL_messages_stickerSet.documents) != null) {
                    i3 = arrayList.size();
                }
                emojiPackHeader.set(tL_messages_stickerSet, i3, z);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == 0) {
                view = EmojiPacksAlert.this.paddingView;
            } else {
                if (i == 1) {
                    view = new EmojiImageView(EmojiPacksAlert.this.getContext());
                } else if (i == 2) {
                    EmojiPacksAlert emojiPacksAlert = EmojiPacksAlert.this;
                    view = emojiPacksAlert.new EmojiPackHeader(emojiPacksAlert.getContext(), EmojiPacksAlert.this.customEmojiPacks.data.length <= 1);
                } else if (i == 3) {
                    view = new TextView(EmojiPacksAlert.this.getContext());
                } else if (i == 4) {
                    EmojiPacksAlert emojiPacksAlert2 = EmojiPacksAlert.this;
                    view = emojiPacksAlert2.new SeparatorView(emojiPacksAlert2.getContext());
                } else {
                    view = null;
                }
            }
            return new RecyclerListView.Holder(view);
        }
    }

    private class ContentView extends FrameLayout {
        boolean attached;
        private Boolean lastOpen;
        ArrayList lineDrawables;
        ArrayList lineDrawablesTmp;
        private Paint paint;
        private Path path;
        private ImageReceiver previewImageReceiver;
        private boolean previewImageVisible;
        private final AnimatedFloat previewImageVisibleT;
        private final AnimatedFloat statusBarT;
        ArrayList unusedArrays;
        ArrayList unusedLineDrawables;
        SparseArray viewsGroupedByLines;

        class DrawingInBackgroundLine extends DrawingInBackgroundThreadDrawable {
            ArrayList drawInBackgroundViews = new ArrayList();
            ArrayList imageViewEmojis;
            public int position;

            DrawingInBackgroundLine() {
            }

            /* JADX WARN: Code restructure failed: missing block: B:26:0x0058, code lost:
            
                prepareDraw(java.lang.System.currentTimeMillis());
                drawInUiThread(r7, r12);
                reset();
             */
            /* JADX WARN: Code restructure failed: missing block: B:27:?, code lost:
            
                return;
             */
            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void draw(Canvas canvas, long j, int i, int i2, float f) {
                ArrayList arrayList = this.imageViewEmojis;
                if (arrayList == null) {
                    return;
                }
                boolean z = true;
                boolean z2 = arrayList.size() <= 3 || SharedConfig.getDevicePerformanceClass() == 0;
                if (!z2) {
                    for (int i3 = 0; i3 < this.imageViewEmojis.size(); i3++) {
                        EmojiImageView emojiImageView = (EmojiImageView) this.imageViewEmojis.get(i3);
                        if (emojiImageView.pressedProgress != 0.0f || emojiImageView.backAnimator != null || emojiImageView.getTranslationX() != 0.0f || emojiImageView.getTranslationY() != 0.0f || emojiImageView.getAlpha() != 1.0f) {
                            break;
                        }
                    }
                }
                z = z2;
                super.draw(canvas, j, i, i2, f);
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void drawInBackground(Canvas canvas) {
                for (int i = 0; i < this.drawInBackgroundViews.size(); i++) {
                    EmojiImageView emojiImageView = (EmojiImageView) this.drawInBackgroundViews.get(i);
                    emojiImageView.imageReceiver.draw(canvas, emojiImageView.backgroundThreadDrawHolder[this.threadIndex]);
                }
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            protected void drawInUiThread(Canvas canvas, float f) {
                AnimatedEmojiDrawable animatedEmojiDrawable;
                if (this.imageViewEmojis != null) {
                    for (int i = 0; i < this.imageViewEmojis.size(); i++) {
                        EmojiImageView emojiImageView = (EmojiImageView) this.imageViewEmojis.get(i);
                        if (emojiImageView.span != null && (animatedEmojiDrawable = (AnimatedEmojiDrawable) EmojiPacksAlert.this.animatedEmojiDrawables.get(emojiImageView.span.getDocumentId())) != null && animatedEmojiDrawable.getImageReceiver() != null && emojiImageView.imageReceiver != null) {
                            animatedEmojiDrawable.setAlpha((int) (255.0f * f * emojiImageView.getAlpha()));
                            float width = ((emojiImageView.getWidth() - emojiImageView.getPaddingLeft()) - emojiImageView.getPaddingRight()) / 2.0f;
                            float height = ((emojiImageView.getHeight() - emojiImageView.getPaddingTop()) - emojiImageView.getPaddingBottom()) / 2.0f;
                            float left = (emojiImageView.getLeft() + emojiImageView.getRight()) / 2.0f;
                            float paddingTop = emojiImageView.getPaddingTop() + height;
                            float f2 = emojiImageView.pressedProgress != 0.0f ? 1.0f * (((1.0f - emojiImageView.pressedProgress) * 0.2f) + 0.8f) : 1.0f;
                            animatedEmojiDrawable.setBounds((int) (left - ((emojiImageView.getScaleX() * width) * f2)), (int) (paddingTop - ((emojiImageView.getScaleY() * height) * f2)), (int) (left + (width * emojiImageView.getScaleX() * f2)), (int) (paddingTop + (height * emojiImageView.getScaleY() * f2)));
                            animatedEmojiDrawable.draw(canvas);
                        }
                    }
                }
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void onFrameReady() {
                super.onFrameReady();
                for (int i = 0; i < this.drawInBackgroundViews.size(); i++) {
                    ((EmojiImageView) this.drawInBackgroundViews.get(i)).backgroundThreadDrawHolder[this.threadIndex].release();
                }
                ((BottomSheet) EmojiPacksAlert.this).containerView.invalidate();
            }

            @Override // org.telegram.ui.Components.DrawingInBackgroundThreadDrawable
            public void prepareDraw(long j) {
                AnimatedEmojiDrawable animatedEmojiDrawable;
                this.drawInBackgroundViews.clear();
                for (int i = 0; i < this.imageViewEmojis.size(); i++) {
                    EmojiImageView emojiImageView = (EmojiImageView) this.imageViewEmojis.get(i);
                    if (emojiImageView.span != null && (animatedEmojiDrawable = (AnimatedEmojiDrawable) EmojiPacksAlert.this.animatedEmojiDrawables.get(emojiImageView.span.getDocumentId())) != null && animatedEmojiDrawable.getImageReceiver() != null) {
                        animatedEmojiDrawable.update(j);
                        ImageReceiver.BackgroundThreadDrawHolder[] backgroundThreadDrawHolderArr = emojiImageView.backgroundThreadDrawHolder;
                        int i2 = this.threadIndex;
                        ImageReceiver imageReceiver = animatedEmojiDrawable.getImageReceiver();
                        ImageReceiver.BackgroundThreadDrawHolder[] backgroundThreadDrawHolderArr2 = emojiImageView.backgroundThreadDrawHolder;
                        int i3 = this.threadIndex;
                        backgroundThreadDrawHolderArr[i2] = imageReceiver.setDrawInBackgroundThread(backgroundThreadDrawHolderArr2[i3], i3);
                        emojiImageView.backgroundThreadDrawHolder[this.threadIndex].time = j;
                        animatedEmojiDrawable.setAlpha(NotificationCenter.newLocationAvailable);
                        android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                        rect.set(emojiImageView.getLeft() + emojiImageView.getPaddingLeft(), emojiImageView.getPaddingTop(), emojiImageView.getRight() - emojiImageView.getPaddingRight(), emojiImageView.getMeasuredHeight() - emojiImageView.getPaddingBottom());
                        emojiImageView.backgroundThreadDrawHolder[this.threadIndex].setBounds(rect);
                        EmojiPacksAlert emojiPacksAlert = EmojiPacksAlert.this;
                        animatedEmojiDrawable.setColorFilter(emojiPacksAlert.getAdaptiveEmojiColorFilter(emojiPacksAlert.getThemedColor(Theme.key_windowBackgroundWhiteBlackText)));
                        emojiImageView.imageReceiver = animatedEmojiDrawable.getImageReceiver();
                        this.drawInBackgroundViews.add(emojiImageView);
                    }
                }
            }
        }

        public ContentView(Context context) {
            super(context);
            this.paint = new Paint();
            this.path = new Path();
            this.lastOpen = null;
            this.viewsGroupedByLines = new SparseArray();
            this.lineDrawables = new ArrayList();
            this.lineDrawablesTmp = new ArrayList();
            this.unusedArrays = new ArrayList();
            this.unusedLineDrawables = new ArrayList();
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.statusBarT = new AnimatedFloat(this, 0L, 350L, cubicBezierInterpolator);
            this.previewImageVisibleT = new AnimatedFloat(this, 0L, 320L, cubicBezierInterpolator);
        }

        private AnimatedEmojiSpan[] getAnimatedEmojiSpans() {
            if (EmojiPacksAlert.this.listView == null) {
                return new AnimatedEmojiSpan[0];
            }
            AnimatedEmojiSpan[] animatedEmojiSpanArr = new AnimatedEmojiSpan[EmojiPacksAlert.this.listView.getChildCount()];
            for (int i = 0; i < EmojiPacksAlert.this.listView.getChildCount(); i++) {
                View childAt = EmojiPacksAlert.this.listView.getChildAt(i);
                if (childAt instanceof EmojiImageView) {
                    animatedEmojiSpanArr[i] = ((EmojiImageView) childAt).span;
                }
            }
            return animatedEmojiSpanArr;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            DrawingInBackgroundLine drawingInBackgroundLine;
            DrawingInBackgroundLine drawingInBackgroundLine2;
            AnimatedEmojiSpan animatedEmojiSpan;
            if (this.attached) {
                this.paint.setColor(EmojiPacksAlert.this.getThemedColor(Theme.key_dialogBackground));
                Theme.applyDefaultShadow(this.paint);
                this.path.reset();
                float f = EmojiPacksAlert.this.lastY = r1.getListTop();
                float f2 = this.statusBarT.set(f <= ((float) ((BottomSheet) EmojiPacksAlert.this).containerView.getPaddingTop()));
                float lerp = AndroidUtilities.lerp(f, 0.0f, f2);
                if (this.previewImageReceiver != null) {
                    float dp = AndroidUtilities.dp(140.0f);
                    float dp2 = AndroidUtilities.dp(20.0f);
                    if (lerp < dp + dp2) {
                        this.previewImageVisible = false;
                    }
                    this.previewImageReceiver.setAlpha(this.previewImageVisibleT.set(this.previewImageVisible));
                    if (this.previewImageReceiver.getAlpha() > 0.0f) {
                        float alpha = ((this.previewImageReceiver.getAlpha() * 0.4f) + 0.6f) * dp;
                        float f3 = alpha / 2.0f;
                        this.previewImageReceiver.setImageCoords((getWidth() / 2.0f) - f3, ((lerp - dp2) - (dp / 2.0f)) - f3, alpha, alpha);
                        this.previewImageReceiver.draw(canvas);
                    } else {
                        this.previewImageReceiver.onDetachedFromWindow();
                        this.previewImageReceiver = null;
                    }
                }
                float dp3 = AndroidUtilities.dp((1.0f - f2) * 14.0f);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(getPaddingLeft(), lerp, getWidth() - getPaddingRight(), getBottom() + dp3);
                this.path.addRoundRect(rectF, dp3, dp3, Path.Direction.CW);
                canvas.drawPath(this.path, this.paint);
                boolean z = f2 > 0.5f;
                Boolean bool = this.lastOpen;
                if (bool == null || z != bool.booleanValue()) {
                    EmojiPacksAlert emojiPacksAlert = EmojiPacksAlert.this;
                    this.lastOpen = Boolean.valueOf(z);
                    emojiPacksAlert.updateLightStatusBar(z);
                }
                Theme.dialogs_onlineCirclePaint.setColor(EmojiPacksAlert.this.getThemedColor(Theme.key_sheet_scrollUp));
                Theme.dialogs_onlineCirclePaint.setAlpha((int) (MathUtils.clamp(lerp / AndroidUtilities.dp(20.0f), 0.0f, 1.0f) * Theme.dialogs_onlineCirclePaint.getAlpha()));
                int dp4 = AndroidUtilities.dp(36.0f);
                float dp5 = lerp + AndroidUtilities.dp(10.0f);
                rectF.set((getMeasuredWidth() - dp4) / 2, dp5, (getMeasuredWidth() + dp4) / 2, AndroidUtilities.dp(4.0f) + dp5);
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                EmojiPacksAlert.this.shadowView.setVisibility((EmojiPacksAlert.this.listView.canScrollVertically(1) || EmojiPacksAlert.this.removeButtonView.getVisibility() == 0) ? 0 : 4);
                if (EmojiPacksAlert.this.listView != null) {
                    canvas.save();
                    canvas.translate(EmojiPacksAlert.this.listView.getLeft(), EmojiPacksAlert.this.listView.getY() + 0.0f);
                    canvas.clipRect(0, 0, EmojiPacksAlert.this.listView.getWidth(), EmojiPacksAlert.this.listView.getHeight());
                    canvas.saveLayerAlpha(0.0f, 0.0f, EmojiPacksAlert.this.listView.getWidth(), EmojiPacksAlert.this.listView.getHeight(), (int) (EmojiPacksAlert.this.listView.getAlpha() * 255.0f), 31);
                    for (int i = 0; i < this.viewsGroupedByLines.size(); i++) {
                        ArrayList arrayList = (ArrayList) this.viewsGroupedByLines.valueAt(i);
                        arrayList.clear();
                        this.unusedArrays.add(arrayList);
                    }
                    this.viewsGroupedByLines.clear();
                    for (int i2 = 0; i2 < EmojiPacksAlert.this.listView.getChildCount(); i2++) {
                        View childAt = EmojiPacksAlert.this.listView.getChildAt(i2);
                        if (childAt instanceof EmojiImageView) {
                            EmojiImageView emojiImageView = (EmojiImageView) childAt;
                            emojiImageView.updatePressedProgress();
                            if (EmojiPacksAlert.this.animatedEmojiDrawables != null && (animatedEmojiSpan = emojiImageView.span) != null) {
                                AnimatedEmojiDrawable animatedEmojiDrawable = (AnimatedEmojiDrawable) EmojiPacksAlert.this.animatedEmojiDrawables.get(animatedEmojiSpan.getDocumentId());
                                if (animatedEmojiDrawable != null) {
                                    EmojiPacksAlert emojiPacksAlert2 = EmojiPacksAlert.this;
                                    animatedEmojiDrawable.setColorFilter(emojiPacksAlert2.getAdaptiveEmojiColorFilter(emojiPacksAlert2.getThemedColor(Theme.key_windowBackgroundWhiteBlackText)));
                                    ArrayList arrayList2 = (ArrayList) this.viewsGroupedByLines.get(childAt.getTop());
                                    if (arrayList2 == null) {
                                        if (this.unusedArrays.isEmpty()) {
                                            arrayList2 = new ArrayList();
                                        } else {
                                            ArrayList arrayList3 = this.unusedArrays;
                                            arrayList2 = (ArrayList) arrayList3.remove(arrayList3.size() - 1);
                                        }
                                        this.viewsGroupedByLines.put(childAt.getTop(), arrayList2);
                                    }
                                    arrayList2.add(emojiImageView);
                                }
                            }
                        } else {
                            canvas.save();
                            canvas.translate(childAt.getLeft(), childAt.getTop());
                            childAt.draw(canvas);
                            canvas.restore();
                        }
                    }
                    this.lineDrawablesTmp.clear();
                    this.lineDrawablesTmp.addAll(this.lineDrawables);
                    this.lineDrawables.clear();
                    long currentTimeMillis = System.currentTimeMillis();
                    for (int i3 = 0; i3 < this.viewsGroupedByLines.size(); i3++) {
                        ArrayList arrayList4 = (ArrayList) this.viewsGroupedByLines.valueAt(i3);
                        View view = (View) arrayList4.get(0);
                        int childAdapterPosition = EmojiPacksAlert.this.listView.getChildAdapterPosition(view);
                        int i4 = 0;
                        while (true) {
                            if (i4 >= this.lineDrawablesTmp.size()) {
                                drawingInBackgroundLine = null;
                                break;
                            } else {
                                if (((DrawingInBackgroundLine) this.lineDrawablesTmp.get(i4)).position == childAdapterPosition) {
                                    drawingInBackgroundLine = (DrawingInBackgroundLine) this.lineDrawablesTmp.get(i4);
                                    this.lineDrawablesTmp.remove(i4);
                                    break;
                                }
                                i4++;
                            }
                        }
                        if (drawingInBackgroundLine == null) {
                            if (this.unusedLineDrawables.isEmpty()) {
                                drawingInBackgroundLine2 = new DrawingInBackgroundLine();
                                drawingInBackgroundLine2.setLayerNum(7);
                            } else {
                                ArrayList arrayList5 = this.unusedLineDrawables;
                                drawingInBackgroundLine2 = (DrawingInBackgroundLine) arrayList5.remove(arrayList5.size() - 1);
                            }
                            drawingInBackgroundLine2.position = childAdapterPosition;
                            drawingInBackgroundLine2.onAttachToWindow();
                        } else {
                            drawingInBackgroundLine2 = drawingInBackgroundLine;
                        }
                        this.lineDrawables.add(drawingInBackgroundLine2);
                        drawingInBackgroundLine2.imageViewEmojis = arrayList4;
                        canvas.save();
                        canvas.translate(0.0f, view.getY() + view.getPaddingTop());
                        drawingInBackgroundLine2.draw(canvas, currentTimeMillis, getMeasuredWidth(), view.getMeasuredHeight() - view.getPaddingBottom(), 1.0f);
                        canvas.restore();
                    }
                    for (int i5 = 0; i5 < this.lineDrawablesTmp.size(); i5++) {
                        if (this.unusedLineDrawables.size() < 3) {
                            this.unusedLineDrawables.add((DrawingInBackgroundLine) this.lineDrawablesTmp.get(i5));
                            ((DrawingInBackgroundLine) this.lineDrawablesTmp.get(i5)).imageViewEmojis = null;
                            ((DrawingInBackgroundLine) this.lineDrawablesTmp.get(i5)).reset();
                        } else {
                            ((DrawingInBackgroundLine) this.lineDrawablesTmp.get(i5)).onDetachFromWindow();
                        }
                    }
                    this.lineDrawablesTmp.clear();
                    canvas.restore();
                    canvas.restore();
                    if (EmojiPacksAlert.this.listView.getAlpha() < 1.0f) {
                        int width = getWidth() / 2;
                        int height = (((int) dp5) + getHeight()) / 2;
                        int dp6 = AndroidUtilities.dp(16.0f);
                        EmojiPacksAlert.this.progressDrawable.setAlpha((int) ((1.0f - EmojiPacksAlert.this.listView.getAlpha()) * 255.0f));
                        EmojiPacksAlert.this.progressDrawable.setBounds(width - dp6, height - dp6, width + dp6, height + dp6);
                        EmojiPacksAlert.this.progressDrawable.draw(canvas);
                        invalidate();
                    }
                }
                super.dispatchDraw(canvas);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && motionEvent.getY() < EmojiPacksAlert.this.getListTop() - AndroidUtilities.dp(6.0f)) {
                EmojiPacksAlert.this.dismiss();
            }
            return super.dispatchTouchEvent(motionEvent);
        }

        public void hidePreviewEmoji() {
            if (this.previewImageVisible) {
                this.previewImageVisible = false;
                invalidate();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.attached = true;
            ImageReceiver imageReceiver = this.previewImageReceiver;
            if (imageReceiver != null) {
                imageReceiver.onAttachedToWindow();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.attached = false;
            for (int i = 0; i < this.lineDrawables.size(); i++) {
                ((DrawingInBackgroundLine) this.lineDrawables.get(i)).onDetachFromWindow();
            }
            for (int i2 = 0; i2 < this.unusedLineDrawables.size(); i2++) {
                ((DrawingInBackgroundLine) this.unusedLineDrawables.get(i2)).onDetachFromWindow();
            }
            this.lineDrawables.clear();
            AnimatedEmojiSpan.release(this, (LongSparseArray<AnimatedEmojiDrawable>) EmojiPacksAlert.this.animatedEmojiDrawables);
            ImageReceiver imageReceiver = this.previewImageReceiver;
            if (imageReceiver != null) {
                imageReceiver.onDetachedFromWindow();
            }
        }

        public void setPreviewEmoji(TLRPC.Document document) {
            ImageReceiver imageReceiver = new ImageReceiver(this);
            this.previewImageReceiver = imageReceiver;
            if (this.attached) {
                imageReceiver.onAttachedToWindow();
            }
            this.previewImageVisible = true;
            this.previewImageVisibleT.set(1.0f, true);
            TLRPC.PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 90);
            this.previewImageReceiver.setImage(ImageLocation.getForDocument(document), "140_140", ImageLocation.getForDocument(closestPhotoSizeWithSize, document), "140_140", DocumentObject.getSvgThumb(document.thumbs, Theme.key_windowBackgroundWhiteGrayIcon, 0.2f, true), 0L, null, null, 0);
            this.previewImageReceiver.setLayerNum(7);
            this.previewImageReceiver.setAllowStartLottieAnimation(true);
            this.previewImageReceiver.setAllowStartAnimation(true);
            this.previewImageReceiver.setAutoRepeat(1);
            this.previewImageReceiver.setAllowDecodeSingleFrame(true);
            this.previewImageReceiver.setParentView(this);
        }

        public void updateEmojiDrawables() {
            EmojiPacksAlert.this.animatedEmojiDrawables = AnimatedEmojiSpan.update(3, this, getAnimatedEmojiSpans(), (LongSparseArray<AnimatedEmojiDrawable>) EmojiPacksAlert.this.animatedEmojiDrawables);
        }
    }

    public static class EmojiImageView extends View {
        ValueAnimator backAnimator;
        public ImageReceiver.BackgroundThreadDrawHolder[] backgroundThreadDrawHolder;
        public ImageReceiver imageReceiver;
        private float pressedProgress;
        public AnimatedEmojiSpan span;

        public EmojiImageView(Context context) {
            super(context);
            this.backgroundThreadDrawHolder = new ImageReceiver.BackgroundThreadDrawHolder[2];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setPressed$0(ValueAnimator valueAnimator) {
            this.pressedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (getParent() instanceof View) {
                ((View) getParent()).invalidate();
            }
        }

        public TLRPC.Document getDocument() {
            AnimatedEmojiSpan animatedEmojiSpan = this.span;
            if (animatedEmojiSpan == null) {
                return null;
            }
            TLRPC.Document document = animatedEmojiSpan.document;
            if (document != null) {
                return document;
            }
            return AnimatedEmojiDrawable.findDocument(UserConfig.selectedAccount, animatedEmojiSpan.getDocumentId());
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setPadding(AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f));
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824));
        }

        @Override // android.view.View
        public void setPressed(boolean z) {
            ValueAnimator valueAnimator;
            if (isPressed() != z) {
                super.setPressed(z);
                invalidate();
                if (z && (valueAnimator = this.backAnimator) != null) {
                    valueAnimator.removeAllListeners();
                    this.backAnimator.cancel();
                }
                if (z) {
                    return;
                }
                float f = this.pressedProgress;
                if (f != 0.0f) {
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(f, 0.0f);
                    this.backAnimator = ofFloat;
                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiImageView$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            EmojiPacksAlert.EmojiImageView.this.lambda$setPressed$0(valueAnimator2);
                        }
                    });
                    this.backAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.EmojiPacksAlert.EmojiImageView.1
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            EmojiImageView.this.backAnimator = null;
                        }
                    });
                    this.backAnimator.setInterpolator(new OvershootInterpolator(5.0f));
                    this.backAnimator.setDuration(350L);
                    this.backAnimator.start();
                }
            }
        }

        public void updatePressedProgress() {
            if (isPressed()) {
                float f = this.pressedProgress;
                if (f != 1.0f) {
                    this.pressedProgress = Utilities.clamp(f + 0.16f, 1.0f, 0.0f);
                    invalidate();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class EmojiPackHeader extends FrameLayout {
        public TextView addButtonView;
        private ValueAnimator animator;
        public BaseFragment dummyFragment;
        public ActionBarMenuItem optionsButton;
        public TextView removeButtonView;
        private TLRPC.TL_messages_stickerSet set;
        private boolean single;
        public TextView subtitleView;
        public LinkSpanDrawable.LinksTextView titleView;
        private float toggleT;
        private boolean toggled;
        public PremiumButtonView unlockButtonView;

        public EmojiPackHeader(Context context, boolean z) {
            super(context);
            float f;
            LinkSpanDrawable.LinksTextView linksTextView;
            float f2;
            float f3;
            float f4;
            float f5;
            int i;
            float f6;
            this.dummyFragment = new BaseFragment() { // from class: org.telegram.ui.Components.EmojiPacksAlert.EmojiPackHeader.1
                @Override // org.telegram.ui.ActionBar.BaseFragment
                public int getCurrentAccount() {
                    return this.currentAccount;
                }

                @Override // org.telegram.ui.ActionBar.BaseFragment
                public View getFragmentView() {
                    return ((BottomSheet) EmojiPacksAlert.this).containerView;
                }

                @Override // org.telegram.ui.ActionBar.BaseFragment
                public FrameLayout getLayoutContainer() {
                    return (FrameLayout) ((BottomSheet) EmojiPacksAlert.this).containerView;
                }

                @Override // org.telegram.ui.ActionBar.BaseFragment
                public Theme.ResourcesProvider getResourceProvider() {
                    return ((BottomSheet) EmojiPacksAlert.this).resourcesProvider;
                }
            };
            this.toggled = false;
            this.toggleT = 0.0f;
            this.single = z;
            if (z) {
                f = 32.0f;
            } else {
                float f7 = 8.0f;
                if (!UserConfig.getInstance(((BottomSheet) EmojiPacksAlert.this).currentAccount).isPremium()) {
                    PremiumButtonView premiumButtonView = new PremiumButtonView(context, AndroidUtilities.dp(4.0f), false, ((BottomSheet) EmojiPacksAlert.this).resourcesProvider);
                    this.unlockButtonView = premiumButtonView;
                    premiumButtonView.setButton(LocaleController.getString(R.string.Unlock), new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPackHeader$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            EmojiPacksAlert.EmojiPackHeader.this.lambda$new$0(view);
                        }
                    });
                    this.unlockButtonView.setIcon(R.raw.unlock_icon);
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.unlockButtonView.getIconView().getLayoutParams();
                    marginLayoutParams.leftMargin = AndroidUtilities.dp(1.0f);
                    marginLayoutParams.topMargin = AndroidUtilities.dp(1.0f);
                    int dp = AndroidUtilities.dp(20.0f);
                    marginLayoutParams.height = dp;
                    marginLayoutParams.width = dp;
                    ((ViewGroup.MarginLayoutParams) this.unlockButtonView.getTextView().getLayoutParams()).leftMargin = AndroidUtilities.dp(3.0f);
                    this.unlockButtonView.getChildAt(0).setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
                    addView(this.unlockButtonView, LayoutHelper.createFrameRelatively(-2.0f, 28.0f, 8388661, 0.0f, 15.66f, 5.66f, 0.0f));
                    this.unlockButtonView.measure(View.MeasureSpec.makeMeasureSpec(99999, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(28.0f), 1073741824));
                    f7 = (this.unlockButtonView.getMeasuredWidth() + AndroidUtilities.dp(16.0f)) / AndroidUtilities.density;
                }
                TextView textView = new TextView(context);
                this.addButtonView = textView;
                textView.setTypeface(AndroidUtilities.bold());
                this.addButtonView.setTextColor(EmojiPacksAlert.this.getThemedColor(Theme.key_featuredStickers_buttonText));
                TextView textView2 = this.addButtonView;
                int i2 = Theme.key_featuredStickers_addButton;
                textView2.setBackground(Theme.AdaptiveRipple.filledRect(EmojiPacksAlert.this.getThemedColor(i2), 4.0f));
                this.addButtonView.setText(LocaleController.getString(R.string.Add));
                this.addButtonView.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
                this.addButtonView.setGravity(17);
                this.addButtonView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPackHeader$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        EmojiPacksAlert.EmojiPackHeader.this.lambda$new$1(view);
                    }
                });
                addView(this.addButtonView, LayoutHelper.createFrameRelatively(-2.0f, 28.0f, 8388661, 0.0f, 15.66f, 5.66f, 0.0f));
                this.addButtonView.measure(View.MeasureSpec.makeMeasureSpec(99999, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(28.0f), 1073741824));
                float max = Math.max(f7, (this.addButtonView.getMeasuredWidth() + AndroidUtilities.dp(16.0f)) / AndroidUtilities.density);
                TextView textView3 = new TextView(context);
                this.removeButtonView = textView3;
                textView3.setTypeface(AndroidUtilities.bold());
                this.removeButtonView.setTextColor(EmojiPacksAlert.this.getThemedColor(i2));
                this.removeButtonView.setBackground(Theme.createRadSelectorDrawable(EmojiPacksAlert.this.getThemedColor(i2) & 268435455, 4, 4));
                this.removeButtonView.setText(LocaleController.getString(R.string.StickersRemove));
                this.removeButtonView.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
                this.removeButtonView.setGravity(17);
                this.removeButtonView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPackHeader$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        EmojiPacksAlert.EmojiPackHeader.this.lambda$new$3(view);
                    }
                });
                this.removeButtonView.setClickable(false);
                addView(this.removeButtonView, LayoutHelper.createFrameRelatively(-2.0f, 28.0f, 8388661, 0.0f, 15.66f, 5.66f, 0.0f));
                this.removeButtonView.setScaleX(0.0f);
                this.removeButtonView.setScaleY(0.0f);
                this.removeButtonView.setAlpha(0.0f);
                this.removeButtonView.measure(View.MeasureSpec.makeMeasureSpec(99999, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(28.0f), 1073741824));
                f = Math.max(max, (this.removeButtonView.getMeasuredWidth() + AndroidUtilities.dp(16.0f)) / AndroidUtilities.density);
            }
            LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context, ((BottomSheet) EmojiPacksAlert.this).resourcesProvider);
            this.titleView = linksTextView2;
            linksTextView2.setPadding(AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(2.0f), 0);
            this.titleView.setTypeface(AndroidUtilities.bold());
            LinkSpanDrawable.LinksTextView linksTextView3 = this.titleView;
            TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
            linksTextView3.setEllipsize(truncateAt);
            this.titleView.setSingleLine(true);
            this.titleView.setLines(1);
            this.titleView.setLinkTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText, ((BottomSheet) EmojiPacksAlert.this).resourcesProvider));
            this.titleView.setTextColor(EmojiPacksAlert.this.getThemedColor(Theme.key_dialogTextBlack));
            LinkSpanDrawable.LinksTextView linksTextView4 = this.titleView;
            if (z) {
                linksTextView4.setTextSize(1, 20.0f);
                linksTextView = this.titleView;
                f2 = 11.0f;
                f3 = 0.0f;
                f4 = -1.0f;
                f5 = -2.0f;
                i = 8388659;
                f6 = 12.0f;
            } else {
                linksTextView4.setTextSize(1, 17.0f);
                linksTextView = this.titleView;
                f2 = 10.0f;
                f3 = 0.0f;
                f4 = -1.0f;
                f5 = -2.0f;
                i = 8388659;
                f6 = 6.0f;
            }
            addView(linksTextView, LayoutHelper.createFrameRelatively(f4, f5, i, f6, f2, f, f3));
            if (!z) {
                TextView textView4 = new TextView(context);
                this.subtitleView = textView4;
                textView4.setTextSize(1, 13.0f);
                this.subtitleView.setTextColor(EmojiPacksAlert.this.getThemedColor(Theme.key_dialogTextGray2));
                this.subtitleView.setEllipsize(truncateAt);
                this.subtitleView.setSingleLine(true);
                this.subtitleView.setLines(1);
                addView(this.subtitleView, LayoutHelper.createFrameRelatively(-1.0f, -2.0f, 8388659, 8.0f, 31.66f, f, 0.0f));
            }
            if (z) {
                ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, (ActionBarMenu) null, 0, EmojiPacksAlert.this.getThemedColor(Theme.key_sheet_other), ((BottomSheet) EmojiPacksAlert.this).resourcesProvider);
                this.optionsButton = actionBarMenuItem;
                actionBarMenuItem.setLongClickEnabled(false);
                this.optionsButton.setSubMenuOpenSide(2);
                this.optionsButton.setIcon(R.drawable.ic_ab_other);
                this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(EmojiPacksAlert.this.getThemedColor(Theme.key_player_actionBarSelector), 1));
                addView(this.optionsButton, LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, 5.0f, 5.0f - (((BottomSheet) EmojiPacksAlert.this).backgroundPaddingLeft / AndroidUtilities.density), 0.0f));
                this.optionsButton.addSubItem(1, R.drawable.msg_share, LocaleController.getString(R.string.StickersShare));
                this.optionsButton.addSubItem(2, R.drawable.msg_link, LocaleController.getString(R.string.CopyLink));
                this.optionsButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPackHeader$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        EmojiPacksAlert.EmojiPackHeader.this.lambda$new$4(view);
                    }
                });
                this.optionsButton.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPackHeader$$ExternalSyntheticLambda5
                    @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
                    public final void onItemClick(int i3) {
                        EmojiPacksAlert.access$6500(EmojiPacksAlert.this, i3);
                    }
                });
                this.optionsButton.setContentDescription(LocaleController.getString(R.string.AccDescrMoreOptions));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            EmojiPacksAlert.this.premiumButtonClicked = SystemClock.elapsedRealtime();
            EmojiPacksAlert.this.showPremiumAlert();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view) {
            EmojiPacksAlert.installSet(this.dummyFragment, this.set, true);
            toggle(true, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2() {
            toggle(true, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(View view) {
            EmojiPacksAlert.uninstallSet(this.dummyFragment, this.set, true, new Runnable() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPackHeader$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    EmojiPacksAlert.EmojiPackHeader.this.lambda$new$2();
                }
            }, true);
            toggle(false, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(View view) {
            this.optionsButton.toggleSubMenu();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$toggle$6(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.toggleT = floatValue;
            this.addButtonView.setScaleX(1.0f - floatValue);
            this.addButtonView.setScaleY(1.0f - this.toggleT);
            this.addButtonView.setAlpha(1.0f - this.toggleT);
            this.removeButtonView.setScaleX(this.toggleT);
            this.removeButtonView.setScaleY(this.toggleT);
            this.removeButtonView.setAlpha(this.toggleT);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void toggle(boolean z, boolean z2) {
            if (this.toggled == z) {
                return;
            }
            this.toggled = z;
            ValueAnimator valueAnimator = this.animator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.animator = null;
            }
            TextView textView = this.addButtonView;
            if (textView == null || this.removeButtonView == null) {
                return;
            }
            textView.setClickable(!z);
            this.removeButtonView.setClickable(z);
            if (z2) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.toggleT, z ? 1.0f : 0.0f);
                this.animator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPackHeader$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        EmojiPacksAlert.EmojiPackHeader.this.lambda$toggle$6(valueAnimator2);
                    }
                });
                this.animator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.animator.setDuration(250L);
                this.animator.start();
                return;
            }
            this.toggleT = z ? 1.0f : 0.0f;
            this.addButtonView.setScaleX(z ? 0.0f : 1.0f);
            this.addButtonView.setScaleY(z ? 0.0f : 1.0f);
            this.addButtonView.setAlpha(z ? 0.0f : 1.0f);
            this.removeButtonView.setScaleX(z ? 1.0f : 0.0f);
            this.removeButtonView.setScaleY(z ? 1.0f : 0.0f);
            this.removeButtonView.setAlpha(z ? 1.0f : 0.0f);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(this.single ? 42.0f : 56.0f), 1073741824));
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0083  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0094  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00b8  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x00e4  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x00eb  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x00f2  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x00f7  */
        /* JADX WARN: Type inference failed for: r0v4, types: [android.widget.TextView, org.telegram.ui.Components.LinkSpanDrawable$LinksTextView] */
        /* JADX WARN: Type inference failed for: r3v11, types: [android.text.SpannableStringBuilder] */
        /* JADX WARN: Type inference failed for: r3v14 */
        /* JADX WARN: Type inference failed for: r3v16 */
        /* JADX WARN: Type inference failed for: r3v17 */
        /* JADX WARN: Type inference failed for: r3v18 */
        /* JADX WARN: Type inference failed for: r3v4, types: [java.lang.CharSequence] */
        /* JADX WARN: Type inference failed for: r4v4, types: [android.text.SpannableStringBuilder] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void set(TLRPC.TL_messages_stickerSet tL_messages_stickerSet, int i, boolean z) {
            TextView textView;
            PremiumButtonView premiumButtonView;
            TextView textView2;
            TextView textView3;
            TLRPC.StickerSet stickerSet;
            1 r3;
            Exception e;
            this.set = tL_messages_stickerSet;
            1 r0 = null;
            if (tL_messages_stickerSet == null || tL_messages_stickerSet.set == null) {
                this.titleView.setText((CharSequence) null);
            } else {
                try {
                    if (EmojiPacksAlert.urlPattern == null) {
                        Pattern unused = EmojiPacksAlert.urlPattern = Pattern.compile("@[a-zA-Z\\d_]{1,32}");
                    }
                    Matcher matcher = EmojiPacksAlert.urlPattern.matcher(tL_messages_stickerSet.set.title);
                    r3 = null;
                    while (true) {
                        try {
                            ?? r32 = r3;
                            if (!matcher.find()) {
                                break;
                            }
                            if (r3 == null) {
                                ?? spannableStringBuilder = new SpannableStringBuilder(tL_messages_stickerSet.set.title);
                                try {
                                    this.titleView.setMovementMethod(new LinkMovementMethodMy(r0));
                                    r32 = spannableStringBuilder;
                                } catch (Exception e2) {
                                    e = e2;
                                    r3 = spannableStringBuilder;
                                    FileLog.e(e);
                                    ?? r02 = this.titleView;
                                    ?? r33 = r3;
                                    if (r3 == null) {
                                    }
                                    r02.setText(r33);
                                    textView = this.subtitleView;
                                    if (textView != null) {
                                    }
                                    if (!z) {
                                    }
                                    premiumButtonView = this.unlockButtonView;
                                    if (premiumButtonView != null) {
                                    }
                                    textView2 = this.addButtonView;
                                    if (textView2 != null) {
                                    }
                                    textView3 = this.removeButtonView;
                                    if (textView3 != null) {
                                    }
                                    toggle(tL_messages_stickerSet == null && MediaDataController.getInstance(((BottomSheet) EmojiPacksAlert.this).currentAccount).isStickerPackInstalled(tL_messages_stickerSet.set.id), false);
                                }
                            }
                            int start = matcher.start();
                            int end = matcher.end();
                            if (tL_messages_stickerSet.set.title.charAt(start) != '@') {
                                start++;
                            }
                            r32.setSpan(new URLSpanNoUnderline(tL_messages_stickerSet.set.title.subSequence(start + 1, end).toString()) { // from class: org.telegram.ui.Components.EmojiPacksAlert.EmojiPackHeader.2
                                @Override // org.telegram.ui.Components.URLSpanNoUnderline, android.text.style.URLSpan, android.text.style.ClickableSpan
                                public void onClick(View view) {
                                    MessagesController.getInstance(((BottomSheet) EmojiPacksAlert.this).currentAccount).openByUserName(getURL(), EmojiPacksAlert.this.fragment, 1);
                                    EmojiPacksAlert.this.onCloseByLink();
                                    EmojiPacksAlert.this.dismiss();
                                }
                            }, start, end, 0);
                            r3 = r32;
                        } catch (Exception e3) {
                            e = e3;
                            r0 = r3;
                            r3 = r0;
                            e = e;
                            FileLog.e(e);
                            ?? r022 = this.titleView;
                            ?? r332 = r3;
                            if (r3 == null) {
                            }
                            r022.setText(r332);
                            textView = this.subtitleView;
                            if (textView != null) {
                            }
                            if (!z) {
                            }
                            premiumButtonView = this.unlockButtonView;
                            if (premiumButtonView != null) {
                            }
                            textView2 = this.addButtonView;
                            if (textView2 != null) {
                            }
                            textView3 = this.removeButtonView;
                            if (textView3 != null) {
                            }
                            toggle(tL_messages_stickerSet == null && MediaDataController.getInstance(((BottomSheet) EmojiPacksAlert.this).currentAccount).isStickerPackInstalled(tL_messages_stickerSet.set.id), false);
                        }
                    }
                } catch (Exception e4) {
                    e = e4;
                }
                ?? r0222 = this.titleView;
                ?? r3322 = r3;
                if (r3 == null) {
                    r3322 = tL_messages_stickerSet.set.title;
                }
                r0222.setText(r3322);
            }
            textView = this.subtitleView;
            if (textView != null) {
                textView.setText((tL_messages_stickerSet == null || (stickerSet = tL_messages_stickerSet.set) == null || stickerSet.emojis) ? LocaleController.formatPluralString("EmojiCount", i, new Object[0]) : LocaleController.formatPluralString("Stickers", i, new Object[0]));
            }
            if (!z && this.unlockButtonView != null && !UserConfig.getInstance(((BottomSheet) EmojiPacksAlert.this).currentAccount).isPremium()) {
                this.unlockButtonView.setVisibility(0);
                TextView textView4 = this.addButtonView;
                if (textView4 != null) {
                    textView4.setVisibility(8);
                }
                TextView textView5 = this.removeButtonView;
                if (textView5 != null) {
                    textView5.setVisibility(8);
                    return;
                }
                return;
            }
            premiumButtonView = this.unlockButtonView;
            if (premiumButtonView != null) {
                premiumButtonView.setVisibility(8);
            }
            textView2 = this.addButtonView;
            if (textView2 != null) {
                textView2.setVisibility(0);
            }
            textView3 = this.removeButtonView;
            if (textView3 != null) {
                textView3.setVisibility(0);
            }
            toggle(tL_messages_stickerSet == null && MediaDataController.getInstance(((BottomSheet) EmojiPacksAlert.this).currentAccount).isStickerPackInstalled(tL_messages_stickerSet.set.id), false);
        }
    }

    class EmojiPacksLoader implements NotificationCenter.NotificationCenterDelegate {
        private int currentAccount;
        public ArrayList[] data;
        public ArrayList inputStickerSets;
        public TLObject parentObject;
        public ArrayList stickerSets;
        final int loadingStickersCount = 12;
        private boolean started = false;

        public EmojiPacksLoader(int i, ArrayList arrayList, TLObject tLObject) {
            this.currentAccount = i;
            if (arrayList == null && tLObject == null) {
                arrayList = new ArrayList();
            }
            this.inputStickerSets = arrayList;
            this.parentObject = tLObject;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$init$0(TLRPC.TL_error tL_error, TLObject tLObject) {
            TLRPC.StickerSet stickerSet;
            if (tL_error != null || !(tLObject instanceof TLRPC.Vector)) {
                EmojiPacksAlert.this.dismiss();
                if (EmojiPacksAlert.this.fragment == null || EmojiPacksAlert.this.fragment.getParentActivity() == null) {
                    return;
                }
                BulletinFactory.of(EmojiPacksAlert.this.fragment).createErrorBulletin(LocaleController.getString(R.string.UnknownError)).show();
                return;
            }
            TLRPC.Vector vector = (TLRPC.Vector) tLObject;
            if (this.inputStickerSets == null) {
                this.inputStickerSets = new ArrayList();
            }
            for (int i = 0; i < vector.objects.size(); i++) {
                Object obj = vector.objects.get(i);
                if ((obj instanceof TLRPC.StickerSetCovered) && (stickerSet = ((TLRPC.StickerSetCovered) obj).set) != null) {
                    this.inputStickerSets.add(MediaDataController.getInputStickerSet(stickerSet));
                }
            }
            this.parentObject = null;
            init();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$init$1(final TLObject tLObject, final TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPacksLoader$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    EmojiPacksAlert.EmojiPacksLoader.this.lambda$init$0(tL_error, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$init$2() {
            EmojiPacksAlert.this.dismiss();
            if (EmojiPacksAlert.this.fragment == null || EmojiPacksAlert.this.fragment.getParentActivity() == null) {
                return;
            }
            BulletinFactory.of(EmojiPacksAlert.this.fragment).createErrorBulletin(LocaleController.getString(R.string.AddEmojiNotFound)).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$init$3(boolean[] zArr, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
            if (tL_messages_stickerSet != null || zArr[0]) {
                return;
            }
            zArr[0] = true;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPacksLoader$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    EmojiPacksAlert.EmojiPacksLoader.this.lambda$init$2();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$init$4() {
            EmojiPacksAlert.this.dismiss();
        }

        private void putStickerSet(int i, TLRPC.TL_messages_stickerSet tL_messages_stickerSet) {
            if (i >= 0) {
                ArrayList[] arrayListArr = this.data;
                if (i >= arrayListArr.length) {
                    return;
                }
                int i2 = 0;
                if (tL_messages_stickerSet == null || tL_messages_stickerSet.documents == null) {
                    arrayListArr[i] = new ArrayList(12);
                    while (i2 < 12) {
                        this.data[i].add(null);
                        i2++;
                    }
                    return;
                }
                arrayListArr[i] = new ArrayList();
                while (i2 < tL_messages_stickerSet.documents.size()) {
                    TLRPC.Document document = tL_messages_stickerSet.documents.get(i2);
                    if (document == null) {
                        this.data[i].add(null);
                    } else {
                        EmojiView.CustomEmoji customEmoji = new EmojiView.CustomEmoji();
                        customEmoji.emoticon = findEmoticon(tL_messages_stickerSet, document.id);
                        customEmoji.stickerSet = tL_messages_stickerSet;
                        customEmoji.documentId = document.id;
                        this.data[i].add(customEmoji);
                        if (EmojiPacksAlert.this.limitCount) {
                            TLRPC.StickerSet stickerSet = tL_messages_stickerSet.set;
                            if (this.data[i].size() >= ((stickerSet == null || stickerSet.emojis) ? 16 : 10)) {
                                return;
                            }
                        } else {
                            continue;
                        }
                    }
                    i2++;
                }
            }
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            TLRPC.StickerSet stickerSet;
            if (i == NotificationCenter.groupStickersDidLoad) {
                for (int i3 = 0; i3 < this.stickerSets.size(); i3++) {
                    if (this.stickerSets.get(i3) == null) {
                        TLRPC.TL_messages_stickerSet stickerSet2 = MediaDataController.getInstance(this.currentAccount).getStickerSet((TLRPC.InputStickerSet) this.inputStickerSets.get(i3), true);
                        if (this.stickerSets.size() == 1 && stickerSet2 != null && (stickerSet = stickerSet2.set) != null && !stickerSet.emojis) {
                            EmojiPacksAlert.this.dismiss();
                            new StickersAlert(EmojiPacksAlert.this.getContext(), EmojiPacksAlert.this.fragment, (TLRPC.InputStickerSet) this.inputStickerSets.get(i3), null, EmojiPacksAlert.this.fragment instanceof ChatActivity ? ((ChatActivity) EmojiPacksAlert.this.fragment).getChatActivityEnterView() : null, ((BottomSheet) EmojiPacksAlert.this).resourcesProvider, false).show();
                            return;
                        } else {
                            this.stickerSets.set(i3, stickerSet2);
                            if (stickerSet2 != null) {
                                putStickerSet(i3, stickerSet2);
                            }
                        }
                    }
                }
                onUpdate();
            }
        }

        public String findEmoticon(TLRPC.TL_messages_stickerSet tL_messages_stickerSet, long j) {
            if (tL_messages_stickerSet == null) {
                return null;
            }
            for (int i = 0; i < tL_messages_stickerSet.packs.size(); i++) {
                TLRPC.TL_stickerPack tL_stickerPack = tL_messages_stickerSet.packs.get(i);
                ArrayList<Long> arrayList = tL_stickerPack.documents;
                if (arrayList != null && arrayList.contains(Long.valueOf(j))) {
                    return tL_stickerPack.emoticon;
                }
            }
            return null;
        }

        public int getItemsCount() {
            int i = 0;
            if (this.data == null) {
                return 0;
            }
            int i2 = 0;
            while (true) {
                ArrayList[] arrayListArr = this.data;
                if (i >= arrayListArr.length) {
                    return i2;
                }
                ArrayList arrayList = arrayListArr[i];
                if (arrayList != null) {
                    i2 = i2 + (arrayListArr.length == 1 ? arrayList.size() : Math.min(EmojiPacksAlert.this.gridLayoutManager.getSpanCount() * 2, this.data[i].size())) + 1;
                }
                i++;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void init() {
            ArrayList arrayList;
            TLRPC.TL_inputStickeredMediaDocument tL_inputStickeredMediaDocument;
            TLRPC.StickerSet stickerSet;
            TLObject tLObject = this.parentObject;
            if ((!(tLObject instanceof TLRPC.Photo) && !(tLObject instanceof TLRPC.Document)) || ((arrayList = this.inputStickerSets) != null && !arrayList.isEmpty())) {
                this.stickerSets = new ArrayList(this.inputStickerSets.size());
                this.data = new ArrayList[this.inputStickerSets.size()];
                NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.groupStickersDidLoad);
                final boolean[] zArr = new boolean[1];
                for (int i = 0; i < this.data.length; i++) {
                    TLRPC.TL_messages_stickerSet stickerSet2 = MediaDataController.getInstance(this.currentAccount).getStickerSet((TLRPC.InputStickerSet) this.inputStickerSets.get(i), null, false, new Utilities.Callback() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPacksLoader$$ExternalSyntheticLambda0
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            EmojiPacksAlert.EmojiPacksLoader.this.lambda$init$3(zArr, (TLRPC.TL_messages_stickerSet) obj);
                        }
                    });
                    if (this.data.length == 1 && stickerSet2 != null && (stickerSet = stickerSet2.set) != null && !stickerSet.emojis) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPacksLoader$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                EmojiPacksAlert.EmojiPacksLoader.this.lambda$init$4();
                            }
                        });
                        new StickersAlert(EmojiPacksAlert.this.getContext(), EmojiPacksAlert.this.fragment, (TLRPC.InputStickerSet) this.inputStickerSets.get(i), null, EmojiPacksAlert.this.fragment instanceof ChatActivity ? ((ChatActivity) EmojiPacksAlert.this.fragment).getChatActivityEnterView() : null, ((BottomSheet) EmojiPacksAlert.this).resourcesProvider, false).show();
                        return;
                    } else {
                        this.stickerSets.add(stickerSet2);
                        putStickerSet(i, stickerSet2);
                    }
                }
                onUpdate();
                return;
            }
            this.data = new ArrayList[2];
            putStickerSet(0, null);
            putStickerSet(1, null);
            TLRPC.TL_messages_getAttachedStickers tL_messages_getAttachedStickers = new TLRPC.TL_messages_getAttachedStickers();
            TLObject tLObject2 = this.parentObject;
            if (!(tLObject2 instanceof TLRPC.Photo)) {
                if (tLObject2 instanceof TLRPC.Document) {
                    TLRPC.Document document = (TLRPC.Document) tLObject2;
                    TLRPC.TL_inputStickeredMediaDocument tL_inputStickeredMediaDocument2 = new TLRPC.TL_inputStickeredMediaDocument();
                    TLRPC.TL_inputDocument tL_inputDocument = new TLRPC.TL_inputDocument();
                    tL_inputStickeredMediaDocument2.id = tL_inputDocument;
                    tL_inputDocument.id = document.id;
                    tL_inputDocument.access_hash = document.access_hash;
                    byte[] bArr = document.file_reference;
                    tL_inputDocument.file_reference = bArr;
                    tL_inputStickeredMediaDocument = tL_inputStickeredMediaDocument2;
                    if (bArr == null) {
                        tL_inputDocument.file_reference = new byte[0];
                        tL_inputStickeredMediaDocument = tL_inputStickeredMediaDocument2;
                    }
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getAttachedStickers, new RequestDelegate() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPacksLoader$$ExternalSyntheticLambda2
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject3, TLRPC.TL_error tL_error) {
                        EmojiPacksAlert.EmojiPacksLoader.this.lambda$init$1(tLObject3, tL_error);
                    }
                });
            }
            TLRPC.Photo photo = (TLRPC.Photo) tLObject2;
            TLRPC.TL_inputStickeredMediaPhoto tL_inputStickeredMediaPhoto = new TLRPC.TL_inputStickeredMediaPhoto();
            TLRPC.TL_inputPhoto tL_inputPhoto = new TLRPC.TL_inputPhoto();
            tL_inputStickeredMediaPhoto.id = tL_inputPhoto;
            tL_inputPhoto.id = photo.id;
            tL_inputPhoto.access_hash = photo.access_hash;
            byte[] bArr2 = photo.file_reference;
            tL_inputPhoto.file_reference = bArr2;
            tL_inputStickeredMediaDocument = tL_inputStickeredMediaPhoto;
            if (bArr2 == null) {
                tL_inputPhoto.file_reference = new byte[0];
                tL_inputStickeredMediaDocument = tL_inputStickeredMediaPhoto;
            }
            tL_messages_getAttachedStickers.media = tL_inputStickeredMediaDocument;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getAttachedStickers, new RequestDelegate() { // from class: org.telegram.ui.Components.EmojiPacksAlert$EmojiPacksLoader$$ExternalSyntheticLambda2
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject3, TLRPC.TL_error tL_error) {
                    EmojiPacksAlert.EmojiPacksLoader.this.lambda$init$1(tLObject3, tL_error);
                }
            });
        }

        protected abstract void onUpdate();

        public void recycle() {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.groupStickersDidLoad);
        }

        public void start() {
            if (this.started) {
                return;
            }
            this.started = true;
            init();
        }
    }

    private static class LinkMovementMethodMy extends LinkMovementMethod {
        private LinkMovementMethodMy() {
        }

        /* synthetic */ LinkMovementMethodMy(1 r1) {
            this();
        }

        @Override // android.text.method.LinkMovementMethod, android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            try {
                boolean onTouchEvent = super.onTouchEvent(textView, spannable, motionEvent);
                if (motionEvent.getAction() != 1) {
                    if (motionEvent.getAction() == 3) {
                    }
                    return onTouchEvent;
                }
                Selection.removeSelection(spannable);
                return onTouchEvent;
            } catch (Exception e) {
                FileLog.e(e);
                return false;
            }
        }
    }

    class SeparatorView extends View {
        public SeparatorView(Context context) {
            super(context);
            setBackgroundColor(EmojiPacksAlert.this.getThemedColor(Theme.key_chat_emojiPanelShadowLine));
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(-1, AndroidUtilities.getShadowHeight());
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = AndroidUtilities.dp(14.0f);
            setLayoutParams(layoutParams);
        }
    }

    public EmojiPacksAlert(BaseFragment baseFragment, Context context, Theme.ResourcesProvider resourcesProvider, ArrayList arrayList) {
        this(baseFragment, context, resourcesProvider, arrayList, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private EmojiPacksAlert(final BaseFragment baseFragment, final Context context, Theme.ResourcesProvider resourcesProvider, final ArrayList arrayList, TLObject tLObject) {
        super(context, false, r6);
        final Theme.ResourcesProvider resourceProvider = (baseFragment == null || baseFragment.getResourceProvider() == null) ? resourcesProvider : baseFragment.getResourceProvider();
        this.highlightStartPosition = -1;
        this.highlightEndPosition = -1;
        this.previewDelegate = new 1();
        this.highlightIndex = -1;
        this.shown = false;
        this.loaded = true;
        this.fragment = baseFragment;
        fixNavigationBar();
        if (arrayList != null) {
            this.limitCount = arrayList.size() > 1;
        }
        this.customEmojiPacks = new EmojiPacksLoader(this.currentAccount, arrayList, tLObject) { // from class: org.telegram.ui.Components.EmojiPacksAlert.2
            @Override // org.telegram.ui.Components.EmojiPacksAlert.EmojiPacksLoader
            protected void onUpdate() {
                EmojiPacksAlert.this.updateButton();
                if (EmojiPacksAlert.this.listView == null || EmojiPacksAlert.this.listView.getAdapter() == null) {
                    return;
                }
                EmojiPacksAlert.this.listView.getAdapter().notifyDataSetChanged();
            }
        };
        float dp = AndroidUtilities.dp(32.0f);
        float dp2 = AndroidUtilities.dp(3.5f);
        int i = Theme.key_featuredStickers_addButton;
        this.progressDrawable = new CircularProgressDrawable(dp, dp2, getThemedColor(i));
        new PorterDuffColorFilter(ColorUtils.setAlphaComponent(getThemedColor(Theme.key_windowBackgroundWhiteLinkText), NotificationCenter.filterSettingsUpdated), PorterDuff.Mode.MULTIPLY);
        ContentView contentView = new ContentView(context);
        this.contentView = contentView;
        this.containerView = contentView;
        this.paddingView = new View(context) { // from class: org.telegram.ui.Components.EmojiPacksAlert.3
            @Override // android.view.View
            protected void onMeasure(int i2, int i3) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                int i4 = point.x;
                int i5 = point.y;
                super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec((int) (i5 * (i4 < i5 ? 0.56f : 0.3f)), 1073741824));
            }
        };
        RecyclerListView recyclerListView = new RecyclerListView(context, this.resourcesProvider) { // from class: org.telegram.ui.Components.EmojiPacksAlert.4
            private Paint highlightPaint = new Paint(1);

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                if (EmojiPacksAlert.this.highlightAlpha != null) {
                    EmojiPacksAlert emojiPacksAlert = EmojiPacksAlert.this;
                    if (emojiPacksAlert.highlightStartPosition >= 0 && emojiPacksAlert.highlightEndPosition >= 0 && emojiPacksAlert.adapter != null && isAttachedToWindow()) {
                        float f = EmojiPacksAlert.this.highlightAlpha.set(0.0f);
                        if (f > 0.0f) {
                            int i2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                            int i3 = Integer.MIN_VALUE;
                            for (int i4 = 0; i4 < getChildCount(); i4++) {
                                View childAt = getChildAt(i4);
                                int childAdapterPosition = getChildAdapterPosition(childAt);
                                if (childAdapterPosition != -1) {
                                    EmojiPacksAlert emojiPacksAlert2 = EmojiPacksAlert.this;
                                    if (childAdapterPosition >= emojiPacksAlert2.highlightStartPosition && childAdapterPosition <= emojiPacksAlert2.highlightEndPosition) {
                                        i2 = Math.min(i2, childAt.getTop() + ((int) childAt.getTranslationY()));
                                        i3 = Math.max(i3, childAt.getBottom() + ((int) childAt.getTranslationY()));
                                    }
                                }
                            }
                            if (i2 < i3) {
                                this.highlightPaint.setColor(Theme.multAlpha(getThemedColor(Theme.key_chat_linkSelectBackground), f));
                                canvas.drawRect(0.0f, i2, getMeasuredWidth(), i3, this.highlightPaint);
                            }
                            invalidate();
                        }
                    }
                }
                super.dispatchDraw(canvas);
            }

            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean drawChild(Canvas canvas, View view, long j) {
                return false;
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                AnimatedEmojiSpan.release(((BottomSheet) EmojiPacksAlert.this).containerView, (LongSparseArray<AnimatedEmojiDrawable>) EmojiPacksAlert.this.animatedEmojiDrawables);
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                return super.onInterceptTouchEvent(motionEvent) || ContentPreviewViewer.getInstance().onInterceptTouchEvent(motionEvent, EmojiPacksAlert.this.listView, 0, EmojiPacksAlert.this.previewDelegate, this.resourcesProvider);
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i2, int i3, int i4, int i5) {
                super.onLayout(z, i2, i3, i4, i5);
                EmojiPacksAlert.this.contentView.updateEmojiDrawables();
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            protected void onMeasure(int i2, int i3) {
                View.MeasureSpec.getSize(i2);
                EmojiPacksAlert.this.gridLayoutManager.setSpanCount(40);
                super.onMeasure(i2, i3);
            }

            @Override // androidx.recyclerview.widget.RecyclerView
            public void onScrolled(int i2, int i3) {
                super.onScrolled(i2, i3);
                EmojiPacksAlert.this.contentView.updateEmojiDrawables();
                ((BottomSheet) EmojiPacksAlert.this).containerView.invalidate();
            }
        };
        this.listView = recyclerListView;
        this.highlightAlpha = new AnimatedFloat(0.0f, recyclerListView, 0L, 1250L, CubicBezierInterpolator.EASE_IN);
        ViewGroup viewGroup = this.containerView;
        int i2 = this.backgroundPaddingLeft;
        viewGroup.setPadding(i2, AndroidUtilities.statusBarHeight, i2, 0);
        this.containerView.setClipChildren(false);
        this.containerView.setClipToPadding(false);
        this.containerView.setWillNotDraw(false);
        this.listView.setWillNotDraw(false);
        this.listView.setSelectorRadius(AndroidUtilities.dp(6.0f));
        this.listView.setSelectorDrawableColor(Theme.getColor(Theme.key_listSelector, resourceProvider));
        this.listView.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(this.limitCount ? 8.0f : 68.0f));
        RecyclerListView recyclerListView2 = this.listView;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 8);
        this.gridLayoutManager = gridLayoutManager;
        recyclerListView2.setLayoutManager(gridLayoutManager);
        this.listView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.Components.EmojiPacksAlert.5
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(android.graphics.Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                if (view instanceof SeparatorView) {
                    rect.left = -EmojiPacksAlert.this.listView.getPaddingLeft();
                    rect.right = -EmojiPacksAlert.this.listView.getPaddingRight();
                } else if (EmojiPacksAlert.this.listView.getChildAdapterPosition(view) == 1) {
                    rect.top = AndroidUtilities.dp(14.0f);
                }
            }
        });
        this.listView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert.6
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                if (EmojiPacksAlert.this.contentView == null || !EmojiPacksAlert.this.listView.scrollingByUser) {
                    return;
                }
                EmojiPacksAlert.this.contentView.hidePreviewEmoji();
            }
        });
        RecyclerListView recyclerListView3 = this.listView;
        final RecyclerListView.OnItemClickListener onItemClickListener = new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda2
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i3) {
                EmojiPacksAlert.this.lambda$new$0(arrayList, baseFragment, resourceProvider, view, i3);
            }
        };
        recyclerListView3.setOnItemClickListener(onItemClickListener);
        this.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view, int i3) {
                boolean lambda$new$2;
                lambda$new$2 = EmojiPacksAlert.this.lambda$new$2(context, view, i3);
                return lambda$new$2;
            }
        });
        this.listView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda4
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$new$3;
                lambda$new$3 = EmojiPacksAlert.this.lambda$new$3(onItemClickListener, view, motionEvent);
                return lambda$new$3;
            }
        });
        this.gridLayoutManager.setReverseLayout(false);
        this.gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.EmojiPacksAlert.8
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i3) {
                TLRPC.StickerSet stickerSet;
                if (EmojiPacksAlert.this.listView.getAdapter() == null || EmojiPacksAlert.this.listView.getAdapter().getItemViewType(i3) != 1) {
                    return EmojiPacksAlert.this.gridLayoutManager.getSpanCount();
                }
                int i4 = 0;
                int i5 = 0;
                while (i4 < EmojiPacksAlert.this.customEmojiPacks.data.length) {
                    int size = EmojiPacksAlert.this.customEmojiPacks.data[i4].size();
                    if (EmojiPacksAlert.this.customEmojiPacks.data.length > 1) {
                        size = Math.min(EmojiPacksAlert.this.gridLayoutManager.getSpanCount() * 2, size);
                    }
                    i5 += size + 2;
                    if (i3 < i5) {
                        break;
                    }
                    i4++;
                }
                TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (EmojiPacksAlert.this.customEmojiPacks.stickerSets == null || i4 >= EmojiPacksAlert.this.customEmojiPacks.stickerSets.size()) ? null : (TLRPC.TL_messages_stickerSet) EmojiPacksAlert.this.customEmojiPacks.stickerSets.get(i4);
                return (tL_messages_stickerSet == null || (stickerSet = tL_messages_stickerSet.set) == null || stickerSet.emojis) ? 5 : 8;
            }
        });
        this.scrollHelper = new RecyclerAnimationScrollHelper(this.listView, this.gridLayoutManager);
        this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        View view = new View(context);
        this.shadowView = view;
        view.setBackgroundColor(Theme.getColor(Theme.key_dialogShadowLine));
        this.containerView.addView(this.shadowView, LayoutHelper.createFrame(-1.0f, 1.0f / AndroidUtilities.density, 80));
        this.shadowView.setTranslationY(-AndroidUtilities.dp(68.0f));
        FrameLayout frameLayout = new FrameLayout(context);
        this.buttonsView = frameLayout;
        frameLayout.setBackgroundColor(getThemedColor(Theme.key_dialogBackground));
        this.containerView.addView(this.buttonsView, LayoutHelper.createFrame(-1, 68, 87));
        TextView textView = new TextView(context);
        this.addButtonView = textView;
        textView.setVisibility(8);
        this.addButtonView.setBackground(Theme.AdaptiveRipple.filledRect(getThemedColor(i), 6.0f));
        this.addButtonView.setTextColor(getThemedColor(Theme.key_featuredStickers_buttonText));
        this.addButtonView.setTypeface(AndroidUtilities.bold());
        this.addButtonView.setGravity(17);
        this.buttonsView.addView(this.addButtonView, LayoutHelper.createFrame(-1, 48.0f, 80, 12.0f, 10.0f, 12.0f, 10.0f));
        TextView textView2 = new TextView(context);
        this.removeButtonView = textView2;
        textView2.setVisibility(8);
        TextView textView3 = this.removeButtonView;
        int i3 = Theme.key_text_RedBold;
        textView3.setBackground(Theme.createRadSelectorDrawable(getThemedColor(i3) & 268435455, 0, 0));
        this.removeButtonView.setTextColor(getThemedColor(i3));
        this.removeButtonView.setTypeface(AndroidUtilities.bold());
        this.removeButtonView.setGravity(17);
        this.removeButtonView.setClickable(true);
        this.buttonsView.addView(this.removeButtonView, LayoutHelper.createFrame(-1, -1.0f, 80, 0.0f, 0.0f, 0.0f, 19.0f));
        PremiumButtonView premiumButtonView = new PremiumButtonView(context, false, this.resourcesProvider);
        this.premiumButtonView = premiumButtonView;
        premiumButtonView.setButton(LocaleController.getString(R.string.UnlockPremiumEmoji), new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                EmojiPacksAlert.this.lambda$new$4(view2);
            }
        });
        this.premiumButtonView.setIcon(R.raw.unlock_icon);
        this.premiumButtonView.buttonLayout.setClickable(true);
        this.buttonsView.addView(this.premiumButtonView, LayoutHelper.createFrame(-1, 48.0f, 80, 12.0f, 10.0f, 12.0f, 10.0f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void access$6500(EmojiPacksAlert emojiPacksAlert, int i) {
        emojiPacksAlert.onSubItemClick(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ColorFilter getAdaptiveEmojiColorFilter(int i) {
        if (i != this.adaptiveEmojiColor || this.adaptiveEmojiColorFilter == null) {
            this.adaptiveEmojiColor = i;
            this.adaptiveEmojiColorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN);
        }
        return this.adaptiveEmojiColorFilter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getListTop() {
        if (this.containerView == null) {
            return 0;
        }
        RecyclerListView recyclerListView = this.listView;
        if (recyclerListView == null || recyclerListView.getChildCount() < 1) {
            return this.containerView.getPaddingTop();
        }
        View childAt = this.listView.getChildAt(0);
        View view = this.paddingView;
        return childAt != view ? this.containerView.getPaddingTop() : view.getBottom() + ((int) this.listView.getY());
    }

    public static void installSet(BaseFragment baseFragment, TLObject tLObject, boolean z) {
        installSet(baseFragment, tLObject, z, null, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0032 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0033  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void installSet(final BaseFragment baseFragment, TLObject tLObject, final boolean z, final Utilities.Callback callback, final Runnable runnable) {
        final TLRPC.StickerSet stickerSet;
        TLRPC.StickerSet stickerSet2;
        final int currentAccount = baseFragment == null ? UserConfig.selectedAccount : baseFragment.getCurrentAccount();
        final View fragmentView = baseFragment == null ? null : baseFragment.getFragmentView();
        if (tLObject == null) {
            return;
        }
        final TLRPC.TL_messages_stickerSet tL_messages_stickerSet = tLObject instanceof TLRPC.TL_messages_stickerSet ? (TLRPC.TL_messages_stickerSet) tLObject : null;
        if (tL_messages_stickerSet != null) {
            stickerSet2 = tL_messages_stickerSet.set;
        } else {
            if (!(tLObject instanceof TLRPC.StickerSet)) {
                stickerSet = null;
                if (stickerSet != null) {
                    return;
                }
                if (MediaDataController.getInstance(currentAccount).cancelRemovingStickerSet(stickerSet.id)) {
                    if (callback != null) {
                        callback.run(Boolean.TRUE);
                        return;
                    }
                    return;
                } else {
                    TLRPC.TL_messages_installStickerSet tL_messages_installStickerSet = new TLRPC.TL_messages_installStickerSet();
                    TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
                    tL_messages_installStickerSet.stickerset = tL_inputStickerSetID;
                    tL_inputStickerSetID.id = stickerSet.id;
                    tL_inputStickerSetID.access_hash = stickerSet.access_hash;
                    ConnectionsManager.getInstance(currentAccount).sendRequest(tL_messages_installStickerSet, new RequestDelegate() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda9
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject2, TLRPC.TL_error tL_error) {
                            EmojiPacksAlert.lambda$installSet$7(TLRPC.StickerSet.this, z, fragmentView, baseFragment, tL_messages_stickerSet, currentAccount, callback, runnable, tLObject2, tL_error);
                        }
                    });
                    return;
                }
            }
            stickerSet2 = (TLRPC.StickerSet) tLObject;
        }
        stickerSet = stickerSet2;
        if (stickerSet != null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$installSet$5(Runnable runnable, ArrayList arrayList) {
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x006b, code lost:
    
        if (r21 != null) goto L27;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static /* synthetic */ void lambda$installSet$6(TLRPC.StickerSet stickerSet, TLRPC.TL_error tL_error, boolean z, View view, BaseFragment baseFragment, TLRPC.TL_messages_stickerSet tL_messages_stickerSet, TLObject tLObject, int i, Utilities.Callback callback, final Runnable runnable) {
        Boolean bool;
        int i2 = stickerSet.masks ? 1 : stickerSet.emojis ? 5 : 0;
        try {
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (tL_error == null) {
            if (z && view != null) {
                Bulletin.make(baseFragment, new StickerSetBulletinLayout(baseFragment.getFragmentView().getContext(), tL_messages_stickerSet == 0 ? stickerSet : tL_messages_stickerSet, 2, null, baseFragment.getResourceProvider()), 1500).show();
            }
            if (tLObject instanceof TLRPC.TL_messages_stickerSetInstallResultArchive) {
                MediaDataController.getInstance(i).processStickerSetInstallResultArchive(baseFragment, true, i2, (TLRPC.TL_messages_stickerSetInstallResultArchive) tLObject);
            }
            if (callback != null) {
                bool = Boolean.TRUE;
            }
            MediaDataController.getInstance(i).loadStickers(i2, false, true, false, new Utilities.Callback() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda11
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    EmojiPacksAlert.lambda$installSet$5(runnable, (ArrayList) obj);
                }
            });
        }
        if (view == null) {
            if (callback != null) {
            }
            MediaDataController.getInstance(i).loadStickers(i2, false, true, false, new Utilities.Callback() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda11
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    EmojiPacksAlert.lambda$installSet$5(runnable, (ArrayList) obj);
                }
            });
        }
        Toast.makeText(baseFragment.getFragmentView().getContext(), LocaleController.getString(R.string.ErrorOccurred), 0).show();
        bool = Boolean.FALSE;
        callback.run(bool);
        MediaDataController.getInstance(i).loadStickers(i2, false, true, false, new Utilities.Callback() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda11
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                EmojiPacksAlert.lambda$installSet$5(runnable, (ArrayList) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$installSet$7(final TLRPC.StickerSet stickerSet, final boolean z, final View view, final BaseFragment baseFragment, final TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final int i, final Utilities.Callback callback, final Runnable runnable, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                EmojiPacksAlert.lambda$installSet$6(TLRPC.StickerSet.this, tL_error, z, view, baseFragment, tL_messages_stickerSet, tLObject, i, callback, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadAnimation$8(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.loadT = floatValue;
        this.listView.setAlpha(floatValue);
        this.addButtonView.setAlpha(this.loadT);
        this.removeButtonView.setAlpha(this.loadT);
        this.containerView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ArrayList arrayList, BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider, View view, int i) {
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet = null;
        int i2 = 0;
        if (arrayList == null || arrayList.size() <= 1) {
            ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
            if (actionBarPopupWindow != null) {
                actionBarPopupWindow.dismiss();
                this.popupWindow = null;
                return;
            }
            if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).getChatActivityEnterView().getVisibility() == 0 && (view instanceof EmojiImageView)) {
                AnimatedEmojiSpan animatedEmojiSpan = ((EmojiImageView) view).span;
                try {
                    TLRPC.Document document = animatedEmojiSpan.document;
                    if (document == null) {
                        document = AnimatedEmojiDrawable.findDocument(this.currentAccount, animatedEmojiSpan.getDocumentId());
                    }
                    SpannableString spannableString = new SpannableString(MessageObject.findAnimatedEmojiEmoticon(document));
                    spannableString.setSpan(animatedEmojiSpan, 0, spannableString.length(), 33);
                    ((ChatActivity) baseFragment).getChatActivityEnterView().messageEditText.getText().append((CharSequence) spannableString);
                    onCloseByLink();
                    dismiss();
                } catch (Exception unused) {
                }
                try {
                    view.performHapticFeedback(3, 1);
                    return;
                } catch (Exception unused2) {
                    return;
                }
            }
            return;
        }
        if (SystemClock.elapsedRealtime() - this.premiumButtonClicked < 250) {
            return;
        }
        int i3 = 0;
        while (true) {
            ArrayList[] arrayListArr = this.customEmojiPacks.data;
            if (i2 >= arrayListArr.length) {
                break;
            }
            int size = arrayListArr[i2].size();
            if (this.customEmojiPacks.data.length > 1) {
                size = Math.min(this.gridLayoutManager.getSpanCount() * 2, size);
            }
            i3 += size + 2;
            if (i < i3) {
                break;
            } else {
                i2++;
            }
        }
        ArrayList arrayList2 = this.customEmojiPacks.stickerSets;
        if (arrayList2 != null && i2 < arrayList2.size()) {
            tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.customEmojiPacks.stickerSets.get(i2);
        }
        if (tL_messages_stickerSet == null || tL_messages_stickerSet.set == null) {
            return;
        }
        ArrayList arrayList3 = new ArrayList();
        TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
        TLRPC.StickerSet stickerSet = tL_messages_stickerSet.set;
        tL_inputStickerSetID.id = stickerSet.id;
        tL_inputStickerSetID.access_hash = stickerSet.access_hash;
        arrayList3.add(tL_inputStickerSetID);
        new EmojiPacksAlert(baseFragment, getContext(), resourcesProvider, arrayList3) { // from class: org.telegram.ui.Components.EmojiPacksAlert.7
            @Override // org.telegram.ui.Components.EmojiPacksAlert
            protected void onCloseByLink() {
                EmojiPacksAlert.this.dismiss();
            }
        }.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(AnimatedEmojiSpan animatedEmojiSpan, View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow == null) {
            return;
        }
        actionBarPopupWindow.dismiss();
        this.popupWindow = null;
        SpannableString spannableString = new SpannableString(MessageObject.findAnimatedEmojiEmoticon(AnimatedEmojiDrawable.findDocument(this.currentAccount, animatedEmojiSpan.getDocumentId())));
        spannableString.setSpan(animatedEmojiSpan, 0, spannableString.length(), 33);
        if (AndroidUtilities.addToClipboard(spannableString)) {
            BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createCopyBulletin(LocaleController.getString(R.string.EmojiCopied)).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$2(Context context, View view, int i) {
        final AnimatedEmojiSpan animatedEmojiSpan;
        if (!(view instanceof EmojiImageView) || (animatedEmojiSpan = ((EmojiImageView) view).span) == null) {
            return false;
        }
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), true, true);
        actionBarMenuSubItem.setItemHeight(48);
        actionBarMenuSubItem.setPadding(AndroidUtilities.dp(26.0f), 0, AndroidUtilities.dp(26.0f), 0);
        actionBarMenuSubItem.setText(LocaleController.getString(R.string.Copy));
        actionBarMenuSubItem.getTextView().setTextSize(1, 14.4f);
        actionBarMenuSubItem.getTextView().setTypeface(AndroidUtilities.bold());
        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                EmojiPacksAlert.this.lambda$new$1(animatedEmojiSpan, view2);
            }
        });
        LinearLayout linearLayout = new LinearLayout(context);
        Drawable mutate = ContextCompat.getDrawable(getContext(), R.drawable.popup_fixed_alert).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_actionBarDefaultSubmenuBackground), PorterDuff.Mode.MULTIPLY));
        linearLayout.setBackground(mutate);
        linearLayout.addView(actionBarMenuSubItem);
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(linearLayout, -2, -2);
        this.popupWindow = actionBarPopupWindow;
        actionBarPopupWindow.setClippingEnabled(true);
        this.popupWindow.setLayoutInScreen(true);
        this.popupWindow.setInputMethodMode(2);
        this.popupWindow.setSoftInputMode(0);
        this.popupWindow.setOutsideTouchable(true);
        this.popupWindow.setAnimationStyle(R.style.PopupAnimation);
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        this.popupWindow.showAtLocation(view, 51, (iArr[0] - AndroidUtilities.dp(49.0f)) + (view.getMeasuredWidth() / 2), iArr[1] - AndroidUtilities.dp(52.0f));
        try {
            view.performHapticFeedback(0, 1);
        } catch (Exception unused) {
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$3(RecyclerListView.OnItemClickListener onItemClickListener, View view, MotionEvent motionEvent) {
        return ContentPreviewViewer.getInstance().onTouch(motionEvent, this.listView, 0, onItemClickListener, this.previewDelegate, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(View view) {
        showPremiumAlert();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateButton$10(final ArrayList arrayList, View view) {
        final int size = arrayList.size();
        final int[] iArr = new int[2];
        for (int i = 0; i < arrayList.size(); i++) {
            installSet(this.fragment, (TLObject) arrayList.get(i), size == 1, size > 1 ? new Utilities.Callback() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    EmojiPacksAlert.this.lambda$updateButton$9(iArr, size, arrayList, (Boolean) obj);
                }
            } : null, null);
        }
        onButtonClicked(true);
        if (size <= 1) {
            dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateButton$11(ArrayList arrayList, View view) {
        dismiss();
        BaseFragment baseFragment = this.fragment;
        if (baseFragment != null) {
            MediaDataController.getInstance(baseFragment.getCurrentAccount()).removeMultipleStickerSets(this.fragment.getContext(), this.fragment, arrayList);
        } else {
            int i = 0;
            while (i < arrayList.size()) {
                uninstallSet(getContext(), (TLRPC.TL_messages_stickerSet) arrayList.get(i), i == 0, null);
                i++;
            }
        }
        onButtonClicked(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateButton$9(int[] iArr, int i, ArrayList arrayList, Boolean bool) {
        iArr[0] = iArr[0] + 1;
        if (bool.booleanValue()) {
            iArr[1] = iArr[1] + 1;
        }
        if (iArr[0] != i || iArr[1] <= 0) {
            return;
        }
        dismiss();
        Bulletin.make(this.fragment, new StickerSetBulletinLayout(this.fragment.getFragmentView().getContext(), (TLObject) arrayList.get(0), iArr[1], 2, null, this.fragment.getResourceProvider()), 1500).show();
    }

    private void loadAnimation() {
        if (this.loadAnimator != null) {
            return;
        }
        this.loadAnimator = ValueAnimator.ofFloat(this.loadT, 1.0f);
        this.fromY = Float.valueOf(this.lastY + this.containerView.getY());
        this.loadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                EmojiPacksAlert.this.lambda$loadAnimation$8(valueAnimator);
            }
        });
        this.loadAnimator.setDuration(250L);
        this.loadAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.loadAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSubItemClick(int i) {
        ArrayList arrayList;
        StringBuilder sb;
        String str;
        EmojiPacksLoader emojiPacksLoader = this.customEmojiPacks;
        if (emojiPacksLoader == null || (arrayList = emojiPacksLoader.stickerSets) == null || arrayList.isEmpty()) {
            return;
        }
        TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) this.customEmojiPacks.stickerSets.get(0);
        TLRPC.StickerSet stickerSet = tL_messages_stickerSet.set;
        if (stickerSet == null || !stickerSet.emojis) {
            sb = new StringBuilder();
            sb.append("https://");
            sb.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
            str = "/addstickers/";
        } else {
            sb = new StringBuilder();
            sb.append("https://");
            sb.append(MessagesController.getInstance(this.currentAccount).linkPrefix);
            str = "/addemoji/";
        }
        sb.append(str);
        sb.append(tL_messages_stickerSet.set.short_name);
        String sb2 = sb.toString();
        if (i != 1) {
            if (i == 2) {
                try {
                    AndroidUtilities.addToClipboard(sb2);
                    BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createCopyLinkBulletin().show();
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            }
            return;
        }
        BaseFragment baseFragment = this.fragment;
        Context parentActivity = baseFragment != null ? baseFragment.getParentActivity() : null;
        if (parentActivity == null) {
            parentActivity = getContext();
        }
        9 r11 = new 9(parentActivity, null, sb2, false, sb2, false, this.resourcesProvider);
        BaseFragment baseFragment2 = this.fragment;
        if (baseFragment2 != null) {
            baseFragment2.showDialog(r11);
        } else {
            r11.show();
        }
    }

    public static void uninstallSet(Context context, TLRPC.TL_messages_stickerSet tL_messages_stickerSet, boolean z, Runnable runnable) {
        if (tL_messages_stickerSet == null) {
            return;
        }
        MediaDataController.getInstance(UserConfig.selectedAccount).toggleStickerSet(context, tL_messages_stickerSet, 0, null, true, z, runnable, true);
    }

    public static void uninstallSet(BaseFragment baseFragment, TLRPC.TL_messages_stickerSet tL_messages_stickerSet, boolean z, Runnable runnable, boolean z2) {
        if (baseFragment == null || tL_messages_stickerSet == null || baseFragment.getFragmentView() == null) {
            return;
        }
        MediaDataController.getInstance(baseFragment.getCurrentAccount()).toggleStickerSet(baseFragment.getFragmentView().getContext(), tL_messages_stickerSet, 0, baseFragment, true, z, runnable, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateButton() {
        TextView textView;
        String formatPluralString;
        TextView textView2;
        View.OnClickListener onClickListener;
        TextView textView3;
        String formatPluralString2;
        TLRPC.StickerSet stickerSet;
        if (this.buttonsView == null) {
            return;
        }
        ArrayList arrayList = this.customEmojiPacks.stickerSets == null ? new ArrayList() : new ArrayList(this.customEmojiPacks.stickerSets);
        int i = 0;
        while (i < arrayList.size()) {
            if (arrayList.get(i) == null) {
                arrayList.remove(i);
                i--;
            }
            i++;
        }
        MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
        final ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            TLRPC.TL_messages_stickerSet tL_messages_stickerSet = (TLRPC.TL_messages_stickerSet) arrayList.get(i2);
            if (tL_messages_stickerSet != null && (stickerSet = tL_messages_stickerSet.set) != null) {
                if (mediaDataController.isStickerPackInstalled(stickerSet.id)) {
                    arrayList2.add(tL_messages_stickerSet);
                } else {
                    arrayList3.add(tL_messages_stickerSet);
                }
            }
        }
        final ArrayList arrayList4 = new ArrayList(arrayList3);
        boolean z = this.customEmojiPacks.inputStickerSets != null && arrayList.size() == this.customEmojiPacks.inputStickerSets.size();
        if (!this.loaded && z) {
            loadAnimation();
        }
        this.loaded = z;
        if (!z) {
            this.listView.setAlpha(0.0f);
        } else if (this.highlightIndex >= 0) {
            int findFirstVisibleItemPosition = this.gridLayoutManager.findFirstVisibleItemPosition();
            int setHeaderPosition = this.adapter.getSetHeaderPosition(this.highlightIndex);
            if (Math.abs(findFirstVisibleItemPosition - setHeaderPosition) > 54) {
                this.scrollHelper.setScrollDirection(findFirstVisibleItemPosition < setHeaderPosition ? 0 : 1);
                this.scrollHelper.scrollToPosition(setHeaderPosition, (AndroidUtilities.displaySize.y / 2) - AndroidUtilities.dp(170.0f), false, true);
            } else {
                this.listView.smoothScrollToPosition(setHeaderPosition);
            }
            this.highlightStartPosition = this.adapter.getSetHeaderPosition(this.highlightIndex);
            this.highlightEndPosition = this.adapter.getSetEndPosition(this.highlightIndex);
            this.highlightAlpha.set(1.0f, true);
            this.listView.invalidate();
            this.highlightIndex = -1;
        }
        if (this.loaded && !this.limitCount) {
            this.premiumButtonView.setVisibility(4);
            if (arrayList4.size() > 0) {
                this.addButtonView.setVisibility(0);
                this.removeButtonView.setVisibility(8);
                if (arrayList4.size() == 1) {
                    textView3 = this.addButtonView;
                    formatPluralString2 = LocaleController.formatPluralString("AddManyEmojiCount", ((TLRPC.TL_messages_stickerSet) arrayList4.get(0)).documents.size(), new Object[0]);
                } else {
                    textView3 = this.addButtonView;
                    formatPluralString2 = LocaleController.formatPluralString("AddManyEmojiPacksCount", arrayList4.size(), new Object[0]);
                }
                textView3.setText(formatPluralString2);
                textView2 = this.addButtonView;
                onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        EmojiPacksAlert.this.lambda$updateButton$10(arrayList4, view);
                    }
                };
            } else if (arrayList2.size() > 0) {
                this.addButtonView.setVisibility(8);
                this.removeButtonView.setVisibility(0);
                if (arrayList2.size() == 1) {
                    textView = this.removeButtonView;
                    formatPluralString = LocaleController.formatPluralString("RemoveManyEmojiCount", ((TLRPC.TL_messages_stickerSet) arrayList2.get(0)).documents.size(), new Object[0]);
                } else {
                    textView = this.removeButtonView;
                    formatPluralString = LocaleController.formatPluralString("RemoveManyEmojiPacksCount", arrayList2.size(), new Object[0]);
                }
                textView.setText(formatPluralString);
                textView2 = this.removeButtonView;
                onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiPacksAlert$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        EmojiPacksAlert.this.lambda$updateButton$11(arrayList2, view);
                    }
                };
            }
            textView2.setOnClickListener(onClickListener);
            updateShowButton(true);
            return;
        }
        this.premiumButtonView.setVisibility(8);
        this.addButtonView.setVisibility(8);
        this.removeButtonView.setVisibility(8);
        updateShowButton(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLightStatusBar(boolean z) {
        boolean z2 = AndroidUtilities.computePerceivedBrightness(getThemedColor(Theme.key_dialogBackground)) > 0.721f;
        boolean z3 = AndroidUtilities.computePerceivedBrightness(Theme.blendOver(getThemedColor(Theme.key_actionBarDefault), AndroidUtilities.DARK_STATUS_BAR_OVERLAY)) > 0.721f;
        if (!z) {
            z2 = z3;
        }
        AndroidUtilities.setLightStatusBar(getWindow(), z2);
    }

    private void updateShowButton(boolean z) {
        boolean z2 = !this.shown && z;
        float dp = this.removeButtonView.getVisibility() == 0 ? AndroidUtilities.dp(19.0f) : 0;
        float f = 0.0f;
        if (z2) {
            ViewPropertyAnimator duration = this.buttonsView.animate().translationY(z ? dp : AndroidUtilities.dp(16.0f)).alpha(z ? 1.0f : 0.0f).setDuration(250L);
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            duration.setInterpolator(cubicBezierInterpolator).start();
            this.shadowView.animate().translationY(z ? -(AndroidUtilities.dp(68.0f) - dp) : 0.0f).alpha(z ? 1.0f : 0.0f).setDuration(250L).setInterpolator(cubicBezierInterpolator).start();
            ViewPropertyAnimator animate = this.listView.animate();
            if (!this.limitCount && !z) {
                f = AndroidUtilities.dp(68.0f) - dp;
            }
            animate.translationY(f).setDuration(250L).setInterpolator(cubicBezierInterpolator).start();
        } else {
            this.buttonsView.setAlpha(z ? 1.0f : 0.0f);
            this.buttonsView.setTranslationY(z ? dp : AndroidUtilities.dp(16.0f));
            this.shadowView.setAlpha(z ? 1.0f : 0.0f);
            this.shadowView.setTranslationY(z ? -(AndroidUtilities.dp(68.0f) - dp) : 0.0f);
            RecyclerListView recyclerListView = this.listView;
            if (!this.limitCount && !z) {
                f = AndroidUtilities.dp(68.0f) - dp;
            }
            recyclerListView.setTranslationY(f);
        }
        this.shown = z;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.stickersDidLoad) {
            updateInstallment();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss() {
        ContentView contentView = this.contentView;
        if (contentView != null) {
            contentView.hidePreviewEmoji();
        }
        super.dismiss();
        EmojiPacksLoader emojiPacksLoader = this.customEmojiPacks;
        if (emojiPacksLoader != null) {
            emojiPacksLoader.recycle();
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 4);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public int getContainerViewHeight() {
        RecyclerListView recyclerListView = this.listView;
        int measuredHeight = (recyclerListView == null ? 0 : recyclerListView.getMeasuredHeight()) - getListTop();
        ViewGroup viewGroup = this.containerView;
        return measuredHeight + (viewGroup != null ? viewGroup.getPaddingTop() : 0) + AndroidUtilities.navigationBarHeight + AndroidUtilities.dp(8.0f);
    }

    public void highlight(int i) {
        this.highlightIndex = i;
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onBackPressed() {
        if (ContentPreviewViewer.getInstance().isVisible()) {
            ContentPreviewViewer.getInstance().closeWithMenu();
        } else {
            super.onBackPressed();
        }
    }

    protected void onButtonClicked(boolean z) {
    }

    protected void onCloseByLink() {
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
    }

    public void setPreviewEmoji(TLRPC.Document document) {
        this.contentView.setPreviewEmoji(document);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        super.show();
        RecyclerListView recyclerListView = this.listView;
        Adapter adapter = new Adapter(this, null);
        this.adapter = adapter;
        recyclerListView.setAdapter(adapter);
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 4);
        this.customEmojiPacks.start();
        updateButton();
        BaseFragment baseFragment = this.fragment;
        MediaDataController.getInstance(baseFragment == null ? UserConfig.selectedAccount : baseFragment.getCurrentAccount()).checkStickers(5);
    }

    public void showPremiumAlert() {
        BaseFragment baseFragment = this.fragment;
        if (baseFragment != null) {
            new PremiumFeatureBottomSheet(baseFragment, 11, false).show();
        } else if (getContext() instanceof LaunchActivity) {
            ((LaunchActivity) getContext()).lambda$runLinkRequest$93(new PremiumPreviewFragment(null));
        }
    }

    public void updateInstallment() {
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt instanceof EmojiPackHeader) {
                EmojiPackHeader emojiPackHeader = (EmojiPackHeader) childAt;
                if (emojiPackHeader.set != null && emojiPackHeader.set.set != null) {
                    emojiPackHeader.toggle(MediaDataController.getInstance(this.currentAccount).isStickerPackInstalled(emojiPackHeader.set.set.id), true);
                }
            }
        }
        updateButton();
    }
}
