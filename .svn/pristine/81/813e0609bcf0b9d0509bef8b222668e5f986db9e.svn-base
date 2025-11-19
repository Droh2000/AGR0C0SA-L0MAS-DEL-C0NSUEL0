/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveDividedPalletsTransaction extends SIDWebTransaction {

    static int QTY_ALLOWED = 24;
    protected PreparedStatement pstmtSelectInventory;
    protected PreparedStatement pstmtUpdateInventory;
    protected PreparedStatement pstmtInsertInventory;
    protected PreparedStatement pstmtSelectQtyAllowedByLine;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveDividedPalletsTransaction() {
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
        String[] mandatoryTags = {
            "linea",
            "product",
            "referenceNumber",
            "quantity",
            "palletQuantity"
        };

        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<SaveDividedPalletsTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveDividedPalletsTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveDividedPalletsTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveDividedPalletsTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtSelectInventory = con.prepareStatement("select * "
                    + "from warehouse "
                    + "where Product = ? "
                    + "and Linea = ? "
                    + "and ReferenceNumber = ?");

            pstmtUpdateInventory = con.prepareStatement("update warehouse set "
                    + "Quantity = Quantity - 1 "
                    + "where Product = ? "
                    + "and Linea = ? "
                    + "and ReferenceNumber = ? "
                    + "and idWarehouse = ? ");

            pstmtInsertInventory = con.prepareStatement("insert into warehouse ("
                    + "Product, "
                    + "Quantity, "
                    + "Linea, "
                    + "ReferenceNumber, "
                    + "Bag, "
                    + "PartialId "
                    + ") values("
                    + "?, "//1 Product
                    + "1, "//Quantity
                    + "?, "//2 Linea
                    + "?, "//3 ReferenceNumber
                    + "?, "//4 Bag
                    + "?  "//5 PartialId
                    + ")");

            pstmtSelectQtyAllowedByLine = con.prepareStatement("SELECT "
                    + "Value as Qty "
                    + "FROM sidconfiguration "
                    + "Where Tag ='QtyAllowedByLine' ");
                    
            this.addPreparedStatement(pstmtSelectInventory);
            this.addPreparedStatement(pstmtUpdateInventory);
            this.addPreparedStatement(pstmtInsertInventory);
            this.addPreparedStatement(pstmtSelectQtyAllowedByLine);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveDividedPalletsTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int rowsAffected = 0;
        String line = "";
        String product = "";
        String referenceNumber = "";
        int palletQuantity = 0;
        int idWarehouse = 0;
        int counter = 0;
        int bag = 0;
        String result = "";
        int actualQty = 0;

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            line = GetNodeArray().find("linea").getStringValue();
            product = GetNodeArray().find("product").getStringValue();
            referenceNumber = GetNodeArray().find("referenceNumber").getStringValue();
            palletQuantity = GetNodeArray().find("palletQuantity").getIntValue();
            
            //select allowed pallet qty by line
            rset = pstmtSelectQtyAllowedByLine.executeQuery();
            if(rset.next()){
                QTY_ALLOWED = rset.getInt("Qty");
            }
            if(rset != null){
                rset.close();
                rset = null;
            }

            //select warehouse
            pstmtSelectInventory.setString(1, product);
            pstmtSelectInventory.setString(2, line);
            pstmtSelectInventory.setString(3, referenceNumber);
            rset = pstmtSelectInventory.executeQuery();
            if (rset.next()) {
                idWarehouse = rset.getInt("idWarehouse");
                actualQty = rset.getInt("Quantity");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (((actualQty - 1) + palletQuantity) > QTY_ALLOWED) {
                conn.rollback();
                resultArray.add("error", "Se excede el numero maximo de pallets por linea de " + QTY_ALLOWED +".");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Se excede el numero de pallets permitido por linea de " + QTY_ALLOWED + ".");
                resultArray.add("RESPONSE_DETAIL", "");
            } else {

                //update quantity
                pstmtUpdateInventory.setString(1, product);
                pstmtUpdateInventory.setString(2, line);
                pstmtUpdateInventory.setString(3, referenceNumber);
                pstmtUpdateInventory.setInt(4, idWarehouse);
                rowsAffected = pstmtUpdateInventory.executeUpdate();
                if (rowsAffected > 0) {
                    //insert new pallets
                    counter = 1;
                    while (counter <= palletQuantity) {

                        if (this.GetNodeArray().existValue("q" + counter)) {
                            bag = GetNodeArray().find("q" + counter).getIntValue();

                            pstmtInsertInventory.setString(1, product);
                            pstmtInsertInventory.setString(2, line);
                            pstmtInsertInventory.setString(3, referenceNumber + "_" + bag);
                            pstmtInsertInventory.setInt(4, bag);
                            pstmtInsertInventory.setInt(5, idWarehouse);
                            rowsAffected = pstmtInsertInventory.executeUpdate();
                            if (rowsAffected > 0) {
                                result += "1";
                            } else {
                                result += "0";
                            }
                        }
                        counter++;
                    }
                }

                if (!result.contains("0")) {

                    conn.commit();
                    resultArray.add("RESPONSE_CODE", "PASS");
                    resultArray.add("RESPONSE_MESSAGE", "Los pallets se han registrado exitosamente.");
                    resultArray.add("RESPONSE_DETAIL", "");

                } else {
                    conn.rollback();
                    resultArray.add("error", "Los pallets no pudieron ser registrados.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "El movimiento de pallets no pudo ser registrado.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }
            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveDividedPalletsTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveDividedPalletsTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            CloseStatements();
            if (rset != null) {
                rset.close();
                rset = null;
            }
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.SaveDividedPalletsTransaction");
        nodeArr.add("idUser", "1");
        nodeArr.add("linea", "13");
        nodeArr.add("product", "JY");
        nodeArr.add("referenceNumber", "3033");
        nodeArr.add("quantity", "2");
        nodeArr.add("palletQuantity", "1");

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
            SaveDividedPalletsTransaction transaction = new SaveDividedPalletsTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.SaveDividedPalletsTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveDividedPalletsTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveDividedPalletsTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.SaveDividedPalletsTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveDividedPalletsTransaction - No results were returned.");
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
            System.out.println("SaveDividedPalletsTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
