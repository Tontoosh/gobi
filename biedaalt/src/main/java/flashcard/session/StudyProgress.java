package flashcard.session;

import flashcard.model.Card;
import java.util.Map;

/**
 * Read-only view of session progress passed to card organizers.
 *
 * @param cardStats statistics for each card
 * @param previousRound summary of the previous round
 */
public record StudyProgress(Map<Card, CardStats> cardStats, RoundSummary previousRound) {
}
