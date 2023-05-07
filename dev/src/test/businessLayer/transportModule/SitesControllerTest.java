package businessLayer.transportModule;

import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.SitesDAO;
import dataAccessLayer.transportModule.SitesDistancesDAO;
import objects.transportObjects.Site;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Response;
import utils.transportUtils.TransportException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SitesControllerTest {

    private SitesController controller;
    private SitesDAO dao;
    private SitesDistancesDAO distancesDAO;
    private Site site;

    @BeforeEach
    void setUp() {
        dao = mock(SitesDAO.class);
        distancesDAO = mock(SitesDistancesDAO.class);
        controller = new SitesController(dao, distancesDAO);

        site = new Site("zone1", "address1", "phone1", "contact1", Site.SiteType.BRANCH);
    }

    @Test
    void addSite() {
        try{
            when(dao.exists(Site.getLookupObject(site.address()))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        assertDoesNotThrow(() -> controller.addSite(site));
    }

    @Test
    void addSiteAlreadyExists(){
        try{
            when(dao.exists(Site.getLookupObject(site.address()))).thenReturn(true);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(TransportException.class, () -> controller.addSite(site));
    }

    @Test
    void removeSite() {

    }

    @Test
    void removeSiteDoesNotExist(){

    }

    @Test
    void updateSite() {

    }

    @Test
    void updateSiteDoesNotExist(){

    }

    @Test
    void getSite() {

    }

    @Test
    void getSiteDoesNotExist(){
    }

    @Test
    void getAllSites() {
    }
}