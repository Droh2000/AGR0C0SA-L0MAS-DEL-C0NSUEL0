package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveTaskAgrochemicalReportTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected Statement pstmtRetrieveReport;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveTaskAgrochemicalReportTransaction() {
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
                System.out.println("<RetrieveTaskAgrochemicalReportTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveTaskAgrochemicalReportTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveTaskAgrochemicalReportTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveTaskAgrochemicalReportTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtRetrieveReport = con.createStatement();
            pstmtSelect = con.prepareStatement(
                    "SELECT idCampSection, "
                    + "SectionName  "
                    + "FROM campsection inner join "
                    + "camp on camp.idCamp = campsection.idCamp "
                    + "where camp.CampName = ? "
                    + "and campsection.Active = 1 "
                    + "order by campsection.SectionName");

            this.addPreparedStatement(pstmtSelect);
            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveTaskAgrochemicalReportTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String qry = "";
        String campName = "";
        String sectionName = "";
        String agrochemicalName = "";

        try {

            resultArray = new xmlNodeArray();

            qry = "select "
                    + "task.idTask, "
                    + "task.TaskType, "
                    + "camp.CampName, "
                    + "campsection.SectionName, "
                    + "date_format(task.TaskDate,'%d/%m/%Y') as TaskDate, "
                    + "task.Status, "
                    + "taskproduct.AgrochemicalName, "
                    + "taskproduct.Brand, "
                    + "taskproduct.ActiveIngredient, "
                    + "taskproduct.MinDose, "
                    + "taskproduct.MaxDose, "
                    + "taskproduct.UOM, "
                    + "taskproduct.AgrochemicalType, "
                    + "taskproduct.SecurityInterval, "
                    + "taskproduct.DelayPeriod, "
                    + "taskproduct.Lote, "
                    + "taskproduct.Caducity, "
                    + "taskproduct.Total, "
                    + "coalesce(taskproduct.RealUse,'') as RealUse, "
                    + "date_format(taskproduct.InsertDate,'%d/%m/%Y %h:%i') as InsertDate "
                    + "from task inner join  "
                    + "camp on camp.idCamp = task.idCamp inner join "
                    + "campsection on campsection.idCamp = camp.idCamp inner join "
                    + "taskproduct on taskproduct.idTask = task.idTask "
                    + "where task.idTask > 0 ";

            if (this.GetNodeArray().existValue("campName")) {
                campName = GetNodeArray().find("campName").getStringValue();
                resultArray.add("SelectedCampName", campName);
                qry += " and camp.CampName like '%" + campName + "%' ";
            }
            if (this.GetNodeArray().existValue("sectionName")) {
                sectionName = GetNodeArray().find("sectionName").getStringValue();
                resultArray.add("SelectedSectionName", sectionName);
                qry += " and campsection.SectionName like '%" + sectionName + "%' ";
            }
            if (this.GetNodeArray().existValue("agrochemicalName")) {
                agrochemicalName = GetNodeArray().find("agrochemicalName").getStringValue();
                resultArray.add("SelectedAgrochemicalName", agrochemicalName);
                qry += " and taskproduct.AgrochemicalName like '%" + agrochemicalName + "%' ";
            }

            qry += "Order by task.idTask, taskproduct.idTaskProduct";

            rset = pstmtRetrieveReport.executeQuery(qry);
            xmlTable reportTable = this.formatDataTable(rset);
            resultArray.add("AgrochemicalReport_Table", reportTable);

            if (pstmtRetrieveReport != null) {
                pstmtRetrieveReport.close();
                pstmtRetrieveReport = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (this.GetNodeArray().existValue("campName")) {
                campName = GetNodeArray().find("campName").getStringValue();
                pstmtSelect.setString(1, campName);
                rset = pstmtSelect.executeQuery();
                xmlTable t = new xmlTable();
                t.addField("Id");
                t.addField("SectionName");
                while (rset != null && rset.next()) {
                    t.addRow();
                    t.setValue(t.getRowsQty() - 1, "Id", rset.getString("idCampSection"));
                    t.setValue(t.getRowsQty() - 1, "SectionName", rset.getString("SectionName"));
                }
                resultArray.add("CampSection_HashMap", t);

                if (rset != null) {
                    rset.close();
                    rset = null;
                }
            }

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("RetrieveTaskAgrochemicalReportTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveTaskAgrochemicalReportTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveTaskAgrochemicalReportTransaction");
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
            RetrieveTaskAgrochemicalReportTransaction transaction = new RetrieveTaskAgrochemicalReportTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveTaskAgrochemicalReportTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveTaskAgrochemicalReportTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveTaskAgrochemicalReportTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveTaskAgrochemicalReportTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveTaskAgrochemicalReportTransaction - No results were returned.");
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
            System.out.println("RetrieveTaskAgrochemicalReportTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
