package tech.blur.redline.core;

import android.app.Dialog;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.jetbrains.annotations.NotNull;
import tech.blur.redline.moxy.MvpAndroidxDialogFragment;

public class MvpBottomSheetDialogFragment extends MvpAndroidxDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), getTheme());
    }
}
