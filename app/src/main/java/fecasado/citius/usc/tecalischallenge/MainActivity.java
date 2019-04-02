package fecasado.citius.usc.tecalischallenge;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.AlphabeticIndex;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.DynamicLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private final static short DIM = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createLayoutDynamically(DIM);
    }

    private void createLayoutDynamically(int n) {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int squareWidth = dm.widthPixels/(DIM);
        LinearLayout parentLayout = (LinearLayout)findViewById(R.id.my_linear_layout);

        for (int i = 0; i < n; i++) {

            // Create a new linear layout
            LinearLayout ll = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            parentLayout.setOrientation(VERTICAL);
            parentLayout.addView(ll, params);

            for (int j = 0; j < n; j++) {
                int btnId = j + DIM*(i);
                int color;
                if ((btnId & 1) == 0 ) {
                    color =  R.color.darkSquare;
                } else {
                    color =   R.color.lightSquare;
                }
                Square square = new Square(this, btnId, ""+btnId, color, squareWidth);
                final int id_ = square.getId();


                ll.addView(square);

                square.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.super.getApplicationContext(),
                                "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }
    }








}
