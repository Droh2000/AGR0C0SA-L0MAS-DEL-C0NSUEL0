package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveAjaxTaskFormulaAndProductTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelectTaskFormula;
    protected PreparedStatement pstmtSelectTaskProduct;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveAjaxTaskFormulaAndProductTransaction() {
        super();
        SetTransactionType(SIDWebTransaction.RetrieveType);
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
        String[] mandatoryTags = {"idTask"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrievePestReportTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrievePestReportTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrievePestReportTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrievePestReportTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtSelectTaskFormula = con.prepareStatement("SELECT "
                    + "idTaskLabor, "
                    + "LaborTypeName "
                    + "FROM tasklabor "
                    + "where idTask = ? "
                    + "and Active = 1 "
                    + "Order by idTaskLabor");

            pstmtSelectTaskProduct = con.prepareStatement("SELECT "
                    + "idTaskProduct, "
                    + "AgrochemicalName, "
                    + "Brand, "
                    + "ActiveIngredient, "
                    + "MinDose, "
                    + "MaxDose, "
                    + "UOM, "
                    + "AgrochemicalType, "
                    + "SecurityInterval, "
                    + "DelayPeriod, "
                    + "Lote, "
                    + "Caducity "
                    + "FROM taskproduct "
                    + "where idTask = ? "
                    + "Order by idTaskProduct");

            this.addPreparedStatement(pstmtSelectTaskFormula);
            this.addPreparedStatement(pstmtSelectTaskProduct);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrievePestReportTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        xmlNodeArray resultArray = null;
        ResultSet rset = null;
        String message = "OK";
        String idTask = "";
        String result = "";

        try {

            resultArray = new xmlNodeArray();

            idTask = GetNodeArray().find("idTask").getStringValue();
            resultArray.add("SelectedIdTask", idTask);

            //Title modal-body
            result += "<p><h3>Por favor antes de cerrar la tarea hay que completar la siguiente información:</h3></p>";

            //Task Formula
            result += "<p><h3>Formulas:</h3></p>";
            pstmtSelectTaskFormula.setString(1, idTask);
            rset = pstmtSelectTaskFormula.executeQuery();
            while (rset.next()) {
                result += " <div class=\"form-group\"> "
                        + " <label class=\"control-label col-sm-3\">" + rset.getString("LaborTypeName") + ": </label> "
                        + " <div class=\"col-sm-3\"> "
                        + " <input name=\"f" + rset.getString("idTaskLabor") + "\" type=\"text\" class=\"form-control pull-right\" required value=\"\" /> "
                        + " </div> "
                        + " </div>";
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //Task Product
            result += "<p><h3>Productos:</h3></p>";
            pstmtSelectTaskProduct.setString(1, idTask);
            rset = pstmtSelectTaskProduct.executeQuery();
            while (rset.next()) {
                result += " <div class=\"form-group\"> "
                        + " <label class=\"control-label col-sm-3\">" + rset.getString("AgrochemicalName") + ": </label> "
                        + " <div class=\"col-sm-3\"> "
                        + " <input name=\"p" + rset.getString("idTaskProduct") + "\" type=\"text\" class=\"form-control pull-right\" required value=\"\" /> "
                        + " </div> "
                        + " </div>";
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            resultArray.add("Result", result);

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("<RetrievePestReportTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("<RetrievePestReportTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrievePestReportTransaction");
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
            RetrieveAjaxTaskFormulaAndProductTransaction transaction = new RetrieveAjaxTaskFormulaAndProductTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrievePestReportTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrievePestReportTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrievePestReportTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrievePestReportTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrievePestReportTransaction - No results were returned.");
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
            System.out.println("RetrievePestReportTransaction::main> caught exception " + e.getMessage());
        }
    }
}
