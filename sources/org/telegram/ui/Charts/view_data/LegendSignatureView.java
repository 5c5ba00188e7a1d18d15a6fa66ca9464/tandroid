package org.telegram.ui.Charts.view_data;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Stars.StarsIntroActivity;

/* loaded from: classes4.dex */
public class LegendSignatureView extends FrameLayout {
    Drawable backgroundDrawable;
    public boolean canGoZoom;
    public ImageView chevron;
    LinearLayout content;
    SimpleDateFormat format;
    SimpleDateFormat format2;
    SimpleDateFormat format3;
    SimpleDateFormat format4;
    private DecimalFormat formatterTON;
    Holder[] holders;
    SimpleDateFormat hourFormat;
    TextView hourTime;
    public boolean isTopHourChart;
    private RadialProgressView progressView;
    private Theme.ResourcesProvider resourcesProvider;
    Drawable shadowDrawable;
    public boolean showPercentage;
    Runnable showProgressRunnable;
    TextView time;
    public boolean useHour;
    public boolean useWeek;
    public boolean zoomEnabled;

    class Holder {
        TextView percentage;
        final LinearLayout root;
        final TextView signature;
        final AnimatedEmojiSpan.TextViewEmojis value;

        Holder() {
            LinearLayout linearLayout = new LinearLayout(LegendSignatureView.this.getContext());
            this.root = linearLayout;
            linearLayout.setPadding(AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f));
            if (LegendSignatureView.this.showPercentage) {
                TextView textView = new TextView(LegendSignatureView.this.getContext());
                this.percentage = textView;
                linearLayout.addView(textView);
                this.percentage.getLayoutParams().width = AndroidUtilities.dp(36.0f);
                this.percentage.setVisibility(8);
                this.percentage.setTypeface(AndroidUtilities.bold());
                this.percentage.setTextSize(1, 13.0f);
            }
            TextView textView2 = new TextView(LegendSignatureView.this.getContext());
            this.signature = textView2;
            linearLayout.addView(textView2, LayoutHelper.createLinear(-2, -2, 0.0f, 0.0f, 20.0f, 0.0f));
            AnimatedEmojiSpan.TextViewEmojis textViewEmojis = new AnimatedEmojiSpan.TextViewEmojis(LegendSignatureView.this.getContext());
            this.value = textViewEmojis;
            linearLayout.addView(textViewEmojis, LayoutHelper.createLinear(-1, -2));
            textView2.setGravity(8388611);
            textViewEmojis.setGravity(8388613);
            textViewEmojis.setTypeface(AndroidUtilities.bold());
            textViewEmojis.setTextSize(1, 13.0f);
            textView2.setTextSize(1, 13.0f);
        }
    }

    public LegendSignatureView(Context context) {
        this(context, null);
    }

    public LegendSignatureView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.format = new SimpleDateFormat("E, ");
        this.format2 = new SimpleDateFormat("MMM dd");
        this.format3 = new SimpleDateFormat("d MMM yyyy");
        this.format4 = new SimpleDateFormat("d MMM");
        this.hourFormat = new SimpleDateFormat(" HH:mm");
        this.canGoZoom = true;
        this.showProgressRunnable = new Runnable() { // from class: org.telegram.ui.Charts.view_data.LegendSignatureView.1
            @Override // java.lang.Runnable
            public void run() {
                LegendSignatureView.this.chevron.animate().setDuration(120L).alpha(0.0f);
                LegendSignatureView.this.progressView.animate().setListener(null).start();
                if (LegendSignatureView.this.progressView.getVisibility() != 0) {
                    LegendSignatureView.this.progressView.setVisibility(0);
                    LegendSignatureView.this.progressView.setAlpha(0.0f);
                }
                LegendSignatureView.this.progressView.animate().setDuration(120L).alpha(1.0f).start();
            }
        };
        this.resourcesProvider = resourcesProvider;
        setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.content = linearLayout;
        linearLayout.setOrientation(1);
        TextView textView = new TextView(context);
        this.time = textView;
        textView.setTextSize(1, 14.0f);
        this.time.setTypeface(AndroidUtilities.bold());
        TextView textView2 = new TextView(context);
        this.hourTime = textView2;
        textView2.setTextSize(1, 14.0f);
        this.hourTime.setTypeface(AndroidUtilities.bold());
        ImageView imageView = new ImageView(context);
        this.chevron = imageView;
        imageView.setImageResource(R.drawable.ic_chevron_right_black_18dp);
        RadialProgressView radialProgressView = new RadialProgressView(context);
        this.progressView = radialProgressView;
        radialProgressView.setSize(AndroidUtilities.dp(12.0f));
        this.progressView.setStrokeWidth(AndroidUtilities.dp(0.5f));
        this.progressView.setVisibility(8);
        addView(this.content, LayoutHelper.createFrame(-2, -2.0f, 0, 0.0f, 22.0f, 0.0f, 0.0f));
        addView(this.time, LayoutHelper.createFrame(-2, -2.0f, 8388611, 4.0f, 0.0f, 4.0f, 0.0f));
        addView(this.hourTime, LayoutHelper.createFrame(-2, -2.0f, 8388613, 4.0f, 0.0f, 4.0f, 0.0f));
        addView(this.chevron, LayoutHelper.createFrame(18, 18.0f, 8388661, 0.0f, 2.0f, 0.0f, 0.0f));
        addView(this.progressView, LayoutHelper.createFrame(18, 18.0f, 8388661, 0.0f, 2.0f, 0.0f, 0.0f));
        recolor();
    }

    private String capitalize(String str) {
        if (str.length() <= 0) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private String formatData(Date date) {
        if (this.useHour) {
            return capitalize(this.format2.format(date));
        }
        return capitalize(this.format.format(date)) + capitalize(this.format2.format(date));
    }

    /*  JADX ERROR: ConcurrentModificationException in pass: ConstructorVisitor
        java.util.ConcurrentModificationException
        	at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1013)
        	at java.base/java.util.ArrayList$Itr.next(ArrayList.java:967)
        	at jadx.core.dex.visitors.ConstructorVisitor.insertPhiInsn(ConstructorVisitor.java:139)
        	at jadx.core.dex.visitors.ConstructorVisitor.processInvoke(ConstructorVisitor.java:91)
        	at jadx.core.dex.visitors.ConstructorVisitor.replaceInvoke(ConstructorVisitor.java:56)
        	at jadx.core.dex.visitors.ConstructorVisitor.visit(ConstructorVisitor.java:42)
        */
    public java.lang.CharSequence formatWholeNumber(
    /*  JADX ERROR: ConcurrentModificationException in pass: ConstructorVisitor
        java.util.ConcurrentModificationException
        	at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1013)
        	at java.base/java.util.ArrayList$Itr.next(ArrayList.java:967)
        	at jadx.core.dex.visitors.ConstructorVisitor.insertPhiInsn(ConstructorVisitor.java:139)
        	at jadx.core.dex.visitors.ConstructorVisitor.processInvoke(ConstructorVisitor.java:91)
        	at jadx.core.dex.visitors.ConstructorVisitor.replaceInvoke(ConstructorVisitor.java:56)
        */
    /*  JADX ERROR: Method generation error
        jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r6v0 ??
        	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:238)
        	at jadx.core.codegen.MethodGen.addMethodArguments(MethodGen.java:223)
        	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:168)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:401)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:335)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:301)
        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
        	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        */

    public void recolor() {
        TextView textView = this.time;
        int i = Theme.key_dialogTextBlack;
        textView.setTextColor(Theme.getColor(i, this.resourcesProvider));
        this.hourTime.setTextColor(Theme.getColor(i, this.resourcesProvider));
        ImageView imageView = this.chevron;
        int i2 = Theme.key_statisticChartChevronColor;
        imageView.setColorFilter(Theme.getColor(i2, this.resourcesProvider));
        this.progressView.setProgressColor(Theme.getColor(i2, this.resourcesProvider));
        this.shadowDrawable = getContext().getResources().getDrawable(R.drawable.stats_tooltip).mutate();
        this.backgroundDrawable = Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(4.0f), Theme.getColor(Theme.key_dialogBackground, this.resourcesProvider), Theme.getColor(Theme.key_listSelector, this.resourcesProvider), -16777216);
        CombinedDrawable combinedDrawable = new CombinedDrawable(this.shadowDrawable, this.backgroundDrawable, AndroidUtilities.dp(3.0f), AndroidUtilities.dp(3.0f));
        combinedDrawable.setFullsize(true);
        setBackground(combinedDrawable);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x00b3  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x022d  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0244  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setData(int i, long j, ArrayList arrayList, boolean z, int i2, float f) {
        TextView textView;
        String formatData;
        TextView textView2;
        String format;
        int i3;
        int i4;
        TextView textView3;
        CharSequence charSequence;
        AnimatedEmojiSpan.TextViewEmojis textViewEmojis;
        int i5;
        int i6;
        long j2;
        TextView textView4;
        int length = this.holders.length;
        int i7 = 2;
        if (z) {
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTransition(new Fade(2).setDuration(150L)).addTransition(new ChangeBounds().setDuration(150L)).addTransition(new Fade(1).setDuration(150L));
            transitionSet.setOrdering(0);
            TransitionManager.beginDelayedTransition(this, transitionSet);
        }
        if (!this.isTopHourChart) {
            if (this.useWeek) {
                textView = this.time;
                formatData = String.format("%s — %s", this.format4.format(new Date(j)), this.format3.format(new Date(604800000 + j)));
            } else {
                textView = this.time;
                formatData = formatData(new Date(j));
            }
            textView.setText(formatData);
            if (this.useHour) {
                textView2 = this.hourTime;
                format = this.hourFormat.format(Long.valueOf(j));
            }
            long j3 = 0;
            for (i3 = 0; i3 < arrayList.size(); i3++) {
                if (((LineViewData) arrayList.get(i3)).enabled) {
                    j3 += ((LineViewData) arrayList.get(i3)).line.y[i];
                }
            }
            i4 = 0;
            while (i4 < length) {
                Holder holder = this.holders[i4];
                int i8 = i4 % 2;
                LineViewData lineViewData = (LineViewData) arrayList.get((i2 == 1 || i2 == i7) ? i4 / 2 : i4);
                if (lineViewData.enabled) {
                    if (holder.root.getMeasuredHeight() == 0) {
                        holder.root.requestLayout();
                    }
                    holder.root.setVisibility(0);
                    AnimatedEmojiSpan.TextViewEmojis textViewEmojis2 = holder.value;
                    int i9 = i4;
                    long j4 = j3;
                    textViewEmojis2.setText(formatWholeNumber(lineViewData.line.y[i], i2, i8, textViewEmojis2, f));
                    if (i2 == 1) {
                        textView3 = holder.signature;
                        charSequence = LocaleController.formatString(i8 == 0 ? R.string.ChartInTON : R.string.ChartInUSD, lineViewData.line.name);
                    } else if (i2 == 2) {
                        textView3 = holder.signature;
                        charSequence = StarsIntroActivity.replaceStarsWithPlain(LocaleController.formatString(i8 == 0 ? R.string.ChartInXTR : R.string.ChartInUSD, lineViewData.line.name), 0.7f);
                    } else {
                        textView3 = holder.signature;
                        charSequence = lineViewData.line.name;
                    }
                    textView3.setText(charSequence);
                    int i10 = lineViewData.line.colorKey;
                    if (i10 < 0 || !Theme.hasThemeKey(i10)) {
                        textViewEmojis = holder.value;
                        i5 = Theme.getCurrentTheme().isDark() ? lineViewData.line.colorDark : lineViewData.line.color;
                    } else {
                        textViewEmojis = holder.value;
                        i5 = Theme.getColor(lineViewData.line.colorKey, this.resourcesProvider);
                    }
                    textViewEmojis.setTextColor(i5);
                    TextView textView5 = holder.signature;
                    int i11 = Theme.key_dialogTextBlack;
                    textView5.setTextColor(Theme.getColor(i11, this.resourcesProvider));
                    if (!this.showPercentage || (textView4 = holder.percentage) == null) {
                        i6 = i9;
                        j2 = j4;
                    } else {
                        textView4.setVisibility(0);
                        holder.percentage.setTextColor(Theme.getColor(i11, this.resourcesProvider));
                        i6 = i9;
                        j2 = j4;
                        float f2 = ((LineViewData) arrayList.get(i6)).line.y[i] / j2;
                        if (f2 >= 0.1f || f2 == 0.0f) {
                            holder.percentage.setText(String.format(Locale.ENGLISH, "%d%s", Integer.valueOf(Math.round(f2 * 100.0f)), "%"));
                            i4 = i6 + 1;
                            j3 = j2;
                            i7 = 2;
                        } else {
                            holder.percentage.setText(String.format(Locale.ENGLISH, "%.1f%s", Float.valueOf(f2 * 100.0f), "%"));
                        }
                    }
                } else {
                    holder.root.setVisibility(8);
                    i6 = i4;
                    j2 = j3;
                }
                i4 = i6 + 1;
                j3 = j2;
                i7 = 2;
            }
            long j5 = j3;
            if (this.zoomEnabled) {
                this.canGoZoom = false;
                this.chevron.setVisibility(8);
                return;
            } else {
                this.canGoZoom = j5 > 0;
                this.chevron.setVisibility(j5 <= 0 ? 8 : 0);
                return;
            }
        }
        textView2 = this.time;
        format = String.format(Locale.ENGLISH, "%02d:00", Long.valueOf(j));
        textView2.setText(format);
        long j32 = 0;
        while (i3 < arrayList.size()) {
        }
        i4 = 0;
        while (i4 < length) {
        }
        long j52 = j32;
        if (this.zoomEnabled) {
        }
    }

    public void setSize(int i) {
        this.content.removeAllViews();
        this.holders = new Holder[i];
        for (int i2 = 0; i2 < i; i2++) {
            this.holders[i2] = new Holder();
            this.content.addView(this.holders[i2].root);
        }
    }

    public void setUseWeek(boolean z) {
        this.useWeek = z;
    }

    public void showProgress(boolean z, boolean z2) {
        if (z) {
            AndroidUtilities.runOnUIThread(this.showProgressRunnable, 300L);
            return;
        }
        AndroidUtilities.cancelRunOnUIThread(this.showProgressRunnable);
        if (z2) {
            this.progressView.setVisibility(8);
            return;
        }
        this.chevron.animate().setDuration(80L).alpha(1.0f).start();
        if (this.progressView.getVisibility() == 0) {
            this.progressView.animate().setDuration(80L).alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Charts.view_data.LegendSignatureView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    LegendSignatureView.this.progressView.setVisibility(8);
                }
            }).start();
        }
    }
}
