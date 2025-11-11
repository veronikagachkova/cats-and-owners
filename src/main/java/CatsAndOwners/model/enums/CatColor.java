package CatsAndOwners.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CatColor {
    BLACK("Черный"),
    WHITE("Белый"),
    GRAY("Серый"),
    GINGER("Рыжий"),
    BROWN("Коричневый"),
    CALICO("Черепаховый"),
    TABBY("Полосатый"),
    BICOLOR("Двухцветный"),
    TRICOLOR("Трехцветный"),
    OTHER("Другой");

    private final String displayName;

    CatColor(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}