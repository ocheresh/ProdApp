package com.example.prodapp.Model.DataOfNakladna;

import com.example.prodapp.Model.ProductsData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SummaryTotal {

    public static String summaryList(List<ProductsData> list)
    {
        double sum = 0.0;

        if (list == null || list.size() == 0)
            return ("0,00 грн.");
        else
        {
            for (ProductsData data : list) {
                sum += (data.getKilbkistb() * data.getPrice());
            }
        }

        String formattedDouble = new DecimalFormat("#0.00").format(sum);
        return (formattedDouble + " грн.");
    }
}
