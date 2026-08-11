package bank;

import com.google.gson.*;

import java.lang.reflect.Type;

public class TransactionAdapter implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    private static final String CLASSNAME = "CLASSNAME";                                // JSON-Property für die Klassentypangabe
    private static final String INSTANCE = "INSTANCE";                                 // JSON-Property für die Transaktionsdaten

    /**
     * Serialisiert ein Transaction-Objekt in JSON.
     *
     * @param src       das zu serialisierende Transaction-Objekt
     * @param typeOfSrc der Typ des Objekts
     * @param context   der JSON-Serialisierungskontext
     * @return ein JsonElement, das das Transaction-Objekt repräsentiert
     */
    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        // Erstelle ein JSON-Objekt
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASSNAME, src.getClass().getSimpleName()); // Füge den Klassennamen hinzu
        JsonElement instance = context.serialize(src); // Die eigentliche Instanz serialisieren
        jsonObject.add(INSTANCE, instance);
        return jsonObject; // Gebe das JSON-Objekt zurück

    }

    /**
     * Deserialisiert ein JSON-Element in ein Transaction-Objekt.
     *
     * @param json    das JSON-Element
     * @param typeOfT der Typ des zu deserialisierenden Objekts
     * @param context der JSON-Deserialisierungskontext
     * @return das deserialisierte Transaction-Objekt
     * @throws JsonParseException falls die Deserialisierung fehlschlägt
     */
    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String className = jsonObject.get(CLASSNAME).getAsString();                     // Klassenname aus JSON lesen
        JsonElement instance = jsonObject.get(INSTANCE);                                // Objektdaten aus JSON lesen

        try {
            Class<?> clazz = Class.forName("bank." + className);              // Dynamisches Laden der Klasse
            return context.deserialize(instance, clazz);                                // Objektdaten in die Klasse deserialisieren
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown class: " + className, e);
        }
    }
}
