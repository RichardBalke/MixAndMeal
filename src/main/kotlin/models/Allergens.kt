package api.models

enum class Allergens(val id: Int, val displayName: String, val description: String) {
    MILK(1, "Melk", "Lactose intolerantie"),
    EGGS(2, "Eieren", "Ei Allergie"),
    FISH(3, "Vis", "Vis Allergie"),
    CRUSTACEANSHELLFISH(4, "Kreeften, Garnalen", "Schaaldier Allergie"),
    TREENUTS(5, "Noten", "Pinda Allergie"),
    SOYBEANS(7, "Soya", "Soya Allergie"),
    SESAME(8, "Sesam", "Sesam Allergie"),
    LUPIN(9, "Lupin", "Lupine Allergie"),
    MOLLUSKS(10, "Mosselen, Oesters", "Weekdier Allergie"),
    GLUTEN(11, "Gluten", "Gluten Allergie"),
    MUSTARD(12, "Mosterd", "Mosterd Allergie"),
}

