
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class JsonInput {
    public static ArrayList<Account> deserializeAccounts() {
        String accountPath = "D:\\UPB\\Anul2\\POO\\Tema1\\src\\accounts.json";
        try {
            String content = new String((Files.readAllBytes(Paths.get(accountPath))));
            JSONObject obj = new JSONObject(content);
            JSONArray accountsArray = (JSONArray) obj.get("accounts");

            ArrayList<Account> accounts = new ArrayList<>();
            for (int i=0; i < accountsArray.length(); i++) {
                JSONObject accountJson = (JSONObject) accountsArray.get(i);
                // name, country, games_number
                String name = (String) accountJson.get("name");
                String country = (String) accountJson.get("country");
                int gamesNumber = Integer.parseInt((String)accountJson.get("maps_completed"));

                // Credentials
                Account.Credentials credentials = null;
                try {
                    JSONObject credentialsJson = (JSONObject) accountJson.get("credentials");
                    String email = (String) credentialsJson.get("email");
                    String password = (String) credentialsJson.get("password");

                    credentials = new Account.Credentials.Builder()
                            .setEmail(email)
                            .setPassword(password)
                            .build();
                } catch (JSONException e) {
                    System.out.println("! This account doesn't have all credentials !");
                }

                // Favorite games
                SortedSet<String> favoriteGames = new TreeSet();
                try {
                    JSONArray games = (JSONArray) accountJson.get("favorite_games");
                    for (int j = 0; j < games.length(); j++) {
                        favoriteGames.add((String) games.get(j));
                    }
                } catch (JSONException e) {
                    System.out.println("! This account doesn't have favorite games !");
                }

                // Characters
                ArrayList<Character> characters = new ArrayList<>();
                try {
                    JSONArray charactersListJson = (JSONArray) accountJson.get("characters");
                    for (int j = 0; j < charactersListJson.length(); j++) {
                        JSONObject charJson = (JSONObject) charactersListJson.get(j);
                        String cname = (String) charJson.get("name");
                        String profession = (String) charJson.get("profession");
                        String level = (String) charJson.get("level");
                        int lvl = Integer.parseInt(level);
                        Integer experience = (Integer) charJson.get("experience");

                        Character newCharacter = null;
                        if (profession.equals("Warrior"))
                            newCharacter = Character.Factory.characterFactory("Warrior", cname, experience, lvl);
                        if (profession.equals("Rogue"))
                            newCharacter = Character.Factory.characterFactory("Rogue", cname, experience, lvl);
                        if (profession.equals("Mage"))
                            newCharacter = Character.Factory.characterFactory("Mage", cname, experience, lvl);
                        characters.add(newCharacter);
                    }
                } catch (JSONException e) {
                    System.out.println("! This account doesn't have characters !");
                }

                Account.Information information = new Account.Information.Builder()
                                                        .setName(name)
                                                        .setCountry(country)
                                                        .setCredentials(credentials)
                                                        .setFavoriteGames(favoriteGames)
                                                        .build();
                Account account = new Account.Builder()
                                            .setCharacters(characters)
                                            .setGamesPlayed(gamesNumber)
                                            .setInformation(information)
                                            .build();
                accounts.add(account);
            }
            return accounts;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}