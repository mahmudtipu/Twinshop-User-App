package store.twinshop.virtualmarket;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    private EditText editTextMobile;

    int RC_SIGN_IN=1;

    TextView pricavyPolicy;

    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    TextView privacyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pricavyPolicy = findViewById(R.id.privacyPolicyId);
        String text = "Review our Privacy Policy and Terms & Conditions for this app";

        SpannableString hl = new SpannableString(text);

        final ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "http://sites.google.com/view/twinshop-privacy-policy";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(Color.parseColor("#FF9800"));
                ds.setUnderlineText(false);
            }
        };

        final ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                String url = "http://sites.google.com/view/twinshop-terms-and-conditions";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);

                ds.setColor(Color.parseColor("#FF9800"));
                ds.setUnderlineText(false);
            }
        };

        hl.setSpan(clickableSpan1, 11,25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        hl.setSpan(clickableSpan2,30,49,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        pricavyPolicy.setText(hl);
        pricavyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        /*if(Build.VERSION.SDK_INT>=21)
        {
            Window window;
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }



        progressBar = findViewById(R.id.progressBarId);

        editTextMobile = findViewById(R.id.editTextMobile);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 11){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(Login.this, VerifyPhoneActivity.class);
                intent.putExtra("mobile", mobile);
                overridePendingTransition(R.anim.activity_transition, R.anim.activity_transition2);
                startActivity(intent);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }
}