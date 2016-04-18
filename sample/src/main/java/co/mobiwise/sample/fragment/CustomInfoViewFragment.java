package co.mobiwise.sample.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import co.mobiwise.materialintro.InfoViewConfiguration;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;
import co.mobiwise.sample.R;

/**
 * Created by yamlee on 4/2/16.
 * demonstration for how to use MaterialIntroView in layout file
 */
public class CustomInfoViewFragment extends Fragment {
    private MaterialIntroView mMaterialIntroView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_use, null);
        Button button = (Button) view.findViewById(R.id.btn_text);
        MaterialIntroView materialIntroView = (MaterialIntroView) inflater.inflate(R.layout.layout_custom_material_intro_view, null);
        MaterialIntroView.Builder builder = new MaterialIntroView.Builder(materialIntroView);
        View customInfoView = inflater.inflate(R.layout.layout_custom_info, null);
        InfoViewConfiguration infoViewConfiguration = new InfoViewConfiguration();
        infoViewConfiguration.setInfoView(customInfoView);
        View ivArrow = customInfoView.findViewById(R.id.iv_arrow);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivArrow, View.TRANSLATION_Y, 0, 20, 0);
        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        infoViewConfiguration.setAnimator(objectAnimator);
        infoViewConfiguration.setAlignCenter(true);
        mMaterialIntroView = builder
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Hi There! Click this card and see what happens.")
                .setInfoViewConfiguration(infoViewConfiguration)
                .setTarget(button)
                .setUsageId("intro_card") //THIS SHOULD BE UNIQUE ID
                .build();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMaterialIntroView.show();
    }
}
