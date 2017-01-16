package io.harry.zealot.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import org.assertj.android.api.content.IntentAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.zealot.BuildConfig;
import io.harry.zealot.R;
import io.harry.zealot.TestZealotApplication;
import io.harry.zealot.module.AjaeScoreModule;
import io.harry.zealot.range.AjaeScoreRange;
import io.harry.zealot.state.AjaePower;
import io.harry.zealot.view.AjaeIndicateImageView;
import io.harry.zealot.view.AjaeIndicateTextView;

import static io.harry.zealot.state.AjaePower.FULL;
import static io.harry.zealot.state.AjaePower.MEDIUM;
import static io.harry.zealot.state.AjaePower.NO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ResultActivityTest {
    private static final int SCORE_NO_MATTER = 80;
    private static final AjaePower POWER_NO_MATTER = AjaePower.FULL;
    private ResultActivity subject;

    @BindView(R.id.ajae_score)
    AjaeIndicateTextView ajaeScore;
    @BindView(R.id.test_again)
    Button testAgain;
    @BindView(R.id.result_message)
    TextView resultMessage;
    @BindView(R.id.share_sns)
    Button share;
    @BindView(R.id.result_image)
    AjaeIndicateImageView resultImage;

    @Inject
    AjaeScoreRange mockAjaeScoreRange;

    private Field ajaeStateOfTextView;
    private Field ajaeStateOfImageView;
    private AjaePower ajaeStateValueOfTextView;
    private AjaePower ajaeStateValueOfImageView;


    @Before
    public void setUp() throws Exception {
        ajaeStateOfTextView = AjaeIndicateTextView.class.getDeclaredField("ajaeState");
        ajaeStateOfTextView.setAccessible(true);

        ajaeStateOfImageView = AjaeIndicateImageView.class.getDeclaredField("ajaeState");
        ajaeStateOfImageView.setAccessible(true);
    }

    public void setUp(int score, AjaePower ajaePower) throws Exception {
        Intent intent = new Intent();
        intent.putExtra("ajaeScore", score);

        ((TestZealotApplication)application).getZealotComponent().inject(this);

        when(mockAjaeScoreRange.getRange(anyInt())).thenReturn(ajaePower);

        subject = Robolectric.buildActivity(ResultActivity.class).withIntent(intent).create().get();
        ButterKnife.bind(this, subject);

        ajaeStateValueOfTextView = (AjaePower) ajaeStateOfTextView.get(this.ajaeScore);
        ajaeStateValueOfImageView = (AjaePower) ajaeStateOfImageView.get(resultImage);
    }

    @After
    public void tearDown() throws Exception {
        ajaeStateOfTextView.setAccessible(false);
        ajaeStateOfImageView.setAccessible(false);
    }

    @Test
    public void onCreate_setsAjaeScore() throws Exception {
        setUp(95, POWER_NO_MATTER);

        assertThat(ajaeScore.getText()).isEqualTo("95 점");
    }

    @Test
    public void onCreate_sets_fullAjae_image_textColor_andMessage_whenScoreIsOver90() throws Exception {
        setUp(SCORE_NO_MATTER, AjaePower.FULL);

        assertThat(resultMessage.getText()).isEqualTo("빼박캔트\n진성아재");
    }

    @Test
    public void onCreate_sets_mediumAjae_image_textColor_andMessage_whenScoreIsOver70andUnder90() throws Exception {
        setUp(SCORE_NO_MATTER, AjaePower.MEDIUM);
        assertThat(resultMessage.getText()).isEqualTo("아직까진\n젊은오빠");
    }

    @Test
    public void onCreate_notAjae_image_textColor_andMessage_whenScoreIsUnder70() throws Exception {
        setUp(SCORE_NO_MATTER, AjaePower.NO);

        assertThat(resultMessage.getText()).isEqualTo("자라나는\n어린새싹");
    }

    @Test
    public void onCreate_changesStateOfScoreAndAjaeImageAsNotAjae_whenScoreIsUnder70() throws Exception {
        setUp(SCORE_NO_MATTER, AjaePower.NO);

        assertThat(ajaeStateValueOfTextView).isEqualTo(NO);
        assertThat(ajaeStateValueOfImageView).isEqualTo(NO);
    }

    @Test
    public void onCreate_changesStateOfScoreAndAjaeImageAsMediumAjae_whenScoreIsBetween70And90() throws Exception {
        setUp(SCORE_NO_MATTER, AjaePower.MEDIUM);

        assertThat(ajaeStateValueOfTextView).isEqualTo(MEDIUM);
        assertThat(ajaeStateValueOfImageView).isEqualTo(MEDIUM);
    }

    @Test
    public void onCreate_changesStateOfScoreAndAjaeImageAsFullAjae_whenScoreIsOver90() throws Exception {
        setUp(SCORE_NO_MATTER, AjaePower.FULL);

        assertThat(ajaeStateValueOfTextView).isEqualTo(FULL);
        assertThat(ajaeStateValueOfImageView).isEqualTo(FULL);
    }

    @Test
    public void clickOnTestAgain_finishesActivity() throws Exception {
        setUp(SCORE_NO_MATTER, POWER_NO_MATTER);

        testAgain.performClick();

        assertThat(subject.isFinishing()).isTrue();
    }

    @Test
    public void clickOnShare_sendShareIntentWithLink() throws Exception {
        setUp(80, POWER_NO_MATTER);

        share.performClick();

        Intent chooser = shadowOf(subject).getNextStartedActivity();

        IntentAssert chooserIntentAssert = new IntentAssert(chooser);

        assertThat(chooserIntentAssert.hasAction(Intent.ACTION_CHOOSER));
        assertThat(chooserIntentAssert.hasExtra(Intent.EXTRA_TITLE, "아재력 알리기"));

        Intent originalIntent = chooser.getParcelableExtra(Intent.EXTRA_INTENT);

        IntentAssert originalIntentAssert = new IntentAssert(originalIntent);

        assertThat(originalIntentAssert.hasAction(Intent.ACTION_SEND));
        assertThat(originalIntentAssert.hasExtra(Intent.EXTRA_TEXT, "http://placeholder.com?score=80"));
        assertThat(originalIntentAssert.hasType("text/plain"));
    }
}