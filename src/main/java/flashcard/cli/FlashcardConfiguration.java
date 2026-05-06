package flashcard.cli;

import flashcard.organizer.OrderType;

/**
 * Immutable command line configuration.
 *
 * @param helpRequested whether help should be printed
 * @param cardsFile path to the cards file
 * @param orderType selected card order
 * @param repetitions required number of correct answers per card
 * @param invertCards whether question and answer should be swapped
 */
public record FlashcardConfiguration(
        boolean helpRequested,
        String cardsFile,
        OrderType orderType,
        int repetitions,
        boolean invertCards) {
}
