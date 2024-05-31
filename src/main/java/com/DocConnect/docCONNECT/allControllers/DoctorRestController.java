/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DocConnect.docCONNECT.allControllers;

import ch.qos.logback.core.model.Model;
import com.DocConnect.docCONNECT.vmm.DBLoader;
import com.DocConnect.docCONNECT.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DoctorRestController {

    @PostMapping("/CheckLoginDoctor")
    public String checkLoginDoctor(@RequestParam String email, @RequestParam String password, HttpSession session) {
        try {

            //EXCEPTION ERROR MAKE executeSQLs
            ResultSet rs = DBLoader.executeSQL("select * from doctors where email='" + email + "' and password='" + password + "'");

            if (rs.next()) {
                session.setAttribute("email", email);

                String name = rs.getString("name"); // Fetching name from the database

                session.setAttribute("name", name);
                return "success";

            } else {
                return "fail";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }

    }

    @PostMapping("/getdoctordetails")
    public String getDoctorDetails(HttpSession session) {
        try {
            String email = (String) session.getAttribute("email");
            System.out.println(email);
            String ans = "";
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM doctors WHERE email='" + email + "'");
            if (rs.next()) {

                ans = ans + rs.getString("name") + ";";

                ans = ans + rs.getString("email") + ";";
                ans = ans + rs.getString("slot_amount") + ";";
                ans = ans + rs.getString("contact") + ";";
                ans = ans + rs.getString("description");

                System.out.println(ans);

                return ans;
            } else {
                return "fail";
            }

        } catch (Exception ex) {

            return ex.toString();
        }
    }

    @PostMapping("/editdoctor")
    public String addCities(@RequestParam String name, @RequestParam String email, @RequestParam String amount, @RequestParam String contact, @RequestParam String desc) {
        String ans = "";

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM doctors where email ='" + email + "' ");

            if (rs.next()) {

                rs.updateString("name", name);
                rs.updateString("slot_amount", amount);
                rs.updateString("contact", contact);
                rs.updateString("description", desc);

                rs.updateRow();

                return "success";
            } else {

                return "fail";
            }

        } catch (Exception ex) {
            return ex.toString();

        }
    }

    @PostMapping("/adddoctorphoto")
    public String addPhotos(@RequestParam MultipartFile file, HttpSession session) {
        String ans = "";

        String oname = file.getOriginalFilename();
        String email = (String) session.getAttribute("email");
        System.out.println(email);

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM doctors_photo ");

            if (rs.next()) {
                byte b[] = file.getBytes();
                String abspath = "src/main/resources/static/myuploads/";
                FileOutputStream fos = new FileOutputStream(abspath + oname);
                fos.write(b);

                rs.moveToInsertRow();
                //insert workbench coloum name
                // rs.updateInt("photo_id", 3);

                rs.updateString("email", email);

                rs.updateString("photo", oname);

                rs.insertRow();

                return "success";
            } else {

                return "fail";
            }

        } catch (Exception ex) {
            return ex.toString();

        }
    }

    @RequestMapping(value = "/getdoctorphotos", produces = "application/json", method = RequestMethod.GET)
    public String getDoctorPhotos(HttpSession session) {
        String email = (String) session.getAttribute("email");
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM doctors_photo where email ='" + email + "'");
        return ans;
    }

    @GetMapping("/deletedoctorphoto")
    public String deleteDoctorPhoto(@RequestParam int photo_id) {
        try {
            System.out.println(photo_id);
            //EXCEPTION ERROR MAKE executeSQLs
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM doctors_photo where photo_id ='" + photo_id + "' ");
            System.out.println(rs);
            if (rs.next()) {

                rs.deleteRow();
                System.out.println("deleted succusfully");
                return "success";
            } else {
                return "fail";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }

    }

    @RequestMapping(value = "/getdoctormanageslots", produces = "application/json", method = RequestMethod.GET)
    public String getDoctorManageSlots(HttpSession session) {
        String doctor_email = (String) session.getAttribute("email");
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM booking where vendor_email ='" + doctor_email + "'");
        return ans;
    }

    @GetMapping("/approvedoctocslot")
    public String aproveDoctor(@RequestParam String booking_id) {
        String ans = "";

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM booking where booking_id ='" + booking_id + "' ");

            if (rs.next()) {

                rs.updateString("status", "approve");

                rs.updateRow();

                return "success";
            } else {

                return "fail";
            }

        } catch (Exception ex) {
            return ex.toString();

        }
    }

    @GetMapping("/blockdoctorslot")
    public String blockDoctor(@RequestParam String booking_id) {
        String ans = "";

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM booking where booking_id ='" + booking_id + "' ");

            if (rs.next()) {

                rs.updateString("status", "pending");

                rs.updateRow();

                return "success";
            } else {

                return "fail";
            }

        } catch (Exception ex) {
            return ex.toString();

        }
    }
}
