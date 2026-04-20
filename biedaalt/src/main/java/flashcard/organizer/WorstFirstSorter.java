package flashcard.organizer;

import flashcard.model.Card;
import flashcard.session.CardStats;
import flashcard.session.StudyProgress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Orders cards by descending number of mistakes.
 */
public final class WorstFirstSorter implements CardOrganizer {

    @Override
    public List<Card> organize(final List<Card> cards, final StudyProgress progress) {
        final Map<Card, CardStats> stats = progress.cardStats();
        final List<Card> ordered = new ArrayList<>(cards);
        ordered.sort(Comparator.comparingInt((Card card) -> stats.get(card).wrongAnswers()).reversed());
        return ordered;
    }
}
