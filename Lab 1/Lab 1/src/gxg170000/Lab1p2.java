package gxg170000;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.VCARD;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Lab1p2 {
    public static void main (String args[]) {

        Logger.getRootLogger().
                setLevel(Level.OFF);

        String personURI    = "http://utdallas/semclass";
        String givenName    = "May";
        String familyName   = "Westford";
        String fullName     = "DR. "+givenName + " " + familyName;
        String title        = "North America Division Vice President";
        String dob          = "March 28, 1965";
        String email        = "May.Westford@consolidatedsem.com";
        String organization = "Consolidated Semantics, U.S.";
        String id           = "874659";

        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        Resource may = model.createResource(personURI+"#"+id)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.TITLE, title)
                .addProperty(VCARD.BDAY, dob)
                .addProperty(VCARD.EMAIL, email)
                .addProperty(VCARD.ORG, organization);

        PrintWriter printWriter = null;
        try{
            printWriter = new PrintWriter("Lab1p2_gxg170000.xml");
            model.write(printWriter, "RDF/XML");

            printWriter = new PrintWriter("Lab1p2_gxg170000.ntp");
            model.write(printWriter, "N-TRIPLE");

            printWriter = new PrintWriter("Lab1p2_gxg170000.n3");
            model.write(printWriter, "N3");
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
