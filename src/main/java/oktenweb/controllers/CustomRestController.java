package oktenweb.controllers;

import oktenweb.dao.ContactDAO;
import oktenweb.models.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
