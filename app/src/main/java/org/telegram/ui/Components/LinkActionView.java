package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_chatInviteExported;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputUserEmpty;
import org.telegram.tgnet.TLRPC$TL_messages_chatInviteImporters;
import org.telegram.tgnet.TLRPC$TL_messages_getChatInviteImporters;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.DialogCell;
/* loaded from: classes3.dex */
public class LinkActionView extends LinearLayout {
    private ActionBarPopupWindow actionBarPopupWindow;
    private final AvatarsContainer avatarsContainer;
    private final TextView copyView;
    private Delegate delegate;
    BaseFragment fragment;
    private final FrameLayout frameLayout;
    private boolean hideRevokeOption;
    private boolean isChannel;
    String link;
    TextView linkView;
    boolean loadingImporters;
    ImageView optionsView;
    private boolean permanent;
    private QRCodeBottomSheet qrCodeBottomSheet;
    private final TextView removeView;
    private final TextView shareView;
    private int usersCount;
    private boolean canEdit = true;
    float[] point = new float[2];

    /* loaded from: classes3.dex */
    public interface Delegate {

        /* renamed from: org.telegram.ui.Components.LinkActionView$Delegate$-CC */
        /* loaded from: classes3.dex */
        public final /* synthetic */ class CC {
            public static void $default$editLink(Delegate delegate) {
            }

            public static void $default$removeLink(Delegate delegate) {
            }

            public static void $default$showUsersForPermanentLink(Delegate delegate) {
            }
        }

        void editLink();

        void removeLink();

        void revokeLink();

        void showUsersForPermanentLink();
    }

    public LinkActionView(Context context, BaseFragment baseFragment, BottomSheet bottomSheet, long j, boolean z, boolean z2) {
        super(context);
        this.fragment = baseFragment;
        this.permanent = z;
        this.isChannel = z2;
        setOrientation(1);
        FrameLayout frameLayout = new FrameLayout(context);
        this.frameLayout = frameLayout;
        TextView textView = new TextView(context);
        this.linkView = textView;
        textView.setPadding(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(40.0f), AndroidUtilities.dp(18.0f));
        this.linkView.setTextSize(1, 16.0f);
        this.linkView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        this.linkView.setSingleLine(true);
        frameLayout.addView(this.linkView);
        ImageView imageView = new ImageView(context);
        this.optionsView = imageView;
        imageView.setImageDrawable(ContextCompat.getDrawable(context, 2131165453));
        this.optionsView.setContentDescription(LocaleController.getString("AccDescrMoreOptions", 2131624003));
        this.optionsView.setScaleType(ImageView.ScaleType.CENTER);
        frameLayout.addView(this.optionsView, LayoutHelper.createFrame(40, 48, 21));
        addView(frameLayout, LayoutHelper.createLinear(-1, -2, 0, 4, 0, 4, 0));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(0);
        TextView textView2 = new TextView(context);
        this.copyView = textView2;
        textView2.setGravity(1);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append((CharSequence) "..").setSpan(new ColoredImageSpan(ContextCompat.getDrawable(context, 2131165698)), 0, 1, 0);
        spannableStringBuilder.setSpan(new DialogCell.FixedWidthSpan(AndroidUtilities.dp(8.0f)), 1, 2, 0);
        spannableStringBuilder.append((CharSequence) LocaleController.getString("LinkActionCopy", 2131626476));
        spannableStringBuilder.append((CharSequence) ".").setSpan(new DialogCell.FixedWidthSpan(AndroidUtilities.dp(5.0f)), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
        textView2.setText(spannableStringBuilder);
        textView2.setContentDescription(LocaleController.getString("LinkActionCopy", 2131626476));
        textView2.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f));
        textView2.setTextSize(1, 14.0f);
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setSingleLine(true);
        linearLayout.addView(textView2, LayoutHelper.createLinear(0, 40, 1.0f, 0, 4, 0, 4, 0));
        TextView textView3 = new TextView(context);
        this.shareView = textView3;
        textView3.setGravity(1);
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
        spannableStringBuilder2.append((CharSequence) "..").setSpan(new ColoredImageSpan(ContextCompat.getDrawable(context, 2131165943)), 0, 1, 0);
        spannableStringBuilder2.setSpan(new DialogCell.FixedWidthSpan(AndroidUtilities.dp(8.0f)), 1, 2, 0);
        spannableStringBuilder2.append((CharSequence) LocaleController.getString("LinkActionShare", 2131626477));
        spannableStringBuilder2.append((CharSequence) ".").setSpan(new DialogCell.FixedWidthSpan(AndroidUtilities.dp(5.0f)), spannableStringBuilder2.length() - 1, spannableStringBuilder2.length(), 0);
        textView3.setText(spannableStringBuilder2);
        textView3.setContentDescription(LocaleController.getString("LinkActionShare", 2131626477));
        textView3.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f));
        textView3.setTextSize(1, 14.0f);
        textView3.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView3.setSingleLine(true);
        linearLayout.addView(textView3, LayoutHelper.createLinear(0, 40, 1.0f, 4, 0, 4, 0));
        TextView textView4 = new TextView(context);
        this.removeView = textView4;
        textView4.setGravity(1);
        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder();
        spannableStringBuilder3.append((CharSequence) "..").setSpan(new ColoredImageSpan(ContextCompat.getDrawable(context, 2131165704)), 0, 1, 0);
        spannableStringBuilder3.setSpan(new DialogCell.FixedWidthSpan(AndroidUtilities.dp(8.0f)), 1, 2, 0);
        spannableStringBuilder3.append((CharSequence) LocaleController.getString("DeleteLink", 2131625429));
        spannableStringBuilder3.append((CharSequence) ".").setSpan(new DialogCell.FixedWidthSpan(AndroidUtilities.dp(5.0f)), spannableStringBuilder3.length() - 1, spannableStringBuilder3.length(), 0);
        textView4.setText(spannableStringBuilder3);
        textView4.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f));
        textView4.setTextSize(1, 14.0f);
        textView4.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView4.setSingleLine(true);
        linearLayout.addView(textView4, LayoutHelper.createLinear(0, -2, 1.0f, 4, 0, 4, 0));
        textView4.setVisibility(8);
        addView(linearLayout, LayoutHelper.createLinear(-1, -2, 0.0f, 20.0f, 0.0f, 0.0f));
        AvatarsContainer avatarsContainer = new AvatarsContainer(context);
        this.avatarsContainer = avatarsContainer;
        addView(avatarsContainer, LayoutHelper.createLinear(-1, 44, 0.0f, 12.0f, 0.0f, 0.0f));
        textView2.setOnClickListener(new LinkActionView$$ExternalSyntheticLambda9(this, bottomSheet, baseFragment));
        if (z) {
            avatarsContainer.setOnClickListener(new LinkActionView$$ExternalSyntheticLambda3(this));
        }
        textView3.setOnClickListener(new LinkActionView$$ExternalSyntheticLambda7(this, baseFragment));
        textView4.setOnClickListener(new LinkActionView$$ExternalSyntheticLambda8(this, baseFragment));
        this.optionsView.setOnClickListener(new LinkActionView$$ExternalSyntheticLambda6(this, context, bottomSheet, baseFragment));
        frameLayout.setOnClickListener(new AnonymousClass4());
        updateColors();
    }

    public /* synthetic */ void lambda$new$0(BottomSheet bottomSheet, BaseFragment baseFragment, View view) {
        try {
            if (this.link == null) {
                return;
            }
            ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", this.link));
            if (bottomSheet != null && bottomSheet.getContainer() != null) {
                BulletinFactory.createCopyLinkBulletin(bottomSheet.getContainer()).show();
            } else {
                BulletinFactory.createCopyLinkBulletin(baseFragment).show();
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$new$1(View view) {
        this.delegate.showUsersForPermanentLink();
    }

    public /* synthetic */ void lambda$new$2(BaseFragment baseFragment, View view) {
        try {
            if (this.link == null) {
                return;
            }
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", this.link);
            baseFragment.startActivityForResult(Intent.createChooser(intent, LocaleController.getString("InviteToGroupByLink", 2131626327)), 500);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$new$4(BaseFragment baseFragment, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString("DeleteLink", 2131625429));
        builder.setMessage(LocaleController.getString("DeleteLinkHelp", 2131625430));
        builder.setPositiveButton(LocaleController.getString("Delete", 2131625384), new LinkActionView$$ExternalSyntheticLambda0(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        baseFragment.showDialog(builder.create());
    }

    public /* synthetic */ void lambda$new$3(DialogInterface dialogInterface, int i) {
        Delegate delegate = this.delegate;
        if (delegate != null) {
            delegate.removeLink();
        }
    }

    public /* synthetic */ void lambda$new$9(Context context, BottomSheet bottomSheet, BaseFragment baseFragment, View view) {
        FrameLayout frameLayout;
        if (this.actionBarPopupWindow != null) {
            return;
        }
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(context);
        if (!this.permanent && this.canEdit) {
            ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(context, true, false);
            actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("Edit", 2131625553), 2131165714);
            actionBarPopupWindowLayout.addView((View) actionBarMenuSubItem, LayoutHelper.createLinear(-1, 48));
            actionBarMenuSubItem.setOnClickListener(new LinkActionView$$ExternalSyntheticLambda4(this));
        }
        ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(context, true, false);
        actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("GetQRCode", 2131626097), 2131165890);
        actionBarPopupWindowLayout.addView((View) actionBarMenuSubItem2, LayoutHelper.createLinear(-1, 48));
        actionBarMenuSubItem2.setOnClickListener(new LinkActionView$$ExternalSyntheticLambda5(this));
        if (!this.hideRevokeOption) {
            ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(context, false, true);
            actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString("RevokeLink", 2131628106), 2131165702);
            actionBarMenuSubItem3.setColors(Theme.getColor("windowBackgroundWhiteRedText"), Theme.getColor("windowBackgroundWhiteRedText"));
            actionBarMenuSubItem3.setOnClickListener(new LinkActionView$$ExternalSyntheticLambda2(this));
            actionBarPopupWindowLayout.addView((View) actionBarMenuSubItem3, LayoutHelper.createLinear(-1, 48));
        }
        if (bottomSheet == null) {
            frameLayout = baseFragment.getParentLayout();
        } else {
            frameLayout = bottomSheet.getContainer();
        }
        if (frameLayout == null) {
            return;
        }
        getPointOnScreen(this.frameLayout, frameLayout, this.point);
        float f = this.point[1];
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(context, frameLayout);
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(this, anonymousClass1);
        frameLayout.getViewTreeObserver().addOnPreDrawListener(anonymousClass2);
        frameLayout.addView(anonymousClass1, LayoutHelper.createFrame(-1, -1.0f));
        float f2 = 0.0f;
        anonymousClass1.setAlpha(0.0f);
        anonymousClass1.animate().alpha(1.0f).setDuration(150L);
        actionBarPopupWindowLayout.measure(View.MeasureSpec.makeMeasureSpec(frameLayout.getMeasuredWidth(), 0), View.MeasureSpec.makeMeasureSpec(frameLayout.getMeasuredHeight(), 0));
        ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(actionBarPopupWindowLayout, -2, -2);
        this.actionBarPopupWindow = actionBarPopupWindow;
        actionBarPopupWindow.setOnDismissListener(new AnonymousClass3(anonymousClass1, frameLayout, anonymousClass2));
        this.actionBarPopupWindow.setOutsideTouchable(true);
        this.actionBarPopupWindow.setFocusable(true);
        this.actionBarPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.actionBarPopupWindow.setAnimationStyle(2131689481);
        this.actionBarPopupWindow.setInputMethodMode(2);
        this.actionBarPopupWindow.setSoftInputMode(0);
        actionBarPopupWindowLayout.setDispatchKeyEventListener(new LinkActionView$$ExternalSyntheticLambda12(this));
        if (AndroidUtilities.isTablet()) {
            f += frameLayout.getPaddingTop();
            f2 = 0.0f - frameLayout.getPaddingLeft();
        }
        this.actionBarPopupWindow.showAtLocation(frameLayout, 0, (int) (((frameLayout.getMeasuredWidth() - actionBarPopupWindowLayout.getMeasuredWidth()) - AndroidUtilities.dp(16.0f)) + frameLayout.getX() + f2), (int) (f + this.frameLayout.getMeasuredHeight() + frameLayout.getY()));
    }

    public /* synthetic */ void lambda$new$5(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.actionBarPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        }
        this.delegate.editLink();
    }

    public /* synthetic */ void lambda$new$6(View view) {
        showQrCode();
    }

    public /* synthetic */ void lambda$new$7(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.actionBarPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        }
        revokeLink();
    }

    /* renamed from: org.telegram.ui.Components.LinkActionView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends View {
        final /* synthetic */ FrameLayout val$finalContainer;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Context context, FrameLayout frameLayout) {
            super(context);
            LinkActionView.this = r1;
            this.val$finalContainer = frameLayout;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(855638016);
            LinkActionView linkActionView = LinkActionView.this;
            linkActionView.getPointOnScreen(linkActionView.frameLayout, this.val$finalContainer, LinkActionView.this.point);
            canvas.save();
            float y = ((View) LinkActionView.this.frameLayout.getParent()).getY() + LinkActionView.this.frameLayout.getY();
            if (y < 1.0f) {
                canvas.clipRect(0.0f, (LinkActionView.this.point[1] - y) + 1.0f, getMeasuredWidth(), getMeasuredHeight());
            }
            float[] fArr = LinkActionView.this.point;
            canvas.translate(fArr[0], fArr[1]);
            LinkActionView.this.frameLayout.draw(canvas);
            canvas.restore();
        }
    }

    /* renamed from: org.telegram.ui.Components.LinkActionView$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ View val$dimView;

        AnonymousClass2(LinkActionView linkActionView, View view) {
            this.val$dimView = view;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            this.val$dimView.invalidate();
            return true;
        }
    }

    /* renamed from: org.telegram.ui.Components.LinkActionView$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 implements PopupWindow.OnDismissListener {
        final /* synthetic */ View val$dimView;
        final /* synthetic */ FrameLayout val$finalContainer;
        final /* synthetic */ ViewTreeObserver.OnPreDrawListener val$preDrawListener;

        AnonymousClass3(View view, FrameLayout frameLayout, ViewTreeObserver.OnPreDrawListener onPreDrawListener) {
            LinkActionView.this = r1;
            this.val$dimView = view;
            this.val$finalContainer = frameLayout;
            this.val$preDrawListener = onPreDrawListener;
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            LinkActionView.this.actionBarPopupWindow = null;
            this.val$dimView.animate().cancel();
            this.val$dimView.animate().alpha(0.0f).setDuration(150L).setListener(new AnonymousClass1());
        }

        /* renamed from: org.telegram.ui.Components.LinkActionView$3$1 */
        /* loaded from: classes3.dex */
        class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
                AnonymousClass3.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (AnonymousClass3.this.val$dimView.getParent() != null) {
                    AnonymousClass3 anonymousClass3 = AnonymousClass3.this;
                    anonymousClass3.val$finalContainer.removeView(anonymousClass3.val$dimView);
                }
                AnonymousClass3.this.val$finalContainer.getViewTreeObserver().removeOnPreDrawListener(AnonymousClass3.this.val$preDrawListener);
            }
        }
    }

    public /* synthetic */ void lambda$new$8(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && this.actionBarPopupWindow.isShowing()) {
            this.actionBarPopupWindow.dismiss(true);
        }
    }

    /* renamed from: org.telegram.ui.Components.LinkActionView$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 implements View.OnClickListener {
        AnonymousClass4() {
            LinkActionView.this = r1;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            LinkActionView.this.copyView.callOnClick();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v9, types: [android.view.View] */
    public void getPointOnScreen(FrameLayout frameLayout, FrameLayout frameLayout2, float[] fArr) {
        float f = 0.0f;
        float f2 = 0.0f;
        FrameLayout frameLayout3 = frameLayout;
        while (frameLayout3 != frameLayout2) {
            f2 += frameLayout3.getY();
            f += frameLayout3.getX();
            if (frameLayout3 instanceof ScrollView) {
                f2 -= frameLayout3.getScrollY();
            }
            ?? r4 = (View) frameLayout3.getParent();
            boolean z = r4 instanceof ViewGroup;
            frameLayout3 = r4;
            if (!z) {
                return;
            }
        }
        fArr[0] = f - frameLayout2.getPaddingLeft();
        fArr[1] = f2 - frameLayout2.getPaddingTop();
    }

    /* renamed from: org.telegram.ui.Components.LinkActionView$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends QRCodeBottomSheet {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(Context context, String str, String str2) {
            super(context, str, str2);
            LinkActionView.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
        public void dismiss() {
            super.dismiss();
            LinkActionView.this.qrCodeBottomSheet = null;
        }
    }

    private void showQrCode() {
        String str;
        int i;
        Context context = getContext();
        String str2 = this.link;
        if (this.isChannel) {
            i = 2131627872;
            str = "QRCodeLinkHelpChannel";
        } else {
            i = 2131627873;
            str = "QRCodeLinkHelpGroup";
        }
        AnonymousClass5 anonymousClass5 = new AnonymousClass5(context, str2, LocaleController.getString(str, i));
        this.qrCodeBottomSheet = anonymousClass5;
        anonymousClass5.show();
        ActionBarPopupWindow actionBarPopupWindow = this.actionBarPopupWindow;
        if (actionBarPopupWindow != null) {
            actionBarPopupWindow.dismiss();
        }
    }

    public void updateColors() {
        this.copyView.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        this.shareView.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        this.removeView.setTextColor(Theme.getColor("featuredStickers_buttonText"));
        this.copyView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed")));
        this.shareView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("featuredStickers_addButton"), Theme.getColor("featuredStickers_addButtonPressed")));
        this.removeView.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("chat_attachAudioBackground"), ColorUtils.setAlphaComponent(Theme.getColor("windowBackgroundWhite"), 120)));
        this.frameLayout.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("graySection"), ColorUtils.setAlphaComponent(Theme.getColor("listSelectorSDK21"), 76)));
        this.linkView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.optionsView.setColorFilter(Theme.getColor("dialogTextGray3"));
        this.avatarsContainer.countTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText"));
        this.avatarsContainer.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), 0, ColorUtils.setAlphaComponent(Theme.getColor("windowBackgroundWhiteBlueText"), 76)));
        QRCodeBottomSheet qRCodeBottomSheet = this.qrCodeBottomSheet;
        if (qRCodeBottomSheet != null) {
            qRCodeBottomSheet.updateColors();
        }
    }

    public void setLink(String str) {
        this.link = str;
        if (str == null) {
            this.linkView.setText(LocaleController.getString("Loading", 2131626520));
        } else if (str.startsWith("https://")) {
            this.linkView.setText(str.substring(8));
        } else {
            this.linkView.setText(str);
        }
    }

    public void setRevoke(boolean z) {
        if (z) {
            this.optionsView.setVisibility(8);
            this.shareView.setVisibility(8);
            this.copyView.setVisibility(8);
            this.removeView.setVisibility(0);
            return;
        }
        this.optionsView.setVisibility(0);
        this.shareView.setVisibility(0);
        this.copyView.setVisibility(0);
        this.removeView.setVisibility(8);
    }

    public void hideRevokeOption(boolean z) {
        if (this.hideRevokeOption != z) {
            this.hideRevokeOption = z;
            this.optionsView.setVisibility(0);
            ImageView imageView = this.optionsView;
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), 2131165453));
        }
    }

    /* loaded from: classes3.dex */
    public class AvatarsContainer extends FrameLayout {
        AvatarsImageView avatarsImageView;
        TextView countTextView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AvatarsContainer(Context context) {
            super(context);
            LinkActionView.this = r6;
            this.avatarsImageView = new AnonymousClass1(context, false, r6);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            addView(linearLayout, LayoutHelper.createFrame(-2, -1, 1));
            TextView textView = new TextView(context);
            this.countTextView = textView;
            textView.setTextSize(1, 14.0f);
            this.countTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            linearLayout.addView(this.avatarsImageView, LayoutHelper.createLinear(-2, -1));
            linearLayout.addView(this.countTextView, LayoutHelper.createLinear(-2, -2, 16));
            setPadding(0, AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f));
            this.avatarsImageView.commitTransition(false);
        }

        /* renamed from: org.telegram.ui.Components.LinkActionView$AvatarsContainer$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AvatarsImageView {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, boolean z, LinkActionView linkActionView) {
                super(context, z);
                AvatarsContainer.this = r1;
            }

            @Override // org.telegram.ui.Components.AvatarsImageView, android.view.View
            public void onMeasure(int i, int i2) {
                int min = Math.min(3, LinkActionView.this.usersCount);
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(min == 0 ? 0 : ((min - 1) * 20) + 24 + 8), 1073741824), i2);
            }
        }
    }

    private void revokeLink() {
        if (this.fragment.getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.fragment.getParentActivity());
        builder.setMessage(LocaleController.getString("RevokeAlert", 2131628103));
        builder.setTitle(LocaleController.getString("RevokeLink", 2131628106));
        builder.setPositiveButton(LocaleController.getString("RevokeButton", 2131628105), new LinkActionView$$ExternalSyntheticLambda1(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
        builder.show();
    }

    public /* synthetic */ void lambda$revokeLink$10(DialogInterface dialogInterface, int i) {
        Delegate delegate = this.delegate;
        if (delegate != null) {
            delegate.revokeLink();
        }
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void setUsers(int i, ArrayList<TLRPC$User> arrayList) {
        this.usersCount = i;
        if (i == 0) {
            this.avatarsContainer.setVisibility(8);
            setPadding(AndroidUtilities.dp(19.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(19.0f), AndroidUtilities.dp(18.0f));
        } else {
            this.avatarsContainer.setVisibility(0);
            setPadding(AndroidUtilities.dp(19.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(19.0f), AndroidUtilities.dp(10.0f));
            this.avatarsContainer.countTextView.setText(LocaleController.formatPluralString("PeopleJoined", i, new Object[0]));
            this.avatarsContainer.requestLayout();
        }
        if (arrayList != null) {
            for (int i2 = 0; i2 < 3; i2++) {
                if (i2 < arrayList.size()) {
                    MessagesController.getInstance(UserConfig.selectedAccount).putUser(arrayList.get(i2), false);
                    this.avatarsContainer.avatarsImageView.setObject(i2, UserConfig.selectedAccount, arrayList.get(i2));
                } else {
                    this.avatarsContainer.avatarsImageView.setObject(i2, UserConfig.selectedAccount, null);
                }
            }
            this.avatarsContainer.avatarsImageView.commitTransition(false);
        }
    }

    public void loadUsers(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported, long j) {
        if (tLRPC$TL_chatInviteExported == null) {
            setUsers(0, null);
            return;
        }
        setUsers(tLRPC$TL_chatInviteExported.usage, tLRPC$TL_chatInviteExported.importers);
        if (tLRPC$TL_chatInviteExported.usage <= 0 || tLRPC$TL_chatInviteExported.importers != null || this.loadingImporters) {
            return;
        }
        TLRPC$TL_messages_getChatInviteImporters tLRPC$TL_messages_getChatInviteImporters = new TLRPC$TL_messages_getChatInviteImporters();
        tLRPC$TL_messages_getChatInviteImporters.link = tLRPC$TL_chatInviteExported.link;
        tLRPC$TL_messages_getChatInviteImporters.peer = MessagesController.getInstance(UserConfig.selectedAccount).getInputPeer(-j);
        tLRPC$TL_messages_getChatInviteImporters.offset_user = new TLRPC$TL_inputUserEmpty();
        tLRPC$TL_messages_getChatInviteImporters.limit = Math.min(tLRPC$TL_chatInviteExported.usage, 3);
        this.loadingImporters = true;
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_messages_getChatInviteImporters, new LinkActionView$$ExternalSyntheticLambda11(this, tLRPC$TL_chatInviteExported));
    }

    public /* synthetic */ void lambda$loadUsers$12(TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LinkActionView$$ExternalSyntheticLambda10(this, tLRPC$TL_error, tLObject, tLRPC$TL_chatInviteExported));
    }

    public /* synthetic */ void lambda$loadUsers$11(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$TL_chatInviteExported tLRPC$TL_chatInviteExported) {
        this.loadingImporters = false;
        if (tLRPC$TL_error == null) {
            TLRPC$TL_messages_chatInviteImporters tLRPC$TL_messages_chatInviteImporters = (TLRPC$TL_messages_chatInviteImporters) tLObject;
            if (tLRPC$TL_chatInviteExported.importers == null) {
                tLRPC$TL_chatInviteExported.importers = new ArrayList<>(3);
            }
            tLRPC$TL_chatInviteExported.importers.clear();
            for (int i = 0; i < tLRPC$TL_messages_chatInviteImporters.users.size(); i++) {
                tLRPC$TL_chatInviteExported.importers.addAll(tLRPC$TL_messages_chatInviteImporters.users);
            }
            setUsers(tLRPC$TL_chatInviteExported.usage, tLRPC$TL_chatInviteExported.importers);
        }
    }

    public void setPermanent(boolean z) {
        this.permanent = z;
    }

    public void setCanEdit(boolean z) {
        this.canEdit = z;
    }
}
