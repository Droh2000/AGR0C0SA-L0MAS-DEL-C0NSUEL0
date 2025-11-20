package AgrocosaTransactions;

/**
 *
 * @author gomez
**/
import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class WarehouseProductManagementTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected PreparedStatement pstmtSelectByFilter;
    protected PreparedStatement pstmtUpdate;
    protected PreparedStatement pstmtDelete;
    protected PreparedStatement pstmtInsert;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public WarehouseProductManagementTransaction() {
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
            "idUser"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<WarehouseProductManagementTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<WarehouseProductManagementTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<WarehouseProductManagementTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<WarehouseProductManagementTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
     * @return <B>true</B> for successful preparation;
     * <B>false</B> for unsuccessful preparation
     * @exception (none)
     */
    @Override
    public synchronized boolean PrepareStatements() {
        //Note1 : Use PreparedStatements instead of Statements where ever possible
        //Note2 : If transaction contains no prepared statements, delete entire function
        //        Unless there are nested transaction, then Prepare will call those.
        try {
            Connection con = this.GetSIDDataBase().GetConnection();
            pstmtSelect = con.prepareStatement(
                    "Select "
                    + "wp.idWarehouseProduct, "
                    + "wp.Name, "
                    + "wp.CodeProduct, "
                    + "wt.WarehouseName, "
                    + "wp.MaxLevel, "
                    + "wp.MinLevel, "
                    + "wp.CycleCount, "
                    + "concat(u.FirstName, ' ', u.LastName) as UserName, "
                    + "date_format(wp.InsertDate,'%d/%m/%Y %h:%i %p') as InsertDate, "
                    + "date_format(wp.ModifiedDate,'%d/%m/%Y %h:%i %p') as ModifiedDate "
                    + "From warehouseproduct wp "
                    + "inner join warehousetype wt on wt.idWarehouseType = wp.idWarehouseType "
                    + "inner join user u on u.idUser = wp.idUser "
                    + "Where wp.Active = 1 "
                    + "Order by wp.idWarehouseProduct");
            pstmtSelectByFilter = con.prepareStatement(
                    "Select "
                    + "wp.idWarehouseProduct, "
                    + "wp.Name, "
                    + "wp.CodeProduct, "
                    + "wt.WarehouseName, "
                    + "wp.MaxLevel, "
                    + "wp.MinLevel, "
                    + "wp.CycleCount, "
                    + "concat(u.FirstName, ' ', u.LastName) as UserName, "
                    + "date_format(wp.InsertDate,'%d/%m/%Y %h:%i %p') as InsertDate, "
                    + "date_format(wp.ModifiedDate,'%d/%m/%Y %h:%i %p') as ModifiedDate "
                    + "From warehouseproduct wp "
                    + "inner join warehousetype wt on wt.idWarehouseType = wp.idWarehouseType "
                    + "inner join user u on u.idUser = wp.idUser "
                    + "Where wp.Active = 1 "
                    + "and wp.Name like ? "
                    + "and wt.WarehouseName like ? "
                    + "Order by wp.idWarehouseProduct");
            pstmtUpdate = con.prepareStatement("Update warehouseproduct set "
                    + "Name = ?, "
                    + "CodeProduct = ?, "
                    + "idWarehouseType = ?, "
                    + "MaxLevel = ?, "
                    + "MinLevel = ?, "
                    + "CycleCount = ?, "
                    + "idUser = ?, "
                    + "ModifiedDate = Now() "
                    + "where idWarehouseProduct = ? ");
            pstmtDelete = con.prepareStatement("Update warehouseproduct set "
                    + "Active = 0, ModifiedDate = Now() "
                    + "where idWarehouseProduct = ? ");
            pstmtInsert = con.prepareStatement("Insert into warehouseproduct ( "
                    + "Name, "
                    + "CodeProduct, "
                    + "idWarehouseType, "
                    + "MaxLevel, "
                    + "MinLevel, "
                    + "CycleCount, "
                    + "idUser, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active "
                    + ")Values( "
                    + "?, " //1 Name
                    + "?, " //2 CodeProduct
                    + "?, " //3 idWarehouseType
                    + "?, " //4 MaxLevel
                    + "?, " //5 MinLevel
                    + "?, " //6 CycleCount
                    + "?, " //7 idUser
                    + "Now(), " //  InsertDate
                    + "Now(), " //  ModifiedDate
                    + "1 " //  Active
                    + ")");

            this.addPreparedStatement(pstmtSelect);
            this.addPreparedStatement(pstmtSelectByFilter);
            this.addPreparedStatement(pstmtUpdate);
            this.addPreparedStatement(pstmtDelete);
            this.addPreparedStatement(pstmtInsert);

            return true;
        } catch (SQLException e) {
            System.out.println("WarehouseProductManagementTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
    public synchronized xmlNodeArray Execute() throws SQLException, Exception {
        Connection conn = null;
        xmlNodeArray resultArray = null;
        ResultSet rset = null;
        String message = "OK";
        int idUser = 0;
        int InsertConteo = 0;
        int counter = 0;
        String conteo = "";
        String name = "%";
        String warehouseTypeName = "%";
        
        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();
            xmlNodeArray param = this.GetNodeArray();
            
            //Mandatory 
            idUser = GetNodeArray().find("idUser").getIntValue();

            if (this.GetNodeArray().existValue("CycleCount")) {
                conteo = GetNodeArray().find("CycleCount").getStringValue();
                resultArray.add("CycleCount", conteo);
                if (conteo.equals("Si")) {
                    InsertConteo = 1;
                } else {
                    InsertConteo = 0;
                }
            }

            if (param.existValue("Method") && counter <= 0) {
                String met = param.find("Method").getStringValue();
                
                if (met.equals("Save")) {
                    if (param.existValue("ID") && param.find("ID").getStringValue().equals("new")) {
                        //insert new 
                        pstmtInsert.setString(1, param.find("Name").getStringValue());
                        pstmtInsert.setString(2, param.find("CodeProduct").getStringValue());
                        pstmtInsert.setInt(3, param.find("WarehouseType").getIntValue());
                        pstmtInsert.setDouble(4, param.find("MaxLevel").getDoubleValue());
                        pstmtInsert.setDouble(5, param.find("MinLevel").getDoubleValue());
                        pstmtInsert.setInt(6, InsertConteo);
                        pstmtInsert.setInt(7, idUser);
                        pstmtInsert.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "El producto fue agregado exitosamente.";
                    } else if (param.existValue("ID")) {
                        //update existing
                        pstmtUpdate.setString(1, param.find("Name").getStringValue());
                        pstmtUpdate.setString(2, param.find("CodeProduct").getStringValue());
                        pstmtUpdate.setInt(3, param.find("WarehouseType").getIntValue());
                        pstmtUpdate.setDouble(4, param.find("MaxLevel").getDoubleValue());
                        pstmtUpdate.setDouble(5, param.find("MinLevel").getDoubleValue());
                        pstmtUpdate.setInt(6, InsertConteo);
                        pstmtUpdate.setInt(7, idUser);
                        pstmtUpdate.setInt(8, param.find("ID").getIntValue());
                        pstmtUpdate.executeUpdate();

                        resultArray.add("Action", "Done");
                        message = "El producto fue actualizado exitosamente.";
                    } else {
                        resultArray.add("RESPONSE_CODE", "FAIL");
                        resultArray.add("RESPONSE_MESSAGE", "No se encontro el ID del registro");
                        resultArray.add("RESPONSE_DETAIL", "");
                        return resultArray;
                    }
                } else if (met.equals("Delete")) {
                    if (param.existValue("ID") && param.find("ID").getIntValue() > 0) {
                        //delete 
                        pstmtDelete.setInt(1, param.find("ID").getIntValue());
                        pstmtDelete.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "El producto fue eliminado exitosamente.";
                    }
                }
                conn.commit();
            }

            rset = pstmtSelect.executeQuery();
            xmlTable tab = formatDataTable(rset);
            if (param.existValue("Method")) {
                String met = param.find("Method").getStringValue();
                if (met.equals("Add")) {
                    tab.addRow();
                    for (int x = 1; x < tab.getFieldsQty(); x++) {
                        tab.setValue(tab.getRowsQty() - 1, x, "");
                    }
                    tab.setValue(tab.getRowsQty() - 1, 0, "new");
                }
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            resultArray.add("WarehouseProduct_Table", tab);

            if (this.GetNodeArray().existValue("SEARCH")
                    && this.GetNodeArray().find("SEARCH").getStringValue().equals("YES")) {

                if (this.GetNodeArray().existValue("SearchName")) {
                    name = GetNodeArray().find("SearchName").getStringValue();
                    resultArray.add("NameSelected", name);
                }

                if (this.GetNodeArray().existValue("SearchWearhouseType")) {
                    warehouseTypeName = GetNodeArray().find("SearchWearhouseType").getStringValue();
                    resultArray.add("WearhouseTypeNameSelected", warehouseTypeName);
                }
                pstmtSelectByFilter.setString(1, "%" + name + "%");
                pstmtSelectByFilter.setString(2, warehouseTypeName);
                rset = pstmtSelectByFilter.executeQuery();
                System.out.println("RESULSET: "+rset);
                xmlTable infoTable = this.formatDataTable(rset);
                resultArray.add("WarehouseProductx_Table", infoTable);
                if (rset != null) {
                    rset.close();
                    rset = null;
                }

            }
            else {
                resultArray.add("WarehouseProductx_Table", tab);
            }

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");
            
            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("<WarehouseProductManagementTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            e.printStackTrace();
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("<WarehouseProductManagementTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception");
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            ex.printStackTrace();
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
     * @param (none)
     * @return xmlNodeArray that contains parameters for the transaction
     * @exception (none)
     */
    public xmlNodeArray GenerateTestParameters() {
        xmlNodeArray nodeArr = new xmlNodeArray();
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "SCM.WarehouseProductManagementTransaction");
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
            WarehouseProductManagementTransaction transaction = new WarehouseProductManagementTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...SCM.WarehouseProductManagementTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://www.sidchihuahua.com:3306/scm", "mfc_admin", "M0vf4mCr12015");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" WarehouseProductManagementTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" WarehouseProductManagementTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("SCM.WarehouseProductManagementTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("WarehouseProductManagementTransaction - No results were returned.");
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
            System.out.println("WarehouseProductManagementTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
