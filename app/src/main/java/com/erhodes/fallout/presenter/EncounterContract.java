package com.erhodes.fallout.presenter;

import com.erhodes.fallout.model.Action;
import com.erhodes.fallout.model.Character;
import com.erhodes.fallout.model.GameObject;
import com.erhodes.fallout.model.TargetGroup;

import java.util.List;

/**
 * Created by Eric on 10/04/2016.
 */
public class EncounterContract {
    public interface UserActionListener {
        void onActionSelected(Action action);
        void setActiveTargetGroup(int group);
        void toggleTarget(GameObject gameObject);
        void performAction();
        void endTurn();
        boolean isCharacterTargetted(Character character);
        int getActiveTargetGroupColor();
        boolean sufficientApRemaining();
        TargetGroup getTargetGroup(int position);
        int getTargetGroupCount();
    }
    public interface View {
        void update();
        void setTargetButtons(List<String> targetGroups);
        void setActiveTargetGroup(int group);
    }
}
