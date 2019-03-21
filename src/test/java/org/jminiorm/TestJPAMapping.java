package org.jminiorm;

import org.jminiorm.attributeconverter.EnumNameAttributeConverter;
import org.jminiorm.attributeconverter.EnumOrdinalAttributeConverter;
import org.jminiorm.mapping.ColumnMapping;
import org.jminiorm.mapping.JPAORMapping;
import org.jminiorm.mapping.ORMapping;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestJPAMapping {

    @Test
    public void testJPAMapping() {
        ORMapping mapping = new JPAORMapping(User.class);

        assertEquals(mapping.getJavaClass(), User.class);
        assertEquals(mapping.getTable(), "users");
        assertEquals(mapping.getSchema(), "public");
        assertEquals(mapping.getIndexes().size(), 1);
        org.jminiorm.mapping.Index i = mapping.getIndexes().get(0);
        assertEquals("index_name", i.getName());
        assertEquals("list", i.getColumns());

        List<ColumnMapping> cols = mapping.getColumnMappings();
        assertEquals(5, cols.size());

        ColumnMapping idColumnMapping = mapping.getIdColumnMapping();
        assertTrue(idColumnMapping.isId());
        assertTrue(idColumnMapping.isGenerated());

        ColumnMapping loginColumnMapping = mapping.getColumnMappingByProperty("login");
        assertNotNull(loginColumnMapping);
        assertEquals("login", loginColumnMapping.getPropertyDescriptor().getName());
        assertEquals("login_column", loginColumnMapping.getColumn());
        assertEquals("definition", loginColumnMapping.getColumnDefinition());
        assertFalse(loginColumnMapping.isInsertable());
        assertFalse(loginColumnMapping.isUpdatable());
        assertEquals(new Integer(123), loginColumnMapping.getLength());
        assertEquals(new Integer(1), loginColumnMapping.getPrecision());
        assertEquals(new Integer(2), loginColumnMapping.getScale());
        assertFalse(loginColumnMapping.isNullable());

        ColumnMapping sometextColumnMapping = mapping.getColumnMappingByColumn("sometext");
        assertNotNull(sometextColumnMapping);
        assertEquals("sometext", sometextColumnMapping.getPropertyDescriptor().getName());
        assertEquals("sometext", sometextColumnMapping.getColumn());
        assertNull(sometextColumnMapping.getColumnDefinition());
        assertTrue(sometextColumnMapping.isInsertable());
        assertTrue(sometextColumnMapping.isUpdatable());
        assertEquals(new Integer(255), sometextColumnMapping.getLength());
        assertNull(sometextColumnMapping.getPrecision());
        assertNull(sometextColumnMapping.getScale());
        assertTrue(sometextColumnMapping.isNullable());

        ColumnMapping enumNameColumnMapping = mapping.getColumnMappingByColumn("name");
        assertEquals(EnumNameAttributeConverter.class, enumNameColumnMapping.getConverter().getClass());

        ColumnMapping enumOrdinalColumnMapping = mapping.getColumnMappingByColumn("ordinal");
        assertEquals(EnumOrdinalAttributeConverter.class, enumOrdinalColumnMapping.getConverter().getClass());
    }

    /**
     * A class used to test JPA mappings.
     */
    @Table(name = "users", schema="public", indexes = {
            @Index(name = "index_name", columnList = "list", unique = true)
    })
    class User {

        @Id
        @GeneratedValue
        private Integer id;
        @Column(name = "login_column", columnDefinition = "definition", insertable = false, length = 123, nullable =
                false, precision = 1, scale = 2, updatable = false)
        private String login;
        private String sometext;
        @Enumerated(EnumType.STRING)
        private EnumerationTest name;
        @Enumerated(EnumType.ORDINAL)
        private EnumerationTest ordinal;

        public User() {
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getSometext() {
            return sometext;
        }

        public void setSometext(String sometext) {
            this.sometext = sometext;
        }

        public EnumerationTest getOrdinal() {
            return ordinal;
        }

        public void setOrdinal(EnumerationTest ordinal) {
            this.ordinal = ordinal;
        }

        public EnumerationTest getName() {
            return name;
        }

        public void setName(EnumerationTest name) {
            this.name = name;
        }
    }
}


