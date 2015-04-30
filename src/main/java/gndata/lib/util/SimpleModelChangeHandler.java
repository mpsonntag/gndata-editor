package gndata.lib.util;

import java.util.List;

import com.hp.hpl.jena.rdf.model.*;

/**
 * Simple change handler for models.
 */
public interface SimpleModelChangeHandler extends ModelChangedListener {

    @Override
    public default void addedStatement(Statement statement) {
        Model m = ModelFactory.createDefaultModel();
        m.add(statement);

        logAddChange(m);
    }

    @Override
    public default void addedStatements(Statement[] statements) {
        Model m = ModelFactory.createDefaultModel();
        m.add(statements);

        logAddChange(m);
    }

    @Override
    public default void addedStatements(List<Statement> list) {
        Model m = ModelFactory.createDefaultModel();
        m.add(list);

        logAddChange(m);
    }

    @Override
    public default void addedStatements(StmtIterator stmtIterator) {
        Model m = ModelFactory.createDefaultModel();
        m.add(stmtIterator);

        logAddChange(m);
    }

    @Override
    public default void addedStatements(Model model) {
        Model m = ModelFactory.createDefaultModel();
        m.add(model);

        logAddChange(m);
    }

    @Override
    public default void removedStatement(Statement statement) {
        Model m = ModelFactory.createDefaultModel();
        m.add(statement);

        logRemoveChange(m);
    }

    @Override
    public default void removedStatements(Statement[] statements) {
        Model m = ModelFactory.createDefaultModel();
        m.add(statements);

        logRemoveChange(m);
    }

    @Override
    public default void removedStatements(List<Statement> list) {
        Model m = ModelFactory.createDefaultModel();
        m.add(list);

        logRemoveChange(m);
    }

    @Override
    public default void removedStatements(StmtIterator stmtIterator) {
        Model m = ModelFactory.createDefaultModel();
        m.add(stmtIterator);

        logRemoveChange(m);
    }

    @Override
    public default void removedStatements(Model model) {
        Model m = ModelFactory.createDefaultModel();
        m.add(model);

        logRemoveChange(m);
    }

    @Override
    public default void notifyEvent(Model model, Object o) { }

    public void logAddChange(Model m);

    public void logRemoveChange(Model m);

}
