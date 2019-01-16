package oktenweb.controllers;

import oktenweb.dao.ContactDAO;
import oktenweb.models.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class CustomRestController {
    @Autowired
    private ContactDAO contactDAO;

    @PostMapping("/saveAJAX")
    public
    //@ResponseBody
    List<Contact> saveAJAX(@RequestBody Contact contact)
    {
        contactDAO.save(contact);
        return contactDAO.findAll();
    }

    @PostMapping("/upload")
    public void upload(
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String phone,
            @RequestParam MultipartFile image
            ){
    Contact contact = new Contact(name, surname, phone);

        String path = System.getProperty("user.home")
                +File.separator
                +"images"
                +File.separator
                +image.getOriginalFilename();
        try {
            image.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        contact.setAvatar(image.getOriginalFilename());

        //System.out.println("saveWholeObject Method");
        System.out.println("contact saved whole: "+contact);

        contactDAO.save(contact);
    }
}
