package flashcard.organizer;

import flashcard.model.Card;
import flashcard.session.RoundSummary;
import flashcard.session.StudyProgress;
import java.util.ArrayList;
import java.util.List;

/**
 * Moves cards answered incorrectly in the previous round to the front while preserving internal order.
 */
public final class RecentMistakesFirstSorter implements CardOrganizer {

    @Override
    public List<Card> organize(final List<Card> cards, final StudyProgress progress) {
        final RoundSummary previousRound = progress.previousRound();
        if (previousRound.isEmpty()) {
            return new ArrayList<>(cards);
        }

        final List<Card> mistakesFirst = new ArrayList<>(cards.size());
        final List<Card> correctLater = new ArrayList<>(cards.size());

        for (Card card : cards) {
            if (previousRound.wasIncorrect(card)) {
                mistakesFirst.add(card);
            } else {
                correctLater.add(card);
            }
        }

        mistakesFirst.addAll(correctLater);
        return mistakesFirst;
    }
}
