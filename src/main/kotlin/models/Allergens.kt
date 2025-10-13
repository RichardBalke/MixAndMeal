package api.models

import kotlinx.serialization.Serializable

@Serializable
enum class Allergens(val id: Int, val displayName: String, val description: String) {
    GLUTEN(1, "Glutenbevattende granen", "Bevat gluten uit tarwe, rogge, gerst, haver, spelt of kamut"),
    SHELLFISH(2, "Schaaldieren", "Kan allergische reacties veroorzaken bij consumptie van garnalen, krab of kreeft"),
    EGGS(3, "Eieren", "Bevat ei-eiwitten die allergieën kunnen veroorzaken"),
    FISH(4, "Vis", "Bevat visproteïnen die allergisch kunnen reageren"),
    PEANUTS(5, "Pinda’s", "Kan ernstige allergische reacties veroorzaken"),
    SOY(6, "Soja", "Bevat soja-eiwitten die allergieën kunnen opwekken"),
    MILK(7, "Melk (inclusief lactose)", "Bevat melkeiwitten en lactose, niet geschikt bij koemelkallergie of lactose-intolerantie"),
    NUTS(8, "Noten", "Bevat noten zoals amandel, hazelnoot, walnoot, cashew, pecannoot, paranoot, pistache of macadamia"),
    CELERY(9, "Selderij", "Kan allergische reacties veroorzaken, ook in kleine hoeveelheden"),
    MUSTARD(10, "Mosterd", "Bevat mosterdzaad, een veelvoorkomende voedselallergie"),
    SESAME(11, "Sesamzaad", "Kan ernstige allergische reacties veroorzaken"),
    SULPHITES(12, "Zwaveldioxide en sulfieten", "Kan reacties geven bij concentraties boven 10 mg/kg of 10 mg/l"),
    LUPIN(13, "Lupine", "Bevat lupine-eiwitten, vaak gebruikt in glutenvrije producten"),
    MOLLUSCS(14, "Weekdieren", "Bevat mosselen, oesters, inktvis of andere weekdieren die allergieën kunnen veroorzaken")
}

