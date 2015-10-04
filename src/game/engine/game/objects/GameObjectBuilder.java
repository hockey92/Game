package game.engine.game.objects;

public class GameObjectBuilder {
    private GameObject gameObject = null;

    public void createNewGameObject() {
        gameObject = new GameObject();
    }

    public void add() {

    }

    public GameObject getResult() {
        return gameObject;
    }
}
