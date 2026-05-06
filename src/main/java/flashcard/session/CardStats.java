package flashcard.session;

/**
 * Mutable per-card statistics collected during a study session.
 */
public final class CardStats {

    private int attempts;
    private int correctAnswers;
    private int wrongAnswers;

    /**
     * Records a user answer.
     *
     * @param correct whether the answer was correct
     */
    public void recordAttempt(final boolean correct) {
        attempts++;
        if (correct) {
            correctAnswers++;
        } else {
            wrongAnswers++;
        }
    }

    public int attempts() {
        return attempts;
    }

    public int correctAnswers() {
        return correctAnswers;
    }

    public int wrongAnswers() {
        return wrongAnswers;
    }

    /**
     * Returns whether the card has reached the configured mastery target.
     *
     * @param repetitions required correct answers
     * @return {@code true} when mastered
     */
    public boolean isMastered(final int repetitions) {
        return correctAnswers >= repetitions;
    }
}
