/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DocConnect.docCONNECT.allControllers;

import ch.qos.logback.core.model.*;
import com.DocConnect.docCONNECT.vmm.DBLoader;
import com.DocConnect.docCONNECT.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AdminRestController {

    @PostMapping("/CheckLogin")
    public String checkLogin(@RequestParam String un, @RequestParam int ps, HttpSession session, Model model) {
        try {
            System.out.println(un);
            System.out.println(ps);
            //EXCEPTION ERROR MAKE executeSQLs
            ResultSet rs = DBLoader.executeSQL("select * from adminlogin where username='" + un + "' and password='" + ps + "'");

            if (rs.next()) {
                session.setAttribute("username", un);

                return "success";
            } else {
                return "fail";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }

    }

    @PostMapping("/addcities")
    public String addCities(@RequestParam String cities, @RequestParam String desc, @RequestParam MultipartFile file, HttpSession session) {
        String ans = "";

        String oname = file.getOriginalFilename();

        System.out.println(cities);
        System.out.println(desc);
        System.out.println(file);

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM cities where cityname ='" + cities + "' ");

            if (rs.next()) {
                return "fail";
            } else {

                byte b[] = file.getBytes();
                String abspath = "src/main/resources/static/myuploads/";
                FileOutputStream fos = new FileOutputStream(abspath + oname);
                fos.write(b);

                rs.moveToInsertRow();
                //insert workbench coloum name
                rs.updateString("cityname", cities);
                rs.updateString("desc", desc);
                rs.updateString("photo", oname);

                rs.insertRow();

                return "success";
            }

        } catch (Exception ex) {
            return ex.toString();

        }
    }

    @RequestMapping(value = "/getcities", produces = "application/json", method = RequestMethod.GET)
    public String getCities() {
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM cities");
        return ans;
    }

    @PostMapping("/deletecity")
    public String deleteCity(@RequestParam String cities, HttpSession session) {
        try {

            System.out.println(cities);

            //EXCEPTION ERROR MAKE executeSQLs
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM cities where cityname ='" + cities + "' ");

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

    @PostMapping("/addspecialities")
    public String addSpecialities(@RequestParam String specialities, @RequestParam String desc, @RequestParam MultipartFile file) {
        String ans = "";

        String oname = file.getOriginalFilename();

        System.out.println(specialities);
        System.out.println(desc);
        System.out.println(file);

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM specialtity where specialitiesname ='" + specialities + "' ");

            if (rs.next()) {
                return "fail";
            } else {

                byte b[] = file.getBytes();
                String abspath = "src/main/resources/static/myuploads/";
                FileOutputStream fos = new FileOutputStream(abspath + oname);
                fos.write(b);

                rs.moveToInsertRow();
                //insert workbench coloum name
                rs.updateString("specialitiesname", specialities);
                rs.updateString("desc", desc);
                rs.updateString("photo", oname);

                rs.insertRow();

                return "success";
            }

        } catch (Exception ex) {
            return ex.toString();

        }
    }

    @RequestMapping(value = "/getspecialities", produces = "application/json", method = RequestMethod.GET)
    public String getSpecialities() {
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM specialtity");
        return ans;
    }

    // @RequestMapping(value = "/getspeciality", produces = "application/json", method = RequestMethod.GET)
    // public String getSpeciality() {
    //   String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM specialtity where specialitiesname ");
    //  return ans;
    // }
    @PostMapping("/ownerViewCities")
    public String showCities() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from cities");

        return ans;
    }

    @PostMapping("/ownerViewSpecialities")
    public String showSpecialities() {
        System.out.println("called");
        String ans = new RDBMS_TO_JSON().generateJSON("select * from specialtity");
        System.out.println(ans);
        return ans;
    }

    @PostMapping("/deletespecialities")
    public String deletespecialities(@RequestParam String specialities) {
        try {

            System.out.println(specialities);

            //EXCEPTION ERROR MAKE executeSQLs
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM specialtity where specialitiesname ='" + specialities + "' ");

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

    @PostMapping("/adddoctor")
    public String addDoctor(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String speciality,
            @RequestParam String city,
            @RequestParam String latitude,
            @RequestParam String longitute,
            @RequestParam String starttime,
            @RequestParam String endtime,
            @RequestParam String slotamount,
            @RequestParam String contact,
            @RequestParam String description,
            @RequestPart MultipartFile photo) {
        String ans = "";

        System.out.println("In rest Controller ");

        String oname = photo.getOriginalFilename();

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM doctors where email ='" + email + "' ");

            if (rs.next()) {
                return "fail";
            } else {

                byte b[] = photo.getBytes();
                String abspath = "src/main/resources/static/myuploads/";
                FileOutputStream fos = new FileOutputStream(abspath + oname);
                fos.write(b);
                System.out.println("In Insert Row");
                rs.moveToInsertRow();
                //insert workbench coloum name
                rs.updateString("name", name);
                rs.updateString("email", email);
                rs.updateString("password", password);
                rs.updateString("speciality", speciality);
                rs.updateString("city", city);
                rs.updateString("latitude", latitude);
                rs.updateString("longitute", longitute);

                rs.updateString("photo", "myuploads/" + oname);

                rs.updateString("start_time", starttime);
                rs.updateString("end_time", endtime);
                rs.updateString("slot_amount", slotamount);

                rs.updateString("status", "pending");
                rs.updateString("contact", contact);
                rs.updateString("description", description);

                rs.insertRow();

                System.out.println("Insert Successfull");

                return "success";
            }

        } catch (Exception ex) {
            return ex.toString();

        }
    }

    @RequestMapping(value = "/adminapprovedoctors", produces = "application/json", method = RequestMethod.GET)
    public String adminManageDoctors() {

        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM doctors");

        return ans;
    }

    @GetMapping("/approvedoctoc")
    public String aproveDoctor(@RequestParam String email) {
        String ans = "";

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM doctors where email ='" + email + "' ");

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

    @GetMapping("/blockdoctor")
    public String blockDoctor(@RequestParam String email) {
        String ans = "";

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM doctors where email ='" + email + "' ");

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
