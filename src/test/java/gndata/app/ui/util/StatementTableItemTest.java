package gndata.app.ui.util;

import com.hp.hpl.jena.datatypes.*;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.*;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic Test class for the StatementTableItem class
 */
public class StatementTableItemTest {

    private final String resourceURI = "TestURI";
    private final String subjURI = "TestSubjURI";
    private final String propertyNamespace = "Test#";
    private final String propertyLocalName = "testProperty";
    private final String literalValue = "plainLiteralValue";
    private final String typedLiteralValue = "12";
    private final RDFDatatype doubleType = TypeMapper.getInstance().getTypeByName(XSDDatatype.XSDdouble.getURI());

    private Statement plainLitStmt;

    private StatementTableItem plainLitSTI;
    private StatementTableItem typedLitSTI;
    private StatementTableItem objPropSTI;

    @Before
    public void setUp() throws Exception {
        // create plain literal StatementTableItem
        Resource r = ResourceFactory.createResource(resourceURI);
        Property p = ResourceFactory.createProperty(propertyNamespace, propertyLocalName);
        Literal l = ResourceFactory.createPlainLiteral(literalValue);
        plainLitStmt = ResourceFactory.createStatement(r, p, l);
        plainLitSTI = new StatementTableItem(plainLitStmt);

        // create typed literal StatementTableItem
        l = ResourceFactory.createTypedLiteral(typedLiteralValue, doubleType);
        typedLitSTI = new StatementTableItem(ResourceFactory.createStatement(r, p, l));

        // create objectProperty StatementTableItem
        Resource subj = ResourceFactory.createResource(subjURI);
        objPropSTI = new StatementTableItem(ResourceFactory.createStatement(r, p, subj));
    }

    @Test
    public void testGetStatement() throws Exception {
        assertThat(plainLitSTI.getStatement()).isEqualToComparingFieldByField(plainLitStmt);
    }

    @Test
    public void testGetPredicate() throws Exception {
        assertThat(plainLitSTI.getPredicate()).isEqualTo(propertyLocalName);
    }

    @Test
    public void testGetLiteral() throws Exception {
        // test DataProperty
        assertThat(plainLitSTI.getLiteral()).isEqualTo(literalValue);
        // test ObjectProperty
        assertThat(objPropSTI.getLiteral()).isEqualTo("");
    }

    @Test
    public void testWithLiteral() throws Exception {
        assertThat(plainLitSTI.withLiteral("newLiteralValue")).isNotEqualTo(plainLitSTI);

        assertThat(plainLitSTI.withLiteral("failingLiteralValue", doubleType)).isNotEqualTo(plainLitSTI);
        assertThat(plainLitSTI.withLiteral(typedLiteralValue, doubleType)).isNotEqualTo(plainLitSTI);
    }

    @Test
    public void testGetType() throws Exception {
        assertThat(plainLitSTI.getType()).isEqualTo("String");
        assertThat(typedLitSTI.getType()).isEqualTo(doubleType.getJavaClass().getSimpleName());
        assertThat(objPropSTI.getType()).isEqualTo("");
    }

}
