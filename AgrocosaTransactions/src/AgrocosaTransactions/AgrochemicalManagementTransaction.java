package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class AgrochemicalManagementTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected PreparedStatement pstmtUpdate;
    protected PreparedStatement pstmtDelete;
    protected PreparedStatement pstmtInsert;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public AgrochemicalManagementTransaction() {
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
                System.out.println("<AgrochemicalManagementTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<AgrochemicalManagementTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<AgrochemicalManagementTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<AgrochemicalManagementTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
                    " select a.idAgrochemical, "
                    + " a.AgrochemicalName, "
                    + " a.Brand, "
                    + " a.ActiveIngredient, "
                    + " coalesce(a.MinDose,'') as MinDose, "
                    + " coalesce(a.MaxDose,'') as MaxDose, "
                    + " a.UOM, "
                    + " coalesce(a.SecurityInterval,'') as SecurityInterval, "
                    + " coalesce(a.DelayPeriod,'') as DelayPeriod,  "
                    + " a.AgrochemicalType, "
                    + " concat(u.FirstName,' ',u.LastName) User, "
                    + " date_format(a.InsertDate,'%d/%m/%Y %h:%i %p') as InsertDate, "
                    + " date_format(a.ModifiedDate,'%d/%m/%Y %h:%i %p') as ModifiedDate "
                    + " from agrochemical a inner join "
                    + " user u on u.idUser = a.idUser "
                    + " Where a.Active = 1 "
                    + " Order by a.AgrochemicalName");
            pstmtUpdate = con.prepareStatement("Update agrochemical set "
                    + " AgrochemicalName = ?, "
                    + " Brand = ?, "
                    + " ActiveIngredient = ?, "
                    + " MinDose = ?, "
                    + " MaxDose = ?, "
                    + " UOM = ?, "
                    + " AgrochemicalType = ?, "
                    + " SecurityInterval = ?, "
                    + " DelayPeriod = ?, "
                    + " idUser = ?, "
                    + " ModifiedDate = Now() "
                    + " Where idAgrochemical = ? ");
            pstmtDelete = con.prepareStatement("Update agrochemical set "
                    + "ModifiedDate = Now(), "
                    + "Active = 0, "
                    + "idUser = ? "
                    + "where idAgrochemical = ? ");
            pstmtInsert = con.prepareStatement("Insert into agrochemical ( "
                    + " AgrochemicalName, "
                    + " Brand, "
                    + " ActiveIngredient, "
                    + " MinDose, "
                    + " MaxDose, "
                    + " UOM, "
                    + " AgrochemicalType, "
                    + " SecurityInterval, "
                    + " DelayPeriod, "
                    + " idUser, "
                    + " InsertDate, "
                    + " ModifiedDate, "
                    + " Active "
                    + ")Values( "
                    + "?, " //1 AgrochecicalName
                    + "?, " //2 Brand
                    + "?, " //3 ActiveIngredient
                    + "?, " //4 MinDose
                    + "?, " //5 MaxDose
                    + "?, " //6 UOM
                    + "?, " //7 AgrochemicalType
                    + "?, " //8 SecurityInterval
                    + "?, " //9 DelayPeriod
                    + "?, " //10 idUser
                    + "Now(), " //  InsertDate
                    + "Now(), " //  ModifiedDate
                    + "1 " //  Active                    
                    + ")");

            this.addPreparedStatement(pstmtSelect);
            this.addPreparedStatement(pstmtUpdate);
            this.addPreparedStatement(pstmtDelete);
            this.addPreparedStatement(pstmtInsert);

            return true;
        } catch (SQLException e) {
            System.out.println("AgrochemicalManagementTransaction::PrepareStatements> SQLException: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * executes sql statements using input arguments and returns result
     *
     * @param (none)
     * @return valid node array if successful else null
     * @exception SQLException if sql error occurs
     * @exception Exception if non sql error occurs
     */
    public synchronized xmlNodeArray Execute() throws SQLException, Exception {
        Connection conn = null;
        xmlNodeArray resultArray = null;
        ResultSet rset = null;
        String message = "OK";

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();
            xmlNodeArray param = this.GetNodeArray();

            if (param.existValue("Method")) {
                String met = param.find("Method").getStringValue();

                if (met.equals("Save")) {
                    if (param.existValue("ID") && param.find("ID").getStringValue().equals("new")) {
                        //insert new 
                        pstmtInsert.setString(1, param.find("AgrochemicalName").getStringValue());
                        pstmtInsert.setString(2, param.find("Brand").getStringValue());
                        pstmtInsert.setString(3, param.find("ActiveIngredient").getStringValue());
                        pstmtInsert.setString(4, param.find("MinDose").getStringValue());
                        pstmtInsert.setString(5, param.find("MaxDose").getStringValue());
                        pstmtInsert.setString(6, param.find("UOM").getStringValue());
                        pstmtInsert.setString(7, param.find("AgrochemicalType").getStringValue());
                        pstmtInsert.setString(8, param.find("SecurityInterval").getStringValue());
                        pstmtInsert.setString(9, param.find("DelayPeriod").getStringValue());
                        pstmtInsert.setInt(10, param.find("idUser").getIntValue());
                        pstmtInsert.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "El agroquimico ha sido guardado existosamente.";
                    } else if (param.existValue("ID")) {
                        //update existing
                        pstmtUpdate.setString(1, param.find("AgrochemicalName").getStringValue());
                        pstmtUpdate.setString(2, param.find("Brand").getStringValue());
                        pstmtUpdate.setString(3, param.find("ActiveIngredient").getStringValue());
                        pstmtUpdate.setString(4, param.find("MinDose").getStringValue());
                        pstmtUpdate.setString(5, param.find("MaxDose").getStringValue());
                        pstmtUpdate.setString(6, param.find("UOM").getStringValue());
                        pstmtUpdate.setString(7, param.find("AgrochemicalType").getStringValue());
                        pstmtUpdate.setString(8, param.find("SecurityInterval").getStringValue());
                        pstmtUpdate.setString(9, param.find("DelayPeriod").getStringValue());
                        pstmtUpdate.setInt(10, param.find("idUser").getIntValue());
                        pstmtUpdate.setString(11, param.find("ID").getStringValue());
                        pstmtUpdate.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "El agroquimico ha sido actualizado exitosamente.";
                    } else {
                        resultArray.add("RESPONSE_CODE", "FAIL");
                        resultArray.add("RESPONSE_MESSAGE", "row ID was not provided");
                        resultArray.add("RESPONSE_DETAIL", "");
                        return resultArray;
                    }
                } else if (met.equals("Delete")) {
                    if (param.existValue("ID") && param.find("ID").getIntValue() > 0) {
                        //delete 
                        pstmtDelete.setInt(1, param.find("idUser").getIntValue());
                        pstmtDelete.setInt(2, param.find("ID").getIntValue());
                        pstmtDelete.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "El agroquimico ha sido borrado exitosamente.";
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
            resultArray.add("AgrochemicalInfo_Table", tab);

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("<AgrochemicalManagementTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            e.printStackTrace();
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("<AgrochemicalManagementTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.AgrochemicalManagementTransaction");
        nodeArr.add("Method", "Add");
        return nodeArr;
    }

    /**
     * The main method for the transaction. Creates a database connection and an
     * error Array, then executes the transaction and reports any errors
     *
     * @param argv argv[0] is an optional configuration file name
     * @return (none)
     * @exception (none)
     */
    public static void main(String[] argv) {
        try {
            AgrochemicalManagementTransaction transaction = new AgrochemicalManagementTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.AgrochemicalManagementTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" AgrochemicalManagementTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" AgrochemicalManagementTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.AgrochemicalManagementTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("AgrochemicalManagementTransaction - No results were returned.");
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
            System.out.println("AgrochemicalManagementTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
