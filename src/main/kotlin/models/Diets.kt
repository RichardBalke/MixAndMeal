package api.models

import kotlinx.serialization.Serializable

@Serializable
enum class Diets(val id: Int, val displayName: String, val description: String) {
    VEGAN(1, "Veganistisch", "Bevat geen dierlijke producten zoals vlees, vis, zuivel, eieren of honing"),
    VEGETARIAN(2, "Vegetarisch", "Bevat geen vlees of vis, maar kan wel zuivel en eieren bevatten"),
    GLUTEN_FREE(3, "Glutenvrij", "Bevat geen glutenbevattende granen zoals tarwe, gerst, rogge of spelt"),
    LACTOSE_FREE(4, "Lactosevrij", "Bevat geen lactose, geschikt voor mensen met lactose-intolerantie"),
    NUT_FREE(5, "Notenvrij", "Bevat geen noten of sporen van noten"),
    DAIRY_FREE(6, "Zuivelvrij", "Bevat geen melk of melkproducten"),
    LOW_SUGAR(7, "Suikerarm", "Bevat weinig of geen toegevoegde suikers"),
    LOW_SALT(8, "Zoutarm", "Bevat weinig natrium, geschikt voor een zoutbeperkt dieet"),
    HALAL(9, "Halal", "Voldoet aan islamitische voedingsvoorschriften"),
    KOSHER(10, "Kosher", "Voldoet aan joodse voedingsvoorschriften"),
    PALEO(11, "Paleo", "Gebaseerd op voeding uit het paleolithicum: geen granen, zuivel of bewerkte producten"),
    KETO(12, "Keto", "Bevat weinig koolhydraten en veel vetten, gericht op ketose"),
    RAW_FOOD(13, "Raw food", "Bevat uitsluitend rauwe of minimaal verhitte voeding"),
    FLEXITARIAN(14, "Flexitarisch", "Overwegend vegetarisch met af en toe vlees of vis")
}