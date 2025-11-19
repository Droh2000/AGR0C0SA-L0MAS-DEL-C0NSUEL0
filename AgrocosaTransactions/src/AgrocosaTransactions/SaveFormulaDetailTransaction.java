/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveFormulaDetailTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtUpdate;
    protected PreparedStatement pstmtInsert;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveFormulaDetailTransaction() {
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
            "dataTable4Save",
            "idFormula"};

        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<SaveFormulaDetailTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveFormulaDetailTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveFormulaDetailTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveFormulaDetailTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtUpdate = con.prepareStatement("Update formuladetail set "
                    + "ModifiedDate = Now(), "
                    + "Active = 0, "
                    + "idUser = ? "
                    + "where idFormula = ? "
                    + "and active = 1");
            pstmtInsert = con.prepareStatement("Insert into formuladetail ( "
                    + "idFormula, "
                    + "ComponentName, "
                    + "Quantity, "
                    + "UOM, "
                    + "idUser, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active "
                    + ")Values( "
                    + "?, " //1 idFormula
                    + "?, " //2 Component Name
                    + "?, " //3 Quantity
                    + "?, " //4 UOM
                    + "?, " //5 idUser
                    + "Now(), " //  InsertDate
                    + "Now(), " //  ModifiedDate
                    + "1 " //  Active                    
                    + ")");

            this.addPreparedStatement(pstmtInsert);
            this.addPreparedStatement(pstmtUpdate);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveFormulaDetailTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int idUser = 0;
        String dataTable4Save = null;
        String idFormula = null;
        String component = "";
        int quantity = 0;
        String uom = "";

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            dataTable4Save = GetNodeArray().find("dataTable4Save").getStringValue();
            idFormula = GetNodeArray().find("idFormula").getStringValue();

            //replace
            dataTable4Save = dataTable4Save.replace("[[", "");
            dataTable4Save = dataTable4Save.replace("[{", "");
            
            dataTable4Save = dataTable4Save.replace("]]", "");
            dataTable4Save = dataTable4Save.replace("}]", "");
            
            dataTable4Save = dataTable4Save.replace("\"0\":", "");
            dataTable4Save = dataTable4Save.replace("\"1\":", "");
            dataTable4Save = dataTable4Save.replace("\"2\":", "");
            
            dataTable4Save = dataTable4Save.replace("\"", "");
            
            dataTable4Save = dataTable4Save.replace("],[", "|");
            dataTable4Save = dataTable4Save.replace("},[", "|");
            dataTable4Save = dataTable4Save.replace("},{", "|");

            dataTable4Save = dataTable4Save.replace(",x", "");

            String[] rowsData = dataTable4Save.split("\\|");

            pstmtUpdate.setInt(1, idUser);
            pstmtUpdate.setString(2, idFormula);
            pstmtUpdate.executeUpdate();

            for (String rowsData1 : rowsData) {
                String[] fieldData = rowsData1.split(",");
                component = fieldData[0]; //material
                quantity = Integer.parseInt(fieldData[1]); //qty
                uom = fieldData[2]; //uom
                //Insert detail
                pstmtInsert.setString(1, idFormula);
                pstmtInsert.setString(2, component);
                pstmtInsert.setInt(3, quantity);
                pstmtInsert.setString(4, uom);
                pstmtInsert.setInt(5, idUser);
                pstmtInsert.executeUpdate();
            }

            conn.commit();
            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", "La formula ha si registrada exitosamente.");
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveFormulaDetailTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveFormulaDetailTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            CloseStatements();
//            System.out.println("<SaveFormulaDetailTransaction::Execute> exit");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.SaveFormulaDetailTransaction");

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
            SaveFormulaDetailTransaction transaction = new SaveFormulaDetailTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.SaveFormulaDetailTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveFormulaDetailTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveFormulaDetailTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.SaveFormulaDetailTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveFormulaDetailTransaction - No results were returned.");
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
            System.out.println("SaveFormulaDetailTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
