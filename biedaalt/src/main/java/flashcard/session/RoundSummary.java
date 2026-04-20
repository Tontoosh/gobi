package flashcard.session;

import flashcard.model.Card;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Result of one study round.
 */
public final class RoundSummary {

    private static final RoundSummary EMPTY = new RoundSummary(0, List.of(), Set.of(), 0L);

    private final int roundNumber;
    private final List<Card> askedCards;
    private final Set<Integer> incorrectCardIds;
    private final long elapsedNanos;

    /**
     * Creates a round summary.
     *
     * @param roundNumber round number starting from 1
     * @param askedCards cards asked in the round
     * @param incorrectCardIds ids of cards answered incorrectly
     * @param elapsedNanos total round duration
     */
    public RoundSummary(final int roundNumber,
                        final List<Card> askedCards,
                        final Set<Integer> incorrectCardIds,
                        final long elapsedNanos) {
        this.roundNumber = roundNumber;
        this.askedCards = List.copyOf(askedCards);
        this.incorrectCardIds = Collections.unmodifiableSet(new HashSet<>(incorrectCardIds));
        this.elapsedNanos = elapsedNanos;
    }

    /**
     * Returns an empty summary for the first round.
     *
     * @return empty summary
     */
    public static RoundSummary empty() {
        return EMPTY;
    }

    public int roundNumber() {
        return roundNumber;
    }

    public List<Card> askedCards() {
        return askedCards;
    }

    public boolean isEmpty() {
        return askedCards.isEmpty();
    }

    public boolean wasIncorrect(final Card card) {
        return incorrectCardIds.contains(card.id());
    }

    public int correctCount() {
        return askedCards.size() - incorrectCardIds.size();
    }

    public int incorrectCount() {
        return incorrectCardIds.size();
    }

    public boolean allCorrect() {
        return !askedCards.isEmpty() && incorrectCardIds.isEmpty();
    }

    public double averageSecondsPerCard() {
        if (askedCards.isEmpty()) {
            return 0.0;
        }
        return (elapsedNanos / 1_000_000_000.0) / askedCards.size();
    }
}
