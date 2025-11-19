package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveSectionCropTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtRetrieveAvailableCrop;
    protected PreparedStatement pstmtRetrieveSectionCrop;
    protected PreparedStatement pstmtRetrieveSectionInfo;
    protected PreparedStatement pstmtRetrieveAvailableCropType;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveSectionCropTransaction() {
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
        String[] mandatoryTags = {"idCampSection"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrieveSectionCropTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveSectionCropTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveSectionCropTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveSectionCropTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtRetrieveAvailableCrop = con.prepareStatement("select "
                    + "crop.CropName "
                    + "from crop "
                    + "where idCrop not in ( "
                    + "select idCrop "
                    + "from sectioncrop "
                    + "where idCampSection = ? "
                    + ") "
                    + "and crop.Active = 1 "
                    + "order by crop.CropName");
            
            pstmtRetrieveAvailableCropType = con.prepareStatement("select "
                    + "crop.CropName "
                    + "from crop inner join "
                    + "croptype on croptype.idCropType = crop.idCropType "
                    + "where idCrop not in ( "
                    + "select idCrop "
                    + "from sectioncrop "
                    + "where idCampSection = ? "
                    + ") "
                    + "and croptype.CropTypeName = ? "
                    + "and crop.Active = 1 "
                    + "order by crop.CropName");

            pstmtRetrieveSectionCrop = con.prepareStatement("select "
                    + "sectioncrop.idSectionCrop, "
                    + "croptype.CropTypeName, "
                    + "crop.CropName, "
                    + "sectioncrop.GrooveQty "
                    + "from sectioncrop inner join "
                    + "crop on crop.idCrop = sectioncrop.idCrop inner join "
                    + "croptype on croptype.idCropType = crop.idCropType "
                    + "where idCampSection = ? "
                    + "and sectioncrop.Active = 1 "
                    + "order by crop.CropName");

            pstmtRetrieveSectionInfo = con.prepareStatement("select "
                    + "SectionName, "
                    + "Area, "
                    + "landtype.LandTypeName, "
                    + "pipetype.PipeTypeName "
                    + "from campsection inner join "
                    + "landtype on landtype.idLandType = campsection.idLandType inner join "
                    + "pipetype on pipetype.idPipeType = campsection.idPipeType "
                    + "where idCampSection = ? ");

            this.addPreparedStatement(pstmtRetrieveAvailableCrop);
            this.addPreparedStatement(pstmtRetrieveAvailableCropType);
            this.addPreparedStatement(pstmtRetrieveSectionCrop);
            this.addPreparedStatement(pstmtRetrieveSectionInfo);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveSectionCropTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String idCampSection = null;
        xmlTable availableTable = null;

        try {

            resultArray = new xmlNodeArray();
            idCampSection = GetNodeArray().find("idCampSection").getStringValue();
            resultArray.add("idCampSection", idCampSection);

            if (this.GetNodeArray().existValue("idCamp")) {
                resultArray.add("idCamp", GetNodeArray().find("idCamp").getStringValue());
            }

            //Available 
            if (this.GetNodeArray().existValue("SelectedCropType")
                    && !GetNodeArray().find("SelectedCropType").getStringValue().equals("empty")) {
                pstmtRetrieveAvailableCropType.setString(1, idCampSection);
                pstmtRetrieveAvailableCropType.setString(2, GetNodeArray().find("SelectedCropType").getStringValue());
                rset = pstmtRetrieveAvailableCropType.executeQuery();
                availableTable = this.formatDataTable(rset);
                resultArray.add("SelectedCropType", GetNodeArray().find("SelectedCropType").getStringValue());
            } else {
                pstmtRetrieveAvailableCrop.setString(1, idCampSection);
                rset = pstmtRetrieveAvailableCrop.executeQuery();
                availableTable = this.formatDataTable(rset);
            }
            resultArray.add("CropAvailable_Table", availableTable);

            //available
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //Section Crop
            pstmtRetrieveSectionCrop.setString(1, idCampSection);
            rset = pstmtRetrieveSectionCrop.executeQuery();
            xmlTable sectionCropTable = this.formatDataTable(rset);
            resultArray.add("SectionCrop_Table", sectionCropTable);
            if (rset != null) {
                rset.close();
                rset = null;
            }

            //Retrieve section info
            pstmtRetrieveSectionInfo.setString(1, idCampSection);
            rset = pstmtRetrieveSectionInfo.executeQuery();
            if (rset.next()) {
                resultArray.add("SectionName", rset.getString("SectionName"));
                resultArray.add("Area", rset.getString("Area"));
                resultArray.add("LandTypeName", rset.getString("LandTypeName"));
                resultArray.add("PipeTypeName", rset.getString("PipeTypeName"));
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
            System.out.println("RetrieveSectionCropTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveSectionCropTransaction::Execute> Exception: " + ex.getMessage());
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "AgrocosaTransactions.RetrieveSectionCropTransaction");
        nodeArr.add("idCampSection", "2");
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
            RetrieveSectionCropTransaction transaction = new RetrieveSectionCropTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaTransactions.RetrieveSectionCropTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveSectionCropTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveSectionCropTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaTransactions.RetrieveSectionCropTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveSectionCropTransaction - No results were returned.");
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
            System.out.println("RetrieveSectionCropTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
