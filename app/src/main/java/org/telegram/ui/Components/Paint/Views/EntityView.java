package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.UUID;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Rect;
/* loaded from: classes3.dex */
public class EntityView extends FrameLayout {
    private EntityViewDelegate delegate;
    private GestureDetector gestureDetector;
    protected Point position;
    private float previousLocationX;
    private float previousLocationY;
    protected SelectionView selectionView;
    private boolean hasPanned = false;
    private boolean hasReleased = false;
    private boolean hasTransformed = false;
    private boolean announcedSelection = false;
    private boolean recognizedLongPress = false;
    private UUID uuid = UUID.randomUUID();

    /* loaded from: classes3.dex */
    public interface EntityViewDelegate {
        boolean allowInteraction(EntityView entityView);

        int[] getCenterLocation(EntityView entityView);

        float getCropRotation();

        float[] getTransformedTouch(float f, float f2);

        boolean onEntityLongClicked(EntityView entityView);

        boolean onEntitySelected(EntityView entityView);
    }

    protected SelectionView createSelectionView() {
        return null;
    }

    public EntityView(Context context, Point point) {
        super(context);
        this.position = point;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { // from class: org.telegram.ui.Components.Paint.Views.EntityView.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
                if (EntityView.this.hasPanned || EntityView.this.hasTransformed || EntityView.this.hasReleased) {
                    return;
                }
                EntityView.this.recognizedLongPress = true;
                if (EntityView.this.delegate == null) {
                    return;
                }
                EntityView.this.performHapticFeedback(0);
                EntityView.this.delegate.onEntityLongClicked(EntityView.this);
            }
        });
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point point) {
        this.position = point;
        updatePosition();
    }

    public float getScale() {
        return getScaleX();
    }

    public void setScale(float f) {
        setScaleX(f);
        setScaleY(f);
    }

    public void setDelegate(EntityViewDelegate entityViewDelegate) {
        this.delegate = entityViewDelegate;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.delegate.allowInteraction(this);
    }

    public boolean onTouchMove(float f, float f2) {
        float scaleX = ((View) getParent()).getScaleX();
        float f3 = (f - this.previousLocationX) / scaleX;
        float f4 = (f2 - this.previousLocationY) / scaleX;
        if (((float) Math.hypot(f3, f4)) > (this.hasPanned ? 6.0f : 16.0f)) {
            pan(f3, f4);
            this.previousLocationX = f;
            this.previousLocationY = f2;
            this.hasPanned = true;
            return true;
        }
        return false;
    }

    public void onTouchUp() {
        EntityViewDelegate entityViewDelegate;
        if (!this.recognizedLongPress && !this.hasPanned && !this.hasTransformed && !this.announcedSelection && (entityViewDelegate = this.delegate) != null) {
            entityViewDelegate.onEntitySelected(this);
        }
        this.recognizedLongPress = false;
        this.hasPanned = false;
        this.hasTransformed = false;
        this.hasReleased = true;
        this.announcedSelection = false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0031, code lost:
        if (r3 != 6) goto L26;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        EntityViewDelegate entityViewDelegate;
        boolean z = false;
        if (motionEvent.getPointerCount() <= 1 && this.delegate.allowInteraction(this)) {
            float[] transformedTouch = this.delegate.getTransformedTouch(motionEvent.getRawX(), motionEvent.getRawY());
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        z = onTouchMove(transformedTouch[0], transformedTouch[1]);
                    } else if (actionMasked != 3) {
                        if (actionMasked != 5) {
                        }
                    }
                    this.gestureDetector.onTouchEvent(motionEvent);
                }
                onTouchUp();
                z = true;
                this.gestureDetector.onTouchEvent(motionEvent);
            }
            if (!isSelected() && (entityViewDelegate = this.delegate) != null) {
                entityViewDelegate.onEntitySelected(this);
                this.announcedSelection = true;
            }
            this.previousLocationX = transformedTouch[0];
            this.previousLocationY = transformedTouch[1];
            this.hasReleased = false;
            z = true;
            this.gestureDetector.onTouchEvent(motionEvent);
        }
        return z;
    }

    public void pan(float f, float f2) {
        Point point = this.position;
        point.x += f;
        point.y += f2;
        updatePosition();
    }

    public void updatePosition() {
        setX(this.position.x - (getMeasuredWidth() / 2.0f));
        setY(this.position.y - (getMeasuredHeight() / 2.0f));
        updateSelectionView();
    }

    public void scale(float f) {
        setScale(Math.max(getScale() * f, 0.1f));
        updateSelectionView();
    }

    public void rotate(float f) {
        setRotation(f);
        updateSelectionView();
    }

    protected Rect getSelectionBounds() {
        return new Rect(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override // android.view.View
    public boolean isSelected() {
        return this.selectionView != null;
    }

    public void updateSelectionView() {
        SelectionView selectionView = this.selectionView;
        if (selectionView != null) {
            selectionView.updatePosition();
        }
    }

    public void select(ViewGroup viewGroup) {
        SelectionView createSelectionView = createSelectionView();
        this.selectionView = createSelectionView;
        viewGroup.addView(createSelectionView);
        createSelectionView.updatePosition();
    }

    public void deselect() {
        SelectionView selectionView = this.selectionView;
        if (selectionView == null) {
            return;
        }
        if (selectionView.getParent() != null) {
            ((ViewGroup) this.selectionView.getParent()).removeView(this.selectionView);
        }
        this.selectionView = null;
    }

    public void setSelectionVisibility(boolean z) {
        SelectionView selectionView = this.selectionView;
        if (selectionView == null) {
            return;
        }
        selectionView.setVisibility(z ? 0 : 8);
    }

    /* loaded from: classes3.dex */
    public class SelectionView extends FrameLayout {
        private int currentHandle;
        protected Paint paint = new Paint(1);
        protected Paint dotPaint = new Paint(1);
        protected Paint dotStrokePaint = new Paint(1);

        protected int pointInsideHandle(float f, float f2) {
            throw null;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SelectionView(Context context) {
            super(context);
            EntityView.this = r2;
            setWillNotDraw(false);
            this.paint.setColor(-1);
            this.dotPaint.setColor(-12793105);
            this.dotStrokePaint.setColor(-1);
            this.dotStrokePaint.setStyle(Paint.Style.STROKE);
            this.dotStrokePaint.setStrokeWidth(AndroidUtilities.dp(1.0f));
        }

        protected void updatePosition() {
            Rect selectionBounds = EntityView.this.getSelectionBounds();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
            layoutParams.leftMargin = (int) selectionBounds.x;
            layoutParams.topMargin = (int) selectionBounds.y;
            layoutParams.width = (int) selectionBounds.width;
            layoutParams.height = (int) selectionBounds.height;
            setLayoutParams(layoutParams);
            setRotation(EntityView.this.getRotation());
        }

        /* JADX WARN: Code restructure failed: missing block: B:11:0x002c, code lost:
            if (r1 != 6) goto L38;
         */
        /* JADX WARN: Removed duplicated region for block: B:40:0x0132  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            float rawX;
            float rawY;
            double atan2;
            int actionMasked = motionEvent.getActionMasked();
            float[] transformedTouch = EntityView.this.delegate.getTransformedTouch(motionEvent.getRawX(), motionEvent.getRawY());
            boolean z = false;
            float f = transformedTouch[0];
            float f2 = transformedTouch[1];
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        int i = this.currentHandle;
                        if (i == 3) {
                            z = EntityView.this.onTouchMove(f, f2);
                        } else if (i != 0) {
                            float f3 = f - EntityView.this.previousLocationX;
                            float f4 = f2 - EntityView.this.previousLocationY;
                            if (EntityView.this.hasTransformed || Math.abs(f3) > AndroidUtilities.dp(2.0f) || Math.abs(f4) > AndroidUtilities.dp(2.0f)) {
                                EntityView.this.hasTransformed = true;
                                double d = f3;
                                double radians = (float) Math.toRadians(getRotation());
                                double cos = Math.cos(radians);
                                Double.isNaN(d);
                                double d2 = f4;
                                double sin = Math.sin(radians);
                                Double.isNaN(d2);
                                float f5 = (float) ((d * cos) + (d2 * sin));
                                if (this.currentHandle == 1) {
                                    f5 *= -1.0f;
                                }
                                EntityView.this.scale(((f5 * 2.0f) / getMeasuredWidth()) + 1.0f);
                                int[] centerLocation = EntityView.this.delegate.getCenterLocation(EntityView.this);
                                float f6 = 0.0f;
                                int i2 = this.currentHandle;
                                if (i2 != 1) {
                                    if (i2 == 2) {
                                        atan2 = Math.atan2(rawY - centerLocation[1], rawX - centerLocation[0]);
                                    }
                                    EntityView.this.rotate(((float) Math.toDegrees(f6)) - EntityView.this.delegate.getCropRotation());
                                    EntityView.this.previousLocationX = f;
                                    EntityView.this.previousLocationY = f2;
                                } else {
                                    atan2 = Math.atan2(centerLocation[1] - rawY, centerLocation[0] - rawX);
                                }
                                f6 = (float) atan2;
                                EntityView.this.rotate(((float) Math.toDegrees(f6)) - EntityView.this.delegate.getCropRotation());
                                EntityView.this.previousLocationX = f;
                                EntityView.this.previousLocationY = f2;
                            }
                            z = true;
                        }
                    } else if (actionMasked != 3) {
                        if (actionMasked != 5) {
                        }
                    }
                    if (this.currentHandle == 3) {
                        EntityView.this.gestureDetector.onTouchEvent(motionEvent);
                    }
                    return z;
                }
                EntityView.this.onTouchUp();
                this.currentHandle = 0;
                z = true;
                if (this.currentHandle == 3) {
                }
                return z;
            }
            int pointInsideHandle = pointInsideHandle(motionEvent.getX(), motionEvent.getY());
            if (pointInsideHandle != 0) {
                this.currentHandle = pointInsideHandle;
                EntityView.this.previousLocationX = f;
                EntityView.this.previousLocationY = f2;
                EntityView.this.hasReleased = false;
                z = true;
            }
            if (this.currentHandle == 3) {
            }
            return z;
        }
    }
}
