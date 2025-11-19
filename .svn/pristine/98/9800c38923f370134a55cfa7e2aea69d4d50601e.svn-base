/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class CloseDeliveryEditTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtUpdateDeliveryInfo;
    protected PreparedStatement pstmtInsertInventoryHistory;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public CloseDeliveryEditTransaction() {
        super();
        SetTransactionType(SIDWebTransaction.SaveType);
    }

    /**
     * Checks to see if the node Array is supported
     *
     * @param nodeArray
     * @return <B>true</B> if the nodeArray is supported. <B>false</B> otherwise
     * @exception (none)
     */
    @Override
    public boolean Supports(xmlNodeArray nodeArray) {

        //Add tag names as comma separated Strings to the mandatoryTags array
        String[] mandatoryTags = {};

        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<CloseDeliveryEditTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<CloseDeliveryEditTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<CloseDeliveryEditTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<CloseDeliveryEditTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
                errArray.add("OPTIONAL_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        return true;
    }

    /**
     * Prepares the SQL statements to be executed
     *
     * @return <B>true</B> for successful preparation; <B>false</B> for
     * unsuccessful preparation
     * @exception (none)
     */
    @Override
    public synchronized boolean PrepareStatements() {
        //Note1 : Use PreparedStatements instead of Statements where ever possible
        //Note2 : If transaction contains no prepared statements, delete entire function
        //        Unless there are nested transaction, then Prepare will call those.
        try {
            Connection con = this.GetSIDDataBase().GetConnection();

//            pstmtDeliveryInfo = con.prepareStatement("select *, "
//                    + "date_format(CreationDate,'%d/%m/%Y') as fCreationDate "
//                    + "from deliveryhistory "
//                    + "where idDelivery = ? "
//                    + "and Status  = 'Nueva' ");
            pstmtUpdateDeliveryInfo = con.prepareStatement("update delivery set "
                    + "ConfirmPickup = ? , "//1. confirmpickup
                    + "ConfirmTrailer = ? , "//2. confrmtrailertype
                    + "ConfirmTrailerNumber = ? , "//3. confirmtrailernumber
                    + "ConfirmQty = ? , "//4. confirmqty
                    + "ConfirmWeight = ? , "//5. confirmweight
                    + "Trucking = ? , "//6. trucking
                    + "DriverName = ? , "//7. drivername
                    + "DriverTel = ? ,"//8. drivertel
                    + "PickupNumber = ? ,"//9. pickup
                    + "TrailerType = ? ,"//10. trailertype
                    + "TrailerNumber = ? ,"//11. trailernumber
                    + "Quantity = ? ,"//12. quantity
                    + "Weight = ? ,"//13. weight
                    + "UOM = ? ,"//14. uom
                    + "Dock = ? ,"//15. dock
                    + "Destination = ? ,"//16. destiny
                    + "Comments = ? "//17. comments
                    + "where idDelivery = ? ");//18. idDelivery

            pstmtInsertInventoryHistory = con.prepareStatement("INSERT INTO warehousehistory"
                    + "(ReferenceNumber, "
                    + "Movement, "
                    + "Trucking, "
                    + "Trailer, "
                    + "Line, "
                    + "Dock, "
                    + "DriverName, "
                    + "Seal, "
                    + "User, "
                    + "InsertDate, "
                    + "Comments,"
                    + "Product,"
                    + "Quantity, "
                    + "Destination)"
                    + "VALUES("
                    + "?, " //1. ReferenceNumber
                    + "'Updated', " //Movement
                    + "?, " //2. Trucking
                    + "?, " //3. Trailer
                    + "?, " //4. Line
                    + "?, " //5. Dock
                    + "?, " //6. DriverName
                    + "?, " //7. Sello                    
                    + "?, " //8. User
                    + "now(), " //InsertDate
                    + "?, " //9. Comments
                    + "?, " //10. Producto
                    + "?, " //11. Quantity
                    + "? " //12. Destiny
                    + ")");

//            this.addPreparedStatement(pstmtDeliveryInfo);
            this.addPreparedStatement(pstmtUpdateDeliveryInfo);
            this.addPreparedStatement(pstmtInsertInventoryHistory);

            return true;
        } catch (SQLException e) {
            System.out.println("CloseDeliveryEditTransaction::PrepareStatements> SQLException: " + e.getMessage());
            return false;
        }
    }

    /**
     * executes sql statements using input arguments and returns result
     *
     * @return valid node array if successful else null
     * @exception SQLException if sql error occurs
     * @exception Exception if non sql error occurs
     */
    @Override
    public synchronized xmlNodeArray Execute() throws SQLException, Exception {
        Connection conn = null;
        xmlNodeArray resultArray = null;
        ResultSet rset = null;
        ResultSet rset1 = null;
        int rowsAffected = 0;
        int idDelivery = 0;
        String pickupconfirmed = "";
        String driverName = "";
        String driverTel = "";
        String trailerTypeConfirmed = "";
        String trailerNumberConfirmed = "";
        String trucking = "";
        double quantityConfirmed = 0;
        double weightConfirmed = 0;

        String pickup = "";
        String trailertype = "";
        String trailernumber = "";
        double quantity = 0;
        double weight = 0;
        String uom = "";
        String dock = "";
        String destiny = "";
        String comments = "";
        int idUser = 0;

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();
            
            if (GetNodeArray().existValue("idUser")) {
                idUser = GetNodeArray().find("idUser").getIntValue();
            }

            if (GetNodeArray().existValue("idDelivery")) {
                idDelivery = GetNodeArray().find("idDelivery").getIntValue();
            }

            pickupconfirmed = GetNodeArray().find("pickupConfirmed").getStringValue();
            driverName = GetNodeArray().find("driverName").getStringValue();
            driverTel = GetNodeArray().find("driverTel").getStringValue();
            trailerTypeConfirmed = GetNodeArray().find("trailerTypeConfirmed").getStringValue();
            trailerNumberConfirmed = GetNodeArray().find("trailerNumberConfirmed").getStringValue();
            trucking = GetNodeArray().find("trucking").getStringValue();
            quantityConfirmed = GetNodeArray().find("qtyinputConfirmed").getDoubleValue();
            weightConfirmed = GetNodeArray().find("weightConfirmed").getDoubleValue();

            pickup = GetNodeArray().find("pickup").getStringValue();
            trailertype = GetNodeArray().find("trailertype").getStringValue();
            trailernumber = GetNodeArray().find("trailernumber").getStringValue();
            quantity = GetNodeArray().find("qtyinput").getDoubleValue();
            weight = GetNodeArray().find("weight").getDoubleValue();
            uom = GetNodeArray().find("uom").getStringValue();
            dock = GetNodeArray().find("dock").getStringValue();
            destiny = GetNodeArray().find("destiny").getStringValue();
            comments = GetNodeArray().find("comments").getStringValue();

            resultArray.add("SelectedPickup", pickup);
            resultArray.add("Action", "Search");

            pstmtUpdateDeliveryInfo.setString(1, pickupconfirmed);
            pstmtUpdateDeliveryInfo.setString(2, trailerTypeConfirmed);
            pstmtUpdateDeliveryInfo.setString(3, trailerNumberConfirmed);
            pstmtUpdateDeliveryInfo.setDouble(4, quantityConfirmed);
            pstmtUpdateDeliveryInfo.setDouble(5, weightConfirmed);
            pstmtUpdateDeliveryInfo.setString(6, trucking);
            pstmtUpdateDeliveryInfo.setString(7, driverName);
            pstmtUpdateDeliveryInfo.setString(8, driverTel);
            pstmtUpdateDeliveryInfo.setString(9, pickup);
            pstmtUpdateDeliveryInfo.setString(10, trailertype);
            pstmtUpdateDeliveryInfo.setString(11, trailernumber);
            pstmtUpdateDeliveryInfo.setDouble(12, quantity);
            pstmtUpdateDeliveryInfo.setDouble(13, weight);
            pstmtUpdateDeliveryInfo.setString(14, uom);
            pstmtUpdateDeliveryInfo.setString(15, dock);
            pstmtUpdateDeliveryInfo.setString(16, destiny);
            pstmtUpdateDeliveryInfo.setString(17, comments);
            pstmtUpdateDeliveryInfo.setInt(18, idDelivery);
            rowsAffected = pstmtUpdateDeliveryInfo.executeUpdate();

            //Insertar historial inventario
            pstmtInsertInventoryHistory.setString(1, pickup);
            pstmtInsertInventoryHistory.setString(2, trucking);
            pstmtInsertInventoryHistory.setString(3, trailernumber);
            pstmtInsertInventoryHistory.setString(4, "");
            pstmtInsertInventoryHistory.setString(5, dock);
            pstmtInsertInventoryHistory.setString(6, driverName);
            pstmtInsertInventoryHistory.setString(7, "");
            pstmtInsertInventoryHistory.setInt(8, idUser);
            pstmtInsertInventoryHistory.setString(9, comments);
            pstmtInsertInventoryHistory.setString(10, "");
            pstmtInsertInventoryHistory.setDouble(11, quantity);
            pstmtInsertInventoryHistory.setString(12, destiny);
            rowsAffected = pstmtInsertInventoryHistory.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit();
                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", "Delivery has been processed succesfully");
                resultArray.add("RESPONSE_DETAIL", "");
            } else {
                conn.rollback();
                resultArray.add("error", "FAIL something is wrong with the delivery process.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "FAIL something is wrong with the delivery process.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("CloseDeliveryEditTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("CloseDeliveryEditTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            CloseStatements();
//            System.out.println("<CloseDeliveryEditTransaction::Execute> exit");
        }
    }

    /**
     * Generates an xmlNodeArray containing parameters for this transaction
     *
     * @return xmlNodeArray that contains parameters for the transaction
     * @exception (none)
     */
    @Override
    public xmlNodeArray GenerateTestParameters() {
        xmlNodeArray nodeArr = new xmlNodeArray();
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.CloseDeliveryEditTransaction");

        return nodeArr;

    }

    /**
     * The main method for the transaction. Creates a database connection and an
     * error Array, then executes the transaction and reports any errors
     *
     * @param argv argv[0] is an optional configuration file name
     * @exception (none)
     */
    public static void main(String[] argv) {
        try {
            CloseDeliveryEditTransaction transaction = new CloseDeliveryEditTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.CloseDeliveryEditTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" CloseDeliveryEditTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" CloseDeliveryEditTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.CloseDeliveryEditTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("CloseDeliveryEditTransaction - No results were returned.");
                            } else {
                                xmlNodeArray array = resultTransaction.GetResultArray();
                                String str = xmlNodeArray.xmlNodeArray2String(array);
                                array = xmlNodeArray.string2xmlNodeArray(str);
                                System.out.println(str);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("CloseDeliveryEditTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
