package quiz_web.Models;

import java.io.Serializable;

public enum AchievementTypes implements Serializable {
    AMATEUR_AUTHOR(0),
    PROLIFIC_AUTHOR(1),
    PRODIGIOUS_AUTHOR(2),
    QUIZ_MACHINE(3),
    I_AM_THE_GREATEST(4),
    PRACTISE_MAKES_PERFECT(5);

    private final int value;
    AchievementTypes(int value) {
        this.value = value;
    }

    public static AchievementTypes getByValue(int value) {
        for (AchievementTypes type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}
