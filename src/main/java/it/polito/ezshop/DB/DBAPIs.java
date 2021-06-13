package it.polito.ezshop.DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import it.polito.ezshop.data.*;

public class DBAPIs {

    // -----------------------------------------------------------//
    // ----------------------SaleTransaction----------------------//
    // -----------------------------------------------------------//

    // This method return true if the transaction it's been correctly added, false
    // otherwise
    public static Boolean createClosedSaleTransaction(Integer ticketNumber, Double discountRate, Double price) {

        if (ticketNumber == null || discountRate == null || price == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;
        try {
            String sql = "INSERT INTO ClosedSaleTransaction(ticketNumber, discountRate, price, payed) VALUES(?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, ticketNumber);
            ps.setDouble(2, discountRate);
            ps.setDouble(3, price);
            ps.setBoolean(4, false);
            // true if the first result is a ResultSet object; false if the first result is
            // an update count or there is no result
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + " addClosedSaleTransaction1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                //// System.out.println(ex.toString() + " addClosedSaleTransaction2");
            }
        }
        return returnValue;
    }

    // This method returns null if the ClosedSaleTransaction does't exist or the
    // SaleTransaction if exists
    public static SaleTransaction getClosedSaleTransaction(Integer transactionId) {

        if (transactionId == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        SaleTransaction sale = null;
        try {
            String sql = "SELECT ticketNumber, discountRate, price FROM ClosedSaleTransaction WHERE ticketNumber = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the SaleTransaction desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                return null;
            sale = new SaleTransactionClass();
            sale.setTicketNumber(rs.getInt("ticketNumber"));
            sale.setDiscountRate(rs.getDouble("discountRate"));
            sale.setPrice(rs.getDouble("price"));
            sale.setEntries(getSaleTicketEntries(transactionId));
        } catch (SQLException ex) {
            //// System.out.println(ex.toString() + "in getClosedSaleTransaction1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getClosedSaleTransaction2");
            }
        }
        return sale;
    }

    public static Boolean getClosedSaleTransactionPayed(Integer transactionId) {

        if (transactionId == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean payed = false;
        try {
            String sql = "SELECT payed FROM ClosedSaleTransaction WHERE ticketNumber = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            rs = ps.executeQuery();
            payed = rs.getBoolean(1);
            // //System.out.println("#ClosedSaleTransaction saved into db: " + count);
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getClosedSaleTransactionPayed");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getClosedSaleTransactionPayed");
            }
        }
        return payed;
    }

    // This method return true if the tuple it's been correctly modified, false
    // otherwise
    public static Boolean updateClosedSaleTransactionDiscountRate(Integer transactionId, Double discountRate) {

        if (transactionId == null || discountRate == null)
            return null;

        if (getClosedSaleTransaction(transactionId) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE ClosedSaleTransaction SET DiscountRate = ? WHERE ticketNumber = ?";
            ps = con.prepareStatement(sql);
            ps.setDouble(1, discountRate);
            ps.setInt(2, transactionId);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
            }
        }
        return returnValue;
    }

    // This method return true if the tuple it's been correctly modified, false
    // otherwise
    public static Boolean updateClosedSaleTransactionPrice(Integer transactionId, Double price) {

        if (transactionId == null || price == null)
            return null;

        if (getClosedSaleTransaction(transactionId) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE ClosedSaleTransaction SET price = ? WHERE ticketNumber = ?";
            ps = con.prepareStatement(sql);
            ps.setDouble(1, price);
            ps.setInt(2, transactionId);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in updateClosedSaleTransactionPrice1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in updateClosedSaleTransactionPrice2");
            }
        }
        return returnValue;
    }

    // This method return true if the tuple it's been correctly deleted, false
    // otherwise
    public static Boolean deleteClosedSaleTransaction(Integer transactionId) {

        if (transactionId == null)
            return null;

        if (getClosedSaleTransaction(transactionId) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;
        try {
            String sql = "DELETE FROM ClosedSaleTransaction WHERE ticketNumber = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in deleteClosedSaleTransaction1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in deleteClosedSaleTransaction2");
            }
        }
        return returnValue;
    }

    public static Integer getClosedSaleTransactionCount() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int max = -1;
        try {
            String sql = "SELECT MAX(ticketNumber) FROM ClosedSaleTransaction";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                max = rs.getInt(1);
            }
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getClosedSaleTransactionCount1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getClosedSaleTransactionCount2");
            }
        }
        return max;
    }

    public static Boolean updateClosedSaleTransactionPayed(Integer transactionId, Boolean payed) {

        if (transactionId == null || payed == null)
            return null;

        if (getClosedSaleTransaction(transactionId) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE ClosedSaleTransaction SET payed = ? WHERE ticketNumber = ?";
            ps = con.prepareStatement(sql);
            ps.setBoolean(1, payed);
            ps.setInt(2, transactionId);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
            }
        }
        return returnValue;
    }

    // -------------------------------------------------------------//
    // ----------------------ReturnTransaction----------------------//
    // -------------------------------------------------------------//

    public static Boolean createReturnSaleTransaction(Integer id, Integer ticketNumber, Double price,
            Double discountRate) {

        if (id == null || ticketNumber == null || price == null || discountRate == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;
        try {
            String sql = "INSERT INTO ReturnTransaction(id , price, discountRate, payed, saleTransactionId) VALUES(?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setDouble(2, price);
            ps.setDouble(3, discountRate);
            ps.setBoolean(4, false);
            ps.setInt(5, ticketNumber);
            // true if the first result is a ResultSet object; false if the first result is
            // an update count or there is no result
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + " addReturnSaleTransaction1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + " addReturnSaleTransaction2");
            }
        }
        return returnValue;
    }

    // The returnId parameter referes the ticketNumber of the saleTransaction not
    // the DB id of the return Transaction
    public static SaleTransaction getReturnTransaction(Integer returnId) {

        if (returnId == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        SaleTransaction returnSale = null;

        try {
            String sql = "SELECT * FROM ReturnTransaction WHERE saleTransactionId = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, returnId);
            rs = ps.executeQuery();
            if (!rs.next())
                // if the list is empty
                throw new SQLException();
            do {
                int ticketNumber = rs.getInt("saleTransactionId");
                Double price = rs.getDouble("price");
                Double discountRate = rs.getDouble("discountRate");
                returnSale = new SaleTransactionClass();
                returnSale.setPrice(price);
                returnSale.setTicketNumber(ticketNumber);
                returnSale.setDiscountRate(discountRate);
            } while (rs.next());
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getReturnTransaction1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getReturnTransaction2");
            }
        }
        return returnSale;
    }

    public static Boolean getReturnSaleTransactionPayed(Integer transactionId) {

        if (transactionId == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean payed = false;

        try {
            String sql = "SELECT payed FROM ReturnTransaction WHERE saleTransactionId = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            rs = ps.executeQuery();
            payed = rs.getBoolean(1);
            // //System.out.println("#ClosedSaleTransaction saved into db: " + count);
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getReturnSaleTransactionPayed");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getReturnSaleTransactionPayed");
            }
        }
        return payed;
    }

    public static Boolean updateReturnSaleTransactionPayed(Integer transactionId, Boolean payed) {

        if (transactionId == null || payed == null)
            return null;

        if (DBAPIs.getReturnTransaction(transactionId) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE ReturnTransaction SET payed = ? WHERE saleTransactionId = ?";
            ps = con.prepareStatement(sql);
            ps.setBoolean(1, payed);
            ps.setInt(2, transactionId);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "updateReturnSaleTransactionPayed1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "updateReturnSaleTransactionPayed2");
            }
        }
        return returnValue;
    }

    public static Integer getReturnSaleTransactionCount() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int max = -1;
        try {
            String sql = "SELECT MAX(id) FROM ReturnTransaction";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                max = rs.getInt(1);
            }
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getReturnSaleTransactionCount1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getReturnSaleTransactionCount2");
            }
        }
        return max;
    }

    // -------------------------------------------------------------//
    // -------------------------TicketEntry-------------------------//
    // -------------------------------------------------------------//

    public static Boolean createTicketEntry(Integer ticketNumber, TicketEntry entry) {

        if (ticketNumber == null || entry == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "INSERT INTO TicketEntry(ticketNumber, barcode, productDescription, amount, pricePerUnit, discountRate) VALUES(?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, ticketNumber);
            ps.setString(2, entry.getBarCode());
            ps.setString(3, entry.getProductDescription());
            ps.setInt(4, entry.getAmount());
            ps.setDouble(5, entry.getPricePerUnit());
            ps.setDouble(6, entry.getDiscountRate());
            returnValue = !ps.execute();
        } catch (SQLException ex) {
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
            }
        }
        return returnValue;
    }

    public static TicketEntry getSaleTicketEntry(Integer transactionId, String productCode) {

        if (transactionId == null || productCode == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        TicketEntry entry = null;

        try {
            String sql = "SELECT barcode, productDescription, amount, pricePerUnit, discountRate, ticketNumber FROM TicketEntry WHERE ticketNumber = ? AND barcode = ? ";
            ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            ps.setString(2, productCode);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the SaleTransaction desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            entry = new TicketEntryClass(rs.getString("barcode"), rs.getString("productDescription"),
                    rs.getInt("amount"), rs.getDouble("pricePerUnit"));
            entry.setDiscountRate(rs.getDouble("discountRate"));
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getSaleTicketEntry1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getSaleTicketEntry2");
            }
        }
        return entry;
    }

    public static Boolean deleteTicketEntry(Integer ticketNumber, String barcode, Double discountRate) {

        if (ticketNumber == null || barcode == null || discountRate == null)
            return null;

        if (getSaleTicketEntry(ticketNumber, barcode) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "DELETE FROM TicketEntry WHERE ticketNumber = ? AND barcode = ? AND discountRate = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, ticketNumber);
            ps.setString(2, barcode);
            ps.setDouble(3, discountRate);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
            }
        }
        return returnValue;
    }

    public static List<TicketEntry> getSaleTicketEntries(Integer transactionId) {

        if (transactionId == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TicketEntry> entries = new ArrayList<>();

        try {
            String sql = "SELECT * FROM TicketEntry WHERE ticketNumber = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            rs = ps.executeQuery();
            if (!rs.next())
                // if the list is empty
                throw new SQLException();
            do {
                String barcode = rs.getString("barcode");
                String description = rs.getString("productDescription");
                int amount = rs.getInt("amount");
                Double price = rs.getDouble("pricePerUnit");
                Double discountRate = rs.getDouble("discountRate");
                TicketEntry entry = new TicketEntryClass(barcode, description, amount, price);
                entry.setDiscountRate(discountRate);
                entries.add(entry);
            } while (rs.next());
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getSaleTicketEntries1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getSaleTicketEntries2");
            }
        }
        return entries;
    }

    // This method return true if the tuple it's been correctly modified, false
    // otherwise
    public static Boolean updateTicketEntryAmount(Integer ticketNumber, String barcode, Integer amount,
            Double discountRate) {

        if (ticketNumber == null || barcode == null || amount == null || discountRate == null)
            return null;

        if (getSaleTicketEntry(ticketNumber, barcode) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE TicketEntry SET Amount = ? WHERE ticketNumber = ? AND barcode = ? AND discountRate = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, amount);
            ps.setInt(2, ticketNumber);
            ps.setString(3, barcode);
            ps.setDouble(4, discountRate);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
            }
        }
        return returnValue;
    }

    // -------------------------------------------------------------------//
    // -------------------------ReturnTicketEntry-------------------------//
    // -------------------------------------------------------------------//

    // ticketNumber is the saleTransactionId, thus the returnId is an internal db id
    public static Boolean createReturnTicketEntry(Integer ticketNumber, TicketEntry entry, Integer returnId) {

        if (ticketNumber == null || entry == null || returnId == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "INSERT INTO ReturnTicketEntry(saleTransactionId, barcode, productDescription, amount, pricePerUnit, discountRate, returnTransactionId) VALUES(?,?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, ticketNumber);
            ps.setString(2, entry.getBarCode());
            ps.setString(3, entry.getProductDescription());
            ps.setInt(4, entry.getAmount());
            ps.setDouble(5, entry.getPricePerUnit());
            ps.setDouble(6, entry.getDiscountRate());
            ps.setInt(7, returnId);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
            }
        }
        return returnValue;
    }

    // This method using returnId differenciate ticketEntries who belongs to the
    // same returnTransaction,
    // associated to the same productCode, but returned in different moments.
    // It's possible to differenciate as we said thanks to the returnId, an internal
    // db id.
    public static TicketEntry getReturnTicketEntry(Integer transactionId, String productCode, Integer returnId) {

        if (transactionId == null || productCode == null || returnId == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        TicketEntry entry = null;

        try {
            String sql = "SELECT barcode, productDescription, amount, pricePerUnit, discountRate, saleTransactionId FROM ReturnTicketEntry WHERE saleTransactionId = ? AND barcode = ? AND returnTransactionId = ? ";
            ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            ps.setString(2, productCode);
            ps.setInt(3, returnId);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the SaleTransaction desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            entry = new TicketEntryClass(rs.getString("barcode"), rs.getString("productDescription"),
                    rs.getInt("amount"), rs.getDouble("pricePerUnit"));
            entry.setDiscountRate(rs.getDouble("discountRate"));
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getReturnSaleTicketEntry1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getReturnSaleTicketEntry2");
            }
        }
        return entry;
    }

    // This method using returnId differenciate ticketEntries who belongs to the
    // same returnTransaction,
    // but returned in different moments.
    // It's possible to differenciate as we said thanks to the returnId, an internal
    // db id.
    public static List<TicketEntry> getReturnTicketEntries(Integer transactionId, Integer returnId) {

        if (transactionId == null || returnId == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TicketEntry> entries = new ArrayList<>();

        try {
            String sql = "SELECT * FROM ReturnTicketEntry WHERE saleTransactionId = ? AND returnTransactionId = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            ps.setInt(2, returnId);
            rs = ps.executeQuery();
            if (!rs.next())
                // if the list is empty
                throw new SQLException();
            do {
                String barcode = rs.getString("barcode");
                String description = rs.getString("productDescription");
                int amount = rs.getInt("amount");
                Double price = rs.getDouble("pricePerUnit");
                Double discountRate = rs.getDouble("discountRate");
                TicketEntry entry = new TicketEntryClass(barcode, description, amount, price);
                entry.setDiscountRate(discountRate);
                entries.add(entry);
            } while (rs.next());
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getReturnTicketEntries1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getReturnTicketEntries2");
            }
        }
        return entries;
    }

    // -----------------------------------------------------------//
    // ------------------------ProductType------------------------//
    // -----------------------------------------------------------//

    public static Boolean createProductType(ProductTypeClass product) {

        if (product == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "INSERT INTO ProductType(id, productDescription, barcode, location, pricePerUnit, note) VALUES(?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, product.getId());
            ps.setString(2, product.getProductDescription());
            ps.setString(3, product.getBarCode());
            ps.setString(4, product.getLocation());
            ps.setDouble(5, product.getPricePerUnit());
            ps.setString(6, product.getNote());
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in createProduct1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in createProduct2");
            }
        }
        return returnValue;
    }

    // This method returns -1 in case of error;
    public static int getProductTypeCount() {
        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = -1;
        try {
            String sql = "SELECT MAX(id) FROM ProductType";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();
            count = rs.getInt(1);
            // //System.out.println("#ProductTypes saved into db: " + count);
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getProductCount1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getProductCount2");
            }
        }
        return count;
    }

    public static ProductType getProductTypeFromId(Integer id) {

        if (id == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductType product = null;

        try {
            String sql = "SELECT id, productDescription, barcode, pricePerUnit, quantity, location, note FROM ProductType WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the ProductType doesn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();

            int id1 = rs.getInt("id");
            String description = rs.getString("productDescription");
            String note = rs.getString("note");
            String barcode = rs.getString("barcode");
            Double price = rs.getDouble("pricePerUnit");
            product = new ProductTypeClass(note, description, barcode, price, id1);
            product.setLocation(rs.getString("location"));
            product.setQuantity(rs.getInt("quantity"));
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getProductType1 - Probably user not
            // exists");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getProductType2");
            }
        }
        return product;
    }

    public static Boolean updateProductType(Integer id, String newDescription, String newCode, Double newPrice,
            String newNote) {

        if (id == null || newDescription == null || newCode == null || newPrice == null)
            return null;

        if (getProductTypeFromId(id) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE ProductType SET productDescription = ?, barcode = ?, pricePerUnit = ?, note = ? WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, newDescription);
            ps.setString(2, newCode);
            ps.setDouble(3, newPrice);
            ps.setString(4, newNote);
            ps.setInt(5, id);
            returnValue = !ps.execute();
            // System.out.println("Product Type updated!");
        } catch (SQLException ex) {
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
            }
        }
        return returnValue;
    }

    public static Boolean deleteProductType(Integer id) {

        if (id == null)
            return null;

        if (getProductTypeFromId(id) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "DELETE FROM ProductType WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString());
            }
        }
        return returnValue;
    }

    public static List<ProductType> getAllProductTypes() {
        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ProductType> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ProductType";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the User desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            do {
                int id1 = rs.getInt("id");
                String description = rs.getString("productDescription");
                String note = rs.getString("note");
                String barcode = rs.getString("barcode");
                Double price = rs.getDouble("pricePerUnit");
                Integer quantity = rs.getInt("quantity");
                String location = rs.getString("location");
                ProductType product = new ProductTypeClass(note, description, barcode, price, id1);
                product.setQuantity(quantity);
                product.setLocation(location);
                list.add(product);
            } while (rs.next());
            con.close();
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "getAllProductTypes1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "getAllProductTypes2");
            }
        }
        return list;
    }

    public static ProductType getProductTypeFromBarcode(String barCode) {

        if (barCode == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProductType product = null;

        try {
            String sql = "SELECT id, productDescription, barcode, pricePerUnit, quantity, location, note FROM ProductType WHERE barcode = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, barCode);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the ProductType doesn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            int id = rs.getInt("id");
            String productDescription = rs.getString("productDescription");
            String note = rs.getString("note");
            String barCode1 = rs.getString("barcode");
            Double pricePerUnit = rs.getDouble("pricePerUnit");
            product = new ProductTypeClass(note, productDescription, barCode1, pricePerUnit, id);
            product.setLocation(rs.getString("location"));
            product.setQuantity(rs.getInt("quantity"));
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getProductTypeFromBarcode1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getProductTypeFromBarcode2");
            }
        }
        return product;
    }

    public static Boolean updateProductTypeQuantity(Integer id, Integer newQuantity) {

        if (id == null || newQuantity == null)
            return null;

        if (getProductTypeFromId(id) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE ProductType SET quantity = ? WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, newQuantity);
            ps.setInt(2, id);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "updateProductTypeQuantity1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "updateProductTypeQuantity2");
            }
        }
        return returnValue;
    }

    public static Boolean updateProductTypePosition(Integer id, String newPosition) {

        if (id == null || newPosition == null)
            return null;

        if (getProductTypeFromId(id) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE ProductType SET location = ? WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, newPosition);
            ps.setInt(2, id);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "updateProductTypePosition1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "updateProductTypePosition2");
            }
        }
        return returnValue;
    }

    // ------------------------------------------------------------//
    // ----------------------------Order---------------------------//
    // ------------------------------------------------------------//

    public static Boolean createOrder(Order order) {

        if (order == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "INSERT INTO Orders(orderId, balanceId, productCode, pricePerUnit, quantity, status) VALUES(?,?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, order.getOrderId());
            ps.setInt(2, order.getBalanceId());
            ps.setString(3, order.getProductCode());
            ps.setDouble(4, order.getPricePerUnit());
            ps.setInt(5, order.getQuantity());
            ps.setString(6, order.getStatus());
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in createOrder1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in createOrder2");
            }
        }
        return returnValue;
    }

    // This method returns -1 in case of error;
    public static Integer getOrderCount() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int max = -1;
        try {
            String sql = "SELECT MAX(orderId) FROM Orders";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                max = rs.getInt(1);
            }
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getOrderCount1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getOrderCount2");
            }
        }
        return max;
    }

    public static Order getOrderFromId(Integer id) {

        if (id == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Order order = null;

        try {
            String sql = "SELECT orderId, balanceId, productCode, pricePerUnit, quantity, status FROM Orders WHERE orderId = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the ProductType doesn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            Integer balanceId = rs.getInt("balanceId");
            Integer quantity = rs.getInt("quantity");
            String status = rs.getString("status");
            String productCode = rs.getString("productCode");
            Double price = rs.getDouble("pricePerUnit");
            order = new OrderClass(id, balanceId, productCode, price, quantity, status);

        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getOrderFromId1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getOrderFromId2");
            }
        }
        return order;
    }

    public static Boolean updateOrder(Order order) {

        if (order == null)
            return null;

        if (getOrderFromId(order.getOrderId()) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE Orders SET balanceId = ?, productCode = ?, pricePerUnit = ?, quantity = ?, status = ?  WHERE orderId = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, order.getBalanceId());
            ps.setString(2, order.getProductCode());
            ps.setDouble(3, order.getPricePerUnit());
            ps.setInt(4, order.getQuantity());
            ps.setString(5, order.getStatus());
            ps.setInt(6, order.getOrderId());
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "updateOrder1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "updateOrder2");
            }
        }
        return returnValue;
    }

    public static List<Order> getAllOrders() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Order> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Orders";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the User desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            do {
                Integer balanceId = rs.getInt("balanceId");
                Integer quantity = rs.getInt("quantity");
                String status = rs.getString("status");
                String productCode = rs.getString("productCode");
                Double price = rs.getDouble("pricePerUnit");
                Integer id = rs.getInt("orderId");
                Order order = new OrderClass(id, balanceId, productCode, price, quantity, status);
                list.add(order);
            } while (rs.next());
            con.close();
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "getAllOrders1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "getAllOrders2");
            }
        }
        return list;
    }

    // -----------------------------------------------------------//
    // ----------------------------User---------------------------//
    // -----------------------------------------------------------//

    public static Boolean createUser(UserClass user) {

        if (user == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "INSERT INTO User(id, username, password, role) VALUES(?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in createUser1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in createUser2");
            }
        }
        return returnValue;
    }

    public static User getUserFromId(Integer id) {

        if (id == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            String sql = "SELECT id, username, password, role FROM User WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the User desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            int id1 = rs.getInt("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String role = rs.getString("role");
            user = new UserClass(id1, username, password, role);
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getUser1 - Probably user not exists");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getUser2");
            }
        }
        return user;
    }

    public static User getUserFromUsername(String username) {

        if (username == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            String sql = "SELECT id, username, password, role FROM User WHERE username = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the User desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();

            int id = rs.getInt("id");
            String username1 = rs.getString("username");
            String password = rs.getString("password");
            String role = rs.getString("role");
            user = new UserClass(id, username1, password, role);
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getUser1 - Probably user not exists");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getUser2");
            }
        }
        return user;
    }

    // This method return true if the tuple it's been correctly deleted, false
    // otherwise
    public static Boolean deleteUser(Integer id) {

        if (id == null)
            return null;

        if (getUserFromId(id) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "DELETE FROM User WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in deleteUser1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in deleteUser2");
            }
        }
        return returnValue;
    }

    public static Boolean updateUserRights(Integer id, String role) {

        if (id == null || role == null)
            return null;

        if (getUserFromId(id) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE User SET role = ? WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, role);
            ps.setInt(2, id);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in updateUserRights1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in updateUserRights2");
            }
        }
        return returnValue;
    }

    // This method returns -1 in case of error;
    public static Integer getUserCount() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int max = -1;

        try {
            String sql = "SELECT MAX(id) FROM User";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                max = rs.getInt(1);
            }
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getUserCount1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getUserCount2");
            }
        }
        return max;
    }

    public static List<User> getAllUsers() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM User";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the User desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            do {
                int id1 = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                User user = new UserClass(id1, username, password, role);
                list.add(user);
            } while (rs.next());
            con.close();
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "getAllUsers1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "getAllUsers2");
            }
        }
        return list;
    }

    // -----------------------------------------------------------//
    // --------------------------Customer-------------------------//
    // -----------------------------------------------------------//

    public static Boolean createCustomer(CustomerClass customer) {

        if (customer == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "INSERT INTO Customer(id, customerName) VALUES(?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getCustomerName());
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in createCustomer1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in createCustomer2");
            }
        }
        return returnValue;
    }

    // This method returns -1 in case of error;
    public static Integer getCustomerCount() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int max = -1;

        try {
            String sql = "SELECT MAX(id) FROM Customer";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                max = rs.getInt(1);
            }
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getCustomerCount1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getCustomerCount2");
            }
        }
        return max;
    }

    // This method returns -1 in case of error;
    public static Integer getCustomerCardMax() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int max = -1;

        try {
            String sql = "SELECT MAX(customerCard) FROM Customer";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                max = rs.getInt(1);
            }
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getCustomerCount1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getCustomerCount2");
            }
        }
        return max;
    }

    public static Customer getCustomerFromId(Integer id) {

        if (id == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            String sql = "SELECT id, customerName, customerCard, points FROM Customer WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the customer doesn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            int id1 = rs.getInt("id");
            String customerName = rs.getString("customerName");
            String customerCard = rs.getString("customerCard");
            int points = rs.getInt("points");
            customer = new CustomerClass(id1, customerName);
            customer.setCustomerCard(customerCard);
            customer.setPoints(points);

        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getCustomerFromId1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getCustomerFromId2");
            }
        }
        return customer;
    }

    public static Customer getCustomerFromName(String customerName) {

        if (customerName == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            String sql = "SELECT id, customerName, customerCard, points FROM Customer WHERE customerName = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, customerName);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the customer doesn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            int id = rs.getInt("id");
            String customerName1 = rs.getString("customerName");
            String customerCard = rs.getString("customerCard");
            int points = rs.getInt("points");
            customer = new CustomerClass(id, customerName1);
            customer.setCustomerCard(customerCard);
            customer.setPoints(points);

        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getCustomerFromName1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getCustomerFromName2");
            }
        }
        return customer;
    }

    public static Customer getCustomerFromCard(String customerCard) {

        if (customerCard == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            String sql = "SELECT id, customerName, customerCard, points FROM Customer WHERE customerCard = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, customerCard);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the customer doesn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            int id = rs.getInt("id");
            String customerName = rs.getString("customerName");
            String customerCard1 = rs.getString("customerCard");
            int points = rs.getInt("points");
            customer = new CustomerClass(id, customerName);
            customer.setCustomerCard(customerCard1);
            customer.setPoints(points);

        } catch (SQLException ex) {
            //// System.out.println(ex.toString() + "in getCustomerFromCard1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                //// System.out.println(ex.toString() + "in getCustomerFromCard2");
            }
        }
        return customer;
    }

    public static Boolean updateCustomer(Customer customer) {

        if (customer == null)
            return null;

        if (getCustomerFromId(customer.getId()) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE Customer SET customerName = ?, customerCard = ? WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getCustomerCard());
            ps.setInt(3, customer.getId());
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in updateCustomer1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in updateCustomer2");
            }
        }
        return returnValue;
    }

    public static Boolean updateCustomerCard(Integer id, String customerCard) {

        if (id == null || customerCard == null)
            return null;

        if (getCustomerFromId(id) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE Customer SET customerCard = ? WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, customerCard);
            ps.setInt(2, id);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in updateCustomerCard1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in updateCustomerCard2");
            }
        }
        return returnValue;
    }

    public static Boolean deleteCustomer(Integer id) {

        if (id == null)
            return null;

        if(getCustomerFromId(id) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "DELETE FROM Customer WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in deleteCustomer1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in deleteCustomer2");
            }
        }
        return returnValue;
    }

    public static List<Customer> getAllCustomers() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Customer> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Customer";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the Customer desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            do {
                Integer id = rs.getInt("id");
                String customerName = rs.getString("customerName");
                String customerCard = rs.getString("customerCard");
                Integer points = rs.getInt("points");
                Customer customer = new CustomerClass(id, customerName);
                customer.setCustomerCard(customerCard);
                customer.setPoints(points);
                list.add(customer);
            } while (rs.next());
            con.close();
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "getAllCustomers1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "getAllCustomers2");
            }
        }
        return list;
    }

    public static Boolean updatePointsOnCard(String customerCard, Integer points) {

        if (customerCard == null || points == null)
            return null;

        if (getCustomerFromCard(customerCard) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE Customer SET points = ? WHERE customerCard = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, points);
            ps.setString(2, customerCard);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in updatePointsOnCard1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in updatePointsOnCard2");
            }
        }
        return returnValue;
    }

    // -----------------------------------------------------------//
    // ----------------------BalanceOperation---------------------//
    // -----------------------------------------------------------//
    // This method returns -1 in case of error;
    public static Integer getBalanceOperationCount() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int max = -1;

        try {
            String sql = "SELECT MAX(balanceid) FROM BalanceOperation";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                max = rs.getInt(1);
            }
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in getBalanceOperationCount1");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getBalanceOperationCount2");
            }
        }
        return max;
    }

    public static Boolean createBalanceOperation(BalanceOperation balanceOp) {

        if (balanceOp == null)
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "INSERT INTO BalanceOperation(balanceId, date, money, type) VALUES(?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, balanceOp.getBalanceId());
            ps.setDate(2, Date.valueOf(balanceOp.getDate()));
            ps.setDouble(3, balanceOp.getMoney());
            ps.setString(4, balanceOp.getType());
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in createBalanceOperation1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in createBalanceOperation2");
            }
        }
        return returnValue;
    }

    public static List<BalanceOperation> getAllBalanceOperations() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BalanceOperation> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM BalanceOperation";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the User desn't exists throws an SQLException
                // catched in the catch statement; it returns null.
                throw new SQLException();
            do {
                int balanceId = rs.getInt("balanceId");
                LocalDate date = rs.getDate("date").toLocalDate();
                Double money = rs.getDouble("money");
                String type = rs.getString("type");
                BalanceOperation balanceOp = new BalanceOperationClass(balanceId, date, money, type);
                list.add(balanceOp);
            } while (rs.next());
            con.close();
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            // //System.out.println(ex.toString() + "in getAllBalanceOperation1"); (Probably
            // there are no BalanceOperations)
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in getAllBalanceOperation2");
            }
        }
        return list;
    }

    public static Boolean reset() {

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String[] sqls = {
                "DELETE FROM ClosedSaleTransaction",
                "DELETE FROM ReturnTransaction",
                "DELETE FROM ReturnTicketEntry",
                "DELETE FROM BalanceOperation",
                "DELETE FROM ProductType",
                "DELETE FROM TicketEntry",
                "DELETE FROM Orders",
                "DELETE FROM User",
                "DELETE FROM Customer",
                "DELETE FROM RFIDProducts"
            };
            for (String sql: sqls) {
                ps = con.prepareStatement(sql);
                returnValue = !ps.execute();
                if (returnValue == false) {
                    return false;
                }
            }
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in reset1");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in reset2");
            }
        }
        return returnValue;

    }

    // -----------------------------------------------------------//
    // ----------------------------RFID---------------------------//
    // -----------------------------------------------------------//

    public static Boolean createRFID(String RFID, String barcode) {

        if(RFID == null || barcode == null || RFID.isEmpty() || barcode.isEmpty())
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "INSERT INTO RFIDProducts(RFID, barcode, sold) VALUES(?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, RFID);
            ps.setString(2, barcode);
            ps.setBoolean(3, false);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
             //System.out.println(ex.toString() + "in createRFID");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in createRFID");
            }
        }
        return returnValue;
    }

    public static Boolean deleteRFID(String RFID) {

        if(RFID == null || RFID.isEmpty())
            return null;

        if (getBarcodeFromRFID(RFID) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "DELETE FROM RFIDProducts WHERE RFID = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, RFID);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in deleteRFID");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in deleteRFID");
            }
        }
        return returnValue;
    }

    public static String getBarcodeFromRFID(String RFID) {

        if(RFID == null || RFID.isEmpty())
            return null;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String barcode = null;

        try {
            String sql = "SELECT barcode FROM RFIDProducts WHERE RFID = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, RFID);
            rs = ps.executeQuery();
            if (!rs.next())
                // If the RFID
                throw new SQLException();
            barcode = rs.getString("barcode");

        } catch (SQLException ex) {
            //System.out.println(ex.toString() + "in getBarcodeFromRFID");
        } finally {
            try {
                con.close();
                ps.close();
                rs.close();
            } catch (SQLException ex) {
                //// System.out.println(ex.toString() + "in getBarcodeFromRFID");
            }
        }
        return barcode;
    }

    public static Boolean updateRFIDSold(String RFID, Boolean sold) {

        if (RFID == null || RFID.isEmpty() || sold == null)
            return null;

        if (getBarcodeFromRFID(RFID) == null)
            return false;

        Connection con = DBConnection.connect();
        PreparedStatement ps = null;
        Boolean returnValue = false;

        try {
            String sql = "UPDATE RFIDProducts SET sold = ? WHERE RFID = ?";
            ps = con.prepareStatement(sql);
            ps.setBoolean(1, sold);
            ps.setString(2, RFID);
            returnValue = !ps.execute();
        } catch (SQLException ex) {
            // System.out.println(ex.toString() + "in updateRFIDSaled");
        } finally {
            try {
                con.close();
                ps.close();
            } catch (SQLException ex) {
                // System.out.println(ex.toString() + "in updateRFIDSaled");
            }
        }
        return returnValue;
    }


}
