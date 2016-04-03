package co.mobiwise.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;
import co.mobiwise.sample.R;

/**
 * Created by yamlee on 4/2/16.
 * demonstration for how to use MaterialIntroView in layout file
 */
public class LayoutUseFragment extends Fragment {
    private MaterialIntroView mMaterialIntroView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_use, null);
        Button button = (Button) view.findViewById(R.id.btn_text);
        MaterialIntroView materialIntroView = (MaterialIntroView) inflater.inflate(R.layout.layout_custom_material_intro_view, null);
        MaterialIntroView.Builder builder = new MaterialIntroView.Builder(materialIntroView);
        mMaterialIntroView = builder.enableDotAnimation(true)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText("Hi There! Click this card and see what happens.")
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
