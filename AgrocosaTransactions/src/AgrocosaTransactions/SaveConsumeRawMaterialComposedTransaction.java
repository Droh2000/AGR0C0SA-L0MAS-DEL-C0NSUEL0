/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveConsumeRawMaterialComposedTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectBomDetail;
    protected PreparedStatement pstmtSelectUomConversion;
    protected PreparedStatement pstmtSelectRMInventory;
    protected PreparedStatement pstmtUpdateRMInventory;
    protected PreparedStatement pstmtInsertRMInventoryLog;
    protected PreparedStatement pstmtSelectStorage;
    protected PreparedStatement pstmtUser;
    protected PreparedStatement pstmtDelete;
    protected PreparedStatement pstmtRawMaterialTypeName;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveConsumeRawMaterialComposedTransaction() {
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
            "dataTable4Save"
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
                System.out.println("<SaveConsumeRawMaterialComposedTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveConsumeRawMaterialComposedTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveConsumeRawMaterialComposedTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveConsumeRawMaterialComposedTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtSelectBomDetail = con.prepareStatement("select "
                    + "bomdetail.idBomDetail, "
                    + "bomdetail.idBom, "
                    + "bomdetail.RawMaterialName, "
                    + "bomdetail.Quantity, "
                    + "bomdetail.UOM "
                    + "from bomdetail inner join "
                    + "bom on bom.idBom = bomdetail.idBom "
                    + "Where bom.BomName = ? "
                    + "and bomdetail.Active = 1 ");

            pstmtSelectUomConversion = con.prepareStatement("select "
                    + "uomconversion.idUomConversion, "
                    + "uomconversion.UomFrom, "
                    + "uomconversion.UomTo, "
                    + "uomconversion.Conversion "
                    + "from uomconversion "
                    + "Where uomconversion.UomFrom = ?  "
                    + "and uomconversion.UomTo = ? "
                    + "and uomconversion.Active = 1 ");

            pstmtSelectRMInventory = con.prepareStatement("select "
                    + "idStorage, "
                    + "idSubstorage, "
                    + "RawMaterialName, "
                    + "Quantity, "
                    + "UOM "
                    + "from rawmaterialinventory "
                    + "where RawMaterialName = ? ");//1 RawMaterialName

            pstmtUpdateRMInventory = con.prepareStatement("update rawmaterialinventory set "
                    + "Quantity = Quantity - ? "//1 Quanity
                    + "where idStorage = ? "//2 idStorage
                    + "and idSubstorage = ? "//3 idSubstorage
                    + "and RawMaterialName = ? ");

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
                    + "where storage.idStorage = ? "//1 idStorageName
                    + "and substorage.idSubstorage = ?");//2 idSubstorageName

            pstmtUser = con.prepareStatement("select concat(FirstName,' ',LastName) User "
                    + "from user "
                    + "where idUser = ? ");

            pstmtDelete = con.prepareStatement("delete  "
                    + "from rawmaterialinventory "
                    + "Where Quantity <= 0");

            pstmtRawMaterialTypeName = con.prepareStatement("select "
                    + "rawmaterialtype.RawMaterialTypeName "
                    + "from rawmaterial inner join "
                    + "rawmaterialtype on rawmaterialtype.idRawMaterialType = rawmaterial.idRawMaterialType "
                    + "Where rawmaterial.RawMaterialName = ? ");
                   
            this.addPreparedStatement(pstmtSelectBomDetail);
            this.addPreparedStatement(pstmtSelectUomConversion);
            this.addPreparedStatement(pstmtSelectRMInventory);
            this.addPreparedStatement(pstmtUpdateRMInventory);
            this.addPreparedStatement(pstmtInsertRMInventoryLog);
            this.addPreparedStatement(pstmtSelectStorage);
            this.addPreparedStatement(pstmtUser);
            this.addPreparedStatement(pstmtDelete);
            this.addPreparedStatement(pstmtRawMaterialTypeName);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveConsumeRawMaterialComposedTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String dataTable4Save = "";
        String comments = "";
        String userName = "";
        String bomName = "";
        String rawMaterialTypeName = null;
        String rawMaterialName = null;
        String storageName = null;
        String substorageName = null;
        String uom = null;
        int quantity = 0;
        int idStorage = 0;
        int idSubstorage = 0;
        String result = "";
        String result2 = "";
        double bomQty = 0;
        String bomUom = "";
        String invUom = "";
        double conversion = 0;
        double moveQty = 0;

        try {
            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            dataTable4Save = GetNodeArray().find("dataTable4Save").getStringValue();

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

            //replace
            dataTable4Save = dataTable4Save.replace("[[", "");
            dataTable4Save = dataTable4Save.replace("]]", "");
            dataTable4Save = dataTable4Save.replace("\"", "");
            dataTable4Save = dataTable4Save.replace("],[", "|");

            String[] rowsData = dataTable4Save.split("\\|");

            //Insert detail
            for (String rowsData1 : rowsData) {
                String[] fieldData = rowsData1.split(",");
                bomName = fieldData[0]; //bomName
                quantity = Integer.parseInt(fieldData[1]); //quantity

                xmlTable bomDetailTable = new xmlTable();
                bomDetailTable.addField("RawMaterialName");
                bomDetailTable.addField("Quantity");
                bomDetailTable.addField("BomDetailUOM");
                bomDetailTable.addField("InventoryUOM");
                bomDetailTable.addField("Conversion");

                //get all bom detail
                pstmtSelectBomDetail.setString(1, bomName);
                rset = pstmtSelectBomDetail.executeQuery();
                while (rset.next()) {
                    rawMaterialName = rset.getString("RawMaterialName");
                    bomQty = rset.getDouble("Quantity");
                    bomUom = rset.getString("UOM");

                    bomDetailTable.addRow();
                    bomDetailTable.setValue(bomDetailTable.getRowsQty() - 1, "RawMaterialName", rawMaterialName);
                    bomDetailTable.setValue(bomDetailTable.getRowsQty() - 1, "Quantity", bomQty);
                    bomDetailTable.setValue(bomDetailTable.getRowsQty() - 1, "BomDetailUOM", bomUom);
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }

                //validate if each bom detail inventory exist
                for (int i = 0; i < bomDetailTable.getRowsQty(); i++) {
                    //validate if record exist in bininventory table                    
                    pstmtSelectRMInventory.setString(1, bomDetailTable.getStringValue(i, "RawMaterialName"));
                    rset = pstmtSelectRMInventory.executeQuery();
                    if (!rset.next()) {
                        result += rawMaterialName + ", ";
                    }
                    if (rset != null) {
                        rset.close();
                        rset = null;
                    }
                }

                if (result.equals("")) {
                    //validate each bom detail conversion 
                    for (int i = 0; i < bomDetailTable.getRowsQty(); i++) {
                        //validate if record exist in bininventory table                    
                        pstmtSelectRMInventory.setString(1, bomDetailTable.getStringValue(i, "RawMaterialName"));
                        rset = pstmtSelectRMInventory.executeQuery();
                        if (rset.next()) {
                            invUom = rset.getString("UOM");
                            bomDetailTable.setValue(i, "InventoryUOM", invUom);
                        }
                        if (rset != null) {
                            rset.close();
                            rset = null;
                        }

                        //validate UOM Conversion
                        pstmtSelectUomConversion.setString(1, bomDetailTable.getStringValue(i, "BomDetailUOM"));//From
                        pstmtSelectUomConversion.setString(2, invUom);//To
                        rset = pstmtSelectUomConversion.executeQuery();
                        if (rset.next()) {
                            conversion = rset.getDouble("Conversion");
                            bomDetailTable.setValue(i, "Conversion", conversion);
                        } else {
                            result += rawMaterialName + ", ";
                        }
                        if (rset != null) {
                            rset.close();
                            rset = null;
                        }

                    }
                }

            }
            if (!result.equals("")) {
                conn.rollback();
                resultArray.add("error", "Los siguientes insumos no se encuentran con inventario. Ningun movimiento ha sido registrado.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Los siguientes insumos no se encuentran con inventario. Ningun movimiento ha sido registrado.");
                resultArray.add("RESPONSE_DETAIL", result);
                return resultArray;
            }
            if (!result2.equals("")) {
                conn.rollback();
                resultArray.add("error", "Las siguientes conversiones de unidad de medida de insumos no se encuentran configurados. Ningun movimiento ha sido registrado.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Las siguientes conversiones de unidad de medida de insumos no se encuentran configurados. Ningun movimiento ha sido registrado.");
                resultArray.add("RESPONSE_DETAIL", result2);
                return resultArray;
            }

            //save
            for (String rowsData1 : rowsData) {
                String[] fieldData = rowsData1.split(",");
                bomName = fieldData[0]; //bomName
                quantity = Integer.parseInt(fieldData[1]); //quantity

                //get all bom detail
                pstmtSelectBomDetail.setString(1, bomName);
                rset = pstmtSelectBomDetail.executeQuery();
                while (rset.next()) {
                    rawMaterialName = rset.getString("RawMaterialName");
                    bomQty = rset.getDouble("Quantity");
                    bomUom = rset.getString("UOM");

                    //raw material type
                    pstmtRawMaterialTypeName.setString(1, rawMaterialName);
                    rset2 = pstmtRawMaterialTypeName.executeQuery();
                    if (rset2.next()) {
                        rawMaterialTypeName = rset2.getString("RawMaterialTypeName");
                    }
                    if (rset2 != null) {
                        rset2.close();
                        rset2 = null;
                    }
                    
                    pstmtSelectRMInventory.setString(1, rawMaterialName);
                    rset2 = pstmtSelectRMInventory.executeQuery();
                    if (rset2.next()) {
                        idStorage = rset2.getInt("idStorage");
                        idSubstorage = rset2.getInt("idSubstorage");
                        invUom = rset2.getString("UOM");
                    }
                    if (rset2 != null) {
                        rset2.close();
                        rset2 = null;
                    }
                    
                    //get StorageName & SubstorageName
                    pstmtSelectStorage.setInt(1, idStorage);
                    pstmtSelectStorage.setInt(2, idSubstorage);
                    rset2 = pstmtSelectStorage.executeQuery();
                    if (rset2.next()) {
                        storageName = rset2.getString("StorageName");
                        substorageName = rset2.getString("SubstorageName");
                    }
                    if (rset2 != null) {
                        rset2.close();
                        rset2 = null;
                    }
                    
                    //validate UOM Conversion
                    pstmtSelectUomConversion.setString(1, bomUom);//From
                    pstmtSelectUomConversion.setString(2, invUom);//To
                    rset2 = pstmtSelectUomConversion.executeQuery();
                    if (rset2.next()) {
                        conversion = rset2.getDouble("Conversion");
                    } 
                    if (rset2 != null) {
                        rset2.close();
                        rset2 = null;
                    }

                    moveQty = ((quantity * bomQty) / conversion);
                            
                    //update
                    pstmtUpdateRMInventory.setDouble(1, moveQty);
                    pstmtUpdateRMInventory.setInt(2, idStorage);
                    pstmtUpdateRMInventory.setInt(3, idSubstorage);
                    pstmtUpdateRMInventory.setString(4, rawMaterialName);
                    rowsAffected = pstmtUpdateRMInventory.executeUpdate();
                    if (rowsAffected > 0) {
                        //insert bininventorymovement
                        pstmtInsertRMInventoryLog.setString(1, "Salida");
                        pstmtInsertRMInventoryLog.setString(2, rawMaterialTypeName);
                        pstmtInsertRMInventoryLog.setString(3, rawMaterialName);
                        pstmtInsertRMInventoryLog.setDouble(4, moveQty);
                        pstmtInsertRMInventoryLog.setString(5, invUom);
                        pstmtInsertRMInventoryLog.setString(6, storageName);
                        pstmtInsertRMInventoryLog.setString(7, substorageName);
                        pstmtInsertRMInventoryLog.setString(8, comments);
                        pstmtInsertRMInventoryLog.setString(9, userName);
                        rowsAffected = pstmtInsertRMInventoryLog.executeUpdate();
                        if (rowsAffected > 0) {

                            //delete inventory negative or zero
                            pstmtDelete.executeUpdate();

                            conn.commit();
                            resultArray.add("Result", "La salida de insumos han sido registrados exitosamente.");
                            resultArray.add("RESPONSE_CODE", "PASS");
                            resultArray.add("RESPONSE_MESSAGE", "La salida de insumos han sido registrados exitosamente.");
                            resultArray.add("RESPONSE_DETAIL", "");
                        } else {
                            conn.rollback();
                            resultArray.add("Result", "La salida de insumos no pudieron ser registrados.");
                            resultArray.add("RESPONSE_CODE", "FAIL");
                            resultArray.add("RESPONSE_MESSAGE", "La Informacion no pudo ser registrada.");
                            resultArray.add("RESPONSE_DETAIL", "");
                        }
                    } else {
                        conn.rollback();
                        resultArray.add("Result", "La salida de insumos no pudieron ser registrados.");
                        resultArray.add("RESPONSE_CODE", "FAIL");
                        resultArray.add("RESPONSE_MESSAGE", "La Informacion no pudo ser registrada.");
                        resultArray.add("RESPONSE_DETAIL", "");
                    }

                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
            }

            return resultArray;

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveConsumeRawMaterialComposedTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveConsumeRawMaterialComposedTransaction::Execute> Exception: " + ex.getMessage());
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
            if (rset2 != null) {
                rset2.close();
                rset2 = null;
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveConsumeRawMaterialComposedTransaction");
        nodeArr.add("idUser", "1");
        nodeArr.add("customer", "BRP");
        nodeArr.add("comments", "Embarcar antes del medio dia");
        nodeArr.add("dataTable4Save", "[[\"BRP\",\"707900700\",\"1\",\"x\"],[\"BRP\",\"707900702\",\"2\",\"x\"],[\"BRP\",\"707900704\",\"3\",\"x\"],[\"BRP\",\"707900705\",\"4\",\"x\"]]");

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
            SaveConsumeRawMaterialComposedTransaction transaction = new SaveConsumeRawMaterialComposedTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveConsumeRawMaterialComposedTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveConsumeRawMaterialComposedTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveConsumeRawMaterialComposedTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveConsumeRawMaterialComposedTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveConsumeRawMaterialComposedTransaction - No results were returned.");
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
            System.out.println("SaveConsumeRawMaterialComposedTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
