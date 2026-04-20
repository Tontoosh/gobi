package flashcard.achievement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import flashcard.model.Card;
import flashcard.session.CardStats;
import flashcard.session.RoundSummary;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AchievementEvaluatorTest {

    @Test
    void unlocksExpectedAchievements() {
        final Card first = new Card(1, "A", "1");
        final Card second = new Card(2, "B", "2");

        final CardStats firstStats = new CardStats();
        firstStats.recordAttempt(true);
        firstStats.recordAttempt(true);
        firstStats.recordAttempt(true);
        firstStats.recordAttempt(false);
        firstStats.recordAttempt(false);
        firstStats.recordAttempt(false);

        final CardStats secondStats = new CardStats();
        secondStats.recordAttempt(true);

        final RoundSummary firstRound = new RoundSummary(1, List.of(first, second), Set.of(1), 3_000_000_000L);
        final RoundSummary finalRound = new RoundSummary(2, List.of(first), Set.of(), 2_000_000_000L);

        final AchievementEvaluator evaluator = new AchievementEvaluator();
        final Set<Achievement> achievements =
                evaluator.evaluate(List.of(firstRound, finalRound), List.of(firstStats, secondStats));

        assertEquals(Set.of(
                Achievement.SPEED,
                Achievement.CORRECT,
                Achievement.REPEAT,
                Achievement.CONFIDENT), achievements);
    }
}
