package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DispatchQueue;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_photoSize;
import org.telegram.tgnet.TLRPC$TL_photoSizeProgressive;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.AudioPlayerCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBarView;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public class AudioPlayerAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate, DownloadController.FileDownloadProgressListener {
    private int TAG;
    private ActionBar actionBar;
    private AnimatorSet actionBarAnimation;
    private View actionBarShadow;
    private ClippingTextViewSwitcher authorTextView;
    private BackupImageView bigAlbumConver;
    private boolean blurredAnimationInProgress;
    private FrameLayout blurredView;
    private CoverContainer coverContainer;
    private boolean currentAudioFinishedLoading;
    private String currentFile;
    private boolean draggingSeekBar;
    private TextView durationTextView;
    private ImageView emptyImageView;
    private TextView emptySubtitleTextView;
    private TextView emptyTitleTextView;
    private LinearLayout emptyView;
    private boolean inFullSize;
    private long lastBufferedPositionCheck;
    private int lastDuration;
    private MessageObject lastMessageObject;
    long lastRewindingTime;
    private int lastTime;
    long lastUpdateRewindingPlayerTime;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private RLottieImageView nextButton;
    private ActionBarMenuItem optionsButton;
    private LaunchActivity parentActivity;
    private ImageView playButton;
    private PlayPauseDrawable playPauseDrawable;
    private ActionBarMenuItem playbackSpeedButton;
    private FrameLayout playerLayout;
    private View playerShadow;
    private ArrayList<MessageObject> playlist;
    private RLottieImageView prevButton;
    private LineProgressView progressView;
    private ActionBarMenuItem repeatButton;
    private ActionBarMenuSubItem repeatListItem;
    private ActionBarMenuSubItem repeatSongItem;
    private ActionBarMenuSubItem reverseOrderItem;
    int rewindingForwardPressedCount;
    int rewindingState;
    private ActionBarMenuItem searchItem;
    private int searchOpenOffset;
    private boolean searchWas;
    private boolean searching;
    private SeekBarView seekBarView;
    private ActionBarMenuSubItem shuffleListItem;
    private SimpleTextView timeTextView;
    private ClippingTextViewSwitcher titleTextView;
    private ActionBarMenuSubItem[] speedItems = new ActionBarMenuSubItem[4];
    private View[] buttons = new View[5];
    private boolean scrollToSong = true;
    private int searchOpenPosition = -1;
    private int scrollOffsetY = Integer.MAX_VALUE;
    float rewindingProgress = -1.0f;
    private final Runnable forwardSeek = new AnonymousClass1();

    public static /* synthetic */ boolean lambda$new$7(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithSwipe() {
        return false;
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onFailedDownload(String str, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressUpload(String str, long j, long j2, boolean z) {
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onSuccessDownload(String str) {
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            AudioPlayerAlert.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            long duration = MediaController.getInstance().getDuration();
            if (duration == 0 || duration == -9223372036854775807L) {
                AudioPlayerAlert.this.lastRewindingTime = System.currentTimeMillis();
                return;
            }
            float f = AudioPlayerAlert.this.rewindingProgress;
            long currentTimeMillis = System.currentTimeMillis();
            AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
            long j = currentTimeMillis - audioPlayerAlert.lastRewindingTime;
            audioPlayerAlert.lastRewindingTime = currentTimeMillis;
            long j2 = currentTimeMillis - audioPlayerAlert.lastUpdateRewindingPlayerTime;
            int i = audioPlayerAlert.rewindingForwardPressedCount;
            float f2 = (float) duration;
            float f3 = ((f * f2) + ((float) (((i == 1 ? 3L : i == 2 ? 6L : 12L) * j) - j))) / f2;
            if (f3 < 0.0f) {
                f3 = 0.0f;
            }
            audioPlayerAlert.rewindingProgress = f3;
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null && playingMessageObject.isMusic()) {
                if (!MediaController.getInstance().isMessagePaused()) {
                    MediaController.getInstance().getPlayingMessageObject().audioProgress = AudioPlayerAlert.this.rewindingProgress;
                }
                AudioPlayerAlert.this.updateProgress(playingMessageObject);
            }
            AudioPlayerAlert audioPlayerAlert2 = AudioPlayerAlert.this;
            if (audioPlayerAlert2.rewindingState != 1 || audioPlayerAlert2.rewindingForwardPressedCount <= 0 || !MediaController.getInstance().isMessagePaused()) {
                return;
            }
            if (j2 > 200 || AudioPlayerAlert.this.rewindingProgress == 0.0f) {
                AudioPlayerAlert.this.lastUpdateRewindingPlayerTime = currentTimeMillis;
                MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), f3);
            }
            AudioPlayerAlert audioPlayerAlert3 = AudioPlayerAlert.this;
            if (audioPlayerAlert3.rewindingForwardPressedCount <= 0 || audioPlayerAlert3.rewindingProgress <= 0.0f) {
                return;
            }
            AndroidUtilities.runOnUIThread(audioPlayerAlert3.forwardSeek, 16L);
        }
    }

    public AudioPlayerAlert(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, true, resourcesProvider);
        TLRPC$User user;
        fixNavigationBar();
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null) {
            this.currentAccount = playingMessageObject.currentAccount;
        } else {
            this.currentAccount = UserConfig.selectedAccount;
        }
        this.parentActivity = (LaunchActivity) context;
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.fileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.musicDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.moreMusicDidLoad);
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.containerView = anonymousClass2;
        anonymousClass2.setWillNotDraw(false);
        ViewGroup viewGroup = this.containerView;
        int i = this.backgroundPaddingLeft;
        viewGroup.setPadding(i, 0, i, 0);
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context, resourcesProvider);
        this.actionBar = anonymousClass3;
        anonymousClass3.setBackgroundColor(getThemedColor("player_actionBar"));
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setItemsColor(getThemedColor("player_actionBarTitle"), false);
        this.actionBar.setItemsBackgroundColor(getThemedColor("player_actionBarSelector"), false);
        this.actionBar.setTitleColor(getThemedColor("player_actionBarTitle"));
        this.actionBar.setTitle(LocaleController.getString("AttachMusic", 2131624501));
        this.actionBar.setSubtitleColor(getThemedColor("player_actionBarSubtitle"));
        this.actionBar.setOccupyStatusBar(false);
        this.actionBar.setAlpha(0.0f);
        if (playingMessageObject != null && !MediaController.getInstance().currentPlaylistIsGlobalSearch()) {
            long dialogId = playingMessageObject.getDialogId();
            if (DialogObject.isEncryptedDialog(dialogId)) {
                TLRPC$EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(dialogId)));
                if (encryptedChat != null && (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(encryptedChat.user_id))) != null) {
                    this.actionBar.setTitle(ContactsController.formatName(user.first_name, user.last_name));
                }
            } else if (DialogObject.isUserDialog(dialogId)) {
                TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(dialogId));
                if (user2 != null) {
                    this.actionBar.setTitle(ContactsController.formatName(user2.first_name, user2.last_name));
                }
            } else {
                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialogId));
                if (chat != null) {
                    this.actionBar.setTitle(chat.title);
                }
            }
        }
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.createMenu().addItem(0, 2131165456).setIsSearchField(true).setActionBarMenuItemSearchListener(new AnonymousClass4());
        this.searchItem = actionBarMenuItemSearchListener;
        actionBarMenuItemSearchListener.setContentDescription(LocaleController.getString("Search", 2131628092));
        EditTextBoldCursor searchField = this.searchItem.getSearchField();
        searchField.setHint(LocaleController.getString("Search", 2131628092));
        searchField.setTextColor(getThemedColor("player_actionBarTitle"));
        searchField.setHintTextColor(getThemedColor("player_time"));
        searchField.setCursorColor(getThemedColor("player_actionBarTitle"));
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass5());
        View view = new View(context);
        this.actionBarShadow = view;
        view.setAlpha(0.0f);
        this.actionBarShadow.setBackgroundResource(2131165446);
        View view2 = new View(context);
        this.playerShadow = view2;
        view2.setBackgroundColor(getThemedColor("dialogShadowLine"));
        this.playerLayout = new AnonymousClass6(context);
        AnonymousClass7 anonymousClass7 = new AnonymousClass7(context);
        this.coverContainer = anonymousClass7;
        this.playerLayout.addView(anonymousClass7, LayoutHelper.createFrame(44, 44.0f, 53, 0.0f, 20.0f, 20.0f, 0.0f));
        AnonymousClass8 anonymousClass8 = new AnonymousClass8(context, context);
        this.titleTextView = anonymousClass8;
        this.playerLayout.addView(anonymousClass8, LayoutHelper.createFrame(-1, -2.0f, 51, 20.0f, 20.0f, 72.0f, 0.0f));
        AnonymousClass9 anonymousClass9 = new AnonymousClass9(context, context);
        this.authorTextView = anonymousClass9;
        this.playerLayout.addView(anonymousClass9, LayoutHelper.createFrame(-1, -2.0f, 51, 14.0f, 47.0f, 72.0f, 0.0f));
        AnonymousClass10 anonymousClass10 = new AnonymousClass10(context, resourcesProvider);
        this.seekBarView = anonymousClass10;
        anonymousClass10.setDelegate(new AnonymousClass11());
        this.seekBarView.setReportChanges(true);
        this.playerLayout.addView(this.seekBarView, LayoutHelper.createFrame(-1, 38.0f, 51, 5.0f, 70.0f, 5.0f, 0.0f));
        LineProgressView lineProgressView = new LineProgressView(context);
        this.progressView = lineProgressView;
        lineProgressView.setVisibility(4);
        this.progressView.setBackgroundColor(getThemedColor("player_progressBackground"));
        this.progressView.setProgressColor(getThemedColor("player_progress"));
        this.playerLayout.addView(this.progressView, LayoutHelper.createFrame(-1, 2.0f, 51, 21.0f, 90.0f, 21.0f, 0.0f));
        SimpleTextView simpleTextView = new SimpleTextView(context);
        this.timeTextView = simpleTextView;
        simpleTextView.setTextSize(12);
        this.timeTextView.setText("0:00");
        this.timeTextView.setTextColor(getThemedColor("player_time"));
        this.timeTextView.setImportantForAccessibility(2);
        this.playerLayout.addView(this.timeTextView, LayoutHelper.createFrame(100, -2.0f, 51, 20.0f, 98.0f, 0.0f, 0.0f));
        TextView textView = new TextView(context);
        this.durationTextView = textView;
        textView.setTextSize(1, 12.0f);
        this.durationTextView.setTextColor(getThemedColor("player_time"));
        this.durationTextView.setGravity(17);
        this.durationTextView.setImportantForAccessibility(2);
        this.playerLayout.addView(this.durationTextView, LayoutHelper.createFrame(-2, -2.0f, 53, 0.0f, 96.0f, 20.0f, 0.0f));
        ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, null, 0, getThemedColor("dialogTextBlack"), false, resourcesProvider);
        this.playbackSpeedButton = actionBarMenuItem;
        actionBarMenuItem.setLongClickEnabled(false);
        this.playbackSpeedButton.setShowSubmenuByMove(false);
        this.playbackSpeedButton.setAdditionalYOffset(-AndroidUtilities.dp(224.0f));
        this.playbackSpeedButton.setContentDescription(LocaleController.getString("AccDescrPlayerSpeed", 2131624043));
        this.playbackSpeedButton.setDelegate(new AudioPlayerAlert$$ExternalSyntheticLambda8(this));
        this.speedItems[0] = this.playbackSpeedButton.addSubItem(1, 2131165943, LocaleController.getString("SpeedSlow", 2131628399));
        this.speedItems[1] = this.playbackSpeedButton.addSubItem(2, 2131165944, LocaleController.getString("SpeedNormal", 2131628398));
        this.speedItems[2] = this.playbackSpeedButton.addSubItem(3, 2131165945, LocaleController.getString("SpeedFast", 2131628397));
        this.speedItems[3] = this.playbackSpeedButton.addSubItem(4, 2131165946, LocaleController.getString("SpeedVeryFast", 2131628400));
        if (AndroidUtilities.density >= 3.0f) {
            this.playbackSpeedButton.setPadding(0, 1, 0, 0);
        }
        this.playbackSpeedButton.setAdditionalXOffset(AndroidUtilities.dp(8.0f));
        this.playbackSpeedButton.setShowedFromBottom(true);
        this.playerLayout.addView(this.playbackSpeedButton, LayoutHelper.createFrame(36, 36.0f, 53, 0.0f, 86.0f, 20.0f, 0.0f));
        this.playbackSpeedButton.setOnClickListener(new AudioPlayerAlert$$ExternalSyntheticLambda0(this));
        this.playbackSpeedButton.setOnLongClickListener(new AudioPlayerAlert$$ExternalSyntheticLambda4(this));
        updatePlaybackButton();
        AnonymousClass12 anonymousClass12 = new AnonymousClass12(context);
        this.playerLayout.addView(anonymousClass12, LayoutHelper.createFrame(-1, 66.0f, 51, 0.0f, 111.0f, 0.0f, 0.0f));
        View[] viewArr = this.buttons;
        ActionBarMenuItem actionBarMenuItem2 = new ActionBarMenuItem(context, null, 0, 0, false, resourcesProvider);
        this.repeatButton = actionBarMenuItem2;
        viewArr[0] = actionBarMenuItem2;
        actionBarMenuItem2.setLongClickEnabled(false);
        this.repeatButton.setShowSubmenuByMove(false);
        this.repeatButton.setAdditionalYOffset(-AndroidUtilities.dp(166.0f));
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            this.repeatButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor("listSelectorSDK21"), 1, AndroidUtilities.dp(18.0f)));
        }
        anonymousClass12.addView(this.repeatButton, LayoutHelper.createFrame(48, 48, 51));
        this.repeatButton.setOnClickListener(new AudioPlayerAlert$$ExternalSyntheticLambda1(this));
        this.repeatSongItem = this.repeatButton.addSubItem(3, 2131166075, LocaleController.getString("RepeatSong", 2131627917));
        this.repeatListItem = this.repeatButton.addSubItem(4, 2131166074, LocaleController.getString("RepeatList", 2131627914));
        this.shuffleListItem = this.repeatButton.addSubItem(2, 2131166076, LocaleController.getString("ShuffleList", 2131628354));
        this.reverseOrderItem = this.repeatButton.addSubItem(1, 2131166068, LocaleController.getString("ReverseOrder", 2131628039));
        this.repeatButton.setShowedFromBottom(true);
        this.repeatButton.setDelegate(new AudioPlayerAlert$$ExternalSyntheticLambda9(this));
        int themedColor = getThemedColor("player_button");
        float scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        View[] viewArr2 = this.buttons;
        AnonymousClass13 anonymousClass13 = new AnonymousClass13(context, scaledTouchSlop);
        this.prevButton = anonymousClass13;
        viewArr2[1] = anonymousClass13;
        anonymousClass13.setScaleType(ImageView.ScaleType.CENTER);
        this.prevButton.setAnimation(2131558504, 20, 20);
        this.prevButton.setLayerColor("Triangle 3.**", themedColor);
        this.prevButton.setLayerColor("Triangle 4.**", themedColor);
        this.prevButton.setLayerColor("Rectangle 4.**", themedColor);
        if (i2 >= 21) {
            this.prevButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor("listSelectorSDK21"), 1, AndroidUtilities.dp(22.0f)));
        }
        anonymousClass12.addView(this.prevButton, LayoutHelper.createFrame(48, 48, 51));
        this.prevButton.setContentDescription(LocaleController.getString("AccDescrPrevious", 2131624045));
        View[] viewArr3 = this.buttons;
        ImageView imageView = new ImageView(context);
        this.playButton = imageView;
        viewArr3[2] = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        ImageView imageView2 = this.playButton;
        PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable(28);
        this.playPauseDrawable = playPauseDrawable;
        imageView2.setImageDrawable(playPauseDrawable);
        this.playPauseDrawable.setPause(!MediaController.getInstance().isMessagePaused(), false);
        this.playButton.setColorFilter(new PorterDuffColorFilter(getThemedColor("player_button"), PorterDuff.Mode.MULTIPLY));
        if (i2 >= 21) {
            this.playButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor("listSelectorSDK21"), 1, AndroidUtilities.dp(24.0f)));
        }
        anonymousClass12.addView(this.playButton, LayoutHelper.createFrame(48, 48, 51));
        this.playButton.setOnClickListener(AudioPlayerAlert$$ExternalSyntheticLambda3.INSTANCE);
        View[] viewArr4 = this.buttons;
        AnonymousClass14 anonymousClass14 = new AnonymousClass14(context, scaledTouchSlop);
        this.nextButton = anonymousClass14;
        viewArr4[3] = anonymousClass14;
        anonymousClass14.setScaleType(ImageView.ScaleType.CENTER);
        this.nextButton.setAnimation(2131558504, 20, 20);
        this.nextButton.setLayerColor("Triangle 3.**", themedColor);
        this.nextButton.setLayerColor("Triangle 4.**", themedColor);
        this.nextButton.setLayerColor("Rectangle 4.**", themedColor);
        this.nextButton.setRotation(180.0f);
        if (i2 >= 21) {
            this.nextButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor("listSelectorSDK21"), 1, AndroidUtilities.dp(22.0f)));
        }
        anonymousClass12.addView(this.nextButton, LayoutHelper.createFrame(48, 48, 51));
        this.nextButton.setContentDescription(LocaleController.getString("Next", 2131626801));
        View[] viewArr5 = this.buttons;
        ActionBarMenuItem actionBarMenuItem3 = new ActionBarMenuItem(context, null, 0, themedColor, false, resourcesProvider);
        this.optionsButton = actionBarMenuItem3;
        viewArr5[4] = actionBarMenuItem3;
        actionBarMenuItem3.setLongClickEnabled(false);
        this.optionsButton.setShowSubmenuByMove(false);
        this.optionsButton.setIcon(2131165453);
        this.optionsButton.setSubMenuOpenSide(2);
        this.optionsButton.setAdditionalYOffset(-AndroidUtilities.dp(157.0f));
        if (i2 >= 21) {
            this.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor("listSelectorSDK21"), 1, AndroidUtilities.dp(18.0f)));
        }
        anonymousClass12.addView(this.optionsButton, LayoutHelper.createFrame(48, 48, 51));
        this.optionsButton.addSubItem(1, 2131165741, LocaleController.getString("Forward", 2131625940));
        this.optionsButton.addSubItem(2, 2131165939, LocaleController.getString("ShareFile", 2131628274));
        this.optionsButton.addSubItem(5, 2131165709, LocaleController.getString("SaveToMusic", 2131628074));
        this.optionsButton.addSubItem(4, 2131165800, LocaleController.getString("ShowInChat", 2131628338));
        this.optionsButton.setShowedFromBottom(true);
        this.optionsButton.setOnClickListener(new AudioPlayerAlert$$ExternalSyntheticLambda2(this));
        this.optionsButton.setDelegate(new AudioPlayerAlert$$ExternalSyntheticLambda7(this));
        this.optionsButton.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131624003));
        LinearLayout linearLayout = new LinearLayout(context);
        this.emptyView = linearLayout;
        linearLayout.setOrientation(1);
        this.emptyView.setGravity(17);
        this.emptyView.setVisibility(8);
        this.containerView.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        this.emptyView.setOnTouchListener(AudioPlayerAlert$$ExternalSyntheticLambda5.INSTANCE);
        ImageView imageView3 = new ImageView(context);
        this.emptyImageView = imageView3;
        imageView3.setImageResource(2131165993);
        this.emptyImageView.setColorFilter(new PorterDuffColorFilter(getThemedColor("dialogEmptyImage"), PorterDuff.Mode.MULTIPLY));
        this.emptyView.addView(this.emptyImageView, LayoutHelper.createLinear(-2, -2));
        TextView textView2 = new TextView(context);
        this.emptyTitleTextView = textView2;
        textView2.setTextColor(getThemedColor("dialogEmptyText"));
        this.emptyTitleTextView.setGravity(17);
        this.emptyTitleTextView.setText(LocaleController.getString("NoAudioFound", 2131626804));
        this.emptyTitleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.emptyTitleTextView.setTextSize(1, 17.0f);
        this.emptyTitleTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
        this.emptyView.addView(this.emptyTitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 11, 0, 0));
        TextView textView3 = new TextView(context);
        this.emptySubtitleTextView = textView3;
        textView3.setTextColor(getThemedColor("dialogEmptyText"));
        this.emptySubtitleTextView.setGravity(17);
        this.emptySubtitleTextView.setTextSize(1, 15.0f);
        this.emptySubtitleTextView.setPadding(AndroidUtilities.dp(40.0f), 0, AndroidUtilities.dp(40.0f), 0);
        this.emptyView.addView(this.emptySubtitleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 6, 0, 0));
        AnonymousClass15 anonymousClass15 = new AnonymousClass15(context);
        this.listView = anonymousClass15;
        anonymousClass15.setClipToPadding(false);
        RecyclerListView recyclerListView = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), 1, false);
        this.layoutManager = linearLayoutManager;
        recyclerListView.setLayoutManager(linearLayoutManager);
        this.listView.setHorizontalScrollBarEnabled(false);
        this.listView.setVerticalScrollBarEnabled(false);
        this.containerView.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
        RecyclerListView recyclerListView2 = this.listView;
        ListAdapter listAdapter = new ListAdapter(context);
        this.listAdapter = listAdapter;
        recyclerListView2.setAdapter(listAdapter);
        this.listView.setGlowColor(getThemedColor("dialogScrollGlow"));
        this.listView.setOnItemClickListener(AudioPlayerAlert$$ExternalSyntheticLambda11.INSTANCE);
        this.listView.setOnScrollListener(new AnonymousClass16());
        this.playlist = MediaController.getInstance().getPlaylist();
        this.listAdapter.notifyDataSetChanged();
        this.containerView.addView(this.playerLayout, LayoutHelper.createFrame(-1, 179, 83));
        this.containerView.addView(this.playerShadow, new FrameLayout.LayoutParams(-1, AndroidUtilities.getShadowHeight(), 83));
        ((FrameLayout.LayoutParams) this.playerShadow.getLayoutParams()).bottomMargin = AndroidUtilities.dp(179.0f);
        this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 3.0f));
        this.containerView.addView(this.actionBar);
        AnonymousClass17 anonymousClass17 = new AnonymousClass17(context);
        this.blurredView = anonymousClass17;
        anonymousClass17.setAlpha(0.0f);
        this.blurredView.setVisibility(4);
        getContainer().addView(this.blurredView);
        BackupImageView backupImageView = new BackupImageView(context);
        this.bigAlbumConver = backupImageView;
        backupImageView.setAspectFit(true);
        this.bigAlbumConver.setRoundRadius(AndroidUtilities.dp(8.0f));
        this.bigAlbumConver.setScaleX(0.9f);
        this.bigAlbumConver.setScaleY(0.9f);
        this.blurredView.addView(this.bigAlbumConver, LayoutHelper.createFrame(-1, -1.0f, 51, 30.0f, 30.0f, 30.0f, 30.0f));
        updateTitle(false);
        updateRepeatButton();
        updateEmptyView();
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends FrameLayout {
        private int lastMeasturedHeight;
        private int lastMeasturedWidth;
        private RectF rect = new RectF();
        private boolean ignoreLayout = false;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            AudioPlayerAlert.this = r1;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return !AudioPlayerAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            int size = View.MeasureSpec.getSize(i2);
            int size2 = View.MeasureSpec.getSize(i);
            boolean z = false;
            if (size != this.lastMeasturedHeight || size2 != this.lastMeasturedWidth) {
                if (AudioPlayerAlert.this.blurredView.getTag() != null) {
                    AudioPlayerAlert.this.showAlbumCover(false, false);
                }
                this.lastMeasturedWidth = size2;
                this.lastMeasturedHeight = size;
            }
            this.ignoreLayout = true;
            if (Build.VERSION.SDK_INT >= 21 && !((BottomSheet) AudioPlayerAlert.this).isFullscreen) {
                setPadding(((BottomSheet) AudioPlayerAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingLeft, 0);
            }
            AudioPlayerAlert.this.playerLayout.setVisibility((AudioPlayerAlert.this.searchWas || ((BottomSheet) AudioPlayerAlert.this).keyboardVisible) ? 4 : 0);
            AudioPlayerAlert.this.playerShadow.setVisibility(AudioPlayerAlert.this.playerLayout.getVisibility());
            int paddingTop = size - getPaddingTop();
            ((FrameLayout.LayoutParams) AudioPlayerAlert.this.listView.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
            ((FrameLayout.LayoutParams) AudioPlayerAlert.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
            ((FrameLayout.LayoutParams) AudioPlayerAlert.this.blurredView.getLayoutParams()).topMargin = -getPaddingTop();
            int dp = AndroidUtilities.dp(179.0f);
            if (AudioPlayerAlert.this.playlist.size() > 1) {
                dp += ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop + (AudioPlayerAlert.this.playlist.size() * AndroidUtilities.dp(56.0f));
            }
            if (AudioPlayerAlert.this.searching || ((BottomSheet) AudioPlayerAlert.this).keyboardVisible) {
                i3 = AndroidUtilities.dp(8.0f);
            } else {
                if (dp >= paddingTop) {
                    dp = (int) ((paddingTop / 5) * 3.5f);
                }
                i3 = (paddingTop - dp) + AndroidUtilities.dp(8.0f);
                if (i3 > paddingTop - AndroidUtilities.dp(329.0f)) {
                    i3 = paddingTop - AndroidUtilities.dp(329.0f);
                }
                if (i3 < 0) {
                    i3 = 0;
                }
            }
            if (AudioPlayerAlert.this.listView.getPaddingTop() != i3) {
                AudioPlayerAlert.this.listView.setPadding(0, i3, 0, (!AudioPlayerAlert.this.searching || !((BottomSheet) AudioPlayerAlert.this).keyboardVisible) ? AudioPlayerAlert.this.listView.getPaddingBottom() : 0);
            }
            this.ignoreLayout = false;
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
            AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
            if (getMeasuredHeight() >= size) {
                z = true;
            }
            audioPlayerAlert.inFullSize = z;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            AudioPlayerAlert.this.updateLayout();
            AudioPlayerAlert.this.updateEmptyViewPosition();
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && AudioPlayerAlert.this.scrollOffsetY != 0 && AudioPlayerAlert.this.actionBar.getAlpha() == 0.0f) {
                boolean z = false;
                if (AudioPlayerAlert.this.listAdapter.getItemCount() <= 0 ? motionEvent.getY() < getMeasuredHeight() - AndroidUtilities.dp(191.0f) : motionEvent.getY() < AudioPlayerAlert.this.scrollOffsetY + AndroidUtilities.dp(12.0f)) {
                    z = true;
                }
                if (z) {
                    AudioPlayerAlert.this.dismiss();
                    return true;
                }
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            if (AudioPlayerAlert.this.playlist.size() <= 1) {
                ((BottomSheet) AudioPlayerAlert.this).shadowDrawable.setBounds(0, (getMeasuredHeight() - AudioPlayerAlert.this.playerLayout.getMeasuredHeight()) - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop, getMeasuredWidth(), getMeasuredHeight());
                ((BottomSheet) AudioPlayerAlert.this).shadowDrawable.draw(canvas);
                return;
            }
            int dp = AndroidUtilities.dp(13.0f);
            int i = (AudioPlayerAlert.this.scrollOffsetY - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop) - dp;
            if (((BottomSheet) AudioPlayerAlert.this).currentSheetAnimationType == 1) {
                i = (int) (i + AudioPlayerAlert.this.listView.getTranslationY());
            }
            int dp2 = AndroidUtilities.dp(20.0f) + i;
            int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(15.0f) + ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop;
            if (((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop + i < ActionBar.getCurrentActionBarHeight()) {
                float dp3 = dp + AndroidUtilities.dp(4.0f);
                float min = Math.min(1.0f, ((ActionBar.getCurrentActionBarHeight() - i) - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop) / dp3);
                int currentActionBarHeight = (int) ((ActionBar.getCurrentActionBarHeight() - dp3) * min);
                i -= currentActionBarHeight;
                dp2 -= currentActionBarHeight;
                measuredHeight += currentActionBarHeight;
                f = 1.0f - min;
            } else {
                f = 1.0f;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                int i2 = AndroidUtilities.statusBarHeight;
                i += i2;
                dp2 += i2;
            }
            ((BottomSheet) AudioPlayerAlert.this).shadowDrawable.setBounds(0, i, getMeasuredWidth(), measuredHeight);
            ((BottomSheet) AudioPlayerAlert.this).shadowDrawable.draw(canvas);
            if (f != 1.0f) {
                Theme.dialogs_onlineCirclePaint.setColor(AudioPlayerAlert.this.getThemedColor("dialogBackground"));
                this.rect.set(((BottomSheet) AudioPlayerAlert.this).backgroundPaddingLeft, ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop + i, getMeasuredWidth() - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingLeft, ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop + i + AndroidUtilities.dp(24.0f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
            }
            if (f != 0.0f) {
                int dp4 = AndroidUtilities.dp(36.0f);
                this.rect.set((getMeasuredWidth() - dp4) / 2, dp2, (getMeasuredWidth() + dp4) / 2, dp2 + AndroidUtilities.dp(4.0f));
                int themedColor = AudioPlayerAlert.this.getThemedColor("key_sheet_scrollUp");
                int alpha = Color.alpha(themedColor);
                Theme.dialogs_onlineCirclePaint.setColor(themedColor);
                Theme.dialogs_onlineCirclePaint.setAlpha((int) (alpha * 1.0f * f));
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
            }
            int themedColor2 = AudioPlayerAlert.this.getThemedColor("dialogBackground");
            Theme.dialogs_onlineCirclePaint.setColor(Color.argb((int) (AudioPlayerAlert.this.actionBar.getAlpha() * 255.0f), (int) (Color.red(themedColor2) * 0.8f), (int) (Color.green(themedColor2) * 0.8f), (int) (Color.blue(themedColor2) * 0.8f)));
            canvas.drawRect(((BottomSheet) AudioPlayerAlert.this).backgroundPaddingLeft, 0.0f, getMeasuredWidth() - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, Theme.dialogs_onlineCirclePaint);
        }

        /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$2$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 implements Bulletin.Delegate {
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onHide(Bulletin bulletin) {
                Bulletin.Delegate.CC.$default$onHide(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onOffsetChange(float f) {
                Bulletin.Delegate.CC.$default$onOffsetChange(this, f);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onShow(Bulletin bulletin) {
                Bulletin.Delegate.CC.$default$onShow(this, bulletin);
            }

            AnonymousClass1() {
                AnonymousClass2.this = r1;
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getBottomOffset(int i) {
                return AudioPlayerAlert.this.playerLayout.getHeight();
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            Bulletin.addDelegate(this, new AnonymousClass1());
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            Bulletin.removeDelegate(this);
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends ActionBar {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            AudioPlayerAlert.this = r1;
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
            ((BottomSheet) AudioPlayerAlert.this).containerView.invalidate();
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends ActionBarMenuItem.ActionBarMenuItemSearchListener {
        AnonymousClass4() {
            AudioPlayerAlert.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchCollapse() {
            if (AudioPlayerAlert.this.searching) {
                AudioPlayerAlert.this.searchWas = false;
                AudioPlayerAlert.this.searching = false;
                AudioPlayerAlert.this.setAllowNestedScroll(true);
                AudioPlayerAlert.this.listAdapter.search(null);
            }
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onSearchExpand() {
            AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
            audioPlayerAlert.searchOpenPosition = audioPlayerAlert.layoutManager.findLastVisibleItemPosition();
            View findViewByPosition = AudioPlayerAlert.this.layoutManager.findViewByPosition(AudioPlayerAlert.this.searchOpenPosition);
            AudioPlayerAlert.this.searchOpenOffset = findViewByPosition == null ? 0 : findViewByPosition.getTop();
            AudioPlayerAlert.this.searching = true;
            AudioPlayerAlert.this.setAllowNestedScroll(false);
            AudioPlayerAlert.this.listAdapter.notifyDataSetChanged();
        }

        @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
        public void onTextChanged(EditText editText) {
            if (editText.length() > 0) {
                AudioPlayerAlert.this.listAdapter.search(editText.getText().toString());
                return;
            }
            AudioPlayerAlert.this.searchWas = false;
            AudioPlayerAlert.this.listAdapter.search(null);
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass5() {
            AudioPlayerAlert.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i != -1) {
                AudioPlayerAlert.this.onSubItemClick(i);
            } else {
                AudioPlayerAlert.this.dismiss();
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass6(Context context) {
            super(context);
            AudioPlayerAlert.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (AudioPlayerAlert.this.playbackSpeedButton == null || AudioPlayerAlert.this.durationTextView == null) {
                return;
            }
            int left = (AudioPlayerAlert.this.durationTextView.getLeft() - AndroidUtilities.dp(4.0f)) - AudioPlayerAlert.this.playbackSpeedButton.getMeasuredWidth();
            AudioPlayerAlert.this.playbackSpeedButton.layout(left, AudioPlayerAlert.this.playbackSpeedButton.getTop(), AudioPlayerAlert.this.playbackSpeedButton.getMeasuredWidth() + left, AudioPlayerAlert.this.playbackSpeedButton.getBottom());
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends CoverContainer {
        private long pressTime;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass7(Context context) {
            super(context);
            AudioPlayerAlert.this = r1;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                if (getImageReceiver().hasBitmapImage()) {
                    AudioPlayerAlert.this.showAlbumCover(true, true);
                    this.pressTime = SystemClock.elapsedRealtime();
                }
            } else if (action != 2 && SystemClock.elapsedRealtime() - this.pressTime >= 400) {
                AudioPlayerAlert.this.showAlbumCover(false, true);
            }
            return true;
        }

        @Override // org.telegram.ui.Components.AudioPlayerAlert.CoverContainer
        protected void onImageUpdated(ImageReceiver imageReceiver) {
            if (AudioPlayerAlert.this.blurredView.getTag() != null) {
                AudioPlayerAlert.this.bigAlbumConver.setImageBitmap(imageReceiver.getBitmap());
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends ClippingTextViewSwitcher {
        final /* synthetic */ Context val$context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass8(Context context, Context context2) {
            super(context);
            AudioPlayerAlert.this = r1;
            this.val$context = context2;
        }

        @Override // org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher
        protected TextView createTextView() {
            TextView textView = new TextView(this.val$context);
            textView.setTextColor(AudioPlayerAlert.this.getThemedColor("player_actionBarTitle"));
            textView.setTextSize(1, 17.0f);
            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setSingleLine(true);
            return textView;
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends ClippingTextViewSwitcher {
        final /* synthetic */ Context val$context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(Context context, Context context2) {
            super(context);
            AudioPlayerAlert.this = r1;
            this.val$context = context2;
        }

        @Override // org.telegram.ui.Components.AudioPlayerAlert.ClippingTextViewSwitcher
        protected TextView createTextView() {
            TextView textView = new TextView(this.val$context);
            textView.setTextColor(AudioPlayerAlert.this.getThemedColor("player_time"));
            textView.setTextSize(1, 13.0f);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setSingleLine(true);
            textView.setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), AndroidUtilities.dp(1.0f));
            textView.setBackground(Theme.createRadSelectorDrawable(AudioPlayerAlert.this.getThemedColor("listSelectorSDK21"), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f)));
            textView.setOnClickListener(new AudioPlayerAlert$9$$ExternalSyntheticLambda0(this, textView));
            return textView;
        }

        public /* synthetic */ void lambda$createTextView$0(TextView textView, View view) {
            if (MessagesController.getInstance(((BottomSheet) AudioPlayerAlert.this).currentAccount).getTotalDialogsCount() <= 10 || TextUtils.isEmpty(textView.getText().toString())) {
                return;
            }
            String charSequence = textView.getText().toString();
            if (AudioPlayerAlert.this.parentActivity.getActionBarLayout().getLastFragment() instanceof DialogsActivity) {
                DialogsActivity dialogsActivity = (DialogsActivity) AudioPlayerAlert.this.parentActivity.getActionBarLayout().getLastFragment();
                if (!dialogsActivity.onlyDialogsAdapter()) {
                    dialogsActivity.setShowSearch(charSequence, 3);
                    AudioPlayerAlert.this.dismiss();
                    return;
                }
            }
            DialogsActivity dialogsActivity2 = new DialogsActivity(null);
            dialogsActivity2.setSearchString(charSequence);
            dialogsActivity2.setInitialSearchType(3);
            AudioPlayerAlert.this.parentActivity.presentFragment(dialogsActivity2, false, false);
            AudioPlayerAlert.this.dismiss();
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends SeekBarView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass10(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            AudioPlayerAlert.this = r1;
        }

        @Override // org.telegram.ui.Components.SeekBarView
        public boolean onTouch(MotionEvent motionEvent) {
            if (AudioPlayerAlert.this.rewindingState != 0) {
                return false;
            }
            return super.onTouch(motionEvent);
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 implements SeekBarView.SeekBarViewDelegate {
        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public /* synthetic */ int getStepsCount() {
            return SeekBarView.SeekBarViewDelegate.CC.$default$getStepsCount(this);
        }

        AnonymousClass11() {
            AudioPlayerAlert.this = r1;
        }

        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public void onSeekBarDrag(boolean z, float f) {
            if (z) {
                MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), f);
            }
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject == null || !playingMessageObject.isMusic()) {
                return;
            }
            AudioPlayerAlert.this.updateProgress(playingMessageObject);
        }

        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public void onSeekBarPressed(boolean z) {
            AudioPlayerAlert.this.draggingSeekBar = z;
        }

        @Override // org.telegram.ui.Components.SeekBarView.SeekBarViewDelegate
        public CharSequence getContentDescription() {
            return LocaleController.formatString("AccDescrPlayerDuration", 2131624042, LocaleController.formatPluralString("Minutes", AudioPlayerAlert.this.lastTime / 60, new Object[0]) + ' ' + LocaleController.formatPluralString("Seconds", AudioPlayerAlert.this.lastTime % 60, new Object[0]), LocaleController.formatPluralString("Minutes", AudioPlayerAlert.this.lastDuration / 60, new Object[0]) + ' ' + LocaleController.formatPluralString("Seconds", AudioPlayerAlert.this.lastDuration % 60, new Object[0]));
        }
    }

    public /* synthetic */ void lambda$new$0(int i) {
        MediaController.getInstance().getPlaybackSpeed(true);
        if (i == 1) {
            MediaController.getInstance().setPlaybackSpeed(true, 0.5f);
        } else if (i == 2) {
            MediaController.getInstance().setPlaybackSpeed(true, 1.0f);
        } else if (i == 3) {
            MediaController.getInstance().setPlaybackSpeed(true, 1.5f);
        } else {
            MediaController.getInstance().setPlaybackSpeed(true, 1.8f);
        }
        updatePlaybackButton();
    }

    public /* synthetic */ void lambda$new$1(View view) {
        if (Math.abs(MediaController.getInstance().getPlaybackSpeed(true) - 1.0f) > 0.001f) {
            MediaController.getInstance().setPlaybackSpeed(true, 1.0f);
        } else {
            MediaController.getInstance().setPlaybackSpeed(true, MediaController.getInstance().getFastPlaybackSpeed(true));
        }
        updatePlaybackButton();
    }

    public /* synthetic */ boolean lambda$new$2(View view) {
        this.playbackSpeedButton.toggleSubMenu();
        return true;
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(Context context) {
            super(context);
            AudioPlayerAlert.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int dp = ((i3 - i) - AndroidUtilities.dp(248.0f)) / 4;
            for (int i5 = 0; i5 < 5; i5++) {
                int dp2 = AndroidUtilities.dp((i5 * 48) + 4) + (dp * i5);
                int dp3 = AndroidUtilities.dp(9.0f);
                AudioPlayerAlert.this.buttons[i5].layout(dp2, dp3, AudioPlayerAlert.this.buttons[i5].getMeasuredWidth() + dp2, AudioPlayerAlert.this.buttons[i5].getMeasuredHeight() + dp3);
            }
        }
    }

    public /* synthetic */ void lambda$new$3(View view) {
        updateSubMenu();
        this.repeatButton.toggleSubMenu();
    }

    public /* synthetic */ void lambda$new$4(int i) {
        if (i == 1 || i == 2) {
            boolean z = SharedConfig.playOrderReversed;
            if ((z && i == 1) || (SharedConfig.shuffleMusic && i == 2)) {
                MediaController.getInstance().setPlaybackOrderType(0);
            } else {
                MediaController.getInstance().setPlaybackOrderType(i);
            }
            this.listAdapter.notifyDataSetChanged();
            if (z != SharedConfig.playOrderReversed) {
                this.listView.stopScroll();
                scrollToCurrentSong(false);
            }
        } else if (i == 4) {
            if (SharedConfig.repeatMode == 1) {
                SharedConfig.setRepeatMode(0);
            } else {
                SharedConfig.setRepeatMode(1);
            }
        } else if (SharedConfig.repeatMode == 2) {
            SharedConfig.setRepeatMode(0);
        } else {
            SharedConfig.setRepeatMode(2);
        }
        updateRepeatButton();
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$13 */
    /* loaded from: classes3.dex */
    public class AnonymousClass13 extends RLottieImageView {
        long lastTime;
        long lastUpdateTime;
        long startTime;
        float startX;
        float startY;
        final /* synthetic */ float val$touchSlop;
        int pressedCount = 0;
        private final Runnable pressedRunnable = new AnonymousClass1();
        private final Runnable backSeek = new AnonymousClass2();

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass13(Context context, float f) {
            super(context);
            AudioPlayerAlert.this = r1;
            this.val$touchSlop = f;
        }

        /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$13$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
                AnonymousClass13.this = r1;
            }

            @Override // java.lang.Runnable
            public void run() {
                AnonymousClass13 anonymousClass13 = AnonymousClass13.this;
                int i = anonymousClass13.pressedCount + 1;
                anonymousClass13.pressedCount = i;
                if (i != 1) {
                    if (i != 2) {
                        return;
                    }
                    AndroidUtilities.runOnUIThread(this, 2000L);
                    return;
                }
                AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                audioPlayerAlert.rewindingState = -1;
                audioPlayerAlert.rewindingProgress = MediaController.getInstance().getPlayingMessageObject().audioProgress;
                AnonymousClass13.this.lastTime = System.currentTimeMillis();
                AndroidUtilities.runOnUIThread(this, 2000L);
                AndroidUtilities.runOnUIThread(AnonymousClass13.this.backSeek);
            }
        }

        /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$13$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 implements Runnable {
            AnonymousClass2() {
                AnonymousClass13.this = r1;
            }

            @Override // java.lang.Runnable
            public void run() {
                long duration = MediaController.getInstance().getDuration();
                if (duration == 0 || duration == -9223372036854775807L) {
                    AnonymousClass13.this.lastTime = System.currentTimeMillis();
                    return;
                }
                float f = AudioPlayerAlert.this.rewindingProgress;
                long currentTimeMillis = System.currentTimeMillis();
                AnonymousClass13 anonymousClass13 = AnonymousClass13.this;
                long j = currentTimeMillis - anonymousClass13.lastTime;
                anonymousClass13.lastTime = currentTimeMillis;
                long j2 = currentTimeMillis - anonymousClass13.lastUpdateTime;
                int i = anonymousClass13.pressedCount;
                float f2 = (float) duration;
                float f3 = ((f * f2) - ((float) (j * (i == 1 ? 3L : i == 2 ? 6L : 12L)))) / f2;
                if (f3 < 0.0f) {
                    f3 = 0.0f;
                }
                AudioPlayerAlert.this.rewindingProgress = f3;
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                if (playingMessageObject != null && playingMessageObject.isMusic()) {
                    AudioPlayerAlert.this.updateProgress(playingMessageObject);
                }
                AnonymousClass13 anonymousClass132 = AnonymousClass13.this;
                AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                if (audioPlayerAlert.rewindingState != -1 || anonymousClass132.pressedCount <= 0) {
                    return;
                }
                if (j2 > 200 || audioPlayerAlert.rewindingProgress == 0.0f) {
                    anonymousClass132.lastUpdateTime = currentTimeMillis;
                    if (audioPlayerAlert.rewindingProgress == 0.0f) {
                        MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), 0.0f);
                        MediaController.getInstance().pauseByRewind();
                    } else {
                        MediaController.getInstance().seekToProgress(MediaController.getInstance().getPlayingMessageObject(), f3);
                    }
                }
                AnonymousClass13 anonymousClass133 = AnonymousClass13.this;
                if (anonymousClass133.pressedCount <= 0 || AudioPlayerAlert.this.rewindingProgress <= 0.0f) {
                    return;
                }
                AndroidUtilities.runOnUIThread(anonymousClass133.backSeek, 16L);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x002c, code lost:
            if (r4 != 3) goto L35;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (AudioPlayerAlert.this.seekBarView.isDragging() || AudioPlayerAlert.this.rewindingState == 1) {
                return false;
            }
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            int action = motionEvent.getAction();
            if (action == 0) {
                this.startX = rawX;
                this.startY = rawY;
                this.startTime = System.currentTimeMillis();
                AudioPlayerAlert.this.rewindingState = 0;
                AndroidUtilities.runOnUIThread(this.pressedRunnable, 300L);
                if (Build.VERSION.SDK_INT >= 21 && getBackground() != null) {
                    getBackground().setHotspot(this.startX, this.startY);
                }
                setPressed(true);
            } else {
                if (action != 1) {
                    if (action == 2) {
                        float f = rawX - this.startX;
                        float f2 = rawY - this.startY;
                        float f3 = this.val$touchSlop;
                        if ((f * f) + (f2 * f2) > f3 * f3 && AudioPlayerAlert.this.rewindingState == 0) {
                            AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                            setPressed(false);
                        }
                    }
                }
                AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                AndroidUtilities.cancelRunOnUIThread(this.backSeek);
                if (AudioPlayerAlert.this.rewindingState == 0 && motionEvent.getAction() == 1 && System.currentTimeMillis() - this.startTime < 300) {
                    MediaController.getInstance().playPreviousMessage();
                    AudioPlayerAlert.this.prevButton.setProgress(0.0f);
                    AudioPlayerAlert.this.prevButton.playAnimation();
                }
                if (this.pressedCount > 0) {
                    this.lastUpdateTime = 0L;
                    this.backSeek.run();
                    MediaController.getInstance().resumeByRewind();
                }
                AudioPlayerAlert.this.rewindingProgress = -1.0f;
                setPressed(false);
                AudioPlayerAlert.this.rewindingState = 0;
                this.pressedCount = 0;
            }
            return true;
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(16);
        }
    }

    public static /* synthetic */ void lambda$new$5(View view) {
        if (MediaController.getInstance().isDownloadingCurrentMessage()) {
            return;
        }
        if (MediaController.getInstance().isMessagePaused()) {
            MediaController.getInstance().playMessage(MediaController.getInstance().getPlayingMessageObject());
        } else {
            MediaController.getInstance().lambda$startAudioAgain$7(MediaController.getInstance().getPlayingMessageObject());
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$14 */
    /* loaded from: classes3.dex */
    public class AnonymousClass14 extends RLottieImageView {
        boolean pressed;
        private final Runnable pressedRunnable = new AnonymousClass1();
        float startX;
        float startY;
        final /* synthetic */ float val$touchSlop;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass14(Context context, float f) {
            super(context);
            AudioPlayerAlert.this = r1;
            this.val$touchSlop = f;
        }

        /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$14$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
                AnonymousClass14.this = r1;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (MediaController.getInstance().getPlayingMessageObject() == null) {
                    return;
                }
                AnonymousClass14 anonymousClass14 = AnonymousClass14.this;
                AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                int i = audioPlayerAlert.rewindingForwardPressedCount + 1;
                audioPlayerAlert.rewindingForwardPressedCount = i;
                if (i != 1) {
                    if (i == 2) {
                        MediaController.getInstance().setPlaybackSpeed(true, 7.0f);
                        AndroidUtilities.runOnUIThread(this, 2000L);
                        return;
                    }
                    MediaController.getInstance().setPlaybackSpeed(true, 13.0f);
                    return;
                }
                anonymousClass14.pressed = true;
                audioPlayerAlert.rewindingState = 1;
                if (MediaController.getInstance().isMessagePaused()) {
                    AudioPlayerAlert.this.startForwardRewindingSeek();
                } else {
                    AudioPlayerAlert audioPlayerAlert2 = AudioPlayerAlert.this;
                    if (audioPlayerAlert2.rewindingState == 1) {
                        AndroidUtilities.cancelRunOnUIThread(audioPlayerAlert2.forwardSeek);
                        AudioPlayerAlert.this.lastUpdateRewindingPlayerTime = 0L;
                    }
                }
                MediaController.getInstance().setPlaybackSpeed(true, 4.0f);
                AndroidUtilities.runOnUIThread(this, 2000L);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x002b, code lost:
            if (r3 != 3) goto L37;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (AudioPlayerAlert.this.seekBarView.isDragging() || AudioPlayerAlert.this.rewindingState == -1) {
                return false;
            }
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            int action = motionEvent.getAction();
            if (action == 0) {
                this.pressed = false;
                this.startX = rawX;
                this.startY = rawY;
                AndroidUtilities.runOnUIThread(this.pressedRunnable, 300L);
                if (Build.VERSION.SDK_INT >= 21 && getBackground() != null) {
                    getBackground().setHotspot(this.startX, this.startY);
                }
                setPressed(true);
            } else {
                if (action != 1) {
                    if (action == 2) {
                        float f = rawX - this.startX;
                        float f2 = rawY - this.startY;
                        float f3 = this.val$touchSlop;
                        if ((f * f) + (f2 * f2) > f3 * f3 && !this.pressed) {
                            AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                            setPressed(false);
                        }
                    }
                }
                if (!this.pressed && motionEvent.getAction() == 1 && isPressed()) {
                    MediaController.getInstance().playNextMessage();
                    AudioPlayerAlert.this.nextButton.setProgress(0.0f);
                    AudioPlayerAlert.this.nextButton.playAnimation();
                }
                AndroidUtilities.cancelRunOnUIThread(this.pressedRunnable);
                if (AudioPlayerAlert.this.rewindingForwardPressedCount > 0) {
                    MediaController.getInstance().setPlaybackSpeed(true, 1.0f);
                    if (MediaController.getInstance().isMessagePaused()) {
                        AudioPlayerAlert audioPlayerAlert = AudioPlayerAlert.this;
                        audioPlayerAlert.lastUpdateRewindingPlayerTime = 0L;
                        audioPlayerAlert.forwardSeek.run();
                    }
                }
                AudioPlayerAlert.this.rewindingState = 0;
                setPressed(false);
                AudioPlayerAlert audioPlayerAlert2 = AudioPlayerAlert.this;
                audioPlayerAlert2.rewindingForwardPressedCount = 0;
                audioPlayerAlert2.rewindingProgress = -1.0f;
            }
            return true;
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(16);
        }
    }

    public /* synthetic */ void lambda$new$6(View view) {
        this.optionsButton.toggleSubMenu();
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$15 */
    /* loaded from: classes3.dex */
    public class AnonymousClass15 extends RecyclerListView {
        boolean ignoreLayout;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass15(Context context) {
            super(context);
            AudioPlayerAlert.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (AudioPlayerAlert.this.searchOpenPosition == -1 || AudioPlayerAlert.this.actionBar.isSearchFieldVisible()) {
                if (!AudioPlayerAlert.this.scrollToSong) {
                    return;
                }
                AudioPlayerAlert.this.scrollToSong = false;
                this.ignoreLayout = true;
                if (AudioPlayerAlert.this.scrollToCurrentSong(true)) {
                    super.onLayout(false, i, i2, i3, i4);
                }
                this.ignoreLayout = false;
                return;
            }
            this.ignoreLayout = true;
            AudioPlayerAlert.this.layoutManager.scrollToPositionWithOffset(AudioPlayerAlert.this.searchOpenPosition, AudioPlayerAlert.this.searchOpenOffset - AudioPlayerAlert.this.listView.getPaddingTop());
            super.onLayout(false, i, i2, i3, i4);
            this.ignoreLayout = false;
            AudioPlayerAlert.this.searchOpenPosition = -1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView
        protected boolean allowSelectChildAtPosition(float f, float f2) {
            return f2 < AudioPlayerAlert.this.playerLayout.getY() - ((float) AudioPlayerAlert.this.listView.getTop());
        }

        @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    public static /* synthetic */ void lambda$new$8(View view, int i) {
        if (view instanceof AudioPlayerCell) {
            ((AudioPlayerCell) view).didPressedButton();
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$16 */
    /* loaded from: classes3.dex */
    public class AnonymousClass16 extends RecyclerView.OnScrollListener {
        AnonymousClass16() {
            AudioPlayerAlert.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i != 0) {
                if (i != 1) {
                    return;
                }
                AndroidUtilities.hideKeyboard(AudioPlayerAlert.this.getCurrentFocus());
                return;
            }
            if (((AudioPlayerAlert.this.scrollOffsetY - ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop) - AndroidUtilities.dp(13.0f)) + ((BottomSheet) AudioPlayerAlert.this).backgroundPaddingTop >= ActionBar.getCurrentActionBarHeight() || !AudioPlayerAlert.this.listView.canScrollVertically(1)) {
                return;
            }
            AudioPlayerAlert.this.listView.getChildAt(0);
            RecyclerListView.Holder holder = (RecyclerListView.Holder) AudioPlayerAlert.this.listView.findViewHolderForAdapterPosition(0);
            if (holder == null || holder.itemView.getTop() <= AndroidUtilities.dp(7.0f)) {
                return;
            }
            AudioPlayerAlert.this.listView.smoothScrollBy(0, holder.itemView.getTop() - AndroidUtilities.dp(7.0f));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            AudioPlayerAlert.this.updateLayout();
            AudioPlayerAlert.this.updateEmptyViewPosition();
            if (!AudioPlayerAlert.this.searchWas) {
                int findFirstVisibleItemPosition = AudioPlayerAlert.this.layoutManager.findFirstVisibleItemPosition();
                int abs = findFirstVisibleItemPosition == -1 ? 0 : Math.abs(AudioPlayerAlert.this.layoutManager.findLastVisibleItemPosition() - findFirstVisibleItemPosition) + 1;
                int itemCount = recyclerView.getAdapter().getItemCount();
                MediaController.getInstance().getPlayingMessageObject();
                if (SharedConfig.playOrderReversed) {
                    if (findFirstVisibleItemPosition >= 10) {
                        return;
                    }
                    MediaController.getInstance().loadMoreMusic();
                } else if (findFirstVisibleItemPosition + abs <= itemCount - 10) {
                } else {
                    MediaController.getInstance().loadMoreMusic();
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$17 */
    /* loaded from: classes3.dex */
    public class AnonymousClass17 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass17(Context context) {
            super(context);
            AudioPlayerAlert.this = r1;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (AudioPlayerAlert.this.blurredView.getTag() != null) {
                AudioPlayerAlert.this.showAlbumCover(false, true);
            }
            return true;
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public int getContainerViewHeight() {
        if (this.playerLayout == null) {
            return 0;
        }
        if (this.playlist.size() <= 1) {
            return this.playerLayout.getMeasuredHeight() + this.backgroundPaddingTop;
        }
        int dp = AndroidUtilities.dp(13.0f);
        int i = (this.scrollOffsetY - this.backgroundPaddingTop) - dp;
        if (this.currentSheetAnimationType == 1) {
            i = (int) (i + this.listView.getTranslationY());
        }
        if (this.backgroundPaddingTop + i < ActionBar.getCurrentActionBarHeight()) {
            float dp2 = dp + AndroidUtilities.dp(4.0f);
            i -= (int) ((ActionBar.getCurrentActionBarHeight() - dp2) * Math.min(1.0f, ((ActionBar.getCurrentActionBarHeight() - i) - this.backgroundPaddingTop) / dp2));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            i += AndroidUtilities.statusBarHeight;
        }
        return this.container.getMeasuredHeight() - i;
    }

    public void startForwardRewindingSeek() {
        if (this.rewindingState == 1) {
            this.lastRewindingTime = System.currentTimeMillis();
            this.rewindingProgress = MediaController.getInstance().getPlayingMessageObject().audioProgress;
            AndroidUtilities.cancelRunOnUIThread(this.forwardSeek);
            AndroidUtilities.runOnUIThread(this.forwardSeek);
        }
    }

    public void updateEmptyViewPosition() {
        if (this.emptyView.getVisibility() != 0) {
            return;
        }
        int dp = this.playerLayout.getVisibility() == 0 ? AndroidUtilities.dp(150.0f) : -AndroidUtilities.dp(30.0f);
        LinearLayout linearLayout = this.emptyView;
        linearLayout.setTranslationY(((linearLayout.getMeasuredHeight() - this.containerView.getMeasuredHeight()) - dp) / 2);
    }

    public void updateEmptyView() {
        this.emptyView.setVisibility((!this.searching || this.listAdapter.getItemCount() != 0) ? 8 : 0);
        updateEmptyViewPosition();
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean scrollToCurrentSong(boolean z) {
        boolean z2;
        int indexOf;
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject != null) {
            if (z) {
                int childCount = this.listView.getChildCount();
                int i = 0;
                while (true) {
                    if (i >= childCount) {
                        break;
                    }
                    View childAt = this.listView.getChildAt(i);
                    if (!(childAt instanceof AudioPlayerCell) || ((AudioPlayerCell) childAt).getMessageObject() != playingMessageObject) {
                        i++;
                    } else if (childAt.getBottom() <= this.listView.getMeasuredHeight()) {
                        z2 = true;
                    }
                }
                if (!z2 && (indexOf = this.playlist.indexOf(playingMessageObject)) >= 0) {
                    if (!SharedConfig.playOrderReversed) {
                        this.layoutManager.scrollToPosition(indexOf);
                    } else {
                        this.layoutManager.scrollToPosition(this.playlist.size() - indexOf);
                    }
                    return true;
                }
            }
            z2 = false;
            if (!z2) {
                if (!SharedConfig.playOrderReversed) {
                }
                return true;
            }
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean onCustomMeasure(View view, int i, int i2) {
        FrameLayout frameLayout = this.blurredView;
        if (view == frameLayout) {
            frameLayout.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), View.MeasureSpec.makeMeasureSpec(i2, 1073741824));
            return true;
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean onCustomLayout(View view, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int i6 = i4 - i2;
        FrameLayout frameLayout = this.blurredView;
        if (view == frameLayout) {
            frameLayout.layout(i, 0, i5 + i, i6);
            return true;
        }
        return false;
    }

    private void setMenuItemChecked(ActionBarMenuSubItem actionBarMenuSubItem, boolean z) {
        if (z) {
            actionBarMenuSubItem.setTextColor(getThemedColor("player_buttonActive"));
            actionBarMenuSubItem.setIconColor(getThemedColor("player_buttonActive"));
            return;
        }
        actionBarMenuSubItem.setTextColor(getThemedColor("actionBarDefaultSubmenuItem"));
        actionBarMenuSubItem.setIconColor(getThemedColor("actionBarDefaultSubmenuItem"));
    }

    private void updateSubMenu() {
        setMenuItemChecked(this.shuffleListItem, SharedConfig.shuffleMusic);
        setMenuItemChecked(this.reverseOrderItem, SharedConfig.playOrderReversed);
        boolean z = false;
        setMenuItemChecked(this.repeatListItem, SharedConfig.repeatMode == 1);
        ActionBarMenuSubItem actionBarMenuSubItem = this.repeatSongItem;
        if (SharedConfig.repeatMode == 2) {
            z = true;
        }
        setMenuItemChecked(actionBarMenuSubItem, z);
    }

    private void updatePlaybackButton() {
        float playbackSpeed = MediaController.getInstance().getPlaybackSpeed(true);
        float f = playbackSpeed - 1.0f;
        String str = Math.abs(f) > 0.001f ? "inappPlayerPlayPause" : "inappPlayerClose";
        this.playbackSpeedButton.setTag(str);
        float fastPlaybackSpeed = MediaController.getInstance().getFastPlaybackSpeed(true);
        if (Math.abs(fastPlaybackSpeed - 1.8f) < 0.001f) {
            this.playbackSpeedButton.setIcon(2131166210);
        } else if (Math.abs(fastPlaybackSpeed - 1.5f) < 0.001f) {
            this.playbackSpeedButton.setIcon(2131166209);
        } else {
            this.playbackSpeedButton.setIcon(2131166208);
        }
        this.playbackSpeedButton.setIconColor(getThemedColor(str));
        if (Build.VERSION.SDK_INT >= 21) {
            this.playbackSpeedButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(str) & 436207615, 1, AndroidUtilities.dp(14.0f)));
        }
        for (int i = 0; i < this.speedItems.length; i++) {
            if ((i == 0 && Math.abs(playbackSpeed - 0.5f) < 0.001f) || ((i == 1 && Math.abs(f) < 0.001f) || ((i == 2 && Math.abs(playbackSpeed - 1.5f) < 0.001f) || (i == 3 && Math.abs(playbackSpeed - 1.8f) < 0.001f)))) {
                this.speedItems[i].setColors(getThemedColor("inappPlayerPlayPause"), getThemedColor("inappPlayerPlayPause"));
            } else {
                this.speedItems[i].setColors(getThemedColor("actionBarDefaultSubmenuItem"), getThemedColor("actionBarDefaultSubmenuItemIcon"));
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0066, code lost:
        if (r12.exists() == false) goto L18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onSubItemClick(int i) {
        LaunchActivity launchActivity;
        File file;
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if (playingMessageObject == null || (launchActivity = this.parentActivity) == null) {
            return;
        }
        if (i == 1) {
            int i2 = UserConfig.selectedAccount;
            int i3 = this.currentAccount;
            if (i2 != i3) {
                launchActivity.switchToAccount(i3, true);
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean("onlySelect", true);
            bundle.putInt("dialogsType", 3);
            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            ArrayList arrayList = new ArrayList();
            arrayList.add(playingMessageObject);
            dialogsActivity.setDelegate(new AudioPlayerAlert$$ExternalSyntheticLambda12(this, arrayList));
            this.parentActivity.lambda$runLinkRequest$59(dialogsActivity);
            dismiss();
            return;
        }
        String str = null;
        if (i == 2) {
            try {
                if (!TextUtils.isEmpty(playingMessageObject.messageOwner.attachPath)) {
                    file = new File(playingMessageObject.messageOwner.attachPath);
                }
                file = null;
                if (file == null) {
                    file = FileLoader.getInstance(this.currentAccount).getPathToMessage(playingMessageObject.messageOwner);
                }
                if (file.exists()) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType(playingMessageObject.getMimeType());
                    if (Build.VERSION.SDK_INT >= 24) {
                        try {
                            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(ApplicationLoader.applicationContext, "org.telegram.messenger.beta.provider", file));
                            intent.setFlags(1);
                        } catch (Exception unused) {
                            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                        }
                    } else {
                        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                    }
                    this.parentActivity.startActivityForResult(Intent.createChooser(intent, LocaleController.getString("ShareFile", 2131628274)), 500);
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this.parentActivity);
                builder.setTitle(LocaleController.getString("AppName", 2131624375));
                builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
                builder.setMessage(LocaleController.getString("PleaseDownload", 2131627559));
                builder.show();
            } catch (Exception e) {
                FileLog.e(e);
            }
        } else if (i != 4) {
            if (i != 5) {
                return;
            }
            int i4 = Build.VERSION.SDK_INT;
            if (i4 >= 23 && ((i4 <= 28 || BuildVars.NO_SCOPED_STORAGE) && launchActivity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0)) {
                this.parentActivity.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                return;
            }
            String documentFileName = FileLoader.getDocumentFileName(playingMessageObject.getDocument());
            if (TextUtils.isEmpty(documentFileName)) {
                documentFileName = playingMessageObject.getFileName();
            }
            String str2 = documentFileName;
            String str3 = playingMessageObject.messageOwner.attachPath;
            if (str3 == null || str3.length() <= 0 || new File(str3).exists()) {
                str = str3;
            }
            MediaController.saveFile((str == null || str.length() == 0) ? FileLoader.getInstance(this.currentAccount).getPathToMessage(playingMessageObject.messageOwner).toString() : str, this.parentActivity, 3, str2, playingMessageObject.getDocument() != null ? playingMessageObject.getDocument().mime_type : "", new AudioPlayerAlert$$ExternalSyntheticLambda6(this));
        } else {
            int i5 = UserConfig.selectedAccount;
            int i6 = this.currentAccount;
            if (i5 != i6) {
                launchActivity.switchToAccount(i6, true);
            }
            Bundle bundle2 = new Bundle();
            long dialogId = playingMessageObject.getDialogId();
            if (DialogObject.isEncryptedDialog(dialogId)) {
                bundle2.putInt("enc_id", DialogObject.getEncryptedChatId(dialogId));
            } else if (DialogObject.isUserDialog(dialogId)) {
                bundle2.putLong("user_id", dialogId);
            } else {
                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-dialogId));
                if (chat != null && chat.migrated_to != null) {
                    bundle2.putLong("migrated_to", dialogId);
                    dialogId = -chat.migrated_to.channel_id;
                }
                bundle2.putLong("chat_id", -dialogId);
            }
            bundle2.putInt("message_id", playingMessageObject.getId());
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
            this.parentActivity.presentFragment(new ChatActivity(bundle2), false, false);
            dismiss();
        }
    }

    public /* synthetic */ void lambda$onSubItemClick$9(ArrayList arrayList, DialogsActivity dialogsActivity, ArrayList arrayList2, CharSequence charSequence, boolean z) {
        if (arrayList2.size() > 1 || ((Long) arrayList2.get(0)).longValue() == UserConfig.getInstance(this.currentAccount).getClientUserId() || charSequence != null) {
            for (int i = 0; i < arrayList2.size(); i++) {
                long longValue = ((Long) arrayList2.get(i)).longValue();
                if (charSequence != null) {
                    SendMessagesHelper.getInstance(this.currentAccount).sendMessage(charSequence.toString(), longValue, null, null, null, true, null, null, null, true, 0, null);
                }
                SendMessagesHelper.getInstance(this.currentAccount).sendMessage((ArrayList<MessageObject>) arrayList, longValue, false, false, true, 0);
            }
            dialogsActivity.finishFragment();
            return;
        }
        long longValue2 = ((Long) arrayList2.get(0)).longValue();
        Bundle bundle = new Bundle();
        bundle.putBoolean("scrollToTopOnResume", true);
        if (DialogObject.isEncryptedDialog(longValue2)) {
            bundle.putInt("enc_id", DialogObject.getEncryptedChatId(longValue2));
        } else if (DialogObject.isUserDialog(longValue2)) {
            bundle.putLong("user_id", longValue2);
        } else {
            bundle.putLong("chat_id", -longValue2);
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        ChatActivity chatActivity = new ChatActivity(bundle);
        if (this.parentActivity.presentFragment(chatActivity, true, false)) {
            chatActivity.showFieldPanelForForward(true, arrayList);
        } else {
            dialogsActivity.finishFragment();
        }
    }

    public /* synthetic */ void lambda$onSubItemClick$10() {
        BulletinFactory.of((FrameLayout) this.containerView, this.resourcesProvider).createDownloadBulletin(BulletinFactory.FileType.AUDIO).show();
    }

    public void showAlbumCover(boolean z, boolean z2) {
        if (z) {
            if (this.blurredView.getVisibility() == 0 || this.blurredAnimationInProgress) {
                return;
            }
            this.blurredView.setTag(1);
            this.bigAlbumConver.setImageBitmap(this.coverContainer.getImageReceiver().getBitmap());
            this.blurredAnimationInProgress = true;
            View fragmentView = this.parentActivity.getActionBarLayout().fragmentsStack.get(this.parentActivity.getActionBarLayout().fragmentsStack.size() - 1).getFragmentView();
            int measuredWidth = (int) (fragmentView.getMeasuredWidth() / 6.0f);
            int measuredHeight = (int) (fragmentView.getMeasuredHeight() / 6.0f);
            Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.scale(0.16666667f, 0.16666667f);
            fragmentView.draw(canvas);
            canvas.translate(this.containerView.getLeft() - getLeftInset(), 0.0f);
            this.containerView.draw(canvas);
            Utilities.stackBlurBitmap(createBitmap, Math.max(7, Math.max(measuredWidth, measuredHeight) / 180));
            this.blurredView.setBackground(new BitmapDrawable(createBitmap));
            this.blurredView.setVisibility(0);
            this.blurredView.animate().alpha(1.0f).setDuration(180L).setListener(new AnonymousClass18()).start();
            this.bigAlbumConver.animate().scaleX(1.0f).scaleY(1.0f).setDuration(180L).start();
        } else if (this.blurredView.getVisibility() != 0) {
        } else {
            this.blurredView.setTag(null);
            if (z2) {
                this.blurredAnimationInProgress = true;
                this.blurredView.animate().alpha(0.0f).setDuration(180L).setListener(new AnonymousClass19()).start();
                this.bigAlbumConver.animate().scaleX(0.9f).scaleY(0.9f).setDuration(180L).start();
                return;
            }
            this.blurredView.setAlpha(0.0f);
            this.blurredView.setVisibility(4);
            this.bigAlbumConver.setImageBitmap(null);
            this.bigAlbumConver.setScaleX(0.9f);
            this.bigAlbumConver.setScaleY(0.9f);
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$18 */
    /* loaded from: classes3.dex */
    public class AnonymousClass18 extends AnimatorListenerAdapter {
        AnonymousClass18() {
            AudioPlayerAlert.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            AudioPlayerAlert.this.blurredAnimationInProgress = false;
        }
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$19 */
    /* loaded from: classes3.dex */
    public class AnonymousClass19 extends AnimatorListenerAdapter {
        AnonymousClass19() {
            AudioPlayerAlert.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            AudioPlayerAlert.this.blurredView.setVisibility(4);
            AudioPlayerAlert.this.bigAlbumConver.setImageBitmap(null);
            AudioPlayerAlert.this.blurredAnimationInProgress = false;
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        AudioPlayerCell audioPlayerCell;
        MessageObject messageObject;
        AudioPlayerCell audioPlayerCell2;
        MessageObject messageObject2;
        MessageObject playingMessageObject;
        int i3 = 0;
        if (i == NotificationCenter.messagePlayingDidStart || i == NotificationCenter.messagePlayingPlayStateChanged || i == NotificationCenter.messagePlayingDidReset) {
            int i4 = NotificationCenter.messagePlayingDidReset;
            updateTitle(i == i4 && ((Boolean) objArr[1]).booleanValue());
            if (i == i4 || i == NotificationCenter.messagePlayingPlayStateChanged) {
                int childCount = this.listView.getChildCount();
                for (int i5 = 0; i5 < childCount; i5++) {
                    View childAt = this.listView.getChildAt(i5);
                    if ((childAt instanceof AudioPlayerCell) && (messageObject = (audioPlayerCell = (AudioPlayerCell) childAt).getMessageObject()) != null && (messageObject.isVoice() || messageObject.isMusic())) {
                        audioPlayerCell.updateButtonState(false, true);
                    }
                }
                if (i != NotificationCenter.messagePlayingPlayStateChanged || MediaController.getInstance().getPlayingMessageObject() == null) {
                    return;
                }
                if (MediaController.getInstance().isMessagePaused()) {
                    startForwardRewindingSeek();
                } else if (this.rewindingState == 1 && this.rewindingProgress != -1.0f) {
                    AndroidUtilities.cancelRunOnUIThread(this.forwardSeek);
                    this.lastUpdateRewindingPlayerTime = 0L;
                    this.forwardSeek.run();
                    this.rewindingProgress = -1.0f;
                }
            } else if (((MessageObject) objArr[0]).eventId == 0) {
                int childCount2 = this.listView.getChildCount();
                for (int i6 = 0; i6 < childCount2; i6++) {
                    View childAt2 = this.listView.getChildAt(i6);
                    if ((childAt2 instanceof AudioPlayerCell) && (messageObject2 = (audioPlayerCell2 = (AudioPlayerCell) childAt2).getMessageObject()) != null && (messageObject2.isVoice() || messageObject2.isMusic())) {
                        audioPlayerCell2.updateButtonState(false, true);
                    }
                }
            }
        } else if (i == NotificationCenter.messagePlayingProgressDidChanged) {
            MessageObject playingMessageObject2 = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject2 == null || !playingMessageObject2.isMusic()) {
                return;
            }
            updateProgress(playingMessageObject2);
        } else if (i == NotificationCenter.musicDidLoad) {
            this.playlist = MediaController.getInstance().getPlaylist();
            this.listAdapter.notifyDataSetChanged();
        } else if (i == NotificationCenter.moreMusicDidLoad) {
            this.playlist = MediaController.getInstance().getPlaylist();
            this.listAdapter.notifyDataSetChanged();
            if (!SharedConfig.playOrderReversed) {
                return;
            }
            this.listView.stopScroll();
            int intValue = ((Integer) objArr[0]).intValue();
            this.layoutManager.findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = this.layoutManager.findLastVisibleItemPosition();
            if (findLastVisibleItemPosition == -1) {
                return;
            }
            View findViewByPosition = this.layoutManager.findViewByPosition(findLastVisibleItemPosition);
            if (findViewByPosition != null) {
                i3 = findViewByPosition.getTop();
            }
            this.layoutManager.scrollToPositionWithOffset(findLastVisibleItemPosition + intValue, i3);
        } else if (i == NotificationCenter.fileLoaded) {
            if (!((String) objArr[0]).equals(this.currentFile)) {
                return;
            }
            updateTitle(false);
            this.currentAudioFinishedLoading = true;
        } else if (i == NotificationCenter.fileLoadProgressChanged && ((String) objArr[0]).equals(this.currentFile) && (playingMessageObject = MediaController.getInstance().getPlayingMessageObject()) != null) {
            Long l = (Long) objArr[1];
            Long l2 = (Long) objArr[2];
            float f = 1.0f;
            if (!this.currentAudioFinishedLoading) {
                long elapsedRealtime = SystemClock.elapsedRealtime();
                if (Math.abs(elapsedRealtime - this.lastBufferedPositionCheck) >= 500) {
                    if (MediaController.getInstance().isStreamingCurrentAudio()) {
                        f = FileLoader.getInstance(this.currentAccount).getBufferedProgressFromPosition(playingMessageObject.audioProgress, this.currentFile);
                    }
                    this.lastBufferedPositionCheck = elapsedRealtime;
                } else {
                    f = -1.0f;
                }
            }
            if (f == -1.0f) {
                return;
            }
            this.seekBarView.setBufferedProgress(f);
        }
    }

    public void updateLayout() {
        if (this.listView.getChildCount() <= 0) {
            RecyclerListView recyclerListView = this.listView;
            int paddingTop = recyclerListView.getPaddingTop();
            this.scrollOffsetY = paddingTop;
            recyclerListView.setTopGlowOffset(paddingTop);
            this.containerView.invalidate();
            return;
        }
        View childAt = this.listView.getChildAt(0);
        RecyclerListView.Holder holder = (RecyclerListView.Holder) this.listView.findContainingViewHolder(childAt);
        int top = childAt.getTop();
        int dp = AndroidUtilities.dp(7.0f);
        if (top < AndroidUtilities.dp(7.0f) || holder == null || holder.getAdapterPosition() != 0) {
            top = dp;
        }
        boolean z = top <= AndroidUtilities.dp(12.0f);
        if ((z && this.actionBar.getTag() == null) || (!z && this.actionBar.getTag() != null)) {
            this.actionBar.setTag(z ? 1 : null);
            AnimatorSet animatorSet = this.actionBarAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.actionBarAnimation = null;
            }
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.actionBarAnimation = animatorSet2;
            animatorSet2.setDuration(180L);
            AnimatorSet animatorSet3 = this.actionBarAnimation;
            Animator[] animatorArr = new Animator[2];
            ActionBar actionBar = this.actionBar;
            Property property = View.ALPHA;
            float[] fArr = new float[1];
            float f = 1.0f;
            fArr[0] = z ? 1.0f : 0.0f;
            animatorArr[0] = ObjectAnimator.ofFloat(actionBar, property, fArr);
            View view = this.actionBarShadow;
            Property property2 = View.ALPHA;
            float[] fArr2 = new float[1];
            if (!z) {
                f = 0.0f;
            }
            fArr2[0] = f;
            animatorArr[1] = ObjectAnimator.ofFloat(view, property2, fArr2);
            animatorSet3.playTogether(animatorArr);
            this.actionBarAnimation.addListener(new AnonymousClass20());
            this.actionBarAnimation.start();
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.listView.getLayoutParams();
        int dp2 = top + (layoutParams.topMargin - AndroidUtilities.dp(11.0f));
        if (this.scrollOffsetY == dp2) {
            return;
        }
        RecyclerListView recyclerListView2 = this.listView;
        this.scrollOffsetY = dp2;
        recyclerListView2.setTopGlowOffset(dp2 - layoutParams.topMargin);
        this.containerView.invalidate();
    }

    /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$20 */
    /* loaded from: classes3.dex */
    public class AnonymousClass20 extends AnimatorListenerAdapter {
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
        }

        AnonymousClass20() {
            AudioPlayerAlert.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            AudioPlayerAlert.this.actionBarAnimation = null;
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStart);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.fileLoadProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.musicDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.moreMusicDidLoad);
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.isSearchFieldVisible()) {
            this.actionBar.closeSearchField();
        } else if (this.blurredView.getTag() != null) {
            showAlbumCover(false, true);
        } else {
            super.onBackPressed();
        }
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public void onProgressDownload(String str, long j, long j2) {
        this.progressView.setProgress(Math.min(1.0f, ((float) j) / ((float) j2)), true);
    }

    @Override // org.telegram.messenger.DownloadController.FileDownloadProgressListener
    public int getObserverTag() {
        return this.TAG;
    }

    private void updateRepeatButton() {
        int i = SharedConfig.repeatMode;
        if (i != 0 && i != 1) {
            if (i != 2) {
                return;
            }
            this.repeatButton.setIcon(2131166075);
            this.repeatButton.setTag("player_buttonActive");
            this.repeatButton.setIconColor(getThemedColor("player_buttonActive"));
            Theme.setSelectorDrawableColor(this.repeatButton.getBackground(), 436207615 & getThemedColor("player_buttonActive"), true);
            this.repeatButton.setContentDescription(LocaleController.getString("AccDescrRepeatOne", 2131624055));
            return;
        }
        if (SharedConfig.shuffleMusic) {
            if (i == 0) {
                this.repeatButton.setIcon(2131166076);
            } else {
                this.repeatButton.setIcon(2131166073);
            }
        } else if (!SharedConfig.playOrderReversed) {
            this.repeatButton.setIcon(2131166074);
        } else if (i == 0) {
            this.repeatButton.setIcon(2131166068);
        } else {
            this.repeatButton.setIcon(2131166072);
        }
        if (i == 0 && !SharedConfig.shuffleMusic && !SharedConfig.playOrderReversed) {
            this.repeatButton.setTag("player_button");
            this.repeatButton.setIconColor(getThemedColor("player_button"));
            Theme.setSelectorDrawableColor(this.repeatButton.getBackground(), getThemedColor("listSelectorSDK21"), true);
            this.repeatButton.setContentDescription(LocaleController.getString("AccDescrRepeatOff", 2131624054));
            return;
        }
        this.repeatButton.setTag("player_buttonActive");
        this.repeatButton.setIconColor(getThemedColor("player_buttonActive"));
        Theme.setSelectorDrawableColor(this.repeatButton.getBackground(), 436207615 & getThemedColor("player_buttonActive"), true);
        if (i == 0) {
            if (SharedConfig.shuffleMusic) {
                this.repeatButton.setContentDescription(LocaleController.getString("ShuffleList", 2131628354));
                return;
            } else {
                this.repeatButton.setContentDescription(LocaleController.getString("ReverseOrder", 2131628039));
                return;
            }
        }
        this.repeatButton.setContentDescription(LocaleController.getString("AccDescrRepeatList", 2131624053));
    }

    public void updateProgress(MessageObject messageObject) {
        updateProgress(messageObject, false);
    }

    private void updateProgress(MessageObject messageObject, boolean z) {
        int i;
        int i2;
        SeekBarView seekBarView = this.seekBarView;
        if (seekBarView != null) {
            if (seekBarView.isDragging()) {
                i = (int) (messageObject.getDuration() * this.seekBarView.getProgress());
            } else {
                boolean z2 = true;
                if (this.rewindingProgress < 0.0f || ((i2 = this.rewindingState) != -1 && (i2 != 1 || !MediaController.getInstance().isMessagePaused()))) {
                    z2 = false;
                }
                if (z2) {
                    this.seekBarView.setProgress(this.rewindingProgress, z);
                } else {
                    this.seekBarView.setProgress(messageObject.audioProgress, z);
                }
                float f = 1.0f;
                if (!this.currentAudioFinishedLoading) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    if (Math.abs(elapsedRealtime - this.lastBufferedPositionCheck) >= 500) {
                        if (MediaController.getInstance().isStreamingCurrentAudio()) {
                            f = FileLoader.getInstance(this.currentAccount).getBufferedProgressFromPosition(messageObject.audioProgress, this.currentFile);
                        }
                        this.lastBufferedPositionCheck = elapsedRealtime;
                    } else {
                        f = -1.0f;
                    }
                }
                if (f != -1.0f) {
                    this.seekBarView.setBufferedProgress(f);
                }
                if (z2) {
                    int duration = (int) (messageObject.getDuration() * this.seekBarView.getProgress());
                    messageObject.audioProgressSec = duration;
                    i = duration;
                } else {
                    i = messageObject.audioProgressSec;
                }
            }
            if (this.lastTime == i) {
                return;
            }
            this.lastTime = i;
            this.timeTextView.setText(AndroidUtilities.formatShortDuration(i));
        }
    }

    private void checkIfMusicDownloaded(MessageObject messageObject) {
        String str = messageObject.messageOwner.attachPath;
        File file = null;
        if (str != null && str.length() > 0) {
            File file2 = new File(messageObject.messageOwner.attachPath);
            if (file2.exists()) {
                file = file2;
            }
        }
        if (file == null) {
            file = FileLoader.getInstance(this.currentAccount).getPathToMessage(messageObject.messageOwner);
        }
        boolean z = SharedConfig.streamMedia && ((int) messageObject.getDialogId()) != 0 && messageObject.isMusic();
        if (!file.exists() && !z) {
            String fileName = messageObject.getFileName();
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this);
            Float fileProgress = ImageLoader.getInstance().getFileProgress(fileName);
            this.progressView.setProgress(fileProgress != null ? fileProgress.floatValue() : 0.0f, false);
            this.progressView.setVisibility(0);
            this.seekBarView.setVisibility(4);
            this.playButton.setEnabled(false);
            return;
        }
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
        this.progressView.setVisibility(4);
        this.seekBarView.setVisibility(0);
        this.playButton.setEnabled(true);
    }

    private void updateTitle(boolean z) {
        MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
        if ((playingMessageObject == null && z) || (playingMessageObject != null && !playingMessageObject.isMusic())) {
            dismiss();
        } else if (playingMessageObject == null) {
            this.lastMessageObject = null;
        } else {
            boolean z2 = playingMessageObject == this.lastMessageObject;
            this.lastMessageObject = playingMessageObject;
            if (playingMessageObject.eventId != 0 || playingMessageObject.getId() <= -2000000000) {
                this.optionsButton.setVisibility(4);
            } else {
                this.optionsButton.setVisibility(0);
            }
            if (MessagesController.getInstance(this.currentAccount).isChatNoForwards(playingMessageObject.getChatId())) {
                this.optionsButton.hideSubItem(1);
                this.optionsButton.hideSubItem(2);
                this.optionsButton.hideSubItem(5);
                this.optionsButton.setAdditionalYOffset(-AndroidUtilities.dp(16.0f));
            } else {
                this.optionsButton.showSubItem(1);
                this.optionsButton.showSubItem(2);
                this.optionsButton.showSubItem(5);
                this.optionsButton.setAdditionalYOffset(-AndroidUtilities.dp(157.0f));
            }
            checkIfMusicDownloaded(playingMessageObject);
            updateProgress(playingMessageObject, !z2);
            updateCover(playingMessageObject, !z2);
            if (MediaController.getInstance().isMessagePaused()) {
                this.playPauseDrawable.setPause(false);
                this.playButton.setContentDescription(LocaleController.getString("AccActionPlay", 2131623955));
            } else {
                this.playPauseDrawable.setPause(true);
                this.playButton.setContentDescription(LocaleController.getString("AccActionPause", 2131623954));
            }
            String musicTitle = playingMessageObject.getMusicTitle();
            String musicAuthor = playingMessageObject.getMusicAuthor();
            this.titleTextView.setText(musicTitle);
            this.authorTextView.setText(musicAuthor);
            int duration = playingMessageObject.getDuration();
            this.lastDuration = duration;
            TextView textView = this.durationTextView;
            if (textView != null) {
                textView.setText(duration != 0 ? AndroidUtilities.formatShortDuration(duration) : "-:--");
            }
            if (duration > 600) {
                this.playbackSpeedButton.setVisibility(0);
            } else {
                this.playbackSpeedButton.setVisibility(8);
            }
            if (z2) {
                return;
            }
            preloadNeighboringThumbs();
        }
    }

    private void updateCover(MessageObject messageObject, boolean z) {
        CoverContainer coverContainer = this.coverContainer;
        BackupImageView nextImageView = z ? coverContainer.getNextImageView() : coverContainer.getImageView();
        AudioInfo audioInfo = MediaController.getInstance().getAudioInfo();
        if (audioInfo != null && audioInfo.getCover() != null) {
            nextImageView.setImageBitmap(audioInfo.getCover());
            this.currentFile = null;
            this.currentAudioFinishedLoading = true;
        } else {
            this.currentFile = FileLoader.getAttachFileName(messageObject.getDocument());
            this.currentAudioFinishedLoading = false;
            String artworkUrl = messageObject.getArtworkUrl(false);
            ImageLocation artworkThumbImageLocation = getArtworkThumbImageLocation(messageObject);
            if (!TextUtils.isEmpty(artworkUrl)) {
                nextImageView.setImage(ImageLocation.getForPath(artworkUrl), null, artworkThumbImageLocation, null, null, 0L, 1, messageObject);
            } else if (artworkThumbImageLocation != null) {
                nextImageView.setImage(null, null, artworkThumbImageLocation, null, null, 0L, 1, messageObject);
            } else {
                nextImageView.setImageDrawable(null);
            }
            nextImageView.invalidate();
        }
        if (z) {
            this.coverContainer.switchImageViews();
        }
    }

    private ImageLocation getArtworkThumbImageLocation(MessageObject messageObject) {
        TLRPC$Document document = messageObject.getDocument();
        TLRPC$PhotoSize closestPhotoSizeWithSize = document != null ? FileLoader.getClosestPhotoSizeWithSize(document.thumbs, 360) : null;
        if (!(closestPhotoSizeWithSize instanceof TLRPC$TL_photoSize) && !(closestPhotoSizeWithSize instanceof TLRPC$TL_photoSizeProgressive)) {
            closestPhotoSizeWithSize = null;
        }
        if (closestPhotoSizeWithSize != null) {
            return ImageLocation.getForDocument(closestPhotoSizeWithSize, document);
        }
        String artworkUrl = messageObject.getArtworkUrl(true);
        if (artworkUrl == null) {
            return null;
        }
        return ImageLocation.getForPath(artworkUrl);
    }

    private void preloadNeighboringThumbs() {
        MediaController mediaController = MediaController.getInstance();
        ArrayList<MessageObject> playlist = mediaController.getPlaylist();
        if (playlist.size() <= 1) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int playingMessageObjectNum = mediaController.getPlayingMessageObjectNum();
        int i = playingMessageObjectNum + 1;
        int i2 = playingMessageObjectNum - 1;
        if (i >= playlist.size()) {
            i = 0;
        }
        if (i2 <= -1) {
            i2 = playlist.size() - 1;
        }
        arrayList.add(playlist.get(i));
        if (i != i2) {
            arrayList.add(playlist.get(i2));
        }
        int size = arrayList.size();
        for (int i3 = 0; i3 < size; i3++) {
            MessageObject messageObject = (MessageObject) arrayList.get(i3);
            ImageLocation artworkThumbImageLocation = getArtworkThumbImageLocation(messageObject);
            if (artworkThumbImageLocation != null) {
                if (artworkThumbImageLocation.path != null) {
                    ImageLoader.getInstance().preloadArtwork(artworkThumbImageLocation.path);
                } else {
                    FileLoader.getInstance(this.currentAccount).loadFile(artworkThumbImageLocation, messageObject, null, 0, 1);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context context;
        private ArrayList<MessageObject> searchResult = new ArrayList<>();
        private Runnable searchRunnable;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return 0;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        public ListAdapter(Context context) {
            AudioPlayerAlert.this = r1;
            this.context = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (AudioPlayerAlert.this.playlist.size() > 1) {
                AudioPlayerAlert.this.playerLayout.setBackgroundColor(AudioPlayerAlert.this.getThemedColor("player_background"));
                AudioPlayerAlert.this.playerShadow.setVisibility(0);
                AudioPlayerAlert.this.listView.setPadding(0, AudioPlayerAlert.this.listView.getPaddingTop(), 0, AndroidUtilities.dp(179.0f));
            } else {
                AudioPlayerAlert.this.playerLayout.setBackground(null);
                AudioPlayerAlert.this.playerShadow.setVisibility(4);
                AudioPlayerAlert.this.listView.setPadding(0, AudioPlayerAlert.this.listView.getPaddingTop(), 0, 0);
            }
            AudioPlayerAlert.this.updateEmptyView();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (!AudioPlayerAlert.this.searchWas) {
                if (AudioPlayerAlert.this.playlist.size() <= 1) {
                    return 0;
                }
                return AudioPlayerAlert.this.playlist.size();
            }
            return this.searchResult.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            Context context = this.context;
            boolean currentPlaylistIsGlobalSearch = MediaController.getInstance().currentPlaylistIsGlobalSearch();
            return new RecyclerListView.Holder(new AudioPlayerCell(context, currentPlaylistIsGlobalSearch ? 1 : 0, ((BottomSheet) AudioPlayerAlert.this).resourcesProvider));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            AudioPlayerCell audioPlayerCell = (AudioPlayerCell) viewHolder.itemView;
            if (AudioPlayerAlert.this.searchWas) {
                audioPlayerCell.setMessageObject(this.searchResult.get(i));
            } else if (SharedConfig.playOrderReversed) {
                audioPlayerCell.setMessageObject((MessageObject) AudioPlayerAlert.this.playlist.get(i));
            } else {
                audioPlayerCell.setMessageObject((MessageObject) AudioPlayerAlert.this.playlist.get((AudioPlayerAlert.this.playlist.size() - i) - 1));
            }
        }

        public void search(String str) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (str == null) {
                this.searchResult.clear();
                notifyDataSetChanged();
                return;
            }
            DispatchQueue dispatchQueue = Utilities.searchQueue;
            AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda1 audioPlayerAlert$ListAdapter$$ExternalSyntheticLambda1 = new AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda1(this, str);
            this.searchRunnable = audioPlayerAlert$ListAdapter$$ExternalSyntheticLambda1;
            dispatchQueue.postRunnable(audioPlayerAlert$ListAdapter$$ExternalSyntheticLambda1, 300L);
        }

        public /* synthetic */ void lambda$search$0(String str) {
            this.searchRunnable = null;
            processSearch(str);
        }

        private void processSearch(String str) {
            AndroidUtilities.runOnUIThread(new AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda0(this, str));
        }

        public /* synthetic */ void lambda$processSearch$2(String str) {
            Utilities.searchQueue.postRunnable(new AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda2(this, str, new ArrayList(AudioPlayerAlert.this.playlist)));
        }

        public /* synthetic */ void lambda$processSearch$1(String str, ArrayList arrayList) {
            TLRPC$Document tLRPC$Document;
            boolean z;
            String str2;
            String lowerCase = str.trim().toLowerCase();
            if (lowerCase.length() == 0) {
                updateSearchResults(new ArrayList<>(), str);
                return;
            }
            String translitString = LocaleController.getInstance().getTranslitString(lowerCase);
            if (lowerCase.equals(translitString) || translitString.length() == 0) {
                translitString = null;
            }
            int i = (translitString != null ? 1 : 0) + 1;
            String[] strArr = new String[i];
            strArr[0] = lowerCase;
            if (translitString != null) {
                strArr[1] = translitString;
            }
            ArrayList<MessageObject> arrayList2 = new ArrayList<>();
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                MessageObject messageObject = (MessageObject) arrayList.get(i2);
                int i3 = 0;
                while (true) {
                    if (i3 < i) {
                        String str3 = strArr[i3];
                        String documentName = messageObject.getDocumentName();
                        if (documentName != null && documentName.length() != 0) {
                            if (documentName.toLowerCase().contains(str3)) {
                                arrayList2.add(messageObject);
                                break;
                            }
                            if (messageObject.type == 0) {
                                tLRPC$Document = messageObject.messageOwner.media.webpage.document;
                            } else {
                                tLRPC$Document = messageObject.messageOwner.media.document;
                            }
                            int i4 = 0;
                            while (true) {
                                if (i4 >= tLRPC$Document.attributes.size()) {
                                    z = false;
                                    break;
                                }
                                TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i4);
                                if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                                    String str4 = tLRPC$DocumentAttribute.performer;
                                    z = str4 != null ? str4.toLowerCase().contains(str3) : false;
                                    if (!z && (str2 = tLRPC$DocumentAttribute.title) != null) {
                                        z = str2.toLowerCase().contains(str3);
                                    }
                                } else {
                                    i4++;
                                }
                            }
                            if (z) {
                                arrayList2.add(messageObject);
                                break;
                            }
                        }
                        i3++;
                    }
                }
            }
            updateSearchResults(arrayList2, str);
        }

        private void updateSearchResults(ArrayList<MessageObject> arrayList, String str) {
            AndroidUtilities.runOnUIThread(new AudioPlayerAlert$ListAdapter$$ExternalSyntheticLambda3(this, arrayList, str));
        }

        public /* synthetic */ void lambda$updateSearchResults$3(ArrayList arrayList, String str) {
            if (!AudioPlayerAlert.this.searching) {
                return;
            }
            AudioPlayerAlert.this.searchWas = true;
            this.searchResult = arrayList;
            notifyDataSetChanged();
            AudioPlayerAlert.this.layoutManager.scrollToPosition(0);
            AudioPlayerAlert.this.emptySubtitleTextView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("NoAudioFoundPlayerInfo", 2131626806, str)));
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        AudioPlayerAlert$$ExternalSyntheticLambda10 audioPlayerAlert$$ExternalSyntheticLambda10 = new AudioPlayerAlert$$ExternalSyntheticLambda10(this);
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "player_actionBar"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "player_actionBarTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "player_actionBarTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBTITLECOLOR, null, null, null, null, "player_actionBarTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "player_actionBarSelector"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, "player_actionBarTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, "player_time"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, "chat_inLoader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, "chat_outLoader"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, "chat_inLoaderSelected"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, "chat_inMediaIcon"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, "chat_inMediaIconSelected"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, "windowBackgroundWhiteGrayText2"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, "chat_inAudioSelectedProgress"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{AudioPlayerCell.class}, null, null, null, "chat_inAudioProgress"));
        arrayList.add(new ThemeDescription(this.containerView, 0, null, null, new Drawable[]{this.shadowDrawable}, null, "dialogBackground"));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, "player_progressBackground"));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, "player_progress"));
        arrayList.add(new ThemeDescription(this.seekBarView, 0, null, null, null, null, "player_progressBackground"));
        arrayList.add(new ThemeDescription(this.seekBarView, 0, null, null, null, null, "key_player_progressCachedBackground"));
        arrayList.add(new ThemeDescription(this.seekBarView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "player_progress"));
        arrayList.add(new ThemeDescription(this.playbackSpeedButton, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "inappPlayerPlayPause"));
        arrayList.add(new ThemeDescription(this.playbackSpeedButton, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "inappPlayerClose"));
        arrayList.add(new ThemeDescription(this.repeatButton, 0, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "player_button"));
        arrayList.add(new ThemeDescription(this.repeatButton, 0, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "player_buttonActive"));
        arrayList.add(new ThemeDescription(this.repeatButton, 0, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.repeatButton, 0, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "actionBarDefaultSubmenuItem"));
        arrayList.add(new ThemeDescription(this.repeatButton, 0, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "actionBarDefaultSubmenuBackground"));
        arrayList.add(new ThemeDescription(this.optionsButton, 0, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "player_button"));
        arrayList.add(new ThemeDescription(this.optionsButton, 0, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.optionsButton, 0, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "actionBarDefaultSubmenuItem"));
        arrayList.add(new ThemeDescription(this.optionsButton, 0, null, null, null, audioPlayerAlert$$ExternalSyntheticLambda10, "actionBarDefaultSubmenuBackground"));
        RLottieImageView rLottieImageView = this.prevButton;
        arrayList.add(new ThemeDescription(rLottieImageView, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView.getAnimatedDrawable()}, "Triangle 3", "player_button"));
        RLottieImageView rLottieImageView2 = this.prevButton;
        arrayList.add(new ThemeDescription(rLottieImageView2, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView2.getAnimatedDrawable()}, "Triangle 4", "player_button"));
        RLottieImageView rLottieImageView3 = this.prevButton;
        arrayList.add(new ThemeDescription(rLottieImageView3, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView3.getAnimatedDrawable()}, "Rectangle 4", "player_button"));
        arrayList.add(new ThemeDescription(this.prevButton, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.playButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "player_button"));
        arrayList.add(new ThemeDescription(this.playButton, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "listSelectorSDK21"));
        RLottieImageView rLottieImageView4 = this.nextButton;
        arrayList.add(new ThemeDescription(rLottieImageView4, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView4.getAnimatedDrawable()}, "Triangle 3", "player_button"));
        RLottieImageView rLottieImageView5 = this.nextButton;
        arrayList.add(new ThemeDescription(rLottieImageView5, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView5.getAnimatedDrawable()}, "Triangle 4", "player_button"));
        RLottieImageView rLottieImageView6 = this.nextButton;
        arrayList.add(new ThemeDescription(rLottieImageView6, 0, (Class[]) null, new RLottieDrawable[]{rLottieImageView6.getAnimatedDrawable()}, "Rectangle 4", "player_button"));
        arrayList.add(new ThemeDescription(this.nextButton, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.playerLayout, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "player_background"));
        arrayList.add(new ThemeDescription(this.playerShadow, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "dialogShadowLine"));
        arrayList.add(new ThemeDescription(this.emptyImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "dialogEmptyImage"));
        arrayList.add(new ThemeDescription(this.emptyTitleTextView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "dialogEmptyText"));
        arrayList.add(new ThemeDescription(this.emptySubtitleTextView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, "dialogEmptyText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "dialogScrollGlow"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.progressView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"));
        arrayList.add(new ThemeDescription(this.progressView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        arrayList.add(new ThemeDescription(this.durationTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "player_time"));
        arrayList.add(new ThemeDescription(this.timeTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "player_time"));
        arrayList.add(new ThemeDescription(this.titleTextView.getTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "player_actionBarTitle"));
        arrayList.add(new ThemeDescription(this.titleTextView.getNextTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "player_actionBarTitle"));
        arrayList.add(new ThemeDescription(this.authorTextView.getTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "player_time"));
        arrayList.add(new ThemeDescription(this.authorTextView.getNextTextView(), ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "player_time"));
        arrayList.add(new ThemeDescription(this.containerView, 0, null, null, null, null, "key_sheet_scrollUp"));
        return arrayList;
    }

    public /* synthetic */ void lambda$getThemeDescriptions$11() {
        this.searchItem.getSearchField().setCursorColor(getThemedColor("player_actionBarTitle"));
        ActionBarMenuItem actionBarMenuItem = this.repeatButton;
        actionBarMenuItem.setIconColor(getThemedColor((String) actionBarMenuItem.getTag()));
        Theme.setSelectorDrawableColor(this.repeatButton.getBackground(), getThemedColor("listSelectorSDK21"), true);
        this.optionsButton.setIconColor(getThemedColor("player_button"));
        Theme.setSelectorDrawableColor(this.optionsButton.getBackground(), getThemedColor("listSelectorSDK21"), true);
        this.progressView.setBackgroundColor(getThemedColor("player_progressBackground"));
        this.progressView.setProgressColor(getThemedColor("player_progress"));
        updateSubMenu();
        this.repeatButton.redrawPopup(getThemedColor("actionBarDefaultSubmenuBackground"));
        this.optionsButton.setPopupItemsColor(getThemedColor("actionBarDefaultSubmenuItem"), false);
        this.optionsButton.setPopupItemsColor(getThemedColor("actionBarDefaultSubmenuItem"), true);
        this.optionsButton.redrawPopup(getThemedColor("actionBarDefaultSubmenuBackground"));
    }

    /* loaded from: classes3.dex */
    public static abstract class CoverContainer extends FrameLayout {
        private int activeIndex;
        private AnimatorSet animatorSet;
        private final BackupImageView[] imageViews = new BackupImageView[2];

        protected abstract void onImageUpdated(ImageReceiver imageReceiver);

        public CoverContainer(Context context) {
            super(context);
            for (int i = 0; i < 2; i++) {
                this.imageViews[i] = new BackupImageView(context);
                this.imageViews[i].getImageReceiver().setDelegate(new AudioPlayerAlert$CoverContainer$$ExternalSyntheticLambda2(this, i));
                this.imageViews[i].setRoundRadius(AndroidUtilities.dp(4.0f));
                if (i == 1) {
                    this.imageViews[i].setVisibility(8);
                }
                addView(this.imageViews[i], LayoutHelper.createFrame(-1, -1.0f));
            }
        }

        public /* synthetic */ void lambda$new$0(int i, ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
            if (i == this.activeIndex) {
                onImageUpdated(imageReceiver);
            }
        }

        public final void switchImageViews() {
            AnimatorSet animatorSet = this.animatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            this.animatorSet = new AnimatorSet();
            int i = this.activeIndex == 0 ? 1 : 0;
            this.activeIndex = i;
            BackupImageView[] backupImageViewArr = this.imageViews;
            BackupImageView backupImageView = backupImageViewArr[i ^ 1];
            BackupImageView backupImageView2 = backupImageViewArr[i];
            boolean hasBitmapImage = backupImageView.getImageReceiver().hasBitmapImage();
            backupImageView2.setAlpha(hasBitmapImage ? 1.0f : 0.0f);
            backupImageView2.setScaleX(0.8f);
            backupImageView2.setScaleY(0.8f);
            backupImageView2.setVisibility(0);
            if (hasBitmapImage) {
                backupImageView.bringToFront();
            } else {
                backupImageView.setVisibility(8);
                backupImageView.setImageDrawable(null);
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.8f, 1.0f);
            ofFloat.setDuration(125L);
            ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            ofFloat.addUpdateListener(new AudioPlayerAlert$CoverContainer$$ExternalSyntheticLambda1(backupImageView2, hasBitmapImage));
            if (hasBitmapImage) {
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(backupImageView.getScaleX(), 0.8f);
                ofFloat2.setDuration(125L);
                ofFloat2.setInterpolator(CubicBezierInterpolator.EASE_IN);
                ofFloat2.addUpdateListener(new AudioPlayerAlert$CoverContainer$$ExternalSyntheticLambda0(backupImageView, backupImageView2));
                ofFloat2.addListener(new AnonymousClass1(this, backupImageView));
                this.animatorSet.playSequentially(ofFloat2, ofFloat);
            } else {
                this.animatorSet.play(ofFloat);
            }
            this.animatorSet.start();
        }

        public static /* synthetic */ void lambda$switchImageViews$1(BackupImageView backupImageView, boolean z, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            backupImageView.setScaleX(floatValue);
            backupImageView.setScaleY(floatValue);
            if (!z) {
                backupImageView.setAlpha(valueAnimator.getAnimatedFraction());
            }
        }

        public static /* synthetic */ void lambda$switchImageViews$2(BackupImageView backupImageView, BackupImageView backupImageView2, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            backupImageView.setScaleX(floatValue);
            backupImageView.setScaleY(floatValue);
            float animatedFraction = valueAnimator.getAnimatedFraction();
            if (animatedFraction <= 0.25f || backupImageView2.getImageReceiver().hasBitmapImage()) {
                return;
            }
            backupImageView.setAlpha(1.0f - ((animatedFraction - 0.25f) * 1.3333334f));
        }

        /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$CoverContainer$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatorListenerAdapter {
            final /* synthetic */ BackupImageView val$prevImageView;

            AnonymousClass1(CoverContainer coverContainer, BackupImageView backupImageView) {
                this.val$prevImageView = backupImageView;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.val$prevImageView.setVisibility(8);
                this.val$prevImageView.setImageDrawable(null);
                this.val$prevImageView.setAlpha(1.0f);
            }
        }

        public final BackupImageView getImageView() {
            return this.imageViews[this.activeIndex];
        }

        public final BackupImageView getNextImageView() {
            return this.imageViews[this.activeIndex == 0 ? (char) 1 : (char) 0];
        }

        public final ImageReceiver getImageReceiver() {
            return getImageView().getImageReceiver();
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class ClippingTextViewSwitcher extends FrameLayout {
        private int activeIndex;
        private AnimatorSet animatorSet;
        private final Paint erasePaint;
        private final Matrix gradientMatrix;
        private final Paint gradientPaint;
        private LinearGradient gradientShader;
        private final TextView[] textViews = new TextView[2];
        private final float[] clipProgress = {0.0f, 0.75f};
        private final int gradientSize = AndroidUtilities.dp(24.0f);
        private int stableOffest = -1;
        private final RectF rectF = new RectF();

        protected abstract TextView createTextView();

        public ClippingTextViewSwitcher(Context context) {
            super(context);
            for (int i = 0; i < 2; i++) {
                this.textViews[i] = createTextView();
                if (i == 1) {
                    this.textViews[i].setAlpha(0.0f);
                    this.textViews[i].setVisibility(8);
                }
                addView(this.textViews[i], LayoutHelper.createFrame(-2, -1.0f));
            }
            this.gradientMatrix = new Matrix();
            Paint paint = new Paint(1);
            this.gradientPaint = paint;
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            Paint paint2 = new Paint(1);
            this.erasePaint = paint2;
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        @Override // android.view.View
        protected void onSizeChanged(int i, int i2, int i3, int i4) {
            super.onSizeChanged(i, i2, i3, i4);
            LinearGradient linearGradient = new LinearGradient(this.gradientSize, 0.0f, 0.0f, 0.0f, 0, -16777216, Shader.TileMode.CLAMP);
            this.gradientShader = linearGradient;
            this.gradientPaint.setShader(linearGradient);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            boolean z;
            TextView[] textViewArr = this.textViews;
            boolean z2 = true;
            int i = view == textViewArr[0] ? 0 : 1;
            if (this.stableOffest <= 0 || textViewArr[this.activeIndex].getAlpha() == 1.0f || this.textViews[this.activeIndex].getLayout() == null) {
                z = false;
            } else {
                float primaryHorizontal = this.textViews[this.activeIndex].getLayout().getPrimaryHorizontal(0);
                float primaryHorizontal2 = this.textViews[this.activeIndex].getLayout().getPrimaryHorizontal(this.stableOffest);
                if (primaryHorizontal == primaryHorizontal2) {
                    z2 = false;
                } else if (primaryHorizontal2 > primaryHorizontal) {
                    this.rectF.set(primaryHorizontal, 0.0f, primaryHorizontal2, getMeasuredHeight());
                } else {
                    this.rectF.set(primaryHorizontal2, 0.0f, primaryHorizontal, getMeasuredHeight());
                }
                if (z2 && i == this.activeIndex) {
                    canvas.save();
                    canvas.clipRect(this.rectF);
                    this.textViews[0].draw(canvas);
                    canvas.restore();
                }
                z = z2;
            }
            if (this.clipProgress[i] > 0.0f || z) {
                float width = view.getWidth();
                float height = view.getHeight();
                int saveLayer = canvas.saveLayer(0.0f, 0.0f, width, height, null, 31);
                boolean drawChild = super.drawChild(canvas, view, j);
                float f = width * (1.0f - this.clipProgress[i]);
                float f2 = f + this.gradientSize;
                this.gradientMatrix.setTranslate(f, 0.0f);
                this.gradientShader.setLocalMatrix(this.gradientMatrix);
                canvas.drawRect(f, 0.0f, f2, height, this.gradientPaint);
                if (width > f2) {
                    canvas.drawRect(f2, 0.0f, width, height, this.erasePaint);
                }
                if (z) {
                    canvas.drawRect(this.rectF, this.erasePaint);
                }
                canvas.restoreToCount(saveLayer);
                return drawChild;
            }
            return super.drawChild(canvas, view, j);
        }

        public void setText(CharSequence charSequence) {
            setText(charSequence, true);
        }

        public void setText(CharSequence charSequence, boolean z) {
            CharSequence text = this.textViews[this.activeIndex].getText();
            if (TextUtils.isEmpty(text) || !z) {
                this.textViews[this.activeIndex].setText(charSequence);
            } else if (!TextUtils.equals(charSequence, text)) {
                this.stableOffest = 0;
                int min = Math.min(charSequence.length(), text.length());
                for (int i = 0; i < min && charSequence.charAt(i) == text.charAt(i); i++) {
                    this.stableOffest++;
                }
                if (this.stableOffest <= 3) {
                    this.stableOffest = -1;
                }
                int i2 = this.activeIndex;
                int i3 = i2 == 0 ? 1 : 0;
                this.activeIndex = i3;
                AnimatorSet animatorSet = this.animatorSet;
                if (animatorSet != null) {
                    animatorSet.cancel();
                }
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.animatorSet = animatorSet2;
                animatorSet2.addListener(new AnonymousClass1(i2));
                this.textViews[i3].setText(charSequence);
                this.textViews[i3].bringToFront();
                this.textViews[i3].setVisibility(0);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.clipProgress[i2], 0.75f);
                ofFloat.setDuration(200L);
                ofFloat.addUpdateListener(new AudioPlayerAlert$ClippingTextViewSwitcher$$ExternalSyntheticLambda0(this, i2));
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.clipProgress[i3], 0.0f);
                ofFloat2.setStartDelay(100L);
                ofFloat2.setDuration(200L);
                ofFloat2.addUpdateListener(new AudioPlayerAlert$ClippingTextViewSwitcher$$ExternalSyntheticLambda1(this, i3));
                ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.textViews[i2], View.ALPHA, 0.0f);
                ofFloat3.setStartDelay(75L);
                ofFloat3.setDuration(150L);
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.textViews[i3], View.ALPHA, 1.0f);
                ofFloat4.setStartDelay(75L);
                ofFloat4.setDuration(150L);
                this.animatorSet.playTogether(ofFloat, ofFloat2, ofFloat3, ofFloat4);
                this.animatorSet.start();
            }
        }

        /* renamed from: org.telegram.ui.Components.AudioPlayerAlert$ClippingTextViewSwitcher$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatorListenerAdapter {
            final /* synthetic */ int val$prevIndex;

            AnonymousClass1(int i) {
                ClippingTextViewSwitcher.this = r1;
                this.val$prevIndex = i;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ClippingTextViewSwitcher.this.textViews[this.val$prevIndex].setVisibility(8);
            }
        }

        public /* synthetic */ void lambda$setText$0(int i, ValueAnimator valueAnimator) {
            this.clipProgress[i] = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        public /* synthetic */ void lambda$setText$1(int i, ValueAnimator valueAnimator) {
            this.clipProgress[i] = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        public TextView getTextView() {
            return this.textViews[this.activeIndex];
        }

        public TextView getNextTextView() {
            return this.textViews[this.activeIndex == 0 ? (char) 1 : (char) 0];
        }
    }
}
