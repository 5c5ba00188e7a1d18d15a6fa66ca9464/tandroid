package org.telegram.ui;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.WebFile;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_documentAttributeSticker;
import org.telegram.tgnet.TLRPC$TL_webDocument;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$WebDocument;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.Cells.StickerEmojiCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class ContentPreviewViewer {
    @SuppressLint({"StaticFieldLeak"})
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
    private boolean isRecentSticker;
    private WindowInsets lastInsets;
    private float lastTouchY;
    private long lastUpdateTime;
    private boolean menuVisible;
    private Runnable openPreviewRunnable;
    private Activity parentActivity;
    private Object parentObject;
    ActionBarPopupWindow popupWindow;
    private Theme.ResourcesProvider resourcesProvider;
    private float showProgress;
    private Drawable slideUpDrawable;
    private float startMoveY;
    private int startX;
    private int startY;
    private StaticLayout stickerEmojiLayout;
    private UnlockPremiumView unlockPremiumView;
    VibrationEffect vibrationEffect;
    private WindowManager.LayoutParams windowLayoutParams;
    private FrameLayout windowView;
    private float moveY = 0.0f;
    private ColorDrawable backgroundDrawable = new ColorDrawable(1895825408);
    private ImageReceiver centerImage = new ImageReceiver();
    private ImageReceiver effectImage = new ImageReceiver();
    private boolean isVisible = false;
    private int keyboardHeight = AndroidUtilities.dp(200.0f);
    private Paint paint = new Paint(1);
    private Runnable showSheetRunnable = new AnonymousClass1();

    /* loaded from: classes3.dex */
    public interface ContentPreviewViewerDelegate {

        /* renamed from: org.telegram.ui.ContentPreviewViewer$ContentPreviewViewerDelegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static String $default$getQuery(ContentPreviewViewerDelegate contentPreviewViewerDelegate, boolean z) {
                return null;
            }

            public static void $default$gifAddedOrDeleted(ContentPreviewViewerDelegate contentPreviewViewerDelegate) {
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

            public static void $default$remove(ContentPreviewViewerDelegate contentPreviewViewerDelegate, SendMessagesHelper.ImportingSticker importingSticker) {
            }

            public static void $default$sendGif(ContentPreviewViewerDelegate contentPreviewViewerDelegate, Object obj, Object obj2, boolean z, int i) {
            }
        }

        boolean canSchedule();

        long getDialogId();

        String getQuery(boolean z);

        void gifAddedOrDeleted();

        boolean isInScheduleMode();

        boolean needMenu();

        boolean needOpen();

        boolean needRemove();

        boolean needSend();

        void openSet(TLRPC$InputStickerSet tLRPC$InputStickerSet, boolean z);

        void remove(SendMessagesHelper.ImportingSticker importingSticker);

        void sendGif(Object obj, Object obj2, boolean z, int i);

        void sendSticker(TLRPC$Document tLRPC$Document, String str, Object obj, boolean z, int i);
    }

    /* loaded from: classes3.dex */
    public class FrameLayoutDrawer extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public FrameLayoutDrawer(Context context) {
            super(context);
            ContentPreviewViewer.this = r1;
            setWillNotDraw(false);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            ContentPreviewViewer.this.onDraw(canvas);
        }
    }

    /* renamed from: org.telegram.ui.ContentPreviewViewer$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
            ContentPreviewViewer.this = r1;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z;
            int i;
            int i2;
            int i3;
            int i4;
            int i5;
            int min;
            String str;
            int i6;
            if (ContentPreviewViewer.this.parentActivity == null) {
                return;
            }
            ContentPreviewViewer.this.closeOnDismiss = true;
            if (ContentPreviewViewer.this.currentContentType == 0) {
                if (!MessageObject.isPremiumSticker(ContentPreviewViewer.this.currentDocument) || AccountInstance.getInstance(ContentPreviewViewer.this.currentAccount).getUserConfig().isPremium()) {
                    boolean isStickerInFavorites = MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).isStickerInFavorites(ContentPreviewViewer.this.currentDocument);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    ArrayList arrayList3 = new ArrayList();
                    ContentPreviewViewer.this.menuVisible = true;
                    ContentPreviewViewer.this.containerView.invalidate();
                    if (ContentPreviewViewer.this.delegate != null) {
                        if (ContentPreviewViewer.this.delegate.needSend() && !ContentPreviewViewer.this.delegate.isInScheduleMode()) {
                            arrayList.add(LocaleController.getString("SendStickerPreview", 2131628208));
                            arrayList3.add(2131165929);
                            arrayList2.add(0);
                        }
                        if (ContentPreviewViewer.this.delegate.needSend() && !ContentPreviewViewer.this.delegate.isInScheduleMode()) {
                            arrayList.add(LocaleController.getString("SendWithoutSound", 2131628212));
                            arrayList3.add(2131165539);
                            arrayList2.add(6);
                        }
                        if (ContentPreviewViewer.this.delegate.canSchedule()) {
                            arrayList.add(LocaleController.getString("Schedule", 2131628081));
                            arrayList3.add(2131165643);
                            arrayList2.add(3);
                        }
                        if (ContentPreviewViewer.this.currentStickerSet != null && ContentPreviewViewer.this.delegate.needOpen()) {
                            arrayList.add(LocaleController.formatString("ViewPackPreview", 2131628919, new Object[0]));
                            arrayList3.add(2131165798);
                            arrayList2.add(1);
                        }
                        if (ContentPreviewViewer.this.delegate.needRemove()) {
                            arrayList.add(LocaleController.getString("ImportStickersRemoveMenu", 2131626213));
                            arrayList3.add(2131165702);
                            arrayList2.add(5);
                        }
                    }
                    if (!MessageObject.isMaskDocument(ContentPreviewViewer.this.currentDocument) && (isStickerInFavorites || (MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).canAddStickerToFavorites() && MessageObject.isStickerHasSet(ContentPreviewViewer.this.currentDocument)))) {
                        if (isStickerInFavorites) {
                            i6 = 2131625408;
                            str = "DeleteFromFavorites";
                        } else {
                            i6 = 2131624292;
                            str = "AddToFavorites";
                        }
                        arrayList.add(LocaleController.getString(str, i6));
                        arrayList3.add(Integer.valueOf(isStickerInFavorites ? 2131165966 : 2131165728));
                        arrayList2.add(2);
                    }
                    if (ContentPreviewViewer.this.isRecentSticker) {
                        arrayList.add(LocaleController.getString("DeleteFromRecent", 2131625409));
                        arrayList3.add(2131165702);
                        arrayList2.add(4);
                    }
                    if (arrayList.isEmpty()) {
                        return;
                    }
                    int[] iArr = new int[arrayList3.size()];
                    for (int i7 = 0; i7 < arrayList3.size(); i7++) {
                        iArr[i7] = ((Integer) arrayList3.get(i7)).intValue();
                    }
                    View$OnClickListenerC00311 view$OnClickListenerC00311 = new View$OnClickListenerC00311(arrayList2, isStickerInFavorites);
                    ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(ContentPreviewViewer.this.containerView.getContext(), 2131166086, ContentPreviewViewer.this.resourcesProvider);
                    for (int i8 = 0; i8 < arrayList.size(); i8++) {
                        ActionBarMenuSubItem addItem = ActionBarMenuItem.addItem(actionBarPopupWindowLayout, ((Integer) arrayList3.get(i8)).intValue(), (CharSequence) arrayList.get(i8), false, ContentPreviewViewer.this.resourcesProvider);
                        addItem.setTag(Integer.valueOf(i8));
                        addItem.setOnClickListener(view$OnClickListenerC00311);
                    }
                    ContentPreviewViewer.this.popupWindow = new AnonymousClass2(actionBarPopupWindowLayout, -2, -2);
                    ContentPreviewViewer.this.popupWindow.setPauseNotifications(true);
                    ContentPreviewViewer.this.popupWindow.setDismissAnimationDuration(100);
                    ContentPreviewViewer.this.popupWindow.setScaleOut(true);
                    ContentPreviewViewer.this.popupWindow.setOutsideTouchable(true);
                    ContentPreviewViewer.this.popupWindow.setClippingEnabled(true);
                    ContentPreviewViewer.this.popupWindow.setAnimationStyle(2131689480);
                    ContentPreviewViewer.this.popupWindow.setFocusable(true);
                    actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
                    ContentPreviewViewer.this.popupWindow.setInputMethodMode(2);
                    ContentPreviewViewer.this.popupWindow.getContentView().setFocusableInTouchMode(true);
                    if (Build.VERSION.SDK_INT >= 21 && ContentPreviewViewer.this.lastInsets != null) {
                        i5 = ContentPreviewViewer.this.lastInsets.getStableInsetBottom() + ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                        i4 = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                    } else {
                        i4 = AndroidUtilities.statusBarHeight;
                        i5 = 0;
                    }
                    int max = ((int) (ContentPreviewViewer.this.moveY + Math.max(i4 + min + (ContentPreviewViewer.this.stickerEmojiLayout != null ? AndroidUtilities.dp(40.0f) : 0), ((ContentPreviewViewer.this.containerView.getHeight() - i5) - ContentPreviewViewer.this.keyboardHeight) / 2) + ((ContentPreviewViewer.this.currentContentType == 1 ? Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i5) - AndroidUtilities.dp(40.0f) : (int) (ContentPreviewViewer.this.drawEffect ? Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i5) - AndroidUtilities.dpf2(40.0f) : Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i5) / 1.8f)) / 2))) + AndroidUtilities.dp(24.0f);
                    if (ContentPreviewViewer.this.drawEffect) {
                        max += AndroidUtilities.dp(24.0f);
                    }
                    ContentPreviewViewer contentPreviewViewer = ContentPreviewViewer.this;
                    contentPreviewViewer.popupWindow.showAtLocation(contentPreviewViewer.containerView, 0, (int) ((ContentPreviewViewer.this.containerView.getMeasuredWidth() - actionBarPopupWindowLayout.getMeasuredWidth()) / 2.0f), max);
                    ContentPreviewViewer.this.containerView.performHapticFeedback(0);
                    return;
                }
                ContentPreviewViewer.this.showUnlockPremiumView();
                ContentPreviewViewer.this.menuVisible = true;
                ContentPreviewViewer.this.containerView.invalidate();
                ContentPreviewViewer.this.containerView.performHapticFeedback(0);
            } else if (ContentPreviewViewer.this.delegate != null) {
                ContentPreviewViewer.this.menuVisible = true;
                ArrayList arrayList4 = new ArrayList();
                ArrayList arrayList5 = new ArrayList();
                ArrayList arrayList6 = new ArrayList();
                if (ContentPreviewViewer.this.delegate.needSend() && !ContentPreviewViewer.this.delegate.isInScheduleMode()) {
                    arrayList4.add(LocaleController.getString("SendGifPreview", 2131628188));
                    arrayList6.add(2131165929);
                    arrayList5.add(0);
                }
                if (ContentPreviewViewer.this.delegate.canSchedule()) {
                    arrayList4.add(LocaleController.getString("Schedule", 2131628081));
                    arrayList6.add(2131165643);
                    arrayList5.add(3);
                }
                if (ContentPreviewViewer.this.currentDocument != null) {
                    z = MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).hasRecentGif(ContentPreviewViewer.this.currentDocument);
                    if (z) {
                        arrayList4.add(LocaleController.formatString("Delete", 2131625368, new Object[0]));
                        arrayList6.add(2131165702);
                        arrayList5.add(1);
                    } else {
                        arrayList4.add(LocaleController.formatString("SaveToGIFs", 2131628068, new Object[0]));
                        arrayList6.add(2131165746);
                        arrayList5.add(2);
                    }
                } else {
                    z = false;
                }
                int[] iArr2 = new int[arrayList6.size()];
                for (int i9 = 0; i9 < arrayList6.size(); i9++) {
                    iArr2[i9] = ((Integer) arrayList6.get(i9)).intValue();
                }
                ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout2 = new ActionBarPopupWindow.ActionBarPopupWindowLayout(ContentPreviewViewer.this.containerView.getContext(), 2131166086, ContentPreviewViewer.this.resourcesProvider);
                ContentPreviewViewer$1$$ExternalSyntheticLambda1 contentPreviewViewer$1$$ExternalSyntheticLambda1 = new ContentPreviewViewer$1$$ExternalSyntheticLambda1(this, arrayList5);
                for (int i10 = 0; i10 < arrayList4.size(); i10++) {
                    ActionBarMenuSubItem addItem2 = ActionBarMenuItem.addItem(actionBarPopupWindowLayout2, ((Integer) arrayList6.get(i10)).intValue(), (CharSequence) arrayList4.get(i10), false, ContentPreviewViewer.this.resourcesProvider);
                    addItem2.setTag(Integer.valueOf(i10));
                    addItem2.setOnClickListener(contentPreviewViewer$1$$ExternalSyntheticLambda1);
                    if (z && i10 == arrayList4.size() - 1) {
                        addItem2.setColors(ContentPreviewViewer.this.getThemedColor("dialogTextRed2"), ContentPreviewViewer.this.getThemedColor("dialogRedIcon"));
                    }
                }
                ContentPreviewViewer.this.popupWindow = new AnonymousClass3(actionBarPopupWindowLayout2, -2, -2);
                ContentPreviewViewer.this.popupWindow.setPauseNotifications(true);
                ContentPreviewViewer.this.popupWindow.setDismissAnimationDuration(150);
                ContentPreviewViewer.this.popupWindow.setScaleOut(true);
                ContentPreviewViewer.this.popupWindow.setOutsideTouchable(true);
                ContentPreviewViewer.this.popupWindow.setClippingEnabled(true);
                ContentPreviewViewer.this.popupWindow.setAnimationStyle(2131689480);
                ContentPreviewViewer.this.popupWindow.setFocusable(true);
                actionBarPopupWindowLayout2.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
                ContentPreviewViewer.this.popupWindow.setInputMethodMode(2);
                ContentPreviewViewer.this.popupWindow.getContentView().setFocusableInTouchMode(true);
                if (Build.VERSION.SDK_INT >= 21 && ContentPreviewViewer.this.lastInsets != null) {
                    i2 = ContentPreviewViewer.this.lastInsets.getStableInsetBottom() + ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                    i = ContentPreviewViewer.this.lastInsets.getStableInsetTop();
                } else {
                    i = AndroidUtilities.statusBarHeight;
                    i2 = 0;
                }
                int min2 = Math.min(ContentPreviewViewer.this.containerView.getWidth(), ContentPreviewViewer.this.containerView.getHeight() - i2) - AndroidUtilities.dp(40.0f);
                float f = ContentPreviewViewer.this.moveY;
                int i11 = i + (min2 / 2);
                int dp = ContentPreviewViewer.this.stickerEmojiLayout != null ? AndroidUtilities.dp(40.0f) : 0;
                ContentPreviewViewer contentPreviewViewer2 = ContentPreviewViewer.this;
                contentPreviewViewer2.popupWindow.showAtLocation(contentPreviewViewer2.containerView, 0, (int) ((ContentPreviewViewer.this.containerView.getMeasuredWidth() - actionBarPopupWindowLayout2.getMeasuredWidth()) / 2.0f), (int) (((int) (f + Math.max(i11 + dp, ((ContentPreviewViewer.this.containerView.getHeight() - i2) - ContentPreviewViewer.this.keyboardHeight) / 2) + i3)) + (AndroidUtilities.dp(24.0f) - ContentPreviewViewer.this.moveY)));
                ContentPreviewViewer.this.containerView.performHapticFeedback(0);
                if (ContentPreviewViewer.this.moveY == 0.0f) {
                    return;
                }
                if (ContentPreviewViewer.this.finalMoveY == 0.0f) {
                    ContentPreviewViewer.this.finalMoveY = 0.0f;
                    ContentPreviewViewer contentPreviewViewer3 = ContentPreviewViewer.this;
                    contentPreviewViewer3.startMoveY = contentPreviewViewer3.moveY;
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new ContentPreviewViewer$1$$ExternalSyntheticLambda0(this));
                ofFloat.setDuration(350L);
                ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ofFloat.start();
            }
        }

        /* renamed from: org.telegram.ui.ContentPreviewViewer$1$1 */
        /* loaded from: classes3.dex */
        class View$OnClickListenerC00311 implements View.OnClickListener {
            final /* synthetic */ ArrayList val$actions;
            final /* synthetic */ boolean val$inFavs;

            View$OnClickListenerC00311(ArrayList arrayList, boolean z) {
                AnonymousClass1.this = r1;
                this.val$actions = arrayList;
                this.val$inFavs = z;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ContentPreviewViewer.this.parentActivity == null) {
                    return;
                }
                int intValue = ((Integer) view.getTag()).intValue();
                if (((Integer) this.val$actions.get(intValue)).intValue() == 0 || ((Integer) this.val$actions.get(intValue)).intValue() == 6) {
                    if (ContentPreviewViewer.this.delegate != null) {
                        ContentPreviewViewer.this.delegate.sendSticker(ContentPreviewViewer.this.currentDocument, ContentPreviewViewer.this.currentQuery, ContentPreviewViewer.this.parentObject, ((Integer) this.val$actions.get(intValue)).intValue() == 0, 0);
                    }
                } else if (((Integer) this.val$actions.get(intValue)).intValue() == 1) {
                    if (ContentPreviewViewer.this.delegate != null) {
                        ContentPreviewViewer.this.delegate.openSet(ContentPreviewViewer.this.currentStickerSet, ContentPreviewViewer.this.clearsInputField);
                    }
                } else if (((Integer) this.val$actions.get(intValue)).intValue() == 2) {
                    MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).addRecentSticker(2, ContentPreviewViewer.this.parentObject, ContentPreviewViewer.this.currentDocument, (int) (System.currentTimeMillis() / 1000), this.val$inFavs);
                } else if (((Integer) this.val$actions.get(intValue)).intValue() == 3) {
                    TLRPC$Document tLRPC$Document = ContentPreviewViewer.this.currentDocument;
                    Object obj = ContentPreviewViewer.this.parentObject;
                    String str = ContentPreviewViewer.this.currentQuery;
                    ContentPreviewViewerDelegate contentPreviewViewerDelegate = ContentPreviewViewer.this.delegate;
                    AlertsCreator.createScheduleDatePickerDialog(ContentPreviewViewer.this.parentActivity, contentPreviewViewerDelegate.getDialogId(), new ContentPreviewViewer$1$1$$ExternalSyntheticLambda0(contentPreviewViewerDelegate, tLRPC$Document, str, obj));
                } else if (((Integer) this.val$actions.get(intValue)).intValue() == 4) {
                    MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).addRecentSticker(0, ContentPreviewViewer.this.parentObject, ContentPreviewViewer.this.currentDocument, (int) (System.currentTimeMillis() / 1000), true);
                } else if (((Integer) this.val$actions.get(intValue)).intValue() == 5) {
                    ContentPreviewViewer.this.delegate.remove(ContentPreviewViewer.this.importingSticker);
                }
                ActionBarPopupWindow actionBarPopupWindow = ContentPreviewViewer.this.popupWindow;
                if (actionBarPopupWindow == null) {
                    return;
                }
                actionBarPopupWindow.dismiss();
            }
        }

        /* renamed from: org.telegram.ui.ContentPreviewViewer$1$2 */
        /* loaded from: classes3.dex */
        class AnonymousClass2 extends ActionBarPopupWindow {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass2(View view, int i, int i2) {
                super(view, i, i2);
                AnonymousClass1.this = r1;
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
        }

        public /* synthetic */ void lambda$run$1(ArrayList arrayList, View view) {
            if (ContentPreviewViewer.this.parentActivity == null) {
                return;
            }
            int intValue = ((Integer) view.getTag()).intValue();
            if (((Integer) arrayList.get(intValue)).intValue() == 0) {
                ContentPreviewViewer.this.delegate.sendGif(ContentPreviewViewer.this.currentDocument != null ? ContentPreviewViewer.this.currentDocument : ContentPreviewViewer.this.inlineResult, ContentPreviewViewer.this.parentObject, true, 0);
            } else if (((Integer) arrayList.get(intValue)).intValue() == 1) {
                MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).removeRecentGif(ContentPreviewViewer.this.currentDocument);
                ContentPreviewViewer.this.delegate.gifAddedOrDeleted();
            } else if (((Integer) arrayList.get(intValue)).intValue() == 2) {
                MediaDataController.getInstance(ContentPreviewViewer.this.currentAccount).addRecentGif(ContentPreviewViewer.this.currentDocument, (int) (System.currentTimeMillis() / 1000), true);
                MessagesController.getInstance(ContentPreviewViewer.this.currentAccount).saveGif("gif", ContentPreviewViewer.this.currentDocument);
                ContentPreviewViewer.this.delegate.gifAddedOrDeleted();
            } else if (((Integer) arrayList.get(intValue)).intValue() == 3) {
                TLRPC$Document tLRPC$Document = ContentPreviewViewer.this.currentDocument;
                TLRPC$BotInlineResult tLRPC$BotInlineResult = ContentPreviewViewer.this.inlineResult;
                Object obj = ContentPreviewViewer.this.parentObject;
                ContentPreviewViewerDelegate contentPreviewViewerDelegate = ContentPreviewViewer.this.delegate;
                AlertsCreator.createScheduleDatePickerDialog(ContentPreviewViewer.this.parentActivity, contentPreviewViewerDelegate.getDialogId(), new ContentPreviewViewer$1$$ExternalSyntheticLambda2(contentPreviewViewerDelegate, tLRPC$Document, tLRPC$BotInlineResult, obj), ContentPreviewViewer.this.resourcesProvider);
            }
            ActionBarPopupWindow actionBarPopupWindow = ContentPreviewViewer.this.popupWindow;
            if (actionBarPopupWindow == null) {
                return;
            }
            actionBarPopupWindow.dismiss();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ void lambda$run$0(ContentPreviewViewerDelegate contentPreviewViewerDelegate, TLRPC$Document tLRPC$Document, TLRPC$BotInlineResult tLRPC$BotInlineResult, Object obj, boolean z, int i) {
            if (tLRPC$Document == null) {
                tLRPC$Document = tLRPC$BotInlineResult;
            }
            contentPreviewViewerDelegate.sendGif(tLRPC$Document, obj, z, i);
        }

        /* renamed from: org.telegram.ui.ContentPreviewViewer$1$3 */
        /* loaded from: classes3.dex */
        class AnonymousClass3 extends ActionBarPopupWindow {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass3(View view, int i, int i2) {
                super(view, i, i2);
                AnonymousClass1.this = r1;
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
        }

        public /* synthetic */ void lambda$run$2(ValueAnimator valueAnimator) {
            ContentPreviewViewer.this.currentMoveYProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ContentPreviewViewer contentPreviewViewer = ContentPreviewViewer.this;
            contentPreviewViewer.moveY = contentPreviewViewer.startMoveY + ((ContentPreviewViewer.this.finalMoveY - ContentPreviewViewer.this.startMoveY) * ContentPreviewViewer.this.currentMoveYProgress);
            ContentPreviewViewer.this.containerView.invalidate();
        }
    }

    public void showUnlockPremiumView() {
        if (this.unlockPremiumView == null) {
            UnlockPremiumView unlockPremiumView = new UnlockPremiumView(this.containerView.getContext(), 0, this.resourcesProvider);
            this.unlockPremiumView = unlockPremiumView;
            this.containerView.addView(unlockPremiumView, LayoutHelper.createFrame(-1, -1.0f));
            this.unlockPremiumView.setOnClickListener(new ContentPreviewViewer$$ExternalSyntheticLambda1(this));
            this.unlockPremiumView.premiumButtonView.buttonLayout.setOnClickListener(new ContentPreviewViewer$$ExternalSyntheticLambda2(this));
        }
        AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, false, 1.0f, false);
        AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, true);
        this.unlockPremiumView.setTranslationY(0.0f);
    }

    public /* synthetic */ void lambda$showUnlockPremiumView$0(View view) {
        this.menuVisible = false;
        this.containerView.invalidate();
        close();
    }

    public /* synthetic */ void lambda$showUnlockPremiumView$1(View view) {
        Activity activity = this.parentActivity;
        if (activity instanceof LaunchActivity) {
            LaunchActivity launchActivity = (LaunchActivity) activity;
            if (launchActivity.getActionBarLayout() != null && launchActivity.getActionBarLayout().getLastFragment() != null) {
                launchActivity.getActionBarLayout().getLastFragment().dismissCurrentDialog();
            }
            launchActivity.lambda$runLinkRequest$59(new PremiumPreviewFragment(PremiumPreviewFragment.featureTypeToServerString(5)));
        }
        this.menuVisible = false;
        this.containerView.invalidate();
        close();
    }

    public static ContentPreviewViewer getInstance() {
        ContentPreviewViewer contentPreviewViewer = Instance;
        if (contentPreviewViewer == null) {
            synchronized (PhotoViewer.class) {
                contentPreviewViewer = Instance;
                if (contentPreviewViewer == null) {
                    contentPreviewViewer = new ContentPreviewViewer();
                    Instance = contentPreviewViewer;
                }
            }
        }
        return contentPreviewViewer;
    }

    public static boolean hasInstance() {
        return Instance != null;
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

    /* JADX WARN: Removed duplicated region for block: B:64:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0169  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x019e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouch(MotionEvent motionEvent, RecyclerListView recyclerListView, int i, Object obj, ContentPreviewViewerDelegate contentPreviewViewerDelegate, Theme.ResourcesProvider resourcesProvider) {
        int i2;
        View view;
        ActionBarPopupWindow actionBarPopupWindow;
        View view2;
        this.delegate = contentPreviewViewerDelegate;
        this.resourcesProvider = resourcesProvider;
        if (this.openPreviewRunnable != null || isVisible()) {
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3 || motionEvent.getAction() == 6) {
                AndroidUtilities.runOnUIThread(new ContentPreviewViewer$$ExternalSyntheticLambda4(recyclerListView, obj), 150L);
                Runnable runnable = this.openPreviewRunnable;
                if (runnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable);
                    this.openPreviewRunnable = null;
                } else if (isVisible()) {
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
            } else if (motionEvent.getAction() != 0) {
                if (this.isVisible) {
                    if (motionEvent.getAction() == 2) {
                        if (this.currentContentType == 1) {
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
                                if (childAt instanceof StickerEmojiCell) {
                                    this.centerImage.setRoundRadius(0);
                                } else if (childAt instanceof StickerCell) {
                                    this.centerImage.setRoundRadius(0);
                                } else {
                                    if (childAt instanceof ContextLinkCell) {
                                        ContextLinkCell contextLinkCell = (ContextLinkCell) childAt;
                                        if (contextLinkCell.isSticker()) {
                                            this.centerImage.setRoundRadius(0);
                                        } else if (contextLinkCell.isGif()) {
                                            this.centerImage.setRoundRadius(AndroidUtilities.dp(6.0f));
                                            i2 = 1;
                                            if (i2 != -1 && childAt != (view = this.currentPreviewCell)) {
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
                                                actionBarPopupWindow = this.popupWindow;
                                                if (actionBarPopupWindow != null) {
                                                    actionBarPopupWindow.dismiss();
                                                }
                                                AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, false);
                                                view2 = this.currentPreviewCell;
                                                if (!(view2 instanceof StickerEmojiCell)) {
                                                    StickerEmojiCell stickerEmojiCell = (StickerEmojiCell) view2;
                                                    TLRPC$Document sticker = stickerEmojiCell.getSticker();
                                                    SendMessagesHelper.ImportingSticker stickerPath = stickerEmojiCell.getStickerPath();
                                                    String emoji = stickerEmojiCell.getEmoji();
                                                    ContentPreviewViewerDelegate contentPreviewViewerDelegate2 = this.delegate;
                                                    open(sticker, stickerPath, emoji, contentPreviewViewerDelegate2 != null ? contentPreviewViewerDelegate2.getQuery(false) : null, null, i2, stickerEmojiCell.isRecent(), stickerEmojiCell.getParentObject(), resourcesProvider);
                                                    stickerEmojiCell.setScaled(true);
                                                } else if (view2 instanceof StickerCell) {
                                                    StickerCell stickerCell = (StickerCell) view2;
                                                    TLRPC$Document sticker2 = stickerCell.getSticker();
                                                    ContentPreviewViewerDelegate contentPreviewViewerDelegate3 = this.delegate;
                                                    open(sticker2, null, null, contentPreviewViewerDelegate3 != null ? contentPreviewViewerDelegate3.getQuery(false) : null, null, i2, false, stickerCell.getParentObject(), resourcesProvider);
                                                    stickerCell.setScaled(true);
                                                    this.clearsInputField = stickerCell.isClearsInputField();
                                                } else if (view2 instanceof ContextLinkCell) {
                                                    ContextLinkCell contextLinkCell2 = (ContextLinkCell) view2;
                                                    TLRPC$Document document = contextLinkCell2.getDocument();
                                                    ContentPreviewViewerDelegate contentPreviewViewerDelegate4 = this.delegate;
                                                    open(document, null, null, contentPreviewViewerDelegate4 != null ? contentPreviewViewerDelegate4.getQuery(true) : null, contextLinkCell2.getBotInlineResult(), i2, false, contextLinkCell2.getBotInlineResult() != null ? contextLinkCell2.getInlineBot() : contextLinkCell2.getParentObject(), resourcesProvider);
                                                    if (i2 != 1) {
                                                        contextLinkCell2.setScaled(true);
                                                    }
                                                }
                                                runSmoothHaptic();
                                                return true;
                                            }
                                        }
                                    }
                                    i2 = -1;
                                    if (i2 != -1) {
                                        if (!(view instanceof StickerEmojiCell)) {
                                        }
                                        this.currentPreviewCell = childAt;
                                        this.clearsInputField = false;
                                        this.menuVisible = false;
                                        this.closeOnDismiss = false;
                                        actionBarPopupWindow = this.popupWindow;
                                        if (actionBarPopupWindow != null) {
                                        }
                                        AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, false);
                                        view2 = this.currentPreviewCell;
                                        if (!(view2 instanceof StickerEmojiCell)) {
                                        }
                                        runSmoothHaptic();
                                        return true;
                                    }
                                }
                                i2 = 0;
                                if (i2 != -1) {
                                }
                            }
                        }
                    }
                    return true;
                } else if (this.openPreviewRunnable != null) {
                    if (motionEvent.getAction() == 2) {
                        if (Math.hypot(this.startX - motionEvent.getX(), this.startY - motionEvent.getY()) > AndroidUtilities.dp(10.0f)) {
                            AndroidUtilities.cancelRunOnUIThread(this.openPreviewRunnable);
                            this.openPreviewRunnable = null;
                        }
                    } else {
                        AndroidUtilities.cancelRunOnUIThread(this.openPreviewRunnable);
                        this.openPreviewRunnable = null;
                    }
                }
            }
        }
        return false;
    }

    public static /* synthetic */ void lambda$onTouch$2(RecyclerListView recyclerListView, Object obj) {
        if (recyclerListView instanceof RecyclerListView) {
            recyclerListView.setOnItemClickListener((RecyclerListView.OnItemClickListener) obj);
        }
    }

    protected void runSmoothHaptic() {
        if (Build.VERSION.SDK_INT >= 26) {
            Vibrator vibrator = (Vibrator) this.containerView.getContext().getSystemService("vibrator");
            if (this.vibrationEffect == null) {
                this.vibrationEffect = VibrationEffect.createWaveform(new long[]{0, 2}, -1);
            }
            vibrator.cancel();
            vibrator.vibrate(this.vibrationEffect);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent, RecyclerListView recyclerListView, int i, ContentPreviewViewerDelegate contentPreviewViewerDelegate, Theme.ResourcesProvider resourcesProvider) {
        int i2;
        this.delegate = contentPreviewViewerDelegate;
        this.resourcesProvider = resourcesProvider;
        if (motionEvent.getAction() == 0) {
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
                    if (childAt instanceof StickerEmojiCell) {
                        if (((StickerEmojiCell) childAt).showingBitmap()) {
                            this.centerImage.setRoundRadius(0);
                            i2 = 0;
                        }
                        i2 = -1;
                    } else if (childAt instanceof StickerCell) {
                        if (((StickerCell) childAt).showingBitmap()) {
                            this.centerImage.setRoundRadius(0);
                            i2 = 0;
                        }
                        i2 = -1;
                    } else {
                        if (childAt instanceof ContextLinkCell) {
                            ContextLinkCell contextLinkCell = (ContextLinkCell) childAt;
                            if (contextLinkCell.showingBitmap()) {
                                if (contextLinkCell.isSticker()) {
                                    this.centerImage.setRoundRadius(0);
                                    i2 = 0;
                                } else if (contextLinkCell.isGif()) {
                                    this.centerImage.setRoundRadius(AndroidUtilities.dp(6.0f));
                                    i2 = 1;
                                }
                            }
                        }
                        i2 = -1;
                    }
                    if (i2 == -1) {
                        return false;
                    }
                    this.startX = x;
                    this.startY = y;
                    this.currentPreviewCell = childAt;
                    ContentPreviewViewer$$ExternalSyntheticLambda6 contentPreviewViewer$$ExternalSyntheticLambda6 = new ContentPreviewViewer$$ExternalSyntheticLambda6(this, recyclerListView, i2, resourcesProvider);
                    this.openPreviewRunnable = contentPreviewViewer$$ExternalSyntheticLambda6;
                    AndroidUtilities.runOnUIThread(contentPreviewViewer$$ExternalSyntheticLambda6, 200L);
                    return true;
                }
            }
        }
        return false;
    }

    public /* synthetic */ void lambda$onInterceptTouchEvent$3(RecyclerListView recyclerListView, int i, Theme.ResourcesProvider resourcesProvider) {
        if (this.openPreviewRunnable == null) {
            return;
        }
        recyclerListView.setOnItemClickListener((RecyclerListView.OnItemClickListener) null);
        recyclerListView.requestDisallowInterceptTouchEvent(true);
        this.openPreviewRunnable = null;
        setParentActivity((Activity) recyclerListView.getContext());
        this.clearsInputField = false;
        View view = this.currentPreviewCell;
        if (view instanceof StickerEmojiCell) {
            StickerEmojiCell stickerEmojiCell = (StickerEmojiCell) view;
            TLRPC$Document sticker = stickerEmojiCell.getSticker();
            SendMessagesHelper.ImportingSticker stickerPath = stickerEmojiCell.getStickerPath();
            String emoji = stickerEmojiCell.getEmoji();
            ContentPreviewViewerDelegate contentPreviewViewerDelegate = this.delegate;
            open(sticker, stickerPath, emoji, contentPreviewViewerDelegate != null ? contentPreviewViewerDelegate.getQuery(false) : null, null, i, stickerEmojiCell.isRecent(), stickerEmojiCell.getParentObject(), resourcesProvider);
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
            if (i != 1) {
                contextLinkCell.setScaled(true);
            }
        }
        this.currentPreviewCell.performHapticFeedback(0, 2);
    }

    public void setParentActivity(Activity activity) {
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        this.centerImage.setCurrentAccount(i);
        this.centerImage.setLayerNum(Integer.MAX_VALUE);
        this.effectImage.setCurrentAccount(this.currentAccount);
        this.effectImage.setLayerNum(Integer.MAX_VALUE);
        if (this.parentActivity == activity) {
            return;
        }
        this.parentActivity = activity;
        this.slideUpDrawable = activity.getResources().getDrawable(2131166088);
        FrameLayout frameLayout = new FrameLayout(activity);
        this.windowView = frameLayout;
        frameLayout.setFocusable(true);
        this.windowView.setFocusableInTouchMode(true);
        int i2 = Build.VERSION.SDK_INT;
        if (i2 >= 21) {
            this.windowView.setFitsSystemWindows(true);
            this.windowView.setOnApplyWindowInsetsListener(new ContentPreviewViewer$$ExternalSyntheticLambda0(this));
        }
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(activity);
        this.containerView = anonymousClass2;
        anonymousClass2.setFocusable(false);
        this.windowView.addView(this.containerView, LayoutHelper.createFrame(-1, -1, 51));
        this.containerView.setOnTouchListener(new ContentPreviewViewer$$ExternalSyntheticLambda3(this));
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
            layoutParams.flags = -2147417848;
        } else {
            layoutParams.flags = 8;
        }
        this.centerImage.setAspectFit(true);
        this.centerImage.setInvalidateAll(true);
        this.centerImage.setParentView(this.containerView);
        this.effectImage.setAspectFit(true);
        this.effectImage.setInvalidateAll(true);
        this.effectImage.setParentView(this.containerView);
    }

    public /* synthetic */ WindowInsets lambda$setParentActivity$4(View view, WindowInsets windowInsets) {
        this.lastInsets = windowInsets;
        return windowInsets;
    }

    /* renamed from: org.telegram.ui.ContentPreviewViewer$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends FrameLayoutDrawer {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            ContentPreviewViewer.this = r1;
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
    }

    public /* synthetic */ boolean lambda$setParentActivity$5(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 6 || motionEvent.getAction() == 3) {
            close();
        }
        return true;
    }

    public void open(TLRPC$Document tLRPC$Document, SendMessagesHelper.ImportingSticker importingSticker, String str, String str2, TLRPC$BotInlineResult tLRPC$BotInlineResult, int i, boolean z, Object obj, Theme.ResourcesProvider resourcesProvider) {
        int i2;
        TLRPC$InputStickerSet tLRPC$InputStickerSet;
        ContentPreviewViewerDelegate contentPreviewViewerDelegate;
        if (this.parentActivity == null || this.windowView == null) {
            return;
        }
        this.resourcesProvider = resourcesProvider;
        this.isRecentSticker = z;
        this.stickerEmojiLayout = null;
        this.backgroundDrawable.setColor(Theme.getActiveTheme().isDark() ? 1895825408 : 1692853990);
        this.drawEffect = false;
        if (i != 0) {
            if (tLRPC$Document != null) {
                TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 90);
                TLRPC$VideoSize documentVideoThumb = MessageObject.getDocumentVideoThumb(tLRPC$Document);
                ImageLocation forDocument = ImageLocation.getForDocument(tLRPC$Document);
                forDocument.imageType = 2;
                if (documentVideoThumb != null) {
                    ImageReceiver imageReceiver = this.centerImage;
                    ImageLocation forDocument2 = ImageLocation.getForDocument(documentVideoThumb, tLRPC$Document);
                    ImageLocation forDocument3 = ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$Document);
                    long j = tLRPC$Document.size;
                    imageReceiver.setImage(forDocument, null, forDocument2, null, forDocument3, "90_90_b", null, j, null, "gif" + tLRPC$Document, 0);
                } else {
                    ImageReceiver imageReceiver2 = this.centerImage;
                    ImageLocation forDocument4 = ImageLocation.getForDocument(closestPhotoSizeWithSize, tLRPC$Document);
                    long j2 = tLRPC$Document.size;
                    imageReceiver2.setImage(forDocument, null, forDocument4, "90_90_b", j2, null, "gif" + tLRPC$Document, 0);
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
            AndroidUtilities.runOnUIThread(this.showSheetRunnable, 2000L);
        } else if (tLRPC$Document == null && importingSticker == null) {
            return;
        } else {
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
                if (tLRPC$InputStickerSet != null && ((contentPreviewViewerDelegate = this.delegate) == null || contentPreviewViewerDelegate.needMenu())) {
                    AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                    AndroidUtilities.runOnUIThread(this.showSheetRunnable, 1300L);
                }
                this.currentStickerSet = tLRPC$InputStickerSet;
                TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(tLRPC$Document.thumbs, 90);
                if (MessageObject.isVideoStickerDocument(tLRPC$Document)) {
                    this.centerImage.setImage(ImageLocation.getForDocument(tLRPC$Document), null, ImageLocation.getForDocument(closestPhotoSizeWithSize2, tLRPC$Document), null, null, 0L, "webp", this.currentStickerSet, 1);
                } else {
                    this.centerImage.setImage(ImageLocation.getForDocument(tLRPC$Document), (String) null, ImageLocation.getForDocument(closestPhotoSizeWithSize2, tLRPC$Document), (String) null, "webp", this.currentStickerSet, 1);
                    if (MessageObject.isPremiumSticker(tLRPC$Document)) {
                        this.drawEffect = true;
                        this.effectImage.setImage(ImageLocation.getForDocument(MessageObject.getPremiumStickerAnimation(tLRPC$Document), tLRPC$Document), (String) null, (ImageLocation) null, (String) null, "tgs", this.currentStickerSet, 1);
                    }
                }
                int i4 = 0;
                while (true) {
                    if (i4 >= tLRPC$Document.attributes.size()) {
                        break;
                    }
                    TLRPC$DocumentAttribute tLRPC$DocumentAttribute2 = tLRPC$Document.attributes.get(i4);
                    if ((tLRPC$DocumentAttribute2 instanceof TLRPC$TL_documentAttributeSticker) && !TextUtils.isEmpty(tLRPC$DocumentAttribute2.alt)) {
                        this.stickerEmojiLayout = new StaticLayout(Emoji.replaceEmoji(tLRPC$DocumentAttribute2.alt, textPaint.getFontMetricsInt(), AndroidUtilities.dp(24.0f), false), textPaint, AndroidUtilities.dp(100.0f), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        break;
                    }
                    i4++;
                }
            } else if (importingSticker != null) {
                this.centerImage.setImage(importingSticker.path, null, null, importingSticker.animated ? "tgs" : null, 0L);
                if (str != null) {
                    this.stickerEmojiLayout = new StaticLayout(Emoji.replaceEmoji(str, textPaint.getFontMetricsInt(), AndroidUtilities.dp(24.0f), false), textPaint, AndroidUtilities.dp(100.0f), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
                if (this.delegate.needMenu()) {
                    AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
                    AndroidUtilities.runOnUIThread(this.showSheetRunnable, 1300L);
                }
            }
        }
        if (this.centerImage.getLottieAnimation() != null) {
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
            return;
        }
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
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.stopAllHeavyOperations, 8);
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public void closeWithMenu() {
        this.menuVisible = false;
        ActionBarPopupWindow actionBarPopupWindow = this.popupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
            this.popupWindow = null;
        }
        close();
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
        UnlockPremiumView unlockPremiumView = this.unlockPremiumView;
        if (unlockPremiumView != null) {
            unlockPremiumView.animate().alpha(0.0f).translationY(AndroidUtilities.dp(56.0f)).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.startAllHeavyOperations, 8);
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
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.startAllHeavyOperations, 8);
    }

    private float rubberYPoisition(float f, float f2) {
        float f3 = 1.0f;
        float f4 = -((1.0f - (1.0f / (((Math.abs(f) * 0.55f) / f2) + 1.0f))) * f2);
        if (f >= 0.0f) {
            f3 = -1.0f;
        }
        return f4 * f3;
    }

    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        Drawable drawable;
        float f;
        WindowInsets windowInsets;
        float f2;
        if (this.containerView == null || this.backgroundDrawable == null) {
            return;
        }
        if (this.menuVisible && this.blurrBitmap == null) {
            prepareBlurBitmap();
        }
        if (this.blurrBitmap != null) {
            boolean z = this.menuVisible;
            if (z) {
                float f3 = this.blurProgress;
                if (f3 != 1.0f) {
                    float f4 = f3 + 0.13333334f;
                    this.blurProgress = f4;
                    if (f4 > 1.0f) {
                        this.blurProgress = 1.0f;
                    }
                    this.containerView.invalidate();
                    f2 = this.blurProgress;
                    if (f2 != 0.0f && this.blurrBitmap != null) {
                        this.paint.setAlpha((int) (f2 * 255.0f));
                        canvas.save();
                        canvas.scale(12.0f, 12.0f);
                        canvas.drawBitmap(this.blurrBitmap, 0.0f, 0.0f, this.paint);
                        canvas.restore();
                    }
                }
            }
            if (!z) {
                float f5 = this.blurProgress;
                if (f5 != 0.0f) {
                    float f6 = f5 - 0.13333334f;
                    this.blurProgress = f6;
                    if (f6 < 0.0f) {
                        this.blurProgress = 0.0f;
                    }
                    this.containerView.invalidate();
                }
            }
            f2 = this.blurProgress;
            if (f2 != 0.0f) {
                this.paint.setAlpha((int) (f2 * 255.0f));
                canvas.save();
                canvas.scale(12.0f, 12.0f);
                canvas.drawBitmap(this.blurrBitmap, 0.0f, 0.0f, this.paint);
                canvas.restore();
            }
        }
        this.backgroundDrawable.setAlpha((int) (this.showProgress * 180.0f));
        this.backgroundDrawable.setBounds(0, 0, this.containerView.getWidth(), this.containerView.getHeight());
        this.backgroundDrawable.draw(canvas);
        canvas.save();
        if (Build.VERSION.SDK_INT >= 21 && (windowInsets = this.lastInsets) != null) {
            i2 = windowInsets.getStableInsetBottom() + this.lastInsets.getStableInsetTop();
            i = this.lastInsets.getStableInsetTop();
        } else {
            i = AndroidUtilities.statusBarHeight;
            i2 = 0;
        }
        if (this.currentContentType == 1) {
            i3 = Math.min(this.containerView.getWidth(), this.containerView.getHeight() - i2) - AndroidUtilities.dp(40.0f);
        } else {
            if (this.drawEffect) {
                f = Math.min(this.containerView.getWidth(), this.containerView.getHeight() - i2) - AndroidUtilities.dpf2(40.0f);
            } else {
                f = Math.min(this.containerView.getWidth(), this.containerView.getHeight() - i2) / 1.8f;
            }
            i3 = (int) f;
        }
        float max = Math.max((i3 / 2) + i + (this.stickerEmojiLayout != null ? AndroidUtilities.dp(40.0f) : 0), ((this.containerView.getHeight() - i2) - this.keyboardHeight) / 2);
        if (this.drawEffect) {
            max += AndroidUtilities.dp(40.0f);
        }
        canvas.translate(this.containerView.getWidth() / 2, this.moveY + max);
        float f7 = this.showProgress;
        int i4 = (int) (i3 * ((f7 * 0.8f) / 0.8f));
        if (this.drawEffect) {
            float f8 = i4;
            float f9 = 0.6669f * f8;
            this.centerImage.setAlpha(f7);
            float f10 = f8 - f9;
            float f11 = f8 / 2.0f;
            this.centerImage.setImageCoords((f10 - f11) - (0.0546875f * f8), (f10 / 2.0f) - f11, f9, f9);
            this.centerImage.draw(canvas);
            this.effectImage.setAlpha(this.showProgress);
            float f12 = (-i4) / 2.0f;
            this.effectImage.setImageCoords(f12, f12, f8, f8);
            this.effectImage.draw(canvas);
        } else {
            this.centerImage.setAlpha(f7);
            float f13 = (-i4) / 2.0f;
            float f14 = i4;
            this.centerImage.setImageCoords(f13, f13, f14, f14);
            this.centerImage.draw(canvas);
        }
        if (this.currentContentType == 1 && (drawable = this.slideUpDrawable) != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = this.slideUpDrawable.getIntrinsicHeight();
            int dp = (int) (this.centerImage.getDrawRegion().top - AndroidUtilities.dp(((this.currentMoveY / AndroidUtilities.dp(60.0f)) * 6.0f) + 17.0f));
            this.slideUpDrawable.setAlpha((int) ((1.0f - this.currentMoveYProgress) * 255.0f));
            this.slideUpDrawable.setBounds((-intrinsicWidth) / 2, (-intrinsicHeight) + dp, intrinsicWidth / 2, dp);
            this.slideUpDrawable.draw(canvas);
        }
        if (this.stickerEmojiLayout != null) {
            if (this.drawEffect) {
                canvas.translate(-AndroidUtilities.dp(50.0f), ((-this.effectImage.getImageHeight()) / 2.0f) - AndroidUtilities.dp(30.0f));
            } else {
                canvas.translate(-AndroidUtilities.dp(50.0f), ((-this.centerImage.getImageHeight()) / 2.0f) - AndroidUtilities.dp(30.0f));
            }
            this.stickerEmojiLayout.draw(canvas);
        }
        canvas.restore();
        if (this.isVisible) {
            if (this.showProgress == 1.0f) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            this.lastUpdateTime = currentTimeMillis;
            this.showProgress += ((float) (currentTimeMillis - this.lastUpdateTime)) / 120.0f;
            this.containerView.invalidate();
            if (this.showProgress <= 1.0f) {
                return;
            }
            this.showProgress = 1.0f;
        } else if (this.showProgress == 0.0f) {
        } else {
            long currentTimeMillis2 = System.currentTimeMillis();
            this.lastUpdateTime = currentTimeMillis2;
            this.showProgress -= ((float) (currentTimeMillis2 - this.lastUpdateTime)) / 120.0f;
            this.containerView.invalidate();
            if (this.showProgress < 0.0f) {
                this.showProgress = 0.0f;
            }
            if (this.showProgress != 0.0f) {
                return;
            }
            this.centerImage.setImageBitmap((Drawable) null);
            AndroidUtilities.unlockOrientation(this.parentActivity);
            AndroidUtilities.runOnUIThread(new ContentPreviewViewer$$ExternalSyntheticLambda5(this));
            Bitmap bitmap = this.blurrBitmap;
            if (bitmap != null) {
                bitmap.recycle();
                this.blurrBitmap = null;
            }
            AndroidUtilities.updateViewVisibilityAnimated(this.unlockPremiumView, false, 1.0f, false);
            this.blurProgress = 0.0f;
            try {
                if (this.windowView.getParent() == null) {
                    return;
                }
                ((WindowManager) this.parentActivity.getSystemService("window")).removeView(this.windowView);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public /* synthetic */ void lambda$onDraw$6() {
        this.centerImage.setImageBitmap((Bitmap) null);
    }

    public int getThemedColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer color = resourcesProvider != null ? resourcesProvider.getColor(str) : null;
        return color != null ? color.intValue() : Theme.getColor(str);
    }

    private void prepareBlurBitmap() {
        Activity activity = this.parentActivity;
        if (activity == null) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        int measuredWidth = (int) (decorView.getMeasuredWidth() / 12.0f);
        int measuredHeight = (int) (decorView.getMeasuredHeight() / 12.0f);
        Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.scale(0.083333336f, 0.083333336f);
        canvas.drawColor(Theme.getColor("windowBackgroundWhite"));
        decorView.draw(canvas);
        Activity activity2 = this.parentActivity;
        if ((activity2 instanceof LaunchActivity) && ((LaunchActivity) activity2).getActionBarLayout().getLastFragment().getVisibleDialog() != null) {
            ((LaunchActivity) this.parentActivity).getActionBarLayout().getLastFragment().getVisibleDialog().getWindow().getDecorView().draw(canvas);
        }
        Utilities.stackBlurBitmap(createBitmap, Math.max(10, Math.max(measuredWidth, measuredHeight) / 180));
        this.blurrBitmap = createBitmap;
    }

    public boolean showMenuFor(View view) {
        if (view instanceof StickerEmojiCell) {
            Activity findActivity = AndroidUtilities.findActivity(view.getContext());
            if (findActivity == null) {
                return true;
            }
            setParentActivity(findActivity);
            StickerEmojiCell stickerEmojiCell = (StickerEmojiCell) view;
            TLRPC$Document sticker = stickerEmojiCell.getSticker();
            SendMessagesHelper.ImportingSticker stickerPath = stickerEmojiCell.getStickerPath();
            String emoji = stickerEmojiCell.getEmoji();
            ContentPreviewViewerDelegate contentPreviewViewerDelegate = this.delegate;
            open(sticker, stickerPath, emoji, contentPreviewViewerDelegate != null ? contentPreviewViewerDelegate.getQuery(false) : null, null, 0, stickerEmojiCell.isRecent(), stickerEmojiCell.getParentObject(), this.resourcesProvider);
            AndroidUtilities.cancelRunOnUIThread(this.showSheetRunnable);
            AndroidUtilities.runOnUIThread(this.showSheetRunnable, 16L);
            stickerEmojiCell.setScaled(true);
            return true;
        }
        return false;
    }
}
