package businessLayer.transportModule;

import businessLayer.transportModule.bingApi.Point;
import dataAccessLayer.dalUtils.DalException;
import dataAccessLayer.transportModule.SitesDAO;
import dataAccessLayer.transportModule.SitesRoutesDAO;
import objects.transportObjects.Site;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serviceLayer.employeeModule.Services.EmployeesService;
import utils.transportUtils.TransportException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SitesControllerTest {

    private SitesController controller;
    private SitesDAO dao;
    private SitesRoutesDAO sitesRoutesDAO;
    private Site site;
    private SitesRoutesController sitesRoutesController;

    @BeforeEach
    void setUp() {
        dao = mock(SitesDAO.class);
        EmployeesService employeesService = mock(EmployeesService.class);
        sitesRoutesDAO = mock(SitesRoutesDAO.class);
        sitesRoutesController = mock(SitesRoutesController.class);
        controller = new SitesController(dao, sitesRoutesDAO, sitesRoutesController);
        controller.injectDependencies(employeesService);

        site = new Site("site1", "address1", "zone1", "phone1", "contact1", Site.SiteType.BRANCH);
    }

    @Test
    void addSite() {
        try{
            when(dao.exists(Site.getLookupObject(site.name()))).thenReturn(false); //checks name
            when(dao.isAddressUnique(site.address())).thenReturn(true); //checks address
            when(sitesRoutesController.getCoordinates(site)).thenReturn(new Point(site.address(), new double[] {1.0, 1.0}));
        } catch (DalException | TransportException e) {
            fail(e);
        }
        assertDoesNotThrow(() -> controller.addSite(site));
    }

    @Test
    void addSiteAlreadyExists(){
        try{
            when(dao.exists(Site.getLookupObject(site.name()))).thenReturn(true); //checks name
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(TransportException.class, () -> controller.addSite(site));
    }

    @Test
    void addSiteAddressAlreadyExists(){
        try{
            when(dao.exists(Site.getLookupObject(site.name()))).thenReturn(false); //checks name
            when(dao.isAddressUnique(site.address())).thenReturn(false); //checks address
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(TransportException.class, () -> controller.addSite(site));
    }

    @Test
    void removeSite() {
        try{
            when(dao.exists(Site.getLookupObject(site.name()))).thenReturn(true);
        } catch (DalException e) {
            fail(e);
        }
        assertDoesNotThrow(() -> controller.removeSite(site.name()));
    }

    @Test
    void removeSiteDoesNotExist(){
        try{
            when(dao.exists(Site.getLookupObject(site.name()))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(TransportException.class, () -> controller.removeSite(site.name()));
    }

    @Test
    void updateSite() {
        Site updatedSite = new Site("site1", site.address(), "zone1", "phone1Updated", "contact1", Site.SiteType.BRANCH);
        try{
            Site lookupObject = Site.getLookupObject(site.name());
            when(dao.exists(lookupObject)).thenReturn(true);
            when(dao.select(lookupObject)).thenReturn(site);
        } catch (DalException e) {
            fail(e);
        }
        assertDoesNotThrow(()->controller.updateSite(updatedSite.name(), updatedSite));
    }

    @Test
    void updateSiteDoesNotExist(){
        Site updatedSite = new Site("site1", site.address(), "zone1", "phone1Updated", "contact1", Site.SiteType.BRANCH);
        try{
            when(dao.exists(Site.getLookupObject(site.name()))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(TransportException.class, ()->controller.updateSite(updatedSite.name(), updatedSite));
    }

    @Test
    void getSite() {
        try{
            Site lookupObject = Site.getLookupObject(site.name());
            when(dao.exists(lookupObject)).thenReturn(true);
            when(dao.select(lookupObject)).thenReturn(site);
        } catch (DalException e) {
            fail(e);
        }
        Site result = assertDoesNotThrow(()->controller.getSite(site.name()));
        assertDeepEquals(site, result);
    }

    @Test
    void getSiteDoesNotExist(){
        try{
            when(dao.exists(Site.getLookupObject(site.name()))).thenReturn(false);
        } catch (DalException e) {
            fail(e);
        }
        assertThrows(TransportException.class, ()->controller.getSite(site.name()));
    }

    @Test
    void getAllSites() {
        Site site2 = new Site("site2", "address2", "zone2", "phone2", "contact2", Site.SiteType.BRANCH);
        try{
            when(dao.selectAll()).thenReturn(List.of(site, site2));
        } catch (DalException e) {
            fail(e);
        }
        List<Site> result = assertDoesNotThrow(()->controller.getAllSites());
        assertEquals(2, result.size());
        assertDeepEquals(site, result.get(0));
        assertDeepEquals(site2, result.get(1));
    }

    private void assertDeepEquals(Site site1, Site site2) {
        assertEquals(site1.transportZone(), site2.transportZone());
        assertEquals(site1.address(), site2.address());
        assertEquals(site1.contactName(), site2.contactName());
        assertEquals(site1.phoneNumber(), site2.phoneNumber());
        assertEquals(site1.siteType(), site2.siteType());
    }
}