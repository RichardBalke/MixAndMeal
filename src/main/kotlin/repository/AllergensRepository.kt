package repository

import api.models.Allergens

interface AllergensRepository {
    suspend fun create(allergens: Allergens)
}

object FakeAllergensRepository {

    public val allergens: List<Allergens> =
        listOf(
            Allergens(1, "GLUTEN", "Glutenbevattende granen", "Bevat gluten uit tarwe, rogge, gerst, haver, spelt of kamut"),
            Allergens(2, "SHELLFISH", "Schaaldieren", "Kan allergische reacties veroorzaken bij consumptie van garnalen, krab of kreeft"),
            Allergens(3, "EGGS", "Eieren", "Bevat ei-eiwitten die allergieën kunnen veroorzaken"),
            Allergens(4, "FISH", "Vis", "Bevat visproteïnen die allergisch kunnen reageren"),
            Allergens(5, "PEANUTS", "Pinda’s", "Kan ernstige allergische reacties veroorzaken"),
            Allergens(6, "SOY", "Soja", "Bevat soja-eiwitten die allergieën kunnen opwekken"),
            Allergens(7, "MILK", "Melk (inclusief lactose)", "Bevat melkeiwitten en lactose, niet geschikt bij koemelkallergie of lactose-intolerantie"),
            Allergens(8, "NUTS", "Noten", "Bevat noten zoals amandel, hazelnoot, walnoot, cashew, pecannoot, paranoot, pistache of macadamia"),
            Allergens(9, "CELERY", "Selderij", "Kan allergische reacties veroorzaken, ook in kleine hoeveelheden"),
            Allergens(10, "MUSTARD", "Mosterd", "Bevat mosterdzaad, een veelvoorkomende voedselallergie"),
            Allergens(11, "SESAME", "Sesamzaad", "Kan ernstige allergische reacties veroorzaken"),
            Allergens(12, "SULPHITES", "Zwaveldioxide en sulfieten", "Kan reacties geven bij concentraties boven 10 mg/kg of 10 mg/l"),
            Allergens(13, "LUPIN", "Lupine", "Bevat lupine-eiwitten, vaak gebruikt in glutenvrije producten"),
            Allergens(14, "MOLLUSCS", "Weekdieren", "Bevat mosselen, oesters, inktvis of andere weekdieren die allergieën kunnen veroorzaken")
        )

}