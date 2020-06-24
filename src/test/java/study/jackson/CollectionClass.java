package study.jackson;

import java.util.List;
import java.util.Queue;
import java.util.Set;

public class CollectionClass {
    private List list;
    private Set set;
    private Queue queue;

    public CollectionClass() {
    }

    public CollectionClass(List list, Set set, Queue queue) {
        this.list = list;
        this.set = set;
        this.queue = queue;
    }

    public List getList() {
        return list;
    }

    public Set getSet() {
        return set;
    }

    public Queue getQueue() {
        return queue;
    }
}
