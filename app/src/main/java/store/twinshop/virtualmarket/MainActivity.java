package store.twinshop.virtualmarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ImageView splashUp, splashDown, splashMiddle, twinshop;
    TextView twinshopText;
    private int splashTimeOut=4500;

    FirebaseAuth mAuth;

    FirebaseUser user;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twinshop = findViewById(R.id.twinshopId);
        twinshopText = findViewById(R.id.twinshopTextId);

        splashUp = findViewById(R.id.splashUpId);
        splashDown = findViewById(R.id.splashDownId);
        splashMiddle = findViewById(R.id.splashMiddleId);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /*Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_left);

                Animation anim2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_right);
                Animation anim3 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_left);

                anim.reset();
                splashUp.clearAnimation();
                splashUp.startAnimation(anim);

                splashUp.setVisibility(View.INVISIBLE);

                anim.reset();
                splashDown.clearAnimation();
                splashDown.startAnimation(anim2);

                splashDown.setVisibility(View.INVISIBLE);

                anim.reset();
                splashMiddle.clearAnimation();
                splashMiddle.startAnimation(anim3);

                splashMiddle.setVisibility(View.INVISIBLE);

                Animation myanim = AnimationUtils.loadAnimation(MainActivity.this,R.anim.splashanimation2);
                icon.startAnimation(myanim);

                icon.setVisibility(View.INVISIBLE);*/

                mAuth = FirebaseAuth.getInstance();

                if(mAuth.getCurrentUser()==null)
                {
                    finish();
                    startActivity(new Intent(MainActivity.this,Login.class));
                }
                else if(mAuth.getCurrentUser()!=null)
                {
                    finish();
                    startActivity(new Intent(MainActivity.this,HomePage.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        },splashTimeOut);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splashanimation);
        twinshop.startAnimation(myanim);
        twinshopText.startAnimation(myanim);

        splashUp.startAnimation(myanim);
        splashDown.startAnimation(myanim);
        splashMiddle.startAnimation(myanim);

        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_left);
        splashUp.startAnimation(anim);
        splashMiddle.startAnimation(anim);

        Animation anim2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_right);
        splashDown.startAnimation(anim2);

        twinshop.startAnimation(myanim);
        twinshopText.startAnimation(myanim);
    }
}
