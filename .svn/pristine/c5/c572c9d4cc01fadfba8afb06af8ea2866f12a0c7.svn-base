/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveConsolidateWarehousePalletsTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtDeleteInventory;
    protected PreparedStatement pstmtInsertInventory;
    protected PreparedStatement pstmtUpdateInventory;
    protected PreparedStatement pstmtValidateInventory;
    protected PreparedStatement pstmtUser;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveConsolidateWarehousePalletsTransaction() {
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
            //            "idUser",
            "tableData"
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
                System.out.println("<SaveConsolidateWarehousePalletsTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveConsolidateWarehousePalletsTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveConsolidateWarehousePalletsTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveConsolidateWarehousePalletsTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtDeleteInventory = con.prepareStatement("delete from warehouse");

            pstmtInsertInventory = con.prepareStatement("insert into warehouse ("
                    + "Product, "
                    + "Linea, "
                    + "Quantity, "
                    + "ReferenceNumber "
                    + ") values("
                    + "?, "//1 Product
                    + "?, "//2 Linea
                    + "?,  "//3 Quantity
                    + "?  "//4 ReferenceNumber
                    + ")");

            pstmtUpdateInventory = con.prepareStatement("update warehouse set "
                    + "Quantity = Quantity + ? "
                    + "where Product = ? "
                    + "and Linea  = ? "
                    + "and ReferenceNumber = ? ");

            pstmtValidateInventory = con.prepareStatement("select * "
                    + "from warehouse "
                    + "where Product = ? "
                    + "and Linea  = ? "
                    + "and ReferenceNumber = ?");

            pstmtUser = con.prepareStatement("select concat(FirstName,' ',LastName) User "
                    + "from user "
                    + "where idUser = ? ");

            this.addPreparedStatement(pstmtDeleteInventory);
            this.addPreparedStatement(pstmtUpdateInventory);
            this.addPreparedStatement(pstmtInsertInventory);
            this.addPreparedStatement(pstmtValidateInventory);
            this.addPreparedStatement(pstmtUser);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveConsolidateWarehousePalletsTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        ResultSet rset2 = null;
        int rowsAffected = 0;
        int idUser = 0;
        int col = 0;
        String userName = "";
        String quantity = "";
        String result = "";
        String tableData = "";
        String referenceNumber = "";

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            tableData = GetNodeArray().find("tableData").getStringValue();

            //User Name
            pstmtUser.setInt(1, idUser);
            rset = pstmtUser.executeQuery();
            if (rset.next()) {
                userName = rset.getString("User");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //delete warehouse
            pstmtDeleteInventory.executeUpdate();

            tableData = tableData.replace("],[", "~");
            tableData = tableData.replace("[", "");
            tableData = tableData.replace("\"", "");
            tableData = tableData.replace("]", "");

            String[] div = tableData.split("~");

            for (String element : div) {

                String[] data = element.split(",");
                col = Integer.parseInt(data[2]);//Column
                String productName = data[4];//ProductName
                String[] productData = productName.split("-");
                quantity = productData[0];
                productName = productData[1];
                referenceNumber = productData[2];

                //validate
                pstmtValidateInventory.setString(1, productName);
                pstmtValidateInventory.setInt(2, col);
                pstmtValidateInventory.setString(3, referenceNumber);
                rset2 = pstmtValidateInventory.executeQuery();
                if (rset2.next()) {
                    //update inventory
                    pstmtUpdateInventory.setString(1, quantity);
                    pstmtUpdateInventory.setString(2, productName);
                    pstmtUpdateInventory.setInt(3, col);
                    pstmtUpdateInventory.setString(4, referenceNumber);
                    rowsAffected = pstmtUpdateInventory.executeUpdate();
                    if (rowsAffected > 0) {
                        result += "1";
                    } else {
                        result += "0";
                    }
                } else {
                    //insert inventory
                    pstmtInsertInventory.setString(1, productName);
                    pstmtInsertInventory.setInt(2, col);
                    pstmtInsertInventory.setString(3, quantity);
                    pstmtInsertInventory.setString(4, referenceNumber);
                    rowsAffected = pstmtInsertInventory.executeUpdate();
                    if (rowsAffected > 0) {
                        result += "1";
                    } else {
                        result += "0";
                    }
                }
                if (rset2 != null) {
                    rset2.close();
                    rset2 = null;
                }

            }

            if (!result.contains("0")) {

                conn.commit();
                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", "Movimiento de pallets registrado exitosamente.");
                resultArray.add("RESPONSE_DETAIL", "");

            } else {
                conn.rollback();
                resultArray.add("error", "El movimiento de pallets no pudo ser registrado.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "El movimiento de pallets no pudo ser registrado.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveConsolidateWarehousePalletsTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveConsolidateWarehousePalletsTransaction::Execute> Exception: " + ex.getMessage());
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
            if (rset2 != null) {
                rset2.close();
                rset2 = null;
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.SaveConsolidateWarehousePalletsTransaction");
        nodeArr.add("idUser", "1");
        nodeArr.add("tableData", "[[\"i0\",1,0,\"gray\",\"JP-2019FJP000042\"],[\"i1\",1,1,\"gray\",\"MW-2019FMW000034\"],[\"i2\",1,3,\"gray\",\"JW-2019GJW000055\"],[\"i3\",1,3,\"gray\",\"JW-2019GJW000056\"],[\"i4\",1,3,\"gray\",\"JW-2019GJW000057\"],[\"i5\",1,4,\"gray\",\"MW-2019GMW000048\"],[\"i6\",1,4,\"gray\",\"MW-2019GMW000049\"],[\"i7\",1,4,\"gray\",\"MW-2019GMW000050\"],[\"i8\",1,4,\"gray\",\"MW-2019GMW000051\"],[\"i9\",1,4,\"gray\",\"MW-2019GMW000052\"]]");

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
            SaveConsolidateWarehousePalletsTransaction transaction = new SaveConsolidateWarehousePalletsTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.SaveConsolidateWarehousePalletsTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveConsolidateWarehousePalletsTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveConsolidateWarehousePalletsTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.SaveConsolidateWarehousePalletsTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveConsolidateWarehousePalletsTransaction - No results were returned.");
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
            System.out.println("SaveConsolidateWarehousePalletsTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
