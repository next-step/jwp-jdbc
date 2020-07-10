package core.nickbernate.action;

import core.nickbernate.session.Session;

import java.util.ArrayList;
import java.util.List;

public class EntityActionQueue {

    private Session session;
    private List<EntityAction> actions = new ArrayList<>();

    public EntityActionQueue(Session session) {
        this.session = session;
    }

    public void add(EntityAction entityAction) {
        this.actions.add(entityAction);
    }
}
