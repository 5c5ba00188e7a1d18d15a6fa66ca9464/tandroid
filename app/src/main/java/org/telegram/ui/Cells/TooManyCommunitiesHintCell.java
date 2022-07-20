package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.text.TextPaint;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;
/* loaded from: classes3.dex */
public class TooManyCommunitiesHintCell extends FrameLayout {
    private TextView headerTextView;
    private FrameLayout imageLayout;
    private ImageView imageView;
    private TextView messageTextView;

    public TooManyCommunitiesHintCell(Context context) {
        super(context);
        ImageView imageView = new ImageView(context);
        this.imageView = imageView;
        imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor("chats_nameMessage_threeLines"), PorterDuff.Mode.MULTIPLY));
        TextView textView = new TextView(context);
        this.headerTextView = textView;
        textView.setTextColor(Theme.getColor("chats_nameMessage_threeLines"));
        this.headerTextView.setTextSize(1, 20.0f);
        this.headerTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.headerTextView.setGravity(17);
        addView(this.headerTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 52.0f, 75.0f, 52.0f, 0.0f));
        TextView textView2 = new TextView(context);
        this.messageTextView = textView2;
        textView2.setTextColor(Theme.getColor("chats_message"));
        this.messageTextView.setTextSize(1, 14.0f);
        this.messageTextView.setGravity(17);
        addView(this.messageTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 36.0f, 110.0f, 36.0f, 0.0f));
        TextPaint textPaint = new TextPaint(1);
        textPaint.setColor(-1);
        textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        textPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(this, context, new Paint(1), textPaint, "500");
        this.imageLayout = anonymousClass1;
        anonymousClass1.setWillNotDraw(false);
        this.imageLayout.addView(this.imageView, LayoutHelper.createFrame(-2, -2, 1));
        addView(this.imageLayout, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 12.0f, 0.0f, 6.0f));
        this.headerTextView.setText(LocaleController.getString("TooManyCommunities", 2131628739));
        this.imageView.setImageResource(2131165442);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.telegram.ui.Cells.TooManyCommunitiesHintCell$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends FrameLayout {
        RectF rect = new RectF();
        final /* synthetic */ Paint val$paint;
        final /* synthetic */ String val$s;
        final /* synthetic */ TextPaint val$textPaint;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(TooManyCommunitiesHintCell tooManyCommunitiesHintCell, Context context, Paint paint, TextPaint textPaint, String str) {
            super(context);
            this.val$paint = paint;
            this.val$textPaint = textPaint;
            this.val$s = str;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.val$paint.setColor(Theme.getColor("windowBackgroundWhiteRedText"));
            canvas.save();
            canvas.translate((getMeasuredWidth() - this.val$textPaint.measureText(this.val$s)) - AndroidUtilities.dp(8.0f), AndroidUtilities.dpf2(7.0f));
            this.rect.set(0.0f, 0.0f, this.val$textPaint.measureText(this.val$s), this.val$textPaint.getTextSize());
            this.rect.inset(-AndroidUtilities.dp(6.0f), -AndroidUtilities.dp(3.0f));
            float textSize = (this.val$textPaint.getTextSize() / 2.0f) + AndroidUtilities.dp(3.0f);
            canvas.drawRoundRect(this.rect, textSize, textSize, this.val$paint);
            canvas.drawText(this.val$s, 0.0f, this.val$textPaint.getTextSize() - AndroidUtilities.dpf2(2.0f), this.val$textPaint);
            canvas.restore();
        }
    }

    public void setMessageText(String str) {
        this.messageTextView.setText(str);
    }
}
