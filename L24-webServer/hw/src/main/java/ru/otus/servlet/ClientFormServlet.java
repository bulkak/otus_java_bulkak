package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ClientFormServlet extends HttpServlet {

    private static final String CLIENTS_FORM_TEMPLATE = "client_form.html";

    private final DBServiceClient serviceClient;
    private final TemplateProcessor templateProcessor;

    public ClientFormServlet(TemplateProcessor templateProcessor, DBServiceClient serviceClient) {
        this.templateProcessor = templateProcessor;
        this.serviceClient = serviceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_FORM_TEMPLATE, paramsMap));
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        var client =  extractClientRequest(req);
        serviceClient.saveClient(client);
        response.sendRedirect("clients");
    }

    private Client extractClientRequest(HttpServletRequest request) {
        var parameters = request.getParameterMap();
        var name = new StringBuilder();
        var phones = new ArrayList<Phone>();
        var address =  new Address("empty");
        for (String key : parameters.keySet()) {
            var paramValues =  parameters.get(key);
            for (int i = 0; i < paramValues.length; i++) {
                switch (key) {
                    case "name":
                        name.append(paramValues[i]);
                        break;
                    case "address":
                        address =  new Address(paramValues[i]);
                        break;
                    case "phone":
                        phones.add(new Phone(paramValues[i]));
                        break;
                }
            }
        }
        return new Client(null, name.toString(), address, phones);
    }
}
