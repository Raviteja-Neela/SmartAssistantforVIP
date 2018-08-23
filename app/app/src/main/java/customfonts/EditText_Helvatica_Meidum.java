package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditText_Helvatica_Meidum extends android.support.v7.widget.AppCompatEditText {

    public EditText_Helvatica_Meidum(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditText_Helvatica_Meidum(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditText_Helvatica_Meidum(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
            setTypeface(tf);
        }
    }

}