package fecasado.citius.usc.tecalischallenge;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

/**
 * This class represents a square on the chess board
 */
public class Square extends android.support.v7.widget.AppCompatButton {

    private Customer customer;

    public Square(Context context, String text, int color, int width) {
        super(context);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setAllCaps(false);
        this.setPadding(1,0,1,0);
        this.setWidth(width);
        this.setHeight(width);
        this.setMinimumWidth(0);
        this.setMinimumHeight(0);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 7f);
        this.setText("\n" + text + "\n");
        this.setBackground(ContextCompat.getDrawable(context, color));
        this.setPressed(false);
    }

    public void setCustomer(Customer c) {
        this.customer = c;
        this.setText(c.toString());
    }

    public Customer getCustomer() {
        return customer;
    }
}
