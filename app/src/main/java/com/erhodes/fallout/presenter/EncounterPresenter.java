package com.erhodes.fallout.presenter;

import android.graphics.Color;
import android.util.Log;

import com.erhodes.fallout.CharacterRepository;
import com.erhodes.fallout.model.Action;
import com.erhodes.fallout.model.GameObject;
import com.erhodes.fallout.model.TargetGroup;
import com.erhodes.fallout.model.Character;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Eric on 10/04/2016.
 */
public class EncounterPresenter implements EncounterContract.UserActionListener {
    EncounterContract.View mView;
    Action mSelectedAction;
    ArrayList<TargetGroup> mTargetGroups;
    TargetGroup mActiveTargetGroup;
    Character mCharacter;

    ArrayList<Integer> mColors;
    int mTargetGroupId;

    public EncounterPresenter(EncounterContract.View view, CharacterRepository service) {
        mView = view;
        mCharacter = service.getActiveCharacter().getValue();

        mColors = new ArrayList<>();
        mColors.add(Color.RED);
        mColors.add(Color.BLUE);
    }

    @Override
    public void onActionSelected(Action action) {
        if (mSelectedAction != null) {
            for (TargetGroup targetGroup : mSelectedAction.getDynamicTargetGroups()) {
                targetGroup.resetTargets();
            }
        }
        mSelectedAction = action;
        mTargetGroups = mSelectedAction.getDynamicTargetGroups();
        Log.d("Eric","action has " + mTargetGroups.size() + " dynamic groups");

        if (mTargetGroups.size() > 0) {
            setActiveTargetGroup(0);
        }
        List<String> result = new ArrayList<>();
        for (TargetGroup targetGroup : mTargetGroups) {
            result.add(targetGroup.getName());
        }

        mView.setTargetButtons(result);
        mView.update();
    }

    @Override
    public void setActiveTargetGroup(int group) {
        mActiveTargetGroup = mTargetGroups.get(group);
        mTargetGroupId = group;
        mView.setActiveTargetGroup(group);
    }

    @Override
    public void toggleTarget(GameObject gameObject) {
        if (mActiveTargetGroup == null)
            return;
        if (mActiveTargetGroup.contains(gameObject)) {
            mActiveTargetGroup.removeTarget(gameObject);
        } else {
            mActiveTargetGroup.addTarget(gameObject);
        }
        mView.update();
    }

    @Override
    public void performAction() {
        mSelectedAction.performAction(mCharacter);
        mView.update();
    }

    @Override
    public void endTurn() {
        mCharacter.newTurn();
        mView.update();
    }

    /**
     * Is this character currently part of the active target group?
     * @param character
     * @return
     */
    @Override
    public boolean isCharacterTargetted(Character character) {
        if (mActiveTargetGroup == null)
            return false;
        return mActiveTargetGroup.mTargets.contains(character);
    }

    @Override
    public int getActiveTargetGroupColor() {
        return mColors.get(mTargetGroupId);
    }

    @Override
    public boolean sufficientApRemaining() {
        if (mSelectedAction == null) {
            return false;
        }
        return mCharacter.actionPoints >= mSelectedAction.cost;
    }

    @Override
    public TargetGroup getTargetGroup(int position) {
        return mTargetGroups.get(position);
    }

    @Override
    public int getTargetGroupCount() {
        if (mTargetGroups == null)
            return 0;
        return mTargetGroups.size();
    }
}
