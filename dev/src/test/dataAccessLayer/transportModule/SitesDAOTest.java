package dataAccessLayer.transportModule;

import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import objects.transportObjects.Site;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static dataAccessLayer.DalFactory.TESTING_DB_NAME;
import static org.junit.jupiter.api.Assertions.*;

class SitesDAOTest {

    private SitesDAO dao;

    private Site site;
    private DalFactory factory;

    @BeforeEach
    void setUp() {
        site = new Site("name1", "address1", "zone1", "12345","kobi", Site.SiteType.SUPPLIER);
        try {
            factory = new DalFactory(TESTING_DB_NAME);
            dao = factory.sitesDAO();
            dao.clearTable();

            dao.insert(site);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
        dao.clearCache();
    }

    @AfterEach
    void tearDown() {
        try {
            dao.selectAll().forEach(site -> {
                try {
                    dao.delete(site);
                } catch (DalException e) {
                    fail(e);
                }
            });
        } catch (DalException e) {
            fail(e);
        }
        dao = null;
        site = null;
    }

    @Test
    void select() {
        try {
            assertDeepEquals(site,dao.select(Site.getLookupObject(site.address())));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void selectAll() {

        // set up
        LinkedList<Site> sites = new LinkedList<>();
        sites.add(site);
        List.of(2,3,4,5,6).forEach(i -> {
            try {
                Site site = new Site("name"+i, "address" + i, "zone1", "12345","kobi", Site.SiteType.SUPPLIER);
                dao.insert(site);
                sites.add(site);
            } catch (DalException e) {
                fail(e);
            }
        });

        // test
        try {
            List<Site> sitesFromDB = dao.selectAll();
            assertEquals(sites.size(),sitesFromDB.size());
            for (int i = 0; i < sites.size(); i++) {
                assertDeepEquals(sites.get(i),sitesFromDB.get(i));
            }
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void insert() {
        try {
            Site site2 = new Site("name2", "address2", "zone1", "12345","kobi", Site.SiteType.SUPPLIER);
            dao.insert(site2);
            assertDeepEquals(site2,dao.select(Site.getLookupObject(site2.name())));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void update() {
        try {
            Site site2 = new Site(site.name(), site.address(), "zone2", "51235","lo kobi", Site.SiteType.LOGISTICAL_CENTER);
            dao.update(site2);
            assertDeepEquals(site2,dao.select(Site.getLookupObject(site2.address())));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void delete() {
        try {
            dao.delete(site);
            assertThrows(DalException.class,() -> dao.select(Site.getLookupObject(site.address())));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void getObjectFromResultSet() {
        SQLExecutor cursor = factory.cursor();
        try{
            OfflineResultSet resultSet = cursor.executeRead(String.format("SELECT * FROM Sites WHERE name = '%s';",site.name()));
            resultSet.next();
            Site siteFromDB = dao.getObjectFromResultSet(resultSet);
            assertDeepEquals(site,siteFromDB);
        } catch (SQLException e) {
            fail(e);
        }
    }

    private void assertDeepEquals(Site site1, Site site2) {
        assertEquals(site1.name(),site2.name());
        assertEquals(site1.address(),site2.address());
        assertEquals(site1.contactName(),site2.contactName());
        assertEquals(site1.phoneNumber(),site2.phoneNumber());
        assertEquals(site1.transportZone(),site2.transportZone());
        assertEquals(site1.siteType(),site2.siteType());
    }
}