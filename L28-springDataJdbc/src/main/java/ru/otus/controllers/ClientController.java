package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceAddress;
import ru.otus.crm.service.DBServiceClient;

import java.util.HashSet;
import java.util.List;

@Controller
public class ClientController {

    private final String osData;
    private final DBServiceClient clientService;
    private final DBServiceAddress addressService;

    public ClientController(@Value("OS: #{T(System).getProperty(\"os.name\")}, " +
                                    "JDK: #{T(System).getProperty(\"java.runtime.version\")}")
                            String osData,
                            DBServiceClient clientService,
                            DBServiceAddress addressService
    ) {
        this.osData = osData;
        this.clientService = clientService;
        this.addressService = addressService;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("osData", osData);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        var phones  = new HashSet<Phone>();
        model.addAttribute("client", new Client());
        model.addAttribute("address", new Address());
        model.addAttribute("phone", new Phone());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute Client client, @ModelAttribute Address address, @ModelAttribute Phone phone) {
        client.addPhone(phone);
        var clientSet  =new HashSet<Client>();
        clientSet.add(client);
        address.setClients(clientSet);
        addressService.saveAddress(address);
        return new RedirectView("/", true);
    }

}
