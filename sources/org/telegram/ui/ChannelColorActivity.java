package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChannelBoostsController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ResultCallback;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.EmojiThemes;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeColors;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ThemePreviewMessagesCell;
import org.telegram.ui.Cells.ThemesHorizontalListCell;
import org.telegram.ui.ChannelColorActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.ChatThemeBottomSheet;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.FlickerLoadingView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.LimitReachedBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Components.ThemeSmallPreviewView;
import org.telegram.ui.PeerColorActivity;
import org.telegram.ui.SelectAnimatedEmojiDialog;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.PreviewView;
import org.telegram.ui.ThemePreviewActivity;

/* loaded from: classes4.dex */
public class ChannelColorActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    protected Adapter adapter;
    public Drawable backgroundDrawable;
    public TL_stories.TL_premium_boostsStatus boostsStatus;
    private BaseFragment bulletinFragment;
    protected ButtonWithCounterView button;
    protected FrameLayout buttonContainer;
    private View changeDayNightView;
    private ValueAnimator changeDayNightViewAnimator;
    private float changeDayNightViewProgress;
    private final SparseIntArray currentColors;
    public int currentLevel;
    public int currentProfileColor;
    public long currentProfileEmoji;
    public int currentReplyColor;
    public long currentReplyEmoji;
    public TLRPC.EmojiStatus currentStatusEmoji;
    public TLRPC.WallPaper currentWallpaper;
    private ActionBarMenuItem dayNightItem;
    public final long dialogId;
    private final Paint dividerPaint;
    private boolean forceDark;
    public TLRPC.WallPaper galleryWallpaper;
    private boolean isDark;
    protected boolean isGroup;
    protected RecyclerListView listView;
    private SpannableStringBuilder lock;
    protected int messagesPreviewRow;
    private final Theme.MessageDrawable msgInDrawable;
    private final Theme.MessageDrawable msgInDrawableSelected;
    private final Drawable msgOutCheckReadDrawable;
    private final Theme.MessageDrawable msgOutDrawable;
    private final Theme.MessageDrawable msgOutDrawableSelected;
    private final Drawable msgOutHalfCheckDrawable;
    protected int packEmojiHintRow;
    protected int packEmojiRow;
    protected int packStickerHintRow;
    protected int packStickerRow;
    private Theme.ResourcesProvider parentResourcesProvider;
    protected int profileColorGridRow;
    protected int profileEmojiRow;
    protected int profileHintRow;
    protected int profilePreviewRow;
    protected int removeProfileColorRow;
    protected int removeProfileColorShadowRow;
    protected int replyColorListRow;
    protected int replyEmojiRow;
    protected int replyHintRow;
    protected int rowsCount;
    private SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialog;
    public int selectedProfileColor;
    public long selectedProfileEmoji;
    public int selectedReplyColor;
    public long selectedReplyEmoji;
    public TLRPC.EmojiStatus selectedStatusEmoji;
    public TLRPC.WallPaper selectedWallpaper;
    protected int statusEmojiRow;
    protected int statusHintRow;
    private RLottieDrawable sunDrawable;
    protected int wallpaperHintRow;
    protected int wallpaperRow;
    protected int wallpaperThemesRow;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class Adapter extends RecyclerListView.SelectionAdapter {
        protected Adapter() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$0(String str) {
            ChannelColorActivity channelColorActivity;
            TLRPC.WallPaper wallPaper;
            if (str == null) {
                channelColorActivity = ChannelColorActivity.this;
                wallPaper = channelColorActivity.galleryWallpaper;
            } else {
                if (!str.equals("❌")) {
                    ChannelColorActivity.this.selectedWallpaper = new TLRPC.TL_wallPaperNoFile();
                    TLRPC.WallPaper wallPaper2 = ChannelColorActivity.this.selectedWallpaper;
                    wallPaper2.id = 0L;
                    wallPaper2.flags |= 4;
                    wallPaper2.settings = new TLRPC.TL_wallPaperSettings();
                    ChannelColorActivity.this.selectedWallpaper.settings.emoticon = str;
                    ChannelColorActivity.this.updateButton(true);
                    ChannelColorActivity.this.updateMessagesPreview(true);
                }
                channelColorActivity = ChannelColorActivity.this;
                wallPaper = null;
            }
            channelColorActivity.selectedWallpaper = wallPaper;
            ChannelColorActivity.this.updateButton(true);
            ChannelColorActivity.this.updateMessagesPreview(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$1(PeerColorPicker peerColorPicker, View view, int i) {
            ChannelColorActivity.this.selectedReplyColor = peerColorPicker.toColorId(i);
            ChannelColorActivity.this.updateButton(true);
            ChannelColorActivity.this.updateMessagesPreview(true);
            ChannelColorActivity.this.updateProfilePreview(true);
            if (view.getLeft() < peerColorPicker.listView.getPaddingLeft() + AndroidUtilities.dp(24.0f)) {
                RecyclerListView recyclerListView = peerColorPicker.listView;
                recyclerListView.smoothScrollBy(-((recyclerListView.getPaddingLeft() + AndroidUtilities.dp(48.0f)) - view.getLeft()), 0);
            } else if (view.getLeft() + view.getWidth() > (peerColorPicker.listView.getMeasuredWidth() - peerColorPicker.listView.getPaddingRight()) - AndroidUtilities.dp(24.0f)) {
                peerColorPicker.listView.smoothScrollBy((view.getLeft() + view.getWidth()) - ((peerColorPicker.listView.getMeasuredWidth() - peerColorPicker.listView.getPaddingRight()) - AndroidUtilities.dp(48.0f)), 0);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreateViewHolder$2(Integer num) {
            ChannelColorActivity.this.selectedProfileColor = num.intValue();
            ChannelColorActivity.this.updateButton(true);
            ChannelColorActivity.this.updateProfilePreview(true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return ChannelColorActivity.this.rowsCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            ChannelColorActivity channelColorActivity = ChannelColorActivity.this;
            if (i == channelColorActivity.messagesPreviewRow) {
                return 0;
            }
            if (i == channelColorActivity.wallpaperThemesRow) {
                return 2;
            }
            if (i == channelColorActivity.profilePreviewRow) {
                return 1;
            }
            if (i == channelColorActivity.replyColorListRow) {
                return 3;
            }
            if (i == channelColorActivity.profileColorGridRow) {
                return 4;
            }
            if (i == channelColorActivity.replyEmojiRow || i == channelColorActivity.profileEmojiRow || i == channelColorActivity.statusEmojiRow || i == channelColorActivity.packEmojiRow || i == channelColorActivity.packStickerRow) {
                return 6;
            }
            return (i == channelColorActivity.wallpaperRow || i == channelColorActivity.removeProfileColorRow) ? 5 : 7;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 5 || itemViewType == 6;
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0077  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x007a  */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            TLRPC.StickerSet stickerSet;
            long j;
            TLRPC.StickerSet stickerSet2;
            int stickerPackInfoStrRes;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 1) {
                ProfilePreview profilePreview = (ProfilePreview) viewHolder.itemView;
                profilePreview.backgroundView.setColor(((BaseFragment) ChannelColorActivity.this).currentAccount, ChannelColorActivity.this.selectedProfileColor, false);
                profilePreview.profileView.setColor(ChannelColorActivity.this.selectedProfileColor, false);
                profilePreview.profileView.setEmoji(ChannelColorActivity.this.selectedProfileEmoji, false);
                profilePreview.profileView.setForum(ChannelColorActivity.this.isForum());
                profilePreview.profileView.setStatusEmoji(DialogObject.getEmojiStatusDocumentId(ChannelColorActivity.this.selectedStatusEmoji), false);
                profilePreview.profileView.overrideAvatarColor(ChannelColorActivity.this.selectedReplyColor);
                return;
            }
            if (itemViewType == 3) {
                ((PeerColorPicker) viewHolder.itemView).setSelected(ChannelColorActivity.this.selectedReplyColor, false);
                return;
            }
            if (itemViewType == 4) {
                ((PeerColorActivity.PeerColorGrid) viewHolder.itemView).setSelected(ChannelColorActivity.this.selectedProfileColor, false);
                return;
            }
            if (itemViewType == 5) {
                TextCell textCell = (TextCell) viewHolder.itemView;
                ChannelColorActivity channelColorActivity = ChannelColorActivity.this;
                if (i == channelColorActivity.removeProfileColorRow) {
                    textCell.setText(LocaleController.getString(R.string.ChannelProfileColorReset), false);
                    return;
                }
                textCell.setText(LocaleController.getString(channelColorActivity.getWallpaperStrRes()), false);
                ChannelColorActivity channelColorActivity2 = ChannelColorActivity.this;
                if (channelColorActivity2.currentLevel < channelColorActivity2.getCustomWallpaperLevelMin()) {
                    textCell.setLockLevel(false, ChannelColorActivity.this.getCustomWallpaperLevelMin());
                    return;
                } else {
                    textCell.setLockLevel(false, 0);
                    return;
                }
            }
            if (itemViewType != 6) {
                if (itemViewType != 7) {
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                textInfoPrivacyCell.setFixedSize(0);
                ChannelColorActivity channelColorActivity3 = ChannelColorActivity.this;
                if (i == channelColorActivity3.replyHintRow) {
                    stickerPackInfoStrRes = R.string.ChannelReplyInfo;
                } else if (i == channelColorActivity3.wallpaperHintRow) {
                    stickerPackInfoStrRes = channelColorActivity3.getWallpaper2InfoStrRes();
                } else if (i == channelColorActivity3.profileHintRow) {
                    stickerPackInfoStrRes = channelColorActivity3.getProfileInfoStrRes();
                } else if (i == channelColorActivity3.statusHintRow) {
                    stickerPackInfoStrRes = channelColorActivity3.getEmojiStatusInfoStrRes();
                } else if (i == channelColorActivity3.packEmojiHintRow) {
                    stickerPackInfoStrRes = channelColorActivity3.getEmojiPackInfoStrRes();
                } else {
                    if (i != channelColorActivity3.packStickerHintRow) {
                        if (i == channelColorActivity3.removeProfileColorShadowRow) {
                            textInfoPrivacyCell.setText("");
                            textInfoPrivacyCell.setFixedSize(12);
                        }
                        Context context = ChannelColorActivity.this.getContext();
                        ChannelColorActivity channelColorActivity4 = ChannelColorActivity.this;
                        textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(context, i != channelColorActivity4.statusHintRow ? R.drawable.greydivider_bottom : R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow, ((BaseFragment) channelColorActivity4).resourceProvider));
                        return;
                    }
                    stickerPackInfoStrRes = channelColorActivity3.getStickerPackInfoStrRes();
                }
                textInfoPrivacyCell.setText(LocaleController.getString(stickerPackInfoStrRes));
                Context context2 = ChannelColorActivity.this.getContext();
                ChannelColorActivity channelColorActivity42 = ChannelColorActivity.this;
                textInfoPrivacyCell.setBackground(Theme.getThemedDrawableByKey(context2, i != channelColorActivity42.statusHintRow ? R.drawable.greydivider_bottom : R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow, ((BaseFragment) channelColorActivity42).resourceProvider));
                return;
            }
            EmojiCell emojiCell = (EmojiCell) viewHolder.itemView;
            emojiCell.setDivider(false);
            ChannelColorActivity channelColorActivity5 = ChannelColorActivity.this;
            if (i == channelColorActivity5.replyEmojiRow) {
                emojiCell.setAdaptiveEmojiColor(((BaseFragment) channelColorActivity5).currentAccount, ChannelColorActivity.this.selectedReplyColor, true);
                emojiCell.setText(LocaleController.getString(R.string.ChannelReplyLogo));
                ChannelColorActivity channelColorActivity6 = ChannelColorActivity.this;
                if (channelColorActivity6.currentLevel < channelColorActivity6.getMessagesController().channelBgIconLevelMin) {
                    emojiCell.setLockLevel(ChannelColorActivity.this.getMessagesController().channelBgIconLevelMin);
                } else {
                    emojiCell.setLockLevel(0);
                }
                j = ChannelColorActivity.this.selectedReplyEmoji;
            } else if (i == channelColorActivity5.profileEmojiRow) {
                emojiCell.setAdaptiveEmojiColor(((BaseFragment) channelColorActivity5).currentAccount, ChannelColorActivity.this.selectedProfileColor, false);
                emojiCell.setText(LocaleController.getString(R.string.ChannelProfileLogo));
                emojiCell.setDivider(ChannelColorActivity.this.removeProfileColorRow >= 0);
                ChannelColorActivity channelColorActivity7 = ChannelColorActivity.this;
                if (channelColorActivity7.currentLevel < channelColorActivity7.getProfileIconLevelMin()) {
                    emojiCell.setLockLevel(ChannelColorActivity.this.getProfileIconLevelMin());
                } else {
                    emojiCell.setLockLevel(0);
                }
                j = ChannelColorActivity.this.selectedProfileEmoji;
            } else if (i == channelColorActivity5.statusEmojiRow) {
                emojiCell.setAdaptiveEmojiColor(((BaseFragment) channelColorActivity5).currentAccount, ChannelColorActivity.this.selectedProfileColor, false);
                emojiCell.setText(LocaleController.getString(ChannelColorActivity.this.getEmojiStatusStrRes()));
                ChannelColorActivity channelColorActivity8 = ChannelColorActivity.this;
                if (channelColorActivity8.currentLevel < channelColorActivity8.getEmojiStatusLevelMin()) {
                    emojiCell.setLockLevel(ChannelColorActivity.this.getEmojiStatusLevelMin());
                } else {
                    emojiCell.setLockLevel(0);
                }
                j = DialogObject.getEmojiStatusDocumentId(ChannelColorActivity.this.selectedStatusEmoji);
            } else {
                if (i == channelColorActivity5.packEmojiRow) {
                    emojiCell.setAdaptiveEmojiColor(((BaseFragment) channelColorActivity5).currentAccount, ChannelColorActivity.this.selectedProfileColor, false);
                    emojiCell.setText(LocaleController.getString(ChannelColorActivity.this.getEmojiPackStrRes()));
                    ChannelColorActivity channelColorActivity9 = ChannelColorActivity.this;
                    if (channelColorActivity9.currentLevel < channelColorActivity9.getEmojiStickersLevelMin()) {
                        emojiCell.setLockLevel(ChannelColorActivity.this.getEmojiStickersLevelMin());
                    } else {
                        emojiCell.setLockLevel(0);
                    }
                    TLRPC.ChatFull chatFull = ChannelColorActivity.this.getMessagesController().getChatFull(-ChannelColorActivity.this.dialogId);
                    if (chatFull != null && (stickerSet2 = chatFull.emojiset) != null) {
                        j = ChannelColorActivity.this.getEmojiSetThumbId(stickerSet2);
                    }
                } else {
                    if (i != channelColorActivity5.packStickerRow) {
                        return;
                    }
                    emojiCell.setText(LocaleController.getString(channelColorActivity5.getStickerPackStrRes()));
                    emojiCell.setLockLevel(0);
                    TLRPC.ChatFull chatFull2 = ChannelColorActivity.this.getMessagesController().getChatFull(-ChannelColorActivity.this.dialogId);
                    if (chatFull2 != null && (stickerSet = chatFull2.stickerset) != null) {
                        emojiCell.setEmoji(ChannelColorActivity.this.getEmojiSetThumb(stickerSet), false);
                        return;
                    }
                }
                j = 0;
            }
            emojiCell.setEmoji(j, false);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            FrameLayout frameLayout;
            FrameLayout frameLayout2;
            if (i == 0) {
                Context context = ChannelColorActivity.this.getContext();
                INavigationLayout iNavigationLayout = ((BaseFragment) ChannelColorActivity.this).parentLayout;
                int messagePreviewType = ChannelColorActivity.this.getMessagePreviewType();
                ChannelColorActivity channelColorActivity = ChannelColorActivity.this;
                ThemePreviewMessagesCell themePreviewMessagesCell = new ThemePreviewMessagesCell(context, iNavigationLayout, messagePreviewType, channelColorActivity.dialogId, ((BaseFragment) channelColorActivity).resourceProvider);
                themePreviewMessagesCell.customAnimation = true;
                themePreviewMessagesCell.setImportantForAccessibility(4);
                ChannelColorActivity channelColorActivity2 = ChannelColorActivity.this;
                themePreviewMessagesCell.fragment = channelColorActivity2;
                Drawable drawable = channelColorActivity2.backgroundDrawable;
                int i2 = ((BaseFragment) channelColorActivity2).currentAccount;
                ChannelColorActivity channelColorActivity3 = ChannelColorActivity.this;
                Drawable backgroundDrawable = PreviewView.getBackgroundDrawable(drawable, i2, channelColorActivity3.selectedWallpaper, channelColorActivity3.isDark);
                channelColorActivity2.backgroundDrawable = backgroundDrawable;
                themePreviewMessagesCell.setOverrideBackground(backgroundDrawable);
                frameLayout2 = themePreviewMessagesCell;
            } else {
                if (i == 2) {
                    ThemeChooser themeChooser = new ThemeChooser(ChannelColorActivity.this.getContext(), false, ((BaseFragment) ChannelColorActivity.this).currentAccount, ((BaseFragment) ChannelColorActivity.this).resourceProvider);
                    themeChooser.setWithRemovedStub(true);
                    themeChooser.setSelectedEmoticon(ChannelColorActivity.this.getThemeChooserEmoticon(), false);
                    themeChooser.setGalleryWallpaper(ChannelColorActivity.this.galleryWallpaper);
                    themeChooser.setOnEmoticonSelected(new Utilities.Callback() { // from class: org.telegram.ui.ChannelColorActivity$Adapter$$ExternalSyntheticLambda0
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            ChannelColorActivity.Adapter.this.lambda$onCreateViewHolder$0((String) obj);
                        }
                    });
                    frameLayout = themeChooser;
                } else if (i == 5) {
                    frameLayout = new TextCell(ChannelColorActivity.this.getContext(), ChannelColorActivity.this.getResourceProvider());
                } else if (i == 6) {
                    frameLayout = new EmojiCell(ChannelColorActivity.this.getContext(), ((BaseFragment) ChannelColorActivity.this).resourceProvider);
                } else if (i == 3) {
                    final PeerColorPicker peerColorPicker = new PeerColorPicker(ChannelColorActivity.this.getContext(), ((BaseFragment) ChannelColorActivity.this).currentAccount, ((BaseFragment) ChannelColorActivity.this).resourceProvider);
                    peerColorPicker.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ChannelColorActivity$Adapter$$ExternalSyntheticLambda1
                        @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                        public final void onItemClick(View view, int i3) {
                            ChannelColorActivity.Adapter.this.lambda$onCreateViewHolder$1(peerColorPicker, view, i3);
                        }
                    });
                    frameLayout = peerColorPicker;
                } else if (i == 4) {
                    PeerColorActivity.PeerColorGrid peerColorGrid = new PeerColorActivity.PeerColorGrid(ChannelColorActivity.this.getContext(), 1, ((BaseFragment) ChannelColorActivity.this).currentAccount, ((BaseFragment) ChannelColorActivity.this).resourceProvider);
                    peerColorGrid.setDivider(false);
                    peerColorGrid.setOnColorClick(new Utilities.Callback() { // from class: org.telegram.ui.ChannelColorActivity$Adapter$$ExternalSyntheticLambda2
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            ChannelColorActivity.Adapter.this.lambda$onCreateViewHolder$2((Integer) obj);
                        }
                    });
                    frameLayout = peerColorGrid;
                } else if (i == 1) {
                    ChannelColorActivity channelColorActivity4 = ChannelColorActivity.this;
                    frameLayout2 = new ProfilePreview(channelColorActivity4.getContext());
                } else {
                    frameLayout2 = new TextInfoPrivacyCell(ChannelColorActivity.this.getContext());
                }
                frameLayout.setBackgroundColor(ChannelColorActivity.this.getThemedColor(Theme.key_windowBackgroundWhite));
                frameLayout2 = frameLayout;
            }
            return new RecyclerListView.Holder(frameLayout2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            if (view instanceof ProfilePreview) {
                ProfilePreview profilePreview = (ProfilePreview) view;
                profilePreview.profileView.setColor(ChannelColorActivity.this.selectedProfileColor, false);
                profilePreview.profileView.setEmoji(ChannelColorActivity.this.selectedProfileEmoji, false);
                profilePreview.profileView.setForum(ChannelColorActivity.this.isForum());
                profilePreview.profileView.setStatusEmoji(DialogObject.getEmojiStatusDocumentId(ChannelColorActivity.this.selectedStatusEmoji), false);
                profilePreview.profileView.overrideAvatarColor(ChannelColorActivity.this.selectedReplyColor);
            } else if (view instanceof ThemePreviewMessagesCell) {
                ((ThemePreviewMessagesCell) view).setOverrideBackground(ChannelColorActivity.this.backgroundDrawable);
            } else {
                ChannelColorActivity.this.updateColors(view);
            }
            super.onViewAttachedToWindow(viewHolder);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class EmojiCell extends FrameLayout {
        private int color;
        private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable imageDrawable;
        private boolean needDivider;
        private Text offText;
        private Theme.ResourcesProvider resourcesProvider;
        private SimpleTextView textView;

        public EmojiCell(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.needDivider = false;
            this.resourcesProvider = resourcesProvider;
            setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider));
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.textView = simpleTextView;
            simpleTextView.setTextSize(16);
            this.textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            addView(this.textView, LayoutHelper.createFrame(-1, -2.0f, 23, 23.0f, 0.0f, 48.0f, 0.0f));
            this.imageDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, false, AndroidUtilities.dp(24.0f), 13);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            updateImageBounds();
            this.imageDrawable.setColor(Integer.valueOf(this.color));
            Text text = this.offText;
            if (text != null) {
                text.draw(canvas, (getMeasuredWidth() - this.offText.getWidth()) - AndroidUtilities.dp(19.0f), getMeasuredHeight() / 2.0f, Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4, this.resourcesProvider), 1.0f);
            } else {
                this.imageDrawable.draw(canvas);
            }
            if (this.needDivider) {
                Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
                Paint paint = resourcesProvider != null ? resourcesProvider.getPaint("paintDivider") : Theme.dividerPaint;
                if (paint != null) {
                    canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(23.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(23.0f) : 0), getMeasuredHeight() - 1, paint);
                }
            }
        }

        public int getColor() {
            return this.color;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.imageDrawable.attach();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.imageDrawable.detach();
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(50.0f), 1073741824));
        }

        public void setAdaptiveEmojiColor(int i, int i2, boolean z) {
            int i3;
            int color;
            if (i2 < 0) {
                int i4 = Theme.key_actionBarDefault;
                if (AndroidUtilities.computePerceivedBrightness(Theme.getColor(i4, this.resourcesProvider)) > 0.8f) {
                    i3 = Theme.key_windowBackgroundWhiteBlueText;
                    color = Theme.getColor(i3, this.resourcesProvider);
                } else {
                    color = AndroidUtilities.computePerceivedBrightness(Theme.getColor(i4, this.resourcesProvider)) < 0.2f ? Theme.multAlpha(Theme.getColor(Theme.key_actionBarDefaultTitle, this.resourcesProvider), 0.5f) : Theme.blendOver(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider), Theme.multAlpha(PeerColorActivity.adaptProfileEmojiColor(Theme.getColor(i4, this.resourcesProvider)), 0.7f));
                }
            } else {
                if (i2 < 7) {
                    i3 = Theme.keys_avatar_nameInMessage[i2];
                } else {
                    MessagesController messagesController = MessagesController.getInstance(i);
                    MessagesController.PeerColors peerColors = z ? messagesController.peerColors : messagesController.profilePeerColors;
                    MessagesController.PeerColor color2 = peerColors == null ? null : peerColors.getColor(i2);
                    if (color2 != null) {
                        color = color2.getColor(0, this.resourcesProvider);
                    } else {
                        i3 = Theme.keys_avatar_nameInMessage[0];
                    }
                }
                color = Theme.getColor(i3, this.resourcesProvider);
            }
            this.color = color;
            invalidate();
        }

        public void setDivider(boolean z) {
            this.needDivider = z;
            setWillNotDraw(!z);
        }

        public void setEmoji(long j, boolean z) {
            if (j != 0) {
                this.imageDrawable.set(j, z);
                this.offText = null;
            } else {
                this.imageDrawable.set((Drawable) null, z);
                if (this.offText == null) {
                    this.offText = new Text(LocaleController.getString(R.string.ChannelReplyIconOff), 16.0f);
                }
            }
        }

        public void setEmoji(TLRPC.Document document, boolean z) {
            if (document != null) {
                this.imageDrawable.set(document, z);
                this.offText = null;
            } else {
                this.imageDrawable.set((Drawable) null, z);
                if (this.offText == null) {
                    this.offText = new Text(LocaleController.getString(R.string.ChannelReplyIconOff), 16.0f);
                }
            }
        }

        public void setLockLevel(int i) {
            if (i <= 0) {
                this.textView.setRightDrawable((Drawable) null);
            } else {
                this.textView.setRightDrawable(new PeerColorActivity.LevelLock(getContext(), i, this.resourcesProvider));
                this.textView.setDrawablePadding(AndroidUtilities.dp(6.0f));
            }
        }

        public void setText(CharSequence charSequence) {
            this.textView.setText(charSequence);
        }

        public void updateColors() {
            this.textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, this.resourcesProvider));
        }

        public void updateImageBounds() {
            this.imageDrawable.setBounds((getWidth() - this.imageDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(21.0f), (getHeight() - this.imageDrawable.getIntrinsicHeight()) / 2, getWidth() - AndroidUtilities.dp(21.0f), (getHeight() + this.imageDrawable.getIntrinsicHeight()) / 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class PeerColorPicker extends FrameLayout {
        public final RecyclerListView.SelectionAdapter adapter;
        private final int currentAccount;
        public final LinearLayoutManager layoutManager;
        public final RecyclerListView listView;
        private final Theme.ResourcesProvider resourcesProvider;
        private int selectedPosition;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class ColorCell extends View {
            private final Paint backgroundPaint;
            private final ButtonBounce bounce;
            private final Path circlePath;
            private final Path color2Path;
            private boolean hasColor2;
            private boolean hasColor3;
            private final Paint paint1;
            private final Paint paint2;
            private final Paint paint3;
            private boolean selected;
            private final AnimatedFloat selectedT;

            public ColorCell(Context context) {
                super(context);
                Paint paint = new Paint(1);
                this.backgroundPaint = paint;
                this.paint1 = new Paint(1);
                this.paint2 = new Paint(1);
                this.paint3 = new Paint(1);
                this.circlePath = new Path();
                this.color2Path = new Path();
                this.bounce = new ButtonBounce(this);
                this.selectedT = new AnimatedFloat(this, 0L, 320L, CubicBezierInterpolator.EASE_OUT_QUINT);
                paint.setStyle(Paint.Style.STROKE);
            }

            @Override // android.view.View
            protected void dispatchDraw(Canvas canvas) {
                canvas.save();
                float scale = this.bounce.getScale(0.05f);
                canvas.scale(scale, scale, getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f);
                canvas.save();
                canvas.clipPath(this.circlePath);
                canvas.drawPaint(this.paint1);
                if (this.hasColor2) {
                    canvas.drawPath(this.color2Path, this.paint2);
                }
                canvas.restore();
                if (this.hasColor3) {
                    canvas.save();
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set((getMeasuredWidth() - AndroidUtilities.dp(12.4f)) / 2.0f, (getMeasuredHeight() - AndroidUtilities.dp(12.4f)) / 2.0f, (getMeasuredWidth() + AndroidUtilities.dp(12.4f)) / 2.0f, (getMeasuredHeight() + AndroidUtilities.dp(12.4f)) / 2.0f);
                    canvas.rotate(45.0f, getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f);
                    canvas.drawRoundRect(rectF, AndroidUtilities.dp(2.33f), AndroidUtilities.dp(2.33f), this.paint3);
                    canvas.restore();
                }
                float f = this.selectedT.set(this.selected);
                if (f > 0.0f) {
                    this.backgroundPaint.setStrokeWidth(AndroidUtilities.dpf2(2.0f));
                    canvas.drawCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, AndroidUtilities.lerp(AndroidUtilities.dp(20.0f) + (this.backgroundPaint.getStrokeWidth() * 0.5f), AndroidUtilities.dp(20.0f) - (this.backgroundPaint.getStrokeWidth() * 2.0f), f), this.backgroundPaint);
                }
                canvas.restore();
            }

            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                setMeasuredDimension(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                this.circlePath.rewind();
                this.circlePath.addCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, AndroidUtilities.dp(20.0f), Path.Direction.CW);
                this.color2Path.rewind();
                this.color2Path.moveTo(getMeasuredWidth(), 0.0f);
                this.color2Path.lineTo(getMeasuredWidth(), getMeasuredHeight());
                this.color2Path.lineTo(0.0f, getMeasuredHeight());
                this.color2Path.close();
            }

            public void set(MessagesController.PeerColor peerColor) {
                Paint paint;
                int color;
                if ((PeerColorPicker.this.resourcesProvider != null ? PeerColorPicker.this.resourcesProvider.isDark() : Theme.isCurrentThemeDark()) && peerColor.hasColor2() && !peerColor.hasColor3()) {
                    this.paint1.setColor(peerColor.getColor(1, PeerColorPicker.this.resourcesProvider));
                    paint = this.paint2;
                    color = peerColor.getColor(0, PeerColorPicker.this.resourcesProvider);
                } else {
                    this.paint1.setColor(peerColor.getColor(0, PeerColorPicker.this.resourcesProvider));
                    paint = this.paint2;
                    color = peerColor.getColor(1, PeerColorPicker.this.resourcesProvider);
                }
                paint.setColor(color);
                this.paint3.setColor(peerColor.getColor(2, PeerColorPicker.this.resourcesProvider));
                this.hasColor2 = peerColor.hasColor2();
                this.hasColor3 = peerColor.hasColor3();
            }

            @Override // android.view.View
            public void setBackgroundColor(int i) {
                this.backgroundPaint.setColor(i);
            }

            @Override // android.view.View
            public void setPressed(boolean z) {
                super.setPressed(z);
                this.bounce.setPressed(z);
            }

            public void setSelected(boolean z, boolean z2) {
                this.selected = z;
                if (!z2) {
                    this.selectedT.set(z, true);
                }
                invalidate();
            }
        }

        public PeerColorPicker(final Context context, final int i, final Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.currentAccount = i;
            this.resourcesProvider = resourcesProvider;
            RecyclerListView recyclerListView = new RecyclerListView(context, resourcesProvider) { // from class: org.telegram.ui.ChannelColorActivity.PeerColorPicker.1
                @Override // org.telegram.ui.Components.RecyclerListView
                public Integer getSelectorColor(int i2) {
                    return 0;
                }
            };
            this.listView = recyclerListView;
            recyclerListView.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(6.0f), 0);
            recyclerListView.setClipToPadding(false);
            RecyclerListView.SelectionAdapter selectionAdapter = new RecyclerListView.SelectionAdapter() { // from class: org.telegram.ui.ChannelColorActivity.PeerColorPicker.2
                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public int getItemCount() {
                    MessagesController.PeerColors peerColors = MessagesController.getInstance(i).peerColors;
                    if (peerColors == null) {
                        return 0;
                    }
                    return peerColors.colors.size();
                }

                @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
                public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                    return true;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i2) {
                    ColorCell colorCell = (ColorCell) viewHolder.itemView;
                    colorCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider));
                    colorCell.setSelected(i2 == PeerColorPicker.this.selectedPosition, false);
                    MessagesController.PeerColors peerColors = MessagesController.getInstance(i).peerColors;
                    if (peerColors == null || i2 < 0 || i2 >= peerColors.colors.size()) {
                        return;
                    }
                    colorCell.set(peerColors.colors.get(i2));
                }

                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i2) {
                    return new RecyclerListView.Holder(new ColorCell(context));
                }
            };
            this.adapter = selectionAdapter;
            recyclerListView.setAdapter(selectionAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            this.layoutManager = linearLayoutManager;
            linearLayoutManager.setOrientation(0);
            recyclerListView.setLayoutManager(linearLayoutManager);
            addView(recyclerListView, LayoutHelper.createFrame(-1, -1.0f));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setSelectedPosition$0(boolean z, View view) {
            ((ColorCell) view).setSelected(this.listView.getChildAdapterPosition(view) == this.selectedPosition, z);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateColors$1(MessagesController.PeerColors peerColors, View view) {
            if (view instanceof ColorCell) {
                ColorCell colorCell = (ColorCell) view;
                colorCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                int childAdapterPosition = this.listView.getChildAdapterPosition(view);
                if (peerColors == null || childAdapterPosition < 0 || childAdapterPosition >= peerColors.colors.size()) {
                    return;
                }
                colorCell.set(peerColors.colors.get(childAdapterPosition));
            }
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (getParent() != null) {
                ViewParent parent = getParent();
                boolean z = true;
                if (!canScrollHorizontally(-1) && !canScrollHorizontally(1)) {
                    z = false;
                }
                parent.requestDisallowInterceptTouchEvent(z);
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        public void setSelected(int i, boolean z) {
            setSelectedPosition(toPosition(i), z);
        }

        public void setSelectedPosition(int i, final boolean z) {
            if (i != this.selectedPosition) {
                this.selectedPosition = i;
                if (!z) {
                    this.layoutManager.scrollToPositionWithOffset(i, (AndroidUtilities.displaySize.x - AndroidUtilities.dp(56.0f)) / 2);
                }
                AndroidUtilities.forEachViews((RecyclerView) this.listView, new Consumer() { // from class: org.telegram.ui.ChannelColorActivity$PeerColorPicker$$ExternalSyntheticLambda1
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        ChannelColorActivity.PeerColorPicker.this.lambda$setSelectedPosition$0(z, (View) obj);
                    }
                });
            }
        }

        public int toColorId(int i) {
            MessagesController.PeerColors peerColors = MessagesController.getInstance(this.currentAccount).peerColors;
            if (peerColors == null || i < 0 || i >= peerColors.colors.size()) {
                return 0;
            }
            return peerColors.colors.get(i).id;
        }

        public int toPosition(int i) {
            MessagesController.PeerColors peerColors = MessagesController.getInstance(this.currentAccount).peerColors;
            if (peerColors == null) {
                return 0;
            }
            for (int i2 = 0; i2 < peerColors.colors.size(); i2++) {
                if (peerColors.colors.get(i2).id == i) {
                    return i2;
                }
            }
            return 0;
        }

        public void updateColors() {
            final MessagesController.PeerColors peerColors = MessagesController.getInstance(this.currentAccount).peerColors;
            AndroidUtilities.forEachViews((RecyclerView) this.listView, new Consumer() { // from class: org.telegram.ui.ChannelColorActivity$PeerColorPicker$$ExternalSyntheticLambda0
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    ChannelColorActivity.PeerColorPicker.this.lambda$updateColors$1(peerColors, (View) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes4.dex */
    public class ProfilePreview extends FrameLayout {
        public final PeerColorActivity.ColoredActionBar backgroundView;
        public LinearLayout infoLayout;
        public final PeerColorActivity.ProfilePreview profileView;
        public TextView textInfo1;
        public TextView textInfo2;
        public SimpleTextView title;

        public ProfilePreview(Context context) {
            super(context);
            PeerColorActivity.ColoredActionBar coloredActionBar = new PeerColorActivity.ColoredActionBar(getContext(), ((BaseFragment) ChannelColorActivity.this).resourceProvider);
            this.backgroundView = coloredActionBar;
            coloredActionBar.setProgressToGradient(1.0f);
            coloredActionBar.ignoreMeasure = true;
            addView(coloredActionBar, LayoutHelper.createFrame(-1, ChannelColorActivity.this.isGroup ? NotificationCenter.savedMessagesDialogsUpdate : NotificationCenter.fileUploaded, 119));
            PeerColorActivity.ProfilePreview profilePreview = new PeerColorActivity.ProfilePreview(getContext(), ((BaseFragment) ChannelColorActivity.this).currentAccount, ChannelColorActivity.this.dialogId, ((BaseFragment) ChannelColorActivity.this).resourceProvider) { // from class: org.telegram.ui.ChannelColorActivity.ProfilePreview.1
                @Override // org.telegram.ui.PeerColorActivity.ProfilePreview
                public void setColor(int i, boolean z) {
                    super.setColor(i, z);
                    ProfilePreview profilePreview2 = ProfilePreview.this;
                    TextView textView = profilePreview2.textInfo1;
                    if (textView != null) {
                        textView.setTextColor(profilePreview2.profileView.subtitleView.getTextColor());
                    }
                }
            };
            this.profileView = profilePreview;
            addView(profilePreview, LayoutHelper.createFrame(-1, 104.0f, 80, 0.0f, 0.0f, 0.0f, ChannelColorActivity.this.isGroup ? 24.0f : 0.0f));
            if (ChannelColorActivity.this.needBoostInfoSection()) {
                SimpleTextView simpleTextView = new SimpleTextView(getContext());
                this.title = simpleTextView;
                simpleTextView.setGravity(19);
                this.title.setTextColor(ChannelColorActivity.this.getThemedColor(Theme.key_actionBarDefaultTitle));
                this.title.setTypeface(AndroidUtilities.bold());
                this.title.setText(LocaleController.getString(R.string.ChangeChannelNameColor2));
                this.title.setAlpha(0.0f);
                setTitleSize();
                addView(this.title, LayoutHelper.createFrame(-1, -2.0f, 80, 72.0f, 0.0f, 0.0f, 16.0f));
                LinearLayout linearLayout = new LinearLayout(context);
                this.infoLayout = linearLayout;
                linearLayout.setOrientation(0);
                this.infoLayout.setBackground(Theme.createSelectorWithBackgroundDrawable(Theme.multAlpha(-16777216, 0.065f), -16777216));
                this.infoLayout.setGravity(17);
                this.infoLayout.setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f));
                TextView textView = new TextView(context);
                this.textInfo1 = textView;
                textView.setTextSize(1, 12.0f);
                this.textInfo1.setTextColor(profilePreview.subtitleView.getTextColor());
                TextView textView2 = new TextView(context);
                this.textInfo2 = textView2;
                textView2.setTextSize(1, 12.0f);
                this.textInfo2.setTextColor(-1);
                TextView textView3 = this.textInfo1;
                TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = ChannelColorActivity.this.boostsStatus;
                textView3.setText(AndroidUtilities.replaceTags(LocaleController.formatPluralString("BoostingGroupBoostCount", tL_premium_boostsStatus != null ? tL_premium_boostsStatus.boosts : 0, new Object[0])));
                this.textInfo2.setText(LocaleController.getString(R.string.BoostingGroupBoostWhatAreBoosts));
                this.infoLayout.addView(this.textInfo1);
                this.infoLayout.addView(this.textInfo2, LayoutHelper.createLinear(-2, -2, 3.0f, 0.0f, 0.0f, 0.0f));
                addView(this.infoLayout, LayoutHelper.createFrame(-1, -2, 80));
            }
        }

        public void setColor(int i, boolean z) {
            this.profileView.setColor(i, z);
            this.backgroundView.setColor(((BaseFragment) ChannelColorActivity.this).currentAccount, i, z);
        }

        public void setEmoji(long j, boolean z) {
            this.profileView.setEmoji(j, z);
        }

        public void setEmojiStatus(TLRPC.EmojiStatus emojiStatus, boolean z) {
            this.profileView.setStatusEmoji(DialogObject.getEmojiStatusDocumentId(emojiStatus), z);
        }

        public void setTitleSize() {
            float f;
            boolean z = getResources().getConfiguration().orientation == 2;
            this.title.setTextSize((AndroidUtilities.isTablet() || !z) ? 20 : 18);
            SimpleTextView simpleTextView = this.title;
            if (AndroidUtilities.isTablet()) {
                f = -2.0f;
            } else {
                f = z ? 4 : 0;
            }
            simpleTextView.setTranslationY(AndroidUtilities.dp(f));
        }
    }

    /* loaded from: classes4.dex */
    public static class ThemeChooser extends FrameLayout {
        private final RecyclerListView.SelectionAdapter adapter;
        private final int currentAccount;
        private String currentEmoticon;
        private boolean dataLoaded;
        private TLRPC.WallPaper fallbackWallpaper;
        public final List items;
        private final RecyclerListView listView;
        private final HashMap loadingThemes;
        private final HashMap loadingWallpapers;
        private Utilities.Callback onEmoticonSelected;
        private FlickerLoadingView progressView;
        private final Theme.ResourcesProvider resourcesProvider;
        private boolean withRemovedStub;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes4.dex */
        public class 4 implements ResultCallback {
            final /* synthetic */ int val$currentAccount;

            4(int i) {
                this.val$currentAccount = i;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(List list) {
                ThemeChooser.this.onDataLoaded(list);
            }

            @Override // org.telegram.tgnet.ResultCallback
            public void onComplete(final List list) {
                NotificationCenter.getInstance(this.val$currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.ChannelColorActivity$ThemeChooser$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChannelColorActivity.ThemeChooser.4.this.lambda$onComplete$0(list);
                    }
                });
            }

            @Override // org.telegram.tgnet.ResultCallback
            public /* synthetic */ void onError(Throwable th) {
                ResultCallback.-CC.$default$onError(this, th);
            }

            @Override // org.telegram.tgnet.ResultCallback
            public void onError(TLRPC.TL_error tL_error) {
                Toast.makeText(ThemeChooser.this.getContext(), tL_error.text, 0).show();
            }
        }

        public ThemeChooser(Context context, final boolean z, final int i, final Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.items = new ArrayList();
            this.loadingThemes = new HashMap();
            this.loadingWallpapers = new HashMap();
            this.currentAccount = i;
            this.resourcesProvider = resourcesProvider;
            if (!z) {
                FlickerLoadingView flickerLoadingView = new FlickerLoadingView(getContext(), resourcesProvider);
                this.progressView = flickerLoadingView;
                flickerLoadingView.setViewType(14);
                this.progressView.setVisibility(0);
                addView(this.progressView, LayoutHelper.createFrame(-1, 104.0f, 8388611, 16.0f, 13.0f, 16.0f, 6.0f));
            }
            RecyclerListView recyclerListView = new RecyclerListView(context, resourcesProvider) { // from class: org.telegram.ui.ChannelColorActivity.ThemeChooser.1
                @Override // org.telegram.ui.Components.RecyclerListView
                public Integer getSelectorColor(int i2) {
                    return 0;
                }
            };
            this.listView = recyclerListView;
            recyclerListView.setClipToPadding(false);
            recyclerListView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(13.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(z ? 13.0f : 6.0f));
            if (z) {
                recyclerListView.setHasFixedSize(false);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.ChannelColorActivity.ThemeChooser.2
                    @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                    public int getSpanSize(int i2) {
                        return 1;
                    }
                });
                recyclerListView.setLayoutManager(gridLayoutManager);
            } else {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(0);
                recyclerListView.setLayoutManager(linearLayoutManager);
                recyclerListView.setAlpha(0.0f);
            }
            RecyclerListView.SelectionAdapter selectionAdapter = new RecyclerListView.SelectionAdapter() { // from class: org.telegram.ui.ChannelColorActivity.ThemeChooser.3
                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public int getItemCount() {
                    return ThemeChooser.this.items.size();
                }

                @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
                public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                    return true;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i2) {
                    ThemeSmallPreviewView themeSmallPreviewView = (ThemeSmallPreviewView) viewHolder.itemView;
                    Theme.ThemeInfo themeInfo = ((ChatThemeBottomSheet.ChatThemeItem) ThemeChooser.this.items.get(i2)).chatTheme.getThemeInfo(((ChatThemeBottomSheet.ChatThemeItem) ThemeChooser.this.items.get(i2)).themeIndex);
                    if (themeInfo != null && themeInfo.pathToFile != null && !themeInfo.previewParsed && new File(themeInfo.pathToFile).exists()) {
                        ThemeChooser.this.parseTheme(themeInfo);
                    }
                    ChatThemeBottomSheet.ChatThemeItem chatThemeItem = (ChatThemeBottomSheet.ChatThemeItem) ThemeChooser.this.items.get(i2);
                    themeSmallPreviewView.setEnabled(true);
                    themeSmallPreviewView.setBackgroundColor(Theme.getColor(Theme.key_dialogBackgroundGray));
                    themeSmallPreviewView.setItem(chatThemeItem, false);
                    themeSmallPreviewView.setSelected(chatThemeItem.isSelected, false);
                    themeSmallPreviewView.setFallbackWallpaper(chatThemeItem.chatTheme.showAsRemovedStub ? null : ThemeChooser.this.fallbackWallpaper);
                }

                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i2) {
                    return new RecyclerListView.Holder(new ThemeSmallPreviewView(viewGroup.getContext(), i, resourcesProvider, z ? 4 : 3) { // from class: org.telegram.ui.ChannelColorActivity.ThemeChooser.3.1
                        @Override // org.telegram.ui.Components.ThemeSmallPreviewView
                        protected String noThemeString() {
                            return LocaleController.getString(R.string.ChannelNoWallpaper);
                        }

                        @Override // org.telegram.ui.Components.ThemeSmallPreviewView
                        protected int noThemeStringTextSize() {
                            if (z) {
                                return super.noThemeStringTextSize();
                            }
                            return 13;
                        }
                    });
                }

                @Override // androidx.recyclerview.widget.RecyclerView.Adapter
                public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    if (adapterPosition < 0 || adapterPosition >= ThemeChooser.this.items.size()) {
                        return;
                    }
                    ChatThemeBottomSheet.ChatThemeItem chatThemeItem = (ChatThemeBottomSheet.ChatThemeItem) ThemeChooser.this.items.get(adapterPosition);
                    ((ThemeSmallPreviewView) viewHolder.itemView).setSelected(chatThemeItem.isSelected, false);
                    ((ThemeSmallPreviewView) viewHolder.itemView).setFallbackWallpaper(chatThemeItem.chatTheme.showAsRemovedStub ? null : ThemeChooser.this.fallbackWallpaper);
                }
            };
            this.adapter = selectionAdapter;
            recyclerListView.setAdapter(selectionAdapter);
            addView(recyclerListView, LayoutHelper.createFrame(-1, z ? -1.0f : 130.0f));
            recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ChannelColorActivity$ThemeChooser$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                public final void onItemClick(View view, int i2) {
                    ChannelColorActivity.ThemeChooser.this.lambda$new$1(z, view, i2);
                }
            });
            ChatThemeController chatThemeController = ChatThemeController.getInstance(i);
            chatThemeController.preloadAllWallpaperThumbs(true);
            chatThemeController.preloadAllWallpaperThumbs(false);
            chatThemeController.preloadAllWallpaperImages(true);
            chatThemeController.preloadAllWallpaperImages(false);
            chatThemeController.requestAllChatThemes(new 4(i), true);
            updateState(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(boolean z, View view, int i) {
            if (i < 0 || i >= this.items.size()) {
                return;
            }
            ChatThemeBottomSheet.ChatThemeItem chatThemeItem = (ChatThemeBottomSheet.ChatThemeItem) this.items.get(i);
            if (!z) {
                setSelectedEmoticon(chatThemeItem.getEmoticon(), true);
                if (view.getLeft() < this.listView.getPaddingLeft() + AndroidUtilities.dp(24.0f)) {
                    RecyclerListView recyclerListView = this.listView;
                    recyclerListView.smoothScrollBy(-((recyclerListView.getPaddingLeft() + AndroidUtilities.dp(48.0f)) - view.getLeft()), 0);
                } else if (view.getLeft() + view.getWidth() > (this.listView.getMeasuredWidth() - this.listView.getPaddingRight()) - AndroidUtilities.dp(24.0f)) {
                    this.listView.smoothScrollBy((view.getLeft() + view.getWidth()) - ((this.listView.getMeasuredWidth() - this.listView.getPaddingRight()) - AndroidUtilities.dp(48.0f)), 0);
                }
            }
            Utilities.Callback callback = this.onEmoticonSelected;
            if (callback != null) {
                callback.run(chatThemeItem.getEmoticon());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$parseTheme$3(TLObject tLObject, Theme.ThemeInfo themeInfo) {
            if (!(tLObject instanceof TLRPC.TL_wallPaper)) {
                themeInfo.badWallpaper = true;
                return;
            }
            TLRPC.WallPaper wallPaper = (TLRPC.WallPaper) tLObject;
            String attachFileName = FileLoader.getAttachFileName(wallPaper.document);
            if (this.loadingThemes.containsKey(attachFileName)) {
                return;
            }
            this.loadingThemes.put(attachFileName, themeInfo);
            FileLoader.getInstance(themeInfo.account).loadFile(wallPaper.document, wallPaper, 1, 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$parseTheme$4(final Theme.ThemeInfo themeInfo, final TLObject tLObject, TLRPC.TL_error tL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChannelColorActivity$ThemeChooser$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ChannelColorActivity.ThemeChooser.this.lambda$parseTheme$3(tLObject, themeInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setGalleryWallpaper$0(View view) {
            if (view instanceof ThemeSmallPreviewView) {
                ThemeSmallPreviewView themeSmallPreviewView = (ThemeSmallPreviewView) view;
                themeSmallPreviewView.setFallbackWallpaper(themeSmallPreviewView.chatThemeItem.chatTheme.showAsRemovedStub ? null : this.fallbackWallpaper);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$updateColors$2(View view) {
            ((ThemeSmallPreviewView) view).setBackgroundColor(Theme.getColor(Theme.key_dialogBackgroundGray, this.resourcesProvider));
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public void onDataLoaded(List list) {
            if (list == null || list.isEmpty()) {
                return;
            }
            this.dataLoaded = true;
            this.items.clear();
            this.items.add(0, new ChatThemeBottomSheet.ChatThemeItem((EmojiThemes) list.get(0)));
            if (this.fallbackWallpaper != null && this.withRemovedStub) {
                this.items.add(0, new ChatThemeBottomSheet.ChatThemeItem(EmojiThemes.createChatThemesRemoved(this.currentAccount)));
            }
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            int isDark = resourcesProvider != null ? resourcesProvider.isDark() : Theme.isCurrentThemeDark();
            for (int i = 1; i < list.size(); i++) {
                EmojiThemes emojiThemes = (EmojiThemes) list.get(i);
                ChatThemeBottomSheet.ChatThemeItem chatThemeItem = new ChatThemeBottomSheet.ChatThemeItem(emojiThemes);
                emojiThemes.loadPreviewColors(this.currentAccount);
                chatThemeItem.themeIndex = isDark;
                this.items.add(chatThemeItem);
            }
            int i2 = -1;
            for (int i3 = 0; i3 < this.items.size(); i3++) {
                ChatThemeBottomSheet.ChatThemeItem chatThemeItem2 = (ChatThemeBottomSheet.ChatThemeItem) this.items.get(i3);
                boolean z = TextUtils.equals(this.currentEmoticon, chatThemeItem2.getEmoticon()) || (TextUtils.isEmpty(this.currentEmoticon) && chatThemeItem2.chatTheme.showAsDefaultStub);
                chatThemeItem2.isSelected = z;
                if (z) {
                    i2 = i3;
                }
            }
            RecyclerListView.SelectionAdapter selectionAdapter = this.adapter;
            if (selectionAdapter != null) {
                selectionAdapter.notifyDataSetChanged();
            }
            this.listView.animate().alpha(1.0f).setDuration(150L).start();
            updateState(true);
            if (i2 < 0 || !(this.listView.getLayoutManager() instanceof LinearLayoutManager)) {
                return;
            }
            ((LinearLayoutManager) this.listView.getLayoutManager()).scrollToPositionWithOffset(i2, (AndroidUtilities.displaySize.x - AndroidUtilities.dp(83.0f)) / 2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:96:0x01c7 A[Catch: all -> 0x00a2, TryCatch #0 {all -> 0x00a2, blocks: (B:10:0x001c, B:14:0x002a, B:16:0x0032, B:18:0x0045, B:20:0x0082, B:22:0x008e, B:25:0x0092, B:27:0x0095, B:31:0x009f, B:29:0x00a6, B:33:0x00a9, B:40:0x00b5, B:42:0x00c1, B:44:0x00db, B:46:0x00e5, B:47:0x00f2, B:49:0x00fa, B:51:0x0104, B:52:0x0112, B:54:0x011a, B:56:0x0124, B:36:0x01ef, B:38:0x01f3, B:58:0x0132, B:60:0x013e, B:61:0x0148, B:63:0x0154, B:64:0x015e, B:66:0x0162, B:71:0x0169, B:116:0x0171, B:73:0x0177, B:75:0x0180, B:77:0x018d, B:79:0x0191, B:81:0x0195, B:83:0x0199, B:85:0x019d, B:87:0x01a1, B:89:0x01ad, B:93:0x01b6, B:94:0x01c3, B:96:0x01c7, B:97:0x01cb, B:99:0x01cf, B:100:0x01d3, B:102:0x01d7, B:103:0x01db, B:105:0x01df, B:106:0x01e2, B:108:0x01e6, B:109:0x01e9, B:111:0x01ed, B:114:0x01bb, B:119:0x01fe), top: B:9:0x001c, outer: #5 }] */
        /* JADX WARN: Removed duplicated region for block: B:97:0x01cb A[Catch: all -> 0x00a2, TryCatch #0 {all -> 0x00a2, blocks: (B:10:0x001c, B:14:0x002a, B:16:0x0032, B:18:0x0045, B:20:0x0082, B:22:0x008e, B:25:0x0092, B:27:0x0095, B:31:0x009f, B:29:0x00a6, B:33:0x00a9, B:40:0x00b5, B:42:0x00c1, B:44:0x00db, B:46:0x00e5, B:47:0x00f2, B:49:0x00fa, B:51:0x0104, B:52:0x0112, B:54:0x011a, B:56:0x0124, B:36:0x01ef, B:38:0x01f3, B:58:0x0132, B:60:0x013e, B:61:0x0148, B:63:0x0154, B:64:0x015e, B:66:0x0162, B:71:0x0169, B:116:0x0171, B:73:0x0177, B:75:0x0180, B:77:0x018d, B:79:0x0191, B:81:0x0195, B:83:0x0199, B:85:0x019d, B:87:0x01a1, B:89:0x01ad, B:93:0x01b6, B:94:0x01c3, B:96:0x01c7, B:97:0x01cb, B:99:0x01cf, B:100:0x01d3, B:102:0x01d7, B:103:0x01db, B:105:0x01df, B:106:0x01e2, B:108:0x01e6, B:109:0x01e9, B:111:0x01ed, B:114:0x01bb, B:119:0x01fe), top: B:9:0x001c, outer: #5 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean parseTheme(final Theme.ThemeInfo themeInfo) {
            int stringKeyToInt;
            int intValue;
            String[] split;
            if (themeInfo == null || themeInfo.pathToFile == null) {
                return false;
            }
            boolean z = true;
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(themeInfo.pathToFile));
                int i = 0;
                boolean z2 = false;
                while (true) {
                    try {
                        int read = fileInputStream.read(ThemesHorizontalListCell.bytes);
                        if (read == -1) {
                            break;
                        }
                        int i2 = i;
                        int i3 = 0;
                        int i4 = 0;
                        while (true) {
                            if (i3 >= read) {
                                break;
                            }
                            byte[] bArr = ThemesHorizontalListCell.bytes;
                            if (bArr[i3] == 10) {
                                int i5 = i3 - i4;
                                int i6 = i5 + 1;
                                String str = new String(bArr, i4, i5, "UTF-8");
                                if (str.startsWith("WLS=")) {
                                    String substring = str.substring(4);
                                    Uri parse = Uri.parse(substring);
                                    themeInfo.slug = parse.getQueryParameter("slug");
                                    themeInfo.pathToWallpaper = new File(ApplicationLoader.getFilesDirFixed(), Utilities.MD5(substring) + ".wp").getAbsolutePath();
                                    String queryParameter = parse.getQueryParameter("mode");
                                    if (queryParameter != null && (split = queryParameter.toLowerCase().split(" ")) != null && split.length > 0) {
                                        int i7 = 0;
                                        while (true) {
                                            if (i7 >= split.length) {
                                                break;
                                            }
                                            if ("blur".equals(split[i7])) {
                                                themeInfo.isBlured = z;
                                                break;
                                            }
                                            i7++;
                                        }
                                    }
                                    if (!TextUtils.isEmpty(parse.getQueryParameter("pattern"))) {
                                        try {
                                            String queryParameter2 = parse.getQueryParameter("bg_color");
                                            if (!TextUtils.isEmpty(queryParameter2)) {
                                                themeInfo.patternBgColor = Integer.parseInt(queryParameter2.substring(0, 6), 16) | (-16777216);
                                                if (queryParameter2.length() >= 13 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(6))) {
                                                    themeInfo.patternBgGradientColor1 = Integer.parseInt(queryParameter2.substring(7, 13), 16) | (-16777216);
                                                }
                                                if (queryParameter2.length() >= 20 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(13))) {
                                                    themeInfo.patternBgGradientColor2 = Integer.parseInt(queryParameter2.substring(14, 20), 16) | (-16777216);
                                                }
                                                if (queryParameter2.length() == 27 && AndroidUtilities.isValidWallChar(queryParameter2.charAt(20))) {
                                                    themeInfo.patternBgGradientColor3 = Integer.parseInt(queryParameter2.substring(21), 16) | (-16777216);
                                                }
                                            }
                                        } catch (Exception unused) {
                                        }
                                        try {
                                            String queryParameter3 = parse.getQueryParameter("rotation");
                                            if (!TextUtils.isEmpty(queryParameter3)) {
                                                themeInfo.patternBgGradientRotation = Utilities.parseInt((CharSequence) queryParameter3).intValue();
                                            }
                                        } catch (Exception unused2) {
                                        }
                                        String queryParameter4 = parse.getQueryParameter("intensity");
                                        if (!TextUtils.isEmpty(queryParameter4)) {
                                            themeInfo.patternIntensity = Utilities.parseInt((CharSequence) queryParameter4).intValue();
                                        }
                                        if (themeInfo.patternIntensity == 0) {
                                            themeInfo.patternIntensity = 50;
                                        }
                                    }
                                } else {
                                    if (str.startsWith("WPS")) {
                                        themeInfo.previewWallpaperOffset = i6 + i2;
                                        z2 = true;
                                        break;
                                    }
                                    int indexOf = str.indexOf(61);
                                    if (indexOf != -1 && ((stringKeyToInt = ThemeColors.stringKeyToInt(str.substring(0, indexOf))) == Theme.key_chat_inBubble || stringKeyToInt == Theme.key_chat_outBubble || stringKeyToInt == Theme.key_chat_wallpaper || stringKeyToInt == Theme.key_chat_wallpaper_gradient_to1 || stringKeyToInt == Theme.key_chat_wallpaper_gradient_to2 || stringKeyToInt == Theme.key_chat_wallpaper_gradient_to3)) {
                                        String substring2 = str.substring(indexOf + 1);
                                        if (substring2.length() > 0 && substring2.charAt(0) == '#') {
                                            try {
                                                intValue = Color.parseColor(substring2);
                                            } catch (Exception unused3) {
                                            }
                                            if (stringKeyToInt != Theme.key_chat_inBubble) {
                                                themeInfo.setPreviewInColor(intValue);
                                            } else if (stringKeyToInt == Theme.key_chat_outBubble) {
                                                themeInfo.setPreviewOutColor(intValue);
                                            } else if (stringKeyToInt == Theme.key_chat_wallpaper) {
                                                themeInfo.setPreviewBackgroundColor(intValue);
                                            } else if (stringKeyToInt == Theme.key_chat_wallpaper_gradient_to1) {
                                                themeInfo.previewBackgroundGradientColor1 = intValue;
                                            } else if (stringKeyToInt == Theme.key_chat_wallpaper_gradient_to2) {
                                                themeInfo.previewBackgroundGradientColor2 = intValue;
                                            } else if (stringKeyToInt == Theme.key_chat_wallpaper_gradient_to3) {
                                                themeInfo.previewBackgroundGradientColor3 = intValue;
                                            }
                                        }
                                        intValue = Utilities.parseInt((CharSequence) substring2).intValue();
                                        if (stringKeyToInt != Theme.key_chat_inBubble) {
                                        }
                                    }
                                }
                                i4 += i6;
                                i2 += i6;
                            }
                            i3++;
                            z = true;
                        }
                        if (z2 || i == i2) {
                            break;
                        }
                        fileInputStream.getChannel().position(i2);
                        i = i2;
                        z = true;
                    } finally {
                    }
                }
                fileInputStream.close();
            } catch (Throwable th) {
                FileLog.e(th);
            }
            if (themeInfo.pathToWallpaper == null || themeInfo.badWallpaper || new File(themeInfo.pathToWallpaper).exists()) {
                themeInfo.previewParsed = true;
                return true;
            }
            if (this.loadingWallpapers.containsKey(themeInfo)) {
                return false;
            }
            this.loadingWallpapers.put(themeInfo, themeInfo.slug);
            TLRPC.TL_account_getWallPaper tL_account_getWallPaper = new TLRPC.TL_account_getWallPaper();
            TLRPC.TL_inputWallPaperSlug tL_inputWallPaperSlug = new TLRPC.TL_inputWallPaperSlug();
            tL_inputWallPaperSlug.slug = themeInfo.slug;
            tL_account_getWallPaper.wallpaper = tL_inputWallPaperSlug;
            ConnectionsManager.getInstance(themeInfo.account).sendRequest(tL_account_getWallPaper, new RequestDelegate() { // from class: org.telegram.ui.ChannelColorActivity$ThemeChooser$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChannelColorActivity.ThemeChooser.this.lambda$parseTheme$4(themeInfo, tLObject, tL_error);
                }
            });
            return false;
        }

        private void updateSelected() {
            int childAdapterPosition;
            for (int i = 0; i < this.listView.getChildCount(); i++) {
                View childAt = this.listView.getChildAt(i);
                if ((childAt instanceof ThemeSmallPreviewView) && (childAdapterPosition = this.listView.getChildAdapterPosition(childAt)) >= 0 && childAdapterPosition < this.items.size()) {
                    ((ThemeSmallPreviewView) childAt).setSelected(((ChatThemeBottomSheet.ChatThemeItem) this.items.get(childAdapterPosition)).isSelected, true);
                }
            }
        }

        private void updateState(boolean z) {
            if (this.dataLoaded) {
                AndroidUtilities.updateViewVisibilityAnimated(this.progressView, false, 1.0f, true, z);
            } else {
                AndroidUtilities.updateViewVisibilityAnimated(this.progressView, true, 1.0f, true, z);
            }
        }

        public boolean isDark() {
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            return resourcesProvider != null ? resourcesProvider.isDark() : Theme.isCurrentThemeDark();
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), i2);
        }

        public void setGalleryWallpaper(TLRPC.WallPaper wallPaper) {
            this.fallbackWallpaper = wallPaper;
            AndroidUtilities.forEachViews((RecyclerView) this.listView, new Consumer() { // from class: org.telegram.ui.ChannelColorActivity$ThemeChooser$$ExternalSyntheticLambda1
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    ChannelColorActivity.ThemeChooser.this.lambda$setGalleryWallpaper$0((View) obj);
                }
            });
            if (this.fallbackWallpaper != null) {
                if ((this.items.isEmpty() || ((ChatThemeBottomSheet.ChatThemeItem) this.items.get(0)).chatTheme.showAsDefaultStub) && this.withRemovedStub) {
                    this.items.add(0, new ChatThemeBottomSheet.ChatThemeItem(EmojiThemes.createChatThemesRemoved(this.currentAccount)));
                    this.adapter.notifyDataSetChanged();
                }
            }
        }

        public void setOnEmoticonSelected(Utilities.Callback<String> callback) {
            this.onEmoticonSelected = callback;
        }

        public void setSelectedEmoticon(String str, boolean z) {
            this.currentEmoticon = str;
            int i = -1;
            for (int i2 = 0; i2 < this.items.size(); i2++) {
                ChatThemeBottomSheet.ChatThemeItem chatThemeItem = (ChatThemeBottomSheet.ChatThemeItem) this.items.get(i2);
                boolean z2 = TextUtils.equals(this.currentEmoticon, chatThemeItem.getEmoticon()) || (TextUtils.isEmpty(str) && chatThemeItem.chatTheme.showAsDefaultStub);
                chatThemeItem.isSelected = z2;
                if (z2) {
                    i = i2;
                }
            }
            if (i >= 0 && !z && (this.listView.getLayoutManager() instanceof LinearLayoutManager)) {
                ((LinearLayoutManager) this.listView.getLayoutManager()).scrollToPositionWithOffset(i, (AndroidUtilities.displaySize.x - AndroidUtilities.dp(83.0f)) / 2);
            }
            updateSelected();
        }

        public void setWithRemovedStub(boolean z) {
            this.withRemovedStub = z;
        }

        public void updateColors() {
            boolean isDark = isDark();
            for (int i = 0; i < this.items.size(); i++) {
                ((ChatThemeBottomSheet.ChatThemeItem) this.items.get(i)).themeIndex = isDark ? 1 : 0;
            }
            AndroidUtilities.forEachViews((RecyclerView) this.listView, new Consumer() { // from class: org.telegram.ui.ChannelColorActivity$ThemeChooser$$ExternalSyntheticLambda0
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    ChannelColorActivity.ThemeChooser.this.lambda$updateColors$2((View) obj);
                }
            });
            this.adapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes4.dex */
    public class ThemeDelegate implements Theme.ResourcesProvider {
        public ThemeDelegate() {
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ void applyServiceShaderMatrix(int i, int i2, float f, float f2) {
            Theme.applyServiceShaderMatrix(i, i2, f, f2);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ ColorFilter getAnimatedEmojiColorFilter() {
            ColorFilter colorFilter;
            colorFilter = Theme.chat_animatedEmojiTextColorFilter;
            return colorFilter;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public int getColor(int i) {
            int indexOfKey = ChannelColorActivity.this.currentColors.indexOfKey(i);
            return indexOfKey >= 0 ? ChannelColorActivity.this.currentColors.valueAt(indexOfKey) : ChannelColorActivity.this.parentResourcesProvider != null ? ChannelColorActivity.this.parentResourcesProvider.getColor(i) : Theme.getColor(i);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ int getColorOrDefault(int i) {
            int color;
            color = getColor(i);
            return color;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ int getCurrentColor(int i) {
            int color;
            color = getColor(i);
            return color;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public Drawable getDrawable(String str) {
            if (str.equals("drawableMsgIn")) {
                return ChannelColorActivity.this.msgInDrawable;
            }
            if (str.equals("drawableMsgInSelected")) {
                return ChannelColorActivity.this.msgInDrawableSelected;
            }
            if (str.equals("drawableMsgOut")) {
                return ChannelColorActivity.this.msgOutDrawable;
            }
            if (str.equals("drawableMsgOutSelected")) {
                return ChannelColorActivity.this.msgOutDrawableSelected;
            }
            if (str.equals("drawableMsgOutCheckRead")) {
                ChannelColorActivity.this.msgOutCheckReadDrawable.setColorFilter(getColor(Theme.key_chat_outSentCheckRead), PorterDuff.Mode.MULTIPLY);
                return ChannelColorActivity.this.msgOutCheckReadDrawable;
            }
            if (!str.equals("drawableMsgOutHalfCheck")) {
                return ChannelColorActivity.this.parentResourcesProvider != null ? ChannelColorActivity.this.parentResourcesProvider.getDrawable(str) : Theme.getThemeDrawable(str);
            }
            ChannelColorActivity.this.msgOutHalfCheckDrawable.setColorFilter(getColor(Theme.key_chat_outSentCheckRead), PorterDuff.Mode.MULTIPLY);
            return ChannelColorActivity.this.msgOutHalfCheckDrawable;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public Paint getPaint(String str) {
            Paint themePaint;
            if (str.equals("paintDivider")) {
                return ChannelColorActivity.this.dividerPaint;
            }
            themePaint = Theme.getThemePaint(str);
            return themePaint;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ boolean hasGradientService() {
            return Theme.ResourcesProvider.-CC.$default$hasGradientService(this);
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public boolean isDark() {
            return ChannelColorActivity.this.isDark;
        }

        @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
        public /* synthetic */ void setAnimatedColor(int i, int i2) {
            Theme.ResourcesProvider.-CC.$default$setAnimatedColor(this, i, i2);
        }

        public void toggle() {
            ChannelColorActivity.this.isDark = !r0.isDark;
            ChannelColorActivity.this.updateThemeColors();
            ChannelColorActivity.this.updateColors();
        }
    }

    public ChannelColorActivity(long j) {
        boolean isCurrentThemeDark = Theme.isCurrentThemeDark();
        this.isDark = isCurrentThemeDark;
        this.rowsCount = 0;
        this.forceDark = isCurrentThemeDark;
        this.currentColors = new SparseIntArray();
        Paint paint = new Paint(1);
        this.dividerPaint = paint;
        paint.setStrokeWidth(1.0f);
        paint.setColor(Theme.getColor(Theme.key_divider, this.resourceProvider));
        this.msgOutCheckReadDrawable = ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_check_s).mutate();
        this.msgOutHalfCheckDrawable = ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_halfcheck).mutate();
        this.dialogId = j;
        final TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-j));
        if (chat != null) {
            this.currentLevel = chat.level;
        }
        MessagesController.getInstance(this.currentAccount).getBoostsController().getBoostsStats(j, new Consumer() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda4
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ChannelColorActivity.this.lambda$new$0(chat, (TL_stories.TL_premium_boostsStatus) obj);
            }
        });
        this.resourceProvider = new ThemeDelegate();
        this.msgInDrawable = new Theme.MessageDrawable(0, false, false, this.resourceProvider);
        this.msgInDrawableSelected = new Theme.MessageDrawable(0, false, true, this.resourceProvider);
        this.msgOutDrawable = new Theme.MessageDrawable(0, true, false, this.resourceProvider);
        this.msgOutDrawableSelected = new Theme.MessageDrawable(0, true, true, this.resourceProvider);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v28, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v37 */
    private void buttonClick() {
        ?? r2;
        TLRPC.TL_inputWallPaperNoFile tL_inputWallPaperNoFile;
        if (this.boostsStatus == null || this.button.isLoading()) {
            return;
        }
        if (this.currentLevel < minLevelRequired()) {
            this.button.setLoading(true);
            showLimit();
            return;
        }
        final int[] iArr = {0};
        final int[] iArr2 = {0};
        final boolean[] zArr = {false};
        final Utilities.Callback callback = new Utilities.Callback() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda7
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ChannelColorActivity.this.lambda$buttonClick$7(zArr, iArr2, iArr, (TLRPC.TL_error) obj);
            }
        };
        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
        if (chat == null) {
            FileLog.e("channel is null in ChannelColorAcitivity");
            BulletinFactory.of(this).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.UnknownError)).show();
            return;
        }
        this.button.setLoading(true);
        if (this.currentReplyColor != this.selectedReplyColor || this.currentReplyEmoji != this.selectedReplyEmoji) {
            TLRPC.TL_channels_updateColor tL_channels_updateColor = new TLRPC.TL_channels_updateColor();
            tL_channels_updateColor.channel = getMessagesController().getInputChannel(-this.dialogId);
            tL_channels_updateColor.for_profile = false;
            if (chat.color == null) {
                chat.color = new TLRPC.TL_peerColor();
                chat.flags2 |= 128;
            }
            int i = tL_channels_updateColor.flags;
            tL_channels_updateColor.flags = i | 4;
            int i2 = this.selectedReplyColor;
            tL_channels_updateColor.color = i2;
            TLRPC.TL_peerColor tL_peerColor = chat.color;
            int i3 = tL_peerColor.flags;
            int i4 = i3 | 1;
            tL_peerColor.flags = i4;
            tL_peerColor.color = i2;
            long j = this.selectedReplyEmoji;
            if (j != 0) {
                tL_channels_updateColor.flags = i | 5;
                tL_channels_updateColor.background_emoji_id = j;
                tL_peerColor.flags = i3 | 3;
                tL_peerColor.background_emoji_id = j;
            } else {
                tL_peerColor.flags = i4 & (-3);
                tL_peerColor.background_emoji_id = 0L;
            }
            iArr[0] = iArr[0] + 1;
            getConnectionsManager().sendRequest(tL_channels_updateColor, new RequestDelegate() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda8
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChannelColorActivity.this.lambda$buttonClick$8(callback, tLObject, tL_error);
                }
            });
        }
        if (this.currentProfileColor != this.selectedProfileColor || this.currentProfileEmoji != this.selectedProfileEmoji) {
            TLRPC.TL_channels_updateColor tL_channels_updateColor2 = new TLRPC.TL_channels_updateColor();
            tL_channels_updateColor2.channel = getMessagesController().getInputChannel(-this.dialogId);
            tL_channels_updateColor2.for_profile = true;
            if (chat.profile_color == null) {
                chat.profile_color = new TLRPC.TL_peerColor();
                chat.flags2 |= 256;
            }
            int i5 = this.selectedProfileColor;
            if (i5 >= 0) {
                tL_channels_updateColor2.flags |= 4;
                tL_channels_updateColor2.color = i5;
                TLRPC.TL_peerColor tL_peerColor2 = chat.profile_color;
                tL_peerColor2.flags |= 1;
                tL_peerColor2.color = i5;
            } else {
                chat.profile_color.flags &= -2;
            }
            long j2 = this.selectedProfileEmoji;
            if (j2 != 0) {
                tL_channels_updateColor2.flags |= 1;
                tL_channels_updateColor2.background_emoji_id = j2;
                TLRPC.TL_peerColor tL_peerColor3 = chat.profile_color;
                tL_peerColor3.flags |= 2;
                tL_peerColor3.background_emoji_id = j2;
            } else {
                TLRPC.TL_peerColor tL_peerColor4 = chat.profile_color;
                tL_peerColor4.flags &= -3;
                tL_peerColor4.background_emoji_id = 0L;
            }
            iArr[0] = iArr[0] + 1;
            getConnectionsManager().sendRequest(tL_channels_updateColor2, new RequestDelegate() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda9
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChannelColorActivity.this.lambda$buttonClick$9(callback, tLObject, tL_error);
                }
            });
        }
        if (!ChatThemeController.wallpaperEquals(this.currentWallpaper, this.selectedWallpaper)) {
            TLRPC.TL_messages_setChatWallPaper tL_messages_setChatWallPaper = new TLRPC.TL_messages_setChatWallPaper();
            tL_messages_setChatWallPaper.peer = getMessagesController().getInputPeer(this.dialogId);
            TLRPC.WallPaper wallPaper = this.selectedWallpaper;
            if (wallPaper != null) {
                if (TextUtils.isEmpty(ChatThemeController.getWallpaperEmoticon(wallPaper))) {
                    tL_messages_setChatWallPaper.flags |= 1;
                    TLRPC.WallPaper wallPaper2 = this.selectedWallpaper;
                    if (wallPaper2 instanceof TLRPC.TL_wallPaper) {
                        TLRPC.TL_inputWallPaper tL_inputWallPaper = new TLRPC.TL_inputWallPaper();
                        TLRPC.WallPaper wallPaper3 = this.selectedWallpaper;
                        tL_inputWallPaper.id = wallPaper3.id;
                        tL_inputWallPaper.access_hash = wallPaper3.access_hash;
                        tL_inputWallPaperNoFile = tL_inputWallPaper;
                    } else if (wallPaper2 instanceof TLRPC.TL_wallPaperNoFile) {
                        TLRPC.TL_inputWallPaperNoFile tL_inputWallPaperNoFile2 = new TLRPC.TL_inputWallPaperNoFile();
                        tL_inputWallPaperNoFile2.id = this.selectedWallpaper.id;
                        tL_inputWallPaperNoFile = tL_inputWallPaperNoFile2;
                    }
                    tL_messages_setChatWallPaper.wallpaper = tL_inputWallPaperNoFile;
                } else {
                    tL_messages_setChatWallPaper.flags |= 1;
                    TLRPC.TL_inputWallPaperNoFile tL_inputWallPaperNoFile3 = new TLRPC.TL_inputWallPaperNoFile();
                    tL_messages_setChatWallPaper.wallpaper = tL_inputWallPaperNoFile3;
                    tL_inputWallPaperNoFile3.id = 0L;
                    tL_messages_setChatWallPaper.flags |= 4;
                    TLRPC.TL_wallPaperSettings tL_wallPaperSettings = new TLRPC.TL_wallPaperSettings();
                    tL_messages_setChatWallPaper.settings = tL_wallPaperSettings;
                    tL_wallPaperSettings.flags |= 128;
                    tL_wallPaperSettings.emoticon = ChatThemeController.getWallpaperEmoticon(this.selectedWallpaper);
                }
            }
            iArr[0] = iArr[0] + 1;
            getConnectionsManager().sendRequest(tL_messages_setChatWallPaper, new RequestDelegate() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda10
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChannelColorActivity.this.lambda$buttonClick$10(callback, tLObject, tL_error);
                }
            });
            TLRPC.ChatFull chatFull = getMessagesController().getChatFull(-this.dialogId);
            ChatThemeController.getInstance(this.currentAccount).saveChatWallpaper(this.dialogId, this.selectedWallpaper);
            if (chatFull != null) {
                TLRPC.WallPaper wallPaper4 = this.selectedWallpaper;
                if (wallPaper4 == null) {
                    chatFull.flags2 &= -129;
                    wallPaper4 = null;
                } else {
                    chatFull.flags2 |= 128;
                }
                chatFull.wallpaper = wallPaper4;
                getMessagesController().putChatFull(chatFull);
                NotificationCenter notificationCenter = getNotificationCenter();
                int i6 = NotificationCenter.chatInfoDidLoad;
                Boolean bool = Boolean.FALSE;
                notificationCenter.lambda$postNotificationNameOnUIThread$1(i6, chatFull, 0, bool, bool);
            }
        }
        if (DialogObject.emojiStatusesEqual(this.currentStatusEmoji, this.selectedStatusEmoji)) {
            r2 = 0;
        } else {
            TLRPC.TL_channels_updateEmojiStatus tL_channels_updateEmojiStatus = new TLRPC.TL_channels_updateEmojiStatus();
            tL_channels_updateEmojiStatus.channel = getMessagesController().getInputChannel(-this.dialogId);
            TLRPC.EmojiStatus emojiStatus = this.selectedStatusEmoji;
            if (emojiStatus == null || (emojiStatus instanceof TLRPC.TL_emojiStatusEmpty)) {
                tL_channels_updateEmojiStatus.emoji_status = new TLRPC.TL_emojiStatusEmpty();
                chat.emoji_status = new TLRPC.TL_emojiStatusEmpty();
                chat.flags2 &= -513;
            } else {
                tL_channels_updateEmojiStatus.emoji_status = emojiStatus;
                chat.emoji_status = emojiStatus;
                chat.flags |= 512;
            }
            getMessagesController().updateEmojiStatusUntilUpdate(this.dialogId, this.selectedStatusEmoji);
            r2 = 0;
            iArr[0] = iArr[0] + 1;
            getConnectionsManager().sendRequest(tL_channels_updateEmojiStatus, new RequestDelegate() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda11
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    ChannelColorActivity.this.lambda$buttonClick$11(callback, tLObject, tL_error);
                }
            });
        }
        if (iArr[r2] == 0) {
            lambda$onBackPressed$321();
            this.button.setLoading(r2);
            return;
        }
        getMessagesController().putChat(chat, r2);
        NotificationCenter notificationCenter2 = getNotificationCenter();
        int i7 = NotificationCenter.updateInterfaces;
        Object[] objArr = new Object[1];
        objArr[r2] = Integer.valueOf(MessagesController.UPDATE_MASK_EMOJI_STATUS);
        notificationCenter2.lambda$postNotificationNameOnUIThread$1(i7, objArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TLRPC.Document getEmojiSetThumb(TLRPC.StickerSet stickerSet) {
        if (stickerSet != null && stickerSet.thumb_document_id == 0) {
            TLRPC.TL_messages_stickerSet groupStickerSetById = getMediaDataController().getGroupStickerSetById(stickerSet);
            if (!groupStickerSetById.documents.isEmpty()) {
                return groupStickerSetById.documents.get(0);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getEmojiSetThumbId(TLRPC.StickerSet stickerSet) {
        if (stickerSet == null) {
            return 0L;
        }
        long j = stickerSet.thumb_document_id;
        if (j != 0) {
            return j;
        }
        TLRPC.TL_messages_stickerSet groupStickerSetById = getMediaDataController().getGroupStickerSetById(stickerSet);
        return !groupStickerSetById.documents.isEmpty() ? groupStickerSetById.documents.get(0).id : j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getThemeChooserEmoticon() {
        String wallpaperEmoticon = ChatThemeController.getWallpaperEmoticon(this.selectedWallpaper);
        return (wallpaperEmoticon == null && this.selectedWallpaper == null && this.galleryWallpaper != null) ? "❌" : wallpaperEmoticon;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buttonClick$10(Utilities.Callback callback, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.Updates) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        }
        if (callback != null) {
            callback.run(tL_error);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buttonClick$11(Utilities.Callback callback, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.Updates) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        }
        if (callback != null) {
            callback.run(tL_error);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buttonClick$6(boolean[] zArr, int[] iArr, int[] iArr2, TLRPC.TL_error tL_error) {
        int i;
        if (zArr[0] || (i = iArr[0]) >= iArr2[0]) {
            return;
        }
        if (tL_error != null) {
            zArr[0] = true;
            if ("BOOSTS_REQUIRED".equals(tL_error.text)) {
                showLimit();
                return;
            } else {
                this.button.setLoading(false);
                BulletinFactory.of(this).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, tL_error.text)).show();
                return;
            }
        }
        int i2 = i + 1;
        iArr[0] = i2;
        if (i2 == iArr2[0]) {
            lambda$onBackPressed$321();
            showBulletin();
            this.button.setLoading(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buttonClick$7(final boolean[] zArr, final int[] iArr, final int[] iArr2, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                ChannelColorActivity.this.lambda$buttonClick$6(zArr, iArr, iArr2, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buttonClick$8(Utilities.Callback callback, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.Updates) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        }
        if (callback != null) {
            callback.run(tL_error);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$buttonClick$9(Utilities.Callback callback, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.Updates) {
            getMessagesController().processUpdates((TLRPC.Updates) tLObject, false);
        }
        if (callback != null) {
            callback.run(tL_error);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(int i, View view, Long l, Integer num) {
        if (i == this.replyEmojiRow) {
            this.selectedReplyEmoji = l.longValue();
            updateMessagesPreview(true);
        } else {
            if (i == this.profileEmojiRow) {
                this.selectedProfileEmoji = l.longValue();
            } else if (i == this.statusEmojiRow) {
                if (l.longValue() == 0) {
                    this.selectedStatusEmoji = null;
                } else if (num != null) {
                    TLRPC.TL_emojiStatusUntil tL_emojiStatusUntil = new TLRPC.TL_emojiStatusUntil();
                    this.selectedStatusEmoji = tL_emojiStatusUntil;
                    tL_emojiStatusUntil.until = num.intValue();
                    ((TLRPC.TL_emojiStatusUntil) this.selectedStatusEmoji).document_id = l.longValue();
                } else {
                    TLRPC.TL_emojiStatus tL_emojiStatus = new TLRPC.TL_emojiStatus();
                    this.selectedStatusEmoji = tL_emojiStatus;
                    tL_emojiStatus.document_id = l.longValue();
                }
            }
            updateProfilePreview(true);
        }
        updateButton(true);
        ((EmojiCell) view).setEmoji(l.longValue(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2() {
        BulletinFactory.of(this).createSimpleBulletin(R.raw.done, LocaleController.getString(R.string.ChannelWallpaperUpdated)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(TLRPC.WallPaper wallPaper) {
        this.currentWallpaper = wallPaper;
        this.selectedWallpaper = wallPaper;
        this.galleryWallpaper = wallPaper;
        updateButton(false);
        updateMessagesPreview(false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                ChannelColorActivity.this.lambda$createView$2();
            }
        }, 350L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(TLRPC.ChatFull chatFull, final View view, final int i) {
        long j = 0;
        if (!(view instanceof EmojiCell)) {
            if (i != this.removeProfileColorRow) {
                if (i == this.wallpaperRow) {
                    ChatThemeBottomSheet.openGalleryForBackground(getParentActivity(), this, this.dialogId, this.resourceProvider, new Utilities.Callback() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda13
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            ChannelColorActivity.this.lambda$createView$3((TLRPC.WallPaper) obj);
                        }
                    }, new ThemePreviewActivity.DayNightSwitchDelegate() { // from class: org.telegram.ui.ChannelColorActivity.2
                        @Override // org.telegram.ui.ThemePreviewActivity.DayNightSwitchDelegate
                        public boolean isDark() {
                            return ((BaseFragment) ChannelColorActivity.this).resourceProvider != null ? ((BaseFragment) ChannelColorActivity.this).resourceProvider.isDark() : Theme.isCurrentThemeDark();
                        }

                        @Override // org.telegram.ui.ThemePreviewActivity.DayNightSwitchDelegate
                        public boolean supportsAnimation() {
                            return false;
                        }

                        @Override // org.telegram.ui.ThemePreviewActivity.DayNightSwitchDelegate
                        public void switchDayNight(boolean z) {
                            if (((BaseFragment) ChannelColorActivity.this).resourceProvider instanceof ThemeDelegate) {
                                ((ThemeDelegate) ((BaseFragment) ChannelColorActivity.this).resourceProvider).toggle();
                            }
                            ChannelColorActivity.this.setForceDark(isDark(), false);
                            ChannelColorActivity.this.updateColors();
                        }
                    }, this.boostsStatus);
                    return;
                }
                return;
            } else {
                this.selectedProfileColor = -1;
                this.selectedProfileEmoji = 0L;
                updateProfilePreview(true);
                updateButton(true);
                updateRows();
                return;
            }
        }
        if (i == this.packStickerRow) {
            if (chatFull == null) {
                return;
            }
            GroupStickersActivity groupStickersActivity = new GroupStickersActivity(-this.dialogId);
            groupStickersActivity.setInfo(chatFull);
            presentFragment(groupStickersActivity);
            return;
        }
        if (i == this.replyEmojiRow) {
            j = this.selectedReplyEmoji;
        } else if (i == this.profileEmojiRow) {
            j = this.selectedProfileEmoji;
        } else if (i == this.statusEmojiRow) {
            j = DialogObject.getEmojiStatusDocumentId(this.selectedStatusEmoji);
        }
        long j2 = j;
        if (i != this.packEmojiRow) {
            showSelectStatusDialog((EmojiCell) view, j2, i == this.statusEmojiRow, new Utilities.Callback2() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda12
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    ChannelColorActivity.this.lambda$createView$1(i, view, (Long) obj, (Integer) obj2);
                }
            });
            return;
        }
        int emojiStickersLevelMin = getEmojiStickersLevelMin();
        TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus = this.boostsStatus;
        if (tL_premium_boostsStatus != null && tL_premium_boostsStatus.level < emojiStickersLevelMin) {
            openBoostDialog(29);
            return;
        }
        GroupStickersActivity groupStickersActivity2 = new GroupStickersActivity(-this.dialogId, true);
        groupStickersActivity2.setInfo(chatFull);
        presentFragment(groupStickersActivity2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(View view) {
        buttonClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TLRPC.Chat chat, TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        this.boostsStatus = tL_premium_boostsStatus;
        if (tL_premium_boostsStatus != null) {
            int i = tL_premium_boostsStatus.level;
            this.currentLevel = i;
            if (chat != null) {
                chat.flags |= 1024;
                chat.level = i;
            }
        }
        updateButton(true);
        ButtonWithCounterView buttonWithCounterView = this.button;
        if (buttonWithCounterView != null) {
            buttonWithCounterView.setLoading(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showLimit$12(TLRPC.Chat chat) {
        presentFragment(StatisticActivity.create(chat));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:51:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$showLimit$13(ChannelBoostsController.CanApplyBoost canApplyBoost) {
        int i;
        int i2;
        if (this.currentReplyColor != this.selectedReplyColor) {
            MessagesController.PeerColors peerColors = getMessagesController().peerColors;
            MessagesController.PeerColor color = peerColors == null ? null : peerColors.getColor(this.selectedReplyColor);
            if (color != null && color.getLvl(this.isGroup) > this.currentLevel) {
                i = color.getLvl(this.isGroup);
                if (this.currentProfileColor != this.selectedProfileColor) {
                    MessagesController.PeerColors peerColors2 = getMessagesController().profilePeerColors;
                    MessagesController.PeerColor color2 = peerColors2 != null ? peerColors2.getColor(this.selectedProfileColor) : null;
                    if (color2 != null && color2.getLvl(this.isGroup) > this.currentLevel) {
                        i = color2.getLvl(this.isGroup);
                        i2 = 24;
                        final int i3 = i;
                        if (this.currentReplyEmoji != this.selectedReplyEmoji && getMessagesController().channelBgIconLevelMin > this.currentLevel) {
                            i2 = 26;
                        }
                        if (this.currentProfileEmoji != this.selectedProfileEmoji && getProfileIconLevelMin() > this.currentLevel) {
                            i2 = 27;
                        }
                        if (!DialogObject.emojiStatusesEqual(this.currentStatusEmoji, this.selectedStatusEmoji) && getEmojiStatusLevelMin() > this.currentLevel) {
                            i2 = 25;
                        }
                        int i4 = !ChatThemeController.wallpaperEquals(this.currentWallpaper, this.selectedWallpaper) ? !TextUtils.isEmpty(ChatThemeController.getWallpaperEmoticon(this.selectedWallpaper)) ? 22 : 23 : i2;
                        if (getContext() == null || getParentActivity() == null) {
                            return;
                        }
                        LimitReachedBottomSheet limitReachedBottomSheet = new LimitReachedBottomSheet(this, getContext(), i4, this.currentAccount, getResourceProvider()) { // from class: org.telegram.ui.ChannelColorActivity.4
                            @Override // org.telegram.ui.Components.Premium.LimitReachedBottomSheet
                            protected int channelColorLevelMin() {
                                return i3;
                            }
                        };
                        limitReachedBottomSheet.setCanApplyBoost(canApplyBoost);
                        limitReachedBottomSheet.setBoostsStats(this.boostsStatus, true);
                        limitReachedBottomSheet.setDialogId(this.dialogId);
                        final TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
                        if (chat != null) {
                            limitReachedBottomSheet.showStatisticButtonInLink(new Runnable() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda19
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ChannelColorActivity.this.lambda$showLimit$12(chat);
                                }
                            });
                        }
                        showDialog(limitReachedBottomSheet);
                        this.button.setLoading(false);
                        return;
                    }
                }
                i2 = 20;
                final int i32 = i;
                if (this.currentReplyEmoji != this.selectedReplyEmoji) {
                    i2 = 26;
                }
                if (this.currentProfileEmoji != this.selectedProfileEmoji) {
                    i2 = 27;
                }
                if (!DialogObject.emojiStatusesEqual(this.currentStatusEmoji, this.selectedStatusEmoji)) {
                    i2 = 25;
                }
                if (!ChatThemeController.wallpaperEquals(this.currentWallpaper, this.selectedWallpaper)) {
                }
                if (getContext() == null) {
                    return;
                } else {
                    return;
                }
            }
        }
        i = 0;
        if (this.currentProfileColor != this.selectedProfileColor) {
        }
        i2 = 20;
        final int i322 = i;
        if (this.currentReplyEmoji != this.selectedReplyEmoji) {
        }
        if (this.currentProfileEmoji != this.selectedProfileEmoji) {
        }
        if (!DialogObject.emojiStatusesEqual(this.currentStatusEmoji, this.selectedStatusEmoji)) {
        }
        if (!ChatThemeController.wallpaperEquals(this.currentWallpaper, this.selectedWallpaper)) {
        }
        if (getContext() == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showUnsavedAlert$14(DialogInterface dialogInterface, int i) {
        lambda$onBackPressed$321();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showUnsavedAlert$15(DialogInterface dialogInterface, int i) {
        buttonClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$toggleTheme$16(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleTheme$17() {
        Theme.ResourcesProvider resourcesProvider = this.resourceProvider;
        if (resourcesProvider instanceof ThemeDelegate) {
            ((ThemeDelegate) resourcesProvider).toggle();
        } else {
            this.isDark = !this.isDark;
            updateThemeColors();
        }
        setForceDark(this.isDark, true);
        updateColors();
    }

    private void showBulletin() {
        BaseFragment baseFragment = this.bulletinFragment;
        if (baseFragment != null) {
            if (baseFragment instanceof ChatEditActivity) {
                ((ChatEditActivity) baseFragment).updateColorCell();
            }
            BulletinFactory.of(this.bulletinFragment).createSimpleBulletin(R.raw.contact_check, LocaleController.getString(this.isGroup ? R.string.GroupAppearanceUpdated : R.string.ChannelAppearanceUpdated)).show();
            this.bulletinFragment = null;
        }
    }

    private void showLimit() {
        getMessagesController().getBoostsController().userCanBoostChannel(this.dialogId, this.boostsStatus, new Consumer() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda17
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ChannelColorActivity.this.lambda$showLimit$13((ChannelBoostsController.CanApplyBoost) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUnsavedAlert() {
        if (getVisibleDialog() != null) {
            return;
        }
        AlertDialog create = new AlertDialog.Builder(getContext(), getResourceProvider()).setTitle(LocaleController.getString(R.string.ChannelColorUnsaved)).setMessage(LocaleController.getString(R.string.ChannelColorUnsavedMessage)).setNegativeButton(LocaleController.getString(R.string.Dismiss), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ChannelColorActivity.this.lambda$showUnsavedAlert$14(dialogInterface, i);
            }
        }).setPositiveButton(LocaleController.getString(R.string.ApplyTheme), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ChannelColorActivity.this.lambda$showUnsavedAlert$15(dialogInterface, i);
            }
        }).create();
        showDialog(create);
        ((TextView) create.getButton(-2)).setTextColor(getThemedColor(Theme.key_text_RedBold));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBoostsAndLevels(TL_stories.TL_premium_boostsStatus tL_premium_boostsStatus) {
        if (tL_premium_boostsStatus != null) {
            TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
            this.boostsStatus = tL_premium_boostsStatus;
            int i = tL_premium_boostsStatus.level;
            this.currentLevel = i;
            if (chat != null) {
                chat.level = i;
            }
            Adapter adapter = this.adapter;
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            updateButton(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateColors(View view) {
        if (view instanceof TextInfoPrivacyCell) {
            ((TextInfoPrivacyCell) view).setBackground(Theme.getThemedDrawableByKey(getContext(), this.listView.getChildAdapterPosition(view) == this.statusHintRow ? R.drawable.greydivider_bottom : R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow, this.resourceProvider));
            return;
        }
        view.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        if (view instanceof EmojiCell) {
            ((EmojiCell) view).updateColors();
            return;
        }
        if (view instanceof TextCell) {
            ((TextCell) view).updateColors();
        } else if (view instanceof PeerColorPicker) {
            ((PeerColorPicker) view).updateColors();
        } else if (view instanceof ThemeChooser) {
            ((ThemeChooser) view).updateColors();
        }
    }

    protected void createListView() {
        this.listView = new RecyclerListView(getContext(), this.resourceProvider);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(-this.dialogId));
        if (chat != null) {
            int colorId = ChatObject.getColorId(chat);
            this.selectedReplyColor = colorId;
            this.currentReplyColor = colorId;
            long emojiId = ChatObject.getEmojiId(chat);
            this.selectedReplyEmoji = emojiId;
            this.currentReplyEmoji = emojiId;
            int profileColorId = ChatObject.getProfileColorId(chat);
            this.selectedProfileColor = profileColorId;
            this.currentProfileColor = profileColorId;
            long profileEmojiId = ChatObject.getProfileEmojiId(chat);
            this.selectedProfileEmoji = profileEmojiId;
            this.currentProfileEmoji = profileEmojiId;
            TLRPC.EmojiStatus emojiStatus = chat.emoji_status;
            this.selectedStatusEmoji = emojiStatus;
            this.currentStatusEmoji = emojiStatus;
        }
        final TLRPC.ChatFull chatFull = getMessagesController().getChatFull(-this.dialogId);
        if (chatFull != null) {
            TLRPC.WallPaper wallPaper = chatFull.wallpaper;
            this.selectedWallpaper = wallPaper;
            this.currentWallpaper = wallPaper;
            if (ChatThemeController.isNotEmoticonWallpaper(wallPaper)) {
                this.galleryWallpaper = this.currentWallpaper;
            }
        }
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setTitle(LocaleController.getString(R.string.ChannelColorTitle2));
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.ChannelColorActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i != -1) {
                    if (i == 1) {
                        ChannelColorActivity.this.toggleTheme();
                    }
                } else {
                    ChannelColorActivity channelColorActivity = ChannelColorActivity.this;
                    if (channelColorActivity.currentLevel < channelColorActivity.minLevelRequired() || !ChannelColorActivity.this.hasUnsavedChanged()) {
                        ChannelColorActivity.this.lambda$onBackPressed$321();
                    } else {
                        ChannelColorActivity.this.showUnsavedAlert();
                    }
                }
            }
        });
        int i = R.raw.sun;
        RLottieDrawable rLottieDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), true, null);
        this.sunDrawable = rLottieDrawable;
        rLottieDrawable.setPlayInDirectionOfCustomEndFrame(true);
        if (this.isDark) {
            this.sunDrawable.setCurrentFrame(35);
            this.sunDrawable.setCustomEndFrame(36);
        } else {
            this.sunDrawable.setCustomEndFrame(0);
            this.sunDrawable.setCurrentFrame(0);
        }
        this.sunDrawable.beginApplyLayerColors();
        int color = Theme.getColor(Theme.key_chats_menuName, this.resourceProvider);
        this.sunDrawable.setLayerColor("Sunny.**", color);
        this.sunDrawable.setLayerColor("Path 6.**", color);
        this.sunDrawable.setLayerColor("Path.**", color);
        this.sunDrawable.setLayerColor("Path 5.**", color);
        this.dayNightItem = this.actionBar.createMenu().addItem(1, this.sunDrawable);
        FrameLayout frameLayout = new FrameLayout(context);
        updateRows();
        createListView();
        RecyclerListView recyclerListView = this.listView;
        Adapter adapter = new Adapter();
        this.adapter = adapter;
        recyclerListView.setAdapter(adapter);
        this.listView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerListView recyclerListView2 = this.listView;
        int i2 = Theme.key_windowBackgroundGray;
        recyclerListView2.setBackgroundColor(getThemedColor(i2));
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 0.0f, 68.0f));
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda0
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i3) {
                ChannelColorActivity.this.lambda$createView$4(chatFull, view, i3);
            }
        });
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setDurations(350L);
        defaultItemAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        defaultItemAnimator.setDelayAnimations(false);
        defaultItemAnimator.setSupportsChangeAnimations(false);
        this.listView.setItemAnimator(defaultItemAnimator);
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, this.resourceProvider);
        this.button = buttonWithCounterView;
        buttonWithCounterView.setText(LocaleController.getString(R.string.ApplyChanges), false);
        this.button.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChannelColorActivity.this.lambda$createView$5(view);
            }
        });
        updateButton(false);
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.buttonContainer = frameLayout2;
        frameLayout2.setBackgroundColor(getThemedColor(i2));
        this.buttonContainer.addView(this.button, LayoutHelper.createFrame(-1, 48.0f, 80, 10.0f, 10.0f, 10.0f, 10.0f));
        frameLayout.addView(this.buttonContainer, LayoutHelper.createFrame(-1, 68, 80));
        Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.ChannelColorActivity.3
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean allowLayoutChanges() {
                return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean bottomOffsetAnimated() {
                return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean clipWithGradient(int i3) {
                return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i3);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getBottomOffset(int i3) {
                return ChannelColorActivity.this.buttonContainer.getMeasuredHeight();
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ int getTopOffset(int i3) {
                return Bulletin.Delegate.-CC.$default$getTopOffset(this, i3);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onBottomOffsetChange(float f) {
                Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onHide(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onShow(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
            }
        });
        this.fragmentView = frameLayout;
        return frameLayout;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.chatWasBoostedByUser) {
            if (this.dialogId == ((Long) objArr[2]).longValue()) {
                updateBoostsAndLevels((TL_stories.TL_premium_boostsStatus) objArr[0]);
            }
        } else {
            if (i != NotificationCenter.boostByChannelCreated || ((Boolean) objArr[1]).booleanValue()) {
                return;
            }
            getMessagesController().getBoostsController().getBoostsStats(this.dialogId, new Consumer() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda5
                @Override // com.google.android.exoplayer2.util.Consumer
                public final void accept(Object obj) {
                    ChannelColorActivity.this.updateBoostsAndLevels((TL_stories.TL_premium_boostsStatus) obj);
                }
            });
        }
    }

    public View findChildAt(int i) {
        for (int i2 = 0; i2 < this.listView.getChildCount(); i2++) {
            View childAt = this.listView.getChildAt(i2);
            if (this.listView.getChildAdapterPosition(childAt) == i) {
                return childAt;
            }
        }
        return null;
    }

    protected int getCustomWallpaperLevelMin() {
        return getMessagesController().channelCustomWallpaperLevelMin;
    }

    protected int getEmojiPackInfoStrRes() {
        return 0;
    }

    protected int getEmojiPackStrRes() {
        return 0;
    }

    protected int getEmojiStatusInfoStrRes() {
        return R.string.ChannelEmojiStatusInfo;
    }

    protected int getEmojiStatusLevelMin() {
        return getMessagesController().channelEmojiStatusLevelMin;
    }

    protected int getEmojiStatusStrRes() {
        return R.string.ChannelEmojiStatus;
    }

    protected int getEmojiStickersLevelMin() {
        return 0;
    }

    protected int getMessagePreviewType() {
        return 3;
    }

    protected int getProfileIconLevelMin() {
        return getMessagesController().channelProfileIconLevelMin;
    }

    protected int getProfileInfoStrRes() {
        return R.string.ChannelProfileInfo;
    }

    protected int getStickerPackInfoStrRes() {
        return 0;
    }

    protected int getStickerPackStrRes() {
        return 0;
    }

    protected int getWallpaper2InfoStrRes() {
        return R.string.ChannelWallpaper2Info;
    }

    protected int getWallpaperLevelMin() {
        return getMessagesController().channelWallpaperLevelMin;
    }

    protected int getWallpaperStrRes() {
        return R.string.ChannelWallpaper;
    }

    public boolean hasUnsavedChanged() {
        return (this.currentReplyColor == this.selectedReplyColor && this.currentReplyEmoji == this.selectedReplyEmoji && this.currentProfileColor == this.selectedProfileColor && this.currentProfileEmoji == this.selectedProfileEmoji && DialogObject.emojiStatusesEqual(this.currentStatusEmoji, this.selectedStatusEmoji) && ChatThemeController.wallpaperEquals(this.currentWallpaper, this.selectedWallpaper)) ? false : true;
    }

    protected boolean isForum() {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return !hasUnsavedChanged() || this.currentLevel < minLevelRequired();
    }

    public int minLevelRequired() {
        int i = 0;
        if (this.currentReplyColor != this.selectedReplyColor) {
            MessagesController.PeerColors peerColors = getMessagesController().peerColors;
            MessagesController.PeerColor color = peerColors == null ? null : peerColors.getColor(this.selectedReplyColor);
            if (color != null) {
                i = Math.max(0, color.getLvl(this.isGroup));
            }
        }
        if (this.currentReplyEmoji != this.selectedReplyEmoji) {
            i = Math.max(i, getMessagesController().channelBgIconLevelMin);
        }
        if (this.currentProfileColor != this.selectedProfileColor) {
            MessagesController.PeerColors peerColors2 = getMessagesController().profilePeerColors;
            MessagesController.PeerColor color2 = peerColors2 != null ? peerColors2.getColor(this.selectedProfileColor) : null;
            if (color2 != null) {
                i = Math.max(i, color2.getLvl(this.isGroup));
            }
        }
        if (this.currentProfileEmoji != this.selectedProfileEmoji) {
            i = Math.max(i, getProfileIconLevelMin());
        }
        if (!DialogObject.emojiStatusesEqual(this.currentStatusEmoji, this.selectedStatusEmoji)) {
            i = Math.max(i, getEmojiStatusLevelMin());
        }
        return !ChatThemeController.wallpaperEquals(this.currentWallpaper, this.selectedWallpaper) ? Math.max(i, getWallpaperLevelMin()) : i;
    }

    protected boolean needBoostInfoSection() {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (this.currentLevel < minLevelRequired() || !hasUnsavedChanged()) {
            return super.onBackPressed();
        }
        showUnsavedAlert();
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        getMediaDataController().loadRestrictedStatusEmojis();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.boostByChannelCreated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatWasBoostedByUser);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.boostByChannelCreated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatWasBoostedByUser);
    }

    protected void openBoostDialog(int i) {
    }

    public void setForceDark(boolean z, boolean z2) {
        if (this.forceDark == z) {
            return;
        }
        this.forceDark = z;
        if (z2) {
            RLottieDrawable rLottieDrawable = this.sunDrawable;
            rLottieDrawable.setCustomEndFrame(z ? rLottieDrawable.getFramesCount() : 0);
            RLottieDrawable rLottieDrawable2 = this.sunDrawable;
            if (rLottieDrawable2 != null) {
                rLottieDrawable2.start();
                return;
            }
            return;
        }
        int framesCount = z ? this.sunDrawable.getFramesCount() - 1 : 0;
        this.sunDrawable.setCurrentFrame(framesCount, false, true);
        this.sunDrawable.setCustomEndFrame(framesCount);
        ActionBarMenuItem actionBarMenuItem = this.dayNightItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.invalidate();
        }
    }

    public ChannelColorActivity setOnApplied(BaseFragment baseFragment) {
        this.bulletinFragment = baseFragment;
        return this;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void setResourceProvider(Theme.ResourcesProvider resourcesProvider) {
        this.parentResourcesProvider = resourcesProvider;
    }

    public void showSelectStatusDialog(EmojiCell emojiCell, long j, boolean z, final Utilities.Callback2 callback2) {
        int i;
        int i2;
        if (this.selectAnimatedEmojiDialog != null || emojiCell == null) {
            return;
        }
        final SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[] selectAnimatedEmojiDialogWindowArr = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow[1];
        boolean z2 = ((float) (emojiCell.getTop() + emojiCell.getHeight())) > ((float) this.listView.getMeasuredHeight()) / 2.0f;
        int min = (int) Math.min(AndroidUtilities.dp(330.0f), AndroidUtilities.displaySize.y * 0.75f);
        int min2 = (int) Math.min(AndroidUtilities.dp(324.0f), AndroidUtilities.displaySize.x * 0.95f);
        emojiCell.imageDrawable.removeOldDrawable();
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = emojiCell.imageDrawable;
        if (emojiCell.imageDrawable != null) {
            emojiCell.imageDrawable.play();
            emojiCell.updateImageBounds();
            Rect rect = AndroidUtilities.rectTmp2;
            rect.set(emojiCell.imageDrawable.getBounds());
            int dp = z2 ? ((-rect.centerY()) + AndroidUtilities.dp(12.0f)) - min : (-(emojiCell.getHeight() - rect.centerY())) - AndroidUtilities.dp(16.0f);
            i = rect.centerX() - (AndroidUtilities.displaySize.x - min2);
            i2 = dp;
        } else {
            i = 0;
            i2 = 0;
        }
        int i3 = i2;
        SelectAnimatedEmojiDialog selectAnimatedEmojiDialog = new SelectAnimatedEmojiDialog(this, getContext(), true, Integer.valueOf(i), z ? z2 ? 10 : 9 : z2 ? 5 : 7, true, getResourceProvider(), z2 ? 24 : 16, emojiCell.getColor()) { // from class: org.telegram.ui.ChannelColorActivity.5
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            protected float getScrimDrawableTranslationY() {
                return 0.0f;
            }

            @Override // org.telegram.ui.SelectAnimatedEmojiDialog
            protected void onEmojiSelected(View view, Long l, TLRPC.Document document, Integer num) {
                Utilities.Callback2 callback22 = callback2;
                if (callback22 != null) {
                    callback22.run(Long.valueOf(l == null ? 0L : l.longValue()), num);
                }
                if (selectAnimatedEmojiDialogWindowArr[0] != null) {
                    ChannelColorActivity.this.selectAnimatedEmojiDialog = null;
                    selectAnimatedEmojiDialogWindowArr[0].dismiss();
                }
            }
        };
        selectAnimatedEmojiDialog.useAccentForPlus = true;
        selectAnimatedEmojiDialog.setSelected(j == 0 ? null : Long.valueOf(j));
        selectAnimatedEmojiDialog.setSaveState(3);
        selectAnimatedEmojiDialog.setScrimDrawable(swapAnimatedEmojiDrawable, emojiCell);
        int i4 = -2;
        SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow selectAnimatedEmojiDialogWindow = new SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow(selectAnimatedEmojiDialog, i4, i4) { // from class: org.telegram.ui.ChannelColorActivity.6
            @Override // org.telegram.ui.SelectAnimatedEmojiDialog.SelectAnimatedEmojiDialogWindow, android.widget.PopupWindow
            public void dismiss() {
                super.dismiss();
                ChannelColorActivity.this.selectAnimatedEmojiDialog = null;
            }
        };
        this.selectAnimatedEmojiDialog = selectAnimatedEmojiDialogWindow;
        selectAnimatedEmojiDialogWindowArr[0] = selectAnimatedEmojiDialogWindow;
        selectAnimatedEmojiDialogWindow.showAsDropDown(emojiCell, 0, i3, 53);
        selectAnimatedEmojiDialogWindowArr[0].dimBehind();
    }

    public void toggleTheme() {
        FrameLayout frameLayout = (FrameLayout) getParentActivity().getWindow().getDecorView();
        final Bitmap createBitmap = Bitmap.createBitmap(frameLayout.getWidth(), frameLayout.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(createBitmap);
        this.dayNightItem.setAlpha(0.0f);
        frameLayout.draw(canvas);
        this.dayNightItem.setAlpha(1.0f);
        final Paint paint = new Paint(1);
        paint.setColor(-16777216);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        final Paint paint2 = new Paint(1);
        paint2.setFilterBitmap(true);
        int[] iArr = new int[2];
        this.dayNightItem.getLocationInWindow(iArr);
        final float f = iArr[0];
        final float f2 = iArr[1];
        final float measuredWidth = f + (this.dayNightItem.getMeasuredWidth() / 2.0f);
        final float measuredHeight = f2 + (this.dayNightItem.getMeasuredHeight() / 2.0f);
        final float max = Math.max(createBitmap.getHeight(), createBitmap.getWidth()) + AndroidUtilities.navigationBarHeight;
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        paint2.setShader(new BitmapShader(createBitmap, tileMode, tileMode));
        View view = new View(getContext()) { // from class: org.telegram.ui.ChannelColorActivity.7
            @Override // android.view.View
            protected void onDraw(Canvas canvas2) {
                super.onDraw(canvas2);
                if (ChannelColorActivity.this.isDark) {
                    if (ChannelColorActivity.this.changeDayNightViewProgress > 0.0f) {
                        canvas.drawCircle(measuredWidth, measuredHeight, max * ChannelColorActivity.this.changeDayNightViewProgress, paint);
                    }
                    canvas2.drawBitmap(createBitmap, 0.0f, 0.0f, paint2);
                } else {
                    canvas2.drawCircle(measuredWidth, measuredHeight, max * (1.0f - ChannelColorActivity.this.changeDayNightViewProgress), paint2);
                }
                canvas2.save();
                canvas2.translate(f, f2);
                ChannelColorActivity.this.dayNightItem.draw(canvas2);
                canvas2.restore();
            }
        };
        this.changeDayNightView = view;
        view.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                boolean lambda$toggleTheme$16;
                lambda$toggleTheme$16 = ChannelColorActivity.lambda$toggleTheme$16(view2, motionEvent);
                return lambda$toggleTheme$16;
            }
        });
        this.changeDayNightViewProgress = 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.changeDayNightViewAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ChannelColorActivity.8
            boolean changedNavigationBarColor = false;

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChannelColorActivity.this.changeDayNightViewProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                ChannelColorActivity.this.changeDayNightView.invalidate();
                if (this.changedNavigationBarColor || ChannelColorActivity.this.changeDayNightViewProgress <= 0.5f) {
                    return;
                }
                this.changedNavigationBarColor = true;
            }
        });
        this.changeDayNightViewAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.ChannelColorActivity.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (ChannelColorActivity.this.changeDayNightView != null) {
                    if (ChannelColorActivity.this.changeDayNightView.getParent() != null) {
                        ((ViewGroup) ChannelColorActivity.this.changeDayNightView.getParent()).removeView(ChannelColorActivity.this.changeDayNightView);
                    }
                    ChannelColorActivity.this.changeDayNightView = null;
                }
                ChannelColorActivity.this.changeDayNightViewAnimator = null;
                super.onAnimationEnd(animator);
            }
        });
        this.changeDayNightViewAnimator.setDuration(400L);
        this.changeDayNightViewAnimator.setInterpolator(Easings.easeInOutQuad);
        this.changeDayNightViewAnimator.start();
        frameLayout.addView(this.changeDayNightView, new ViewGroup.LayoutParams(-1, -1));
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                ChannelColorActivity.this.lambda$toggleTheme$17();
            }
        });
    }

    public void updateButton(boolean z) {
        SpannableStringBuilder spannableStringBuilder;
        ButtonWithCounterView buttonWithCounterView;
        if (this.boostsStatus == null) {
            return;
        }
        int minLevelRequired = minLevelRequired();
        if (this.currentLevel >= minLevelRequired) {
            buttonWithCounterView = this.button;
            spannableStringBuilder = null;
        } else {
            if (this.lock == null) {
                this.lock = new SpannableStringBuilder("l");
                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(R.drawable.mini_switch_lock);
                coloredImageSpan.setTopOffset(1);
                this.lock.setSpan(coloredImageSpan, 0, 1, 33);
            }
            spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append((CharSequence) this.lock).append((CharSequence) LocaleController.formatPluralString("BoostLevelRequired", minLevelRequired, new Object[0]));
            buttonWithCounterView = this.button;
        }
        buttonWithCounterView.setSubText(spannableStringBuilder, z);
    }

    public void updateColors() {
        this.actionBar.setBackgroundColor(getThemedColor(Theme.key_actionBarDefault));
        this.actionBar.setTitleColor(getThemedColor(Theme.key_actionBarDefaultTitle));
        this.actionBar.setItemsColor(getThemedColor(Theme.key_actionBarDefaultIcon), false);
        this.actionBar.setItemsBackgroundColor(getThemedColor(Theme.key_actionBarDefaultSelector), false);
        RecyclerListView recyclerListView = this.listView;
        int i = Theme.key_windowBackgroundGray;
        recyclerListView.setBackgroundColor(getThemedColor(i));
        this.adapter.notifyDataSetChanged();
        AndroidUtilities.forEachViews((RecyclerView) this.listView, new Consumer() { // from class: org.telegram.ui.ChannelColorActivity$$ExternalSyntheticLambda6
            @Override // com.google.android.exoplayer2.util.Consumer
            public final void accept(Object obj) {
                ChannelColorActivity.this.updateColors((View) obj);
            }
        });
        this.buttonContainer.setBackgroundColor(getThemedColor(i));
        this.button.updateColors();
        setNavigationBarColor(getNavigationBarColor());
    }

    public void updateMessagesPreview(boolean z) {
        MessageObject messageObject;
        View findChildAt = findChildAt(this.messagesPreviewRow);
        View findChildAt2 = findChildAt(this.replyColorListRow);
        View findChildAt3 = findChildAt(this.replyEmojiRow);
        View findChildAt4 = findChildAt(this.wallpaperThemesRow);
        if (findChildAt instanceof ThemePreviewMessagesCell) {
            ThemePreviewMessagesCell themePreviewMessagesCell = (ThemePreviewMessagesCell) findChildAt;
            ChatMessageCell[] cells = themePreviewMessagesCell.getCells();
            for (int i = 0; i < cells.length; i++) {
                ChatMessageCell chatMessageCell = cells[i];
                if (chatMessageCell != null && (messageObject = chatMessageCell.getMessageObject()) != null) {
                    messageObject.overrideLinkColor = this.selectedReplyColor;
                    messageObject.overrideLinkEmoji = this.selectedReplyEmoji;
                    cells[i].setAvatar(messageObject);
                    cells[i].invalidate();
                }
            }
            Drawable backgroundDrawable = PreviewView.getBackgroundDrawable(this.backgroundDrawable, this.currentAccount, this.selectedWallpaper, this.isDark);
            this.backgroundDrawable = backgroundDrawable;
            themePreviewMessagesCell.setOverrideBackground(backgroundDrawable);
        }
        if (findChildAt2 instanceof PeerColorActivity.PeerColorGrid) {
            ((PeerColorActivity.PeerColorGrid) findChildAt2).setSelected(this.selectedReplyColor, z);
        } else if (findChildAt2 instanceof PeerColorPicker) {
            ((PeerColorPicker) findChildAt2).setSelected(this.selectedReplyColor, z);
        }
        if (findChildAt3 instanceof EmojiCell) {
            EmojiCell emojiCell = (EmojiCell) findChildAt3;
            emojiCell.setAdaptiveEmojiColor(this.currentAccount, this.selectedReplyColor, true);
            emojiCell.setEmoji(this.selectedReplyEmoji, z);
        }
        if (findChildAt4 instanceof ThemeChooser) {
            ThemeChooser themeChooser = (ThemeChooser) findChildAt4;
            themeChooser.setSelectedEmoticon(getThemeChooserEmoticon(), z);
            themeChooser.setGalleryWallpaper(this.galleryWallpaper);
        }
    }

    public void updateProfilePreview(boolean z) {
        TLRPC.StickerSet stickerSet;
        TLRPC.StickerSet stickerSet2;
        View findChildAt = findChildAt(this.profilePreviewRow);
        View findChildAt2 = findChildAt(this.profileColorGridRow);
        View findChildAt3 = findChildAt(this.profileEmojiRow);
        View findChildAt4 = findChildAt(this.statusEmojiRow);
        View findChildAt5 = findChildAt(this.packEmojiRow);
        View findChildAt6 = findChildAt(this.packStickerRow);
        if (findChildAt instanceof ProfilePreview) {
            ProfilePreview profilePreview = (ProfilePreview) findChildAt;
            profilePreview.setColor(this.selectedProfileColor, z);
            profilePreview.setEmoji(this.selectedProfileEmoji, z);
            profilePreview.setEmojiStatus(this.selectedStatusEmoji, z);
            profilePreview.profileView.overrideAvatarColor(this.selectedReplyColor);
        }
        if (findChildAt2 instanceof PeerColorActivity.PeerColorGrid) {
            ((PeerColorActivity.PeerColorGrid) findChildAt2).setSelected(this.selectedProfileColor, z);
        } else if (findChildAt2 instanceof PeerColorPicker) {
            ((PeerColorPicker) findChildAt2).setSelected(this.selectedReplyColor, z);
        }
        if (findChildAt3 instanceof EmojiCell) {
            EmojiCell emojiCell = (EmojiCell) findChildAt3;
            emojiCell.setAdaptiveEmojiColor(this.currentAccount, this.selectedProfileColor, false);
            emojiCell.setEmoji(this.selectedProfileEmoji, z);
        }
        if (findChildAt4 instanceof EmojiCell) {
            EmojiCell emojiCell2 = (EmojiCell) findChildAt4;
            emojiCell2.setAdaptiveEmojiColor(this.currentAccount, this.selectedProfileColor, false);
            emojiCell2.setEmoji(DialogObject.getEmojiStatusDocumentId(this.selectedStatusEmoji), z);
        }
        if (findChildAt5 instanceof EmojiCell) {
            EmojiCell emojiCell3 = (EmojiCell) findChildAt5;
            emojiCell3.setAdaptiveEmojiColor(this.currentAccount, this.selectedProfileColor, false);
            TLRPC.ChatFull chatFull = getMessagesController().getChatFull(-this.dialogId);
            if (chatFull == null || (stickerSet2 = chatFull.emojiset) == null) {
                emojiCell3.setEmoji(0L, false);
            } else {
                emojiCell3.setEmoji(getEmojiSetThumbId(stickerSet2), false);
            }
        }
        if (findChildAt6 instanceof EmojiCell) {
            TLRPC.ChatFull chatFull2 = getMessagesController().getChatFull(-this.dialogId);
            if (chatFull2 == null || (stickerSet = chatFull2.stickerset) == null) {
                ((EmojiCell) findChildAt6).setEmoji(0L, false);
            } else {
                ((EmojiCell) findChildAt6).setEmoji(getEmojiSetThumb(stickerSet), false);
            }
        }
        updateRows();
    }

    protected void updateRows() {
        Adapter adapter;
        Adapter adapter2;
        this.messagesPreviewRow = 0;
        this.replyColorListRow = 1;
        this.replyEmojiRow = 2;
        this.replyHintRow = 3;
        this.wallpaperThemesRow = 4;
        this.wallpaperRow = 5;
        this.wallpaperHintRow = 6;
        this.profilePreviewRow = 7;
        this.profileColorGridRow = 8;
        this.rowsCount = 10;
        this.profileEmojiRow = 9;
        if (this.selectedProfileEmoji != 0 || this.selectedProfileColor >= 0) {
            boolean z = this.removeProfileColorRow >= 0;
            this.rowsCount = 11;
            this.removeProfileColorRow = 10;
            if (!z && (adapter = this.adapter) != null) {
                adapter.notifyItemInserted(10);
                this.adapter.notifyItemChanged(this.profileEmojiRow);
            }
        } else {
            int i = this.removeProfileColorRow;
            this.removeProfileColorRow = -1;
            if (i >= 0 && (adapter2 = this.adapter) != null) {
                adapter2.notifyItemRemoved(i);
                this.adapter.notifyItemChanged(this.profileEmojiRow);
            }
        }
        int i2 = this.rowsCount;
        this.profileHintRow = i2;
        this.statusEmojiRow = i2 + 1;
        this.rowsCount = i2 + 3;
        this.statusHintRow = i2 + 2;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0069  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateThemeColors() {
        SparseIntArray themeFileValues;
        int[] defaultColors;
        View findChildAt;
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
        String str = "Blue";
        String string = sharedPreferences.getString("lastDayTheme", "Blue");
        if (Theme.getTheme(string) == null || Theme.getTheme(string).isDark()) {
            string = "Blue";
        }
        String str2 = "Dark Blue";
        String string2 = sharedPreferences.getString("lastDarkTheme", "Dark Blue");
        if (Theme.getTheme(string2) == null || !Theme.getTheme(string2).isDark()) {
            string2 = "Dark Blue";
        }
        Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
        if (!string.equals(string2)) {
            str2 = string2;
        } else if (activeTheme.isDark() || string.equals("Dark Blue") || string.equals("Night")) {
            str2 = string2;
            Theme.ThemeInfo theme = !this.isDark ? Theme.getTheme(str2) : Theme.getTheme(str);
            this.currentColors.clear();
            String[] strArr = new String[1];
            String str3 = theme.assetName;
            themeFileValues = str3 == null ? Theme.getThemeFileValues(null, str3, strArr) : Theme.getThemeFileValues(new File(theme.pathToFile), null, strArr);
            defaultColors = Theme.getDefaultColors();
            if (defaultColors != null) {
                for (int i = 0; i < defaultColors.length; i++) {
                    this.currentColors.put(i, defaultColors[i]);
                }
            }
            if (themeFileValues != null) {
                for (int i2 = 0; i2 < themeFileValues.size(); i2++) {
                    this.currentColors.put(themeFileValues.keyAt(i2), themeFileValues.valueAt(i2));
                }
                Theme.ThemeAccent accent = theme.getAccent(false);
                if (accent != null) {
                    accent.fillAccentColors(themeFileValues, this.currentColors);
                }
            }
            this.dividerPaint.setColor(Theme.getColor(Theme.key_divider, this.resourceProvider));
            this.backgroundDrawable = PreviewView.getBackgroundDrawable(this.backgroundDrawable, this.currentAccount, this.selectedWallpaper, this.isDark);
            findChildAt = findChildAt(this.messagesPreviewRow);
            if (findChildAt instanceof ThemePreviewMessagesCell) {
                return;
            }
            ((ThemePreviewMessagesCell) findChildAt).setOverrideBackground(this.backgroundDrawable);
            return;
        }
        str = string;
        if (!this.isDark) {
        }
        this.currentColors.clear();
        String[] strArr2 = new String[1];
        String str32 = theme.assetName;
        if (str32 == null) {
        }
        defaultColors = Theme.getDefaultColors();
        if (defaultColors != null) {
        }
        if (themeFileValues != null) {
        }
        this.dividerPaint.setColor(Theme.getColor(Theme.key_divider, this.resourceProvider));
        this.backgroundDrawable = PreviewView.getBackgroundDrawable(this.backgroundDrawable, this.currentAccount, this.selectedWallpaper, this.isDark);
        findChildAt = findChildAt(this.messagesPreviewRow);
        if (findChildAt instanceof ThemePreviewMessagesCell) {
        }
    }
}
