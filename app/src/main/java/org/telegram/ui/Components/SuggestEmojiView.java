package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.RecyclerListView;
/* loaded from: classes3.dex */
public class SuggestEmojiView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private final Adapter adapter;
    private Integer arrowToEnd;
    private Emoji.EmojiSpan arrowToSpan;
    private Integer arrowToStart;
    private float arrowX;
    private AnimatedFloat arrowXAnimated;
    private Paint backgroundPaint;
    private final FrameLayout containerView;
    private final int currentAccount;
    private final ChatActivityEnterView enterView;
    private boolean forceClose;
    private String[] lastLang;
    private String lastQuery;
    private float lastSpanY;
    private final LinearLayoutManager layout;
    private Drawable leftGradient;
    private AnimatedFloat leftGradientAlpha;
    private final RecyclerListView listView;
    private AnimatedFloat listViewCenterAnimated;
    private AnimatedFloat listViewWidthAnimated;
    private final Theme.ResourcesProvider resourcesProvider;
    private Drawable rightGradient;
    private AnimatedFloat rightGradientAlpha;
    private Runnable searchRunnable;
    private boolean show;
    private AnimatedFloat showFloat1;
    private AnimatedFloat showFloat2;
    private ArrayList<MediaDataController.KeywordResult> keywordResults = new ArrayList<>();
    private Path path = new Path();
    private Path circlePath = new Path();

    public SuggestEmojiView(Context context, int i, final ChatActivityEnterView chatActivityEnterView, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Components.SuggestEmojiView.1
            @Override // android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                SuggestEmojiView.this.drawContainerBegin(canvas);
                super.dispatchDraw(canvas);
                SuggestEmojiView.this.drawContainerEnd(canvas);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i2, int i3) {
                setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(6.66f));
                super.onMeasure(i2, i3);
            }
        };
        this.containerView = frameLayout;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.showFloat1 = new AnimatedFloat(frameLayout, 120L, 350L, cubicBezierInterpolator);
        this.showFloat2 = new AnimatedFloat(frameLayout, 150L, 600L, cubicBezierInterpolator);
        new OvershootInterpolator(0.4f);
        this.leftGradientAlpha = new AnimatedFloat(frameLayout, 300L, cubicBezierInterpolator);
        this.rightGradientAlpha = new AnimatedFloat(frameLayout, 300L, cubicBezierInterpolator);
        this.arrowXAnimated = new AnimatedFloat(frameLayout, 200L, cubicBezierInterpolator);
        this.listViewCenterAnimated = new AnimatedFloat(frameLayout, 350L, cubicBezierInterpolator);
        this.listViewWidthAnimated = new AnimatedFloat(frameLayout, 350L, cubicBezierInterpolator);
        this.currentAccount = i;
        this.enterView = chatActivityEnterView;
        this.resourcesProvider = resourcesProvider;
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.Components.SuggestEmojiView.2
            private boolean left;
            private boolean right;

            @Override // androidx.recyclerview.widget.RecyclerView
            public void onScrolled(int i2, int i3) {
                super.onScrolled(i2, i3);
                boolean canScrollHorizontally = canScrollHorizontally(-1);
                boolean canScrollHorizontally2 = canScrollHorizontally(1);
                if (this.left == canScrollHorizontally && this.right == canScrollHorizontally2) {
                    return;
                }
                SuggestEmojiView.this.containerView.invalidate();
                this.left = canScrollHorizontally;
                this.right = canScrollHorizontally2;
            }
        };
        this.listView = recyclerListView;
        Adapter adapter = new Adapter();
        this.adapter = adapter;
        recyclerListView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        this.layout = linearLayoutManager;
        linearLayoutManager.setOrientation(0);
        recyclerListView.setLayoutManager(linearLayoutManager);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setDurations(45L);
        defaultItemAnimator.setTranslationInterpolator(cubicBezierInterpolator);
        recyclerListView.setItemAnimator(defaultItemAnimator);
        recyclerListView.setSelectorDrawableColor(Theme.getColor("listSelectorSDK21", resourcesProvider));
        recyclerListView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.SuggestEmojiView$$ExternalSyntheticLambda4
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i2) {
                SuggestEmojiView.this.lambda$new$0(view, i2);
            }
        });
        frameLayout.addView(recyclerListView, LayoutHelper.createFrame(-1, 52.0f));
        addView(frameLayout, LayoutHelper.createFrame(-1.0f, 66.66f, 80));
        chatActivityEnterView.getEditField().addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Components.SuggestEmojiView.3
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (chatActivityEnterView.getVisibility() == 0) {
                    SuggestEmojiView.this.update();
                }
            }
        });
        Drawable mutate = getResources().getDrawable(R.drawable.gradient_right).mutate();
        this.leftGradient = mutate;
        mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_stickersHintPanel", resourcesProvider), PorterDuff.Mode.MULTIPLY));
        Drawable mutate2 = getResources().getDrawable(R.drawable.gradient_left).mutate();
        this.rightGradient = mutate2;
        mutate2.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_stickersHintPanel", resourcesProvider), PorterDuff.Mode.MULTIPLY));
        MediaDataController.getInstance(i).checkStickers(5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view, int i) {
        onClick(((Adapter.EmojiImageView) view).emoji);
    }

    public void onTextSelectionChanged(int i, int i2) {
        update();
    }

    @Override // android.view.View
    public boolean isShown() {
        return this.show;
    }

    public void updateColors() {
        Paint paint = this.backgroundPaint;
        if (paint != null) {
            paint.setColor(Theme.getColor("chat_stickersHintPanel", this.resourcesProvider));
        }
        this.leftGradient.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_stickersHintPanel", this.resourcesProvider), PorterDuff.Mode.MULTIPLY));
        this.rightGradient.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chat_stickersHintPanel", this.resourcesProvider), PorterDuff.Mode.MULTIPLY));
    }

    public void forceClose() {
        this.show = false;
        this.forceClose = true;
        this.containerView.invalidate();
    }

    public void update() {
        ChatActivityEnterView chatActivityEnterView = this.enterView;
        if (chatActivityEnterView == null || chatActivityEnterView.getEditField() == null || this.enterView.getFieldText() == null) {
            this.show = false;
            this.containerView.invalidate();
            return;
        }
        int selectionStart = this.enterView.getEditField().getSelectionStart();
        int selectionEnd = this.enterView.getEditField().getSelectionEnd();
        if (selectionStart != selectionEnd) {
            this.show = false;
            this.containerView.invalidate();
            return;
        }
        CharSequence fieldText = this.enterView.getFieldText();
        Emoji.EmojiSpan[] emojiSpanArr = fieldText instanceof Spanned ? (Emoji.EmojiSpan[]) ((Spanned) fieldText).getSpans(Math.max(0, selectionEnd - 24), selectionEnd, Emoji.EmojiSpan.class) : null;
        if (emojiSpanArr != null && emojiSpanArr.length > 0) {
            Emoji.EmojiSpan emojiSpan = emojiSpanArr[emojiSpanArr.length - 1];
            if (emojiSpan != null) {
                Spanned spanned = (Spanned) fieldText;
                int spanStart = spanned.getSpanStart(emojiSpan);
                int spanEnd = spanned.getSpanEnd(emojiSpan);
                if (selectionStart == spanEnd) {
                    String substring = fieldText.toString().substring(spanStart, spanEnd);
                    this.show = true;
                    setVisibility(0);
                    this.arrowToSpan = emojiSpan;
                    this.arrowToEnd = null;
                    this.arrowToStart = null;
                    searchAnimated(substring);
                    this.containerView.invalidate();
                    return;
                }
            }
        } else if (selectionEnd < 52) {
            this.show = true;
            setVisibility(0);
            this.arrowToSpan = null;
            searchKeywords(fieldText.toString().substring(0, selectionEnd));
            this.containerView.invalidate();
            return;
        }
        Runnable runnable = this.searchRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.searchRunnable = null;
        }
        this.show = false;
        this.containerView.invalidate();
    }

    private void searchKeywords(final String str) {
        if (str != null) {
            String str2 = this.lastQuery;
            if (str2 != null && str2.equals(str) && !this.keywordResults.isEmpty()) {
                return;
            }
            this.lastQuery = str;
            final String[] currentKeyboardLanguage = AndroidUtilities.getCurrentKeyboardLanguage();
            String[] strArr = this.lastLang;
            if (strArr == null || !Arrays.equals(currentKeyboardLanguage, strArr)) {
                MediaDataController.getInstance(this.currentAccount).fetchNewEmojiKeywords(currentKeyboardLanguage);
            }
            this.lastLang = currentKeyboardLanguage;
            Runnable runnable = this.searchRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.searchRunnable = null;
            }
            this.searchRunnable = new Runnable() { // from class: org.telegram.ui.Components.SuggestEmojiView$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    SuggestEmojiView.this.lambda$searchKeywords$2(currentKeyboardLanguage, str);
                }
            };
            if (this.keywordResults.isEmpty()) {
                AndroidUtilities.runOnUIThread(this.searchRunnable, 600L);
            } else {
                this.searchRunnable.run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchKeywords$2(String[] strArr, final String str) {
        MediaDataController.getInstance(this.currentAccount).getEmojiSuggestions(strArr, str, true, new MediaDataController.KeywordResultCallback() { // from class: org.telegram.ui.Components.SuggestEmojiView$$ExternalSyntheticLambda3
            @Override // org.telegram.messenger.MediaDataController.KeywordResultCallback
            public final void run(ArrayList arrayList, String str2) {
                SuggestEmojiView.this.lambda$searchKeywords$1(str, arrayList, str2);
            }
        }, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchKeywords$1(String str, ArrayList arrayList, String str2) {
        if (str.equals(this.lastQuery)) {
            if (arrayList != null && !arrayList.isEmpty()) {
                this.forceClose = false;
                setVisibility(0);
                this.lastSpanY = AndroidUtilities.dp(10.0f);
                this.keywordResults = arrayList;
                this.arrowToStart = 0;
                this.arrowToEnd = Integer.valueOf(str.length());
                this.adapter.notifyDataSetChanged();
                return;
            }
            forceClose();
        }
    }

    private void searchAnimated(final String str) {
        if (str != null) {
            String str2 = this.lastQuery;
            if (str2 != null && str2.equals(str) && !this.keywordResults.isEmpty()) {
                return;
            }
            this.lastQuery = str;
            Runnable runnable = this.searchRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.searchRunnable = null;
            }
            this.searchRunnable = new Runnable() { // from class: org.telegram.ui.Components.SuggestEmojiView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SuggestEmojiView.this.lambda$searchAnimated$4(str);
                }
            };
            if (this.keywordResults.isEmpty()) {
                AndroidUtilities.runOnUIThread(this.searchRunnable, 600L);
            } else {
                this.searchRunnable.run();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchAnimated$4(final String str) {
        final ArrayList<MediaDataController.KeywordResult> arrayList = new ArrayList<>(1);
        arrayList.add(new MediaDataController.KeywordResult(str, null));
        MediaDataController.getInstance(this.currentAccount).fillWithAnimatedEmoji(arrayList, 15, new Runnable() { // from class: org.telegram.ui.Components.SuggestEmojiView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SuggestEmojiView.this.lambda$searchAnimated$3(str, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$searchAnimated$3(String str, ArrayList arrayList) {
        if (str.equals(this.lastQuery)) {
            arrayList.remove(arrayList.size() - 1);
            if (!arrayList.isEmpty()) {
                this.forceClose = false;
                setVisibility(0);
                this.keywordResults = arrayList;
                this.adapter.notifyDataSetChanged();
                return;
            }
            forceClose();
        }
    }

    private void onClick(String str) {
        ChatActivityEnterView chatActivityEnterView;
        int intValue;
        int intValue2;
        SpannableString spannableString;
        AnimatedEmojiSpan animatedEmojiSpan;
        if (!this.show || (chatActivityEnterView = this.enterView) == null || !(chatActivityEnterView.getFieldText() instanceof Spanned)) {
            return;
        }
        if (this.arrowToSpan != null) {
            intValue = ((Spanned) this.enterView.getFieldText()).getSpanStart(this.arrowToSpan);
            intValue2 = ((Spanned) this.enterView.getFieldText()).getSpanEnd(this.arrowToSpan);
        } else {
            Integer num = this.arrowToStart;
            if (num == null || this.arrowToEnd == null) {
                return;
            }
            intValue = num.intValue();
            intValue2 = this.arrowToEnd.intValue();
            this.arrowToEnd = null;
            this.arrowToStart = null;
        }
        Paint.FontMetricsInt fontMetricsInt = this.enterView.getEditField().getPaint().getFontMetricsInt();
        if (str != null && str.startsWith("animated_")) {
            try {
                long parseLong = Long.parseLong(str.substring(9));
                TLRPC$Document findDocument = AnimatedEmojiDrawable.findDocument(this.currentAccount, parseLong);
                SpannableString spannableString2 = new SpannableString(MessageObject.findAnimatedEmojiEmoticon(findDocument));
                if (findDocument == null) {
                    animatedEmojiSpan = new AnimatedEmojiSpan(parseLong, fontMetricsInt);
                } else {
                    animatedEmojiSpan = new AnimatedEmojiSpan(findDocument, fontMetricsInt);
                }
                spannableString2.setSpan(animatedEmojiSpan, 0, spannableString2.length(), 33);
                spannableString = spannableString2;
            } catch (Exception unused) {
                return;
            }
        } else {
            spannableString = Emoji.replaceEmoji(str, fontMetricsInt, AndroidUtilities.dp(20.0f), true);
        }
        Editable text = this.enterView.getEditField().getText();
        if (intValue < 0 || intValue2 < 0 || intValue > text.length() || intValue2 > text.length()) {
            return;
        }
        if (this.arrowToSpan != null) {
            if (this.enterView.getFieldText() instanceof Spannable) {
                ((Spannable) this.enterView.getFieldText()).removeSpan(this.arrowToSpan);
            }
            this.arrowToSpan = null;
        }
        text.replace(intValue, intValue2, spannableString);
        this.show = false;
        this.containerView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawContainerBegin(Canvas canvas) {
        ChatActivityEnterView chatActivityEnterView = this.enterView;
        if (chatActivityEnterView != null && chatActivityEnterView.getEditField() != null) {
            Emoji.EmojiSpan emojiSpan = this.arrowToSpan;
            if (emojiSpan != null && emojiSpan.drawn) {
                float x = this.enterView.getEditField().getX() + this.enterView.getEditField().getPaddingLeft();
                Emoji.EmojiSpan emojiSpan2 = this.arrowToSpan;
                this.arrowX = x + emojiSpan2.lastDrawX;
                this.lastSpanY = emojiSpan2.lastDrawY;
            } else if (this.arrowToStart != null && this.arrowToEnd != null) {
                this.arrowX = this.enterView.getEditField().getX() + this.enterView.getEditField().getPaddingLeft() + AndroidUtilities.dp(12.0f);
            }
        }
        float f = this.showFloat1.set((!this.show || this.forceClose || this.keywordResults.isEmpty()) ? 0.0f : 1.0f);
        float f2 = this.showFloat2.set((!this.show || this.forceClose || this.keywordResults.isEmpty()) ? 0.0f : 1.0f);
        float f3 = this.arrowXAnimated.set(this.arrowX);
        if (f <= 0.0f && f2 <= 0.0f && (!this.show || this.forceClose || this.keywordResults.isEmpty())) {
            setVisibility(8);
        }
        this.path.rewind();
        float left = this.listView.getLeft();
        float left2 = this.listView.getLeft() + (this.keywordResults.size() * AndroidUtilities.dp(44.0f));
        boolean z = this.listViewWidthAnimated.get() <= 0.0f;
        float f4 = left2 - left;
        float f5 = f4 <= 0.0f ? this.listViewWidthAnimated.get() : this.listViewWidthAnimated.set(f4, z);
        float f6 = this.listViewCenterAnimated.set((left + left2) / 2.0f, z);
        ChatActivityEnterView chatActivityEnterView2 = this.enterView;
        if (chatActivityEnterView2 != null && chatActivityEnterView2.getEditField() != null) {
            this.containerView.setTranslationY(((-this.enterView.getEditField().getHeight()) - this.enterView.getEditField().getScrollY()) + this.lastSpanY + AndroidUtilities.dp(5.0f));
        }
        float f7 = f5 / 4.0f;
        float f8 = f5 / 2.0f;
        int max = (int) Math.max((this.arrowX - Math.max(f7, Math.min(f8, AndroidUtilities.dp(66.0f)))) - this.listView.getLeft(), 0.0f);
        if (this.listView.getPaddingLeft() != max) {
            this.listView.setPadding(max, 0, 0, 0);
            this.listView.scrollBy(this.listView.getPaddingLeft() - max, 0);
        }
        this.listView.setTranslationX(((int) Math.max((f3 - Math.max(f7, Math.min(f8, AndroidUtilities.dp(66.0f)))) - this.listView.getLeft(), 0.0f)) - max);
        float paddingLeft = (f6 - f8) + this.listView.getPaddingLeft() + this.listView.getTranslationX();
        float top = this.listView.getTop() + this.listView.getTranslationY() + this.listView.getPaddingTop();
        float min = Math.min(f6 + f8 + this.listView.getPaddingLeft() + this.listView.getTranslationX(), getWidth() - this.containerView.getPaddingRight());
        float bottom = (this.listView.getBottom() + this.listView.getTranslationY()) - AndroidUtilities.dp(6.66f);
        float min2 = Math.min(AndroidUtilities.dp(9.0f), f8) * 2.0f;
        RectF rectF = AndroidUtilities.rectTmp;
        float f9 = bottom - min2;
        float f10 = paddingLeft + min2;
        rectF.set(paddingLeft, f9, f10, bottom);
        this.path.arcTo(rectF, 90.0f, 90.0f);
        float f11 = top + min2;
        rectF.set(paddingLeft, top, f10, f11);
        this.path.arcTo(rectF, -180.0f, 90.0f);
        float f12 = min - min2;
        rectF.set(f12, top, min, f11);
        this.path.arcTo(rectF, -90.0f, 90.0f);
        rectF.set(f12, f9, min, bottom);
        this.path.arcTo(rectF, 0.0f, 90.0f);
        this.path.lineTo(AndroidUtilities.dp(8.66f) + f3, bottom);
        this.path.lineTo(f3, AndroidUtilities.dp(6.66f) + bottom);
        this.path.lineTo(f3 - AndroidUtilities.dp(8.66f), bottom);
        this.path.close();
        if (this.backgroundPaint == null) {
            Paint paint = new Paint(1);
            this.backgroundPaint = paint;
            paint.setPathEffect(new CornerPathEffect(AndroidUtilities.dp(2.0f)));
            this.backgroundPaint.setShadowLayer(AndroidUtilities.dp(4.33f), 0.0f, AndroidUtilities.dp(0.33333334f), AndroidUtilities.DARK_STATUS_BAR_OVERLAY);
            this.backgroundPaint.setColor(Theme.getColor("chat_stickersHintPanel", this.resourcesProvider));
        }
        if (f < 1.0f) {
            this.circlePath.rewind();
            float dp = AndroidUtilities.dp(6.66f) + bottom;
            double d = f3 - paddingLeft;
            double d2 = dp - top;
            double d3 = f3 - min;
            double d4 = dp - bottom;
            this.circlePath.addCircle(f3, dp, ((float) Math.sqrt(Math.max(Math.max(Math.pow(d, 2.0d) + Math.pow(d2, 2.0d), Math.pow(d3, 2.0d) + Math.pow(d2, 2.0d)), Math.max(Math.pow(d, 2.0d) + Math.pow(d4, 2.0d), Math.pow(d3, 2.0d) + Math.pow(d4, 2.0d))))) * f, Path.Direction.CW);
            canvas.save();
            canvas.clipPath(this.circlePath);
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (255.0f * f), 31);
        }
        canvas.drawPath(this.path, this.backgroundPaint);
        canvas.save();
        canvas.clipPath(this.path);
    }

    public void drawContainerEnd(Canvas canvas) {
        float f = this.listViewWidthAnimated.get();
        float f2 = this.listViewCenterAnimated.get();
        float f3 = f / 2.0f;
        float paddingLeft = (f2 - f3) + this.listView.getPaddingLeft() + this.listView.getTranslationX();
        float top = this.listView.getTop() + this.listView.getPaddingTop();
        float min = Math.min(f2 + f3 + this.listView.getPaddingLeft() + this.listView.getTranslationX(), getWidth() - this.containerView.getPaddingRight());
        float bottom = this.listView.getBottom();
        float f4 = this.leftGradientAlpha.set(this.listView.canScrollHorizontally(-1) ? 1.0f : 0.0f);
        if (f4 > 0.0f) {
            int i = (int) paddingLeft;
            this.leftGradient.setBounds(i, (int) top, AndroidUtilities.dp(32.0f) + i, (int) bottom);
            this.leftGradient.setAlpha((int) (f4 * 255.0f));
            this.leftGradient.draw(canvas);
        }
        float f5 = this.rightGradientAlpha.set(this.listView.canScrollHorizontally(1) ? 1.0f : 0.0f);
        if (f5 > 0.0f) {
            int i2 = (int) min;
            this.rightGradient.setBounds(i2 - AndroidUtilities.dp(32.0f), (int) top, i2, (int) bottom);
            this.rightGradient.setAlpha((int) (f5 * 255.0f));
            this.rightGradient.draw(canvas);
        }
        canvas.restore();
        if (this.showFloat1.get() < 1.0f) {
            canvas.restore();
            canvas.restore();
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        boolean z = true;
        boolean z2 = getVisibility() == i;
        super.setVisibility(i);
        if (!z2) {
            if (i != 0) {
                z = false;
            }
            for (int i2 = 0; i2 < this.listView.getChildCount(); i2++) {
                if (z) {
                    ((Adapter.EmojiImageView) this.listView.getChildAt(i2)).attach();
                } else {
                    ((Adapter.EmojiImageView) this.listView.getChildAt(i2)).detach();
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        float f = this.listViewWidthAnimated.get();
        float f2 = this.listViewCenterAnimated.get();
        RectF rectF = AndroidUtilities.rectTmp;
        float f3 = f / 2.0f;
        rectF.set((f2 - f3) + this.listView.getPaddingLeft() + this.listView.getTranslationX(), this.listView.getTop() + this.listView.getPaddingTop(), Math.min(f2 + f3 + this.listView.getPaddingLeft() + this.listView.getTranslationX(), getWidth() - this.containerView.getPaddingRight()), this.listView.getBottom());
        rectF.offset(this.containerView.getX(), this.containerView.getY());
        if (this.show && rectF.contains(motionEvent.getX(), motionEvent.getY())) {
            return super.dispatchTouchEvent(motionEvent);
        }
        if (motionEvent.getAction() == 0) {
            return false;
        }
        if (motionEvent.getAction() == 0) {
            motionEvent.setAction(3);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newEmojiSuggestionsAvailable);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.newEmojiSuggestionsAvailable) {
            if (this.keywordResults.isEmpty()) {
                return;
            }
            update();
        } else if (i == NotificationCenter.emojiLoaded) {
            for (int i3 = 0; i3 < this.listView.getChildCount(); i3++) {
                this.listView.getChildAt(i3).invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Adapter extends RecyclerListView.SelectionAdapter {
        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class EmojiImageView extends View {
            private boolean attached;
            private Drawable drawable;
            private String emoji;
            private AnimatedFloat pressed = new AnimatedFloat(this, 350, new OvershootInterpolator(5.0f));

            public EmojiImageView(Context context) {
                super(context);
            }

            @Override // android.view.View
            protected void onMeasure(int i, int i2) {
                setPadding(AndroidUtilities.dp(3.0f), AndroidUtilities.dp(3.0f), AndroidUtilities.dp(3.0f), AndroidUtilities.dp(9.66f));
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(44.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(52.0f), 1073741824));
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setEmoji(String str) {
                this.emoji = str;
                if (str != null && str.startsWith("animated_")) {
                    try {
                        long parseLong = Long.parseLong(str.substring(9));
                        Drawable drawable = this.drawable;
                        if ((drawable instanceof AnimatedEmojiDrawable) && ((AnimatedEmojiDrawable) drawable).getDocumentId() == parseLong) {
                            return;
                        }
                        setImageDrawable(AnimatedEmojiDrawable.make(SuggestEmojiView.this.currentAccount, 2, parseLong));
                        return;
                    } catch (Exception unused) {
                        setImageDrawable(null);
                        return;
                    }
                }
                setImageDrawable(Emoji.getEmojiBigDrawable(str));
            }

            public void setImageDrawable(Drawable drawable) {
                Drawable drawable2 = this.drawable;
                if (drawable2 instanceof AnimatedEmojiDrawable) {
                    ((AnimatedEmojiDrawable) drawable2).removeView(this);
                }
                this.drawable = drawable;
                if (!(drawable instanceof AnimatedEmojiDrawable) || !this.attached) {
                    return;
                }
                ((AnimatedEmojiDrawable) drawable).addView(this);
            }

            @Override // android.view.View
            protected void dispatchDraw(Canvas canvas) {
                float f = ((1.0f - this.pressed.set(isPressed() ? 1.0f : 0.0f)) * 0.2f) + 0.8f;
                if (this.drawable != null) {
                    int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
                    int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                    float width2 = getWidth() / 2;
                    float f2 = (width / 2) * f;
                    float height2 = ((getHeight() - getPaddingBottom()) + getPaddingTop()) / 2;
                    float f3 = (height / 2) * f;
                    this.drawable.setBounds((int) (width2 - f2), (int) (height2 - f3), (int) (width2 + f2), (int) (height2 + f3));
                    Drawable drawable = this.drawable;
                    if (drawable instanceof AnimatedEmojiDrawable) {
                        ((AnimatedEmojiDrawable) drawable).setTime(System.currentTimeMillis());
                    }
                    this.drawable.draw(canvas);
                }
            }

            @Override // android.view.View
            public boolean performClick() {
                try {
                    performHapticFeedback(3, 1);
                } catch (Exception unused) {
                }
                return super.performClick();
            }

            @Override // android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                attach();
            }

            @Override // android.view.View
            protected void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                detach();
            }

            public void detach() {
                Drawable drawable = this.drawable;
                if (drawable instanceof AnimatedEmojiDrawable) {
                    ((AnimatedEmojiDrawable) drawable).removeView(this);
                }
                this.attached = false;
            }

            public void attach() {
                Drawable drawable = this.drawable;
                if (drawable instanceof AnimatedEmojiDrawable) {
                    ((AnimatedEmojiDrawable) drawable).addView(this);
                }
                this.attached = true;
            }
        }

        public Adapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return ((MediaDataController.KeywordResult) SuggestEmojiView.this.keywordResults.get(i)).emoji.hashCode();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RecyclerListView.Holder(new EmojiImageView(SuggestEmojiView.this.getContext()));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ((EmojiImageView) viewHolder.itemView).setEmoji(((MediaDataController.KeywordResult) SuggestEmojiView.this.keywordResults.get(i)).emoji);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return SuggestEmojiView.this.keywordResults.size();
        }
    }
}
