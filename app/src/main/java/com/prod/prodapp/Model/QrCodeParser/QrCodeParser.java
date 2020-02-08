package com.prod.prodapp.Model.QrCodeParser;

import com.prod.prodapp.Model.ChooseProduct.HandlerXMLParser;
import com.prod.prodapp.Model.InfoOfNakladna.InfoOfNakladna;
import com.prod.prodapp.Model.ProductsData;
import com.prod.prodapp.View.ChoiseMenu.ChoiseMenuView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class QrCodeParser {

    String parser = "286_2_18_178_2/29.10.2019/ВійськСервіс/145/15.11.2019/GAZ/AV2585KL/Шумахер Антон Борисович/1014/20/1018/20/1023/20/1028/20/1030/20/1033/20/1034/20";

    String data = "";

    public InfoOfNakladna getInfoOfNakladna() {
        return infoOfNakladna;
    }

    static public InfoOfNakladna infoOfNakladna = null;
    static public List<ProductsData> productsDatalist = null;
    static public List<ProductsData> templist = new ArrayList<ProductsData>();
    String [] separate = null;

    public QrCodeParser(String data)
    {
        this.data = data;
        infoOfNakladna = new InfoOfNakladna();
        productsDatalist = new ArrayList<ProductsData>();
        separate = this.data.split("/");
        initInfoOfNakladna();
        read_data_xml();
        initListProduct();
    }

    public List<ProductsData> getProductsDatalist() {
        return productsDatalist;
    }

    public List<ProductsData> getTemplist() {
        return templist;
    }

    public void initInfoOfNakladna()
    {
        if (infoOfNakladna != null && separate.length > 7) {
            infoOfNakladna.setNameDogovir(separate[0]);
            infoOfNakladna.setNumberNakladna(separate[3]);
            infoOfNakladna.setDateNakladna(separate[4]);
            infoOfNakladna.setMarkaAvto(separate[5]);
            infoOfNakladna.setNomerAvto(separate[6]);
            infoOfNakladna.setNameDriver(separate[7]);
        }
    }

    public void initListProduct()
    {

        for (int i = 7; i < separate.length; i++)
        {
            for (ProductsData productsData : templist) {
                if (separate[i].length() == 4 && productsData.getKod().indexOf(separate[i]) !=-1? true: false)
                {
                    ProductsData temp = new ProductsData(productsData);
                    temp.setKilbkistb(Double.parseDouble(separate[++i]));
                    productsDatalist.add(temp);
                }
            }
        }

    }

    private void read_data_xml()
    {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            String name = infoOfNakladna.getNameDogovir() + ".xml";
            InputStream is = ChoiseMenuView.class.getClassLoader().getResourceAsStream("assets/" + name);
            SAXParser saxParser = saxParserFactory.newSAXParser();
            HandlerXMLParser handler = new HandlerXMLParser();
            saxParser.parse(is, handler);
            //Get Employees list
            templist = handler.getEmpList();
            //print employee information
//            for(ProductsData emp : templist)
//                System.out.println(emp);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }




}
