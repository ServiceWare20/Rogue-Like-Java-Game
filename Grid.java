import java.util.Random;

class Grid extends Game{
    private int n;
    private int m;
    private Cell current;
    private int copyToKill = 0;

    public Grid() {
        randomize();
        map = new Cell[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                map[i][j] = new Cell(i, j, 0);
            }
        }
    }

    public void setKill(int kill) {
        kill = copyToKill;
    }

    public Grid(boolean hardcode) {
        if (!hardcode)
            randomize();
        else {
            n = 5;
            m = 5;
            nMapSize = n;
            mMapSize = m;
        }
        map = new Cell[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                map[i][j] = new Cell(i, j, 0);
            }
        }
    }

    public void randomize() {
        Random rdm = new Random();
        n = rdm.nextInt(4, 10);
        m = rdm.nextInt(4, 10);
        nMapSize = n;
        mMapSize = m;
    }

    public boolean placeEntity(int entity) {
        Random rdm = new Random();
        int i = rdm.nextInt(0, n);
        int j = rdm.nextInt(0, m);
        while (map[i][j].entity != 0) {
            i = rdm.nextInt(0, n);
            j = rdm.nextInt(0, m);
        }
        if (map[i][j].entity == 0) {
            map[i][j].entity = entity;
            if (entity == 5) {
                nPPos = i;
                mPPos = j;
                if(floor == 1)
                    map[i][j].being = new Mage();
                else
                    map[i][j].being = playerSave;
            } else if (entity == 4) {
                ++totalToKill;
                System.out.println(i + " " + j);
                map[i][j].enemy = new Enemy();
            }
            return true;
        }
        return false;
    }

    public boolean placeEntity(int entity, Character character, boolean hardcode) {
        Random rdm = new Random();
        int i = rdm.nextInt(0, n);
        int j = rdm.nextInt(0, m);

        if (map[i][j].entity == 0 && !hardcode) {
            map[i][j].entity = entity;
            if (entity == 5) {
                nPPos = i;
                mPPos = j;
                if(floor == 1)
                    map[i][j].being = character;
                else
                    map[i][j].being = playerSave;
            } else if (entity == 4) {
                while(map[i][j].entity != 0) {
                    i = rdm.nextInt(0, n);
                    j = rdm.nextInt(0, m);
                }
                System.out.println(i + " " + j);
                map[i][j].enemy = new Enemy();
            }
            return true;
        } else if (hardcode) {
            map[0][0].being = character;
            map[0][0].entity = 5;
            map[0][3].entity = 2;
            map[1][3].entity = 2;
            map[4][3].entity = 2;
            map[2][0].entity = 2;
            map[3][4].entity = 4;
            map[3][4].enemy = new Enemy();
            map[4][4].entity = 9;
            return true;
        }
        return false;
    }

    public void generateMap(boolean hardcode) {
        Random rdm = new Random();
        int enemiesToPlace = rdm.nextInt(4, 6);
        int sanctuariesToPlace = 3;

        while (!placeEntity(5)) ; // Player position
        while (!placeEntity(9)) ; // Portal position
        while (sanctuariesToPlace-- > 0) placeEntity(2); // Sanctuaries
        while (enemiesToPlace-- > 0) placeEntity(4); // Enemies

        showMap();
    }

    public void generateMap(boolean hardcode, Character character) {
        Random rdm = new Random();
        int enemiesToPlace = rdm.nextInt(4, 6);
        int sanctuariesToPlace = 3;

        if (!hardcode) {
            while (!placeEntity(5, character, hardcode)) ; // Player position
            while (!placeEntity(9)) ; // Portal position
            while (sanctuariesToPlace-- > 0) placeEntity(2); // Sanctuaries
            while (enemiesToPlace-- > 0) placeEntity(4); // Enemies
        } else
            placeEntity(5, character, hardcode); // Hardcoded positions
        showMap();
    }
}