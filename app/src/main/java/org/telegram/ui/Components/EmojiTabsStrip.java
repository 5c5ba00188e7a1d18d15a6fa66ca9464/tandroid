package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.EmojiTabsStrip;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.Premium.PremiumLockIconView;
/* loaded from: classes3.dex */
public class EmojiTabsStrip extends ScrollableHorizontalScrollView {
    private ValueAnimator appearAnimation;
    private int appearCount;
    private LinearLayout contentView;
    private EmojiTabsView emojiTabs;
    private ArrayList<EmojiTabButton> emojipackTabs;
    private boolean includeAnimated;
    private Runnable onSettingsOpenRunnable;
    private EmojiView parent;
    private EmojiTabButton recentTab;
    private Theme.ResourcesProvider resourcesProvider;
    private ValueAnimator selectAnimator;
    private EmojiTabButton settingsTab;
    private boolean wasDrawn;
    private static int[] emojiTabsDrawableIds = {R.drawable.msg_emoji_smiles, R.drawable.msg_emoji_cat, R.drawable.msg_emoji_food, R.drawable.msg_emoji_activities, R.drawable.msg_emoji_travel, R.drawable.msg_emoji_objects, R.drawable.msg_emoji_other, R.drawable.msg_emoji_flags};
    private static int[] emojiTabsAnimatedDrawableIds = {R.raw.msg_emoji_smiles, R.raw.msg_emoji_cat, R.raw.msg_emoji_food, R.raw.msg_emoji_activities, R.raw.msg_emoji_travel, R.raw.msg_emoji_objects, R.raw.msg_emoji_other, R.raw.msg_emoji_flags};
    private int recentDrawableId = R.drawable.msg_emoji_recent;
    private int settingsDrawableId = R.drawable.smiles_tab_settings;
    private HashMap<View, android.graphics.Rect> removingViews = new HashMap<>();
    private float selectT = 0.0f;
    private float selectAnimationT = 0.0f;
    private int selected = 0;
    private int wasIndex = 0;
    private boolean recentFirstChange = true;
    private boolean recentIsShown = true;
    boolean first = true;

    protected boolean onTabClick(int i) {
        throw null;
    }

    public EmojiTabsStrip(Context context, Theme.ResourcesProvider resourcesProvider, final boolean z, EmojiView emojiView, Runnable runnable) {
        super(context);
        this.parent = emojiView;
        this.includeAnimated = z;
        this.resourcesProvider = resourcesProvider;
        this.onSettingsOpenRunnable = runnable;
        LinearLayout linearLayout = new LinearLayout(context) { // from class: org.telegram.ui.Components.EmojiTabsStrip.1
            HashMap<Integer, Integer> lastX = new HashMap<>();
            private Paint paint = new Paint(1);
            private RectF from = new RectF();
            private RectF to = new RectF();
            private RectF rect = new RectF();
            private Path path = new Path();

            @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i, int i2, int i3, int i4) {
                int i5 = (i4 - i2) / 2;
                int i6 = 0;
                if (z) {
                    int paddingLeft = getPaddingLeft() - (!EmojiTabsStrip.this.recentIsShown ? AndroidUtilities.dp(33.0f) : 0);
                    for (int i7 = 0; i7 < getChildCount(); i7++) {
                        View childAt = getChildAt(i7);
                        if (childAt != EmojiTabsStrip.this.settingsTab && !EmojiTabsStrip.this.removingViews.containsKey(childAt) && childAt != null) {
                            childAt.layout(paddingLeft, i5 - (childAt.getMeasuredHeight() / 2), childAt.getMeasuredWidth() + paddingLeft, (childAt.getMeasuredHeight() / 2) + i5);
                            boolean z3 = childAt instanceof EmojiTabButton;
                            Integer valueOf = z3 ? ((EmojiTabButton) childAt).id : childAt instanceof EmojiTabsView ? Integer.valueOf(((EmojiTabsView) childAt).id) : null;
                            if (z3) {
                                EmojiTabButton emojiTabButton = (EmojiTabButton) childAt;
                                if (emojiTabButton.newly) {
                                    emojiTabButton.newly = false;
                                    childAt.setScaleX(0.0f);
                                    childAt.setScaleY(0.0f);
                                    childAt.setAlpha(0.0f);
                                    childAt.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
                                }
                            }
                            if (valueOf != null) {
                                if (this.lastX.get(valueOf) != null && this.lastX.get(valueOf).intValue() != paddingLeft) {
                                    childAt.setTranslationX(this.lastX.get(valueOf).intValue() - paddingLeft);
                                    childAt.animate().translationX(0.0f).setDuration(250L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
                                }
                                this.lastX.put(valueOf, Integer.valueOf(paddingLeft));
                            }
                            paddingLeft += childAt.getMeasuredWidth() + AndroidUtilities.dp(3.0f);
                        }
                    }
                    if (EmojiTabsStrip.this.settingsTab == null) {
                        return;
                    }
                    if (!EmojiTabsStrip.this.recentIsShown) {
                        i6 = AndroidUtilities.dp(33.0f);
                    }
                    int i8 = paddingLeft + i6;
                    Integer num = EmojiTabsStrip.this.settingsTab.id;
                    if (EmojiTabsStrip.this.settingsTab.getMeasuredWidth() + i8 + getPaddingRight() <= EmojiTabsStrip.this.getMeasuredWidth()) {
                        EmojiTabButton emojiTabButton2 = EmojiTabsStrip.this.settingsTab;
                        int i9 = i3 - i;
                        int paddingRight = (i9 - getPaddingRight()) - EmojiTabsStrip.this.settingsTab.getMeasuredWidth();
                        emojiTabButton2.layout(paddingRight, i5 - (EmojiTabsStrip.this.settingsTab.getMeasuredHeight() / 2), i9 - getPaddingRight(), i5 + (EmojiTabsStrip.this.settingsTab.getMeasuredHeight() / 2));
                        i8 = paddingRight;
                    } else {
                        EmojiTabsStrip.this.settingsTab.layout(i8, i5 - (EmojiTabsStrip.this.settingsTab.getMeasuredHeight() / 2), EmojiTabsStrip.this.settingsTab.getMeasuredWidth() + i8, i5 + (EmojiTabsStrip.this.settingsTab.getMeasuredHeight() / 2));
                    }
                    if (num == null) {
                        return;
                    }
                    if (this.lastX.get(num) != null && this.lastX.get(num).intValue() != i8) {
                        EmojiTabsStrip.this.settingsTab.setTranslationX(this.lastX.get(num).intValue() - i8);
                        EmojiTabsStrip.this.settingsTab.animate().translationX(0.0f).setDuration(350L).start();
                    }
                    this.lastX.put(num, Integer.valueOf(i8));
                    return;
                }
                int childCount = getChildCount() - (!EmojiTabsStrip.this.recentIsShown ? 1 : 0);
                int paddingLeft2 = (int) (((((i3 - i) - getPaddingLeft()) - getPaddingRight()) - (AndroidUtilities.dp(30.0f) * childCount)) / Math.max(1, childCount - 1));
                int paddingLeft3 = getPaddingLeft();
                while (i6 < childCount) {
                    View childAt2 = getChildAt((!EmojiTabsStrip.this.recentIsShown ? 1 : 0) + i6);
                    if (childAt2 != null) {
                        childAt2.layout(paddingLeft3, i5 - (childAt2.getMeasuredHeight() / 2), childAt2.getMeasuredWidth() + paddingLeft3, (childAt2.getMeasuredHeight() / 2) + i5);
                        paddingLeft3 += childAt2.getMeasuredWidth() + paddingLeft2;
                    }
                    i6++;
                }
            }

            @Override // android.widget.LinearLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(99999999, Integer.MIN_VALUE);
                int paddingLeft = (getPaddingLeft() + getPaddingRight()) - ((int) (EmojiTabsStrip.this.recentIsShown ? 0.0f : EmojiTabsStrip.this.recentTab.getAlpha() * AndroidUtilities.dp(33.0f)));
                for (int i3 = 0; i3 < getChildCount(); i3++) {
                    View childAt = getChildAt(i3);
                    if (childAt != null) {
                        childAt.measure(makeMeasureSpec, i2);
                        paddingLeft += childAt.getMeasuredWidth() + (i3 + 1 < getChildCount() ? AndroidUtilities.dp(3.0f) : 0);
                    }
                }
                if (!z) {
                    setMeasuredDimension(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
                } else {
                    setMeasuredDimension(Math.max(paddingLeft, View.MeasureSpec.getSize(i)), View.MeasureSpec.getSize(i2));
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                for (Map.Entry entry : EmojiTabsStrip.this.removingViews.entrySet()) {
                    View view = (View) entry.getKey();
                    android.graphics.Rect rect = (android.graphics.Rect) entry.getValue();
                    canvas.save();
                    canvas.translate(rect.left, rect.top);
                    canvas.scale(view.getScaleX(), view.getScaleY(), rect.width() / 2.0f, rect.height() / 2.0f);
                    view.draw(canvas);
                    canvas.restore();
                }
                int floor = (int) Math.floor(EmojiTabsStrip.this.selectT);
                int ceil = (int) Math.ceil(EmojiTabsStrip.this.selectT);
                getChildBounds(floor, this.from);
                getChildBounds(ceil, this.to);
                AndroidUtilities.lerp(this.from, this.to, EmojiTabsStrip.this.selectT - floor, this.rect);
                float f = 0.0f;
                if (EmojiTabsStrip.this.emojiTabs != null) {
                    f = MathUtils.clamp(1.0f - Math.abs(EmojiTabsStrip.this.selectT - 1.0f), 0.0f, 1.0f);
                }
                float f2 = EmojiTabsStrip.this.selectAnimationT * 4.0f * (1.0f - EmojiTabsStrip.this.selectAnimationT);
                float width = (this.rect.width() / 2.0f) * ((0.3f * f2) + 1.0f);
                float height = (this.rect.height() / 2.0f) * (1.0f - (f2 * 0.05f));
                RectF rectF = this.rect;
                rectF.set(rectF.centerX() - width, this.rect.centerY() - height, this.rect.centerX() + width, this.rect.centerY() + height);
                float dp = AndroidUtilities.dp(AndroidUtilities.lerp(8.0f, 16.0f, f));
                this.paint.setColor(EmojiTabsStrip.this.selectorColor());
                this.path.rewind();
                this.path.addRoundRect(this.rect, dp, dp, Path.Direction.CW);
                canvas.drawPath(this.path, this.paint);
                if (EmojiTabsStrip.this.emojiTabs != null) {
                    this.path.addCircle(EmojiTabsStrip.this.emojiTabs.getLeft() + AndroidUtilities.dp(15.0f), (EmojiTabsStrip.this.emojiTabs.getTop() + EmojiTabsStrip.this.emojiTabs.getBottom()) / 2.0f, AndroidUtilities.dp(15.0f), Path.Direction.CW);
                }
                super.dispatchDraw(canvas);
                EmojiTabsStrip.this.wasDrawn = true;
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                if (view == EmojiTabsStrip.this.emojiTabs) {
                    canvas.save();
                    canvas.clipPath(this.path);
                    boolean drawChild = super.drawChild(canvas, view, j);
                    canvas.restore();
                    return drawChild;
                }
                return super.drawChild(canvas, view, j);
            }

            private void getChildBounds(int i, RectF rectF) {
                View childAt = getChildAt(MathUtils.clamp(i, 0, getChildCount() - 1));
                rectF.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
            }
        };
        this.contentView = linearLayout;
        linearLayout.setClipToPadding(false);
        this.contentView.setPadding(AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(3.0f), 0);
        this.contentView.setOrientation(0);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        addView(this.contentView);
        LinearLayout linearLayout2 = this.contentView;
        EmojiTabButton emojiTabButton = new EmojiTabButton(context, this.recentDrawableId, false, false);
        this.recentTab = emojiTabButton;
        linearLayout2.addView(emojiTabButton);
        this.recentTab.id = -934918565;
        if (z) {
            LinearLayout linearLayout3 = this.contentView;
            EmojiTabsView emojiTabsView = new EmojiTabsView(context);
            this.emojiTabs = emojiTabsView;
            linearLayout3.addView(emojiTabsView);
            this.emojiTabs.id = 3552126;
            if (runnable != null) {
                LinearLayout linearLayout4 = this.contentView;
                EmojiTabButton emojiTabButton2 = new EmojiTabButton(context, this.settingsDrawableId, false, true);
                this.settingsTab = emojiTabButton2;
                linearLayout4.addView(emojiTabButton2);
                this.settingsTab.id = 1434631203;
                this.settingsTab.setAlpha(0.0f);
            }
            updateEmojiPacks();
            return;
        }
        int i = 0;
        while (true) {
            int[] iArr = emojiTabsDrawableIds;
            if (i < iArr.length) {
                this.contentView.addView(new EmojiTabButton(context, iArr[i], false, i == 0));
                i++;
            } else {
                updateClickListeners();
                return;
            }
        }
    }

    public void showRecent(boolean z) {
        if (this.recentIsShown == z) {
            return;
        }
        this.recentIsShown = z;
        float f = 1.0f;
        if (this.recentFirstChange) {
            EmojiTabButton emojiTabButton = this.recentTab;
            if (!z) {
                f = 0.0f;
            }
            emojiTabButton.setAlpha(f);
        } else {
            ViewPropertyAnimator animate = this.recentTab.animate();
            if (!z) {
                f = 0.0f;
            }
            animate.alpha(f).setDuration(200L).setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT).start();
        }
        if ((!z && this.selected == 0) || (z && this.selected == 1)) {
            select(0, !this.recentFirstChange);
        }
        this.contentView.requestLayout();
        this.recentFirstChange = false;
    }

    private boolean isFreeEmojiPack(TLRPC$StickerSet tLRPC$StickerSet, ArrayList<TLRPC$Document> arrayList) {
        if (tLRPC$StickerSet == null || arrayList == null) {
            return false;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (!MessageObject.isFreeEmoji(arrayList.get(i))) {
                return false;
            }
        }
        return true;
    }

    private TLRPC$Document getThumbDocument(TLRPC$StickerSet tLRPC$StickerSet, ArrayList<TLRPC$Document> arrayList) {
        if (tLRPC$StickerSet == null) {
            return null;
        }
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                TLRPC$Document tLRPC$Document = arrayList.get(i);
                if (tLRPC$Document.id == tLRPC$StickerSet.thumb_document_id) {
                    return tLRPC$Document;
                }
            }
        }
        if (arrayList != null && arrayList.size() >= 1) {
            return arrayList.get(0);
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x0222 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0220  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateEmojiPacks() {
        int i;
        EmojiTabButton emojiTabButton;
        Integer num;
        boolean z;
        EmojiView.EmojiPack emojiPack;
        if (!this.includeAnimated) {
            return;
        }
        if (this.first && !MediaDataController.getInstance(UserConfig.selectedAccount).areStickersLoaded(5)) {
            return;
        }
        this.first = false;
        ArrayList<EmojiView.EmojiPack> emojipacks = this.parent.getEmojipacks();
        if (emojipacks == null) {
            return;
        }
        if (this.emojipackTabs == null) {
            this.emojipackTabs = new ArrayList<>();
        }
        boolean z2 = this.emojipackTabs.size() == 0 && emojipacks.size() > 0 && this.appearCount != emojipacks.size() && this.wasDrawn;
        if (this.appearAnimation != null && this.appearCount != emojipacks.size()) {
            this.appearAnimation.cancel();
            this.appearAnimation = null;
        }
        this.appearCount = emojipacks.size();
        boolean isPremium = UserConfig.getInstance(UserConfig.selectedAccount).isPremium();
        int i2 = 0;
        while (true) {
            i = 2;
            if (i2 >= this.emojipackTabs.size()) {
                break;
            }
            final EmojiTabButton emojiTabButton2 = this.emojipackTabs.get(i2);
            if (emojiTabButton2 != null && emojiTabButton2.id != null) {
                int i3 = 0;
                while (i3 < emojipacks.size()) {
                    emojiPack = emojipacks.get(i3);
                    z = isPremium;
                    if (Arrays.hashCode(new Object[]{Long.valueOf(emojiPack.set.id), Boolean.valueOf(emojiPack.featured)}) == emojiTabButton2.id.intValue()) {
                        break;
                    }
                    i3++;
                    isPremium = z;
                }
            }
            z = isPremium;
            emojiPack = null;
            if (emojiPack == null && emojiTabButton2 != null) {
                android.graphics.Rect rect = new android.graphics.Rect();
                rect.set(emojiTabButton2.getLeft(), emojiTabButton2.getTop(), emojiTabButton2.getRight(), emojiTabButton2.getBottom());
                this.removingViews.put(emojiTabButton2, rect);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(emojiTabButton2.getAlpha(), 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.EmojiTabsStrip$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        EmojiTabsStrip.this.lambda$updateEmojiPacks$0(emojiTabButton2, valueAnimator);
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.EmojiTabsStrip.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        EmojiTabsStrip.this.removingViews.remove(emojiTabButton2);
                        EmojiTabsStrip.this.contentView.invalidate();
                    }
                });
                ofFloat.setDuration(200L);
                ofFloat.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                ofFloat.start();
                this.emojipackTabs.remove(i2);
                i2--;
            }
            this.contentView.removeView(emojiTabButton2);
            i2++;
            isPremium = z;
        }
        boolean z3 = isPremium;
        int i4 = 0;
        while (i4 < emojipacks.size()) {
            EmojiView.EmojiPack emojiPack2 = emojipacks.get(i4);
            Object[] objArr = new Object[i];
            objArr[0] = Long.valueOf(emojiPack2.set.id);
            objArr[1] = Boolean.valueOf(emojiPack2.featured);
            int hashCode = Arrays.hashCode(objArr);
            int i5 = 0;
            while (true) {
                if (i5 >= this.emojipackTabs.size()) {
                    emojiTabButton = null;
                    break;
                }
                emojiTabButton = this.emojipackTabs.get(i5);
                if (emojiTabButton != null && (num = emojiTabButton.id) != null && num.intValue() == hashCode) {
                    break;
                }
                i5++;
            }
            boolean isFreeEmojiPack = isFreeEmojiPack(emojiPack2.set, emojiPack2.documents);
            AnimatedEmojiDrawable animatedEmojiDrawable = emojiTabButton == null ? null : (AnimatedEmojiDrawable) emojiTabButton.getDrawable();
            TLRPC$Document thumbDocument = getThumbDocument(emojiPack2.set, emojiPack2.documents);
            if (thumbDocument != null && (animatedEmojiDrawable == null || animatedEmojiDrawable.getDocumentId() != thumbDocument.id)) {
                animatedEmojiDrawable = AnimatedEmojiDrawable.make(UserConfig.selectedAccount, 3, thumbDocument);
            }
            AnimatedEmojiDrawable animatedEmojiDrawable2 = animatedEmojiDrawable;
            if (emojiTabButton == null) {
                EmojiTabButton emojiTabButton3 = new EmojiTabButton(getContext(), (Drawable) animatedEmojiDrawable2, isFreeEmojiPack, false, false);
                emojiTabButton3.id = Integer.valueOf(hashCode);
                if (animatedEmojiDrawable2 != null) {
                    animatedEmojiDrawable2.addView(emojiTabButton3.imageView);
                }
                this.emojipackTabs.add(emojiTabButton3);
                emojiTabButton = emojiTabButton3;
            } else {
                if (emojiTabButton.getDrawable() instanceof AnimatedEmojiDrawable) {
                    ((AnimatedEmojiDrawable) emojiTabButton.getDrawable()).removeView(emojiTabButton.imageView);
                }
                emojiTabButton.setDrawable(animatedEmojiDrawable2);
                if (animatedEmojiDrawable2 != null) {
                    animatedEmojiDrawable2.addView(emojiTabButton.imageView);
                }
            }
            if (!z3 && !isFreeEmojiPack) {
                emojiTabButton.setLock(Boolean.TRUE);
            } else if (!emojiPack2.installed && !this.parent.installedEmojiSets.contains(Long.valueOf(emojiPack2.set.id))) {
                emojiTabButton.setLock(Boolean.FALSE);
            } else {
                emojiTabButton.setLock(null);
                if (!z2) {
                    emojiTabButton.newly = false;
                }
                this.contentView.addView(emojiTabButton);
                i4++;
                i = 2;
            }
            if (!z2) {
            }
            this.contentView.addView(emojiTabButton);
            i4++;
            i = 2;
        }
        EmojiTabButton emojiTabButton4 = this.settingsTab;
        if (emojiTabButton4 != null) {
            emojiTabButton4.bringToFront();
            if (this.settingsTab.getAlpha() < 1.0f) {
                this.settingsTab.animate().alpha(1.0f).setDuration(200L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
            }
        }
        updateClickListeners();
    }

    public /* synthetic */ void lambda$updateEmojiPacks$0(EmojiTabButton emojiTabButton, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        emojiTabButton.setAlpha(floatValue);
        emojiTabButton.setScaleX(floatValue);
        emojiTabButton.setScaleY(floatValue);
        this.contentView.invalidate();
    }

    public void updateClickListeners() {
        int i = 0;
        final int i2 = 0;
        while (i < this.contentView.getChildCount()) {
            View childAt = this.contentView.getChildAt(i);
            if (childAt instanceof EmojiTabsView) {
                EmojiTabsView emojiTabsView = (EmojiTabsView) childAt;
                int i3 = 0;
                while (i3 < emojiTabsView.contentView.getChildCount()) {
                    emojiTabsView.contentView.getChildAt(i3).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiTabsStrip$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            EmojiTabsStrip.this.lambda$updateClickListeners$2(i2, view);
                        }
                    });
                    i3++;
                    i2++;
                }
                i2--;
            } else if (childAt != null) {
                childAt.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiTabsStrip$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        EmojiTabsStrip.this.lambda$updateClickListeners$3(i2, view);
                    }
                });
            }
            i++;
            i2++;
        }
        EmojiTabButton emojiTabButton = this.settingsTab;
        if (emojiTabButton != null) {
            emojiTabButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.EmojiTabsStrip$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    EmojiTabsStrip.this.lambda$updateClickListeners$4(view);
                }
            });
        }
    }

    public /* synthetic */ void lambda$updateClickListeners$2(int i, View view) {
        onTabClick(i);
    }

    public /* synthetic */ void lambda$updateClickListeners$3(int i, View view) {
        onTabClick(i);
    }

    public /* synthetic */ void lambda$updateClickListeners$4(View view) {
        Runnable runnable = this.onSettingsOpenRunnable;
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        this.contentView.setPadding(AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(11.0f), 0);
        super.onMeasure(i, i2);
    }

    public void updateColors() {
        EmojiTabButton emojiTabButton = this.recentTab;
        if (emojiTabButton != null) {
            emojiTabButton.updateColor();
        }
    }

    public void select(int i) {
        select(i, true);
    }

    public void select(int i, boolean z) {
        int i2;
        int i3;
        if (!this.recentIsShown) {
            i = Math.max(1, i);
        }
        int i4 = this.selected;
        int i5 = 0;
        int i6 = 0;
        while (i5 < this.contentView.getChildCount()) {
            View childAt = this.contentView.getChildAt(i5);
            if (childAt instanceof EmojiTabsView) {
                EmojiTabsView emojiTabsView = (EmojiTabsView) childAt;
                int i7 = i6;
                int i8 = 0;
                while (i8 < emojiTabsView.contentView.getChildCount()) {
                    View childAt2 = emojiTabsView.contentView.getChildAt(i8);
                    if (childAt2 instanceof EmojiTabButton) {
                        ((EmojiTabButton) childAt2).updateSelect(i == i7, z);
                    }
                    i8++;
                    i7++;
                }
                i3 = i7 - 1;
            } else {
                if (childAt instanceof EmojiTabButton) {
                    ((EmojiTabButton) childAt).updateSelect(i == i6, z);
                }
                i3 = i6;
            }
            if (i >= i6 && i <= i3) {
                this.selected = i5;
            }
            i5++;
            i6 = i3 + 1;
        }
        if (i4 != this.selected) {
            ValueAnimator valueAnimator = this.selectAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            final float f = this.selectT;
            final float f2 = this.selected;
            if (z) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.selectAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.EmojiTabsStrip$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        EmojiTabsStrip.this.lambda$select$5(f, f2, valueAnimator2);
                    }
                });
                this.selectAnimator.setDuration(350L);
                this.selectAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.selectAnimator.start();
            } else {
                this.selectAnimationT = 1.0f;
                this.selectT = AndroidUtilities.lerp(f, f2, 1.0f);
                this.contentView.invalidate();
            }
            EmojiTabsView emojiTabsView2 = this.emojiTabs;
            if (emojiTabsView2 != null) {
                emojiTabsView2.show(this.selected == 1, z);
            }
            View childAt3 = this.contentView.getChildAt(this.selected);
            if (this.selected >= 2) {
                scrollToVisible(childAt3.getLeft(), childAt3.getRight());
            } else {
                scrollTo(0);
            }
        }
        if (this.wasIndex != i) {
            EmojiTabsView emojiTabsView3 = this.emojiTabs;
            if (emojiTabsView3 != null && this.selected == 1 && i >= 1 && i <= emojiTabsView3.contentView.getChildCount() + 1) {
                this.emojiTabs.scrollToVisible(AndroidUtilities.dp(((i - 1) * 36) - 6), AndroidUtilities.dp(i2 + 30));
            }
            this.wasIndex = i;
        }
    }

    public /* synthetic */ void lambda$select$5(float f, float f2, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.selectAnimationT = floatValue;
        this.selectT = AndroidUtilities.lerp(f, f2, floatValue);
        this.contentView.invalidate();
    }

    public int selectorColor() {
        return Theme.getColor("chat_emojiPanelIcon", this.resourcesProvider) & 788529151;
    }

    /* loaded from: classes3.dex */
    public class EmojiTabButton extends ViewGroup {
        private boolean forceSelector;
        public Integer id;
        private ImageView imageView;
        private ValueAnimator lockAnimator;
        private float lockT;
        private PremiumLockIconView lockView;
        private RLottieDrawable lottieDrawable;
        public boolean newly;
        private boolean round;
        private ValueAnimator selectAnimator;
        private float selectT;
        private boolean selected;
        private boolean wasVisible;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public EmojiTabButton(Context context, int i, int i2, boolean z, boolean z2) {
            super(context);
            EmojiTabsStrip.this = r8;
            this.round = z;
            this.forceSelector = z2;
            if (z) {
                setBackground(Theme.createCircleSelectorDrawable(r8.selectorColor(), 0, 0));
            } else if (z2) {
                setBackground(Theme.createRadSelectorDrawable(r8.selectorColor(), 8, 8));
            }
            if (Build.VERSION.SDK_INT >= 23) {
                RLottieDrawable rLottieDrawable = new RLottieDrawable(i2, "" + i2, AndroidUtilities.dp(24.0f), AndroidUtilities.dp(24.0f), false, null);
                this.lottieDrawable = rLottieDrawable;
                rLottieDrawable.setBounds(AndroidUtilities.dp(3.0f), AndroidUtilities.dp(3.0f), AndroidUtilities.dp(27.0f), AndroidUtilities.dp(27.0f));
                this.lottieDrawable.setMasterParent(this);
                this.lottieDrawable.setAllowDecodeSingleFrame(true);
                this.lottieDrawable.start();
            } else {
                ImageView imageView = new ImageView(context);
                this.imageView = imageView;
                imageView.setImageDrawable(context.getResources().getDrawable(i).mutate());
                addView(this.imageView);
            }
            setColor(Theme.getColor("chat_emojiPanelIcon", r8.resourcesProvider));
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public EmojiTabButton(Context context, int i, boolean z, boolean z2) {
            super(context);
            EmojiTabsStrip.this = r1;
            this.round = z;
            this.forceSelector = z2;
            if (z) {
                setBackground(Theme.createCircleSelectorDrawable(r1.selectorColor(), 0, 0));
            } else if (z2) {
                setBackground(Theme.createRadSelectorDrawable(r1.selectorColor(), 8, 8));
            }
            ImageView imageView = new ImageView(context);
            this.imageView = imageView;
            imageView.setImageDrawable(context.getResources().getDrawable(i).mutate());
            setColor(Theme.getColor("chat_emojiPanelIcon", r1.resourcesProvider));
            addView(this.imageView);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public EmojiTabButton(Context context, Drawable drawable, boolean z, boolean z2, boolean z3) {
            super(context);
            EmojiTabsStrip.this = r1;
            this.newly = true;
            this.round = z2;
            this.forceSelector = z3;
            if (z2) {
                setBackground(Theme.createCircleSelectorDrawable(r1.selectorColor(), 0, 0));
            } else if (z3) {
                setBackground(Theme.createRadSelectorDrawable(r1.selectorColor(), 8, 8));
            }
            ImageView imageView = new ImageView(context, r1) { // from class: org.telegram.ui.Components.EmojiTabsStrip.EmojiTabButton.1
                @Override // android.view.View
                public void invalidate() {
                    super.invalidate();
                    EmojiTabButton.this.updateLockImageReceiver();
                }

                @Override // android.view.View
                protected void dispatchDraw(Canvas canvas) {
                    if (getDrawable() != null) {
                        getDrawable().setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                        getDrawable().setAlpha(255);
                        getDrawable().draw(canvas);
                    }
                }
            };
            this.imageView = imageView;
            imageView.setImageDrawable(drawable);
            addView(this.imageView);
            PremiumLockIconView premiumLockIconView = new PremiumLockIconView(context, PremiumLockIconView.TYPE_STICKERS_PREMIUM_LOCKED);
            this.lockView = premiumLockIconView;
            premiumLockIconView.setAlpha(0.0f);
            this.lockView.setScaleX(0.0f);
            this.lockView.setScaleY(0.0f);
            updateLockImageReceiver();
            addView(this.lockView);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            RLottieDrawable rLottieDrawable = this.lottieDrawable;
            if (rLottieDrawable != null) {
                rLottieDrawable.draw(canvas);
            }
        }

        @Override // android.view.View
        public boolean performClick() {
            return super.performClick();
        }

        public void updateVisibilityInbounds(boolean z, boolean z2) {
            RLottieDrawable rLottieDrawable;
            if (!this.wasVisible && z && (rLottieDrawable = this.lottieDrawable) != null && !rLottieDrawable.isRunning() && !z2) {
                this.lottieDrawable.setProgress(0.0f);
                this.lottieDrawable.start();
            }
            this.wasVisible = z;
        }

        public void setLock(Boolean bool) {
            if (this.lockView == null) {
                return;
            }
            if (bool == null) {
                updateLock(false);
                return;
            }
            updateLock(true);
            if (bool.booleanValue()) {
                this.lockView.setImageResource(R.drawable.msg_mini_lockedemoji);
                return;
            }
            Drawable mutate = getResources().getDrawable(R.drawable.msg_mini_addemoji).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.MULTIPLY));
            this.lockView.setImageDrawable(mutate);
        }

        private void updateLock(final boolean z) {
            ValueAnimator valueAnimator = this.lockAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float f = 1.0f;
            if (Math.abs(this.lockT - (z ? 1.0f : 0.0f)) < 0.01f) {
                return;
            }
            this.lockView.setVisibility(0);
            float[] fArr = new float[2];
            fArr[0] = this.lockT;
            if (!z) {
                f = 0.0f;
            }
            fArr[1] = f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            this.lockAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.EmojiTabsStrip$EmojiTabButton$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    EmojiTabsStrip.EmojiTabButton.this.lambda$updateLock$0(valueAnimator2);
                }
            });
            this.lockAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.EmojiTabsStrip.EmojiTabButton.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!z) {
                        EmojiTabButton.this.lockView.setVisibility(8);
                    }
                }
            });
            this.lockAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            this.lockAnimator.setDuration(200L);
            this.lockAnimator.start();
        }

        public /* synthetic */ void lambda$updateLock$0(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.lockT = floatValue;
            this.lockView.setScaleX(floatValue);
            this.lockView.setScaleY(this.lockT);
            this.lockView.setAlpha(this.lockT);
        }

        public void updateLockImageReceiver() {
            AnimatedEmojiDrawable.EmojiImageReceiver imageReceiver;
            PremiumLockIconView premiumLockIconView = this.lockView;
            if (premiumLockIconView == null || premiumLockIconView.ready() || !(getDrawable() instanceof AnimatedEmojiDrawable) || (imageReceiver = ((AnimatedEmojiDrawable) getDrawable()).getImageReceiver()) == null) {
                return;
            }
            this.lockView.setImageReceiver(imageReceiver);
            this.lockView.invalidate();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            setMeasuredDimension(AndroidUtilities.dp(30.0f), AndroidUtilities.dp(30.0f));
            ImageView imageView = this.imageView;
            if (imageView != null) {
                imageView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
            }
            PremiumLockIconView premiumLockIconView = this.lockView;
            if (premiumLockIconView != null) {
                premiumLockIconView.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(12.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(12.0f), 1073741824));
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            ImageView imageView = this.imageView;
            if (imageView != null) {
                int i5 = (i3 - i) / 2;
                int i6 = (i4 - i2) / 2;
                imageView.layout(i5 - (imageView.getMeasuredWidth() / 2), i6 - (this.imageView.getMeasuredHeight() / 2), i5 + (this.imageView.getMeasuredWidth() / 2), i6 + (this.imageView.getMeasuredHeight() / 2));
            }
            PremiumLockIconView premiumLockIconView = this.lockView;
            if (premiumLockIconView != null) {
                int i7 = i3 - i;
                int i8 = i4 - i2;
                premiumLockIconView.layout(i7 - premiumLockIconView.getMeasuredWidth(), i8 - this.lockView.getMeasuredHeight(), i7, i8);
            }
        }

        public Drawable getDrawable() {
            ImageView imageView = this.imageView;
            if (imageView != null) {
                return imageView.getDrawable();
            }
            return null;
        }

        public void setDrawable(Drawable drawable) {
            AnimatedEmojiDrawable.EmojiImageReceiver imageReceiver;
            if (this.lockView != null && (drawable instanceof AnimatedEmojiDrawable) && (imageReceiver = ((AnimatedEmojiDrawable) drawable).getImageReceiver()) != null) {
                this.lockView.setImageReceiver(imageReceiver);
            }
            ImageView imageView = this.imageView;
            if (imageView != null) {
                imageView.setImageDrawable(drawable);
            }
        }

        public void updateSelect(final boolean z, boolean z2) {
            ImageView imageView = this.imageView;
            if ((imageView == null || imageView.getDrawable() != null) && this.selected != z) {
                this.selected = z;
                ValueAnimator valueAnimator = this.selectAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    this.selectAnimator = null;
                }
                float f = 1.0f;
                if (z2) {
                    float[] fArr = new float[2];
                    fArr[0] = this.selectT;
                    if (!z) {
                        f = 0.0f;
                    }
                    fArr[1] = f;
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                    this.selectAnimator = ofFloat;
                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.EmojiTabsStrip$EmojiTabButton$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            EmojiTabsStrip.EmojiTabButton.this.lambda$updateSelect$1(valueAnimator2);
                        }
                    });
                    this.selectAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.EmojiTabsStrip.EmojiTabButton.3
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (!EmojiTabButton.this.round) {
                                if (z || EmojiTabButton.this.forceSelector) {
                                    if (EmojiTabButton.this.getBackground() != null) {
                                        return;
                                    }
                                    EmojiTabButton emojiTabButton = EmojiTabButton.this;
                                    emojiTabButton.setBackground(Theme.createRadSelectorDrawable(EmojiTabsStrip.this.selectorColor(), 8, 8));
                                    return;
                                }
                                EmojiTabButton.this.setBackground(null);
                            }
                        }
                    });
                    this.selectAnimator.setDuration(350L);
                    this.selectAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                    this.selectAnimator.start();
                    return;
                }
                if (!z) {
                    f = 0.0f;
                }
                this.selectT = f;
                updateColor();
            }
        }

        public /* synthetic */ void lambda$updateSelect$1(ValueAnimator valueAnimator) {
            this.selectT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            setColor(ColorUtils.blendARGB(Theme.getColor("chat_emojiPanelIcon", EmojiTabsStrip.this.resourcesProvider), Theme.getColor("chat_emojiPanelIconSelected", EmojiTabsStrip.this.resourcesProvider), this.selectT));
        }

        public void updateColor() {
            Theme.setSelectorDrawableColor(getBackground(), EmojiTabsStrip.this.selectorColor(), false);
            setColor(ColorUtils.blendARGB(Theme.getColor("chat_emojiPanelIcon", EmojiTabsStrip.this.resourcesProvider), Theme.getColor("chat_emojiPanelIconSelected", EmojiTabsStrip.this.resourcesProvider), this.selectT));
        }

        private void setColor(int i) {
            PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY);
            ImageView imageView = this.imageView;
            if (imageView != null) {
                imageView.setColorFilter(porterDuffColorFilter);
                this.imageView.invalidate();
            }
            RLottieDrawable rLottieDrawable = this.lottieDrawable;
            if (rLottieDrawable != null) {
                rLottieDrawable.setColorFilter(porterDuffColorFilter);
                invalidate();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class EmojiTabsView extends ScrollableHorizontalScrollView {
        private LinearLayout contentView;
        public int id;
        private ValueAnimator showAnimator;
        private boolean touching = false;
        private boolean shown = false;
        private float showT = 0.0f;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public EmojiTabsView(Context context) {
            super(context);
            EmojiTabsStrip.this = r12;
            setSmoothScrollingEnabled(true);
            setHorizontalScrollBarEnabled(false);
            setVerticalScrollBarEnabled(false);
            if (Build.VERSION.SDK_INT >= 21) {
                setNestedScrollingEnabled(true);
            }
            LinearLayout linearLayout = new LinearLayout(context, r12) { // from class: org.telegram.ui.Components.EmojiTabsStrip.EmojiTabsView.1
                @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
                protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                    int paddingLeft = getPaddingLeft();
                    int i5 = (i4 - i2) / 2;
                    for (int i6 = 0; i6 < getChildCount(); i6++) {
                        View childAt = getChildAt(i6);
                        if (childAt != EmojiTabsStrip.this.settingsTab && childAt != null) {
                            childAt.layout(paddingLeft, i5 - (childAt.getMeasuredHeight() / 2), childAt.getMeasuredWidth() + paddingLeft, (childAt.getMeasuredHeight() / 2) + i5);
                            paddingLeft += childAt.getMeasuredWidth() + AndroidUtilities.dp(2.0f);
                        }
                    }
                }

                @Override // android.widget.LinearLayout, android.view.View
                protected void onMeasure(int i, int i2) {
                    super.onMeasure(Math.max(View.MeasureSpec.getSize(i), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp((EmojiTabsView.this.contentView.getChildCount() * 32) - 2), 1073741824)), i2);
                }
            };
            this.contentView = linearLayout;
            linearLayout.setOrientation(0);
            addView(this.contentView, new FrameLayout.LayoutParams(-2, -1));
            for (int i = 0; i < EmojiTabsStrip.emojiTabsDrawableIds.length; i++) {
                this.contentView.addView(new EmojiTabButton(context, EmojiTabsStrip.emojiTabsDrawableIds[i], EmojiTabsStrip.emojiTabsAnimatedDrawableIds[i], true, false, r12) { // from class: org.telegram.ui.Components.EmojiTabsStrip.EmojiTabsView.2
                    {
                        EmojiTabsView.this = this;
                        EmojiTabsStrip emojiTabsStrip = EmojiTabsStrip.this;
                    }

                    @Override // android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        EmojiTabsView.this.intercept(motionEvent);
                        return super.onTouchEvent(motionEvent);
                    }
                });
            }
        }

        @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.lerp(AndroidUtilities.dp(30.0f), maxWidth(), this.showT), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(30.0f), 1073741824));
        }

        public int maxWidth() {
            return AndroidUtilities.dp((Math.min(5.7f, this.contentView.getChildCount()) * 32.0f) - 2.0f);
        }

        @Override // android.view.View
        protected void onScrollChanged(int i, int i2, int i3, int i4) {
            super.onScrollChanged(i, i2, i3, i4);
            if ((Math.abs(i2 - i4) < 2 || i2 >= getMeasuredHeight() || i2 == 0) && !this.touching) {
                EmojiTabsStrip.this.requestDisallowInterceptTouchEvent(false);
            }
            updateButtonsVisibility();
        }

        private void updateButtonsVisibility() {
            ValueAnimator valueAnimator;
            int childCount = this.contentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.contentView.getChildAt(i);
                if (childAt instanceof EmojiTabButton) {
                    EmojiTabButton emojiTabButton = (EmojiTabButton) childAt;
                    boolean z = true;
                    boolean z2 = childAt.getRight() - getScrollX() > 0 && childAt.getLeft() - getScrollX() < getMeasuredWidth();
                    if (!this.scrollingAnimation || ((valueAnimator = this.showAnimator) != null && valueAnimator.isRunning())) {
                        z = false;
                    }
                    emojiTabButton.updateVisibilityInbounds(z2, z);
                }
            }
        }

        public void intercept(MotionEvent motionEvent) {
            if (!this.shown || this.scrollingAnimation) {
                return;
            }
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action == 1) {
                    this.touching = false;
                    return;
                } else if (action != 2) {
                    return;
                }
            }
            this.touching = true;
            if (!this.scrollingAnimation) {
                resetScrollTo();
            }
            EmojiTabsStrip.this.requestDisallowInterceptTouchEvent(true);
        }

        @Override // android.widget.HorizontalScrollView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            intercept(motionEvent);
            return super.onTouchEvent(motionEvent);
        }

        public void show(boolean z, boolean z2) {
            if (z == this.shown) {
                return;
            }
            this.shown = z;
            if (!z) {
                scrollTo(0);
            }
            ValueAnimator valueAnimator = this.showAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            float f = 1.0f;
            if (z2) {
                float[] fArr = new float[2];
                fArr[0] = this.showT;
                if (!z) {
                    f = 0.0f;
                }
                fArr[1] = f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                this.showAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.EmojiTabsStrip$EmojiTabsView$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        EmojiTabsStrip.EmojiTabsView.this.lambda$show$0(valueAnimator2);
                    }
                });
                this.showAnimator.setDuration(475L);
                this.showAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.showAnimator.start();
                return;
            }
            if (!z) {
                f = 0.0f;
            }
            this.showT = f;
            invalidate();
            requestLayout();
            updateButtonsVisibility();
            EmojiTabsStrip.this.contentView.invalidate();
        }

        public /* synthetic */ void lambda$show$0(ValueAnimator valueAnimator) {
            this.showT = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
            requestLayout();
            updateButtonsVisibility();
            EmojiTabsStrip.this.contentView.invalidate();
        }
    }
}
