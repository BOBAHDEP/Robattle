import java.util.ArrayList;
import java.util.List;

/**
 * Created by Вова on 09.10.2015.
 */
public class PlayMap {

    static final int MAX_FIELD_SIZE = 9;

    private List<Entity> entities = new ArrayList<Entity>();

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void attack(int x, int y, int dLifeLevel) {
        for (Entity entity : entities) {
            if (entity.x == x && entity.y == y) {
                entity.suffer(dLifeLevel);
            }
        }
    }

    public void consolePrintMap() {
        System.out.println("-----------------");
        boolean isEntityInCell = false;
        for (int i = 0; i < MAX_FIELD_SIZE; i++) {
            for (int j = 0; j < MAX_FIELD_SIZE; j++) {
                for (Entity entity : entities) {
                    if (entity.x == i && entity.y == j) {
                        System.out.print(entity.lifeLevel + " ");
                        isEntityInCell = true;
                    }
                }
                if (!isEntityInCell) {
                    System.out.print(". ");
                }
                isEntityInCell = false;
            }
            System.out.println("");
        }
    }

    public int[][] getCoordinatesOfEntities() {
        int res[][] = new int[entities.size()][2];
        for (int i = 0; i < entities.size(); i++) {
            res[i][0] = entities.get(i).x;
            res[i][1] = entities.get(i).y;
        }
        return res;
    }

    public boolean isTaken(int x, int y) {
        for (Entity entity : entities) {
            if (entity.x == x && entity.y == y) {
                return true;
            }
        }
        return false;
    }
}
