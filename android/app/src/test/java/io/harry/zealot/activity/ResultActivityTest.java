package io.harry.zealot.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.assertj.android.api.content.IntentAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.zealot.BuildConfig;
import io.harry.zealot.R;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ResultActivityTest {

    private static final int AJAE_SCORE = 95;
    private ResultActivity subject;

    @BindView(R.id.ajae_score)
    TextView ajaeScore;

    @BindView(R.id.test_again)
    Button testAgain;

    @BindView(R.id.share_sns)
    Button share;

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("ajaeScore", AJAE_SCORE);

        subject = Robolectric.buildActivity(ResultActivity.class).withIntent(intent).create().get();
        ButterKnife.bind(this, subject);
    }

    @Test
    public void onCreate_setsAjaeScore() throws Exception {
        assertThat(ajaeScore.getText()).isEqualTo("95");
    }

    @Test
    public void clickOnTestAgain_finishesActivity() throws Exception {
        testAgain.performClick();

        assertThat(subject.isFinishing()).isTrue();
    }

    @Test
    public void clickOnShare_sendShareIntentWithLink() throws Exception {
        share.performClick();

        Intent chooser = shadowOf(subject).getNextStartedActivity();

        IntentAssert chooserIntentAssert = new IntentAssert(chooser);

        assertThat(chooserIntentAssert.hasAction(Intent.ACTION_CHOOSER));
        assertThat(chooserIntentAssert.hasExtra(Intent.EXTRA_TITLE, "아재력 알리기"));

        Intent originalIntent = chooser.getParcelableExtra(Intent.EXTRA_INTENT);

        IntentAssert originalIntentAssert = new IntentAssert(originalIntent);

        assertThat(originalIntentAssert.hasAction(Intent.ACTION_SEND));
        assertThat(originalIntentAssert.hasExtra(Intent.EXTRA_TEXT, "http://placeholder.com?score=95"));
        assertThat(originalIntentAssert.hasType("text/plain"));
    }
}