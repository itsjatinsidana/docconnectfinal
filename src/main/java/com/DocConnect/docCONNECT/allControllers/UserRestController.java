/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DocConnect.docCONNECT.allControllers;

import com.DocConnect.docCONNECT.vmm.DBLoader;
import com.DocConnect.docCONNECT.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserRestController {

    @PostMapping("/usersignuppage1")
    public String addDoctor(@RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String contact,
            @RequestParam String address,
            @RequestParam String dob,
            @RequestParam String bloodgroup,
            @RequestParam String selectedGender,
            @RequestPart MultipartFile f1) {
        String ans = "";

        System.out.println("In rest Controller ");

        String oname = f1.getOriginalFilename();

        try {
            //EXCEPTION ERROR MAKE executeSQL
            ResultSet rs = DBLoader.executeSQL("SELECT * FROM user_signup where user_email ='" + email + "' ");

            if (rs.next()) {
                return "fail";
            } else {

                byte b[] = f1.getBytes();
                String abspath = "src/main/resources/static/myuploads/";
                FileOutputStream fos = new FileOutputStream(abspath + oname);
                fos.write(b);

                rs.moveToInsertRow();
                //insert workbench coloum name
                rs.updateString("username", name);
                rs.updateString("user_email", email);
                rs.updateString("user_password", password);
                rs.updateString("user_contact", contact);

                rs.updateString("user_photo", "myuploads/" + oname);
                rs.updateString("user_address", contact);

                rs.updateString("user_gender", selectedGender);
                rs.updateString("user_dob", dob);
                rs.updateString("user_bloodgroup", bloodgroup);

                rs.insertRow();

                System.out.println("Insert Successfull");

                return "success";
            }

        } catch (Exception ex) {
            return ex.toString();

        }
    }

    @PostMapping("/userloginform")
    public String userLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        try {

            //EXCEPTION ERROR MAKE executeSQLs
            ResultSet rs = DBLoader.executeSQL("select * from user_signup where user_email='" + email + "' and user_password='" + password + "'");

            if (rs.next()) {
                session.setAttribute("user_email", email);

                return "success";
            } else {
                return "fail";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }

    }

    @RequestMapping(value = "/getadmincities", produces = "application/json", method = RequestMethod.GET)
    public String getDoctorPhotos(HttpSession session, Model model) {
        String user_email = (String) session.getAttribute("user_email");

        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM cities");

        return ans;
    }

    @RequestMapping(value = "/userGetDoctorSpeciality", produces = "application/json", method = RequestMethod.POST)
    public String userGetDoctorSpeciality(@RequestParam String cityname) {
        System.out.println(cityname);
        cityname = cityname.trim();
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT d.name AS doctor_name, d.email, d.speciality, s.desc AS speciality_desc, s.photo\n"
                + "FROM doctors d\n"
                + "JOIN specialtity s ON d.speciality = s.specialitiesname\n"
                + "WHERE d.city ='" + cityname + "' ");

        return ans;

    }

    @RequestMapping(value = "/userGetDoctorProfile", produces = "application/json", method = RequestMethod.POST)
    public String userGetDoctorProfile(@RequestParam String speciality, @RequestParam String cityname) {
        System.out.println(speciality);
        System.out.println(cityname);
        cityname = cityname.trim();
        speciality = speciality.trim();
        String status = "approve";
        String ans = new RDBMS_TO_JSON().generateJSON("select * from doctors where speciality='" + speciality + "' and city='" + cityname + "' and status='" + status + "' ");
        System.out.println(ans);
        return ans;

    }

    @RequestMapping(value = "/usergetspecificspecialitydoctor", produces = "application/json", method = RequestMethod.POST)
    public String userGetSpecificDoctorProfile(@RequestParam String email) {
        System.out.println(email);

        email = email.trim();
        String status = "approve";

        String ans = new RDBMS_TO_JSON().generateJSON("select * from doctors where email='" + email + "' ");
        System.out.println(ans);

        return ans;

    }

    @GetMapping("/view_slots")
    String view_slots(@RequestParam String email, @RequestParam String date) {

        System.out.println(date);
        System.out.println(email);
        try {
            ResultSet rs = DBLoader.executeSQL("select * from doctors where email='" + email + "'");

            String start;
            String end;
            String slot;
            if (rs.next()) {
                start = rs.getString("start_time");
                end = rs.getString("end_time");
                slot = rs.getString("slot_amount");

            } else {
                String err = "failed";
                return err;
            }
            int Start = Integer.parseInt(start);
            int End = Integer.parseInt(end);
            int Slot = Integer.parseInt(slot);
            JSONObject ans = new JSONObject();

            //Define JSONArray
            JSONArray arr = new JSONArray();
            for (int i = Start; i <= End; i++) {
                JSONObject row = new JSONObject();
                row.put("start_slot", Start);
                row.put("end_slot", ++Start);
                row.put("slot_amount", slot);

                ResultSet rs2 = DBLoader.executeSQL("select * from booking_detail where start_slot ='" + i + "' and booking_id in (select booking_id from booking where date=\'" + date + "\' and vendor_email =\'" + email + "\' ) ");
                if (rs2.next()) {
                    row.put("status", "Booked");
                } else {
                    row.put("status", "Available");
                }

                arr.add(row);
            }
            ans.put("ans", arr);
            System.out.println(ans.toString());
            return (ans.toJSONString());

        } catch (Exception e) {
            return e.toString();
        }

    }

    @GetMapping("/paymentReq")
    String payment(@RequestParam String date, @RequestParam String v_email, @RequestParam String amount, @RequestParam String slots,
            HttpSession session, @RequestParam String type, @RequestParam String status, Model model) {
        String ans = "";
        String useremail = (String) (session.getAttribute("user_email"));
//        System.out.println("user email is" + useremail);

        model.addAttribute("useremail", useremail);

        try {
            ResultSet rs = DBLoader.executeSQL("select * from  booking");

            rs.moveToInsertRow();
            rs.updateString("date", date);
            rs.updateString("vendor_email", v_email);
            rs.updateString("user_email", useremail);
            rs.updateString("total_price", amount);
            rs.updateString("payment_type", type);
            rs.updateString("status", status);
            rs.insertRow();
            int bookingid = 0;
            ResultSet rs2 = DBLoader.executeSQL("select MAX(booking_id) from booking");
            if (rs2.next()) {
                bookingid = rs2.getInt("MAX(booking_id)");
                System.out.println(bookingid);
            }
            StringTokenizer st = new StringTokenizer(slots, ",");
            while (st.hasMoreTokens()) {
                int stslot = Integer.parseInt(st.nextToken());
                int endslot = stslot + 1;
                ResultSet rs3 = DBLoader.executeSQL("select * from booking_detail");
                rs3.moveToInsertRow();
                rs3.updateInt("start_slot", stslot);
                rs3.updateInt("end_slot", endslot);
                rs3.updateInt("booking_id", bookingid);
                rs3.insertRow();
            }

            ans = ans + "success";
            return ans;
        } catch (Exception ex) {
            return ex.toString();
        }

    }

    @RequestMapping(value = "/getuserview", produces = "application/json", method = RequestMethod.GET)
    public String getDoctorManageSlots(HttpSession session) {
        String user_email = (String) session.getAttribute("user_email");
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM booking where user_email ='" + user_email + "'");
        System.out.println("user email isssss" + user_email);
        return ans;
    }

    @RequestMapping(value = "/showaboutdoctors", produces = "application/json", method = RequestMethod.GET)
    public String showAboutDoctor() {
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM doctors");

        return ans;
    }

    @GetMapping("/searchcitydl")

    public String searchCityDl() {
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM cities");
        return ans;
    }

    @GetMapping("/searchdoctordl")
    public String searchDoctorDl() {
        String ans = new RDBMS_TO_JSON().generateJSON("SELECT * FROM doctors");
        return ans;
    }

    @GetMapping("/userShowAverageRatings")
    public String userShowAverageRatings(@RequestParam String doctor_email) {

        // Assuming RDBMS_TO_JSON is available as a service or component
        String ans = new RDBMS_TO_JSON().generateJSON("select avg(rating) as r1 from review_table where doctor_email='" + doctor_email + "' ");
        System.out.println(ans);
        return ans;

    }

    @GetMapping("/userShowRatings")
    public String userShowRatings(@RequestParam String doctor_email) {

        // Assuming RDBMS_TO_JSON is available as a service or component
        String ans = new RDBMS_TO_JSON().generateJSON("select * from review_table where doctor_email='" + doctor_email + "' ");
        System.out.println(ans);
        return ans;

    }

    @GetMapping("/userAddReview")
    public String userAddReview(@RequestParam String doctor_email, @RequestParam int rating, @RequestParam String comment, HttpSession session) {
        String user_email = (String) session.getAttribute("user_email");
        System.out.println(user_email);
//        System.out.println(rating);
        String ans = "";
        try {
            ResultSet rs = DBLoader.executeSQL("Select * from review_table");

            rs.moveToInsertRow();
            rs.updateString("doctor_email", doctor_email);
            rs.updateString("user_email", user_email);
            rs.updateString("comment", comment);
            rs.updateInt("rating", rating);
            rs.insertRow();
            ans = "success";

        } catch (Exception e) {
            ans = e.toString();
        }

        return ans;
    }
}
