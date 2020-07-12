package core.nickbernate.session;

import core.nickbernate.manager.EntityManager;

import java.io.Closeable;

public interface Session extends EntityManager, Closeable {
}
