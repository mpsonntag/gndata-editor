package gndata.lib.util;

import java.util.List;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Simple change handler for models.
 */
public interface SimpleModelChangeHandler extends ModelChangedListener {

    @Override
    public default void addedStatement(Statement statement) { changedHappened(); }

    @Override
    public default void addedStatements(Statement[] statements) { changedHappened(); }

    @Override
    public default void addedStatements(List<Statement> list) { changedHappened(); }

    @Override
    public default void addedStatements(StmtIterator stmtIterator) { changedHappened(); }

    @Override
    public default void addedStatements(Model model) { changedHappened(); }

    @Override
    public default void removedStatement(Statement statement) { changedHappened(); }

    @Override
    public default void removedStatements(Statement[] statements) { changedHappened(); }

    @Override
    public default void removedStatements(List<Statement> list) { changedHappened(); }

    @Override
    public default void removedStatements(StmtIterator stmtIterator) { changedHappened(); }

    @Override
    public default void removedStatements(Model model) { changedHappened(); }

    @Override
    public default void notifyEvent(Model model, Object o) { }

    public void changedHappened();

}
