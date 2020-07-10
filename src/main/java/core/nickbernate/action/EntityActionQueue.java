package core.nickbernate.action;

import core.nickbernate.session.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityActionQueue {

    private Session session;
    private List<EntityAction> actions = new ArrayList<>();

    public EntityActionQueue(Session session) {
        this.session = session;
    }

    public void add(EntityAction entityAction) {
        this.actions.add(entityAction);
    }

    public void addAll(List<EntityAction> entityActions) {
        this.actions.addAll(entityActions);
    }

    public List<String> getAllQueries() {
        return actions.stream()
                .map(EntityAction::getQuery)
                .collect(Collectors.toList());
    }

}
