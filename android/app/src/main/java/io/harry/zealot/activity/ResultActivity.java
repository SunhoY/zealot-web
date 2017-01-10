package io.harry.zealot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.zealot.R;

public class ResultActivity extends ZealotBaseActivity {
    private static final String AJAE_SCORE = "ajaeScore";

    private String ajaeScoreText;

    @BindView(R.id.ajae_score)
    TextView ajaeScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        ajaeScoreText = String.valueOf(getIntent().getIntExtra(AJAE_SCORE, 100));
        ajaeScore.setText(ajaeScoreText);
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
