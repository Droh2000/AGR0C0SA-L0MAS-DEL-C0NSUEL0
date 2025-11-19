package AgrocosaTransactions;

import SIDWebEngine.*;
import java.sql.*;
import xmlNodeArray.*;

/**
 *
 * @author Juan
 */
public class RetrieveAjaxShippingCheckListInfoTransaction extends SIDWebTransaction {

    protected PreparedStatement pstmtSelect;

    /**
     * Default Constructor
     *
     * @exception (none)
     */
    public RetrieveAjaxShippingCheckListInfoTransaction() {
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
        String[] mandatoryTags = {"idShippingOrder"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};

        xmlNodeArray errArray = new xmlNodeArray();
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<RetrieveAjaxShippingCheckListInfoTransaction::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<RetrieveAjaxShippingCheckListInfoTransaction::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<RetrieveAjaxShippingCheckListInfoTransaction::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<RetrieveAjaxShippingCheckListInfoTransaction::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
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
            pstmtSelect = con.prepareStatement("SELECT "
                    + "coalesce(TruckBoxCleaning,'empty') as TruckBoxCleaning,  "
                    + "coalesce(MaintenanceConditions,'empty') as MaintenanceConditions, "
                    + "coalesce(OpenWindows,'empty') as OpenWindows, "
                    + "coalesce(SealedPallets,'empty') as SealedPallets, "
                    + "coalesce(PestFree,'empty') as PestFree, "
                    + "coalesce(SmellStrangeFree,'empty') as SmellStrangeFree, "
                    + "coalesce(Label,'empty') as Label "
                    + "from shippingOrder "
                    + "where idShippingOrder = ?");

            this.addPreparedStatement(pstmtSelect);

            return true;
        } catch (SQLException e) {
            System.out.println("RetrieveAjaxShippingCheckListInfoTransaction::PrepareStatements> SQLException: " + e.getMessage());
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
        String result = "";
        int idShippingOrder = 0;
        String truckBoxCleaning = "";
        String truckBoxCleaning1 = "";
        String truckBoxCleaning2 = "";
        String maintenanceConditions = "";
        String maintenanceConditions1 = "";
        String maintenanceConditions2 = "";
        String openWindows = "";
        String openWindows1 = "";
        String openWindows2 = "";
        String sealedPallets = "";
        String sealedPallets1 = "";
        String sealedPallets2 = "";
        String pestFree = "";
        String pestFree1 = "";
        String pestFree2 = "";
        String smellStrangeFree = "";
        String smellStrangeFree1 = "";
        String smellStrangeFree2 = "";
        String label = "";
        String label1 = "";
        String label2 = "";

        try {
            resultArray = new xmlNodeArray();

            idShippingOrder = GetNodeArray().find("idShippingOrder").getIntValue();

            pstmtSelect.setInt(1, idShippingOrder);
            rset = pstmtSelect.executeQuery();
            if (rset.next()) {
                truckBoxCleaning = rset.getString("TruckBoxCleaning");
                if(truckBoxCleaning.equals("Aceptable")){
                    truckBoxCleaning1 = "Checked";
                }else if(truckBoxCleaning.equals("No Aceptable")){
                    truckBoxCleaning2 = "Checked";
                }
                maintenanceConditions = rset.getString("MaintenanceConditions");
                if(maintenanceConditions.equals("Aceptable")){
                    maintenanceConditions1 = "Checked";
                }else if(maintenanceConditions.equals("No Aceptable")){
                    maintenanceConditions2 = "Checked";
                }
                openWindows = rset.getString("OpenWindows");
                if(openWindows.equals("Si")){
                    openWindows1 = "Checked";
                }else if(openWindows.equals("No")){
                    openWindows2 = "Checked";
                }
                sealedPallets = rset.getString("SealedPallets");
                if(sealedPallets.equals("Si")){
                    sealedPallets1 = "Checked"; 
                }else if (sealedPallets.equals("No")){
                    sealedPallets2 = "Checked"; 
                }
                pestFree = rset.getString("PestFree");
                if(pestFree.equals("Si")){
                    pestFree1 = "Checked";
                }else if(pestFree.equals("No")){
                    pestFree2 = "Checked";
                }
                smellStrangeFree = rset.getString("SmellStrangeFree");
                if(smellStrangeFree.equals("Si")){
                    smellStrangeFree1 = "Checked";
                }else if(smellStrangeFree.equals("No")){
                    smellStrangeFree2 = "Checked";
                }
                label = rset.getString("Label");
                if(label.equals("Aceptable")){
                    label1 = "Checked";
                }else if (label.equals("No Aceptable")){
                    label2 = "Checked";
                }

                result = "<table class='table table-striped'>"
                        + "    <tr>"
                        + "        <td>Limpieza de Caja</td>"
                        + "        <td><input type='radio' name='truckBoxCleaning' class='minimal-red' value='Aceptable' " + truckBoxCleaning1 + ">Aceptable</td>"
                        + "        <td><input type='radio' name='truckBoxCleaning' class='minimal-red' value='No Aceptable' " + truckBoxCleaning2 + ">No&nbsp;Aceptable</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Condiciones de Mantenimiento</td>"
                        + "        <td><input type='radio' name='maintenanceConditions' class='minimal-red' value='Aceptable' " + maintenanceConditions1 + ">Aceptable</td>"
                        + "        <td><input type='radio' name='maintenanceConditions' class='minimal-red' value='No Aceptable' " + maintenanceConditions2 + ">No&nbsp;Aceptable</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Ventilas Abiertas</td>"
                        + "        <td><input type='radio' name='openWindows' class='minimal-red' value='Si' " + openWindows1 + ">Si</td>"
                        + "        <td><input type='radio' name='openWindows' class='minimal-red' value='No' " + openWindows2 + ">No</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Tarimas Selladas</td>"
                        + "        <td><input type='radio' name='sealedPallets' class='minimal-red' value='Si' " + sealedPallets1 + ">Si</td>"
                        + "        <td><input type='radio' name='sealedPallets' class='minimal-red' value='No' " + sealedPallets2 + ">No</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Libre de Plagas</td>"
                        + "        <td><input type='radio' name='pestFree' class='minimal-red' value='Si' " + pestFree1 + ">Si</td>"
                        + "        <td><input type='radio' name='pestFree' class='minimal-red' value='No' " + pestFree2 + ">No</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Libre Aromas Extraños</td>"
                        + "        <td><input type='radio' name='smellStrangeFree' class='minimal-red' value='Si' " + smellStrangeFree1 + ">Si</td>"
                        + "        <td><input type='radio' name='smellStrangeFree' class='minimal-red' value='No' " + smellStrangeFree2 + ">No</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Etiqueta</td>"
                        + "        <td><input type='radio' name='label' class='minimal-red' value='Aceptable' " + label1 + ">Aceptable</td>"
                        + "        <td><input type='radio' name='label' class='minimal-red' value='No Aceptable' " + label2 + ">No&nbsp;Aceptable</td>"
                        + "    </tr>"
                        + "</table>"
                        + "<div class='form-group'>"
                        + "    <label>&nbsp;&nbsp;</label>"
                        + "    <span id='msgCheckList' class='pull-left' style='color: red;'>&nbsp;&nbsp;</span>"
                        + "</div>";
            } else {
                result = "<table class='table table-striped'>"
                        + "    <tr>"
                        + "        <td>Limpieza de Caja</td>"
                        + "        <td><input type='radio' name='truckBoxCleaning' class='minimal-red' value='Aceptable'>Aceptable</td>"
                        + "        <td><input type='radio' name='truckBoxCleaning' class='minimal-red' value='No Aceptable'>No&nbsp;Aceptable</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Condiciones de Mantenimiento</td>"
                        + "        <td><input type='radio' name='maintenanceConditions' class='minimal-red' value='Aceptable'>Aceptable</td>"
                        + "        <td><input type='radio' name='maintenanceConditions' class='minimal-red' value='No Aceptable'>No&nbsp;Aceptable</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Ventilas Abiertas</td>"
                        + "        <td><input type='radio' name='openWindows' class='minimal-red' value='Si'>Si</td>"
                        + "        <td><input type='radio' name='openWindows' class='minimal-red' value='No'>No</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Tarimas Selladas</td>"
                        + "        <td><input type='radio' name='sealedPallets' class='minimal-red' value='Si'>Si</td>"
                        + "        <td><input type='radio' name='sealedPallets' class='minimal-red' value='No'>No</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Libre de Plagas</td>"
                        + "        <td><input type='radio' name='pestFree' class='minimal-red' value='Si'>Si</td>"
                        + "        <td><input type='radio' name='pestFree' class='minimal-red' value='No'>No</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Libre Aromas Extraños</td>"
                        + "        <td><input type='radio' name='smellStrangeFree' class='minimal-red' value='Si'>Si</td>"
                        + "        <td><input type='radio' name='smellStrangeFree' class='minimal-red' value='No'>No</td>"
                        + "    </tr>"
                        + "    <tr>"
                        + "        <td>Etiqueta</td>"
                        + "        <td><input type='radio' name='label' class='minimal-red' value='Aceptable'>Aceptable</td>"
                        + "        <td><input type='radio' name='label' class='minimal-red' value='No Aceptable'>No&nbsp;Aceptable</td>"
                        + "    </tr>"
                        + "</table>"
                        + "<div class='form-group'>"
                        + "    <label>&nbsp;&nbsp;</label>"
                        + "    <span id='msgCheckList' class='pull-left' style='color: red;'>&nbsp;&nbsp;</span>"
                        + "</div>";
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
            System.out.println("RetrieveAjaxShippingCheckListInfoTransaction::Execute> SQLException: " + e.getMessage());
            resultArray = new xmlNodeArray();
            resultArray.add("RESPONSE_CODE", "FAIL");
            resultArray.add("RESPONSE_MESSAGE", "SQLException");
            resultArray.add("RESPONSE_DETAIL", e.getMessage());
            return resultArray;
        } catch (Exception ex) {
            System.out.println("RetrieveAjaxShippingCheckListInfoTransaction::Execute> Exception: " + ex.getMessage());
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

//            System.out.println("<RetrieveAjaxShippingCheckListInfoTransaction::Execute> exit");
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
        nodeArr.add("TRANSACTION_CLASS_TO_EXECUTE", "JonesPlasticTransactions.RetrieveAjaxShippingCheckListInfoTransaction");
        nodeArr.add("idShippingDetail", "7");
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
            RetrieveAjaxShippingCheckListInfoTransaction transaction = new RetrieveAjaxShippingCheckListInfoTransaction();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterArray = null;
            //CIMDataBase database = null;
            System.out.println("Usage: java -classpath ...JonesPlasticTransactions.RetrieveAjaxShippingCheckListInfoTransaction");

            //<Add Database connection parameter for testing>
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");

            transaction.SetSIDDataBase(database);
            inputParameterArray = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println(" RetrieveAjaxShippingCheckListInfoTransaction contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterArray)) {
                    System.out.println(" RetrieveAjaxShippingCheckListInfoTransaction does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("JonesPlasticTransactions.RetrieveAjaxShippingCheckListInfoTransaction", inputParameterArray);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result array is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("RetrieveAjaxShippingCheckListInfoTransaction - No results were returned.");
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
            System.out.println("RetrieveAjaxShippingCheckListInfoTransaction::main> caught exception " + e.getMessage());
            e.printStackTrace();
        }
    }
}
