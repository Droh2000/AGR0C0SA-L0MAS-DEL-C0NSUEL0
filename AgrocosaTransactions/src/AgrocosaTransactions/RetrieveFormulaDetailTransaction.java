package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveFormulaDetailTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtFormula;
    protected PreparedStatement pstmtFormulaDetail;
    protected PreparedStatement pstmtFormulaDetailId;
    protected PreparedStatement pstmtVegetalNutrition;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveFormulaDetailTransaction() {
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
        String[] mandatoryTags = {"idFormula"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrieveFormulaDetailTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveFormulaDetailTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveFormulaDetailTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveFormulaDetailTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtFormula = con.prepareStatement(
                    " select * from formula where idFormula = ? and Active = 1");

            pstmtFormulaDetail = con.prepareStatement("SELECT * "
                    + "FROM formuladetail "
                    + "where idFormula = ? "
                    + "and Active = 1 "
                    + "order by idFormulaDetail");

            pstmtFormulaDetailId = con.prepareStatement("SELECT idFormulaDetail "
                    + "FROM formuladetail "
                    + "where idFormula = ? "
                    + "and Active = 1 "
                    + "order by idFormulaDetail");

            pstmtVegetalNutrition = con.prepareStatement("SELECT "
                    + "VegetalNutrition, "
                    + "Portion "
                    + "FROM vegetalnutrition "
                    + "where idFormulaDetail = ? "
                    + "and active = 1 "
                    + "order by idVegetalNutrition");

            this.addPreparedStatement(pstmtFormula);
            this.addPreparedStatement(pstmtFormulaDetail);
            this.addPreparedStatement(pstmtFormulaDetailId);
            this.addPreparedStatement(pstmtVegetalNutrition);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveFormulaDetailTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        ResultSet rset2 = null;
        String message = "OK";
        int idFormula = 0;

        try {
            resultArray = new xmlNodeArray();

            idFormula = GetNodeArray().find("idFormula").getIntValue();

            pstmtFormula.setInt(1, idFormula);
            rset = pstmtFormula.executeQuery();
            if (rset.next()) {
                resultArray.add("idFormula", rset.getString("idFormula"));
                resultArray.add("FormulaName", rset.getString("FormulaName"));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            pstmtFormulaDetail.setInt(1, idFormula);
            rset = pstmtFormulaDetail.executeQuery();
            xmlTable formulaDetailTable = this.formatDataTable(rset);
            resultArray.add("FormulaDetail_Table", formulaDetailTable);
            if (rset != null) {
                rset.close();
                rset = null;
            }

            xmlTable t = new xmlTable();
            t.addField("idFormulaDetail");
            t.addField("VegetalNutrition");
            t.addField("Portion");

            pstmtFormulaDetailId.setInt(1, idFormula);
            rset = pstmtFormulaDetailId.executeQuery();
            while (rset.next()) {
                pstmtVegetalNutrition.setInt(1, rset.getInt("idFormulaDetail"));
                rset2 = pstmtVegetalNutrition.executeQuery();
                while (rset2.next()) {
                    t.addRow();
                    t.setValue(t.getRowsQty() - 1, "idFormulaDetail", rset.getInt("idFormulaDetail"));
                    t.setValue(t.getRowsQty() - 1, "VegetalNutrition", rset2.getString("VegetalNutrition"));
                    t.setValue(t.getRowsQty() - 1, "Portion", rset2.getString("Portion"));
                }
                if (rset2 != null) {
                    rset2.close();
                    rset2 = null;
                }
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            resultArray.add("VegetalNutritionDetail_Table", t);

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("<RetrieveFormulaDetailTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("<RetrieveFormulaDetailTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveFormulaDetailTransaction");
        nodeArr.add("idFormula", "1");
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
            RetrieveFormulaDetailTransaction transaction = new RetrieveFormulaDetailTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveFormulaDetailTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveFormulaDetailTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveFormulaDetailTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveFormulaDetailTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveFormulaDetailTransaction - No results were returned.");
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
            System.out.println("RetrieveFormulaDetailTransaction::main> caught exception " + e.getMessage());
        }
    }
}
