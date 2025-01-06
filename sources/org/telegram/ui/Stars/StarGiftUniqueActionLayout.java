package org.telegram.ui.Stars;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_stars;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.ButtonBounce;
import org.telegram.ui.Components.Text;
import org.telegram.ui.Gifts.GiftSheet;
import org.telegram.ui.Stars.StarsReactionsSheet;

/* loaded from: classes4.dex */
public class StarGiftUniqueActionLayout {
    TLRPC.TL_messageActionStarGiftUnique action;
    private boolean attached;
    private TL_stars.starGiftAttributeBackdrop backdrop;
    private final ButtonBounce bounce;
    private final ButtonBounce buttonBounce;
    private float buttonHeight;
    private Text buttonText;
    private float buttonY;
    private final int currentAccount;
    MessageObject currentMessageObject;
    private final AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable emoji;
    private RadialGradient gradient;
    private int gradientRadius;
    int height;
    public final ImageReceiver imageReceiver;
    private TL_stars.starGiftAttributeModel model;
    private float nameWidth;
    private TL_stars.starGiftAttributePattern pattern;
    public boolean repost;
    private final Theme.ResourcesProvider resourcesProvider;
    private final GiftSheet.RibbonDrawable ribbon;
    private Text subtitle;
    private float subtitleY;
    private Text title;
    private float titleY;
    private float valueWidth;
    private final ChatActionCell view;
    int width;
    private final Paint backgroundPaint = new Paint(1);
    private final Matrix matrix = new Matrix();
    private final RectF backgroundRect = new RectF();
    private final Path backgroundPath = new Path();
    private final ArrayList table = new ArrayList();
    private final RectF buttonRect = new RectF();
    private final Path buttonPath = new Path();
    private final Paint buttonBackgroundPaint = new Paint();
    private final StarsReactionsSheet.Particles buttonParticles = new StarsReactionsSheet.Particles(1, 25);

    private static final class Row {
        public final Text name;
        public final Text value;
        public final float y;

        public Row(float f, CharSequence charSequence, CharSequence charSequence2) {
            this.name = new Text(charSequence, 12.0f);
            this.value = new Text(charSequence2, 12.0f, AndroidUtilities.bold());
            this.y = f + (getHeight() / 2.0f);
        }

        public float getHeight() {
            return Math.max(this.name.getHeight(), this.value.getHeight());
        }
    }

    public StarGiftUniqueActionLayout(int i, ChatActionCell chatActionCell, Theme.ResourcesProvider resourcesProvider) {
        this.currentAccount = i;
        this.view = chatActionCell;
        this.resourcesProvider = resourcesProvider;
        this.ribbon = new GiftSheet.RibbonDrawable(chatActionCell, 1.0f);
        this.buttonBounce = new ButtonBounce(chatActionCell);
        this.bounce = new ButtonBounce(chatActionCell);
        this.imageReceiver = new ImageReceiver(chatActionCell);
        this.emoji = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(chatActionCell, AndroidUtilities.dp(28.0f));
    }

    public void attach() {
        this.attached = true;
        if (this.action != null) {
            this.imageReceiver.onAttachedToWindow();
            this.emoji.attach();
        }
    }

    public void detach() {
        this.attached = false;
        this.imageReceiver.onDetachedFromWindow();
        this.emoji.detach();
    }

    public void draw(Canvas canvas) {
        float width = getWidth() / 2.0f;
        this.backgroundRect.set(0.0f, 0.0f, getWidth(), getHeight());
        int width2 = ((int) (this.backgroundRect.width() + this.backgroundRect.height())) / 2;
        if (this.backdrop != null && (this.gradient == null || this.gradientRadius != width2)) {
            this.gradientRadius = width2;
            float f = width2;
            TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop = this.backdrop;
            this.gradient = new RadialGradient(0.0f, 0.0f, f, new int[]{stargiftattributebackdrop.center_color | (-16777216), stargiftattributebackdrop.edge_color | (-16777216)}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
        }
        if (this.gradient != null) {
            this.matrix.reset();
            this.matrix.postTranslate(width, width);
            this.gradient.setLocalMatrix(this.matrix);
            this.backgroundPaint.setShader(this.gradient);
        }
        this.backgroundPath.rewind();
        this.backgroundPath.addRoundRect(this.backgroundRect, AndroidUtilities.dp(14.0f), AndroidUtilities.dp(14.0f), Path.Direction.CW);
        canvas.save();
        float scale = this.bounce.getScale(0.0125f);
        canvas.scale(scale, scale, this.backgroundRect.centerX(), this.backgroundRect.centerY());
        canvas.save();
        canvas.clipPath(this.backgroundPath);
        canvas.drawPaint(this.backgroundPaint);
        canvas.save();
        canvas.translate(width, AndroidUtilities.dp(65.0f));
        TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop2 = this.backdrop;
        if (stargiftattributebackdrop2 != null) {
            this.emoji.setColor(Integer.valueOf(stargiftattributebackdrop2.pattern_color | (-16777216)));
        }
        StarGiftPatterns.drawPatterns(canvas, 1, this.emoji, this.backgroundRect.width(), this.backgroundRect.height(), 1.0f, 1.1f);
        canvas.restore();
        this.imageReceiver.setImageCoords(width - (AndroidUtilities.dp(110.0f) / 2.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(110.0f), AndroidUtilities.dp(110.0f));
        this.imageReceiver.draw(canvas);
        int multAlpha = Theme.multAlpha(-1, 0.6f);
        TL_stars.starGiftAttributeBackdrop stargiftattributebackdrop3 = this.backdrop;
        if (stargiftattributebackdrop3 != null) {
            multAlpha = stargiftattributebackdrop3.text_color | (-16777216);
        }
        int i = multAlpha;
        this.title.ellipsize(getWidth() - AndroidUtilities.dp(12.0f));
        Text text = this.title;
        text.draw(canvas, width - (text.getCurrentWidth() / 2.0f), this.titleY, -1, 1.0f);
        this.subtitle.ellipsize(getWidth() - AndroidUtilities.dp(12.0f));
        Text text2 = this.subtitle;
        text2.draw(canvas, width - (text2.getCurrentWidth() / 2.0f), this.subtitleY, i, 1.0f);
        float dp = this.nameWidth + AndroidUtilities.dp(9.0f) + this.valueWidth;
        Iterator it = this.table.iterator();
        while (it.hasNext()) {
            Row row = (Row) it.next();
            Text text3 = row.name;
            float f2 = width - (dp / 2.0f);
            text3.draw(canvas, (f2 + this.nameWidth) - text3.getCurrentWidth(), row.y, i, 1.0f);
            row.value.draw(canvas, f2 + this.nameWidth + AndroidUtilities.dp(9.0f), row.y, -1, 1.0f);
        }
        if (!this.repost) {
            this.buttonRect.set(width - ((this.buttonText.getCurrentWidth() + AndroidUtilities.dp(30.0f)) / 2.0f), this.buttonY, width + ((this.buttonText.getCurrentWidth() + AndroidUtilities.dp(30.0f)) / 2.0f), this.buttonY + this.buttonHeight);
            this.buttonPath.rewind();
            Path path = this.buttonPath;
            RectF rectF = this.buttonRect;
            float f3 = this.buttonHeight / 2.0f;
            path.addRoundRect(rectF, f3, f3, Path.Direction.CW);
            this.buttonBackgroundPaint.setColor(Theme.multAlpha(-16777216, 0.13f));
            float scale2 = this.buttonBounce.getScale(0.075f);
            canvas.scale(scale2, scale2, this.buttonRect.centerX(), this.buttonRect.centerY());
            canvas.drawPath(this.buttonPath, this.buttonBackgroundPaint);
            canvas.restore();
            this.ribbon.setBounds(((int) this.backgroundRect.right) - AndroidUtilities.dp(46.67f), ((int) this.backgroundRect.top) - AndroidUtilities.dp(1.33f), ((int) this.backgroundRect.right) + AndroidUtilities.dp(1.33f), ((int) this.backgroundRect.top) + AndroidUtilities.dp(46.67f));
            this.ribbon.setTextColor(i);
            this.ribbon.draw(canvas);
        }
        canvas.restore();
    }

    public void drawOutbounds(Canvas canvas) {
        if (this.repost) {
            return;
        }
        canvas.save();
        float scale = this.bounce.getScale(0.0125f);
        canvas.scale(scale, scale, this.backgroundRect.centerX(), this.backgroundRect.centerY());
        float scale2 = this.buttonBounce.getScale(0.075f);
        canvas.scale(scale2, scale2, this.buttonRect.centerX(), this.buttonRect.centerY());
        canvas.clipPath(this.buttonPath);
        this.buttonParticles.setBounds(this.buttonRect);
        this.buttonParticles.process();
        this.buttonParticles.draw(canvas, Theme.multAlpha(-1, 0.7f));
        this.buttonText.draw(canvas, this.buttonRect.left + AndroidUtilities.dp(15.0f), this.buttonRect.centerY(), -1, 1.0f);
        canvas.restore();
        this.view.invalidateOutbounds();
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }

    public boolean has() {
        return this.action != null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x00b4, code lost:
    
        if (r9.bounce.isPressed() != false) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(float f, float f2, MotionEvent motionEvent) {
        ButtonBounce buttonBounce;
        boolean contains = this.buttonRect.contains(motionEvent.getX() - f, motionEvent.getY() - f2);
        boolean contains2 = this.backgroundRect.contains(motionEvent.getX() - f, motionEvent.getY() - f2);
        if (motionEvent.getAction() == 0) {
            this.bounce.setPressed(contains2 && !contains);
            this.buttonBounce.setPressed(contains);
        } else {
            if (motionEvent.getAction() != 2) {
                if (motionEvent.getAction() == 1 && (this.buttonBounce.isPressed() || this.bounce.isPressed())) {
                    new StarGiftSheet(this.view.getContext(), this.currentAccount, this.currentMessageObject.getDialogId(), this.resourcesProvider).set(this.currentMessageObject).show();
                } else if (motionEvent.getAction() == 3) {
                    if (!this.buttonBounce.isPressed()) {
                    }
                }
                this.buttonBounce.setPressed(false);
                this.bounce.setPressed(false);
                return true;
            }
            if (this.buttonBounce.isPressed() && !contains) {
                buttonBounce = this.buttonBounce;
            } else if (this.bounce.isPressed() && !contains2) {
                buttonBounce = this.bounce;
            }
            buttonBounce.setPressed(false);
        }
        return this.buttonBounce.isPressed() || this.bounce.isPressed();
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0045 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void set(MessageObject messageObject, boolean z) {
        TLRPC.TL_messageActionStarGiftUnique tL_messageActionStarGiftUnique;
        int dp;
        Text text;
        int dp2;
        TLRPC.Message message;
        this.currentMessageObject = messageObject;
        if (messageObject != null && (message = messageObject.messageOwner) != null) {
            TLRPC.MessageAction messageAction = message.action;
            if (messageAction instanceof TLRPC.TL_messageActionStarGiftUnique) {
                tL_messageActionStarGiftUnique = (TLRPC.TL_messageActionStarGiftUnique) messageAction;
                if (tL_messageActionStarGiftUnique != null || tL_messageActionStarGiftUnique.refunded || !(tL_messageActionStarGiftUnique.gift instanceof TL_stars.TL_starGiftUnique)) {
                    tL_messageActionStarGiftUnique = null;
                }
                if (this.attached && tL_messageActionStarGiftUnique != null && this.action == null) {
                    this.imageReceiver.onAttachedToWindow();
                    this.emoji.attach();
                }
                this.action = tL_messageActionStarGiftUnique;
                this.repost = messageObject == null && messageObject.isRepostPreview;
                if (tL_messageActionStarGiftUnique != null) {
                    return;
                }
                TL_stars.TL_starGiftUnique tL_starGiftUnique = (TL_stars.TL_starGiftUnique) tL_messageActionStarGiftUnique.gift;
                this.backdrop = (TL_stars.starGiftAttributeBackdrop) StarsController.findAttribute(tL_starGiftUnique.attributes, TL_stars.starGiftAttributeBackdrop.class);
                this.pattern = (TL_stars.starGiftAttributePattern) StarsController.findAttribute(tL_starGiftUnique.attributes, TL_stars.starGiftAttributePattern.class);
                TL_stars.starGiftAttributeModel stargiftattributemodel = this.model;
                this.model = (TL_stars.starGiftAttributeModel) StarsController.findAttribute(tL_starGiftUnique.attributes, TL_stars.starGiftAttributeModel.class);
                Paint paint = this.backgroundPaint;
                this.gradient = null;
                paint.setShader(null);
                TL_stars.starGiftAttributePattern stargiftattributepattern = this.pattern;
                if (stargiftattributepattern != null) {
                    this.emoji.set(stargiftattributepattern.document, z);
                } else {
                    this.emoji.set((Drawable) null, z);
                }
                TL_stars.starGiftAttributeModel stargiftattributemodel2 = this.model;
                if (stargiftattributemodel2 != null && (stargiftattributemodel == null || stargiftattributemodel.document.id != stargiftattributemodel2.document.id)) {
                    if (this.repost) {
                        this.imageReceiver.setAllowStartLottieAnimation(true);
                        this.imageReceiver.setAllowStartAnimation(true);
                        this.imageReceiver.setAutoRepeat(1);
                    } else {
                        this.imageReceiver.setAutoRepeatCount(0);
                        this.imageReceiver.clearDecorators();
                        this.imageReceiver.setAutoRepeat(0);
                    }
                    StarsIntroActivity.setGiftImage(this.imageReceiver, this.model.document, 110);
                }
                this.ribbon.setBackdrop(this.backdrop, true);
                this.ribbon.setText(11, LocaleController.getString(R.string.Gift2UniqueRibbon), true);
                if (this.repost) {
                    dp = AndroidUtilities.dp(200.0f);
                } else {
                    this.width = Math.min((int) (AndroidUtilities.isTablet() ? AndroidUtilities.getMinTabletSide() * 0.6f : (AndroidUtilities.displaySize.x * 0.62f) - AndroidUtilities.dp(34.0f)), ((AndroidUtilities.displaySize.y - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(64.0f));
                    if (!AndroidUtilities.isTablet()) {
                        this.width = (int) (this.width * 1.2f);
                    }
                    dp = this.width - AndroidUtilities.dp(8.0f);
                }
                this.width = dp;
                float f = this.width;
                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf((tL_messageActionStarGiftUnique.upgrade ^ true) == messageObject.isOutOwner() ? UserConfig.getInstance(this.currentAccount).getClientUserId() : messageObject.getDialogId()));
                float dp3 = AndroidUtilities.dp(10.0f) + 0.0f + AndroidUtilities.dp(110.0f) + AndroidUtilities.dp(9.33f);
                if (this.repost) {
                    this.title = new Text(tL_starGiftUnique.title, 14.0f, AndroidUtilities.bold());
                } else {
                    this.title = new Text(LocaleController.formatString(R.string.Gift2UniqueTitle, UserObject.getForcedFirstName(user)), 14.0f, AndroidUtilities.bold());
                }
                this.titleY = (this.title.getHeight() / 2.0f) + dp3;
                float height = dp3 + this.title.getHeight() + AndroidUtilities.dp(3.0f);
                if (this.repost) {
                    text = new Text(LocaleController.formatPluralStringComma("Gift2CollectionNumber", tL_starGiftUnique.num), 12.0f, AndroidUtilities.bold());
                } else {
                    text = new Text(tL_starGiftUnique.title + " #" + LocaleController.formatNumber(tL_starGiftUnique.num, ','), 12.0f);
                }
                this.subtitle = text;
                this.subtitleY = (this.subtitle.getHeight() / 2.0f) + height;
                float height2 = height + this.subtitle.getHeight() + AndroidUtilities.dp(this.repost ? 14.0f : 11.0f);
                this.table.clear();
                this.nameWidth = 0.0f;
                this.valueWidth = 0.0f;
                if (this.model != null) {
                    if (!this.table.isEmpty()) {
                        height2 += AndroidUtilities.dp(6.0f);
                    }
                    Row row = new Row(height2, LocaleController.getString(R.string.Gift2AttributeModel), this.model.name);
                    this.table.add(row);
                    float f2 = f * 0.5f;
                    row.name.ellipsize(f2);
                    this.nameWidth = Math.max(this.nameWidth, row.name.getCurrentWidth());
                    row.value.ellipsize(f2);
                    this.valueWidth = Math.max(this.valueWidth, row.value.getCurrentWidth());
                    height2 += row.getHeight();
                }
                if (this.backdrop != null) {
                    if (!this.table.isEmpty()) {
                        height2 += AndroidUtilities.dp(6.0f);
                    }
                    Row row2 = new Row(height2, LocaleController.getString(R.string.Gift2AttributeBackdrop), this.backdrop.name);
                    this.table.add(row2);
                    float f3 = f * 0.5f;
                    row2.name.ellipsize(f3);
                    this.nameWidth = Math.max(this.nameWidth, row2.name.getCurrentWidth());
                    row2.value.ellipsize(f3);
                    this.valueWidth = Math.max(this.valueWidth, row2.value.getCurrentWidth());
                    height2 += row2.getHeight();
                }
                if (this.pattern != null) {
                    if (!this.table.isEmpty()) {
                        height2 += AndroidUtilities.dp(6.0f);
                    }
                    Row row3 = new Row(height2, LocaleController.getString(R.string.Gift2AttributeSymbol), this.pattern.name);
                    this.table.add(row3);
                    float f4 = f * 0.5f;
                    row3.name.ellipsize(f4);
                    this.nameWidth = Math.max(this.nameWidth, row3.name.getCurrentWidth());
                    row3.value.ellipsize(f4);
                    this.valueWidth = Math.max(this.valueWidth, row3.value.getCurrentWidth());
                    height2 += row3.getHeight();
                }
                float dp4 = height2 + AndroidUtilities.dp(11.66f);
                if (this.repost) {
                    dp2 = AndroidUtilities.dp(10.0f);
                } else {
                    this.buttonY = dp4;
                    this.buttonText = new Text(LocaleController.getString(R.string.Gift2UniqueView), 14.0f, AndroidUtilities.bold());
                    float dp5 = AndroidUtilities.dp(30.0f);
                    this.buttonHeight = dp5;
                    dp4 += dp5;
                    dp2 = AndroidUtilities.dp(11.0f);
                }
                this.height = (int) (dp4 + dp2);
                return;
            }
        }
        tL_messageActionStarGiftUnique = null;
        if (tL_messageActionStarGiftUnique != null) {
        }
        tL_messageActionStarGiftUnique = null;
        if (this.attached) {
            this.imageReceiver.onAttachedToWindow();
            this.emoji.attach();
        }
        this.action = tL_messageActionStarGiftUnique;
        this.repost = messageObject == null && messageObject.isRepostPreview;
        if (tL_messageActionStarGiftUnique != null) {
        }
    }
}
