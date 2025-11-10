package CatsAndOwners;

import CatsAndOwners.console.ConsoleInterface;
import CatsAndOwners.controller.CatController;
import CatsAndOwners.controller.OwnerController;
import CatsAndOwners.dao.cat.impl.CatDaoImpl;
import CatsAndOwners.dao.owner.impl.OwnerDaoImpl;
import CatsAndOwners.service.cat.impl.CatServiceImpl;
import CatsAndOwners.service.owner.impl.OwnerServiceImpl;
import CatsAndOwners.util.HibernateUtil;
import org.hibernate.SessionFactory;

public class Application {
    public static void main(String[] args) {
        System.out.println("Запуск приложения CatsAndOwners...");

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        var catDao = new CatDaoImpl(sessionFactory);
        var ownerDao = new OwnerDaoImpl(sessionFactory);

        var catService = new CatServiceImpl(catDao);
        var ownerService = new OwnerServiceImpl(ownerDao, catDao);

        var catController = new CatController(catService);
        var ownerController = new OwnerController(ownerService);

        var consoleInterface = new ConsoleInterface(catController, ownerController);
        consoleInterface.start();

        HibernateUtil.shutdown();
    }
}