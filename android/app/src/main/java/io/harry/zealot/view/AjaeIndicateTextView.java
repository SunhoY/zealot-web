package io.harry.zealot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import io.harry.zealot.R;
import io.harry.zealot.state.AjaePower;

import static io.harry.zealot.state.AjaePower.*;

public class AjaeIndicateTextView extends TextView {
    private AjaePower ajaeState;
    private int[] AJAE_STATE_FULL = new int[] {R.attr.state_ajae_full};
    private int[] AJAE_STATE_MEDIUM = new int[] {R.attr.state_ajae_medium};
    private int[] AJAE_STATE_NOT = new int[] {R.attr.state_ajae_not};

    public AjaeIndicateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] state = super.onCreateDrawableState(extraSpace + 1);

        if(ajaeState == null) {
            return state;
        }

        switch (ajaeState) {
            case FULL:
                mergeDrawableStates(state, AJAE_STATE_FULL);
                break;
            case MEDIUM:
                mergeDrawableStates(state, AJAE_STATE_MEDIUM);
                break;
            case NO:
                mergeDrawableStates(state, AJAE_STATE_NOT);
                break;
        }

        return state;
    }

    public void setAjaeState(AjaePower ajaeState) {
        this.ajaeState = ajaeState;
        refreshDrawableState();
    }
}
