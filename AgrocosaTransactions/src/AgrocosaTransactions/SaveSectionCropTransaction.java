/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

public class SaveSectionCropTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtUpdate;
    protected PreparedStatement pstmtInsert;
    protected PreparedStatement pstmtSelectIdCrop;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveSectionCropTransaction() {
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
            "selectedCrop",
            "idCampSection"};

        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<SaveSectionCropTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveSectionCropTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveSectionCropTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveSectionCropTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtUpdate = con.prepareStatement("Update sectioncrop set "
                    + "ModifiedDate = Now(), "
                    + "Active = 0, "
                    + "idUser = ? "
                    + "where idCampSection = ? "
                    + "and active = 1");
            pstmtInsert = con.prepareStatement("Insert into sectioncrop ( "
                    + "idCampSection, "
                    + "idCrop, "
                    + "GrooveQty, "
                    + "idUser, "
                    + "InsertDate, "
                    + "ModifiedDate, "
                    + "Active "
                    + ")Values( "
                    + "?, " //1 idCampSection
                    + "?, " //2 idCrop
                    + "?, " //3 groove qty
                    + "?, " //4 idUser
                    + "Now(), " //  InsertDate
                    + "Now(), " //  ModifiedDate
                    + "1 " //  Active                    
                    + ")");

            pstmtSelectIdCrop = con.prepareStatement("SELECT idCrop "
                    + "FROM crop inner join "
                    + "croptype on croptype.idCropType = crop.idCropType "
                    + "where crop.CropName = ? "
                    + "and croptype.CropTypeName = ? "
                    + "and crop.Active = 1 ");

            this.addPreparedStatement(pstmtInsert);
            this.addPreparedStatement(pstmtUpdate);
            this.addPreparedStatement(pstmtSelectIdCrop);

            return true;
        } catch (SQLException e) {
            System.out.println("SaveSectionCropTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        ResultSet rset = null;
        xmlNodeArray resultArray = null;
        int idUser = 0;
        String selectedCrop = null;
        String idCampSection = null;
        String crop = "";
        String cropType = "";
        int quantity = 0;
        int idCrop = 0;
        String result = "";

        try {

            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            idUser = GetNodeArray().find("idUser").getIntValue();
            selectedCrop = GetNodeArray().find("selectedCrop").getStringValue();
            idCampSection = GetNodeArray().find("idCampSection").getStringValue();

            //replace
            selectedCrop = selectedCrop.replace("[[", "");
            selectedCrop = selectedCrop.replace("[{", "");

            selectedCrop = selectedCrop.replace("]]", "");
            selectedCrop = selectedCrop.replace("}]", "");

            selectedCrop = selectedCrop.replace("\"0\":", "");
            selectedCrop = selectedCrop.replace("\"1\":", "");
            selectedCrop = selectedCrop.replace("\"2\":", "");

            selectedCrop = selectedCrop.replace("\"", "");

            selectedCrop = selectedCrop.replace("],[", "|");
            selectedCrop = selectedCrop.replace("},[", "|");
            selectedCrop = selectedCrop.replace("},{", "|");

            selectedCrop = selectedCrop.replace(",x", "");

            String[] rowsData = selectedCrop.split("\\|");

            pstmtUpdate.setInt(1, idUser);
            pstmtUpdate.setString(2, idCampSection);
            pstmtUpdate.executeUpdate();

            for (String rowsData1 : rowsData) {
                String[] fieldData = rowsData1.split(",");
                cropType = fieldData[0]; //croptype : Cultivo
                crop = fieldData[1]; //crop : Variedad
                quantity = Integer.parseInt(fieldData[2]); //qty

                pstmtSelectIdCrop.setString(1, crop);
                pstmtSelectIdCrop.setString(2, cropType);
                rset = pstmtSelectIdCrop.executeQuery();

                if (rset.next()) {
                    idCrop = rset.getInt("idCrop");
                    //Insert detail
                    pstmtInsert.setString(1, idCampSection);
                    pstmtInsert.setInt(2, idCrop);
                    pstmtInsert.setInt(3, quantity);
                    pstmtInsert.setInt(4, idUser);
                    pstmtInsert.executeUpdate();
                    result += "1";
                } else {
                    result += "0";
                }
            }
            if (result.contains("0")) {
                conn.rollback();

                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "No se pudo registrar la informacion");
                resultArray.add("RESPONSE_DETAIL", "");
            } else {
                conn.commit();
                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", "El cultivo ha sido agregado a la seccion.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveSectionCropTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveSectionCropTransaction::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
            CloseStatements();
//            System.out.println("<SaveSectionCropTransaction::Execute> exit");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveSectionCropTransaction");

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
            SaveSectionCropTransaction transaction = new SaveSectionCropTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveSectionCropTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveSectionCropTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveSectionCropTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveSectionCropTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveSectionCropTransaction - No results were returned.");
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
            System.out.println("SaveSectionCropTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
