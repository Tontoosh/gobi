package flashcard.achievement;

import flashcard.session.CardStats;
import flashcard.session.RoundSummary;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Evaluates achievements from session history.
 */
public final class AchievementEvaluator {

    /**
     * Evaluates all unlocked achievements.
     *
     * @param rounds completed rounds
     * @param cardStats statistics for all cards
     * @return unlocked achievements
     */
    public Set<Achievement> evaluate(final List<RoundSummary> rounds, final Collection<CardStats> cardStats) {
        final EnumSet<Achievement> unlocked = EnumSet.noneOf(Achievement.class);

        final boolean speedUnlocked =
                rounds.stream().anyMatch(round -> !round.isEmpty() && round.averageSecondsPerCard() < 5.0);
        if (speedUnlocked) {
            unlocked.add(Achievement.SPEED);
        }

        if (!rounds.isEmpty() && rounds.get(rounds.size() - 1).allCorrect()) {
            unlocked.add(Achievement.CORRECT);
        }

        final boolean repeatUnlocked = cardStats.stream().anyMatch(stats -> stats.attempts() > 5);
        if (repeatUnlocked) {
            unlocked.add(Achievement.REPEAT);
        }

        final boolean confidentUnlocked = cardStats.stream().anyMatch(stats -> stats.correctAnswers() >= 3);
        if (confidentUnlocked) {
            unlocked.add(Achievement.CONFIDENT);
        }

        return unlocked;
    }
}
