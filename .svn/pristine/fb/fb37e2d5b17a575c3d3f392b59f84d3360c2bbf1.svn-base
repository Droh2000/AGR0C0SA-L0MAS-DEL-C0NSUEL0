/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import java.util.HashMap;
import xmlNodeArray.*;

public class SaveConsolidatePalletsTransaction extends SIDWebTransaction {

    static String CROSSDOCK = "Bodega";
    protected PreparedStatement pstmtSelectCrossdockStorage;
    protected PreparedStatement pstmtSelectPalletInfo;
    protected PreparedStatement pstmtUpdateInventory;
    protected PreparedStatement pstmtInsertInventoryMovement;
    protected PreparedStatement pstmtSelectActualStorage;
    protected PreparedStatement pstmtUser;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveConsolidatePalletsTransaction() {
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
                System.out.println("<SaveConsolidatePalletsTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveConsolidatePalletsTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveConsolidatePalletsTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveConsolidatePalletsTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtSelectCrossdockStorage = con.prepareStatement("Select "
                    + "storage.idStorage, "
                    + "storage.StorageName, "
                    + "substorage.idSubstorage, "
                    + "substorage.SubstorageName "
                    + "from storage inner join "
                    + "substorage on substorage.idStorage = storage.idStorage "
                    + "where storage.StorageName = '" + CROSSDOCK + "' "
                    + "and substorage.Active = 1 "
                    + "Order by substorage.SubstorageName");

            pstmtSelectPalletInfo = con.prepareStatement("select "
                    + "PalletId, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "Quantity "
                    + "from palletinfo "
                    + "where PalletId = ? ");

            pstmtSelectActualStorage = con.prepareStatement("select "
                    + "storage.StorageName, "
                    + "substorage.SubstorageName, "
                    + "substorage.idSubstorage "
                    + "from inventory inner join "
                    + "storage on storage.idStorage = inventory.idStorage inner join "
                    + "substorage on substorage.idSubStorage = inventory.idSubstorage "
                    + "where PalletId = ? ");

            pstmtUpdateInventory = con.prepareStatement("update inventory set "
                    + "idStorage = ?, "
                    + "idSubstorage = ? "
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

            pstmtUser = con.prepareStatement("select concat(FirstName,' ',LastName) User "
                    + "from user "
                    + "where idUser = ? ");

            this.addPreparedStatement(pstmtSelectCrossdockStorage);
            this.addPreparedStatement(pstmtSelectPalletInfo);
            this.addPreparedStatement(pstmtSelectActualStorage);
            this.addPreparedStatement(pstmtUpdateInventory);
            this.addPreparedStatement(pstmtInsertInventoryMovement);
            this.addPreparedStatement(pstmtUser);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveConsolidatePalletsTransaction::PrepareStatements> SQLException: " + e.getMessage());
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

        String proforma = "";
        String userName = "";
        int idStorage = 0;
        String storageName = "";
        String productName = "";
        String size = "";
        String color = "";
        int quantity = 0;
        String palletId = "";
        String result = "";
        String idSubstorage = "";
        String tableData = "";
        int counter = 0;
        String storageNameFrom = "";
        String substorageNameFrom = "";

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

            HashMap idSubstorageMap = new HashMap();
            HashMap substorageNameMap = new HashMap();
            //crossdock storage
            rset = pstmtSelectCrossdockStorage.executeQuery();
            while (rset.next()) {
                idStorage = rset.getInt("idStorage");
                storageName = rset.getString("StorageName");
                idSubstorageMap.put(String.valueOf(counter), rset.getString("idSubstorage"));
                substorageNameMap.put(String.valueOf(counter), rset.getString("SubstorageName"));
                counter++;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            tableData = tableData.replace("],[", "~");
            tableData = tableData.replace("[", "");
            tableData = tableData.replace("\"", "");
            tableData = tableData.replace("]", "");

            String[] div = tableData.split("~");

            for (String element : div) {
                element = element.replace("|", "~");

                String[] data = element.split(",");
                String[] key = data[0].split("~");//idStockTransferOrder|ProductName
                String col = data[2];//Column
                String[] palletInfo = data[4].split("-");//ProductNAme-Quantity
                palletId = palletInfo[1];

                pstmtSelectPalletInfo.setString(1, palletId);
                rset = pstmtSelectPalletInfo.executeQuery();
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

                //get actual storage / substorage
                pstmtSelectActualStorage.setString(1, palletId);
                rset2 = pstmtSelectActualStorage.executeQuery();
                if (rset2.next()) {
                    storageNameFrom = rset2.getString("StorageName");
                    substorageNameFrom = rset2.getString("SubstorageName");
                    idSubstorage = rset2.getString("idSubstorage");
                }
                if (rset2 != null) {
                    rset2.close();
                    rset2 = null;
                }

                //validate 
                if (!idSubstorage.equals(idSubstorageMap.get(col).toString())) {
                    //update inventory
                    pstmtUpdateInventory.setInt(1, idStorage);
                    pstmtUpdateInventory.setString(2, idSubstorageMap.get(col).toString());
                    pstmtUpdateInventory.setString(3, palletId);
                    rowsAffected = pstmtUpdateInventory.executeUpdate();
                    if (rowsAffected > 0) {
                        //Insert pallet Movement
                        pstmtInsertInventoryMovement.setString(1, palletId);
                        pstmtInsertInventoryMovement.setString(2, "Consolidar");
                        pstmtInsertInventoryMovement.setString(3, "Transferencia");
                        pstmtInsertInventoryMovement.setString(4, productName);
                        pstmtInsertInventoryMovement.setString(5, size);
                        pstmtInsertInventoryMovement.setString(6, color);
                        pstmtInsertInventoryMovement.setInt(7, quantity);
                        pstmtInsertInventoryMovement.setString(8, storageNameFrom);
                        pstmtInsertInventoryMovement.setString(9, substorageNameFrom);
                        pstmtInsertInventoryMovement.setString(10, storageName);
                        pstmtInsertInventoryMovement.setString(11, substorageNameMap.get(col).toString());
                        pstmtInsertInventoryMovement.setString(12, "Acomodo de pallets");
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
            System.out.println("SaveConsolidatePalletsTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveConsolidatePalletsTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.SaveConsolidatePalletsTransaction");
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
            SaveConsolidatePalletsTransaction transaction = new SaveConsolidatePalletsTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.SaveConsolidatePalletsTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveConsolidatePalletsTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveConsolidatePalletsTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.SaveConsolidatePalletsTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveConsolidatePalletsTransaction - No results were returned.");
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
            System.out.println("SaveConsolidatePalletsTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
