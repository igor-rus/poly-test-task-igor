package io.polybius.testtask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class MainTest {

    private PredicateService predicateService;
    private List<JsonObject> originalList;

    @Before
    public void setUp() {
        predicateService = new PredicateService();
        originalList = getJsonObjectList(getTestJsonAsString());
    }

    @Test
    public void queryStringConainsIntervalExpression() {
        String query = "age>20 && age<30";
        Predicate<JsonObject> predicate = predicateService.getPredicate(query);
        List<JsonObject> filtered = Main.filterObjects(originalList, predicate);
        String expected = "[\n" +
                "   {\n" +
                "    \"index\": 2,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 29,\n" +
                "    \"eyeColor\": \"blue\",\n" +
                "    \"name\": \"Alice Woodward\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"LYRICHORD\",\n" +
                "    \"email\": \"alicewoodward@lyrichord.com\"\n" +
                "  }, \n" +
                "  {\n" +
                "    \"index\": 4,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 26,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Shawn Mckenzie\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"ACRODANCE\",\n" +
                "    \"email\": \"shawnmckenzie@acrodance.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 7,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 21,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Perry Espinoza\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"company\": \"HINWAY\",\n" +
                "    \"email\": \"perryespinoza@hinway.com\"\n" +
                "  }\n" +
                "]";

        List<JsonObject> expectedList = getJsonObjectList(expected);
        assertEquals(3, filtered.size());
        assertEquals(expectedList, filtered);
    }


    @Test
    public void filterWithLongBooleanExpression() {

        String query = "gender=female && company=CENTREXIN && age=46 && eyeColor=green || name=Cherie || gender=female && company=PLASMOX || age=21";
        Predicate<JsonObject> predicate = predicateService.getPredicate(query);
        List<JsonObject> filtered = Main.filterObjects(originalList, predicate);

        String expected = "[\n" +
                "  {\n" +
                "    \"index\": 3,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 46,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Bertie Paul\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"CENTREXIN\",\n" +
                "    \"email\": \"bertiepaul@centrexin.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 7,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 21,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Perry Espinoza\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"company\": \"HINWAY\",\n" +
                "    \"email\": \"perryespinoza@hinway.com\"\n" +
                "  }\n" +
                "]";

        List<JsonObject> expectedList = getJsonObjectList(expected);

        assertEquals(2, filtered.size());
        assertEquals(expectedList, filtered);
    }

    @Test
    public void filterWithSinglePredicate() {

        String query = "age>=51";
        Predicate<JsonObject> predicate = predicateService.getPredicate(query);
        List<JsonObject> filtered = Main.filterObjects(originalList, predicate);

        String expected = "[\n" +
                "  {\n" +
                "    \"index\": 1,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 55,\n" +
                "    \"eyeColor\": \"gray\",\n" +
                "    \"name\": \"Amanda Woods\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"UNDERTAP\",\n" +
                "    \"email\": \"amandawoods@undertap.com\"\n" +
                "  },  {\n" +
                "    \"index\": 9,\n" +
                "    \"isActive\": false,\n" +
                "    \"age\": 51,\n" +
                "    \"eyeColor\": \"gray\",\n" +
                "    \"name\": \"Sherrie Mathews\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"ATOMICA\",\n" +
                "    \"email\": \"sherriemathews@atomica.com\"\n" +
                "  }\n" +
                "]";

        List<JsonObject> expectedList = getJsonObjectList(expected);

        assertEquals(2, filtered.size());
        assertEquals(expectedList, filtered);
    }


    @Test
    public void moreThanOneObjectWithSameFieldMatches() {
        String query = "name=Alice Woodward";
        Predicate<JsonObject> predicate = predicateService.getPredicate(query);
        List<JsonObject> filtered = Main.filterObjects(originalList, predicate);

        String expected = "[\n" +
                "   {\n" +
                "    \"index\": 2,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 29,\n" +
                "    \"eyeColor\": \"blue\",\n" +
                "    \"name\": \"Alice Woodward\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"LYRICHORD\",\n" +
                "    \"email\": \"alicewoodward@lyrichord.com\"\n" +
                "  }, \n" +
                "  {\n" +
                "    \"index\": 6,\n" +
                "    \"isActive\": false,\n" +
                "    \"age\": 48,\n" +
                "    \"eyeColor\": \"gray\",\n" +
                "    \"name\": \"Alice Woodward\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"MARQET\",\n" +
                "    \"email\": \"rosemaryduran@marqet.com\"\n" +
                "  }\n" +
                "]";

        List<JsonObject> expectedList = getJsonObjectList(expected);

        assertEquals(2, filtered.size());
        assertEquals(expectedList, filtered);

    }

    @Test
    public void nothingMatchesQuery() {
        String query = "age=100";
        Predicate<JsonObject> predicate = predicateService.getPredicate(query);
        List<JsonObject> filtered = Main.filterObjects(originalList, predicate);
        assertTrue(filtered.isEmpty());
    }

    private List<JsonObject> getJsonObjectList(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(jsonString).getAsJsonArray();
        List<JsonObject> jsonObjectsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObjectsList.add(jsonArray.get(i).getAsJsonObject());
        }
        return jsonObjectsList;
    }

    private String getTestJsonAsString() {

        return "[\n" +
                "  {\n" +
                "    \"index\": 0,\n" +
                "    \"isActive\": false,\n" +
                "    \"age\": 32,\n" +
                "    \"eyeColor\": \"brown\",\n" +
                "    \"name\": \"Bradshaw Sherman\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"company\": \"ZILODYNE\",\n" +
                "    \"email\": \"bradshawsherman@zilodyne.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 1,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 55,\n" +
                "    \"eyeColor\": \"gray\",\n" +
                "    \"name\": \"Amanda Woods\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"UNDERTAP\",\n" +
                "    \"email\": \"amandawoods@undertap.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 2,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 29,\n" +
                "    \"eyeColor\": \"blue\",\n" +
                "    \"name\": \"Alice Woodward\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"LYRICHORD\",\n" +
                "    \"email\": \"alicewoodward@lyrichord.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 3,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 46,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Bertie Paul\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"CENTREXIN\",\n" +
                "    \"email\": \"bertiepaul@centrexin.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 4,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 26,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Shawn Mckenzie\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"ACRODANCE\",\n" +
                "    \"email\": \"shawnmckenzie@acrodance.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 5,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 37,\n" +
                "    \"eyeColor\": \"gray\",\n" +
                "    \"name\": \"Leblanc Dillon\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"company\": \"PORTICO\",\n" +
                "    \"email\": \"leblancdillon@portico.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 6,\n" +
                "    \"isActive\": false,\n" +
                "    \"age\": 48,\n" +
                "    \"eyeColor\": \"gray\",\n" +
                "    \"name\": \"Alice Woodward\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"MARQET\",\n" +
                "    \"email\": \"rosemaryduran@marqet.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 7,\n" +
                "    \"isActive\": true,\n" +
                "    \"age\": 21,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Perry Espinoza\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"company\": \"HINWAY\",\n" +
                "    \"email\": \"perryespinoza@hinway.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 8,\n" +
                "    \"isActive\": false,\n" +
                "    \"age\": 46,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Phelps Shields\",\n" +
                "    \"gender\": \"male\",\n" +
                "    \"company\": \"CONJURICA\",\n" +
                "    \"email\": \"phelpsshields@conjurica.com\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"index\": 9,\n" +
                "    \"isActive\": false,\n" +
                "    \"age\": 51,\n" +
                "    \"eyeColor\": \"gray\",\n" +
                "    \"name\": \"Sherrie Mathews\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"ATOMICA\",\n" +
                "    \"email\": \"sherriemathews@atomica.com\"\n" +
                "  }\n" +
                "]";

    }


}