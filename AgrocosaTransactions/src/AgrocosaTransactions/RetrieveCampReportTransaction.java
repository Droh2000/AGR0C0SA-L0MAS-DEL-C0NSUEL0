package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveCampReportTransaction extends SIDWebTransaction {

    protected Statement pstmtRetrieveCampReport;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveCampReportTransaction() {
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
                System.out.println("<RetrieveCampReportTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveCampReportTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveCampReportTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveCampReportTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtRetrieveCampReport = con.createStatement();
            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveCampReportTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String supervisorName = "";
        String bombPrimary = "";
        String bombSecondary = "";
        String landTypeName = "";
        String pipeTypeName = "";
        String cropTypeName = "";
        String cropName = "";

        try {

            resultArray = new xmlNodeArray();

            qry = "select camp.CampName, "
                    + "camp.SectionQty, "
                    + "camp.Latitude, "
                    + "camp.Longitude, "
                    + "camp.Weather, "
                    + "camp.Precipitation, "
                    + "concat(user1.FirstName,' ',user1.LastName) Supervisor, "
                    + "bomb1.BombName as 'PrimaryBomb', "
                    + "bomb2.BombName as 'SecondaryBomb', "
                    + "campsection.SectionName, "
                    + "campsection.Area, "
                    + "landtype.LandTypeName, "
                    + "pipetype.PipeTypeName, "
                    + "croptype.CropTypeName, "
                    + "crop.CropName "
                    + "from campinfo inner join "
                    + "camp on camp.idCamp = campinfo.idCamp inner join "
                    + "user user1 on user1.idUser = campinfo.idSupervisor inner join "
                    + "bomb bomb1 on bomb1.idBomb = campinfo.idBombPrimary inner join "
                    + "bomb bomb2 on bomb2.idBomb = campinfo.idBombSecondary inner join "
                    + "user user2 on user2.idUser = campinfo.idUser inner join "
                    + "campsection on campsection.idCamp = campinfo.idCamp inner join "
                    + "landtype on landtype.idLandType = campsection.idLandType inner join "
                    + "pipetype on pipetype.idPipeType = campsection.idPipeType inner join "
                    + "sectioncrop on sectioncrop.idCampSection = campsection.idCampSection inner join "
                    + "crop on crop.idCrop = sectioncrop.idCrop inner join "
                    + "croptype on croptype.idCropType = crop.idCropType "
                    + "where camp.Active = 1 "
                    + "and campinfo.Active = 1 "
                    + "and campsection.Active = 1 ";

            if (this.GetNodeArray().existValue("campName")) {
                campName = GetNodeArray().find("campName").getStringValue();
                resultArray.add("SelectedCampName", campName);
                qry += " and camp.CampName like '%" + campName + "%' ";
            }
            if (this.GetNodeArray().existValue("sectionName")) {
                sectionName = GetNodeArray().find("sectionName").getStringValue();
                resultArray.add("SelectedCampSection", sectionName);
                qry += " and SectionName like '%" + sectionName + "%' ";
            }
            if (this.GetNodeArray().existValue("supervisorName")) {
                supervisorName = GetNodeArray().find("supervisorName").getStringValue();
                resultArray.add("SelectedSupervisor", supervisorName);
                qry += " and concat(user1.FirstName,' ',user1.LastName) like '%" + supervisorName + "%' ";
            }
            if (this.GetNodeArray().existValue("bombPrimary")) {
                bombPrimary = GetNodeArray().find("bombPrimary").getStringValue();
                resultArray.add("SelectedBombPrimary", bombPrimary);
                qry += " and bomb1.BombName like '%" + bombPrimary + "%' ";
            }
            if (this.GetNodeArray().existValue("bombSecondary")) {
                bombSecondary = GetNodeArray().find("bombSecondary").getStringValue();
                resultArray.add("SelectedBombSecondary", bombSecondary);
                qry += " and bomb2.BombName like '%" + bombSecondary + "%' ";
            }
            if (this.GetNodeArray().existValue("landTypeName")) {
                landTypeName = GetNodeArray().find("landTypeName").getStringValue();
                resultArray.add("SelectedLandTypeName", landTypeName);
                qry += " and landtype.LandTypeName like '%" + landTypeName + "%' ";
            }
            if (this.GetNodeArray().existValue("pipeTypeName")) {
                pipeTypeName = GetNodeArray().find("pipeTypeName").getStringValue();
                resultArray.add("SelectedPipeTypeName", pipeTypeName);
                qry += " and pipetype.PipeTypeName like '%" + pipeTypeName + "%' ";
            }
            if (this.GetNodeArray().existValue("cropTypeName")) {
                cropTypeName = GetNodeArray().find("cropTypeName").getStringValue();
                resultArray.add("SelectedCropTypeName", cropTypeName);
                qry += " and croptype.CropTypeName like '%" + cropTypeName + "%' ";
            }
            if (this.GetNodeArray().existValue("cropName")) {
                cropName = GetNodeArray().find("cropName").getStringValue();
                resultArray.add("SelectedCropTypeName", cropName);
                qry += " and crop.CropName like '%" + cropName + "%' ";
            }

            qry += "Order by CampName, SectionName";

            rset = pstmtRetrieveCampReport.executeQuery(qry);
            xmlTable reportTable = this.formatDataTable(rset);
            resultArray.add("CampReport_Table", reportTable);

            if (pstmtRetrieveCampReport != null) {
                pstmtRetrieveCampReport.close();
                pstmtRetrieveCampReport = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("RetrieveCampReportTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveCampReportTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveCampReportTransaction");
        nodeArr.add("SelectedCamp", "Santa Rosa");
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
            RetrieveCampReportTransaction transaction = new RetrieveCampReportTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveCampReportTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveCampReportTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveCampReportTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveCampReportTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveCampReportTransaction - No results were returned.");
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
            System.out.println("RetrieveCampReportTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
