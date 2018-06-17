package io.polybius.testtask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {

    if(args.length < 2) {
      throw new IllegalArgumentException("Please support two parameters!");
    }

    String query = args[0];
    String dataString = args[1];

    QueryValidator.validateQuery(query);

    List<JsonObject> jsonObjectsList = new ArrayList<>();
    new JsonParser().parse(dataString).getAsJsonArray().forEach(jo->jsonObjectsList.add(jo.getAsJsonObject()));
    PredicateService predicateService = new PredicateService();

    System.out.println(new Gson().toJson(filterObjects(jsonObjectsList, predicateService.getPredicate(query))));
  }

  public static List<JsonObject> filterObjects(List<JsonObject> jsonObjectsList, Predicate<JsonObject> predicate) {
    return jsonObjectsList.stream().filter(predicate).collect(Collectors.toList());
  }



}
