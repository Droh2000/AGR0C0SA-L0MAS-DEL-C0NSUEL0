/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Agrocosa;

import SIDWebEngine.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import xmlNodeArray.xmlNodeArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class SaveUserPictureServlet extends SIDServlet {

    private BufferedInputStream bis = null;
    private BufferedImage bufimg = null;
    private BufferedImage resized = null;
    private ByteArrayOutputStream bytestream = null;
    private BufferedOutputStream bos = null;
    private byte[] newimage = null;
    private FileOutputStream fos = null;

    // Servlet configuration variables
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @param nodeArray
     */
    @Override
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response,
            xmlNodeArray nodeArray) {
        RequestDispatcher rd = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        FileInputStream fis = null;
        HttpSession session = null;
        double scale = 0;
        String fileExtension = "";
        String fileNameToLowerCase = "";
        int y = 0;
        boolean boolContinue = false;

        try {
            session = request.getSession();
            String idUser = session.getAttribute("idUser").toString();

            Part filePart = request.getPart("fileName");
            String fileName = getFileName(filePart);
            fileNameToLowerCase = fileName.toLowerCase();
            fileExtension = fileNameToLowerCase.substring(fileNameToLowerCase.indexOf(".") + 1, fileNameToLowerCase.length());

            if (fileExtension.equals("png")
                    || fileExtension.equals("jpg")
                    || fileExtension.equals("jpeg")
                    || fileExtension.equals("gif")
                    || fileExtension.equals("bmp")) {
                boolContinue = true;
            } else {
                request.setAttribute("error", "Archivo invalido, Por favor seleccione una imagen jpg");
            }

            if (fileName != null && boolContinue) {

                OutputStream out = null;
                InputStream filecontent = null;
                String root = getServletContext().getRealPath("/");
                File path = new File(root + "/uploads");

                try {

                    if (!path.exists()) {
                        path.mkdirs();
                    }

                    out = new FileOutputStream(new File(path + File.separator
                            + fileName));
                    filecontent = filePart.getInputStream();

                    int read = 0;
                    final byte[] bytes = new byte[1024];

                    while ((read = filecontent.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                } catch (FileNotFoundException fne) {
                    request.setAttribute("error", "You either did not specify a file to upload or are "
                            + "trying to upload a file to a protected or nonexistent "
                            + "location. "
                            + "<br/> ERROR:" + fne.getMessage());

                } finally {
                    if (out != null) {
                        out.close();
                    }
                    if (filecontent != null) {
                        filecontent.close();
                    }
                }

                //------------------------------------------
                //  R E S I Z E  &   U P L O A D   F I L E   thumbs
                //------------------------------------------
                bis = new BufferedInputStream(new FileInputStream(path + File.separator
                        + fileName));
                bufimg = ImageIO.read(bis);
                int img_width = bufimg.getWidth();
                int img_height = bufimg.getHeight();
                if (img_width > img_height) {
                    scale = (double) 128 / (double) img_width;
                } else {
                    scale = (double) 128 / (double) img_height;
                }
                Image sized = null;
                int w = (int) (bufimg.getWidth() * scale);
                int h = (int) (bufimg.getHeight() * scale);
                sized = bufimg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                w = sized.getWidth(null);
                h = sized.getHeight(null);
                resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resized.createGraphics();
                g.drawImage(sized, 0, 0, null);
                g.dispose();
                bytestream = new ByteArrayOutputStream();
                bos = new BufferedOutputStream(bytestream);
                if (fileExtension.equals("png")) {
                    ImageIO.write(resized, "png", bos);
                } else if (fileExtension.equals("jpg")) {
                    ImageIO.write(resized, "jpg", bos);
                } else if (fileExtension.equals("jpeg")) {
                    ImageIO.write(resized, "jpeg", bos);
                } else if (fileExtension.equals("gif")) {
                    ImageIO.write(resized, "gif", bos);
                } else if (fileExtension.equals("bmp")) {
                    ImageIO.write(resized, "bmp", bos);
                }
                bytestream.flush();
                newimage = bytestream.toByteArray();

                String newfilethumb = idUser + "_" + y + "." + fileExtension;

                fos = new FileOutputStream(path + "/" + newfilethumb);
                fos.write(newimage);
                fos.flush();
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
                if (bufimg != null) {
                    bufimg.flush();
                    bufimg = null;
                }
                if (resized != null) {
                    resized.flush();
                    resized = null;
                }
                if (bytestream != null) {
                    bytestream.close();
                    bytestream = null;
                }
                if (bos != null) {
                    bos.close();
                    bos = null;
                }
                if (fos != null) {
                    fos.close();
                    fos = null;
                }

                //------------------------------------------
                //  Update Picture on DB
                //------------------------------------------
                SIDDataBase database = new SIDDataBase();
                con = database.GetConnection();
                //update
                pstmt = con.prepareStatement("Update user set Picture = ? where idUser = ? ");
                fis = new FileInputStream(path + "/" + newfilethumb);
                pstmt.setBinaryStream(1, fis, fis.available());
                pstmt.setString(2, idUser);
                int i = pstmt.executeUpdate();
                if (i != 0) {
                    request.setAttribute("RESPONSE_CODE", "PASS");
                    request.setAttribute("RESPONSE_MESSAGE", "Su foto ha sido actualizada.");
                    request.setAttribute("RESPONSE_DETAIL", "");
                } else {
                    request.setAttribute("error", "La foto no pudo ser guardada.");
                    request.setAttribute("RESPONSE_CODE", "FAIL");
                    request.setAttribute("RESPONSE_MESSAGE", "Foto no guardada");
                    request.setAttribute("RESPONSE_DETAIL", fileName);
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }

            }

            rd = request.getRequestDispatcher("/profile.page");
            rd.forward(request, response);

        } catch (SQLException sqle) {
            request.setAttribute("error", "SQL_EXCEPTION_ERROR" + sqle.getMessage());
        } catch (IOException ioe) {
            request.setAttribute("error", "IO_EXCEPTION_ERROR" + ioe.getMessage());
        } catch (ServletException se) {
            request.setAttribute("error", "SERVLET_EXCEPTION_ERROR" + se.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
                if (con != null && !con.isClosed()) {
                    con.close();
                    con = null;
                }
            } catch (SQLException sqle) {
                request.setAttribute("error", "SQL_EXCEPTION_ERROR" + sqle.getMessage());
            }
        }
    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }

}
