package org.telegram.ui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetEmpty;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_messages_getMyStickers;
import org.telegram.tgnet.TLRPC$TL_messages_myStickers;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_stickerSetNoCovered;
import org.telegram.tgnet.TLRPC$TL_webDocument;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WebDocument;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.Cells.StickerEmojiCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EmojiPacksAlert;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PaintingOverlay;
import org.telegram.ui.Components.Reactions.CustomEmojiReactionsWindow;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.ReactionsContainerLayout;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickersDialogs;
import org.telegram.ui.Components.SuggestEmojiView;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.Stories.DarkThemeResourceProvider;
/* loaded from: classes4.dex */
public class ContentPreviewViewer {
    private static volatile ContentPreviewViewer Instance;
    private static TextPaint textPaint;
    private float blurProgress;
    private Bitmap blurrBitmap;
    private boolean clearsInputField;
    private boolean closeOnDismiss;
    private FrameLayoutDrawer containerView;
    private int currentAccount;
    private int currentContentType;
    private TLRPC$Document currentDocument;
    private float currentMoveY;
    private float currentMoveYProgress;
    private View currentPreviewCell;
    private String currentQuery;
    private TLRPC$InputStickerSet currentStickerSet;
    private ContentPreviewViewerDelegate delegate;
    private boolean drawEffect;
    private float finalMoveY;
    private SendMessagesHelper.ImportingSticker importingSticker;
    private TLRPC$BotInlineResult inlineResult;
    private boolean isPhotoEditor;
    private boolean isRecentSticker;
    private boolean isStickerEditor;
    private WindowInsets lastInsets;
    private float lastTouchY;
    private long lastUpdateTime;
    private boolean menuVisible;
    private Runnable openPreviewRunnable;
    public PaintingOverlay paintingOverlay;
    private Path paintingOverlayClipPath;
    private Activity parentActivity;
    private Object parentObject;
    private View popupLayout;
    ActionBarPopupWindow popupWindow;
    private boolean preparingBitmap;
    private ReactionsContainerLayout reactionsLayout;
    private FrameLayout reactionsLayoutContainer;
    private Theme.ResourcesProvider resourcesProvider;
    private ArrayList selectedEmojis;
    private float showProgress;
    private Drawable slideUpDrawable;
    private float startMoveY;
    private int startX;
    private int startY;
    private StaticLayout stickerEmojiLayout;
    private TLRPC$TL_messages_stickerSet stickerSetForCustomSticker;
    private UnlockPremiumView unlockPremiumView;
    VibrationEffect vibrationEffect;
    private WindowManager.LayoutParams windowLayoutParams;
    private FrameLayout windowView;
    private float moveY = 0.0f;
    private ColorDrawable backgroundDrawable = new ColorDrawable(1895825408);
    public ImageReceiver centerImage = new ImageReceiver();
    private ImageReceiver effectImage = new ImageReceiver();
    private boolean isVisible = false;
    private int keyboardHeight = AndroidUtilities.dp(200.0f);
    private Paint paint = new Paint(1);
    private final Runnable showSheetRunnable = new 1();

    /* loaded from: classes4.dex */
    public class 1 implements Runnable {

        /* loaded from: classes4.dex */
        class 1 implements View.OnClickListener {
            final /* synthetic */ ArrayList val$actions;
            final /* synthetic */ boolean val$inFavs;

            1(ArrayList arrayList, boolean z) {
                1.this = r1;
                this.val$actions = arrayList;
                this.val$inFavs = z;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MediaDataController mediaDataController;
                Object obj;
                TLRPC$Document tLRPC$Document;
                int currentTimeMillis;
                int i;
                boolean z;
                if (ContentPreviewViewer.this.parentActivity == null) {
                    return;
                }
                int intValue = ((Integer) view.getTag()).intValue();
                if (((Integer) this.val$actions.get(intValue)).intValue() == 0 || ((Integer) this.val$actions.get(intValue)).intValue() == 6) {
                    if (ContentPreviewViewer.this.delegate != null) {
                        ContentPreviewViewer.this.delegate.sendSticker(ContentPreviewViewer.this.currentDocument, ContentPreviewViewer.this.currentQuery, ContentPreviewViewer.this.parentObject, ((Integer) this.val$actions.get(intValue)).intValue() == 0, 0);
                    }
                } else if (((Integer) this.val$actions.get(intValue)).intValue() != 1) {
                    if (((Integer) this.val$actions.get(intValue)).intValue() == 2) {
                        mediaDataController = MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount);
                        obj = ContentPreviewViewer.this.parentObject;
                        tLRPC$Document = ContentPreviewViewer.this.currentDocument;
                        currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
                        z = this.val$inFavs;
                        i = 2;
                    } else if (((Integer) this.val$actions.get(intValue)).intValue() == 3) {
                        final TLRPC$Document tLRPC$Document2 = ContentPreviewViewer.this.currentDocument;
                        final Object obj2 = ContentPreviewViewer.this.parentObject;
                        final String str = ContentPreviewViewer.this.currentQuery;
                        final ContentPreviewViewerDelegate contentPreviewViewerDelegate = ContentPreviewViewer.this.delegate;
                        if (contentPreviewViewerDelegate == null) {
                            return;
                        }
                        AlertsCreator.createScheduleDatePickerDialog(ContentPreviewViewer.this.parentActivity, contentPreviewViewerDelegate.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.ContentPreviewViewer$1$1$$ExternalSyntheticLambda0
                            @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                            public final void didSelectDate(boolean z2, int i2) {
                                ContentPreviewViewer.ContentPreviewViewerDelegate.this.sendSticker(tLRPC$Document2, str, obj2, z2, i2);
                            }
                        });
                    } else if (((Integer) this.val$actions.get(intValue)).intValue() == 4) {
                        mediaDataController = MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount);
                        obj = ContentPreviewViewer.this.parentObject;
                        tLRPC$Document = ContentPreviewViewer.this.currentDocument;
                        currentTimeMillis = (int) (System.currentTimeMillis() / 1000);
                        i = 0;
                        z = true;
                    } else if (((Integer) this.val$actions.get(intValue)).intValue() == 5) {
                        ContentPreviewViewer.this.delegate.remove(ContentPreviewViewer.this.importingSticker);
                    } else if (((Integer) this.val$actions.get(intValue)).intValue() == 7) {
                        ContentPreviewViewer.this.delegate.editSticker(ContentPreviewViewer.this.currentDocument);
                    } else if (((Integer) this.val$actions.get(intValue)).intValue() == 8) {
                        ContentPreviewViewer.this.delegate.deleteSticker(ContentPreviewViewer.this.currentDocument);
                    }
                    mediaDataController.addRecentSticker(i, obj, tLRPC$Document, currentTimeMillis, z);
                } else if (ContentPreviewViewer.this.delegate != null) {
                    ContentPreviewViewer.this.delegate.openSet(ContentPreviewViewer.this.currentStickerSet, ContentPreviewViewer.this.clearsInputField);
                }
                ContentPreviewViewer.this.dismissPopupWindow();
            }
        }

        1() {
            ContentPreviewViewer.this = r1;
        }

        public /* synthetic */ void lambda$run$0(Utilities.Callback callback, Boolean bool) {
            callback.run(bool);
            if (bool.booleanValue()) {
                ContentPreviewViewer.this.dismissPopupWindow();
            }
        }

        public /* synthetic */ void lambda$run$1(CharSequence charSequence, final Utilities.Callback callback) {
            if (ContentPreviewViewer.this.delegate != null) {
                ContentPreviewViewer.this.delegate.newStickerPackSelected(charSequence, TextUtils.join("", ContentPreviewViewer.this.selectedEmojis), callback != null ? new Utilities.Callback() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda9
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        ContentPreviewViewer.1.this.lambda$run$0(callback, (Boolean) obj);
                    }
                } : null);
                if (callback == null) {
                    ContentPreviewViewer.this.dismissPopupWindow();
                }
            }
        }

        public /* synthetic */ void lambda$run$2(View view, int i) {
            TLRPC$StickerSetCovered cover = ((StickerPackNameView) view).getCover();
            CustomEmojiReactionsWindow reactionsWindow = ContentPreviewViewer.this.reactionsLayout.getReactionsWindow();
            if (reactionsWindow != null && reactionsWindow.isShowing()) {
                reactionsWindow.dismiss();
            }
            if (cover instanceof TLRPC$TL_stickerSetNoCovered) {
                StickersDialogs.showNameEditorDialog(null, ContentPreviewViewer.this.resourcesProvider, ContentPreviewViewer.this.containerView.getContext(), new Utilities.Callback2() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda7
                    @Override // org.telegram.messenger.Utilities.Callback2
                    public final void run(Object obj, Object obj2) {
                        ContentPreviewViewer.1.this.lambda$run$1((CharSequence) obj, (Utilities.Callback) obj2);
                    }
                });
                return;
            }
            if (ContentPreviewViewer.this.delegate != null) {
                ContentPreviewViewer.this.delegate.stickerSetSelected(cover.set, TextUtils.join("", ContentPreviewViewer.this.selectedEmojis));
            }
            ContentPreviewViewer.this.dismissPopupWindow();
        }

        public /* synthetic */ void lambda$run$3(ArrayList arrayList, RecyclerListView recyclerListView, LinearLayout linearLayout, ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, View view) {
            if (ContentPreviewViewer.this.parentActivity == null) {
                return;
            }
            int intValue = ((Integer) view.getTag()).intValue();
            if (((Integer) arrayList.get(intValue)).intValue() == 2) {
                if (ContentPreviewViewer.this.stickerSetForCustomSticker == null) {
                    recyclerListView.requestLayout();
                    linearLayout.requestLayout();
                    recyclerListView.getAdapter().notifyDataSetChanged();
                    actionBarPopupWindowLayout.getSwipeBack().openForeground(1);
                    return;
                } else if (ContentPreviewViewer.this.delegate != null) {
                    ContentPreviewViewer.this.delegate.stickerSetSelected(ContentPreviewViewer.this.stickerSetForCustomSticker.set, TextUtils.join("", ContentPreviewViewer.this.selectedEmojis));
                }
            } else if (ContentPreviewViewer.this.delegate != null) {
                if (((Integer) arrayList.get(intValue)).intValue() == 1) {
                    ContentPreviewViewer.this.delegate.addToFavoriteSelected(TextUtils.join("", ContentPreviewViewer.this.selectedEmojis));
                } else if (((Integer) arrayList.get(intValue)).intValue() == 0) {
                    if (ContentPreviewViewer.this.delegate.isSettingIntroSticker()) {
                        ContentPreviewViewer.this.delegate.setIntroSticker(TextUtils.join("", ContentPreviewViewer.this.selectedEmojis));
                    } else {
                        ContentPreviewViewer.this.delegate.sendSticker();
                    }
                }
            }
            ContentPreviewViewer.this.dismissPopupWindow();
        }

        public static /* synthetic */ void lambda$run$4(ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout, View view) {
            actionBarPopupWindowLayout.getSwipeBack().closeForeground();
        }

        public /* synthetic */ void lambda$run$5(ArrayList arrayList, boolean z, View view) {
            if (ContentPreviewViewer.this.parentActivity == null || ContentPreviewViewer.this.delegate == null) {
                return;
            }
            int intValue = ((Integer) arrayList.get(((Integer) view.getTag()).intValue())).intValue();
            if (intValue == 0) {
                ContentPreviewViewer.this.delegate.sendEmoji(ContentPreviewViewer.this.currentDocument);
            } else if (intValue == 1) {
                ContentPreviewViewer.this.delegate.setAsEmojiStatus(ContentPreviewViewer.this.currentDocument, null);
            } else if (intValue == 2) {
                ContentPreviewViewer.this.delegate.setAsEmojiStatus(null, null);
            } else if (intValue == 3) {
                ContentPreviewViewer.this.delegate.copyEmoji(ContentPreviewViewer.this.currentDocument);
            } else if (intValue == 4) {
                ContentPreviewViewer.this.delegate.removeFromRecent(ContentPreviewViewer.this.currentDocument);
            } else if (intValue == 5) {
                MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).addRecentSticker(2, ContentPreviewViewer.this.parentObject, ContentPreviewViewer.this.currentDocument, (int) (System.currentTimeMillis() / 1000), z);
            }
            ContentPreviewViewer.this.dismissPopupWindow();
        }

        public /* synthetic */ void lambda$run$6(ValueAnimator valueAnimator) {
            ContentPreviewViewer.this.currentMoveYProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ContentPreviewViewer contentPreviewViewer = ContentPreviewViewer.this;
            contentPreviewViewer.moveY = contentPreviewViewer.startMoveY + ((ContentPreviewViewer.this.finalMoveY - ContentPreviewViewer.this.startMoveY) * ContentPreviewViewer.this.currentMoveYProgress);
            ContentPreviewViewer.this.containerView.invalidate();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ void lambda$run$7(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document, TLRPC$BotInlineResult tLRPC$BotInlineResult, Object obj, boolean z, int i) {
            if (tLRPC$Document == null) {
                tLRPC$Document = tLRPC$BotInlineResult;
            }
            contentPreviewViewerDelegate.sendGif(tLRPC$Document, obj, z, i);
        }

        public /* synthetic */ void lambda$run$8(ArrayList arrayList, View view) {
            if (ContentPreviewViewer.this.parentActivity == null) {
                return;
            }
            int intValue = ((Integer) view.getTag()).intValue();
            if (((Integer) arrayList.get(intValue)).intValue() == 0) {
                ContentPreviewViewer.this.delegate.sendGif(ContentPreviewViewer.this.currentDocument != null ? ContentPreviewViewer.this.currentDocument : ContentPreviewViewer.this.inlineResult, ContentPreviewViewer.this.parentObject, true, 0);
            } else if (((Integer) arrayList.get(intValue)).intValue() == 4) {
                ContentPreviewViewer.this.delegate.sendGif(ContentPreviewViewer.this.currentDocument != null ? ContentPreviewViewer.this.currentDocument : ContentPreviewViewer.this.inlineResult, ContentPreviewViewer.this.parentObject, false, 0);
            } else {
                if (((Integer) arrayList.get(intValue)).intValue() == 1) {
                    MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).removeRecentGif(ContentPreviewViewer.this.currentDocument);
                } else if (((Integer) arrayList.get(intValue)).intValue() == 2) {
                    MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).addRecentGif(ContentPreviewViewer.this.currentDocument, (int) (System.currentTimeMillis() / 1000), true);
                    MessagesController.getInstance(ContentPreviewViewer.this.currentAccount).saveGif("gif", ContentPreviewViewer.this.currentDocument);
                } else if (((Integer) arrayList.get(intValue)).intValue() == 3) {
                    final TLRPC$Document tLRPC$Document = ContentPreviewViewer.this.currentDocument;
                    final TLRPC$BotInlineResult tLRPC$BotInlineResult = ContentPreviewViewer.this.inlineResult;
                    final Object obj = ContentPreviewViewer.this.parentObject;
                    final ContentPreviewViewerDelegate contentPreviewViewerDelegate = ContentPreviewViewer.this.delegate;
                    AlertsCreator.createScheduleDatePickerDialog(ContentPreviewViewer.this.parentActivity, contentPreviewViewerDelegate.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda8
                        @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                        public final void didSelectDate(boolean z, int i) {
                            ContentPreviewViewer.1.lambda$run$7(ContentPreviewViewer.ContentPreviewViewerDelegate.this, tLRPC$Document, tLRPC$BotInlineResult, obj, z, i);
                        }
                    }, ContentPreviewViewer.this.resourcesProvider);
                }
                ContentPreviewViewer.this.delegate.gifAddedOrDeleted();
            }
            ContentPreviewViewer.this.dismissPopupWindow();
        }

        public /* synthetic */ void lambda$run$9(ValueAnimator valueAnimator) {
            ContentPreviewViewer.this.currentMoveYProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ContentPreviewViewer contentPreviewViewer = ContentPreviewViewer.this;
            contentPreviewViewer.moveY = contentPreviewViewer.startMoveY + ((ContentPreviewViewer.this.finalMoveY - ContentPreviewViewer.this.startMoveY) * ContentPreviewViewer.this.currentMoveYProgress);
            ContentPreviewViewer.this.containerView.invalidate();
        }

        /* JADX WARN: Removed duplicated region for block: B:627:0x0e2f  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void run() {
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout;
            boolean z;
            int i;
            int i2;
            int i3;
            int stableInsetBottom;
            int stableInsetTop;
            int i4;
            int i5;
            int i6;
            int stableInsetBottom2;
            int stableInsetTop2;
            int i7;
            int i8;
            int min;
            int stableInsetBottom3;
            int stableInsetTop3;
            TLRPC$TL_messages_stickerSet stickerSet;
            int i9;
            int i10;
            int i11;
            int stableInsetBottom4;
            int stableInsetTop4;
            int i12;
            if (ContentPreviewViewer.this.parentActivity == null || ContentPreviewViewer.this.isPhotoEditor) {
                return;
            }
            ContentPreviewViewer.this.closeOnDismiss = true;
            final ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = new ActionBarPopupWindow.ActionBarPopupWindowLayout(ContentPreviewViewer.this.containerView.getContext(), R.drawable.popup_fixed_alert3, ContentPreviewViewer.this.resourcesProvider, ContentPreviewViewer.this.currentContentType == 3 ? 1 : 0);
            if (ContentPreviewViewer.this.currentContentType == 3) {
                ArrayList arrayList = new ArrayList();
                final ArrayList arrayList2 = new ArrayList();
                ArrayList arrayList3 = new ArrayList();
                if (ContentPreviewViewer.this.stickerSetForCustomSticker == null) {
                    if (ContentPreviewViewer.this.delegate == null || !ContentPreviewViewer.this.delegate.isSettingIntroSticker()) {
                        arrayList.add(LocaleController.getString(R.string.SendStickerPreview));
                        arrayList3.add(Integer.valueOf(R.drawable.msg_send));
                        arrayList2.add(0);
                        arrayList.add(LocaleController.getString(R.string.AddToFavorites));
                        arrayList3.add(Integer.valueOf(R.drawable.msg_fave));
                        i12 = 1;
                    } else {
                        arrayList.add(LocaleController.getString(R.string.SetIntroSticker));
                        arrayList3.add(Integer.valueOf(R.drawable.menu_sticker_add));
                        i12 = 0;
                    }
                    arrayList2.add(i12);
                }
                if (ContentPreviewViewer.this.delegate == null || !ContentPreviewViewer.this.delegate.isSettingIntroSticker()) {
                    arrayList.add(LocaleController.getString((ContentPreviewViewer.this.delegate == null || !ContentPreviewViewer.this.delegate.isReplacedSticker()) ? R.string.AddToStickerPack : R.string.StickersReplaceSticker));
                    arrayList3.add(Integer.valueOf((ContentPreviewViewer.this.delegate == null || !ContentPreviewViewer.this.delegate.isReplacedSticker()) ? R.drawable.menu_sticker_add : R.drawable.msg_replace));
                    arrayList2.add(2);
                }
                ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem((Context) ContentPreviewViewer.this.parentActivity, true, false, ContentPreviewViewer.this.resourcesProvider);
                actionBarMenuSubItem.setItemHeight(44);
                actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.Back), R.drawable.msg_arrow_back);
                actionBarMenuSubItem.getTextView().setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(40.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(40.0f) : 0, 0);
                FrameLayout frameLayout = new FrameLayout(ContentPreviewViewer.this.containerView.getContext());
                final LinearLayout linearLayout = new LinearLayout(ContentPreviewViewer.this.containerView.getContext());
                linearLayout.setBackgroundColor(ContentPreviewViewer.this.getThemedColor(Theme.key_actionBarDefaultSubmenuBackground));
                linearLayout.setOrientation(1);
                final RecyclerListView createMyStickerPacksListView = ContentPreviewViewer.this.createMyStickerPacksListView();
                createMyStickerPacksListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i13) {
                        ContentPreviewViewer.1.this.lambda$run$2(view, i13);
                    }
                });
                frameLayout.addView(actionBarMenuSubItem);
                linearLayout.addView(frameLayout);
                linearLayout.addView(new ActionBarPopupWindow.GapView(ContentPreviewViewer.this.containerView.getContext(), ContentPreviewViewer.this.resourcesProvider), LayoutHelper.createLinear(-1, 8));
                View.OnClickListener onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ContentPreviewViewer.1.this.lambda$run$3(arrayList2, createMyStickerPacksListView, linearLayout, actionBarPopupWindowLayout2, view);
                    }
                };
                for (int i13 = 0; i13 < arrayList.size(); i13++) {
                    ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout2, ((Integer) arrayList3.get(i13)).intValue(), (CharSequence) arrayList.get(i13), false, ContentPreviewViewer.this.resourcesProvider);
                    addItem.setTag(Integer.valueOf(i13));
                    addItem.setOnClickListener(onClickListener);
                }
                actionBarPopupWindowLayout2.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
                linearLayout.addView(createMyStickerPacksListView, new LinearLayout.LayoutParams(actionBarPopupWindowLayout2.getMeasuredWidth() - AndroidUtilities.dp(16.0f), (int) (actionBarPopupWindowLayout2.getMeasuredHeight() * 1.5f)));
                actionBarPopupWindowLayout2.addViewToSwipeBack(linearLayout);
                frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda5
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ContentPreviewViewer.1.lambda$run$4(ActionBarPopupWindow.ActionBarPopupWindowLayout.this, view);
                    }
                });
                if (Build.VERSION.SDK_INT < 21 || ContentPreviewViewer.this.lastInsets == null) {
                    i10 = AndroidUtilities.statusBarHeight;
                    i11 = 0;
                } else {
                    stableInsetBottom4 = ContentPreviewViewer.this.lastInsets.getStableInsetBottom();
                    stableInsetTop4 = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                    i11 = stableInsetBottom4 + stableInsetTop4;
                    i10 = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                }
                int min2 = ((int) (Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i11) / 1.8f)) / 2;
                ContentPreviewViewer.this.containerView.addView(actionBarPopupWindowLayout2, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, (((int) ((ContentPreviewViewer.this.moveY + Math.max(i10 + min2, ((ContentPreviewViewer.this.containerView.getHeight() - i11) - ContentPreviewViewer.this.keyboardHeight) / 2)) + min2)) + AndroidUtilities.dp(84.0f)) / AndroidUtilities.density, 0.0f, 0.0f));
                ContentPreviewViewer.this.popupLayout = actionBarPopupWindowLayout2;
                ContentPreviewViewer.this.popupLayout.setTranslationY(-AndroidUtilities.dp(12.0f));
                ContentPreviewViewer.this.popupLayout.setAlpha(0.0f);
                ContentPreviewViewer.this.popupLayout.setScaleX(0.8f);
                ContentPreviewViewer.this.popupLayout.setScaleY(0.8f);
                ContentPreviewViewer.this.popupLayout.setPivotY(0.0f);
                ContentPreviewViewer.this.popupLayout.setPivotX(ContentPreviewViewer.this.popupLayout.getMeasuredWidth() / 2.0f);
                ContentPreviewViewer.this.popupLayout.animate().translationY(0.0f).alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(320L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
                ContentPreviewViewer.this.showEmojiSelectorForStickers();
                ContentPreviewViewer.this.menuVisible = true;
                ContentPreviewViewer.this.containerView.invalidate();
            } else if (ContentPreviewViewer.this.currentContentType != 0) {
                if (ContentPreviewViewer.this.currentContentType != 2 || ContentPreviewViewer.this.delegate == null) {
                    actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
                    if (ContentPreviewViewer.this.delegate != null) {
                        ArrayList arrayList4 = new ArrayList();
                        final ArrayList arrayList5 = new ArrayList();
                        ArrayList arrayList6 = new ArrayList();
                        if (ContentPreviewViewer.this.delegate.needSend(ContentPreviewViewer.this.currentContentType) && !ContentPreviewViewer.this.delegate.isInScheduleMode()) {
                            arrayList4.add(LocaleController.getString(R.string.SendGifPreview));
                            arrayList6.add(Integer.valueOf(R.drawable.msg_send));
                            arrayList5.add(0);
                        }
                        if (ContentPreviewViewer.this.delegate.needSend(ContentPreviewViewer.this.currentContentType) && !ContentPreviewViewer.this.delegate.isInScheduleMode()) {
                            arrayList4.add(LocaleController.getString(R.string.SendWithoutSound));
                            arrayList6.add(Integer.valueOf(R.drawable.input_notify_off));
                            arrayList5.add(4);
                        }
                        if (ContentPreviewViewer.this.delegate.canSchedule()) {
                            arrayList4.add(LocaleController.getString(R.string.Schedule));
                            arrayList6.add(Integer.valueOf(R.drawable.msg_autodelete));
                            arrayList5.add(3);
                        }
                        if (ContentPreviewViewer.this.currentDocument != null) {
                            z = MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).hasRecentGif(ContentPreviewViewer.this.currentDocument);
                            if (z) {
                                arrayList4.add(LocaleController.formatString("Delete", R.string.Delete, new Object[0]));
                                arrayList6.add(Integer.valueOf(R.drawable.msg_delete));
                                arrayList5.add(1);
                            } else {
                                arrayList4.add(LocaleController.formatString("SaveToGIFs", R.string.SaveToGIFs, new Object[0]));
                                arrayList6.add(Integer.valueOf(R.drawable.msg_gif_add));
                                arrayList5.add(2);
                            }
                        } else {
                            z = false;
                        }
                        if (arrayList4.isEmpty()) {
                            return;
                        }
                        ContentPreviewViewer.this.menuVisible = true;
                        ContentPreviewViewer.this.containerView.invalidate();
                        int[] iArr = new int[arrayList6.size()];
                        for (int i14 = 0; i14 < arrayList6.size(); i14++) {
                            iArr[i14] = ((Integer) arrayList6.get(i14)).intValue();
                        }
                        View.OnClickListener onClickListener2 = new View.OnClickListener() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                ContentPreviewViewer.1.this.lambda$run$8(arrayList5, view);
                            }
                        };
                        for (int i15 = 0; i15 < arrayList4.size(); i15++) {
                            ActionBarMenuSubItem addItem2 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, ((Integer) arrayList6.get(i15)).intValue(), (CharSequence) arrayList4.get(i15), false, ContentPreviewViewer.this.resourcesProvider);
                            addItem2.setTag(Integer.valueOf(i15));
                            addItem2.setOnClickListener(onClickListener2);
                            if (z && i15 == arrayList4.size() - 1) {
                                addItem2.setColors(ContentPreviewViewer.this.getThemedColor(Theme.key_text_RedBold), ContentPreviewViewer.this.getThemedColor(Theme.key_text_RedRegular));
                            }
                        }
                        ContentPreviewViewer.this.popupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2) { // from class: org.telegram.ui.ContentPreviewViewer.1.4
                            {
                                1.this = this;
                            }

                            @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
                            public void dismiss() {
                                super.dismiss();
                                ContentPreviewViewer contentPreviewViewer = ContentPreviewViewer.this;
                                contentPreviewViewer.popupWindow = null;
                                contentPreviewViewer.menuVisible = false;
                                if (ContentPreviewViewer.this.closeOnDismiss) {
                                    ContentPreviewViewer.this.close();
                                }
                            }
                        };
                        ContentPreviewViewer.this.popupWindow.setPauseNotifications(true);
                        ContentPreviewViewer.this.popupWindow.setDismissAnimationDuration(150);
                        ContentPreviewViewer.this.popupWindow.setScaleOut(true);
                        ContentPreviewViewer.this.popupWindow.setOutsideTouchable(true);
                        ContentPreviewViewer.this.popupWindow.setClippingEnabled(true);
                        ContentPreviewViewer.this.popupWindow.setAnimationStyle(R.style.PopupContextAnimation);
                        ContentPreviewViewer.this.popupWindow.setFocusable(true);
                        actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
                        ContentPreviewViewer.this.popupWindow.setInputMethodMode(2);
                        ContentPreviewViewer.this.popupWindow.getContentView().setFocusableInTouchMode(true);
                        if (Build.VERSION.SDK_INT < 21 || ContentPreviewViewer.this.lastInsets == null) {
                            i = AndroidUtilities.statusBarHeight;
                            i2 = 0;
                        } else {
                            stableInsetBottom = ContentPreviewViewer.this.lastInsets.getStableInsetBottom();
                            stableInsetTop = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                            i2 = stableInsetBottom + stableInsetTop;
                            i = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                        }
                        int min3 = Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i2) - AndroidUtilities.dp(40.0f);
                        float f = ContentPreviewViewer.this.moveY;
                        int i16 = i + (min3 / 2);
                        int dp = ContentPreviewViewer.this.stickerEmojiLayout != null ? AndroidUtilities.dp(40.0f) : 0;
                        ContentPreviewViewer contentPreviewViewer = ContentPreviewViewer.this;
                        contentPreviewViewer.popupWindow.showAtLocation(contentPreviewViewer.containerView, 0, (int) ((ContentPreviewViewer.this.containerView.getMeasuredWidth() - actionBarPopupWindowLayout.getMeasuredWidth()) / 2.0f), (int) (((int) (f + Math.max(i16 + dp, ((ContentPreviewViewer.this.containerView.getHeight() - i2) - ContentPreviewViewer.this.keyboardHeight) / 2) + i3)) + (AndroidUtilities.dp(24.0f) - ContentPreviewViewer.this.moveY)));
                        ContentPreviewViewer.this.containerView.performHapticFeedback(0);
                        if (ContentPreviewViewer.this.moveY != 0.0f) {
                            if (ContentPreviewViewer.this.finalMoveY == 0.0f) {
                                ContentPreviewViewer.this.finalMoveY = 0.0f;
                                ContentPreviewViewer contentPreviewViewer2 = ContentPreviewViewer.this;
                                contentPreviewViewer2.startMoveY = contentPreviewViewer2.moveY;
                            }
                            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda3
                                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    ContentPreviewViewer.1.this.lambda$run$9(valueAnimator);
                                }
                            });
                            ofFloat.setDuration(350L);
                            ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
                            ofFloat.start();
                        }
                        i9 = 0;
                        while (i9 < actionBarPopupWindowLayout.getItemsCount()) {
                            View itemAt = actionBarPopupWindowLayout.getItemAt(i9);
                            if (itemAt instanceof ActionBarMenuSubItem) {
                                ((ActionBarMenuSubItem) itemAt).updateSelectorBackground(i9 == 0, i9 == actionBarPopupWindowLayout.getItemsCount() - 1, 8);
                            }
                            i9++;
                        }
                    }
                } else {
                    ArrayList arrayList7 = new ArrayList();
                    final ArrayList arrayList8 = new ArrayList();
                    ArrayList arrayList9 = new ArrayList();
                    if (ContentPreviewViewer.this.delegate.needSend(ContentPreviewViewer.this.currentContentType)) {
                        arrayList7.add(LocaleController.getString(R.string.SendEmojiPreview));
                        arrayList9.add(Integer.valueOf(R.drawable.msg_send));
                        arrayList8.add(0);
                    }
                    Boolean canSetAsStatus = ContentPreviewViewer.this.delegate.canSetAsStatus(ContentPreviewViewer.this.currentDocument);
                    if (canSetAsStatus != null) {
                        if (canSetAsStatus.booleanValue()) {
                            arrayList7.add(LocaleController.getString(R.string.SetAsEmojiStatus));
                            arrayList9.add(Integer.valueOf(R.drawable.msg_smile_status));
                            arrayList8.add(1);
                        } else {
                            arrayList7.add(LocaleController.getString(R.string.RemoveStatus));
                            arrayList9.add(Integer.valueOf(R.drawable.msg_smile_status));
                            arrayList8.add(2);
                        }
                    }
                    if (ContentPreviewViewer.this.delegate.needCopy(ContentPreviewViewer.this.currentDocument)) {
                        arrayList7.add(LocaleController.getString(R.string.CopyEmojiPreview));
                        arrayList9.add(Integer.valueOf(R.drawable.msg_copy));
                        arrayList8.add(3);
                    }
                    if (ContentPreviewViewer.this.delegate.needRemoveFromRecent(ContentPreviewViewer.this.currentDocument)) {
                        arrayList7.add(LocaleController.getString(R.string.RemoveFromRecent));
                        arrayList9.add(Integer.valueOf(R.drawable.msg_delete));
                        arrayList8.add(4);
                    }
                    final boolean isStickerInFavorites = MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).isStickerInFavorites(ContentPreviewViewer.this.currentDocument);
                    if (!MessageObject.isAnimatedEmoji(ContentPreviewViewer.this.currentDocument) && !MessageObject.isMaskDocument(ContentPreviewViewer.this.currentDocument) && (isStickerInFavorites || (MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).canAddStickerToFavorites() && MessageObject.isStickerHasSet(ContentPreviewViewer.this.currentDocument)))) {
                        arrayList7.add(LocaleController.getString(isStickerInFavorites ? R.string.DeleteFromFavorites : R.string.AddToFavorites));
                        arrayList9.add(Integer.valueOf(isStickerInFavorites ? R.drawable.msg_unfave : R.drawable.msg_fave));
                        arrayList8.add(5);
                    }
                    if (arrayList7.isEmpty()) {
                        return;
                    }
                    ContentPreviewViewer.this.menuVisible = true;
                    ContentPreviewViewer.this.containerView.invalidate();
                    int[] iArr2 = new int[arrayList9.size()];
                    for (int i17 = 0; i17 < arrayList9.size(); i17++) {
                        iArr2[i17] = ((Integer) arrayList9.get(i17)).intValue();
                    }
                    View.OnClickListener onClickListener3 = new View.OnClickListener() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda6
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ContentPreviewViewer.1.this.lambda$run$5(arrayList8, isStickerInFavorites, view);
                        }
                    };
                    int i18 = 0;
                    while (i18 < arrayList7.size()) {
                        ArrayList arrayList10 = arrayList9;
                        ArrayList arrayList11 = arrayList8;
                        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout3 = actionBarPopupWindowLayout2;
                        ActionBarMenuSubItem addItem3 = ActionBarMenuItem.addItem(i18 == 0, i18 == arrayList7.size() - 1, actionBarPopupWindowLayout2, ((Integer) arrayList9.get(i18)).intValue(), (CharSequence) arrayList7.get(i18), false, ContentPreviewViewer.this.resourcesProvider);
                        if (((Integer) arrayList11.get(i18)).intValue() == 4) {
                            addItem3.setIconColor(ContentPreviewViewer.this.getThemedColor(Theme.key_text_RedRegular));
                            addItem3.setTextColor(ContentPreviewViewer.this.getThemedColor(Theme.key_text_RedBold));
                        }
                        addItem3.setTag(Integer.valueOf(i18));
                        addItem3.setOnClickListener(onClickListener3);
                        i18++;
                        actionBarPopupWindowLayout2 = actionBarPopupWindowLayout3;
                        arrayList8 = arrayList11;
                        arrayList9 = arrayList10;
                    }
                    actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
                    ContentPreviewViewer.this.popupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2) { // from class: org.telegram.ui.ContentPreviewViewer.1.3
                        {
                            1.this = this;
                        }

                        @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
                        public void dismiss() {
                            super.dismiss();
                            ContentPreviewViewer contentPreviewViewer3 = ContentPreviewViewer.this;
                            contentPreviewViewer3.popupWindow = null;
                            contentPreviewViewer3.menuVisible = false;
                            if (ContentPreviewViewer.this.closeOnDismiss) {
                                ContentPreviewViewer.this.close();
                            }
                        }
                    };
                    ContentPreviewViewer.this.popupWindow.setPauseNotifications(true);
                    ContentPreviewViewer.this.popupWindow.setDismissAnimationDuration(150);
                    ContentPreviewViewer.this.popupWindow.setScaleOut(true);
                    ContentPreviewViewer.this.popupWindow.setOutsideTouchable(true);
                    ContentPreviewViewer.this.popupWindow.setClippingEnabled(true);
                    ContentPreviewViewer.this.popupWindow.setAnimationStyle(R.style.PopupContextAnimation);
                    ContentPreviewViewer.this.popupWindow.setFocusable(true);
                    actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
                    ContentPreviewViewer.this.popupWindow.setInputMethodMode(2);
                    ContentPreviewViewer.this.popupWindow.getContentView().setFocusableInTouchMode(true);
                    if (Build.VERSION.SDK_INT < 21 || ContentPreviewViewer.this.lastInsets == null) {
                        i4 = AndroidUtilities.statusBarHeight;
                        i5 = 0;
                    } else {
                        stableInsetBottom2 = ContentPreviewViewer.this.lastInsets.getStableInsetBottom();
                        stableInsetTop2 = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                        i5 = stableInsetBottom2 + stableInsetTop2;
                        i4 = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                    }
                    int min4 = Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i5) - AndroidUtilities.dp(40.0f);
                    float f2 = ContentPreviewViewer.this.moveY;
                    int i19 = i4 + (min4 / 2);
                    int dp2 = ContentPreviewViewer.this.stickerEmojiLayout != null ? AndroidUtilities.dp(40.0f) : 0;
                    ContentPreviewViewer contentPreviewViewer3 = ContentPreviewViewer.this;
                    contentPreviewViewer3.popupWindow.showAtLocation(contentPreviewViewer3.containerView, 0, (int) ((ContentPreviewViewer.this.containerView.getMeasuredWidth() - actionBarPopupWindowLayout.getMeasuredWidth()) / 2.0f), (int) (((int) (f2 + Math.max(i19 + dp2, ((ContentPreviewViewer.this.containerView.getHeight() - i5) - ContentPreviewViewer.this.keyboardHeight) / 2) + i6)) + (AndroidUtilities.dp(24.0f) - ContentPreviewViewer.this.moveY)));
                    ActionBarPopupWindow.startAnimation(actionBarPopupWindowLayout);
                    ContentPreviewViewer.this.containerView.performHapticFeedback(0);
                    if (ContentPreviewViewer.this.moveY != 0.0f) {
                        if (ContentPreviewViewer.this.finalMoveY == 0.0f) {
                            ContentPreviewViewer.this.finalMoveY = 0.0f;
                            ContentPreviewViewer contentPreviewViewer4 = ContentPreviewViewer.this;
                            contentPreviewViewer4.startMoveY = contentPreviewViewer4.moveY;
                        }
                        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.ContentPreviewViewer$1$$ExternalSyntheticLambda1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                ContentPreviewViewer.1.this.lambda$run$6(valueAnimator);
                            }
                        });
                        ofFloat2.setDuration(350L);
                        ofFloat2.setInterpolator(CubicBezierInterpolator.DEFAULT);
                        ofFloat2.start();
                    }
                }
                i9 = 0;
                while (i9 < actionBarPopupWindowLayout.getItemsCount()) {
                }
            } else if (MessageObject.isPremiumSticker(ContentPreviewViewer.this.currentDocument) && !AccountInstance.getInstance(ContentPreviewViewer.this.currentAccount).getUserConfig().isPremium()) {
                ContentPreviewViewer.this.showUnlockPremiumView();
                ContentPreviewViewer.this.menuVisible = true;
                ContentPreviewViewer.this.containerView.invalidate();
                ContentPreviewViewer.this.containerView.performHapticFeedback(0);
                return;
            } else {
                boolean isStickerInFavorites2 = MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).isStickerInFavorites(ContentPreviewViewer.this.currentDocument);
                ArrayList arrayList12 = new ArrayList();
                ArrayList arrayList13 = new ArrayList();
                ArrayList arrayList14 = new ArrayList();
                if (ContentPreviewViewer.this.delegate != null) {
                    if (ContentPreviewViewer.this.delegate.needSend(ContentPreviewViewer.this.currentContentType) && !ContentPreviewViewer.this.delegate.isInScheduleMode()) {
                        arrayList12.add(LocaleController.getString(R.string.SendStickerPreview));
                        arrayList14.add(Integer.valueOf(R.drawable.msg_send));
                        arrayList13.add(0);
                    }
                    if (ContentPreviewViewer.this.delegate.needSend(ContentPreviewViewer.this.currentContentType) && !ContentPreviewViewer.this.delegate.isInScheduleMode()) {
                        arrayList12.add(LocaleController.getString(R.string.SendWithoutSound));
                        arrayList14.add(Integer.valueOf(R.drawable.input_notify_off));
                        arrayList13.add(6);
                    }
                    if (ContentPreviewViewer.this.delegate.canSchedule()) {
                        arrayList12.add(LocaleController.getString(R.string.Schedule));
                        arrayList14.add(Integer.valueOf(R.drawable.msg_autodelete));
                        arrayList13.add(3);
                    }
                    if (ContentPreviewViewer.this.currentStickerSet != null && !(ContentPreviewViewer.this.currentStickerSet instanceof TLRPC$TL_inputStickerSetEmpty) && ContentPreviewViewer.this.delegate.needOpen()) {
                        arrayList12.add(LocaleController.formatString("ViewPackPreview", R.string.ViewPackPreview, new Object[0]));
                        arrayList14.add(Integer.valueOf(R.drawable.msg_media));
                        arrayList13.add(1);
                    }
                    if (ContentPreviewViewer.this.delegate.needRemove()) {
                        arrayList12.add(LocaleController.getString(R.string.ImportStickersRemoveMenu));
                        arrayList14.add(Integer.valueOf(R.drawable.msg_delete));
                        arrayList13.add(5);
                    }
                }
                if (!MessageObject.isMaskDocument(ContentPreviewViewer.this.currentDocument) && (isStickerInFavorites2 || (MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).canAddStickerToFavorites() && MessageObject.isStickerHasSet(ContentPreviewViewer.this.currentDocument)))) {
                    arrayList12.add(LocaleController.getString(isStickerInFavorites2 ? R.string.DeleteFromFavorites : R.string.AddToFavorites));
                    arrayList14.add(Integer.valueOf(isStickerInFavorites2 ? R.drawable.msg_unfave : R.drawable.msg_fave));
                    arrayList13.add(2);
                }
                if (ContentPreviewViewer.this.isRecentSticker) {
                    arrayList12.add(LocaleController.getString(R.string.DeleteFromRecent));
                    arrayList14.add(Integer.valueOf(R.drawable.msg_delete));
                    arrayList13.add(4);
                }
                if (ContentPreviewViewer.this.currentStickerSet != null && ContentPreviewViewer.this.currentDocument != null && (stickerSet = MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).getStickerSet(ContentPreviewViewer.this.currentStickerSet, true)) != null && stickerSet.set.creator) {
                    if (ContentPreviewViewer.this.delegate != null && ContentPreviewViewer.this.delegate.canEditSticker()) {
                        TLRPC$StickerSet tLRPC$StickerSet = stickerSet.set;
                        if (!tLRPC$StickerSet.emojis && !tLRPC$StickerSet.masks) {
                            arrayList12.add(LocaleController.getString(R.string.EditSticker));
                            arrayList14.add(Integer.valueOf(R.drawable.msg_edit));
                            arrayList13.add(7);
                        }
                    }
                    if (ContentPreviewViewer.this.delegate != null && ContentPreviewViewer.this.delegate.canDeleteSticker(ContentPreviewViewer.this.currentDocument)) {
                        arrayList12.add(LocaleController.getString(R.string.DeleteSticker));
                        arrayList14.add(Integer.valueOf(R.drawable.msg_delete));
                        arrayList13.add(8);
                    }
                }
                if (arrayList12.isEmpty()) {
                    return;
                }
                ContentPreviewViewer.this.menuVisible = true;
                ContentPreviewViewer.this.containerView.invalidate();
                1 r1 = new 1(arrayList13, isStickerInFavorites2);
                for (int i20 = 0; i20 < arrayList12.size(); i20++) {
                    ActionBarMenuSubItem addItem4 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout2, ((Integer) arrayList14.get(i20)).intValue(), (CharSequence) arrayList12.get(i20), false, ContentPreviewViewer.this.resourcesProvider);
                    addItem4.setTag(Integer.valueOf(i20));
                    addItem4.setOnClickListener(r1);
                    if (((Integer) arrayList13.get(i20)).intValue() == 8) {
                        int themedColor = ContentPreviewViewer.this.getThemedColor(Theme.key_text_RedBold);
                        addItem4.setColors(themedColor, themedColor);
                        addItem4.setSelectorColor(Theme.multAlpha(themedColor, 0.1f));
                    }
                }
                ContentPreviewViewer.this.popupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout2, -2, -2) { // from class: org.telegram.ui.ContentPreviewViewer.1.2
                    {
                        1.this = this;
                    }

                    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
                    public void dismiss() {
                        super.dismiss();
                        ContentPreviewViewer contentPreviewViewer5 = ContentPreviewViewer.this;
                        contentPreviewViewer5.popupWindow = null;
                        contentPreviewViewer5.menuVisible = false;
                        if (ContentPreviewViewer.this.closeOnDismiss) {
                            ContentPreviewViewer.this.close();
                        }
                        if (ContentPreviewViewer.this.currentPreviewCell != null) {
                            if (ContentPreviewViewer.this.currentPreviewCell instanceof StickerEmojiCell) {
                                ((StickerEmojiCell) ContentPreviewViewer.this.currentPreviewCell).setScaled(false);
                            } else if (ContentPreviewViewer.this.currentPreviewCell instanceof StickerCell) {
                                ((StickerCell) ContentPreviewViewer.this.currentPreviewCell).setScaled(false);
                            } else if (ContentPreviewViewer.this.currentPreviewCell instanceof ContextLinkCell) {
                                ((ContextLinkCell) ContentPreviewViewer.this.currentPreviewCell).setScaled(false);
                            }
                            ContentPreviewViewer.this.currentPreviewCell = null;
                        }
                    }
                };
                ContentPreviewViewer.this.popupWindow.setPauseNotifications(true);
                ContentPreviewViewer.this.popupWindow.setDismissAnimationDuration(100);
                ContentPreviewViewer.this.popupWindow.setScaleOut(true);
                ContentPreviewViewer.this.popupWindow.setOutsideTouchable(true);
                ContentPreviewViewer.this.popupWindow.setClippingEnabled(true);
                ContentPreviewViewer.this.popupWindow.setAnimationStyle(R.style.PopupContextAnimation);
                ContentPreviewViewer.this.popupWindow.setFocusable(true);
                actionBarPopupWindowLayout2.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
                ContentPreviewViewer.this.popupWindow.setInputMethodMode(2);
                ContentPreviewViewer.this.popupWindow.getContentView().setFocusableInTouchMode(true);
                if (Build.VERSION.SDK_INT < 21 || ContentPreviewViewer.this.lastInsets == null) {
                    i7 = AndroidUtilities.statusBarHeight;
                    i8 = 0;
                } else {
                    stableInsetBottom3 = ContentPreviewViewer.this.lastInsets.getStableInsetBottom();
                    stableInsetTop3 = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                    i8 = stableInsetBottom3 + stableInsetTop3;
                    i7 = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                }
                int max = ((int) (ContentPreviewViewer.this.moveY + Math.max(i7 + min + (ContentPreviewViewer.this.stickerEmojiLayout != null ? AndroidUtilities.dp(40.0f) : 0), ((ContentPreviewViewer.this.containerView.getHeight() - i8) - ContentPreviewViewer.this.keyboardHeight) / 2) + ((ContentPreviewViewer.this.currentContentType == 1 ? Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i8) - AndroidUtilities.dp(40.0f) : (int) (ContentPreviewViewer.this.drawEffect ? Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i8) - AndroidUtilities.dpf2(40.0f) : Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i8) / 1.8f)) / 2))) + AndroidUtilities.dp(24.0f);
                if (ContentPreviewViewer.this.drawEffect) {
                    max += AndroidUtilities.dp(24.0f);
                }
                ContentPreviewViewer contentPreviewViewer5 = ContentPreviewViewer.this;
                contentPreviewViewer5.popupWindow.showAtLocation(contentPreviewViewer5.containerView, 0, (int) ((ContentPreviewViewer.this.containerView.getMeasuredWidth() - actionBarPopupWindowLayout2.getMeasuredWidth()) / 2.0f), max);
                ContentPreviewViewer.this.containerView.performHapticFeedback(0);
            }
            actionBarPopupWindowLayout = actionBarPopupWindowLayout2;
            i9 = 0;
            while (i9 < actionBarPopupWindowLayout.getItemsCount()) {
            }
        }
    }

    /* loaded from: classes4.dex */
    public interface ContentPreviewViewerDelegate {

        /* loaded from: classes4.dex */
        public abstract /* synthetic */ class -CC {
            public static void $default$addToFavoriteSelected(ContentPreviewViewerDelegate contentPreviewViewerDelegate, String str) {
            }

            public static boolean $default$can(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return true;
            }

            public static boolean $default$canDeleteSticker(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document) {
                return false;
            }

            public static boolean $default$canEditSticker(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return false;
            }

            public static boolean $default$canSchedule(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return false;
            }

            public static Boolean $default$canSetAsStatus(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document) {
                return null;
            }

            public static void $default$copyEmoji(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document) {
            }

            public static void $default$deleteSticker(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document) {
            }

            public static void $default$editSticker(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document) {
            }

            public static String $default$getQuery(ContentPreviewViewerDelegate contentPreviewViewerDelegate, boolean z) {
                return null;
            }

            public static void $default$gifAddedOrDeleted(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
            }

            public static boolean $default$isInScheduleMode(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return false;
            }

            public static boolean $default$isPhotoEditor(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return false;
            }

            public static boolean $default$isReplacedSticker(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return false;
            }

            public static boolean $default$isSettingIntroSticker(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return false;
            }

            public static boolean $default$isStickerEditor(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return false;
            }

            public static boolean $default$needCopy(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document) {
                return false;
            }

            public static boolean $default$needMenu(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return true;
            }

            public static boolean $default$needOpen(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return true;
            }

            public static boolean $default$needRemove(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
                return false;
            }

            public static boolean $default$needRemoveFromRecent(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document) {
                return false;
            }

            public static boolean $default$needSend(ContentPreviewViewerDelegate contentPreviewViewerDelegate, int i) {
                return false;
            }

            public static void $default$newStickerPackSelected(ContentPreviewViewerDelegate contentPreviewViewerDelegate, CharSequence charSequence, String str, Utilities.Callback callback) {
            }

            public static void $default$openSet(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$InputStickerSet tLRPC$InputStickerSet, boolean z) {
            }

            public static void $default$remove(ContentPreviewViewerDelegate contentPreviewViewerDelegate, SendMessagesHelper.ImportingSticker importingSticker) {
            }

            public static void $default$removeFromRecent(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document) {
            }

            public static void $default$resetTouch(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
            }

            public static void $default$sendEmoji(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document) {
            }

            public static void $default$sendGif(ContentPreviewViewerDelegate contentPreviewViewerDelegate, Object obj, Object obj2, boolean z, int i) {
            }

            public static void $default$sendSticker(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
            }

            public static void $default$sendSticker(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document, String str, Object obj, boolean z, int i) {
            }

            public static void $default$setAsEmojiStatus(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document, Integer num) {
            }

            public static void $default$setIntroSticker(ContentPreviewViewerDelegate contentPreviewViewerDelegate, String str) {
            }

            public static void $default$stickerSetSelected(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$StickerSet tLRPC$StickerSet, String str) {
            }
        }

        void addToFavoriteSelected(String str);

        boolean can();

        boolean canDeleteSticker(TLRPC$Document tLRPC$Document);

        boolean canEditSticker();

        boolean canSchedule();

        Boolean canSetAsStatus(TLRPC$Document tLRPC$Document);

        void copyEmoji(TLRPC$Document tLRPC$Document);

        void deleteSticker(TLRPC$Document tLRPC$Document);

        void editSticker(TLRPC$Document tLRPC$Document);

        long getDialogId();

        String getQuery(boolean z);

        void gifAddedOrDeleted();

        boolean isInScheduleMode();

        boolean isPhotoEditor();

        boolean isReplacedSticker();

        boolean isSettingIntroSticker();

        boolean isStickerEditor();

        boolean needCopy(TLRPC$Document tLRPC$Document);

        boolean needMenu();

        boolean needOpen();

        boolean needRemove();

        boolean needRemoveFromRecent(TLRPC$Document tLRPC$Document);

        boolean needSend(int i);

        void newStickerPackSelected(CharSequence charSequence, String str, Utilities.Callback callback);

        void openSet(TLRPC$InputStickerSet tLRPC$InputStickerSet, boolean z);

        void remove(SendMessagesHelper.ImportingSticker importingSticker);

        void removeFromRecent(TLRPC$Document tLRPC$Document);

        void resetTouch();

        void sendEmoji(TLRPC$Document tLRPC$Document);

        void sendGif(Object obj, Object obj2, boolean z, int i);

        void sendSticker();

        void sendSticker(TLRPC$Document tLRPC$Document, String str, Object obj, boolean z, int i);

        void setAsEmojiStatus(TLRPC$Document tLRPC$Document, Integer num);

        void setIntroSticker(String str);

        void stickerSetSelected(TLRPC$StickerSet tLRPC$StickerSet, String str);
    }

    /* loaded from: classes4.dex */
    public class FrameLayoutDrawer extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public FrameLayoutDrawer(Context context) {
            super(context);
            ContentPreviewViewer.this = r1;
            setWillNotDraw(false);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view instanceof PaintingOverlay) {
                return false;
            }
            return super.drawChild(canvas, view, j);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            ContentPreviewViewer.this.onDraw(canvas);
        }
    }

    /* loaded from: classes4.dex */
    public static class StickerPackNameView extends LinearLayout {
        private TLRPC$StickerSetCovered cover;
        private final BackupImageView imageView;
        private final Theme.ResourcesProvider resourcesProvider;
        private final SimpleTextView textView;

        public StickerPackNameView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            BackupImageView backupImageView = new BackupImageView(context);
            this.imageView = backupImageView;
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.textView = simpleTextView;
            simpleTextView.setTextSize(16);
            simpleTextView.setTextColor(-1);
            setOrientation(0);
            addView(backupImageView, LayoutHelper.createLinear(24, 24, 17, 17, 0, 17, 0));
            addView(simpleTextView, LayoutHelper.createLinear(-2, -2, 17, 0, 0, 12, 0));
        }

        public void bind(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
            String str;
            BackupImageView backupImageView;
            ImageLocation imageLocation;
            String str2;
            this.cover = tLRPC$StickerSetCovered;
            if (tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetNoCovered) {
                this.textView.setText(LocaleController.getString(R.string.NewStickerPack));
                this.imageView.setImageResource(R.drawable.msg_addbot);
                return;
            }
            this.textView.setText(tLRPC$StickerSetCovered.set.title);
            TLRPC$Document tLRPC$Document = tLRPC$StickerSetCovered.cover;
            if (tLRPC$Document == null) {
                this.imageView.setImage((ImageLocation) null, (String) null, (ImageLocation) null, (String) null, (Drawable) null, (Object) 0);
                return;
            }
            TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 90);
            SvgHelper.SvgDrawable svgThumb = DocumentObject.getSvgThumb(tLRPC$StickerSetCovered.cover, Theme.key_windowBackgroundGray, 1.0f, 1.0f, this.resourcesProvider);
            if (svgThumb == null) {
                BackupImageView backupImageView2 = this.imageView;
                ImageLocation forDocument = ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$StickerSetCovered.cover);
                str = "webp";
                svgThumb = null;
                backupImageView = backupImageView2;
                imageLocation = forDocument;
                str2 = null;
            } else if (closestPhotoSizeWithSize != null) {
                BackupImageView backupImageView3 = this.imageView;
                ImageLocation forDocument2 = ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$StickerSetCovered.cover);
                backupImageView = backupImageView3;
                imageLocation = forDocument2;
                str2 = null;
                str = "webp";
            } else {
                backupImageView = this.imageView;
                imageLocation = ImageLocation.getForDocument(tLRPC$StickerSetCovered.cover);
                str2 = null;
                str = "webp";
            }
            backupImageView.setImage(imageLocation, str2, str, svgThumb, tLRPC$StickerSetCovered);
        }

        public TLRPC$StickerSetCovered getCover() {
            return this.cover;
        }
    }

    public void dismissPopupWindow() {
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
            this.popupWindow = null;
            return;
        }
        View view = this.popupLayout;
        if (view != null) {
            view.animate().alpha(0.0f).scaleX(0.8f).scaleY(0.8f).translationY(AndroidUtilities.dp(-12.0f)).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).setDuration(320L).start();
            this.popupLayout = null;
            this.menuVisible = false;
            if (this.closeOnDismiss) {
                close();
            }
        }
    }

    public static ContentPreviewViewer getInstance() {
        ContentPreviewViewer contentPreviewViewer = Instance;
        if (contentPreviewViewer == null) {
            synchronized (PhotoViewer.class) {
                try {
                    contentPreviewViewer = Instance;
                    if (contentPreviewViewer == null) {
                        contentPreviewViewer = new ContentPreviewViewer();
                        Instance = contentPreviewViewer;
                    }
                } finally {
                }
            }
        }
        return contentPreviewViewer;
    }

    private void getMyStickersRemote(final TLRPC$TL_messages_getMyStickers tLRPC$TL_messages_getMyStickers, final List list) {
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_getMyStickers, new RequestDelegate() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda5
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                ContentPreviewViewer.this.lambda$getMyStickersRemote$12(list, tLRPC$TL_messages_getMyStickers, tLObject, tLRPC$TL_error);
            }
        });
    }

    public int getThemedColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    public static boolean hasInstance() {
        return Instance != null;
    }

    public /* synthetic */ void lambda$close$8() {
        this.resourcesProvider = null;
    }

    public /* synthetic */ void lambda$getMyStickersRemote$11(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, List list, TLRPC$TL_messages_getMyStickers tLRPC$TL_messages_getMyStickers) {
        if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_messages_myStickers)) {
            TLRPC$TL_messages_myStickers tLRPC$TL_messages_myStickers = (TLRPC$TL_messages_myStickers) tLObject;
            Iterator it = tLRPC$TL_messages_myStickers.sets.iterator();
            while (it.hasNext()) {
                TLRPC$StickerSetCovered tLRPC$StickerSetCovered = (TLRPC$StickerSetCovered) it.next();
                TLRPC$StickerSet tLRPC$StickerSet = tLRPC$StickerSetCovered.set;
                if (!tLRPC$StickerSet.emojis && !tLRPC$StickerSet.masks) {
                    TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
                    tLRPC$TL_inputStickerSetID.id = tLRPC$StickerSetCovered.set.id;
                    TLRPC$TL_messages_stickerSet stickerSet = MediaDataController.getInstance(this.currentAccount).getStickerSet(tLRPC$TL_inputStickerSetID, true);
                    if (stickerSet == null || stickerSet.documents.size() < 120) {
                        list.add(tLRPC$StickerSetCovered);
                    }
                }
            }
            if (tLRPC$TL_messages_myStickers.sets.size() == tLRPC$TL_messages_getMyStickers.limit) {
                ArrayList arrayList = tLRPC$TL_messages_myStickers.sets;
                tLRPC$TL_messages_getMyStickers.offset_id = ((TLRPC$StickerSetCovered) arrayList.get(arrayList.size() - 1)).set.id;
                getMyStickersRemote(tLRPC$TL_messages_getMyStickers, list);
            }
        }
    }

    public /* synthetic */ void lambda$getMyStickersRemote$12(final List list, final TLRPC$TL_messages_getMyStickers tLRPC$TL_messages_getMyStickers, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ContentPreviewViewer.this.lambda$getMyStickersRemote$11(tLRPC$TL_error, tLObject, list, tLRPC$TL_messages_getMyStickers);
            }
        });
    }

    public /* synthetic */ void lambda$onDraw$9() {
        this.centerImage.setImageBitmap((Bitmap) null);
        PaintingOverlay paintingOverlay = this.paintingOverlay;
        if (paintingOverlay != null) {
            paintingOverlay.reset();
            this.containerView.removeView(this.paintingOverlay);
            this.paintingOverlay = null;
        }
    }

    public /* synthetic */ void lambda$onInterceptTouchEvent$5(RecyclerListView recyclerListView, int i, Theme.ResourcesProvider resourcesProvider) {
        TLRPC$Document tLRPC$Document;
        if (this.openPreviewRunnable == null) {
            return;
        }
        recyclerListView.setOnItemClickListener((RecyclerListView.OnItemClickListener) null);
        recyclerListView.requestDisallowInterceptTouchEvent(true);
        this.openPreviewRunnable = null;
        setParentActivity(AndroidUtilities.findActivity(recyclerListView.getContext()));
        this.clearsInputField = false;
        View view = this.currentPreviewCell;
        if (view instanceof StickerEmojiCell) {
            StickerEmojiCell stickerEmojiCell = (StickerEmojiCell) view;
            TLRPC$Document sticker = stickerEmojiCell.getSticker();
            SendMessagesHelper.ImportingSticker stickerPath = stickerEmojiCell.getStickerPath();
            String findAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(stickerEmojiCell.getSticker(), null, Integer.valueOf(this.currentAccount));
            ContentPreviewViewerDelegate contentPreviewViewerDelegate = this.delegate;
            open(sticker, stickerPath, findAnimatedEmojiEmoticon, contentPreviewViewerDelegate != null ? contentPreviewViewerDelegate.getQuery(false) : null, null, i, stickerEmojiCell.isRecent(), stickerEmojiCell.getParentObject(), this.resourcesProvider);
            stickerEmojiCell.setScaled(true);
        } else if (view instanceof StickerCell) {
            StickerCell stickerCell = (StickerCell) view;
            TLRPC$Document sticker2 = stickerCell.getSticker();
            ContentPreviewViewerDelegate contentPreviewViewerDelegate2 = this.delegate;
            open(sticker2, null, null, contentPreviewViewerDelegate2 != null ? contentPreviewViewerDelegate2.getQuery(false) : null, null, i, false, stickerCell.getParentObject(), resourcesProvider);
            stickerCell.setScaled(true);
            this.clearsInputField = stickerCell.isClearsInputField();
        } else if (view instanceof ContextLinkCell) {
            ContextLinkCell contextLinkCell = (ContextLinkCell) view;
            TLRPC$Document document = contextLinkCell.getDocument();
            ContentPreviewViewerDelegate contentPreviewViewerDelegate3 = this.delegate;
            open(document, null, null, contentPreviewViewerDelegate3 != null ? contentPreviewViewerDelegate3.getQuery(true) : null, contextLinkCell.getBotInlineResult(), i, false, contextLinkCell.getBotInlineResult() != null ? contextLinkCell.getInlineBot() : contextLinkCell.getParentObject(), resourcesProvider);
            if (i != 1 || this.isPhotoEditor) {
                contextLinkCell.setScaled(true);
            }
        } else {
            if (view instanceof EmojiPacksAlert.EmojiImageView) {
                tLRPC$Document = ((EmojiPacksAlert.EmojiImageView) view).getDocument();
                if (tLRPC$Document == null) {
                    return;
                }
            } else if (view instanceof EmojiView.ImageViewEmoji) {
                AnimatedEmojiSpan span = ((EmojiView.ImageViewEmoji) view).getSpan();
                if (span != null) {
                    tLRPC$Document = span.document;
                    if (tLRPC$Document == null) {
                        tLRPC$Document = AnimatedEmojiDrawable.findDocument(this.currentAccount, span.getDocumentId());
                    }
                } else {
                    tLRPC$Document = null;
                }
                if (tLRPC$Document == null) {
                    return;
                }
            } else if (!(view instanceof SuggestEmojiView.EmojiImageView)) {
                return;
            } else {
                Drawable drawable = ((SuggestEmojiView.EmojiImageView) view).drawable;
                TLRPC$Document document2 = drawable instanceof AnimatedEmojiDrawable ? ((AnimatedEmojiDrawable) drawable).getDocument() : null;
                if (document2 == null) {
                    return;
                }
                tLRPC$Document = document2;
            }
            open(tLRPC$Document, null, MessageObject.findAnimatedEmojiEmoticon(tLRPC$Document, null, Integer.valueOf(this.currentAccount)), null, null, i, false, null, resourcesProvider);
        }
        this.currentPreviewCell.performHapticFeedback(0, 2);
        ContentPreviewViewerDelegate contentPreviewViewerDelegate4 = this.delegate;
        if (contentPreviewViewerDelegate4 != null) {
            contentPreviewViewerDelegate4.resetTouch();
        }
    }

    public static /* synthetic */ void lambda$onTouch$4(RecyclerListView recyclerListView, Object obj) {
        if (recyclerListView instanceof RecyclerListView) {
            recyclerListView.setOnItemClickListener((RecyclerListView.OnItemClickListener) obj);
        }
    }

    public /* synthetic */ void lambda$prepareBlurBitmap$10(Bitmap bitmap) {
        this.blurrBitmap = bitmap;
        this.preparingBitmap = false;
        FrameLayoutDrawer frameLayoutDrawer = this.containerView;
        if (frameLayoutDrawer != null) {
            frameLayoutDrawer.invalidate();
        }
    }

    public /* synthetic */ WindowInsets lambda$setParentActivity$6(View view, WindowInsets windowInsets) {
        this.lastInsets = windowInsets;
        return windowInsets;
    }

    public /* synthetic */ boolean lambda$setParentActivity$7(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 6 || motionEvent.getAction() == 3) {
            if (this.isStickerEditor) {
                closeWithMenu();
            } else {
                close();
            }
        }
        return true;
    }

    public /* synthetic */ void lambda$showEmojiSelectorForStickers$0(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
        if (visibleReaction == null) {
            return;
        }
        CustomEmojiReactionsWindow reactionsWindow = this.reactionsLayout.getReactionsWindow();
        if (!this.selectedEmojis.contains(visibleReaction.emojicon)) {
            this.selectedEmojis.add(visibleReaction.emojicon);
            if (this.selectedEmojis.size() > 7) {
                this.selectedEmojis.remove(0);
            }
        } else if (this.selectedEmojis.size() <= 1) {
            return;
        } else {
            this.selectedEmojis.remove(visibleReaction.emojicon);
        }
        this.reactionsLayout.setSelectedEmojis(this.selectedEmojis);
        if (reactionsWindow != null) {
            this.reactionsLayout.setMessage(null, null, false);
            if (reactionsWindow.getSelectAnimatedEmojiDialog() != null) {
                reactionsWindow.getSelectAnimatedEmojiDialog().setSelectedReactions(this.selectedEmojis);
                reactionsWindow.getSelectAnimatedEmojiDialog().setRecentReactions(this.reactionsLayout.allReactionsList);
            }
            reactionsWindow.dismiss();
        }
    }

    public /* synthetic */ void lambda$showEmojiSelectorForStickers$1() {
        this.reactionsLayoutContainer.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(420L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
    }

    public /* synthetic */ void lambda$showUnlockPremiumView$2(View view) {
        this.menuVisible = false;
        this.containerView.invalidate();
        close();
    }

    public /* synthetic */ void lambda$showUnlockPremiumView$3(View view) {
        Activity activity = this.parentActivity;
        if (activity instanceof LaunchActivity) {
            LaunchActivity launchActivity = (LaunchActivity) activity;
            if (launchActivity.getActionBarLayout() != null && launchActivity.getActionBarLayout().getLastFragment() != null) {
                launchActivity.getActionBarLayout().getLastFragment().dismissCurrentDialog();
            }
            launchActivity.lambda$runLinkRequest$91(new PremiumPreviewFragment(PremiumPreviewFragment.featureTypeToServerString(5)));
        }
        this.menuVisible = false;
        this.containerView.invalidate();
        close();
    }

    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        int min;
        ImageReceiver imageReceiver;
        StaticLayout staticLayout;
        Drawable drawable;
        WindowInsets windowInsets;
        int stableInsetBottom;
        int stableInsetTop;
        float f;
        if (this.containerView == null || this.backgroundDrawable == null) {
            return;
        }
        if (this.menuVisible && this.blurrBitmap == null) {
            prepareBlurBitmap();
        }
        if (this.blurrBitmap != null) {
            boolean z = this.menuVisible;
            if (z) {
                float f2 = this.blurProgress;
                if (f2 != 1.0f) {
                    float f3 = f2 + 0.13333334f;
                    this.blurProgress = f3;
                    if (f3 > 1.0f) {
                        this.blurProgress = 1.0f;
                    }
                    this.containerView.invalidate();
                    f = this.blurProgress;
                    if (f != 0.0f && this.blurrBitmap != null) {
                        this.paint.setAlpha((int) (f * 255.0f));
                        canvas.save();
                        canvas.scale(12.0f, 12.0f);
                        canvas.drawColor(Theme.multAlpha(Theme.getColor(Theme.key_windowBackgroundGray, this.resourcesProvider), this.blurProgress));
                        canvas.drawBitmap(this.blurrBitmap, 0.0f, 0.0f, this.paint);
                        canvas.restore();
                    }
                }
            }
            if (!z) {
                float f4 = this.blurProgress;
                if (f4 != 0.0f) {
                    float f5 = f4 - 0.13333334f;
                    this.blurProgress = f5;
                    if (f5 < 0.0f) {
                        this.blurProgress = 0.0f;
                    }
                    this.containerView.invalidate();
                }
            }
            f = this.blurProgress;
            if (f != 0.0f) {
                this.paint.setAlpha((int) (f * 255.0f));
                canvas.save();
                canvas.scale(12.0f, 12.0f);
                canvas.drawColor(Theme.multAlpha(Theme.getColor(Theme.key_windowBackgroundGray, this.resourcesProvider), this.blurProgress));
                canvas.drawBitmap(this.blurrBitmap, 0.0f, 0.0f, this.paint);
                canvas.restore();
            }
        }
        this.backgroundDrawable.setAlpha((int) (this.showProgress * 180.0f));
        this.backgroundDrawable.setBounds(0, 0, this.containerView.getWidth(), this.containerView.getHeight());
        this.backgroundDrawable.draw(canvas);
        canvas.save();
        if (Build.VERSION.SDK_INT < 21 || (windowInsets = this.lastInsets) == null) {
            i = AndroidUtilities.statusBarHeight;
            i2 = 0;
        } else {
            stableInsetBottom = windowInsets.getStableInsetBottom();
            stableInsetTop = this.lastInsets.getStableInsetTop();
            i2 = stableInsetBottom + stableInsetTop;
            i = this.lastInsets.getStableInsetTop();
        }
        if (this.currentContentType == 1) {
            min = Math.min(this.containerView.getWidth(), this.containerView.getHeight() - i2) - AndroidUtilities.dp(40.0f);
        } else {
            min = (int) (this.drawEffect ? Math.min(this.containerView.getWidth(), this.containerView.getHeight() - i2) - AndroidUtilities.dpf2(40.0f) : Math.min(this.containerView.getWidth(), this.containerView.getHeight() - i2) / 1.8f);
        }
        float max = Math.max((min / 2) + i + (this.stickerEmojiLayout != null ? AndroidUtilities.dp(40.0f) : 0), ((this.containerView.getHeight() - i2) - this.keyboardHeight) / 2);
        if (this.drawEffect) {
            max += AndroidUtilities.dp(40.0f);
        }
        canvas.translate(this.containerView.getWidth() / 2, this.moveY + max);
        int i3 = (int) (min * ((this.showProgress * 0.8f) / 0.8f));
        if (this.currentContentType == 3) {
            canvas.translate(0.0f, AndroidUtilities.dp(70.0f));
        }
        if (this.drawEffect) {
            float f6 = i3;
            float f7 = 0.6669f * f6;
            this.centerImage.setAlpha(this.showProgress);
            float f8 = f6 - f7;
            float f9 = f6 / 2.0f;
            this.centerImage.setImageCoords((f8 - f9) - (0.0546875f * f6), (f8 / 2.0f) - f9, f7, f7);
            this.centerImage.draw(canvas);
            this.effectImage.setAlpha(this.showProgress);
            float f10 = (-i3) / 2.0f;
            this.effectImage.setImageCoords(f10, f10, f6, f6);
            imageReceiver = this.effectImage;
        } else {
            this.centerImage.setAlpha(this.showProgress);
            float f11 = (-i3) / 2.0f;
            float f12 = i3;
            this.centerImage.setImageCoords(f11, f11, f12, f12);
            imageReceiver = this.centerImage;
        }
        imageReceiver.draw(canvas);
        if (this.paintingOverlay != null) {
            canvas.save();
            float f13 = (-i3) / 2.0f;
            canvas.translate(f13, f13);
            float f14 = i3;
            canvas.scale(f14 / this.paintingOverlay.getWidth(), f14 / this.paintingOverlay.getHeight());
            this.paintingOverlay.setAlpha(this.showProgress);
            if (this.paintingOverlayClipPath == null) {
                this.paintingOverlayClipPath = new Path();
            }
            this.paintingOverlayClipPath.rewind();
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, this.paintingOverlay.getWidth(), this.paintingOverlay.getHeight());
            float f15 = f14 / 8.0f;
            this.paintingOverlayClipPath.addRoundRect(rectF, f15, f15, Path.Direction.CW);
            canvas.clipPath(this.paintingOverlayClipPath);
            this.paintingOverlay.draw(canvas);
            canvas.restore();
        }
        if (this.currentContentType == 1 && !this.isPhotoEditor && (drawable = this.slideUpDrawable) != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = this.slideUpDrawable.getIntrinsicHeight();
            int dp = (int) (this.centerImage.getDrawRegion().top - AndroidUtilities.dp(((this.currentMoveY / AndroidUtilities.dp(60.0f)) * 6.0f) + 17.0f));
            this.slideUpDrawable.setAlpha((int) ((1.0f - this.currentMoveYProgress) * 255.0f));
            this.slideUpDrawable.setBounds((-intrinsicWidth) / 2, (-intrinsicHeight) + dp, intrinsicWidth / 2, dp);
            this.slideUpDrawable.draw(canvas);
        }
        if (this.stickerEmojiLayout != null) {
            canvas.translate((-staticLayout.getWidth()) / 2.0f, ((-(this.drawEffect ? this.effectImage : this.centerImage).getImageHeight()) / 2.0f) - AndroidUtilities.dp(30.0f));
            textPaint.setAlpha((int) (this.showProgress * 255.0f));
            this.stickerEmojiLayout.draw(canvas);
        }
        canvas.restore();
        if (this.isVisible) {
            if (this.showProgress != 1.0f) {
                long currentTimeMillis = System.currentTimeMillis();
                this.lastUpdateTime = currentTimeMillis;
                this.showProgress += ((float) (currentTimeMillis - this.lastUpdateTime)) / 120.0f;
                this.containerView.invalidate();
                if (this.showProgress > 1.0f) {
                    this.showProgress = 1.0f;
                }
            }
        } else if (this.showProgress != 0.0f) {
            long currentTimeMillis2 = System.currentTimeMillis();
            this.lastUpdateTime = currentTimeMillis2;
            this.showProgress -= ((float) (currentTimeMillis2 - this.lastUpdateTime)) / 120.0f;
            this.containerView.invalidate();
            if (this.showProgress < 0.0f) {
                this.showProgress = 0.0f;
            }
            if (this.showProgress == 0.0f) {
                this.centerImage.setImageBitmap((Drawable) null);
                AndroidUtilities.unlockOrientation(this.parentActivity);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        ContentPreviewViewer.this.lambda$onDraw$9();
                    }
                });
                Bitmap bitmap = this.blurrBitmap;
                if (bitmap != null) {
                    bitmap.recycle();
                    this.blurrBitmap = null;
                }
                AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, false, 1.0f, false);
                this.blurProgress = 0.0f;
                try {
                    if (this.windowView.getParent() != null) {
                        ((WindowManager) this.parentActivity.getSystemService("window")).removeView(this.windowView);
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
    }

    private void prepareBlurBitmap() {
        if (this.parentActivity == null || this.preparingBitmap) {
            return;
        }
        this.preparingBitmap = true;
        AndroidUtilities.makeGlobalBlurBitmap(new Utilities.Callback() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda12
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ContentPreviewViewer.this.lambda$prepareBlurBitmap$10((Bitmap) obj);
            }
        }, 12.0f);
    }

    private float rubberYPoisition(float f, float f2) {
        return (-((1.0f - (1.0f / (((Math.abs(f) * 0.55f) / f2) + 1.0f))) * f2)) * (f >= 0.0f ? -1.0f : 1.0f);
    }

    public void showEmojiSelectorForStickers() {
        if (this.reactionsLayout == null) {
            ReactionsContainerLayout reactionsContainerLayout = new ReactionsContainerLayout(4, null, this.containerView.getContext(), UserConfig.selectedAccount, this.resourcesProvider) { // from class: org.telegram.ui.ContentPreviewViewer.2
                {
                    ContentPreviewViewer.this = this;
                }

                @Override // org.telegram.ui.Components.ReactionsContainerLayout
                public void invalidateLoopViews() {
                    super.invalidateLoopViews();
                    ContentPreviewViewer.this.setFocusable(getReactionsWindow() != null);
                }
            };
            this.reactionsLayout = reactionsContainerLayout;
            reactionsContainerLayout.skipEnterAnimation = true;
            reactionsContainerLayout.setPadding(0, AndroidUtilities.dp(22.0f), 0, AndroidUtilities.dp(22.0f));
            this.reactionsLayout.setClipChildren(false);
            this.reactionsLayout.setClipToPadding(false);
            this.reactionsLayout.setVisibility(0);
            this.reactionsLayout.setHint(LocaleController.getString(R.string.StickersSetEmojiForSticker));
            this.reactionsLayout.setBubbleOffset(-AndroidUtilities.dp(105.0f));
            this.reactionsLayout.setMiniBubblesOffset(-AndroidUtilities.dp(14.0f));
            FrameLayout frameLayout = new FrameLayout(this.containerView.getContext());
            this.reactionsLayoutContainer = frameLayout;
            frameLayout.addView(this.reactionsLayout, LayoutHelper.createFrame(-2, 116.0f, 1, 0.0f, 0.0f, 0.0f, 0.0f));
            this.containerView.addView(this.reactionsLayoutContainer, LayoutHelper.createFrame(-2, -2.0f, 1, 0.0f, 100.0f, 0.0f, 0.0f));
        }
        this.reactionsLayout.setSelectedEmojis(this.selectedEmojis);
        this.reactionsLayout.setDelegate(new ReactionsContainerLayout.ReactionsContainerDelegate() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
            public /* synthetic */ boolean drawBackground() {
                return ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$drawBackground(this);
            }

            @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
            public /* synthetic */ void drawRoundRect(Canvas canvas, RectF rectF, float f, float f2, float f3, int i, boolean z) {
                ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$drawRoundRect(this, canvas, rectF, f, f2, f3, i, z);
            }

            @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
            public /* synthetic */ boolean needEnterText() {
                return ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$needEnterText(this);
            }

            @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
            public /* synthetic */ void onEmojiWindowDismissed() {
                ReactionsContainerLayout.ReactionsContainerDelegate.-CC.$default$onEmojiWindowDismissed(this);
            }

            @Override // org.telegram.ui.Components.ReactionsContainerLayout.ReactionsContainerDelegate
            public final void onReactionClicked(View view, ReactionsLayoutInBubble.VisibleReaction visibleReaction, boolean z, boolean z2) {
                ContentPreviewViewer.this.lambda$showEmojiSelectorForStickers$0(view, visibleReaction, z, z2);
            }
        });
        this.reactionsLayout.setMessage(null, null, false);
        this.reactionsLayoutContainer.setScaleY(0.6f);
        this.reactionsLayoutContainer.setScaleX(0.6f);
        this.reactionsLayoutContainer.setAlpha(0.0f);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ContentPreviewViewer.this.lambda$showEmojiSelectorForStickers$1();
            }
        }, 10L);
    }

    public void showUnlockPremiumView() {
        if (this.unlockPremiumView == null) {
            UnlockPremiumView unlockPremiumView = new UnlockPremiumView(this.containerView.getContext(), 0, this.resourcesProvider);
            this.unlockPremiumView = unlockPremiumView;
            this.containerView.addView(unlockPremiumView, LayoutHelper.createFrame(-1, -1.0f));
            this.unlockPremiumView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ContentPreviewViewer.this.lambda$showUnlockPremiumView$2(view);
                }
            });
            this.unlockPremiumView.premiumButtonView.buttonLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ContentPreviewViewer.this.lambda$showUnlockPremiumView$3(view);
                }
            });
        }
        AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, false, 1.0f, false);
        AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, true);
        this.unlockPremiumView.setTranslationY(0.0f);
    }

    public void clearDelegate(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
        if (this.delegate == contentPreviewViewerDelegate) {
            this.currentDocument = null;
            this.currentStickerSet = null;
            this.currentQuery = null;
            this.delegate = null;
            this.resourcesProvider = null;
            reset();
        }
    }

    public void close() {
        if (this.parentActivity == null || this.menuVisible) {
            return;
        }
        AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
        this.showProgress = 1.0f;
        this.lastUpdateTime = System.currentTimeMillis();
        this.containerView.invalidate();
        this.currentDocument = null;
        this.currentStickerSet = null;
        this.currentQuery = null;
        this.delegate = null;
        this.isVisible = false;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ContentPreviewViewer.this.lambda$close$8();
            }
        }, 200L);
        UnlockPremiumView unlockPremiumView = this.unlockPremiumView;
        if (unlockPremiumView != null) {
            unlockPremiumView.animate().alpha(0.0f).translationY(AndroidUtilities.dp(56.0f)).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
        }
        FrameLayout frameLayout = this.reactionsLayoutContainer;
        if (frameLayout != null) {
            frameLayout.animate().alpha(0.0f).setDuration(150L).scaleX(0.6f).scaleY(0.6f).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 8);
    }

    public void closeWithMenu() {
        CustomEmojiReactionsWindow reactionsWindow;
        ReactionsContainerLayout reactionsContainerLayout = this.reactionsLayout;
        if (reactionsContainerLayout != null && (reactionsWindow = reactionsContainerLayout.getReactionsWindow()) != null && reactionsWindow.isShowing()) {
            reactionsWindow.dismiss();
            return;
        }
        this.menuVisible = false;
        dismissPopupWindow();
        close();
    }

    public RecyclerListView createMyStickerPacksListView() {
        if (this.parentActivity == null) {
            return null;
        }
        final ArrayList arrayList = new ArrayList();
        arrayList.add(new TLRPC$TL_stickerSetNoCovered());
        TLRPC$TL_messages_getMyStickers tLRPC$TL_messages_getMyStickers = new TLRPC$TL_messages_getMyStickers();
        tLRPC$TL_messages_getMyStickers.limit = 100;
        getMyStickersRemote(tLRPC$TL_messages_getMyStickers, arrayList);
        RecyclerListView recyclerListView = new RecyclerListView(this.parentActivity) { // from class: org.telegram.ui.ContentPreviewViewer.5
            {
                ContentPreviewViewer.this = this;
            }

            @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.View
            public void onMeasure(int i, int i2) {
                int size = View.MeasureSpec.getSize(i2);
                int dp = AndroidUtilities.dp(4.0f) + (AndroidUtilities.dp(50.0f) * getAdapter().getItemCount());
                if (dp <= size) {
                    size = dp;
                }
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
            }
        };
        recyclerListView.setLayoutManager(new LinearLayoutManager(this.parentActivity));
        recyclerListView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.ContentPreviewViewer.6
            {
                ContentPreviewViewer.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                if (recyclerView.getChildAdapterPosition(view) == arrayList.size() - 1) {
                    rect.bottom = AndroidUtilities.dp(4.0f);
                }
            }
        });
        recyclerListView.setAdapter(new RecyclerListView.SelectionAdapter() { // from class: org.telegram.ui.ContentPreviewViewer.7
            {
                ContentPreviewViewer.this = this;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return arrayList.size();
            }

            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return true;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                ((StickerPackNameView) viewHolder.itemView).bind((TLRPC$StickerSetCovered) arrayList.get(i));
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                StickerPackNameView stickerPackNameView = new StickerPackNameView(viewGroup.getContext(), ContentPreviewViewer.this.resourcesProvider);
                stickerPackNameView.setLayoutParams(new RecyclerView.LayoutParams(-2, AndroidUtilities.dp(48.0f)));
                return new RecyclerListView.Holder(stickerPackNameView);
            }
        });
        return recyclerListView;
    }

    public void destroy() {
        this.isVisible = false;
        this.delegate = null;
        this.currentDocument = null;
        this.currentQuery = null;
        this.currentStickerSet = null;
        if (this.parentActivity == null || this.windowView == null) {
            return;
        }
        Bitmap bitmap = this.blurrBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.blurrBitmap = null;
        }
        this.blurProgress = 0.0f;
        this.menuVisible = false;
        try {
            if (this.windowView.getParent() != null) {
                ((WindowManager) this.parentActivity.getSystemService("window")).removeViewImmediate(this.windowView);
            }
            this.windowView = null;
        } catch (Exception e) {
            FileLog.e(e);
        }
        Instance = null;
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 8);
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x0071, code lost:
        if (((org.telegram.ui.Cells.StickerCell) r2).showingBitmap() != false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0063, code lost:
        if (((org.telegram.ui.Cells.StickerEmojiCell) r2).showingBitmap() != false) goto L31;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onInterceptTouchEvent(MotionEvent motionEvent, final RecyclerListView recyclerListView, int i, ContentPreviewViewerDelegate contentPreviewViewerDelegate, final Theme.ResourcesProvider resourcesProvider) {
        final int i2;
        this.delegate = contentPreviewViewerDelegate;
        if (contentPreviewViewerDelegate != null) {
            this.isPhotoEditor = contentPreviewViewerDelegate.isPhotoEditor();
            this.isStickerEditor = this.delegate.isStickerEditor();
        }
        ContentPreviewViewerDelegate contentPreviewViewerDelegate2 = this.delegate;
        if ((contentPreviewViewerDelegate2 == null || contentPreviewViewerDelegate2.can()) && motionEvent.getAction() == 0) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int childCount = recyclerListView.getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = recyclerListView.getChildAt(i3);
                if (childAt == null) {
                    return false;
                }
                int top = childAt.getTop();
                int bottom = childAt.getBottom();
                int left = childAt.getLeft();
                int right = childAt.getRight();
                if (top <= y && bottom >= y && left <= x && right >= x) {
                    if (!(childAt instanceof StickerEmojiCell)) {
                        if (!(childAt instanceof StickerCell)) {
                            if (childAt instanceof ContextLinkCell) {
                                ContextLinkCell contextLinkCell = (ContextLinkCell) childAt;
                                if (contextLinkCell.showingBitmap()) {
                                    if (!contextLinkCell.isSticker()) {
                                        if (contextLinkCell.isGif()) {
                                            this.centerImage.setRoundRadius(AndroidUtilities.dp(6.0f));
                                            i2 = 1;
                                        }
                                    }
                                    this.centerImage.setRoundRadius(0);
                                    i2 = 0;
                                }
                                i2 = -1;
                            } else {
                                if ((childAt instanceof EmojiPacksAlert.EmojiImageView) || (((childAt instanceof EmojiView.ImageViewEmoji) && ((EmojiView.ImageViewEmoji) childAt).getSpan() != null) || ((childAt instanceof SuggestEmojiView.EmojiImageView) && (((SuggestEmojiView.EmojiImageView) childAt).drawable instanceof AnimatedEmojiDrawable)))) {
                                    this.centerImage.setRoundRadius(0);
                                    i2 = 2;
                                }
                                i2 = -1;
                            }
                        }
                    }
                    if (i2 == -1) {
                        return false;
                    }
                    this.startX = x;
                    this.startY = y;
                    this.currentPreviewCell = childAt;
                    Runnable runnable = new Runnable() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            ContentPreviewViewer.this.lambda$onInterceptTouchEvent$5(recyclerListView, i2, resourcesProvider);
                        }
                    };
                    this.openPreviewRunnable = runnable;
                    AndroidUtilities.runOnUIThread(runnable, 200L);
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:321:0x025e, code lost:
        if (r1 != null) goto L114;
     */
    /* JADX WARN: Removed duplicated region for block: B:273:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:278:0x0160  */
    /* JADX WARN: Removed duplicated region for block: B:281:0x0169  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x0198  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x01d5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouch(MotionEvent motionEvent, final RecyclerListView recyclerListView, int i, final Object obj, ContentPreviewViewerDelegate contentPreviewViewerDelegate, Theme.ResourcesProvider resourcesProvider) {
        Runnable runnable;
        int i2;
        ContentPreviewViewerDelegate contentPreviewViewerDelegate2;
        View view;
        View view2;
        TLRPC$Document tLRPC$Document;
        this.delegate = contentPreviewViewerDelegate;
        if (contentPreviewViewerDelegate != null) {
            this.isPhotoEditor = contentPreviewViewerDelegate.isPhotoEditor();
            this.isStickerEditor = this.delegate.isStickerEditor();
        }
        ContentPreviewViewerDelegate contentPreviewViewerDelegate3 = this.delegate;
        if (contentPreviewViewerDelegate3 == null || contentPreviewViewerDelegate3.can()) {
            if (this.openPreviewRunnable != null || isVisible()) {
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3 || motionEvent.getAction() == 6) {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            ContentPreviewViewer.lambda$onTouch$4(RecyclerListView.this, obj);
                        }
                    }, 150L);
                    runnable = this.openPreviewRunnable;
                    if (runnable == null) {
                        if (isVisible()) {
                            close();
                            View view3 = this.currentPreviewCell;
                            if (view3 != null) {
                                if (view3 instanceof StickerEmojiCell) {
                                    ((StickerEmojiCell) view3).setScaled(false);
                                } else if (view3 instanceof StickerCell) {
                                    ((StickerCell) view3).setScaled(false);
                                } else if (view3 instanceof ContextLinkCell) {
                                    ((ContextLinkCell) view3).setScaled(false);
                                }
                                this.currentPreviewCell = null;
                            }
                        }
                    }
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                    this.openPreviewRunnable = null;
                } else if (motionEvent.getAction() != 0) {
                    if (this.isVisible) {
                        if (motionEvent.getAction() == 2) {
                            if (this.currentContentType == 1 && !this.isPhotoEditor) {
                                if (!this.menuVisible && this.showProgress == 1.0f) {
                                    if (this.lastTouchY == -10000.0f) {
                                        this.lastTouchY = motionEvent.getY();
                                        this.currentMoveY = 0.0f;
                                        this.moveY = 0.0f;
                                    } else {
                                        float y = motionEvent.getY();
                                        float f = this.currentMoveY + (y - this.lastTouchY);
                                        this.currentMoveY = f;
                                        this.lastTouchY = y;
                                        if (f > 0.0f) {
                                            this.currentMoveY = 0.0f;
                                        } else if (f < (-AndroidUtilities.dp(60.0f))) {
                                            this.currentMoveY = -AndroidUtilities.dp(60.0f);
                                        }
                                        this.moveY = rubberYPoisition(this.currentMoveY, AndroidUtilities.dp(200.0f));
                                        this.containerView.invalidate();
                                        if (this.currentMoveY <= (-AndroidUtilities.dp(55.0f))) {
                                            AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                                            this.showSheetRunnable.run();
                                        }
                                    }
                                }
                                return true;
                            }
                            int x = (int) motionEvent.getX();
                            int y2 = (int) motionEvent.getY();
                            int childCount = recyclerListView.getChildCount();
                            int i3 = 0;
                            while (true) {
                                if (i3 >= childCount) {
                                    break;
                                }
                                View childAt = recyclerListView.getChildAt(i3);
                                if (childAt == null) {
                                    return false;
                                }
                                int top = childAt.getTop();
                                int bottom = childAt.getBottom();
                                int left = childAt.getLeft();
                                int right = childAt.getRight();
                                if (top > y2 || bottom < y2 || left > x || right < x) {
                                    i3++;
                                } else {
                                    if (!(childAt instanceof StickerEmojiCell) && !(childAt instanceof StickerCell)) {
                                        if (childAt instanceof ContextLinkCell) {
                                            ContextLinkCell contextLinkCell = (ContextLinkCell) childAt;
                                            if (!contextLinkCell.isSticker()) {
                                                if (contextLinkCell.isGif()) {
                                                    this.centerImage.setRoundRadius(AndroidUtilities.dp(6.0f));
                                                    i2 = 1;
                                                    if (i2 != -1 && childAt != this.currentPreviewCell) {
                                                        contentPreviewViewerDelegate2 = this.delegate;
                                                        if (contentPreviewViewerDelegate2 != null) {
                                                            contentPreviewViewerDelegate2.resetTouch();
                                                        }
                                                        view = this.currentPreviewCell;
                                                        if (!(view instanceof StickerEmojiCell)) {
                                                            ((StickerEmojiCell) view).setScaled(false);
                                                        } else if (view instanceof StickerCell) {
                                                            ((StickerCell) view).setScaled(false);
                                                        } else if (view instanceof ContextLinkCell) {
                                                            ((ContextLinkCell) view).setScaled(false);
                                                        }
                                                        this.currentPreviewCell = childAt;
                                                        this.clearsInputField = false;
                                                        this.menuVisible = false;
                                                        this.closeOnDismiss = false;
                                                        dismissPopupWindow();
                                                        AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, false);
                                                        view2 = this.currentPreviewCell;
                                                        if (!(view2 instanceof StickerEmojiCell)) {
                                                            StickerEmojiCell stickerEmojiCell = (StickerEmojiCell) view2;
                                                            TLRPC$Document sticker = stickerEmojiCell.getSticker();
                                                            SendMessagesHelper.ImportingSticker stickerPath = stickerEmojiCell.getStickerPath();
                                                            String findAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(stickerEmojiCell.getSticker(), null, Integer.valueOf(this.currentAccount));
                                                            ContentPreviewViewerDelegate contentPreviewViewerDelegate4 = this.delegate;
                                                            open(sticker, stickerPath, findAnimatedEmojiEmoticon, contentPreviewViewerDelegate4 != null ? contentPreviewViewerDelegate4.getQuery(false) : null, null, i2, stickerEmojiCell.isRecent(), stickerEmojiCell.getParentObject(), resourcesProvider);
                                                            stickerEmojiCell.setScaled(true);
                                                        } else if (view2 instanceof StickerCell) {
                                                            StickerCell stickerCell = (StickerCell) view2;
                                                            TLRPC$Document sticker2 = stickerCell.getSticker();
                                                            String findAnimatedEmojiEmoticon2 = MessageObject.findAnimatedEmojiEmoticon(stickerCell.getSticker(), null, Integer.valueOf(this.currentAccount));
                                                            ContentPreviewViewerDelegate contentPreviewViewerDelegate5 = this.delegate;
                                                            open(sticker2, null, findAnimatedEmojiEmoticon2, contentPreviewViewerDelegate5 != null ? contentPreviewViewerDelegate5.getQuery(false) : null, null, i2, false, stickerCell.getParentObject(), resourcesProvider);
                                                            stickerCell.setScaled(true);
                                                            this.clearsInputField = stickerCell.isClearsInputField();
                                                        } else if (view2 instanceof ContextLinkCell) {
                                                            ContextLinkCell contextLinkCell2 = (ContextLinkCell) view2;
                                                            TLRPC$Document document = contextLinkCell2.getDocument();
                                                            ContentPreviewViewerDelegate contentPreviewViewerDelegate6 = this.delegate;
                                                            open(document, null, null, contentPreviewViewerDelegate6 != null ? contentPreviewViewerDelegate6.getQuery(true) : null, contextLinkCell2.getBotInlineResult(), i2, false, contextLinkCell2.getBotInlineResult() != null ? contextLinkCell2.getInlineBot() : contextLinkCell2.getParentObject(), resourcesProvider);
                                                            if (i2 != 1 || this.isPhotoEditor) {
                                                                contextLinkCell2.setScaled(true);
                                                            }
                                                        } else if (view2 instanceof EmojiPacksAlert.EmojiImageView) {
                                                            tLRPC$Document = ((EmojiPacksAlert.EmojiImageView) view2).getDocument();
                                                        } else {
                                                            if (view2 instanceof EmojiView.ImageViewEmoji) {
                                                                AnimatedEmojiSpan span = ((EmojiView.ImageViewEmoji) view2).getSpan();
                                                                if (span != null) {
                                                                    tLRPC$Document = span.document;
                                                                    if (tLRPC$Document == null) {
                                                                        tLRPC$Document = AnimatedEmojiDrawable.findDocument(this.currentAccount, span.getDocumentId());
                                                                    }
                                                                } else {
                                                                    tLRPC$Document = null;
                                                                }
                                                                if (tLRPC$Document == null) {
                                                                    return false;
                                                                }
                                                            } else if (view2 instanceof SuggestEmojiView.EmojiImageView) {
                                                                Drawable drawable = ((SuggestEmojiView.EmojiImageView) view2).drawable;
                                                                TLRPC$Document document2 = drawable instanceof AnimatedEmojiDrawable ? ((AnimatedEmojiDrawable) drawable).getDocument() : null;
                                                                if (document2 == null) {
                                                                    return false;
                                                                }
                                                                tLRPC$Document = document2;
                                                            }
                                                            open(tLRPC$Document, null, MessageObject.findAnimatedEmojiEmoticon(tLRPC$Document, null, Integer.valueOf(this.currentAccount)), null, null, i2, false, null, resourcesProvider);
                                                        }
                                                        runSmoothHaptic();
                                                        return true;
                                                    }
                                                }
                                                i2 = -1;
                                                if (i2 != -1) {
                                                    contentPreviewViewerDelegate2 = this.delegate;
                                                    if (contentPreviewViewerDelegate2 != null) {
                                                    }
                                                    view = this.currentPreviewCell;
                                                    if (!(view instanceof StickerEmojiCell)) {
                                                    }
                                                    this.currentPreviewCell = childAt;
                                                    this.clearsInputField = false;
                                                    this.menuVisible = false;
                                                    this.closeOnDismiss = false;
                                                    dismissPopupWindow();
                                                    AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, false);
                                                    view2 = this.currentPreviewCell;
                                                    if (!(view2 instanceof StickerEmojiCell)) {
                                                    }
                                                    runSmoothHaptic();
                                                    return true;
                                                }
                                            }
                                        } else {
                                            if ((childAt instanceof EmojiPacksAlert.EmojiImageView) || ((childAt instanceof EmojiView.ImageViewEmoji) && ((EmojiView.ImageViewEmoji) childAt).getSpan() != null)) {
                                                this.centerImage.setRoundRadius(0);
                                                i2 = 2;
                                                if (i2 != -1) {
                                                }
                                            }
                                            i2 = -1;
                                            if (i2 != -1) {
                                            }
                                        }
                                    }
                                    this.centerImage.setRoundRadius(0);
                                    i2 = 0;
                                    if (i2 != -1) {
                                    }
                                }
                            }
                        }
                        return true;
                    } else if (this.openPreviewRunnable != null && (motionEvent.getAction() != 2 || Math.hypot(this.startX - motionEvent.getX(), this.startY - motionEvent.getY()) > AndroidUtilities.dp(10.0f))) {
                        runnable = this.openPreviewRunnable;
                        AndroidUtilities.cancelRunOnUIThread(runnable);
                        this.openPreviewRunnable = null;
                    }
                }
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:247:0x0389  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0394  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x03c5  */
    /* JADX WARN: Removed duplicated region for block: B:276:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void open(TLRPC$Document tLRPC$Document, SendMessagesHelper.ImportingSticker importingSticker, String str, String str2, TLRPC$BotInlineResult tLRPC$BotInlineResult, int i, boolean z, Object obj, Theme.ResourcesProvider resourcesProvider) {
        boolean z2;
        Runnable runnable;
        long j;
        TLRPC$InputStickerSet tLRPC$InputStickerSet;
        ContentPreviewViewerDelegate contentPreviewViewerDelegate;
        int i2;
        if (this.parentActivity == null || this.windowView == null) {
            return;
        }
        this.resourcesProvider = resourcesProvider;
        this.isRecentSticker = z;
        this.stickerEmojiLayout = null;
        this.backgroundDrawable.setColor(AndroidUtilities.isDarkColor(Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider)) ? 1895825408 : 1692853990);
        this.drawEffect = false;
        this.centerImage.setColorFilter(null);
        if (i == 0 || i == 2 || i == 3) {
            if (tLRPC$Document == null && importingSticker == null) {
                return;
            }
            if (textPaint == null) {
                TextPaint textPaint2 = new TextPaint(1);
                textPaint = textPaint2;
                textPaint2.setTextSize(AndroidUtilities.dp(24.0f));
            }
            this.effectImage.clearImage();
            this.drawEffect = false;
            if (tLRPC$Document != null) {
                int i3 = 0;
                while (true) {
                    if (i3 >= tLRPC$Document.attributes.size()) {
                        tLRPC$InputStickerSet = null;
                        break;
                    }
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute = tLRPC$Document.attributes.get(i3);
                    if ((tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeSticker) && (tLRPC$InputStickerSet = tLRPC$DocumentAttribute.stickerset) != null) {
                        break;
                    }
                    i3++;
                }
                if (str != null) {
                    this.stickerEmojiLayout = new StaticLayout(AndroidUtilities.replaceCharSequence("…", TextUtils.ellipsize(Emoji.replaceEmoji((CharSequence) str, textPaint.getFontMetricsInt(), AndroidUtilities.dp(24.0f), false), textPaint, AndroidUtilities.dp(200.0f), TextUtils.TruncateAt.END), ""), textPaint, AndroidUtilities.dp(200.0f), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
                if ((tLRPC$InputStickerSet != null || i == 2) && ((contentPreviewViewerDelegate = this.delegate) == null || contentPreviewViewerDelegate.needMenu())) {
                    AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                    AndroidUtilities.runOnUIThread(this.showSheetRunnable, 1300L);
                }
                TLRPC$TL_messages_stickerSet stickerSet = MediaDataController.getInstance(this.currentAccount).getStickerSet(tLRPC$InputStickerSet, true);
                this.currentStickerSet = (stickerSet == null || !stickerSet.documents.isEmpty()) ? tLRPC$InputStickerSet : null;
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 90);
                if (MessageObject.isVideoStickerDocument(tLRPC$Document)) {
                    this.centerImage.setImage(ImageLocation.getForDocument(tLRPC$Document), null, ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$Document), null, null, 0L, "webp", this.currentStickerSet, 1);
                } else {
                    this.centerImage.setImage(ImageLocation.getForDocument(tLRPC$Document), (String) null, ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$Document), (String) null, "webp", this.currentStickerSet, 1);
                    if (MessageObject.isPremiumSticker(tLRPC$Document)) {
                        this.drawEffect = true;
                        this.effectImage.setImage(ImageLocation.getForDocument(MessageObject.getPremiumStickerAnimation(tLRPC$Document), tLRPC$Document), (String) null, (ImageLocation) null, (String) null, "tgs", this.currentStickerSet, 1);
                    }
                }
                if (MessageObject.isTextColorEmoji(tLRPC$Document)) {
                    this.centerImage.setColorFilter(Theme.getAnimatedEmojiColorFilter(resourcesProvider));
                }
                if (this.stickerEmojiLayout == null) {
                    int i4 = 0;
                    while (true) {
                        if (i4 >= tLRPC$Document.attributes.size()) {
                            break;
                        }
                        TLRPC$DocumentAttribute tLRPC$DocumentAttribute2 = tLRPC$Document.attributes.get(i4);
                        if ((tLRPC$DocumentAttribute2 instanceof TLRPC$TL_documentAttributeSticker) && !TextUtils.isEmpty(tLRPC$DocumentAttribute2.alt)) {
                            this.stickerEmojiLayout = new StaticLayout(AndroidUtilities.replaceCharSequence("…", TextUtils.ellipsize(Emoji.replaceEmoji((CharSequence) tLRPC$DocumentAttribute2.alt, textPaint.getFontMetricsInt(), AndroidUtilities.dp(24.0f), false), textPaint, AndroidUtilities.dp(200.0f), TextUtils.TruncateAt.END), ""), textPaint, AndroidUtilities.dp(200.0f), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                            break;
                        }
                        i4++;
                    }
                }
            } else if (importingSticker != null) {
                this.centerImage.setImage(importingSticker.path, null, null, importingSticker.animated ? "tgs" : null, 0L);
                if (importingSticker.videoEditedInfo != null) {
                    if (this.paintingOverlay == null) {
                        PaintingOverlay paintingOverlay = new PaintingOverlay(this.containerView.getContext());
                        this.paintingOverlay = paintingOverlay;
                        this.containerView.addView(paintingOverlay, new FrameLayout.LayoutParams((int) LiteMode.FLAG_CALLS_ANIMATIONS, (int) LiteMode.FLAG_CALLS_ANIMATIONS));
                    }
                    z2 = false;
                    this.paintingOverlay.setEntities(importingSticker.videoEditedInfo.mediaEntities, true, true, false);
                } else {
                    z2 = false;
                }
                if (str != null) {
                    this.stickerEmojiLayout = new StaticLayout(AndroidUtilities.replaceCharSequence("…", TextUtils.ellipsize(Emoji.replaceEmoji(str, textPaint.getFontMetricsInt(), AndroidUtilities.dp(24.0f), z2), textPaint, AndroidUtilities.dp(200.0f), TextUtils.TruncateAt.END), ""), textPaint, AndroidUtilities.dp(200.0f), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
                if (this.delegate.needMenu()) {
                    AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                    runnable = this.showSheetRunnable;
                    j = 1300;
                }
            }
            if (this.centerImage.getLottieAnimation() == null) {
                i2 = 0;
                this.centerImage.getLottieAnimation().setCurrentFrame(0);
            } else {
                i2 = 0;
            }
            if (this.drawEffect && this.effectImage.getLottieAnimation() != null) {
                this.effectImage.getLottieAnimation().setCurrentFrame(i2);
            }
            this.currentContentType = i;
            this.currentDocument = tLRPC$Document;
            this.importingSticker = importingSticker;
            this.currentQuery = str2;
            this.inlineResult = tLRPC$BotInlineResult;
            this.parentObject = obj;
            this.resourcesProvider = resourcesProvider;
            this.containerView.invalidate();
            if (this.isVisible) {
                AndroidUtilities.lockOrientation(this.parentActivity);
                try {
                    if (this.windowView.getParent() != null) {
                        ((WindowManager) this.parentActivity.getSystemService("window")).removeView(this.windowView);
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                ((WindowManager) this.parentActivity.getSystemService("window")).addView(this.windowView, this.windowLayoutParams);
                this.isVisible = true;
                this.showProgress = 0.0f;
                this.lastTouchY = -10000.0f;
                this.currentMoveYProgress = 0.0f;
                this.finalMoveY = 0.0f;
                this.currentMoveY = 0.0f;
                this.moveY = 0.0f;
                this.lastUpdateTime = System.currentTimeMillis();
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 8);
                return;
            }
            return;
        }
        if (tLRPC$Document != null) {
            TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 90);
            TLRPC$VideoSize documentVideoThumb = MessageObject.getDocumentVideoThumb(tLRPC$Document);
            ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$Document);
            forDocument.imageType = 2;
            ImageReceiver imageReceiver = this.centerImage;
            if (documentVideoThumb != null) {
                imageReceiver.setImage(forDocument, null, ImageLocation.getForDocument(documentVideoThumb, tLRPC$Document), null, ImageLocation.getForDocument(closestPhotoSizeWithSize2, tLRPC$Document), "90_90_b", null, tLRPC$Document.size, null, "gif" + tLRPC$Document, 0);
            } else {
                imageReceiver.setImage(forDocument, null, ImageLocation.getForDocument(closestPhotoSizeWithSize2, tLRPC$Document), "90_90_b", tLRPC$Document.size, null, "gif" + tLRPC$Document, 0);
            }
        } else if (tLRPC$BotInlineResult == null || tLRPC$BotInlineResult.content == null) {
            return;
        } else {
            TLRPC$WebDocument tLRPC$WebDocument = tLRPC$BotInlineResult.thumb;
            if ((tLRPC$WebDocument instanceof TLRPC$TL_webDocument) && "video/mp4".equals(tLRPC$WebDocument.mime_type)) {
                this.centerImage.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(tLRPC$BotInlineResult.content)), null, ImageLocation.getForWebFile(WebFile.createWithWebDocument(tLRPC$BotInlineResult.thumb)), null, ImageLocation.getForWebFile(WebFile.createWithWebDocument(tLRPC$BotInlineResult.thumb)), "90_90_b", null, tLRPC$BotInlineResult.content.size, null, "gif" + tLRPC$BotInlineResult, 1);
            } else {
                this.centerImage.setImage(ImageLocation.getForWebFile(WebFile.createWithWebDocument(tLRPC$BotInlineResult.content)), null, ImageLocation.getForWebFile(WebFile.createWithWebDocument(tLRPC$BotInlineResult.thumb)), "90_90_b", tLRPC$BotInlineResult.content.size, null, "gif" + tLRPC$BotInlineResult, 1);
            }
        }
        AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
        runnable = this.showSheetRunnable;
        j = 2000;
        AndroidUtilities.runOnUIThread(runnable, j);
        if (this.centerImage.getLottieAnimation() == null) {
        }
        if (this.drawEffect) {
            this.effectImage.getLottieAnimation().setCurrentFrame(i2);
        }
        this.currentContentType = i;
        this.currentDocument = tLRPC$Document;
        this.importingSticker = importingSticker;
        this.currentQuery = str2;
        this.inlineResult = tLRPC$BotInlineResult;
        this.parentObject = obj;
        this.resourcesProvider = resourcesProvider;
        this.containerView.invalidate();
        if (this.isVisible) {
        }
    }

    public void reset() {
        Runnable runnable = this.openPreviewRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.openPreviewRunnable = null;
        }
        View view = this.currentPreviewCell;
        if (view != null) {
            if (view instanceof StickerEmojiCell) {
                ((StickerEmojiCell) view).setScaled(false);
            } else if (view instanceof StickerCell) {
                ((StickerCell) view).setScaled(false);
            } else if (view instanceof ContextLinkCell) {
                ((ContextLinkCell) view).setScaled(false);
            }
            this.currentPreviewCell = null;
        }
    }

    protected void runSmoothHaptic() {
        VibrationEffect createWaveform;
        if (Build.VERSION.SDK_INT >= 26) {
            Vibrator vibrator = (Vibrator) this.containerView.getContext().getSystemService("vibrator");
            if (this.vibrationEffect == null) {
                createWaveform = VibrationEffect.createWaveform(new long[]{0, 2}, -1);
                this.vibrationEffect = createWaveform;
            }
            vibrator.cancel();
            vibrator.vibrate(this.vibrationEffect);
        }
    }

    public void setDelegate(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
        this.delegate = contentPreviewViewerDelegate;
        if (contentPreviewViewerDelegate != null) {
            this.isPhotoEditor = contentPreviewViewerDelegate.isPhotoEditor();
            this.isStickerEditor = this.delegate.isStickerEditor();
        }
    }

    public void setFocusable(boolean z) {
        if (z) {
            WindowManager.LayoutParams layoutParams = this.windowLayoutParams;
            layoutParams.flags &= -131073;
            layoutParams.softInputMode = 16;
        } else {
            this.windowLayoutParams.flags |= 131072;
        }
        try {
            ((WindowManager) this.parentActivity.getSystemService("window")).updateViewLayout(this.windowView, this.windowLayoutParams);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void setParentActivity(Activity activity) {
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        this.centerImage.setCurrentAccount(i);
        this.centerImage.setLayerNum(ConnectionsManager.DEFAULT_DATACENTER_ID);
        this.effectImage.setCurrentAccount(this.currentAccount);
        this.effectImage.setLayerNum(ConnectionsManager.DEFAULT_DATACENTER_ID);
        if (this.parentActivity == activity) {
            return;
        }
        this.parentActivity = activity;
        this.slideUpDrawable = activity.getResources().getDrawable(R.drawable.preview_arrow);
        FrameLayout frameLayout = new FrameLayout(activity) { // from class: org.telegram.ui.ContentPreviewViewer.3
            {
                ContentPreviewViewer.this = this;
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1) {
                    if (ContentPreviewViewer.this.isStickerEditor || ContentPreviewViewer.this.menuVisible) {
                        ContentPreviewViewer.this.closeWithMenu();
                    } else {
                        ContentPreviewViewer.this.close();
                    }
                    return true;
                }
                return super.dispatchKeyEvent(keyEvent);
            }
        };
        this.windowView = frameLayout;
        frameLayout.setFocusable(true);
        this.windowView.setFocusableInTouchMode(true);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            this.windowView.setFitsSystemWindows(true);
            this.windowView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda6
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    WindowInsets lambda$setParentActivity$6;
                    lambda$setParentActivity$6 = ContentPreviewViewer.this.lambda$setParentActivity$6(view, windowInsets);
                    return lambda$setParentActivity$6;
                }
            });
        }
        FrameLayoutDrawer frameLayoutDrawer = new FrameLayoutDrawer(activity) { // from class: org.telegram.ui.ContentPreviewViewer.4
            {
                ContentPreviewViewer.this = this;
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                ContentPreviewViewer.this.centerImage.onAttachedToWindow();
                ContentPreviewViewer.this.effectImage.onAttachedToWindow();
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                ContentPreviewViewer.this.centerImage.onDetachedFromWindow();
                ContentPreviewViewer.this.effectImage.onDetachedFromWindow();
            }
        };
        this.containerView = frameLayoutDrawer;
        frameLayoutDrawer.setFocusable(false);
        this.windowView.addView(this.containerView, LayoutHelper.createFrame(-1, -1, 51));
        this.containerView.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.ContentPreviewViewer$$ExternalSyntheticLambda7
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$setParentActivity$7;
                lambda$setParentActivity$7 = ContentPreviewViewer.this.lambda$setParentActivity$7(view, motionEvent);
                return lambda$setParentActivity$7;
            }
        });
        MessagesController.getInstance(this.currentAccount);
        this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0f));
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        this.windowLayoutParams = layoutParams;
        layoutParams.height = -1;
        layoutParams.format = -3;
        layoutParams.width = -1;
        layoutParams.gravity = 48;
        layoutParams.type = 99;
        if (i2 >= 21) {
            layoutParams.flags = -2147286784;
        }
        this.centerImage.setAspectFit(true);
        this.centerImage.setInvalidateAll(true);
        this.centerImage.setParentView(this.containerView);
        this.effectImage.setAspectFit(true);
        this.effectImage.setInvalidateAll(true);
        this.effectImage.setParentView(this.containerView);
    }

    public void setStickerSetForCustomSticker(TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet) {
        this.stickerSetForCustomSticker = tLRPC$TL_messages_stickerSet;
    }

    public void showCustomStickerActions(String str, VideoEditedInfo videoEditedInfo, View view, ArrayList arrayList, ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
        Activity findActivity = AndroidUtilities.findActivity(view.getContext());
        if (findActivity == null) {
            return;
        }
        setParentActivity(findActivity);
        setDelegate(contentPreviewViewerDelegate);
        SendMessagesHelper.ImportingSticker importingSticker = new SendMessagesHelper.ImportingSticker();
        importingSticker.path = str;
        importingSticker.videoEditedInfo = videoEditedInfo;
        this.selectedEmojis = arrayList;
        open(null, importingSticker, null, null, null, 3, false, null, new DarkThemeResourceProvider());
        AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
        AndroidUtilities.runOnUIThread(this.showSheetRunnable, 16L);
    }

    public boolean showMenuFor(View view) {
        if (view instanceof StickerEmojiCell) {
            Activity findActivity = AndroidUtilities.findActivity(view.getContext());
            if (findActivity == null) {
                return true;
            }
            setParentActivity(findActivity);
            StickerEmojiCell stickerEmojiCell = (StickerEmojiCell) view;
            View view2 = this.currentPreviewCell;
            if (view2 instanceof StickerEmojiCell) {
                ((StickerEmojiCell) view2).setScaled(false);
            } else if (view2 instanceof StickerCell) {
                ((StickerCell) view2).setScaled(false);
            } else if (view2 instanceof ContextLinkCell) {
                ((ContextLinkCell) view2).setScaled(false);
            }
            this.currentPreviewCell = stickerEmojiCell;
            TLRPC$Document sticker = stickerEmojiCell.getSticker();
            SendMessagesHelper.ImportingSticker stickerPath = stickerEmojiCell.getStickerPath();
            String findAnimatedEmojiEmoticon = MessageObject.findAnimatedEmojiEmoticon(stickerEmojiCell.getSticker(), null, Integer.valueOf(this.currentAccount));
            ContentPreviewViewerDelegate contentPreviewViewerDelegate = this.delegate;
            open(sticker, stickerPath, findAnimatedEmojiEmoticon, contentPreviewViewerDelegate != null ? contentPreviewViewerDelegate.getQuery(false) : null, null, 0, stickerEmojiCell.isRecent(), stickerEmojiCell.getParentObject(), this.resourcesProvider);
            AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
            AndroidUtilities.runOnUIThread(this.showSheetRunnable, 16L);
            stickerEmojiCell.setScaled(true);
            return true;
        }
        return false;
    }
}
