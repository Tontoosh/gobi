package flashcard.organizer;

import flashcard.model.Card;
import flashcard.session.StudyProgress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Randomizes the order of cards for each round.
 */
public final class RandomCardOrganizer implements CardOrganizer {

    private final Random random;

    /**
     * Creates an organizer with a default random source.
     */
    public RandomCardOrganizer() {
        this(new Random());
    }

    RandomCardOrganizer(final Random random) {
        this.random = random;
    }

    @Override
    public List<Card> organize(final List<Card> cards, final StudyProgress progress) {
        final List<Card> shuffled = new ArrayList<>(cards);
        Collections.shuffle(shuffled, random);
        return shuffled;
    }
}
