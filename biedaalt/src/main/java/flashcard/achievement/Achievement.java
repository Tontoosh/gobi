package flashcard.achievement;

/**
 * Achievements that can be unlocked during a study session.
 */
public enum Achievement {
    SPEED("SPEED", "Average answer time in a round dropped below 5 seconds per card."),
    CORRECT("CORRECT", "Every card in the final round was answered correctly."),
    REPEAT("REPEAT", "At least one card was answered more than 5 times."),
    CONFIDENT("CONFIDENT", "At least one card was answered correctly 3 or more times.");

    private final String title;
    private final String description;

    Achievement(final String title, final String description) {
        this.title = title;
        this.description = description;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }
}
