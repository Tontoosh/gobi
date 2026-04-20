package flashcard.organizer;

import flashcard.model.Card;
import flashcard.session.StudyProgress;
import java.util.List;

/**
 * Strategy interface for ordering cards before each round.
 */
public interface CardOrganizer {

    /**
     * Creates a round order for the currently pending cards.
     *
     * @param cards cards that still need to be answered
     * @param progress study progress accumulated so far
     * @return ordered cards for the next round
     */
    List<Card> organize(List<Card> cards, StudyProgress progress);
}
