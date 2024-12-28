package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.LongSparseArray;
import android.util.Property;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.BotHelpCell;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ChatGreetingsView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ThanosEffect;
import org.telegram.ui.TextMessageEnterTransition;
import org.telegram.ui.VoiceMessageEnterTransition;

/* loaded from: classes.dex */
public abstract class ChatListItemAnimator extends DefaultItemAnimator {
    public static final Interpolator DEFAULT_INTERPOLATOR = new CubicBezierInterpolator(0.19919472913616398d, 0.010644531250000006d, 0.27920937042459737d, 0.91025390625d);
    private final ChatActivity activity;
    long alphaEnterDelay;
    private ChatGreetingsView chatGreetingsView;
    private Utilities.Callback0Return getThanosEffectContainer;
    private RecyclerView.ViewHolder greetingsSticker;
    private final RecyclerListView recyclerListView;
    private final Theme.ResourcesProvider resourcesProvider;
    private boolean reversePositions;
    private boolean shouldAnimateEnterFromBottom;
    private Utilities.Callback0Return supportsThanosEffectContainer;
    private HashMap willRemovedGroup = new HashMap();
    private ArrayList willChangedGroups = new ArrayList();
    HashMap animators = new HashMap();
    ArrayList thanosViews = new ArrayList();
    ArrayList runOnAnimationsEnd = new ArrayList();
    HashMap groupIdToEnterDelay = new HashMap();
    private final ArrayList toBeSnapped = new ArrayList();

    class ItemHolderInfoExtended extends RecyclerView.ItemAnimator.ItemHolderInfo {
        float imageHeight;
        float imageWidth;
        float imageX;
        float imageY;

        ItemHolderInfoExtended() {
        }
    }

    class MoveInfoExtended extends DefaultItemAnimator.MoveInfo {
        public boolean animateBackgroundOnly;
        public boolean animateChangeGroupBackground;
        public boolean animateChangeInternal;
        boolean animateImage;
        public boolean animatePinnedBottom;
        boolean animateRemoveGroup;
        int deltaBottom;
        int deltaLeft;
        int deltaRight;
        int deltaTop;
        public int groupOffsetBottom;
        public int groupOffsetLeft;
        public int groupOffsetRight;
        public int groupOffsetTop;
        float imageHeight;
        float imageWidth;
        float imageX;
        float imageY;

        MoveInfoExtended(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
            super(viewHolder, i, i2, i3, i4);
        }
    }

    public ChatListItemAnimator(ChatActivity chatActivity, RecyclerListView recyclerListView, Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
        this.activity = chatActivity;
        this.recyclerListView = recyclerListView;
        this.translationInterpolator = DEFAULT_INTERPOLATOR;
        this.alwaysCreateMoveAnimationIfPossible = true;
        setSupportsChangeAnimations(false);
    }

    private void animateRemoveGroupImpl(final ArrayList arrayList) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate remove group impl with thanos");
        }
        this.mRemoveAnimations.addAll(arrayList);
        ThanosEffect thanosEffect = (ThanosEffect) this.getThanosEffectContainer.run();
        for (int i = 0; i < arrayList.size(); i++) {
            dispatchRemoveStarting((RecyclerView.ViewHolder) arrayList.get(i));
        }
        final ArrayList arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(((RecyclerView.ViewHolder) arrayList.get(i2)).itemView);
        }
        thanosEffect.animateGroup(arrayList2, new Runnable() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                ChatListItemAnimator.this.lambda$animateRemoveGroupImpl$10(arrayList2, arrayList);
            }
        });
        this.thanosViews.add((View) arrayList2.get(0));
        this.recyclerListView.stopScroll();
    }

    private void cancelAnimators() {
        ThanosEffect thanosEffect;
        ArrayList arrayList = new ArrayList(this.animators.values());
        this.animators.clear();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Animator animator = (Animator) it.next();
            if (animator != null) {
                animator.cancel();
            }
        }
        if (this.thanosViews.isEmpty() || (thanosEffect = (ThanosEffect) this.getThanosEffectContainer.run()) == null) {
            return;
        }
        thanosEffect.kill();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateAddImpl$8(ChatMessageCell chatMessageCell, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatMessageCell.getTransitionParams().animateChangeProgress = floatValue;
        if (chatMessageCell.getTransitionParams().animateChangeProgress > 1.0f) {
            chatMessageCell.getTransitionParams().animateChangeProgress = 1.0f;
        }
        float f9 = 1.0f - floatValue;
        chatMessageCell.getPhotoImage().setImageCoords(f + (f2 * f9), f3 + (f4 * f9), (f5 * f9) + (f6 * floatValue), (f7 * f9) + (f8 * floatValue));
        chatMessageCell.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateMoveImpl$2(MoveInfoExtended moveInfoExtended, ChatMessageCell.TransitionParams transitionParams, boolean z, float f, float f2, ChatMessageCell chatMessageCell, int[] iArr, RecyclerView.ViewHolder viewHolder, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float f3 = 1.0f - floatValue;
        float f4 = (moveInfoExtended.imageX * f3) + (transitionParams.animateToImageX * floatValue);
        float f5 = (moveInfoExtended.imageY * f3) + (transitionParams.animateToImageY * floatValue);
        float f6 = (moveInfoExtended.imageWidth * f3) + (transitionParams.animateToImageW * floatValue);
        float f7 = (moveInfoExtended.imageHeight * f3) + (transitionParams.animateToImageH * floatValue);
        if (z) {
            float f8 = (f * f3) + (f2 * floatValue);
            transitionParams.captionEnterProgress = f8;
            if (chatMessageCell.getCurrentMessagesGroup() != null) {
                chatMessageCell.getCurrentMessagesGroup().transitionParams.captionEnterProgress = f8;
            }
        }
        if (transitionParams.animateRadius) {
            int[] iArr2 = transitionParams.animateToRadius;
            chatMessageCell.getPhotoImage().setRoundRadius((int) ((iArr[0] * f3) + (iArr2[0] * floatValue)), (int) ((iArr[1] * f3) + (iArr2[1] * floatValue)), (int) ((iArr[2] * f3) + (iArr2[2] * floatValue)), (int) ((iArr[3] * f3) + (iArr2[3] * floatValue)));
        }
        chatMessageCell.setImageCoords(f4, f5, f6, f7);
        viewHolder.itemView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateMoveImpl$3(MoveInfoExtended moveInfoExtended, ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell, ValueAnimator valueAnimator) {
        float translationY;
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (moveInfoExtended.animateBackgroundOnly) {
            transitionParams.deltaLeft = (-moveInfoExtended.deltaLeft) * floatValue;
            transitionParams.deltaRight = (-moveInfoExtended.deltaRight) * floatValue;
            transitionParams.deltaTop = (-moveInfoExtended.deltaTop) * floatValue;
            translationY = (-moveInfoExtended.deltaBottom) * floatValue;
        } else {
            transitionParams.deltaLeft = ((-moveInfoExtended.deltaLeft) * floatValue) - chatMessageCell.getAnimationOffsetX();
            transitionParams.deltaRight = ((-moveInfoExtended.deltaRight) * floatValue) - chatMessageCell.getAnimationOffsetX();
            transitionParams.deltaTop = ((-moveInfoExtended.deltaTop) * floatValue) - chatMessageCell.getTranslationY();
            translationY = ((-moveInfoExtended.deltaBottom) * floatValue) - chatMessageCell.getTranslationY();
        }
        transitionParams.deltaBottom = translationY;
        chatMessageCell.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateMoveImpl$4(MessageObject.GroupedMessages.TransitionParams transitionParams, MoveInfoExtended moveInfoExtended, boolean z, float f, float f2, RecyclerListView recyclerListView, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        transitionParams.offsetTop = moveInfoExtended.groupOffsetTop * floatValue;
        transitionParams.offsetBottom = moveInfoExtended.groupOffsetBottom * floatValue;
        transitionParams.offsetLeft = moveInfoExtended.groupOffsetLeft * floatValue;
        transitionParams.offsetRight = moveInfoExtended.groupOffsetRight * floatValue;
        if (z) {
            transitionParams.captionEnterProgress = (f * floatValue) + (f2 * (1.0f - floatValue));
        }
        if (recyclerListView != null) {
            recyclerListView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateMoveImpl$5(ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell, ValueAnimator valueAnimator) {
        transitionParams.changePinnedBottomProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatMessageCell.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateMoveImpl$6(ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell, ValueAnimator valueAnimator) {
        transitionParams.animateChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatMessageCell.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateMoveImpl$7(ChatActionCell.TransitionParams transitionParams, ChatActionCell chatActionCell, ValueAnimator valueAnimator) {
        transitionParams.animateChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatActionCell.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateRemoveGroupImpl$10(ArrayList arrayList, ArrayList arrayList2) {
        for (int i = 0; i < arrayList.size(); i++) {
            ((View) arrayList.get(i)).setVisibility(0);
        }
        if (this.mRemoveAnimations.removeAll(arrayList2)) {
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                dispatchRemoveFinished((RecyclerView.ViewHolder) arrayList2.get(i2));
            }
            dispatchFinishedWhenDone();
        }
        this.thanosViews.removeAll(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateRemoveImpl$9(View view, RecyclerView.ViewHolder viewHolder) {
        view.setVisibility(0);
        if (this.mRemoveAnimations.remove(viewHolder)) {
            dispatchRemoveFinished(viewHolder);
            dispatchFinishedWhenDone();
        }
        this.thanosViews.remove(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$runAlphaEnterTransition$1(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        return viewHolder2.itemView.getTop() - viewHolder.itemView.getTop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runPendingAnimations$0(ValueAnimator valueAnimator) {
        ChatActivity chatActivity = this.activity;
        if (chatActivity != null) {
            chatActivity.onListItemAnimatorTick();
        } else {
            this.recyclerListView.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreTransitionParams(View view) {
        view.setAlpha(1.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.setTranslationY(0.0f);
        if (view instanceof BotHelpCell) {
            int measuredHeight = (this.recyclerListView.getMeasuredHeight() / 2) - (view.getMeasuredHeight() / 2);
            ((BotHelpCell) view).setAnimating(false);
            view.setTranslationY(view.getTop() > measuredHeight ? measuredHeight - view.getTop() : 0.0f);
        } else if (view instanceof ChatMessageCell) {
            ChatMessageCell chatMessageCell = (ChatMessageCell) view;
            chatMessageCell.getTransitionParams().resetAnimation();
            chatMessageCell.setAnimationOffsetX(0.0f);
        } else if (view instanceof ChatActionCell) {
            ((ChatActionCell) view).getTransitionParams().resetAnimation();
        } else {
            view.setTranslationX(0.0f);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:104:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0122  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x018e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void runAlphaEnterTransition() {
        final boolean z;
        Iterator it;
        MessageObject.GroupedMessages currentMessagesGroup;
        MessageObject messageObject;
        Utilities.Callback0Return callback0Return;
        boolean z2 = !this.mPendingRemovals.isEmpty();
        boolean z3 = !this.mPendingMoves.isEmpty();
        boolean z4 = !this.mPendingChanges.isEmpty();
        boolean z5 = !this.mPendingAdditions.isEmpty();
        if (!z2 && !z3 && !z5 && !z4) {
            return;
        }
        boolean z6 = (this.getThanosEffectContainer == null || (callback0Return = this.supportsThanosEffectContainer) == null || !((Boolean) callback0Return.run()).booleanValue()) ? false : true;
        if (z6) {
            LongSparseArray longSparseArray = null;
            int i = 0;
            while (i < this.mPendingRemovals.size()) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) this.mPendingRemovals.get(i);
                if (this.toBeSnapped.contains(viewHolder)) {
                    View view = viewHolder.itemView;
                    if ((view instanceof ChatMessageCell) && ((ChatMessageCell) view).getCurrentMessagesGroup() != null && (messageObject = ((ChatMessageCell) viewHolder.itemView).getMessageObject()) != null && messageObject.getGroupId() != 0) {
                        if (longSparseArray == null) {
                            longSparseArray = new LongSparseArray();
                        }
                        ArrayList arrayList = (ArrayList) longSparseArray.get(messageObject.getGroupId());
                        if (arrayList == null) {
                            long groupId = messageObject.getGroupId();
                            ArrayList arrayList2 = new ArrayList();
                            longSparseArray.put(groupId, arrayList2);
                            arrayList = arrayList2;
                        }
                        this.toBeSnapped.remove(viewHolder);
                        this.mPendingRemovals.remove(i);
                        i--;
                        arrayList.add(viewHolder);
                    }
                }
                i++;
            }
            if (longSparseArray != null) {
                z = false;
                for (int i2 = 0; i2 < longSparseArray.size(); i2++) {
                    ArrayList arrayList3 = (ArrayList) longSparseArray.valueAt(i2);
                    if (arrayList3.size() > 0) {
                        View view2 = ((RecyclerView.ViewHolder) arrayList3.get(0)).itemView;
                        if (!(view2 instanceof ChatMessageCell) || (currentMessagesGroup = ((ChatMessageCell) view2).getCurrentMessagesGroup()) == null || currentMessagesGroup.messages.size() <= arrayList3.size()) {
                            animateRemoveGroupImpl(arrayList3);
                            z = true;
                        } else {
                            this.mPendingRemovals.addAll(arrayList3);
                        }
                    }
                }
                it = this.mPendingRemovals.iterator();
                while (it.hasNext()) {
                    RecyclerView.ViewHolder viewHolder2 = (RecyclerView.ViewHolder) it.next();
                    boolean z7 = this.toBeSnapped.remove(viewHolder2) && z6;
                    animateRemoveImpl(viewHolder2, z7);
                    if (z7) {
                        z = true;
                    }
                }
                this.mPendingRemovals.clear();
                if (z3) {
                    final ArrayList arrayList4 = new ArrayList();
                    arrayList4.addAll(this.mPendingMoves);
                    this.mMovesList.add(arrayList4);
                    this.mPendingMoves.clear();
                    Runnable runnable = new Runnable() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Iterator it2 = arrayList4.iterator();
                            while (it2.hasNext()) {
                                DefaultItemAnimator.MoveInfo moveInfo = (DefaultItemAnimator.MoveInfo) it2.next();
                                ChatListItemAnimator.this.animateMoveImpl(moveInfo.holder, moveInfo, z);
                            }
                            arrayList4.clear();
                            ChatListItemAnimator.this.mMovesList.remove(arrayList4);
                        }
                    };
                    if (this.delayAnimations && z2) {
                        ViewCompat.postOnAnimationDelayed(((DefaultItemAnimator.MoveInfo) arrayList4.get(0)).holder.itemView, runnable, z ? 0L : getMoveAnimationDelay());
                    } else {
                        runnable.run();
                    }
                }
                if (z4) {
                    final ArrayList arrayList5 = new ArrayList();
                    arrayList5.addAll(this.mPendingChanges);
                    this.mChangesList.add(arrayList5);
                    this.mPendingChanges.clear();
                    Runnable runnable2 = new Runnable() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Iterator it2 = arrayList5.iterator();
                            while (it2.hasNext()) {
                                ChatListItemAnimator.this.animateChangeImpl((DefaultItemAnimator.ChangeInfo) it2.next());
                            }
                            arrayList5.clear();
                            ChatListItemAnimator.this.mChangesList.remove(arrayList5);
                        }
                    };
                    if (this.delayAnimations && z2) {
                        ViewCompat.postOnAnimationDelayed(((DefaultItemAnimator.ChangeInfo) arrayList5.get(0)).oldHolder.itemView, runnable2, 0L);
                    } else {
                        runnable2.run();
                    }
                }
                if (z5) {
                    return;
                }
                ArrayList arrayList6 = new ArrayList();
                arrayList6.addAll(this.mPendingAdditions);
                this.mPendingAdditions.clear();
                this.alphaEnterDelay = 0L;
                Collections.sort(arrayList6, new Comparator() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda7
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$runAlphaEnterTransition$1;
                        lambda$runAlphaEnterTransition$1 = ChatListItemAnimator.lambda$runAlphaEnterTransition$1((RecyclerView.ViewHolder) obj, (RecyclerView.ViewHolder) obj2);
                        return lambda$runAlphaEnterTransition$1;
                    }
                });
                Iterator it2 = arrayList6.iterator();
                while (it2.hasNext()) {
                    animateAddImpl((RecyclerView.ViewHolder) it2.next());
                }
                arrayList6.clear();
                return;
            }
        }
        z = false;
        it = this.mPendingRemovals.iterator();
        while (it.hasNext()) {
        }
        this.mPendingRemovals.clear();
        if (z3) {
        }
        if (z4) {
        }
        if (z5) {
        }
    }

    private void runMessageEnterTransition() {
        int i;
        boolean z = !this.mPendingRemovals.isEmpty();
        boolean z2 = !this.mPendingMoves.isEmpty();
        boolean z3 = !this.mPendingChanges.isEmpty();
        boolean z4 = !this.mPendingAdditions.isEmpty();
        if (z || z2 || z4 || z3) {
            int i2 = 0;
            while (i < this.mPendingAdditions.size()) {
                View view = ((RecyclerView.ViewHolder) this.mPendingAdditions.get(i)).itemView;
                if (view instanceof ChatMessageCell) {
                    ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                    i = (chatMessageCell.getCurrentPosition() != null && (chatMessageCell.getCurrentPosition().flags & 1) == 0) ? i + 1 : 0;
                }
                i2 += ((RecyclerView.ViewHolder) this.mPendingAdditions.get(i)).itemView.getHeight();
            }
            Iterator it = this.mPendingRemovals.iterator();
            while (it.hasNext()) {
                animateRemoveImpl((RecyclerView.ViewHolder) it.next());
            }
            this.mPendingRemovals.clear();
            if (z2) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(this.mPendingMoves);
                this.mPendingMoves.clear();
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    DefaultItemAnimator.MoveInfo moveInfo = (DefaultItemAnimator.MoveInfo) it2.next();
                    animateMoveImpl(moveInfo.holder, moveInfo);
                }
                arrayList.clear();
            }
            if (z4) {
                ArrayList arrayList2 = new ArrayList();
                arrayList2.addAll(this.mPendingAdditions);
                this.mPendingAdditions.clear();
                Iterator it3 = arrayList2.iterator();
                while (it3.hasNext()) {
                    animateAddImpl((RecyclerView.ViewHolder) it3.next(), i2);
                }
                arrayList2.clear();
            }
        }
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        resetAnimation(viewHolder);
        viewHolder.itemView.setAlpha(0.0f);
        if (this.shouldAnimateEnterFromBottom) {
            View view = viewHolder.itemView;
            if (view instanceof ChatMessageCell) {
                ((ChatMessageCell) view).getTransitionParams().messageEntering = true;
            }
        } else {
            viewHolder.itemView.setScaleX(0.9f);
            viewHolder.itemView.setScaleY(0.9f);
        }
        this.mPendingAdditions.add(viewHolder);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x023c  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x024a  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0230  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void animateAddImpl(final RecyclerView.ViewHolder viewHolder) {
        ViewPropertyAnimator translationX;
        boolean z;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate add impl");
        }
        final View view = viewHolder.itemView;
        this.mAddAnimations.add(viewHolder);
        if (viewHolder == this.greetingsSticker) {
            view.setAlpha(1.0f);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        if (view instanceof ChatMessageCell) {
            ChatMessageCell chatMessageCell = (ChatMessageCell) view;
            if (chatMessageCell.getAnimationOffsetX() != 0.0f) {
                animatorSet.playTogether(ObjectAnimator.ofFloat(chatMessageCell, (Property<ChatMessageCell, Float>) chatMessageCell.ANIMATION_OFFSET_X, chatMessageCell.getAnimationOffsetX(), 0.0f));
            }
            chatMessageCell.setPivotX(chatMessageCell.getBackgroundDrawableLeft() + ((chatMessageCell.getBackgroundDrawableRight() - chatMessageCell.getBackgroundDrawableLeft()) / 2.0f));
            translationX = view.animate();
        } else {
            translationX = view.animate().translationX(0.0f);
        }
        translationX.translationY(0.0f).setDuration(getAddDuration()).start();
        long max = (long) ((1.0f - Math.max(0.0f, Math.min(1.0f, view.getBottom() / this.recyclerListView.getMeasuredHeight()))) * 100.0f);
        if (view instanceof ChatMessageCell) {
            if (viewHolder == this.greetingsSticker) {
                ChatGreetingsView chatGreetingsView = this.chatGreetingsView;
                if (chatGreetingsView != null) {
                    chatGreetingsView.stickerToSendView.setAlpha(0.0f);
                }
                this.recyclerListView.setClipChildren(false);
                final ChatMessageCell chatMessageCell2 = (ChatMessageCell) view;
                View view2 = (View) this.chatGreetingsView.getParent();
                float x = this.chatGreetingsView.stickerToSendView.getX() + this.chatGreetingsView.getX() + view2.getX();
                float y = this.chatGreetingsView.stickerToSendView.getY() + this.chatGreetingsView.getY() + view2.getY();
                float imageX = chatMessageCell2.getPhotoImage().getImageX() + this.recyclerListView.getX() + chatMessageCell2.getX();
                float imageY = chatMessageCell2.getPhotoImage().getImageY() + this.recyclerListView.getY() + chatMessageCell2.getY();
                final float width = this.chatGreetingsView.stickerToSendView.getWidth();
                final float height = this.chatGreetingsView.stickerToSendView.getHeight();
                final float imageWidth = chatMessageCell2.getPhotoImage().getImageWidth();
                final float imageHeight = chatMessageCell2.getPhotoImage().getImageHeight();
                final float f = x - imageX;
                final float f2 = y - imageY;
                final float imageX2 = chatMessageCell2.getPhotoImage().getImageX();
                final float imageY2 = chatMessageCell2.getPhotoImage().getImageY();
                chatMessageCell2.getTransitionParams().imageChangeBoundsTransition = true;
                chatMessageCell2.getTransitionParams().animateDrawingTimeAlpha = true;
                chatMessageCell2.getPhotoImage().setImageCoords(imageX2 + f, imageX2 + f2, width, height);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda8
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatListItemAnimator.lambda$animateAddImpl$8(ChatMessageCell.this, imageX2, f, imageY2, f2, width, imageWidth, height, imageHeight, valueAnimator);
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.9
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        chatMessageCell2.getTransitionParams().resetAnimation();
                        chatMessageCell2.getPhotoImage().setImageCoords(imageX2, imageY2, imageWidth, imageHeight);
                        if (ChatListItemAnimator.this.chatGreetingsView != null) {
                            ChatListItemAnimator.this.chatGreetingsView.stickerToSendView.setAlpha(1.0f);
                        }
                        chatMessageCell2.invalidate();
                    }
                });
                animatorSet.play(ofFloat);
                max = max;
                z = false;
                view.setAlpha(0.0f);
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, (Property<View, Float>) View.ALPHA, view.getAlpha(), 1.0f));
                if (z) {
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
                } else {
                    view.setScaleX(0.9f);
                    view.setScaleY(0.9f);
                    animatorSet.playTogether(ObjectAnimator.ofFloat(view, (Property<View, Float>) View.SCALE_Y, view.getScaleY(), 1.0f));
                    animatorSet.playTogether(ObjectAnimator.ofFloat(view, (Property<View, Float>) View.SCALE_X, view.getScaleX(), 1.0f));
                }
                if (viewHolder != this.greetingsSticker) {
                    animatorSet.setDuration(350L);
                    animatorSet.setInterpolator(new OvershootInterpolator());
                } else {
                    animatorSet.setStartDelay(max);
                    animatorSet.setDuration(250L);
                }
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.10
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        view.setAlpha(1.0f);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        animator.removeAllListeners();
                        view.setAlpha(1.0f);
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        view.setTranslationY(0.0f);
                        view.setTranslationY(0.0f);
                        if (ChatListItemAnimator.this.mAddAnimations.remove(viewHolder)) {
                            ChatListItemAnimator.this.dispatchAddFinished(viewHolder);
                            ChatListItemAnimator.this.dispatchFinishedWhenDone();
                        }
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        ChatListItemAnimator.this.dispatchAddStarting(viewHolder);
                    }
                });
                this.animators.put(viewHolder, animatorSet);
                animatorSet.start();
            }
            MessageObject.GroupedMessages currentMessagesGroup = ((ChatMessageCell) view).getCurrentMessagesGroup();
            if (currentMessagesGroup != null) {
                Long l = (Long) this.groupIdToEnterDelay.get(Long.valueOf(currentMessagesGroup.groupId));
                if (l == null) {
                    this.groupIdToEnterDelay.put(Long.valueOf(currentMessagesGroup.groupId), Long.valueOf(max));
                } else {
                    max = l.longValue();
                    if (currentMessagesGroup != null && currentMessagesGroup.transitionParams.backgroundChangeBounds) {
                        animatorSet.setStartDelay(140L);
                    }
                }
            }
            max = max;
            if (currentMessagesGroup != null) {
                animatorSet.setStartDelay(140L);
            }
        }
        z = true;
        view.setAlpha(0.0f);
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, (Property<View, Float>) View.ALPHA, view.getAlpha(), 1.0f));
        if (z) {
        }
        if (viewHolder != this.greetingsSticker) {
        }
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.10
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                view.setAlpha(1.0f);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                animator.removeAllListeners();
                view.setAlpha(1.0f);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setTranslationY(0.0f);
                view.setTranslationY(0.0f);
                if (ChatListItemAnimator.this.mAddAnimations.remove(viewHolder)) {
                    ChatListItemAnimator.this.dispatchAddFinished(viewHolder);
                    ChatListItemAnimator.this.dispatchFinishedWhenDone();
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ChatListItemAnimator.this.dispatchAddStarting(viewHolder);
            }
        });
        this.animators.put(viewHolder, animatorSet);
        animatorSet.start();
    }

    public void animateAddImpl(final RecyclerView.ViewHolder viewHolder, int i) {
        final View view = viewHolder.itemView;
        final ViewPropertyAnimator animate = view.animate();
        this.mAddAnimations.add(viewHolder);
        view.setTranslationY(i);
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
        View view2 = viewHolder.itemView;
        ChatMessageCell chatMessageCell = view2 instanceof ChatMessageCell ? (ChatMessageCell) view2 : null;
        if (chatMessageCell == null || !chatMessageCell.getTransitionParams().ignoreAlpha) {
            viewHolder.itemView.setAlpha(1.0f);
        }
        ChatActivity chatActivity = this.activity;
        if (chatActivity != null && chatMessageCell != null && chatActivity.animatingMessageObjects.contains(chatMessageCell.getMessageObject())) {
            this.activity.animatingMessageObjects.remove(chatMessageCell.getMessageObject());
            if (this.activity.getChatActivityEnterView().canShowMessageTransition()) {
                if (chatMessageCell.getMessageObject().isVoice()) {
                    if (Math.abs(view.getTranslationY()) < view.getMeasuredHeight() * 3.0f) {
                        new VoiceMessageEnterTransition(chatMessageCell, this.activity.getChatActivityEnterView(), this.recyclerListView, this.activity.messageEnterTransitionContainer, this.resourcesProvider).start();
                    }
                } else if (SharedConfig.getDevicePerformanceClass() != 0 && Math.abs(view.getTranslationY()) < this.recyclerListView.getMeasuredHeight()) {
                    ChatActivity chatActivity2 = this.activity;
                    new TextMessageEnterTransition(chatMessageCell, chatActivity2, this.recyclerListView, chatActivity2.messageEnterTransitionContainer, this.resourcesProvider).start();
                }
                this.activity.getChatActivityEnterView().startMessageTransition();
            }
        }
        animate.translationY(0.0f).setDuration(getMoveDuration()).setInterpolator(this.translationInterpolator).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                view.setTranslationY(0.0f);
                View view3 = view;
                if (view3 instanceof ChatMessageCell) {
                    ((ChatMessageCell) view3).getTransitionParams().messageEntering = false;
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                View view3 = view;
                if (view3 instanceof ChatMessageCell) {
                    ((ChatMessageCell) view3).getTransitionParams().messageEntering = false;
                }
                animate.setListener(null);
                if (ChatListItemAnimator.this.mAddAnimations.remove(viewHolder)) {
                    ChatListItemAnimator.this.dispatchAddFinished(viewHolder);
                    ChatListItemAnimator.this.dispatchFinishedWhenDone();
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ChatListItemAnimator.this.dispatchAddStarting(viewHolder);
            }
        }).start();
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean animateAppearance(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2) {
        int i;
        boolean animateAppearance = super.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2);
        if (animateAppearance && this.shouldAnimateEnterFromBottom) {
            boolean z = false;
            for (int i2 = 0; i2 < this.mPendingAdditions.size(); i2++) {
                if (((RecyclerView.ViewHolder) this.mPendingAdditions.get(i2)).getLayoutPosition() == 0) {
                    z = true;
                }
            }
            if (z) {
                i = 0;
                for (int i3 = 0; i3 < this.mPendingAdditions.size(); i3++) {
                    i += ((RecyclerView.ViewHolder) this.mPendingAdditions.get(i3)).itemView.getHeight();
                }
            } else {
                i = 0;
            }
            for (int i4 = 0; i4 < this.mPendingAdditions.size(); i4++) {
                ((RecyclerView.ViewHolder) this.mPendingAdditions.get(i4)).itemView.setTranslationY(i);
            }
        }
        return animateAppearance;
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, int i, int i2, int i3, int i4) {
        if (viewHolder == viewHolder2) {
            return animateMove(viewHolder, itemHolderInfo, i, i2, i3, i4);
        }
        View view = viewHolder.itemView;
        float animationOffsetX = view instanceof ChatMessageCell ? ((ChatMessageCell) view).getAnimationOffsetX() : view.getTranslationX();
        float translationY = viewHolder.itemView.getTranslationY();
        float alpha = viewHolder.itemView.getAlpha();
        resetAnimation(viewHolder);
        int i5 = (int) ((i3 - i) - animationOffsetX);
        int i6 = (int) ((i4 - i2) - translationY);
        View view2 = viewHolder.itemView;
        if (view2 instanceof ChatMessageCell) {
            ((ChatMessageCell) view2).setAnimationOffsetX(animationOffsetX);
        } else {
            view2.setTranslationX(animationOffsetX);
        }
        viewHolder.itemView.setTranslationY(translationY);
        viewHolder.itemView.setAlpha(alpha);
        if (viewHolder2 != null) {
            resetAnimation(viewHolder2);
            View view3 = viewHolder2.itemView;
            if (view3 instanceof ChatMessageCell) {
                ((ChatMessageCell) view3).setAnimationOffsetX(-i5);
            } else {
                view3.setTranslationX(-i5);
            }
            viewHolder2.itemView.setTranslationY(-i6);
            viewHolder2.itemView.setAlpha(0.0f);
        }
        this.mPendingChanges.add(new DefaultItemAnimator.ChangeInfo(viewHolder, viewHolder2, i, i2, i3, i4));
        checkIsRunning();
        return true;
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    public void animateChangeImpl(final DefaultItemAnimator.ChangeInfo changeInfo) {
        RecyclerView.ViewHolder viewHolder = changeInfo.oldHolder;
        final View view = viewHolder == null ? null : viewHolder.itemView;
        RecyclerView.ViewHolder viewHolder2 = changeInfo.newHolder;
        final View view2 = viewHolder2 != null ? viewHolder2.itemView : null;
        if (view != null) {
            final ViewPropertyAnimator duration = view.animate().setDuration(getChangeDuration());
            this.mChangeAnimations.add(changeInfo.oldHolder);
            duration.translationX(changeInfo.toX - changeInfo.fromX);
            duration.translationY(changeInfo.toY - changeInfo.fromY);
            duration.alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    duration.setListener(null);
                    view.setAlpha(1.0f);
                    view.setScaleX(1.0f);
                    view.setScaleX(1.0f);
                    View view3 = view;
                    if (view3 instanceof ChatMessageCell) {
                        ((ChatMessageCell) view3).setAnimationOffsetX(0.0f);
                    } else {
                        view3.setTranslationX(0.0f);
                    }
                    view.setTranslationY(0.0f);
                    if (ChatListItemAnimator.this.mChangeAnimations.remove(changeInfo.oldHolder)) {
                        ChatListItemAnimator.this.dispatchChangeFinished(changeInfo.oldHolder, true);
                        ChatListItemAnimator.this.dispatchFinishedWhenDone();
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    ChatListItemAnimator.this.dispatchChangeStarting(changeInfo.oldHolder, true);
                }
            }).start();
        }
        if (view2 != null) {
            final ViewPropertyAnimator animate = view2.animate();
            this.mChangeAnimations.add(changeInfo.newHolder);
            animate.translationX(0.0f).translationY(0.0f).setDuration(getChangeDuration()).alpha(1.0f).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.8
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    animate.setListener(null);
                    view2.setAlpha(1.0f);
                    view2.setScaleX(1.0f);
                    view2.setScaleX(1.0f);
                    View view3 = view2;
                    if (view3 instanceof ChatMessageCell) {
                        ((ChatMessageCell) view3).setAnimationOffsetX(0.0f);
                    } else {
                        view3.setTranslationX(0.0f);
                    }
                    view2.setTranslationY(0.0f);
                    if (ChatListItemAnimator.this.mChangeAnimations.remove(changeInfo.newHolder)) {
                        ChatListItemAnimator.this.dispatchChangeFinished(changeInfo.newHolder, false);
                        ChatListItemAnimator.this.dispatchFinishedWhenDone();
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    ChatListItemAnimator.this.dispatchChangeStarting(changeInfo.newHolder, false);
                }
            }).start();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0064  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x0478  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x009d  */
    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean animateMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, int i, int i2, int i3, int i4) {
        int i5;
        int translationX;
        ChatMessageCell chatMessageCell;
        ChatActionCell chatActionCell;
        int translationX2;
        float f;
        float f2;
        float f3;
        float f4;
        int i6;
        int i7;
        int i8;
        boolean z;
        float f5;
        MessageObject.GroupedMessages groupedMessages;
        int i9;
        int i10;
        View view = viewHolder.itemView;
        if (view instanceof ChatMessageCell) {
            ChatMessageCell chatMessageCell2 = (ChatMessageCell) view;
            translationX2 = i + ((int) chatMessageCell2.getAnimationOffsetX());
            i5 = chatMessageCell2.getTransitionParams().lastTopOffset != chatMessageCell2.getTopMediaOffset() ? i2 + (chatMessageCell2.getTransitionParams().lastTopOffset - chatMessageCell2.getTopMediaOffset()) : i2;
            chatMessageCell = chatMessageCell2;
            chatActionCell = null;
        } else {
            if (!(view instanceof ChatActionCell)) {
                i5 = i2;
                translationX = i + ((int) view.getTranslationX());
                chatMessageCell = null;
                chatActionCell = null;
                int translationY = i5 + ((int) viewHolder.itemView.getTranslationY());
                int[] iArr = new int[4];
                if (chatMessageCell == null) {
                    float imageX = chatMessageCell.getPhotoImage().getImageX();
                    float imageY = chatMessageCell.getPhotoImage().getImageY();
                    f2 = chatMessageCell.getPhotoImage().getImageWidth();
                    float imageHeight = chatMessageCell.getPhotoImage().getImageHeight();
                    for (int i11 = 0; i11 < 4; i11++) {
                        iArr[i11] = chatMessageCell.getPhotoImage().getRoundRadius()[i11];
                    }
                    f4 = imageX;
                    f3 = imageY;
                    f = imageHeight;
                } else {
                    f = 0.0f;
                    f2 = 0.0f;
                    f3 = 0.0f;
                    f4 = 0.0f;
                }
                resetAnimation(viewHolder);
                int i12 = i3 - translationX;
                i6 = i4 - translationY;
                if (i6 != 0) {
                    view.setTranslationY(-i6);
                }
                ChatActionCell chatActionCell2 = chatActionCell;
                int i13 = i12;
                float f6 = f;
                float f7 = f2;
                float f8 = f3;
                float f9 = f4;
                MoveInfoExtended moveInfoExtended = new MoveInfoExtended(viewHolder, translationX, translationY, i3, i4);
                if (chatMessageCell == null) {
                    ChatMessageCell.TransitionParams transitionParams = chatMessageCell.getTransitionParams();
                    if (!transitionParams.supportChangeAnimation()) {
                        if (i13 == 0 && i6 == 0) {
                            dispatchMoveFinished(viewHolder);
                            return false;
                        }
                        if (i13 != 0) {
                            view.setTranslationX(-i13);
                        }
                        this.mPendingMoves.add(moveInfoExtended);
                        checkIsRunning();
                        return true;
                    }
                    MessageObject.GroupedMessages currentMessagesGroup = chatMessageCell.getCurrentMessagesGroup();
                    if (i13 != 0) {
                        chatMessageCell.setAnimationOffsetX(-i13);
                    }
                    if (itemHolderInfo instanceof ItemHolderInfoExtended) {
                        ImageReceiver photoImage = chatMessageCell.getPhotoImage();
                        ItemHolderInfoExtended itemHolderInfoExtended = (ItemHolderInfoExtended) itemHolderInfo;
                        boolean z2 = (!transitionParams.wasDraw || itemHolderInfoExtended.imageHeight == 0.0f || itemHolderInfoExtended.imageWidth == 0.0f) ? false : true;
                        moveInfoExtended.animateImage = z2;
                        if (z2) {
                            this.recyclerListView.setClipChildren(false);
                            this.recyclerListView.invalidate();
                            transitionParams.imageChangeBoundsTransition = true;
                            if (chatMessageCell.getMessageObject().isRoundVideo()) {
                                transitionParams.animateToImageX = f9;
                                transitionParams.animateToImageY = f8;
                                transitionParams.animateToImageW = f7;
                                transitionParams.animateToImageH = f6;
                                transitionParams.animateToRadius = iArr;
                            } else {
                                transitionParams.animateToImageX = photoImage.getImageX();
                                transitionParams.animateToImageY = photoImage.getImageY();
                                transitionParams.animateToImageW = photoImage.getImageWidth();
                                transitionParams.animateToImageH = photoImage.getImageHeight();
                                transitionParams.animateToRadius = photoImage.getRoundRadius();
                            }
                            transitionParams.animateRadius = false;
                            int i14 = 0;
                            while (true) {
                                if (i14 >= 4) {
                                    break;
                                }
                                if (transitionParams.imageRoundRadius[i14] != transitionParams.animateToRadius[i14]) {
                                    transitionParams.animateRadius = true;
                                    break;
                                }
                                i14++;
                            }
                            float f10 = transitionParams.animateToImageX;
                            float f11 = itemHolderInfoExtended.imageX;
                            if (f10 == f11 && transitionParams.animateToImageY == itemHolderInfoExtended.imageY && transitionParams.animateToImageH == itemHolderInfoExtended.imageHeight && transitionParams.animateToImageW == itemHolderInfoExtended.imageWidth && !transitionParams.animateRadius) {
                                transitionParams.imageChangeBoundsTransition = false;
                                moveInfoExtended.animateImage = false;
                            } else {
                                moveInfoExtended.imageX = f11;
                                moveInfoExtended.imageY = itemHolderInfoExtended.imageY;
                                moveInfoExtended.imageWidth = itemHolderInfoExtended.imageWidth;
                                moveInfoExtended.imageHeight = itemHolderInfoExtended.imageHeight;
                                if (currentMessagesGroup != null) {
                                    boolean z3 = currentMessagesGroup.hasCaption;
                                    MessageObject.GroupedMessages.TransitionParams transitionParams2 = currentMessagesGroup.transitionParams;
                                    boolean z4 = transitionParams2.drawCaptionLayout;
                                    if (z3 != z4) {
                                        transitionParams2.captionEnterProgress = z4 ? 1.0f : 0.0f;
                                    }
                                }
                                if (transitionParams.animateRadius) {
                                    if (transitionParams.animateToRadius == photoImage.getRoundRadius()) {
                                        transitionParams.animateToRadius = new int[4];
                                        for (int i15 = 0; i15 < 4; i15++) {
                                            transitionParams.animateToRadius[i15] = photoImage.getRoundRadius()[i15];
                                        }
                                    }
                                    photoImage.setRoundRadius(transitionParams.imageRoundRadius);
                                }
                                chatMessageCell.setImageCoords(moveInfoExtended.imageX, moveInfoExtended.imageY, moveInfoExtended.imageWidth, moveInfoExtended.imageHeight);
                            }
                        }
                        if (currentMessagesGroup == null && transitionParams.wasDraw) {
                            boolean isOutOwner = chatMessageCell.getMessageObject().isOutOwner();
                            boolean z5 = (isOutOwner && transitionParams.lastDrawingBackgroundRect.left != chatMessageCell.getBackgroundDrawableLeft()) || !(isOutOwner || transitionParams.lastDrawingBackgroundRect.right == chatMessageCell.getBackgroundDrawableRight());
                            if (z5 || transitionParams.lastDrawingBackgroundRect.top != chatMessageCell.getBackgroundDrawableTop() || transitionParams.lastDrawingBackgroundRect.bottom != chatMessageCell.getBackgroundDrawableBottom()) {
                                moveInfoExtended.deltaBottom = chatMessageCell.getBackgroundDrawableBottom() - transitionParams.lastDrawingBackgroundRect.bottom;
                                moveInfoExtended.deltaTop = chatMessageCell.getBackgroundDrawableTop() - transitionParams.lastDrawingBackgroundRect.top;
                                if (isOutOwner) {
                                    moveInfoExtended.deltaLeft = chatMessageCell.getBackgroundDrawableLeft() - transitionParams.lastDrawingBackgroundRect.left;
                                } else {
                                    moveInfoExtended.deltaRight = chatMessageCell.getBackgroundDrawableRight() - transitionParams.lastDrawingBackgroundRect.right;
                                }
                                moveInfoExtended.animateBackgroundOnly = true;
                                transitionParams.animateBackgroundBoundsInner = true;
                                transitionParams.animateBackgroundWidth = z5;
                                transitionParams.deltaLeft = -moveInfoExtended.deltaLeft;
                                transitionParams.deltaRight = -moveInfoExtended.deltaRight;
                                transitionParams.deltaTop = -moveInfoExtended.deltaTop;
                                transitionParams.deltaBottom = -moveInfoExtended.deltaBottom;
                                this.recyclerListView.setClipChildren(false);
                                this.recyclerListView.invalidate();
                            }
                        }
                    }
                    if (currentMessagesGroup == null || !this.willChangedGroups.contains(currentMessagesGroup)) {
                        i7 = i13;
                        i8 = i6;
                    } else {
                        this.willChangedGroups.remove(currentMessagesGroup);
                        RecyclerListView recyclerListView = (RecyclerListView) viewHolder.itemView.getParent();
                        MessageObject.GroupedMessages.TransitionParams transitionParams3 = currentMessagesGroup.transitionParams;
                        int i16 = 0;
                        int i17 = 0;
                        int i18 = 0;
                        int i19 = 0;
                        int i20 = 0;
                        boolean z6 = true;
                        while (i18 < recyclerListView.getChildCount()) {
                            View childAt = recyclerListView.getChildAt(i18);
                            if (childAt instanceof ChatMessageCell) {
                                ChatMessageCell chatMessageCell3 = (ChatMessageCell) childAt;
                                if (chatMessageCell3.getCurrentMessagesGroup() == currentMessagesGroup && !chatMessageCell3.getMessageObject().deleted) {
                                    int left = chatMessageCell3.getLeft() + chatMessageCell3.getBackgroundDrawableLeft();
                                    groupedMessages = currentMessagesGroup;
                                    int left2 = chatMessageCell3.getLeft() + chatMessageCell3.getBackgroundDrawableRight();
                                    i10 = i6;
                                    int top = chatMessageCell3.getTop() + chatMessageCell3.getBackgroundDrawableTop();
                                    i9 = i13;
                                    int top2 = chatMessageCell3.getTop() + chatMessageCell3.getBackgroundDrawableBottom();
                                    if (i16 == 0 || left < i16) {
                                        i16 = left;
                                    }
                                    if (i17 == 0 || left2 > i17) {
                                        i17 = left2;
                                    }
                                    if (chatMessageCell3.getTransitionParams().wasDraw || transitionParams3.isNewGroup) {
                                        if (i19 == 0 || top < i19) {
                                            i19 = top;
                                        }
                                        if (i20 == 0 || top2 > i20) {
                                            i20 = top2;
                                        }
                                        z6 = false;
                                    }
                                    i18++;
                                    currentMessagesGroup = groupedMessages;
                                    i6 = i10;
                                    i13 = i9;
                                }
                            }
                            groupedMessages = currentMessagesGroup;
                            i9 = i13;
                            i10 = i6;
                            i18++;
                            currentMessagesGroup = groupedMessages;
                            i6 = i10;
                            i13 = i9;
                        }
                        i7 = i13;
                        i8 = i6;
                        transitionParams3.isNewGroup = false;
                        if (i19 == 0 && i20 == 0 && i16 == 0 && i17 == 0) {
                            moveInfoExtended.animateChangeGroupBackground = false;
                            transitionParams3.backgroundChangeBounds = false;
                        } else {
                            int i21 = (-i19) + transitionParams3.top;
                            moveInfoExtended.groupOffsetTop = i21;
                            int i22 = (-i20) + transitionParams3.bottom;
                            moveInfoExtended.groupOffsetBottom = i22;
                            int i23 = (-i16) + transitionParams3.left;
                            moveInfoExtended.groupOffsetLeft = i23;
                            int i24 = (-i17) + transitionParams3.right;
                            moveInfoExtended.groupOffsetRight = i24;
                            moveInfoExtended.animateChangeGroupBackground = true;
                            transitionParams3.backgroundChangeBounds = true;
                            transitionParams3.offsetTop = i21;
                            transitionParams3.offsetBottom = i22;
                            transitionParams3.offsetLeft = i23;
                            transitionParams3.offsetRight = i24;
                            transitionParams3.captionEnterProgress = transitionParams3.drawCaptionLayout ? 1.0f : 0.0f;
                            recyclerListView.setClipChildren(false);
                            recyclerListView.invalidate();
                        }
                        transitionParams3.drawBackgroundForDeletedItems = z6;
                    }
                    MessageObject.GroupedMessages groupedMessages2 = (MessageObject.GroupedMessages) this.willRemovedGroup.get(Integer.valueOf(chatMessageCell.getMessageObject().getId()));
                    if (groupedMessages2 != null) {
                        MessageObject.GroupedMessages.TransitionParams transitionParams4 = groupedMessages2.transitionParams;
                        this.willRemovedGroup.remove(Integer.valueOf(chatMessageCell.getMessageObject().getId()));
                        if (transitionParams.wasDraw) {
                            int left3 = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableLeft();
                            int left4 = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableRight();
                            int top3 = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableTop();
                            int top4 = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableBottom();
                            moveInfoExtended.animateRemoveGroup = true;
                            transitionParams.animateBackgroundBoundsInner = true;
                            moveInfoExtended.deltaLeft = left3 - transitionParams4.left;
                            moveInfoExtended.deltaRight = left4 - transitionParams4.right;
                            moveInfoExtended.deltaTop = top3 - transitionParams4.top;
                            moveInfoExtended.deltaBottom = top4 - transitionParams4.bottom;
                            moveInfoExtended.animateBackgroundOnly = false;
                            transitionParams.deltaLeft = (int) ((-r2) - chatMessageCell.getAnimationOffsetX());
                            transitionParams.deltaRight = (int) ((-moveInfoExtended.deltaRight) - chatMessageCell.getAnimationOffsetX());
                            transitionParams.deltaTop = (int) ((-moveInfoExtended.deltaTop) - chatMessageCell.getTranslationY());
                            transitionParams.deltaBottom = (int) ((-moveInfoExtended.deltaBottom) - chatMessageCell.getTranslationY());
                            z = true;
                            transitionParams.transformGroupToSingleMessage = true;
                            this.recyclerListView.setClipChildren(false);
                            this.recyclerListView.invalidate();
                        } else {
                            z = true;
                            transitionParams4.drawBackgroundForDeletedItems = true;
                        }
                    } else {
                        z = true;
                    }
                    if (transitionParams.drawPinnedBottomBackground != chatMessageCell.isDrawPinnedBottom()) {
                        moveInfoExtended.animatePinnedBottom = z;
                        f5 = 0.0f;
                        transitionParams.changePinnedBottomProgress = 0.0f;
                    } else {
                        f5 = 0.0f;
                    }
                    boolean animateChange = transitionParams.animateChange();
                    moveInfoExtended.animateChangeInternal = animateChange;
                    if (animateChange) {
                        transitionParams.animateChange = z;
                        transitionParams.animateChangeProgress = f5;
                    }
                    if (i7 == 0 && i8 == 0 && !moveInfoExtended.animateImage && !moveInfoExtended.animateRemoveGroup && !moveInfoExtended.animateChangeGroupBackground && !moveInfoExtended.animatePinnedBottom && !moveInfoExtended.animateBackgroundOnly && !animateChange) {
                        dispatchMoveFinished(viewHolder);
                        return false;
                    }
                } else if (chatActionCell2 != null) {
                    ChatActionCell.TransitionParams transitionParams5 = chatActionCell2.getTransitionParams();
                    if (!transitionParams5.supportChangeAnimation()) {
                        if (i13 == 0 && i6 == 0) {
                            dispatchMoveFinished(viewHolder);
                            return false;
                        }
                        if (i13 != 0) {
                            view.setTranslationX(-i13);
                        }
                        this.mPendingMoves.add(moveInfoExtended);
                        checkIsRunning();
                        return true;
                    }
                    if (i13 != 0) {
                        view.setTranslationX(-i13);
                    }
                    boolean animateChange2 = transitionParams5.animateChange();
                    moveInfoExtended.animateChangeInternal = animateChange2;
                    if (animateChange2) {
                        transitionParams5.animateChange = true;
                        transitionParams5.animateChangeProgress = 0.0f;
                    }
                    if (i13 == 0 && i6 == 0 && !animateChange2) {
                        dispatchMoveFinished(viewHolder);
                        return false;
                    }
                } else {
                    View view2 = viewHolder.itemView;
                    if (view2 instanceof BotHelpCell) {
                        ((BotHelpCell) view2).setAnimating(true);
                    } else {
                        if (i13 == 0 && i6 == 0) {
                            dispatchMoveFinished(viewHolder);
                            return false;
                        }
                        if (i13 != 0) {
                            view.setTranslationX(-i13);
                        }
                    }
                }
                this.mPendingMoves.add(moveInfoExtended);
                checkIsRunning();
                return true;
            }
            translationX2 = i + ((int) view.getTranslationX());
            i5 = i2;
            chatActionCell = (ChatActionCell) view;
            chatMessageCell = null;
        }
        translationX = translationX2;
        int translationY2 = i5 + ((int) viewHolder.itemView.getTranslationY());
        int[] iArr2 = new int[4];
        if (chatMessageCell == null) {
        }
        resetAnimation(viewHolder);
        int i122 = i3 - translationX;
        i6 = i4 - translationY2;
        if (i6 != 0) {
        }
        ChatActionCell chatActionCell22 = chatActionCell;
        int i132 = i122;
        float f62 = f;
        float f72 = f2;
        float f82 = f3;
        float f92 = f4;
        MoveInfoExtended moveInfoExtended2 = new MoveInfoExtended(viewHolder, translationX, translationY2, i3, i4);
        if (chatMessageCell == null) {
        }
        this.mPendingMoves.add(moveInfoExtended2);
        checkIsRunning();
        return true;
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    protected void animateMoveImpl(RecyclerView.ViewHolder viewHolder, DefaultItemAnimator.MoveInfo moveInfo) {
        animateMoveImpl(viewHolder, moveInfo, false);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r1v44 android.animation.TimeInterpolator, still in use, count: 2, list:
          (r1v44 android.animation.TimeInterpolator) from 0x024f: IF  (r1v44 android.animation.TimeInterpolator) != (null android.animation.TimeInterpolator)  -> B:11:0x0249 A[HIDDEN]
          (r1v44 android.animation.TimeInterpolator) from 0x0249: PHI (r1v49 android.animation.TimeInterpolator) = (r1v44 android.animation.TimeInterpolator) binds: [B:19:0x024f] A[DONT_GENERATE, DONT_INLINE]
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:125)
        	at jadx.core.dex.visitors.regions.TernaryMod.processRegion(TernaryMod.java:62)
        	at jadx.core.dex.visitors.regions.TernaryMod.enterRegion(TernaryMod.java:45)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:67)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1085)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
        	at jadx.core.dex.visitors.regions.TernaryMod.process(TernaryMod.java:35)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.process(IfRegionVisitor.java:34)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:30)
        */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0247  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0259  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x024d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void animateMoveImpl(final androidx.recyclerview.widget.RecyclerView.ViewHolder r28, androidx.recyclerview.widget.DefaultItemAnimator.MoveInfo r29, boolean r30) {
        /*
            Method dump skipped, instructions count: 684
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.ChatListItemAnimator.animateMoveImpl(androidx.recyclerview.widget.RecyclerView$ViewHolder, androidx.recyclerview.widget.DefaultItemAnimator$MoveInfo, boolean):void");
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate remove");
        }
        boolean animateRemove = super.animateRemove(viewHolder, itemHolderInfo);
        if (animateRemove && itemHolderInfo != null) {
            int i = itemHolderInfo.top;
            int top = viewHolder.itemView.getTop();
            int left = viewHolder.itemView.getLeft() - itemHolderInfo.left;
            if (top - i != 0) {
                viewHolder.itemView.setTranslationY(-r2);
            }
            View view = viewHolder.itemView;
            if (view instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                if (left != 0) {
                    chatMessageCell.setAnimationOffsetX(-left);
                }
                if (itemHolderInfo instanceof ItemHolderInfoExtended) {
                    ItemHolderInfoExtended itemHolderInfoExtended = (ItemHolderInfoExtended) itemHolderInfo;
                    chatMessageCell.setImageCoords(itemHolderInfoExtended.imageX, itemHolderInfoExtended.imageY, itemHolderInfoExtended.imageWidth, itemHolderInfoExtended.imageHeight);
                }
            } else if (left != 0) {
                view.setTranslationX(-left);
            }
        }
        return animateRemove;
    }

    protected void animateRemoveImpl(final RecyclerView.ViewHolder viewHolder, boolean z) {
        Utilities.Callback0Return callback0Return;
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder sb = new StringBuilder();
            sb.append("animate remove impl ");
            sb.append(z ? " with thanos" : "");
            FileLog.d(sb.toString());
        }
        final View view = viewHolder.itemView;
        this.mRemoveAnimations.add(viewHolder);
        if (!z || (callback0Return = this.getThanosEffectContainer) == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, (Property<View, Float>) View.ALPHA, view.getAlpha(), 0.0f);
            dispatchRemoveStarting(viewHolder);
            ofFloat.setDuration(getRemoveDuration());
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.11
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    animator.removeAllListeners();
                    view.setAlpha(1.0f);
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
                    view.setTranslationX(0.0f);
                    view.setTranslationY(0.0f);
                    if (ChatListItemAnimator.this.mRemoveAnimations.remove(viewHolder)) {
                        ChatListItemAnimator.this.dispatchRemoveFinished(viewHolder);
                        ChatListItemAnimator.this.dispatchFinishedWhenDone();
                    }
                }
            });
            this.animators.put(viewHolder, ofFloat);
            ofFloat.start();
        } else {
            ThanosEffect thanosEffect = (ThanosEffect) callback0Return.run();
            dispatchRemoveStarting(viewHolder);
            thanosEffect.animate(view, new Runnable() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    ChatListItemAnimator.this.lambda$animateRemoveImpl$9(view, viewHolder);
                }
            });
            this.thanosViews.add(view);
        }
        this.recyclerListView.stopScroll();
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {
        ThanosEffect thanosEffect;
        Animator animator = (Animator) this.animators.remove(viewHolder);
        if (animator != null) {
            animator.cancel();
        }
        if (this.thanosViews.contains(viewHolder.itemView) && (thanosEffect = (ThanosEffect) this.getThanosEffectContainer.run()) != null) {
            thanosEffect.cancel(viewHolder.itemView);
        }
        super.endAnimation(viewHolder);
        restoreTransitionParams(viewHolder.itemView);
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimations() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("end animations");
        }
        Iterator it = this.willChangedGroups.iterator();
        while (it.hasNext()) {
            ((MessageObject.GroupedMessages) it.next()).transitionParams.isNewGroup = false;
        }
        this.willChangedGroups.clear();
        cancelAnimators();
        ChatGreetingsView chatGreetingsView = this.chatGreetingsView;
        if (chatGreetingsView != null) {
            chatGreetingsView.stickerToSendView.setAlpha(1.0f);
        }
        this.greetingsSticker = null;
        this.chatGreetingsView = null;
        for (int size = this.mPendingMoves.size() - 1; size >= 0; size--) {
            DefaultItemAnimator.MoveInfo moveInfo = (DefaultItemAnimator.MoveInfo) this.mPendingMoves.get(size);
            restoreTransitionParams(moveInfo.holder.itemView);
            dispatchMoveFinished(moveInfo.holder);
            this.mPendingMoves.remove(size);
        }
        for (int size2 = this.mPendingRemovals.size() - 1; size2 >= 0; size2--) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) this.mPendingRemovals.get(size2);
            restoreTransitionParams(viewHolder.itemView);
            dispatchRemoveFinished(viewHolder);
            this.mPendingRemovals.remove(size2);
        }
        for (int size3 = this.mPendingAdditions.size() - 1; size3 >= 0; size3--) {
            RecyclerView.ViewHolder viewHolder2 = (RecyclerView.ViewHolder) this.mPendingAdditions.get(size3);
            restoreTransitionParams(viewHolder2.itemView);
            dispatchAddFinished(viewHolder2);
            this.mPendingAdditions.remove(size3);
        }
        for (int size4 = this.mPendingChanges.size() - 1; size4 >= 0; size4--) {
            endChangeAnimationIfNecessary((DefaultItemAnimator.ChangeInfo) this.mPendingChanges.get(size4));
        }
        this.mPendingChanges.clear();
        if (isRunning()) {
            for (int size5 = this.mMovesList.size() - 1; size5 >= 0; size5--) {
                ArrayList arrayList = (ArrayList) this.mMovesList.get(size5);
                for (int size6 = arrayList.size() - 1; size6 >= 0; size6--) {
                    DefaultItemAnimator.MoveInfo moveInfo2 = (DefaultItemAnimator.MoveInfo) arrayList.get(size6);
                    restoreTransitionParams(moveInfo2.holder.itemView);
                    dispatchMoveFinished(moveInfo2.holder);
                    arrayList.remove(size6);
                    if (arrayList.isEmpty()) {
                        this.mMovesList.remove(arrayList);
                    }
                }
            }
            for (int size7 = this.mAdditionsList.size() - 1; size7 >= 0; size7--) {
                ArrayList arrayList2 = (ArrayList) this.mAdditionsList.get(size7);
                for (int size8 = arrayList2.size() - 1; size8 >= 0; size8--) {
                    RecyclerView.ViewHolder viewHolder3 = (RecyclerView.ViewHolder) arrayList2.get(size8);
                    restoreTransitionParams(viewHolder3.itemView);
                    dispatchAddFinished(viewHolder3);
                    arrayList2.remove(size8);
                    if (arrayList2.isEmpty()) {
                        this.mAdditionsList.remove(arrayList2);
                    }
                }
            }
            for (int size9 = this.mChangesList.size() - 1; size9 >= 0; size9--) {
                ArrayList arrayList3 = (ArrayList) this.mChangesList.get(size9);
                for (int size10 = arrayList3.size() - 1; size10 >= 0; size10--) {
                    endChangeAnimationIfNecessary((DefaultItemAnimator.ChangeInfo) arrayList3.get(size10));
                    if (arrayList3.isEmpty()) {
                        this.mChangesList.remove(arrayList3);
                    }
                }
            }
            cancelAll(this.mRemoveAnimations);
            cancelAll(this.mMoveAnimations);
            cancelAll(this.mAddAnimations);
            cancelAll(this.mChangeAnimations);
            dispatchAnimationsFinished();
        }
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    protected boolean endChangeAnimationIfNecessary(DefaultItemAnimator.ChangeInfo changeInfo, RecyclerView.ViewHolder viewHolder) {
        ThanosEffect thanosEffect;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("end change if necessary");
        }
        Animator animator = (Animator) this.animators.remove(viewHolder);
        if (animator != null) {
            animator.cancel();
        }
        if (this.thanosViews.contains(viewHolder.itemView) && (thanosEffect = (ThanosEffect) this.getThanosEffectContainer.run()) != null) {
            thanosEffect.cancel(viewHolder.itemView);
        }
        boolean z = false;
        if (changeInfo.newHolder == viewHolder) {
            changeInfo.newHolder = null;
        } else {
            if (changeInfo.oldHolder != viewHolder) {
                return false;
            }
            changeInfo.oldHolder = null;
            z = true;
        }
        restoreTransitionParams(viewHolder.itemView);
        dispatchChangeFinished(viewHolder, z);
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public long getChangeDuration() {
        return 250L;
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    protected long getMoveAnimationDelay() {
        return 0L;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public long getMoveDuration() {
        return 250L;
    }

    public void groupWillChanged(MessageObject.GroupedMessages groupedMessages) {
        if (groupedMessages == null) {
            return;
        }
        if (groupedMessages.messages.size() == 0) {
            groupedMessages.transitionParams.drawBackgroundForDeletedItems = true;
            return;
        }
        MessageObject.GroupedMessages.TransitionParams transitionParams = groupedMessages.transitionParams;
        if (transitionParams.top == 0 && transitionParams.bottom == 0 && transitionParams.left == 0 && transitionParams.right == 0) {
            int childCount = this.recyclerListView.getChildCount();
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    break;
                }
                View childAt = this.recyclerListView.getChildAt(i);
                if (childAt instanceof ChatMessageCell) {
                    ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                    MessageObject messageObject = chatMessageCell.getMessageObject();
                    if (chatMessageCell.getTransitionParams().wasDraw && groupedMessages.messages.contains(messageObject)) {
                        groupedMessages.transitionParams.top = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableTop();
                        groupedMessages.transitionParams.bottom = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableBottom();
                        groupedMessages.transitionParams.left = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableLeft();
                        groupedMessages.transitionParams.right = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableRight();
                        groupedMessages.transitionParams.drawCaptionLayout = chatMessageCell.hasCaptionLayout();
                        groupedMessages.transitionParams.pinnedTop = chatMessageCell.isPinnedTop();
                        groupedMessages.transitionParams.pinnedBotton = chatMessageCell.isPinnedBottom();
                        groupedMessages.transitionParams.isNewGroup = true;
                        break;
                    }
                }
                i++;
            }
        }
        this.willChangedGroups.add(groupedMessages);
    }

    public void groupWillTransformToSingleMessage(MessageObject.GroupedMessages groupedMessages) {
        this.willRemovedGroup.put(Integer.valueOf(groupedMessages.messages.get(0).getId()), groupedMessages);
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    protected void onAllAnimationsDone() {
        super.onAllAnimationsDone();
        this.recyclerListView.setClipChildren(true);
        while (!this.runOnAnimationsEnd.isEmpty()) {
            ((Runnable) this.runOnAnimationsEnd.remove(0)).run();
        }
        cancelAnimators();
    }

    public void onAnimationStart() {
    }

    public void onDestroy() {
        onAllAnimationsDone();
    }

    public void onGreetingStickerTransition(RecyclerView.ViewHolder viewHolder, ChatGreetingsView chatGreetingsView) {
        this.greetingsSticker = viewHolder;
        this.chatGreetingsView = chatGreetingsView;
        this.shouldAnimateEnterFromBottom = false;
    }

    public void prepareThanos(RecyclerView.ViewHolder viewHolder) {
        MessageObject messageObject;
        if (viewHolder == null) {
            return;
        }
        this.toBeSnapped.add(viewHolder);
        View view = viewHolder.itemView;
        if (!(view instanceof ChatMessageCell) || (messageObject = ((ChatMessageCell) view).getMessageObject()) == null) {
            return;
        }
        messageObject.deletedByThanos = true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public RecyclerView.ItemAnimator.ItemHolderInfo recordPreLayoutInformation(RecyclerView.State state, RecyclerView.ViewHolder viewHolder, int i, List list) {
        RecyclerView.ItemAnimator.ItemHolderInfo recordPreLayoutInformation = super.recordPreLayoutInformation(state, viewHolder, i, list);
        View view = viewHolder.itemView;
        if (!(view instanceof ChatMessageCell)) {
            return recordPreLayoutInformation;
        }
        ItemHolderInfoExtended itemHolderInfoExtended = new ItemHolderInfoExtended();
        itemHolderInfoExtended.left = recordPreLayoutInformation.left;
        itemHolderInfoExtended.top = recordPreLayoutInformation.top;
        itemHolderInfoExtended.right = recordPreLayoutInformation.right;
        itemHolderInfoExtended.bottom = recordPreLayoutInformation.bottom;
        ChatMessageCell.TransitionParams transitionParams = ((ChatMessageCell) view).getTransitionParams();
        itemHolderInfoExtended.imageX = transitionParams.lastDrawingImageX;
        itemHolderInfoExtended.imageY = transitionParams.lastDrawingImageY;
        itemHolderInfoExtended.imageWidth = transitionParams.lastDrawingImageW;
        itemHolderInfoExtended.imageHeight = transitionParams.lastDrawingImageH;
        return itemHolderInfoExtended;
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void runPendingAnimations() {
        int i;
        boolean z = !this.mPendingRemovals.isEmpty();
        boolean z2 = !this.mPendingMoves.isEmpty();
        boolean z3 = !this.mPendingChanges.isEmpty();
        boolean z4 = !this.mPendingAdditions.isEmpty();
        if (z || z2 || z4 || z3) {
            boolean z5 = false;
            if (this.shouldAnimateEnterFromBottom) {
                boolean z6 = false;
                while (i < this.mPendingAdditions.size()) {
                    if (this.reversePositions) {
                        i = ((RecyclerView.ViewHolder) this.mPendingAdditions.get(i)).getLayoutPosition() != (this.recyclerListView.getAdapter() == null ? 0 : this.recyclerListView.getAdapter().getItemCount()) - 1 ? i + 1 : 0;
                        z6 = true;
                    } else {
                        if (((RecyclerView.ViewHolder) this.mPendingAdditions.get(i)).getLayoutPosition() != 0) {
                        }
                        z6 = true;
                    }
                }
                z5 = z6;
            }
            onAnimationStart();
            if (z5) {
                runMessageEnterTransition();
            } else {
                runAlphaEnterTransition();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatListItemAnimator.this.lambda$runPendingAnimations$0(valueAnimator);
                }
            });
            ofFloat.setDuration(getRemoveDuration() + getMoveDuration());
            ofFloat.start();
        }
    }

    public void setOnSnapMessage(Utilities.Callback0Return callback0Return, Utilities.Callback0Return callback0Return2) {
        this.supportsThanosEffectContainer = callback0Return;
        this.getThanosEffectContainer = callback0Return2;
    }

    public void setReversePositions(boolean z) {
        this.reversePositions = z;
    }

    public void setShouldAnimateEnterFromBottom(boolean z) {
        this.shouldAnimateEnterFromBottom = z;
    }

    public boolean willAddedFromAlpha(View view) {
        RecyclerView.ViewHolder childViewHolder;
        if (this.shouldAnimateEnterFromBottom || (childViewHolder = this.recyclerListView.getChildViewHolder(view)) == null) {
            return false;
        }
        return this.mPendingAdditions.contains(childViewHolder) || this.mAddAnimations.contains(childViewHolder);
    }

    public boolean willRemoved(View view) {
        RecyclerView.ViewHolder childViewHolder = this.recyclerListView.getChildViewHolder(view);
        if (childViewHolder != null) {
            return this.mPendingRemovals.contains(childViewHolder) || this.mRemoveAnimations.contains(childViewHolder);
        }
        return false;
    }
}
