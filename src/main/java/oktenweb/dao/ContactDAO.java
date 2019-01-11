package oktenweb.dao;

import oktenweb.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactDAO extends JpaRepository<Contact, Integer>{

    // customized methods

    // the first realization
    @Query("select c from Contact c where c.name=:xxx")
    List<Contact> byName(@Param("xxx") String name);

    // the second realization (the names of methods have to appear after Ctrl+space)
    List<Contact> findAllByName(String name);
}
