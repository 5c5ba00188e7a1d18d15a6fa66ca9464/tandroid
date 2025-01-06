package org.telegram.ui.Stars;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.core.graphics.ColorUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BirthdayController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ShareDialogCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ButtonSpan;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CompatDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.FireworksOverlay;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.LoadingSpan;
import org.telegram.ui.Components.Premium.boosts.UserSelectorBottomSheet;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.TableView;
import org.telegram.ui.Components.spoilers.SpoilersTextView;
import org.telegram.ui.FilterCreateActivity;
import org.telegram.ui.Gifts.GiftSheet;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stars.StarGiftSheet;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.bots.AffiliateProgramFragment;
import org.telegram.ui.bots.BotWebViewSheet;

/* loaded from: classes4.dex */
public class StarGiftSheet extends BottomSheet {
    private final LinkSpanDrawable.LinksTextView afterTableTextView;
    private final LinkSpanDrawable.LinksTextView beforeTableTextView;
    private final ButtonWithCounterView button;
    private final CheckBox2 checkbox;
    private final LinearLayout checkboxLayout;
    private final View checkboxSeparator;
    private final TextView checkboxTextView;
    private ContainerView container;
    private HintView2 currentHintView;
    private TextView currentHintViewTextView;
    private float currentPage;
    private final long dialogId;
    private final AffiliateProgramFragment.FeatureCell[] featureCells;
    private FireworksOverlay fireworksOverlay;
    private boolean firstSet;
    private final LinearLayout infoLayout;
    private boolean isLearnMore;
    private MessageObject messageObject;
    private boolean messageObjectRepolled;
    private boolean messageObjectRepolling;
    private boolean myProfile;
    private ArrayList sample_attributes;
    private ShareAlert shareAlert;
    private String slug;
    private TL_stars.TL_starGiftUnique slugStarGift;
    private ValueAnimator switchingPagesAnimator;
    private final TableView tableView;
    private final TopView topView;
    private ColoredImageSpan upgradeIconSpan;
    private final LinearLayout upgradeLayout;
    private TLRPC.PaymentForm upgrade_form;
    private TL_stars.UserStarGift userStarGift;

    private class ContainerView extends FrameLayout {
        private final Paint backgroundPaint;
        private final Path path;
        private final RectF rect;

        public ContainerView(Context context) {
            super(context);
            this.rect = new RectF();
            this.backgroundPaint = new Paint(1);
            this.path = new Path();
            setWillNotDraw(false);
            setClipChildren(false);
            setClipToPadding(false);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            canvas.save();
            float pVar = top();
            float dp = AndroidUtilities.dp(12.0f);
            this.rect.set(((BottomSheet) StarGiftSheet.this).backgroundPaddingLeft, pVar, getWidth() - ((BottomSheet) StarGiftSheet.this).backgroundPaddingLeft, getHeight() + dp);
            this.backgroundPaint.setColor(StarGiftSheet.this.getThemedColor(Theme.key_dialogBackground));
            this.path.rewind();
            this.path.addRoundRect(this.rect, dp, dp, Path.Direction.CW);
            canvas.drawPath(this.path, this.backgroundPaint);
            canvas.clipPath(this.path);
            super.dispatchDraw(canvas);
            updateTopViewTranslation();
            canvas.restore();
        }

        public float height() {
            return AndroidUtilities.lerp(StarGiftSheet.this.infoLayout.getMeasuredHeight(), StarGiftSheet.this.upgradeLayout.getMeasuredHeight(), StarGiftSheet.this.currentPage) + StarGiftSheet.this.topView.getRealHeight();
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            StarGiftSheet starGiftSheet = StarGiftSheet.this;
            starGiftSheet.onSwitchedPage(starGiftSheet.currentPage);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (childAt == StarGiftSheet.this.button) {
                    StarGiftSheet.this.button.measure(View.MeasureSpec.makeMeasureSpec((size - (((BottomSheet) StarGiftSheet.this).backgroundPaddingLeft * 2)) - (AndroidUtilities.dp(14.0f) * 2), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
                } else if (childAt instanceof HintView2) {
                    childAt.measure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), 1073741824));
                } else {
                    childAt.measure(i, i2);
                }
            }
            setMeasuredDimension(size, Math.max(StarGiftSheet.this.infoLayout.getMeasuredHeight(), StarGiftSheet.this.upgradeLayout.getMeasuredHeight()) + StarGiftSheet.this.topView.getMeasuredHeight());
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            super.setTranslationY(f);
            FrameLayout frameLayout = StarGiftSheet.this.topBulletinContainer;
            if (frameLayout != null) {
                frameLayout.setTranslationY((getTranslationY() - height()) - AndroidUtilities.navigationBarHeight);
            }
        }

        public float top() {
            return Math.max(0.0f, getHeight() - height());
        }

        public void updateTopViewTranslation() {
            StarGiftSheet.this.topView.setTranslationY((StarGiftSheet.this.topView.getMeasuredHeight() - StarGiftSheet.this.topView.getRealHeight()) - AndroidUtilities.lerp(StarGiftSheet.this.infoLayout.getMeasuredHeight(), StarGiftSheet.this.upgradeLayout.getMeasuredHeight(), StarGiftSheet.this.currentPage));
            FrameLayout frameLayout = StarGiftSheet.this.topBulletinContainer;
            if (frameLayout != null) {
                frameLayout.setTranslationY((getTranslationY() - height()) - AndroidUtilities.navigationBarHeight);
            }
        }
    }

    public static class GiftTransferTopView extends View {
        private final Paint arrowPaint;
        private final Path arrowPath;
        private final AvatarDrawable avatarDrawable;
        private final StarGiftDrawableIcon giftDrawable;
        private final ImageReceiver userImageReceiver;

        public GiftTransferTopView(Context context, TL_stars.StarGift starGift, TLRPC.User user) {
            super(context);
            Path path = new Path();
            this.arrowPath = path;
            Paint paint = new Paint(1);
            this.arrowPaint = paint;
            this.giftDrawable = new StarGiftDrawableIcon(this, starGift, 60, 0.3f);
            AvatarDrawable avatarDrawable = new AvatarDrawable();
            this.avatarDrawable = avatarDrawable;
            avatarDrawable.setInfo(user);
            ImageReceiver imageReceiver = new ImageReceiver(this);
            this.userImageReceiver = imageReceiver;
            imageReceiver.setRoundRadius(AndroidUtilities.dp(30.0f));
            imageReceiver.setForUserOrChat(user, avatarDrawable);
            paint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText7));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            path.rewind();
            path.moveTo(0.0f, -AndroidUtilities.dp(8.0f));
            path.lineTo(AndroidUtilities.dp(6.166f), 0.0f);
            path.lineTo(0.0f, AndroidUtilities.dp(8.0f));
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.userImageReceiver.onAttachedToWindow();
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.userImageReceiver.onDetachedFromWindow();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int width = (getWidth() / 2) - (AndroidUtilities.dp(156.0f) / 2);
            int height = (getHeight() / 2) - AndroidUtilities.dp(30.0f);
            this.giftDrawable.setBounds(width, height, AndroidUtilities.dp(60.0f) + width, AndroidUtilities.dp(60.0f) + height);
            this.giftDrawable.draw(canvas);
            canvas.save();
            canvas.translate((getWidth() / 2.0f) - (AndroidUtilities.dp(6.166f) / 2.0f), getHeight() / 2.0f);
            canvas.drawPath(this.arrowPath, this.arrowPaint);
            canvas.restore();
            this.userImageReceiver.setImageCoords(width + AndroidUtilities.dp(96.0f), height, AndroidUtilities.dp(60.0f), AndroidUtilities.dp(60.0f));
            this.userImageReceiver.draw(canvas);
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), 1073741824));
        }
    }

    public static class StarGiftDrawableIcon extends CompatDrawable {
        private RadialGradient gradient;
        private final ImageReceiver imageReceiver;
        private final Matrix matrix;
        private final Path path;
        private final AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable pattern;
        private float patternsScale;
        private int patternsType;
        private final RectF rect;
        private int rounding;
        private final int sizeDp;

        public StarGiftDrawableIcon(View view, TL_stars.StarGift starGift, int i, float f) {
            super(view);
            this.path = new Path();
            this.rect = new RectF();
            this.matrix = new Matrix();
            this.rounding = AndroidUtilities.dp(16.0f);
            this.patternsType = 0;
            this.patternsScale = f;
            ImageReceiver imageReceiver = new ImageReceiver(view);
            this.imageReceiver = imageReceiver;
            AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(view, false, AndroidUtilities.dp(i > 180 ? 24.0f : 18.0f));
            this.pattern = swapAnimatedEmojiDrawable;
            this.sizeDp = i;
            if (starGift != null) {
                TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop = (TL_stars.starGiftAttributeBackdrop) StarsController.findAttribute(starGift.attributes, TL_stars.starGiftAttributeBackdrop.class);
                TL_stars.starGiftAttributePattern stargiftattributepattern = (TL_stars.starGiftAttributePattern) StarsController.findAttribute(starGift.attributes, TL_stars.starGiftAttributePattern.class);
                TL_stars.starGiftAttributeModel stargiftattributemodel = (TL_stars.starGiftAttributeModel) StarsController.findAttribute(starGift.attributes, TL_stars.starGiftAttributeModel.class);
                if (stargiftattributebackdrop != null) {
                    this.gradient = new RadialGradient(0.0f, 0.0f, AndroidUtilities.dpf2(i) / 2.0f, new int[]{stargiftattributebackdrop.center_color | (-16777216), stargiftattributebackdrop.edge_color | (-16777216)}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                    swapAnimatedEmojiDrawable.setColor(Integer.valueOf(stargiftattributebackdrop.pattern_color | (-16777216)));
                }
                if (stargiftattributepattern != null) {
                    swapAnimatedEmojiDrawable.set(stargiftattributepattern.document, false);
                }
                if (stargiftattributemodel != null) {
                    StarsIntroActivity.setGiftImage(imageReceiver, stargiftattributemodel.document, (int) (i * 0.75f));
                }
            }
            this.paint.setShader(this.gradient);
            if (view.isAttachedToWindow()) {
                onAttachedToWindow();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            this.rect.set(getBounds());
            canvas.save();
            this.path.rewind();
            Path path = this.path;
            RectF rectF = this.rect;
            float f = this.rounding;
            path.addRoundRect(rectF, f, f, Path.Direction.CW);
            canvas.clipPath(this.path);
            if (this.gradient != null) {
                this.matrix.reset();
                this.matrix.postTranslate(this.rect.centerX(), this.rect.centerY());
                this.gradient.setLocalMatrix(this.matrix);
                this.paint.setShader(this.gradient);
            }
            canvas.drawPaint(this.paint);
            canvas.save();
            canvas.translate(this.rect.centerX(), this.rect.centerY());
            StarGiftPatterns.drawPatterns(canvas, this.patternsType, this.pattern, this.rect.width(), this.rect.height(), 1.0f, this.patternsScale);
            canvas.restore();
            float min = Math.min(this.rect.width(), this.rect.height()) * 0.75f;
            float f2 = min / 2.0f;
            this.imageReceiver.setImageCoords(this.rect.centerX() - f2, this.rect.centerY() - f2, min, min);
            this.imageReceiver.draw(canvas);
            canvas.restore();
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(this.sizeDp);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(this.sizeDp);
        }

        @Override // org.telegram.ui.Components.CompatDrawable
        public void onAttachedToWindow() {
            this.pattern.attach();
            this.imageReceiver.onAttachedToWindow();
        }

        @Override // org.telegram.ui.Components.CompatDrawable
        public void onDetachedToWindow() {
            this.pattern.detach();
            this.imageReceiver.onDetachedFromWindow();
        }

        public StarGiftDrawableIcon setPatternsType(int i) {
            this.patternsType = i;
            return this;
        }

        public StarGiftDrawableIcon setRounding(int i) {
            this.rounding = i;
            return this;
        }
    }

    public static class TopView extends FrameLayout {
        private boolean attached;
        private final TL_stars.starGiftAttributeBackdrop[] backdrop;
        private BagRandomizer backdrops;
        private final RadialGradient[] backgroundGradient;
        private final Matrix[] backgroundMatrix;
        private final Paint[] backgroundPaint;
        private final Runnable checkToRotateRunnable;
        private final ImageView closeView;
        private int currentImageIndex;
        private float currentPage;
        private boolean hasLink;
        private final FrameLayout imageLayout;
        private final BackupImageView[] imageView;
        private final LinearLayout[] layout;
        private final FrameLayout.LayoutParams[] layoutLayoutParams;
        private BagRandomizer models;
        private final ImageView optionsView;
        private final AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[] pattern;
        private BagRandomizer patterns;
        private LinkSpanDrawable.LinksTextView priceView;
        private final Theme.ResourcesProvider resourcesProvider;
        private ValueAnimator rotationAnimator;
        private ArrayList sampleAttributes;
        private final ImageView shareView;
        private final LinkSpanDrawable.LinksTextView[] subtitleView;
        private final LinearLayout.LayoutParams[] subtitleViewLayoutParams;
        private ValueAnimator switchAnimator;
        private float switchScale;
        private final LinkSpanDrawable.LinksTextView[] titleView;
        private float toggleBackdrop;
        private int toggled;

        public TopView(Context context, Theme.ResourcesProvider resourcesProvider, final Runnable runnable, View.OnClickListener onClickListener, View.OnClickListener onClickListener2) {
            super(context);
            this.imageView = new BackupImageView[3];
            this.currentImageIndex = 0;
            this.layout = new LinearLayout[2];
            this.layoutLayoutParams = new FrameLayout.LayoutParams[2];
            this.titleView = new LinkSpanDrawable.LinksTextView[2];
            this.subtitleView = new LinkSpanDrawable.LinksTextView[2];
            this.subtitleViewLayoutParams = new LinearLayout.LayoutParams[2];
            this.backdrop = new TL_stars.starGiftAttributeBackdrop[3];
            this.checkToRotateRunnable = new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$TopView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StarGiftSheet.TopView.this.lambda$new$1();
                }
            };
            this.backgroundPaint = new Paint[3];
            this.backgroundGradient = new RadialGradient[3];
            this.backgroundMatrix = new Matrix[3];
            this.pattern = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[2];
            int i = 0;
            while (true) {
                Paint[] paintArr = this.backgroundPaint;
                if (i >= paintArr.length) {
                    break;
                }
                paintArr[i] = new Paint(1);
                i++;
            }
            int i2 = 0;
            while (true) {
                AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable[] swapAnimatedEmojiDrawableArr = this.pattern;
                if (i2 >= swapAnimatedEmojiDrawableArr.length) {
                    break;
                }
                swapAnimatedEmojiDrawableArr[i2] = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(28.0f));
                i2++;
            }
            this.switchScale = 1.0f;
            this.resourcesProvider = resourcesProvider;
            setWillNotDraw(false);
            FrameLayout frameLayout = new FrameLayout(context);
            this.imageLayout = frameLayout;
            addView(frameLayout, LayoutHelper.createFrame(NotificationCenter.audioRouteChanged, 160.0f, 49, 0.0f, 8.0f, 0.0f, 0.0f));
            int i3 = 0;
            while (i3 < 3) {
                this.imageView[i3] = new BackupImageView(context);
                this.imageView[i3].setLayerNum(6660);
                if (i3 > 0) {
                    this.imageView[i3].getImageReceiver().setCrossfadeDuration(1);
                }
                this.imageLayout.addView(this.imageView[i3], LayoutHelper.createFrame(-1, -1, 119));
                this.imageView[i3].setAlpha(i3 == this.currentImageIndex ? 1.0f : 0.0f);
                i3++;
            }
            int i4 = 0;
            while (i4 < 2) {
                this.layout[i4] = new LinearLayout(context);
                this.layout[i4].setOrientation(1);
                View view = this.layout[i4];
                FrameLayout.LayoutParams[] layoutParamsArr = this.layoutLayoutParams;
                ViewGroup.LayoutParams createFrame = LayoutHelper.createFrame(-1, -2.0f, 119, 20.0f, 170.0f, 20.0f, 0.0f);
                layoutParamsArr[i4] = createFrame;
                addView(view, createFrame);
                this.titleView[i4] = new LinkSpanDrawable.LinksTextView(context);
                LinkSpanDrawable.LinksTextView linksTextView = this.titleView[i4];
                int i5 = Theme.key_dialogTextBlack;
                linksTextView.setTextColor(Theme.getColor(i5, resourcesProvider));
                this.titleView[i4].setTextSize(1, 20.0f);
                this.titleView[i4].setTypeface(AndroidUtilities.bold());
                this.titleView[i4].setGravity(17);
                this.layout[i4].addView(this.titleView[i4], LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 0));
                if (i4 == 0) {
                    LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context);
                    this.priceView = linksTextView2;
                    linksTextView2.setTextSize(1, 18.0f);
                    this.priceView.setTypeface(AndroidUtilities.bold());
                    this.priceView.setGravity(17);
                    this.priceView.setTextColor(Theme.getColor(Theme.key_color_green, resourcesProvider));
                    this.layout[i4].addView(this.priceView, LayoutHelper.createLinear(-1, -2, 17, 0, 0, 0, 4));
                }
                this.subtitleView[i4] = new LinkSpanDrawable.LinksTextView(context);
                this.subtitleView[i4].setTextColor(Theme.getColor(i5, resourcesProvider));
                this.subtitleView[i4].setTextSize(1, 14.0f);
                this.subtitleView[i4].setGravity(17);
                this.subtitleView[i4].setLinkTextColor(Theme.getColor(Theme.key_chat_messageLinkIn, resourcesProvider));
                this.subtitleView[i4].setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                this.subtitleView[i4].setDisablePaddingsOffsetY(true);
                LinearLayout linearLayout = this.layout[i4];
                LinkSpanDrawable.LinksTextView linksTextView3 = this.subtitleView[i4];
                LinearLayout.LayoutParams[] layoutParamsArr2 = this.subtitleViewLayoutParams;
                LinearLayout.LayoutParams createLinear = LayoutHelper.createLinear(-1, -2, 17, 20, 0, 20, 0);
                layoutParamsArr2[i4] = createLinear;
                linearLayout.addView(linksTextView3, createLinear);
                this.subtitleViewLayoutParams[i4].topMargin = AndroidUtilities.dp(i4 == 1 ? 7.33f : this.backdrop[0] == null ? 9.0f : 5.66f);
                i4++;
            }
            ImageView imageView = new ImageView(context);
            this.closeView = imageView;
            imageView.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(28.0f), 620756991));
            imageView.setImageResource(R.drawable.msg_close);
            ScaleStateListAnimator.apply(imageView);
            addView(imageView, LayoutHelper.createFrame(28, 28.0f, 53, 0.0f, 12.0f, 12.0f, 0.0f));
            imageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$TopView$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    runnable.run();
                }
            });
            imageView.setVisibility(8);
            ImageView imageView2 = new ImageView(context);
            this.optionsView = imageView2;
            imageView2.setImageResource(R.drawable.media_more);
            ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
            imageView2.setScaleType(scaleType);
            imageView2.setBackground(Theme.createSelectorDrawable(553648127, 1));
            ScaleStateListAnimator.apply(imageView2);
            addView(imageView2, LayoutHelper.createFrame(42, 42.0f, 53, 0.0f, 5.0f, 5.0f, 0.0f));
            imageView2.setOnClickListener(onClickListener);
            imageView2.setVisibility(8);
            ImageView imageView3 = new ImageView(context);
            this.shareView = imageView3;
            imageView3.setImageResource(R.drawable.msg_header_share);
            imageView3.setScaleType(scaleType);
            imageView3.setBackground(Theme.createSelectorDrawable(553648127, 1));
            ScaleStateListAnimator.apply(imageView3);
            addView(imageView3, LayoutHelper.createFrame(42, 42.0f, 53, 0.0f, 5.0f, 47.0f, 0.0f));
            imageView3.setOnClickListener(onClickListener2);
            imageView3.setVisibility(8);
        }

        private void animateSwitch() {
            ValueAnimator valueAnimator = this.switchAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.switchAnimator = null;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.switchAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$TopView$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    StarGiftSheet.TopView.this.lambda$animateSwitch$3(valueAnimator2);
                }
            });
            this.switchAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stars.StarGiftSheet.TopView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    TopView.this.switchScale = 1.0f;
                    TopView.this.imageLayout.setScaleX(TopView.this.switchScale);
                    TopView.this.imageLayout.setScaleY(TopView.this.switchScale);
                    TopView.this.invalidate();
                }
            });
            this.switchAnimator.setDuration(320L);
            this.switchAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT);
            this.switchAnimator.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateSwitch$3(ValueAnimator valueAnimator) {
            float pow = (((float) Math.pow((r5 * 2.0f) - 2.0f, 2.0d)) * 0.075f * ((Float) valueAnimator.getAnimatedValue()).floatValue()) + 1.0f;
            this.switchScale = pow;
            this.imageLayout.setScaleX(pow);
            this.imageLayout.setScaleY(this.switchScale);
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1() {
            if (this.imageView[2 - this.toggled].getImageReceiver().hasImageLoaded()) {
                rotateAttributes();
            } else {
                AndroidUtilities.cancelRunOnUIThread(this.checkToRotateRunnable);
                AndroidUtilities.runOnUIThread(this.checkToRotateRunnable, 150L);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$rotateAttributes$2(ValueAnimator valueAnimator) {
            this.toggleBackdrop = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            onSwitchPage(this.currentPage);
        }

        private void rotateAttributes() {
            if (this.currentPage < 0.5f || !isAttachedToWindow()) {
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.checkToRotateRunnable);
            ValueAnimator valueAnimator = this.rotationAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.rotationAnimator = null;
            }
            int i = 1 - this.toggled;
            this.toggled = i;
            RLottieDrawable lottieAnimation = this.imageView[2 - i].getImageReceiver().getLottieAnimation();
            RLottieDrawable lottieAnimation2 = this.imageView[this.toggled + 1].getImageReceiver().getLottieAnimation();
            if (lottieAnimation2 != null && lottieAnimation != null) {
                lottieAnimation2.setProgress(lottieAnimation.getProgress(), false);
            }
            this.models.next();
            int i2 = this.toggled + 1;
            TL_stars.starGiftAttributeBackdrop[] stargiftattributebackdropArr = this.backdrop;
            TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop = (TL_stars.starGiftAttributeBackdrop) this.backdrops.next();
            stargiftattributebackdropArr[i2] = stargiftattributebackdrop;
            setBackdropPaint(i2, stargiftattributebackdrop);
            setPattern(1, (TL_stars.starGiftAttributePattern) this.patterns.next());
            animateSwitch();
            float f = this.toggled;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f - f, f);
            this.rotationAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$TopView$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    StarGiftSheet.TopView.this.lambda$rotateAttributes$2(valueAnimator2);
                }
            });
            this.rotationAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stars.StarGiftSheet.TopView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    TopView.this.toggleBackdrop = r3.toggled;
                    TopView topView = TopView.this;
                    topView.onSwitchPage(topView.currentPage);
                    StarsIntroActivity.setGiftImage(TopView.this.imageView[2 - TopView.this.toggled].getImageReceiver(), ((TL_stars.starGiftAttributeModel) TopView.this.models.getNext()).document, NotificationCenter.audioRouteChanged);
                    AndroidUtilities.cancelRunOnUIThread(TopView.this.checkToRotateRunnable);
                    AndroidUtilities.runOnUIThread(TopView.this.checkToRotateRunnable, 2500L);
                }
            });
            this.rotationAnimator.setDuration(320L);
            this.rotationAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.rotationAnimator.start();
        }

        private void setBackdropPaint(int i, TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop) {
            if (stargiftattributebackdrop == null) {
                return;
            }
            this.backgroundGradient[i] = new RadialGradient(0.0f, 0.0f, AndroidUtilities.dp(200.0f), new int[]{stargiftattributebackdrop.center_color | (-16777216), stargiftattributebackdrop.edge_color | (-16777216)}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
            this.backgroundMatrix[i] = new Matrix();
            this.backgroundPaint[i].setShader(this.backgroundGradient[i]);
        }

        private void setPattern(int i, TL_stars.starGiftAttributePattern stargiftattributepattern) {
            if (stargiftattributepattern == null) {
                return;
            }
            this.pattern[i].set(stargiftattributepattern.document, true);
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x01cb  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x01d5  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x01cd  */
        @Override // android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void dispatchDraw(Canvas canvas) {
            float width;
            Paint paint;
            float realHeight = getRealHeight();
            float width2 = getWidth() / 2.0f;
            float lerp = AndroidUtilities.lerp(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(24.0f), this.currentPage) + AndroidUtilities.dp(80.0f);
            float f = this.currentPage;
            if (f < 1.0f && this.backdrop[0] != null) {
                this.backgroundPaint[0].setAlpha((int) ((1.0f - f) * 255.0f));
                this.backgroundMatrix[0].reset();
                this.backgroundMatrix[0].postTranslate(width2, lerp);
                this.backgroundGradient[0].setLocalMatrix(this.backgroundMatrix[0]);
                canvas.drawRect(0.0f, 0.0f, getWidth(), realHeight, this.backgroundPaint[0]);
                canvas.save();
                canvas.translate(width2, lerp);
                this.pattern[0].setColor(Integer.valueOf(this.backdrop[0].pattern_color | (-16777216)));
                StarGiftPatterns.drawPatterns(canvas, this.pattern[0], getWidth(), getRealHeight(), 1.0f - this.currentPage, 1.0f);
                canvas.restore();
            }
            float f2 = this.currentPage;
            if (f2 > 0.0f) {
                if (this.toggled == 0) {
                    if (this.toggleBackdrop > 0.0f && this.backdrop[2] != null) {
                        this.backgroundPaint[2].setAlpha((int) (f2 * 255.0f));
                        this.backgroundMatrix[2].reset();
                        this.backgroundMatrix[2].postTranslate(width2, lerp);
                        this.backgroundGradient[2].setLocalMatrix(this.backgroundMatrix[2]);
                        canvas.drawRect(0.0f, 0.0f, getWidth(), realHeight, this.backgroundPaint[2]);
                    }
                    float f3 = this.toggleBackdrop;
                    if (f3 < 1.0f && this.backdrop[1] != null) {
                        this.backgroundPaint[1].setAlpha((int) (this.currentPage * 255.0f * (1.0f - f3)));
                        this.backgroundMatrix[1].reset();
                        this.backgroundMatrix[1].postTranslate(width2, lerp);
                        this.backgroundGradient[1].setLocalMatrix(this.backgroundMatrix[1]);
                        width = getWidth();
                        paint = this.backgroundPaint[1];
                        canvas.drawRect(0.0f, 0.0f, width, realHeight, paint);
                    }
                    canvas.save();
                    canvas.translate(width2, lerp);
                    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.pattern[1];
                    TL_stars.starGiftAttributeBackdrop[] stargiftattributebackdropArr = this.backdrop;
                    TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop = stargiftattributebackdropArr[1];
                    int i = stargiftattributebackdrop != null ? 0 : stargiftattributebackdrop.pattern_color | (-16777216);
                    TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop2 = stargiftattributebackdropArr[2];
                    swapAnimatedEmojiDrawable.setColor(Integer.valueOf(ColorUtils.blendARGB(i, stargiftattributebackdrop2 != null ? stargiftattributebackdrop2.pattern_color | (-16777216) : 0, this.toggleBackdrop)));
                    StarGiftPatterns.drawPatterns(canvas, this.pattern[1], getWidth(), getRealHeight(), this.currentPage, this.switchScale);
                    canvas.restore();
                } else {
                    if (this.toggleBackdrop < 1.0f && this.backdrop[1] != null) {
                        this.backgroundPaint[1].setAlpha((int) (f2 * 255.0f));
                        this.backgroundMatrix[1].reset();
                        this.backgroundMatrix[1].postTranslate(width2, lerp);
                        this.backgroundGradient[1].setLocalMatrix(this.backgroundMatrix[1]);
                        canvas.drawRect(0.0f, 0.0f, getWidth(), realHeight, this.backgroundPaint[1]);
                    }
                    float f4 = this.toggleBackdrop;
                    if (f4 > 0.0f && this.backdrop[2] != null) {
                        this.backgroundPaint[2].setAlpha((int) (this.currentPage * 255.0f * f4));
                        this.backgroundMatrix[2].reset();
                        this.backgroundMatrix[2].postTranslate(width2, lerp);
                        this.backgroundGradient[2].setLocalMatrix(this.backgroundMatrix[2]);
                        width = getWidth();
                        paint = this.backgroundPaint[2];
                        canvas.drawRect(0.0f, 0.0f, width, realHeight, paint);
                    }
                    canvas.save();
                    canvas.translate(width2, lerp);
                    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.pattern[1];
                    TL_stars.starGiftAttributeBackdrop[] stargiftattributebackdropArr2 = this.backdrop;
                    TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop3 = stargiftattributebackdropArr2[1];
                    if (stargiftattributebackdrop3 != null) {
                    }
                    TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop22 = stargiftattributebackdropArr2[2];
                    swapAnimatedEmojiDrawable2.setColor(Integer.valueOf(ColorUtils.blendARGB(i, stargiftattributebackdrop22 != null ? stargiftattributebackdrop22.pattern_color | (-16777216) : 0, this.toggleBackdrop)));
                    StarGiftPatterns.drawPatterns(canvas, this.pattern[1], getWidth(), getRealHeight(), this.currentPage, this.switchScale);
                    canvas.restore();
                }
            }
            super.dispatchDraw(canvas);
        }

        public float getRealHeight() {
            return AndroidUtilities.lerp(AndroidUtilities.dp(this.backdrop[0] != null ? 24.0f : 10.0f), AndroidUtilities.dp(this.backdrop[1] != null ? 24.0f : 10.0f), this.currentPage) + AndroidUtilities.dp(160.0f) + AndroidUtilities.lerp(this.layout[0].getMeasuredHeight(), this.layout[1].getMeasuredHeight(), this.currentPage);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            this.attached = true;
            super.onAttachedToWindow();
            this.pattern[0].attach();
            this.pattern[1].attach();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            this.attached = false;
            super.onDetachedFromWindow();
            this.pattern[0].detach();
            this.pattern[1].detach();
            AndroidUtilities.cancelRunOnUIThread(this.checkToRotateRunnable);
        }

        /* JADX WARN: Removed duplicated region for block: B:48:0x0136  */
        /* JADX WARN: Removed duplicated region for block: B:51:0x014e  */
        /* JADX WARN: Removed duplicated region for block: B:54:0x0164 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:55:0x013a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onSwitchPage(float f) {
            float f2;
            boolean z;
            FrameLayout.LayoutParams layoutParams;
            this.currentPage = f;
            float f3 = 1.0f - f;
            this.layout[0].setAlpha(f3);
            this.layout[1].setAlpha(f);
            this.closeView.setAlpha(AndroidUtilities.lerp(false, this.backdrop[1] != null, f));
            int i = 8;
            this.closeView.setVisibility((this.backdrop[1] == null || f <= 0.0f) ? 8 : 0);
            this.optionsView.setAlpha(AndroidUtilities.lerp(this.backdrop[0] != null, false, f));
            this.optionsView.setVisibility((this.backdrop[0] == null || f3 <= 0.0f) ? 8 : 0);
            this.shareView.setAlpha(AndroidUtilities.lerp(this.backdrop[0] != null, false, f));
            ImageView imageView = this.shareView;
            if (this.hasLink && this.backdrop[0] != null && f3 > 0.0f) {
                i = 0;
            }
            imageView.setVisibility(i);
            int i2 = 0;
            while (i2 < 2) {
                int color = Theme.getColor(Theme.key_dialogTextBlack, this.resourcesProvider);
                this.titleView[i2].setTextColor(this.backdrop[Math.min(1, i2)] == null ? color : -1);
                LinkSpanDrawable.LinksTextView linksTextView = this.subtitleView[i2];
                if (i2 == 0) {
                    TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop = this.backdrop[i2];
                    if (stargiftattributebackdrop != null) {
                        color = stargiftattributebackdrop.text_color | (-16777216);
                    }
                } else {
                    TL_stars.starGiftAttributeBackdrop[] stargiftattributebackdropArr = this.backdrop;
                    TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop2 = stargiftattributebackdropArr[1];
                    int i3 = stargiftattributebackdrop2 == null ? color : stargiftattributebackdrop2.text_color | (-16777216);
                    TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop3 = stargiftattributebackdropArr[2];
                    if (stargiftattributebackdrop3 != null) {
                        color = stargiftattributebackdrop3.text_color | (-16777216);
                    }
                    color = ColorUtils.blendARGB(i3, color, this.toggleBackdrop);
                }
                linksTextView.setTextColor(color);
                if (this.backdrop[i2] != null) {
                    f2 = 184.0f;
                    z = AndroidUtilities.dp(184.0f) != this.layoutLayoutParams[i2].topMargin;
                    if (z) {
                        this.layout[i2].setPadding(0, 0, 0, AndroidUtilities.dp(18.0f));
                        layoutParams = this.layoutLayoutParams[i2];
                        layoutParams.topMargin = AndroidUtilities.dp(f2);
                    }
                    this.subtitleViewLayoutParams[i2].topMargin = AndroidUtilities.dp(i2 != 1 ? 7.33f : this.backdrop[0] == null ? 9.0f : 5.66f);
                    if (!z) {
                        this.layout[i2].setLayoutParams(this.layoutLayoutParams[i2]);
                        this.subtitleView[i2].setLayoutParams(this.subtitleViewLayoutParams[i2]);
                    }
                    i2++;
                } else {
                    f2 = 170.0f;
                    z = AndroidUtilities.dp(170.0f) != this.layoutLayoutParams[i2].topMargin;
                    if (z) {
                        this.layout[i2].setPadding(0, 0, 0, AndroidUtilities.dp(3.0f));
                        layoutParams = this.layoutLayoutParams[i2];
                        layoutParams.topMargin = AndroidUtilities.dp(f2);
                    }
                    this.subtitleViewLayoutParams[i2].topMargin = AndroidUtilities.dp(i2 != 1 ? 7.33f : this.backdrop[0] == null ? 9.0f : 5.66f);
                    if (!z) {
                    }
                    i2++;
                }
            }
            this.imageView[0].setAlpha(f3);
            this.imageView[1].setAlpha((1.0f - this.toggleBackdrop) * f);
            this.imageView[2].setAlpha(this.toggleBackdrop * f);
            this.imageLayout.setTranslationY(AndroidUtilities.dp(16.0f) * f);
            invalidate();
        }

        public void prepareSwitchPage(int i, int i2) {
            if (i != i2) {
                RLottieDrawable lottieAnimation = this.imageView[i].getImageReceiver().getLottieAnimation();
                RLottieDrawable lottieAnimation2 = this.imageView[i2].getImageReceiver().getLottieAnimation();
                if (lottieAnimation2 == null || lottieAnimation == null) {
                    return;
                }
                lottieAnimation2.setProgress(lottieAnimation.getProgress(), false);
            }
        }

        public void setGift(TL_stars.StarGift starGift, boolean z) {
            LinkSpanDrawable.LinksTextView linksTextView;
            float f;
            if (starGift instanceof TL_stars.TL_starGiftUnique) {
                this.backdrop[0] = (TL_stars.starGiftAttributeBackdrop) StarsController.findAttribute(starGift.attributes, TL_stars.starGiftAttributeBackdrop.class);
                setPattern(0, (TL_stars.starGiftAttributePattern) StarsController.findAttribute(starGift.attributes, TL_stars.starGiftAttributePattern.class));
                linksTextView = this.subtitleView[0];
                f = 13.0f;
            } else {
                this.backdrop[0] = null;
                setPattern(0, null);
                linksTextView = this.subtitleView[0];
                f = 14.0f;
            }
            linksTextView.setTextSize(1, f);
            this.hasLink = z;
            setBackdropPaint(0, this.backdrop[0]);
            StarsIntroActivity.setGiftImage(this.imageView[0].getImageReceiver(), starGift, NotificationCenter.audioRouteChanged);
            onSwitchPage(this.currentPage);
        }

        public void setPreviewingAttributes(ArrayList<TL_stars.StarGiftAttribute> arrayList) {
            this.sampleAttributes = arrayList;
            this.models = new BagRandomizer(StarsController.findAttributes(arrayList, TL_stars.starGiftAttributeModel.class));
            this.patterns = new BagRandomizer(StarsController.findAttributes(arrayList, TL_stars.starGiftAttributePattern.class));
            this.backdrops = new BagRandomizer(StarsController.findAttributes(arrayList, TL_stars.starGiftAttributeBackdrop.class));
            this.subtitleView[1].setTextSize(1, 14.0f);
            this.toggleBackdrop = 0.0f;
            this.toggled = 0;
            setPattern(1, (TL_stars.starGiftAttributePattern) this.patterns.next());
            StarsIntroActivity.setGiftImage(this.imageView[1].getImageReceiver(), ((TL_stars.starGiftAttributeModel) this.models.next()).document, NotificationCenter.audioRouteChanged);
            TL_stars.starGiftAttributeBackdrop[] stargiftattributebackdropArr = this.backdrop;
            TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop = (TL_stars.starGiftAttributeBackdrop) this.backdrops.next();
            stargiftattributebackdropArr[1] = stargiftattributebackdrop;
            setBackdropPaint(1, stargiftattributebackdrop);
            StarsIntroActivity.setGiftImage(this.imageView[2].getImageReceiver(), ((TL_stars.starGiftAttributeModel) this.models.getNext()).document, NotificationCenter.audioRouteChanged);
            AndroidUtilities.cancelRunOnUIThread(this.checkToRotateRunnable);
            AndroidUtilities.runOnUIThread(this.checkToRotateRunnable, 2500L);
            invalidate();
        }

        public void setText(int i, CharSequence charSequence, long j, CharSequence charSequence2) {
            this.titleView[i].setText(charSequence);
            if (i == 0) {
                this.priceView.setTextColor(Theme.getColor(Theme.key_color_green, this.resourcesProvider));
                this.priceView.setText(StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatNumber((int) j, ' ') + " ⭐️", 0.8f));
                this.priceView.setVisibility(j != 0 ? 0 : 8);
            }
            this.subtitleView[i].setText(charSequence2);
            this.subtitleView[i].setVisibility(TextUtils.isEmpty(charSequence2) ? 8 : 0);
        }
    }

    public static class UpgradeIcon extends CompatDrawable {
        private final Path arrow;
        private final long start;
        private final Paint strokePaint;
        private final View view;

        public UpgradeIcon(View view, int i) {
            super(view);
            Paint paint = new Paint(1);
            this.strokePaint = paint;
            Path path = new Path();
            this.arrow = path;
            this.start = System.currentTimeMillis();
            this.view = view;
            if (view.isAttachedToWindow()) {
                onAttachedToWindow();
            }
            this.paint.setColor(-1);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setColor(i);
            path.rewind();
            path.moveTo(-AndroidUtilities.dpf2(2.91f), AndroidUtilities.dpf2(1.08f));
            path.lineTo(0.0f, -AndroidUtilities.dpf2(1.08f));
            path.lineTo(AndroidUtilities.dpf2(2.91f), AndroidUtilities.dpf2(1.08f));
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            canvas.drawCircle(getBounds().centerX(), getBounds().centerY(), getBounds().width() / 2.0f, this.paint);
            float currentTimeMillis = ((System.currentTimeMillis() - this.start) % 400) / 400.0f;
            this.strokePaint.setStrokeWidth(AndroidUtilities.dpf2(1.33f));
            canvas.save();
            canvas.translate(getBounds().centerX(), getBounds().centerY() - (((AndroidUtilities.dpf2(2.16f) * 3.0f) + (AndroidUtilities.dpf2(1.166f) * 2.0f)) / 2.0f));
            int i = 0;
            while (i < 4) {
                float f = i == 0 ? 1.0f - currentTimeMillis : i == 3 ? currentTimeMillis : 1.0f;
                this.strokePaint.setAlpha((int) (255.0f * f));
                canvas.save();
                float lerp = AndroidUtilities.lerp(0.5f, 1.0f, f);
                canvas.scale(lerp, lerp);
                canvas.drawPath(this.arrow, this.strokePaint);
                canvas.restore();
                canvas.translate(0.0f, AndroidUtilities.dpf2(3.3260002f) * f);
                i++;
            }
            canvas.restore();
            View view = this.view;
            if (view != null) {
                view.invalidate();
            }
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return AndroidUtilities.dp(18.0f);
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return AndroidUtilities.dp(18.0f);
        }
    }

    public StarGiftSheet(Context context, int i, long j, Theme.ResourcesProvider resourcesProvider) {
        super(context, false, resourcesProvider);
        this.firstSet = true;
        this.currentAccount = i;
        this.dialogId = j;
        ContainerView containerView = new ContainerView(context);
        this.container = containerView;
        this.containerView = containerView;
        fixNavigationBar(getThemedColor(Theme.key_dialogBackground));
        LinearLayout linearLayout = new LinearLayout(context);
        this.infoLayout = linearLayout;
        linearLayout.setOrientation(1);
        linearLayout.setPadding(this.backgroundPaddingLeft + AndroidUtilities.dp(14.0f), AndroidUtilities.dp(16.0f), this.backgroundPaddingLeft + AndroidUtilities.dp(14.0f), AndroidUtilities.dp(56.0f));
        this.containerView.addView(linearLayout, LayoutHelper.createFrame(-1, -1, 87));
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        this.beforeTableTextView = linksTextView;
        int i2 = Theme.key_dialogTextGray2;
        linksTextView.setTextColor(Theme.getColor(i2, resourcesProvider));
        linksTextView.setTextSize(1, 14.0f);
        linksTextView.setGravity(17);
        linksTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        int i3 = Theme.key_chat_messageLinkIn;
        linksTextView.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
        linksTextView.setDisablePaddingsOffsetY(true);
        linearLayout.addView(linksTextView, LayoutHelper.createLinear(-2, -2, 1, 5, -4, 5, 10));
        linksTextView.setVisibility(8);
        TableView tableView = new TableView(context, resourcesProvider);
        this.tableView = tableView;
        linearLayout.addView(tableView, LayoutHelper.createLinear(-1, -2, 0.0f, 0.0f, 0.0f, 12.0f));
        LinkSpanDrawable.LinksTextView linksTextView2 = new LinkSpanDrawable.LinksTextView(context, resourcesProvider);
        this.afterTableTextView = linksTextView2;
        linksTextView2.setTextColor(Theme.getColor(i2, resourcesProvider));
        linksTextView2.setTextSize(1, 14.0f);
        linksTextView2.setGravity(17);
        linksTextView2.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        linksTextView2.setLinkTextColor(Theme.getColor(i3, resourcesProvider));
        linksTextView2.setDisablePaddingsOffsetY(true);
        linearLayout.addView(linksTextView2, LayoutHelper.createLinear(-2, -2, 1, 5, 6, 5, 16));
        linksTextView2.setVisibility(8);
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        this.button = buttonWithCounterView;
        buttonWithCounterView.setText(LocaleController.getString(R.string.OK), false);
        FrameLayout.LayoutParams createFrame = LayoutHelper.createFrame(-1, 48.0f, 87, 0.0f, 0.0f, 0.0f, 4.0f);
        createFrame.leftMargin = this.backgroundPaddingLeft + AndroidUtilities.dp(14.0f);
        createFrame.rightMargin = this.backgroundPaddingLeft + AndroidUtilities.dp(14.0f);
        this.containerView.addView(buttonWithCounterView, createFrame);
        LinearLayout linearLayout2 = new LinearLayout(context);
        this.upgradeLayout = linearLayout2;
        linearLayout2.setOrientation(1);
        linearLayout2.setPadding(AndroidUtilities.dp(4.0f) + this.backgroundPaddingLeft, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(4.0f) + this.backgroundPaddingLeft, AndroidUtilities.dp(56.0f));
        this.containerView.addView(linearLayout2, LayoutHelper.createFrame(-1, -1, 87));
        AffiliateProgramFragment.FeatureCell[] featureCellArr = {r9, r9, new AffiliateProgramFragment.FeatureCell(context, resourcesProvider)};
        this.featureCells = featureCellArr;
        AffiliateProgramFragment.FeatureCell featureCell = new AffiliateProgramFragment.FeatureCell(context, resourcesProvider);
        featureCell.set(R.drawable.menu_feature_unique, LocaleController.getString(R.string.Gift2UpgradeFeature1Title), LocaleController.getString(R.string.Gift2UpgradeFeature1Text));
        linearLayout2.addView(featureCellArr[0], LayoutHelper.createLinear(-1, -2));
        AffiliateProgramFragment.FeatureCell featureCell2 = new AffiliateProgramFragment.FeatureCell(context, resourcesProvider);
        featureCell2.set(R.drawable.menu_feature_transfer, LocaleController.getString(R.string.Gift2UpgradeFeature2Title), LocaleController.getString(R.string.Gift2UpgradeFeature2Text));
        linearLayout2.addView(featureCellArr[1], LayoutHelper.createLinear(-1, -2));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.Gift2UpgradeFeature3Title));
        spannableStringBuilder.append((CharSequence) "  d");
        FilterCreateActivity.NewSpan newSpan = new FilterCreateActivity.NewSpan(10.0f);
        newSpan.setColor(getThemedColor(Theme.key_featuredStickers_addButton));
        newSpan.setText(LocaleController.getString(R.string.Gift2UpgradeFeatureSoon));
        spannableStringBuilder.setSpan(newSpan, spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
        featureCellArr[2].set(R.drawable.menu_feature_tradable, spannableStringBuilder, LocaleController.getString(R.string.Gift2UpgradeFeature3Text));
        linearLayout2.addView(featureCellArr[2], LayoutHelper.createLinear(-1, -2));
        View view = new View(context);
        this.checkboxSeparator = view;
        view.setBackgroundColor(Theme.getColor(Theme.key_divider, resourcesProvider));
        linearLayout2.addView(view, LayoutHelper.createLinear(-2, 1.0f / AndroidUtilities.density, 7, 17, -4, 17, 6));
        LinearLayout linearLayout3 = new LinearLayout(context);
        this.checkboxLayout = linearLayout3;
        linearLayout3.setPadding(AndroidUtilities.dp(12.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(8.0f));
        linearLayout3.setOrientation(0);
        linearLayout3.setBackground(Theme.createRadSelectorDrawable(Theme.getColor(Theme.key_listSelector, resourcesProvider), 6, 6));
        CheckBox2 checkBox2 = new CheckBox2(context, 24, resourcesProvider);
        this.checkbox = checkBox2;
        checkBox2.setColor(Theme.key_radioBackgroundChecked, Theme.key_checkboxDisabled, Theme.key_checkboxCheck);
        checkBox2.setDrawUnchecked(true);
        checkBox2.setChecked(false, false);
        checkBox2.setDrawBackgroundAsArc(10);
        linearLayout3.addView(checkBox2, LayoutHelper.createLinear(24, 24, 16, 0, 0, 0, 0));
        TextView textView = new TextView(context);
        this.checkboxTextView = textView;
        textView.setTextColor(getThemedColor(Theme.key_dialogTextBlack));
        textView.setTextSize(1, 14.0f);
        textView.setText(LocaleController.getString(R.string.Gift2AddSenderName));
        linearLayout3.addView(textView, LayoutHelper.createLinear(-2, -2, 16, 9, 0, 0, 0));
        linearLayout2.addView(linearLayout3, LayoutHelper.createLinear(-2, -2, 1, 0, 0, 0, 4));
        ScaleStateListAnimator.apply(linearLayout3, 0.025f, 1.5f);
        linearLayout3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda26
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                StarGiftSheet.this.lambda$new$0(view2);
            }
        });
        linearLayout.setAlpha(1.0f);
        linearLayout2.setAlpha(0.0f);
        TopView topView = new TopView(context, resourcesProvider, new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda27
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.onBackPressed();
            }
        }, new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda28
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                StarGiftSheet.this.onMenuPressed(view2);
            }
        }, new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda29
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                StarGiftSheet.this.onSharePressed(view2);
            }
        });
        this.topView = topView;
        int i4 = this.backgroundPaddingLeft;
        topView.setPadding(i4, 0, i4, 0);
        this.containerView.addView(topView, LayoutHelper.createFrame(-1, -2, 87));
        FireworksOverlay fireworksOverlay = new FireworksOverlay(context);
        this.fireworksOverlay = fireworksOverlay;
        this.container.addView(fireworksOverlay, LayoutHelper.createFrame(-1, -1.0f));
    }

    private void addAttributeRow(final TL_stars.StarGiftAttribute starGiftAttribute) {
        int i;
        if (starGiftAttribute instanceof TL_stars.starGiftAttributeModel) {
            i = R.string.Gift2AttributeModel;
        } else if (starGiftAttribute instanceof TL_stars.starGiftAttributeBackdrop) {
            i = R.string.Gift2AttributeBackdrop;
        } else if (!(starGiftAttribute instanceof TL_stars.starGiftAttributePattern)) {
            return;
        } else {
            i = R.string.Gift2AttributeSymbol;
        }
        final ButtonSpan.TextViewButtons[] textViewButtonsArr = new ButtonSpan.TextViewButtons[1];
        textViewButtonsArr[0] = (ButtonSpan.TextViewButtons) ((TableView.TableRowContent) this.tableView.addRow(LocaleController.getString(i), starGiftAttribute.name, AffiliateProgramFragment.percents(starGiftAttribute.rarity_permille), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$addAttributeRow$8(starGiftAttribute, textViewButtonsArr);
            }
        }).getChildAt(1)).getChildAt(0);
    }

    private boolean applyNewGiftFromUpdates(TLRPC.Updates updates) {
        TLRPC.TL_updateNewMessage tL_updateNewMessage;
        if (updates == null) {
            return false;
        }
        TLRPC.Update update = updates.update;
        if (update instanceof TLRPC.TL_updateNewMessage) {
            tL_updateNewMessage = (TLRPC.TL_updateNewMessage) update;
        } else {
            if (updates.updates != null) {
                for (int i = 0; i < updates.updates.size(); i++) {
                    TLRPC.Update update2 = updates.updates.get(i);
                    if (update2 instanceof TLRPC.TL_updateNewMessage) {
                        tL_updateNewMessage = (TLRPC.TL_updateNewMessage) update2;
                        break;
                    }
                }
            }
            tL_updateNewMessage = null;
        }
        if (tL_updateNewMessage == null) {
            return false;
        }
        this.userStarGift = null;
        this.myProfile = false;
        MessageObject messageObject = new MessageObject(this.currentAccount, tL_updateNewMessage.message, false, false);
        messageObject.setType();
        set(messageObject);
        return true;
    }

    private boolean canConvert() {
        MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
                TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction;
                return (!messageObject.isOutOwner() || ((this.messageObject.getDialogId() > UserConfig.getInstance(this.currentAccount).getClientUserId() ? 1 : (this.messageObject.getDialogId() == UserConfig.getInstance(this.currentAccount).getClientUserId() ? 0 : -1)) == 0)) && !tL_messageActionStarGift.converted && tL_messageActionStarGift.convert_stars > 0 && MessagesController.getInstance(this.currentAccount).stargiftsConvertPeriodMax - (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - this.messageObject.messageOwner.date) > 0;
            }
        } else {
            TL_stars.UserStarGift userStarGift = this.userStarGift;
            if (userStarGift != null) {
                int currentTime = MessagesController.getInstance(this.currentAccount).stargiftsConvertPeriodMax - (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - userStarGift.date);
                if (this.myProfile) {
                    int i = this.userStarGift.flags;
                    if ((i & 8) != 0 && (i & 16) != 0 && (i & 2) != 0 && currentTime > 0) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void convert() {
        long j;
        final int i;
        int i2;
        final long j2;
        long j3;
        final long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            i = messageObject.getId();
            MessageObject messageObject2 = this.messageObject;
            int i3 = messageObject2.messageOwner.date;
            boolean isOutOwner = messageObject2.isOutOwner();
            long j4 = this.dialogId;
            j = isOutOwner ? clientUserId : j4;
            j3 = ((TLRPC.TL_messageActionStarGift) this.messageObject.messageOwner.action).convert_stars;
            i2 = i3;
            j2 = j4;
        } else {
            TL_stars.UserStarGift userStarGift = this.userStarGift;
            if (userStarGift == null) {
                return;
            }
            int i4 = userStarGift.msg_id;
            int i5 = userStarGift.date;
            j = ((userStarGift.flags & 2) == 0 || userStarGift.name_hidden) ? UserObject.ANONYMOUS : userStarGift.from_id;
            long j5 = userStarGift.convert_stars;
            long j6 = userStarGift.from_id;
            i = i4;
            i2 = i5;
            j2 = j6;
            j3 = j5;
        }
        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j2));
        int max = Math.max(1, (MessagesController.getInstance(this.currentAccount).stargiftsConvertPeriodMax - (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - i2)) / 86400);
        final long j7 = j3;
        new AlertDialog.Builder(getContext(), this.resourcesProvider).setTitle(LocaleController.getString(R.string.Gift2ConvertTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatPluralString("Gift2ConvertText2", max, (user == null || UserObject.isService(j)) ? LocaleController.getString(R.string.StarsTransactionHidden) : UserObject.getForcedFirstName(user), LocaleController.formatPluralStringComma("Gift2ConvertStars", (int) j3)))).setPositiveButton(LocaleController.getString(R.string.Gift2ConvertButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda47
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                StarGiftSheet.this.lambda$convert$35(i, j2, clientUserId, j7, dialogInterface, i6);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void doTransfer(final long j, final Runnable runnable) {
        TLRPC.Message message;
        long j2;
        int i;
        final String str;
        RequestDelegate requestDelegate;
        ConnectionsManager connectionsManager;
        TLRPC.TL_payments_getPaymentForm tL_payments_getPaymentForm;
        TL_stars.UserStarGift userStarGift = this.userStarGift;
        if (userStarGift == null || !(userStarGift.gift instanceof TL_stars.TL_starGiftUnique)) {
            MessageObject messageObject = this.messageObject;
            if (messageObject == null || (message = messageObject.messageOwner) == null) {
                return;
            }
            TLRPC.MessageAction messageAction = message.action;
            if (!(messageAction instanceof TLRPC.TL_messageActionStarGiftUnique)) {
                return;
            }
            TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique = (TLRPC.TL_messageActionStarGiftUnique) messageAction;
            int id = messageObject.getId();
            j2 = tL_messageActionStarGiftUnique.transfer_stars;
            i = id;
            str = tL_messageActionStarGiftUnique.gift.title + " #" + LocaleController.formatNumber(tL_messageActionStarGiftUnique.gift.num, ',');
        } else {
            i = userStarGift.msg_id;
            j2 = userStarGift.transfer_stars;
            str = this.userStarGift.gift.title + " #" + LocaleController.formatNumber(this.userStarGift.gift.num, ',');
        }
        if (j2 <= 0) {
            TL_stars.transferStarGift transferstargift = new TL_stars.transferStarGift();
            transferstargift.msg_id = i;
            transferstargift.to_id = MessagesController.getInstance(this.currentAccount).getInputUser(j);
            ConnectionsManager connectionsManager2 = ConnectionsManager.getInstance(this.currentAccount);
            final String str2 = str;
            requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda72
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    StarGiftSheet.this.lambda$doTransfer$64(runnable, j, str2, tLObject, tL_error);
                }
            };
            tL_payments_getPaymentForm = transferstargift;
            connectionsManager = connectionsManager2;
        } else {
            final StarsController starsController = StarsController.getInstance(this.currentAccount);
            if (!starsController.balanceAvailable()) {
                starsController.getBalance(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda73
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarGiftSheet.this.lambda$doTransfer$65(starsController, j, runnable);
                    }
                });
                return;
            }
            final TLRPC.TL_inputInvoiceStarGiftTransfer tL_inputInvoiceStarGiftTransfer = new TLRPC.TL_inputInvoiceStarGiftTransfer();
            tL_inputInvoiceStarGiftTransfer.msg_id = i;
            tL_inputInvoiceStarGiftTransfer.to_id = MessagesController.getInstance(this.currentAccount).getInputUser(j);
            TLRPC.TL_payments_getPaymentForm tL_payments_getPaymentForm2 = new TLRPC.TL_payments_getPaymentForm();
            tL_payments_getPaymentForm2.invoice = tL_inputInvoiceStarGiftTransfer;
            JSONObject makeThemeParams = BotWebViewSheet.makeThemeParams(this.resourcesProvider);
            if (makeThemeParams != null) {
                TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
                tL_payments_getPaymentForm2.theme_params = tL_dataJSON;
                tL_dataJSON.data = makeThemeParams.toString();
                tL_payments_getPaymentForm2.flags |= 1;
            }
            ConnectionsManager connectionsManager3 = ConnectionsManager.getInstance(this.currentAccount);
            requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda74
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    StarGiftSheet.this.lambda$doTransfer$74(tL_inputInvoiceStarGiftTransfer, runnable, j, str, tLObject, tL_error);
                }
            };
            tL_payments_getPaymentForm = tL_payments_getPaymentForm2;
            connectionsManager = connectionsManager3;
        }
        connectionsManager.sendRequest(tL_payments_getPaymentForm, requestDelegate);
    }

    private void doUpgrade() {
        int i;
        final long j;
        if (this.button.isLoading()) {
            return;
        }
        MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            i = messageObject.getId();
            TLRPC.MessageAction messageAction = this.messageObject.messageOwner.action;
            if (!(messageAction instanceof TLRPC.TL_messageActionStarGift)) {
                return;
            } else {
                j = ((TLRPC.TL_messageActionStarGift) messageAction).upgrade_stars;
            }
        } else {
            TL_stars.UserStarGift userStarGift = this.userStarGift;
            if (userStarGift == null) {
                return;
            }
            int i2 = userStarGift.msg_id;
            long j2 = userStarGift.upgrade_stars;
            i = i2;
            j = j2;
        }
        if (j > 0 || this.upgrade_form != null) {
            this.button.setLoading(true);
            if (j > 0) {
                TL_stars.upgradeStarGift upgradestargift = new TL_stars.upgradeStarGift();
                upgradestargift.keep_original_details = this.checkbox.isChecked();
                upgradestargift.msg_id = i;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(upgradestargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda69
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        StarGiftSheet.this.lambda$doUpgrade$51(tLObject, tL_error);
                    }
                });
                return;
            }
            final StarsController starsController = StarsController.getInstance(this.currentAccount);
            if (!starsController.balanceAvailable()) {
                starsController.getBalance(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda70
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarGiftSheet.this.lambda$doUpgrade$52(starsController);
                    }
                });
                return;
            }
            TLRPC.TL_inputInvoiceStarGiftUpgrade tL_inputInvoiceStarGiftUpgrade = new TLRPC.TL_inputInvoiceStarGiftUpgrade();
            tL_inputInvoiceStarGiftUpgrade.keep_original_details = this.checkbox.isChecked();
            tL_inputInvoiceStarGiftUpgrade.msg_id = i;
            TL_stars.TL_payments_sendStarsForm tL_payments_sendStarsForm = new TL_stars.TL_payments_sendStarsForm();
            tL_payments_sendStarsForm.form_id = this.upgrade_form.form_id;
            tL_payments_sendStarsForm.invoice = tL_inputInvoiceStarGiftUpgrade;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_sendStarsForm, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda71
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    StarGiftSheet.this.lambda$doUpgrade$58(j, tLObject, tL_error);
                }
            });
        }
    }

    private long getDialogId() {
        MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            if (message == null) {
                return 0L;
            }
            TLRPC.MessageAction messageAction = message.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
                return messageObject.isOutOwner() ? this.messageObject.getDialogId() : UserConfig.getInstance(this.currentAccount).getClientUserId();
            }
            if (messageAction instanceof TLRPC.TL_messageActionStarGiftUnique) {
                TL_stars.StarGift starGift = ((TLRPC.TL_messageActionStarGiftUnique) messageAction).gift;
                if (starGift instanceof TL_stars.TL_starGiftUnique) {
                    return starGift.owner_id;
                }
            }
        } else {
            TL_stars.UserStarGift userStarGift = this.userStarGift;
            if (userStarGift != null) {
                TL_stars.StarGift starGift2 = userStarGift.gift;
                return starGift2 instanceof TL_stars.TL_starGiftUnique ? ((TL_stars.TL_starGiftUnique) starGift2).owner_id : this.dialogId;
            }
            TL_stars.TL_starGiftUnique tL_starGiftUnique = this.slugStarGift;
            if (tL_starGiftUnique != null) {
                return tL_starGiftUnique.owner_id;
            }
        }
        return 0L;
    }

    private String getLink() {
        TL_stars.StarGift gift = getGift();
        if (!(gift instanceof TL_stars.TL_starGiftUnique) || gift.slug == null) {
            return null;
        }
        return MessagesController.getInstance(this.currentAccount).linkPrefix + "/nft/" + gift.slug;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addAttributeRow$8(TL_stars.StarGiftAttribute starGiftAttribute, ButtonSpan.TextViewButtons[] textViewButtonsArr) {
        showHint(starGiftAttribute.rarity_permille, textViewButtonsArr[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$convert$32(StarsIntroActivity starsIntroActivity, long j) {
        BulletinFactory.of(starsIntroActivity).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.Gift2ConvertedTitle), LocaleController.formatPluralStringComma("Gift2Converted", (int) j)).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$convert$33(AlertDialog alertDialog, TLObject tLObject, long j, long j2, final long j3, TLRPC.TL_error tL_error) {
        alertDialog.dismissUnless(400L);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        if (!(tLObject instanceof TLRPC.TL_boolTrue)) {
            getBulletinFactory().createErrorBulletin(tL_error != null ? LocaleController.formatString(R.string.UnknownErrorCode, tL_error.text) : LocaleController.getString(R.string.UnknownError)).show(false);
            return;
        }
        dismiss();
        StarsController.getInstance(this.currentAccount).invalidateProfileGifts(j);
        TLRPC.UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(j2);
        if (userFull != null) {
            int max = Math.max(0, userFull.stargifts_count - 1);
            userFull.stargifts_count = max;
            if (max <= 0) {
                userFull.flags2 &= -257;
            }
        }
        StarsController.getInstance(this.currentAccount).invalidateBalance();
        StarsController.getInstance(this.currentAccount).invalidateTransactions(true);
        if (safeLastFragment instanceof StarsIntroActivity) {
            BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.stars_topup, LocaleController.getString(R.string.Gift2ConvertedTitle), LocaleController.formatPluralStringComma("Gift2Converted", (int) j3)).show(true);
            return;
        }
        final StarsIntroActivity starsIntroActivity = new StarsIntroActivity();
        starsIntroActivity.whenFullyVisible(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda77
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.lambda$convert$32(StarsIntroActivity.this, j3);
            }
        });
        safeLastFragment.presentFragment(starsIntroActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$convert$34(final AlertDialog alertDialog, final long j, final long j2, final long j3, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda65
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$convert$33(alertDialog, tLObject, j, j2, j3, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$convert$35(int i, final long j, final long j2, final long j3, DialogInterface dialogInterface, int i2) {
        final AlertDialog alertDialog = new AlertDialog(ApplicationLoader.applicationContext, 3);
        alertDialog.showDelayed(500L);
        TL_stars.convertStarGift convertstargift = new TL_stars.convertStarGift();
        convertstargift.msg_id = i;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(convertstargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda64
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarGiftSheet.this.lambda$convert$34(alertDialog, j, j2, j3, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$doTransfer$62(ChatActivity chatActivity, String str, TLRPC.User user) {
        BulletinFactory.of(chatActivity).createSimpleBulletin(R.raw.forward, LocaleController.getString(R.string.Gift2TransferredTitle), AndroidUtilities.replaceTags(LocaleController.formatString(R.string.Gift2TransferredText, str, UserObject.getForcedFirstName(user)))).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$63(Runnable runnable, TLObject tLObject, long j, final String str, TLRPC.TL_error tL_error) {
        if (runnable != null) {
            runnable.run();
        }
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            if (tLObject instanceof TLRPC.Updates) {
                final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
                final ChatActivity of = ChatActivity.of(j);
                of.whenFullyVisible(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarGiftSheet.lambda$doTransfer$62(ChatActivity.this, str, user);
                    }
                });
                safeLastFragment.presentFragment(of);
            } else {
                BulletinFactory.of(safeLastFragment).showForError(tL_error);
            }
        }
        StarsController.getInstance(this.currentAccount).invalidateProfileGifts(UserConfig.getInstance(this.currentAccount).getClientUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$64(final Runnable runnable, final long j, final String str, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.Updates) {
            MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates) tLObject, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda79
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doTransfer$63(runnable, tLObject, j, str, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$65(StarsController starsController, long j, Runnable runnable) {
        if (starsController.balanceAvailable()) {
            doTransfer(j, runnable);
        } else {
            getBulletinFactory().createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, "NO_BALANCE")).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$doTransfer$66(ChatActivity chatActivity, String str, TLRPC.User user) {
        BulletinFactory.of(chatActivity).createSimpleBulletin(R.raw.forward, LocaleController.getString(R.string.Gift2TransferredTitle), AndroidUtilities.replaceTags(LocaleController.formatString(R.string.Gift2TransferredText, str, UserObject.getForcedFirstName(user)))).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$67(TLRPC.TL_payments_paymentResult tL_payments_paymentResult) {
        MessagesController.getInstance(this.currentAccount).processUpdates(tL_payments_paymentResult.updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$68(boolean[] zArr, long j, Runnable runnable) {
        zArr[0] = true;
        this.button.setLoading(false);
        doTransfer(j, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$69(DialogInterface dialogInterface) {
        this.button.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$70(long j, final long j2, final Runnable runnable) {
        final boolean[] zArr = {false};
        StarsIntroActivity.StarsNeededSheet starsNeededSheet = new StarsIntroActivity.StarsNeededSheet(getContext(), this.resourcesProvider, j, 11, null, new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doTransfer$68(zArr, j2, runnable);
            }
        });
        starsNeededSheet.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                StarGiftSheet.this.lambda$doTransfer$69(dialogInterface);
            }
        });
        starsNeededSheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$71(TLObject tLObject, final Runnable runnable, final long j, final String str, TLRPC.TL_error tL_error, final long j2) {
        if (!(tLObject instanceof TLRPC.TL_payments_paymentResult)) {
            if (tL_error == null || !"BALANCE_TOO_LOW".equals(tL_error.text)) {
                getBulletinFactory().showForError(tL_error);
                return;
            } else if (MessagesController.getInstance(this.currentAccount).starsPurchaseAvailable()) {
                StarsController.getInstance(this.currentAccount).invalidateBalance(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarGiftSheet.this.lambda$doTransfer$70(j2, j, runnable);
                    }
                });
                return;
            } else {
                this.button.setLoading(false);
                StarsController.showNoSupportDialog(getContext(), this.resourcesProvider);
                return;
            }
        }
        final TLRPC.TL_payments_paymentResult tL_payments_paymentResult = (TLRPC.TL_payments_paymentResult) tLObject;
        MessagesController.getInstance(this.currentAccount).putUsers(tL_payments_paymentResult.updates.users, false);
        MessagesController.getInstance(this.currentAccount).putChats(tL_payments_paymentResult.updates.chats, false);
        StarsController.getInstance(this.currentAccount).invalidateTransactions(false);
        StarsController.getInstance(this.currentAccount).invalidateProfileGifts(UserConfig.getInstance(this.currentAccount).getClientUserId());
        StarsController.getInstance(this.currentAccount).invalidateBalance();
        if (runnable != null) {
            runnable.run();
        }
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            final TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
            final ChatActivity of = ChatActivity.of(j);
            of.whenFullyVisible(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    StarGiftSheet.lambda$doTransfer$66(ChatActivity.this, str, user);
                }
            });
            safeLastFragment.presentFragment(of);
        }
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doTransfer$67(tL_payments_paymentResult);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$72(final Runnable runnable, final long j, final String str, final long j2, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doTransfer$71(tLObject, runnable, j, str, tL_error, j2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$73(TLObject tLObject, TLRPC.TL_inputInvoiceStarGiftTransfer tL_inputInvoiceStarGiftTransfer, final Runnable runnable, final long j, final String str, TLRPC.TL_error tL_error) {
        if (!(tLObject instanceof TLRPC.PaymentForm)) {
            getBulletinFactory().makeForError(tL_error).show();
            return;
        }
        TLRPC.PaymentForm paymentForm = (TLRPC.PaymentForm) tLObject;
        MessagesController.getInstance(this.currentAccount).putUsers(paymentForm.users, false);
        TL_stars.TL_payments_sendStarsForm tL_payments_sendStarsForm = new TL_stars.TL_payments_sendStarsForm();
        tL_payments_sendStarsForm.form_id = paymentForm.form_id;
        tL_payments_sendStarsForm.invoice = tL_inputInvoiceStarGiftTransfer;
        Iterator<TLRPC.TL_labeledPrice> it = paymentForm.invoice.prices.iterator();
        final long j2 = 0;
        while (it.hasNext()) {
            j2 += it.next().amount;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_sendStarsForm, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda2
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject2, TLRPC.TL_error tL_error2) {
                StarGiftSheet.this.lambda$doTransfer$72(runnable, j, str, j2, tLObject2, tL_error2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doTransfer$74(final TLRPC.TL_inputInvoiceStarGiftTransfer tL_inputInvoiceStarGiftTransfer, final Runnable runnable, final long j, final String str, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda81
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doTransfer$73(tLObject, tL_inputInvoiceStarGiftTransfer, runnable, j, str, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$49(TLObject tLObject) {
        MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates) tLObject, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$50(TLRPC.TL_error tL_error, final TLObject tLObject) {
        String str;
        if (tL_error != null || !(tLObject instanceof TLRPC.Updates)) {
            getBulletinFactory().showForError(tL_error);
            return;
        }
        StarsController.getInstance(this.currentAccount).invalidateProfileGifts(UserConfig.getInstance(this.currentAccount).getClientUserId());
        if (!applyNewGiftFromUpdates((TLRPC.Updates) tLObject)) {
            dismiss();
            return;
        }
        this.button.setLoading(false);
        this.fireworksOverlay.start(true);
        switchPage(false, true);
        if (getGift() != null) {
            str = getGift().title + " #" + LocaleController.formatNumber(getGift().num, ',');
        } else {
            str = "";
        }
        getBulletinFactory().createSimpleBulletin(R.raw.gift_upgrade, LocaleController.getString(R.string.Gift2UpgradedTitle), AndroidUtilities.replaceTags(LocaleController.formatString(R.string.Gift2UpgradedText, str))).setDuration(5000).show();
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda80
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doUpgrade$49(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$51(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        if (tLObject instanceof TLRPC.Updates) {
            TLRPC.Updates updates = (TLRPC.Updates) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(updates.users, false);
            MessagesController.getInstance(this.currentAccount).putChats(updates.chats, false);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda78
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doUpgrade$50(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$52(StarsController starsController) {
        if (!starsController.balanceAvailable()) {
            getBulletinFactory().createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.UnknownErrorCode, "NO_BALANCE")).show();
        } else {
            this.button.setLoading(false);
            doUpgrade();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$53(TLRPC.TL_payments_paymentResult tL_payments_paymentResult) {
        MessagesController.getInstance(this.currentAccount).processUpdates(tL_payments_paymentResult.updates, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$54(boolean[] zArr) {
        zArr[0] = true;
        this.button.setLoading(false);
        doUpgrade();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$55(DialogInterface dialogInterface) {
        this.button.setLoading(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$56(long j) {
        final boolean[] zArr = {false};
        StarsIntroActivity.StarsNeededSheet starsNeededSheet = new StarsIntroActivity.StarsNeededSheet(getContext(), this.resourcesProvider, j, 10, null, new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doUpgrade$54(zArr);
            }
        });
        starsNeededSheet.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                StarGiftSheet.this.lambda$doUpgrade$55(dialogInterface);
            }
        });
        starsNeededSheet.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$57(TLObject tLObject, TLRPC.TL_error tL_error, final long j) {
        String str;
        if (!(tLObject instanceof TLRPC.TL_payments_paymentResult)) {
            if (tL_error == null || !"BALANCE_TOO_LOW".equals(tL_error.text)) {
                getBulletinFactory().showForError(tL_error);
                return;
            } else if (MessagesController.getInstance(this.currentAccount).starsPurchaseAvailable()) {
                StarsController.getInstance(this.currentAccount).invalidateBalance(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda83
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarGiftSheet.this.lambda$doUpgrade$56(j);
                    }
                });
                return;
            } else {
                this.button.setLoading(false);
                StarsController.showNoSupportDialog(getContext(), this.resourcesProvider);
                return;
            }
        }
        final TLRPC.TL_payments_paymentResult tL_payments_paymentResult = (TLRPC.TL_payments_paymentResult) tLObject;
        MessagesController.getInstance(this.currentAccount).putUsers(tL_payments_paymentResult.updates.users, false);
        MessagesController.getInstance(this.currentAccount).putChats(tL_payments_paymentResult.updates.chats, false);
        StarsController.getInstance(this.currentAccount).invalidateTransactions(false);
        StarsController.getInstance(this.currentAccount).invalidateProfileGifts(UserConfig.getInstance(this.currentAccount).getClientUserId());
        StarsController.getInstance(this.currentAccount).invalidateBalance();
        if (!applyNewGiftFromUpdates(tL_payments_paymentResult.updates)) {
            dismiss();
            return;
        }
        this.button.setLoading(false);
        this.fireworksOverlay.start(true);
        switchPage(false, true);
        if (getGift() != null) {
            str = getGift().title + " #" + LocaleController.formatNumber(getGift().num, ',');
        } else {
            str = "";
        }
        getBulletinFactory().createSimpleBulletin(R.raw.gift_upgrade, LocaleController.getString(R.string.Gift2UpgradedTitle), AndroidUtilities.replaceTags(LocaleController.formatString(R.string.Gift2UpgradedText, str))).setDuration(5000).show();
        Utilities.stageQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda82
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doUpgrade$53(tL_payments_paymentResult);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doUpgrade$58(final long j, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda76
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$doUpgrade$57(tLObject, tL_error, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        if (this.button.isLoading()) {
            return;
        }
        this.checkbox.setChecked(!r3.isChecked(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMenuPressed$1(String str) {
        AndroidUtilities.addToClipboard(str);
        getBulletinFactory().createCopyLinkBulletin(false).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onMenuPressed$2() {
        onSharePressed(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openAsLearnMore$30(View view) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openAsLearnMore$31(String str, TL_stars.starGiftUpgradePreview stargiftupgradepreview) {
        if (stargiftupgradepreview == null) {
            return;
        }
        this.topView.setPreviewingAttributes(stargiftupgradepreview.sample_attributes);
        switchPage(true, false);
        this.topView.setText(1, LocaleController.getString(R.string.Gift2LearnMoreTitle), 0L, LocaleController.formatString(R.string.Gift2LearnMoreText, str));
        this.featureCells[0].setText(LocaleController.getString(R.string.Gift2UpgradeFeature1TextLearn));
        this.featureCells[1].setText(LocaleController.getString(R.string.Gift2UpgradeFeature2TextLearn));
        this.featureCells[2].setText(LocaleController.getString(R.string.Gift2UpgradeFeature3TextLearn));
        this.checkboxLayout.setVisibility(8);
        this.checkboxSeparator.setVisibility(8);
        this.button.setText(LocaleController.getString(R.string.OK), false);
        this.button.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda75
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                StarGiftSheet.this.lambda$openAsLearnMore$30(view);
            }
        });
        show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openTransfer$59(UserSelectorBottomSheet[] userSelectorBottomSheetArr) {
        userSelectorBottomSheetArr[0].dismiss();
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openTransfer$60(Long l, final UserSelectorBottomSheet[] userSelectorBottomSheetArr, DialogInterface dialogInterface, int i) {
        doTransfer(l.longValue(), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda66
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$openTransfer$59(userSelectorBottomSheetArr);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openTransfer$61(int i, int i2, int i3, TL_stars.TL_starGiftUnique tL_starGiftUnique, long j, String str, final UserSelectorBottomSheet[] userSelectorBottomSheetArr, final Long l) {
        AlertDialog.Builder builder;
        AlertDialog.Builder title;
        String string;
        if (l.longValue() == -99) {
            Context context = getContext();
            Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
            if (i < i2) {
                builder = new AlertDialog.Builder(context, resourcesProvider);
                title = builder.setTitle(LocaleController.getString(R.string.Gift2ExportTONUnlocksAlertTitle));
                string = LocaleController.formatPluralString("Gift2ExportTONUnlocksAlertText", Math.max(1, i3), new Object[0]);
            } else {
                builder = new AlertDialog.Builder(context, resourcesProvider);
                title = builder.setTitle(LocaleController.getString(R.string.Gift2ExportTONUpdateRequiredTitle));
                string = LocaleController.getString(R.string.Gift2ExportTONUpdateRequiredText);
            }
            title.setMessage(string).setPositiveButton(LocaleController.getString(R.string.OK), null).show();
            return;
        }
        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(l);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.addView(new GiftTransferTopView(getContext(), tL_starGiftUnique, user), LayoutHelper.createLinear(-1, -2, 48, 0, -4, 0, 0));
        TextView textView = new TextView(getContext());
        textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, this.resourcesProvider));
        textView.setTextSize(1, 16.0f);
        textView.setText(AndroidUtilities.replaceTags(j > 0 ? LocaleController.formatPluralString("Gift2TransferPriceText", (int) j, str, UserObject.getForcedFirstName(MessagesController.getInstance(this.currentAccount).getUser(l))) : LocaleController.formatString(R.string.Gift2TransferText, str, UserObject.getForcedFirstName(user))));
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 48, 24, 4, 24, 4));
        new AlertDialog.Builder(getContext(), this.resourcesProvider).setView(linearLayout).setPositiveButton(j > 0 ? StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.Gift2TransferDoPrice, Integer.valueOf((int) j))) : LocaleController.getString(R.string.Gift2TransferDo), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda63
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i4) {
                StarGiftSheet.this.lambda$openTransfer$60(l, userSelectorBottomSheetArr, dialogInterface, i4);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openUpgrade$45(TL_stars.starGiftUpgradePreview stargiftupgradepreview) {
        if (stargiftupgradepreview == null) {
            return;
        }
        this.sample_attributes = stargiftupgradepreview.sample_attributes;
        openUpgradeAfter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openUpgrade$46(TLObject tLObject, TLRPC.TL_error tL_error) {
        if (!(tLObject instanceof TLRPC.PaymentForm)) {
            getBulletinFactory().makeForError(tL_error).show();
            return;
        }
        TLRPC.PaymentForm paymentForm = (TLRPC.PaymentForm) tLObject;
        MessagesController.getInstance(this.currentAccount).putUsers(paymentForm.users, false);
        this.upgrade_form = paymentForm;
        openUpgradeAfter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openUpgrade$47(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda62
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$openUpgrade$46(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openUpgradeAfter$48(View view) {
        doUpgrade();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$repostStory$5(Long l) {
        TLRPC.Chat chat;
        String str = (l.longValue() >= 0 || (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-l.longValue()))) == null) ? "" : chat.title;
        getBulletinFactory().createSimpleBulletin(R.raw.contact_check, AndroidUtilities.replaceTags(TextUtils.isEmpty(str) ? LocaleController.getString(R.string.GiftRepostedToProfile) : LocaleController.formatString(R.string.GiftRepostedToChannelProfile, str))).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$repostStory$6(StoryRecorder storyRecorder, View view, Long l, Runnable runnable, Boolean bool, final Long l2) {
        boolean booleanValue = bool.booleanValue();
        StoryRecorder.SourceView sourceView = null;
        if (booleanValue) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda60
                @Override // java.lang.Runnable
                public final void run() {
                    StarGiftSheet.this.lambda$repostStory$5(l2);
                }
            });
            storyRecorder.replaceSourceView(null);
            ShareAlert shareAlert = this.shareAlert;
            if (shareAlert != null) {
                shareAlert.dismiss();
                this.shareAlert = null;
            }
        } else {
            if ((view instanceof ShareDialogCell) && view.isAttachedToWindow()) {
                sourceView = StoryRecorder.SourceView.fromShareCell((ShareDialogCell) view);
            }
            storyRecorder.replaceSourceView(sourceView);
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$10(TL_stars.TL_starGiftUnique tL_starGiftUnique) {
        lambda$set$23(tL_starGiftUnique.owner_id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$11(MessageObject messageObject) {
        this.messageObjectRepolled = true;
        set(messageObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$12(int i, TLObject tLObject, TLRPC.TL_error tL_error) {
        final MessageObject messageObject;
        if (tLObject instanceof TLRPC.messages_Messages) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(messages_messages.users, false);
            for (int i2 = 0; i2 < messages_messages.messages.size(); i2++) {
                TLRPC.Message message = messages_messages.messages.get(i2);
                if (message != null && message.id == i && (message.action instanceof TLRPC.TL_messageActionStarGiftUnique)) {
                    messageObject = new MessageObject(this.currentAccount, message, false, false);
                    messageObject.setType();
                    break;
                }
            }
        }
        messageObject = null;
        if (messageObject != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda54
                @Override // java.lang.Runnable
                public final void run() {
                    StarGiftSheet.this.lambda$set$11(messageObject);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$13(View view) {
        toggleShow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$14(View view) {
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$15() {
        new ExplainStarsSheet(getContext()).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$17(long j) {
        new GiftSheet(getContext(), this.currentAccount, j, new StarGiftSheet$$ExternalSyntheticLambda48(this)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$18(View view) {
        openUpgrade();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$19(View view) {
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$20() {
        new ExplainStarsSheet(getContext()).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$22(long j) {
        new GiftSheet(getContext(), this.currentAccount, j, new StarGiftSheet$$ExternalSyntheticLambda48(this)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$24(long j) {
        new GiftSheet(getContext(), this.currentAccount, j, new StarGiftSheet$$ExternalSyntheticLambda48(this)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$25(MessageObject messageObject) {
        this.messageObjectRepolled = true;
        set(messageObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$26(TLObject tLObject, TLRPC.TL_error tL_error) {
        final MessageObject messageObject;
        if (tLObject instanceof TLRPC.messages_Messages) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            for (int i = 0; i < messages_messages.messages.size(); i++) {
                TLRPC.Message message = messages_messages.messages.get(i);
                if (message != null && (message.action instanceof TLRPC.TL_messageActionStarGift)) {
                    messageObject = new MessageObject(this.currentAccount, message, false, false);
                    messageObject.setType();
                    break;
                }
            }
        }
        messageObject = null;
        if (messageObject != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda56
                @Override // java.lang.Runnable
                public final void run() {
                    StarGiftSheet.this.lambda$set$25(messageObject);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$27(View view) {
        openUpgrade();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$28(View view) {
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$29(long j) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        dismiss();
        if ((safeLastFragment instanceof ProfileActivity) && ((ProfileActivity) safeLastFragment).myProfile) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", j);
        bundle.putBoolean("my_profile", true);
        bundle.putBoolean("open_gifts", true);
        safeLastFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$set$9(TL_stars.TL_starGiftUnique tL_starGiftUnique) {
        lambda$set$23(tL_starGiftUnique.owner_id);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$show$39(TL_stars.TL_payments_uniqueStarGift tL_payments_uniqueStarGift) {
        TL_stars.TL_starGiftUnique tL_starGiftUnique = (TL_stars.TL_starGiftUnique) tL_payments_uniqueStarGift.gift;
        this.slugStarGift = tL_starGiftUnique;
        set(tL_starGiftUnique, true, false);
        super.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$show$40(AlertDialog alertDialog) {
        alertDialog.dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.UniqueGiftNotFound)).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$show$41(final AlertDialog alertDialog, TLObject tLObject, TLRPC.TL_error tL_error) {
        if (tLObject instanceof TL_stars.TL_payments_uniqueStarGift) {
            final TL_stars.TL_payments_uniqueStarGift tL_payments_uniqueStarGift = (TL_stars.TL_payments_uniqueStarGift) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(tL_payments_uniqueStarGift.users, false);
            if (tL_payments_uniqueStarGift.gift instanceof TL_stars.TL_starGiftUnique) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda45
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarGiftSheet.this.lambda$show$39(tL_payments_uniqueStarGift);
                    }
                });
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.lambda$show$40(AlertDialog.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$show$42(AlertDialog alertDialog, MessageObject messageObject) {
        alertDialog.dismiss();
        this.messageObjectRepolled = true;
        set(messageObject);
        super.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$show$43(AlertDialog alertDialog) {
        alertDialog.dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            BulletinFactory.of(safeLastFragment).createSimpleBulletin(R.raw.error, LocaleController.getString(R.string.MessageNotFound)).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$show$44(TLRPC.TL_messageActionStarGift tL_messageActionStarGift, final AlertDialog alertDialog, TLObject tLObject, TLRPC.TL_error tL_error) {
        final MessageObject messageObject;
        if (tLObject instanceof TLRPC.messages_Messages) {
            TLRPC.messages_Messages messages_messages = (TLRPC.messages_Messages) tLObject;
            MessagesController.getInstance(this.currentAccount).putUsers(messages_messages.users, false);
            for (int i = 0; i < messages_messages.messages.size(); i++) {
                TLRPC.Message message = messages_messages.messages.get(i);
                if (message != null && !(message instanceof TLRPC.TL_messageEmpty) && message.id == tL_messageActionStarGift.upgrade_msg_id) {
                    messageObject = new MessageObject(this.currentAccount, message, false, false);
                    messageObject.setType();
                    break;
                }
            }
        }
        messageObject = null;
        if (messageObject != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda49
                @Override // java.lang.Runnable
                public final void run() {
                    StarGiftSheet.this.lambda$show$42(alertDialog, messageObject);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda50
                @Override // java.lang.Runnable
                public final void run() {
                    StarGiftSheet.lambda$show$43(AlertDialog.this);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchPage$7(ValueAnimator valueAnimator) {
        onSwitchedPage(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleShow$36(BaseFragment baseFragment) {
        Bundle bundle = new Bundle();
        bundle.putLong("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
        bundle.putBoolean("my_profile", true);
        bundle.putBoolean("open_gifts", true);
        baseFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleShow$37(TLObject tLObject, long j, TLRPC.Document document, boolean z, TLRPC.TL_error tL_error) {
        final BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            dismiss();
            StarsController.getInstance(this.currentAccount).invalidateProfileGifts(j);
            BulletinFactory.of(safeLastFragment).createEmojiBulletin(document, LocaleController.getString(z ? R.string.Gift2MadePrivateTitle : R.string.Gift2MadePublicTitle), AndroidUtilities.replaceSingleTag(LocaleController.getString(z ? R.string.Gift2MadePrivate : R.string.Gift2MadePublic), safeLastFragment instanceof ProfileActivity ? null : new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda68
                @Override // java.lang.Runnable
                public final void run() {
                    StarGiftSheet.this.lambda$toggleShow$36(safeLastFragment);
                }
            })).show(true);
        } else if (tL_error != null) {
            getBulletinFactory().createErrorBulletin(LocaleController.formatString(R.string.UnknownErrorCode, tL_error.text)).show(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toggleShow$38(final long j, final TLRPC.Document document, final boolean z, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda59
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$toggleShow$37(tLObject, j, document, z, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMenuPressed(View view) {
        final String link = getLink();
        ItemOptions.makeOptions(this.container, this.resourcesProvider, view).addIf(link != null, R.drawable.msg_link, LocaleController.getString(R.string.CopyLink), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$onMenuPressed$1(link);
            }
        }).addIf(link != null, R.drawable.msg_share, LocaleController.getString(R.string.ShareFile), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$onMenuPressed$2();
            }
        }).addIf(canTransfer(), R.drawable.menu_feature_transfer, LocaleController.getString(R.string.Gift2TransferOption), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$onMenuPressed$3();
            }
        }).addIf(this.userStarGift == null && getDialogId() != 0, R.drawable.msg_view_file, LocaleController.getString(R.string.Gift2ViewInProfile), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda43
            @Override // java.lang.Runnable
            public final void run() {
                StarGiftSheet.this.lambda$onMenuPressed$4();
            }
        }).setDrawScrim(false).setOnTopOfScrim().setDimAlpha(0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSwitchedPage(float f) {
        this.currentPage = f;
        this.infoLayout.setAlpha(1.0f - f);
        this.upgradeLayout.setAlpha(f);
        this.topView.onSwitchPage(f);
        this.container.updateTopViewTranslation();
        this.container.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: openInProfile, reason: merged with bridge method [inline-methods] */
    public void lambda$onMenuPressed$4() {
        long dialogId = getDialogId();
        if (dialogId == 0) {
            return;
        }
        lambda$set$23(dialogId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: openProfile, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$set$23(long j) {
        HintView2 hintView2 = this.currentHintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.currentHintView = null;
        }
        dismiss();
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null || UserObject.isService(j)) {
            return;
        }
        Bundle bundle = new Bundle();
        if (j > 0) {
            bundle.putLong("user_id", j);
            if (j == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                bundle.putBoolean("my_profile", true);
            }
            bundle.putBoolean("open_gifts", true);
        } else {
            bundle.putLong("chat_id", -j);
        }
        safeLastFragment.presentFragment(new ProfileActivity(bundle));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0061, code lost:
    
        if (android.text.TextUtils.isEmpty(r0.text) == false) goto L17;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void openUpgrade() {
        long j;
        long j2;
        int i;
        boolean z;
        boolean z2;
        TextView textView;
        int i2;
        HintView2 hintView2 = this.currentHintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.currentHintView = null;
        }
        if (this.switchingPagesAnimator != null) {
            return;
        }
        MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
            if (!(messageAction instanceof TLRPC.TL_messageActionStarGift)) {
                return;
            }
            TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction;
            j = tL_messageActionStarGift.gift.id;
            j2 = tL_messageActionStarGift.upgrade_stars;
            int id = messageObject.getId();
            boolean z3 = tL_messageActionStarGift.name_hidden;
            TLRPC.TL_textWithEntities tL_textWithEntities = tL_messageActionStarGift.message;
            if (tL_textWithEntities == null || TextUtils.isEmpty(tL_textWithEntities.text)) {
                z = z3;
                i = id;
                z2 = false;
            } else {
                z = z3;
                i = id;
                z2 = true;
            }
        } else {
            TL_stars.UserStarGift userStarGift = this.userStarGift;
            if (userStarGift == null) {
                return;
            }
            TL_stars.StarGift starGift = userStarGift.gift;
            j = starGift.id;
            j2 = userStarGift.upgrade_stars;
            i = userStarGift.msg_id;
            z = (starGift instanceof TL_stars.TL_starGift) && userStarGift.name_hidden;
            TLRPC.TL_textWithEntities tL_textWithEntities2 = userStarGift.message;
            if (tL_textWithEntities2 != null) {
            }
            z2 = false;
        }
        if (z) {
            textView = this.checkboxTextView;
            i2 = R.string.Gift2AddMyNameName;
        } else if (z2) {
            textView = this.checkboxTextView;
            i2 = R.string.Gift2AddSenderNameComment;
        } else {
            textView = this.checkboxTextView;
            i2 = R.string.Gift2AddSenderName;
        }
        textView.setText(LocaleController.getString(i2));
        this.checkbox.setChecked(!z && j2 > 0, false);
        ArrayList arrayList = this.sample_attributes;
        if (arrayList != null && (j2 > 0 || this.upgrade_form != null)) {
            openUpgradeAfter();
            return;
        }
        if (arrayList == null) {
            StarsController.getInstance(this.currentAccount).getStarGiftPreview(j, new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda51
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    StarGiftSheet.this.lambda$openUpgrade$45((TL_stars.starGiftUpgradePreview) obj);
                }
            });
        }
        if (j2 > 0 || this.upgrade_form != null) {
            return;
        }
        TLRPC.TL_inputInvoiceStarGiftUpgrade tL_inputInvoiceStarGiftUpgrade = new TLRPC.TL_inputInvoiceStarGiftUpgrade();
        tL_inputInvoiceStarGiftUpgrade.keep_original_details = this.checkbox.isChecked();
        tL_inputInvoiceStarGiftUpgrade.msg_id = i;
        TLRPC.TL_payments_getPaymentForm tL_payments_getPaymentForm = new TLRPC.TL_payments_getPaymentForm();
        tL_payments_getPaymentForm.invoice = tL_inputInvoiceStarGiftUpgrade;
        JSONObject makeThemeParams = BotWebViewSheet.makeThemeParams(this.resourcesProvider);
        if (makeThemeParams != null) {
            TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
            tL_payments_getPaymentForm.theme_params = tL_dataJSON;
            tL_dataJSON.data = makeThemeParams.toString();
            tL_payments_getPaymentForm.flags |= 1;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda52
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarGiftSheet.this.lambda$openUpgrade$47(tLObject, tL_error);
            }
        });
    }

    private void openUpgradeAfter() {
        long j;
        MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
            if (!(messageAction instanceof TLRPC.TL_messageActionStarGift)) {
                return;
            } else {
                j = ((TLRPC.TL_messageActionStarGift) messageAction).upgrade_stars;
            }
        } else {
            TL_stars.UserStarGift userStarGift = this.userStarGift;
            if (userStarGift == null) {
                return;
            } else {
                j = userStarGift.upgrade_stars;
            }
        }
        if (this.sample_attributes != null) {
            if (j > 0 || this.upgrade_form != null) {
                long j2 = 0;
                if (this.upgrade_form != null) {
                    for (int i = 0; i < this.upgrade_form.invoice.prices.size(); i++) {
                        j2 += this.upgrade_form.invoice.prices.get(i).amount;
                    }
                }
                this.topView.setPreviewingAttributes(this.sample_attributes);
                this.topView.setText(1, LocaleController.getString(R.string.Gift2UpgradeTitle), 0L, LocaleController.getString(R.string.Gift2UpgradeText));
                if (j2 > 0) {
                    this.button.setText(StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.Gift2UpgradeButton, Long.valueOf(j2))), true);
                } else {
                    this.button.setText(LocaleController.getString(R.string.Confirm), true);
                }
                this.button.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda61
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        StarGiftSheet.this.lambda$openUpgradeAfter$48(view);
                    }
                });
                switchPage(true, true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void repostStory(final View view) {
        LaunchActivity launchActivity = LaunchActivity.instance;
        if (launchActivity == null) {
            return;
        }
        StoryRecorder.SourceView fromShareCell = view instanceof ShareDialogCell ? StoryRecorder.SourceView.fromShareCell((ShareDialogCell) view) : null;
        ArrayList arrayList = new ArrayList();
        MessageObject messageObject = this.messageObject;
        if (messageObject == null) {
            if (!(getGift() instanceof TL_stars.TL_starGiftUnique)) {
                return;
            }
            long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            TL_stars.TL_starGiftUnique tL_starGiftUnique = (TL_stars.TL_starGiftUnique) getGift();
            TLRPC.TL_messageService tL_messageService = new TLRPC.TL_messageService();
            tL_messageService.peer_id = MessagesController.getInstance(this.currentAccount).getPeer(clientUserId);
            tL_messageService.from_id = MessagesController.getInstance(this.currentAccount).getPeer(clientUserId);
            tL_messageService.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique = new TLRPC.TL_messageActionStarGiftUnique();
            tL_messageActionStarGiftUnique.gift = tL_starGiftUnique;
            tL_messageActionStarGiftUnique.upgrade = true;
            tL_messageService.action = tL_messageActionStarGiftUnique;
            messageObject = new MessageObject(this.currentAccount, tL_messageService, false, false);
            messageObject.setType();
        }
        arrayList.add(messageObject);
        final StoryRecorder storyRecorder = StoryRecorder.getInstance(launchActivity, this.currentAccount);
        storyRecorder.setOnPrepareCloseListener(new Utilities.Callback4() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda57
            @Override // org.telegram.messenger.Utilities.Callback4
            public final void run(Object obj, Object obj2, Object obj3, Object obj4) {
                StarGiftSheet.this.lambda$repostStory$6(storyRecorder, view, (Long) obj, (Runnable) obj2, (Boolean) obj3, (Long) obj4);
            }
        });
        storyRecorder.openRepost(fromShareCell, StoryEntry.repostMessage(arrayList));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleShow() {
        final boolean z;
        final TLRPC.Document document;
        int i;
        boolean z2;
        TL_stars.StarGift starGift;
        if (this.button.isLoading()) {
            return;
        }
        final long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        MessageObject messageObject = this.messageObject;
        if (messageObject == null || messageObject.messageOwner == null) {
            TL_stars.UserStarGift userStarGift = this.userStarGift;
            if (userStarGift == null) {
                return;
            }
            int i2 = userStarGift.msg_id;
            z = !userStarGift.unsaved;
            document = userStarGift.gift.getDocument();
            i = i2;
        } else {
            i = messageObject.getId();
            TLRPC.MessageAction messageAction = this.messageObject.messageOwner.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
                TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction;
                z2 = tL_messageActionStarGift.saved;
                starGift = tL_messageActionStarGift.gift;
            } else {
                if (!(messageAction instanceof TLRPC.TL_messageActionStarGiftUnique)) {
                    return;
                }
                TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique = (TLRPC.TL_messageActionStarGiftUnique) messageAction;
                z2 = tL_messageActionStarGiftUnique.saved;
                starGift = tL_messageActionStarGiftUnique.gift;
            }
            z = z2;
            document = starGift.getDocument();
        }
        this.button.setLoading(true);
        TL_stars.saveStarGift savestargift = new TL_stars.saveStarGift();
        savestargift.unsave = z;
        savestargift.msg_id = i;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(savestargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda53
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                StarGiftSheet.this.lambda$toggleShow$38(clientUserId, document, z, tLObject, tL_error);
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0027, code lost:
    
        if ((r0 instanceof org.telegram.tgnet.tl.TL_stars.TL_starGiftUnique) != false) goto L18;
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0040 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean canTransfer() {
        TL_stars.TL_starGiftUnique tL_starGiftUnique;
        TL_stars.StarGift starGift;
        TLRPC.Message message;
        MessageObject messageObject = this.messageObject;
        if (messageObject != null && (message = messageObject.messageOwner) != null) {
            TLRPC.MessageAction messageAction = message.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGiftUnique) {
                TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique = (TLRPC.TL_messageActionStarGiftUnique) messageAction;
                if ((tL_messageActionStarGiftUnique.flags & 16) == 0) {
                    return false;
                }
                starGift = tL_messageActionStarGiftUnique.gift;
                if (!(starGift instanceof TL_stars.TL_starGiftUnique)) {
                    return false;
                }
                tL_starGiftUnique = (TL_stars.TL_starGiftUnique) starGift;
                return tL_starGiftUnique.owner_id != UserConfig.getInstance(this.currentAccount).getClientUserId();
            }
        }
        TL_stars.UserStarGift userStarGift = this.userStarGift;
        if (userStarGift != null) {
            starGift = userStarGift.gift;
        }
        tL_starGiftUnique = this.slugStarGift;
        if (tL_starGiftUnique == null) {
            return false;
        }
        if (tL_starGiftUnique.owner_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
        }
    }

    protected BulletinFactory getBulletinFactory() {
        return BulletinFactory.of(this.topBulletinContainer, this.resourcesProvider);
    }

    public TL_stars.StarGift getGift() {
        MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            TLRPC.Message message = messageObject.messageOwner;
            if (message == null) {
                return null;
            }
            TLRPC.MessageAction messageAction = message.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
                return ((TLRPC.TL_messageActionStarGift) messageAction).gift;
            }
            if (messageAction instanceof TLRPC.TL_messageActionStarGiftUnique) {
                return ((TLRPC.TL_messageActionStarGiftUnique) messageAction).gift;
            }
        } else {
            TL_stars.UserStarGift userStarGift = this.userStarGift;
            if (userStarGift != null) {
                return userStarGift.gift;
            }
            TL_stars.TL_starGiftUnique tL_starGiftUnique = this.slugStarGift;
            if (tL_starGiftUnique != null) {
                return tL_starGiftUnique;
            }
        }
        return null;
    }

    public boolean isSaved() {
        TLRPC.Message message;
        MessageObject messageObject = this.messageObject;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            if (this.userStarGift != null) {
                return !r0.unsaved;
            }
            return false;
        }
        TLRPC.MessageAction messageAction = message.action;
        if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
            return ((TLRPC.TL_messageActionStarGift) messageAction).saved;
        }
        if (messageAction instanceof TLRPC.TL_messageActionStarGiftUnique) {
            return ((TLRPC.TL_messageActionStarGiftUnique) messageAction).saved;
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onBackPressed() {
        if (this.currentPage <= 0.0f || this.button.isLoading() || this.isLearnMore) {
            super.onBackPressed();
            return;
        }
        MessageObject messageObject = this.messageObject;
        if (messageObject != null) {
            set(messageObject);
        } else {
            TL_stars.UserStarGift userStarGift = this.userStarGift;
            if (userStarGift != null) {
                set(this.myProfile, userStarGift);
            }
        }
        switchPage(false, true);
    }

    public void onSharePressed(View view) {
        ShareAlert shareAlert = this.shareAlert;
        if (shareAlert != null && shareAlert.isShown()) {
            this.shareAlert.dismiss();
        }
        String link = getLink();
        ChatActivity chatActivity = null;
        ArrayList arrayList = null;
        String str = null;
        boolean z = false;
        String str2 = null;
        boolean z2 = false;
        ShareAlert shareAlert2 = new ShareAlert(getContext(), chatActivity, arrayList, link, str, z, link, str2, z2, false, true, this.resourcesProvider) { // from class: org.telegram.ui.Stars.StarGiftSheet.1
            {
                this.includeStoryFromMessage = true;
            }

            @Override // org.telegram.ui.Components.ShareAlert
            protected void onSend(LongSparseArray longSparseArray, int i, TLRPC.TL_forumTopic tL_forumTopic) {
                Bulletin createSimpleBulletin;
                super.onSend(longSparseArray, i, tL_forumTopic);
                BulletinFactory bulletinFactory = StarGiftSheet.this.getBulletinFactory();
                if (bulletinFactory != null) {
                    if (longSparseArray.size() == 1) {
                        long keyAt = longSparseArray.keyAt(0);
                        if (keyAt == UserConfig.getInstance(this.currentAccount).clientUserId) {
                            createSimpleBulletin = bulletinFactory.createSimpleBulletin(R.raw.saved_messages, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.LinkSharedToSavedMessages, new Object[0])), 5000);
                        } else if (keyAt < 0) {
                            createSimpleBulletin = bulletinFactory.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.LinkSharedTo, tL_forumTopic != null ? tL_forumTopic.title : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-keyAt)).title)), 5000);
                        } else {
                            createSimpleBulletin = bulletinFactory.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.LinkSharedTo, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(keyAt)).first_name)), 5000);
                        }
                    } else {
                        createSimpleBulletin = bulletinFactory.createSimpleBulletin(R.raw.forward, AndroidUtilities.replaceTags(LocaleController.formatPluralString("LinkSharedToManyChats", longSparseArray.size(), Integer.valueOf(longSparseArray.size()))));
                    }
                    createSimpleBulletin.hideAfterBottomSheet(false).show();
                    try {
                        this.container.performHapticFeedback(3);
                    } catch (Exception unused) {
                    }
                }
            }

            @Override // org.telegram.ui.Components.ShareAlert
            protected void onShareStory(View view2) {
                StarGiftSheet.this.repostStory(view2);
            }
        };
        this.shareAlert = shareAlert2;
        shareAlert2.setDelegate(new ShareAlert.ShareAlertDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet.2
            @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
            public boolean didCopy() {
                StarGiftSheet.this.getBulletinFactory().createCopyLinkBulletin(false).show();
                return true;
            }

            @Override // org.telegram.ui.Components.ShareAlert.ShareAlertDelegate
            public /* synthetic */ void didShare() {
                ShareAlert.ShareAlertDelegate.-CC.$default$didShare(this);
            }
        });
        this.shareAlert.show();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected void onSwipeStarts() {
        HintView2 hintView2 = this.currentHintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.currentHintView = null;
        }
    }

    public void openAsLearnMore(long j, final String str) {
        this.isLearnMore = true;
        StarsController.getInstance(this.currentAccount).getStarGiftPreview(j, new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda67
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                StarGiftSheet.this.lambda$openAsLearnMore$31(str, (TL_stars.starGiftUpgradePreview) obj);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x009d  */
    /* renamed from: openTransfer, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void lambda$onMenuPressed$3() {
        TLRPC.Message message;
        final long j;
        final TL_stars.TL_starGiftUnique tL_starGiftUnique;
        final int i;
        HintView2 hintView2 = this.currentHintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.currentHintView = null;
        }
        TL_stars.UserStarGift userStarGift = this.userStarGift;
        if (userStarGift != null) {
            TL_stars.StarGift starGift = userStarGift.gift;
            if (starGift instanceof TL_stars.TL_starGiftUnique) {
                i = userStarGift.can_export_at;
                j = userStarGift.transfer_stars;
                tL_starGiftUnique = (TL_stars.TL_starGiftUnique) starGift;
                final int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                final String str = tL_starGiftUnique.title + " #" + LocaleController.formatNumber(tL_starGiftUnique.num, ',');
                UserSelectorBottomSheet userSelectorBottomSheet = new UserSelectorBottomSheet(new BaseFragment() { // from class: org.telegram.ui.Stars.StarGiftSheet.6
                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public Context getContext() {
                        return StarGiftSheet.this.getContext();
                    }

                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public Activity getParentActivity() {
                        LaunchActivity launchActivity = LaunchActivity.instance;
                        return launchActivity == null ? AndroidUtilities.findActivity(StarGiftSheet.this.getContext()) : launchActivity;
                    }

                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public Theme.ResourcesProvider getResourceProvider() {
                        return ((BottomSheet) StarGiftSheet.this).resourcesProvider;
                    }

                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public boolean presentFragment(BaseFragment baseFragment) {
                        r2[0].dismiss();
                        StarGiftSheet.this.dismiss();
                        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                        if (safeLastFragment != null) {
                            return safeLastFragment.presentFragment(safeLastFragment);
                        }
                        return false;
                    }
                }, 0L, BirthdayController.getInstance(this.currentAccount).getState(), 3, true);
                final UserSelectorBottomSheet[] userSelectorBottomSheetArr = {userSelectorBottomSheet};
                userSelectorBottomSheet.setTitle(LocaleController.formatString(R.string.Gift2Transfer, str));
                final int max = currentTime <= i ? 0 : Math.max(1, Math.round(Math.max(0, i - currentTime) / 86400.0f));
                userSelectorBottomSheetArr[0].addTONOption(max);
                userSelectorBottomSheetArr[0].setOnUserSelector(new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda55
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        StarGiftSheet.this.lambda$openTransfer$61(currentTime, i, max, tL_starGiftUnique, j, str, userSelectorBottomSheetArr, (Long) obj);
                    }
                });
                userSelectorBottomSheetArr[0].show();
            }
        }
        MessageObject messageObject = this.messageObject;
        if (messageObject == null || (message = messageObject.messageOwner) == null) {
            return;
        }
        TLRPC.MessageAction messageAction = message.action;
        if (messageAction instanceof TLRPC.TL_messageActionStarGiftUnique) {
            TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique = (TLRPC.TL_messageActionStarGiftUnique) messageAction;
            TL_stars.StarGift starGift2 = tL_messageActionStarGiftUnique.gift;
            if (starGift2 instanceof TL_stars.TL_starGiftUnique) {
                int i2 = tL_messageActionStarGiftUnique.can_export_at;
                messageObject.getId();
                j = tL_messageActionStarGiftUnique.transfer_stars;
                tL_starGiftUnique = (TL_stars.TL_starGiftUnique) starGift2;
                i = i2;
                final int currentTime2 = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                final String str2 = tL_starGiftUnique.title + " #" + LocaleController.formatNumber(tL_starGiftUnique.num, ',');
                UserSelectorBottomSheet userSelectorBottomSheet2 = new UserSelectorBottomSheet(new BaseFragment() { // from class: org.telegram.ui.Stars.StarGiftSheet.6
                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public Context getContext() {
                        return StarGiftSheet.this.getContext();
                    }

                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public Activity getParentActivity() {
                        LaunchActivity launchActivity = LaunchActivity.instance;
                        return launchActivity == null ? AndroidUtilities.findActivity(StarGiftSheet.this.getContext()) : launchActivity;
                    }

                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public Theme.ResourcesProvider getResourceProvider() {
                        return ((BottomSheet) StarGiftSheet.this).resourcesProvider;
                    }

                    @Override // org.telegram.ui.ActionBar.BaseFragment
                    public boolean presentFragment(BaseFragment baseFragment) {
                        userSelectorBottomSheetArr[0].dismiss();
                        StarGiftSheet.this.dismiss();
                        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                        if (safeLastFragment != null) {
                            return safeLastFragment.presentFragment(safeLastFragment);
                        }
                        return false;
                    }
                }, 0L, BirthdayController.getInstance(this.currentAccount).getState(), 3, true);
                final UserSelectorBottomSheet[] userSelectorBottomSheetArr2 = {userSelectorBottomSheet2};
                userSelectorBottomSheet2.setTitle(LocaleController.formatString(R.string.Gift2Transfer, str2));
                if (currentTime2 <= i) {
                }
                userSelectorBottomSheetArr2[0].addTONOption(max);
                userSelectorBottomSheetArr2[0].setOnUserSelector(new Utilities.Callback() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda55
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        StarGiftSheet.this.lambda$openTransfer$61(currentTime2, i, max, tL_starGiftUnique, j, str2, userSelectorBottomSheetArr2, (Long) obj);
                    }
                });
                userSelectorBottomSheetArr2[0].show();
            }
        }
    }

    public StarGiftSheet set(String str, TL_stars.TL_starGiftUnique tL_starGiftUnique) {
        this.slug = str;
        this.slugStarGift = tL_starGiftUnique;
        set(tL_starGiftUnique, true, false);
        this.beforeTableTextView.setVisibility(8);
        this.afterTableTextView.setVisibility(8);
        if (this.firstSet) {
            switchPage(false, false);
            this.firstSet = false;
        }
        return this;
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x03a0 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:106:0x03ae  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x03b9  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x03bc  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x03b1  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x03ce A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x03dd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0466  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0497  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x04c1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0303  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x02ab  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x02a4  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x05c3  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x02a1  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02a8  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02bf  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x030b  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x0343  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public StarGiftSheet set(MessageObject messageObject) {
        boolean isOutOwner;
        boolean z;
        boolean z2;
        TL_stars.StarGift starGift;
        long j;
        TLRPC.TL_textWithEntities tL_textWithEntities;
        boolean z3;
        boolean z4;
        long j2;
        boolean z5;
        boolean z6;
        long j3;
        TLRPC.TL_textWithEntities tL_textWithEntities2;
        boolean z7;
        boolean z8;
        TopView topView;
        String string;
        TopView topView2;
        long j4;
        CharSequence formatString;
        TopView topView3;
        String str;
        TL_stars.StarGift starGift2;
        CharSequence charSequence;
        String string2;
        char c;
        CharSequence charSequence2;
        CharSequence concat;
        final long j5;
        final long j6;
        boolean z9;
        boolean z10;
        TL_stars.StarGift starGift3;
        ButtonWithCounterView buttonWithCounterView;
        View.OnClickListener onClickListener;
        TL_stars.StarGift starGift4;
        boolean z11;
        boolean z12;
        String string3;
        LinkSpanDrawable.LinksTextView linksTextView;
        int i;
        boolean z13;
        boolean z14 = false;
        if (messageObject != null && messageObject.messageOwner != null) {
            this.myProfile = false;
            this.userStarGift = null;
            this.messageObject = messageObject;
            long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            boolean z15 = messageObject.getDialogId() == clientUserId;
            TLRPC.MessageAction messageAction = messageObject.messageOwner.action;
            if ((messageAction instanceof TLRPC.TL_messageActionStarGift) || (((z13 = messageAction instanceof TLRPC.TL_messageActionStarGiftUnique)) && (((TLRPC.TL_messageActionStarGiftUnique) messageAction).gift instanceof TL_stars.TL_starGift))) {
                isOutOwner = messageObject.isOutOwner();
                if (z15) {
                    isOutOwner = false;
                }
                TLRPC.Message message = messageObject.messageOwner;
                int i2 = message.date;
                TLRPC.MessageAction messageAction2 = message.action;
                if (messageAction2 instanceof TLRPC.TL_messageActionStarGift) {
                    TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction2;
                    z4 = tL_messageActionStarGift.converted;
                    z = tL_messageActionStarGift.saved;
                    boolean z16 = tL_messageActionStarGift.refunded;
                    boolean z17 = tL_messageActionStarGift.name_hidden;
                    starGift = tL_messageActionStarGift.gift;
                    boolean z18 = tL_messageActionStarGift.can_upgrade;
                    j2 = tL_messageActionStarGift.convert_stars;
                    z2 = z16;
                    long j7 = tL_messageActionStarGift.upgrade_stars;
                    TLRPC.TL_textWithEntities tL_textWithEntities3 = tL_messageActionStarGift.message;
                    boolean z19 = tL_messageActionStarGift.upgraded;
                    z3 = z18;
                    tL_textWithEntities = tL_textWithEntities3;
                    z5 = z19;
                    z6 = z17;
                    j = j7;
                } else {
                    TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique = (TLRPC.TL_messageActionStarGiftUnique) messageAction2;
                    z = tL_messageActionStarGiftUnique.saved;
                    z2 = tL_messageActionStarGiftUnique.refunded;
                    starGift = tL_messageActionStarGiftUnique.gift;
                    j = 0;
                    tL_textWithEntities = null;
                    z3 = false;
                    z4 = false;
                    j2 = 0;
                    z5 = true;
                    z6 = false;
                }
                j3 = clientUserId;
                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
                boolean isBot = UserObject.isBot(user);
                TopView topView4 = this.topView;
                if (getLink() != null) {
                    tL_textWithEntities2 = tL_textWithEntities;
                    z7 = true;
                } else {
                    tL_textWithEntities2 = tL_textWithEntities;
                    z7 = false;
                }
                topView4.setGift(starGift, z7);
                if (z15) {
                    topView = this.topView;
                    string = LocaleController.getString(R.string.Gift2TitleSaved);
                    if (z2) {
                        z8 = z15;
                        j4 = j;
                        topView3 = topView;
                        str = string;
                        formatString = null;
                        topView3.setText(0, str, 0L, formatString);
                        starGift2 = starGift;
                        charSequence = "";
                        this.tableView.clear();
                        j5 = isOutOwner ? j3 : this.dialogId;
                        j6 = isOutOwner ? this.dialogId : j3;
                        TLRPC.User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j5));
                        if (j5 != j3) {
                            z10 = z3;
                            z9 = z;
                            this.tableView.addRowUser(LocaleController.getString(R.string.Gift2From), this.currentAccount, j5, new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda34
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StarGiftSheet.this.lambda$set$21(j5);
                                }
                            }, (j5 == j3 || j5 == UserObject.ANONYMOUS || UserObject.isDeleted(user2) || isBot) ? null : LocaleController.getString(R.string.Gift2ButtonSendGift), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda35
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StarGiftSheet.this.lambda$set$22(j5);
                                }
                            });
                        } else {
                            z9 = z;
                            z10 = z3;
                        }
                        if (j6 != j3) {
                            this.tableView.addRowUser(LocaleController.getString(R.string.Gift2To), this.currentAccount, j6, new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda36
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StarGiftSheet.this.lambda$set$23(j6);
                                }
                            }, null, new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda37
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StarGiftSheet.this.lambda$set$24(j6);
                                }
                            });
                        }
                        this.tableView.addRowDateTime(LocaleController.getString(R.string.StarsTransactionDate), i2);
                        starGift3 = starGift2;
                        if (starGift3.stars > 0) {
                            this.tableView.addRow(LocaleController.getString(R.string.Gift2Value), StarsIntroActivity.replaceStarsWithPlain(TextUtils.concat("⭐️ " + LocaleController.formatNumber(starGift3.stars + j4, ','), " ", (!canConvert() || z2) ? charSequence : ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) j2), new StarGiftSheet$$ExternalSyntheticLambda19(this), this.resourcesProvider)), 0.8f));
                        }
                        if (!isOutOwner && !z4 && !z2) {
                            this.tableView.addRow(LocaleController.getString(R.string.Gift2Visibility), LocaleController.getString(!z9 ? R.string.Gift2Visible : R.string.Gift2Invisible), LocaleController.getString(!z9 ? R.string.Gift2VisibleHide : R.string.Gift2InvisibleShow), new StarGiftSheet$$ExternalSyntheticLambda20(this));
                        }
                        if (starGift3.limited && !z2) {
                            StarsIntroActivity.addAvailabilityRow(this.tableView, this.currentAccount, starGift3, this.resourcesProvider);
                        }
                        if (!isOutOwner && !z2) {
                            if (this.messageObjectRepolled && !z5) {
                                TextView textView = (TextView) ((TableView.TableRowContent) this.tableView.addRow(LocaleController.getString(R.string.Gift2Status), charSequence).getChildAt(1)).getChildAt(0);
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("x ");
                                LoadingSpan loadingSpan = new LoadingSpan(textView, AndroidUtilities.dp(90.0f), 0, this.resourcesProvider);
                                int i3 = Theme.key_windowBackgroundWhiteBlackText;
                                loadingSpan.setColors(Theme.multAlpha(Theme.getColor(i3, this.resourcesProvider), 0.21f), Theme.multAlpha(Theme.getColor(i3, this.resourcesProvider), 0.08f));
                                spannableStringBuilder.setSpan(loadingSpan, 0, 1, 33);
                                textView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                                if (!this.messageObjectRepolling) {
                                    this.messageObjectRepolling = true;
                                    TLRPC.TL_messages_getMessages tL_messages_getMessages = new TLRPC.TL_messages_getMessages();
                                    tL_messages_getMessages.id.add(Integer.valueOf(messageObject.getId()));
                                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getMessages, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda38
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                            StarGiftSheet.this.lambda$set$26(tLObject, tL_error);
                                        }
                                    });
                                }
                            } else if (z10) {
                                this.tableView.addRow(LocaleController.getString(R.string.Gift2Status), LocaleController.getString(R.string.Gift2StatusNonUnique));
                            } else {
                                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
                                spannableStringBuilder2.append((CharSequence) LocaleController.getString(R.string.Gift2StatusNonUnique));
                                spannableStringBuilder2.append((CharSequence) " ");
                                spannableStringBuilder2.append(ButtonSpan.make(LocaleController.getString(R.string.Gift2StatusUpgrade), new StarGiftSheet$$ExternalSyntheticLambda21(this), this.resourcesProvider));
                                this.tableView.addRow(LocaleController.getString(R.string.Gift2Status), spannableStringBuilder2);
                            }
                        }
                        if (tL_textWithEntities2 != null) {
                            TLRPC.TL_textWithEntities tL_textWithEntities4 = tL_textWithEntities2;
                            if (!TextUtils.isEmpty(tL_textWithEntities4.text) && !z2) {
                                this.tableView.addFullRow(tL_textWithEntities4.text, tL_textWithEntities4.entities);
                            }
                        }
                        if (!isOutOwner || !z10 || j4 <= 0 || z2) {
                            this.button.setText(LocaleController.getString(R.string.OK), true);
                            buttonWithCounterView = this.button;
                            onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda32
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    StarGiftSheet.this.lambda$set$28(view);
                                }
                            };
                        } else {
                            SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(LocaleController.getString(R.string.Gift2UpgradeButtonFree));
                            spannableStringBuilder3.append((CharSequence) " ^");
                            if (this.upgradeIconSpan == null) {
                                this.upgradeIconSpan = new ColoredImageSpan(new UpgradeIcon(this.button, Theme.getColor(Theme.key_featuredStickers_addButton, this.resourcesProvider)));
                            }
                            spannableStringBuilder3.setSpan(this.upgradeIconSpan, spannableStringBuilder3.length() - 1, spannableStringBuilder3.length(), 33);
                            this.button.setText(spannableStringBuilder3, true);
                            buttonWithCounterView = this.button;
                            onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda39
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    StarGiftSheet.this.lambda$set$27(view);
                                }
                            };
                        }
                        buttonWithCounterView.setOnClickListener(onClickListener);
                        starGift4 = starGift3;
                        z11 = z2;
                        z14 = z6;
                        z12 = z9;
                    } else if (z3) {
                        j4 = j;
                        topView3 = topView;
                        str = string;
                        formatString = AndroidUtilities.replaceTags(LocaleController.getString(R.string.Gift2SelfInfoUpgrade));
                        z8 = z15;
                        topView3.setText(0, str, 0L, formatString);
                        starGift2 = starGift;
                        charSequence = "";
                        this.tableView.clear();
                        if (isOutOwner) {
                        }
                        if (isOutOwner) {
                        }
                        TLRPC.User user22 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j5));
                        if (j5 != j3) {
                        }
                        if (j6 != j3) {
                        }
                        this.tableView.addRowDateTime(LocaleController.getString(R.string.StarsTransactionDate), i2);
                        starGift3 = starGift2;
                        if (starGift3.stars > 0) {
                        }
                        if (!isOutOwner) {
                            this.tableView.addRow(LocaleController.getString(R.string.Gift2Visibility), LocaleController.getString(!z9 ? R.string.Gift2Visible : R.string.Gift2Invisible), LocaleController.getString(!z9 ? R.string.Gift2VisibleHide : R.string.Gift2InvisibleShow), new StarGiftSheet$$ExternalSyntheticLambda20(this));
                        }
                        if (starGift3.limited) {
                            StarsIntroActivity.addAvailabilityRow(this.tableView, this.currentAccount, starGift3, this.resourcesProvider);
                        }
                        if (!isOutOwner) {
                            if (this.messageObjectRepolled) {
                            }
                            if (z10) {
                            }
                        }
                        if (tL_textWithEntities2 != null) {
                        }
                        if (isOutOwner) {
                        }
                        this.button.setText(LocaleController.getString(R.string.OK), true);
                        buttonWithCounterView = this.button;
                        onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda32
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                StarGiftSheet.this.lambda$set$28(view);
                            }
                        };
                        buttonWithCounterView.setOnClickListener(onClickListener);
                        starGift4 = starGift3;
                        z11 = z2;
                        z14 = z6;
                        z12 = z9;
                    } else {
                        if (j2 > 0) {
                            topView2 = topView;
                            z8 = z15;
                            string3 = LocaleController.formatPluralStringComma(z4 ? "Gift2SelfInfoConverted" : "Gift2SelfInfoConvert", (int) j2);
                        } else {
                            topView2 = topView;
                            z8 = z15;
                            string3 = LocaleController.getString(R.string.Gift2SelfInfo);
                        }
                        j4 = j;
                        formatString = AndroidUtilities.replaceTags(string3);
                        str = string;
                        topView3 = topView2;
                        topView3.setText(0, str, 0L, formatString);
                        starGift2 = starGift;
                        charSequence = "";
                        this.tableView.clear();
                        if (isOutOwner) {
                        }
                        if (isOutOwner) {
                        }
                        TLRPC.User user222 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j5));
                        if (j5 != j3) {
                        }
                        if (j6 != j3) {
                        }
                        this.tableView.addRowDateTime(LocaleController.getString(R.string.StarsTransactionDate), i2);
                        starGift3 = starGift2;
                        if (starGift3.stars > 0) {
                        }
                        if (!isOutOwner) {
                        }
                        if (starGift3.limited) {
                        }
                        if (!isOutOwner) {
                        }
                        if (tL_textWithEntities2 != null) {
                        }
                        if (isOutOwner) {
                        }
                        this.button.setText(LocaleController.getString(R.string.OK), true);
                        buttonWithCounterView = this.button;
                        onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda32
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                StarGiftSheet.this.lambda$set$28(view);
                            }
                        };
                        buttonWithCounterView.setOnClickListener(onClickListener);
                        starGift4 = starGift3;
                        z11 = z2;
                        z14 = z6;
                        z12 = z9;
                    }
                } else {
                    z8 = z15;
                    if ((isOutOwner || z3) && j > 0) {
                        topView = this.topView;
                        string = LocaleController.getString(isOutOwner ? R.string.Gift2TitleSent : R.string.Gift2TitleReceived);
                        if (!z2) {
                            if (isOutOwner) {
                                topView2 = topView;
                                j4 = j;
                                formatString = LocaleController.formatString(R.string.Gift2InfoFreeUpgrade, UserObject.getForcedFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId))));
                                str = string;
                                topView3 = topView2;
                                topView3.setText(0, str, 0L, formatString);
                                starGift2 = starGift;
                                charSequence = "";
                                this.tableView.clear();
                                if (isOutOwner) {
                                }
                                if (isOutOwner) {
                                }
                                TLRPC.User user2222 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j5));
                                if (j5 != j3) {
                                }
                                if (j6 != j3) {
                                }
                                this.tableView.addRowDateTime(LocaleController.getString(R.string.StarsTransactionDate), i2);
                                starGift3 = starGift2;
                                if (starGift3.stars > 0) {
                                }
                                if (!isOutOwner) {
                                }
                                if (starGift3.limited) {
                                }
                                if (!isOutOwner) {
                                }
                                if (tL_textWithEntities2 != null) {
                                }
                                if (isOutOwner) {
                                }
                                this.button.setText(LocaleController.getString(R.string.OK), true);
                                buttonWithCounterView = this.button;
                                onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda32
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view) {
                                        StarGiftSheet.this.lambda$set$28(view);
                                    }
                                };
                                buttonWithCounterView.setOnClickListener(onClickListener);
                                starGift4 = starGift3;
                                z11 = z2;
                                z14 = z6;
                                z12 = z9;
                            } else {
                                j4 = j;
                                topView3 = topView;
                                formatString = LocaleController.getString(R.string.Gift2InfoInFreeUpgrade);
                                str = string;
                                topView3.setText(0, str, 0L, formatString);
                                starGift2 = starGift;
                                charSequence = "";
                                this.tableView.clear();
                                if (isOutOwner) {
                                }
                                if (isOutOwner) {
                                }
                                TLRPC.User user22222 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j5));
                                if (j5 != j3) {
                                }
                                if (j6 != j3) {
                                }
                                this.tableView.addRowDateTime(LocaleController.getString(R.string.StarsTransactionDate), i2);
                                starGift3 = starGift2;
                                if (starGift3.stars > 0) {
                                }
                                if (!isOutOwner) {
                                }
                                if (starGift3.limited) {
                                }
                                if (!isOutOwner) {
                                }
                                if (tL_textWithEntities2 != null) {
                                }
                                if (isOutOwner) {
                                }
                                this.button.setText(LocaleController.getString(R.string.OK), true);
                                buttonWithCounterView = this.button;
                                onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda32
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view) {
                                        StarGiftSheet.this.lambda$set$28(view);
                                    }
                                };
                                buttonWithCounterView.setOnClickListener(onClickListener);
                                starGift4 = starGift3;
                                z11 = z2;
                                z14 = z6;
                                z12 = z9;
                            }
                        }
                        j4 = j;
                        topView3 = topView;
                        str = string;
                        formatString = null;
                        topView3.setText(0, str, 0L, formatString);
                        starGift2 = starGift;
                        charSequence = "";
                        this.tableView.clear();
                        if (isOutOwner) {
                        }
                        if (isOutOwner) {
                        }
                        TLRPC.User user222222 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j5));
                        if (j5 != j3) {
                        }
                        if (j6 != j3) {
                        }
                        this.tableView.addRowDateTime(LocaleController.getString(R.string.StarsTransactionDate), i2);
                        starGift3 = starGift2;
                        if (starGift3.stars > 0) {
                        }
                        if (!isOutOwner) {
                        }
                        if (starGift3.limited) {
                        }
                        if (!isOutOwner) {
                        }
                        if (tL_textWithEntities2 != null) {
                        }
                        if (isOutOwner) {
                        }
                        this.button.setText(LocaleController.getString(R.string.OK), true);
                        buttonWithCounterView = this.button;
                        onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda32
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                StarGiftSheet.this.lambda$set$28(view);
                            }
                        };
                        buttonWithCounterView.setOnClickListener(onClickListener);
                        starGift4 = starGift3;
                        z11 = z2;
                        z14 = z6;
                        z12 = z9;
                    } else {
                        j4 = j;
                        TopView topView5 = this.topView;
                        String string4 = LocaleController.getString(isOutOwner ? R.string.Gift2TitleSent : R.string.Gift2TitleReceived);
                        if (z2) {
                            starGift2 = starGift;
                            charSequence = "";
                            concat = null;
                        } else {
                            if (isBot || !canConvert()) {
                                starGift2 = starGift;
                                charSequence = "";
                                if (isOutOwner) {
                                    string2 = LocaleController.formatString((!z3 || j4 <= 0) ? R.string.Gift2Info2OutExpired : R.string.Gift2Info2OutUpgrade, UserObject.getForcedFirstName(user));
                                } else {
                                    string2 = LocaleController.getString(!z ? R.string.Gift2Info2BotKeep : R.string.Gift2Info2BotRemove);
                                }
                            } else if (!isOutOwner) {
                                starGift2 = starGift;
                                charSequence = "";
                                string2 = LocaleController.formatPluralStringComma(z4 ? "Gift2InfoConverted" : "Gift2Info3", (int) j2);
                            } else if (!z || z4) {
                                charSequence = "";
                                starGift2 = starGift;
                                string2 = LocaleController.formatPluralStringComma(z4 ? "Gift2InfoOutConverted" : "Gift2InfoOut", (int) j2, UserObject.getForcedFirstName(user));
                            } else {
                                charSequence = "";
                                string2 = LocaleController.formatString(R.string.Gift2InfoOutPinned, UserObject.getForcedFirstName(user));
                                starGift2 = starGift;
                            }
                            SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(string2);
                            if (isBot || !canConvert()) {
                                c = 1;
                                charSequence2 = charSequence;
                            } else {
                                c = 1;
                                charSequence2 = AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.Gift2More).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda31
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        StarGiftSheet.this.lambda$set$20();
                                    }
                                }), true);
                            }
                            CharSequence[] charSequenceArr = new CharSequence[3];
                            charSequenceArr[0] = replaceTags;
                            charSequenceArr[c] = " ";
                            charSequenceArr[2] = charSequence2;
                            concat = TextUtils.concat(charSequenceArr);
                        }
                        topView5.setText(0, string4, 0L, concat);
                        this.tableView.clear();
                        if (isOutOwner) {
                        }
                        if (isOutOwner) {
                        }
                        TLRPC.User user2222222 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j5));
                        if (j5 != j3) {
                        }
                        if (j6 != j3) {
                        }
                        this.tableView.addRowDateTime(LocaleController.getString(R.string.StarsTransactionDate), i2);
                        starGift3 = starGift2;
                        if (starGift3.stars > 0) {
                        }
                        if (!isOutOwner) {
                        }
                        if (starGift3.limited) {
                        }
                        if (!isOutOwner) {
                        }
                        if (tL_textWithEntities2 != null) {
                        }
                        if (isOutOwner) {
                        }
                        this.button.setText(LocaleController.getString(R.string.OK), true);
                        buttonWithCounterView = this.button;
                        onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda32
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                StarGiftSheet.this.lambda$set$28(view);
                            }
                        };
                        buttonWithCounterView.setOnClickListener(onClickListener);
                        starGift4 = starGift3;
                        z11 = z2;
                        z14 = z6;
                        z12 = z9;
                    }
                }
            } else {
                if (!z13) {
                    return this;
                }
                TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique2 = (TLRPC.TL_messageActionStarGiftUnique) messageAction;
                TL_stars.StarGift starGift5 = tL_messageActionStarGiftUnique2.gift;
                if (!(starGift5 instanceof TL_stars.TL_starGiftUnique)) {
                    return this;
                }
                TL_stars.TL_starGiftUnique tL_starGiftUnique = (TL_stars.TL_starGiftUnique) starGift5;
                boolean z20 = (tL_messageActionStarGiftUnique2.flags & 16) != 0;
                z11 = tL_messageActionStarGiftUnique2.refunded;
                set(tL_starGiftUnique, z20, z11);
                z12 = tL_messageActionStarGiftUnique2.saved;
                starGift4 = tL_messageActionStarGiftUnique2.gift;
                isOutOwner = (tL_messageActionStarGiftUnique2.upgrade ^ true) == messageObject.isOutOwner();
                if (messageObject.getDialogId() == clientUserId) {
                    isOutOwner = false;
                }
                j3 = clientUserId;
                z8 = z15;
                z4 = false;
            }
            if (z11) {
                this.beforeTableTextView.setVisibility(0);
                this.beforeTableTextView.setText(LocaleController.getString(R.string.Gift2Refunded));
                linksTextView = this.beforeTableTextView;
                i = Theme.key_text_RedBold;
            } else if (isOutOwner || !z14 || z8) {
                this.beforeTableTextView.setVisibility(8);
                if (!isOutOwner || z4 || z11 || ((starGift4 instanceof TL_stars.TL_starGiftUnique) && starGift4.owner_id != j3)) {
                    this.afterTableTextView.setVisibility(8);
                } else {
                    this.afterTableTextView.setVisibility(0);
                    final long j8 = j3;
                    this.afterTableTextView.setText(AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(z12 ? R.string.Gift2ProfileVisible2 : R.string.Gift2ProfileInvisible), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda33
                        @Override // java.lang.Runnable
                        public final void run() {
                            StarGiftSheet.this.lambda$set$29(j8);
                        }
                    }), true, AndroidUtilities.dp(2.6666667f), AndroidUtilities.dp(0.66f)));
                }
                if (this.firstSet) {
                    switchPage(false, false);
                    this.firstSet = false;
                }
            } else {
                this.beforeTableTextView.setVisibility(0);
                this.beforeTableTextView.setText(LocaleController.getString(R.string.Gift2SenderHidden));
                linksTextView = this.beforeTableTextView;
                i = Theme.key_dialogTextGray2;
            }
            linksTextView.setTextColor(Theme.getColor(i, this.resourcesProvider));
            if (isOutOwner) {
            }
            this.afterTableTextView.setVisibility(8);
            if (this.firstSet) {
            }
        }
        return this;
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x03a6  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x026d  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0498  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x02e5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x031f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x036b  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x038f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public StarGiftSheet set(boolean z, TL_stars.UserStarGift userStarGift) {
        boolean z2;
        boolean z3;
        TopView topView;
        String string;
        CharSequence charSequence;
        CharSequence formatString;
        TopView topView2;
        String str;
        long j;
        long abs;
        String formatString2;
        char c;
        CharSequence charSequence2;
        CharSequence concat;
        CharSequence charSequence3;
        final long j2;
        TL_stars.StarGift starGift;
        long j3;
        TLRPC.TL_textWithEntities tL_textWithEntities;
        ButtonWithCounterView buttonWithCounterView;
        View.OnClickListener onClickListener;
        LinkSpanDrawable.LinksTextView linksTextView;
        int i;
        boolean z4;
        if (userStarGift == null) {
            return this;
        }
        this.myProfile = z;
        this.userStarGift = userStarGift;
        this.messageObject = null;
        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
        boolean isBot = UserObject.isBot(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(userStarGift.from_id)));
        int currentTime = MessagesController.getInstance(this.currentAccount).stargiftsConvertPeriodMax - (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - userStarGift.date);
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        int i2 = userStarGift.flags;
        long j4 = ((i2 & 2) == 0 || userStarGift.name_hidden) ? 2666000L : userStarGift.from_id;
        boolean z5 = userStarGift.refunded;
        TL_stars.StarGift starGift2 = userStarGift.gift;
        if (starGift2 instanceof TL_stars.TL_starGiftUnique) {
            set((TL_stars.TL_starGiftUnique) starGift2, (i2 & 256) != 0, z5);
            j3 = j4;
            z2 = false;
        } else {
            z2 = clientUserId == j4;
            this.topView.setGift(starGift2, getLink() != null);
            this.tableView.clear();
            if (z2) {
                topView = this.topView;
                string = LocaleController.getString(R.string.Gift2TitleSaved);
                if (z5) {
                    z3 = z5;
                    topView2 = topView;
                    str = string;
                    charSequence = "";
                    charSequence3 = null;
                    topView2.setText(0, str, 0L, charSequence3);
                    j = j4;
                    if (clientUserId == j) {
                        j2 = j;
                        this.tableView.addRowUser(LocaleController.getString(R.string.Gift2From), this.currentAccount, j2, new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda17
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarGiftSheet.this.lambda$set$16(j2);
                            }
                        }, (j2 == clientUserId || j2 == UserObject.ANONYMOUS || isBot || UserObject.isDeleted(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j)))) ? null : LocaleController.getString(R.string.Gift2ButtonSendGift), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda18
                            @Override // java.lang.Runnable
                            public final void run() {
                                StarGiftSheet.this.lambda$set$17(j2);
                            }
                        });
                    } else {
                        j2 = j;
                    }
                    long j5 = j2;
                    this.tableView.addRow(LocaleController.getString(R.string.StarsTransactionDate), LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(userStarGift.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(userStarGift.date * 1000))));
                    this.tableView.addRow(LocaleController.getString(R.string.Gift2Value), StarsIntroActivity.replaceStarsWithPlain(TextUtils.concat("⭐️ " + LocaleController.formatNumber(userStarGift.gift.stars + userStarGift.upgrade_stars, ','), " ", (canConvert() || z3) ? charSequence : ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) userStarGift.convert_stars), new StarGiftSheet$$ExternalSyntheticLambda19(this), this.resourcesProvider)), 0.8f));
                    starGift = userStarGift.gift;
                    if (starGift.limited && !z3) {
                        StarsIntroActivity.addAvailabilityRow(this.tableView, this.currentAccount, starGift, this.resourcesProvider);
                    }
                    if (z) {
                        j3 = j5;
                    } else {
                        int i3 = userStarGift.flags;
                        j3 = j5;
                        if ((i3 & 8) != 0 && (i3 & 2) != 0 && !z3) {
                            this.tableView.addRow(LocaleController.getString(R.string.Gift2Visibility), LocaleController.getString(!userStarGift.unsaved ? R.string.Gift2Visible : R.string.Gift2Invisible), LocaleController.getString(!userStarGift.unsaved ? R.string.Gift2VisibleHide : R.string.Gift2InvisibleShow), new StarGiftSheet$$ExternalSyntheticLambda20(this));
                        }
                    }
                    if (!z3 && userStarGift.can_upgrade) {
                        this.tableView.addRow(LocaleController.getString(R.string.Gift2Status), LocaleController.getString(R.string.Gift2StatusNonUnique), LocaleController.getString(R.string.Gift2StatusUpgrade), new StarGiftSheet$$ExternalSyntheticLambda21(this));
                    }
                    tL_textWithEntities = userStarGift.message;
                    if (tL_textWithEntities != null && !TextUtils.isEmpty(tL_textWithEntities.text) && !z3) {
                        TableView tableView = this.tableView;
                        TLRPC.TL_textWithEntities tL_textWithEntities2 = userStarGift.message;
                        tableView.addFullRow(tL_textWithEntities2.text, tL_textWithEntities2.entities);
                    }
                    if (z || !userStarGift.can_upgrade || userStarGift.upgrade_stars <= 0) {
                        this.button.setText(LocaleController.getString(R.string.OK), true);
                        buttonWithCounterView = this.button;
                        onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda23
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                StarGiftSheet.this.lambda$set$19(view);
                            }
                        };
                    } else {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.Gift2UpgradeButtonFree));
                        spannableStringBuilder.append((CharSequence) " ^");
                        if (this.upgradeIconSpan == null) {
                            this.upgradeIconSpan = new ColoredImageSpan(new UpgradeIcon(this.button, Theme.getColor(Theme.key_featuredStickers_addButton, this.resourcesProvider)));
                        }
                        spannableStringBuilder.setSpan(this.upgradeIconSpan, spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 33);
                        this.button.setText(spannableStringBuilder, true);
                        buttonWithCounterView = this.button;
                        onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda22
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                StarGiftSheet.this.lambda$set$18(view);
                            }
                        };
                    }
                    buttonWithCounterView.setOnClickListener(onClickListener);
                } else {
                    if (userStarGift.can_upgrade) {
                        formatString = AndroidUtilities.replaceTags(LocaleController.getString(R.string.Gift2SelfInfoUpgrade));
                        z3 = z5;
                    } else {
                        z3 = z5;
                        long j6 = userStarGift.convert_stars;
                        formatString = AndroidUtilities.replaceTags(j6 > 0 ? LocaleController.formatPluralStringComma("Gift2SelfInfoConvert", (int) j6) : LocaleController.getString(R.string.Gift2SelfInfo));
                    }
                    topView2 = topView;
                    str = string;
                    charSequence = "";
                    charSequence3 = formatString;
                    topView2.setText(0, str, 0L, charSequence3);
                    j = j4;
                    if (clientUserId == j) {
                    }
                    long j52 = j2;
                    this.tableView.addRow(LocaleController.getString(R.string.StarsTransactionDate), LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(userStarGift.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(userStarGift.date * 1000))));
                    this.tableView.addRow(LocaleController.getString(R.string.Gift2Value), StarsIntroActivity.replaceStarsWithPlain(TextUtils.concat("⭐️ " + LocaleController.formatNumber(userStarGift.gift.stars + userStarGift.upgrade_stars, ','), " ", (canConvert() || z3) ? charSequence : ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) userStarGift.convert_stars), new StarGiftSheet$$ExternalSyntheticLambda19(this), this.resourcesProvider)), 0.8f));
                    starGift = userStarGift.gift;
                    if (starGift.limited) {
                        StarsIntroActivity.addAvailabilityRow(this.tableView, this.currentAccount, starGift, this.resourcesProvider);
                    }
                    if (z) {
                    }
                    if (!z3) {
                        this.tableView.addRow(LocaleController.getString(R.string.Gift2Status), LocaleController.getString(R.string.Gift2StatusNonUnique), LocaleController.getString(R.string.Gift2StatusUpgrade), new StarGiftSheet$$ExternalSyntheticLambda21(this));
                    }
                    tL_textWithEntities = userStarGift.message;
                    if (tL_textWithEntities != null) {
                        TableView tableView2 = this.tableView;
                        TLRPC.TL_textWithEntities tL_textWithEntities22 = userStarGift.message;
                        tableView2.addFullRow(tL_textWithEntities22.text, tL_textWithEntities22.entities);
                    }
                    if (z) {
                    }
                    this.button.setText(LocaleController.getString(R.string.OK), true);
                    buttonWithCounterView = this.button;
                    onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda23
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarGiftSheet.this.lambda$set$19(view);
                        }
                    };
                    buttonWithCounterView.setOnClickListener(onClickListener);
                }
            } else {
                z3 = z5;
                if ((!z || userStarGift.can_upgrade) && userStarGift.upgrade_stars > 0) {
                    topView = this.topView;
                    string = LocaleController.getString(z ? R.string.Gift2TitleReceived : R.string.Gift2TitleProfile);
                    if (!z3) {
                        if (z) {
                            formatString = LocaleController.getString(R.string.Gift2InfoInFreeUpgrade);
                            topView2 = topView;
                            str = string;
                            charSequence = "";
                            charSequence3 = formatString;
                            topView2.setText(0, str, 0L, charSequence3);
                            j = j4;
                            if (clientUserId == j) {
                            }
                            long j522 = j2;
                            this.tableView.addRow(LocaleController.getString(R.string.StarsTransactionDate), LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(userStarGift.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(userStarGift.date * 1000))));
                            this.tableView.addRow(LocaleController.getString(R.string.Gift2Value), StarsIntroActivity.replaceStarsWithPlain(TextUtils.concat("⭐️ " + LocaleController.formatNumber(userStarGift.gift.stars + userStarGift.upgrade_stars, ','), " ", (canConvert() || z3) ? charSequence : ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) userStarGift.convert_stars), new StarGiftSheet$$ExternalSyntheticLambda19(this), this.resourcesProvider)), 0.8f));
                            starGift = userStarGift.gift;
                            if (starGift.limited) {
                            }
                            if (z) {
                            }
                            if (!z3) {
                            }
                            tL_textWithEntities = userStarGift.message;
                            if (tL_textWithEntities != null) {
                            }
                            if (z) {
                            }
                            this.button.setText(LocaleController.getString(R.string.OK), true);
                            buttonWithCounterView = this.button;
                            onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda23
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    StarGiftSheet.this.lambda$set$19(view);
                                }
                            };
                            buttonWithCounterView.setOnClickListener(onClickListener);
                        } else {
                            charSequence = "";
                            formatString = LocaleController.formatString(R.string.Gift2InfoFreeUpgrade, UserObject.getForcedFirstName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId))));
                            topView2 = topView;
                            str = string;
                            charSequence3 = formatString;
                            topView2.setText(0, str, 0L, charSequence3);
                            j = j4;
                            if (clientUserId == j) {
                            }
                            long j5222 = j2;
                            this.tableView.addRow(LocaleController.getString(R.string.StarsTransactionDate), LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(userStarGift.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(userStarGift.date * 1000))));
                            this.tableView.addRow(LocaleController.getString(R.string.Gift2Value), StarsIntroActivity.replaceStarsWithPlain(TextUtils.concat("⭐️ " + LocaleController.formatNumber(userStarGift.gift.stars + userStarGift.upgrade_stars, ','), " ", (canConvert() || z3) ? charSequence : ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) userStarGift.convert_stars), new StarGiftSheet$$ExternalSyntheticLambda19(this), this.resourcesProvider)), 0.8f));
                            starGift = userStarGift.gift;
                            if (starGift.limited) {
                            }
                            if (z) {
                            }
                            if (!z3) {
                            }
                            tL_textWithEntities = userStarGift.message;
                            if (tL_textWithEntities != null) {
                            }
                            if (z) {
                            }
                            this.button.setText(LocaleController.getString(R.string.OK), true);
                            buttonWithCounterView = this.button;
                            onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda23
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    StarGiftSheet.this.lambda$set$19(view);
                                }
                            };
                            buttonWithCounterView.setOnClickListener(onClickListener);
                        }
                    }
                    topView2 = topView;
                    str = string;
                    charSequence = "";
                    charSequence3 = null;
                    topView2.setText(0, str, 0L, charSequence3);
                    j = j4;
                    if (clientUserId == j) {
                    }
                    long j52222 = j2;
                    this.tableView.addRow(LocaleController.getString(R.string.StarsTransactionDate), LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(userStarGift.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(userStarGift.date * 1000))));
                    this.tableView.addRow(LocaleController.getString(R.string.Gift2Value), StarsIntroActivity.replaceStarsWithPlain(TextUtils.concat("⭐️ " + LocaleController.formatNumber(userStarGift.gift.stars + userStarGift.upgrade_stars, ','), " ", (canConvert() || z3) ? charSequence : ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) userStarGift.convert_stars), new StarGiftSheet$$ExternalSyntheticLambda19(this), this.resourcesProvider)), 0.8f));
                    starGift = userStarGift.gift;
                    if (starGift.limited) {
                    }
                    if (z) {
                    }
                    if (!z3) {
                    }
                    tL_textWithEntities = userStarGift.message;
                    if (tL_textWithEntities != null) {
                    }
                    if (z) {
                    }
                    this.button.setText(LocaleController.getString(R.string.OK), true);
                    buttonWithCounterView = this.button;
                    onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda23
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarGiftSheet.this.lambda$set$19(view);
                        }
                    };
                    buttonWithCounterView.setOnClickListener(onClickListener);
                } else {
                    charSequence = "";
                    TopView topView3 = this.topView;
                    String string2 = LocaleController.getString(clientUserId == j4 ? R.string.Gift2TitleSaved : z ? R.string.Gift2TitleReceived : R.string.Gift2TitleProfile);
                    if (z3) {
                        j = j4;
                        abs = 0;
                    } else {
                        j = j4;
                        abs = Math.abs(Math.max(userStarGift.gift.convert_stars, userStarGift.convert_stars));
                    }
                    if (z3) {
                        concat = null;
                    } else {
                        if (isBot || !canConvert()) {
                            if (z) {
                                formatString2 = LocaleController.getString(userStarGift.unsaved ? R.string.Gift2Info2BotKeep : R.string.Gift2Info2BotRemove);
                            } else {
                                formatString2 = LocaleController.formatString((!userStarGift.can_upgrade || userStarGift.upgrade_stars <= 0) ? R.string.Gift2Info2OutExpired : R.string.Gift2Info2OutUpgrade, UserObject.getForcedFirstName(user));
                            }
                        } else if (z) {
                            formatString2 = LocaleController.formatPluralStringComma(currentTime <= 0 ? "Gift2Info2Expired" : "Gift2Info3", (int) userStarGift.convert_stars);
                        } else {
                            formatString2 = LocaleController.formatPluralStringComma("Gift2Info2Out", (int) userStarGift.convert_stars, UserObject.getForcedFirstName(user));
                        }
                        SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(formatString2);
                        if (isBot || !canConvert()) {
                            c = 1;
                            charSequence2 = charSequence;
                        } else {
                            c = 1;
                            charSequence2 = AndroidUtilities.replaceArrows(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.Gift2More).replace(' ', (char) 160), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda16
                                @Override // java.lang.Runnable
                                public final void run() {
                                    StarGiftSheet.this.lambda$set$15();
                                }
                            }), true);
                        }
                        CharSequence[] charSequenceArr = new CharSequence[3];
                        charSequenceArr[0] = replaceTags;
                        charSequenceArr[c] = " ";
                        charSequenceArr[2] = charSequence2;
                        concat = TextUtils.concat(charSequenceArr);
                    }
                    topView3.setText(0, string2, abs, concat);
                    if (clientUserId == j) {
                    }
                    long j522222 = j2;
                    this.tableView.addRow(LocaleController.getString(R.string.StarsTransactionDate), LocaleController.formatString(R.string.formatDateAtTime, LocaleController.getInstance().getFormatterGiveawayCard().format(new Date(userStarGift.date * 1000)), LocaleController.getInstance().getFormatterDay().format(new Date(userStarGift.date * 1000))));
                    this.tableView.addRow(LocaleController.getString(R.string.Gift2Value), StarsIntroActivity.replaceStarsWithPlain(TextUtils.concat("⭐️ " + LocaleController.formatNumber(userStarGift.gift.stars + userStarGift.upgrade_stars, ','), " ", (canConvert() || z3) ? charSequence : ButtonSpan.make(LocaleController.formatPluralStringComma("Gift2ButtonSell", (int) userStarGift.convert_stars), new StarGiftSheet$$ExternalSyntheticLambda19(this), this.resourcesProvider)), 0.8f));
                    starGift = userStarGift.gift;
                    if (starGift.limited) {
                    }
                    if (z) {
                    }
                    if (!z3) {
                    }
                    tL_textWithEntities = userStarGift.message;
                    if (tL_textWithEntities != null) {
                    }
                    if (z) {
                    }
                    this.button.setText(LocaleController.getString(R.string.OK), true);
                    buttonWithCounterView = this.button;
                    onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda23
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            StarGiftSheet.this.lambda$set$19(view);
                        }
                    };
                    buttonWithCounterView.setOnClickListener(onClickListener);
                }
            }
        }
        if (userStarGift.refunded) {
            this.beforeTableTextView.setVisibility(0);
            this.beforeTableTextView.setText(LocaleController.getString(R.string.Gift2Refunded));
            linksTextView = this.beforeTableTextView;
            i = Theme.key_text_RedBold;
        } else {
            if (!z || !(userStarGift.gift instanceof TL_stars.TL_starGift) || !userStarGift.name_hidden || z2 || j3 == UserObject.ANONYMOUS) {
                this.beforeTableTextView.setVisibility(8);
                if (z || this.dialogId != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                    z4 = false;
                    this.afterTableTextView.setVisibility(8);
                } else {
                    this.afterTableTextView.setText(LocaleController.getString(!userStarGift.unsaved ? R.string.Gift2ProfileVisible : R.string.Gift2ProfileInvisible));
                    z4 = false;
                    this.afterTableTextView.setVisibility(0);
                }
                if (this.firstSet) {
                    switchPage(z4, z4);
                    this.firstSet = z4;
                }
                return this;
            }
            this.beforeTableTextView.setVisibility(0);
            this.beforeTableTextView.setText(LocaleController.getString(R.string.Gift2SenderHidden));
            linksTextView = this.beforeTableTextView;
            i = Theme.key_dialogTextGray2;
        }
        linksTextView.setTextColor(Theme.getColor(i, this.resourcesProvider));
        if (z) {
        }
        z4 = false;
        this.afterTableTextView.setVisibility(8);
        if (this.firstSet) {
        }
        return this;
    }

    public void set(final TL_stars.TL_starGiftUnique tL_starGiftUnique, boolean z, boolean z2) {
        ButtonWithCounterView buttonWithCounterView;
        View.OnClickListener onClickListener;
        SpannableString spannableString;
        boolean z3;
        TableView tableView;
        CharSequence formatSpannable;
        TableView tableView2;
        CharSequence formatSpannable2;
        TableView.TableRowFullContent addFullRow;
        TableView tableView3;
        String string;
        StringBuilder sb;
        this.topView.setGift(tL_starGiftUnique, getLink() != null);
        this.topView.setText(0, tL_starGiftUnique.title, 0L, LocaleController.formatPluralStringComma("Gift2CollectionNumber", tL_starGiftUnique.num));
        this.tableView.clear();
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (!z2) {
            long j = tL_starGiftUnique.owner_id;
            if (j == 0 && tL_starGiftUnique.owner_name != null) {
                this.tableView.addRow(LocaleController.getString(R.string.Gift2Owner), tL_starGiftUnique.owner_name);
            } else if (j != 0 && j == clientUserId && z) {
                this.tableView.addRowUser(LocaleController.getString(R.string.Gift2Owner), this.currentAccount, tL_starGiftUnique.owner_id, new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda10
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarGiftSheet.this.lambda$set$9(tL_starGiftUnique);
                    }
                }, LocaleController.getString(R.string.Gift2OwnerChange), new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarGiftSheet.this.lambda$onMenuPressed$3();
                    }
                });
            } else if (j != 0) {
                this.tableView.addRowUser(LocaleController.getString(R.string.Gift2Owner), this.currentAccount, tL_starGiftUnique.owner_id, new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda12
                    @Override // java.lang.Runnable
                    public final void run() {
                        StarGiftSheet.this.lambda$set$10(tL_starGiftUnique);
                    }
                });
            }
        }
        addAttributeRow(StarsController.findAttribute(tL_starGiftUnique.attributes, TL_stars.starGiftAttributeModel.class));
        addAttributeRow(StarsController.findAttribute(tL_starGiftUnique.attributes, TL_stars.starGiftAttributeBackdrop.class));
        addAttributeRow(StarsController.findAttribute(tL_starGiftUnique.attributes, TL_stars.starGiftAttributePattern.class));
        if (!z2) {
            if (this.messageObject == null) {
                tableView3 = this.tableView;
                string = LocaleController.getString(R.string.Gift2Quantity);
                sb = new StringBuilder();
            } else if (this.messageObjectRepolled) {
                tableView3 = this.tableView;
                string = LocaleController.getString(R.string.Gift2Quantity);
                sb = new StringBuilder();
            } else {
                TextView textView = (TextView) ((TableView.TableRowContent) this.tableView.addRow(LocaleController.getString(R.string.Gift2Quantity), "").getChildAt(1)).getChildAt(0);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("x ");
                LoadingSpan loadingSpan = new LoadingSpan(textView, AndroidUtilities.dp(90.0f), 0, this.resourcesProvider);
                int i = Theme.key_windowBackgroundWhiteBlackText;
                loadingSpan.setColors(Theme.multAlpha(Theme.getColor(i, this.resourcesProvider), 0.21f), Theme.multAlpha(Theme.getColor(i, this.resourcesProvider), 0.08f));
                spannableStringBuilder.setSpan(loadingSpan, 0, 1, 33);
                textView.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
                if (!this.messageObjectRepolling) {
                    final int id = this.messageObject.getId();
                    this.messageObjectRepolling = true;
                    TLRPC.TL_messages_getMessages tL_messages_getMessages = new TLRPC.TL_messages_getMessages();
                    tL_messages_getMessages.id.add(Integer.valueOf(id));
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getMessages, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda13
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            StarGiftSheet.this.lambda$set$12(id, tLObject, tL_error);
                        }
                    });
                }
            }
            sb.append(LocaleController.formatPluralStringComma("Gift2QuantityIssued1", tL_starGiftUnique.availability_issued));
            sb.append(LocaleController.formatPluralStringComma("Gift2QuantityIssued2", tL_starGiftUnique.availability_total));
            tableView3.addRow(string, sb.toString());
        }
        final TL_stars.starGiftAttributeOriginalDetails stargiftattributeoriginaldetails = (TL_stars.starGiftAttributeOriginalDetails) StarsController.findAttribute(tL_starGiftUnique.attributes, TL_stars.starGiftAttributeOriginalDetails.class);
        if (stargiftattributeoriginaldetails != null) {
            Spannable spannable = null;
            if ((stargiftattributeoriginaldetails.flags & 1) != 0) {
                spannableString = new SpannableString(UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(stargiftattributeoriginaldetails.sender_id))));
                spannableString.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarGiftSheet.4
                    @Override // android.text.style.ClickableSpan
                    public void onClick(View view) {
                        StarGiftSheet.this.lambda$set$23(stargiftattributeoriginaldetails.sender_id);
                    }

                    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                    public void updateDrawState(TextPaint textPaint) {
                        textPaint.setColor(textPaint.linkColor);
                    }
                }, 0, spannableString.length(), 33);
            } else {
                spannableString = null;
            }
            SpannableString spannableString2 = new SpannableString(UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(stargiftattributeoriginaldetails.recipient_id))));
            spannableString2.setSpan(new ClickableSpan() { // from class: org.telegram.ui.Stars.StarGiftSheet.5
                @Override // android.text.style.ClickableSpan
                public void onClick(View view) {
                    StarGiftSheet.this.lambda$set$23(stargiftattributeoriginaldetails.recipient_id);
                }

                @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                public void updateDrawState(TextPaint textPaint) {
                    textPaint.setColor(textPaint.linkColor);
                }
            }, 0, spannableString2.length(), 33);
            if (stargiftattributeoriginaldetails.message != null) {
                TextPaint textPaint = new TextPaint(1);
                textPaint.setTextSize(AndroidUtilities.dp(14.0f));
                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(stargiftattributeoriginaldetails.message.text);
                MessageObject.addEntitiesToText(spannableStringBuilder2, stargiftattributeoriginaldetails.message.entities, false, false, false, false);
                spannable = MessageObject.replaceAnimatedEmoji(Emoji.replaceEmoji(spannableStringBuilder2, textPaint.getFontMetricsInt(), false), stargiftattributeoriginaldetails.message.entities, textPaint.getFontMetricsInt());
            }
            String replaceAll = LocaleController.getInstance().getFormatterYear().format(stargiftattributeoriginaldetails.date * 1000).replaceAll("\\.", "/");
            if (stargiftattributeoriginaldetails.sender_id != stargiftattributeoriginaldetails.recipient_id) {
                z3 = true;
                if (spannableString != null) {
                    tableView = this.tableView;
                    formatSpannable = spannable == null ? LocaleController.formatSpannable(R.string.Gift2AttributeOriginalDetails, spannableString, spannableString2, replaceAll) : LocaleController.formatSpannable(R.string.Gift2AttributeOriginalDetailsComment, spannableString, spannableString2, replaceAll, spannable);
                } else if (spannable == null) {
                    tableView2 = this.tableView;
                    formatSpannable2 = LocaleController.formatSpannable(R.string.Gift2AttributeOriginalDetailsNoSender, spannableString2, replaceAll);
                    addFullRow = tableView2.addFullRow(formatSpannable2);
                } else {
                    tableView = this.tableView;
                    formatSpannable = LocaleController.formatSpannable(R.string.Gift2AttributeOriginalDetailsNoSenderComment, spannableString2, replaceAll, spannable);
                }
                addFullRow = tableView.addFullRow(formatSpannable);
            } else if (spannable == null) {
                tableView2 = this.tableView;
                z3 = true;
                formatSpannable2 = LocaleController.formatSpannable(R.string.Gift2AttributeOriginalDetailsSelf, spannableString, replaceAll);
                addFullRow = tableView2.addFullRow(formatSpannable2);
            } else {
                z3 = true;
                tableView = this.tableView;
                formatSpannable = LocaleController.formatSpannable(R.string.Gift2AttributeOriginalDetailsSelfComment, spannableString, replaceAll, spannable);
                addFullRow = tableView.addFullRow(formatSpannable);
            }
            addFullRow.setFilled(z3);
            ((SpoilersTextView) addFullRow.getChildAt(0)).setGravity(17);
        }
        if (z2 || tL_starGiftUnique.owner_id != clientUserId) {
            this.button.setText(LocaleController.getString(R.string.OK), true);
            buttonWithCounterView = this.button;
            onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda15
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StarGiftSheet.this.lambda$set$14(view);
                }
            };
        } else {
            this.button.setText(LocaleController.getString(isSaved() ? R.string.Gift2ProfileMakeInvisible : R.string.Gift2ProfileMakeVisible), true);
            buttonWithCounterView = this.button;
            onClickListener = new View.OnClickListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda14
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    StarGiftSheet.this.lambda$set$13(view);
                }
            };
        }
        buttonWithCounterView.setOnClickListener(onClickListener);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        MessageObject messageObject;
        TLRPC.Message message;
        if (this.slug != null && this.slugStarGift == null) {
            final AlertDialog alertDialog = new AlertDialog(getContext(), 3);
            alertDialog.showDelayed(500L);
            TL_stars.getUniqueStarGift getuniquestargift = new TL_stars.getUniqueStarGift();
            getuniquestargift.slug = this.slug;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(getuniquestargift, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda24
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    StarGiftSheet.this.lambda$show$41(alertDialog, tLObject, tL_error);
                }
            });
        } else if (this.userStarGift == null && (messageObject = this.messageObject) != null && (message = messageObject.messageOwner) != null) {
            TLRPC.MessageAction messageAction = message.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGift) {
                final TLRPC.TL_messageActionStarGift tL_messageActionStarGift = (TLRPC.TL_messageActionStarGift) messageAction;
                if (tL_messageActionStarGift.upgraded) {
                    final AlertDialog alertDialog2 = new AlertDialog(getContext(), 3);
                    alertDialog2.showDelayed(500L);
                    TLRPC.TL_messages_getMessages tL_messages_getMessages = new TLRPC.TL_messages_getMessages();
                    tL_messages_getMessages.id.add(Integer.valueOf(tL_messageActionStarGift.upgrade_msg_id));
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_getMessages, new RequestDelegate() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda25
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                            StarGiftSheet.this.lambda$show$44(tL_messageActionStarGift, alertDialog2, tLObject, tL_error);
                        }
                    });
                    return;
                }
            }
        }
        super.show();
    }

    public void showHint(int i, TextView textView) {
        Layout layout;
        HintView2 hintView2 = this.currentHintView;
        if ((hintView2 != null && hintView2.shown() && this.currentHintViewTextView == textView) || textView == null || (layout = textView.getLayout()) == null) {
            return;
        }
        CharSequence text = layout.getText();
        if (text instanceof Spanned) {
            Spanned spanned = (Spanned) text;
            ButtonSpan[] buttonSpanArr = (ButtonSpan[]) spanned.getSpans(0, spanned.length(), ButtonSpan.class);
            if (buttonSpanArr == null || buttonSpanArr.length <= 0) {
                return;
            }
            float paddingLeft = textView.getPaddingLeft() + layout.getPrimaryHorizontal(spanned.getSpanStart(buttonSpanArr[buttonSpanArr.length - 1])) + (r4.getSize() / 2.0f);
            int[] iArr = new int[2];
            int[] iArr2 = new int[2];
            textView.getLocationOnScreen(iArr);
            this.container.getLocationOnScreen(iArr2);
            iArr[0] = iArr[0] - iArr2[0];
            iArr[1] = iArr[1] - iArr2[1];
            HintView2 hintView22 = this.currentHintView;
            if (hintView22 != null) {
                hintView22.hide();
                this.currentHintView = null;
            }
            final HintView2 hintView23 = new HintView2(getContext(), 3);
            hintView23.setMultilineText(true);
            hintView23.setText(LocaleController.formatString(R.string.Gift2RarityHint, AffiliateProgramFragment.percents(i)));
            hintView23.setJointPx(0.0f, (iArr[0] + paddingLeft) - (AndroidUtilities.dp(16.0f) + this.backgroundPaddingLeft));
            hintView23.setTranslationY(((iArr[1] - AndroidUtilities.dp(100.0f)) - (textView.getHeight() / 2.0f)) + AndroidUtilities.dp(4.33f));
            hintView23.setDuration(3000L);
            hintView23.setPadding(AndroidUtilities.dp(16.0f) + this.backgroundPaddingLeft, 0, AndroidUtilities.dp(16.0f) + this.backgroundPaddingLeft, 0);
            hintView23.setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda58
                @Override // java.lang.Runnable
                public final void run() {
                    AndroidUtilities.removeFromParent(HintView2.this);
                }
            });
            hintView23.show();
            this.container.addView(hintView23, LayoutHelper.createFrame(-1, 100.0f));
            this.currentHintView = hintView23;
            this.currentHintViewTextView = textView;
        }
    }

    public void switchPage(final boolean z, boolean z2) {
        ValueAnimator valueAnimator = this.switchingPagesAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.switchingPagesAnimator = null;
        }
        if (!z) {
            AndroidUtilities.cancelRunOnUIThread(this.topView.checkToRotateRunnable);
        }
        if (z2) {
            this.infoLayout.setVisibility(0);
            this.upgradeLayout.setVisibility(0);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.currentPage, z ? 1.0f : 0.0f);
            this.switchingPagesAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Stars.StarGiftSheet$$ExternalSyntheticLambda30
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    StarGiftSheet.this.lambda$switchPage$7(valueAnimator2);
                }
            });
            this.switchingPagesAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Stars.StarGiftSheet.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    StarGiftSheet.this.onSwitchedPage(z ? 1.0f : 0.0f);
                    StarGiftSheet.this.infoLayout.setVisibility(z ? 8 : 0);
                    StarGiftSheet.this.upgradeLayout.setVisibility(z ? 0 : 8);
                    StarGiftSheet.this.switchingPagesAnimator = null;
                }
            });
            this.switchingPagesAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.switchingPagesAnimator.setDuration(320L);
            this.switchingPagesAnimator.start();
            this.topView.prepareSwitchPage(Math.round(this.currentPage), z ? 1 : 0);
        } else {
            onSwitchedPage(z ? 1.0f : 0.0f);
            this.infoLayout.setVisibility(z ? 8 : 0);
            this.upgradeLayout.setVisibility(z ? 0 : 8);
        }
        HintView2 hintView2 = this.currentHintView;
        if (hintView2 != null) {
            hintView2.hide();
            this.currentHintView = null;
        }
    }
}
