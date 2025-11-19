/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import xmlNodeArray.*;

public class SaveNewStockTransferOrderTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtInsertStockTransferOrderDetail;
    protected PreparedStatement pstmtInsertStockTransferOrderHistory;
    protected PreparedStatement pstmtPalletInfo;
    protected PreparedStatement pstmtUser;
    protected PreparedStatement pstmtInsertInventoryMovement;
    protected PreparedStatement pstmtInsertInventory;
    protected PreparedStatement pstmtSelectTransferOrder;
    protected PreparedStatement pstmtExistsPalletInAnyOrder;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveNewStockTransferOrderTransaction() {
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
            "idEmbarque",
            "idUser",
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
                System.out.println("<SaveNewStockTransferOrderTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveNewStockTransferOrderTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveNewStockTransferOrderTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveNewStockTransferOrderTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtInsertStockTransferOrderDetail = con.prepareStatement("insert into stocktransferorderdetail ("
                    + "idStockTransferOrder, "
                    + "PalletId, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "Quantity, "
                    + "CampName "
                    + ") values("
                    + "?, "//1 idStockTransferOrder
                    + "?, "//2 PalletId
                    + "?, "//3 ProductName
                    + "?, "//4 Size
                    + "?, "//5 Color,
                    + "?,  "//6 Quantity
                    + "?  "//7 CampName
                    + ")");

            pstmtInsertStockTransferOrderHistory = con.prepareStatement("insert into stocktransferorderhistory ("
                    + "idStockTransferOrder, "
                    + "Proforma, "
                    + "Process, "
                    + "MovementType, "
                    + "Quantity, "
                    + "Comments, "
                    + "User, "
                    + "InsertDate, "
                    + "Active "
                    + ") values("
                    + "?, "//1 idStockTransferOrder
                    + "?, "//2 Proforma
                    + "?, "//3 Process
                    + "?, "//4 MovementType
                    + "?, "//5 Quantity
                    + "?, "//6 Comments
                    + "?, "//7 User
                    + "Now(), "
                    + "1 "
                    + ")");

            pstmtPalletInfo = con.prepareStatement("select "
                    + "PalletId, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "Quantity, "
                    + "CampName "
                    + "from palletinfo "
                    + "where PalletId = ? ");

            pstmtUser = con.prepareStatement("select concat(FirstName,' ',LastName) User "
                    + "from user "
                    + "where idUser = ? ");

            pstmtInsertInventoryMovement = con.prepareStatement("insert into inventorymovement ("
                    + "PalletId, "
                    + "Process, "
                    + "MovementType, "
                    + "ProductName, "
                    + "Size, "
                    + "Color, "
                    + "CampName, "// Agregar Campo de "CampName"
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

            pstmtInsertInventory = con.prepareStatement("SELECT "
                    + "storage.StorageName, "
                    + "substorage.SubstorageName "
                    + "FROM inventory inner join "
                    + "storage on storage.idStorage = inventory.idStorage inner join "
                    + "substorage on substorage.idSubstorage = inventory.idSubstorage "
                    + "Where inventory.PalletId = ?");
            
            pstmtSelectTransferOrder = con.prepareStatement("SELECT "
                    + "idStockTransferOrder, "
                    + "TransportCompany, "
                    + "TruckDriverName, "
                    + "TruckPlate, "
                    + "TruckBoxNumber "
                    + "FROM stocktransferorder "
                    + "WHERE idStockTransferOrder = ?"
            );
            
            pstmtExistsPalletInAnyOrder = con.prepareStatement(
                "SELECT d.idStockTransferOrder " +
                "FROM stocktransferorderdetail d " +
                "JOIN stocktransferorder s ON s.idStockTransferOrder = d.idStockTransferOrder " +
                "WHERE d.PalletId = ? AND s.Active = 1 " +
                " AND s.Status IN ('Nueva','EnTransito','Embarcada')" // para filtrar solo “abiertas”
            );

            this.addPreparedStatement(pstmtInsertStockTransferOrderDetail);
            this.addPreparedStatement(pstmtInsertStockTransferOrderHistory);
            this.addPreparedStatement(pstmtPalletInfo);
            this.addPreparedStatement(pstmtUser);
            this.addPreparedStatement(pstmtInsertInventoryMovement);
            this.addPreparedStatement(pstmtInsertInventory);
            this.addPreparedStatement(pstmtSelectTransferOrder);
            this.addPreparedStatement(pstmtExistsPalletInAnyOrder);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveNewStockTransferOrderTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String transportCompanyName = "";
        String truckDriverName = "";
        String truckPlate = "";
        String palletInfo = "";
        String productName = "";
        String size = "";
        String color = "";
        int quantity = 0;
        String palletId = "";
        String result = "";
        int palletQty = 0;
        String storageName = null;
        String substorageName = null;
        String truckBoxNumber = "";
        String validPalletInfo = "";
        int idEmbarque = 0;
        String campName = "";

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            palletInfo = GetNodeArray().find("palletInfo").getStringValue();
            idEmbarque = Integer.parseInt(GetNodeArray().find("idEmbarque").getStringValue());

            String lines[] = palletInfo.trim().split("\\r?\\n");
            palletQty = lines.length;
            System.out.println("Cantidad de Pallets Obtnenidos: "+palletQty);
            
            List<String> palletIds = new ArrayList<>();
            
            // Get Data from Transport
            pstmtSelectTransferOrder.setInt(1, idEmbarque);
            rset = pstmtSelectTransferOrder.executeQuery();
            if (rset.next()) {
                transportCompanyName = rset.getString("TransportCompany");
                truckDriverName = rset.getString("TruckDriverName");
                truckPlate = rset.getString("TruckPlate");
                truckBoxNumber = rset.getString("TruckBoxNumber"); 
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

            //validate if pallet id exist in palletinfo table 
            for (int i = 0; i < lines.length; i++) {
                palletId = lines[i].trim();
                System.out.println("Pallet Registrado: "+palletId);
                //if (!palletId.isEmpty() || !palletId.equals("")){ 
                    palletIds.add(palletId);
                    pstmtPalletInfo.setString(1, palletId);
                    rset = pstmtPalletInfo.executeQuery();
                    if (rset.next()) {
                        validPalletInfo += "1";
                    } else {
                        validPalletInfo += "0";
                    }
                    if (rset != null) {
                        rset.close();
                        rset = null;
                    }
                //}
            }

            if (validPalletInfo.contains("0")) {
                conn.rollback();
                resultArray.add("error", "Pallet ID invalido.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Alguno de los Pallet ID es invalido. Por favor verifiquelo.");
                resultArray.add("RESPONSE_DETAIL", "");
                return resultArray;
            }

            // dedup local para no consultar dos veces el mismo
            LinkedHashSet<String> uniquePallets = new LinkedHashSet<>(palletIds);
            
            List<String> yaOcupados = new ArrayList<>();

            for (String pallId : uniquePallets) {
                pstmtExistsPalletInAnyOrder.setString(1, pallId);
                rset = pstmtExistsPalletInAnyOrder.executeQuery();
                if (rset.next()) {
                    yaOcupados.add(pallId);
                }
                if (rset != null) { rset.close(); rset = null; }
            }

            if (!yaOcupados.isEmpty()) {
                // No insertes nada, aborta con FAIL
                conn.rollback();
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Pallet(s) ya asignado(s) a otro embarque.");
                resultArray.add("RESPONSE_DETAIL", String.join(", ", yaOcupados));
                // Cierra PS y retorna
                if (pstmtExistsPalletInAnyOrder != null) { pstmtExistsPalletInAnyOrder.close(); pstmtExistsPalletInAnyOrder = null; }
                return resultArray;
            }

            //Insert
            for (int i = 0; i < lines.length; i++) {
                palletId = lines[i].trim();
                //get productName Size, color & quantity
                pstmtPalletInfo.setString(1, palletId);
                rset = pstmtPalletInfo.executeQuery();
                if (rset.next()) {
                    productName = rset.getString("ProductName");
                    size = rset.getString("Size");
                    color = rset.getString("Color");
                    campName = rset.getString("CampName");
                    quantity = rset.getInt("Quantity");
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }

                //get storage * substorage
                pstmtInsertInventory.setString(1, palletId);
                rset = pstmtInsertInventory.executeQuery();
                if (rset.next()) {
                    storageName = rset.getString("StorageName");
                    substorageName = rset.getString("SubstorageName");
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }

                //Insert stocktransferorderdetail
                pstmtInsertStockTransferOrderDetail.setInt(1, idEmbarque);
                pstmtInsertStockTransferOrderDetail.setString(2, palletId);
                pstmtInsertStockTransferOrderDetail.setString(3, productName);
                pstmtInsertStockTransferOrderDetail.setString(4, size);
                pstmtInsertStockTransferOrderDetail.setString(5, color);
                pstmtInsertStockTransferOrderDetail.setInt(6, quantity);
                pstmtInsertStockTransferOrderDetail.setString(7, campName);
                rowsAffected = pstmtInsertStockTransferOrderDetail.executeUpdate();
                if (rowsAffected > 0) {
                    //Insert pallet Movement
                    pstmtInsertInventoryMovement.setString(1, palletId);
                    pstmtInsertInventoryMovement.setString(2, "Embarque");
                    pstmtInsertInventoryMovement.setString(3, "Salida");
                    pstmtInsertInventoryMovement.setString(4, productName);
                    pstmtInsertInventoryMovement.setString(5, size);
                    pstmtInsertInventoryMovement.setString(6, color);
                    pstmtInsertInventoryMovement.setString(7, campName);
                    pstmtInsertInventoryMovement.setInt(8, quantity);
                    pstmtInsertInventoryMovement.setString(9, null);
                    pstmtInsertInventoryMovement.setString(10, null);
                    pstmtInsertInventoryMovement.setString(11, storageName);
                    pstmtInsertInventoryMovement.setString(12, substorageName);
                    pstmtInsertInventoryMovement.setString(13, idEmbarque + ", " + transportCompanyName + ", " + truckDriverName + ", " + truckPlate + ", " + truckBoxNumber);
                    pstmtInsertInventoryMovement.setString(14, userName);
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

            if (!result.contains("0")) {
                //Insert stocktransferorderhistory
                pstmtInsertStockTransferOrderHistory.setInt(1, idEmbarque);
                pstmtInsertStockTransferOrderHistory.setString(2, null);
                pstmtInsertStockTransferOrderHistory.setString(3, "Embarque");
                pstmtInsertStockTransferOrderHistory.setString(4, "Nueva");
                pstmtInsertStockTransferOrderHistory.setInt(5, palletQty);
                pstmtInsertStockTransferOrderHistory.setString(6, idEmbarque + ", " + transportCompanyName + ", " + truckDriverName + ", " + truckPlate + ", " + truckBoxNumber);
                pstmtInsertStockTransferOrderHistory.setString(7, userName);
                rowsAffected = pstmtInsertStockTransferOrderHistory.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    resultArray.add("RESPONSE_CODE", "PASS");
                    resultArray.add("RESPONSE_MESSAGE", "Embarque registrado exitosamente.");
                    resultArray.add("RESPONSE_DETAIL", "");
                } else {
                    conn.rollback();
                    resultArray.add("error", "El Embarque no pudo ser registrado.");
                    resultArray.add("RESPONSE_CODE", "FAIL");
                    resultArray.add("RESPONSE_MESSAGE", "El pallet no pudo ser registrado.");
                    resultArray.add("RESPONSE_DETAIL", "");
                }
            } else {
                conn.rollback();
                resultArray.add("error", "El Embarque no pudo ser registrado.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "El pallet no pudo ser registrado.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveNewStockTransferOrderTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveNewStockTransferOrderTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveNewStockTransferOrderTransaction");

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
            SaveNewStockTransferOrderTransaction transaction = new SaveNewStockTransferOrderTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveNewStockTransferOrderTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveNewStockTransferOrderTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveNewStockTransferOrderTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveNewStockTransferOrderTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveNewStockTransferOrderTransaction - No results were returned.");
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
            System.out.println("SaveNewStockTransferOrderTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
