package flashcard.organizer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import flashcard.model.Card;
import flashcard.session.CardStats;
import flashcard.session.RoundSummary;
import flashcard.session.StudyProgress;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RecentMistakesFirstSorterTest {

    @Test
    void movesRecentlyIncorrectCardsToTheFrontWithoutChangingInternalOrder() {
        final Card first = new Card(1, "A", "1");
        final Card second = new Card(2, "B", "2");
        final Card third = new Card(3, "C", "3");
        final Card fourth = new Card(4, "D", "4");

        final List<Card> previousOrder = List.of(first, fourth, third, second);
        final RoundSummary previousRound = new RoundSummary(1, previousOrder, Set.of(4, 2), 1_000_000_000L);

        final Map<Card, CardStats> stats = new LinkedHashMap<>();
        for (Card card : previousOrder) {
            stats.put(card, new CardStats());
        }

        final RecentMistakesFirstSorter sorter = new RecentMistakesFirstSorter();
        final List<Card> sorted = sorter.organize(previousOrder, new StudyProgress(stats, previousRound));

        assertEquals(List.of(fourth, second, first, third), sorted);
    }
}
