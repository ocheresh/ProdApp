package com.example.prodapp.Model.ChooseProduct;

import com.example.prodapp.Model.ProductsData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class HandlerXMLParser extends DefaultHandler {

    // List to hold Employees object
    private List<ProductsData> empList = null;
    private ProductsData emp = null;
    private StringBuilder data = null;

    // getter method for employee list
    public List<ProductsData> getEmpList() {
        return empList;
    }

    boolean bYch = false;
    boolean bName = false;
    boolean bPrice = false;
    boolean bKodP = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase("Product")) {
            // create a new Employee and put it in Map
            String id = attributes.getValue("KodP");
            // initialize Employee object and set id attribute
            emp = new ProductsData();
            emp.setKod(id);
            // initialize list
            if (empList == null)
                empList = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("Naimenov")) {
            bName = true;
        }
        else if (qName.equalsIgnoreCase("EdYch")) {
            bYch = true;
        }
        else if (qName.equalsIgnoreCase("Price")) {
            bPrice = true;
        }
        else if (qName.equalsIgnoreCase("KodP")) {
            bKodP = true;
        }
        // create the data container
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (bYch) {
            // age element, set Employee age
            emp.setEdYch(data.toString());
            bYch = false;
        } else if (bName) {
            emp.setName(data.toString());
            bName = false;
        } else if (bPrice) {
            emp.setPrice(Double.parseDouble(data.toString().replace(',','.')));
            bPrice = false;
        } else if (bKodP) {
            emp.setKod(data.toString());
            bKodP = false;
        }

        if (qName.equalsIgnoreCase("Product")) {
            // add Employee object to list
            empList.add(emp);
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }
}
