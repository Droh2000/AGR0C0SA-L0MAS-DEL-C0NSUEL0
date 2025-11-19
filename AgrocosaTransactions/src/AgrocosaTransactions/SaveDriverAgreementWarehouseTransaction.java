/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import java.util.ArrayList;
import xmlNodeArray.*;

public class SaveDriverAgreementWarehouseTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtInsertDriverAgreement;
    
    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public SaveDriverAgreementWarehouseTransaction() {
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
            "pu",
            "destinationDeliveryCity",
            "firstName",
            "lastName",
            "phone",
            "driverLicense",
            "sateIssuedDL",
            "dot",
            "truckingCo",
            "truck",
            "truckPlates",
            "trailer",
            "trailerPlates",
            "emptyWeightTare"
        };

        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<SaveDriverAgreementWarehouse::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<SaveDriverAgreementWarehouse::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<SaveDriverAgreementWarehouse::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<SaveDriverAgreementWarehouse::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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

            pstmtInsertDriverAgreement = con.prepareStatement("insert into driveragreementwarehouse ( "
                    + "FirstName, "
                    + "LastName, "
                    + "Pu, "
                    + "DestinationDeliveryCity, "
                    + "Phone, "
                    + "DriverLicense, "
                    + "SateIssuedDL, "
                    + "DOT, "
                    + "TruckingCo, "
                    + "Truck, "
                    + "TruckPlates, "
                    + "Trailer, "
                    + "TrailerPlates, "
                    + "TypeOfTrailer, "
                    + "EmptyWeightTare"
                    + ") values("
                    + "?, " // 1 FirstName
                    + "?, " // 2 LastName
                    + "?, " // 3 Pu
                    + "?, " // 4 DestinationDeliveryCity
                    + "?, " // 5 Phone
                    + "?, " // 6 DriverLicense
                    + "?, " // 7 SateIssuedDL
                    + "?, " // 8 DOT
                    + "?, " // 9 TruckingCo
                    + "?, " // 10 Truck
                    + "?, " // 11 TruckPlates
                    + "?, " // 12 Trailer
                    + "?, " // 13 TrailerPlates
                    + "?, " // 14 TypeOfTrailer
                    + "? "  // 15 EmptyWeightTare
                    + ")"//, Statement.RETURN_GENERATED_KEYS -> Por si acaso aunque esto en muchos casos da error
            );
                    
            this.addPreparedStatement(pstmtInsertDriverAgreement);
            
            return true;
        } catch (SQLException e) {
            System.out.println("SaveDriverAgreementWarehouse::PrepareStatements> SQLException: " + e.getMessage());
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
        ResultSet rset = null;
        int rowsAffected = 0;
        String pu = "";
        String destinationDeliveryCity = "";
        String firstName = "";
        String lastName = "";
        String phone = "";
        String driverLicense = "";
        String sateIssuedDL = "";
        String dot = "";
        String truckingCo = "";
        String truck = "";
        String truckPlates = "";
        String trailer = "";
        String trailerPlates = "";
        String emptyWeightTare = "";
        //int idStockTransferOrder = 0;
        //String result = "";
        
        try {
            conn = this.GetSIDDataBase().GetConnection();
            conn.setAutoCommit(false);
            resultArray = new xmlNodeArray();

            pu = GetNodeArray().find("pu").getStringValue();
            destinationDeliveryCity = GetNodeArray().find("destinationDeliveryCity").getStringValue();
            
            // Obtener los valores de los checkboxes multiples
            xmlNodeArray nodeArray = GetNodeArray();
            ArrayList<xmlNode> allNodes = nodeArray.getNodeArray();
            ArrayList<String> selected = new ArrayList<>();

            for (xmlNode node : allNodes) {
                String name = node.getName();
                if (name.startsWith("typeOfTrailer_")) {  // todos los checkboxes inician igual
                    String value = node.getStringValue();
                    if (value != null && !value.isEmpty()) {
                        selected.add(value);
                    }
                }
            }
            String typeOfTrailer = String.join(",", selected);
            
            firstName = GetNodeArray().find("firstName").getStringValue();
            lastName = GetNodeArray().find("lastName").getStringValue();
            phone = GetNodeArray().find("phone").getStringValue();
            driverLicense = GetNodeArray().find("driverLicense").getStringValue();
            sateIssuedDL = GetNodeArray().find("sateIssuedDL").getStringValue();
            dot = GetNodeArray().find("dot").getStringValue();
            truckingCo = GetNodeArray().find("truckingCo").getStringValue();
            truck = GetNodeArray().find("truck").getStringValue();
            truckPlates = GetNodeArray().find("truckPlates").getStringValue();
            trailer = GetNodeArray().find("trailer").getStringValue();
            trailerPlates = GetNodeArray().find("trailerPlates").getStringValue();
            emptyWeightTare = GetNodeArray().find("emptyWeightTare").getStringValue();

            //insert stocktransferorder
            pstmtInsertDriverAgreement.setString(1, firstName);
            pstmtInsertDriverAgreement.setString(2, lastName);
            pstmtInsertDriverAgreement.setString(3, pu);
            pstmtInsertDriverAgreement.setString(4, destinationDeliveryCity);
            pstmtInsertDriverAgreement.setString(5, phone);
            pstmtInsertDriverAgreement.setString(6, driverLicense);
            pstmtInsertDriverAgreement.setString(7, sateIssuedDL);
            pstmtInsertDriverAgreement.setString(8, dot);
            pstmtInsertDriverAgreement.setString(9, truckingCo);
            pstmtInsertDriverAgreement.setString(10, truck);
            pstmtInsertDriverAgreement.setString(11, truckPlates);
            pstmtInsertDriverAgreement.setString(12, trailer);
            pstmtInsertDriverAgreement.setString(13, trailerPlates);
            pstmtInsertDriverAgreement.setString(14, typeOfTrailer);
            pstmtInsertDriverAgreement.setString(15, emptyWeightTare);
            
            rowsAffected = pstmtInsertDriverAgreement.executeUpdate();
            /*if (rowsAffected > 0) {
                rset = pstmtInsertDriverAgreement.getGeneratedKeys();
                if(rset.next()){
                    idStockTransferOrder = rset.getInt(1);
                }
                if (rset != null) {
                    rset.close();
                    rset = null;
                }
            } else {
                result += "0";
            }*/

            if (rowsAffected > 0) {
                conn.commit();
                resultArray.add("RESPONSE_CODE", "PASS");
                resultArray.add("RESPONSE_MESSAGE", "Driver Agreement Successfully Registered.");
                resultArray.add("RESPONSE_DETAIL", "");
            } else {
                conn.rollback();
                resultArray.add("error", "Driver Agreement Could Not Be Registered.");
                resultArray.add("RESPONSE_CODE", "FAIL");
                resultArray.add("RESPONSE_MESSAGE", "Driver Agreement Could Not Be Registered.");
                resultArray.add("RESPONSE_DETAIL", "");
            }

            return resultArray;
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("SaveDriverAgreementWarehouse::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException:" + e.getMessage());
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            conn.rollback();
            System.out.println("SaveDriverAgreementWarehouse::Execute> Exception: " + ex.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "Exception:" + ex.getMessage());
            resultArray.add("RESPONSE_DETAIL", ex.getMessage());
            return resultArray;
        } finally {
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.SaveDriverAgreementWarehouse");

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
            SaveDriverAgreementWarehouseTransaction transaction = new SaveDriverAgreementWarehouseTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.SaveDriverAgreementWarehouse");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" SaveDriverAgreementWarehouse contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" SaveDriverAgreementWarehouse does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.SaveDriverAgreementWarehouse", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("SaveDriverAgreementWarehouse - No results were returned.");
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
            System.out.println("SaveDriverAgreementWarehouse::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
