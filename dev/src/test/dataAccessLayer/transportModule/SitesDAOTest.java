package dataAccessLayer.transportModule;

import dataAccessLayer.DalFactory;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.dalUtils.OfflineResultSet;
import dataAccessLayer.dalUtils.SQLExecutor;
import dataAccessLayer.dalUtils.SQLExecutorProductionImpl;
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

    @BeforeEach
    void setUp() {
        site = new Site("zone1","address1","12345","kobi", Site.SiteType.SUPPLIER);
        try {
            DalFactory factory = new DalFactory(TESTING_DB_NAME);
            dao = factory.sitesDAO();
            dao.clearTable();

            dao.insert(site);
        } catch (DalException e) {
            throw new RuntimeException(e);
        }
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
        List.of("address2","address3","address4","address5","address6").forEach(address -> {
            try {
                Site site = new Site("zone1",address,"12345","kobi", Site.SiteType.SUPPLIER);
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
            Site site2 = new Site("zone1","address2","12345","kobi", Site.SiteType.SUPPLIER);
            dao.insert(site2);
            assertDeepEquals(site2,dao.select(Site.getLookupObject(site2.address())));
        } catch (DalException e) {
            fail(e);
        }
    }

    @Test
    void update() {
        try {
            Site site2 = new Site("zone2",site.address(),"51235","lo kobi", Site.SiteType.LOGISTICAL_CENTER);
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
        SQLExecutor cursor = new SQLExecutorProductionImpl(TESTING_DB_NAME);
        try{
            OfflineResultSet resultSet = cursor.executeRead(String.format("SELECT * FROM Sites WHERE address = '%s';",site.address()));
            resultSet.next();
            Site siteFromDB = dao.getObjectFromResultSet(resultSet);
            assertDeepEquals(site,siteFromDB);
        } catch (SQLException e) {
            fail(e);
        }
    }

    private void assertDeepEquals(Site site1, Site site2) {
        assertEquals(site1.address(),site2.address());
        assertEquals(site1.address(),site2.address());
        assertEquals(site1.contactName(),site2.contactName());
        assertEquals(site1.phoneNumber(),site2.phoneNumber());
        assertEquals(site1.transportZone(),site2.transportZone());
        assertEquals(site1.siteType(),site2.siteType());
    }
}