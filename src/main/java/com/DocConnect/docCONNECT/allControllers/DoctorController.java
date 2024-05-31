/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DocConnect.docCONNECT.allControllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoctorController {

    @GetMapping("/doctorlogin")
    public String doctorLogin() {

        return "DoctorLogin";
    }

    @GetMapping("/doctordashboard")
    public String doctorDashboard(HttpSession session) {

        String doc_email = (String) session.getAttribute("email");
        if (doc_email == null) {
            return "redirect:doctorlogin";
        }

        return "DoctorDashboard";
    }

    @GetMapping("/footer")
    public String Footer() {

        return "Footer";
    }

    @GetMapping("/managedoctorprofile")
    public String manageDoctorProfile(HttpSession session) {

        String doc_email = (String) session.getAttribute("email");
        if (doc_email == null) {
            return "redirect:doctorlogin";
        }

        return "DoctorManageProfile";
    }

    @GetMapping("/adddoctorphotos")
    public String addDoctorPhotos(HttpSession session) {

        String doc_email = (String) session.getAttribute("email");
        if (doc_email == null) {
            return "redirect:doctorlogin";
        }
        return "AddDoctorPhotos";
    }

    @GetMapping("/doctormanageslots")
    public String doctorManageSlots(HttpSession session) {
        String doc_email = (String) session.getAttribute("email");
        if (doc_email == null) {
            return "redirect:doctorlogin";
        }

        return "DoctorManageSlots";
    }

    @GetMapping("/doctorlogout")
    public String DoctorLogout(HttpSession session) {

        session.removeAttribute("email");
        return "redirect:/";
    }
    
    @GetMapping("/doctornavbar")
    public String DoctorNavbar() {

       
        return "DoctorNavbar";
    }

}
