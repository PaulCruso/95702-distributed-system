/**
 * Author: Kaizhong Ying
 * Last Modified: Feb 3, 2024
 *
 * This Program demonstrates a simple hash computations
 * It requires user enter a string and select hash function
 * The program transfers string to hexadecimal text and base 64 notation
 * The program illustrates the transforming from string to hash value
 */

package ds.project1task1;

import java.io.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.xml.bind.DatatypeConverter;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ComputeHash", value = "/compute-hash")
public class ComputeHashes extends HttpServlet {
    /**
     * Process the request and generate the response to "index.jsp"
     * @param request The HttpServletRequest given by the user request
     * @param response The response generated from the request
     */

    public void doGet(HttpServletRequest request, HttpServletResponse response)  {
        response.setContentType("text/html");

        String input = request.getParameter("input");
        String type = request.getParameter("type");

        try {
            String hex = computeHex(input,type);
            String base64 = computeBase64(input,type);

            request.setAttribute("input",input);
            request.setAttribute("hash function",type);
            request.setAttribute("hex text",hex);
            request.setAttribute("base64",base64);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        try {
            dispatcher.forward(request,response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Computes hexadecimal text from a give String using specific cryptographic hash value
     *
     * @param input The string used to compute hexadecimal text
     * @param type A specific hash function (MD5 or SHA-256)
     * @return The hexadecimal text generated from the give String
     * @throws NoSuchAlgorithmException If the specific digest algorithm does not exist
     */
    public String computeHex(String input, String type) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(type);
        md.update(input.getBytes());
        byte [] output = md.digest();
        return DatatypeConverter.printHexBinary(output);
    }

    /**
     * Computes base 64 notation from a give String using specific cryptographic hash value
     *
     * @param input The string used to compute base 64 notation
     * @param type A specific cryptographic hash value (MD5 or SHA-256)
     * @return The base 64 notation generated from the give String
     * @throws NoSuchAlgorithmException If the specific digest algorithm does not exist
     */

    public String computeBase64(String input, String type) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(type);
        md.update(input.getBytes());
        byte [] output = md.digest();
        return DatatypeConverter.printBase64Binary(output);
    }
}