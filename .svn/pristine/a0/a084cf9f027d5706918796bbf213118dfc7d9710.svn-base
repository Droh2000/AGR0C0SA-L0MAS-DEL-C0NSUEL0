package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveCampDetailTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtCamp;
    protected PreparedStatement pstmtBombPrimary;
    protected PreparedStatement pstmtBombSecondary;
    protected PreparedStatement pstmtSupervisor;
    protected PreparedStatement pstmtSection;
    protected PreparedStatement pstmtSectionCrop;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveCampDetailTransaction() {
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
        String[] mandatoryTags = {"idCamp"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrieveCampDetailTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveCampDetailTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveCampDetailTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveCampDetailTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtCamp = con.prepareStatement(
                    " select * from camp where idCamp = ? ");

            pstmtBombPrimary = con.prepareStatement("select bomb.idBomb, bomb.BombName "
                    + "from bomb inner join  "
                    + "campinfo on campinfo.idBombPrimary = bomb.idBomb "
                    + "where idCamp = ?");

            pstmtBombSecondary = con.prepareStatement("select bomb.idBomb, bomb.BombName "
                    + "from bomb inner join  "
                    + "campinfo on campinfo.idBombSecondary = bomb.idBomb "
                    + "where idCamp = ?");

            pstmtSupervisor = con.prepareStatement("SELECT user.idUser as idSupervisor, "
                    + "concat(user.FirstName,' ',user.LastName) SupervisorName, "
                    + "role.Role as SupervisorRole "
                    + "FROM campinfo inner join "
                    + "user on user.idUser = campinfo.idSupervisor inner join "
                    + "role on role.idRole = user.idRole "
                    + "where idCamp = ?");

            pstmtSection = con.prepareStatement("SELECT campsection.idCampSection, "
                    + "campsection.SectionName, "
                    + "campsection.Area, "
                    + "campsection.GrooveQty, "
                    + "landtype.LandTypeName, "
                    + "pipetype.PipeTypeName "
                    + "FROM campsection inner join "
                    + "landtype on landtype.idLandType = campsection.idLandType inner join "
                    + "pipetype on pipetype.idPipeType = campsection.idPipeType "
                    + "where idCamp = ? "
                    + "order by campsection.SectionName");

            pstmtSectionCrop = con.prepareStatement("select GROUP_CONCAT(DISTINCT  crop.CropName SEPARATOR ', ') as SectionCropString,"
                    + "croptype.CropTypeName "
                    + "from sectioncrop inner join "
                    + "crop on crop.idCrop = sectioncrop.idCrop inner join "
                    + "croptype on croptype.idCropType = crop.idCropType "
                    + "where idCampSection = ? "
                    + "and  sectioncrop.Active = 1 "
                    + "GROUP BY 'all', croptype.CropTypeName");

            this.addPreparedStatement(pstmtCamp);
            this.addPreparedStatement(pstmtBombPrimary);
            this.addPreparedStatement(pstmtBombSecondary);
            this.addPreparedStatement(pstmtSupervisor);
            this.addPreparedStatement(pstmtSection);
            this.addPreparedStatement(pstmtSectionCrop);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveCampDetailTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        int idCamp = 0;

        try {
            resultArray = new xmlNodeArray();

            idCamp = GetNodeArray().find("idCamp").getIntValue();

            pstmtCamp.setInt(1, idCamp);
            rset = pstmtCamp.executeQuery();
            if (rset.next()) {
                resultArray.add("idCamp", rset.getString("idCamp"));
                resultArray.add("CampName", rset.getString("CampName"));
                resultArray.add("SectionQty", rset.getString("SectionQty"));
                resultArray.add("Latitude", rset.getString("Latitude"));
                resultArray.add("Longitude", rset.getString("Longitude"));
                resultArray.add("Weather", rset.getString("Weather"));
                resultArray.add("Precipitation", rset.getString("Precipitation"));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //bomb primary
            pstmtBombPrimary.setInt(1, idCamp);
            rset = pstmtBombPrimary.executeQuery();
            if (rset.next()) {
                resultArray.add("BombPrimary", rset.getString("BombName"));
                resultArray.add("SelectedBombPrimary", rset.getString("idBomb"));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //bomb secondary
            pstmtBombSecondary.setInt(1, idCamp);
            rset = pstmtBombSecondary.executeQuery();
            if (rset.next()) {
                resultArray.add("BomSecondary", rset.getString("BombName"));
                resultArray.add("SelectedBombSecondary", rset.getString("idBomb"));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //supervisor            
            pstmtSupervisor.setInt(1, idCamp);
            rset = pstmtSupervisor.executeQuery();
            if (rset.next()) {
                resultArray.add("idSupervisor", rset.getString("idSupervisor"));
                resultArray.add("SupervisorName", rset.getString("SupervisorName"));
                resultArray.add("SupervisorRole", rset.getString("SupervisorRole"));
                resultArray.add("SelectedSupervisor", rset.getString("SupervisorName"));
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }

            xmlTable sectionTable = new xmlTable();
            sectionTable.addField("idCampSection");
            sectionTable.addField("SectionName");
            sectionTable.addField("Area");
            sectionTable.addField("GrooveQty");
            sectionTable.addField("LandTypeName");
            sectionTable.addField("PipeTypeName");
            sectionTable.addField("SectionCrop");
            sectionTable.addField("SectionCropType");

            //Section
            pstmtSection.setInt(1, idCamp);
            rset = pstmtSection.executeQuery();
            while (rset.next()) {
                sectionTable.addRow();
                sectionTable.setValue(sectionTable.getRowsQty() - 1, "idCampSection", rset.getString("idCampSection"));
                sectionTable.setValue(sectionTable.getRowsQty() - 1, "SectionName", rset.getString("SectionName"));
                sectionTable.setValue(sectionTable.getRowsQty() - 1, "Area", rset.getString("Area"));
                sectionTable.setValue(sectionTable.getRowsQty() - 1, "GrooveQty", rset.getInt("GrooveQty"));
                sectionTable.setValue(sectionTable.getRowsQty() - 1, "LandTypeName", rset.getString("LandTypeName"));
                sectionTable.setValue(sectionTable.getRowsQty() - 1, "PipeTypeName", rset.getString("PipeTypeName"));

                pstmtSectionCrop.setString(1, rset.getString("idCampSection"));
                rset2 = pstmtSectionCrop.executeQuery();
                if (rset2.next()) {
                    sectionTable.setValue(sectionTable.getRowsQty() - 1, "SectionCrop", rset2.getString("SectionCropString"));
                    sectionTable.setValue(sectionTable.getRowsQty() - 1, "SectionCropType", rset2.getString("CropTypeName"));
                } else {
                    sectionTable.setValue(sectionTable.getRowsQty() - 1, "SectionCrop", "");
                    sectionTable.setValue(sectionTable.getRowsQty() - 1, "SectionCropType", "");
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
            resultArray.add("Section_Table", sectionTable);

            resultArray.add("RESPONSE_CODE", "PASS");
            resultArray.add("RESPONSE_MESSAGE", message);
            resultArray.add("RESPONSE_DETAIL", "");

            return resultArray;
        } catch (SQLException e) {
            System.out.println("<RetrieveCampDetailTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("<RetrieveCampDetailTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveCampDetailTransaction");
        nodeArr.add("idCamp", "1");
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
            RetrieveCampDetailTransaction transaction = new RetrieveCampDetailTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveCampDetailTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveCampDetailTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveCampDetailTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveCampDetailTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveCampDetailTransaction - No results were returned.");
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
            System.out.println("RetrieveCampDetailTransaction::main> caught exception " + e.getMessage());
        }
    }
}
