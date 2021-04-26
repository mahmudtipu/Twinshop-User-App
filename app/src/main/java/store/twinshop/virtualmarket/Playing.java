package store.twinshop.virtualmarket;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import store.twinshop.virtualmarket.Common.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Playing extends AppCompatActivity implements View.OnClickListener {

    int i;
    int correct,incorrect;

    TextView textCorrect,showCorrect;

    FirebaseAuth mAuth;
    FirebaseUser user;

    String uid;

    ImageView question_image;
    TextView question_text,description;
    Button btnA,btnB,btnC,btnD,nextQuestion;
    CardView descriptionCardId;

    int index=0,totalQuestion=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        assert user != null;
        uid = user.getUid();

        descriptionCardId = findViewById(R.id.descriptionCardId);
        showCorrect = findViewById(R.id.showCorrectId);

        question_text = findViewById(R.id.questionTextId);

        question_image = findViewById(R.id.questionImageId);

        btnA = findViewById(R.id.btnAnswerA);
        btnB = findViewById(R.id.btnAnswerB);
        btnC = findViewById(R.id.btnAnswerC);
        btnD = findViewById(R.id.btnAnswerD);
        description = findViewById(R.id.descriptionId);

        nextQuestion = findViewById(R.id.nextId);

        nextQuestion.setOnClickListener(this);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(index < totalQuestion)
        {
            Button clickButton = (Button)v;

            btnA.setBackgroundColor(Color.parseColor("#F7931E"));
            btnB.setBackgroundColor(Color.parseColor("#F7931E"));
            btnC.setBackgroundColor(Color.parseColor("#F7931E"));
            btnD.setBackgroundColor(Color.parseColor("#F7931E"));

            if(clickButton.getText().equals(Common.questionList.get(index).getCorrectAnswer()))
            {
                showCorrect.setVisibility(View.VISIBLE);
                showCorrect.setText(String.valueOf("Correct!"));

                clickButton.setBackgroundColor(Color.parseColor("#009245"));

                descriptionCardId.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                description.setText(Common.questionList.get(index).getDescription());
            }
            else
            {
                if(clickButton==btnA|clickButton==btnB|clickButton==btnC|clickButton==btnD)
                {
                    descriptionCardId.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);

                    clickButton.setBackgroundColor(Color.RED);
                    description.setText(Common.questionList.get(index).getDescription());

                    showCorrect.setText(Common.questionList.get(index).getCorrectAnswer());

                    showCorrect.setVisibility(View.VISIBLE);
                }
            }

            if(clickButton==nextQuestion)
            {
                descriptionCardId.setVisibility(View.INVISIBLE);
                description.setVisibility(View.INVISIBLE);

                showCorrect.setVisibility(View.INVISIBLE);

                showQuestion(++index);

            }

        }
    }

    private void showQuestion(int index) {

        if(index < totalQuestion)
        {
            if(Common.questionList.get(index).getIsImageQuestion().equals("true"))
            {
                Picasso.with(getBaseContext())
                        .load(Common.questionList.get(index).getQuestion())
                        .into(question_image);
                question_image.setVisibility(View.VISIBLE);
                question_text.setVisibility(View.INVISIBLE);
            }

            else
            {
                question_text.setText(Common.questionList.get(index).getQuestion());

                question_image.setVisibility(View.INVISIBLE);
                question_text.setVisibility(View.VISIBLE);
            }

            btnA.setText(Common.questionList.get(index).getAnswerA());
            btnB.setText(Common.questionList.get(index).getAnswerB());
            btnC.setText(Common.questionList.get(index).getAnswerC());
            btnD.setText(Common.questionList.get(index).getAnswerD());
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        totalQuestion = Common.questionList.size();

        showQuestion(index);
    }

    @Override
    protected void onPause() {
        super.onPause();

        btnA.setBackgroundColor(Color.parseColor("#F7931E"));
        btnB.setBackgroundColor(Color.parseColor("#F7931E"));
        btnC.setBackgroundColor(Color.parseColor("#F7931E"));
        btnD.setBackgroundColor(Color.parseColor("#F7931E"));

        i=index;
    }

    /*private void addDetail() {

        String cateName = Common.proName;
        String indexString = Common.questionList.get(index).getQuestion();
        String totalCorrectString = Common.questionList.get(index).getDescription();

        if(!TextUtils.isEmpty(cateName)&&!TextUtils.isEmpty(indexString)&&!TextUtils.isEmpty(totalCorrectString))
        {
            com.applicationull.twinfish.fooddeliveryservice.Detail detail = new com.applicationull.twinfish.fooddeliveryservice.Detail(""+cateName,""+indexString,""+totalCorrectString);

            wrongAnswerDescription.child(uid)
                    .setValue(detail);
        }
    }*/

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }


}