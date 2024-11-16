package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.SQLite.SQLiteDatabase;
import org.telegram.SQLite.SQLitePreparedStatement;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.CacheFetcher;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickerCategoriesListView;

/* loaded from: classes3.dex */
public abstract class StickerCategoriesListView extends RecyclerListView {
    private static EmojiGroupFetcher fetcher;
    public static CacheFetcher search;
    private Adapter adapter;
    private Paint backgroundPaint;
    private EmojiCategory[] categories;
    private boolean categoriesShouldShow;
    private ValueAnimator categoriesShownAnimator;
    private float categoriesShownT;
    private int categoriesType;
    private int dontOccupyWidth;
    public Integer layerNum;
    private LinearLayoutManager layoutManager;
    private AnimatedFloat leftBoundAlpha;
    private Drawable leftBoundDrawable;
    private Utilities.Callback onCategoryClick;
    private Utilities.Callback onScrollFully;
    private Utilities.Callback onScrollIntoOccupiedWidth;
    private View paddingView;
    private int paddingWidth;
    private final RectF rect1;
    private final RectF rect2;
    private final RectF rect3;
    private AnimatedFloat rightBoundAlpha;
    private Drawable rightBoundDrawable;
    private boolean scrolledFully;
    private boolean scrolledIntoOccupiedWidth;
    private AnimatedFloat selectedAlpha;
    private int selectedCategoryIndex;
    private AnimatedFloat selectedIndex;
    private Paint selectedPaint;
    private float shownButtonsAtStart;
    private static Set loadedIconsType = new HashSet();
    static int loadedCategoryIcons = 0;

    private class Adapter extends RecyclerListView.SelectionAdapter {
        private int lastItemCount;

        private Adapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int length = (StickerCategoriesListView.this.categories == null ? 0 : StickerCategoriesListView.this.categories.length) + 1;
            if (length != this.lastItemCount) {
                if (StickerCategoriesListView.this.paddingView != null) {
                    StickerCategoriesListView.this.paddingView.requestLayout();
                }
                this.lastItemCount = length;
            }
            return length;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == 0 ? 0 : 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() != 1 || StickerCategoriesListView.this.categories == null) {
                return;
            }
            int i2 = i - 1;
            EmojiCategory emojiCategory = StickerCategoriesListView.this.categories[i2];
            CategoryButton categoryButton = (CategoryButton) viewHolder.itemView;
            categoryButton.set(emojiCategory, i2, StickerCategoriesListView.this.selectedCategoryIndex == i2);
            categoryButton.setAlpha(StickerCategoriesListView.this.categoriesShownT);
            categoryButton.setScaleX(StickerCategoriesListView.this.categoriesShownT);
            categoryButton.setScaleY(StickerCategoriesListView.this.categoriesShownT);
            categoryButton.play(false);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View categoryButton;
            if (i == 0) {
                categoryButton = StickerCategoriesListView.this.paddingView = new View(StickerCategoriesListView.this.getContext()) { // from class: org.telegram.ui.Components.StickerCategoriesListView.Adapter.1
                    @Override // android.view.View
                    protected void onMeasure(int i2, int i3) {
                        int size = View.MeasureSpec.getSize(i2);
                        if (size <= 0) {
                            size = ((View) getParent()).getMeasuredWidth();
                        }
                        int size2 = View.MeasureSpec.getSize(i3) - AndroidUtilities.dp(4.0f);
                        StickerCategoriesListView stickerCategoriesListView = StickerCategoriesListView.this;
                        super.onMeasure(View.MeasureSpec.makeMeasureSpec(stickerCategoriesListView.paddingWidth = Math.max(stickerCategoriesListView.dontOccupyWidth > 0 ? StickerCategoriesListView.this.dontOccupyWidth + AndroidUtilities.dp(4.0f) : 0, (int) (size - Math.min(((Adapter.this.getItemCount() - 1) * size2) + AndroidUtilities.dp(4.0f), StickerCategoriesListView.this.shownButtonsAtStart * size2))), 1073741824), i3);
                    }
                };
            } else {
                StickerCategoriesListView stickerCategoriesListView = StickerCategoriesListView.this;
                categoryButton = stickerCategoriesListView.new CategoryButton(stickerCategoriesListView.getContext());
            }
            return new RecyclerListView.Holder(categoryButton);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() == 1) {
                CategoryButton categoryButton = (CategoryButton) viewHolder.itemView;
                categoryButton.setSelected(StickerCategoriesListView.this.selectedCategoryIndex == viewHolder.getAdapterPosition() - 1, false);
                categoryButton.play(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class CategoryButton extends RLottieImageView {
        ValueAnimator backAnimator;
        private int imageColor;
        private int index;
        private long lastPlayed;
        ValueAnimator loadAnimator;
        float loadProgress;
        private boolean loaded;
        float pressedProgress;
        private ValueAnimator selectedAnimator;
        private float selectedT;

        public CategoryButton(Context context) {
            super(context);
            this.loaded = false;
            this.loadProgress = 1.0f;
            setImageColor(StickerCategoriesListView.this.getThemedColor(Theme.key_chat_emojiPanelIcon));
            setScaleType(ImageView.ScaleType.CENTER);
            setLayerNum(StickerCategoriesListView.this.layerNum);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLoaded$2(ValueAnimator valueAnimator) {
            this.loadProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$set$0(boolean z, TLRPC.Document document) {
            setOnlyLastFrame(!z);
            setAnimation(document, 24, 24);
            playAnimation();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$set$1() {
            if (this.loaded) {
                return;
            }
            this.loadProgress = 0.0f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setPressed$4(ValueAnimator valueAnimator) {
            this.pressedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setSelected$3(ValueAnimator valueAnimator) {
            updateSelectedT(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateSelectedT(float f) {
            this.selectedT = f;
            setImageColor(ColorUtils.blendARGB(StickerCategoriesListView.this.getThemedColor(Theme.key_chat_emojiPanelIcon), StickerCategoriesListView.this.getThemedColor(Theme.key_chat_emojiPanelIconSelected), this.selectedT));
            invalidate();
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            updatePressedProgress();
            float scale = getScale();
            if (scale != 1.0f) {
                canvas.save();
                canvas.scale(scale, scale, getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f);
            }
            super.draw(canvas);
            if (scale != 1.0f) {
                canvas.restore();
            }
        }

        public float getScale() {
            return (((1.0f - this.pressedProgress) * 0.15f) + 0.85f) * this.loadProgress;
        }

        @Override // org.telegram.ui.Components.RLottieImageView
        protected void onLoaded() {
            this.loaded = true;
            if (this.loadProgress < 1.0f) {
                ValueAnimator valueAnimator = this.loadAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    this.loadAnimator = null;
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.loadProgress, 1.0f);
                this.loadAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.StickerCategoriesListView$CategoryButton$$ExternalSyntheticLambda4
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        StickerCategoriesListView.CategoryButton.this.lambda$onLoaded$2(valueAnimator2);
                    }
                });
                this.loadAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.StickerCategoriesListView.CategoryButton.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        CategoryButton categoryButton = CategoryButton.this;
                        categoryButton.loadProgress = 1.0f;
                        categoryButton.invalidate();
                        CategoryButton.this.loadAnimator = null;
                    }
                });
                this.loadAnimator.setDuration(320L);
                this.loadAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.loadAnimator.start();
            }
        }

        @Override // android.widget.ImageView, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i2);
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(size - AndroidUtilities.dp(4.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        }

        public void play(boolean z) {
            if (System.currentTimeMillis() - this.lastPlayed > 250 || z) {
                this.lastPlayed = System.currentTimeMillis();
                RLottieDrawable animatedDrawable = getAnimatedDrawable();
                if (animatedDrawable == null && getImageReceiver() != null) {
                    animatedDrawable = getImageReceiver().getLottieAnimation();
                }
                if (animatedDrawable != null) {
                    animatedDrawable.stop();
                    animatedDrawable.setCurrentFrame(0);
                    animatedDrawable.restart(true);
                } else if (animatedDrawable == null) {
                    setProgress(0.0f);
                    playAnimation();
                }
            }
        }

        public void set(EmojiCategory emojiCategory, int i, boolean z) {
            this.index = i;
            ValueAnimator valueAnimator = this.loadAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.loadAnimator = null;
            }
            if (emojiCategory.remote) {
                setImageResource(0);
                clearAnimationDrawable();
                final boolean isTabIconsAnimationEnabled = StickerCategoriesListView.this.isTabIconsAnimationEnabled(true);
                this.loaded = false;
                this.loadProgress = 1.0f;
                AnimatedEmojiDrawable.getDocumentFetcher(UserConfig.selectedAccount).fetchDocument(emojiCategory.documentId, new AnimatedEmojiDrawable.ReceivedDocument() { // from class: org.telegram.ui.Components.StickerCategoriesListView$CategoryButton$$ExternalSyntheticLambda1
                    @Override // org.telegram.ui.Components.AnimatedEmojiDrawable.ReceivedDocument
                    public final void run(TLRPC.Document document) {
                        StickerCategoriesListView.CategoryButton.this.lambda$set$0(isTabIconsAnimationEnabled, document);
                    }
                });
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.StickerCategoriesListView$CategoryButton$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        StickerCategoriesListView.CategoryButton.this.lambda$set$1();
                    }
                }, 60L);
            } else {
                if (emojiCategory.animated) {
                    this.cached = false;
                    setImageResource(0);
                    setAnimation(emojiCategory.iconResId, 24, 24);
                    playAnimation();
                } else {
                    clearAnimationDrawable();
                    setImageResource(emojiCategory.iconResId);
                }
                this.loadProgress = 1.0f;
            }
            setSelected(z, false);
        }

        public void setImageColor(int i) {
            if (this.imageColor != i) {
                this.imageColor = i;
                setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.MULTIPLY));
            }
        }

        @Override // android.view.View
        public void setPressed(boolean z) {
            ValueAnimator valueAnimator;
            if (isPressed() != z) {
                super.setPressed(z);
                invalidate();
                StickerCategoriesListView.this.invalidate();
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
                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.StickerCategoriesListView$CategoryButton$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            StickerCategoriesListView.CategoryButton.this.lambda$setPressed$4(valueAnimator2);
                        }
                    });
                    this.backAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.StickerCategoriesListView.CategoryButton.3
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            CategoryButton.this.backAnimator = null;
                        }
                    });
                    this.backAnimator.setInterpolator(new OvershootInterpolator(3.0f));
                    this.backAnimator.setDuration(350L);
                    this.backAnimator.start();
                }
            }
        }

        public void setSelected(boolean z, boolean z2) {
            if (Math.abs(this.selectedT - (z ? 1.0f : 0.0f)) > 0.01f) {
                ValueAnimator valueAnimator = this.selectedAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    this.selectedAnimator = null;
                }
                if (!z2) {
                    updateSelectedT(z ? 1.0f : 0.0f);
                    return;
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.selectedT, z ? 1.0f : 0.0f);
                this.selectedAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.StickerCategoriesListView$CategoryButton$$ExternalSyntheticLambda3
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        StickerCategoriesListView.CategoryButton.this.lambda$setSelected$3(valueAnimator2);
                    }
                });
                this.selectedAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.StickerCategoriesListView.CategoryButton.2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        CategoryButton categoryButton = CategoryButton.this;
                        categoryButton.updateSelectedT(((Float) categoryButton.selectedAnimator.getAnimatedValue()).floatValue());
                        CategoryButton.this.selectedAnimator = null;
                    }
                });
                this.selectedAnimator.setDuration(350L);
                this.selectedAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
                this.selectedAnimator.start();
            }
        }

        public void updatePressedProgress() {
            if (isPressed()) {
                float f = this.pressedProgress;
                if (f != 1.0f) {
                    this.pressedProgress = Utilities.clamp(f + ((1000.0f / AndroidUtilities.screenRefreshRate) / 100.0f), 1.0f, 0.0f);
                    invalidate();
                    StickerCategoriesListView.this.invalidate();
                }
            }
        }
    }

    public static class EmojiCategory {
        public boolean animated;
        public long documentId;
        public String emojis;
        public boolean greeting;
        public int iconResId;
        public boolean premium;
        public boolean remote;
        public String title;

        public static EmojiCategory remote(TLRPC.EmojiGroup emojiGroup) {
            EmojiCategory emojiCategory = new EmojiCategory();
            emojiCategory.remote = true;
            emojiCategory.documentId = emojiGroup.icon_emoji_id;
            if (emojiGroup instanceof TLRPC.TL_emojiGroupPremium) {
                emojiCategory.emojis = "premium";
                emojiCategory.premium = true;
            } else {
                emojiCategory.emojis = TextUtils.concat((CharSequence[]) emojiGroup.emoticons.toArray(new String[0])).toString();
            }
            emojiCategory.greeting = emojiGroup instanceof TLRPC.TL_emojiGroupGreeting;
            emojiCategory.title = emojiGroup.title;
            return emojiCategory;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class EmojiGroupFetcher extends CacheFetcher {
        private EmojiGroupFetcher() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:25:0x006d  */
        /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public static /* synthetic */ void lambda$getLocal$1(int i, Integer num, Utilities.Callback2 callback2) {
            SQLiteCursor sQLiteCursor;
            TLRPC.messages_EmojiGroups messages_emojigroups;
            NativeByteBuffer byteBufferValue;
            SQLiteCursor sQLiteCursor2 = null;
            try {
                try {
                    SQLiteDatabase database = MessagesStorage.getInstance(i).getDatabase();
                    if (database != null) {
                        try {
                            sQLiteCursor = database.queryFinalized("SELECT data FROM emoji_groups WHERE type = ?", num);
                            try {
                                if (!sQLiteCursor.next() || (byteBufferValue = sQLiteCursor.byteBufferValue(0)) == null) {
                                    messages_emojigroups = null;
                                } else {
                                    messages_emojigroups = TLRPC.messages_EmojiGroups.TLdeserialize(byteBufferValue, byteBufferValue.readInt32(false), true);
                                    byteBufferValue.reuse();
                                }
                                if (messages_emojigroups instanceof TLRPC.TL_messages_emojiGroups) {
                                    callback2.run(Long.valueOf(r0.hash), (TLRPC.TL_messages_emojiGroups) messages_emojigroups);
                                } else {
                                    callback2.run(0L, null);
                                }
                                sQLiteCursor2 = sQLiteCursor;
                            } catch (Exception e) {
                                e = e;
                                FileLog.e(e);
                                callback2.run(0L, null);
                                if (sQLiteCursor == null) {
                                    sQLiteCursor2 = sQLiteCursor;
                                    sQLiteCursor2.dispose();
                                }
                                return;
                            }
                        } catch (Exception e2) {
                            e = e2;
                            sQLiteCursor = null;
                            FileLog.e(e);
                            callback2.run(0L, null);
                            if (sQLiteCursor == null) {
                            }
                        } catch (Throwable th) {
                            th = th;
                            if (sQLiteCursor2 != null) {
                                sQLiteCursor2.dispose();
                            }
                            throw th;
                        }
                    }
                    if (sQLiteCursor2 == null) {
                        return;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    sQLiteCursor2 = i;
                }
            } catch (Exception e3) {
                e = e3;
            } catch (Throwable th3) {
                th = th3;
            }
            sQLiteCursor2.dispose();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getRemote$0(Utilities.Callback4 callback4, TLObject tLObject, TLRPC.TL_error tL_error) {
            if (tLObject instanceof TLRPC.TL_messages_emojiGroupsNotModified) {
                Boolean bool = Boolean.TRUE;
                callback4.run(bool, null, 0L, bool);
            } else if (!(tLObject instanceof TLRPC.TL_messages_emojiGroups)) {
                callback4.run(Boolean.FALSE, null, 0L, Boolean.TRUE);
            } else {
                callback4.run(Boolean.FALSE, (TLRPC.TL_messages_emojiGroups) tLObject, Long.valueOf(r4.hash), Boolean.TRUE);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$setLocal$2(int i, TLRPC.TL_messages_emojiGroups tL_messages_emojiGroups, Integer num) {
            SQLitePreparedStatement executeFast;
            try {
                SQLiteDatabase database = MessagesStorage.getInstance(i).getDatabase();
                if (database != null) {
                    if (tL_messages_emojiGroups == null) {
                        executeFast = database.executeFast("DELETE FROM emoji_groups WHERE type = " + num).stepThis();
                    } else {
                        executeFast = database.executeFast("REPLACE INTO emoji_groups VALUES(?, ?)");
                        executeFast.requery();
                        NativeByteBuffer nativeByteBuffer = new NativeByteBuffer(tL_messages_emojiGroups.getObjectSize());
                        tL_messages_emojiGroups.serializeToStream(nativeByteBuffer);
                        executeFast.bindInteger(1, num.intValue());
                        executeFast.bindByteBuffer(2, nativeByteBuffer);
                        executeFast.step();
                        nativeByteBuffer.reuse();
                    }
                    executeFast.dispose();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.messenger.CacheFetcher
        public void getLocal(final int i, final Integer num, final Utilities.Callback2 callback2) {
            MessagesStorage.getInstance(i).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Components.StickerCategoriesListView$EmojiGroupFetcher$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    StickerCategoriesListView.EmojiGroupFetcher.lambda$getLocal$1(i, num, callback2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.telegram.messenger.CacheFetcher
        public void getRemote(int i, Integer num, long j, final Utilities.Callback4 callback4) {
            TLRPC.TL_messages_getEmojiGroups tL_messages_getEmojiGroups;
            if (num.intValue() == 1) {
                TLRPC.TL_messages_getEmojiStatusGroups tL_messages_getEmojiStatusGroups = new TLRPC.TL_messages_getEmojiStatusGroups();
                tL_messages_getEmojiStatusGroups.hash = (int) j;
                tL_messages_getEmojiGroups = tL_messages_getEmojiStatusGroups;
            } else if (num.intValue() == 2) {
                TLRPC.TL_messages_getEmojiProfilePhotoGroups tL_messages_getEmojiProfilePhotoGroups = new TLRPC.TL_messages_getEmojiProfilePhotoGroups();
                tL_messages_getEmojiProfilePhotoGroups.hash = (int) j;
                tL_messages_getEmojiGroups = tL_messages_getEmojiProfilePhotoGroups;
            } else if (num.intValue() == 3) {
                TLRPC.TL_messages_getEmojiStickerGroups tL_messages_getEmojiStickerGroups = new TLRPC.TL_messages_getEmojiStickerGroups();
                tL_messages_getEmojiStickerGroups.hash = (int) j;
                tL_messages_getEmojiGroups = tL_messages_getEmojiStickerGroups;
            } else {
                TLRPC.TL_messages_getEmojiGroups tL_messages_getEmojiGroups2 = new TLRPC.TL_messages_getEmojiGroups();
                tL_messages_getEmojiGroups2.hash = (int) j;
                tL_messages_getEmojiGroups = tL_messages_getEmojiGroups2;
            }
            ConnectionsManager.getInstance(i).sendRequest(tL_messages_getEmojiGroups, new RequestDelegate() { // from class: org.telegram.ui.Components.StickerCategoriesListView$EmojiGroupFetcher$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    StickerCategoriesListView.EmojiGroupFetcher.lambda$getRemote$0(Utilities.Callback4.this, tLObject, tL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.messenger.CacheFetcher
        public void setLocal(final int i, final Integer num, final TLRPC.TL_messages_emojiGroups tL_messages_emojiGroups, long j) {
            MessagesStorage.getInstance(i).getStorageQueue().postRunnable(new Runnable() { // from class: org.telegram.ui.Components.StickerCategoriesListView$EmojiGroupFetcher$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StickerCategoriesListView.EmojiGroupFetcher.lambda$setLocal$2(i, tL_messages_emojiGroups, num);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class EmojiSearch extends CacheFetcher {
        private EmojiSearch() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$getRemote$0(Utilities.Callback4 callback4, TLObject tLObject, TLRPC.TL_error tL_error) {
            if (tLObject instanceof TLRPC.TL_emojiListNotModified) {
                Boolean bool = Boolean.TRUE;
                callback4.run(bool, null, 0L, bool);
            } else if (!(tLObject instanceof TLRPC.TL_emojiList)) {
                callback4.run(Boolean.FALSE, null, 0L, Boolean.TRUE);
            } else {
                TLRPC.TL_emojiList tL_emojiList = (TLRPC.TL_emojiList) tLObject;
                callback4.run(Boolean.FALSE, tL_emojiList, Long.valueOf(tL_emojiList.hash), Boolean.TRUE);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.messenger.CacheFetcher
        public void getRemote(int i, String str, long j, final Utilities.Callback4 callback4) {
            TLRPC.TL_messages_searchCustomEmoji tL_messages_searchCustomEmoji = new TLRPC.TL_messages_searchCustomEmoji();
            tL_messages_searchCustomEmoji.emoticon = str;
            tL_messages_searchCustomEmoji.hash = j;
            ConnectionsManager.getInstance(i).sendRequest(tL_messages_searchCustomEmoji, new RequestDelegate() { // from class: org.telegram.ui.Components.StickerCategoriesListView$EmojiSearch$$ExternalSyntheticLambda0
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    StickerCategoriesListView.EmojiSearch.lambda$getRemote$0(Utilities.Callback4.this, tLObject, tL_error);
                }
            });
        }
    }

    static {
        fetcher = new EmojiGroupFetcher();
        search = new EmojiSearch();
    }

    public StickerCategoriesListView(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
        this(context, null, i, resourcesProvider);
    }

    public StickerCategoriesListView(Context context, final EmojiCategory[] emojiCategoryArr, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        this.shownButtonsAtStart = 6.5f;
        this.categories = null;
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.leftBoundAlpha = new AnimatedFloat(this, 360L, cubicBezierInterpolator);
        this.rightBoundAlpha = new AnimatedFloat(this, 360L, cubicBezierInterpolator);
        this.selectedPaint = new Paint(1);
        this.selectedCategoryIndex = -1;
        this.categoriesShownT = 0.0f;
        this.categoriesShouldShow = true;
        this.selectedAlpha = new AnimatedFloat(this, 350L, cubicBezierInterpolator);
        this.selectedIndex = new AnimatedFloat(this, 350L, cubicBezierInterpolator);
        this.rect1 = new RectF();
        this.rect2 = new RectF();
        this.rect3 = new RectF();
        this.categoriesType = i;
        setPadding(0, 0, AndroidUtilities.dp(2.0f), 0);
        Adapter adapter = new Adapter();
        this.adapter = adapter;
        setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        this.layoutManager = linearLayoutManager;
        setLayoutManager(linearLayoutManager);
        this.layoutManager.setOrientation(0);
        setSelectorRadius(AndroidUtilities.dp(15.0f));
        setSelectorType(1);
        int i2 = Theme.key_listSelector;
        setSelectorDrawableColor(getThemedColor(i2));
        this.selectedPaint.setColor(getThemedColor(i2));
        setWillNotDraw(false);
        setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.StickerCategoriesListView$$ExternalSyntheticLambda1
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view, int i3) {
                StickerCategoriesListView.this.lambda$new$1(view, i3);
            }
        });
        final long currentTimeMillis = System.currentTimeMillis();
        fetcher.fetch(UserConfig.selectedAccount, Integer.valueOf(i), new Utilities.Callback() { // from class: org.telegram.ui.Components.StickerCategoriesListView$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                StickerCategoriesListView.this.lambda$new$3(emojiCategoryArr, currentTimeMillis, (TLRPC.TL_messages_emojiGroups) obj);
            }
        });
    }

    private void drawSelectedHighlight(Canvas canvas) {
        float f = this.selectedAlpha.set(this.selectedCategoryIndex >= 0 ? 1.0f : 0.0f);
        int i = this.selectedCategoryIndex;
        float f2 = i >= 0 ? this.selectedIndex.set(i) : this.selectedIndex.get();
        if (f <= 0.0f) {
            return;
        }
        float f3 = f2 + 1.0f;
        double d = f3;
        int max = Math.max(1, (int) Math.floor(d));
        int max2 = Math.max(1, (int) Math.ceil(d));
        View view = null;
        View view2 = null;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            int childAdapterPosition = getChildAdapterPosition(childAt);
            if (childAdapterPosition == max) {
                view = childAt;
            }
            if (childAdapterPosition == max2) {
                view2 = childAt;
            }
            if (view != null && view2 != null) {
                break;
            }
        }
        int alpha = this.selectedPaint.getAlpha();
        this.selectedPaint.setAlpha((int) (alpha * f));
        if (view != null && view2 != null) {
            float f4 = max == max2 ? 0.5f : (f3 - max) / (max2 - max);
            getChildBounds(view, this.rect1);
            getChildBounds(view2, this.rect2);
            AndroidUtilities.lerp(this.rect1, this.rect2, f4, this.rect3);
            canvas.drawRoundRect(this.rect3, AndroidUtilities.dp(15.0f), AndroidUtilities.dp(15.0f), this.selectedPaint);
        }
        this.selectedPaint.setAlpha(alpha);
    }

    private void getChildBounds(View view, RectF rectF) {
        float right = (view.getRight() + view.getLeft()) / 2.0f;
        float bottom = (view.getBottom() + view.getTop()) / 2.0f;
        float width = ((view.getWidth() / 2.0f) - AndroidUtilities.dp(1.0f)) * (view instanceof CategoryButton ? ((CategoryButton) view).getScale() : 1.0f);
        rectF.set(right - width, bottom - width, right + width, bottom + width);
    }

    private int getScrollToStartWidth() {
        if (getChildCount() <= 0) {
            return 0;
        }
        View childAt = getChildAt(0);
        return childAt instanceof CategoryButton ? this.paddingWidth + Math.max(0, (getChildAdapterPosition(childAt) - 1) * getHeight()) + (-childAt.getLeft()) : -childAt.getLeft();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(EmojiCategory[] emojiCategoryArr, TLRPC.TL_messages_emojiGroups tL_messages_emojiGroups, long j) {
        this.categories = new EmojiCategory[(emojiCategoryArr == null ? 0 : emojiCategoryArr.length) + tL_messages_emojiGroups.groups.size()];
        int i = 0;
        if (emojiCategoryArr != null) {
            while (i < emojiCategoryArr.length) {
                this.categories[i] = emojiCategoryArr[i];
                i++;
            }
        }
        for (int i2 = 0; i2 < tL_messages_emojiGroups.groups.size(); i2++) {
            this.categories[i + i2] = EmojiCategory.remote(tL_messages_emojiGroups.groups.get(i2));
        }
        this.categories = preprocessCategories(this.categories);
        this.adapter.notifyDataSetChanged();
        setCategoriesShownT(0.0f);
        updateCategoriesShown(this.categoriesShouldShow, System.currentTimeMillis() - j > 16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(final EmojiCategory[] emojiCategoryArr, final long j, final TLRPC.TL_messages_emojiGroups tL_messages_emojiGroups) {
        if (tL_messages_emojiGroups != null) {
            NotificationCenter.getInstance(UserConfig.selectedAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.Components.StickerCategoriesListView$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    StickerCategoriesListView.this.lambda$new$2(emojiCategoryArr, tL_messages_emojiGroups, j);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$preload$0(int i, TLRPC.TL_messages_emojiGroups tL_messages_emojiGroups) {
        ArrayList<TLRPC.EmojiGroup> arrayList;
        if (tL_messages_emojiGroups == null || (arrayList = tL_messages_emojiGroups.groups) == null) {
            return;
        }
        Iterator<TLRPC.EmojiGroup> it = arrayList.iterator();
        while (it.hasNext()) {
            AnimatedEmojiDrawable.getDocumentFetcher(i).fetchDocument(it.next().icon_emoji_id, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scrollToSelected$4(int i) {
        onScrolled(i, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateCategoriesShown$5(ValueAnimator valueAnimator) {
        setCategoriesShownT(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onItemClick, reason: merged with bridge method [inline-methods] */
    public void lambda$new$1(int i, View view) {
        EmojiCategory[] emojiCategoryArr;
        if (i >= 1 && (emojiCategoryArr = this.categories) != null) {
            EmojiCategory emojiCategory = emojiCategoryArr[i - 1];
            int dp = AndroidUtilities.dp(64.0f);
            if (getMeasuredWidth() - view.getRight() < dp) {
                smoothScrollBy(dp - (getMeasuredWidth() - view.getRight()), 0, CubicBezierInterpolator.EASE_OUT_QUINT);
            } else if (view.getLeft() < dp) {
                smoothScrollBy(-(dp - view.getLeft()), 0, CubicBezierInterpolator.EASE_OUT_QUINT);
            }
            Utilities.Callback callback = this.onCategoryClick;
            if (callback != null) {
                callback.run(emojiCategory);
            }
        }
    }

    public static void preload(final int i, int i2) {
        fetcher.fetch(i, Integer.valueOf(i2), new Utilities.Callback() { // from class: org.telegram.ui.Components.StickerCategoriesListView$$ExternalSyntheticLambda0
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                StickerCategoriesListView.lambda$preload$0(i, (TLRPC.TL_messages_emojiGroups) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCategoriesShownT(float f) {
        this.categoriesShownT = f;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof CategoryButton) {
                float cascade = AndroidUtilities.cascade(f, (getChildCount() - 1) - getChildAdapterPosition(childAt), getChildCount() - 1, 3.0f);
                if (cascade > 0.0f && childAt.getAlpha() <= 0.0f) {
                    ((CategoryButton) childAt).play(false);
                }
                childAt.setAlpha(cascade);
                childAt.setScaleX(cascade);
                childAt.setScaleY(cascade);
            }
        }
        invalidate();
    }

    @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            View findChildViewUnder = findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (!(findChildViewUnder instanceof CategoryButton) || findChildViewUnder.getAlpha() < 0.5f) {
                return false;
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void draw(Canvas canvas) {
        Drawable drawable;
        if (this.backgroundPaint != null) {
            int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
            int i2 = Integer.MIN_VALUE;
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (childAt instanceof CategoryButton) {
                    i = Math.min(i, childAt.getLeft());
                    i2 = Math.max(i2, childAt.getRight());
                }
            }
            if (i < i2) {
                int width = (int) (i + ((getWidth() + AndroidUtilities.dp(32.0f)) * (1.0f - this.categoriesShownT)));
                int width2 = (int) (i2 + ((getWidth() + AndroidUtilities.dp(32.0f)) * (1.0f - this.categoriesShownT)));
                canvas.drawRect(width, 0.0f, width2, getHeight(), this.backgroundPaint);
                if (width2 < getWidth() && (drawable = this.leftBoundDrawable) != null) {
                    drawable.setAlpha(NotificationCenter.notificationsCountUpdated);
                    Drawable drawable2 = this.leftBoundDrawable;
                    drawable2.setBounds(width2, 0, drawable2.getIntrinsicWidth() + width2, getHeight());
                    this.leftBoundDrawable.draw(canvas);
                }
            }
        }
        drawSelectedHighlight(canvas);
        super.draw(canvas);
        Drawable drawable3 = this.leftBoundDrawable;
        if (drawable3 != null) {
            drawable3.setAlpha((int) (255.0f * this.leftBoundAlpha.set((canScrollHorizontally(-1) && this.scrolledFully) ? 1.0f : 0.0f) * this.categoriesShownT));
            if (this.leftBoundDrawable.getAlpha() > 0) {
                Drawable drawable4 = this.leftBoundDrawable;
                drawable4.setBounds(0, 0, drawable4.getIntrinsicWidth(), getHeight());
                this.leftBoundDrawable.draw(canvas);
            }
        }
    }

    public int getCategoryIndex() {
        return this.selectedCategoryIndex;
    }

    public EmojiCategory getSelectedCategory() {
        int i;
        EmojiCategory[] emojiCategoryArr = this.categories;
        if (emojiCategoryArr == null || (i = this.selectedCategoryIndex) < 0 || i >= emojiCategoryArr.length) {
            return null;
        }
        return emojiCategoryArr[i];
    }

    public boolean isCategoriesShown() {
        return this.categoriesShownT > 0.5f;
    }

    public boolean isScrolledIntoOccupiedWidth() {
        return this.scrolledIntoOccupiedWidth;
    }

    protected abstract boolean isTabIconsAnimationEnabled(boolean z);

    @Override // org.telegram.ui.Components.RecyclerListView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateCategoriesShown(this.categoriesShouldShow, false);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        View view = this.paddingView;
        if (view != null) {
            view.requestLayout();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0025  */
    @Override // androidx.recyclerview.widget.RecyclerView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onScrolled(int i, int i2) {
        boolean z;
        boolean z2;
        boolean z3;
        Utilities.Callback callback;
        super.onScrolled(i, i2);
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            z2 = true;
            if (childAt instanceof CategoryButton) {
                z = true;
            } else if (childAt.getRight() <= this.dontOccupyWidth) {
                z = false;
            }
            z3 = this.scrolledIntoOccupiedWidth;
            if (z3 == z2) {
                this.scrolledIntoOccupiedWidth = z2;
                Utilities.Callback callback2 = this.onScrollIntoOccupiedWidth;
                if (callback2 != null) {
                    callback2.run(Integer.valueOf(z2 ? Math.max(0, getScrollToStartWidth() - (this.paddingWidth - this.dontOccupyWidth)) : 0));
                }
                invalidate();
            } else if (z3 && (callback = this.onScrollIntoOccupiedWidth) != null) {
                callback.run(Integer.valueOf(Math.max(0, getScrollToStartWidth() - (this.paddingWidth - this.dontOccupyWidth))));
            }
            if (this.scrolledFully == z) {
                this.scrolledFully = z;
                Utilities.Callback callback3 = this.onScrollFully;
                if (callback3 != null) {
                    callback3.run(Boolean.valueOf(z));
                }
                invalidate();
                return;
            }
            return;
        }
        z = false;
        z2 = false;
        z3 = this.scrolledIntoOccupiedWidth;
        if (z3 == z2) {
        }
        if (this.scrolledFully == z) {
        }
    }

    protected EmojiCategory[] preprocessCategories(EmojiCategory[] emojiCategoryArr) {
        return emojiCategoryArr;
    }

    public void scrollToSelected() {
        final int max = ((-getScrollToStartWidth()) - Math.max(0, this.dontOccupyWidth)) + (this.selectedCategoryIndex * AndroidUtilities.dp(34.0f));
        scrollBy(max, 0);
        post(new Runnable() { // from class: org.telegram.ui.Components.StickerCategoriesListView$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                StickerCategoriesListView.this.lambda$scrollToSelected$4(max);
            }
        });
    }

    public void scrollToStart() {
        smoothScrollBy(-getScrollToStartWidth(), 0, CubicBezierInterpolator.EASE_OUT_QUINT);
    }

    public void selectCategory(int i) {
        if (this.selectedCategoryIndex < 0 && i >= 0) {
            this.selectedIndex.set(i, true);
        }
        this.selectedCategoryIndex = i;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            if (childAt instanceof CategoryButton) {
                ((CategoryButton) childAt).setSelected(this.selectedCategoryIndex == getChildAdapterPosition(childAt) - 1, true);
            }
        }
        invalidate();
    }

    public void selectCategory(EmojiCategory emojiCategory) {
        int i;
        if (this.categories != null) {
            i = 0;
            while (true) {
                EmojiCategory[] emojiCategoryArr = this.categories;
                if (i >= emojiCategoryArr.length) {
                    break;
                } else if (emojiCategoryArr[i] == emojiCategory) {
                    break;
                } else {
                    i++;
                }
            }
            selectCategory(i);
        }
        i = -1;
        selectCategory(i);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        if (this.backgroundPaint == null) {
            this.backgroundPaint = new Paint(1);
        }
        this.backgroundPaint.setColor(i);
        Drawable mutate = getContext().getResources().getDrawable(R.drawable.gradient_right).mutate();
        this.leftBoundDrawable = mutate;
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        mutate.setColorFilter(new PorterDuffColorFilter(i, mode));
        Drawable mutate2 = getContext().getResources().getDrawable(R.drawable.gradient_left).mutate();
        this.rightBoundDrawable = mutate2;
        mutate2.setColorFilter(new PorterDuffColorFilter(i, mode));
    }

    public void setDontOccupyWidth(int i) {
        this.dontOccupyWidth = i;
    }

    public void setOnCategoryClick(Utilities.Callback<EmojiCategory> callback) {
        this.onCategoryClick = callback;
    }

    public void setOnScrollFully(Utilities.Callback<Boolean> callback) {
        this.onScrollFully = callback;
    }

    public void setOnScrollIntoOccupiedWidth(Utilities.Callback<Integer> callback) {
        this.onScrollIntoOccupiedWidth = callback;
    }

    public void setShownButtonsAtStart(float f) {
        this.shownButtonsAtStart = f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v1, types: [int] */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v9 */
    public void updateCategoriesShown(boolean z, boolean z2) {
        this.categoriesShouldShow = z;
        ?? r5 = z;
        if (this.categories == null) {
            r5 = 0;
        }
        if (this.categoriesShownT == ((float) r5)) {
            return;
        }
        ValueAnimator valueAnimator = this.categoriesShownAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.categoriesShownAnimator = null;
        }
        if (!z2) {
            setCategoriesShownT(r5 != 0 ? 1.0f : 0.0f);
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.categoriesShownT, r5 != 0 ? 1.0f : 0.0f);
        this.categoriesShownAnimator = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.StickerCategoriesListView$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                StickerCategoriesListView.this.lambda$updateCategoriesShown$5(valueAnimator2);
            }
        });
        this.categoriesShownAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.StickerCategoriesListView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                StickerCategoriesListView stickerCategoriesListView = StickerCategoriesListView.this;
                stickerCategoriesListView.setCategoriesShownT(((Float) stickerCategoriesListView.categoriesShownAnimator.getAnimatedValue()).floatValue());
                StickerCategoriesListView.this.categoriesShownAnimator = null;
            }
        });
        this.categoriesShownAnimator.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        this.categoriesShownAnimator.setDuration((this.categories == null ? 5 : r6.length) * 120);
        this.categoriesShownAnimator.start();
    }
}
