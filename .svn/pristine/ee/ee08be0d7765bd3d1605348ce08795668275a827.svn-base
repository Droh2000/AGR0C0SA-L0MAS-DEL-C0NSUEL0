package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class ToolManagementTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected PreparedStatement pstmtUpdate;
    protected PreparedStatement pstmtDelete;
    protected PreparedStatement pstmtInsert;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public ToolManagementTransaction() {
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
                System.out.println("<ToolManagementTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<ToolManagementTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<ToolManagementTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<ToolManagementTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
                    " select t.idTool, "
                    + " t.Number, "
                    + " t.ToolName, "
                    + " t.Description, "
                    + " t.Comments, "
                    + " concat(u.FirstName,' ',u.LastName) User, "
                    + " date_format(t.InsertDate,'%d/%m/%Y %h:%i %p') as InsertDate, "
                    + " date_format(t.ModifiedDate,'%d/%m/%Y %h:%i %p') as ModifiedDate "
                    + " from tool t inner join "
                    + " user u on u.idUser = t.idUser "
                    + " Where t.Active = 1 "
                    + " Order by t.ToolName");
            pstmtUpdate = con.prepareStatement("Update tool set "
                    + " Number = ?, "
                    + " ToolName = ?, "
                    + " Description = ?, "
                    + " Comments = ?, "
                    + " idUser = ?, "
                    + " ModifiedDate = Now() "
                    + " Where idTool = ? ");
            pstmtDelete = con.prepareStatement("Update tool set "
                    + "ModifiedDate = Now(), "
                    + "Active = 0, "
                    + "idUser = ? "
                    + "where idTool = ? ");
            pstmtInsert = con.prepareStatement("Insert into tool ( "
                    + " Number, "
                    + " ToolName, "
                    + " Description, "
                    + " Comments, "
                    + " idUser, "
                    + " InsertDate, "
                    + " ModifiedDate, "
                    + " Active "
                    + ")Values( "
                    + "?, " //1 Number
                    + "?, " //2 Tool Name
                    + "?, " //3 Description
                    + "?, " //4 Comments
                    + "?, " //5 idUser
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
            System.out.println("ToolManagementTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
                        pstmtInsert.setString(1, param.find("Number").getStringValue());
                        pstmtInsert.setString(2, param.find("ToolName").getStringValue());
                        pstmtInsert.setString(3, param.find("Description").getStringValue());
                        pstmtInsert.setString(4, param.find("Comments").getStringValue());
                        pstmtInsert.setInt(5, param.find("idUser").getIntValue());
                        pstmtInsert.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "La herramienta ha sido guardada existosamente.";
                    } else if (param.existValue("ID")) {
                        //update existing
                        pstmtUpdate.setString(1, param.find("Number").getStringValue());
                        pstmtUpdate.setString(2, param.find("ToolName").getStringValue());
                        pstmtUpdate.setString(3, param.find("Description").getStringValue());
                        pstmtUpdate.setString(4, param.find("Comments").getStringValue());
                        pstmtUpdate.setInt(5, param.find("idUser").getIntValue());
                        pstmtUpdate.setString(6, param.find("ID").getStringValue());
                        pstmtUpdate.executeUpdate();
                        resultArray.add("Action", "Done");
                        message = "La herramienta ha sido actualizada exitosamente.";
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
                        message = "La herramienta ha sido borrada exitosamente.";
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
            resultArray.add("ToolInfo_Table", tab);

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("<ToolManagementTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            e.printStackTrace();
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("<ToolManagementTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.ToolManagementTransaction");
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
            ToolManagementTransaction transaction = new ToolManagementTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.ToolManagementTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" ToolManagementTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" ToolManagementTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.ToolManagementTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("ToolManagementTransaction - No results were returned.");
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
            System.out.println("ToolManagementTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
