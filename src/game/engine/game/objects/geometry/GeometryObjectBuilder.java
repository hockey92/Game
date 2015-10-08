package game.engine.game.objects.geometry;

import game.engine.game.objects.geometry.GeometryObject;

public class GeometryObjectBuilder {
    private GeometryObject geometryObject = null;

    public void createNewGameObject() {
        geometryObject = new GeometryObject();
    }

    public void add() {

    }

    public GeometryObject getResult() {
        return geometryObject;
    }
}
