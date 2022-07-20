package org.telegram.ui.Components;

import android.content.Context;
import java.util.HashMap;
import java.util.Map;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Cells.ChatActionCell$$ExternalSyntheticLambda2;
/* loaded from: classes3.dex */
public class ChatActivityEnterViewAnimatedIconView extends RLottieImageView {
    private TransitState animatingState;
    private State currentState;
    private Map<TransitState, RLottieDrawable> stateMap = new AnonymousClass1(this);

    /* loaded from: classes3.dex */
    public enum State {
        VOICE,
        VIDEO,
        STICKER,
        KEYBOARD,
        SMILE,
        GIF
    }

    /* renamed from: org.telegram.ui.Components.ChatActivityEnterViewAnimatedIconView$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends HashMap<TransitState, RLottieDrawable> {
        AnonymousClass1(ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView) {
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public RLottieDrawable get(Object obj) {
            RLottieDrawable rLottieDrawable = (RLottieDrawable) super.get(obj);
            if (rLottieDrawable == null) {
                int i = ((TransitState) obj).resource;
                return new RLottieDrawable(i, String.valueOf(i), AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
            }
            return rLottieDrawable;
        }
    }

    public ChatActivityEnterViewAnimatedIconView(Context context) {
        super(context);
    }

    public void setState(State state, boolean z) {
        if (!z || state != this.currentState) {
            State state2 = this.currentState;
            this.currentState = state;
            if (!z || state2 == null || getState(state2, state) == null) {
                RLottieDrawable rLottieDrawable = this.stateMap.get(getAnyState(this.currentState));
                rLottieDrawable.stop();
                rLottieDrawable.setProgress(0.0f, false);
                setAnimation(rLottieDrawable);
                return;
            }
            TransitState state3 = getState(state2, this.currentState);
            if (state3 == this.animatingState) {
                return;
            }
            this.animatingState = state3;
            RLottieDrawable rLottieDrawable2 = this.stateMap.get(state3);
            rLottieDrawable2.stop();
            rLottieDrawable2.setProgress(0.0f, false);
            rLottieDrawable2.setAutoRepeat(0);
            rLottieDrawable2.setOnAnimationEndListener(new ChatActivityEnterViewAnimatedIconView$$ExternalSyntheticLambda0(this));
            setAnimation(rLottieDrawable2);
            AndroidUtilities.runOnUIThread(new ChatActionCell$$ExternalSyntheticLambda2(rLottieDrawable2));
        }
    }

    public /* synthetic */ void lambda$setState$0() {
        this.animatingState = null;
    }

    private TransitState getAnyState(State state) {
        TransitState[] values;
        for (TransitState transitState : TransitState.values()) {
            if (transitState.firstState == state) {
                return transitState;
            }
        }
        return null;
    }

    private TransitState getState(State state, State state2) {
        TransitState[] values;
        for (TransitState transitState : TransitState.values()) {
            if (transitState.firstState == state && transitState.secondState == state2) {
                return transitState;
            }
        }
        return null;
    }

    /* JADX WARN: Init of enum GIF_TO_KEYBOARD can be incorrect */
    /* JADX WARN: Init of enum GIF_TO_SMILE can be incorrect */
    /* JADX WARN: Init of enum KEYBOARD_TO_GIF can be incorrect */
    /* JADX WARN: Init of enum KEYBOARD_TO_SMILE can be incorrect */
    /* JADX WARN: Init of enum KEYBOARD_TO_STICKER can be incorrect */
    /* JADX WARN: Init of enum SMILE_TO_GIF can be incorrect */
    /* JADX WARN: Init of enum SMILE_TO_KEYBOARD can be incorrect */
    /* JADX WARN: Init of enum SMILE_TO_STICKER can be incorrect */
    /* JADX WARN: Init of enum STICKER_TO_KEYBOARD can be incorrect */
    /* JADX WARN: Init of enum STICKER_TO_SMILE can be incorrect */
    /* JADX WARN: Init of enum VIDEO_TO_VOICE can be incorrect */
    /* JADX WARN: Init of enum VOICE_TO_VIDEO can be incorrect */
    /* loaded from: classes3.dex */
    public enum TransitState {
        VOICE_TO_VIDEO(r7, r8, 2131558602),
        STICKER_TO_KEYBOARD(r16, r17, 2131558559),
        SMILE_TO_KEYBOARD(r10, r17, 2131558547),
        VIDEO_TO_VOICE(r8, r7, 2131558597),
        KEYBOARD_TO_STICKER(r17, r16, 2131558484),
        KEYBOARD_TO_GIF(r17, r12, 2131558482),
        KEYBOARD_TO_SMILE(r17, r10, 2131558483),
        GIF_TO_KEYBOARD(r12, r17, 2131558455),
        GIF_TO_SMILE(r12, r10, 2131558456),
        SMILE_TO_GIF(r10, r12, 2131558546),
        SMILE_TO_STICKER(r10, r16, 2131558548),
        STICKER_TO_SMILE(r16, r10, 2131558560);
        
        final State firstState;
        final int resource;
        final State secondState;

        static {
            State state = State.VOICE;
            State state2 = State.VIDEO;
            State state3 = State.STICKER;
            State state4 = State.KEYBOARD;
            State state5 = State.SMILE;
            State state6 = State.GIF;
        }

        TransitState(State state, State state2, int i) {
            this.firstState = state;
            this.secondState = state2;
            this.resource = i;
        }
    }
}
