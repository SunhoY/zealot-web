package io.harry.zealot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.zealot.R;
import io.harry.zealot.range.AjaeScoreRange;
import io.harry.zealot.state.AjaePower;
import io.harry.zealot.view.AjaeIndicateImageView;
import io.harry.zealot.view.AjaeIndicateTextView;

public class ResultActivity extends ZealotBaseActivity {
    private static final String AJAE_SCORE = "ajaeScore";

    private String ajaeScoreText;

    @BindView(R.id.ajae_score)
    AjaeIndicateTextView ajaeScore;
    @BindView(R.id.result_image)
    AjaeIndicateImageView resultImage;
    @BindView(R.id.result_message)
    TextView resultMessage;

    @Inject
    AjaeScoreRange ajaeScoreRange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zealotComponent.inject(this);

        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        int score = getIntent().getIntExtra(AJAE_SCORE, 100);

        ajaeScoreText = String.valueOf(score);
        ajaeScore.setText(getResources().getString(R.string.score, score));

        ajaeScore.setAjaeState(getAjaeStateByScore(score));
        resultImage.setAjaeState(getAjaeStateByScore(score));

        resultMessage.setText(getResultMessageByScore(score));
    }

    private AjaePower getAjaeStateByScore(int score) {
        return ajaeScoreRange.getRange(score);
    }

    private int getResultMessageByScore(int score) {
        AjaePower range = ajaeScoreRange.getRange(score);

        switch (range) {
            case FULL:
                return R.string.real_ajae_message;
            case MEDIUM:
                return R.string.medium_ajae_message;
            case NO:
                return R.string.not_ajae_message;
        }

        return -1;
    }

    @OnClick(R.id.test_again)
    public void onTestAgainClick() {
        finish();
    }

    @OnClick(R.id.share_sns)
    public void onShareSNSClick() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://placeholder.com?score=" + ajaeScoreText);

        Intent chooser = Intent.createChooser(intent, getString(R.string.share_ajae_power));

        startActivity(chooser);
    }
}
