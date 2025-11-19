package AgrocosaSecurity;

import SIDWebEngine.SIDDataBase;
import SIDWebEngine.SIDWebTransaction;
import java.sql.*;
import xmlNodeArray.*;

public class AuthenticateUser extends SIDWebTransaction {

    protected final static String WRONG_USERNAME_PASSWORD_ERROR = "Invalid user name or password";
    protected final static String PLEASE_VERIFY_USERNAME_AND_PASSWORD = "Please verify your user name or password";
    protected final static String USERNAME_FOUND = "USERNAME_FOUND";
    protected PreparedStatement pstmtRetrieveUser = null;
    protected PreparedStatement pstmtRetrieveUserArea = null;

    public AuthenticateUser() {
        super();
        this.SetTransactionType(SIDWebTransaction.RetrieveType);
        this.transactionSafelyNestable = true;
    }

    @Override
    public boolean Supports(xmlNodeArray nodeArray) {
        xmlNodeArray errArray = new xmlNodeArray();
        //Add tag names as comma separated Strings to the mandatoryTags array
        String[] mandatoryTags = {"USERNAME", "PASSWORD"};
        //Add tag names as comma separated Strings to the optionalTags array
        String[] optionalTags = {};
        //Add tag names as comma separated Strings to the mandatorySets array
        String[] mandatorySets = {};
        //Add tag names as comma separated Strings to the optionalSetTags array
        String[] optionalSets = {};
        for (int i = 0; i < mandatoryTags.length; i++) {
            if (!nodeArray.existValue(mandatoryTags[i])) {
                System.out.println("<AuthenticateUser::Supports> " + mandatoryTags[i] + " Mandatory tag not found or value is empty/null");
                errArray.add("ERROR", "MANDATORY_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalTags.length; i++) {
            if (nodeArray.exist(optionalTags[i]) && !nodeArray.existValue(optionalTags[i])) {
                System.out.println("<AuthenticateUser::Supports> " + optionalTags[i] + " Optional tag not found or value is empty/null");
                errArray.add("OPTIONAL_TAG_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < mandatorySets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) == null || nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0) {
                System.out.println("<AuthenticateUser::Supports> " + mandatorySets[i] + " Mandatory Set not found or value is empty/null");
                errArray.add("MANDATORY_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        for (int i = 0; i < optionalSets.length; i++) {
            if (nodeArray.find(mandatorySets[i]) != null && (nodeArray.find(mandatorySets[i]).getTable() == null || nodeArray.find(mandatorySets[i]).getTable().getRowsQty() == 0)) {
                System.out.println("<AuthenticateUser::Supports> " + optionalSets[i] + " Optional Set not found or value is empty/null");
                errArray.add("OPTIONAL_SET_NOT_FOUND_ERROR");
                errArray.add("ERROR_MSG", mandatoryTags[i]);
                this.SetError(errArray);
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized boolean PrepareStatements() {
        try {
            Connection con = this.GetSIDDataBase().GetConnection();

            pstmtRetrieveUser = con.prepareStatement(
                    "select u.idUser, u.FirstName, u.LastName, u.UserName, r.Role, r.idRole "
                    + "from user u inner join "
                    + "role r on r.idRole = u.idRole "
                    + "Where u.UserName = ?  "
                    + "and u.Password = ? "
                    + "and u.Active = 1");

            this.addPreparedStatement(pstmtRetrieveUser);

            return true;
        } catch (SQLException e) {
            System.out.println("<AuthenticateUser::PrepareStatements> SQLException: "
                    + e.getMessage());
            return false;
        }
    }

    @Override
    public xmlNodeArray Execute() throws SQLException {
        xmlNodeArray resultItemList = new xmlNodeArray();
        ResultSet rset = null;
        String username = "";
        String password = "";
        String rol = "";
        String firstName = "";
        String lastName = "";
        String idUser = "";
        String idRole = "";

        try {
            username = this.GetNodeArray().find("USERNAME").getStringValue();
            password = this.GetNodeArray().find("PASSWORD").getStringValue();

            pstmtRetrieveUser.setString(1, username);
            pstmtRetrieveUser.setString(2, password);
            rset = pstmtRetrieveUser.executeQuery();

            if (!rset.next()) {
                resultItemList.add("ERROR", "Usuario o contraseña invalida.");
                resultItemList.add("ERROR_MSG", "Por favor verifica tus datos.");
            } else {
                rol = rset.getString("Role");
                firstName = rset.getString("FirstName");
                lastName = rset.getString("LastName");
                idUser = rset.getString("idUser");
                idRole = rset.getString("idRole");

                resultItemList.add("RESULT", USERNAME_FOUND);
                resultItemList.add("IDUSER", idUser);
                resultItemList.add("USER", username);
                resultItemList.add("ROL", rol);
                resultItemList.add("IDROL", idRole);
                resultItemList.add("FIRSTNAME", firstName);
                resultItemList.add("LASTNAME", lastName);
            }

            return resultItemList;
        } catch (SQLException sqle) {
            resultItemList = new xmlNodeArray();
            resultItemList.add("ERROR", "SQL_TXN_EXCEPTION_ERROR");
            resultItemList.add("ERROR_MSG", "<" + getClass().getName() + "::Execute> SQLException: " + sqle.toString().trim());
            System.out.println("<" + getClass().getName() + "::Execute> SQLException: " + sqle.toString().trim());
            return resultItemList;
        } catch (Exception e) {
            resultItemList = new xmlNodeArray();
            resultItemList.add("ERROR", "TXN_EXCEPTION_ERROR");
            resultItemList.add("ERROR_MSG", "<" + getClass().getName() + "::Execute> TXNException: " + e.toString().trim());
            System.out.println("<" + getClass().getName() + "::Execute> TXNException: " + e.toString().trim());
            return resultItemList;
        } finally {
            if (rset != null) {
                rset.close();
                rset = null;
            }
            this.CloseStatements();
        }
    }

    @Override
    public xmlNodeArray GenerateTestParameters() {
        xmlNodeArray paramList = new xmlNodeArray();
        paramList.add("USERNAME", "sid");
        paramList.add("PASSWORD", "123");

        return paramList;
    }

    public static void main(String[] args) {
        try {
            AuthenticateUser transaction = new AuthenticateUser();
            SIDWebTransaction resultTransaction = null;
            xmlNodeArray inputParameterList = null;
            SIDDataBase database = null;
            System.out.println("Usage: java -classpath ...AgrocosaSecurity.AuthenticateUser");

            //Rebates MySQL DataBase
            database = new SIDDataBase("jdbc:mysql://localhost:3306/agrocosa", "root", "entrar123");
            transaction.SetSIDDataBase(database);
            inputParameterList = transaction.GenerateTestParameters();
            if (!transaction.IsValidTransaction()) {
                System.out.println("AuthenticateUser contains an invalid transaction type.");
            } else {
                if (!transaction.Supports(inputParameterList)) {
                    System.out.println("AuthenticateUser does not support this list of parameters.");
                } else {
                    resultTransaction = database.ExecuteTransaction("AgrocosaSecurity.AuthenticateUser", inputParameterList);
                    if (resultTransaction == null) {
                        System.out.println("The transaction's result list is null.");
                    } else {
                        if (resultTransaction.GetError() != null) {
                            resultTransaction.GetError().print();
                        } else {
                            if (resultTransaction.GetResultArray() == null) {
                                System.out.println("AuthenticateUser - No results were returned.");
                            } else {
                                xmlNodeArray list = resultTransaction.GetResultArray();
                                String str = xmlNodeArray.xmlNodeArray2String(list);
                                list = xmlNodeArray.string2xmlNodeArray(str);
                                System.out.println(str);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("AuthenticateUser::main> caught exception " + e.getMessage());
        }
    }
}
