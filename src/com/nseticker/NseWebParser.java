/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nseticker;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author F-effect
 */
public class NseWebParser {
    
    int company_name = 0;
    int share_price  = 1;
    int change       = 2;
    
    StringBuilder gainers = new StringBuilder();
    StringBuilder loosers = new StringBuilder();
    StringBuilder nochange = new StringBuilder();
    
    NseWebParser() throws IOException {
        

            Document doc = Jsoup.connect("https://www.nse.co.ke/").get();
            
            Elements marquee = doc.select("div.marquee");
            Element span=marquee.first();
            
            int size=span.getElementsByTag("span").size();
            
            Elements el=span.getElementsByTag("span");
            
            for(int i=0;i<size/3;i++){
                
                if(el.get(change).text().charAt(0)=='-'){
                    loosers.append(el.get(company_name).text())
                            .append(" ").append(el.get(share_price).text())
                            .append(" ").append(el.get(change).text()
                            ).append(" ");
                }else if(el.get(change).text().charAt(0)=='+'){
                    gainers.append(el.get(company_name).text()).append(" ")
                            .append(el.get(share_price).text()).append(" ")
                            .append(el.get(change).text())
                            .append(" ");
                }else{
                    nochange.append(el.get(company_name).text()).
                            append(" ").append(el.get(share_price).text()).
                            append(" ").append(el.get(change).text())
                            .append(" ");
                }
                company_name+=3;
                share_price+=3;
                change+=3;
            }
     
    }
    public String getGainers(){
        return gainers.toString();
    }
    public String getLoosers(){
        return loosers.toString();
    }
    public String getNoChange(){
        return nochange.toString();
    }
}
