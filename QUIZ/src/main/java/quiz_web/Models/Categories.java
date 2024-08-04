package quiz_web.Models;

import java.io.Serializable;

public enum Categories implements Serializable {
    ENTERTAINMENT(0),
    GEOGRAPHY(1),
    HISTORY(2),
    LANGUAGE(3),
    LITERATURE(4),
    MIXED(5),
    MOVIES(6),
    MUSIC(7),
    OTHER(8),
    SCIENCE(9),
    SPORTS(10);

    private final int value;

    Categories(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Categories getByValue(int value) {
        for (Categories category : values()) {
            if (category.value == value) {
                return category;
            }
        }
        return null;
    }

    public static Categories getByName(String name) {
        System.out.println(name);
        if (name != null) {
            for (Categories category : Categories.values()) {
                if (name.equalsIgnoreCase(category.name())) {
                    System.out.println(category.name());
                    return category;
                }
            }
        }
        return null; // or throw an IllegalArgumentException if preferred
    }

}