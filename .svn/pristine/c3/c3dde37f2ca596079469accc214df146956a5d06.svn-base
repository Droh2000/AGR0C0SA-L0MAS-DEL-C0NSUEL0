package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveTaskJournalReportTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;
    protected Statement pstmtRetrieveReport;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveTaskJournalReportTransaction() {
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
                System.out.println("<RetrieveTaskJournalReportTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveTaskJournalReportTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveTaskJournalReportTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveTaskJournalReportTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            System.out.println("RetrieveTaskJournalReportTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String laborTypeName = "";

        try {

            resultArray = new xmlNodeArray();

            qry = "select task.idTask, "
                    + "task.TaskType, "
                    + "taskhistory.CampName, "
                    + "taskhistory.SectionName, "
                    + "IFNULL(taskhistory.LaborTypeName,'') as LaborTypeName, "
                    + "IFNULL(taskhistory.SectionCrop,'') as SectionCrop, "
                    + "date_format(task.TaskDate,'%d/%m/%Y') as TaskDate, "
                    + "IFNULL(task.Pressure,'') as Pressure, "
                    + "IFNULL(task.Formula,'') as Formula, "
                    + "task.Comments, "
                    + "task.Status, "
                    + "date_format(task.InsertDate,'%d/%m/%Y') as InsertDate, "
                    + "IFNULL(task.Manager,'') as Manager, "
                    + "task.WorkersQty, "
                    + "IFNULL(task.Temperature,'') as Temperature, "
                    + "taskhistory.User, "
                    + "taskhistory.SupervisorName, "
                    + "b1.BombName as BombPrimary, "
                    + "b2.BombName as BombSecondary, "
                    + "convert(CONCAT( "
                    + "FLOOR(HOUR(TIMEDIFF(now(), task.InsertDate)) / 24), ' dias,  ', "
                    + "MOD(HOUR(TIMEDIFF(now(), task.InsertDate)), 24), ' hrs,  ', "
                    + "MINUTE(TIMEDIFF(now(), task.InsertDate)), ' min') USING utf8) as ElapsedTime "
                    + "from task inner join "
                    + "taskhistory on taskhistory.idTask = task.idTask inner join "
                    + "campinfo on campinfo.idCamp = campinfo.idCamp  and task.idCamp = campinfo.idCamp inner join "
                    + "bomb b1 on b1.idBomb = campinfo.idBombPrimary inner join "
                    + "bomb b2 on b2.idBomb = campinfo.idBombSecondary "
                    + "where taskhistory.Status = 'Nueva' "
                    + "and task.Active = 1 "
                    + "and task.TaskType = 'Jornal' ";

            if (this.GetNodeArray().existValue("campName")) {
                campName = GetNodeArray().find("campName").getStringValue();
                resultArray.add("SelectedCampName", campName);
                qry += " and taskhistory.CampName like '%" + campName + "%' ";
            }
            if (this.GetNodeArray().existValue("sectionName")) {
                sectionName = GetNodeArray().find("sectionName").getStringValue();
                resultArray.add("SelectedSectionName", sectionName);
                qry += " and taskhistory.SectionName like '%" + sectionName + "%' ";
            }
            if (this.GetNodeArray().existValue("laborTypeName")) {
                laborTypeName = GetNodeArray().find("laborTypeName").getStringValue();
                resultArray.add("SelectedLaborTypeName", laborTypeName);
                qry += " and taskhistory.LaborTypeName like '%" + laborTypeName + "%' ";
            }

            qry += "Order by idTask desc";

            rset = pstmtRetrieveReport.executeQuery(qry);
            xmlTable reportTable = this.formatDataTable(rset);
            resultArray.add("TaskReport_Table", reportTable);

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
            System.out.println("RetrieveTaskJournalReportTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveTaskJournalReportTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveTaskJournalReportTransaction");
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
            RetrieveTaskJournalReportTransaction transaction = new RetrieveTaskJournalReportTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveTaskJournalReportTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveTaskJournalReportTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveTaskJournalReportTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveTaskJournalReportTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveTaskJournalReportTransaction - No results were returned.");
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
            System.out.println("RetrieveTaskJournalReportTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
