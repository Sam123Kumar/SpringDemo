package com.example.demo.Service;

import com.example.demo.Mapper.PersonMapper;
import com.example.demo.Modal.Person;
import com.example.demo.Pojo.PersonListPojo;
import com.example.demo.Pojo.PersonListResponsePojo;
import com.example.demo.Pojo.PersonRequestPojo;
import com.example.demo.Repository.PersonRepository;
import com.example.demo.Repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;
    @Autowired
    private final PhoneRepository phoneRepository;
    @Autowired
    public PersonService(PersonRepository personRepository, PhoneRepository phoneRepository) {
        this.personRepository = personRepository;

        this.phoneRepository = phoneRepository;
    }

    @Transactional
    public String createUser(PersonRequestPojo pojo) {
        Person ue = personRepository.findByEmail(pojo.getEmail());
        if (ue == null) {
            ue = personRepository.save(PersonMapper.mapPojoToEntity(pojo));
        }
        return "user Inserted";
    }

    @Transactional(readOnly = true)
    public PersonListResponsePojo listUsers() {
        List<PersonListPojo> userList = new ArrayList<>();
        for (Person ue : personRepository.getAllUsers()) {
            userList.add(PersonMapper.mapEntityIntoPojoUsersPojo(ue));
        }
        PersonListResponsePojo personListResponsePojo = new PersonListResponsePojo(200, "Success!", userList);
        return personListResponsePojo;
    }


    @Transactional(readOnly = true)
    public PersonListPojo getById(int id) {
        PersonListPojo listPojo = new PersonListPojo();
        Person person = personRepository.findById(id);
        if (person != null) {
            listPojo = PersonMapper.mapEntityIntoPersonPojo(person);
        }
        return listPojo;
    }
    @Transactional(readOnly = true)
    public PersonListPojo getByEmail(String email) {
        PersonListPojo listPojo = new PersonListPojo();
        Person person = personRepository.findByEmail(email);
        if (person != null) {
            listPojo = PersonMapper.mapEntityIntoPersonPojo(person);
        }
        return listPojo;
    }
    @Transactional(readOnly = true)
    public Object getByPhoneNumber(long phoneNumber) {

        Object person = phoneRepository.fethByPhoneNumber(phoneNumber);

        return person;

    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Person person=personRepository.findByEmail(s);
        if(person==null){
            throw new UsernameNotFoundException("404 user not found");
        }
        return new UserPrincipal(person);
    }
}