import java.util.ArrayList;
import java.util.SortedSet;

class Account extends Game {
    Information information;
    ArrayList<Character> characters;
    private int gamesPlayed;

    private Account(Builder builder) {
        this.information = builder.information;
        this.characters = builder.characters;
        this.gamesPlayed = builder.gamesPlayed;
    }

    public static class Builder {
        private Information information;
        private ArrayList<Character> characters = new ArrayList<>();
        private int gamesPlayed;

        public Builder setInformation(Information information) {
            this.information = information;
            return this;
        }

        public Builder setCharacters(ArrayList<Character> characters) {
            this.characters = characters;
            return this;
        }

        public Builder setGamesPlayed(int gamesPlayed) {
            this.gamesPlayed = gamesPlayed;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }

    static class Information {
        Credentials credentials;
        private SortedSet<String> favoriteGames;
        String name;
        private String country;

        private Information(Builder builder) {
            this.credentials = builder.credentials;
            this.favoriteGames = builder.favoriteGames;
            this.name = builder.name;
            this.country = builder.country;
        }

        public static class Builder {
            private Credentials credentials;
            private SortedSet<String> favoriteGames;
            private String name;
            private String country;

            public Builder setCredentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public Builder setFavoriteGames(SortedSet<String> favoriteGames) {
                this.favoriteGames = favoriteGames;
                return this;
            }

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setCountry(String country) {
                this.country = country;
                return this;
            }

            public Information build() {
                return new Information(this);
            }
        }
    }

    static class Credentials {
        String email;
        String password;

        private Credentials(Builder builder) {
            this.email = builder.email;
            this.password = builder.password;
        }

        public static class Builder {
            private String email;
            private String password;

            public Builder setEmail(String email) {
                this.email = email;
                return this;
            }

            public Builder setPassword(String password) {
                this.password = password;
                return this;
            }

            public Credentials build() {
                return new Credentials(this);
            }
        }
    }
}