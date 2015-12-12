package game.engine.geometry.collision;

public enum CollisionType {

    EDGE_TO_POINT(0),
    EDGE_TO_EDGE(1);

    private int index;

    CollisionType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
