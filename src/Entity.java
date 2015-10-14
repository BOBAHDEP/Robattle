/**
 * Created by Вова on 08.10.2015.
 */
public class Entity {

    public Entity(int x, int y, int lifeLevel) {
        this.x = x;
        this.y = y;
        this.lifeLevel = lifeLevel;
    }

    private int condition;
    public   int x, y;
    public int lifeLevel;

    public void move(int dx, int dy, PlayMap map) {
        int coordinatesOfEntities[][] = map.getCoordinatesOfEntities();
        /*System.out.println(coordinatesOfEntities[0][0]);
        System.out.println(coordinatesOfEntities[0][1]);
        System.out.println(coordinatesOfEntities[1][0]);
        System.out.println(coordinatesOfEntities[1][1]); */
        for (int i = 0; i < coordinatesOfEntities.length; i++) {
            if ((y == coordinatesOfEntities[i][1] && (x < coordinatesOfEntities[i][0]) && (x + dx >= coordinatesOfEntities[i][0]))) {
                x = coordinatesOfEntities[i][0] - 1;
                return;
            }
            if ((y == coordinatesOfEntities[i][1] && (x > coordinatesOfEntities[i][0]) && (x + dx <= coordinatesOfEntities[i][0]))) {
                x = coordinatesOfEntities[i][0] + 1;
                return;
            }
            if ((x == coordinatesOfEntities[i][0] && (y < coordinatesOfEntities[i][1]) && (y + dy >= coordinatesOfEntities[i][1]))) {
                y = coordinatesOfEntities[i][1] - 1;
                return;
            }
            if ((x == coordinatesOfEntities[i][0] && (y > coordinatesOfEntities[i][1]) && (y + dy <= coordinatesOfEntities[i][1]))) {
                y = coordinatesOfEntities[i][1] + 1;
                return;
            }
        }
        x += dx;
        y += dy;
        if (x >= PlayMap.MAX_FIELD_SIZE) {
            x = PlayMap.MAX_FIELD_SIZE - 1;
        }
        if (y >= PlayMap.MAX_FIELD_SIZE) {
            y = PlayMap.MAX_FIELD_SIZE - 1;
        }
        if (x <= 0) {
            x = 0;
        }
        if (y <= 0) {
            y = 0;
        }
    }

    public void addX(int dx) {
        this.x += dx;
    }

    public void addY(int dy) {
        this.y += dy;
    }

    public void suffer(int dLifeLevel) {
        this.lifeLevel -= dLifeLevel;
    }

    public  void die() {
        this.condition = -1;
    }

    public  void nextStep(int dx, int dy, int dLifeLevel) {
        x += dx;
        y += dy;
        if (x >= PlayMap.MAX_FIELD_SIZE) {
            x = PlayMap.MAX_FIELD_SIZE - 1;
        }
        if (y >= PlayMap.MAX_FIELD_SIZE) {
            y = PlayMap.MAX_FIELD_SIZE - 1;
        }
        if (x <= 0) {
            x = 0;
        }
        if (y <= 0) {
            y = 0;
        }
        lifeLevel -= dLifeLevel;
    }
    public void say(String speech) {
        System.out.println(speech);
    }
}
