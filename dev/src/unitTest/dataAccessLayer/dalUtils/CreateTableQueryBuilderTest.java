package dataAccessLayer.dalUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dataAccessLayer.dalUtils.CreateTableQueryBuilder.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateTableQueryBuilderTest {


    CreateTableQueryBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new CreateTableQueryBuilder("test_table");
    }

    @Test
    void addColumn_default_check() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        builder.addCheck("test_column >= 5");
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51,
                CONSTRAINT CHK_1 CHECK (test_column >= 5)
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addColumn_default() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addColumn_check() {
        builder.addColumn("test_column", ColumnType.REAL, ColumnModifier.NOT_NULL);
        builder.addCheck("test_column >= 5");
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL,
                CONSTRAINT CHK_1 CHECK (test_column >= 5)
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addColumn() {
        builder.addColumn("test_column", ColumnType.REAL, ColumnModifier.NOT_NULL);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addCompositeForeignKey_on_delete_on_update() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        String[] columns = {"test_column", "test_column2"};
        builder.addColumn("test_column2", ColumnType.TEXT,"'default text'", ColumnModifier.NOT_NULL);
        String[] references = {"parent_column", "parent_column2"};
        builder.addCompositeForeignKey(columns,"parent_table", references, ON_DELETE.CASCADE, ON_UPDATE.SET_DEFAULT);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51,
                "test_column2" TEXT NOT NULL DEFAULT 'default text',
                CONSTRAINT FK_parent_table_1_2 FOREIGN KEY ("test_column","test_column2") REFERENCES "parent_table"("parent_column","parent_column2") ON DELETE CASCADE ON UPDATE SET DEFAULT
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addCompositeForeignKey_on_delete() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        String[] columns = {"test_column", "test_column2"};
        builder.addColumn("test_column2", ColumnType.TEXT,"'default text'" , ColumnModifier.NOT_NULL);
        String[] references = {"parent_column", "parent_column2"};
        builder.addCompositeForeignKey(columns,"parent_table", references, ON_DELETE.CASCADE);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51,
                "test_column2" TEXT NOT NULL DEFAULT 'default text',
                CONSTRAINT FK_parent_table_1_2 FOREIGN KEY ("test_column","test_column2") REFERENCES "parent_table"("parent_column","parent_column2") ON DELETE CASCADE
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addCompositeForeignKey_on_update() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        String[] columns = {"test_column", "test_column2"};
        builder.addColumn("test_column2", ColumnType.TEXT,"'default text'", ColumnModifier.NOT_NULL);
        String[] references = {"parent_column", "parent_column2"};
        builder.addCompositeForeignKey(columns,"parent_table", references, ON_UPDATE.SET_DEFAULT);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51,
                "test_column2" TEXT NOT NULL DEFAULT 'default text',
                CONSTRAINT FK_parent_table_1_2 FOREIGN KEY ("test_column","test_column2") REFERENCES "parent_table"("parent_column","parent_column2") ON UPDATE SET DEFAULT
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addCompositeForeignKey() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        String[] columns = {"test_column", "test_column2"};
        builder.addColumn("test_column2", ColumnType.TEXT,"'default text'" , ColumnModifier.NOT_NULL);
        String[] references = {"parent_column", "parent_column2"};
        builder.addCompositeForeignKey(columns,"parent_table", references);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51,
                "test_column2" TEXT NOT NULL DEFAULT 'default text',
                CONSTRAINT FK_parent_table_1_2 FOREIGN KEY ("test_column","test_column2") REFERENCES "parent_table"("parent_column","parent_column2")
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addForeignKey_on_delete_on_update() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        String column = "test_column";
        String reference = "parent_column";
        builder.addForeignKey(column,"parent_table", reference, ON_DELETE.CASCADE, ON_UPDATE.SET_DEFAULT);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51,
                CONSTRAINT FK_parent_table_1 FOREIGN KEY ("test_column") REFERENCES "parent_table"("parent_column") ON DELETE CASCADE ON UPDATE SET DEFAULT
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addForeignKey_on_delete() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        String column = "test_column";
        String reference = "parent_column";
        builder.addForeignKey(column,"parent_table", reference, ON_DELETE.CASCADE);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51,
                CONSTRAINT FK_parent_table_1 FOREIGN KEY ("test_column") REFERENCES "parent_table"("parent_column") ON DELETE CASCADE
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addForeignKey_on_update() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        String column = "test_column";
        String reference = "parent_column";
        builder.addForeignKey(column,"parent_table", reference, ON_UPDATE.SET_DEFAULT);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51,
                CONSTRAINT FK_parent_table_1 FOREIGN KEY ("test_column") REFERENCES "parent_table"("parent_column") ON UPDATE SET DEFAULT
                );""";
        assertEquals(expected, builder.buildQuery());
    }

    @Test
    void addForeignKey() {
        builder.addColumn("test_column", ColumnType.REAL,"5.51", ColumnModifier.NOT_NULL);
        String column = "test_column";
        String reference = "parent_column";
        builder.addForeignKey(column,"parent_table", reference);
        String expected = """
                CREATE TABLE IF NOT EXISTS test_table (
                "test_column" REAL NOT NULL DEFAULT 5.51,
                CONSTRAINT FK_parent_table_1 FOREIGN KEY ("test_column") REFERENCES "parent_table"("parent_column")
                );""";
        assertEquals(expected, builder.buildQuery());
    }
}