package it.verde.config;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import it.verde.ApplicationRunner;
import it.verde.controller.ApplicationController;
import it.verde.controller.AuthenticationController;
import it.verde.controller.PlantSpeciesController;
import it.verde.controller.PriceController;
import it.verde.controller.RetailCompanyController;
import it.verde.controller.SalesOrderController;
import it.verde.controller.SupplierController;
import it.verde.controller.SupplyOrderController;
import it.verde.controller.UserController;
import it.verde.controller.WarehouseController;
import it.verde.controller.ui.PlantSpeciesUiController;
import it.verde.controller.ui.PriceUiController;
import it.verde.controller.ui.RetailCompanyUiController;
import it.verde.controller.ui.SalesOrderUiController;
import it.verde.controller.ui.SupplierUiController;
import it.verde.controller.ui.SupplyOrderUiController;
import it.verde.controller.ui.UserUiController;
import it.verde.controller.ui.WarehouseUiController;
import it.verde.controller.factory.AdministratorControllerFactory;
import it.verde.controller.factory.LogisticsManagerControllerFactory;
import it.verde.controller.factory.RoleControllerRouter;
import it.verde.controller.factory.SalesManagerControllerFactory;
import it.verde.exception.DaoException;
import it.verde.mapper.AddressMapper;
import it.verde.mapper.AuthenticationResultMapper;
import it.verde.mapper.ContactMapper;
import it.verde.mapper.CriticalStockMapper;
import it.verde.mapper.PlantSpeciesMapper;
import it.verde.mapper.PriceHistoryMapper;
import it.verde.mapper.RetailCompanyMapper;
import it.verde.mapper.SalesOrderItemMapper;
import it.verde.mapper.SalesOrderMapper;
import it.verde.mapper.SpeciesCatalogItemMapper;
import it.verde.mapper.SpeciesSalesReportMapper;
import it.verde.mapper.SupplierMapper;
import it.verde.mapper.SupplyOrderItemMapper;
import it.verde.mapper.SupplyOrderMapper;
import it.verde.mapper.UserAccountMapper;
import it.verde.mapper.WarehouseStockMapper;
import it.verde.persistence.cache.CachedPlantSpeciesDao;
import it.verde.persistence.cache.CachedRetailCompanyDao;
import it.verde.persistence.cache.CachedSupplierDao;
import it.verde.persistence.connection.ConnectionProvider;
import it.verde.persistence.connection.DatabaseSession;
import it.verde.persistence.connection.JdbcConnectionManager;
import it.verde.persistence.dao.AuthenticationDao;
import it.verde.persistence.dao.PlantSpeciesDao;
import it.verde.persistence.dao.PriceHistoryDao;
import it.verde.persistence.dao.RetailCompanyDao;
import it.verde.persistence.dao.SalesOrderDao;
import it.verde.persistence.dao.SupplierDao;
import it.verde.persistence.dao.SupplyOrderDao;
import it.verde.persistence.dao.UserDao;
import it.verde.persistence.dao.WarehouseDao;
import it.verde.persistence.jdbc.JdbcAuthenticationDao;
import it.verde.persistence.jdbc.JdbcPlantSpeciesDao;
import it.verde.persistence.jdbc.JdbcPriceHistoryDao;
import it.verde.persistence.jdbc.JdbcRetailCompanyDao;
import it.verde.persistence.jdbc.JdbcSalesOrderDao;
import it.verde.persistence.jdbc.JdbcSupplierDao;
import it.verde.persistence.jdbc.JdbcSupplyOrderDao;
import it.verde.persistence.jdbc.JdbcUserDao;
import it.verde.persistence.jdbc.JdbcWarehouseDao;
import it.verde.view.AdministratorView;
import it.verde.view.ApplicationView;
import it.verde.view.LoginView;
import it.verde.view.LogisticsManagerView;
import it.verde.view.PlantSpeciesView;
import it.verde.view.PriceView;
import it.verde.view.RetailCompanyView;
import it.verde.view.SalesManagerView;
import it.verde.view.SalesOrderView;
import it.verde.view.SupplierView;
import it.verde.view.SupplyOrderView;
import it.verde.view.UserView;
import it.verde.view.WarehouseView;
import it.verde.view.console.ConsoleInputView;
import it.verde.view.console.ConsoleOutputView;
import it.verde.view.console.ConsoleUiManager;
import it.verde.view.core.InputView;
import it.verde.view.core.OutputView;
import it.verde.view.core.UiManager;
import it.verde.view.core.UiTheme;

import java.util.List;
import java.util.Scanner;

public final class ApplicationCompositionRoot implements AutoCloseable {
    private final ApplicationRunner applicationRunner;
    private final DatabaseSession databaseSession;

    private final ApplicationController applicationController;
    private final PlantSpeciesController plantSpeciesController;
    private final PriceController priceController;
    private final RetailCompanyController retailCompanyController;
    private final SalesOrderController salesOrderController;
    private final SupplierController supplierController;
    private final SupplyOrderController supplyOrderController;
    private final UserController userController;
    private final WarehouseController warehouseController;

    private final ApplicationView applicationView;
    private final LoginView loginView;
    private final AdministratorView administratorView;
    private final SalesManagerView salesManagerView;
    private final LogisticsManagerView logisticsManagerView;
    private final PlantSpeciesView plantSpeciesView;
    private final PriceView priceView;
    private final RetailCompanyView retailCompanyView;
    private final SalesOrderView salesOrderView;
    private final SupplierView supplierView;
    private final SupplyOrderView supplyOrderView;
    private final UserView userView;
    private final WarehouseView warehouseView;

    public ApplicationCompositionRoot() {
        JdbcConnectionManager connectionManager = JdbcConnectionManager.getInstance();
        ConnectionProvider connectionProvider = connectionManager;
        databaseSession = connectionManager;

        AuthenticationDao authenticationDao = new JdbcAuthenticationDao(connectionProvider);
        PlantSpeciesDao plantSpeciesDao = new CachedPlantSpeciesDao(new JdbcPlantSpeciesDao(connectionProvider), connectionProvider);
        PriceHistoryDao priceHistoryDao = new JdbcPriceHistoryDao(connectionProvider);
        RetailCompanyDao retailCompanyDao = new CachedRetailCompanyDao(new JdbcRetailCompanyDao(connectionProvider), connectionProvider);
        SalesOrderDao salesOrderDao = new JdbcSalesOrderDao(connectionProvider);
        SupplierDao supplierDao = new CachedSupplierDao(new JdbcSupplierDao(connectionProvider), connectionProvider);
        SupplyOrderDao supplyOrderDao = new JdbcSupplyOrderDao(connectionProvider);
        UserDao userDao = new JdbcUserDao(connectionProvider);
        WarehouseDao warehouseDao = new JdbcWarehouseDao(connectionProvider);

        AddressMapper addressMapper = new AddressMapper();
        ContactMapper contactMapper = new ContactMapper();
        AuthenticationResultMapper authenticationResultMapper = new AuthenticationResultMapper();
        PlantSpeciesMapper plantSpeciesMapper = new PlantSpeciesMapper();
        PriceHistoryMapper priceHistoryMapper = new PriceHistoryMapper();
        SpeciesCatalogItemMapper catalogItemMapper = new SpeciesCatalogItemMapper();
        SpeciesSalesReportMapper salesReportMapper = new SpeciesSalesReportMapper();
        WarehouseStockMapper warehouseStockMapper = new WarehouseStockMapper();
        CriticalStockMapper criticalStockMapper = new CriticalStockMapper();
        RetailCompanyMapper retailCompanyMapper = new RetailCompanyMapper(addressMapper, contactMapper);
        SalesOrderItemMapper salesOrderItemMapper = new SalesOrderItemMapper();
        SalesOrderMapper salesOrderMapper = new SalesOrderMapper(salesOrderItemMapper);
        SupplierMapper supplierMapper = new SupplierMapper(addressMapper);
        SupplyOrderItemMapper supplyOrderItemMapper = new SupplyOrderItemMapper();
        SupplyOrderMapper supplyOrderMapper = new SupplyOrderMapper(supplyOrderItemMapper);
        UserAccountMapper userAccountMapper = new UserAccountMapper();

        AuthenticationController authenticationController = new AuthenticationController(authenticationDao, authenticationResultMapper);
        RoleControllerRouter roleControllerRouter = new RoleControllerRouter(List.of(
                new AdministratorControllerFactory(),
                new SalesManagerControllerFactory(),
                new LogisticsManagerControllerFactory()
        ));

        applicationController = new ApplicationController(authenticationController, roleControllerRouter, databaseSession);
        plantSpeciesController = new PlantSpeciesController(plantSpeciesDao, plantSpeciesMapper, catalogItemMapper, salesReportMapper);
        priceController = new PriceController(priceHistoryDao, plantSpeciesDao, priceHistoryMapper);
        retailCompanyController = new RetailCompanyController(retailCompanyDao, retailCompanyMapper);
        salesOrderController = new SalesOrderController(salesOrderDao, retailCompanyDao, plantSpeciesDao, warehouseDao, priceHistoryDao, salesOrderMapper, salesOrderItemMapper);
        supplierController = new SupplierController(supplierDao, supplierMapper, plantSpeciesMapper);
        supplyOrderController = new SupplyOrderController(
                new SupplyOrderController.DaoDependencies(supplyOrderDao, supplierDao, plantSpeciesDao, warehouseDao),
                new SupplyOrderController.MapperDependencies(supplyOrderMapper, supplyOrderItemMapper, supplierMapper, plantSpeciesMapper)
        );
        userController = new UserController(userDao, userAccountMapper);
        warehouseController = new WarehouseController(warehouseDao, plantSpeciesDao, warehouseStockMapper, catalogItemMapper, criticalStockMapper);

        UiTheme theme = new UiTheme();
        Scanner scanner = new Scanner(System.in);
        InputView input = new ConsoleInputView(scanner, theme);
        OutputView output = new ConsoleOutputView(theme);
        UiManager ui = new ConsoleUiManager(theme);

        applicationView = new ApplicationView(input, output, ui);
        loginView = new LoginView(input, output, ui);
        administratorView = new AdministratorView(input, output, ui);
        salesManagerView = new SalesManagerView(input, output, ui);
        logisticsManagerView = new LogisticsManagerView(input, output, ui);
        plantSpeciesView = new PlantSpeciesView(input, output, ui);
        priceView = new PriceView(input, output, ui);
        retailCompanyView = new RetailCompanyView(input, output, ui);
        salesOrderView = new SalesOrderView(input, output, ui);
        supplierView = new SupplierView(input, output, ui);
        supplyOrderView = new SupplyOrderView(input, output, ui);
        userView = new UserView(input, output, ui);
        warehouseView = new WarehouseView(input, output, ui);

        PlantSpeciesUiController plantSpeciesUiController = new PlantSpeciesUiController(plantSpeciesController, plantSpeciesView);
        PriceUiController priceUiController = new PriceUiController(priceController, priceView);
        RetailCompanyUiController retailCompanyUiController = new RetailCompanyUiController(retailCompanyController, retailCompanyView);
        SalesOrderUiController salesOrderUiController = new SalesOrderUiController(salesOrderController, salesOrderView);
        WarehouseUiController warehouseUiController = new WarehouseUiController(warehouseController, warehouseView);
        SupplyOrderUiController supplyOrderUiController = new SupplyOrderUiController(supplyOrderController, supplyOrderView);
        SupplierUiController supplierUiController = new SupplierUiController(supplierController, supplierView);
        UserUiController userUiController = new UserUiController(userController, userView);

        applicationRunner = new ApplicationRunner(
                applicationController,
                new ApplicationRunner.Views(
                        applicationView,
                        loginView,
                        administratorView,
                        salesManagerView,
                        logisticsManagerView
                ),
                new ApplicationRunner.AdministratorUiControllers(
                        userUiController
                ),
                new ApplicationRunner.SalesUiControllers(
                        plantSpeciesUiController,
                        priceUiController,
                        retailCompanyUiController,
                        salesOrderUiController
                ),
                new ApplicationRunner.LogisticsUiControllers(
                        warehouseUiController,
                        supplyOrderUiController,
                        supplierUiController
                )
        );
    }

    public ApplicationRunner applicationRunner() { return applicationRunner; }

    public ApplicationController applicationController() { return applicationController; }
    public PlantSpeciesController plantSpeciesController() { return plantSpeciesController; }
    public PriceController priceController() { return priceController; }
    public RetailCompanyController retailCompanyController() { return retailCompanyController; }
    public SalesOrderController salesOrderController() { return salesOrderController; }
    public SupplierController supplierController() { return supplierController; }
    public SupplyOrderController supplyOrderController() { return supplyOrderController; }
    public UserController userController() { return userController; }
    public WarehouseController warehouseController() { return warehouseController; }

    public ApplicationView applicationView() { return applicationView; }
    public LoginView loginView() { return loginView; }
    public AdministratorView administratorView() { return administratorView; }
    public SalesManagerView salesManagerView() { return salesManagerView; }
    public LogisticsManagerView logisticsManagerView() { return logisticsManagerView; }
    public PlantSpeciesView plantSpeciesView() { return plantSpeciesView; }
    public PriceView priceView() { return priceView; }
    public RetailCompanyView retailCompanyView() { return retailCompanyView; }
    public SalesOrderView salesOrderView() { return salesOrderView; }
    public SupplierView supplierView() { return supplierView; }
    public SupplyOrderView supplyOrderView() { return supplyOrderView; }
    public UserView userView() { return userView; }
    public WarehouseView warehouseView() { return warehouseView; }

    @Override
    public void close() throws DaoException {
        try {
            databaseSession.close();
        } finally {
            AbandonedConnectionCleanupThread.checkedShutdown();
        }
    }
}
