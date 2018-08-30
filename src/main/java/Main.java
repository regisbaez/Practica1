
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String [] args){
        Document document;
        Connection.Response c;
        String URL;
        Scanner scanner = new Scanner(System.in);
        System.out.println("=======================Practica 1 Cliente HTTP============================");


        while (true){
            try{
                System.out.print("Introduzca un URL:");
                URL = scanner.nextLine();

                document = Jsoup.connect("http://"+URL).get();
                c = Jsoup.connect("http://"+URL).execute();
            }catch (Exception ex){
                System.out.println("Su URL no es correcta intente de nuevo");
                continue;
            }
            System.out.println("su URL ha sido insertado satisfactoriamente");
            break;
        }


        System.out.println("[][][][][][][][][][]Informacion sobre el documento[][][][][][][[][][][][]");


        System.out.println("Cantidad de Líneas: " + c.body().split("\n").length);

        Elements elements = document.getElementsByTag("p");
        System.out.println("Cantidad de párrafos (etiquetas <p>): " + elements.size());


        System.out.println("Cantidad de fotos dentro de parrafos (etiquetas <img> dentro de etiquetas <p>): " + document.select("p img").size());
        elements = document.getElementsByTag("form");
        System.out.println("Cantidad de formularios: (etiquetas <form>): " + elements.size());

        int Get = 0, Post = 0;
        for(FormElement form : document.getElementsByTag("form").forms()){
            if(form.attr("method").equalsIgnoreCase("GET")){
                Get++;
            }
            if(form.attr("method").equalsIgnoreCase("POST")){
                Post++;
            }
        }
        System.out.println("De los cuales, " + Get + " son de tipo GET y, " + Post + " son de tipo POST.");



        //e) Para cada formulario mostrar los campos del tipo input y su
        //respectivo tipo que contiene en el documento HTML.
        int formulario=1;

        for(Element e: elements) {
            System.out.println("==================================================");
            System.out.println("Formulario " + formulario + ": " + elements.attr("name"));
            System.out.println("==================================================");
            int input = 1;
            for(Element element : e.getAllElements()){
                if(element.tagName().equals("input")){
                    System.out.println("Input " + input + ": " + element.attr("name"));
                    System.out.println("===============================================");

                    for (Attribute attribute : element.attributes()){
                        System.out.println(" " + attribute.getKey() + " =  " +  attribute.getValue());
                    }
                    System.out.println("====================================");
                    input++;
                    System.out.println();
                }
            }
            formulario++;
        }

        System.out.println("==================================================");
        int i = 1;
        Document ResultingDoc;
        for(Element form : document.getElementsByTag("form").forms()){
            Elements PostForms = form.getElementsByAttributeValueContaining("method", "post");
            for(Element formz : PostForms){
                try{
                    System.out.println("Formulario " + i + ":");
                    String absURL = formz.absUrl("action");
                    ResultingDoc = Jsoup.connect(absURL).data("asignatura", "practica1").header("matricula", "20140324").post();

                    System.out.println("==================================================");
                    System.out.println("Resultado");
                    System.out.println(ResultingDoc.body().toString());
                    System.out.println("=======================================");
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            i++;
        }


    }
}
