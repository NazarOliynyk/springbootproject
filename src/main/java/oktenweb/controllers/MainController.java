package oktenweb.controllers;

import oktenweb.dao.ContactDAO;
import oktenweb.models.Contact;
import oktenweb.models.User;
import oktenweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ContactDAO contactDAO;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("message", "hello from MainController");
        System.out.println("homeMethod");
        return "index";
    }

    // this method is obsolete. use the method saveWholeObject below
    @GetMapping("/save")
    public String save(
            // in case if the name of parameters coinside with the names of fields it is
            // possible to not write them after: @RequestParam int id,
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("phone") String phone
    ){
        //Contact contact = new Contact(id, name, surname, phone);
        System.out.println("save Method");
        //System.out.println("contact: "+contact);
        return "redirect:/";
    }



    @PostMapping("/saveWholeObject")
    public String saveWholeObject(
            Contact contact,
            //@RequestParam("image")MultipartFile multipartFile
            @RequestParam MultipartFile image
            ){
        String path = System.getProperty("user.home")+File.separator+"images"+File.separator+image.getOriginalFilename();
        try {
            image.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        contact.setAvatar(image.getOriginalFilename());

        //System.out.println("saveWholeObject Method");
        //System.out.println("contact saved whole: "+contact);

        contactDAO.save(contact);
        return "redirect:/";
    }



    @GetMapping("/showAllContacts")
    public String showAllContacts(Model model){

        List<Contact> contacts = contactDAO.findAll();
        model.addAttribute("contacts", contacts);
        return "contactList";
    }
    
    @GetMapping("/contactDetail-{xxx}")
    public String resolveSingleContact(@PathVariable("xxx") int id,Model model)
    {
        Contact contact = contactDAO.getOne(id);
        model.addAttribute("contact", contact);
        return "singleContact";
    }

    @PostMapping("/updateContact")
    public String updateContact(Contact contact){
        contactDAO.save(contact);
        return "redirect:/showAllContacts";
    }

    // the following method is simplified in CustomRestController

//    @PostMapping("/saveAJAX")
//    public @ResponseBody
//    List<Contact> saveAJAX(@RequestBody Contact contact)
//    {
//        return contactDAO.findAll();
//    }

    @PostMapping("/successURL")
    public String successURL(){
        return "redirect:/showAllContacts";
    }

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/saveUser")
    public String saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword())); // to encode the pass
        userService.save(user);
        return "redirect:/login";
    }
    
}
