/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveOutPalletsTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectInventory;
    protected PreparedStatement pstmtDeleteInventory;
    protected PreparedStatement pstmtInsertInventoryMovement;
    protected PreparedStatement pstmtUpdatePalletInfo;
    protected PreparedStatement pstmtPalletInfo;
    protected PreparedStatement pstmtUser;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveOutPalletsTransaction() {
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
            "idUser",
            "categoryName",
            "palletInfo"
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
                System.out.println("<SaveOutPalletsTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveOutPalletsTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveOutPalletsTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveOutPalletsTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtSelectInventory = con.prepareStatement("SELECT "
                    + "inventory.idInventory, "
                    + "inventory.PalletId, "
                    + "storage.StorageName, "
                    + "substorage.SubstorageName "
                    + "FROM inventory inner join "
                    + "storage on storage.idStorage = inventory.idStorage inner join  "
                    + "substorage on substorage.idSubStorage = inventory.idSubstorage "
                    + "where inventory.PalletId = ? ");

            pstmtDeleteInventory = con.prepareStatement("Delete "
                    + "from inventory "
                    + "where PalletId = ? ");

            pstmtInsertInventoryMovement = con.prepareStatement("insert into inventorymovement ("
                    + "PalletId, "
                    + "Process, "
                    + "MovementType, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "Quantity, "
                    + "StorageNameOut, "
                    + "SubstorageNameOut, "
                    + "StorageNameIn, "
                    + "SubstorageNameIn, "
                    + "Comments, "
                    + "User, "
                    + "InsertDate, "
                    + "Active "
                    + ") values("
                    + "?, "//1 PalletId
                    + "?, "//2 Process
                    + "?, "//3 MovementType
                    + "?, "//4 ProductName
                    + "?, "//5 Size
                    + "?, "//6 Color
                    + "?, "//7 Quantity
                    + "?, "//8 StorageNameOut
                    + "?, "//9 SubstorageNameOut
                    + "?, "//10 StorageNameIn
                    + "?, "//11 SubstorageNameIn
                    + "?, "//12 Comment
                    + "?, "//13 User
                    + "Now(), "
                    + "1 "
                    + ")");

            pstmtUpdatePalletInfo = con.prepareStatement("update palletinfo set "
                    + "Cancel = 1, "
                    + "ModifiedDate = Now() "
                    + "where PalletId = ? ");

            pstmtPalletInfo = con.prepareStatement("select "
                    + "PalletId, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "Quantity "
                    + "from palletinfo "
                    + "where PalletId = ? ");

            pstmtUser = con.prepareStatement("select concat(FirstName,' ',LastName) User "
                    + "from user "
                    + "where idUser = ? ");

            this.addPreparedStatement(pstmtSelectInventory);
            this.addPreparedStatement(pstmtDeleteInventory);
            this.addPreparedStatement(pstmtInsertInventoryMovement);
            this.addPreparedStatement(pstmtUpdatePalletInfo);
            this.addPreparedStatement(pstmtPalletInfo);
            this.addPreparedStatement(pstmtUser);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveOutPalletsTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int idUser = 0;
        String categoryName = "";
        String palletInfo = "";
        String comments = "";
        String userName = "";
        String result = "";
        String storageName = "";
        String substorageName = "";
        String productName = "";
        String size = "";
        String color = "";
        int quantity = 0;

        try {
            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            categoryName = GetNodeArray().find("categoryName").getStringValue();
            palletInfo = GetNodeArray().find("palletInfo").getStringValue();

            if (this.GetNodeArray().existValue("comments")) {
                comments = GetNodeArray().find("comments").getStringValue();
            }

            //validate if pallet id exist
            String lines[] = palletInfo.split("\\r?\\n");
            result = "";
            for (String pallet : lines) {
                pstmtSelectInventory.setString(1, pallet.trim());
                rset = pstmtSelectInventory.executeQuery();
                if (!rset.next()) {
                    result += pallet.trim() + ", ";
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
            }
            if (!result.equals("")) {
                conn.rollback();
                resultArray.add("error", "Los siguientes pallets no se encuentran en la localidad de inventario.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Los siguientes pallets no se encuentran en la localidad de inventario.");
                resultArray.add("RESPONSE_DETAIL", result);
                return resultArray;
            }

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
            
            result = "";
            for (String pallet : lines) {
                //Get palletId, StorageName & SubstorageName
                pstmtSelectInventory.setString(1, pallet.trim());
                rset = pstmtSelectInventory.executeQuery();
                if (rset.next()) {
                    storageName = rset.getString("StorageName");
                    substorageName = rset.getString("SubstorageName");
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }

                //update palletinfo Cancel = 1
                pstmtUpdatePalletInfo.setString(1, pallet.trim());
                rowsAffected = pstmtUpdatePalletInfo.executeUpdate();
                if (rowsAffected > 0) {
                    //Delete inventory
                    pstmtDeleteInventory.setString(1, pallet.trim());
                    rowsAffected = pstmtDeleteInventory.executeUpdate();
                    if (rowsAffected > 0) {
                        //pallet info
                        pstmtPalletInfo.setString(1, pallet.trim());
                        rset = pstmtPalletInfo.executeQuery();
                        if (rset.next()) {
                            productName = rset.getString("ProductName");
                            size = rset.getString("Size");
                            color = rset.getString("Color");
                            quantity = rset.getInt("Quantity");
                        }
                        if (rset != null) {
                            rset.close();
                            rset = null;
                        }

                        //Insert inventorymovement
                        pstmtInsertInventoryMovement.setString(1, pallet.trim());
                        pstmtInsertInventoryMovement.setString(2, categoryName);
                        pstmtInsertInventoryMovement.setString(3, "Salida");
                        pstmtInsertInventoryMovement.setString(4, productName);
                        pstmtInsertInventoryMovement.setString(5, size);
                        pstmtInsertInventoryMovement.setString(6, color);
                        pstmtInsertInventoryMovement.setInt(7, quantity);
                        pstmtInsertInventoryMovement.setString(8, storageName);
                        pstmtInsertInventoryMovement.setString(9, substorageName);
                        pstmtInsertInventoryMovement.setString(10, null);
                        pstmtInsertInventoryMovement.setString(11, null);
                        pstmtInsertInventoryMovement.setString(12, comments);
                        pstmtInsertInventoryMovement.setString(13, userName);
                        rowsAffected = pstmtInsertInventoryMovement.executeUpdate();
                        if (rowsAffected > 0) {
                            result += "1";
                        } else {
                            result += "0";
                        }
                    } else {
                        result += "0";
                    }
                } else {
                    result += "0";
                }
            }

            if (!result.contains("0")) {
                conn.commit();
                resultArray.add("pass", "La salida de los pallets ha sido registrada exitosamente.");
                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", "La salida de los pallets ha sido registrada exitosamente.");
                resultArray.add("RESPONSE_DETAIL", "");

            } else {
                conn.rollback();
                resultArray.add("error", "La salida de pallets no pudo ser registrada.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "La salida de pallets no pudo ser registrada.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveOutPalletsTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveOutPalletsTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            if (rset != null) {
                rset.close();
                rset = null;
            }
            CloseStatements();
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveOutPalletsTransaction");
        nodeArr.add("idUser", "1");

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
            SaveOutPalletsTransaction transaction = new SaveOutPalletsTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveOutPalletsTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveOutPalletsTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveOutPalletsTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveOutPalletsTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveOutPalletsTransaction - No results were returned.");
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
            System.out.println("SaveOutPalletsTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
