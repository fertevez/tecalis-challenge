package fecasado.citius.usc.tecalischallenge;

import android.content.Context;
import android.support.v4.content.ContextCompat;

public class Square extends android.support.v7.widget.AppCompatButton {

    public Square(Context context, int id, String text, int color, int width) {
        super(context);
        this.setText(text);
        this.setTextSize(4);
        this.setId(id);
        this.setWidth(width);
        this.setHeight(width);
        this.setMinimumWidth(0);
        this.setMinimumHeight(0);
        this.setBackground(ContextCompat.getDrawable(context, color));
    }
}
