package CatsAndOwners.model.enums;

public enum CatBreed {
    SIAMESE("Сиамская"),
    BRITISH("Британская"),
    MAINE_COON("Мейн-кун"),
    PERSIAN("Персидская"),
    SPHYNX("Сфинкс"),
    RAGDOLL("Рэгдолл"),
    SCOTTISH_FOLD("Шотландская вислоухая"),
    SIBERIAN("Сибирская"),
    BIRMAN("Бирманская"),
    ORIENTAL("Ориентал"),
    MIXED("Дворовая"),
    OTHER("Другая");

    private final String displayName;

    CatBreed(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}