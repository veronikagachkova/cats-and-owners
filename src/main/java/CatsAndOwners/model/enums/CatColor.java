package CatsAndOwners.model.enums;

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

    public String getDisplayName() {
        return displayName;
    }
}