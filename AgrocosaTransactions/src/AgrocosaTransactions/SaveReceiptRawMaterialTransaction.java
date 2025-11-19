/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveReceiptRawMaterialTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectRMInventory;
    protected PreparedStatement pstmtInsertRMInventory;
    protected PreparedStatement pstmtUpdateRMInventory;
    protected PreparedStatement pstmtInsertRMInventoryLog;
    protected PreparedStatement pstmtSelectStorage;
    protected PreparedStatement pstmtUser;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveReceiptRawMaterialTransaction() {
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
            "rawMaterialTypeName",
            "rawMaterialName",
            "storageName",
            "substorageName",
            "quantity",
            "uom"
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
                System.out.println("<SaveReceiptRawMaterialTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveReceiptRawMaterialTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveReceiptRawMaterialTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveReceiptRawMaterialTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtSelectRMInventory = con.prepareStatement("select "
                    + "idStorage, "
                    + "idSubstorage, "
                    + "RawMaterialName, "
                    + "Quantity, "
                    + "UOM "
                    + "from rawmaterialinventory "
                    + "where idStorage = ? "//1 idStorage
                    + "and idSubstorage = ? "//2 idSubstorage
                    + "and RawMaterialName = ? "//3 RawMaterialName
                    + "and UOM = ? ");//4 uom

            pstmtInsertRMInventory = con.prepareStatement("insert into rawmaterialinventory ( "
                    + "idStorage, "
                    + "idSubstorage, "
                    + "RawMaterialName, "
                    + "Quantity, "
                    + "UOM "
                    + ") values ("
                    + "?, "//1 idStorage
                    + "?, "//2 idSubstorage
                    + "?, "//3 RawMaterialName
                    + "?, "//4 Quantity
                    + "?  "//5 UOM
                    + ")");

            pstmtUpdateRMInventory = con.prepareStatement("update rminventory set "
                    + "Quantity = Quantity + ? "//1 Quantity
                    + "where idStorage = ? "//2 idStorage
                    + "and idSubstorage = ? "//3 idSubstorage
                    + "and RawMaterialName = ? "//4 CropName
                    + "and UOM = ? ");//5 UOM

            pstmtInsertRMInventoryLog = con.prepareStatement("insert into rawmaterialinventorylog ( "
                    + "MovementType, "
                    + "RawMaterialTypeName, "
                    + "RawMaterialName, "
                    + "Quantity, "
                    + "UOM, "
                    + "StorageName, "
                    + "SubstorageName, "
                    + "Comments, "
                    + "User, "
                    + "InsertDate, "
                    + "Active "
                    + ") values ( "
                    + "?, "//1 MovementType
                    + "?, "//2 RawMaterialTypeName
                    + "?, "//3 RawMaterialName
                    + "?, "//4 Quantity
                    + "?, "//5 UOM
                    + "?, "//6 StorageName
                    + "?, "//7 SubstorageName
                    + "?, "//8 Comments
                    + "?, "//9 User
                    + "Now(), "
                    + "1 "
                    + ")");

            pstmtSelectStorage = con.prepareStatement("SELECT "
                    + "storage.idStorage, "
                    + "storage.StorageName, "
                    + "substorage.idSubstorage, "
                    + "substorage.SubstorageName "
                    + "FROM storage inner join "
                    + "substorage on substorage.idStorage = storage.idStorage "
                    + "where storage.StorageName = ? "//1 StorageNAme
                    + "and substorage.SubstorageName = ?");//2 SubstorageName

            pstmtUser = con.prepareStatement("select concat(FirstName,' ',LastName) User "
                    + "from user "
                    + "where idUser = ? ");

            this.addPreparedStatement(pstmtSelectRMInventory);
            this.addPreparedStatement(pstmtInsertRMInventory);
            this.addPreparedStatement(pstmtUpdateRMInventory);
            this.addPreparedStatement(pstmtInsertRMInventoryLog);
            this.addPreparedStatement(pstmtSelectStorage);
            this.addPreparedStatement(pstmtUser);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveReceiptRawMaterialTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        ResultSet rset = null;
        xmlNodeArray resultArray = null;
        int rowsAffected = 0;
        int idUser = 0;
        String rawMaterialName = "";
        String rawMaterialTypeName = "";
        String storageName = "";
        String substorageName = "";
        int qty = 0;
        String uom = "";
        String userName = "";
        int idStorage = 0;
        int idSubstorage = 0;
        boolean boolSave = false;
        String comments = "";

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            rawMaterialTypeName = GetNodeArray().find("rawMaterialTypeName").getStringValue();
            rawMaterialName = GetNodeArray().find("rawMaterialName").getStringValue();
            storageName = GetNodeArray().find("storageName").getStringValue();
            substorageName = GetNodeArray().find("substorageName").getStringValue();
            qty = GetNodeArray().find("quantity").getIntValue();
            uom = GetNodeArray().find("uom").getStringValue();

            if (this.GetNodeArray().existValue("comments")) {
                comments = GetNodeArray().find("comments").getStringValue();
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

            //StorageName & SubstorageName
            pstmtSelectStorage.setString(1, storageName);
            pstmtSelectStorage.setString(2, substorageName);
            rset = pstmtSelectStorage.executeQuery();
            if (rset.next()) {
                idStorage = rset.getInt("idStorage");
                idSubstorage = rset.getInt("idSubstorage");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //validate if record exist in bininventory table
            pstmtSelectRMInventory.setInt(1, idStorage);
            pstmtSelectRMInventory.setInt(2, idSubstorage);
            pstmtSelectRMInventory.setString(3, rawMaterialName);
            pstmtSelectRMInventory.setString(4, uom);
            rset = pstmtSelectRMInventory.executeQuery();
            if (rset.next()) {
                //update
                pstmtUpdateRMInventory.setInt(1, qty);
                pstmtUpdateRMInventory.setInt(2, idStorage);
                pstmtUpdateRMInventory.setInt(3, idSubstorage);
                pstmtUpdateRMInventory.setString(4, rawMaterialName);
                pstmtUpdateRMInventory.setString(5, uom);
                rowsAffected = pstmtUpdateRMInventory.executeUpdate();
                if (rowsAffected > 0) {
                    boolSave = true;
                }
            } else {
                //insert
                pstmtInsertRMInventory.setInt(1, idStorage);
                pstmtInsertRMInventory.setInt(2, idSubstorage);
                pstmtInsertRMInventory.setString(3, rawMaterialName);
                pstmtInsertRMInventory.setInt(4, qty);
                pstmtInsertRMInventory.setString(5, uom);
                rowsAffected = pstmtInsertRMInventory.executeUpdate();
                if (rowsAffected > 0) {
                    boolSave = true;
                }
            }

            if (boolSave) {

                //insert rawmaterialinventorylog
                pstmtInsertRMInventoryLog.setString(1, "Entrada");
                pstmtInsertRMInventoryLog.setString(2, rawMaterialTypeName);
                pstmtInsertRMInventoryLog.setString(3, rawMaterialName);
                pstmtInsertRMInventoryLog.setInt(4, qty);
                pstmtInsertRMInventoryLog.setString(5, uom);
                pstmtInsertRMInventoryLog.setString(6, storageName);
                pstmtInsertRMInventoryLog.setString(7, substorageName);
                pstmtInsertRMInventoryLog.setString(8, comments);
                pstmtInsertRMInventoryLog.setString(9, userName);
                rowsAffected = pstmtInsertRMInventoryLog.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    resultArray.add("Result", "Los datos del recibo han sido registrados exitosamente.");
                    resultArray.add("RESPONSE_CODE", "PASS");
                    resultArray.add("RESPONSE_MESSAGE", "Datos del recibo registrados exitosamente.");
                    resultArray.add("RESPONSE_DETAIL", "");
                } else {
                    conn.rollback();
                    resultArray.add("Result", "Los datos del recibo no pudieron ser registrados.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "La Informacion no pudo ser registrada.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }
            } else {
                conn.rollback();
                resultArray.add("Result", "Los datos del recibo no pudieron ser registrados.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "La Informacion no pudo ser registrada.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveReceiptRawMaterialTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveReceiptRawMaterialTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveReceiptRawMaterialTransaction");

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
            SaveReceiptRawMaterialTransaction transaction = new SaveReceiptRawMaterialTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveReceiptRawMaterialTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveReceiptRawMaterialTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveReceiptRawMaterialTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveReceiptRawMaterialTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveReceiptRawMaterialTransaction - No results were returned.");
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
            System.out.println("SaveReceiptRawMaterialTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
