/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DocConnect.docCONNECT.allControllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @GetMapping("/")
    public String home() {
        return "Home";

    }
    @GetMapping("/adminnav")
    public String adminNavf() {
        return "AdminNavbar";
    }

    @GetMapping("/usersignup")
    public String userSignup() {
        return "UserSignup";
    }

    @GetMapping("/userlogin")
    public String userLogin() {
        return "UserLogin";
    }

    @GetMapping("/usergetDoctorspeciality")
    public String userGetDoctorSpeciality(@RequestParam String cityname, Model model, HttpSession session) {
        String email = (String) session.getAttribute("user_email");
        if (email == null) {
            return "redirect:userlogin";
        }
        model.addAttribute("cityname", cityname);
        return "UserGetSpeciality";
    }

    @GetMapping("/userspecialitydoctor")
    public String userSpecialityDoctor(@RequestParam String speciality, @RequestParam String cityname, HttpSession session,
            Model model) {
        String email = (String) session.getAttribute("user_email");
        if (email == null) {
            return "redirect:userlogin";
        }
        model.addAttribute("speciality", speciality);
        model.addAttribute("cityname", cityname);

        return "UserSpecialityDoctor";
    }

    @GetMapping("/userspecificdoc")
    public String userSpecificDoctor(@RequestParam String email, Model model, HttpSession session) {
        System.out.println(email);
        String uemail = (String) session.getAttribute("user_email");
        if (uemail == null) {
            return "redirect:userlogin";
        }
        model.addAttribute("email", email);

        return "UserSpecificDoctor";
    }

    @GetMapping("/bookslots")
    public String bookSlots(@RequestParam String email, Model model) {
        System.out.println(email);

        model.addAttribute("email", email);

        return "BookSlots";
    }

    @GetMapping("/payment")
    public String userPayment(@RequestParam String email,
            @RequestParam String date,
            @RequestParam String total,
            @RequestParam String slots,
            Model model,
            HttpSession session) {

        String uemail = (String) session.getAttribute("user_email");

        if (uemail == null) {
            return "redirect:userlogin";
        }

        System.out.println(email);

        model.addAttribute("email", email);
        model.addAttribute("date", date);
        model.addAttribute("total", total);
        model.addAttribute("slots", slots);

        return "Payment";
    }

    @GetMapping("/paymentdoneicon")
    public String paymentdoneicon() {

        return "payment_done_icon";
    }

    @GetMapping("/userviewslot")
    public String userViewSlot(HttpSession session) {
        String email = (String) session.getAttribute("user_email");
        if (email == null) {
            return "redirect:userlogin";
        }
        return "UserViewSlots";
    }

    @GetMapping("/userLogout")
    public String userLogout(HttpSession session) {
        session.removeAttribute("user_email");
        return "redirect:/";
    }

    @GetMapping("/usernavbar1")
    public String userNavbar1() {

        return "UserNavbar1";
    }

    @GetMapping("/aboutus")
    public String aboutUs() {

        return "AboutUs";
    }

    @GetMapping("/blog")
    public String Blog() {

        return "Blogs";
    }

    @GetMapping("/contactus")
    public String contactUs() {

        return "ContactUs";
    }
}
