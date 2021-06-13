package it.polito.ezshop.acceptanceTests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.DB.DBAPIs;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

public class TestEZShop {
  EZShop shop = new EZShop();

  /**
   * Test UserClass
   */
  @Test
  public void testUserClass() {
    User user = new UserClass(1, "username", "password", "Administrator");
    assertNotNull("User should not be null", user);

    assertEquals(Integer.valueOf(1), user.getId());
    assertEquals("username", user.getUsername());
    assertEquals("password", user.getPassword());
    assertEquals("Administrator", user.getRole());

    user.setId(2);
    assertEquals(Integer.valueOf(2), user.getId());
    user.setUsername("test");
    assertEquals("test", user.getUsername());
    user.setPassword("test");
    assertEquals("test", user.getPassword());
    user.setRole("Cashier");
    assertEquals("Cashier", user.getRole());
  }

  /**
   * Test CustomerClass
   */
  @Test
  public void testCustomerClass() {
    Customer customer = new CustomerClass(1, "test");
    assertNotNull("Customer should not be null", customer);

    assertEquals(Integer.valueOf(1), customer.getId());
    assertEquals("test", customer.getCustomerName());

    customer.setId(2);
    assertEquals(Integer.valueOf(2), customer.getId());
    customer.setCustomerName("customerName");
    assertEquals("customerName", customer.getCustomerName());
    customer.setCustomerCard("123456789");
    assertEquals("123456789", customer.getCustomerCard());
    customer.setPoints(10);
    assertEquals(Integer.valueOf(10), customer.getPoints());
  }

  /**
   * Test BalanceOperationClass
   */
  @Test
  public void testBalanceOperationClass() {
    BalanceOperation balance = new BalanceOperationClass(1, LocalDate.now(), 10.0, "CREDIT");
    assertNotNull("Balance should not be null", balance);

    assertEquals(Integer.valueOf(1), Integer.valueOf(balance.getBalanceId()));
    assertEquals(LocalDate.now(), balance.getDate());
    assertEquals(Double.valueOf(10.0), Double.valueOf(balance.getMoney()));
    assertEquals("CREDIT", balance.getType());

    balance.setBalanceId(2);
    assertEquals(Integer.valueOf(2), Integer.valueOf(balance.getBalanceId()));
    balance.setDate(LocalDate.of(2021, 5, 2));
    assertEquals(LocalDate.of(2021, 5, 2), balance.getDate());
    balance.setMoney(20.0);
    assertEquals(Double.valueOf(20.0), Double.valueOf(balance.getMoney()));
    balance.setType("DEBIT");
    assertEquals("DEBIT", balance.getType());
  }

  /**
   * Test OrderClass
   */
  @Test
  public void testOrderClass() {
    Order order = new OrderClass(1, 1, "123456", 10.0, 2, "ISSUED");
    assertNotNull("Order should not be null", order);

    assertEquals(Integer.valueOf(1), order.getOrderId());
    assertEquals(Integer.valueOf(1), order.getBalanceId());
    assertEquals("123456", order.getProductCode());
    assertEquals(Double.valueOf(10.0), Double.valueOf(order.getPricePerUnit()));
    assertEquals(Integer.valueOf(2), Integer.valueOf(order.getQuantity()));
    assertEquals("ISSUED", order.getStatus());

    order.setOrderId(2);
    assertEquals(Integer.valueOf(2), Integer.valueOf(order.getOrderId()));
    order.setBalanceId(2);
    assertEquals(Integer.valueOf(2), order.getBalanceId());
    order.setProductCode("234578");
    assertEquals("234578", order.getProductCode());
    order.setPricePerUnit(20.0);
    assertEquals(Double.valueOf(20.0), Double.valueOf(order.getPricePerUnit()));
    order.setQuantity(4);
    assertEquals(Integer.valueOf(4), Integer.valueOf(order.getQuantity()));
    order.setStatus("ORDERED");
    assertEquals("ORDERED", order.getStatus());
  }

  /**
   * Test ProductTypeClass
   */
  @Test
  public void testProductTypeClass() {
    ProductType product = new ProductTypeClass("test", "test", "123456", 10.0, 1);
    assertNotNull("Product should not be null", product);

    assertEquals(Integer.valueOf(1), product.getId());
    assertEquals("test", product.getNote());
    assertEquals("test", product.getProductDescription());
    assertEquals("123456", product.getBarCode());
    assertEquals(Double.valueOf(10.0), product.getPricePerUnit());

    product.setId(2);
    assertEquals(Integer.valueOf(2), product.getId());
    product.setNote("note");
    assertEquals("note", product.getNote());
    product.setProductDescription("desc");
    assertEquals("desc", product.getProductDescription());
    product.setBarCode("234567");
    assertEquals("234567", product.getBarCode());
    product.setPricePerUnit(20.0);
    assertEquals(Double.valueOf(20.0), product.getPricePerUnit());
    product.setQuantity(10);
    assertEquals(Integer.valueOf(10), product.getQuantity());
    product.setLocation("TEST");
    assertEquals("TEST", product.getLocation());
  }

  /**
   * Test SaleTransactionClass
   */
  @Test
  public void testSaleTransactionClass() {
    SaleTransaction sale = new SaleTransactionClass();
    assertNotNull("Sale should not be null", sale);

    sale.setTicketNumber(1);
    assertEquals(Integer.valueOf(1), sale.getTicketNumber());
    sale.setDiscountRate(20.0);
    assertEquals(Double.valueOf(20.0), Double.valueOf(sale.getDiscountRate()));
    sale.setPrice(10.0);
    assertEquals(Double.valueOf(10.0), Double.valueOf(sale.getPrice()));

    TicketEntry ticket = new TicketEntryClass("12345", "test", 5, 10.0);
    assertNotNull("Ticket should not be null", ticket);

    List<TicketEntry> entries = new ArrayList<>();
    entries.add(ticket);

    sale.setEntries(entries);
    assertFalse("Ticket list should not be empty", sale.getEntries().isEmpty());
  }

  /**
   * Test TicketEntryClass
   */
  @Test
  public void testTicketEntryClass() {
    TicketEntry ticket = new TicketEntryClass("12345", "test", 5, 10.0);
    assertNotNull("Ticket should not be null", ticket);

    assertEquals("12345", ticket.getBarCode());
    assertEquals("test", ticket.getProductDescription());
    assertEquals(Integer.valueOf(5), Integer.valueOf(ticket.getAmount()));
    assertEquals(Double.valueOf(10.0), Double.valueOf(ticket.getPricePerUnit()));

    ticket.setBarCode("234567");
    assertEquals("234567", ticket.getBarCode());
    ticket.setProductDescription("test");
    assertEquals("test", ticket.getProductDescription());
    ticket.setAmount(10);
    assertEquals(Integer.valueOf(10), Integer.valueOf(ticket.getAmount()));
    ticket.setPricePerUnit(20.0);
    assertEquals(Double.valueOf(20.0), Double.valueOf(ticket.getPricePerUnit()));
    ticket.setDiscountRate(20.0);
    assertEquals(Double.valueOf(20.0), Double.valueOf(ticket.getDiscountRate()));
  }

  /**
   * Test DBAPIs Closed Sale Transaction
   */
  @Test
  public void testDBAPIsClosedSaleTransaction() {
    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    /**
     * Closed Sale Transaction
     */

    // createClosedSaleTransaction
    Integer ticketNumber = 0;
    Boolean addSaleTransaction = DBAPIs.createClosedSaleTransaction(ticketNumber, 10.0, 10.0);
    assertTrue(addSaleTransaction);
    addSaleTransaction = DBAPIs.createClosedSaleTransaction(0, 10.0, 10.0);
    assertFalse(addSaleTransaction);
    addSaleTransaction = DBAPIs.createClosedSaleTransaction(null, 10.0, 10.0);
    assertNull(addSaleTransaction);

    // getClosedSaleTransaction
    SaleTransaction closedSaleTransaction = DBAPIs.getClosedSaleTransaction(ticketNumber);
    assertNotNull(closedSaleTransaction);
    closedSaleTransaction = DBAPIs.getClosedSaleTransaction(-1);
    assertNull(closedSaleTransaction);
    closedSaleTransaction = DBAPIs.getClosedSaleTransaction(null);
    assertNull(closedSaleTransaction);

    // updateClosedSaleTransactionPayed
    Boolean updateSaleTransaction = DBAPIs.updateClosedSaleTransactionPayed(ticketNumber, true);
    assertTrue(updateSaleTransaction);
    updateSaleTransaction = DBAPIs.updateClosedSaleTransactionPayed(-1, true);
    assertFalse(updateSaleTransaction);
    updateSaleTransaction = DBAPIs.updateClosedSaleTransactionPayed(null, true);
    assertNull(updateSaleTransaction);

    // getClosedSaleTransactionPayed
    Boolean payed = DBAPIs.getClosedSaleTransactionPayed(ticketNumber);
    assertTrue(payed);
    payed = DBAPIs.getClosedSaleTransactionPayed(-1);
    assertFalse(payed);
    payed = DBAPIs.getClosedSaleTransactionPayed(null);
    assertNull(payed);

    // updateClosedSaleTransactionDiscountRate
    updateSaleTransaction = DBAPIs.updateClosedSaleTransactionDiscountRate(ticketNumber, 20.0);
    assertTrue(updateSaleTransaction);
    updateSaleTransaction = DBAPIs.updateClosedSaleTransactionDiscountRate(-1, 20.0);
    assertFalse(updateSaleTransaction);
    updateSaleTransaction = DBAPIs.updateClosedSaleTransactionDiscountRate(null, 20.0);
    assertNull(updateSaleTransaction);

    // updateClosedSaleTransactionPrice
    updateSaleTransaction = DBAPIs.updateClosedSaleTransactionPrice(ticketNumber, 20.0);
    assertTrue(updateSaleTransaction);
    updateSaleTransaction = DBAPIs.updateClosedSaleTransactionPrice(-1, 20.0);
    assertFalse(updateSaleTransaction);
    updateSaleTransaction = DBAPIs.updateClosedSaleTransactionPrice(null, 20.0);
    assertNull(updateSaleTransaction);

    // deleteClosedSaleTransaction
    updateSaleTransaction = DBAPIs.deleteClosedSaleTransaction(ticketNumber);
    assertTrue(updateSaleTransaction);
    updateSaleTransaction = DBAPIs.deleteClosedSaleTransaction(-1);
    assertFalse(updateSaleTransaction);
    updateSaleTransaction = DBAPIs.deleteClosedSaleTransaction(null);
    assertNull(updateSaleTransaction);

    // getClosedSaleTransactionCount
    Integer saleTransactionCount = DBAPIs.getClosedSaleTransactionCount();
    assertNotEquals(Integer.valueOf(-1), saleTransactionCount);

  }

  /**
   * Test DBAPIs Return Sale Transaction
   */
  @Test
  public void testDBAPIsReturnSaleTransaction() {
    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    /**
     * Return Sale Transaction
     */

    // getReturnSaleTransactionCount
    Integer countReturnSaleTransaction = DBAPIs.getReturnSaleTransactionCount();
    assertNotEquals(Integer.valueOf(-1), countReturnSaleTransaction);

    // createReturnSaleTransaction
    Integer ticketNumber = 0;
    countReturnSaleTransaction += 1;
    Boolean addReturnSaleTransaction = DBAPIs.createReturnSaleTransaction(countReturnSaleTransaction, ticketNumber,
        10.0, 10.0);
    assertTrue(addReturnSaleTransaction);
    addReturnSaleTransaction = DBAPIs.createReturnSaleTransaction(countReturnSaleTransaction, 0, 10.0, 10.0);
    assertFalse(addReturnSaleTransaction);
    addReturnSaleTransaction = DBAPIs.createReturnSaleTransaction(null, 0, 10.0, 10.0);
    assertNull(addReturnSaleTransaction);

    // getReturnTransaction
    SaleTransaction returnTransaction = DBAPIs.getReturnTransaction(ticketNumber);
    assertNotNull(returnTransaction);
    returnTransaction = DBAPIs.getReturnTransaction(-1);
    assertNull(returnTransaction);
    returnTransaction = DBAPIs.getReturnTransaction(null);
    assertNull(returnTransaction);

    // updateReturnSaleTransactionPayed
    Boolean updateReturnSaleTransaction = DBAPIs.updateReturnSaleTransactionPayed(ticketNumber, true);
    assertTrue(updateReturnSaleTransaction);
    updateReturnSaleTransaction = DBAPIs.updateReturnSaleTransactionPayed(-1, true);
    assertFalse(updateReturnSaleTransaction);
    updateReturnSaleTransaction = DBAPIs.updateReturnSaleTransactionPayed(null, true);
    assertNull(updateReturnSaleTransaction);

    // getReturnSaleTransactionPayed
    Boolean payed = DBAPIs.getReturnSaleTransactionPayed(ticketNumber);
    assertTrue(payed);
    payed = DBAPIs.getReturnSaleTransactionPayed(-1);
    assertFalse(payed);
    payed = DBAPIs.getReturnSaleTransactionPayed(null);
    assertNull(payed);
  }

  /**
   * Test DBAPIs TicketEntry
   */
  @Test
  public void testDBAPIsTicketEntry() {
    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    /**
     * Ticket Entry
     */

    // addTicketEntry
    Integer ticketNumber = 0;
    TicketEntry ticket = new TicketEntryClass("12345", "test", 5, 10.0);
    assertNotNull("Ticket should not be null", ticket);

    // createTicketEntry
    Boolean addTicketEntry = DBAPIs.createTicketEntry(ticketNumber, ticket);
    assertTrue(addTicketEntry);
    addTicketEntry = DBAPIs.createTicketEntry(ticketNumber, ticket);
    assertFalse(addTicketEntry);
    addTicketEntry = DBAPIs.createTicketEntry(null, ticket);
    assertNull(addTicketEntry);

    // getSaleTicketEntry
    TicketEntry ticketEntry = DBAPIs.getSaleTicketEntry(ticketNumber, "12345");
    assertNotNull(ticketEntry);
    ticketEntry = DBAPIs.getSaleTicketEntry(null, "12345");
    assertNull(ticketEntry);

    // updateTicketEntryAmount
    Boolean updateTicketEntryAmount = DBAPIs.updateTicketEntryAmount(ticketNumber, "12345", 10, 10.0);
    assertTrue(updateTicketEntryAmount);
    updateTicketEntryAmount = DBAPIs.updateTicketEntryAmount(-1, "12345", 10, 10.0);
    assertFalse(updateTicketEntryAmount);
    updateTicketEntryAmount = DBAPIs.updateTicketEntryAmount(null, "12345", 10, 10.0);
    assertNull(updateTicketEntryAmount);

    // getSaleTicketEntries
    List<TicketEntry> entries = DBAPIs.getSaleTicketEntries(ticketNumber);
    assertFalse("Ticket list should not be empty", entries.isEmpty());
    entries = DBAPIs.getSaleTicketEntries(null);
    assertNull("Ticket list should not be null", entries);

    // deleteTicketEntry
    Boolean deleteTicketEntry = DBAPIs.deleteTicketEntry(ticketNumber, "12345", 10.0);
    assertTrue(deleteTicketEntry);
    deleteTicketEntry = DBAPIs.deleteTicketEntry(-1, "12345", 10.0);
    assertFalse(deleteTicketEntry);
    deleteTicketEntry = DBAPIs.deleteTicketEntry(null, "12345", 10.0);
    assertNull(deleteTicketEntry);
  }

  /**
   * Test DBAPIs Return TicketEntry
   */
  @Test
  public void testDBAPIsReturnTicketEntry() {
    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    /**
     * Return Ticket Entry
     */

    // addReturnTicketEntry
    Integer ticketNumber = 0;
    TicketEntry ticket = new TicketEntryClass("12345", "test", 5, 10.0);
    assertNotNull("Ticket should not be null", ticket);

    Boolean addReturnTicketEntry = DBAPIs.createReturnTicketEntry(ticketNumber, ticket, 0);
    assertTrue(addReturnTicketEntry);
    addReturnTicketEntry = DBAPIs.createReturnTicketEntry(ticketNumber, ticket, 0);
    assertFalse(addReturnTicketEntry);
    addReturnTicketEntry = DBAPIs.createReturnTicketEntry(null, ticket, 0);
    assertNull(addReturnTicketEntry);

    // getReturnTicketEntry
    TicketEntry returnTicketEntry = DBAPIs.getReturnTicketEntry(ticketNumber, "12345", 0);
    assertNotNull(returnTicketEntry);
    returnTicketEntry = DBAPIs.getReturnTicketEntry(-1, "12345", 0);
    assertNull(returnTicketEntry);
    returnTicketEntry = DBAPIs.getReturnTicketEntry(null, "12345", 0);
    assertNull(returnTicketEntry);

    // getReturnTicketEntries
    List<TicketEntry> entries = DBAPIs.getReturnTicketEntries(ticketNumber, 0);
    assertFalse("Ticket list should not be empty", entries.isEmpty());
    entries = DBAPIs.getReturnTicketEntries(null, 0);
    assertNull("Ticket list should not be empty", entries);
  }

  /**
   * Test DBAPIs ProductType
   */
  @Test
  public void testDBAPIsProductType() {
    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    /**
     * Product Type
     */

    // createProductType
    Integer productId = 0;
    ProductTypeClass productType = new ProductTypeClass("test", "test", "123456", 10.0, productId);
    assertNotNull(productType);

    Boolean createProductType = DBAPIs.createProductType(productType);
    assertTrue(createProductType);
    createProductType = DBAPIs.createProductType(productType);
    assertFalse(createProductType);
    createProductType = DBAPIs.createProductType(null);
    assertNull(createProductType);

    // getProductTypeCount
    Integer countProductType = DBAPIs.getProductTypeCount();
    assertNotEquals(Integer.valueOf(-1), countProductType);

    // getProductTypeFromId
    ProductType getProductType = DBAPIs.getProductTypeFromId(productId);
    assertNotNull(getProductType);
    getProductType = DBAPIs.getProductTypeFromId(-1);
    assertNull(getProductType);
    getProductType = DBAPIs.getProductTypeFromId(null);
    assertNull(getProductType);

    // updateProductType
    Boolean updateProductType = DBAPIs.updateProductType(productId, "newDescription", "234567", 20.0, "newNote");
    assertTrue(updateProductType);
    updateProductType = DBAPIs.updateProductType(-1, "newDescription", "234567", 20.0, "newNote");
    assertFalse(updateProductType);
    updateProductType = DBAPIs.updateProductType(null, "newDescription", "234567", 20.0, "newNote");
    assertNull(updateProductType);

    // getAllProductTypes
    List<ProductType> productTypeEntries = DBAPIs.getAllProductTypes();
    assertFalse("Product entries list should not be empty", productTypeEntries.isEmpty());

    // getProductTypeFromBarcode
    getProductType = DBAPIs.getProductTypeFromBarcode("234567");
    assertNotNull(getProductType);
    getProductType = DBAPIs.getProductTypeFromBarcode("");
    assertNull(getProductType);
    getProductType = DBAPIs.getProductTypeFromBarcode(null);
    assertNull(getProductType);

    // updateProductTypeQuantity
    updateProductType = DBAPIs.updateProductTypeQuantity(productId, 20);
    assertTrue(updateProductType);
    updateProductType = DBAPIs.updateProductTypeQuantity(-1, 20);
    assertFalse(updateProductType);
    updateProductType = DBAPIs.updateProductTypeQuantity(null, 20);
    assertNull(updateProductType);

    // updateProductTypePosition
    updateProductType = DBAPIs.updateProductTypePosition(productId, "NEWP");
    assertTrue(updateProductType);
    updateProductType = DBAPIs.updateProductTypePosition(-1, "NEWP");
    assertFalse(updateProductType);
    updateProductType = DBAPIs.updateProductTypePosition(null, "NEWP");
    assertNull(updateProductType);

    // deleteProductType
    updateProductType = DBAPIs.deleteProductType(productId);
    assertTrue(updateProductType);
    updateProductType = DBAPIs.deleteProductType(-1);
    assertFalse(updateProductType);
    updateProductType = DBAPIs.deleteProductType(null);
    assertNull(updateProductType);
  }

  /**
   * Test DBAPIs Customer
   */
  @Test
  public void testDBAPIsCustomer() {
    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    /**
     * Customer
     */
    Integer customerId = 0;
    CustomerClass customer = new CustomerClass(customerId, "test");
    assertNotNull("Customer should not be null", customer);

    // createCustomer
    Boolean createCustomer = DBAPIs.createCustomer(customer);
    assertTrue(createCustomer);
    createCustomer = DBAPIs.createCustomer(customer);
    assertFalse(createCustomer);
    createCustomer = DBAPIs.createCustomer(null);
    assertNull(createCustomer);

    // getCustomerCount
    Integer countCustomer = DBAPIs.getCustomerCount();
    assertNotEquals(Integer.valueOf(-1), countCustomer);

    // getCustomerCardMax
    Integer maxCustomerCard = DBAPIs.getCustomerCardMax();
    assertNotEquals(Integer.valueOf(-1), maxCustomerCard);

    // getCustomerFromId
    Customer getCustomer = DBAPIs.getCustomerFromId(customerId);
    assertNotNull(getCustomer);
    getCustomer = DBAPIs.getCustomerFromId(-1);
    assertNull("Customer should not be null", getCustomer);
    getCustomer = DBAPIs.getCustomerFromId(null);
    assertNull("Customer should not be null", getCustomer);

    // getCustomerFromName
    getCustomer = DBAPIs.getCustomerFromName("test");
    assertNotNull(getCustomer);
    getCustomer = DBAPIs.getCustomerFromName("");
    assertNull("Customer should not be null", getCustomer);
    getCustomer = DBAPIs.getCustomerFromName(null);
    assertNull("Customer should not be null", getCustomer);

    // updateCustomer
    customer.setCustomerName("testName");
    customer.setCustomerCard("TEST");
    Boolean updateCustomer = DBAPIs.updateCustomer(customer);
    assertTrue(updateCustomer);
    customer.setId(-1);
    updateCustomer = DBAPIs.updateCustomer(customer);
    assertFalse(updateCustomer);
    updateCustomer = DBAPIs.updateCustomer(null);
    assertNull(updateCustomer);

    // updatePointsOnCard
    updateCustomer = DBAPIs.updatePointsOnCard("TEST", 10);
    assertTrue(updateCustomer);
    updateCustomer = DBAPIs.updatePointsOnCard("", 10);
    assertFalse(updateCustomer);
    updateCustomer = DBAPIs.updatePointsOnCard(null, 10);
    assertNull(updateCustomer);

    // getCustomerFromCard
    getCustomer = DBAPIs.getCustomerFromCard("TEST");
    assertNotNull("Customer should not be null", getCustomer);
    getCustomer = DBAPIs.getCustomerFromCard("");
    assertNull("Customer should not be null", getCustomer);
    getCustomer = DBAPIs.getCustomerFromCard(null);
    assertNull("Customer should not be null", getCustomer);

    // updateCustomerCard
    updateCustomer = DBAPIs.updateCustomerCard(customerId, "PROVA");
    assertTrue(updateCustomer);
    updateCustomer = DBAPIs.updateCustomerCard(-1, "PROVA");
    assertFalse(updateCustomer);
    updateCustomer = DBAPIs.updateCustomerCard(null, "PROVA");
    assertNull(updateCustomer);

    // getAllCustomers
    List<Customer> customerEntries = DBAPIs.getAllCustomers();
    assertFalse("Customer entries list should not be empty", customerEntries.isEmpty());

    // deleteCustomer
    updateCustomer = DBAPIs.deleteCustomer(customerId);
    assertTrue(updateCustomer);
    updateCustomer = DBAPIs.deleteCustomer(-1);
    assertFalse(updateCustomer);
    updateCustomer = DBAPIs.deleteCustomer(null);
    assertNull(updateCustomer);
  }

  /**
   * Test DBAPIs BalanceOperation
   */
  @Test
  public void testDBAPIsBalanceOperation() {
    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    /**
     * Balance Operation
     */
    Integer balanceId = 0;
    BalanceOperation balanceOp = new BalanceOperationClass(balanceId, LocalDate.now(), 20.0, "CREDIT");
    assertNotNull(balanceOp);

    // createBalanceOperation
    Boolean createBalanceOperation = DBAPIs.createBalanceOperation(balanceOp);
    assertTrue(createBalanceOperation);
    createBalanceOperation = DBAPIs.createBalanceOperation(balanceOp);
    assertFalse(createBalanceOperation);
    createBalanceOperation = DBAPIs.createBalanceOperation(null);
    assertNull(createBalanceOperation);

    Integer countBalanceOperation = DBAPIs.getBalanceOperationCount();
    assertNotEquals(Integer.valueOf(-1), countBalanceOperation);

    List<BalanceOperation> listBalanceOperation = DBAPIs.getAllBalanceOperations();
    assertFalse("List should not be empty", listBalanceOperation.isEmpty());
  }

  /**
   * Test DBAPIs User
   */
  @Test
  public void testDBAPIsUser() {
    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    /**
     * User
     */
    Integer userId = 0;
    UserClass newUser = new UserClass(userId, "username", "password", "Aministrator");
    assertNotNull(newUser);

    // createUser
    Boolean createUser = DBAPIs.createUser(newUser);
    assertTrue(createUser);
    createUser = DBAPIs.createUser(newUser);
    assertFalse(createUser);
    createUser = DBAPIs.createUser(null);
    assertNull(createUser);

    // getUserFromId
    User user = DBAPIs.getUserFromId(userId);
    assertNotNull(user);
    user = DBAPIs.getUserFromId(-1);
    assertNull(user);
    user = DBAPIs.getUserFromId(null);
    assertNull(user);

    // getUserFromUsername
    user = DBAPIs.getUserFromUsername("username");
    assertNotNull(user);
    user = DBAPIs.getUserFromUsername("");
    assertNull(user);
    user = DBAPIs.getUserFromUsername(null);
    assertNull(user);

    Integer countUser = DBAPIs.getUserCount();
    assertNotEquals(Integer.valueOf(-1), countUser);

    List<User> listUser = DBAPIs.getAllUsers();
    assertFalse("List should not be empty", listUser.isEmpty());

    // updateUserRights
    createUser = DBAPIs.updateUserRights(userId, "Cashier");
    assertTrue(createUser);
    createUser = DBAPIs.updateUserRights(-1, "Cashier");
    assertFalse(createUser);
    createUser = DBAPIs.updateUserRights(null, "Cashier");
    assertNull(createUser);

    // deleteUser
    Boolean deleteUser = DBAPIs.deleteUser(userId);
    assertTrue(deleteUser);
    deleteUser = DBAPIs.deleteUser(-1);
    assertFalse(deleteUser);
    deleteUser = DBAPIs.deleteUser(null);
    assertNull(deleteUser);
  }

  /**
   * Test DBAPIs Order
   */
  @Test
  public void testDBAPIsOrder() {
    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    /**
     * Order
     */
    Integer orderId = 0;
    OrderClass newOrder = new OrderClass(orderId, 1, "123456", 10.0, 2, "ISSUED");
    assertNotNull(newOrder);

    Boolean createOrder = DBAPIs.createOrder(newOrder);
    assertTrue(createOrder);
    createOrder = DBAPIs.createOrder(newOrder);
    assertFalse(createOrder);
    createOrder = DBAPIs.createOrder(null);
    assertNull(createOrder);

    Integer countOrder = DBAPIs.getOrderCount();
    assertNotEquals(Integer.valueOf(-1), countOrder);

    newOrder.setStatus("PAYED");
    Boolean updateOrder = DBAPIs.updateOrder(newOrder);
    assertTrue("Order should be updated", updateOrder);
    newOrder.setOrderId(-1);
    updateOrder = DBAPIs.updateOrder(newOrder);
    assertFalse(updateOrder);
    updateOrder = DBAPIs.updateOrder(null);
    assertNull("Order should not be null", updateOrder);

    // getOrderFromId
    Order order = DBAPIs.getOrderFromId(orderId);
    assertNotNull(order);
    order = DBAPIs.getOrderFromId(-1);
    assertNull(order);
    order = DBAPIs.getOrderFromId(null);
    assertNull(order);

    List<Order> listOrders = DBAPIs.getAllOrders();
    assertFalse("List should not be empty", listOrders.isEmpty());
  }

    /**
   * Test DBAPIs RFID
   */
  @Test
  public void testDBAPIsRFID() {
    String validRFID = "1111111111";
    String validBarcode = "111111111111";

    //CreateRFID
    assertTrue(DBAPIs.createRFID(validRFID, validBarcode));
    assertFalse(DBAPIs.createRFID(validRFID, validBarcode));
    assertNull(DBAPIs.createRFID(null, validBarcode));
    assertNull(DBAPIs.createRFID(validRFID, null));
    assertNull(DBAPIs.createRFID("", validBarcode));
    assertNull(DBAPIs.createRFID(validRFID, ""));

    //GetBarcodeFromRFID
    assertNotNull(DBAPIs.getBarcodeFromRFID(validRFID));
    assertNull(DBAPIs.getBarcodeFromRFID("9999999999"));
    assertNull(DBAPIs.getBarcodeFromRFID(null));
    assertNull(DBAPIs.getBarcodeFromRFID(""));

    //UpdateRFIDSold
    assertTrue(DBAPIs.updateRFIDSold(validRFID, true));
    assertFalse(DBAPIs.updateRFIDSold("9999999999", true));
    assertNull(DBAPIs.updateRFIDSold(null, true));
    assertNull(DBAPIs.updateRFIDSold("", true));
    assertNull(DBAPIs.updateRFIDSold(validRFID, null));

    //DeleteRFID
    assertTrue(DBAPIs.deleteRFID(validRFID));
    assertFalse(DBAPIs.deleteRFID("9999999999"));
    assertNull(DBAPIs.deleteRFID(null));
    assertNull(DBAPIs.deleteRFID(""));
  }

  @Test
  public void testReceiveCashPaymentLoop() {

    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    EZShop loopTest = new EZShop();

    try {
      loopTest.login("marco", "marco");

      SaleTransaction sale1 = new SaleTransactionClass();
      sale1.setTicketNumber(1);
      List<TicketEntry> list1 = new ArrayList<>();
      TicketEntry entry1 = new TicketEntryClass("1", "1", 1, 1.0);
      list1.add(entry1);
      sale1.setEntries(list1);
      DBAPIs.createClosedSaleTransaction(sale1.getTicketNumber(), 0.0, 1.0);
      DBAPIs.createTicketEntry(1, entry1);
      double x = loopTest.receiveCashPayment(1, 10.0);
      assertEquals(9.0, x, 0.0);

      SaleTransaction sale2 = new SaleTransactionClass();
      sale2.setTicketNumber(2);
      List<TicketEntry> list2 = new ArrayList<>();
      TicketEntry entry2 = new TicketEntryClass("2", "2", 1, 2.0);
      TicketEntry entry3 = new TicketEntryClass("3", "3", 1, 2.0);
      list2.add(entry2);
      list2.add(entry3);
      sale2.setEntries(list2);
      DBAPIs.createClosedSaleTransaction(sale2.getTicketNumber(), 0.0, 4.0);
      DBAPIs.createTicketEntry(2, entry2);
      DBAPIs.createTicketEntry(2, entry3);
      double x1 = loopTest.receiveCashPayment(2, 10.0);
      assertEquals(6.0, x1, 0.0);

      SaleTransaction sale3 = new SaleTransactionClass();
      sale3.setTicketNumber(3);
      List<TicketEntry> list3 = new ArrayList<>();
      TicketEntry entry4 = new TicketEntryClass("4", "4", 1, 3.0);
      TicketEntry entry5 = new TicketEntryClass("5", "5", 1, 4.0);
      TicketEntry entry6 = new TicketEntryClass("6", "6", 1, 4.0);
      list3.add(entry4);
      list3.add(entry5);
      list3.add(entry6);
      sale3.setEntries(list3);
      DBAPIs.createClosedSaleTransaction(sale3.getTicketNumber(), 0.0, 11.0);
      DBAPIs.createTicketEntry(3, entry4);
      DBAPIs.createTicketEntry(3, entry5);
      DBAPIs.createTicketEntry(3, entry6);
      double x2 = loopTest.receiveCashPayment(3, 11.0);
      assertEquals(0.0, x2, 0.0);

    } catch (InvalidTransactionIdException ex) {

    } catch (InvalidPaymentException ex1) {

    } catch (UnauthorizedException ex2) {

    } catch (InvalidPasswordException ex3) {

    } catch (InvalidUsernameException ex4) {

    }

  }

  @Test
  public void test() {
    assertThrows(InvalidUsernameException.class, () -> shop.createUser("", "password", "Cashier"));
  }

  /**
   * INTEGRATION TESTS
   */

  // ------------------------------------------ //
  // ------------------ USER ------------------ //
  // ------------------------------------------ //

  @Test
  public void testFR1() {
    // Reset
    DBAPIs.reset();

    // Create user
    assertThrows(InvalidUsernameException.class, () -> shop.createUser("", "password", "Cashier"));

    assertThrows(InvalidPasswordException.class, () -> shop.createUser("test", "", "Cashier"));

    assertThrows(InvalidRoleException.class, () -> shop.createUser("test", "password", ""));
    assertThrows(InvalidRoleException.class, () -> shop.createUser("test", "password", "test"));

    Integer id = 0;
    try {
      id = shop.createUser("test", "test", "Administrator");
      assertNotEquals("Should not be 0", Integer.valueOf(0), id);
      assertNotEquals("Should not be -1", Integer.valueOf(-1), id);
      assertEquals(Integer.valueOf(-1), shop.createUser("test", "test", "Administrator"));
    } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
      e.printStackTrace();
    }

    Integer testUnauthorized = 0;
    try {
      testUnauthorized = shop.createUser("un", "un", "Cashier");
    } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
      e.printStackTrace();
    }

    // Login
    assertThrows(InvalidUsernameException.class, () -> shop.login("", "password"));
    assertThrows(InvalidPasswordException.class, () -> shop.login("test", ""));
    User loggedUser = null;
    try {
      loggedUser = shop.login("test", "password2");
      assertNull("Logged user should not be null", loggedUser);
      loggedUser = shop.login("test", "test");
      assertNotNull("Logged user should not be null", loggedUser);
      assertNull(shop.login("testError", "password"));
    } catch (InvalidUsernameException | InvalidPasswordException e) {
      e.printStackTrace();
    }

    // Logout
    Boolean logout = shop.logout();
    assertTrue(logout);
    loggedUser = null;
    logout = shop.logout();
    assertFalse(logout);

    // Test unauthorized ex logged user null
    assertThrows(UnauthorizedException.class, () -> shop.getUser(1));
    assertThrows(UnauthorizedException.class, () -> shop.getAllUsers());
    assertThrows(UnauthorizedException.class, () -> shop.deleteUser(1));
    assertThrows(UnauthorizedException.class, () -> shop.updateUserRights(1, "role"));

    // Test unauthorized ex logged user
    try {
      loggedUser = shop.login("un", "un");
    } catch (InvalidUsernameException | InvalidPasswordException e) {
      e.printStackTrace();
    }
    assertThrows(UnauthorizedException.class, () -> shop.getUser(1));
    assertThrows(UnauthorizedException.class, () -> shop.getAllUsers());
    assertThrows(UnauthorizedException.class, () -> shop.deleteUser(1));
    assertThrows(UnauthorizedException.class, () -> shop.updateUserRights(1, "role"));

    shop.logout();
    try {
      loggedUser = shop.login("test", "test");
    } catch (InvalidUsernameException | InvalidPasswordException e) {
      e.printStackTrace();
    }

    // Get new user
    assertThrows(InvalidUserIdException.class, () -> shop.getUser(0));
    User user = null;
    try {
      user = shop.getUser(testUnauthorized);
      assertNotNull("User should not be null", user);
    } catch (InvalidUserIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Update user rights
    assertThrows(InvalidUserIdException.class, () -> shop.updateUserRights(0, "ShopManager"));
    Integer prova = testUnauthorized;
    assertThrows(InvalidRoleException.class, () -> shop.updateUserRights(prova, ""));
    Boolean updateUser = null;
    try {
      updateUser = shop.updateUserRights(testUnauthorized, "ShopManager");
      assertTrue(updateUser);
    } catch (InvalidUserIdException | InvalidRoleException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Get all user
    List<User> list = null;
    try {
      list = shop.getAllUsers();
      assertFalse("User list should not be empty", list.isEmpty());
    } catch (UnauthorizedException e) {
      e.printStackTrace();
    }

    // Delete user
    assertThrows(InvalidUserIdException.class, () -> shop.deleteUser(0));
    try {
      updateUser = shop.deleteUser(testUnauthorized);
      assertTrue(updateUser);
      updateUser = shop.deleteUser(id);
      assertTrue(updateUser);
    } catch (InvalidUserIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

  }

  // ------------------------------------------ //
  // -------------- PRODUCT TYPE -------------- //
  // ------------------------------------------ //

  @Test
  public void testFR3() {

    // ** Reset
    shop.reset();

    // ** Common Exceptions - Unauthorized
    User loggedUser = null;
    assertThrows(UnauthorizedException.class, () -> shop.createProductType("description", "978020137962", 1, "note"));
    assertThrows(UnauthorizedException.class,
        () -> shop.updateProduct(1, "newDescription", "978020137961", 2, "newNote"));
    assertThrows(UnauthorizedException.class, () -> shop.deleteProductType(1));
    assertThrows(UnauthorizedException.class, () -> shop.getAllProductTypes());
    assertThrows(UnauthorizedException.class, () -> shop.getProductTypeByBarCode("978020137961"));
    assertThrows(UnauthorizedException.class, () -> shop.getProductTypesByDescription("newDescription"));
    assertThrows(UnauthorizedException.class, () -> shop.updateQuantity(1, 10));
    assertThrows(UnauthorizedException.class, () -> shop.updatePosition(1, "1-A-1"));

    // ** Login, setting logged admin user for the next tests
    Integer id = 0;
    try {
      id = shop.createUser("adminFR3", "adminFR3", "Administrator");
    } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
      e.printStackTrace();
    }

    try {
      loggedUser = shop.login("adminFR3", "adminFR3");
    } catch (InvalidUsernameException | InvalidPasswordException e) {
      e.printStackTrace();
    }

    // ** Common Exceptions - Invalid Product Description/Id/Barcode
    assertThrows(InvalidProductDescriptionException.class, () -> shop.createProductType("", "productCode", 1, "note"));
    assertThrows(InvalidProductCodeException.class, () -> shop.createProductType("description", "", 1, "note"));
    assertThrows(InvalidPricePerUnitException.class,
        () -> shop.createProductType("description", "978020137961", -1, "note"));
    assertThrows(InvalidProductIdException.class,
        () -> shop.updateProduct(-1, "newDescription", "newCode", 1, "newNote"));
    assertThrows(InvalidProductDescriptionException.class, () -> shop.updateProduct(1, "", "newCode", 1, "newNote"));
    assertThrows(InvalidProductCodeException.class, () -> shop.updateProduct(1, "newDescription", "", 1, "newNote"));
    assertThrows(InvalidPricePerUnitException.class,
        () -> shop.updateProduct(1, "newDescription", "978020137965", 0, "newNote"));
    assertThrows(InvalidProductIdException.class, () -> shop.deleteProductType(0));
    assertThrows(InvalidProductCodeException.class, () -> shop.getProductTypeByBarCode(""));
    assertThrows(InvalidProductIdException.class, () -> shop.updateQuantity(0, 10));
    assertThrows(InvalidProductIdException.class, () -> shop.updatePosition(0, "1-B-1"));
    assertThrows(InvalidLocationException.class, () -> shop.updatePosition(1, "A-1-A"));

    // ** Check for uniqueness productCode, position, etc
    Integer[] productId = {0};
    try {
      productId[0] = shop.createProductType("Jeans", "978020137978", 15, "note");
      assertEquals(Integer.valueOf(-1), shop.createProductType("description", "978020137978", 1, "note"));
    } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException
        | UnauthorizedException e) {
      e.printStackTrace();
    }
    
    try {
      shop.updatePosition(productId[0], "2-R-1");
    } catch (InvalidProductIdException | InvalidLocationException | UnauthorizedException e) {
      e.printStackTrace();
    }

    Boolean newPosition = true;
    try {
      newPosition = shop.updatePosition(productId[0], "2-R-1");
      assertFalse(newPosition);
    } catch (InvalidProductIdException | InvalidLocationException | UnauthorizedException e) {
      e.printStackTrace();
    }
    
    // Test wrong id
    try {
      newPosition = shop.updatePosition(100, "2-R-1");
      assertFalse(newPosition);
    } catch (InvalidProductIdException | InvalidLocationException | UnauthorizedException e2) {
      e2.printStackTrace();
    }
    // ** Nominal cases

    // Update product
    Boolean newPrice = true;
    try {
      newPrice = shop.updateProduct(productId[0], "T-Shirt", "978020137918", 10, "newNote");
    } catch (InvalidProductIdException | InvalidProductDescriptionException | InvalidProductCodeException
        | InvalidPricePerUnitException | UnauthorizedException e) {
      e.printStackTrace();
    }
    assertTrue(newPrice);

    // Get product from barcode
    ProductType p = null;
    try {
      p = shop.getProductTypeByBarCode("978020137918");
    } catch (InvalidProductCodeException | UnauthorizedException e) {
      e.printStackTrace();
    }
    assertNotNull(p);
    // Test barcode not present
    try {
      p = shop.getProductTypeByBarCode("978020137961");
    } catch (InvalidProductCodeException | UnauthorizedException e1) {
      e1.printStackTrace();
    }
    assertNull(p);

    // Get product from description
    List<ProductType> list = null;
    try {
      list = shop.getProductTypesByDescription("checkBarcode");
      assertTrue("List should be empty", list.isEmpty());
      list = shop.getProductTypesByDescription("Jeans");
      assertFalse("List should not be empty", list.isEmpty());

    } catch (UnauthorizedException e) {
      e.printStackTrace();
    }

    // Update product quantity
    Boolean newQuantity = true;
    try {
      newQuantity = shop.updateQuantity(productId[0], 10);
      assertTrue(newQuantity);
    } catch (InvalidProductIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    try {
      shop.logout();
      loggedUser = shop.login("adminFR3", "adminFR3");
    } catch (InvalidUsernameException | InvalidPasswordException e) {
      e.printStackTrace();
    }
    
    // Test negative new quantity
    try {
      newQuantity = shop.updateQuantity(productId[0], -30);
      assertFalse(newQuantity);
    } catch (InvalidProductIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Delete product
    Boolean delete = true;
    try {
      delete = shop.deleteProductType(productId[0]);
      assertTrue(delete);
    } catch (InvalidProductIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

  }

  // ------------------------------------------ //
  // ----------------- ORDER ------------------ //
  // ------------------------------------------ //

  @Test
  public void testFR4() {
    // Reset
    shop.reset();

    // ** Common Exceptions - Unauthorized
    User loggedUser = null;
    String RFID = "0000001000";
    assertThrows(UnauthorizedException.class, () -> shop.issueOrder("978020137962", 100, 2));
    assertThrows(UnauthorizedException.class, () -> shop.payOrderFor("978020137962", 100, 2));
    assertThrows(UnauthorizedException.class, () -> shop.payOrder(1));
    assertThrows(UnauthorizedException.class, () -> shop.recordOrderArrival(1));
    assertThrows(UnauthorizedException.class, () -> shop.getAllOrders());

    assertThrows(UnauthorizedException.class, () -> shop.recordOrderArrivalRFID(1, RFID));

    // ** Login, setting logged admin user for the next tests
    Integer id = 0;
    try {
      id = shop.createUser("adminFR4", "adminFR4", "Administrator");
    } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
      e.printStackTrace();
    }

    try {
      loggedUser = shop.login("adminFR4", "adminFR4");
    } catch (InvalidUsernameException | InvalidPasswordException e) {
      e.printStackTrace();
    }

    // Create product
    Integer[] productId = {0};
    Integer[] productIdRFID = {0};
    try {
      productId[0] = shop.createProductType("product desc", "978020137962", 10, "note");
      productIdRFID[0] = shop.createProductType("product desc", "978020137973", 1, "note");
    } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException
        | UnauthorizedException e2) {
      e2.printStackTrace();
    }

    // ** Common Exceptions - Invalid Order Id/Barcode/PricePerUnit,Quantity
    assertThrows(InvalidProductCodeException.class, () -> shop.issueOrder("", 100, 2));
    assertThrows(InvalidPricePerUnitException.class, () -> shop.issueOrder("978020137962", 100, 0));
    assertThrows(InvalidQuantityException.class, () -> shop.issueOrder("978020137962", 0, 2));
    assertThrows(InvalidProductCodeException.class, () -> shop.payOrderFor("", 100, 2));
    assertThrows(InvalidPricePerUnitException.class, () -> shop.payOrderFor("978020137962", 100, 0));
    assertThrows(InvalidQuantityException.class, () -> shop.payOrderFor("978020137962", 0, 2));
    assertThrows(InvalidProductCodeException.class, () -> shop.issueOrder("", 100, 2));
    assertThrows(InvalidPricePerUnitException.class, () -> shop.issueOrder("978020137962", 100, 0));
    assertThrows(InvalidQuantityException.class, () -> shop.issueOrder("978020137962", 0, 2));
    assertThrows(InvalidOrderIdException.class, () -> shop.payOrder(0));
    assertThrows(InvalidOrderIdException.class, () -> shop.recordOrderArrival(0));

    assertThrows(InvalidOrderIdException.class, () -> shop.recordOrderArrivalRFID(0, RFID));
    assertThrows(InvalidRFIDException.class, () -> shop.recordOrderArrivalRFID(1, null));

    // ** Nominal cases

    // Create Order
    Integer[] orderId = {-1};
    Integer[] orderIdRFID = {-1};
    try {
      orderId[0] = shop.issueOrder("978020137962", 100, 1);
      assertTrue(orderId[0] != -1);
      //issue order for RFID
      orderIdRFID[0] = shop.issueOrder("978020137973", 1, 1); 
    } catch (InvalidProductCodeException | InvalidQuantityException | InvalidPricePerUnitException
        | UnauthorizedException e) {
      e.printStackTrace();
    }

    // PayOrderFor
    Boolean addCreditToBalance = false;
    try {
      addCreditToBalance = shop.recordBalanceUpdate(12); // add credit to balance
    } catch (UnauthorizedException e1) {
      e1.printStackTrace();
    }

    // There is enough balance to pay
    Integer newPayOrderFor = -1;
    try {
      newPayOrderFor = shop.payOrderFor("978020137962", 10, 1);
      assertTrue(newPayOrderFor != -1);
      shop.payOrderFor("978020137973", 1, 1);
    } catch (InvalidProductCodeException | InvalidQuantityException | InvalidPricePerUnitException
        | UnauthorizedException e) {
      e.printStackTrace();
    }
    
    // There is NOT enough balance to pay
    try {
      newPayOrderFor = shop.payOrderFor("978020137962", 10, 0.1);
      assertTrue(newPayOrderFor == -1);
    } catch (InvalidProductCodeException | InvalidQuantityException | InvalidPricePerUnitException
        | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Record Order Arrival
    assertThrows(InvalidLocationException.class, () -> shop.recordOrderArrival(orderId[0]));
    assertThrows(InvalidLocationException.class, () -> shop.recordOrderArrivalRFID(orderIdRFID[0], RFID));
    try {
      shop.updatePosition(productId[0], "1-A-1");
      shop.updatePosition(productIdRFID[0], "1-A-2");
    } catch (InvalidProductIdException | InvalidLocationException | UnauthorizedException e1) {
      e1.printStackTrace();
    }

    Boolean orderArrival = false;
    try {
      orderArrival = shop.recordOrderArrival(orderId[0]);
      assertTrue(orderArrival);
      orderArrival = shop.recordOrderArrivalRFID(orderIdRFID[0], RFID);
      assertTrue(orderArrival);
    } catch (InvalidOrderIdException | UnauthorizedException | InvalidLocationException | InvalidRFIDException e) {
      e.printStackTrace();
    }

    List<Order> ordersList = null;
    try {
      ordersList = shop.getAllOrders();
      assertFalse("Order list should not be empty", ordersList.isEmpty());
    } catch (UnauthorizedException e) {
      e.printStackTrace();
    }

  }

  // ------------------------------------------ //
  // ---------------- CUSTOMER ---------------- //
  // ------------------------------------------ //
  
  @Test
  public void testFR5() {

    boolean testReturn;

  // Reset
    DBAPIs.reset();

  // Common Exceptions - Unauthorized
    User loggedUser = null;
    assertThrows(UnauthorizedException.class, () -> shop.defineCustomer("a"));
    assertThrows(UnauthorizedException.class, () -> shop.modifyCustomer(1, "a", "123"));
    assertThrows(UnauthorizedException.class, () -> shop.deleteCustomer(1));
    assertThrows(UnauthorizedException.class, () -> shop.getCustomer(1));
    assertThrows(UnauthorizedException.class, () -> shop.getAllCustomers());
    assertThrows(UnauthorizedException.class, () -> shop.createCard());
    assertThrows(UnauthorizedException.class, () -> shop.attachCardToCustomer("123", 1));
    assertThrows(UnauthorizedException.class, () -> shop.modifyPointsOnCard("123", 1));

  // Login, setting logged admin user for the next tests
    Integer id = 0;
    try {
      id = shop.createUser("adminFR5", "adminFR5", "Administrator");
    } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
      e.printStackTrace();
    }

    try {
      loggedUser = shop.login("adminFR5", "adminFR5");
    } catch (InvalidUsernameException | InvalidPasswordException e) {
      e.printStackTrace();
    }

    // Reset
    try {
    testReturn=shop.deleteCustomer(1);
    testReturn=shop.deleteCustomer(2);
    testReturn=shop.deleteCustomer(3);
    testReturn=shop.deleteCustomer(4);
    testReturn=shop.deleteCustomer(5);
    } catch (InvalidCustomerIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

  // Common Exceptions - Invalid Customer name/id
    assertThrows(InvalidCustomerNameException.class, () -> shop.defineCustomer(""));
    Integer cusnullID = null;

  // defineCustomer
    try {
      id = shop.defineCustomer("cus1");
      assertEquals(Integer.valueOf(1), Integer.valueOf(id));
    } catch (InvalidCustomerNameException | UnauthorizedException e) {
      e.printStackTrace();
    }

    try {
      id = shop.defineCustomer("cus1");                                 // duplicated name
      assertEquals(Integer.valueOf(-1), Integer.valueOf(id));
      Integer id2 = shop.defineCustomer("cus2");                        // for further testing
    } catch (InvalidCustomerNameException | UnauthorizedException e) {
      e.printStackTrace();
    }

  // Common Exceptions - With correct customer
    assertThrows(InvalidCustomerNameException.class, () -> shop.modifyCustomer(1, "", "123"));
    assertThrows(InvalidCustomerCardException.class, () -> shop.modifyCustomer(1, "testname", "123"));
    assertThrows(InvalidCustomerCardException.class, () -> shop.attachCardToCustomer("123", 1));
    // assertThrows(InvalidCustomerCardException.class, () -> shop.modifyPointsOnCard("123", 1));

  // createCard
    String card1= "123", card2 = "123";
    try {
      card1 = shop.createCard();
      assertEquals(card1, card1);
      card2 = shop.createCard();                                        // for further testing
    } catch (UnauthorizedException e) {
      e.printStackTrace();
    }

  // attachCardToCustomer
    testReturn = false;
    try {
      testReturn = shop.attachCardToCustomer(card1,1); // ok
      assertEquals(true, testReturn);
    } catch (InvalidCustomerIdException | UnauthorizedException | InvalidCustomerCardException e) {
      e.printStackTrace();
    }

    try {
      testReturn = shop.attachCardToCustomer(card1,2); // card already attached
      assertEquals(false, testReturn);
    } catch (InvalidCustomerIdException | UnauthorizedException | InvalidCustomerCardException e) {
      e.printStackTrace();
    }

  // modifyPointsOnCard
    try {
      testReturn = shop.modifyPointsOnCard(card1,1); // ok
      assertEquals(true, testReturn);
    } catch ( UnauthorizedException | InvalidCustomerCardException e) {
      e.printStackTrace();
    }

    try {
      testReturn = shop.modifyPointsOnCard(card2,1); // card not attached
      assertEquals(false, testReturn);
    } catch ( UnauthorizedException | InvalidCustomerCardException e) {
      e.printStackTrace();
    }

    testReturn = true;
    try {
      testReturn = shop.modifyPointsOnCard(card1,-200); // negative points
      assertEquals(false, testReturn);
    } catch ( UnauthorizedException | InvalidCustomerCardException e) {
      e.printStackTrace();
    }

  // getCustomer
    try {
      Customer testcustomerreturn = shop.getCustomer(1);
      assertEquals(card1, testcustomerreturn.getCustomerCard());
      assertEquals("cus1", testcustomerreturn.getCustomerName());
      assertEquals(Integer.valueOf(1), Integer.valueOf(testcustomerreturn.getId()));
      assertEquals(Integer.valueOf(1), Integer.valueOf(testcustomerreturn.getPoints()));
    } catch ( UnauthorizedException | InvalidCustomerIdException e) {
      e.printStackTrace();
    }  

  // modifyCustomer
    testReturn = true;
    try {
      testReturn = shop.modifyCustomer(11,"cus11",card2); // customer id doesn't exist
      assertEquals(false, testReturn);
    } catch ( InvalidCustomerNameException | InvalidCustomerIdException | UnauthorizedException | InvalidCustomerCardException e) {
      e.printStackTrace();
    }

    testReturn = true;
    try {
      testReturn = shop.modifyCustomer(2,"cus2",card1); // card already attached
      assertEquals(false, testReturn);
    } catch ( InvalidCustomerNameException | InvalidCustomerIdException | UnauthorizedException | InvalidCustomerCardException e) {
      e.printStackTrace();
    }

    try {
      testReturn = shop.modifyCustomer(1,"cus1",""); // effectively removing card
      assertEquals(true, testReturn);
    } catch ( InvalidCustomerNameException | InvalidCustomerIdException | UnauthorizedException | InvalidCustomerCardException e) {
      e.printStackTrace();
    }

  // getAllCustomers
    try {
      List<Customer> testcustomerreturnlist = shop.getAllCustomers();
      Customer testcustomerreturn = testcustomerreturnlist.get(0);
      assertEquals("", testcustomerreturn.getCustomerCard());
      assertEquals("cus1", testcustomerreturn.getCustomerName());
      assertEquals(Integer.valueOf(1), Integer.valueOf(testcustomerreturn.getId()));
      assertEquals(Integer.valueOf(1), Integer.valueOf(testcustomerreturn.getPoints()));
      testcustomerreturn = testcustomerreturnlist.get(1);
      assertEquals(null, testcustomerreturn.getCustomerCard());
      assertEquals("cus2", testcustomerreturn.getCustomerName());
      assertEquals(Integer.valueOf(2), Integer.valueOf(testcustomerreturn.getId()));
      assertEquals(Integer.valueOf(0), Integer.valueOf(testcustomerreturn.getPoints()));
    } catch ( UnauthorizedException e) {
      e.printStackTrace();
    }  

    assertThrows(InvalidCustomerIdException.class, () -> shop.deleteCustomer(-1));
    assertThrows(InvalidCustomerIdException.class, () -> shop.getCustomer(-1));
    assertThrows(InvalidCustomerIdException.class, () -> shop.attachCardToCustomer("test", 0));

  // deleteCustomer
    testReturn = false;
    try {
      testReturn = shop.deleteCustomer(2);
      assertEquals(true, testReturn);
      testReturn = shop.deleteCustomer(1);                // to clean DB
    } catch ( UnauthorizedException | InvalidCustomerIdException e) {
      e.printStackTrace();
    }
    
  }

  // ------------------------------------------ //
  // ------------ SALE TRANSACTION ------------ //
  // ------------------------------------------ //

  @Test
  public void testFR6() {
    // Reset
    DBAPIs.reset();

    // Create user
    Integer userId = 0;
    User loggedUser = null;
    try {
      userId = shop.createUser("test", "test", "Administrator");
    } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e) {
      e.printStackTrace();
    }

    // Common Exceptions - Unauthorized
    assertThrows(UnauthorizedException.class, () -> shop.startSaleTransaction());
    assertThrows(UnauthorizedException.class, () -> shop.addProductToSale(1, "test", 10));
    assertThrows(UnauthorizedException.class, () -> shop.deleteProductFromSale(1, "test", 10));
    assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToProduct(1, "test", 10));
    assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToSale(1, 10));
    assertThrows(UnauthorizedException.class, () -> shop.computePointsForSale(1));
    assertThrows(UnauthorizedException.class, () -> shop.endSaleTransaction(1));
    assertThrows(UnauthorizedException.class, () -> shop.deleteSaleTransaction(1));
    assertThrows(UnauthorizedException.class, () -> shop.getSaleTransaction(1));
    assertThrows(UnauthorizedException.class, () -> shop.startReturnTransaction(1));
    assertThrows(UnauthorizedException.class, () -> shop.returnProduct(1, "test", 10));
    assertThrows(UnauthorizedException.class, () -> shop.endReturnTransaction(1, true));
    assertThrows(UnauthorizedException.class, () -> shop.deleteReturnTransaction(1));

    assertThrows(UnauthorizedException.class, () -> shop.addProductToSaleRFID(1, "1111111111"));
    assertThrows(UnauthorizedException.class, () -> shop.deleteProductFromSaleRFID(1, "1111111111"));

    // Login
    try {
      loggedUser = shop.login("test", "test");
    } catch (InvalidUsernameException | InvalidPasswordException e) {
      e.printStackTrace();
    }

    /**
     * Sale transaction
     */
    Integer[] transactionId = {0};
    Integer productId = null;
    Integer productIdRFID = null;
    String productCode = "123456789123";
    String productCodeRFID = "123456789456";
    String invalidProductCode = "123456789999";
  
    try {
      assertFalse(shop.addProductToSaleRFID(transactionId[0], "0000001000"));
      transactionId[0] = shop.startSaleTransaction();
      productId = shop.createProductType("product desc", productCode, 10, "note");
      shop.updatePosition(productId, "7-Z-8");
      shop.updateQuantity(productId, 20);

      productIdRFID = shop.createProductType("product desc", productCodeRFID, 1, "note");
      shop.updatePosition(productIdRFID, "1-Z-3");

    } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException
        | InvalidProductIdException | InvalidLocationException | UnauthorizedException | InvalidTransactionIdException | InvalidRFIDException | InvalidQuantityException e) {
      e.printStackTrace();
    }

    // Add product to sale;
    assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(0, productCode, 10));
    assertThrows(InvalidProductCodeException.class, () -> shop.addProductToSale(transactionId[0], "", 10));
    assertThrows(InvalidQuantityException.class, () -> shop.addProductToSale(transactionId[0], productCode, -10));

    Boolean flag = null;
    try {
      flag = shop.addProductToSale(transactionId[0], productCode, 10);
      assertTrue(flag);
      flag = shop.addProductToSale(transactionId[0], productCode, 100);
      assertFalse(flag);
      flag = shop.addProductToSale(transactionId[0], invalidProductCode, 10);
      assertFalse(flag);
    } catch (InvalidTransactionIdException | InvalidProductCodeException | InvalidQuantityException
        | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Add product to sale RFID;
    String RFID = "0000001000";
    assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSaleRFID(0, RFID));
    assertThrows(InvalidRFIDException.class, () -> shop.addProductToSaleRFID(transactionId[0], null));

    try {
      int orderId = shop.issueOrder(productCodeRFID, 1, 1.0);
      shop.recordOrderArrivalRFID(orderId, RFID);
      assertThrows(InvalidQuantityException.class, () -> shop.addProductToSaleRFID(transactionId[0], RFID));
      shop.updateQuantity(productIdRFID, 2);
      flag = shop.addProductToSaleRFID(transactionId[0], RFID);
      assertTrue(flag);
      flag = shop.addProductToSaleRFID(transactionId[0], "0000001001");
      assertFalse(flag);
    } catch (InvalidTransactionIdException | InvalidProductCodeException | InvalidQuantityException
        | UnauthorizedException | InvalidOrderIdException | InvalidLocationException | InvalidRFIDException | InvalidPricePerUnitException | InvalidProductIdException e) {
      e.printStackTrace();
    }

    // Delete product from sale
    assertThrows(InvalidTransactionIdException.class, () -> shop.deleteProductFromSale(0, productCode, 5));
    assertThrows(InvalidProductCodeException.class, () -> shop.deleteProductFromSale(transactionId[0], "", 5));
    assertThrows(InvalidQuantityException.class, () -> shop.deleteProductFromSale(transactionId[0], productCode, -5));

    try {
      flag = shop.deleteProductFromSale(transactionId[0], productCode, 5);
      assertTrue(flag);
      flag = shop.deleteProductFromSale(transactionId[0], invalidProductCode, 5);
      assertFalse(flag);
    } catch (InvalidTransactionIdException | InvalidProductCodeException | InvalidQuantityException
        | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Delete product from sale RFID
    assertThrows(InvalidTransactionIdException.class, () -> shop.deleteProductFromSaleRFID(0, RFID));
    assertThrows(InvalidRFIDException.class, () -> shop.deleteProductFromSaleRFID(transactionId[0], ""));

    try {
      flag = shop.deleteProductFromSaleRFID(transactionId[0], RFID);
      assertTrue(flag);
      flag = shop.deleteProductFromSaleRFID(transactionId[0], RFID);
      assertFalse(flag); //No RFID in the sale
      shop.addProductToSaleRFID(transactionId[0], RFID);
    } catch (InvalidTransactionIdException | InvalidQuantityException
        | UnauthorizedException | InvalidRFIDException e) {
      e.printStackTrace();
    }


    // Apply discount to product
    assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToProduct(0, productCode, 0.5));
    assertThrows(InvalidProductCodeException.class, () -> shop.applyDiscountRateToProduct(transactionId[0], "", 0.5));
    assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToProduct(transactionId[0], productCode, -2));

    try {
      flag = shop.applyDiscountRateToProduct(transactionId[0], productCode, 0.5);
      assertTrue(flag);
      flag = shop.applyDiscountRateToProduct(transactionId[0], invalidProductCode, 0.5);
      assertFalse(flag);
    } catch (InvalidTransactionIdException | InvalidProductCodeException | InvalidDiscountRateException
        | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Apply discount to sale
    assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToSale(0, 0.5));
    assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToSale(transactionId[0], -2));

    try {
      flag = shop.applyDiscountRateToSale(transactionId[0], 0.5);
      assertTrue(flag);
    } catch (InvalidTransactionIdException | InvalidDiscountRateException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Compute points
    assertThrows(InvalidTransactionIdException.class, () -> shop.computePointsForSale(0));

    Integer[] points = {0};
    try {
      points[0] = shop.computePointsForSale(transactionId[0]);
      assertNotEquals(Integer.valueOf(-1), points[0]);
    } catch (InvalidTransactionIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // End sale transaction
    assertThrows(InvalidTransactionIdException.class, () -> shop.endSaleTransaction(0));

    try {
      flag = shop.endSaleTransaction(transactionId[0]);
      assertTrue(flag);
      flag = shop.endSaleTransaction(1000);
      assertFalse(flag);
      assertEquals(Integer.valueOf(-1), Integer.valueOf(shop.computePointsForSale(100)));
    } catch (InvalidTransactionIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Get sale transaction
    assertThrows(InvalidTransactionIdException.class, () -> shop.getSaleTransaction(0));
    SaleTransaction test = null;
    try {
      test = shop.getSaleTransaction(100);
      assertNull(test);
    } catch (InvalidTransactionIdException | UnauthorizedException e1) {
      e1.printStackTrace();
    }

    // Test not open sale transaction
    try {
      assertFalse(shop.addProductToSale(transactionId[0], productCode, 10));
      assertFalse(shop.deleteProductFromSale(transactionId[0], productCode, 10));
      assertFalse(shop.addProductToSale(transactionId[0], productCode, 10));
      assertFalse(shop.applyDiscountRateToProduct(transactionId[0], productCode, 0.5));
      assertTrue(shop.applyDiscountRateToSale(transactionId[0], 0.1));
      assertNotEquals(Integer.valueOf(-1), Integer.valueOf(shop.computePointsForSale(transactionId[0])));
      assertFalse(shop.endSaleTransaction(transactionId[0]));
    } catch (InvalidTransactionIdException | InvalidProductCodeException | InvalidDiscountRateException
        | UnauthorizedException | InvalidQuantityException e1) {
      e1.printStackTrace();
    }

    // Start return transaction
    assertThrows(InvalidTransactionIdException.class, () -> shop.startReturnTransaction(0));

    Integer[] returnTransaction = {0};
    try {
      returnTransaction[0] = shop.startReturnTransaction(100);
      assertEquals(Integer.valueOf(-1), returnTransaction[0]);
      returnTransaction[0] = shop.startReturnTransaction(transactionId[0]);
      assertNotEquals(Integer.valueOf(-1), returnTransaction[0]);
    } catch (InvalidTransactionIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Return product
    assertThrows(InvalidTransactionIdException.class, () -> shop.returnProduct(0, productCode, 2));
    assertThrows(InvalidProductCodeException.class, () -> shop.returnProduct(returnTransaction[0], "", 2));
    assertThrows(InvalidQuantityException.class, () -> shop.returnProduct(returnTransaction[0], productCode, -2));
    
    try {
      flag = shop.returnProduct(returnTransaction[0], productCode, 5);
      assertTrue(flag);
      flag = shop.returnProduct(returnTransaction[0], invalidProductCode, 2);
      assertFalse(flag);
      flag = shop.returnProduct(returnTransaction[0], invalidProductCode, 3);
      assertFalse(flag);
    } catch (InvalidTransactionIdException | InvalidProductCodeException | InvalidQuantityException
        | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Return product RFID //TODO
    assertThrows(InvalidTransactionIdException.class, () -> shop.returnProductRFID(0, RFID));
    assertThrows(InvalidRFIDException.class, () -> shop.returnProductRFID(returnTransaction[0], ""));
    
    try {
      flag = shop.returnProductRFID(returnTransaction[0], RFID);
      assertTrue(flag);
      flag = shop.returnProductRFID(returnTransaction[0], RFID);
      assertFalse(flag);
    } catch (InvalidTransactionIdException | UnauthorizedException | InvalidRFIDException e) {
      e.printStackTrace();
    }

    // End return transaction
    assertThrows(InvalidTransactionIdException.class, () -> shop.endReturnTransaction(0, true));

    try {
      flag = shop.endReturnTransaction(1000, true);
      assertFalse(flag);
      flag = shop.endReturnTransaction(returnTransaction[0], true);
      assertTrue(flag);
    } catch (InvalidTransactionIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Delete return transaction
    assertThrows(InvalidTransactionIdException.class, () -> shop.deleteReturnTransaction(0));

    try {
      flag = shop.deleteReturnTransaction(returnTransaction[0]);
      assertTrue(flag);
      flag = shop.deleteReturnTransaction(1000);
      assertFalse(flag);
    } catch (InvalidTransactionIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Delete sale transaction
    assertThrows(InvalidTransactionIdException.class, () -> shop.deleteSaleTransaction(0));

    try {
      flag = shop.deleteSaleTransaction(transactionId[0]);
      assertTrue(flag);
      flag = shop.deleteSaleTransaction(1000);
      assertFalse(flag);
    } catch (InvalidTransactionIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Delete product
    try {
      flag = shop.deleteProductType(productId);
      assertTrue(flag);
    } catch (InvalidProductIdException | UnauthorizedException e) {
      e.printStackTrace();
    }

    /**
     * Test other methods
     */
    try {
      transactionId[0] = shop.startSaleTransaction();
      productId = shop.createProductType("product desc", productCode, 10, "note");
      shop.updatePosition(productId, "7-Z-8");
      shop.updateQuantity(productId, 20);
      flag = shop.addProductToSale(transactionId[0], productCode, 10);
      assertTrue(flag);
      flag = shop.endSaleTransaction(transactionId[0]);
      shop.receiveCashPayment(transactionId[0], 100);
      flag = shop.deleteSaleTransaction(transactionId[0]);
      assertFalse(flag);
    } catch (InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException
        | InvalidProductIdException | InvalidLocationException | UnauthorizedException | InvalidTransactionIdException | InvalidQuantityException | InvalidPaymentException e) {
      e.printStackTrace();
    }
    
  }

  // ------------------------------------------ //
  // ----------------- PAYMENT ---------------- //
  // ------------------------------------------ //
  @Test
  public void testFR7() {

    // Reset
    DBAPIs.reset();

    // Common Exceptions - Unauthorized (no user logged)
    assertThrows(UnauthorizedException.class, () -> shop.receiveCashPayment(1, 10.0));

    assertThrows(UnauthorizedException.class, () -> shop.receiveCreditCardPayment(1, "5123456789012346"));

    assertThrows(UnauthorizedException.class, () -> shop.returnCashPayment(1));

    assertThrows(UnauthorizedException.class, () -> shop.returnCreditCardPayment(1, "5123456789012346"));

    Integer u1 = 0;
    Integer u2 = 0;
    Integer u3 = 0;
    Integer productId = 0;

    // User and productType creation
    try {
      u1 = shop.createUser("PaymentTestAd", "PaymentTestAd", "Administrator");
      u2 = shop.createUser("PaymentTestSm", "PaymentTestSm", "ShopManager");
      u3 = shop.createUser("PaymentTestCash", "PaymentTestCash", "Cashier");
      shop.login("PaymentTestAd", "PaymentTestAd");
      productId = shop.createProductType("PaymentTestProduct", "123456789123", 1, "PaymentTestProduct");
      shop.updatePosition(productId, "2-B-2");
      shop.updateQuantity(productId, 10);
    } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException
        | InvalidProductDescriptionException | InvalidProductCodeException | InvalidPricePerUnitException
        | UnauthorizedException | InvalidProductIdException | InvalidLocationException e1) {
      e1.printStackTrace();
    }

    // Receive Cash Payment
    try {
      Integer saleId = shop.startSaleTransaction();
      shop.addProductToSale(saleId, "123456789123", 2);
      shop.endSaleTransaction(saleId);

      assertThrows(InvalidTransactionIdException.class, () -> shop.receiveCashPayment(0, 10.0));

      assertThrows(InvalidPaymentException.class, () -> shop.receiveCashPayment(saleId, 0));

      // SaleId not exists
      assertEquals(-1, shop.receiveCashPayment(saleId + 1, 10.0), 0.0);

      // Cash not enough
      assertEquals(-1, shop.receiveCashPayment(saleId, 1.0), 0.0);

      assertEquals(0, shop.receiveCashPayment(saleId, 2.0), 0.0);

    } catch (UnauthorizedException | InvalidProductCodeException | InvalidTransactionIdException
        | InvalidQuantityException | InvalidPaymentException e) {
      e.printStackTrace();
    }

    // Receive CreditCard Payment
    try {
      Integer saleId = shop.startSaleTransaction();
      shop.addProductToSale(saleId, "123456789123", 2);
      shop.endSaleTransaction(saleId);

      assertThrows(InvalidTransactionIdException.class, () -> shop.receiveCreditCardPayment(0, "5123456789012346"));

      // Null creditCard number
      assertThrows(InvalidCreditCardException.class, () -> shop.receiveCreditCardPayment(saleId, null));

      // Empty creditCard number
      assertThrows(InvalidCreditCardException.class, () -> shop.receiveCreditCardPayment(saleId, ""));

      // Invalid creditCard number (Luhn algorithm)
      assertThrows(InvalidCreditCardException.class, () -> shop.receiveCreditCardPayment(saleId, "123"));

      // Credit Card not registered
      assertFalse(shop.receiveCreditCardPayment(saleId, "5836858689847424"));

      // Credit Card not enough
      assertFalse(shop.receiveCreditCardPayment(saleId, "9008869470153389"));

      assertTrue(shop.receiveCreditCardPayment(saleId, "4987654321098769"));

    } catch (UnauthorizedException | InvalidTransactionIdException | InvalidProductCodeException
        | InvalidQuantityException | InvalidCreditCardException e) {
      e.printStackTrace();
    }

    // Return Cash Payment

    try {
      Integer saleId = shop.startSaleTransaction();
      shop.addProductToSale(saleId, "123456789123", 2);
      shop.endSaleTransaction(saleId);
      Integer returnId = shop.startReturnTransaction(saleId);
      shop.returnProduct(returnId, "123456789123", 2);

      // Invalid Transaction Id
      assertThrows(InvalidTransactionIdException.class, () -> shop.returnCashPayment(0));

      // Return not closed transaction
      assertEquals(-1, shop.returnCashPayment(returnId), 0.0);

      shop.endReturnTransaction(returnId, true);

      // Return Transaction doesn't exists
      assertEquals(-1, shop.returnCashPayment(returnId + 1), 0.0);

      assertEquals(2, shop.returnCashPayment(returnId), 0.0);

    } catch (InvalidTransactionIdException | InvalidProductCodeException | InvalidQuantityException
        | UnauthorizedException e) {
      e.printStackTrace();
    }

    // Return CreditCard Payment
    Integer saleId;
    try {
      saleId = shop.startSaleTransaction();
      shop.addProductToSale(saleId, "123456789123", 2);
      shop.endSaleTransaction(saleId);
      Integer returnId = shop.startReturnTransaction(saleId);
      shop.returnProduct(returnId, "123456789123", 2);
      shop.endReturnTransaction(returnId, true);

      assertThrows(InvalidTransactionIdException.class, () -> shop.returnCreditCardPayment(0, "5123456789012346"));

      // Empty creditCard number
      assertThrows(InvalidCreditCardException.class, () -> shop.returnCreditCardPayment(returnId, ""));

      // Invalid creditCard number (Luhn algorithm)
      assertThrows(InvalidCreditCardException.class, () -> shop.returnCreditCardPayment(returnId, "123"));

      // CreditCard not registered
      assertEquals(-1, shop.returnCreditCardPayment(returnId, "5836858689847424"), 0.0);

      assertEquals(2, shop.returnCreditCardPayment(returnId, "4987654321098769"), 0.0);

    } catch (UnauthorizedException | InvalidTransactionIdException | InvalidProductCodeException
        | InvalidQuantityException | InvalidCreditCardException e) {
      e.printStackTrace();
    }

    try {
      shop.deleteUser(u1);
      shop.deleteUser(u2);
      shop.deleteUser(u3);
      shop.deleteProductType(productId);
    } catch (InvalidUserIdException | UnauthorizedException | InvalidProductIdException e) {
      
      e.printStackTrace();
    }

  }

  // ------------------------------------------ //
  // ----------- BALANCE OPERATION ------------ //
  // ------------------------------------------ //
  @Test
  public void testFR8() {

    /**
     * Reset
     */
    boolean reset = DBAPIs.reset();
    assertTrue(reset);

    // Common Exceptions - Unauthorized

    assertThrows(UnauthorizedException.class, () -> shop.recordBalanceUpdate(10.0));

    LocalDate fromDate = LocalDate.now();
    LocalDate toDate = fromDate.plusDays(10);

    assertThrows(UnauthorizedException.class, () -> shop.getCreditsAndDebits(fromDate, toDate));

    assertThrows(UnauthorizedException.class, () -> shop.computeBalance());

    Integer u1 = 0;
    Integer u2 = 0;
    

    try {
      u1 = shop.createUser("BalanceTestAd", "BalanceTestAd", "Administrator");
      shop.login("BalanceTestAd", "BalanceTestAd");
      u2 = shop.createUser("BalanceTestCash", "BalanceTestCash", "Cashier");
      shop.logout();
    } catch (InvalidUsernameException | InvalidPasswordException | InvalidRoleException e2) {
      e2.printStackTrace();
    }

    // Compute Balance
    try {
      // Testing UnauthorizedException with Cashier Role
      shop.login("BalanceTestCash", "BalanceTestCash");
      assertThrows(UnauthorizedException.class, () -> shop.computeBalance());
      shop.logout();
      shop.login("BalanceTestAd", "BalanceTestAd");
      assertEquals(0.0, shop.computeBalance(), 0.0);

    } catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException e1) {
      e1.printStackTrace();
    }

    // Record credit and debit
    try {
      // Testing UnauthorizedException with Cashier Role
      shop.login("BalanceTestCash", "BalanceTestCash");
      assertThrows(UnauthorizedException.class, () -> shop.recordBalanceUpdate(10.0));
      shop.logout();
      shop.login("BalanceTestAd", "BalanceTestAd");

      // Record Credit
      assertTrue(shop.recordBalanceUpdate(10.0));

      // Record Debit
      assertTrue(shop.recordBalanceUpdate(-10.0));

      // Record Debit (balance goes negative)
      assertFalse(shop.recordBalanceUpdate(-10.0));

    } catch (UnauthorizedException | InvalidUsernameException | InvalidPasswordException e) {
      e.printStackTrace();
    }

    // Show credits and debits over a period of time
    try {
      // Testing UnauthorizedException with Cashier Role
      shop.login("BalanceTestCash", "BalanceTestCash");
      assertThrows(UnauthorizedException.class, () -> shop.computeBalance());
      shop.logout();
      shop.login("BalanceTestAd", "BalanceTestAd");

      assertNotNull(shop.getCreditsAndDebits(null, null));

      assertNotNull(shop.getCreditsAndDebits(fromDate, null));

      assertNotNull(shop.getCreditsAndDebits(null, toDate));

      assertNotNull(shop.getCreditsAndDebits(fromDate, toDate));

    } catch (InvalidUsernameException | InvalidPasswordException | UnauthorizedException e) {
      e.printStackTrace();
    }

    try {
      shop.deleteUser(u1);
      shop.deleteUser(u2);
    } catch (InvalidUserIdException | UnauthorizedException e) {
      
      e.printStackTrace();
    }

  }

}
