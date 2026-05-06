package flashcard.achievement;

/**
 * Achievements that can be unlocked during a study session.
 */
public enum Achievement {
    SPEED("ХУРД", "Тойргийн дундаж хариулт 5 секундаас бага байлаа."),
    CORRECT("ЗӨВХӨН ЗӨВ", "Сүүлийн тойрогт бүх картанд зөв хариулав."),
    REPEAT("ДАВТАГЧ", "Нэг картыг 5-аас олон удаа хариулав."),
    CONFIDENT("ИТГЭЛТЭЙ", "Нэг картыг 3 ба түүнээс олон удаа зөв хариулав.");

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
