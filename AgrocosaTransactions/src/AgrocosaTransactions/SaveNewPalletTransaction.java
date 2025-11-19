/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveNewPalletTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectPalletId;
    protected PreparedStatement pstmtUpdatePalletId;
    protected PreparedStatement pstmtInsertPalletInfo;
    protected PreparedStatement pstmtInsertInventory;
    protected PreparedStatement pstmtInsertInventoryMovement;
    protected PreparedStatement pstmtUser;
    protected PreparedStatement pstmtSelectSecuenceYear;
    protected PreparedStatement pstmtSelectPalletProduct;
    protected PreparedStatement pstmtSelectPalletDefinition;
    protected PreparedStatement pstmtSelectStorage;
    protected PreparedStatement pstmtSelectSubstorage;
    protected PreparedStatement pstmtSeqUpdate;
    protected PreparedStatement pstmVerifyNumGenerated;
    protected PreparedStatement pstmtInsertPalletSequence;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveNewPalletTransaction() {
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
            "palletName",
            "productName",
            "storageName",
            "substorageName",
            "quantity"
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
                System.out.println("<SaveNewPalletTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveNewPalletTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveNewPalletTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveNewPalletTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            /*pstmtSelectPalletId = con.prepareStatement("select "
                    + "LPAD(max(id) + 1,6,'0') as Num "
                    + "from sequence");*/

            pstmtUpdatePalletId = con.prepareStatement("update sequence set id = LAST_INSERT_ID(id+1)");

            pstmtInsertPalletInfo = con.prepareStatement("insert into palletinfo ( "
                    + "PalletId, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "Quantity, "
                    + "Ship, "
                    + "CampName, "
                    + "idUser, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active, "
                    + "Printed "
                    + ") values("
                    + "?, " //1 PalletId
                    + "?, " //2 ProductName
                    + "?, " //3 Size
                    + "?, " //4 Color
                    + "?, " //5 Qualtity
                    + "0, " //  Ship
                    + "?, " //6  CampName
                    + "?, " //7 idUser
                    + "Now(), " // InsertDate
                    + "Now(), " // ModifiedDate
                    + "1, " // Active
                    + "0  " // Printed
                    + ")");

            pstmtInsertInventory = con.prepareStatement("insert into inventory ("
                    + "idStorage, "
                    + "idSubstorage, "
                    + "idPalletProduct, "
                    + "Quantity, "
                    + "PalletId, "
                    + "PackingType "
                    + ") values("
                    + "?, "//1 idStorage
                    + "?, "//2 idSubsorage 
                    + "?, "//3 idPalletProduct
                    + "?, "//4 Quantity
                    + "?, "//5 PalletId,
                    + "'Pallet'  "//  PackingTYpe 
                    + ")");

            pstmtInsertInventoryMovement = con.prepareStatement("insert into inventorymovement ("
                    + "PalletId, "
                    + "Process, "
                    + "MovementType, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "CampName, "
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
                    + "?, "//7 CampName
                    + "?, "//8 Quantity
                    + "?, "//9 StorageNameOut
                    + "?, "//10 SubstorageNameOut
                    + "?, "//11 StorageNameIn
                    + "?, "//12 SubstorageNameIn
                    + "?, "//13 Comment
                    + "?, "//14 User
                    + "Now(), "
                    + "1 "
                    + ")");

            pstmtUser = con.prepareStatement("select concat(FirstName,' ',LastName) User "
                    + "from user "
                    + "where idUser = ? ");

            pstmtSelectSecuenceYear = con.prepareStatement("Select "
                    + "Tag "
                    + "from sidconfiguration "
                    + "where Tag in ( "
                    + "SELECT Value "
                    + "FROM sidconfiguration "
                    + "where Tag = 'SecuenceYear') "
                    + "and  Value = ? ");

            pstmtSelectPalletProduct = con.prepareStatement("select "
                    + "idPalletProduct, "
                    + "ProductName, "
                    + "Size, "
                    + "Color "
                    + "FROM palletproduct "
                    + "where ProductName = ? ");

            pstmtSelectPalletDefinition = con.prepareStatement("SELECT "
                    + "PalletName, "
                    + "Quantity "
                    + "FROM palletdefinition "
                    + "where PalletName = ? "
                    + "and active = 1");

            pstmtSelectStorage = con.prepareStatement("select "
                    + "idStorage "
                    + "from storage "
                    + "Where StorageName = ? "
                    + "and active = 1");

            pstmtSelectSubstorage = con.prepareStatement("select "
                    + "idSubstorage "
                    + "from substorage "
                    + "Where SubstorageName = ? "
                    + "and active = 1");
            
            // PSTM de la nueva tabla
            pstmVerifyNumGenerated = con.prepareStatement(
                "SELECT next_num FROM pallet_sequence WHERE palletId=? FOR UPDATE"
            );
            
            pstmtSeqUpdate = con.prepareStatement(
                "UPDATE pallet_sequence SET next_num = ? WHERE palletId = ?"
            );
            
            pstmtInsertPalletSequence = con.prepareStatement("insert into pallet_sequence ("
                    + "palletId, "
                    + "next_num"
                    + ") values ("
                    + "?, " // 1 palletId
                    + "1) "
            );
        
            //this.addPreparedStatement(pstmtSelectPalletId);
            this.addPreparedStatement(pstmtUpdatePalletId);
            this.addPreparedStatement(pstmtInsertPalletInfo);
            this.addPreparedStatement(pstmtInsertInventory);
            this.addPreparedStatement(pstmtInsertInventoryMovement);
            this.addPreparedStatement(pstmtUser);
            this.addPreparedStatement(pstmtSelectSecuenceYear);
            this.addPreparedStatement(pstmtSelectPalletProduct);
            this.addPreparedStatement(pstmtSelectStorage);
            this.addPreparedStatement(pstmtSelectSubstorage);
            this.addPreparedStatement(pstmtInsertPalletSequence);
            this.addPreparedStatement(pstmtSeqUpdate);
            this.addPreparedStatement(pstmVerifyNumGenerated);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveNewPalletTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String userName = "";
        String palletName = "";
        String productName = "";
        String storageName = "";
        String substorageName = "";
        int quantity = 0;
        Date date = new Date();
        String year = "";
        String month = "";
        String monthCode = "";
        String palletId = "";
        String size = "";
        String color = "";
        int bagQty = 0;
        int palletQty = 1;
        int idPalletProduct = 0;
        int idStorage = 0;
        int idSubstorage = 0;
        String result = "";
        xmlTable tabla;
        String campName = "";
        int nextNum = 0;
        String serial = "";

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            palletName = GetNodeArray().find("palletName").getStringValue();
            productName = GetNodeArray().find("productName").getStringValue();
            storageName = GetNodeArray().find("storageName").getStringValue();
            substorageName = GetNodeArray().find("substorageName").getStringValue();
            quantity = GetNodeArray().find("quantity").getIntValue();
            campName = GetNodeArray().find("CampName").getStringValue();

            //validate quantity
            if (quantity <= 0) {
                conn.rollback();
                resultArray.add("error", "La cantidad debe de ser mayor a 0.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "La cantidad no puede ser 0.");
                resultArray.add("RESPONSE_DETAIL", "");
                return resultArray;
            }

            //year & month
            SimpleDateFormat yf = new SimpleDateFormat("yyyy");
            year = yf.format(date);
            SimpleDateFormat mf = new SimpleDateFormat("MMM", Locale.ENGLISH);
            month = mf.format(date);

            //get month code from sidconfiguration
            pstmtSelectSecuenceYear.setString(1, month);
            rset = pstmtSelectSecuenceYear.executeQuery();
            if (rset.next()) {
                monthCode = rset.getString("Tag");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //get pallet product Size & color
            pstmtSelectPalletProduct.setString(1, productName);
            rset = pstmtSelectPalletProduct.executeQuery();
            if (rset.next()) {
                idPalletProduct = rset.getInt("idPalletProduct");
                size = rset.getString("Size");
                color = rset.getString("Color");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //get pallet definition  / Quantity
            pstmtSelectPalletDefinition.setString(1, palletName);
            rset = pstmtSelectPalletDefinition.executeQuery();
            if (rset.next()) {
                bagQty = rset.getInt("Quantity");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //get storage name
            pstmtSelectStorage.setString(1, storageName);
            rset = pstmtSelectStorage.executeQuery();
            if (rset.next()) {
                idStorage = rset.getInt("idStorage");
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //get substorage name
            pstmtSelectSubstorage.setString(1, substorageName);
            rset = pstmtSelectSubstorage.executeQuery();
            if (rset.next()) {
                idSubstorage = rset.getInt("idSubstorage");
            }
            if (rset != null) {
                rset.close();
                rset = null;
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

            //Insert
            tabla = new xmlTable();
            tabla.addField("PalletId");
            tabla.addField("ProductName");
            tabla.addField("Color");
            tabla.addField("Quantity");
            tabla.addField("Size");
            tabla.addField("CampName");
            
            // Quitar le guion de productName
            productName = deleteGuionFromProductName(productName);
            
            for (int i = 0; i < quantity; i++) {
                //generate pallet Id
                /*rset = pstmtSelectPalletId.executeQuery();
                if (rset.next()) {
                    palletId = year + monthCode + productName + rset.getString("Num");
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }*/
                
                // Nueva logica de Generacion del codigo de barras
                palletId = year + monthCode + productName;
                
                // Lee e incrementa secuencia con bloqueo de fila
                pstmVerifyNumGenerated.setString(1, palletId);
                rset = pstmVerifyNumGenerated.executeQuery();
                if (rset.next()) {
                    int current = rset.getInt("next_num");
                    nextNum = current + 1;
                    
                    rset.close(); 
                    rset = null;

                    pstmtSeqUpdate.setInt(1, nextNum);
                    pstmtSeqUpdate.setString(2, palletId);
                    pstmtSeqUpdate.executeUpdate();
                } else {
                    // no existe fila para este prefijo → primer uso
                    rset.close(); 
                    rset = null;

                    pstmtInsertPalletSequence.setString(1, palletId);
                    pstmtInsertPalletSequence.executeUpdate();
                    nextNum = 1;
                }

                // Arma el PalletId final
                serial  = String.format("%06d", nextNum);
                palletId = year + monthCode + productName + serial;

                //Insert pallet info
                pstmtInsertPalletInfo.setString(1, palletId);
                pstmtInsertPalletInfo.setString(2, productName);
                pstmtInsertPalletInfo.setString(3, size);
                pstmtInsertPalletInfo.setString(4, color);
                pstmtInsertPalletInfo.setInt(5, bagQty);
                pstmtInsertPalletInfo.setString(6, campName);
                pstmtInsertPalletInfo.setInt(7, idUser);
                
                // Insert in the table from JSP
                tabla.addRow();
                tabla.setValue(tabla.getRowsQty() - 1, "PalletId", palletId);
                tabla.setValue(tabla.getRowsQty() - 1, "ProductName", productName);
                tabla.setValue(tabla.getRowsQty() - 1, "Color", color);
                tabla.setValue(tabla.getRowsQty() - 1, "Quantity", bagQty);
                tabla.setValue(tabla.getRowsQty() - 1, "Size", size);
                tabla.setValue(tabla.getRowsQty() - 1, "CampName", campName);
                
                rowsAffected = pstmtInsertPalletInfo.executeUpdate();
                if (rowsAffected > 0) {
                    //Insert Inventory
                    pstmtInsertInventory.setInt(1, idStorage);
                    pstmtInsertInventory.setInt(2, idSubstorage);
                    pstmtInsertInventory.setInt(3, idPalletProduct);
                    pstmtInsertInventory.setInt(4, palletQty);
                    pstmtInsertInventory.setString(5, palletId);
                    rowsAffected = pstmtInsertInventory.executeUpdate();

                    if (rowsAffected > 0) {
                        //Insert Movement
                        pstmtInsertInventoryMovement.setString(1, palletId);
                        pstmtInsertInventoryMovement.setString(2, "NuevoPallet");
                        pstmtInsertInventoryMovement.setString(3, "Entrada");
                        pstmtInsertInventoryMovement.setString(4, productName);
                        pstmtInsertInventoryMovement.setString(5, size);
                        pstmtInsertInventoryMovement.setString(6, color);
                        pstmtInsertInventoryMovement.setString(7, campName);                        
                        pstmtInsertInventoryMovement.setInt(8, bagQty);                        
                        pstmtInsertInventoryMovement.setString(9, null);
                        pstmtInsertInventoryMovement.setString(10, null);
                        pstmtInsertInventoryMovement.setString(11, storageName);
                        pstmtInsertInventoryMovement.setString(12, substorageName);
                        pstmtInsertInventoryMovement.setString(13, "");
                        pstmtInsertInventoryMovement.setString(14, userName);
                        rowsAffected = pstmtInsertInventoryMovement.executeUpdate();
                        if(rowsAffected > 0){
                            result += "1";
                        }else{
                            result += "0";
                        }                        
                    } else {
                        result += "0";
                    }
                } else {
                    result += "0";
                }

                //update pallet id
                pstmtUpdatePalletId.executeUpdate();
            }

            if (!result.contains("0")) {
                conn.commit();
                resultArray.add("PalletJustSaved_Table", tabla);
                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", "Pallets registrados exitosamente.");
                resultArray.add("RESPONSE_DETAIL", "");
            } else {
                conn.rollback();
                resultArray.add("error", "Los pallets no pudieron ser registrados.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Los pallets no pudieron ser registrados.");
                resultArray.add("RESPONSE_DETAIL", "");
            }
            
            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveNewPalletTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveNewPalletTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            CloseStatements();
        }
    }
    
    private String deleteGuionFromProductName(String productName) {
        return productName.replaceAll("-", "");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveNewPalletTransaction");

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
            SaveNewPalletTransaction transaction = new SaveNewPalletTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveNewPalletTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveNewPalletTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveNewPalletTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveNewPalletTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveNewPalletTransaction - No results were returned.");
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
            System.out.println("SaveNewPalletTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
