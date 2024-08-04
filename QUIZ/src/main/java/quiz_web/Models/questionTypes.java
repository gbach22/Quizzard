package quiz_web.Models;

import java.io.Serializable;

public enum questionTypes implements Serializable {
    QUESTION_RESPONSE(0),
    FILL_IN_THE_BLANK(1),
    MULTIPLE_CHOICE(2),
    PICTURE_RESPONSE(3),
    MULTI_ANSWER(4),
    MULTI_CHOICE_MULTI_ANS(5),
    MATCHING(6);
    private final int value;

    questionTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static questionTypes getByValue(int value) {
        for (questionTypes type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

}
