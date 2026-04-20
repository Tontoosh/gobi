package flashcard.model;

import java.util.Objects;

/**
 * Immutable flashcard.
 */
public final class Card {

    private final int id;
    private final String question;
    private final String answer;

    /**
     * Creates a card.
     *
     * @param id card identifier
     * @param question prompt side
     * @param answer expected answer side
     */
    public Card(final int id, final String question, final String answer) {
        this.id = id;
        this.question = requireText(question, "question");
        this.answer = requireText(answer, "answer");
    }

    public int id() {
        return id;
    }

    public String question() {
        return question;
    }

    public String answer() {
        return answer;
    }

    /**
     * Creates a new card with its question and answer swapped.
     *
     * @return inverted card
     */
    public Card inverted() {
        return new Card(id, answer, question);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Card card)) {
            return false;
        }
        return id == card.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return question;
    }

    private static String requireText(final String value, final String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Card " + fieldName + " must not be blank.");
        }
        return value;
    }
}
