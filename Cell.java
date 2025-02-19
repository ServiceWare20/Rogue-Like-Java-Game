class Cell {
    private final int x;
    private final int y;
    private int visit; // 0 - unvisited, 1 - visited
    int entity;
    Character being;
    Enemy enemy;

    public Cell(int x, int y, int entity) {
        this.x = x;
        this.y = y;
        this.entity = entity;
        this.visit = 0;
    }
}