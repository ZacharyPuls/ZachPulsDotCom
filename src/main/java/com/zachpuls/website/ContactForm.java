package com.zachpuls.website;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by jenni on 3/16/2016.
 */

@ManagedBean
@ViewScoped
public class ContactForm implements Serializable {

    private String firstName = "";
    private String lastName = "";
    private String message = "";

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void submit() {
        System.out.println("firstName = " + firstName);
        System.out.println("lastName = " + lastName);
        System.out.println("message = " + message);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success",
                "Thank you for your message, " + firstName + "! I will reply back as soon as I can."));

        setFirstName("");
        setLastName("");
        setMessage("");
    }
}
