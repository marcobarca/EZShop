package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//import javax.transaction.InvalidTransactionException;

import it.polito.ezshop.DB.DBAPIs;
import it.polito.ezshop.Utils.*;

public class EZShop implements EZShopInterface {

    private User loggedUser;
    SaleTransaction openSaleTransaction = null;
    SaleTransaction openReturnSaleTransaction = null;
    Hashtable<Integer, ProductType> productTypeLocalList = new Hashtable<>();
    Hashtable<String, String> RFIDLocalList = new Hashtable<>();
    Hashtable<String, String> RFIDReturnLocalList = new Hashtable<>();

    // --------------------------------------------------------------------------------------------//

    // ---------------------------------------------------------------------//
    // -----------------------------METHODS---------------------------------//
    // ---------------------------------------------------------------------//

    @Override
    public void reset() {
        DBAPIs.reset();
    }

    @Override
    public Integer createUser(String username, String password, String role)
            throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        if (username == null || username.isEmpty())
            throw new InvalidUsernameException("Username not valid");
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException("Password not valid");
        if (role == null || role.isEmpty() || !(role.equals("Administrator") || role.equals("ShopManager") || role.equals("Cashier")))
            throw new InvalidRoleException("Invalid role");

        if (DBAPIs.getUserFromUsername(username) != null)
            // If the username already exists return -1
            return -1;

        int id = DBAPIs.getUserCount() + 1;
        UserClass user = new UserClass(id, username, password, role);
        DBAPIs.createUser(user);
        return id;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        // Check for logged user (Only Administrator)
        if (Objects.isNull(loggedUser) || !(loggedUser.getRole().equals("Administrator")))
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidUserIdException("Id not valid");

        // delete user
        return DBAPIs.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        // Check for logged user (Only Administrator)
        if (Objects.isNull(loggedUser) || !(loggedUser.getRole().equals("Administrator")))
            throw new UnauthorizedException();
        return DBAPIs.getAllUsers();
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        // Check for logged user (Only Administrator)
        if (Objects.isNull(loggedUser) || !(loggedUser.getRole().equals("Administrator")))
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidUserIdException("Id not valid");

        // return null if user does't exists
        return DBAPIs.getUserFromId(id);
    }

    @Override
    public boolean updateUserRights(Integer id, String role)
            throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        // Check for logged user (Only Administrator)
        if (Objects.isNull(loggedUser) || !(loggedUser.getRole().equals("Administrator")))
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidUserIdException("Id not valid");

        if (role == null || role.isEmpty() || !(role.equals("Administrator") || role.equals("ShopManager") || role.equals("Cashier")))
            throw new InvalidRoleException("Invalid role");

        return DBAPIs.updateUserRights(id, role);
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if (username == null || username.isEmpty())
            throw new InvalidUsernameException("Username not valid");
        if (password == null || password.isEmpty())
            throw new InvalidPasswordException("Password not valid");
        User user = DBAPIs.getUserFromUsername(username);

        if (user == null)
            return null;

        if (password.equals(user.getPassword()))
            loggedUser = user;
        else 
            return null;

        for (ProductType product : DBAPIs.getAllProductTypes()) 
            productTypeLocalList.put(product.getId(), product);

        // Add rfid to list
    
        return user;
    }

    @Override
    public boolean logout() {
        if (loggedUser == null)
            return false;
        else {
            loggedUser = null;
            openSaleTransaction = null;
            openReturnSaleTransaction = null;
            productTypeLocalList = new Hashtable<>();
            RFIDLocalList = new Hashtable<>();
            RFIDReturnLocalList = new Hashtable<>();
            return true;
        }
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note)
            throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
            UnauthorizedException {
        // Check for logged user (Only Administrator)
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (description == null || description.isEmpty())
            throw new InvalidProductDescriptionException("Description not valid");
        if (productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?")
                || !BarcodeCheck.checkEAN13validity(productCode))
            throw new InvalidProductCodeException("ProductCode not valid");
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException("PricePerUnit less or equal to zero");
        // Check on existing produtCode
        if (DBAPIs.getProductTypeFromBarcode(productCode) != null) {
            return -1;
        }

        int maxId = DBAPIs.getProductTypeCount();
        int nextId;
        if (Objects.isNull(maxId))
            nextId = 1;
        else
            nextId = maxId + 1;

        ProductTypeClass p = new ProductTypeClass(note, description, productCode, pricePerUnit, nextId);

        DBAPIs.createProductType(p);
        productTypeLocalList.put(p.getId(), p);
        return nextId;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
            throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException,
            InvalidPricePerUnitException, UnauthorizedException {
        // Check for logged user (Only Administrator)
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (id <= 0 || id == null)
            throw new InvalidProductIdException("Id not valid");
        if (newDescription == null || newDescription.isEmpty())
            throw new InvalidProductDescriptionException("Description not valid");
        if (newCode == null || newCode.isEmpty() || !newCode.matches("-?\\d+(\\.\\d+)?")
                || !BarcodeCheck.checkEAN13validity(newCode))
            throw new InvalidProductCodeException("ProductCode not valid");
        if (newPrice <= 0)
            throw new InvalidPricePerUnitException("PricePerUnit less or equal to zero");
        
        // Check on existing produtCode
        if (DBAPIs.getProductTypeFromBarcode(newCode) != null 
            && DBAPIs.getProductTypeFromBarcode(newCode).getId() != id) {
            throw new InvalidProductCodeException("ProductCode not valid");
        }   

        // Update Product locally
        ProductType localProduct = productTypeLocalList.get(id);
        if(localProduct == null) return false;
        localProduct.setBarCode(newCode);
        localProduct.setPricePerUnit(newPrice);
        localProduct.setNote(newNote);

        return DBAPIs.updateProductType(id, newDescription, newCode, newPrice, newNote);
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        // Check for logged user (Only Administrator)
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (id == null || id <= 0)
            throw new InvalidProductIdException("Id not valid");

        // Remove product locally
        productTypeLocalList.remove(id);

        return DBAPIs.deleteProductType(id);
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        // Check for logged user (Only Administrator or ShopManager)
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        return new ArrayList<ProductType>(productTypeLocalList.values());
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode)
            throws InvalidProductCodeException, UnauthorizedException {
        // Check for logged user (Only Administrator or ShopManager)
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (barCode == null || barCode.isEmpty() || !barCode.matches("-?\\d+(\\.\\d+)?")
                || !BarcodeCheck.checkEAN13validity(barCode))
            throw new InvalidProductCodeException("ProductCode not valid");

        for (ProductType product : productTypeLocalList.values()) {
            if (product.getBarCode().equals(barCode))
                return product;
        }
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        // Check for logged user (Only Administrator or ShopManager)
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();

        if(description == null)
            description = "";

        List<ProductType> rightProducts = new ArrayList<>();
        for (ProductType product : getAllProductTypes()) {
            if (product.getProductDescription().contains(description)) {
                rightProducts.add(product);
            }
        }
        return rightProducts;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded)
            throws InvalidProductIdException, UnauthorizedException {
        // Check for logged user (Only Administrator or ShopManager)
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (productId == null || productId <= 0)
            throw new InvalidProductIdException("Id not valid");

        ProductType product = DBAPIs.getProductTypeFromId(productId);

        if (product == null || product.getProductDescription().isEmpty() || product.getLocation().equals("undefined")
                || (product.getQuantity() + toBeAdded) < 0)
            return false;

        ProductType localProduct = productTypeLocalList.get(productId);
        localProduct.setQuantity(localProduct.getQuantity() + toBeAdded);

        return DBAPIs.updateProductTypeQuantity(productId, localProduct.getQuantity());
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos)
            throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (productId == null || productId <= 0)
            throw new InvalidProductIdException("Id not valid");

        String[] parts = newPos.split("-");
        String isleNumber = parts[0];
        String rackAlphabeticIdentifier = parts[1];
        String levelNumber = parts[2];

        if (!isleNumber.matches("-?\\d+(\\.\\d+)?") || !rackAlphabeticIdentifier.matches("^[a-zA-Z]*$")
                || !levelNumber.matches("-?\\d+(\\.\\d+)?"))
            throw new InvalidLocationException();

        ProductType product = DBAPIs.getProductTypeFromId(productId);
        if (Objects.isNull(product))
            return false;

        // Check of uniqueness position
        List<ProductType> allProducts = getAllProductTypes();
        for (ProductType p : allProducts) {
            if (!Objects.isNull(p.getLocation())) {
                if (p.getLocation().equals(newPos))
                    return false;
            }
        }

        productTypeLocalList.get(productId).setLocation(newPos);

        return DBAPIs.updateProductTypePosition(productId, newPos);
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException,
            InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException("PricePerUnit less or equal to zero");
        if (quantity <= 0)
            throw new InvalidQuantityException("Quantity less or equal to zero");
        if (productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?")
        || !BarcodeCheck.checkEAN13validity(productCode))
            throw new InvalidProductCodeException("ProductCode not valid");
        ProductType localProduct = DBAPIs.getProductTypeFromBarcode(productCode);
        if(localProduct == null) return -1; //if product doesn't exist

        int orderId = DBAPIs.getOrderCount() + 1;
        OrderClass order = new OrderClass(orderId, 0, productCode, pricePerUnit, quantity, "ISSUED");
        if (DBAPIs.createOrder(order))
            return orderId;
        return -1;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
            throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException,
            UnauthorizedException {
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        
        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException("PricePerUnit less or equal to zero");
        if (quantity <= 0)
            throw new InvalidQuantityException("Quantity less or equal to zero");
        if (productCode == null || productCode.isEmpty() || !productCode.matches("-?\\d+(\\.\\d+)?")
            || !BarcodeCheck.checkEAN13validity(productCode))
            throw new InvalidProductCodeException("ProductCode not valid");
        ProductType localProduct = DBAPIs.getProductTypeFromBarcode(productCode);
        if(localProduct == null) return -1; //if product doesn't exist

        int orderId = DBAPIs.getOrderCount() + 1;
        int balanceId = DBAPIs.getBalanceOperationCount() + 1;
        OrderClass order = new OrderClass(orderId, balanceId, productCode, pricePerUnit, quantity, "ISSUED");
        double currentBalance = this.computeBalance();
        double totalCost = pricePerUnit * quantity;
        

        if (totalCost < currentBalance) {
            try {
                DBAPIs.createOrder(order);
                this.payOrder(orderId);
            } catch (InvalidOrderIdException e) {
            }
            return orderId;
        }

        return -1;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException("OrderId not valid");

        Order order = DBAPIs.getOrderFromId(orderId);
        if (Objects.isNull(order) || !order.getStatus().equals("ISSUED"))
            return false;

        double currentBalance = this.computeBalance();
        double totalCost = order.getPricePerUnit() * order.getQuantity();
        if (totalCost > currentBalance) return false;
        
        this.recordBalanceUpdate(-totalCost);
        order.setStatus("PAYED");

        return DBAPIs.updateOrder(order);
    }

    @Override
    public boolean recordOrderArrival(Integer orderId)
            throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException("OrderId not valid");
        
        Order order = DBAPIs.getOrderFromId(orderId);
        if (order == null)
            return false;

        if (DBAPIs.getProductTypeFromBarcode(order.getProductCode()).getLocation().equals("undefined"))
            throw new InvalidLocationException("Location not assigned!");
        
        if (order.getStatus().equals("ISSUED") || order.getStatus().equals("PAYED")) {
            ProductType product = DBAPIs.getProductTypeFromBarcode(order.getProductCode());
            product.setQuantity(order.getQuantity());
            if (DBAPIs.updateProductTypeQuantity(product.getId(), product.getQuantity())) {
                order.setStatus("COMPLETED");
                return DBAPIs.updateOrder(order);
            }
        }

        return false;
    }

    @Override
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, 
InvalidLocationException, InvalidRFIDException {

        if (Objects.isNull(loggedUser)
            || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();

        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException("OrderId not valid");

        if(RFIDfrom == null || RFIDfrom.isEmpty() || !RFIDfrom.matches("^\\d{10}$") || Integer.parseInt(RFIDfrom)<0 || DBAPIs.getBarcodeFromRFID(RFIDfrom) != null)
            throw new InvalidRFIDException();

        Order order = DBAPIs.getOrderFromId(orderId);
        if (order == null)
            return false;

        if (DBAPIs.getProductTypeFromBarcode(order.getProductCode()).getLocation().equals("undefined"))
            throw new InvalidLocationException("Location not assigned!");

        if (order.getStatus().equals("ISSUED") || order.getStatus().equals("COMPLETED") || order.getStatus().equals("PAYED")) {
            ProductType product = DBAPIs.getProductTypeFromBarcode(order.getProductCode());
            product.setQuantity(order.getQuantity());
            if (DBAPIs.updateProductTypeQuantity(product.getId(), product.getQuantity())) {
                for (int i=0; i<order.getQuantity(); i++){
                    DecimalFormat df = new DecimalFormat("0000000000"); 
                    DBAPIs.createRFID(df.format(Integer.parseInt(RFIDfrom)+i), product.getBarCode());
                }
                order.setStatus("COMPLETED");
                return DBAPIs.updateOrder(order);
            }
        }
        

        return false;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();
        return DBAPIs.getAllOrders();
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Check for empty customer name
        if (customerName == null || customerName.isEmpty())
            throw new InvalidCustomerNameException("Username not valid");

        if (DBAPIs.getCustomerFromName(customerName) != null)
            // customer with chosen name already exists
            return -1;

        // Creation of user
        int id = DBAPIs.getCustomerCount() + 1;
        CustomerClass newCustomer = new CustomerClass(id, customerName);
        DBAPIs.createCustomer(newCustomer);
        return id;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
            throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
            UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Check id
        if (id == null || id <= 0) 
            throw new InvalidCustomerIdException("Id not valid");

        // Check for empty customer name
        if (newCustomerName == null || newCustomerName.isEmpty())
            throw new InvalidCustomerNameException("Username not valid");

        // The file EZShopInterface.java contains an inconsistency in directives. We
        // choose to accept an empty value for customerCard and in this case to delete
        // the card already assigned. Thus a null value is accepted an in that case
        // we don't modify the customer card.
        if(!newCustomerCard.isEmpty() && (newCustomerCard.length() != 10 || !newCustomerCard.matches("[0-9]+")))
            throw new InvalidCustomerCardException("CustomerCard not valid");

        Customer customer = DBAPIs.getCustomerFromId(id);

        if (customer == null)
            // Customer with given id doen't exist.
            return false;

        if (newCustomerCard.isEmpty()) {
            // Removing the existing card
            customer.setCustomerCard(null);
        }

        if (newCustomerCard != null && !newCustomerCard.isEmpty()
                && DBAPIs.getCustomerFromCard(newCustomerCard) != null)
            // customerCard already exist. It must be unique.
            return false;

        customer.setCustomerCard(newCustomerCard);

        customer.setCustomerName(newCustomerName);

        return DBAPIs.updateCustomer(customer);
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Check for empty id
        if (id == null || id <= 0)
            throw new InvalidCustomerIdException("id not valid");

        return DBAPIs.deleteCustomer(id);
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Check for empty id
        if (id == null || id <= 0)
            throw new InvalidCustomerIdException("id not valid");

        return DBAPIs.getCustomerFromId(id);
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        return DBAPIs.getAllCustomers();
    }

    @Override
    public String createCard() throws UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // chose a Character random from this String
        String AlphaNumericString = "0123456789";
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(10);

        do {
            for (int i = 0; i < 10; i++) {
                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index = (int) (AlphaNumericString.length() * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString.charAt(index));
            }
        } while (DBAPIs.getCustomerFromCard(sb.toString()) != null);

        return sb.toString();
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId)
            throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Check for empty id
        if (customerId == null || customerId <= 0)
            throw new InvalidCustomerIdException("id not valid");

        // Check for invalid customerCard
        if (customerCard == null || customerCard.length() != 10 || customerCard.isEmpty())
            throw new InvalidCustomerCardException("CustomerCard not valid");

        if (DBAPIs.getCustomerFromCard(customerCard) != null)
            // customerCard must be unique.
            return false;

        return DBAPIs.updateCustomerCard(customerId, customerCard);
    }

    // The file EZShopInterface.java contains an inconsistency in directives. We
    // choose to accept null or negative values of pointsToBeAdded.
    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
            throws InvalidCustomerCardException, UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Check for invalid customerCard
        if (customerCard == null || customerCard.length() != 10 || !customerCard.matches("[0-9]+") || customerCard.isEmpty())
            throw new InvalidCustomerCardException("CustomerCard not valid");

        Customer customer = DBAPIs.getCustomerFromCard(customerCard);

        if (customer == null)
            // customer(so, the card) doesn't exists or problem on DB
            return false;

        if (pointsToBeAdded < 0 && ((customer.getPoints() + pointsToBeAdded) < 0))
            // pointsToBeAdded is negative and not enough points on card
            return false;

        return DBAPIs.updatePointsOnCard(customerCard, customer.getPoints() + pointsToBeAdded);
    }

    // -----------------------------------------------------------//
    // ----------------------SaleTransaction----------------------//
    // -----------------------------------------------------------//

    @Override
    // This method creates a new SaleTransaction and sets his id(ticketNumber)
    // taking it as the size of the hashtable.
    public Integer startSaleTransaction() throws UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        SaleTransactionClass sale = new SaleTransactionClass();
        int id = DBAPIs.getClosedSaleTransactionCount() + 1;
        sale.setTicketNumber(id);
        openSaleTransaction = sale;
        return id;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
            UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        // Invalid amount check
        if (amount <= 0)
            throw new InvalidQuantityException();

        ProductType productType;

        // ProductType check
        if (!(productCode == null || productCode == "")) {
            productType = getProductTypeByBarCode(productCode);
            if (productType == null)
                // ProductCode not correspond to an existing ProductType
                return false;
        } else {
            // ProductCode not valid
            throw new InvalidProductCodeException();
        }

        
        for (ProductType product : productTypeLocalList.values()) 
            if (product.getBarCode().equals(productCode)) 
                if(product.getQuantity() < amount)
                    // ProductType quantity cannot satisfy the request
                    return false;    

        if (openSaleTransaction == null)
            // Transaction id does not identify a started and open transaction.
            return false;

        TicketEntry entry = new TicketEntryClass(productType.getBarCode(), productType.getProductDescription(), amount,
                productType.getPricePerUnit());

        // Update TEMOPORARY productType quantity
        for (ProductType product : productTypeLocalList.values()) {
            if (product.getBarCode().equals(productCode)) {
                product.setQuantity(product.getQuantity() - amount);
            }
        }

        // Add product to sale
        List<TicketEntry> entries = openSaleTransaction.getEntries();
        if (entries.add(entry)) {
            // Update the price of the sale
            openSaleTransaction.setPrice(openSaleTransaction.getPrice() + productType.getPricePerUnit()  * amount);
            // The GUI update already the temporary quantity of the product.
            openSaleTransaction.setEntries(entries);
            return true;
        } else {
            // Product not added to SaleTransaction TicketEntry list
            return false;
        }
    }

    @Override
    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        if (openSaleTransaction == null)
            // Transaction id does not identify a started and open transaction.
            return false;

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        if(RFID == null || RFID.isEmpty() || !RFID.matches("^\\d{10}$") || Integer.parseInt(RFID)<0 )
            throw new InvalidRFIDException();

        ProductType productType;

        String productCode = DBAPIs.getBarcodeFromRFID(RFID);

        // ProductType check
        if (!(productCode == null || productCode == "")) {
            try{
                productType = getProductTypeByBarCode(productCode);
                if (productType == null) {
                    // ProductCode not correspond to an existing ProductType
                    return false;
                }
            }catch(InvalidProductCodeException ex){
                // ProductCode not correspond to an existing ProductType
                return false;
            }
        } else {
            // RFID not valid
            return false;
        }

        for (ProductType product : productTypeLocalList.values()) 
            if (product.getBarCode().equals(productCode)) 
                if(product.getQuantity() < 1)
                    // ProductType quantity cannot satisfy the request
                    throw new InvalidQuantityException();   

        TicketEntry entry = new TicketEntryClass(productType.getBarCode(), productType.getProductDescription(), 1,
                productType.getPricePerUnit());

        // Update TEMOPORARY productType quantity
        for (ProductType product : productTypeLocalList.values()) {
            if (product.getBarCode().equals(productCode)) {
                product.setQuantity(product.getQuantity() - 1);
            }
        }

        // Add product to sale
        List<TicketEntry> entries = openSaleTransaction.getEntries();
        if (entries.add(entry)) {
            // Update the price of the sale
            openSaleTransaction.setPrice(openSaleTransaction.getPrice() + productType.getPricePerUnit());
            // The GUI update already the temporary quantity of the product.
            openSaleTransaction.setEntries(entries);
            // Add to rfid list
            RFIDLocalList.put(RFID, productCode);
            return true;
        } else {
            // Product not added to SaleTransaction TicketEntry list
            return false;
        }
            
    }
    

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
            throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
            UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        // Invalid amount check
        if (amount <= 0)
            throw new InvalidQuantityException();

        ProductType productType;

        // ProductType check
        if (!(productCode == null || productCode == "")) {
            productType = getProductTypeByBarCode(productCode);
            if (productType == null)
                // ProductCode not correspond to an existing ProductType
                return false;
        } else {
            // ProductCode not valid
            throw new InvalidProductCodeException();
        }

        if (openSaleTransaction == null)
            // Transaction id does not identify a started and open transaction.
            return false;

        for (TicketEntry entry : openSaleTransaction.getEntries()) {
            if (entry.getBarCode().equals(productType.getBarCode())) {

                int entryAmount = entry.getAmount();
                if (entryAmount < amount)
                    return false;

                // Update the price of the sale
                openSaleTransaction.setPrice(openSaleTransaction.getPrice() - (productType.getPricePerUnit() * amount));

                // Update TEMOPORARY productType quantity
                for (ProductType product : productTypeLocalList.values()) {
                    if (product.getBarCode().equals(productCode)) {
                        product.setQuantity(product.getQuantity() + amount);
                    }
                }

                if (entryAmount == amount) {
                    // Remove object from the list
                    openSaleTransaction.getEntries().remove(entry);
                    return true;
                } else {
                    // Update product in sale quantity
                    entry.setAmount(entryAmount - amount);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        // Check RFID
        if(RFID == null || RFID.isEmpty() || !RFID.matches("^\\d{10}$") || Integer.parseInt(RFID)<0 )
            throw new InvalidRFIDException();

        ProductType productType;

        String productCode = DBAPIs.getBarcodeFromRFID(RFID);

        // ProductType check
        if (!(productCode == null || productCode == "")) {
            try{
                productType = getProductTypeByBarCode(productCode);
                if (productType == null) {
                    // ProductCode not correspond to an existing ProductType
                    return false;
                }
            }catch(InvalidProductCodeException ex){
                // ProductCode not correspond to an existing ProductType
                return false;
            }
        } else {
            // RFID not valid
            return false;
        }

        if (openSaleTransaction == null)
            // Transaction id does not identify a started and open transaction.
            return false;

        for (TicketEntry entry : openSaleTransaction.getEntries()) {
            if (entry.getBarCode().equals(productType.getBarCode())) {

                int entryAmount = entry.getAmount();

                // Update the price of the sale
                openSaleTransaction.setPrice(openSaleTransaction.getPrice() - productType.getPricePerUnit());

                // Update TEMOPORARY productType quantity
                for (ProductType product : productTypeLocalList.values()) {
                    if (product.getBarCode().equals(productCode)) {
                        product.setQuantity(product.getQuantity() + 1);
                    }
                }

                if (entryAmount == 1) {
                    // Remove object from the list
                    openSaleTransaction.getEntries().remove(entry);
                    RFIDLocalList.remove(RFID);
                    return true;
                } else {
                    // Update product in sale quantity
                    entry.setAmount(entryAmount - 1);
                    RFIDLocalList.remove(RFID);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
            throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException,
            UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        // Invalid DiscountRate check
        if (discountRate <= 0 || discountRate >= 1.00)
            throw new InvalidDiscountRateException();

        ProductType productType;

        // ProductType check
        if (!(productCode == null || productCode == "")) {
            productType = getProductTypeByBarCode(productCode);
            if (productType == null)
                // ProductCode not correspond to an existing ProductType
                return false;
        } else {
            // ProductCode not valid
            throw new InvalidProductCodeException();
        }

        if (openSaleTransaction == null)
            // Transaction id does not identify a started and open transaction.
            return false;

        for (TicketEntry entry : openSaleTransaction.getEntries()) {
            if (entry.getBarCode() == productType.getBarCode()) {
                entry.setDiscountRate(discountRate);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
            throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        // Invalid DiscountRate check
        if (discountRate <= 0 || discountRate >= 1.00)
            throw new InvalidDiscountRateException();

        if (openSaleTransaction == null) {
            // Check valid transactionId
            if (getSaleTransaction(transactionId) == null)
                return false;

            if (!DBAPIs.updateClosedSaleTransactionDiscountRate(transactionId, discountRate))
                return false;
            else {
                return true;
            }
        }
        openSaleTransaction.setDiscountRate(discountRate);
        return true;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        if (openSaleTransaction == null) {
            openSaleTransaction = DBAPIs.getClosedSaleTransaction(transactionId);
            if (openSaleTransaction == null)
                return -1;
        }
        return (int) openSaleTransaction.getPrice() / 10;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId <= 0 || transactionId == null)
            throw new InvalidTransactionIdException();

        if (openSaleTransaction == null)
            // Transaction does not exists or Transaction already closed
            return false;

        // Check if transaction has already been closed
        if (DBAPIs.getClosedSaleTransaction(transactionId) != null)
            return false;

        // Set discount price
        Double tmp = 0.0;
        for (TicketEntry entry: openSaleTransaction.getEntries()) {
            tmp += (entry.getPricePerUnit() * (1-entry.getDiscountRate())) * entry.getAmount();
        }
        openSaleTransaction.setPrice(tmp * (1-openSaleTransaction.getDiscountRate()));

        // Add to CloseSaleTransaction. If something goes wrong return false;
        if (!DBAPIs.createClosedSaleTransaction(openSaleTransaction.getTicketNumber(),
                openSaleTransaction.getDiscountRate(), openSaleTransaction.getPrice()))
            return false;

        // ADD TicketeEntries to DB
        for (TicketEntry entry : openSaleTransaction.getEntries()) {
            DBAPIs.createTicketEntry(transactionId, entry);
        }

        // Update productType quantity to DB
        for (ProductType product : DBAPIs.getAllProductTypes()) {
            int localQuantity = productTypeLocalList.get(product.getId()).getQuantity();
            if (localQuantity != product.getQuantity()) {
                // If quantities are not aligned, modify database
                DBAPIs.updateProductTypeQuantity(product.getId(), localQuantity);
            }
        }

        openSaleTransaction = null;
        for (String rfid: RFIDLocalList.keySet()) {
            DBAPIs.updateRFIDSold(rfid, true);
        }
        RFIDLocalList = new Hashtable<>();
        
        return true;
    }

    @Override
    public boolean deleteSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        // ClosedSaleTransaction has been payed
        if (DBAPIs.getClosedSaleTransactionPayed(transactionId))
            return false;

        // Delete ClosedSaleTransaction Entries and update productType quantity
        for (TicketEntry entry : DBAPIs.getSaleTicketEntries(transactionId)) {
            ProductType product = DBAPIs.getProductTypeFromBarcode(entry.getBarCode());
            DBAPIs.updateProductTypeQuantity(product.getId(), product.getQuantity() + entry.getAmount());
            productTypeLocalList.get(product.getId()).setQuantity(product.getQuantity() + entry.getAmount());
            DBAPIs.deleteTicketEntry(transactionId, entry.getBarCode(), entry.getDiscountRate());
        }

        // Return true if the transaction it's been correctly deleted, false otherwise
        boolean flag = DBAPIs.deleteClosedSaleTransaction(transactionId);
        return flag;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        // return the SaleTransaction if exists, null otherwise
        return DBAPIs.getClosedSaleTransaction(transactionId);
    }

    @Override
    public Integer startReturnTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (transactionId == null || transactionId <= 0)
            throw new InvalidTransactionIdException();

        if (DBAPIs.getClosedSaleTransaction(transactionId) == null)
            // ClosedSaleTransaction doesn't exists
            return -1;

        openReturnSaleTransaction = new SaleTransactionClass();
        // Le direttive richiedono un id >=0 ma in addProduct considerano un id >0. !!!!
        // Si sceglie quindi di non avere un id<=0

        openReturnSaleTransaction.setTicketNumber(transactionId);

        return openReturnSaleTransaction.getTicketNumber();
    }

    // It's possible to have in the chart the same product with different
    // discountRate, so in order to return the
    // correct product it's necessary to pass the discountRate as a parameter.
    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException,
            InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        // Invalid amount check
        if (amount <= 0)
            // LE DIRETTIVE ACCETTANO UNA QUANTITA' = 0 !!!!
            throw new InvalidQuantityException();

        ProductType productType;

        // ProductType check
        if (!(productCode == null || productCode == "")) {
            productType = getProductTypeByBarCode(productCode);
            if (productType == null)
                // ProductCode not correspond to an existing ProductType
                return false;
        } else {
            // ProductCode not valid
            throw new InvalidProductCodeException();
        }

        TicketEntry entry = DBAPIs.getSaleTicketEntry(returnId, productCode);

        if (entry == null)
            // product not in the transaction
            return false;

        if (entry.getAmount() < amount)
            return false;

        TicketEntry returnEntry = new TicketEntryClass(productCode, entry.getProductDescription(), amount,
                entry.getPricePerUnit());
        returnEntry.setDiscountRate(entry.getDiscountRate());

        List<TicketEntry> entries = openReturnSaleTransaction.getEntries();
        entries.add(returnEntry);

        return true;
    }

    @Override
    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException 
    {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        if(RFID == null || RFID.isEmpty() || !RFID.matches("^\\d{10}$") || Integer.parseInt(RFID)<0 )
            throw new InvalidRFIDException();

        if(RFIDReturnLocalList.get(RFID)!=null)
        //RFID already added to return transaction
        return false;

        ProductType productType;

        String productCode = DBAPIs.getBarcodeFromRFID(RFID);

        // ProductType check
        if (!(productCode == null || productCode == "")) {
            try{
                productType = getProductTypeByBarCode(productCode);
                if (productType == null) {
                    // ProductCode not correspond to an existing ProductType
                    return false;
                }
            }catch(InvalidProductCodeException ex){
                // ProductCode not correspond to an existing ProductType
                return false;
            }
        } else {
            // RFID not valid
            return false;
        }

        TicketEntry entry = DBAPIs.getSaleTicketEntry(returnId, productCode);

        if (entry == null)
            // product not in the transaction
            return false;

        if (entry.getAmount() < 1)
            return false;

        TicketEntry returnEntry = new TicketEntryClass(productCode, entry.getProductDescription(), 1,
                entry.getPricePerUnit());
        returnEntry.setDiscountRate(entry.getDiscountRate());

        List<TicketEntry> entries = openReturnSaleTransaction.getEntries();
        entries.add(returnEntry);

        RFIDReturnLocalList.put(RFID, productCode);
        return true;
    }


    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit)
            throws InvalidTransactionIdException, UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        if (openReturnSaleTransaction.getTicketNumber() != returnId)
            return false;

        if (commit) {
            double returnAmount = 0;
            for (TicketEntry entry : openReturnSaleTransaction.getEntries()) {
                ProductType product = DBAPIs.getProductTypeFromBarcode(entry.getBarCode());
                // Update local quantity ?
                DBAPIs.updateProductTypeQuantity(product.getId(), product.getQuantity() + entry.getAmount());

                returnAmount += (entry.getPricePerUnit() * (1 - entry.getDiscountRate())) * entry.getAmount();

                entry.setAmount(-entry.getAmount());
            }

            SaleTransaction closeSaleTransaction = DBAPIs.getClosedSaleTransaction(returnId);

            // returnAmount is a negative integer
            DBAPIs.updateClosedSaleTransactionPrice(returnId, closeSaleTransaction.getPrice() - returnAmount);

            // If all products are been returned, delete ClosedSaleTransaction and all it's
            // TicketEntries
            if (closeSaleTransaction.getPrice() == 0) {
                // Delete all ticketEntries
                for (TicketEntry ticket : DBAPIs.getSaleTicketEntries(returnId)) {
                    DBAPIs.deleteTicketEntry(returnId, ticket.getBarCode(), ticket.getDiscountRate());
                }
                // Delete ClosedSaleTransaction
                DBAPIs.deleteClosedSaleTransaction(returnId);

            } else { // Not all product of the ClosedSaleTransaction are been returned
                // Update ClosedSale entries
                for (TicketEntry entry : openReturnSaleTransaction.getEntries()) {
                    TicketEntry closedSaleEntry = DBAPIs.getSaleTicketEntry(returnId, entry.getBarCode());
                    if (closedSaleEntry.getAmount() == -entry.getAmount())
                        // All product of the TicketEntry are been returned
                        DBAPIs.deleteTicketEntry(returnId, entry.getBarCode(), entry.getDiscountRate());
                    else
                        // Not all product are been returned
                        DBAPIs.updateTicketEntryAmount(returnId, entry.getBarCode(),
                                closedSaleEntry.getAmount() + entry.getAmount(), entry.getDiscountRate());
                }
            }

            openReturnSaleTransaction.setPrice(returnAmount);

            int newId = DBAPIs.getReturnSaleTransactionCount() + 1;

            DBAPIs.createReturnSaleTransaction(newId, returnId, -returnAmount, closeSaleTransaction.getDiscountRate());

            for (TicketEntry entry : openReturnSaleTransaction.getEntries()) {
                DBAPIs.createReturnTicketEntry(returnId, entry, newId);
            }

            openReturnSaleTransaction = null;
            
            for(String RFID : RFIDReturnLocalList.keySet())
                DBAPIs.updateRFIDSold(RFID, false);
            
            RFIDReturnLocalList = new Hashtable<>();
        }
        return true;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId)
            throws InvalidTransactionIdException, UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        SaleTransaction returnSale = DBAPIs.getReturnTransaction(returnId);
        if (returnSale == null || DBAPIs.getReturnSaleTransactionPayed(returnId))
            return false;

        TicketEntry closedSaleEntry = null;
        Double price = 0.0;

        int lastId = DBAPIs.getReturnSaleTransactionCount();

        for (TicketEntry returnEntry : DBAPIs.getReturnTicketEntries(returnId, lastId)) {
            closedSaleEntry = DBAPIs.getSaleTicketEntry(returnId, returnEntry.getBarCode());
            if (closedSaleEntry != null)
                DBAPIs.updateTicketEntryAmount(returnId, returnEntry.getBarCode(),
                        closedSaleEntry.getAmount() - returnEntry.getAmount(), returnEntry.getDiscountRate());
            else {
                // If all the products of the transaction was returned
                returnEntry.setAmount(-returnEntry.getAmount());
                DBAPIs.createTicketEntry(returnId, returnEntry);
            }
            // price -= returnEntry.getAmount()
            price -= returnEntry.getPricePerUnit() * returnEntry.getAmount();
        }
        if (closedSaleEntry == null) {
            // If all the products of the transaction was returned
            DBAPIs.createClosedSaleTransaction(returnId, returnSale.getDiscountRate(), price);
        } else
            DBAPIs.updateClosedSaleTransactionPrice(returnId, price);

        return true;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash)
            throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (ticketNumber == null || ticketNumber <= 0)
            throw new InvalidTransactionIdException();

        // Invalid Payment check
        if (cash <= 0)
            throw new InvalidPaymentException();

        SaleTransaction sale;
        sale = DBAPIs.getClosedSaleTransaction(ticketNumber);

        if (sale == null)
            return -1;

        double salePrice = 0;

        // Apply productDiscountRate
        for (TicketEntry entry : DBAPIs.getSaleTicketEntries(ticketNumber)) {
            salePrice += (entry.getPricePerUnit() * (1 - entry.getDiscountRate())) * entry.getAmount();
        }

        if (salePrice * (1 - sale.getDiscountRate()) > cash) {
            return -1;
        }

        DBAPIs.updateClosedSaleTransactionPayed(ticketNumber, true);

        recordBalanceUpdate(salePrice * (1 - sale.getDiscountRate()));

        // Update SaleTransaction price, appling productsDiscountRate
        sale.setPrice(salePrice * (1 - sale.getDiscountRate()));
        DBAPIs.updateClosedSaleTransactionPrice(ticketNumber, salePrice * (1 - sale.getDiscountRate()));

        // return change
        return cash - (salePrice * (1 - sale.getDiscountRate()));
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (ticketNumber == null || ticketNumber <= 0 )
            throw new InvalidTransactionIdException();

        // CreditCard check
        if (creditCard == null)
            throw new InvalidCreditCardException();

        // CreditCard check
        if (creditCard.isEmpty())
            throw new InvalidCreditCardException();

        // LUHN ALGORITHM
        if(!LuhnAlgorithm.chackCreditCard(creditCard))
            throw new InvalidCreditCardException();

        double salePrice = 0;

        // Apply productDiscountRate
        for (TicketEntry entry : DBAPIs.getSaleTicketEntries(ticketNumber)) {
            salePrice += (entry.getPricePerUnit() * (1 - entry.getDiscountRate())) * entry.getAmount();
        }

        // Check if the Credit Card is registered and it's cash is enough
        if (!CreditCard.checkCreditCard(creditCard, salePrice)) {
            return false;
        }

        SaleTransaction sale = DBAPIs.getClosedSaleTransaction(ticketNumber);

        recordBalanceUpdate(salePrice * (1 - sale.getDiscountRate()));

        return true;

    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        if (openReturnSaleTransaction != null)
            // ReturnTransaction not ended
            return -1;

        SaleTransaction returnTransaction = DBAPIs.getReturnTransaction(returnId);
        if (returnTransaction == null)
            // ReturnTransaction does't exists
            return -1;

        DBAPIs.updateReturnSaleTransactionPayed(returnId, true);

        double returnPrice = 0;

        int lastId = DBAPIs.getReturnSaleTransactionCount();

        // Apply productDiscountRate
        for (TicketEntry entry : DBAPIs.getReturnTicketEntries(returnId, lastId)) {
            returnPrice += (entry.getPricePerUnit() * (1 - entry.getDiscountRate())) * entry.getAmount();
        }
        recordBalanceUpdate(returnPrice * (1 - returnTransaction.getDiscountRate()));

        return -(returnPrice * (1 - returnTransaction.getDiscountRate()));
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {

        // Check for logged user
        if (Objects.isNull(loggedUser))
            throw new UnauthorizedException();

        // Invalid transactionId check
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        // CreditCard check
        if (creditCard == null || creditCard.isEmpty())
            throw new InvalidCreditCardException();

        // LUHN ALGORITHM
        if(!LuhnAlgorithm.chackCreditCard(creditCard))
            throw new InvalidCreditCardException();

        if (!CreditCard.checkExistCreditCard(creditCard)) {
            // CreditCard not registered
            return -1;
        }

        SaleTransaction returnTransaction = DBAPIs.getReturnTransaction(returnId);

        double returnPrice = 0;

        int lastId = DBAPIs.getReturnSaleTransactionCount();

        // Apply productDiscountRate
        for (TicketEntry entry : DBAPIs.getReturnTicketEntries(returnId, lastId)) {
            returnPrice += (entry.getPricePerUnit() * (1 - entry.getDiscountRate())) * entry.getAmount();
        }

        recordBalanceUpdate(returnPrice * (1 - returnTransaction.getDiscountRate()));

        return -(returnPrice * (1 - returnTransaction.getDiscountRate()));
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();

        if(computeBalance() + toBeAdded < 0)
            return false;

        String type = (toBeAdded >= 0) ? "CREDIT" : "DEBIT";
        int id = DBAPIs.getBalanceOperationCount() + 1;

        BalanceOperation newBalanceOperation = new BalanceOperationClass(id, LocalDate.now(), toBeAdded, type);

        return DBAPIs.createBalanceOperation(newBalanceOperation);
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager"))) {
            throw new UnauthorizedException();
        }

        if (Objects.isNull(from) && Objects.isNull(to))
            return DBAPIs.getAllBalanceOperations();

        if (Objects.isNull(to))
            return DBAPIs.getAllBalanceOperations().stream()
                    .filter((BalanceOperation b) -> !(b.getDate().isBefore(from))).collect(Collectors.toList());

        if (Objects.isNull(from))
            return DBAPIs.getAllBalanceOperations().stream()
                .filter((BalanceOperation b) -> !(b.getDate().isAfter(to))).collect(Collectors.toList());

        return DBAPIs.getAllBalanceOperations().stream()
                .filter((BalanceOperation b) -> !(b.getDate().isBefore(from) || b.getDate().isAfter(to)))
                .collect(Collectors.toList());

    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        // Check for logged user
        if (Objects.isNull(loggedUser)
                || !(loggedUser.getRole().equals("Administrator") || loggedUser.getRole().equals("ShopManager")))
            throw new UnauthorizedException();

        return DBAPIs.getAllBalanceOperations().stream().mapToDouble(x -> x.getMoney()).sum();
    }

}
