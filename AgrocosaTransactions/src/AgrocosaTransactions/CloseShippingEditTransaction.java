/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class CloseShippingEditTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtUpdateDeliveryInfo;
    protected PreparedStatement pstmtInsertInventoryHistory;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public CloseShippingEditTransaction() {
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
                System.out.println("<CloseShippingEditTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<CloseShippingEditTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<CloseShippingEditTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<CloseShippingEditTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            
            pstmtUpdateDeliveryInfo = con.prepareStatement("update shipping set "
                    + "Proforma = ?, "//1. proforma
                    + "Factura = ?, "//2. factura
                    + "Entry = ?, "//3. entry
                    + "ScacCode = ?, "//4. scaccode
                    + "Trucking = ?, "//5. trucking
                    + "Trailer = ?, "//6. trailer
                    + "Seal = ?, "//7. sello
                    + "Gate = ?, "//8. gate
                    + "DriverName = ?, "//9. drivername
                    + "TodayCol = ?, "//10. today
                    + "Comments = ? "//11. comments
                    + "WHERE idShipping = ? ");//12. idShipping

            pstmtInsertInventoryHistory = con.prepareStatement("insert into shippinghistory ("
                    + "idShipping, "
                    + "Gate, "
                    + "DriverName, "
                    + "Status, "
                    + "Comments, "
                    + "InsertDate) "
                    + "values("
                    + "?,"//1. idShipping
                    + "?,"//2. gate
                    + "?,"//3. drivername
                    + "'Modificado',"
                    + "?,"//4. comments
                    + "now()"
                    + ")");

//            this.addPreparedStatement(pstmtDeliveryInfo);
            this.addPreparedStatement(pstmtUpdateDeliveryInfo);
            this.addPreparedStatement(pstmtInsertInventoryHistory);

            return true;
        } catch (SQLException e) {
            System.out.println("CloseShippingEditTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int idShipping = 0;
        String proforma = "";
        String factura = "";
        String entry = "";
        String scaccode = "";
        String trucking = "";
        String trailer = "";
        String sello = "";
        String gate = "";
        String drivername = "";
        int today = 0;
        String comments = "";
        
        int idUser = 0;

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();
            
            if (GetNodeArray().existValue("idUser")) {
                idUser = GetNodeArray().find("idUser").getIntValue();
            }

            idShipping = GetNodeArray().find("idShipping").getIntValue();
            proforma = GetNodeArray().find("proformamodal").getStringValue();
            factura = GetNodeArray().find("factura").getStringValue();
            entry = GetNodeArray().find("entry").getStringValue();
            scaccode = GetNodeArray().find("scaccode").getStringValue();
            trucking = GetNodeArray().find("trucking").getStringValue();
            trailer = GetNodeArray().find("trailer").getStringValue();
            sello = GetNodeArray().find("sello").getStringValue();
            gate = GetNodeArray().find("gate").getStringValue();
            drivername = GetNodeArray().find("drivername").getStringValue();
            today = GetNodeArray().find("today").getIntValue();
            comments = GetNodeArray().find("comments").getStringValue();

            resultArray.add("SelectedPickup", proforma);
            resultArray.add("Action", "Search");

            pstmtUpdateDeliveryInfo.setString(1, proforma);
            pstmtUpdateDeliveryInfo.setString(2, factura);
            pstmtUpdateDeliveryInfo.setString(3, entry);
            pstmtUpdateDeliveryInfo.setString(4, scaccode);
            pstmtUpdateDeliveryInfo.setString(5, trucking);
            pstmtUpdateDeliveryInfo.setString(6, trailer);
            pstmtUpdateDeliveryInfo.setString(7, sello);
            pstmtUpdateDeliveryInfo.setString(8, gate);
            pstmtUpdateDeliveryInfo.setString(9, drivername);
            pstmtUpdateDeliveryInfo.setInt(10, today);
            pstmtUpdateDeliveryInfo.setString(11, comments);
            pstmtUpdateDeliveryInfo.setInt(12, idShipping);
            rowsAffected = pstmtUpdateDeliveryInfo.executeUpdate();

            //Insertar historial inventario
            pstmtInsertInventoryHistory.setInt(1, idShipping);
            pstmtInsertInventoryHistory.setString(2, gate);
            pstmtInsertInventoryHistory.setString(3, drivername);
            pstmtInsertInventoryHistory.setString(4, comments);
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
            System.out.println("CloseShippingEditTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("CloseShippingEditTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            CloseStatements();
//            System.out.println("<CloseShippingEditTransaction::Execute> exit");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.CloseShippingEditTransaction");

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
            CloseShippingEditTransaction transaction = new CloseShippingEditTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.CloseShippingEditTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" CloseShippingEditTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" CloseShippingEditTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.CloseShippingEditTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("CloseShippingEditTransaction - No results were returned.");
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
            System.out.println("CloseShippingEditTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
