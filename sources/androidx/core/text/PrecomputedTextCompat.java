package androidx.core.text;

import android.graphics.Typeface;
import android.os.Build;
import android.os.LocaleList;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import androidx.core.util.ObjectsCompat;
import java.util.Locale;

/* loaded from: classes.dex */
public abstract class PrecomputedTextCompat implements Spannable {

    /* loaded from: classes.dex */
    public static final class Params {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        private final TextPaint mPaint;
        private final TextDirectionHeuristic mTextDir;
        final PrecomputedText.Params mWrapped;

        /* loaded from: classes.dex */
        public static class Builder {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;

            public Builder(TextPaint textPaint) {
                this.mPaint = textPaint;
                if (Build.VERSION.SDK_INT >= 23) {
                    this.mBreakStrategy = 1;
                    this.mHyphenationFrequency = 1;
                } else {
                    this.mHyphenationFrequency = 0;
                    this.mBreakStrategy = 0;
                }
                this.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            }

            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }

            public Builder setBreakStrategy(int i) {
                this.mBreakStrategy = i;
                return this;
            }

            public Builder setHyphenationFrequency(int i) {
                this.mHyphenationFrequency = i;
                return this;
            }

            public Builder setTextDirection(TextDirectionHeuristic textDirectionHeuristic) {
                this.mTextDir = textDirectionHeuristic;
                return this;
            }
        }

        public Params(PrecomputedText.Params params) {
            TextPaint textPaint;
            TextDirectionHeuristic textDirection;
            int breakStrategy;
            int hyphenationFrequency;
            textPaint = params.getTextPaint();
            this.mPaint = textPaint;
            textDirection = params.getTextDirection();
            this.mTextDir = textDirection;
            breakStrategy = params.getBreakStrategy();
            this.mBreakStrategy = breakStrategy;
            hyphenationFrequency = params.getHyphenationFrequency();
            this.mHyphenationFrequency = hyphenationFrequency;
            this.mWrapped = Build.VERSION.SDK_INT < 29 ? null : params;
        }

        Params(TextPaint textPaint, TextDirectionHeuristic textDirectionHeuristic, int i, int i2) {
            PrecomputedText.Params params;
            PrecomputedText.Params.Builder breakStrategy;
            PrecomputedText.Params.Builder hyphenationFrequency;
            PrecomputedText.Params.Builder textDirection;
            if (Build.VERSION.SDK_INT >= 29) {
                breakStrategy = new PrecomputedText.Params.Builder(textPaint).setBreakStrategy(i);
                hyphenationFrequency = breakStrategy.setHyphenationFrequency(i2);
                textDirection = hyphenationFrequency.setTextDirection(textDirectionHeuristic);
                params = textDirection.build();
            } else {
                params = null;
            }
            this.mWrapped = params;
            this.mPaint = textPaint;
            this.mTextDir = textDirectionHeuristic;
            this.mBreakStrategy = i;
            this.mHyphenationFrequency = i2;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Params)) {
                return false;
            }
            Params params = (Params) obj;
            return equalsWithoutTextDirection(params) && this.mTextDir == params.getTextDirection();
        }

        public boolean equalsWithoutTextDirection(Params params) {
            LocaleList textLocales;
            LocaleList textLocales2;
            boolean equals;
            float letterSpacing;
            float letterSpacing2;
            String fontFeatureSettings;
            String fontFeatureSettings2;
            int i = Build.VERSION.SDK_INT;
            if ((i >= 23 && (this.mBreakStrategy != params.getBreakStrategy() || this.mHyphenationFrequency != params.getHyphenationFrequency())) || this.mPaint.getTextSize() != params.getTextPaint().getTextSize() || this.mPaint.getTextScaleX() != params.getTextPaint().getTextScaleX() || this.mPaint.getTextSkewX() != params.getTextPaint().getTextSkewX()) {
                return false;
            }
            if (i >= 21) {
                letterSpacing = this.mPaint.getLetterSpacing();
                letterSpacing2 = params.getTextPaint().getLetterSpacing();
                if (letterSpacing != letterSpacing2) {
                    return false;
                }
                fontFeatureSettings = this.mPaint.getFontFeatureSettings();
                fontFeatureSettings2 = params.getTextPaint().getFontFeatureSettings();
                if (!TextUtils.equals(fontFeatureSettings, fontFeatureSettings2)) {
                    return false;
                }
            }
            if (this.mPaint.getFlags() != params.getTextPaint().getFlags()) {
                return false;
            }
            if (i >= 24) {
                textLocales = this.mPaint.getTextLocales();
                textLocales2 = params.getTextPaint().getTextLocales();
                equals = textLocales.equals(textLocales2);
                if (!equals) {
                    return false;
                }
            } else if (!this.mPaint.getTextLocale().equals(params.getTextPaint().getTextLocale())) {
                return false;
            }
            return this.mPaint.getTypeface() == null ? params.getTextPaint().getTypeface() == null : this.mPaint.getTypeface().equals(params.getTextPaint().getTypeface());
        }

        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }

        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }

        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        public int hashCode() {
            float letterSpacing;
            boolean isElegantTextHeight;
            float letterSpacing2;
            LocaleList textLocales;
            boolean isElegantTextHeight2;
            int i = Build.VERSION.SDK_INT;
            if (i >= 24) {
                Float valueOf = Float.valueOf(this.mPaint.getTextSize());
                Float valueOf2 = Float.valueOf(this.mPaint.getTextScaleX());
                Float valueOf3 = Float.valueOf(this.mPaint.getTextSkewX());
                letterSpacing2 = this.mPaint.getLetterSpacing();
                Float valueOf4 = Float.valueOf(letterSpacing2);
                Integer valueOf5 = Integer.valueOf(this.mPaint.getFlags());
                textLocales = this.mPaint.getTextLocales();
                Typeface typeface = this.mPaint.getTypeface();
                isElegantTextHeight2 = this.mPaint.isElegantTextHeight();
                return ObjectsCompat.hash(valueOf, valueOf2, valueOf3, valueOf4, valueOf5, textLocales, typeface, Boolean.valueOf(isElegantTextHeight2), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            }
            if (i < 21) {
                return ObjectsCompat.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
            }
            Float valueOf6 = Float.valueOf(this.mPaint.getTextSize());
            Float valueOf7 = Float.valueOf(this.mPaint.getTextScaleX());
            Float valueOf8 = Float.valueOf(this.mPaint.getTextSkewX());
            letterSpacing = this.mPaint.getLetterSpacing();
            Float valueOf9 = Float.valueOf(letterSpacing);
            Integer valueOf10 = Integer.valueOf(this.mPaint.getFlags());
            Locale textLocale = this.mPaint.getTextLocale();
            Typeface typeface2 = this.mPaint.getTypeface();
            isElegantTextHeight = this.mPaint.isElegantTextHeight();
            return ObjectsCompat.hash(valueOf6, valueOf7, valueOf8, valueOf9, valueOf10, textLocale, typeface2, Boolean.valueOf(isElegantTextHeight), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
        }

        public String toString() {
            StringBuilder sb;
            Object textLocale;
            String fontVariationSettings;
            float letterSpacing;
            boolean isElegantTextHeight;
            StringBuilder sb2 = new StringBuilder("{");
            sb2.append("textSize=" + this.mPaint.getTextSize());
            sb2.append(", textScaleX=" + this.mPaint.getTextScaleX());
            sb2.append(", textSkewX=" + this.mPaint.getTextSkewX());
            int i = Build.VERSION.SDK_INT;
            if (i >= 21) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(", letterSpacing=");
                letterSpacing = this.mPaint.getLetterSpacing();
                sb3.append(letterSpacing);
                sb2.append(sb3.toString());
                StringBuilder sb4 = new StringBuilder();
                sb4.append(", elegantTextHeight=");
                isElegantTextHeight = this.mPaint.isElegantTextHeight();
                sb4.append(isElegantTextHeight);
                sb2.append(sb4.toString());
            }
            if (i >= 24) {
                sb = new StringBuilder();
                sb.append(", textLocale=");
                textLocale = this.mPaint.getTextLocales();
            } else {
                sb = new StringBuilder();
                sb.append(", textLocale=");
                textLocale = this.mPaint.getTextLocale();
            }
            sb.append(textLocale);
            sb2.append(sb.toString());
            sb2.append(", typeface=" + this.mPaint.getTypeface());
            if (i >= 26) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(", variationSettings=");
                fontVariationSettings = this.mPaint.getFontVariationSettings();
                sb5.append(fontVariationSettings);
                sb2.append(sb5.toString());
            }
            sb2.append(", textDir=" + this.mTextDir);
            sb2.append(", breakStrategy=" + this.mBreakStrategy);
            sb2.append(", hyphenationFrequency=" + this.mHyphenationFrequency);
            sb2.append("}");
            return sb2.toString();
        }
    }
}
